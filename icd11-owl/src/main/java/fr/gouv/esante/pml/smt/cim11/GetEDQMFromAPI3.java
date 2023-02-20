package fr.gouv.esante.pml.smt.cim11;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class GetEDQMFromAPI3 {

//  private final static String TOKEN_ENPOINT = PropertiesUtil.getProperties("TOKEN_ENPOINT");
//  private final static String CLIENT_ID = PropertiesUtil.getProperties("CLIENT_ID");
//  private final static String CLIENT_SECRET = PropertiesUtil.getProperties("CLIENT_SECRET");
//  private final static String SCOPE = PropertiesUtil.getProperties("SCOPE");
//  private final static String GRANT_TYPE = PropertiesUtil.getProperties("GRANT_TYPE");
//  private static OntModel m = ModelFactory.createOntologyModel();
//  private static String entityURI = PropertiesUtil.getProperties("entityURI");
//  private static String entityURI = "https://id.who.int/icd/release/11/2020-09/mms";
//  private static Integer nb = 0;

//  private static String jsonFileName = PropertiesUtil.getProperties("jsonFileName");
//  private static OutputStream fout = null;
//  private static ArrayList<String> idConcept = new ArrayList<String>();
//  private static String token = null;
//  
//  static InputStream input = null;
  static String data = null;

  public static void main(final String[] args) throws Exception {

    String uri = "https://standardterms.edqm.eu/standardterms/api/v1/basic_data_by_class/CLO/1/1";
//    String uri = "https://id.who.int/icd/release/11/2020-09/mms/codeinfo/2A00.0";
//    String uri = "https://icdapi.azurewebsites.net/icd/release/11/2021-05/mms/850137482";
//    fout = new FileOutputStream(jsonFileName);
//    token = getToken();
    data = getURI(uri);
    System.out.println(data);
     
//    final OntModel m2 = ModelFactory.createOntologyModel();
//    
//    RDFParser.create().lang(Lang.JSONLD).source(new StringReader(data)).parse(m);
//    RDFParser.create().lang(Lang.JSONLD).source(new StringReader(data)).parse(m2);
//
//     
//    
//    
//    final StmtIterator iter = m2.listStatements();
//
//    while (iter.hasNext()) {
//      final Statement stmt = iter.nextStatement(); // get next statement
//      final Resource subject = stmt.getSubject(); // get the subject
//      final Property predicate = stmt.getPredicate(); // get the predicate
//      final RDFNode object = stmt.getObject();
//      
//      if (predicate.toString().equals("http://www.w3.org/2004/02/skos/core#narrowerTransitive")
//          && !idConcept.contains(object.toString().split("/")[5])) {
//        //idConcept.add(object.toString().split("/")[8]);
//    	  if(object.toString().split("/").length == 10) {
//    		  getData(entityURI + "/" + object.toString().split("/")[8] + "/" + object.toString().split("/")[9]);
//    	  }else {
//    		  getData(entityURI + "/" + object.toString().split("/")[5]);
//    	  }
//
//      }
//    }
//    m.write(fout, "JSONLD");
    

  }



//  public static void getData(final String uri) {
//    try {
////    System.out.println("Traitement num : " + nb);
//    nb++;
//    if ((nb % 10000) == 0) {
//      token = getToken();
//      System.out.println("Getting token: " + token);
//    }
//
//    data = getURI(token, uri);
//    System.out.println(data);
//    final OntModel m2 = ModelFactory.createOntologyModel();
//
//    RDFParser.create().lang(Lang.JSONLD).source(new StringReader(data)).parse(m);
//    RDFParser.create().lang(Lang.JSONLD).source(new StringReader(data)).parse(m2);
//    
//    final ByteArrayOutputStream out = new ByteArrayOutputStream();
//    m2.write(out, "RDF/JSON");
//    input = new ByteArrayInputStream(out.toByteArray());
//    //final OWLOntologyDocumentSource documentSource = new StringDocumentSource(data);
//    final OWLOntologyLoaderConfiguration config = manager.getOntologyLoaderConfiguration();
//    manager.setOntologyLoaderConfiguration(
//        config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT));
//    OWLOntology onto = manager.loadOntologyFromOntologyDocument(input);
////    axioms = new HashSet<OWLAxiom>();
////    axioms.addAll(onto.getAxioms());
//    manager.addAxioms(ontology, onto.axioms());
//    
    
//    final StmtIterator iter = m2.listStatements();
//
//    while (iter.hasNext()) {
//      final Statement stmt = iter.nextStatement(); // get next statement
//      final Resource subject = stmt.getSubject(); // get the subject
//      final Property predicate = stmt.getPredicate(); // get the predicate
//      final RDFNode object = stmt.getObject();
//
//      if (predicate.toString().equals("http://www.w3.org/2004/02/skos/core#narrowerTransitive")
//          && !idConcept.contains(object.toString().split("/")[5])) {
////        idConcept.add(object.toString().split("/")[8]);
//        if(object.toString().split("/").length == 10) {
//  		  getData(entityURI + "/" + object.toString().split("/")[8] + "/" + object.toString().split("/")[9]);
//  	  	}else {
//  		  getData(entityURI + "/" + object.toString().split("/")[5]);
//  	  	}
//
//      }
//    }
//    }catch(Exception ex) {
//      ex.printStackTrace();
//    }

//  }


//  // get the OAUTH2 token
//  private static String getToken() throws Exception {
//
//    System.out.println("Getting token...");
//
//    final URL url = new URL(TOKEN_ENPOINT);
//    final HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//    con.setRequestMethod("POST");
//
//    // set parameters to post
//    final String urlParameters = "client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8")
//        + "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, "UTF-8") + "&scope="
//        + URLEncoder.encode(SCOPE, "UTF-8") + "&grant_type="
//        + URLEncoder.encode(GRANT_TYPE, "UTF-8");
//    con.setDoOutput(true);
//    final DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//    wr.writeBytes(urlParameters);
//    wr.flush();
//    wr.close();
//
//    final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//    String inputLine;
//    final StringBuffer response = new StringBuffer();
//    while ((inputLine = in.readLine()) != null) {
//      response.append(inputLine);
//    }
//    in.close();
//
//    // parse JSON response
//    final JSONObject jsonObj = new JSONObject(response.toString());
//    return jsonObj.getString("access_token");
//  }


  // access ICD API
  private static String getURI(final String uri) throws Exception {

    // System.out.println("Getting URI...");
	  String Login = "Ali.GOCHATH.EXT@esante.gouv.fr";
	  String YourSecretAccessKey = "74qvhYRz";
	  String HOST = "standardterms.edqm.eu";
	  String ur = "/standardterms/api/v1/basic_data_by_class/CLO/1/1";
	  String reqDate = getServerTime();
	  String StringToSign = "GET" +"&" + ur + "&" + HOST + "&" + reqDate;
	  String Signature = hashSignature( YourSecretAccessKey, StringToSign ) ;
	  String X_STAPI_KEY = Login +"|"+ Signature;
	  System.out.println(X_STAPI_KEY);
	  System.out.println(StringToSign);
	  
	 
    final URL url = new URL(uri);
    final HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");

    // HTTP header fields to set
    con.setRequestProperty("Date", reqDate);
    con.setRequestProperty("X-STAPI-KEY", X_STAPI_KEY);
    

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
  
  public static String getServerTime() {
	    Calendar calendar = Calendar.getInstance();
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
	    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    return dateFormat.format(calendar.getTime());
	}
  
  public static String hashSignature(String secret, String message) throws NoSuchAlgorithmException, InvalidKeyException {
	  Mac sha256_HMAC = Mac.getInstance("HmacSHA512");
	  SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
	  sha256_HMAC.init(secret_key);

	  String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
	     return hash.substring(hash.length()-22);
  }

}
