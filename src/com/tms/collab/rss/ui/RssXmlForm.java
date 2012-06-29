package com.tms.collab.rss.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import kacang.ui.Event;
import kacang.ui.Widget;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class RssXmlForm extends Widget{
	// XML Settings
	private static final String RSSVERSION = "2.0";	
	private static final String XML_VERSION = "1.0";
	private static final String XML_ENCODING = "iso-8859-2";

	
	private static final String TAG_RSS = "rss";
	private static final String TAG_CHANNEL = "channel";
	private static final String TAG_ITEM = "item";
	private static final String TAG_TITLE = "title";
	private static final String TAG_LINK = "link";	
	private static final String TAG_DESCRIPTION = "description";
	
	String channelId;
	Document dom;
	
	public RssXmlForm(){
		System.out.println("Started .. ");
		createDocument();
		generateData();
		System.out.println("Generated file successfully.");
	}

/*	
	public void init(){
		System.out.println("Started .. ");
		createDocument();
		generateData();
		System.out.println("Generated file successfully.");
	}
	          
	*/

	
	private void createDocument() {
		//get an instance of factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
		//get an instance of builder
		DocumentBuilder db = dbf.newDocumentBuilder();
		//create an instance of DOM
		dom = db.newDocument();
		
		}catch(ParserConfigurationException pce) {
			//dump it
			System.out.println("Error while trying to instantiate DocumentBuilder " + pce);
			System.exit(1);
		}
	}	
	
	                   
    public void generateData() {
    	Element rssEle = dom.createElement(TAG_RSS);
    	rssEle.setAttribute("version", "2.0");
    	
    	//channel
    	Element channelEle = dom.createElement(TAG_CHANNEL);
    	Element channeltitleEle = dom.createElement(TAG_TITLE);
    	channeltitleEle.appendChild(dom.createTextNode("SMJK"));
    	channelEle.appendChild(channeltitleEle);

    	Element channellinkEle = dom.createElement(TAG_LINK);
    	channellinkEle.appendChild(dom.createTextNode("SMJK"));
    	channelEle.appendChild(channellinkEle);
    	
    	Element channeldescEle = dom.createElement(TAG_DESCRIPTION);
    	channeldescEle.appendChild(dom.createTextNode("SMJK"));
    	channelEle.appendChild(channeldescEle);
    	
    	rssEle.appendChild(channelEle);
    	dom.appendChild(rssEle);
    	
    	
    	
    	
    	
    	
    	
    	//printToFile();
    	//System.out.println("verEle="+dom.appendChild(rssEle));
    }
    

	 /**
	  * This method uses Xerces specific classes
	  * prints the XML document to file.
	 */

    private void printToFile(){
		try
		{
			//print
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);
			format.setEncoding(XML_ENCODING);
			format.setVersion(XML_VERSION);
			
			//to generate output to console use this serializer
			//XMLSerializer serializer1 = new XMLSerializer(System.out, format);

			//to generate a file output use fileoutputstream instead of system.out
			XMLSerializer serializer = new XMLSerializer(
			new FileOutputStream(new File("tablix1.xml")), format);
			
			//serializer1.serialize(dom);
			serializer.serialize(dom);
			//validateSchema("http://www.tablix.org/releases/dtd/tablix2r0.dtd","tablix.xml");

		} catch(IOException ie) {
		    ie.printStackTrace();
		}
	}

    
	public void onRequest(Event evt) {
		channelId = evt.getRequest().getParameter("channelId");
		//System.out.println("Started .. ");
		//generateData();
		//System.out.println("Generated file successfully.");
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	

	
}
