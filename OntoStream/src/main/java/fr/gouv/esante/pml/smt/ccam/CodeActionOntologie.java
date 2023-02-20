package fr.gouv.esante.pml.smt.ccam;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import fr.gouv.esante.pml.smt.utils.CCAMVocabulary;
import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.SkosVocabulary;
import fr.gouv.esante.pml.smt.utils.StringOperation;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

public class CodeActionOntologie {

	private static String ExcelFile = PropertiesUtil.getCCAMProperties("ccamCodeActionExcele");
	private static String csvFile = PropertiesUtil.getCCAMProperties("ccamCatVerbeActionCSV");
	private static String OWLFile = PropertiesUtil.getCCAMProperties("ccamCodeActionOWL");

	private static OWLDataFactory fact = null;

	
	private static OWLOntologyManager manager = null;
	private static OWLOntology onto = null;
	private static final Logger logger = Logger.getLogger(CodeActionOntologie.class);

	public static void main(final String[] args) throws Exception {

		manager = OWLManager.createOWLOntologyManager();
		onto = manager.createOntology(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ActionONTO"));

		fact = onto.getOWLOntologyManager().getOWLDataFactory();
		
		createVerbeAction();
		createCategorieVerbeAction();
		
		final OutputStream fileoutputstream = new FileOutputStream(OWLFile);
		final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
		manager.saveOntology(onto, ontologyFormat, fileoutputstream);

	}

	private static void createVerbeAction() {
		logger.info("construction de la liste de verbe d'action");
		try {
			ChargeMapping.chargeListeAction(ExcelFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map<String, String> labelListe = new HashMap<String, String>();
		
		for (String verbe : ChargeMapping.listAction.keySet()) {
			String codeAction = ChargeMapping.listAction.get(verbe).split("£")[3];
			String definition = ChargeMapping.listAction.get(verbe).split("£")[1];
			String synonymes = ChargeMapping.listAction.get(verbe).split("£")[2];
//			if(labelListe.containsKey(codeAction)) {
//				String label = labelListe.get(codeAction);
//				label += " | " + verbe;
//				labelListe.put(codeAction, label);
//			}else {
//				labelListe.put(codeAction, verbe);
//			}
//		}
//
//		for (String code : labelListe.keySet()) {
			OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/VerbeAction_" + StringOperation.sansAccents(reformeVerbe(verbe))));
			OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			manager.applyChange(new AddAxiom(onto, declare));
			
//			OWLSubClassOfAxiom ax = fact.getOWLSubClassOfAxiom(owlClass,
//						fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/VerbeAction")));
//				manager.applyChange(new AddAxiom(onto, ax));
				
				OWLSubClassOfAxiom ax1 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Action_" + codeAction)),
						fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Action")));
				manager.applyChange(new AddAxiom(onto, ax1));
				
//				OWLAnnotation annotAction = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("Action_" + codeAction, "fr"));
//				OWLAxiom axiomAction = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Action_" + codeAction), annotAction);
//				manager.applyChange(new AddAxiom(onto, axiomAction));
				
				OWLAnnotation annotAction1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("Action", "fr"));
				OWLAxiom axiomAction1 = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Action"), annotAction1);
				manager.applyChange(new AddAxiom(onto, axiomAction1));
				
				OWLSubClassOfAxiom ax2 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Action")),
						fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/AxeActe")));
				manager.applyChange(new AddAxiom(onto, ax2));
				
				
			OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(notation, fact.getOWLLiteral(codeAction));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(
					IRI.create("http://data.esante.gouv.fr/cnam/ccam/Action_" + codeAction), annot);
			manager.applyChange(new AddAxiom(onto, axiom));
			
			OWLAnnotationProperty verbeAction = new OWLAnnotationPropertyImpl(CCAMVocabulary.verbeAction.getIRI());
			OWLAnnotation annotverbeAction = fact.getOWLAnnotation(verbeAction, IRI.create("http://data.esante.gouv.fr/cnam/ccam/VerbeAction_" + StringOperation.sansAccents(reformeVerbe(verbe))));
			OWLAxiom axiomverbeAction = fact.getOWLAnnotationAssertionAxiom(
					IRI.create("http://data.esante.gouv.fr/cnam/ccam/Action_" + codeAction), annotverbeAction);
			manager.applyChange(new AddAxiom(onto, axiomverbeAction));
			
			OWLAnnotation annot1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(reformeVerbe(verbe), "fr"));
			OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot1);
			manager.applyChange(new AddAxiom(onto, axiom1));
			
			OWLAnnotationProperty note = new OWLAnnotationPropertyImpl(SKOSVocabulary.DEFINITION.getIRI());
			OWLAnnotation annot2 = fact.getOWLAnnotation(note, fact.getOWLLiteral(definition, "fr"));
			OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(
					IRI.create("http://data.esante.gouv.fr/cnam/ccam/VerbeAction_" + StringOperation.sansAccents(reformeVerbe(verbe))), annot2);
			manager.applyChange(new AddAxiom(onto, axiom2));
			
			
			OWLObjectProperty anntaPourVerbeAction = fact.getOWLObjectProperty(IRI.create("http://data.esante.gouv.fr/cnam/ccam/aPourVerbeAction"));
	        OWLClass head3 = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/VerbeAction_" + StringOperation.sansAccents(reformeVerbe(verbe))));
	        OWLClassExpression aPourVerbeAction = fact.getOWLObjectSomeValuesFrom(anntaPourVerbeAction, head3);
	        OWLSubClassOfAxiom ax4 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Action_" + codeAction)), aPourVerbeAction);
	        AddAxiom addAx3 = new AddAxiom(onto, ax4);
	        manager.applyChange(addAx3);
		
		
		for (String syno : synonymes.split("\n")) {
			
			OWLAnnotationProperty alt = new OWLAnnotationPropertyImpl(SKOSVocabulary.ALTLABEL.getIRI());
			OWLAnnotation annot3 = fact.getOWLAnnotation(alt, fact.getOWLLiteral(syno, "fr"));
			OWLAxiom axiom3 = fact.getOWLAnnotationAssertionAxiom(
					IRI.create("http://data.esante.gouv.fr/cnam/ccam/VerbeAction_" + StringOperation.sansAccents(reformeVerbe(verbe))), annot3);
			manager.applyChange(new AddAxiom(onto, axiom3));
		}
		}

		
	}
	
	
	private static void createCategorieVerbeAction() {
		logger.info("construction de la liste de categorie pour verbe d'action");
		try {
			ChargeMapping.chargeCSVCatigorieVerbeActionList(csvFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for (String[] row : ChargeMapping.listCatigorieVerbeAction) {
			String uri = row[0];
			String label = row[1];
			String not = row[2];
			String sup = row[3];

			OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + StringOperation.sansAccents(uri)));
			OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			manager.applyChange(new AddAxiom(onto, declare));
			
			OWLSubClassOfAxiom ax = fact.getOWLSubClassOfAxiom(owlClass,
						fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + StringOperation.sansAccents(sup))));
			manager.applyChange(new AddAxiom(onto, ax));
				
			OWLAnnotation annotAction = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(label, "fr"));
			OWLAxiom axiomAction = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotAction);
			manager.applyChange(new AddAxiom(onto, axiomAction));
			
			OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(notation, fact.getOWLLiteral(StringOperation.sansAccents(not)));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot);
			manager.applyChange(new AddAxiom(onto, axiom));
				
		}
	}
	
	private static String reformeVerbe(String verbe) {
		String result = "";
		result = verbe.substring(0, 1).toUpperCase();
		result += verbe.substring(1).toLowerCase();
		return result;
	}

}
