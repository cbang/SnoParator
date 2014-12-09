/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snoparator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
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
    private ArrayList<Comparison> results;
    private ArrayList<Relationship> relBuffer;
    private ArrayList<Relationship> uniqueRelationships;
    
    public Logic()
    {
        subsets = new ArrayList<Subset>();
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
            System.out.println("Validation error: " + ex);
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
            System.out.println("Load XML DATA ERROR: "+ex.toString());
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
    
    public void compareSubsets()
    {
        String result = "";
        
        if(subsets.size()==2) //First we check if there's enough subsets to compare
        {
            //Get the subsets
            Subset subA = subsets.get(0);
            Subset subB = subsets.get(1);
            //Then find out how many Normforms there are in each of the subsets
            int subASize = subA.normForms.size();
            int subBSize = subB.normForms.size();
            System.out.println("A count: "+String.valueOf(subASize));
            System.out.println("B count: "+String.valueOf(subBSize));
            int totalIterations = subASize*subBSize;
            System.out.println("Total iterations: "+String.valueOf(totalIterations));
            //Instantiate Results buffer (to hold the comparisons we are about to make)
            results = new ArrayList<Comparison>();
            
            //We have to compare all normforms in subset A with all normforms in subset B.
            //We need three counters, i for subset A, j for subset B, and then k for alle iterations that have to be made:
            //With these nested for loops, we iterate the first normform with all the other normforms, then the second normform with all 
            //other normforms and so on
            for (int i = 0; i<subASize; i++)
            { 
                for (int j = 0; j<subBSize; j++)
                {
                    //We get the normforms that have to be compared from the subsets:
                    Normform normformA = subA.normForms.get(i);
                    Normform normformB = subB.normForms.get(j);
                    //First we compare fcsId's to see if they match
                    if(normformA.getFcsId()==normformB.getFcsId())
                    {
                        //Now we have to compare all the relationships in the two normforms. This is done in two steps
                        //First, we count the matches of attributes with the attributeRelationsMatches method:
                        int attributeMatches = attributeMatches(normformA,normformB);
                        //We then find the max amount of attributeRelationship matches there can be between two normforms, with the method:
                        int totalRel = maxAttributeRelationshipMatches(normformA,normformB);
                        //We then see if all the attributes match:
                        if (attributeMatches == totalRel)
                        {   
                            //We first calculate the matches of relationships
                            int RelationshipMatches = RelationshipMatches(normformA,normformB);
                            //We then see if all the destinationsconcepts match:
                            if(RelationshipMatches == totalRel)
                            {
                                //If they do, We then make a comparison, which are added to the result
                                //The resultClassifier is 1, which indicates a full match
                                //Total relation
                                Comparison c = new Comparison(normformA,normformB,1,totalRel,RelationshipMatches,0,false);
                                results.add(c);
                            }
                            
                            else //If not all relationships are the same, we then look for granularity 
                            {
                                
                            }
                            
                        }
                        
                        else //If there is not a full match, we investigate further
                        {
                            
                        }
                    }
                    
                    else //If fcsId's are not the same, then:
                    {
                        
                    }
                }
            }
              
        }
                
        else
        {
            result = "There's not two subsets available";
        }
        
        //Print results
        for(int m = 0; m < results.size(); m++)
        {
            System.out.println(results.get(m).myToString());
        }
        
    }
    
    public int attributeMatches(Normform normformA, Normform normformB) 
            //Method returns the amount of matches in attributes
    {
        int matchingAtts = 0; //Int for telling how many attribute matches there are
        int NoOfRelNormformA = normformA.getRelationships().size(); //Get the count of relationships in each normform
        int NoOfRelNormformB = normformB.getRelationships().size();
        
        //Counter i form index of relationships in normFormA
        //Counter j for index of relationships in normFormB
        for (int i = 0; i<NoOfRelNormformA; i++)
            { 
                for (int j = 0; j<NoOfRelNormformB; j++)
                {
                    //We compare the relationships in the two normforms (both attributes and destinationId's)
                    if(normformA.getRelationships().get(i).getAttributeId()==normformB.getRelationships().get(j).getAttributeId())
                    {
                        matchingAtts++; //We count up match with one 
                    }
                }
            }
        
        return matchingAtts;
    }
    
    public int RelationshipMatches(Normform normformA, Normform normformB) 
            //Method returns the amount of matches in both attributes and relationships
    {
        int matchingRelationships = 0; //Int for telling how many attributerelationship matches there are
        int NoOfRelNormformA = normformA.getRelationships().size(); //Get the count of relationships in each normform
        int NoOfRelNormformB = normformB.getRelationships().size();
        
        //Counter i form index of relationships in normFormA
        //Counter j for index of relationships in normFormB
        for (int i = 0; i<NoOfRelNormformA; i++)
            { 
                for (int j = 0; j<NoOfRelNormformB; j++)
                {
                    //We compare the relationships in the two normforms (both attributes and destinationId's)
                    if(normformA.getRelationships().get(i).getAttributeId()==normformB.getRelationships().get(j).getAttributeId()
                            && normformA.getRelationships().get(i).getDestinationId()==normformB.getRelationships().get(j).getDestinationId())
                    {
                        matchingRelationships++; //We count up match with one 
                    }
                }
            }
        
        return matchingRelationships;
    }
    
    public int maxAttributeRelationshipMatches(Normform normformA, Normform normformB)
    {
        //The max amount of maxmatches there can be is the product of unique relationships that are compared
        relBuffer = new ArrayList<Relationship>();
        uniqueRelationships = new ArrayList<Relationship>();
        boolean flag = false;
        //First we add all the relationships from the two normforms
        for(int i = 0; i < normformA.getRelationships().size(); i++)
        {
            relBuffer.add(normformA.getRelationships().get(i));
        }
        for(int i = 0; i < normformB.getRelationships().size(); i++)
        {
            relBuffer.add(normformB.getRelationships().get(i));
        }
        //We then add only unique relationships to the uniqueRelationship buffer:
        //First, just add the first relationship in the relBuffer:
        uniqueRelationships.add(relBuffer.get(0));
        //Then we compare the relBuffer, with all the ones in the uniquebuffer:
        for(int i = 0; i<relBuffer.size(); i++) //For all the elements in relbuffer (6)
        {
            for(int m = 0; m<uniqueRelationships.size();m++) //We iterate through elements in the unique (1)
            {
                if(uniqueRelationships.get(m).getAttributeId() == relBuffer.get(i).getAttributeId()
                   && uniqueRelationships.get(m).getDestinationId() == relBuffer.get(i).getDestinationId())
                {   //If we find a match within all 
                    flag = true;
                    //System.out.println("match found");
                }
            }
            
            if(flag == false)
            {
                uniqueRelationships.add(relBuffer.get(i));
                //System.out.println("addition");
            }
            
            flag = false;
        }
        
        int maxMatches = uniqueRelationships.size(); //Return the size of the maxmatches
        uniqueRelationships.clear();
        relBuffer.clear();
        return maxMatches;
    }

}
