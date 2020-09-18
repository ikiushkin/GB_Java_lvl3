package server.service;

import server.handler.ClientHandler;
import server.inter.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerImpl implements Server {

    public List<ClientHandler> clients;
    private static final Logger LOGGER = LogManager.getLogger(ServerImpl.class);

    public ServerImpl() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            clients = new LinkedList<>();
            while (true) {
                LOGGER.info("Ожидаем подключения клиентов");
                Socket socket = serverSocket.accept();
                LOGGER.info("Клиент подключился");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            LOGGER.error("Проблема на сервере");
        } finally {
            LOGGER.info("Сервер остановлен");
        }
    }

    @Override
    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler c : clients) {
            if (c.getNick() != null && c.getNick().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized void broadcastMsg(String msg) {
        for (ClientHandler c : clients) {
            c.sendMsg(msg);
        }
    }

    @Override
    public synchronized void subscribe(ClientHandler client) {
        clients.add(client);
    }

    @Override
    public synchronized void unsubscribe(ClientHandler client) {
        clients.remove(client);
    }
}