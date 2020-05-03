package listviewexample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
import java.util.stream.Collectors;

public class Main extends Application {

    private ObservableList<Organization> data = FXCollections.observableArrayList();
    private ObservableList<User> users = FXCollections.observableArrayList();
    private List<Place> places = new ArrayList<>();
    private ListView<Organization> dataView;
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
                    try {//(1)
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
                        if (!flagId) {//(2)
                            User newUser = new User(Integer.parseInt(str[1]));
                            for (Place i2 : places) {//(3)
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


//            data.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

        } catch (IOException e) {
            System.out.println("Error with init");  //????
        }
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setCenter(createListViewUser());
        root.setRight(view.getPane());
//        root.setLeft(createButtons());
        root.setTop(createMenu());
        primaryStage.setTitle("List of users");
        primaryStage.setScene(new Scene(root, 700, 600));
////        showBeginMessage();
        primaryStage.show();
////        showBeginMessage();
    }

//    private void showBeginMessage(){
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Hello");
//        alert.setHeaderText(null);
//        alert.setContentText(beginMessage);
//        alert.getDialogPane().setStyle("-fx-font-size: 16px;");
//        alert.showAndWait();
//    }

    private ListView<User> createListViewUser() {
        userView = new ListView<>(users);
        userView.setStyle("-fx-font-size: 20px;");
        userView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) view.setUser(newValue);
        });
        return userView;
    }

    private VBox createButtons() {
        VBox boxButtons = new VBox();
        boxButtons.setPadding(new Insets(10));
        boxButtons.setSpacing(10);
        boxButtons.setAlignment(Pos.CENTER);
        Button filterBossName = new Button("Filter same boss");
        filterBossName.setFont(font);
        filterBossName.setOnAction((ActionEvent e) -> {
            Organization org = dataView.getSelectionModel().getSelectedItem();
            if (org != null) {
                ObservableList<Organization> dataFilter = FXCollections.observableArrayList();
                dataFilter.setAll(data.stream().filter(organization -> organization.isTheSameBoss(org)).collect(Collectors.toList()));
                dataFilterView(dataFilter);
            } else {
                showMessage("No selected item!");
            }
        });
        Button showBossName = new Button("Show same boss");
        showBossName.setFont(font);
        showBossName.setOnAction((ActionEvent e) -> {
            Organization org = dataView.getSelectionModel().getSelectedItem();
            if (org != null) {
                ObservableList<Organization> dataShow = FXCollections.observableArrayList();
                dataShow.setAll(data.stream().filter(organization -> organization.isTheSameBoss(org)).collect(Collectors.toList()));
                dataView.setItems(dataShow);
            } else {
                showMessage("No selected item!");
            }
        });
        Button showAll = new Button("Show all");
        showAll.setFont(font);
        showAll.setOnAction((ActionEvent e) -> dataView.setItems(data));
        boxButtons.getChildren().addAll(filterBossName, showBossName, showAll);
        return boxButtons;
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
//        MenuItem edit = new MenuItem("Edit organization");
//        editMenu.getItems().add(edit);
//        edit.setOnAction((ActionEvent event) -> {
//            handleButtonEdit();
//        });
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

        Menu exitMenu = new Menu("Exit");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> Platform.exit());
        exitMenu.getItems().add(exitItem);

        return new MenuBar(addMenu, showMenu, exitMenu);
    }

    private void handleButtonEdit() {
        Organization organization = dataView.getSelectionModel().getSelectedItem();
        if (organization != null) {
            DialogEditOrg orgEditDialog = new DialogEditOrg(organization);
            data.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        } else {
            showMessage("No selected item!");
        }
    }

    private void handleButtonShowGraphic() {
        DialogForGraphic graphicDialog = new DialogForGraphic(places);
    }

    private void handleButtonAdd() {
        DialogAddLocation addDialog = new DialogAddLocation(null, places, users);
//        Organization organization = orgEditDialog.getOrg();
//        if (organization != null) {
//            data.add(organization);
//            data.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
//        }
    }

    private void dataFilterView(ObservableList<Organization> dataFilter) {
        Stage view = new Stage();
        ListView<Organization> dataFilterView = new ListView<>(dataFilter);
        dataFilterView.setStyle("-fx-font-size: 20px;");
        Button ok = new Button("Ok");
        ok.setOnAction(e -> view.close());
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(dataFilterView, ok);
        view.setScene(new Scene(root, 200, 450));
        view.show();
    }

    @Override
    public void stop() {
        try {
            PrintWriter out = new PrintWriter("data.txt");
            for (Organization org : data)
                out.println(org.getName() + " " + org.getBossName() + " " + org.getPersonnel());
            out.close();
        } catch (IOException e) {
            System.out.println("Error input data");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

