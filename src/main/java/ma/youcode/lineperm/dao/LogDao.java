package ma.youcode.lineperm.dao;

import ma.youcode.lineperm.model.AccessLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogDao extends AbstractDao<AccessLog> {

    public LogDao() throws SQLException {
        super();
    }

    @Override
    public void save(AccessLog log) {
        String sql = "INSERT INTO logs (date, heure, utilisateur, action, fichier, resultat) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, log.getDate());
            stmt.setString(2, log.getHeure());
            stmt.setString(3, log.getUtilisateur());
            stmt.setString(4, log.getAction());
            stmt.setString(5, log.getFichier());
            stmt.setString(6, log.getResultat());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement du log : " + e.getMessage());
        }
    }

    @Override
    public AccessLog findById(int id) {
        String sql = "SELECT * FROM logs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccessLog(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du log par ID : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<AccessLog> findAll() {
        List<AccessLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM logs";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                logs.add(mapResultSetToAccessLog(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des logs : " + e.getMessage());
        }
        return logs;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM logs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du log : " + e.getMessage());
        }
    }

    private AccessLog mapResultSetToAccessLog(ResultSet rs) throws SQLException {
        return new AccessLog(
                rs.getString("date"),
                rs.getString("heure"),
                rs.getString("utilisateur"),
                rs.getString("action"),
                rs.getString("fichier"),
                rs.getString("resultat"));
    }
}