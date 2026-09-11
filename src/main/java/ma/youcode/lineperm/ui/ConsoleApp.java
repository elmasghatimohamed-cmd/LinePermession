package ma.youcode.lineperm.ui;

import java.util.Scanner;

import ma.youcode.lineperm.access.ControleAcces;
import ma.youcode.lineperm.model.FichierProtege;
import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.FileService;
import ma.youcode.lineperm.service.UserService;

public class ConsoleApp {

    private UserService userService;
    private Scanner scanner;
    private User utilisateurConnecte;
    private FileService fileService;
    private boolean actif;

    public ConsoleApp() {
        this.userService = new UserService();
        this.fileService = new FileService();
        this.scanner = new Scanner(System.in);
        this.utilisateurConnecte = null;
        this.actif = true;
    }

    public void demarrer() {
        userService.charger();
        fileService.charger();

        System.out.println("LinPerm - gestion de fichiers & droits");
        System.out.println("Non connecte. Commandes signup, login, exit");

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

        userService.charger();
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
            case "logout":
                logout();
                break;
            case "exit":
                exit();
                break;
            case "ls":
            case "ls -l":
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
            System.out.println("Erreur");
            return;
        }
        String nom = mots[1];
        if (fileService.creerFichier(utilisateurConnecte, nom)) {
            System.out.println("Fichier '" + nom + "' cree.");
        } else {
            System.out.println("Nom de fichier invalide.");
        }
    }

    private void cat(String[] mots) {
        if (mots.length < 2) {
            System.out.println("Erreur");
            return;
        }
        String nom = mots[1];
        String contenu = fileService.lireContenu(utilisateurConnecte, nom);
        if (contenu == null) {
            System.out.println("Permission denied");
        } else if (contenu.isEmpty()) {
            System.out.println("fichier vide");
        } else {
            System.out.print(contenu);
            if (!contenu.endsWith("\n"))
                System.out.println();
        }
    }

    private void nano(String[] mots) {
        if (mots.length < 2) {
            System.out.println("Erreur");
            return;
        }
        String nom = mots[1];
        FichierProtege f = fileService.getFichier(nom);
        if (f == null) {
            System.out.println("Fichier inexistant");
            return;
        }

        if (!ControleAcces.estAutorise(utilisateurConnecte, f, 'w')) {
            System.out.println("Permission denied");
            return;
        }

        StringBuilder sb = new StringBuilder();
        String contenuActuel = "";

        if (ControleAcces.estAutorise(utilisateurConnecte, f, 'r')) {
            contenuActuel = fileService.lireContenu(utilisateurConnecte, nom);
            if (contenuActuel != null && !contenuActuel.isEmpty()) {
                System.out.print(contenuActuel);
                if (!contenuActuel.endsWith("\n"))
                    System.out.println();
            
                sb.append(contenuActuel);
                if (!contenuActuel.endsWith("\n")) {
                    sb.append("\n");
                }
            } else {
                System.out.println("(fichier vide)");
            }
    }

    System.out.println("Saisis ton texte... Tape EOF pour enregistrer.");

    while (true) {
        String line = scanner.nextLine();
        if (line.equals("EOF")) {
            break;
        }
        sb.append(line).append("\n");
    }

    if (fileService.ecrireContenu(utilisateurConnecte, nom, sb.toString())) {
        System.out.println("Fichier '" + nom + "' enregistre");
    }
}

    private void chmod(String[] mots) {
        if (mots.length < 3) {
            System.out.println("Erreur");
            return;
        }
        String argDroit = mots[1];
        String nom = mots[2];

        FichierProtege f = fileService.getFichier(nom);
        if (f == null) {
            System.out.println("Fichier inexistant.");
            return;
        }

        if (!utilisateurConnecte.getLogin().equals(f.getProprietaire())) {
            System.out.println("Permission denied.");
            return;
        }

        String avant = f.getDroitsFormates();
        if (fileService.modifierDroits(utilisateurConnecte, nom, argDroit)) {
            String apres = f.getDroitsFormates();
            System.out.println(nom + ": " + avant + "\n->\n" + apres);
        }
    }

    private void rm(String[] mots) {
        if (mots.length < 2) {
            System.out.println("Usage: rm <fichier>");
            return;
        }
        String nom = mots[1];
        FichierProtege f = fileService.getFichier(nom);

        if (f == null) {
            System.out.println("Fichier inexistant.");
            return;
        }

        if (fileService.supprimerFichier(utilisateurConnecte, nom)) {
            System.out.println("Fichier '" + nom + "' supprime.");
        } else {
            System.out.println("Permission denied.");
        }
}
}