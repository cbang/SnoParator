/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dialogs;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Christian
 */
public class ErrorDialogController implements Initializable {

    private String errorMessage;
            
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleErrorLabel.wrapTextProperty();
    }
    
    @FXML
    private Button okBtn;

    @FXML
    private Label errorLabel;
    
    @FXML
    private Label titleErrorLabel;

    @FXML
    void okBtnPressed(ActionEvent event) {
        closeDialog();
    }
    
    public void setErrorLabel(String errorMessage)
    {
        errorLabel.setText(errorMessage);
    }
    
    public void setErrorTitleLabel(String errorMessage)
    {
        titleErrorLabel.setText(errorMessage);
    }
    
    public void closeDialog()
    {
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }
    
}
