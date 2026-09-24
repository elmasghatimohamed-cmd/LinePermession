package ma.youcode.lineperm.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ma.youcode.lineperm.dao.LogDao;
import ma.youcode.lineperm.model.AccessLog;

public class LogService {

    private final LogDao logDao;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LogService(LogDao logDao) {
        this.logDao = logDao;
    }

    public void enregistrer(
            String utilisateur,
            String action,
            String fichier,
            String resultat) {

        LocalDateTime now = LocalDateTime.now();

        String date = now.format(DATE_FORMATTER);
        String heure = now.format(TIME_FORMATTER);

        AccessLog log = new AccessLog(
                date,
                heure,
                utilisateur,
                action,
                fichier,
                resultat);

        logDao.save(log);
    }

    public List<AccessLog> chargerLogs() {
        return logDao.findAll();
    }
}