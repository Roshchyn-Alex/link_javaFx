package org.example.diplom_fx.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.example.diplom_fx.DB;
import javafx.scene.Node;
import org.example.diplom_fx.HelloApplication;

public class LinksPanelController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField full_link_textField, short_link_textField;

    @FXML
    private VBox panelVbox;

    @FXML
    private Label messageLabel;

    @FXML
    private Button addLink_btn;

    private DB db = new DB();

//    метод Node нужен для отображении всех ссылок и для обновления всего списка
    private Node linkNode (String short_link) throws IOException {
        Node node = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("link.fxml")));

        Hyperlink link_show = (Hyperlink) node.lookup("#link_show");
        link_show.setText(short_link);
        return node;
    }

    @FXML
    void initialize() throws SQLException, IOException, ClassNotFoundException {
        ResultSet resultSet = db.getLinks();
        while (resultSet.next()) {
            panelVbox.getChildren().add(linkNode(resultSet.getString("short_link")));
            panelVbox.setSpacing(10);
        }

        addLink_btn.setOnAction(actionEvent ->
        {
            try {
                addlink();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void addlink() throws IOException {
//   получаем данные от пользователя
//   getCharacters получает все символы и при помощи (toString) приводим к формату
        String full_link = full_link_textField.getCharacters().toString();
        String short_link = short_link_textField.getCharacters().toString();

//     если прошли все проверки на ошибки будет установлена белая обводка, а иначе красная
        full_link_textField.setStyle("-fx-border-color: #fafafa");
        short_link_textField.setStyle("-fx-border-color: #fafafa");


        if (full_link.length() <= 3 || short_link.length() < 2) {
            full_link_textField.setStyle("-fx-border-color: #F44336");
            short_link_textField.setStyle("-fx-border-color: #F44336");
            messageLabel.setText("Введите корректное значение");
        }
        else if (db.isExistsLink(short_link)) {
            short_link_textField.setStyle("-fx-border-color: #F44336");
            messageLabel.setText("Укажите другое сокращение");
        }
//   добавление в БД, очищаем поля
        else {
            db.addLink(full_link, short_link);
            panelVbox.getChildren().add(linkNode(short_link));
            messageLabel.setText("Добавлено!");
            full_link_textField.clear();
            short_link_textField.clear();
        }
    }
}
