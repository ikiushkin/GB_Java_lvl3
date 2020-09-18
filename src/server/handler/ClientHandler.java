package server.handler;

import db.dao.User;
import db.dao.UsersDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.service.ServerImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler {

    private ServerImpl server;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean isClosed;
    private int timeout = 120;

    private String nick;
    private String login;

    private final UsersDAO usersDAO = new UsersDAO();
    private static final Logger LOGGER = LogManager.getLogger(ClientHandler.class);

    public ClientHandler(ServerImpl server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.nick = "";
            this.login = "";

            ExecutorService executorService = Executors.newCachedThreadPool();

            executorService.execute(() -> {
                try {
                    authentication();
                    readMessage();
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            });

            executorService.execute(() -> {
                try {
                    Thread.sleep(timeout * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (getNick().equals("")) {
                    sendMsg("Ваше соединение разорвано по причине долгого отсутствия");
                    LOGGER.warn("Ваше соединение разорвано по причине долгого отсутствия");
                    sendMsg("/disconnect");
                    executorService.shutdownNow();
                    closeConnection();
                }
            });

/*            Thread connection = new Thread(() -> {
                try {
                    authentication();
                    readMessage();
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            });
            connection.start();*/

/*            new Thread(() -> {
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
            }).start();*/
        } catch (IOException e) {
            LOGGER.error("Проблемы при создании обработчика клиента");
            throw new RuntimeException("Проблемы при создании обработчика клиента");
        }
    }

    private void authentication() throws IOException, SQLException {
        while (!isClosed) {
            if (dis.available() > 0) {
                String str = dis.readUTF();

                if (str.startsWith("/auth") && str.split("\\s").length == 3) {
                    String[] dataArray = str.split("\\s");
                    String login = dataArray[1];
                    User user = usersDAO.getUserByLogin(login);
                    if (user.getLogin() != null) {
                        if (user.getPass().equals(dataArray[2])) {
                            this.nick = user.getNick();
                        }
                    } else {
                        sendMsg("Вы ввели неверный пароль");
                        LOGGER.warn("(" + nick + ")" + "Вы ввели неверный пароль");
                        continue;
                    }
                    if (nick != null) {
                        if (!server.isNickBusy(nick)) {
                            sendMsg("/authOk " + nick);
                            this.login = login;
                            server.subscribe(this);
                            server.broadcastMsg(nick + " присоединился к чату");
                            return;
                        } else {
                            sendMsg("Пользователь с данным ником уже подключён");
                        }
                    } else {
                        LOGGER.warn("(" + nick + ")" + "Неверный логин или пароль");
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
                    } else if (clientStr.startsWith("/nick")) {
                        String newNick = clientStr.split("\\s")[1];
                        server.unsubscribe(this);
                        usersDAO.changeUserNick(nick, newNick);
                        LOGGER.info("(" + nick + ")" + "Ваш новый ник: " + newNick);
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
                    } else if (clientStr.startsWith("/add")) {
                        if (this.login.equals("admin")) {
                            String[] newUser = clientStr.split("\\s");
                            if (newUser.length == 4) {
                                usersDAO.addUserToBD(newUser[1], newUser[2], newUser[3]);
                                sendMsg("Пользователь с ником " + newUser[3] + " добавлен в базу");
                            } else {
                                sendMsg("Неверный формат ввода пользователя");
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
            e.getStackTrace();
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
        LOGGER.info("(" + nick + ")" + ": Вышел из чата");
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