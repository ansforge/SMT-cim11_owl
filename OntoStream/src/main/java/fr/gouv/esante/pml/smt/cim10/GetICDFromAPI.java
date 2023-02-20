package fr.gouv.esante.pml.smt.cim10;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class GetICDFromAPI {

  private final static String TOKEN_ENPOINT = PropertiesUtil.getProperties("TOKEN_ENPOINT");
  private final static String CLIENT_ID = PropertiesUtil.getProperties("CLIENT_ID");
  private final static String CLIENT_SECRET = PropertiesUtil.getProperties("CLIENT_SECRET");
  private final static String SCOPE = PropertiesUtil.getProperties("SCOPE");
  private final static String GRANT_TYPE = PropertiesUtil.getProperties("GRANT_TYPE");
  private static OntModel m = ModelFactory.createOntologyModel();
  private static String entityCIM10URI = PropertiesUtil.getProperties("entityCIM10URI");
  private static Integer idLabel = 0;
  private static Integer nb = 0;

  private static String CIM10jsonFileName = PropertiesUtil.getProperties("CIM10jsonFileName");
  private static String CIM10labelFileName = PropertiesUtil.getProperties("CIM10labelFileName");
  private static OutputStream fout = null;
  private static OutputStream fout2 = null;
  private static ArrayList<String> SKOSlabelURI =
      new ArrayList<String>(Arrays.asList("http://www.w3.org/2004/02/skos/core#prefLabel",
          "http://www.w3.org/2004/02/skos/core#definition",
          "http://www.w3.org/2004/02/skos/core#altLabel"));
  private static ArrayList<String> ICDlabelURI =
      new ArrayList<String>(Arrays.asList("http://id.who.int/icd/schema/exclusion",
          "http://id.who.int/icd/schema/longDefinition", "http://id.who.int/icd/schema/inclusion",
          "http://id.who.int/icd/schema/narrowerTerm"));
  private static StringBuilder csvLabel = new StringBuilder();

  private static ArrayList<String> idConcept = new ArrayList<String>();
  private static String token = null;

  public static void main(final String[] args) throws Exception {

    // String uri = "http://id.who.int/icd/entity/1597357976";
    fout = new FileOutputStream(CIM10jsonFileName);
    fout2 = new FileOutputStream(CIM10labelFileName);
    token = getToken();
    final String data = getURI(token, entityCIM10URI);
    System.out.println(data);
    final OntModel m2 = ModelFactory.createOntologyModel();

    RDFParser.create().lang(Lang.JSONLD).source(new StringReader(data)).parse(m);
    RDFParser.create().lang(Lang.JSONLD).source(new StringReader(data)).parse(m2);

    
    
    final StmtIterator iter = m2.listStatements();

    while (iter.hasNext()) {
      final Statement stmt = iter.nextStatement(); // get next statement
      final Resource subject = stmt.getSubject(); // get the subject
      final Property predicate = stmt.getPredicate(); // get the predicate
      final RDFNode object = stmt.getObject();
      // get the object
      if (SKOSlabelURI.contains(predicate.toString())) {
        csvLabel.append(idLabel).append(";").append(subject.toString()).append(";")
            .append(predicate.toString().split("#")[1]).append(";")
            .append(object.toString().split("@")[0]).append(";")
            .append(System.getProperty("line.separator"));
        idLabel++;
      } else if (ICDlabelURI.contains(predicate.toString())) {
        csvLabel.append(idLabel).append(";").append(subject.toString()).append(";")
            .append(predicate.toString().split("/")[5]).append(";")
            .append(object.toString().split("@")[0]).append(";")
            .append(System.getProperty("line.separator"));
        idLabel++;
      }

      if (predicate.toString().equals("http://www.w3.org/2004/02/skos/core#narrowerTransitive")
          && !idConcept.contains(object.toString().split("/")[7])) {
        idConcept.add(object.toString().split("/")[7]);
        getData(entityCIM10URI + "/" + object.toString().split("/")[7]);

      }
    }
    m.write(fout, "JSONLD");
    fout2.write(csvLabel.toString().getBytes());
    fout2.close();

  }



  public static void getData(final String uri) throws Exception {
//    System.out.println("Traitement num : " + nb);
    nb++;
    if ((nb % 10000) == 0) {
      token = getToken();
      System.out.println("Getting token: " + token);
    }

    final String data = getURI(token, uri);

    final OntModel m2 = ModelFactory.createOntologyModel();

    RDFParser.create().lang(Lang.JSONLD).source(new StringReader(data)).parse(m);
    RDFParser.create().lang(Lang.JSONLD).source(new StringReader(data)).parse(m2);

    final StmtIterator iter = m2.listStatements();

    while (iter.hasNext()) {
      final Statement stmt = iter.nextStatement(); // get next statement
      final Resource subject = stmt.getSubject(); // get the subject
      final Property predicate = stmt.getPredicate(); // get the predicate
      final RDFNode object = stmt.getObject();
      //System.out.println(predicate.toString());
      // get the object
      if (SKOSlabelURI.contains(predicate.toString())) {
        csvLabel.append(idLabel).append(";").append(subject.toString()).append(";")
            .append(predicate.toString().split("#")[1]).append(";")
            .append(object.toString().split("@")[0]).append(";")
            .append(System.getProperty("line.separator"));
        idLabel++;
      } else if (ICDlabelURI.contains(predicate.toString())) {
        csvLabel.append(idLabel).append(";").append(subject.toString()).append(";")
            .append(predicate.toString().split("/")[5]).append(";")
            .append(object.toString().split("@")[0]).append(";")
            .append(System.getProperty("line.separator"));
        idLabel++;
      }

      if (predicate.toString().equals("http://www.w3.org/2004/02/skos/core#narrowerTransitive")
          && !idConcept.contains(object.toString().split("/")[7])) {
        idConcept.add(object.toString().split("/")[7]);
        getData(entityCIM10URI + "/" + object.toString().split("/")[7]);

      }
    }

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
    con.setRequestProperty("API-Version", "v1");

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
