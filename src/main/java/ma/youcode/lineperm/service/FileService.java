package ma.youcode.lineperm.service;

import ma.youcode.lineperm.model.FichierProtege;
import ma.youcode.lineperm.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileService {
    private final Map<String, FichierProtege> fichiers = new LinkedHashMap<>();
    private final Path metaPath = Paths.get("resources/files.txt");
    private final Path dataDir = Paths.get("resources/data");

    public FileService() {
        charger();
    }

    public boolean creerFichier(User user, String nom) {
        if (nom.contains("/") || nom.contains("\\") || fichiers.containsKey(nom)) {
            return false;
        }
        FichierProtege nouveau = new FichierProtege(nom, user.getLogin());
        fichiers.put(nom, nouveau);
        sauvegarderMetadonnees();

        try {
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
            Files.writeString(dataDir.resolve(nom), "");
        } catch (IOException e) {
            System.err.println("Erreur création fichier data: " + e.getMessage());
        }
        return true;
    }

    private void sauvegarderMetadonnees() {
        try {
            if (metaPath.getParent() != null && !Files.exists(metaPath.getParent())) {
                Files.createDirectories(metaPath.getParent());
            }
            List<String> lines = new ArrayList<>();
            for (FichierProtege f : fichiers.values()) {
                lines.add(f.getNom() + ";" + f.getProprietaire() + ";" + f.getBlocProprietaire() + ";"
                        + f.getBlocAutres());
            }
            Files.write(metaPath, lines);
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde fichiers: " + e.getMessage());
        }
    }

    public Collection<FichierProtege> listerTous() {
        return fichiers.values();
    }

    private void charger() {
        fichiers.clear();
        if (!Files.exists(metaPath)) {
            return;
        }
        try {
            List<String> lines = Files.readAllLines(metaPath);
            for (String line : lines) {
                if (line.trim().isEmpty())
                    continue;
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    String nom = parts[0];
                    String proprietaire = parts[1];
                    String propBloc = parts[2];
                    String autBloc = parts[3];

                    boolean pR = propBloc.contains("r");
                    boolean pW = propBloc.contains("w");
                    boolean pD = propBloc.contains("d");

                    boolean aR = autBloc.contains("r");
                    boolean aW = autBloc.contains("w");
                    boolean aD = autBloc.contains("d");

                    FichierProtege f = new FichierProtege(nom, proprietaire, pR, pW, pD, aR, aW, aD);
                    fichiers.put(nom, f);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur chargement fichiers: " + e.getMessage());
        }
    }
}