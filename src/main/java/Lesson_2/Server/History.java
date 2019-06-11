package Lesson_2.Server;

import Lesson_2.Guard;
import Lesson_2.Victim;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

class HistoryException extends RuntimeException {
    public HistoryException(Throwable e) {
        super("History object error", e);
    }
}

interface HistoryWalking {
    void messageUpdate(int db_id, String msg);
}

public class History {
    private Connection connection;
    private Guard<Statement> statement;

    public void connect() throws HistoryException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:"+getClass().getResource("dbusers.db"));
            statement = new Guard<>(connection.createStatement());
        } catch (java.sql.SQLException|ClassNotFoundException e) {
            throw new HistoryException(e);
        }
    }

    public void archivate(int id, String msg) throws HistoryException {
        try (Victim<Statement> st = statement.acquire()) {
            String sql = String.format("INSERT INTO history (userid, txt) VALUES (%d, \"%s\")", id, msg);
            st.body.executeUpdate(sql);
        } catch (Exception e) {
            throw new HistoryException(e);
        }
    }

    public void iterateHistory(HistoryWalking interfc) throws HistoryException {
        try (Victim<Statement> st = statement.acquire()) {
            String sql = "SELECT userid, txt FROM history ORDER BY id";
            ResultSet rs = st.body.executeQuery(sql);
            while (rs.next()) {
                interfc.messageUpdate(rs.getInt(1), rs.getString(2));
            }
        } catch (Exception e) {
            throw new HistoryException(e);
        }
    }
}
