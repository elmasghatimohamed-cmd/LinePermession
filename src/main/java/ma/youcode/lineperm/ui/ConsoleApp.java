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

}