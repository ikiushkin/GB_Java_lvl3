package server.service;

import db.dao.User;
import server.inter.AuthService;

import java.sql.SQLException;
import java.util.List;

import static server.service.ServerImpl.usersDAO;

public class AuthServiceImpl implements AuthService {

    public List<User> usersList;

    public AuthServiceImpl() {
        try {
            this.usersList = usersDAO.getAllUser();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        System.out.println("Сервис аутентификации запущен");
    }

    @Override
    public String getNick(String login, String password) {
        for (User u : usersList) {
            if (u.getLogin().equals(login) && u.getPass().equals(password)) {
                return u.getNick();
            }
        }
        return null;
    }

    @Override
    public void stop() {
        System.out.println("Сервис аутентификации остановлен");
    }
}
