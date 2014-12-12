 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snoparator;

import java.util.ArrayList;
import org.xml.sax.Attributes; //MUST BE ORG.XML!!! OR IT WILL NOT WORK!!!
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SaxHandler class used for handling the XML-input file
 * Through these methods, information from the XML file is extracted
 * @author Christian
 */
public class SaxHandler extends DefaultHandler
{
    
    public boolean bFcsId = false;
    public boolean bAttribute =false;
    public boolean bDestId =false;
    public String fcsIdBuffer ="";
    public ArrayList<Relationship> relationshipBuffer = new ArrayList<Relationship>();
    public ArrayList<String> attributeBuffer = new ArrayList<String>();
    public ArrayList<String> destIdBuffer = new ArrayList<String>();
    public Subset subset = new Subset();
    public int normFormCounter = 0;
    public Logic logicReference;
    
    public SaxHandler(Logic logicReference)
    {
        this.logicReference = logicReference;
    }
    
    @Override
    public void startDocument()
    {
        //System.out.println("I've begun parsing");
    }
    
    public void startElement(String uri, String localName, String qName, Attributes attributes)
    {
                
        if (qName.equalsIgnoreCase("FCSID")) {
            bFcsId = true;
        }
        
        if (qName.equalsIgnoreCase("ATTRIBUTE")) {
            bAttribute = true;
        }
        
        if (qName.equalsIgnoreCase("DESTID")) {
            bDestId = true;
        } 
    }
    
    public void characters(char ch[], int start, int length) throws SAXException {
                //When we meet FCSID element
		if (bFcsId) {
                        
                        //If the normFormCounter is zero, we increment by one, to indicate we are past the first fcsId
                        //We dont write anything the first time, but the next time we meet the fcsId element, we write what are in
                        //The buffers along with this first FcsID
                        if(normFormCounter == 0)
                        {
                            String text = new String(ch, start, length);
                            //System.out.println("Focus id : " + text);
                            fcsIdBuffer = text; //We set the buffer when we meet a FcsId element
                            fcsIdBuffer.replaceAll("\\s+",""); //Makes sure there is no whitespace
                            normFormCounter++;
                        }
                        
                        //If the normFormCounter is one, that means we have to add the first normForm
                        else
                        {
                            //Add the current relationships to the relationshipBuffer
                            for (int i = 0; i<attributeBuffer.size(); i++)
                            {
                                String attribute = attributeBuffer.get(i);
                                attribute.replaceAll("\\s+",""); //Makes sure there is no whitespace
                                Long attributeLongVal = Long.valueOf(attribute);
                                
                                String destId = destIdBuffer.get(i);
                                destId.replaceAll("\\s+",""); //Makes sure there is no whitespace
                                Long destIdLongVal = Long.valueOf(destId);
                                
                                relationshipBuffer.add(new Relationship(attributeLongVal,destIdLongVal));
                                
                            }
                            
                            //Then we add the relationships to a normform
                            Long fcsIdLongVal = Long.valueOf(fcsIdBuffer);
                            ArrayList<Relationship> rel = new ArrayList<Relationship>();
                            
                            for(int i=0;i<relationshipBuffer.size();i++)
                            {
                                rel.add(relationshipBuffer.get(i));
                            }
                            
                            Expression nf = new Expression(fcsIdLongVal,rel);
                            //Add this to the subset
                            subset.normForms.add(nf);
                            //Reset Buffers
                            relationshipBuffer.clear();
                            attributeBuffer.clear();
                            destIdBuffer.clear();
                            //Read next fcsId                            
                            String text = new String(ch, start, length);
                            fcsIdBuffer = text; //We set the buffer when we meet a FcsId element
                            //System.out.println("Focus id : " + text);
                            fcsIdBuffer.replaceAll("\\s+",""); //Makes sure there is no whitespace
                            normFormCounter++;
                        }
                                         
                            bFcsId = false; //Reset boolean flag FcsId Indicator
                            
                        }
                        //If the normFormCounter is not zero or one, we are past the first fcsId, and we add normform to subset
                
                //When we meet attribute element
		if (bAttribute) {
                        String text = new String(ch, start, length);
                        //System.out.println("Attribute : " + text);
                        attributeBuffer.add(text); //Add attribute to the buffer
                        //System.out.println("ding attribute");
			bAttribute = false;
		}
                //When we meet destId element
		if (bDestId) {
                        String text = new String(ch, start, length);
                        //System.out.println("Dest id : " + text);
                        destIdBuffer.add(text); //Add destId to the buffer
                        //System.out.println("ding destID");
			bDestId = false;
		}
 
	}
    
    @Override
    public void endDocument()
    {
       //When the end of the document is reached, we have to write the last buffer containments to the subset, 
       //since no more FcsID elements will be reached:
        
       //Add the current relationships to the relationshipBuffer
        for (int i = 0; i<attributeBuffer.size(); i++)
        {
            String attribute = attributeBuffer.get(i);
            attribute.replaceAll("\\s+",""); //Makes sure there is no whitespace
            Long attributeLongVal = Long.valueOf(attribute);

            String destId = destIdBuffer.get(i);
            destId.replaceAll("\\s+",""); //Makes sure there is no whitespace
            Long destIdLongVal = Long.valueOf(destId);

            relationshipBuffer.add(new Relationship(attributeLongVal,destIdLongVal));

        }

        //Then we add the relationships to a normform
        Long fcsIdLongVal = Long.valueOf(fcsIdBuffer);
        ArrayList<Relationship> rel = new ArrayList<Relationship>();

        for(int i=0;i<relationshipBuffer.size();i++)
        {
            rel.add(relationshipBuffer.get(i));
        }

        Expression nf = new Expression(fcsIdLongVal,rel);
        //Add this to the subset
        subset.normForms.add(nf);
        //Reset Buffers for the last time
        relationshipBuffer.clear();
        attributeBuffer.clear();
        destIdBuffer.clear();
        fcsIdBuffer="";
                            
       //System.out.println("I'm done parsing");
       //System.out.println("No of normforms in subset: "+String.valueOf(subset.normForms.size()));
       //System.out.println("Containment check: ");
       for (int i = 0; i<subset.normForms.size(); i++)
       {
           Expression normform = subset.normForms.get(i);

           for (int k = 0; k < normform.getRelationships().size(); k++)
           {
               //System.out.println("Attribute: "+String.valueOf(normform.getRelationships().get(k).getAttributeId())+" | destId: "+String.valueOf(normform.getRelationships().get(k).getDestinationId()));
           }
       }
       
       logicReference.addSubset(subset);
    }
}