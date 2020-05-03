package listviewexample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class GraphicWindow {
    private GridPane root;
    private Stage dialog;
    private Place place;
    private Font font = Font.font(16);

    public GraphicWindow(Place place) {
        this.place = place;
        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        createPie();
        createButton();
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Graphic of place");
        Scene scene = new Scene(root, 600, 550);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private Group createPie() {
        Group groupChart = new Group();
        DateFormat formatter = new SimpleDateFormat("d.MM.yy H:m");
        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList();
        for (Frequency i : place.getFrequency()) {
            pieChartData.add(new PieChart.Data(formatter.format(i.getDate()), i.getCount()));
        }
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle(place.getName());
        chart.setLegendSide(Side.LEFT);
        groupChart.getChildren().add(chart);
        root.add(groupChart, 0, 1);
        return groupChart;
    }

    private void createButton() {
        Button btnCancel = new Button("Cancel");
        btnCancel.setFont(font);
        root.add(btnCancel, 1, 6);
        btnCancel.setOnAction((ActionEvent e) -> {
            place = null;
            dialog.close();
        });
    }
}
