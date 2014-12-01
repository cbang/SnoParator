/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snoparator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Christian
 */
public class Logic {

    private File subsetA;
    private File subsetB;
    private ArrayList<Subset> subsets;
    private Data dataInstance;
    
    public Logic()
    {
        dataInstance = new Data();
    }

    public Logic(File subsetA, File subsetB) {
        this.subsetA = subsetA;
        this.subsetB = subsetB;
        subsets = new ArrayList<Subset>();
        dataInstance = new Data();
    }
    
    public String validateInputXML(String subsetPath)
    {
        String result;
        result = "";
        
        String editedPathXML = subsetPath.replace("\\", "/");
        InputStream XSDFile = this.getClass().getClassLoader().getResourceAsStream("xml/subset.xsd");
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        
        try
        {
            Schema schema = factory.newSchema(new StreamSource(XSDFile));
            Validator validator = schema.newValidator(); //Load schema from /xml folder, and make a validator instance
            InputStream subsetXML = new FileInputStream(editedPathXML);
            validator.validate(new StreamSource(subsetXML));
            result = "true";
        }
        catch(Exception ex)
        {
            result = ex.toString();
        }
                
        return result;
    }

    public String validateInputs(String pathSubsetA, String pathSubsetB) {
        String result = "";
        String subAResult = validateInputXML(pathSubsetA);
        String subBResult = validateInputXML(pathSubsetB);
        
        if(subAResult=="true"&&subBResult=="true")
        {
            result="true";
        }
                        
        if(subAResult!="true")
        {
            result="Error in Subset A:\n"+subAResult+"\n";
        }
        
        if(subBResult!="true")
        {
            result=result+"Error in Subset B:\n"+subBResult;
        }
        
        return result;
    }
    
    public String loadXMLData(String subsetPath)
    {
        String successful = "";
        String editedPathXML = subsetPath.replace("\\", "/");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try
        {
            SAXParser saxParser = factory.newSAXParser();
            InputStream xmlInput = new FileInputStream(editedPathXML);
            //Instantiation of new SaxHandler handler instance for SAX Operation, which listens for the things 
            //we want to extract from the xml. We also pass a reference to the handler, so we can pass back 
            //information obtained (Subsets in object-model form) to this logic instance. This is done through
            //the addSubset method below
            DefaultHandler handler  = new SaxHandler(this); 
            saxParser.parse(xmlInput, handler); //Parses the XML file, and adds subset to the logic instance subset-buffer
            successful = "true";
        }
        
        catch(Exception ex)
        {
            System.out.println(ex.toString());
            successful = ex.toString();
        }
        
        return successful;
    }
    
    public void addSubset(Subset subset)
    {
        subsets.add(subset);
    }
    
    public ArrayList<Relationship> getTransitiveClosure(Long sctID)
    {
        ArrayList<Relationship> result = dataInstance.getTransitiveClosure(sctID);
        return result;
    }

    /**
     * @return the subsetA
     */
    public File getSubsetA() {
        return subsetA;
    }

    /**
     * @param subsetA the subsetA to set
     */
    public void setSubsetA(File subsetA) {
        this.subsetA = subsetA;
    }

    /**
     * @return the subsetB
     */
    public File getSubsetB() {
        return subsetB;
    }

    /**
     * @param subsetB the subsetB to set
     */
    public void setSubsetB(File subsetB) {
        this.subsetB = subsetB;
    }

}
