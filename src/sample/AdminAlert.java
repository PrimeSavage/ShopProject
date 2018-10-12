package sample;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class AdminAlert {
    public static void display(){
        Stage window = new Stage(StageStyle.UNDECORATED);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(400);
        window.setMinHeight(250);

        Label label = new Label("Введите логин и пароль");
        label.setFont(Font.font(15));

        PasswordField login = new PasswordField();
        login.setPromptText("логин");
        login.setMaxWidth(200);
        PasswordField password = new PasswordField();
        password.setPromptText("пароль");
        password.setMaxWidth(200);

        Button enterButton = new Button("Продолжить");
        String realLogin = "Qwerty1234";
        String realPassword = "4567";
        enterButton.setOnAction(event -> {
            if(login.getText().matches(realLogin) && password.getText().matches(realPassword)){
                window.close();
                }
    });
        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, login, password, enterButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }
}
