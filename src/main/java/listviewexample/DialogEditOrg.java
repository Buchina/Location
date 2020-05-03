package listviewexample;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogEditOrg {
    private  Organization org;
    private Stage dialog;
    private Font font = Font.font(16);;
    private GridPane root;
    private TextField nameEdit, nameBossEdit;
    private Spinner<Integer> personnelEdit;

    public DialogEditOrg (Organization org) {
        this.org = org;
        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        createNameEdit();
        createPerssonalEdit();
        createButtons();
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit organization");
        Scene scene = new Scene(root, 400, 300);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public Organization getOrg(){
        return org;
    }

    private void createNameEdit() {
        Label nameOrg = new Label("Name:");
        nameOrg.setFont(font);
        root.add(nameOrg, 0, 0);
        nameEdit = new TextField();
        root.add(nameEdit, 1, 0);

        Label nameBoss = new Label("Name boss:");
        nameBoss.setFont(font);
        root.add(nameBoss, 0, 1);
        nameBossEdit = new TextField();
        root.add(nameBossEdit, 1, 1);

        if(org!=null) {
            nameEdit.setText(org.getName());
            nameBossEdit.setText(org.getBossName());
        }
    }

    private void createPerssonalEdit() {
        Label personnelOrg = new Label("Personnel:");
        personnelOrg.setFont(font);
        root.add(personnelOrg, 0, 2);
        if(org!= null)
            personnelEdit = new Spinner(0, 100, org.getPersonnel());
        else
            personnelEdit = new Spinner(0, 100, 0);
        root.add(personnelEdit, 1, 2);
    }

    private void createButtons() {
        Button btnOk = new Button("Ok");
        btnOk.setFont(font);
        root.add(btnOk, 0, 5);
        btnOk.setOnAction((ActionEvent e) -> {
            if (isInputValid()) {
                if(org==null)
                    org = new Organization(nameEdit.getText(), nameBossEdit.getText(), personnelEdit.getValue());
                else {
                    org.setName(nameEdit.getText());
                    org.setBossName(nameBossEdit.getText());
                    org.setPersonnel(personnelEdit.getValue());
                }
                dialog.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Data entry error");
                alert.setHeaderText(null);
                alert.setContentText("The name of organization consists of letters, numbers, spaces, +, -!!!\n" +
                        "The name of the boss begins with a capital letter!!!");
                alert.showAndWait();
            }
        });
        Button btnCancel = new Button("Cancel");
        btnCancel.setFont(font);
        root.add(btnCancel, 1, 5);
        btnCancel.setOnAction((ActionEvent e) -> {
            org = null;
            dialog.close();
        });
    }

    private boolean isInputValid() {
        return nameEdit.getText().matches("[a-zA-Z0-9&\\-\\+ ]+") && nameBossEdit.getText().matches("[A-Z][a-z]*");
    }
}

