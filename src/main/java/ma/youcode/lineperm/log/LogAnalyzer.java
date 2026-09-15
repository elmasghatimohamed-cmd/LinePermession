package ma.youcode.lineperm.log;

import java.util.*;
import java.util.stream.Collectors;
import ma.youcode.lineperm.model.AccessLog;

public class LogAnalyzer {
    private final List<AccessLog> logs;

    public LogAnalyzer(List<AccessLog> logs) {
        this.logs = logs;
    }

    public long nbrTotaleAction(){
        return logs.stream().count();
    }

    public long nombreAccesRefuses() {
        return logs.stream()
                .filter(log -> "REFUSE".equalsIgnoreCase(log.getResultat()))
                .count();
    }

    public List<String> utilisateursDistinct(){
        return logs.stream()
            .map(AccessLog::getUtilisateur)
            .distinct()
            .collect(Collectors.toList());
    }


    public Map<String, Long> actionParUtilisateur(){
        return logs.stream().
            collect(Collectors.groupingBy
                (AccessLog::getUtilisateur, 
                Collectors.counting()
            ));
    }

    public List<Map.Entry<String,Long>> topFichierConsultes(){

        return logs.stream()
            .collect(Collectors.groupingBy(
                AccessLog::getFichier, 
                Collectors.counting())).
                entrySet().
                stream().
                sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public long accesRefusesUtilisateur(String nomUtilisateur){

        return logs.stream()
            .filter(log -> log.getUtilisateur().equalsIgnoreCase(nomUtilisateur))
            .filter(log -> "REFUSE".equalsIgnoreCase(log.getResultat()))
            .count();
    }

    public Optional<Map.Entry<String, Long>> utilisateurPlusActif(){

        return logs.stream()
            .collect(
                Collectors.groupingBy(
                    AccessLog::getUtilisateur, Collectors.counting()
                ))
            .entrySet()
            .stream()
            .max(Map.Entry.comparingByValue());
    }
}
