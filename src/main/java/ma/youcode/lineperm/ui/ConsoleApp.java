package ma.youcode.lineperm.ui;

import java.util.Scanner;
import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.UserService;

public class ConsoleApp {

    private UserService userService;
    private Scanner scanner;
    private User utilisateurConnecte;
    private boolean actif;

    public ConsoleApp() {
        this.userService = new UserService();
        this.scanner = new Scanner(System.in);
        this.utilisateurConnecte = null;
        this.actif = true;
    }

    public void demarrer() {
        userService.charger();

        System.out.println("LinPermission - gestion de fichiers");
        System.out.println("Non connecte. Commandes signup, login, exit");
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
}