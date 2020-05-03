package listviewexample;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ViewOrg {
    private  Organization org;
    private StackPane pane;

    public ViewOrg () {
        createPane();
    }

    private  void createPane(){
        pane = new StackPane();
        pane.setPadding(new Insets(5));
        Rectangle rect = new Rectangle(150, 120);
        rect.setFill(Color.AQUA);
        rect.setStroke(Color.DARKBLUE);
        rect.setStrokeWidth(3);
        pane.getChildren().add(rect);
        Text textOrg = new Text();
        textOrg.setFont(Font.font(16));
        pane.getChildren().add(textOrg);
    }

    public void setOrganization(Organization org) {
        this.org = org;
        ((Text)pane.getChildren().get(1)).setText(org.getName()+"\n"+org.getBossName()+"\n"+org.getPersonnel());
    }

    public StackPane getPane () {
        return pane;
    }

}

