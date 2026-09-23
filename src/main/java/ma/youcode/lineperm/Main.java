package ma.youcode.lineperm;

import java.sql.SQLException;
import ma.youcode.lineperm.db.DatabaseInitializer;
import ma.youcode.lineperm.ui.ConsoleApp;

public class Main {

    public static void main(String[] args) throws SQLException {

        DatabaseInitializer.init();

        ConsoleApp app = new ConsoleApp();
        app.demarrer();
    }
}