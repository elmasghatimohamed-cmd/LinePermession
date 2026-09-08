package ma.youcode.lineperm.service;

import ma.youcode.lineperm.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private final Map<String, User> comptes = new HashMap<>();
    private final Path filePath = Paths.get("resources/users.txt");

    public void charger() {
        comptes.clear();
        if (!Files.exists(filePath)) {
            return;
        }
        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                if (line.trim().isEmpty())
                    continue;
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    User user = new User(parts[0], parts[1]);
                    comptes.put(parts[0], user);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des utilisateurs: " + e.getMessage());
        }
    }

    public boolean existe(String login) {
        return comptes.containsKey(login);
    }

    public boolean creerCompte(String login, String motDePasse) {
        if (existe(login)) {
            return false;
        }
        String salt = BCrypt.gensalt();
        String hash = BCrypt.hashpw(motDePasse, salt);
        User user = new User(login, hash);
        comptes.put(login, user);
        sauvegarder();
        return true;
    }

    public User connecter(String login, String motDePasse) {
        User user = comptes.get(login);
        if (user == null) {
            return null;
        }
        if (BCrypt.checkpw(motDePasse, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    public void sauvegarder() {
        try {
            if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            List<String> lines = new ArrayList<>();
            for (User user : comptes.values()) {
                lines.add(user.getLogin() + ":" + user.getPasswordHash());
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.err.println("Erreur de sauvegarde: " + e.getMessage());
        }
    }
}