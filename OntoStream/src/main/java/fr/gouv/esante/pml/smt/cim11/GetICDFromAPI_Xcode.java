package fr.gouv.esante.pml.smt.cim11;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import javax.net.ssl.HttpsURLConnection;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.json.JSONObject;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class GetICDFromAPI_Xcode {

  private final static String TOKEN_ENPOINT = PropertiesUtil.getProperties("TOKEN_ENPOINT");
  private final static String CLIENT_ID = PropertiesUtil.getProperties("CLIENT_ID");
  private final static String CLIENT_SECRET = PropertiesUtil.getProperties("CLIENT_SECRET");
  private final static String SCOPE = PropertiesUtil.getProperties("SCOPE");
  private final static String GRANT_TYPE = PropertiesUtil.getProperties("GRANT_TYPE");
  private static OntModel m = ModelFactory.createOntologyModel();
  private static String entityURI = PropertiesUtil.getProperties("entityURI");
  private static String mmsentityURI = "https://id.who.int/icd/release/11/2020-09/mms/";
  
  private static ArrayList<String> idConcept = new ArrayList<String>();
  private static String token = null;
  
//  static InputStream input = null;
  static String data = null;
  private static Writer csvWriter;

  public static void main(final String[] args) throws Exception {
	csvWriter = new OutputStreamWriter(new FileOutputStream("D:\\X_Extensions_Codes22.csv"), StandardCharsets.UTF_8);  
//    String uri = "https://id.who.int/icd/entity/1991139272";
//    String uri = "https://id.who.int/icd/release/11/2020-09/mms/codeinfo/2A00.0";
	  String uri = "https://id.who.int/icd/release/11/2020-09/mms/1991139272";
//    fout = new FileOutputStream(jsonFileName);
    token = getToken();
    data = getURI(token, uri);
    System.out.println(data);
     Integer profondeur = 3;
     String id = "";
     String label = "";
     String code = "";
     String parentId = "";
     String parentLabel = "";
     String parentCode = "";
     String child = "";
    final OntModel m2 = ModelFactory.createOntologyModel();
    RDFParser.create().lang(Lang.JSONLD).source(new StringReader(data)).parse(m2);

     
    
    
    final StmtIterator iter = m2.listStatements();

    while (iter.hasNext()) {
      final Statement stmt = iter.nextStatement(); // get next statement
      final Resource subject = stmt.getSubject(); // get the subject
      final Property predicate = stmt.getPredicate(); // get the predicate
      final RDFNode object = stmt.getObject();
      
      if (predicate.toString().equals("http://www.w3.org/2004/02/skos/core#prefLabel") )
      {
    	  label = object.toString().split("@")[0];
    	  id = subject.toString();
    	  code = getDataCode(subject.toString().split("/")[8]);
      }
      
      if (predicate.toString().equals("http://www.w3.org/2004/02/skos/core#broaderTransitive") )
      {
    	  parentId = object.toString();
    	  parentLabel = getDataParentLabel(object.toString().replace("http", "https"));
    	  parentCode = getDataCode(object.toString().split("/")[8]);
      }
      
      if (predicate.toString().equals("http://www.w3.org/2004/02/skos/core#narrowerTransitive") )
      {
    	  getData(object.toString().replace("http", "https"), profondeur);
    	  
      }
    }
      csvWriter.append(""+profondeur).append(";");
      csvWriter.append(id).append(";");
      csvWriter.append(code).append(";");
      csvWriter.append(label).append(";");
      csvWriter.append(parentId).append(";");
      csvWriter.append(parentCode).append(";");
      csvWriter.append(parentLabel).append(";");
      csvWriter.append("\n");
      
    
    csvWriter.flush();
    csvWriter.close();
    

  }



  public static void getData(final String uri, Integer profondeur) {
    try {
    	Integer profondeur2 = profondeur + 1;
        String id = "";
        String label = "";
        String code = "";
        String parentId = "";
        String parentLabel = "";
        String parentCode = "";
        
    data = getURI(token, uri);
    final OntModel m2 = ModelFactory.createOntologyModel();
    RDFParser.create().lang(Lang.JSONLD).source(new StringReader(data)).parse(m2);
    
    final StmtIterator iter = m2.listStatements();

    while (iter.hasNext()) {
      final Statement stmt = iter.nextStatement(); // get next statement
      final Resource subject = stmt.getSubject(); // get the subject
      final Property predicate = stmt.getPredicate(); // get the predicate
      final RDFNode object = stmt.getObject();

      if (predicate.toString().equals("http://www.w3.org/2004/02/skos/core#prefLabel") )
      {
    	  label = object.toString().split("@")[0];
    	  id = subject.toString();
    	  code = getDataCode(subject.toString().split("/")[8]);
      }
      
      if (predicate.toString().equals("http://www.w3.org/2004/02/skos/core#broaderTransitive") )
      {
    	  parentId = object.toString();
    	  parentLabel = getDataParentLabel(object.toString().replace("http", "https"));
    	  parentCode = getDataCode(object.toString().split("/")[8]);
      }
      
      if (predicate.toString().equals("http://id.who.int/icd/schema/postcoordinationScale") )
      {
    	  System.out.println(object.toString());
    	 
      }
      
      if (predicate.toString().equals("http://www.w3.org/2004/02/skos/core#narrowerTransitive") 
    		  && !idConcept.contains(object.toString()) ) {
        idConcept.add(object.toString());
    	  getData(object.toString().replace("http", "https"), profondeur2);
    	  
      }
    }
      csvWriter.append(""+profondeur2).append(";");
      csvWriter.append(id).append(";");
      csvWriter.append(code).append(";");
      csvWriter.append(label).append(";");
      csvWriter.append(parentId).append(";");
      csvWriter.append(parentCode).append(";");
      csvWriter.append(parentLabel).append(";");
      csvWriter.append("\n");
      
    
    }catch(Exception ex) {
      ex.printStackTrace();
    }

  }
  
  public static String getDataParentLabel(final String uri) {
	  String label = "";  
	  try {
	    	
	    data = getURI(token, uri);
	    final OntModel m2 = ModelFactory.createOntologyModel();

	    RDFParser.create().lang(Lang.JSONLD).source(new StringReader(data)).parse(m2);
	    
	    final StmtIterator iter = m2.listStatements();

	    while (iter.hasNext()) {
	      final Statement stmt = iter.nextStatement(); // get next statement
	      final Resource subject = stmt.getSubject(); // get the subject
	      final Property predicate = stmt.getPredicate(); // get the predicate
	      final RDFNode object = stmt.getObject();

	      if (predicate.toString().equals("http://www.w3.org/2004/02/skos/core#prefLabel")) {
	            label = object.toString().split("@")[0];
	          }
	    }
	    
	    
	    }catch(Exception ex) {
	      ex.printStackTrace();
	    }
	  return label;
	  }
  
  public static String getDataCode(final String id) {
	  String code = "";  
	  try {
	    	
	    data = getURI(token, mmsentityURI+id);
	    final OntModel m2 = ModelFactory.createOntologyModel();

	    RDFParser.create().lang(Lang.JSONLD).source(new StringReader(data)).parse(m2);
	    
	    final StmtIterator iter = m2.listStatements();

	    while (iter.hasNext()) {
	      final Statement stmt = iter.nextStatement(); // get next statement
	      final Resource subject = stmt.getSubject(); // get the subject
	      final Property predicate = stmt.getPredicate(); // get the predicate
	      final RDFNode object = stmt.getObject();

	      if (predicate.toString().equals("http://id.who.int/icd/schema/code")) {
	            code = object.toString();
	          }
	    }
	    
	    
	    }catch(Exception ex) {
	      ex.printStackTrace();
	    }
	  return code;
	  }


  // get the OAUTH2 token
  private static String getToken() throws Exception {

    System.out.println("Getting token...");

    final URL url = new URL(TOKEN_ENPOINT);
    final HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
    con.setRequestMethod("POST");

    // set parameters to post
    final String urlParameters = "client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8")
        + "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, "UTF-8") + "&scope="
        + URLEncoder.encode(SCOPE, "UTF-8") + "&grant_type="
        + URLEncoder.encode(GRANT_TYPE, "UTF-8");
    con.setDoOutput(true);
    final DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    wr.writeBytes(urlParameters);
    wr.flush();
    wr.close();

    final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    final StringBuffer response = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    // parse JSON response
    final JSONObject jsonObj = new JSONObject(response.toString());
    return jsonObj.getString("access_token");
  }


  // access ICD API
  private static String getURI(final String token, final String uri) throws Exception {

    // System.out.println("Getting URI...");

    final URL url = new URL(uri);
    final HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");

    // HTTP header fields to set
    con.setRequestProperty("Authorization", "Bearer " + token);
    con.setRequestProperty("Accept", "application/json");
    con.setRequestProperty("Accept-Language", "en");
    con.setRequestProperty("API-Version", "v2");

    final BufferedReader in =
        new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
    String inputLine;
    final StringBuffer response = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    return response.toString();
  }

}
