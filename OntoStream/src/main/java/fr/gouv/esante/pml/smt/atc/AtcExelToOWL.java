/**
 * 
 */
package fr.gouv.esante.pml.smt.atc;

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
public class AtcExelToOWL {

	private static String atcExcel = PropertiesUtil.getATCProperties("atcExcel");
	private static String atcOWL = PropertiesUtil.getATCProperties("atcOWL");
	private static OWLOntologyManager manager = null;
	private static OWLOntology onto = null;
	private static OWLDataFactory fact = null;

	private static final Logger logger = LoggerFactory.getLogger(AtcExelToOWL.class);

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		logger.info("Début de construction de la terminologie");
		manager = OWLManager.createOWLOntologyManager();

		onto = manager.createOntology(IRI.create("http://data.esante.gouv.fr/whocc/atc"));

		fact = onto.getOWLOntologyManager().getOWLDataFactory();

		OWLClass owlClassracine = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/whocc/atc/racine"));
		OWLAxiom declareracine = fact.getOWLDeclarationAxiom(owlClassracine);
		manager.applyChange(new AddAxiom(onto, declareracine));

		OWLAnnotationProperty notationracine = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
		OWLAnnotation annotracine0 = fact.getOWLAnnotation(notationracine, fact.getOWLLiteral("ATC"));
		OWLAxiom axiomracine0 = fact
				.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/whocc/atc/racine"), annotracine0);
		manager.applyChange(new AddAxiom(onto, axiomracine0));

		OWLAnnotation annotracine = fact.getOWLAnnotation(fact.getRDFSLabel(),
				fact.getOWLLiteral("Anatomique, Thérapeutique et Chimique", "fr"));
		OWLAxiom axiomracine = fact.getOWLAnnotationAssertionAxiom(owlClassracine.getIRI(), annotracine);
		manager.applyChange(new AddAxiom(onto, axiomracine));
		
		OWLAnnotation annotracineEn = fact.getOWLAnnotation(fact.getRDFSLabel(),
				fact.getOWLLiteral("Anatomical Therapeutic Chemical", "en"));
		OWLAxiom axiomracineEn = fact.getOWLAnnotationAssertionAxiom(owlClassracine.getIRI(), annotracineEn);
		manager.applyChange(new AddAxiom(onto, axiomracineEn));

		
		ChargeMapping.chargeXLXSATCCodeFileToList(atcExcel);
		for (String id : ChargeMapping.listCodeATC.keySet()) {
			String labelFR = ChargeMapping.listCodeATC.get(id).split("£")[0];
			String labelEN = ChargeMapping.listCodeATC.get(id).split("£")[1];
			if (!labelFR.trim().isEmpty()) {

				OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/whocc/atc/" + id));
				OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
				manager.applyChange(new AddAxiom(onto, declare));

				OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
				OWLAnnotation annot0 = fact.getOWLAnnotation(notation, fact.getOWLLiteral(id));
				OWLAxiom axiom0 = fact.getOWLAnnotationAssertionAxiom(
						IRI.create("http://data.esante.gouv.fr/whocc/atc/" + id), annot0);
				manager.applyChange(new AddAxiom(onto, axiom0));

				OWLAnnotation annot1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(labelFR, "fr"));
				OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot1);
				manager.applyChange(new AddAxiom(onto, axiom1));
				
				OWLAnnotation annotEN = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(labelEN, "en"));
				OWLAxiom axiomEN = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotEN);
				manager.applyChange(new AddAxiom(onto, axiomEN));

				OWLAnnotationProperty type = new OWLAnnotationPropertyImpl(DCVocabulary.type.getIRI());

				if(id.trim().length() == 1) {
					OWLSubClassOfAxiom ax1 = fact.getOWLSubClassOfAxiom(owlClass, owlClassracine);
					manager.applyChange(new AddAxiom(onto, ax1));
				}else if(id.trim().length() == 3) {
					OWLAnnotation annottype2 = fact.getOWLAnnotation(type, fact.getOWLLiteral("Classe Thérapeutique"));
					OWLAxiom axiomtype2 = fact.getOWLAnnotationAssertionAxiom(
							IRI.create("http://data.esante.gouv.fr/whocc/atc/" + id), annottype2);
					manager.applyChange(new AddAxiom(onto, axiomtype2));
					
					OWLSubClassOfAxiom ax2 = fact.getOWLSubClassOfAxiom(owlClass, fact.getOWLClass(
							IRI.create("http://data.esante.gouv.fr/whocc/atc/" + id.trim().substring(0, id.trim().length() - 2))));
					manager.applyChange(new AddAxiom(onto, ax2));
				} else if(id.trim().length() == 4) {
					OWLSubClassOfAxiom ax3 = fact.getOWLSubClassOfAxiom(owlClass, fact.getOWLClass(
							IRI.create("http://data.esante.gouv.fr/whocc/atc/" + id.trim().substring(0, id.trim().length() - 1))));
					manager.applyChange(new AddAxiom(onto, ax3));
				} else if(id.trim().length() == 5) {
					OWLSubClassOfAxiom ax3 = fact.getOWLSubClassOfAxiom(owlClass, fact.getOWLClass(
							IRI.create("http://data.esante.gouv.fr/whocc/atc/" + id.trim().substring(0, id.trim().length() - 1))));
					manager.applyChange(new AddAxiom(onto, ax3));
				} else if(id.trim().length() == 7) {
					OWLAnnotation annottype3 = fact.getOWLAnnotation(type, fact.getOWLLiteral("DCI"));
					OWLAxiom axiomtype3 = fact.getOWLAnnotationAssertionAxiom(
							IRI.create("http://data.esante.gouv.fr/whocc/atc/" + id), annottype3);
					manager.applyChange(new AddAxiom(onto, axiomtype3));

					OWLSubClassOfAxiom ax3 = fact.getOWLSubClassOfAxiom(owlClass, fact.getOWLClass(
							IRI.create("http://data.esante.gouv.fr/whocc/atc/" + id.trim().substring(0, id.trim().length() - 2))));
					manager.applyChange(new AddAxiom(onto, ax3));
				} 
				
			}
		}

		final OutputStream fileoutputstream = new FileOutputStream(atcOWL);
		final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
		manager.saveOntology(onto, ontologyFormat, fileoutputstream);

	}

}
