package web;

import impl.ADF;
import impl.Address_Verifer;
import impl.Name_Verifer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * Servlet implementation class ScoreLead
 */
@WebServlet("/ScoreLead")
public class ScoreLead extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String addresses_file = "c://ZipCodes.csv";
	private static final String names_file = "c://Names.txt";
	
	private static final Boolean enable_debug = true;
	
	private static Address_Verifer address_verifier = null;
	private static Name_Verifer name_verifer = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScoreLead() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		
		// init verifiers
		address_verifier = new Address_Verifer(addresses_file);
		name_verifer = new Name_Verifer(names_file);
		if (enable_debug){
				System.out.println("-----------------------------------");
				System.out.println("---------- Init verifers ----------");
				System.out.println("-----------------------------------");			
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (enable_debug){
			doPost(request,response);
		}
		else{
			PrintWriter pp = response.getWriter();
	        pp.println("Check other options!");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if ( enable_debug){
			
	        org.w3c.dom.Document inXML = Generate_Fake_XML_Input ( request );
	        
	        ADF cur_adf = Generate_ADF_From_XML(inXML);
	        
	        float result = cur_adf.Calculate_Lead_Score();
	        
	        if ( enable_debug) {
	        	System.out.println("Newly generated ADF: "+cur_adf);
	        	System.out.println("Its score: " + result);
	        	//PrintWriter pp = response.getWriter();	       	        
	        	//pp.println("name is: "+result);
	        }
	        
			//Result output = new StreamResult(new File("C://output.xml"));
	        //StreamResult console = new StreamResult(System.out);	        
			
			org.w3c.dom.Document document = null;        
	        try
	        {
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();

	            document = builder.newDocument(); 

	            Element root = (Element)document.createElement("LeadScore");
	            document.appendChild(root);
	            
	            Element score = document.createElement("Score");
	            score.setTextContent(Float.toString(result));
	    		root.appendChild(score);
	    		
	    		{
	    			// debug only
	    			Transformer transformer = TransformerFactory.newInstance().newTransformer();
	    			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
	    			Result output = new StreamResult(new File("C://result.xml"));
	    			Source input = new DOMSource(document);	    			
	    			transformer.transform(input, output);	    		
	    		}    			

	    		
	    		javax.xml.transform.TransformerFactory tfactory = TransformerFactory.newInstance();
	    		javax.xml.transform.Transformer xform = tfactory.newTransformer();	    		
	    		javax.xml.transform.Source src=new DOMSource(document);	    		
	    		java.io.StringWriter writer = new StringWriter();
	    		Result out = new javax.xml.transform.stream.StreamResult(writer);
	    		xform.transform(src, out);
	    		
	    		//System.out.println(writer.toString());
	    		
	        	PrintWriter pp = response.getWriter();	       	        
	        	pp.println(writer.toString());
	        }
	        catch (Exception e) {
	              System.out.println("doPost: The following exception occurred: "+e.getMessage());
	              e.printStackTrace();
	           }	        	        
	        
			/*
			Transformer transformer = TransformerFactory.newInstance().newTransformer(); 
			Source input = new DOMSource(document);
			
			response.getOutputStream().write();
			response.getOutputStream().flush(); 
			
			transformer.transform(input, output);*/
		}
	}
	
	
	
	/* SIMPLE PARSER ROUTINE  */
	
	protected static Node getNode(String tagName, NodeList nodes) {
	    for ( int x = 0; x < nodes.getLength(); x++ ) {
	        Node node = nodes.item(x);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            return node;
	        }
	    }
	 
	    return null;
	}
	 
	protected String getNodeValue( Node node ) {
	    NodeList childNodes = node.getChildNodes();
	    for (int x = 0; x < childNodes.getLength(); x++ ) {
	        Node data = childNodes.item(x);
	        if ( data.getNodeType() == Node.TEXT_NODE )
	            return data.getNodeValue();
	    }
	    return "";
	}
	 
	protected static String getNodeValue(String tagName, NodeList nodes ) {
	    for ( int x = 0; x < nodes.getLength(); x++ ) {
	        Node node = nodes.item(x);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            NodeList childNodes = node.getChildNodes();
	            for (int y = 0; y < childNodes.getLength(); y++ ) {
	                Node data = childNodes.item(y);
	                if ( data.getNodeType() == Node.TEXT_NODE )
	                    return data.getNodeValue();
	            }
	        }
	    }
	    return "";
	}
	 
	protected static String getNodeAttr(String attrName, Node node ) {
	    NamedNodeMap attrs = node.getAttributes();
	    for (int y = 0; y < attrs.getLength(); y++ ) {
	        Node attr = attrs.item(y);
	        if (attr.getNodeName().equalsIgnoreCase(attrName)) {
	            return attr.getNodeValue();
	        }
	    }
	    return "";
	}
	 
	protected String getNodeAttr(String tagName, String attrName, NodeList nodes ) {
	    for ( int x = 0; x < nodes.getLength(); x++ ) {
	        Node node = nodes.item(x);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            NodeList childNodes = node.getChildNodes();
	            for (int y = 0; y < childNodes.getLength(); y++ ) {
	                Node data = childNodes.item(y);
	                if ( data.getNodeType() == Node.ATTRIBUTE_NODE ) {
	                    if ( data.getNodeName().equalsIgnoreCase(attrName) )
	                        return data.getNodeValue();
	                }
	            }
	        }
	    }
	 
	    return "";
	}
	
	/* SIMPLE PARSER ROUTINE  */
	
	private static ADF Generate_ADF_From_XML ( org.w3c.dom.Document inXML ){
		
		String first_name = "";
		String last_name = "";
		String email = "";
		String phone = "";
		String city = "";
		String state = "";
		String zip_code = "";
		String timeframe = "";
		String prefered_purchase_type = "";
		String prefered_contact_type = "";
		String prefered_contact_time = "";
		
	    // Get the document's root XML node
	    NodeList root = inXML.getChildNodes();
	 
	    // Navigate down the hierarchy to get to the CEO node
	    Node adf = getNode("adf", root);
	    Node prospect = getNode("prospect", adf.getChildNodes() );
	    Node vehicle = getNode("vehicle", prospect.getChildNodes() );
	    Node finance = getNode("finance", vehicle.getChildNodes() );
	    
	    prefered_purchase_type = getNodeValue("method", finance.getChildNodes());
	    
	    Node customer = getNode("customer", prospect.getChildNodes() );
	    Node contact = getNode("contact", customer.getChildNodes() );
	    
	    NodeList nodes = contact.getChildNodes();
	    
	    for (int i =0; i< nodes.getLength(); i++ ){
	    	String name = getNodeAttr("part",nodes.item(i));
	    	if (name!=null){
	    		if (name.contains("last"))
	    			last_name = nodes.item(i).getTextContent();
	    		else 
	    		if(name.contains("first"))
	    			first_name = nodes.item(i).getTextContent();
	    	}
	    }
	    
	    Node n_email = getNode("email", contact.getChildNodes() );
	    email = n_email.getTextContent();
	    if (getNodeAttr("preferredcontact",n_email) == "1"){
	    	prefered_contact_type = "email";
	    	prefered_contact_time = getNodeAttr("time",n_email);
	    }	    
	    
	    Node n_phone = getNode("phone", contact.getChildNodes() );
	    phone = n_phone.getTextContent();
	    if (getNodeAttr("preferredcontact",n_phone) == "1"){
	    	prefered_contact_type = "phone";
	    	prefered_contact_time = getNodeAttr("time",n_phone);
	    }
	    
	    Node address = getNode("address",contact.getChildNodes());
	    
	    city = getNodeValue("city", address.getChildNodes());
	    state = getNodeValue("regioncode", address.getChildNodes());
	    zip_code = getNodeValue("postalcode", address.getChildNodes());
	    
	    Node time_frame = getNode("timeframe", customer.getChildNodes() );
	    timeframe = getNodeValue("description",time_frame.getChildNodes());	    
		
		return new ADF( first_name, last_name, email,
    			phone, name_verifer,
    			city, state, zip_code, address_verifier,
    			timeframe, prefered_purchase_type,
    			prefered_contact_type,
    			prefered_contact_time);
	}
	
	private static org.w3c.dom.Document Generate_Fake_XML_Input(HttpServletRequest request){

		//Get the values that are passed from the Loan HTML page
        String first_name = (String)request.getParameter("first_name");
        String last_name = (String)request.getParameter("last_name");
        String phone = (String)request.getParameter("phone");
        String email = (String)request.getParameter("email");
        
        String city = (String)request.getParameter("city");
        String state = (String)request.getParameter("state");
        String zip_code = (String)request.getParameter("zip_code");
        
        String prefered_time_frame = (String)request.getParameter("time_frame");
        String prefered_purchase_type = (String)request.getParameter("finance");
        String prefered_contact_type = (String)request.getParameter("contact_type");
        String prefered_contact_time = (String)request.getParameter("contact_time");
		
		// combine them into XML file
        org.w3c.dom.Document document = null;        
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            document = builder.newDocument(); 

            Element root = (Element)document.createElement("adf");
            document.appendChild(root);
            
    		Element prospect = document.createElement("prospect");
    		root.appendChild(prospect);
    		
    		Element vehicle = document.createElement("vehicle");
    		prospect.appendChild(vehicle);
    		
    		Element finance = document.createElement("finance");
    		vehicle.appendChild(finance);
    		
    		Element method = document.createElement("method");
    		
    		if (prefered_purchase_type != null)
    		{
    			if (prefered_purchase_type.contains("finance"))
    				method.appendChild(document.createTextNode("finance"));
    			else
    				if (prefered_purchase_type.contains("cash"))
    					method.appendChild(document.createTextNode("cash"));
    				else
    					method.appendChild(document.createTextNode("other"));
    		}
    		else
				method.appendChild(document.createTextNode("other"));
    		
    		finance.appendChild(method);
    		
    		Element customer = document.createElement("customer");
    		prospect.appendChild(customer);
    		Element contact = document.createElement("contact");
    		customer.appendChild(contact);
    		
    		Element efirst_name = document.createElement("name");
    		efirst_name.setAttribute("part", "first");
    		efirst_name.setTextContent(first_name);
    		contact.appendChild(efirst_name);
    		
    		Element elast_name = document.createElement("name");
    		elast_name.setAttribute("part", "last");
    		elast_name.setTextContent(last_name);
    		contact.appendChild(elast_name);

    		Element eemail = document.createElement("email");
    		
    		if (prefered_contact_type!=null){
    			if (prefered_contact_type.contains("email")) {
    				eemail.setAttribute("preferredcontact", "1");
    				eemail.setAttribute("time", prefered_contact_time);
    			}
    		}    		
    		eemail.setTextContent(email);
    		contact.appendChild(eemail);    		

    		Element ephone = document.createElement("phone");
    		if (prefered_contact_type!=null){
    			if (prefered_contact_type.contains("phone")) {
    				ephone.setAttribute("preferredcontact", "1");
    				ephone.setAttribute("time", prefered_contact_time);
    			}
    		}
    		ephone.setTextContent(phone);
    		contact.appendChild(ephone);
    		
    		Element address = document.createElement("address");
    		contact.appendChild(address);
    		
    		Element ecity = document.createElement("city");
    		ecity.setTextContent(city);
    		address.appendChild(ecity);

    		Element regioncode = document.createElement("regioncode");
    		regioncode.setTextContent(state);
    		address.appendChild(regioncode);

    		Element postalcode = document.createElement("postalcode");
    		postalcode.setTextContent(zip_code);
    		address.appendChild(postalcode);
    		
    		Element timefreame = document.createElement("timeframe");
    		customer.appendChild(timefreame);
    		Element description = document.createElement("description");
    		description.setTextContent(prefered_time_frame);
    		timefreame.appendChild(description);

    		if ( enable_debug){
    			Transformer transformer = TransformerFactory.newInstance().newTransformer();
    			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
    			Result output = new StreamResult(new File("C://output.xml"));
    			//StreamResult console = new StreamResult(System.out);
    			Source input = new DOMSource(document);
    			
    			transformer.transform(input, output);
    			//transformer.transform(input, console);
    		}
    		
          }
     catch (Exception e) {
              System.out.println("Generate_Fake_XML_Input: The following exception occurred: "+e.getMessage());
              e.printStackTrace();
           }
    return document;
	}
}
