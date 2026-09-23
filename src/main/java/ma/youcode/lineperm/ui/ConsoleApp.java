package ma.youcode.lineperm.ui;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import ma.youcode.lineperm.dao.FichierDao;
import ma.youcode.lineperm.dao.UserDao;
import ma.youcode.lineperm.log.LogAnalyzer;
import ma.youcode.lineperm.log.LogService;
import ma.youcode.lineperm.model.FichierProtege;
import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.FileService;
import ma.youcode.lineperm.service.UserService;

public class ConsoleApp {

    private final UserService userService;
    private final FileService fileService;
    private final LogService logService;
    private final Scanner scanner;
    private User utilisateurConnecte;
    private boolean actif;

    public ConsoleApp() throws SQLException {
        UserDao userDao = new UserDao();
        FichierDao fichierDao = new FichierDao();
        this.logService = new LogService();
        this.userService = new UserService(userDao);
        this.fileService = new FileService(fichierDao, logService);
        this.scanner = new Scanner(System.in);
        this.utilisateurConnecte = null;
        this.actif = true;
    }

    public void demarrer() {
        System.out.println("LinPerm - gestion de fichiers & droits");
        System.out.println("Non connecte. Commandes signup, login, stats, exit");

        while (actif) {
            String ligne = lireLigne(prompt());
            traiter(ligne);
        }
    }

    private String lireLigne(String message) {
        System.out.print(message);
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return "";
    }

    public void login() {

        String login = lireLigne("Login ").trim();
        String mdp = lireLigne("Mot de passe: ");

        User user = userService.connecter(login, mdp);
        if (user == null) {
            System.out.println("Identifiants incorrects.");
        } else {
            utilisateurConnecte = user;
            System.out.println("Bienvenue " + user.getLogin() + " !");
        }
    }

    public void signup() {
        String login = lireLigne("Login ").trim();
        if (login.isEmpty() || login.contains(" ") || login.contains(":")) {
            System.out.println("Login invalide");
            return;
        }

        if (userService.existe(login)) {
            System.out.println("Ce utilisateur existe deja");
            return;
        }

        String mdp = lireLigne("Mot de passe: ");
        if (mdp.trim().isEmpty()) {
            System.out.println("Le mot de passe ne peut pas etre vide");
            return;
        }

        if (userService.creerCompte(login, mdp)) {
            System.out.println("Compte cree avec succes");
        }
    }

    private String prompt() {
        if (utilisateurConnecte != null) {
            return utilisateurConnecte.getLogin() + "@linperm> ";
        }
        return "linperm> ";
    }

    public void traiter(String ligne) {
        String nettoyee = ligne.trim();
        if (nettoyee.isEmpty()) {
            return;
        }

        String[] mots = nettoyee.split("\\s+");
        String commande = mots[0].toLowerCase();

        if (utilisateurConnecte == null && requisConnexion(commande)) {
            System.out.println("Vous devez etre connecte.");
            return;
        }

        if (utilisateurConnecte != null && (commande.equals("signup") || commande.equals("login"))) {
            System.out.println("Vous etes deja connecte");
            return;
        }

        switch (commande) {
            case "signup":
                signup();
                break;
            case "login":
                login();
                break;
            case "stats":
                ouvrirMenuStats();
                break;
            case "logout":
                logout();
                break;
            case "exit":
                exit();
                break;
            case "ls":
                listerFichiers();
                break;
            case "touch":
                touch(mots);
                break;
            case "cat":
                cat(mots);
                break;
            case "nano":
                nano(mots);
                break;
            case "chmod":
                chmod(mots);
                break;

            case "rm":
                rm(mots);
                break;
            default:
                System.out.println("Commande inconnue. Tape 'help'.");
                break;
        }
    }

    private void logout() {
        utilisateurConnecte = null;
        System.out.println("Deconnecte");
    }

    private void exit() {
        System.out.println("Au revoir");
        actif = false;
    }

    private boolean requisConnexion(String commande) {
        return commande.equals("logout") || commande.equals("ls")
                || commande.equals("touch") || commande.equals("cat")
                || commande.equals("nano") || commande.equals("chmod")
                || commande.equals("rm");
    }

    private void listerFichiers() {
        for (FichierProtege f : fileService.listerTous()) {
            System.out.println(f.getDroitsFormates() + " " + f.getProprietaire() + " " + f.getNom());
        }
    }

    private void touch(String[] mots) {
        if (mots.length < 2) {
            System.out.println("Usage: touch <fichier>");
            return;
        }
        String nom = mots[1];
        if (fileService.creerFichier(utilisateurConnecte, nom)) {
            System.out.println("Fichier '" + nom + "' cree.");
        } else {
            System.out.println("Nom de fichier invalide ou fichier deja existant.");
        }
    }

    private void cat(String[] mots) {
        if (mots.length < 2) {
            System.out.println("Usage: cat <fichier>");
            return;
        }
        String nom = mots[1];

        String contenu = fileService.lireContenu(
                utilisateurConnecte,
                nom);

        if (contenu == null) {
            System.out.println("Permission denied ou fichier inexistant.");
        } else if (contenu.isEmpty()) {
            System.out.println("fichier vide");
        } else {
            System.out.print(contenu);
            if (!contenu.endsWith("\n")) {
                System.out.println();
            }
        }
    }

