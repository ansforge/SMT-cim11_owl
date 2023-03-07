package fr.gouv.esante.pml.smt.cim11;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.RDFWriter;
import org.json.JSONObject;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class GetICDFromAPI {

    private static String tokenEnpoint;
    private static String clientId;
    private static String clientSecret;
    private static String scope;
    private static String grantType;
    private static String entityURI;
    private static Model m = ModelFactory.createOntologyModel();

    //  private static String entityURI = "https://id.who.int/icd/release/11/2020-09/mms";
    private static Integer nb = 0;

    private static final String JSON_FILE_NAME = "CIM11_JSON_FILE_NAME.json";
    private static OutputStream fout = null;
    private static final ArrayList<String> idConcept = new ArrayList<String>();
    private static String token = null;

    static InputStream input = null;
    static String data = null;
    static String dataFR = null;

    public static void main(final String[] args) throws Exception {
        Options options = new Options();
        options.addOption("l", "langue", true, "langue pour owl file");
        options.addOption("o", "output", true, "path owl File");
        options.addOption("c", "conf", true, "path to conf file");
        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);
        String langue = line.getOptionValue("langue");
        String config_path = line.getOptionValue("conf");
        tokenEnpoint = PropertiesUtil.getProperties("TOKEN_ENPOINT",config_path);
        clientId = PropertiesUtil.getProperties("CLIENT_ID",config_path);
        clientSecret = PropertiesUtil.getProperties("CLIENT_SECRET",config_path);
        scope = PropertiesUtil.getProperties("SCOPE",config_path);
        grantType = PropertiesUtil.getProperties("GRANT_TYPE",config_path);
        entityURI = PropertiesUtil.getProperties("entityURI",config_path);

        if (langue == null) {
            langue = PropertiesUtil.getProperties("icd_language",config_path);
        }

        fout = new FileOutputStream(JSON_FILE_NAME);
        token = getToken();
        System.out.println("Getting first token: " + token);
        data = getURI(token, entityURI, langue);
        System.out.println("Getting first entity: " + data);

        final OntModel m2 = ModelFactory.createOntologyModel();
        RDFParser.create().lang(Lang.JSONLD).source(new StringReader(data)).parse(m);
        RDFParser.create().lang(Lang.JSONLD).source(new StringReader(data)).parse(m2);

        final StmtIterator iter = m2.listStatements();
        while (iter.hasNext()) {
            final Statement stmt = iter.nextStatement(); // get next statement
            final Resource subject = stmt.getSubject(); // get the subject
            final Property predicate = stmt.getPredicate(); // get the predicate
            final RDFNode object = stmt.getObject();
            if (predicate.toString().equals("http://www.w3.org/2004/02/skos/core#narrower")) {
                getData(object.toString().replace("http://id.who.int", "https://id.who.int"), langue);
            }
        }
        RDFWriter.create().source(m).lang(Lang.JSONLD).output(fout);
        //m.write(fout, "JSON-LD");
        fout.close();
    }

    public static void getData(final String uri, final String langue) {
        try {
            nb++;
            if ((nb % 10000) == 0) {
                token = getToken();
                System.out.println("Getting token: " + token);
            }

            data = getURI(token, uri, langue);
            final OntModel m2 = ModelFactory.createOntologyModel();

            RDFParser.create().lang(Lang.JSONLD).source(new StringReader(data)).parse(m);
            RDFParser.create().lang(Lang.JSONLD).source(new StringReader(data)).parse(m2);
            final StmtIterator iter = m2.listStatements();
            while (iter.hasNext()) {
                final Statement stmt = iter.nextStatement(); // get next statement
                final Resource subject = stmt.getSubject(); // get the subject
                final Property predicate = stmt.getPredicate(); // get the predicate
                final RDFNode object = stmt.getObject();

                if (predicate.toString().equals("http://www.w3.org/2004/02/skos/core#narrower")
                        && !idConcept.contains(object.toString())) {
                    idConcept.add(object.toString());
                    getData(object.toString().replace("http://id.who.int", "https://id.who.int"), langue);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    // get the OAUTH2 token
    private static String getToken() throws Exception {
        final URL url = new URL(tokenEnpoint);
        final HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        // set parameters to post
        final String urlParameters = "client_id=" + URLEncoder.encode(clientId, "UTF-8")
                + "&client_secret=" + URLEncoder.encode(clientSecret, "UTF-8") + "&scope="
                + URLEncoder.encode(scope, "UTF-8") + "&grant_type="
                + URLEncoder.encode(grantType, "UTF-8");
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
    private static String getURI(final String token, final String uri, final String langue) throws Exception {
        final URL url = new URL(uri);
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // HTTP header fields to set
        con.setRequestProperty("Authorization", "Bearer " + token);
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Accept-Language", langue);
        con.setRequestProperty("API-Version", "v2");

        final BufferedReader in =
                new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        final StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

}
