package ma.youcode.lineperm.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ma.youcode.lineperm.model.FichierProtege;

public class FichierDao extends AbstractDao<FichierProtege> {

    public FichierDao() throws SQLException {
        super();
    }

    @Override
    public void save(FichierProtege fichier) {

        String sql = """
                INSERT INTO fichiers (
                    nom, proprietaire,
                    prop_r, prop_w, prop_d,
                    aut_r, aut_w, aut_d
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement pstmt = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, fichier.getNom());
            pstmt.setString(2, fichier.getProprietaire());

            pstmt.setBoolean(3, fichier.isPropR());
            pstmt.setBoolean(4, fichier.isPropW());
            pstmt.setBoolean(5, fichier.isPropD());

            pstmt.setBoolean(6, fichier.isAutR());
            pstmt.setBoolean(7, fichier.isAutW());
            pstmt.setBoolean(8, fichier.isAutD());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    fichier.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur ajout fichier : " + e.getMessage());
        }
    }

    @Override
    public FichierProtege findById(int id) {

        String sql = "SELECT * FROM fichiers WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapperFichier(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur findById : " + e.getMessage());
        }

        return null;
    }

    public FichierProtege findByNom(String nom) {

        String sql = "SELECT * FROM fichiers WHERE nom = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, nom);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapperFichier(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur findByNom : " + e.getMessage());
        }

        return null;
    }

    public List<FichierProtege> findByProprietaire(String proprietaire) {

        List<FichierProtege> fichiers = new ArrayList<>();

        String sql = "SELECT * FROM fichiers WHERE proprietaire = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, proprietaire);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    fichiers.add(mapperFichier(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur findByProprietaire : " + e.getMessage());
        }

        return fichiers;
    }

    @Override
    public List<FichierProtege> findAll() {

        List<FichierProtege> fichiers = new ArrayList<>();

        String sql = "SELECT * FROM fichiers";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                fichiers.add(mapperFichier(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur findAll : " + e.getMessage());
        }

        return fichiers;
    }

    public void updateDroits(int id, String droits) {

        String[] blocs = droits.split("\\|", -1);

        if (blocs.length != 2
                || blocs[0].length() != 3
                || blocs[1].length() != 3) {

            System.err.println("Format invalide. Exemple : rwx|---");
            return;
        }

        String prop = blocs[0];
        String aut = blocs[1];

        String sql = """
                UPDATE fichiers
                SET prop_r = ?,
                    prop_w = ?,
                    prop_d = ?,
                    aut_r = ?,
                    aut_w = ?,
                    aut_d = ?
                WHERE id = ?
                """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setBoolean(1, prop.charAt(0) == 'r');
            pstmt.setBoolean(2, prop.charAt(1) == 'w');
            pstmt.setBoolean(3, prop.charAt(2) == 'd');

            pstmt.setBoolean(4, aut.charAt(0) == 'r');
            pstmt.setBoolean(5, aut.charAt(1) == 'w');
            pstmt.setBoolean(6, aut.charAt(2) == 'd');

            pstmt.setInt(7, id);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur updateDroits : " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {

        String sql = "DELETE FROM fichiers WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur suppression fichier : " + e.getMessage());
        }
    }

    private FichierProtege mapperFichier(ResultSet rs) throws SQLException {

        return new FichierProtege(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("proprietaire"),

                rs.getBoolean("prop_r"),
                rs.getBoolean("prop_w"),
                rs.getBoolean("prop_d"),

                rs.getBoolean("aut_r"),
                rs.getBoolean("aut_w"),
                rs.getBoolean("aut_d"));
    }
}