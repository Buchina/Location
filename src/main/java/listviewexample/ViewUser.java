package listviewexample;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewUser {
    private User user;
    private StackPane pane;

    public ViewUser() {
        createPane();
    }


    private void createPane() {
        pane = new StackPane();
        pane.setPadding(new Insets(5));
        Rectangle rect = new Rectangle(150, 120);
        rect.setFill(Color.PEACHPUFF);
        rect.setStrokeWidth(3);
        pane.getChildren().add(rect);
        Text textPet = new Text();
        textPet.setFont(Font.font(16));
        pane.getChildren().add(textPet);
    }

    public void setUser(User user) {
        this.user = user;
        String placeName = "Not Found";
        Date currentDate = new Date();
        DateFormat formatter = new SimpleDateFormat("d.MM.yy H:m");
        for (int i = user.getLocations().size() - 1; i >= 0; i--) {
            if (user.getLocations().get(i).getPlace().getName() != "notPlace") {
                placeName = user.getLocations().get(i).getPlace().getName();
                currentDate = user.getLocations().get(i).getDate();
                break;
            }
        }
        ((Text) pane.getChildren().get(1)).setText("The last place:\n"
                + placeName + "\n"
                + formatter.format(currentDate));
    }

    public StackPane getPane() {
        return pane;
    }
}
