package ma.youcode.lineperm.access;

import ma.youcode.lineperm.model.FichierProtege;
import ma.youcode.lineperm.model.User;

public class ControleAcces {

    public static boolean estAutorise(User user, FichierProtege fichier, char droit) {
        if (user == null || fichier == null) {
            return false;
        }

        boolean isOwner = user.getLogin().equals(fichier.getProprietaire());

        if (isOwner) {
            return switch (droit) {
                case 'r' -> fichier.isPropR();
                case 'w' -> fichier.isPropW();
                case 'd' -> fichier.isPropD();
                default -> false;
            };
        } else {
            return switch (droit) {
                case 'r' -> fichier.isAutR();
                case 'w' -> fichier.isAutW();
                case 'd' -> fichier.isAutD();
                default -> false;
            };
        }
    }
}