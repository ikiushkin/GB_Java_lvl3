package db;

import db.dao.User;
import db.dao.UsersDAO;
import db.dbconn.DBConn;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        /*DBConn
                .getInstance()
                .connection()
                .prepareStatement("create TABLE USERS (\n" +
                        "    LOGIN VARCHAR(30) UNIQUE NOT NULL,\n" +
                        "    PASS VARCHAR(10) NOT NULL,\n" +
                        "    NICK VARCHAR(10) UNIQUE NOT NULL\n" +
                        ");\n").execute();*/

        UsersDAO usersDAO = new UsersDAO();
        List<User> list = usersDAO.getAllUser();
        System.out.println(list.toString());
        usersDAO.changeUserNick("Test2", "Test111");
    }
}
