package Lesson_2.Client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application {

    private int chatWidth = 360;
    private int chatHeight = 600;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
        Parent root = loader.load();
        final ChatController controller = loader.getController();
        root.setId("pane");
        primaryStage.setTitle("Chat");
        primaryStage.setScene(new Scene(root, chatWidth, chatHeight));
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    if (controller.out != null)
                        controller.out.writeUTF("/end");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