    private void nano(String[] mots) {
        if (mots.length < 2) {
            System.out.println("Usage: nano <fichier>");
            return;
        }
        String nom = mots[1];

        FichierProtege fichier = fileService.getFichier(nom);

        if (fichier == null) {
            System.out.println("Fichier inexistant");
            return;
        }

        String contenuActuel = fileService.lireContenu(utilisateurConnecte, nom);

        if (contenuActuel == null) {
            System.out.println("Permission denied");
            return;
        }

        StringBuilder contenu = new StringBuilder();

        if (!contenuActuel.isEmpty()) {

            System.out.print(contenuActuel);

            if (!contenuActuel.endsWith("\n")) {
                System.out.println();
                contenu.append(contenuActuel).append("\n");
            } else {
                contenu.append(contenuActuel);
            }
        }

        System.out.println("Saisis ton texte... Tape EOF pour enregistrer.");

        while (true) {

            String ligne = scanner.nextLine();

            if (ligne.equals("EOF")) {
                break;
            }

            contenu.append(ligne).append("\n");
        }

        if (fileService.ecrireContenu(
                utilisateurConnecte,
                nom,
                contenu.toString())) {

            System.out.println("Fichier '" + nom + "' enregistre");
        } else {
            System.out.println("Permission denied");
        }
    }

    private void chmod(String[] mots) {
        if (mots.length < 3) {
            System.out.println("Usage: chmod <droits> <fichier>");
            return;
        }
        String argDroit = mots[1];
        String nom = mots[2];

        FichierProtege fichier = fileService.getFichier(nom);

        if (fichier == null) {
            System.out.println("Fichier inexistant.");
            return;
        }

        String avant = fichier.getDroitsFormates();
        if (fileService.modifierDroits(utilisateurConnecte, nom, argDroit)) {
            String apres = fichier.getDroitsFormates();
            System.out.println(nom + ": " + avant + "\n->\n" + apres);

        } else {
            System.out.println("Permission denied.");
        }
    }

    private void rm(String[] mots) {
        if (mots.length < 2) {
            System.out.println("Usage: rm <fichier>");
            return;
        }
        String nom = mots[1];

        if (fileService.getFichier(nom) == null) {
            System.out.println("Fichier inexistant.");
            return;
        }

        if (fileService.supprimerFichier(utilisateurConnecte, nom)) {
            System.out.println("Fichier '" + nom + "' supprime.");
        } else {
            System.out.println("Permission denied.");
        }
    }

    private void ouvrirMenuStats() {
        LogAnalyzer analyzer = new LogAnalyzer(logService.chargerLogs());
        System.out.println("Bienvenue dans LogAnalyzer. Choisissez une statistique par son numero.");

        boolean dansMenu = true;
        while (dansMenu) {
            System.out.println("\n=== LogAnalyzer ===");
            System.out.println("1) Nombre total d'actions");
            System.out.println("2) Nombre d'acces refuses");
            System.out.println("3) Utilisateurs distincts");
            System.out.println("4) Actions par utilisateur");
            System.out.println("5) Top 3 des fichiers consultes");
            System.out.println("6) Acces refuses d'un utilisateur");
            System.out.println("7) Utilisateur le plus actif");
            System.out.println("8) Repartition des actions par type");
            System.out.println("0) Quitter");

            String choix = lireLigne("Choix : ").trim();
            switch (choix) {
                case "1":
                    System.out.println("Nombre total d'actions : " + analyzer.nbrTotaleAction());
                    break;
                case "2":
                    System.out.println("Acces refuses : " + analyzer.nombreAccesRefuses());
                    break;
                case "3":
                    System.out.println("Utilisateurs distincts : " + analyzer.utilisateursDistinct());
                    break;
                case "4":
                    System.out.println("Actions par utilisateur : " + analyzer.actionParUtilisateur());
                    break;
                case "5":
                    System.out.println("Top 3 des fichiers consultes : " + analyzer.topFichierConsultes());
                    break;
                case "6":
                    String userTarget = lireLigne("Nom de l'utilisateur : ").trim();
                    System.out.println(
                            "Acces refuses pour " + userTarget + " : " + analyzer.accesRefusesUtilisateur(userTarget));
                    break;
                case "7":
                    Optional<Map.Entry<String, Long>> plusActif = analyzer.utilisateurPlusActif();
                    if (plusActif.isPresent()) {
                        System.out.println("Utilisateur le plus actif : " + plusActif.get().getKey() + " ("
                                + plusActif.get().getValue() + " actions)");
                    } else {
                        System.out.println("Aucun log disponible.");
                    }
                    break;
                case "8":
                    System.out.println("Repartition des actions par type : " + analyzer.repartitionParAction());
                    break;
                case "0":
                    dansMenu = false;
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez ressayer...");
                    break;
            }
        }
    }
}