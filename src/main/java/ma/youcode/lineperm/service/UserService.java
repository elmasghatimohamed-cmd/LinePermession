package ma.youcode.lineperm.service;

import ma.youcode.lineperm.dao.UserDao;
import ma.youcode.lineperm.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean existe(String login) {
        return userDao.findByLogin(login) != null;
    }

    public boolean creerCompte(String login, String motDePasse) {
        if (existe(login)) {
            return false;
        }

        String hash = BCrypt.hashpw(motDePasse, BCrypt.gensalt());
        User user = new User(login, hash);

        userDao.save(user);
        return true;
    }

    public User connecter(String login, String motDePasse) {
        User user = userDao.findByLogin(login);

        if (user == null) {
            return null;
        }

        if (BCrypt.checkpw(motDePasse, user.getPasswordHash())) {
            return user;
        }

        return null;
    }
}