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
            switch (droit) {
                case 'r':
                    return fichier.isPropR();
                case 'w':
                    return fichier.isPropW();
                case 'd':
                    return fichier.isPropD();
                default:
                    return false;
            }
        } else {
            switch (droit) {
                case 'r':
                    return fichier.isAutR();
                case 'w':
                    return fichier.isAutW();
                case 'd':
                    return fichier.isAutD();
                default:
                    return false;
            }
        }
    }
}