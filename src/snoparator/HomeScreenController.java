/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snoparator;

import dialogs.ErrorDialogController;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Christian
 */
public class HomeScreenController implements Initializable {

    ////////////////    class attributes    //////////////////
    
    private File defaultDirectory; //Used to set default directory where subsets are situated
    private File subsetAXML; //Input files
    private File subsetBXML;
    private Stage errorDialogStage;
    private Button errorDialogOkBtn;
    private Logic logic;
    private Boolean fileChooserAOpen;
    private Boolean fileChooserBOpen;
    
    ////////////////    Initialization of controller class    //////////////////
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        defaultDirectory = new File("C:/Users/Christian/Dropbox/Snomed-CT Msc. 1 semester/SnoParator/SnoParator/src/xml"); //Set of default directorty
        makeLabelsInvisible(); //All labels are made invisible
        logic = new Logic(); //Instantiation of logic layer
        fileChooserAOpen = false;
        fileChooserBOpen = false;
    }
    
    ////////////////    Class methods   //////////////////
    
    public void makeLabelsVisible()
    {
       label_filenameA.setVisible(true);
       label_filenameB.setVisible(true);
    }
    
    public void makeLabelsInvisible()
    {
       label_filenameA.setVisible(false);
       label_filenameB.setVisible(false);
    }
    
    public void showErrorDialog(String errorText) //Spawns an error dialog, with the specified errorText
    {
        try
        {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/dialogs/errorDialog.fxml")); //Instantiering af FXML-loader, der henter indhold af UpdateField.fxml
                Parent root = (Parent) loader.load(); //Indhold af .fxml fil indhentes til root
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Error");
                stage.initModality(Modality.APPLICATION_MODAL);
                // NB! Loaderens load-statement SKAL udføres før get af controller
                ErrorDialogController errorController = loader.<ErrorDialogController>getController(); //Get controller instans
                errorController.setErrorLabel(errorText); //Setter værdien af error label
                stage.show(); //Viser dialogvinduet
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
        
    ////////////////    FXML attributes (layout components)    //////////////////
    
     @FXML
    private Label label_filenameA;

    @FXML
    private Button btn_selectFileSubA;

    @FXML
    private Button btn_selectFileSubB;

    @FXML
    private Label label_filenameB;
    
    @FXML
    private Button compareBtn;
    
    @FXML
    private Button btn_test;
    
    ////////////////    FXML handler methods    //////////////////

    @FXML
    void selectSubsetAPressed(ActionEvent event) {
        
        if(fileChooserAOpen == false) //Only open filechooser if theres noone open
        {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilterXML = new FileChooser.ExtensionFilter("XML files (*.xml", "*.XML");
            fileChooser.getExtensionFilters().addAll(extFilterXML); //Only allow xml files to be selected
            fileChooser.setInitialDirectory(defaultDirectory);
            fileChooserAOpen = true;
            subsetAXML = fileChooser.showOpenDialog(null);
        
            if(subsetAXML != null)
            {
                label_filenameA.setText(subsetAXML.getName());
                label_filenameA.setVisible(true);
                fileChooserAOpen = false;
            }
            else
            {
                System.out.println("Nothing is selected");
                fileChooserAOpen = false;
            } 
        }
        else
        {
            //Do nothing
        }
        
    }

    @FXML
    void selectSubsetBPressed(ActionEvent event) {
        
        if(fileChooserBOpen == false) //Only open filechooser if theres noone open
        {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilterXML = new FileChooser.ExtensionFilter("XML files (*.xml", "*.XML");
            fileChooser.getExtensionFilters().addAll(extFilterXML); //Only allow xml files to be selected
            fileChooser.setInitialDirectory(defaultDirectory);
            fileChooserBOpen = true;
            subsetBXML = fileChooser.showOpenDialog(null);
            
            if(subsetBXML != null)
            {
                label_filenameB.setText(subsetBXML.getName());
                label_filenameB.setVisible(true);
                fileChooserBOpen = false;                
            }
            else
            {
                System.out.println("Nothing is selected");
                fileChooserBOpen = false; 
            }
        }
        else
        {
           //Do nothing 
        }
        
    }
    
    @FXML
    void compareBtnClicked(ActionEvent event) {
        //First, we check if two files are selected
        
        if(subsetAXML!=null && subsetBXML!=null)
        {
            //Set the logic subset file variables
            logic.setSubsetA(subsetAXML);
            logic.setSubsetB(subsetBXML);
            //Get file paths for the subsets 
            String pathA = subsetAXML.getPath();
            String pathB = subsetBXML.getPath();
            //Perfom validation of subset files
            String validation = logic.validateInputs(pathA, pathB);
            
            if(validation=="true") //Both files are validated successfully in accordance to the XSD, continue
            {
                String successfulParseSubsetA = logic.loadXMLData(pathA);
                String successfulParseSubsetB = logic.loadXMLData(pathB);
                
                if(successfulParseSubsetA == "true" && successfulParseSubsetB == "true") //Files were parsed correctly, and subsets are now present in the logic instance subset buffer
                {
                    System.out.println("All is good, we proceed with comparing the stuff");
                    logic.compareSubsetsNrTwo();
                    
                }
                else //If parsing of files were not successful
                {
                    if(successfulParseSubsetA != "true" && successfulParseSubsetB != "true")
                    {
                       showErrorDialog("Errors in both subsets:\n"+successfulParseSubsetA+"\n"+successfulParseSubsetB); 
                    }
                    if(successfulParseSubsetA != "true")
                    {
                       showErrorDialog("Error in subset A:\n"+successfulParseSubsetA);
                    }
                    if(successfulParseSubsetB != "true")
                    {
                       showErrorDialog("Error in subset B:\n"+successfulParseSubsetB);
                    } 
                }
                
            }
            else //Something went wrong in validation
            {
                showErrorDialog(validation);
            }
            
        }
        
        else //Files are not selected
        {
            if(subsetAXML==null && subsetBXML==null)
            {
               showErrorDialog("Please select two input subsets."); 
            }
            
            else
            {
                if(subsetAXML==null)
                {
                   showErrorDialog("Please select subset A."); 
                }
            
                if(subsetBXML==null)
                {
                   showErrorDialog("Please select subset B."); 
                }
            }    
        }
    }
    
    @FXML
    void testPressed(ActionEvent event) {
        //logic.getTransitiveClosure(31978002L); //Transitive closure result of fracture of Tibia
        //logic.getSubsumption(127909008L, 1);
        ArrayList<Long> result = logic.getSubsumption(74732009L, 1);
        
        for (int i = 0; i<result.size() ; i++)
        {
            System.out.println(String.valueOf(result.get(i).longValue()));
        }
        
    }
    
}
