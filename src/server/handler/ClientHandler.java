package server.handler;

import server.service.ServerImpl;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

import static server.service.ServerImpl.usersDAO;

public class ClientHandler {

    private ServerImpl server;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean isClosed;
    private int timeout = 120;

    private volatile String nick;
    private volatile String login;

    public ClientHandler(ServerImpl server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.nick = "";
            this.login = "";

            Thread connection = new Thread(() -> {
                try {
                    authentication();
                    readMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            });
            connection.start();

            new Thread(() -> {
                try {
                    Thread.sleep(timeout * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (getNick().equals("")) {
                    sendMsg("Вы отключены! Превышение времени ожидания!");
                    sendMsg("/disconnect");
                    connection.interrupt();
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException("Проблемы при создании обработчика клиента");
        }
    }

    private void authentication() throws IOException {
        while (!isClosed) {
            if (dis.available() > 0) {
                String str = dis.readUTF();
                if (str.startsWith("/auth") && str.split("\\s").length == 3) {
                    String[] dataArray = str.split("\\s");
                    String login = dataArray[1];
                    String nick = server.getAuthService().getNick(dataArray[1], dataArray[2]);
                    if (nick != null) {
                        if (!server.isNickBusy(nick)) {
                            sendMsg("/authOk " + nick);
                            this.login = login;
                            this.nick = nick;
                            server.subscribe(this);
                            server.broadcastMsg(this.nick + " присоединился к чату");
                            return;
                        } else {
                            sendMsg("Пользователь с данным ником уже подключён");
                        }
                    } else {
                        sendMsg("Неверный логин или пароль");
                    }
                }
            }
        }
    }

    public void sendMsg(String msg) {
        try {
            dos.writeUTF(msg);
        } catch (IOException e) {
            System.out.println(nick + " вышел из чата и отключился");
        }
    }

    public void readMessage() {
        try {
            while (true) {
                String clientStr = dis.readUTF();
                System.out.println("От " + this.nick + ": " + clientStr);

                if (clientStr.startsWith("/")) {
                    if (clientStr.startsWith("/w") && clientStr.split("\\s").length > 2) {
                        String toUser = clientStr.split("\\s")[1];
                        String msg = clientStr.split("\\s", 3)[2];
                        privateMsg(ClientHandler.this, toUser, msg);
                        // Смена ника
                    } else if (clientStr.startsWith("/nick")) {
                        String newNick = clientStr.split("\\s")[1];
                        server.unsubscribe(this);
                        usersDAO.changeUserNick(nick, newNick);
                        sendMsg("Ваш новый ник: " + newNick);
                        this.nick = newNick;
                        server.subscribe(this);
                    } else if (clientStr.equals("/exit")) {
                        closeConnection();
                        return;
                    } else if (clientStr.equals("/online")) {
                        sendMsg("Пользователи в сети: ");
                        for (ClientHandler c : server.clients) {
                            sendMsg(c.getNick());
                        }
                        // Добавление нового клиента (только админ)
                    } else if (clientStr.startsWith("/add")) {
                        if (this.login.equals("admin")) {
                            String[] newUser = clientStr.split("\\s");
                            if (newUser.length == 4) {
                                usersDAO.addUserToBD(newUser[1], newUser[2], newUser[3]);
                                sendMsg("Пользователь с ником " + newUser[3] + " добавлен в базу");
                            } else {
                                sendMsg("Неверный формат воода пользователя");
                            }
                        } else {
                            sendMsg("Авторизация пользователей доступна только Администратору");
                        }
                    } else {
                        sendMsg("Вы ввели неверную команду");
                    }
                } else {
                    server.broadcastMsg(nick + ": " + clientStr);
                }
            }
        } catch (IOException | SQLException e) {
            sendMsg("Вы ввели неверную команду");
            sendMsg(String.valueOf(e.getStackTrace()));
        }
    }

    public void privateMsg(ClientHandler fromUser, String toUser, String msg) {
        if (!server.isNickBusy(toUser)) {
            sendMsg(toUser + " не в сети!");
        } else {
            for (ClientHandler c : server.clients) {
                if (toUser.equals(c.getNick())) {
                    c.sendMsg("От " + fromUser.getNick() + ": " + msg);
                    break;
                }
            }
            fromUser.sendMsg("Кому " + toUser + ": " + msg);
        }
    }

    public String getNick() {
        return nick;
    }

    private void closeConnection() {
        isClosed = true;
        server.unsubscribe(this);
        server.broadcastMsg(this.nick + ": Вышел из чата");

        try {
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}