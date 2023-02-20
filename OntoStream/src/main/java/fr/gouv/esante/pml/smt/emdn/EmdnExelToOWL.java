/**
 * 
 */
package fr.gouv.esante.pml.smt.emdn;

import java.io.FileOutputStream;
import java.io.OutputStream;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.DCVocabulary;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.SkosVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

/**
 * @author gouch
 *
 */
public class EmdnExelToOWL {

	private static String emdnExcel = PropertiesUtil.getEMDNProperties("emdnExcel");
	private static String emdnOWL = PropertiesUtil.getEMDNProperties("emdnOWL");
	private static OWLOntologyManager manager = null;
	private static OWLOntology onto = null;
	private static OWLDataFactory fact = null;

	private static final Logger logger = LoggerFactory.getLogger(EmdnExelToOWL.class);

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		logger.info("Début de construction de la terminologie");
		manager = OWLManager.createOWLOntologyManager();

		onto = manager.createOntology(IRI.create("http://data.esante.gouv.fr/ec/emdn"));

		fact = onto.getOWLOntologyManager().getOWLDataFactory();

		OWLClass owlClassracine = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/ec/emdn/racine"));
		OWLAxiom declareracine = fact.getOWLDeclarationAxiom(owlClassracine);
		manager.applyChange(new AddAxiom(onto, declareracine));

		OWLAnnotationProperty notationracine = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
		OWLAnnotation annotracine0 = fact.getOWLAnnotation(notationracine, fact.getOWLLiteral("EMDN"));
		OWLAxiom axiomracine0 = fact
				.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/ec/emdn/racine"), annotracine0);
		manager.applyChange(new AddAxiom(onto, axiomracine0));

		OWLAnnotation annotracine = fact.getOWLAnnotation(fact.getRDFSLabel(),
				fact.getOWLLiteral("European Medical Device Nomenclature", "en"));
		OWLAxiom axiomracine = fact.getOWLAnnotationAssertionAxiom(owlClassracine.getIRI(), annotracine);
		manager.applyChange(new AddAxiom(onto, axiomracine));

		OWLAnnotation annotracine1 = fact.getOWLAnnotation(fact.getRDFSLabel(),
				fact.getOWLLiteral("Nomenclature Européenne des Dispositifs Médicaux", "fr"));
		OWLAxiom axiomracine1 = fact.getOWLAnnotationAssertionAxiom(owlClassracine.getIRI(), annotracine1);
		manager.applyChange(new AddAxiom(onto, axiomracine1));

