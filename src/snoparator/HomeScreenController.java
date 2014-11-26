/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snoparator;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Christian
 */
public class HomeScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //All labels are made invisible
        label_filenameA.setText("");
        label_filenameB.setText("");        
    }
    
     @FXML
    private Label label_filenameA;

    @FXML
    private Button btn_selectFileSubA;

    @FXML
    private Button btn_selectFileSubB;

    @FXML
    private Label label_filenameB;

    @FXML
    void selectSubsetAPressed(ActionEvent event) {
        label_filenameA.setText("Lolburgerfarts");
    }

    @FXML
    void selectSubsetBPressed(ActionEvent event) {
        label_filenameB.setText("Much wow in this program");
    }
    
}
