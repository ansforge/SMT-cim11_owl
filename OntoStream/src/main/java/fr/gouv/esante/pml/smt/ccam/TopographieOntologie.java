package fr.gouv.esante.pml.smt.ccam;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.SkosVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

public class TopographieOntologie {

	
	private static String ExcelFile = PropertiesUtil.getCCAMProperties("ccamTopographieExcele");
	private static String OWLFile = PropertiesUtil.getCCAMProperties("ccamTopographieOWL");

	private static OWLDataFactory fact = null;

	
	private static OWLOntologyManager manager = null;
	private static OWLOntology onto = null;
	private static final Logger logger = Logger.getLogger(TopographieOntologie.class);

	public static void main(final String[] args) throws Exception {

		manager = OWLManager.createOWLOntologyManager();
		onto = manager.createOntology(IRI.create("http://data.esante.gouv.fr/cnam/ccam/TopographieOnto"));

		fact = onto.getOWLOntologyManager().getOWLDataFactory();
		
		OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/racine"));
		OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
		manager.applyChange(new AddAxiom(onto, declare));
		
		OWLAnnotation annot = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("CCAM", "fr"));
		OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot);
		manager.applyChange(new AddAxiom(onto, axiom));

		createTopographie();

		final OutputStream fileoutputstream = new FileOutputStream(OWLFile);
		final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
		manager.saveOntology(onto, ontologyFormat, fileoutputstream);

	}

	
	private static void createTopographie() {
		logger.info("construction de la liste Topographie");
		try {
			ChargeMapping.chargelistTopographie(ExcelFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String topog : ChargeMapping.listTopograohie.keySet()) {
			OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Topographie_" + ChargeMapping.listTopograohie.get(topog)));
			OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			manager.applyChange(new AddAxiom(onto, declare));
			
			if(ChargeMapping.listTopograohie.get(topog).length() == 2) {
			OWLSubClassOfAxiom ax = fact.getOWLSubClassOfAxiom(owlClass,
						fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Topographie_" + ChargeMapping.listTopograohie.get(topog).substring(0, 1))));
				manager.applyChange(new AddAxiom(onto, ax));
			}else {
				OWLSubClassOfAxiom ax = fact.getOWLSubClassOfAxiom(owlClass,
						fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Topographie")));
				manager.applyChange(new AddAxiom(onto, ax));
				
				OWLAnnotation annotTopographie = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("Topographie", "fr"));
				OWLAxiom axiomModeTopographie = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Topographie"), annotTopographie);
				manager.applyChange(new AddAxiom(onto, axiomModeTopographie));
				
				OWLSubClassOfAxiom ax1 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Topographie")),
						fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/AxeActe")));
				manager.applyChange(new AddAxiom(onto, ax1));
			}
			
			OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(notation, fact.getOWLLiteral(ChargeMapping.listTopograohie.get(topog)));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(
					IRI.create("http://data.esante.gouv.fr/cnam/ccam/Topographie_" + ChargeMapping.listTopograohie.get(topog)), annot);
			manager.applyChange(new AddAxiom(onto, axiom));	
		
			OWLAnnotation annot1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(reformeVerbe(topog), "fr"));
			OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot1);
			manager.applyChange(new AddAxiom(onto, axiom1));
		}

		
	}
	
	private static String reformeVerbe(String verbe) {
		String result = "";
		result = verbe.substring(0, 1).toUpperCase();
		result += verbe.substring(1).toLowerCase();
		return result;
	}
}
