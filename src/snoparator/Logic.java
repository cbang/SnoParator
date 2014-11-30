/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snoparator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 *
 * @author Christian
 */
public class Logic {

    private File subsetA;
    private File subsetB;

    public Logic(File subsetA, File subsetB) {
        this.subsetA = subsetA;
        this.subsetB = subsetB;

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
    
    public void loadXMLData()
    {
        
    }

}
