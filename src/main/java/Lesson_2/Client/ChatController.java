package Lesson_2.Client;

import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class Victim <VictimType> implements AutoCloseable {
    VictimType body;
    Lock lock;

    @Override
    public void close() throws Exception {
        lock.unlock();
    }

    Victim(VictimType o, Lock _l) {
        body = o;
        lock = _l;
    }
}

class Guard <VictimType> {
    private final Lock lock = new ReentrantLock();
    private VictimType victim;
    Victim<VictimType> acquire() {
        lock.lock();
        return new Victim<VictimType>(victim, lock);
    }

    Guard() {}
    Guard(VictimType v) {
        victim = v;
    }
}

class ChatMembers {
    public ListView<String> membersList;
    public String[] membersArray;
}

public class ChatController implements Initializable {
    @FXML
    TextField textField;

    @FXML
    VBox msg_container;

    @FXML
    ScrollPane spane;

    @FXML
    StackPane body;

    @FXML
    Button btn1;

    @FXML
    TextField usernameField;

    @FXML
    TextField passwordField;

    @FXML
    Text errmsg;

    @FXML
    VBox vbpane;

    @FXML
    HBox allscene;

    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    final String IP_ADRESS = "localhost";
    final int    PORT = 10000;

    private Guard<ChatMembers> membersGuard = new Guard<ChatMembers>(new ChatMembers());
    private String myNick;

    public void sendMsg(final String msg) {
        if (msg.isEmpty())
            return;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                Label chatMessage = new Label(msg);
                chatMessage.getStyleClass().add("chat-bubble");
                chatMessage.setWrapText(true);
                chatMessage.setTextAlignment(TextAlignment.JUSTIFY);
                chatMessage.setMaxWidth(400);

                msg_container.getChildren().add(chatMessage);
                msg_container.autosize();
                msg_container.layout();
                spane.setVvalue(Double.MAX_VALUE);
            }
        });
    }

    public void tryAuth() {
        try {
            out.writeUTF(String.format("/auth %s %s", usernameField.getText(), passwordField.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg() {
        try {
            out.writeUTF(textField.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }

        textField.clear();
        textField.requestFocus();
    }

    private void validateMemberList(ChatMembers m) {
        if (m.membersArray != null && m.membersList != null) {
            m.membersList.getItems().clear();
            for (String i : m.membersArray) {
                if (myNick!=null && i!=null && !i.equals(myNick)) {
                    m.membersList.getItems().add(i);
                }
            }
        }
    }

    private void on_authOk(String msg) {

        myNick = msg.substring("/authok ".length());

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                body.getChildren().remove(vbpane);
                Stage st = (Stage) body.getScene().getWindow();
                st.setWidth(600);

                try (Victim<ChatMembers> v = membersGuard.acquire()) {
                    v.body.membersList = new ListView<String>();
                    v.body.membersList.prefWidth(100);
                    v.body.membersList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getClickCount() == 2) {

                                if (event.getTarget() instanceof LabeledText) {
                                    LabeledText t = (LabeledText) event.getTarget();
                                    textField.setText("/w "+t.getText()+ " ");
                                }

                            }
                        }
                    });

                    final ContextMenu contextMenu = new ContextMenu();
                    MenuItem cut = new MenuItem("Add to blocklist");
                    contextMenu.getItems().addAll(cut);
                    MenuItem uncut = new MenuItem("Remove from blocklist");
                    contextMenu.getItems().addAll(uncut);
                    cut.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            for (String s : v.body.membersList.getSelectionModel().getSelectedItems()) {
                                try {
                                    out.writeUTF("/block "+s);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    break;
                                }
                            }
                        }
                    });

                    uncut.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            for (String s : v.body.membersList.getSelectionModel().getSelectedItems()) {
                                try {
                                    out.writeUTF("/unblock "+s);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    break;
                                }
                            }
                        }
                    });

                    v.body.membersList.setContextMenu(contextMenu);

                    allscene.getChildren().add(v.body.membersList);
                    validateMemberList(v.body);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        textField.visibleProperty().setValue(true);
        btn1.visibleProperty().setValue(true);
    }

    private void on_members(final String allMembers) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try (Victim<ChatMembers> v = membersGuard.acquire()) {
                    v.body.membersArray = allMembers.substring("/members ".length()).split(",");

                    validateMemberList(v.body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void chatMainCycle() {
        
        try {
            socket = new Socket(IP_ADRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String msg = in.readUTF();

                            if (msg.startsWith("/authfail")) {
                                errmsg.setVisible(true);
                                continue;
                            }

                            if (msg.startsWith("/authok ")) {
                                on_authOk(msg);
                                continue;
                            }

                            if (msg.startsWith("/members ")) {
                                on_members(msg);
                                continue;
                            }

                            sendMsg(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        msg_container.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                spane.setVvalue((Double)newValue);
            }
        });

        textField.visibleProperty().setValue(false);
        btn1.visibleProperty().setValue(false);

        chatMainCycle();
    }
}
