package listviewexample;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class MapWindow {
    private GridPane root;
    private Stage dialog;
    private User user;
    private Font font = Font.font(16);

    public MapWindow(User user) {
        this.user = user;
        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        createMap();
        createButton();
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Map of user");
        Scene scene = new Scene(root, 250, 200);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void createMap() {
        Canvas canvas = new Canvas(100, 100);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.VIOLET);
        gc.beginPath();
        gc.moveTo(Double.valueOf(user.getLocations().get(0).getX()), Double.valueOf(user.getLocations().get(0).getY()));
        for (int i = 1; i < user.getLocations().size(); i++) {
            gc.lineTo(Double.valueOf(user.getLocations().get(i).getX()), Double.valueOf(user.getLocations().get(i).getY()));
        }
        gc.stroke();
        root.add(canvas, 0, 1);
    }

    private void createButton() {
        Button btnCancel = new Button("Cancel");
        btnCancel.setFont(font);
        root.add(btnCancel, 0, 2);
        btnCancel.setOnAction((ActionEvent e) -> {
            user = null;
            dialog.close();
        });
    }
}
