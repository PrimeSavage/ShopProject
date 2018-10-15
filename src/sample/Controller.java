package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;


public class Controller {
    @FXML
    private Label paymentLabel;
    @FXML
    private TextField fieldSerial;
    @FXML
    private TextField fieldPrice;
    @FXML
    private TableView<Cloth> tblView;
    @FXML
    private TableColumn<Cloth, Integer> serialColumn;
    @FXML
    private TableColumn<Cloth, String> typeColumn;
    @FXML
    private TableColumn<Cloth, String> colorColumn;
    @FXML
    private TableColumn<Cloth, Integer> priceColumn;
    @FXML
    private TableView<Cloth> tblView1;
    @FXML
    private TableColumn<Cloth, Integer> serialColumn1;
    @FXML
    private TableColumn<Cloth, String> typeColumn1;
    @FXML
    private TableColumn<Cloth, String> colorColumn1;
    @FXML
    private TableColumn<Cloth, Integer> priceColumn1;
    @FXML
    private TableView<Cloth> tblView2;
    @FXML
    private TableColumn<Cloth, Integer> serialColumn2;
    @FXML
    private TableColumn<Cloth, String> typeColumn2;
    @FXML
    private TableColumn<Cloth, String> colorColumn2;
    @FXML
    private TableColumn<Cloth, Integer> priceColumn2;

    private ObservableList<Cloth> clothes = FXCollections.observableArrayList();
    private ObservableList<Cloth> bin = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox cbType = new ChoiceBox();
    @FXML
    private ChoiceBox cbColor = new ChoiceBox();

    private Connection connection;
    private Statement statement;
    private ResultSet rs;
    private PreparedStatement preparedStatement;
    private int pmnt = 0, n = 0;


    public Controller() {
    }

    @FXML
    private void initialize() {
        serialColumn.setCellValueFactory(new PropertyValueFactory<>("serial"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        serialColumn1.setCellValueFactory(new PropertyValueFactory<>("serial"));
        typeColumn1.setCellValueFactory(new PropertyValueFactory<>("type"));
        colorColumn1.setCellValueFactory(new PropertyValueFactory<>("color"));
        priceColumn1.setCellValueFactory(new PropertyValueFactory<>("price"));
        serialColumn2.setCellValueFactory(new PropertyValueFactory<>("serial"));
        typeColumn2.setCellValueFactory(new PropertyValueFactory<>("type"));
        colorColumn2.setCellValueFactory(new PropertyValueFactory<>("color"));
        priceColumn2.setCellValueFactory(new PropertyValueFactory<>("price"));
        cbType.setItems(FXCollections.observableArrayList("", "Рубашка", "Платье", "Футболка", "Свитер", "Майка"));
        cbColor.setItems(FXCollections.observableArrayList("", "Белый", "Чёрный", "Красный", "Зелёный", "Синий", "Жёлтый", "Розовый"));
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/clothesBase?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
            statement = connection.createStatement();
            rs = statement.executeQuery("select serial, color, type, price from clothes");

            while (rs.next()) {
                int serial = rs.getInt(1);
                String color = rs.getString(2);
                String type = rs.getString(3);
                int price = rs.getInt(4);
                clothes.add(new Cloth(serial, color, type, price));
            }
            tblView.setItems(clothes);
            tblView1.setItems(clothes);
            System.out.println(connection.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onAdd() {
        if (!fieldSerial.getText().isEmpty() && !fieldPrice.getText().isEmpty()) {
            try {
                if (cbType.getValue() == null || cbColor.getValue() == null)
                    throw new IllegalArgumentException("Заполните все поля");
                if (Integer.parseInt(fieldSerial.getText()) <= 0 || Integer.parseInt(fieldPrice.getText()) <= 0)
                    throw new IllegalArgumentException("Заполните поля ввода");
                for (Cloth cl : clothes) {
                    if (Integer.parseInt(fieldSerial.getText()) == cl.getSerial())
                        throw new IllegalArgumentException("Товар этой серии уже имеется");
                }
                clothes.add(new Cloth(Integer.parseInt(fieldSerial.getText()), cbColor.getValue().toString(), cbType.getValue().toString(), Integer.parseInt(fieldPrice.getText())));
                preparedStatement = connection.prepareStatement("insert into clothes (serial, color, type, price) values (?, ?, ?, ?);");
                preparedStatement.setInt(1, Integer.parseInt(fieldSerial.getText()));
                preparedStatement.setString(2, cbColor.getValue().toString());
                preparedStatement.setString(3, cbType.getValue().toString());
                preparedStatement.setInt(4, Integer.parseInt(fieldPrice.getText()));
                preparedStatement.executeUpdate();
                tblView.setItems(clothes);
                tblView1.setItems(clothes);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
            }
            fieldSerial.clear();
            fieldPrice.clear();
            cbColor.setValue(null);
            cbType.setValue(null);
        }
    }

    public void onRemove() {
        for (Cloth clth : clothes) {
            if (tblView1.getSelectionModel().selectedItemProperty().get().getSerial() == (clth.getSerial()))
            {
                clothes.remove(clth);
                try {
                    preparedStatement = connection.prepareStatement("delete from clothes where serial = ?");
                    preparedStatement.setInt(1, clth.getSerial());
                    preparedStatement.executeUpdate();
                } catch (SQLException e) { }
                break;
            }
        }
        tblView.refresh();
        tblView1.refresh();
    }

    public void onAddToBin() {
        tblView2.getItems().addAll(tblView.getSelectionModel().getSelectedItems());
        for (Cloth clth : clothes) {
            if (tblView2.getItems().get(n).getSerial() == (clth.getSerial())) {
                pmnt += clth.getPrice();
                n++;
                break;
            }
        }
        paymentLabel.setText(String.valueOf(pmnt));
    }

    public void onBuy() {
        if(!tblView2.getItems().isEmpty()) {
            tblView2.getItems().removeAll(tblView2.getItems());
            tblView2.refresh();
            paymentLabel.setText("Заказ принят!");
            pmnt = 0;
            n = 0;
        }
        else paymentLabel.setText("Нечего заказывать");
    }
    private boolean result = true;
    public void onAdminSelect(){
        if(result){
        AdminAlert.display();
        result = false;
        }
    }
}
