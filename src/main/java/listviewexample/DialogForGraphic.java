package listviewexample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class DialogForGraphic {
    private List<Place> places;
    private Font font = Font.font(16);
    private ComboBox<Place> placeChoose;
    private GridPane root;
    private Stage dialog;
    private Place place;

    public DialogForGraphic(List<Place> places) {
        this.places = places;
        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        createPlaceChoose();
        createButtons();
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Choose place");
        Scene scene = new Scene(root, 500, 300);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void createPlaceChoose() {
        Label place = new Label("Place:");
        place.setFont(font);
        root.add(place, 0, 2);
        placeChoose = new ComboBox<>();
        placeChoose.setStyle("-fx-font-size: 15 pt");
        placeChoose.getItems().addAll(places);
        placeChoose.setValue(places.get(0));
        root.add(placeChoose, 1, 2);
    }

    private void createButtons() {
        Button btnOk = new Button("Ok");
        btnOk.setFont(font);
        root.add(btnOk, 0, 6);
        btnOk.setOnAction((
                ActionEvent e) -> {
            place = placeChoose.getValue();
        });
        Button btnCancel = new Button("Cancel");
        btnCancel.setFont(font);
        root.add(btnCancel, 1, 6);
        btnCancel.setOnAction((ActionEvent e) -> {
            place = null;
            dialog.close();
        });
    }
}
