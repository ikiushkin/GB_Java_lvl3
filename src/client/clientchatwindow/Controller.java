package client.clientchatwindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Controller {

    @FXML
    TextArea mainTextArea;
    @FXML
    TextField messageArea;
    @FXML
    Button sendButton;
    @FXML
    TextField loginArea;
    @FXML
    TextField passwordArea;
    @FXML
    Button loginButton;
    @FXML
    HBox logPassBox;
    @FXML
    HBox chatBox;

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean isClosed;

    private final File history = new File("history.txt");

    public void clickAction(ActionEvent actionEvent) {
        if (!messageArea.getText().trim().isEmpty()) {
            if (socket == null || socket.isClosed()) {
                clientService();
            }
            try {
                dos.writeUTF(messageArea.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
            messageArea.clear();
        }
    }

    public void loginAction(ActionEvent actionEvent) {
        if (!loginArea.getText().trim().isEmpty() && !passwordArea.getText().trim().isEmpty()) {
            if (socket == null || socket.isClosed()) {
                clientService();
            }
            try {
                dos.writeUTF(String.format("/auth %s %s", loginArea.getText(), passwordArea.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loginArea.clear();
        passwordArea.clear();
    }

    public void clientService() {

        try {
            isClosed = false;
            socket = new Socket("localhost", 8189);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            setAuth(false);

            Thread t1 = new Thread(() -> {
                try {
                    while (!isClosed) {
                        if (dis.available() > 0) {
                            String strMsg = dis.readUTF();
                            if (strMsg.startsWith("/authOk")) {
                                setAuth(true);
                                readHistory();
                                break;
                            } else if (strMsg.equals("/disconnect")) {
                                closeConnection();
                                return;
                            }
                            mainTextArea.appendText(strMsg + "\n");
                        }
                    }
                    while (!isClosed) {
                        String strMsg = dis.readUTF();
                        if (strMsg.equals("/exit")) {
                            break;
                        }
                        writeHistory(new SimpleDateFormat("E yyyy.MM.dd HH:mm ").format(new Date()) + ": " + strMsg);
                        mainTextArea.appendText(strMsg + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t1.setDaemon(true);
            t1.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readHistory() {
        int size = 20;
        ArrayList<String> list = new ArrayList<>();
        try {
            FileReader fr = new FileReader(history);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            if (line.length() > 0) {
                while (line != null) {
                    list.add(line);
                    line = reader.readLine();
                }
                if (list.size() >= size) {
                    for (int i = list.size() - size; i < list.size(); i++) {
                        mainTextArea.appendText(list.get(i) + "\n");
                    }
                } else {
                    for (String s : list) {
                        mainTextArea.appendText(s + "\n");
                    }
                }
            } else {
                mainTextArea.appendText("История чата пуста" + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeHistory(String writeToHistoryStr) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(history, true));
            writer.write(writeToHistoryStr + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setAuth(boolean b) {
        if (b) {
            logPassBox.setVisible(false);
            chatBox.setVisible(true);
        } else {
            logPassBox.setVisible(true);
            chatBox.setVisible(false);
        }
    }

    private void closeConnection() {
        isClosed = true;

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
