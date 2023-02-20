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
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.SkosVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

public class ModeAccesOntologie {

	private static String ExcelFile = PropertiesUtil.getCCAMProperties("ccamModeAccesExcele");
	private static String OWLFile = PropertiesUtil.getCCAMProperties("ccamModeAccesOWL");

	private static OWLDataFactory fact = null;

	
	private static OWLOntologyManager manager = null;
	private static OWLOntology onto = null;
	private static final Logger logger = Logger.getLogger(ModeAccesOntologie.class);

	public static void main(final String[] args) throws Exception {

		manager = OWLManager.createOWLOntologyManager();
		onto = manager.createOntology(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ModeAccesOnto"));

		fact = onto.getOWLOntologyManager().getOWLDataFactory();
		
		OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/racine"));
		OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
		manager.applyChange(new AddAxiom(onto, declare));
		
		OWLAnnotation annot = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("CCAM", "fr"));
		OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot);
		manager.applyChange(new AddAxiom(onto, axiom));

		createModeAcces();
		
		final OutputStream fileoutputstream = new FileOutputStream(OWLFile);
		final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
		manager.saveOntology(onto, ontologyFormat, fileoutputstream);

	}
	
	private static void createModeAcces() {
		logger.info("construction de la liste ModeAcces");
		try {
			ChargeMapping.chargeModeAcces(ExcelFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String code : ChargeMapping.listModeAcces.keySet()) {
			OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ModeAcces_" + code));
			OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			manager.applyChange(new AddAxiom(onto, declare));
			
			OWLSubClassOfAxiom ax = fact.getOWLSubClassOfAxiom(owlClass,
						fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ModeAcces")));
				manager.applyChange(new AddAxiom(onto, ax));
				
				OWLAnnotation annotModeAcces = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("ModeAcces", "fr"));
				OWLAxiom axiomModeAcces = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ModeAcces"), annotModeAcces);
				manager.applyChange(new AddAxiom(onto, axiomModeAcces));
				
				OWLSubClassOfAxiom ax1 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ModeAcces")),
						fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/AxeActe")));
				manager.applyChange(new AddAxiom(onto, ax1));
				
			OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(notation, fact.getOWLLiteral(code));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(
					IRI.create("http://data.esante.gouv.fr/cnam/ccam/ModeAcces_" + code), annot);
			manager.applyChange(new AddAxiom(onto, axiom));	
			
			String label = ChargeMapping.listModeAcces.get(code).split("£")[0];
			OWLAnnotation annot1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(reformeVerbe(label), "fr"));
			OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot1);
			manager.applyChange(new AddAxiom(onto, axiom1));
			
			String noteMode = ChargeMapping.listModeAcces.get(code).split("£")[1];
			OWLAnnotationProperty note = new OWLAnnotationPropertyImpl(SKOSVocabulary.DEFINITION.getIRI());
			OWLAnnotation annot2 = fact.getOWLAnnotation(note, fact.getOWLLiteral(noteMode, "fr"));
			OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(
					IRI.create("http://data.esante.gouv.fr/cnam/ccam/ModeAcces_" + code), annot2);
			manager.applyChange(new AddAxiom(onto, axiom2));
			
			for (String syno : ChargeMapping.listModeAcces.get(code).split("£")[2].split("\n")) {
				
				OWLAnnotationProperty alt = new OWLAnnotationPropertyImpl(SKOSVocabulary.ALTLABEL.getIRI());
				OWLAnnotation annot3 = fact.getOWLAnnotation(alt, fact.getOWLLiteral(syno, "fr"));
				OWLAxiom axiom3 = fact.getOWLAnnotationAssertionAxiom(
						IRI.create("http://data.esante.gouv.fr/cnam/ccam/ModeAcces_" + code), annot3);
				manager.applyChange(new AddAxiom(onto, axiom3));
			}
		}

		
	}
	
	private static String reformeVerbe(String verbe) {
		String result = "";
		result = verbe.substring(0, 1).toUpperCase();
		result += verbe.substring(1).toLowerCase();
		return result;
	}
}
