package ma.youcode.lineperm.log;

import ma.youcode.lineperm.model.AccessLog;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class LogService {
    private final Path logPath = Paths.get("resources/access.log");

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");


    public List<AccessLog> chargerLogs(){

        if(!Files.exists(logPath)){
            return Collections.emptyList();
        }

        try{
            System.out.println("Chemin : " + logPath.toAbsolutePath());
            System.out.println("Existe : " + Files.exists(logPath));
            return Files.readAllLines(logPath)
                .stream()
                .filter(line -> !line.trim().isEmpty())
                .map(AccessLog::depuisLog)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        } catch(IOException e){
            System.err.println("Erreur lors de chargement des logs: "+ e.getMessage());
            return Collections.emptyList();
        }
    }

    public void enregistrer(String utilisateur, String action, String fichier, String resultat) {
        LocalDateTime now = LocalDateTime.now();
        
        String date = now.format(DATE_FORMATTER);
        String heure = now.format(TIME_FORMATTER);

        String ligneLog = String.format("%s;%s;%s;%s;%s;%s%n", date, heure, utilisateur, action, fichier, resultat);

        try {
            if (logPath.getParent() != null && !Files.exists(logPath.getParent())) {
                Files.createDirectories(logPath.getParent());
            }
            Files.writeString(logPath, ligneLog, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'enregistrement du log: " + e.getMessage());
        }
    }
}
