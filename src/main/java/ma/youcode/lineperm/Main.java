package ma.youcode.lineperm;

import java.util.Arrays;
import java.util.List;

import ma.youcode.lineperm.log.LogAnalyzer;
import ma.youcode.lineperm.model.AccessLog;
// import ma.youcode.lineperm.ui.ConsoleApp;

public class Main {
    public static void main(String[] args) {
        // ConsoleApp app = new ConsoleApp();
        // app.demarrer();


        List<AccessLog> logs = Arrays.asList(
                new AccessLog("2026-09-15", "10:00", "Mohamed", "LECTURE", "test.txt", "AUTORISE"),
                new AccessLog("2026-09-15", "10:05", "Mohamed", "LECTURE", "test.txt", "REFUSE"),
                new AccessLog("2026-09-15", "10:10", "Ali", "Ecriture", "test.txt", "AUTORISE"),
                new AccessLog("2026-09-15", "10:15", "Ali", "LECTURE", "data.txt", "AUTORISE"),
                new AccessLog("2026-09-15", "10:20", "Sara", "Suppression", "data.txt", "REFUSE"),
                new AccessLog("2026-09-15", "10:25", "Mohamed", "LECTURE", "data.txt", "AUTORISE"),
                new AccessLog("2026-09-15", "10:30", "Ali", "LECTURE", "test.txt", "AUTORISE")
        );

        LogAnalyzer analyzer = new LogAnalyzer(logs);

        System.out.println("Nombre total d'actions : "+ analyzer.nbrTotaleAction());

        System.out.println("Nombre d'acces refuses : "+ analyzer.nombreAccesRefuses());

        System.out.println("Utilisateurs distincts : "+ analyzer.utilisateursDistinct());

        System.out.println("Actions par utilisateur : "+ analyzer.actionParUtilisateur());

        System.out.println("Top 3 fichiers consultes : "+ analyzer.topFichierConsultes());
        System.out.println("Acces refuses pour Mohamed : "+ analyzer.accesRefusesUtilisateur("Mohamed"));

        System.out.println("Acces refuses pour Ali : "+ analyzer.accesRefusesUtilisateur("Ali"));

        System.out.println("Acces refuses pour Sara : "+ analyzer.accesRefusesUtilisateur("Sara"));
        System.out.println("Utilisateur le plus actif : "+ analyzer.utilisateurPlusActif());
        System.out.println("Repartition par actions : "+ analyzer.repartitionParAction());
    }

}