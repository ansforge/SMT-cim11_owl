package fr.gouv.esante.pml.smt.cim11;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.HasProperty;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.search.EntitySearcher;
import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class GetICDLabel {
  
  
  private static String skosFileName = PropertiesUtil.getProperties("skosFileName");
  private static String jsonFileName = PropertiesUtil.getProperties("jsonFileName");
  private static String owlFileName = PropertiesUtil.getProperties("owlFileName");
  private static String labelFileName = PropertiesUtil.getProperties("labelFileName");
  private static String traductionFileName = PropertiesUtil.getProperties("traductionFileName");
  private static ArrayList<String> labelICDFondation = new ArrayList<String>();
  private static ArrayList<String> labelICDMMS = new ArrayList<String>();
//  private static ArrayList<String> NewlabelICDFondation = new ArrayList<String>();
  private static ArrayList<String> NewlabelICDMMS = new ArrayList<String>();
 
  static InputStream input1 = null;
  static InputStream input2 = null;
  static InputStream input3 = null;
  static InputStream input4 = null;
  
  public static void main(final String[] args) throws Exception {
    
//    fout = new FileOutputStream(labelFileName);
    input1 = new FileInputStream("D:\\cim11\\newModelisation\\CGTS_SEM_ICD11-V202009-EN-NewModel.owl");
    input2 = new FileInputStream("D:\\cim11\\newModelisation\\CGTS_SEM_ICD11-MMS-OWL-R202009-EN.xml");
    input3 = new FileInputStream("D:\\cim11\\R202105\\CGTS_SEM_ICD11-MMS-R202105-EN-V1.owl");
    input4 = new FileInputStream("D:\\cim11\\R202105\\CGTS_SEM_ICD11-R202105-EN-V1.owl");
    
    Writer csvWriter = new OutputStreamWriter(new FileOutputStream("D:\\cim11\\R202105\\NewLabelICD11.csv"), StandardCharsets.UTF_8);
     
    final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLOntology onto1 = manager.loadOntologyFromOntologyDocument(input1);
    OWLOntology onto2 = manager.loadOntologyFromOntologyDocument(input2);
    OWLOntology onto3 = manager.loadOntologyFromOntologyDocument(input3);
    OWLOntology onto4 = manager.loadOntologyFromOntologyDocument(input4);
    
//    onto.axioms().forEach(ax -> {System.out.println(ax);});
    onto1.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2008/05/skos-xl#literalForm>") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:comment")).
//    filter(ax -> !((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("http://id.who.int/icd/entity")).
    forEach(ax -> { //System.out.println(((OWLAnnotationAssertionAxiom) ax).getSubject()+"---"+((OWLAnnotationAssertionAxiom) ax).getProperty()+"---"+((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1));
    	labelICDFondation.add(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""));   
               
                  });
    
    onto2.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2008/05/skos-xl#literalForm>") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:comment")).
//    filter(ax -> !((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("http://id.who.int/icd/entity")).
    forEach(ax -> { //System.out.println(((OWLAnnotationAssertionAxiom) ax).getSubject()+"---"+((OWLAnnotationAssertionAxiom) ax).getProperty()+"---"+((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1));
    	labelICDMMS.add(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""));   
               
                  });
    
    onto3.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2008/05/skos-xl#literalForm>") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#prefLabel>") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#definition>")).
//    filter(ax -> !((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("http://id.who.int/icd/entity")).
    forEach(ax -> { //System.out.println(((OWLAnnotationAssertionAxiom) ax).getSubject()+"---"+((OWLAnnotationAssertionAxiom) ax).getProperty()+"---"+((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1));
      if(!labelICDFondation.contains(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""))) {    
    	  if(!labelICDMMS.contains(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""))) {    
                   try {
                	   NewlabelICDMMS.add(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""));
                      csvWriter.append(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()).append(";");
                      csvWriter.append(((OWLAnnotationAssertionAxiom) ax).getProperty().toString()).append(";");
                      csvWriter.append("vrai").append(";");
                      csvWriter.append(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).replaceAll("\n", "").replaceAll(";", ","));
                      csvWriter.append("\n");
                    } catch (IOException e) {
                    }
    	  }
      }
                  });
    
    onto4.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2008/05/skos-xl#literalForm>") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#prefLabel>") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#definition>")).
//    filter(ax -> !((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("http://id.who.int/icd/entity")).
    forEach(ax -> { //System.out.println(((OWLAnnotationAssertionAxiom) ax).getSubject()+"---"+((OWLAnnotationAssertionAxiom) ax).getProperty()+"---"+((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1));
      if(!labelICDFondation.contains(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""))) {    
    	  if(!labelICDMMS.contains(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""))) {    
    		  if(!NewlabelICDMMS.contains(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""))) {    
                      try {
                      csvWriter.append(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()).append(";");
                      csvWriter.append(((OWLAnnotationAssertionAxiom) ax).getProperty().toString()).append(";");
                      csvWriter.append("faux").append(";");
                      csvWriter.append(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).replaceAll("\n", "").replaceAll(";", ","));
                      csvWriter.append("\n");
                    } catch (IOException e) {
                    }
    		  }
    	  }
      }
                  });
   
//    final OntModel m = ModelFactory.createOntologyModel();
//    RDFParser.create().lang(Lang.RDFXML).source(input).parse(m);
//    

    csvWriter.flush();
    csvWriter.close();
    
//    fout.write(csvLabel.toString().getBytes());
//    fout.close();
//    
  }

}
