package ma.youcode.lineperm.log;

import ma.youcode.lineperm.model.AccessLog;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class LogService {
    private final Path logPath = Paths.get("resources/access.log");


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
}
