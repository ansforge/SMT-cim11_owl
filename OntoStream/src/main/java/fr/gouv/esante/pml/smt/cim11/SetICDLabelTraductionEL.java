package fr.gouv.esante.pml.smt.cim11;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.SetOntologyID;
import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class SetICDLabelTraductionEL {
  
  
  private static String owlFileNameEL = PropertiesUtil.getProperties("owlFileNameEL");
  private static String owlFileNamePL = PropertiesUtil.getProperties("owlFileNamePL");
  private static String labelsFileNameEL = PropertiesUtil.getProperties("labelsFileNameEL");
  private static OWLDataFactory fact = null;
  static InputStream input = null;
  
  public static void main(final String[] args) throws Exception {
    
    input = new FileInputStream(owlFileNamePL);
    ChargeMapping.chargeCSVTraductionELFileToList(labelsFileNameEL);
    
    final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLOntology onto = manager.loadOntologyFromOntologyDocument(input);
    
    fact = onto.getOWLOntologyManager().getOWLDataFactory();
//    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
//    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label") || 
//                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:comment") ).
//    forEach(ax -> {
//      if("el".equals(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[1])) {    
//        manager.applyChange(new RemoveAxiom(onto, ax));
//      }
//                  });
    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:comment") ).
    forEach(ax -> {
      if(ChargeMapping.listTraductionEL.containsKey(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""))) {    
        final OWLAnnotation labelAnno = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(),fact.getOWLLiteral(ChargeMapping.listTraductionEL.get(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", "")), "el"));
        final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), labelAnno);
        manager.applyChange(new AddAxiom(onto, ax1));
      }
                  });

    final OutputStream fileoutputstream = new FileOutputStream(owlFileNameEL);
    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
    IRI icd11IRI = IRI.create("http://esante.gouv.fr/terminologie-icd11");
    manager.applyChange(new SetOntologyID(onto,  new OWLOntologyID(icd11IRI)));
    
    manager.saveOntology(onto, ontologyFormat, fileoutputstream);


  }

}
