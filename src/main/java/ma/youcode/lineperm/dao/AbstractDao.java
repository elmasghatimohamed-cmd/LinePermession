package ma.youcode.lineperm.dao;

import ma.youcode.lineperm.db.DBConnection;
import java.sql.Connection;

public abstract class AbstractDao<T> implements Dao<T> {
    protected Connection connection;

    public AbstractDao() throws SQLException {
        this.connection = DBConnection.getConnection();
    }
}