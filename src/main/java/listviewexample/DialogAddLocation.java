package listviewexample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class DialogAddLocation {
    private TextField idEdit;
    private User user;
    private ObservableList<User> listOfUsers;
    private List<Place> places;
    private DatePicker dateEdit;
    private Font font = Font.font(16);
    //    private ComboBox<Place> placeEdit;
    private TextField timeEdit;
    private Spinner<Integer> xEdit, yEdit;
    private GridPane root;
    private Stage dialog;

    public DialogAddLocation(User user, List<Place> places, ObservableList<User> listOfUsers) {
        this.places = places;
        this.user = user;
        this.listOfUsers = listOfUsers;
        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        createIdEdit();
//        createPlaceEdit();
        createDateEdit();
        createTimeEdit();
        createXYEdit();
        createButtons();
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add location of user");
        Scene scene = new Scene(root, 500, 300);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void createXYEdit() {
        Label x = new Label("x:");
        x.setFont(font);
        root.add(x, 0, 2);
        xEdit = new Spinner(0, 100, 0);
        root.add(xEdit, 1, 2);
        Label y = new Label("y:");
        y.setFont(font);
        root.add(y, 0, 3);
        yEdit = new Spinner(0, 100, 0);
        root.add(yEdit, 1, 3);
    }

    private void createIdEdit() {
        Label id = new Label("Id:");
        id.setFont(font);
        root.add(id, 0, 1);
        idEdit = new TextField();
        root.add(idEdit, 1, 1);
    }

//    private void createPlaceEdit() {
//        Label place = new Label("Place:");
//        place.setFont(font);
//        root.add(place, 0, 2);
//        placeEdit = new ComboBox<>();
//        placeEdit.setStyle("-fx-font-size: 15 pt");
//        placeEdit.getItems().addAll(places);
//        placeEdit.setValue(places.get(0));
//        root.add(placeEdit, 1, 2);
//    }

    public void createDateEdit() {
        Label date = new Label("Date:");
        date.setFont(font);
        root.add(date, 0, 4);
        dateEdit = new DatePicker(LocalDate.now());
        dateEdit.getEditor().setDisable(true);
        dateEdit.setStyle("-fx-font-size: 18 pt");
        root.add(dateEdit, 1, 4);
    }

    public void createTimeEdit() {
        Label time = new Label("Time: (hh:mm format)");
        time.setFont(font);
        root.add(time, 0, 5);
        timeEdit = new TextField();
        root.add(timeEdit, 1, 5);
    }

    public void createButtons() {
        Button btnOk = new Button("Ok");
        btnOk.setFont(font);
        root.add(btnOk, 0, 6);
        btnOk.setOnAction((ActionEvent e) -> {
            if (isInputValid()) {
                boolean flagId = false;
                boolean flagPlace = false;
                boolean flagPlaceNull = false;
                String[] time = timeEdit.getText().split(":");
                LocalDateTime dateTime = dateEdit.getValue().atStartOfDay().withHour(Integer.parseInt(time[0])).withMinute(Integer.parseInt(time[1]));
                Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
                for (User j : listOfUsers) {
                    if (j.getId() == Integer.parseInt(idEdit.getText())) {
                        flagId = true;
                        for (Place i1 : places) {
                            if (i1.inThisPlace(xEdit.getValue(), yEdit.getValue())) {
                                flagPlaceNull = true;
                                i1.addCount();
                                j.addAtListOfLocations(date, i1, xEdit.getValue(), yEdit.getValue());
                                for (Frequency k1 : i1.getFrequency()) {
                                    if (k1.getDate().equals(date)) {
                                        k1.add();
                                        flagPlace = true;
                                    }
                                }
                                if (!flagPlace) {
                                    i1.addAtListFrequency(date);
                                }
                            }
                        }
                        if (!flagPlaceNull) {
                            j.addAtListOfLocations(date, new Place("notPlace", 0, 0, 100, 100), xEdit.getValue(), yEdit.getValue());
                        }
                        addAtFileNewInfo(date, j, xEdit.getValue(), yEdit.getValue());
                    }
                }
                if (!flagId) {
                    user = new User(Integer.parseInt(idEdit.getText()));
                    for (Place i2 : places) {
                        if (i2.inThisPlace(xEdit.getValue(), yEdit.getValue())) {
                            flagPlaceNull = true;
                            i2.addCount();
                            user.addAtListOfLocations(date, i2, xEdit.getValue(), yEdit.getValue());
                            for (Frequency k1 : i2.getFrequency()) {
                                if (k1.getDate().equals(date)) {
                                    k1.add();
                                    flagPlace = true;
                                }
                            }
                            if (!flagPlace) {
                                i2.addAtListFrequency(date);
                            }
//                            addAtFileNewInfo(date, user, i2);
                        }
                    }
                    if (!flagPlaceNull) {
                        user.addAtListOfLocations(date, new Place("notPlace", 0, 0, 100, 100), xEdit.getValue(), yEdit.getValue());
                    }
                    listOfUsers.add(user);
                    addAtFileNewInfo(date, user, xEdit.getValue(), yEdit.getValue());
                }
                for (User i : listOfUsers) {
                    i.sortLocations();
                }
                dialog.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Id entry error");
                alert.setHeaderText(null);
                alert.setContentText("Id of user is numbers!!!\n" +
                        "The time must be in the format hh:mm!!!");
                alert.showAndWait();
            }
        });
        Button btnCancel = new Button("Cancel");
        btnCancel.setFont(font);
        root.add(btnCancel, 1, 6);
        btnCancel.setOnAction((ActionEvent e) -> {
            user = null;
            dialog.close();
        });
    }

    private void addAtFileNewInfo(Date date, User user, int x, int y) {
        try {
            DateFormat formatter = new SimpleDateFormat("d.MM.yy H:m");
            FileWriter out = new FileWriter("user.txt", true);
            out.write(formatter.format(date) + "," + user.getId() + "," + x + "," + y + "\n");
            out.close();
        } catch (IOException e) {
            System.out.println("Error with rewriting user.txt");
        }
    }

    private boolean isInputValid() {
        return (idEdit.getText().matches("[0-9]+")
                && timeEdit.getText().matches("((0[1-9])|(2[0-4])|(1[0-9])):[0-5][0-9]"));
    }
}
