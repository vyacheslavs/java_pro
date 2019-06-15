package Lesson_2.Server;

import java.sql.*;
import java.util.HashSet;

public class Auth {

    class Credentials {
        public int db_id;
        public String nickname;

        private boolean valid;

        public boolean isValid() {
            return valid;
        }

        private void setValid() {
            valid = true;
        }

        Credentials() {
            valid = false;
        }
    }


    private Connection connection;
    private Statement statement;

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:"+getClass().getResource("dbusers.db"));
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public Credentials getCredentials(String login, String pass) {
        String sql = String.format("SELECT id, nickname FROM users WHERE login='%s' AND passw='%s'", login, pass);
        try {
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {

                Credentials r = new Credentials();
                r.setValid();
                r.db_id = rs.getInt(1);
                r.nickname = rs.getString(2);
                return r;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Credentials();
    }

    public HashSet<String> getBlocks(int forWhom) {
        String sql = String.format("SELECT users.nickname FROM users INNER JOIN blocks ON (users.id = blocks.blocks_userid) WHERE blocks.userid=%d", forWhom);
        try {
            ResultSet rs = statement.executeQuery(sql);
            StringBuilder sb = new StringBuilder();
            HashSet<String> rv = new HashSet<String>();
            while (rs.next()) {
                rv.add(rs.getString(1));
            }
            return rv;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addBlock(int whosBlocking, int whom) {
        String sql = String.format("INSERT INTO blocks (userid, blocks_userid) VALUES (%d, %d)", whosBlocking, whom);
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBlock(int whosUnblocking, int whom) {
        String sql = String.format("DELETE FROM blocks WHERE userid=%d AND blocks_userid=%d", whosUnblocking, whom);
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Credentials chatAuthenticate(String command) {
        String[] splited = command.split("\\s");
        if (splited.length<3)
            return null;

        return getCredentials(splited[1], splited[2]);
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
