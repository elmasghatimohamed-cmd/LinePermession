package ma.youcode.lineperm;

// import ma.youcode.lineperm.ui.ConsoleApp;
import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.FileService;
import ma.youcode.lineperm.model.FichierProtege;

public class Main {
    public static void main(String[] args) {
        // ConsoleApp app = new ConsoleApp();
        // app.demarrer();

        FileService fileService = new FileService();

        User user = new User("Mohamed", "123456");

        System.out.println("\n========== AVANT CREATION ==========");
        for (FichierProtege fichier : fileService.listerTous()) {
            System.out.println(fichier.getNom());
        }

        System.out.println("\n========== CREATION ==========");

        boolean resultat = fileService.creerFichier(user, "instant.txt");

        System.out.println(
                resultat
                        ? "Nouveau fichier cree avec succes."
                        : "Echec de creation.");

        System.out.println("\n========== APRES CREATION ==========");

        for (FichierProtege fichier : fileService.listerTous()) {
            System.out.println(
                    fichier.getNom()
                            + " | Proprietaire: "
                            + fichier.getProprietaire()
                            + " | Droits: "
                            + fichier.getBlocProprietaire()
                            + "|" + fichier.getBlocAutres());
        }

    }

}