		ChargeMapping.chargeXLXSEMDNCodeFileToList(emdnExcel);
		for (String id : ChargeMapping.listCodeEMDN.keySet()) {
			String data = ChargeMapping.listCodeEMDN.get(id);
			if (!data.trim().isEmpty()) {
				
				String code = data.split("£")[3];
			
			 if(code.trim()!=null  && !code.trim().isEmpty() ) {	
				
				 System.out.println("** Codelen "+code.length());
				System.out.println("** Code "+code);
				 
				String frLabel = data.split("£")[5];
				String enLabel = data.split("£")[4];

				OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/ec/emdn/" + code));
				OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
				manager.applyChange(new AddAxiom(onto, declare));

				OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
				OWLAnnotation annot0 = fact.getOWLAnnotation(notation, fact.getOWLLiteral(code));
				OWLAxiom axiom0 = fact.getOWLAnnotationAssertionAxiom(
						IRI.create("http://data.esante.gouv.fr/ec/emdn/" + code), annot0);
				manager.applyChange(new AddAxiom(onto, axiom0));

				OWLAnnotation annot1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(frLabel, "fr"));
				OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot1);
				manager.applyChange(new AddAxiom(onto, axiom1));

				OWLAnnotation annot2 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(enLabel, "en"));
				OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot2);
				manager.applyChange(new AddAxiom(onto, axiom2));

				OWLAnnotationProperty type = new OWLAnnotationPropertyImpl(DCVocabulary.type.getIRI());

				if(code.trim().length() == 1) {
					OWLAnnotation annottype1 = fact.getOWLAnnotation(type, fact.getOWLLiteral("CATEGORY"));
					OWLAxiom axiomtype1 = fact.getOWLAnnotationAssertionAxiom(
							IRI.create("http://data.esante.gouv.fr/ec/emdn/" + code), annottype1);
					manager.applyChange(new AddAxiom(onto, axiomtype1));

					OWLSubClassOfAxiom ax1 = fact.getOWLSubClassOfAxiom(owlClass, owlClassracine);
					manager.applyChange(new AddAxiom(onto, ax1));
				}else if(code.trim().length() == 3) {
					OWLAnnotation annottype2 = fact.getOWLAnnotation(type, fact.getOWLLiteral("GROUP"));
					OWLAxiom axiomtype2 = fact.getOWLAnnotationAssertionAxiom(
							IRI.create("http://data.esante.gouv.fr/ec/emdn/" + code), annottype2);
					manager.applyChange(new AddAxiom(onto, axiomtype2));
					
					OWLSubClassOfAxiom ax2 = fact.getOWLSubClassOfAxiom(owlClass, fact.getOWLClass(
							IRI.create("http://data.esante.gouv.fr/ec/emdn/" + code.trim().substring(0, code.trim().length() - 2))));
					manager.applyChange(new AddAxiom(onto, ax2));
				} else if(code.trim().length() == 5) {
					OWLAnnotation annottype3 = fact.getOWLAnnotation(type, fact.getOWLLiteral("TYPE Level 1"));
					OWLAxiom axiomtype3 = fact.getOWLAnnotationAssertionAxiom(
							IRI.create("http://data.esante.gouv.fr/ec/emdn/" + code), annottype3);
					manager.applyChange(new AddAxiom(onto, axiomtype3));

					OWLSubClassOfAxiom ax3 = fact.getOWLSubClassOfAxiom(owlClass, fact.getOWLClass(
							IRI.create("http://data.esante.gouv.fr/ec/emdn/" + code.trim().substring(0, code.trim().length() - 2))));
					manager.applyChange(new AddAxiom(onto, ax3));
				} else if(code.trim().length() == 7) {
					OWLAnnotation annottype3 = fact.getOWLAnnotation(type, fact.getOWLLiteral("TYPE Level 2"));
					OWLAxiom axiomtype3 = fact.getOWLAnnotationAssertionAxiom(
							IRI.create("http://data.esante.gouv.fr/ec/emdn/" + code), annottype3);
					manager.applyChange(new AddAxiom(onto, axiomtype3));

					OWLSubClassOfAxiom ax3 = fact.getOWLSubClassOfAxiom(owlClass, fact.getOWLClass(
							IRI.create("http://data.esante.gouv.fr/ec/emdn/" + code.trim().substring(0, code.trim().length() - 2))));
					manager.applyChange(new AddAxiom(onto, ax3));
				} else if(code.trim().length() == 9) {
					OWLAnnotation annottype3 = fact.getOWLAnnotation(type, fact.getOWLLiteral("TYPE Level 3"));
					OWLAxiom axiomtype3 = fact.getOWLAnnotationAssertionAxiom(
							IRI.create("http://data.esante.gouv.fr/ec/emdn/" + code), annottype3);
					manager.applyChange(new AddAxiom(onto, axiomtype3));

					OWLSubClassOfAxiom ax3 = fact.getOWLSubClassOfAxiom(owlClass, fact.getOWLClass(
							IRI.create("http://data.esante.gouv.fr/ec/emdn/" + code.trim().substring(0, code.trim().length() - 2))));
					manager.applyChange(new AddAxiom(onto, ax3));
				} else if(code.trim().length() == 11) {
					OWLAnnotation annottype3 = fact.getOWLAnnotation(type, fact.getOWLLiteral("TYPE Level 4"));
					OWLAxiom axiomtype3 = fact.getOWLAnnotationAssertionAxiom(
							IRI.create("http://data.esante.gouv.fr/ec/emdn/" + code), annottype3);
					manager.applyChange(new AddAxiom(onto, axiomtype3));

					OWLSubClassOfAxiom ax3 = fact.getOWLSubClassOfAxiom(owlClass, fact.getOWLClass(
							IRI.create("http://data.esante.gouv.fr/ec/emdn/" + code.trim().substring(0, code.trim().length() - 2))));
					manager.applyChange(new AddAxiom(onto, ax3));
				} else if(code.trim().length() == 13) {
					OWLAnnotation annottype3 = fact.getOWLAnnotation(type, fact.getOWLLiteral("TYPE Level 5"));
					OWLAxiom axiomtype3 = fact.getOWLAnnotationAssertionAxiom(
							IRI.create("http://data.esante.gouv.fr/ec/emdn/" + code), annottype3);
					manager.applyChange(new AddAxiom(onto, axiomtype3));

					OWLSubClassOfAxiom ax3 = fact.getOWLSubClassOfAxiom(owlClass, fact.getOWLClass(
							IRI.create("http://data.esante.gouv.fr/ec/emdn/" + code.trim().substring(0, code.trim().length() - 2))));
					manager.applyChange(new AddAxiom(onto, ax3));
				}
				
			}
			 
			}
		}

		final OutputStream fileoutputstream = new FileOutputStream(emdnOWL);
		final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
		manager.saveOntology(onto, ontologyFormat, fileoutputstream);

	}

}
