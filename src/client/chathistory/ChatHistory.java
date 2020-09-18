package client.chathistory;

import server.handler.ClientHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChatHistory {

    public static File history = new File("history.txt");

    public static void writeHistory(String writeToHistoryStr) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(history, true));
            writer.write(writeToHistoryStr + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readHistory(ClientHandler clientHandler) {
        List<String> list = new ArrayList<>();
        try {
            FileReader fr = new FileReader(history);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                list.add(line);
                line = reader.readLine();
            }
            if (list.size() > 0) {
                if (list.size() >= 20) {
                    for (int i = list.size() - 20; i < list.size(); i++) {
                        clientHandler.sendMsg(list.get(i));
                    }
                } else {
                    for (String str: list) {
                        clientHandler.sendMsg(str);
                    }
                }
            } else {
                clientHandler.sendMsg("История пуста");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
