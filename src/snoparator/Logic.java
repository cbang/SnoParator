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
    
    ////////////////// CLASS PARAMETERS //////////////////
    
    private File subsetA;
    private File subsetB;
    private ArrayList<Subset> subsets;
    private Data dataInstance;
    private ArrayList<Comparison> results;
    private ArrayList<Relationship> relBuffer;
    private ArrayList<Relationship> uniqueRelationships;
    
    ////////////////// CONSTRUCTORS //////////////////
    
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
    
    ////////////////// GET AND SET METHODS //////////////////
    
    public void addSubset(Subset subset)
    {
        subsets.add(subset);
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
    
    ////////////////// VALIDATION METHODS //////////////////
    
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
    
    ////////////////// XML LOADING METHODS //////////////////
    
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
    
    ////////////////// COMPARISON METHODS //////////////////
    
    public void compareSubsetsNrTwo()
    {
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
        
        for (int i = 0; i<subASize; i++)
        { 
            for (int j = 0; j<subBSize; j++)
            {
                //We get the normforms that have to be compared from the subsets:
                Normform nfA = subA.normForms.get(i);
                Normform nfB = subB.normForms.get(j);
                                
                //Now, NODE A1 (FcsId's the same?):
                if(compareFcsIds(nfA,nfB) == true) //FLOW: A1
                {
                    //Find the amount of unique matches possible:
                    int maxMatches = maxUniqueMatches(nfA,nfB);
                    //Find the amount of attribute matches (only attributes):
                    int attributeMatches = attributeMatches(nfA,nfB);
                    
                    //Then, NODE A2 (Are all attributes the same?)
                    if(sameAttributesOnly(nfA,nfB) == true)
                    {
                        //Find the amount of relationship matches (attributes + destination concept match):
                        int relationshipMatches = RelationshipMatches(nfA,nfB);
                        
                        //Then NODE A3 (Are all destination concepts the same?)
                        if(allDestConceptsSame(relationshipMatches,maxMatches) == true)
                        {
                            //THEN RESULTQUALIFIER 1:
                            //The resultClassifier is 1, which indicates a full match
                            Comparison c = new Comparison(nfA,nfB,1,maxMatches,relationshipMatches,0,new ArrayList<Relationship>(),false);
                            results.add(c);
                            System.out.println("Result with classifier 1 added");
                        }
                        else //We procede to NODE A4 (PERFORM SUBSUMPTION)
                        {
                            ArrayList<Relationship> subsumpMatchesList = subSumptionDestConceptMatches(nfA, nfB);
                            
                            if (subsumpMatchesList.size() > 0) //FLOW: A4 - YES!
                            {
                                int noOfsubsumpMatches = subsumpMatchesList.size()/2; //Relationships are pairwise
                                //THEN RESULTQUALIFIER 2, indicating that all attributes are alike, but there is granularity present
                                //for destination concepts
                                Comparison c = new Comparison(nfA,nfB,2,maxMatches,relationshipMatches,noOfsubsumpMatches,subsumpMatchesList,false);
                                results.add(c);
                                System.out.println("Result with classifier 2 added");
                            }
                            
                            else //FLOW: A4 - NO!
                            {
                                //THEN RESULTQUALIFIER 3, indicating that all attributes are alike, but there is no granularity present
                                //for destination concepts
                                int noOfsubsumpMatches = subsumpMatchesList.size()/2; //Relationships are pairwise
                                Comparison c = new Comparison(nfA,nfB,3,maxMatches,relationshipMatches,noOfsubsumpMatches,subsumpMatchesList,false);
                                results.add(c);
                                System.out.println("Result with classifier 3 added");
                            }
                        }
                        
                        
                    }
                    else //We procede to node A5 (Attributes not all the same)
                    {
                        //Are all destination concepts the same?) Find the matching relationships
                        int relationshipMatches = RelationshipMatches(nfA,nfB);
                        //FLOW: A5
                        if(sameDestConceptsOnly(nfA,nfB) == true)
                        {
                            Comparison c = new Comparison(nfA,nfB,4,maxMatches,relationshipMatches,0,new ArrayList<Relationship>(),false);
                            results.add(c);
                            System.out.println("Result with classifier 4 added");
                        }
                        
                        else //PROCEDE WITH NODE A6 (PERFORM SUBSUMPTION)
                        {
                            ArrayList<Relationship> subsumpMatchesList = subSumptionDestConceptMatches(nfA, nfB);
                            
                            if (subsumpMatchesList.size() > 0)  //FLOW: A6 - YES!
                            {
                                int noOfsubsumpMatches = subsumpMatchesList.size()/2; //Relationships are pairwise
                                //THEN RESULTQUALIFIER 5, indicating that all attributes are not alike, and there is granularity present
                                //for destination concepts
                                Comparison c = new Comparison(nfA,nfB,5,maxMatches,relationshipMatches,noOfsubsumpMatches,subsumpMatchesList,false);
                                results.add(c);
                                System.out.println("Result with classifier 5 added");
                            }
                            
                            else                                //FLOW: A6 - NO!
                            {
                                //THEN RESULTQUALIFIER 6, indicating that all attributes are not alike, and there is no granularity present
                                //for destination concepts
                                int noOfsubsumpMatches = subsumpMatchesList.size()/2; //Relationships are pairwise
                                Comparison c = new Comparison(nfA,nfB,6,maxMatches,relationshipMatches,noOfsubsumpMatches,subsumpMatchesList,false);
                                results.add(c);
                                System.out.println("Result with classifier 6 added");
                            }
                        }   
                         
                        
                    }
                    
                    
                
                }
                else //We procede to node B1
                {
                    
                }
            }
                
                
            }
        
            for(int m = 0; m < results.size(); m++)
            {
                System.out.println(results.get(m).myToString());
            }
        
        }
        
    public boolean sameAttributesOnly(Normform nfA , Normform nfB)
    {
        boolean result = false;
        //This method finds out, if the two normforms, have the samme attributes
        //in an equal number in both normForms
        int attributeMatches = attributeMatches(nfA,nfB);
        //Then find the total amount of relationships
        
        //Buffer A and B:
        ArrayList<Long> a = new ArrayList<Long>();
        a.add(1L); //So we can iterate
        ArrayList<Long> b = new ArrayList<Long>();
        b.add(1L); //So we can iterate
        boolean foundMatchFlag = false;
              
        for (int i = 0; i<nfA.getRelationships().size(); i++)
        {
            for (int m = 0; m<a.size() ; m++)
            {
                if(nfA.getRelationships().get(i).getAttributeId() == a.get(m))
                {
                    foundMatchFlag = true;
                }
            }
            
            if (foundMatchFlag == false)
            {
                a.add(nfA.getRelationships().get(i).getAttributeId());
            }
            foundMatchFlag = false;
        }
        
        for (int i = 0; i<nfB.getRelationships().size(); i++)
        {
            for (int m = 0; m<b.size() ; m++)
            {
                if(nfB.getRelationships().get(i).getAttributeId() == b.get(m))
                {
                    foundMatchFlag = true;
                }
            }
            
            if (foundMatchFlag == false)
            {
                b.add(nfB.getRelationships().get(i).getAttributeId());
            }
            foundMatchFlag = false;
        }
        
        a.remove(0); //Init stuff removed
        b.remove(0);
        
        //now we have two buffers with only unique values of attributes from both normalforms
        //now we have to see, if the attributes in a can be found in b
        
        boolean allPresent = true;
        boolean foundFlag = false;
        
        for (int i = 0; i<a.size(); i++)
        {
            for (int j = 0; j<b.size(); j++)
            {
                if(a.get(i).longValue()==b.get(j).longValue())
                {
                    foundFlag = true;
                }
                
                if(foundFlag == false)
                {
                allPresent = false;
                }
                 
            }
            foundFlag = false;
            
        }
                
        if (a.size() == b.size() && allPresent == true)
        {
            result = true;
        }
        return result;
    }
    
    public boolean sameDestConceptsOnly(Normform nfA , Normform nfB)
    {
        boolean result = false;
        //This method finds out, if the two normforms, have the samme destination Concepts
        //in an equal number in both normForms
        int attributeMatches = attributeMatches(nfA,nfB);
        //Then find the total amount of relationships
        
        //Buffer A and B:
        ArrayList<Long> a = new ArrayList<Long>();
        a.add(1L); //So we can iterate
        ArrayList<Long> b = new ArrayList<Long>();
        b.add(1L); //So we can iterate
        boolean foundMatchFlag = false;
              
        for (int i = 0; i<nfA.getRelationships().size(); i++)
        {
            for (int m = 0; m<a.size() ; m++)
            {
                if(nfA.getRelationships().get(i).getDestinationId() == a.get(m))
                {
                    foundMatchFlag = true;
                }
            }
            
            if (foundMatchFlag == false)
            {
                a.add(nfA.getRelationships().get(i).getDestinationId());
            }
            foundMatchFlag = false;
        }
        
        for (int i = 0; i<nfB.getRelationships().size(); i++)
        {
            for (int m = 0; m<b.size() ; m++)
            {
                if(nfB.getRelationships().get(i).getDestinationId() == b.get(m))
                {
                    foundMatchFlag = true;
                }
            }
            
            if (foundMatchFlag == false)
            {
                b.add(nfB.getRelationships().get(i).getDestinationId());
            }
            foundMatchFlag = false;
        }
        
        a.remove(0); //Init stuff removed
        b.remove(0);
        
        //now we have two buffers with only unique values of destination Concepts from both normalforms
        //now we have to see, if the destination concepts in a can be found in b
        
        boolean allPresent = true;
        boolean foundFlag = false;
        
        for (int i = 0; i<a.size(); i++)
        {
            for (int j = 0; j<b.size(); j++)
            {
                if(a.get(i).longValue()==b.get(j).longValue())
                {
                    foundFlag = true;
                }
                
                if(foundFlag == false)
                {
                allPresent = false;
                }
                 
            }
            foundFlag = false;
            
        }
                
        if (a.size() == b.size() && allPresent == true)
        {
            result = true;
        }
        return result;
    }
    
    public boolean allDestConceptsSame(int relationshipMatch, int maxMatches)
    {
        boolean result = false;
        
        if(relationshipMatch == maxMatches)
        {
            result = true;
        }
        return result;
    }
    
    public boolean compareFcsIds(Normform nfA, Normform nfB)
    {
        boolean result = false;
        
        if(nfA.getFcsId()==nfB.getFcsId())
        {
            result = true;
        }
        
        return result;
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
            //Method returns the amount of matches in both attributes and relationships combined
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
    
    public ArrayList<Relationship> subSumptionDestConceptMatches(Normform normformA, Normform normformB)
            //Method returns the pair-wise relationships, where two of relationships have the same
            //attributes, but two granulated destination concepts with a leap of 1 in subsumption
            //Pairwise: first two are a pair, 3-4 is a pair and so on...
    {
       
        int NoOfRelNormformA = normformA.getRelationships().size(); //Get the count of relationships in each normform
        int NoOfRelNormformB = normformB.getRelationships().size();
        ArrayList<Relationship> granularityResults = new ArrayList<Relationship>(); // list for telling how many subsumption matches there are
        
        for (int i = 0; i<NoOfRelNormformA; i++)
        { 
                for (int j = 0; j<NoOfRelNormformB; j++)
                {
                    //Get the relationships:
                    Relationship a = normformA.getRelationships().get(i);
                    Relationship b = normformB.getRelationships().get(j);
                    
                        //We check if the destination concept-Id's are the same, if not, we perfom subsumption
                        Long destA = a.getDestinationId();
                        Long destB = b.getDestinationId();
                        if(destA != destB)
                        {
                            //We then get the subsumption results for this concept in normform b:
                            ArrayList<Long> resultSet = dataInstance.getSubsumption(destB,1);
                            
                            for(int m = 0; m<resultSet.size(); m++)
                            {   //This list we compare, to see, if there are granulated concepts to the
                                //destination b concept (resultSet), with respect to a:
                                long r = resultSet.get(m).longValue();
                                if(r==destA)
                                {
                                    granularityResults.add(a);
                                    granularityResults.add(b);
                                } 
                            }
                        }
                    
                    
                }
        }
        
        return granularityResults;
    }
    
    public int maxUniqueMatches(Normform normformA, Normform normformB)
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
    
    ////////////////// DATA-LAYER METHODS //////////////////
    
    public ArrayList<Relationship> getTransitiveClosure(Long sctID)
    {
        ArrayList<Relationship> result = dataInstance.getTransitiveClosure(sctID);
        return result;
    }
    
    public ArrayList<Long> getSubsumption(Long sctID, int steps)
    {
        ArrayList<Long> result = dataInstance.getSubsumption(sctID, steps);
        return result;
    }

}
