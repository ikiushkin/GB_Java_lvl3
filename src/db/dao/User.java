package db.dao;

public class User {

    private String login;
    private String pass;
    private String nick;

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getNick() {
        return nick;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
