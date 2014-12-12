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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
    private ObservableList<String> subAExpressions;
    private ObservableList<String> subBExpressions;
    
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
    
    @FXML
    private Label label_explaination;

    @FXML
    private Label label_rankedList;
    
    @FXML
    private Label label_subsetAList;
     
    @FXML
    private Label label_subsetBList;
    
    @FXML
    private ListView<String> listview_RankedList;
    
    @FXML
    private ListView<String> listview_subsetAList;
    
    @FXML
    private ListView<String> listview_subsetBList;
    
    @FXML
    private TableView<TableData> tableView_overlaps;
    
    @FXML
    private TableColumn<TableData, String> collumn_expA;
    
    @FXML
    private TableColumn<TableData, String> collumn_expB;
    
    @FXML
    private TableColumn<TableData, String> collumn_RQ;
    
    
    ////////////////    Initialization of controller class    //////////////////
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        defaultDirectory = new File("C:/Users/Christian/Dropbox/Snomed-CT Msc. 1 semester/SnoParator/SnoParator/src/xml"); //Set of default directorty
        makeLabelsInvisible(); //All labels are made invisible
        visibilityBeforeComparison(); //All stuff in the comparison side is made invilsible
        logic = new Logic(); //Instantiation of logic layer
        fileChooserAOpen = false;
        fileChooserBOpen = false;
        listview_subsetBList.setMouseTransparent(false); //Makes B-subset list not interactable
        listview_subsetBList.setFocusTraversable(true);
        //Association of table values
        collumn_expA.setCellValueFactory(new PropertyValueFactory<TableData, String>("expAFcsId"));
        collumn_expB.setCellValueFactory(new PropertyValueFactory<TableData, String>("expBFcsId"));
        collumn_RQ.setCellValueFactory(new PropertyValueFactory<TableData, String>("RQ"));
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
    
    public void visibilityBeforeComparison()
    {
        label_explaination.setVisible(false);
        label_rankedList.setVisible(false);
        label_subsetAList.setVisible(false);
        label_subsetBList.setVisible(false);
        tableView_overlaps.setVisible(false);
        listview_subsetAList.setVisible(false);
        listview_subsetBList.setVisible(false);
       
    }
    
    public void visibilityAfterComparison()
    {
        label_explaination.setVisible(true);
        label_rankedList.setVisible(true);
        label_subsetAList.setVisible(true);
        label_subsetBList.setVisible(true);
        tableView_overlaps.setVisible(true);
        listview_subsetAList.setVisible(true);
        listview_subsetBList.setVisible(true);
               
    }
    
    private void updateObservables() //Ved kald af denne metode, opdateres observable arraylists, som er baseret på ArrayLists
    {
        ArrayList<Subset> subsets = logic.subsets;
        ArrayList<String> subAStrings = new ArrayList<String>();
        ArrayList<String> subBStrings = new ArrayList<String>();
        
        for(int i=0;i<subsets.get(0).normForms.size();i++)
        {
            subAStrings.add(String.valueOf(subsets.get(0).normForms.get(i).getFcsId()));
        }
        for(int i=0;i<subsets.get(1).normForms.size();i++)
        {
            subBStrings.add(String.valueOf(subsets.get(1).normForms.get(i).getFcsId()));
        }
        
        subAExpressions = FXCollections.observableArrayList(subAStrings); //makes a observable list
        subBExpressions = FXCollections.observableArrayList(subBStrings); 
        listview_subsetAList.setItems(subAExpressions); 
        listview_subsetBList.setItems(subBExpressions);
        
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
    
    private void updateTableData(int subASelection)
    {
        
        ArrayList<Subset> subsets = logic.subsets;
        int subASize = subsets.get(0).normForms.size();
        int subBSize = subsets.get(1).normForms.size();
        ArrayList<Comparison> comparisonResults = logic.results;
       // ArrayList<String> subAStrings = new ArrayList<String>();
        //ArrayList<String> subBStrings = new ArrayList<String>();
        //ArrayList<Expression> subA = logic.subsets.get(0).normForms;
        //ArrayList<Expression> subB = logic.subsets.get(1).normForms;
        ObservableList<TableData> results = FXCollections.observableArrayList();
//        for(int i=0;i<subBSize;i++) //To make sure that you get the same amount of As as Bs
//        {
//            subAStrings.add(String.valueOf(subsets.get(subASelection).normForms.get(i).getFcsId()));
//        }
        
        int intervalSize = subBSize; //The amount of comparison results we have to take out is equal to the size of B
        int startPoint = subASelection*intervalSize; //The starting point where we begin to take comparisons, are
        
        if(subASize == 1) //However, if the subset A size is only 1, the startindex has to be zero
        {
            startPoint = 0; 
        }
        
        for(int i=startPoint;i<(startPoint+subBSize);i++)
        {
            TableData t = new TableData(String.valueOf(comparisonResults.get(i).getExpA().getFcsId()),String.valueOf(comparisonResults.get(i).getExpB().getFcsId()),String.valueOf(comparisonResults.get(i).getResultQualifier()));
            results.add(t);
        }
         
        tableView_overlaps.setItems(results);
        
    }
    
    private void clearTable()
    {
        
    }
        
    
    ////////////////    FXML handler methods    //////////////////
    
    @FXML public void handleMouseClickInSubsetAList(MouseEvent arg0) { //Event handler for click in listView A
        clearTable();
               
        int selectedItem = listview_subsetAList.getSelectionModel().getSelectedIndex();
        
        updateTableData(selectedItem);
        System.out.println(selectedItem);
    }

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
                    logic.compareSubsets();
                    visibilityAfterComparison(); //Makes stuff visible
                    updateObservables();
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
