package db.dao;

import db.dbconn.DBConn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UsersDAO {

    PreparedStatement ps;

    public void addUser(User user) throws SQLException {
        ps = DBConn
                .getInstance()
                .connection()
                .prepareStatement("INSERT INTO users (login, pass, nick)  VALUES (?, ?, ?)");
        ps.setString(1, user.getLogin());
        ps.setString(2, user.getPass());
        ps.setString(3, user.getNick());
        ps.executeUpdate();
    }

    public User getUserByLogin(String login) throws SQLException {
        ps = DBConn
                .getInstance()
                .connection()
                .prepareStatement("SELECT * FROM users WHERE login = ?");
        ps.setString(1, login);

        ResultSet set = ps.executeQuery();

        User user = new User();

        if (set.next()) {
            user.setLogin(set.getString("LOGIN"));
            user.setPass(set.getString("PASS"));
            user.setNick(set.getString("NICK"));
        }
        return user;
    }

    public List<User> getAllUser() throws SQLException {
        List<User> list = new LinkedList<>();

        ps = DBConn
                .getInstance()
                .connection()
                .prepareStatement("SELECT * FROM users");

        ResultSet set = ps.executeQuery();

        while (set.next()) {
            User user = new User();
            user.setLogin(set.getString("LOGIN"));
            user.setPass(set.getString("PASS"));
            user.setNick(set.getString("NICK"));
            list.add(user);
        }
        return list;
    }

    public void changeUserNick (String oldNick, String newNick) throws SQLException {
        ps = DBConn
                .getInstance()
                .connection()
                .prepareStatement("UPDATE users SET nick=? WHERE nick = ?");
        ps.setString(1, newNick);
        ps.setString(2, oldNick);
        int set = ps.executeUpdate();
    }

    public void addUserToBD(String login, String pass, String nick) throws SQLException {
        ps = DBConn
                .getInstance()
                .connection()
                .prepareStatement("INSERT INTO users (LOGIN, PASS, NICK) VALUES (?, ?, ?)");
        ps.setString(1, login);
        ps.setString(2, pass);
        ps.setString(3, nick);
        int set = ps.executeUpdate();
    }
}
