package listviewexample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class SortedPlacesWindow {
    private GridPane root;
    private Stage dialog;
    private ObservableList<Place> places = FXCollections.observableArrayList();
    private Font font = Font.font(16);
    private ListView<Place> placeView;

    public SortedPlacesWindow(List<Place> places) {
        this.places.addAll(places);
        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        createList();
        createButton();
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Top places (in descending order)");
        Scene scene = new Scene(root, 400, 550);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private ListView<Place> createList() {
        places.sort((o1, o2) -> -Integer.compare(o1.getCount(), o2.getCount()));
        placeView = new ListView<>(places);
        placeView.setStyle("-fx-font-size: 20px;");
        root.add(placeView, 0, 1);
        return placeView;
    }

    private void createButton() {
        Button btnCancel = new Button("Cancel");
        btnCancel.setFont(font);
        root.add(btnCancel, 1, 6);
        btnCancel.setOnAction((ActionEvent e) -> {
            places = null;
            dialog.close();
        });
    }
}
