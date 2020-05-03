package listviewexample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main extends Application {

    private ObservableList<Organization> data = FXCollections.observableArrayList();
    private ObservableList<User> users = FXCollections.observableArrayList();
    private List<Place> places = new ArrayList<>();
    private ListView<User> userView;
    private ViewUser view = new ViewUser();
    private Font font = Font.font(18);


    @Override
    public void init() {
        try {
            int line = 0;
            PrintWriter out = new PrintWriter("error.txt");
            out.print("");
            out.close();
            Scanner inPlace = new Scanner(new File("place.txt"));
            while (inPlace.hasNextLine()) {
                line++;
                String[] str = inPlace.nextLine().split(" +");
                if (str.length != 5 || !str[1].matches("[0-9]+") || !str[2].matches("[0-9]+")  //сделать ограничение по координатам?
                        || !str[3].matches("[0-9]+") || !str[4].matches("[0-9]+")
                        || str[1].equals(str[3]) || str[2].equals(4)) {
                    try {
                        FileWriter errorOut = new FileWriter("error.txt", true);
                        errorOut.write("ERROR file: place.txt, string: " + line + "\n");
                        errorOut.close();
                    } catch (IOException e) {
                        System.out.println("Error with error.txt");
                    }
                } else
                    places.add(new Place(str[0], Integer.parseInt(str[1]), Integer.parseInt(str[2]), Integer.parseInt(str[3]), Integer.parseInt(str[4])));
            }
            inPlace.close();
            line = 0;
            Scanner inUser = new Scanner(new File("user.txt"));
            while (inUser.hasNextLine()) {
                line++;
                String[] str = inUser.nextLine().split(",");
                if (str.length != 4 || !str[0].matches("(0[1-9]|[12][0-9]|3[01]|[1-9]).(0[1-9]|1[012]).([0-9][0-9]) ((0[1-9])|(2[0-4])|(1[0-9])|([1-9])):[0-5][0-9]") || !str[1].matches("[0-9]+")
                        || !str[2].matches("[0-9]+") || !str[3].matches("[0-9]+")) {
                    try {
                        FileWriter errorOut = new FileWriter("error.txt", true);
                        errorOut.write("ERROR file: user.txt, string: " + line + "\n");
                        errorOut.close();
                    } catch (IOException e) {
                        System.out.println("Error with error.txt");
                    }
                } else {
                    try {
                        boolean flagId = false;
                        boolean flagPlace = false;
                        boolean flagPlaceNull = false;
                        DateFormat formatter = new SimpleDateFormat("d.MM.yy H:m");
                        Date date = (Date) formatter.parse(str[0]);
                        for (User j : users) {
                            if (j.getId() == Integer.parseInt(str[1])) {
                                flagId = true;
                                for (Place i1 : places) {
                                    if (i1.inThisPlace(Integer.parseInt(str[2]), Integer.parseInt(str[3]))) {
                                        flagPlaceNull = true;
                                        i1.addCount();
                                        j.addAtListOfLocations(date, i1, Integer.parseInt(str[2]), Integer.parseInt(str[3]));
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
                                    j.addAtListOfLocations(date, new Place("notPlace", 0, 0, 100, 100), Integer.parseInt(str[2]), Integer.parseInt(str[3]));
                                }
                            }
                        }
                        if (!flagId) {
                            User newUser = new User(Integer.parseInt(str[1]));
                            for (Place i2 : places) {
                                if (i2.inThisPlace(Integer.parseInt(str[2]), Integer.parseInt(str[3]))) {//(4)
                                    flagPlaceNull = true;
                                    i2.addCount();
                                    newUser.addAtListOfLocations(date, i2, Integer.parseInt(str[2]), Integer.parseInt(str[3]));
                                    for (Frequency k1 : i2.getFrequency()) {
                                        if (k1.getDate().equals(date)) {
                                            k1.add();
                                            flagPlace = true;
                                        }
                                    }
                                    if (!flagPlace) {
                                        i2.addAtListFrequency(date);
                                    }
                                }
                            }
                            if (!flagPlaceNull) {
                                newUser.addAtListOfLocations(date, new Place("notPlace", 0, 0, 100, 100), Integer.parseInt(str[2]), Integer.parseInt(str[3]));
                            }
                            users.add(newUser);
                        }

                    } catch (ParseException e) {
                        System.out.println("Parse error");
                    }
                }
            }
            inUser.close();
            for (User i : users) {
                i.sortLocations();
            }
            System.out.println("Юзеры " + users.toString());
            System.out.println("Места " + places.toString());

        } catch (IOException e) {
            System.out.println("Error with init");  //????
        }
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setCenter(createListViewUser());
        root.setRight(view.getPane());
        root.setTop(createMenu());
        primaryStage.setTitle("List of users");
        primaryStage.setScene(new Scene(root, 700, 600));
        primaryStage.show();
    }

    private ListView<User> createListViewUser() {
        userView = new ListView<>(users);
        userView.setStyle("-fx-font-size: 20px;");
        userView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) view.setUser(newValue);
        });
        return userView;
    }


    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle("-fx-font-size: 20px;");
        alert.showAndWait();
    }

    private MenuBar createMenu() {
        Menu addMenu = new Menu("Add");
        MenuItem add = new MenuItem("Add location of user");
        addMenu.getItems().add(add);
        add.setOnAction((ActionEvent event) -> {
            handleButtonAdd();
        });

        Menu showMenu = new Menu("Show");
        MenuItem showGraphic = new MenuItem("Show graphic of place");
        showMenu.getItems().add(showGraphic);
        showGraphic.setOnAction((ActionEvent event) -> {
            handleButtonShowGraphic();
        });
        MenuItem showSortedPlaces = new MenuItem("Show a list of sorted places");
        showMenu.getItems().add(showSortedPlaces);
        showSortedPlaces.setOnAction((ActionEvent event) -> {
            handleButtonShowSortedPlaces();
        });
        MenuItem showMap = new MenuItem("Show map of the selected user's locations");
        showMenu.getItems().add(showMap);
        showMap.setOnAction((ActionEvent event) -> {
            handleButtonShowMap();
        });

        Menu exitMenu = new Menu("Exit");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> Platform.exit());
        exitMenu.getItems().add(exitItem);

        return new MenuBar(addMenu, showMenu, exitMenu);
    }

    private void handleButtonShowMap() {
        User currentUser = userView.getSelectionModel().getSelectedItem();
        if (currentUser != null) {
            MapWindow mapWindow = new MapWindow(currentUser);
        } else {
            showMessage("No selected user!");
        }
    }

    private void handleButtonShowSortedPlaces() {
        SortedPlacesWindow sortedPlacesWindow = new SortedPlacesWindow(places);
    }

    private void handleButtonShowGraphic() {
        DialogForGraphic graphicDialog = new DialogForGraphic(places);
    }

    private void handleButtonAdd() {
        DialogAddLocation addDialog = new DialogAddLocation(null, places, users);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

