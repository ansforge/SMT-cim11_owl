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
import org.semanticweb.owlapi.model.SetOntologyID;
import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class SetICDLabelTraductionZH {
  
  
  private static String owlFileNameRU = PropertiesUtil.getProperties("owlFileNameRU");
  private static String owlFileNameZH = PropertiesUtil.getProperties("owlFileNameZH");
  private static String labelsFileName = PropertiesUtil.getProperties("labelsFileNameZH");
  private static OWLDataFactory fact = null;
  static InputStream input = null;
  
  public static void main(final String[] args) throws Exception {
    
    input = new FileInputStream(owlFileNameRU);
    ChargeMapping.chargeXLXSTraductionFileToListAR_RU_ZH(labelsFileName);
    
    final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLOntology onto = manager.loadOntologyFromOntologyDocument(input);
    
    fact = onto.getOWLOntologyManager().getOWLDataFactory();
    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label") || 
                 ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:comment") ).
    forEach(ax -> {
      if(ChargeMapping.listTraduction.containsKey(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", ""))) {    
        final OWLAnnotation labelAnno = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(),fact.getOWLLiteral(ChargeMapping.listTraduction.get(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1).toLowerCase().replaceAll("[^a-zA-Z0-9_-]", "")), "zh"));
        final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), labelAnno);
        manager.applyChange(new AddAxiom(onto, ax1));
      }
                  });

    final OutputStream fileoutputstream = new FileOutputStream(owlFileNameZH);
    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
    IRI icd11IRI = IRI.create("http://esante.gouv.fr/terminologie-icd11");
    manager.applyChange(new SetOntologyID(onto,  new OWLOntologyID(icd11IRI)));
    
    manager.saveOntology(onto, ontologyFormat, fileoutputstream);


  }

}
