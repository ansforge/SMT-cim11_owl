package fr.gouv.esante.pml.smt.ccam;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import fr.gouv.esante.pml.smt.ccam.data.GetActe;
import fr.gouv.esante.pml.smt.ccam.data.GetActeNote;
import fr.gouv.esante.pml.smt.ccam.data.GetActeType;
import fr.gouv.esante.pml.smt.ccam.data.GetActesAssociations;
import fr.gouv.esante.pml.smt.ccam.data.GetActesIncompatibles;
import fr.gouv.esante.pml.smt.ccam.data.GetActesProcedures;
import fr.gouv.esante.pml.smt.ccam.data.GetActivite;
import fr.gouv.esante.pml.smt.ccam.data.GetActiviteActe;
import fr.gouv.esante.pml.smt.ccam.data.GetActiviteModificateur;
import fr.gouv.esante.pml.smt.ccam.data.GetDent;
import fr.gouv.esante.pml.smt.ccam.data.GetDentIncompatible;
import fr.gouv.esante.pml.smt.ccam.data.GetMenu;
import fr.gouv.esante.pml.smt.ccam.data.GetMenuNote;
import fr.gouv.esante.pml.smt.ccam.data.GetModificateur;
import fr.gouv.esante.pml.smt.ccam.data.GetPhase;
import fr.gouv.esante.pml.smt.ccam.data.GetPhaseActiviteActe;
import fr.gouv.esante.pml.smt.ccam.data.GetRegroupement;
import fr.gouv.esante.pml.smt.ccam.models.Acte;
import fr.gouv.esante.pml.smt.ccam.models.ActeActivite;
import fr.gouv.esante.pml.smt.ccam.models.ActeNote;
import fr.gouv.esante.pml.smt.ccam.models.ActeProcedure;
import fr.gouv.esante.pml.smt.ccam.models.ActiviteModificateur;
import fr.gouv.esante.pml.smt.ccam.models.Association;
import fr.gouv.esante.pml.smt.ccam.models.Menu;
import fr.gouv.esante.pml.smt.ccam.models.MenuNote;
import fr.gouv.esante.pml.smt.ccam.models.Modificateur;
import fr.gouv.esante.pml.smt.ccam.models.Note;
import fr.gouv.esante.pml.smt.utils.ANSICD11Vocabulary;
import fr.gouv.esante.pml.smt.utils.CCAMVocabulary;
import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.DCTVocabulary;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.SkosVocabulary;
import fr.gouv.esante.pml.smt.utils.XSkosVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

public class ModelingOntologyCCAM {

	private static Map<Integer, Menu> menuListe = new HashMap<Integer, Menu>();
	private static Map<Integer, MenuNote> notesListe = new HashMap<Integer, MenuNote>();
	private static Map<String, Acte> acteListe = new HashMap<String, Acte>();
	private static Map<Integer, String> menuCodeRang = new HashMap<Integer, String>();
	private static Map<String, Modificateur> modificateurListe = new HashMap<String, Modificateur>();
	private static Map<String, ActeNote> acteNoteListe = new HashMap<String, ActeNote>();
	private static Map<Integer, String> phaseListe = new HashMap<Integer, String>();
	private static Map<String, String> activiteListe = new HashMap<String, String>();
	private static Map<String, String> regroupementListe = new HashMap<String, String>();
	private static Map<String, ActeActivite> activiteActeListe = new HashMap<String, ActeActivite>();
	private static Map<String, String> phaseActiviteActeListe = new HashMap<String, String>();
	private static List<ActiviteModificateur> activiteModificateurListe = new ArrayList<ActiviteModificateur>();
	private static List<String> actesIncompatibles = new ArrayList<String>();
	private static List<ActeProcedure> actesProcedures = new ArrayList<ActeProcedure>();
	private static List<Association> actesAssociations = new ArrayList<Association>();

	private static OWLDataFactory fact = null;
	private static InputStream input = null;
	private static String OWLFile = PropertiesUtil.getCCAMProperties("ccamOWL");
	private static String coreOWLFile = PropertiesUtil.getCCAMProperties("ccamCoreOWL");
	private static String topographieOWLFile = PropertiesUtil.getCCAMProperties("ccamTopographieOWL");
	private static String actionOWLFile = PropertiesUtil.getCCAMProperties("ccamCodeActionOWL");
	private static String modeAccesOWLFile = PropertiesUtil.getCCAMProperties("ccamModeAccesOWL");
	private static OWLOntologyManager manager = null;
	private static OWLOntology onto = null;
	
	private static final Logger logger = LoggerFactory.getLogger(ModelingOntologyCCAM.class);

	public static void main(final String[] args) throws Exception {

		logger.info("Début de construction de la terminologie");
		manager = OWLManager.createOWLOntologyManager();
		
		onto = manager.createOntology(IRI.create("http://data.esante.gouv.fr/cnam/ccam"));

		fact = onto.getOWLOntologyManager().getOWLDataFactory();
		
		

		getOntologieCore();
		constructionHierarchie();
		createChaptres();
		getTopographie();
		getModeAcces();
		getAction();
		createActivite();
		createPhase();
		createRegroupement();
		createActeType();
		createActe();
		createActiviteActe();
		createPhaseActiviteActe();
		createModificateur();
		createActiviteModificateur();
		createAssociations();
		createProcedures();
		createDent();
		createActivitePhaseDent();
//		createIncompatibilites();
//		cleanning();

		final OutputStream fileoutputstream = new FileOutputStream(OWLFile);
		final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
		manager.saveOntology(onto, ontologyFormat, fileoutputstream);

	}
	
    private static void getOntologieCore() throws FileNotFoundException, OWLOntologyCreationException {
    	logger.info("importer le core de la terminologie");
		input = new FileInputStream(coreOWLFile);
	    manager = OWLManager.createOWLOntologyManager();
	    OWLOntology core = manager.loadOntologyFromOntologyDocument(input);
	    
	    core.axioms().
	    forEach(ax -> {
	    	manager.applyChange(new AddAxiom(onto, ax));
	    	});
	    logger.info("fin import du core de la terminologie");
	}

	
    private static void constructionHierarchie() {
    	OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/racine"));
		OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
		manager.applyChange(new AddAxiom(onto, declare));
		
		OWLAnnotation annot = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("CCAM", "fr"));
		OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot);
		manager.applyChange(new AddAxiom(onto, axiom));
		
		OWLSubClassOfAxiom ax1 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Acte")),
				fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/racine")));
		manager.applyChange(new AddAxiom(onto, ax1));
		
		OWLAnnotation annotActe = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("Acte", "fr"));
		OWLAxiom axiomActe = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Acte"), annotActe);
		manager.applyChange(new AddAxiom(onto, axiomActe));
		
		OWLSubClassOfAxiom ax2 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/AxeActe")),
				fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/racine")));
		manager.applyChange(new AddAxiom(onto, ax2));
		
		OWLAnnotation annotAxeActe = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("AxeActe", "fr"));
		OWLAxiom axiomAxeActe = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/AxeActe"), annotAxeActe);
		manager.applyChange(new AddAxiom(onto, axiomAxeActe));
		
		OWLSubClassOfAxiom ax3 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ActeActivite")),
				fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/racine")));
		manager.applyChange(new AddAxiom(onto, ax3));
//		OWLSubClassOfAxiom ax4 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/PartieActe")),
//				fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/racine")));
//		manager.applyChange(new AddAxiom(onto, ax4));
//		
		OWLSubClassOfAxiom ax5 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ActeActivitePhase")),
				fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/racine")));
		manager.applyChange(new AddAxiom(onto, ax5));
		
		OWLAnnotation annotActeActivite = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("ActeActivite", "fr"));
		OWLAxiom axiomActeActivite = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ActeActivite"), annotActeActivite);
		manager.applyChange(new AddAxiom(onto, axiomActeActivite));
		
//		OWLAnnotation annotPartieActe = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("PartieActe", "fr"));
//		OWLAxiom axiomPartieActe = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/PartieActe"), annotPartieActe);
//		manager.applyChange(new AddAxiom(onto, axiomPartieActe));
		
		OWLAnnotation annotActeActivitePhase = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("ActeActivitePhase", "fr"));
		OWLAxiom axiomActeActivitePhase = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ActeActivitePhase"), annotActeActivitePhase);
		manager.applyChange(new AddAxiom(onto, axiomActeActivitePhase));
		
		OWLSubClassOfAxiom ax6 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/CodeComplementaire")),
				fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/racine")));
		manager.applyChange(new AddAxiom(onto, ax6));
		
		OWLAnnotation annotCodeComplementaire = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("CodeComplementaire", "fr"));
		OWLAxiom axiomCodeComplementaire = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/CodeComplementaire"), annotCodeComplementaire);
		manager.applyChange(new AddAxiom(onto, axiomCodeComplementaire));
		
		OWLAnnotation annotModificateur = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("Modificateur", "fr"));
		OWLAxiom axiomModificateur = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Modificateur"), annotModificateur);
		manager.applyChange(new AddAxiom(onto, axiomModificateur));
		
		OWLSubClassOfAxiom ax7 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Liste")),
				fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/racine")));
		manager.applyChange(new AddAxiom(onto, ax7));
		
		OWLSubClassOfAxiom ax8 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/VerbeAction")),
				fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/racine")));
		manager.applyChange(new AddAxiom(onto, ax8));
		
		OWLAnnotation annotListe = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("Liste", "fr"));
		OWLAxiom axiomListe = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Liste"), annotListe);
		manager.applyChange(new AddAxiom(onto, axiomListe));
//		OWLAnnotation annotListeOptionnelle = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("ListeOptionnelle", "fr"));
//		OWLAxiom axiomListeOptionnelle = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ListeOptionnelle"), annotListeOptionnelle);
//		manager.applyChange(new AddAxiom(onto, axiomListeOptionnelle));
//		OWLAnnotation annotListeObligatoire = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("ListeObligatoire", "fr"));
//		OWLAxiom axiomListeObligatoire = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ListeObligatoire"), annotListeObligatoire);
//		manager.applyChange(new AddAxiom(onto, axiomListeObligatoire));
		OWLAnnotation annotVerbeAction = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("VerbeAction", "fr"));
		OWLAxiom axiomVerbeAction = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/VerbeAction"), annotVerbeAction);
		manager.applyChange(new AddAxiom(onto, axiomVerbeAction));
    }
    
    
    private static void createChaptres() {
    	logger.info("début de la construction des chapitres de la terminologie");
		// add chaptre class
		GetMenu getMenu = new GetMenu();
		menuListe = getMenu.getMenuListe();
		
		for (Integer id : menuListe.keySet()) {
			Menu menu = menuListe.get(id);
			String rang = String.format("%02d", menu.getRang()) + ".";
			if(menu.getId_menu() > 1) {
			Menu menuPere = menuListe.get(menu.getId_menu_sup());
			while(menuPere.getId_menu() > 1) {
				rang = String.format("%02d", menuPere.getRang()) + "." + rang;
				menuPere = menuListe.get(menuPere.getId_menu_sup());
			}
			}
			rang = rang.substring(0, rang.length()-1);
			menuCodeRang.put(id, rang);
		}

		for (Integer id : menuListe.keySet()) {
			Menu menu = menuListe.get(id);
			String rang = menuCodeRang.get(id);
			
			if(!rang.contains("19.03")) {
			OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Categorie_" + rang));
			OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			manager.applyChange(new AddAxiom(onto, declare));

			
			if (menu.getId_menu_sup() > 0) {
				OWLSubClassOfAxiom ax = fact.getOWLSubClassOfAxiom(owlClass,
						fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Categorie_" + menuCodeRang.get(menu.getId_menu_sup()))));
				manager.applyChange(new AddAxiom(onto, ax));
			}else {
				OWLSubClassOfAxiom ax = fact.getOWLSubClassOfAxiom(owlClass,
						fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Acte")));
				manager.applyChange(new AddAxiom(onto, ax));
				
			}
			
			
			OWLAnnotation annot = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(rang + " " +menu.getLabel(), "fr"));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot);
			manager.applyChange(new AddAxiom(onto, axiom));
			
			OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
			OWLAnnotation annot1 = fact.getOWLAnnotation(notation, fact.getOWLLiteral(rang));
			OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(
					owlClass.getIRI(), annot1);
			manager.applyChange(new AddAxiom(onto, axiom1));
			}
		}
		
		logger.info("construction des annotations des chapitres de la terminologie");
		// add chaptre notes
		GetMenuNote getMenuNote = new GetMenuNote();
		notesListe = getMenuNote.getMenuNoteListe();

		for (Integer id : notesListe.keySet()) {

			MenuNote menu = notesListe.get(id);

			String noteValue = "";
			String inclusionValue = "";
			String exclusionValue = "";
			String exempleValue = "";
			String definitionValue = "";
			String scopeNoteValue = "";

			for (Integer id_note : menu.getNotes().keySet()) {
				Note nt = menu.getNotes().get(id_note);
				switch(nt.getTypnot_cod()) {
				case 1:
					exclusionValue += nt.getTexte_note();
					break;
				case 2:
					inclusionValue += nt.getTexte_note() + "\n";
					break;
				case 3:
					inclusionValue += nt.getTexte_note() + "\n";
					break;
				case 4:
					definitionValue += nt.getTexte_note()+ "\n";
					break;
				case 5:
					exempleValue += nt.getTexte_note()+ "\n";
					break;
				case 6:
					noteValue += nt.getTexte_note() + "\n";
					break;
				case 8:
					inclusionValue += nt.getTexte_note() + "\n";
					break;
				case 9:
					noteValue += nt.getTexte_note() + "\n";
					break;
				case 13:
					scopeNoteValue += nt.getTexte_note() + "\n";
					break;
				case 14:
					scopeNoteValue += nt.getTexte_note() + "\n";
					break;
				case 15:
					scopeNoteValue += nt.getTexte_note() + "\n";
					break;
				case 16:
					if(!nt.getTexte_note().isEmpty()) {
					OWLAnnotationProperty receuilProspectifDeDonnees = new OWLAnnotationPropertyImpl(CCAMVocabulary.receuilProspectifDeDonnees.getIRI());
			    	OWLAnnotation annot2 = fact.getOWLAnnotation(receuilProspectifDeDonnees, fact.getOWLLiteral(nt.getTexte_note().replace("û ", "– "), "fr"));
					OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Categorie_" + menuCodeRang.get(id)), annot2);
					manager.applyChange(new AddAxiom(onto, axiom2));
					}
					break;
				case 17:
					if(!nt.getTexte_note().isEmpty() && !menuCodeRang.get(id).contains("19.03")) {
					OWLAnnotationProperty facturation = new OWLAnnotationPropertyImpl(CCAMVocabulary.facturation.getIRI());
			    	OWLAnnotation annot2 = fact.getOWLAnnotation(facturation, fact.getOWLLiteral(nt.getTexte_note().replace("û ", "– "), "fr"));
					OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Categorie_" + menuCodeRang.get(id)), annot2);
					manager.applyChange(new AddAxiom(onto, axiom2));
					}
					break;	
				}
			}
			if(menuCodeRang.containsKey(id) && !menuCodeRang.get(id).contains("19.03")) {
			if (!StringUtils.isBlank(noteValue)) {
				OWLAnnotationProperty classNote = new OWLAnnotationPropertyImpl(SKOSVocabulary.NOTE.getIRI());
				OWLAnnotation annot = fact.getOWLAnnotation(classNote, fact.getOWLLiteral(noteValue.replace("û ", "– "), "fr"));
				OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(
						IRI.create("http://data.esante.gouv.fr/cnam/ccam/Categorie_" + menuCodeRang.get(id)), annot);
				manager.applyChange(new AddAxiom(onto, axiom));
			}
			if (!StringUtils.isBlank(exempleValue)) {
				OWLAnnotationProperty classNote = new OWLAnnotationPropertyImpl(SKOSVocabulary.EXAMPLE.getIRI());
				OWLAnnotation annot = fact.getOWLAnnotation(classNote, fact.getOWLLiteral(exempleValue.replace("û ", "– "), "fr"));
				OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(
						IRI.create("http://data.esante.gouv.fr/cnam/ccam/Categorie_" + menuCodeRang.get(id)), annot);
				manager.applyChange(new AddAxiom(onto, axiom));
			}
			if (!StringUtils.isBlank(definitionValue)) {
				OWLAnnotationProperty classNote = new OWLAnnotationPropertyImpl(SKOSVocabulary.DEFINITION.getIRI());
				OWLAnnotation annot = fact.getOWLAnnotation(classNote, fact.getOWLLiteral(definitionValue.replace("û ", "– "), "fr"));
				OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(
						IRI.create("http://data.esante.gouv.fr/cnam/ccam/Categorie_" + menuCodeRang.get(id)), annot);
				manager.applyChange(new AddAxiom(onto, axiom));
			}
			if (!StringUtils.isBlank(inclusionValue)) {
				OWLAnnotationProperty classNote = new OWLAnnotationPropertyImpl(XSkosVocabulary.inclusionNote.getIRI());
				OWLAnnotation annot = fact.getOWLAnnotation(classNote, fact.getOWLLiteral(inclusionValue.replace("û ", "– "), "fr"));
				OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(
						IRI.create("http://data.esante.gouv.fr/cnam/ccam/Categorie_" + menuCodeRang.get(id)), annot);
				manager.applyChange(new AddAxiom(onto, axiom));
			}
			if (!StringUtils.isBlank(exclusionValue)) {
				OWLAnnotationProperty classNote = new OWLAnnotationPropertyImpl(XSkosVocabulary.exclusionNote.getIRI());
				OWLAnnotation annot = fact.getOWLAnnotation(classNote, fact.getOWLLiteral(exclusionValue.replace("û ", "– "), "fr"));
				OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(
						IRI.create("http://data.esante.gouv.fr/cnam/ccam/Categorie_" + menuCodeRang.get(id)), annot);
				manager.applyChange(new AddAxiom(onto, axiom));
			}
			if (!StringUtils.isBlank(scopeNoteValue)) {
				OWLAnnotationProperty scopeNote = new OWLAnnotationPropertyImpl(SKOSVocabulary.SCOPENOTE.getIRI());
				OWLAnnotation annot = fact.getOWLAnnotation(scopeNote, fact.getOWLLiteral(scopeNoteValue.replace("û ", "– "), "fr"));
				OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(
						IRI.create("http://data.esante.gouv.fr/cnam/ccam/Categorie_" + menuCodeRang.get(id)), annot);
				manager.applyChange(new AddAxiom(onto, axiom));
			}
			}

		}
		logger.info("fin de la construction des chapitres de la terminologie");
	}
	
	
	
	private static void createActe() {
		logger.info("début de la construction des actes de la terminologie");
		// add acte class
		GetActe getActe = new GetActe();
		acteListe = getActe.getActeListe();
		
		GetActeNote getActeNote = new GetActeNote();
		acteNoteListe = getActeNote.getActeNoteListe();
		String acteType[] = {"ActeIsole", "Procedure", "ActeComplementaire"};

		for (String code : acteListe.keySet()) {
			OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + code));
			OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			manager.applyChange(new AddAxiom(onto, declare));
			
			Acte acte = acteListe.get(code);

			OWLSubClassOfAxiom ax = fact.getOWLSubClassOfAxiom(owlClass,
						fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Categorie_" + menuCodeRang.get(acte.getMenu_cod()))));
				manager.applyChange(new AddAxiom(onto, ax));
			
				OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
				OWLAnnotation annot0 = fact.getOWLAnnotation(notation, fact.getOWLLiteral(code));
				OWLAxiom axiom0 = fact.getOWLAnnotationAssertionAxiom(
						IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + code), annot0);
				manager.applyChange(new AddAxiom(onto, axiom0));	
			
		
			OWLAnnotation annot1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(acte.getLabel(), "fr"));
			OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot1);
			manager.applyChange(new AddAxiom(onto, axiom1));
			
			String topographie = code.substring(0, 2);
			String action = code.substring(2, 3);
			String mode = code.substring(3, 4);
			
			
			// association acte/topographie
			OWLObjectProperty aPourTopographie = fact.getOWLObjectProperty(IRI.create("http://data.esante.gouv.fr/cnam/ccam/aPourTopographie"));
	        OWLClass head = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Topographie_" + topographie));
	        OWLClassExpression hasPartSomeTopographie = fact.getOWLObjectSomeValuesFrom(aPourTopographie, head);
//	        OWLIndividual indivTopographie = fact.getOWLNamedIndividual(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Topographie_" + topographie));
//	        OWLObjectOneOf onlyTop = fact.getOWLObjectAllValuesFrom(property, classExpression)OneOf(indivTopographie);
	        OWLObjectAllValuesFrom hasOnlyTop = fact.getOWLObjectAllValuesFrom(aPourTopographie, head);
//	        OWLSubClassOfAxiom ax1 = fact.getOWLSubClassOfAxiom(owlClass, hasPartSomeTopographie);
//	        AddAxiom addAx = new AddAxiom(onto, ax1);
//	        manager.applyChange(addAx);
	        
	        OWLAnnotationProperty topo = new OWLAnnotationPropertyImpl(CCAMVocabulary.topographie.getIRI());
			OWLAnnotation annotTopo = fact.getOWLAnnotation(topo, IRI.create("http://data.esante.gouv.fr/cnam/ccam/Topographie_" + topographie));
			OWLAxiom topoAxiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotTopo);
			manager.applyChange(new AddAxiom(onto, topoAxiom));	
	        
	        // association acte/action
	        OWLObjectProperty aPourAction = fact.getOWLObjectProperty(IRI.create("http://data.esante.gouv.fr/cnam/ccam/aPourAction"));
	        OWLClass head1 = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Action_" + action));
	        OWLClassExpression hasPartSomeAction = fact.getOWLObjectSomeValuesFrom(aPourAction, head1);
//	        OWLIndividual indivAction = fact.getOWLNamedIndividual(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Action_" + action));
//	        OWLObjectOneOf onlyAction = fact.getOWLObjectOneOf(indivAction);
	        OWLObjectAllValuesFrom hasOnlyAction = fact.getOWLObjectAllValuesFrom(aPourAction, head1);
//	        OWLSubClassOfAxiom ax2 = fact.getOWLSubClassOfAxiom(owlClass, hasPartSomeAction);
//	        AddAxiom addAx1 = new AddAxiom(onto, ax2);
//	        manager.applyChange(addAx1);
	        
	        OWLAnnotationProperty act = new OWLAnnotationPropertyImpl(CCAMVocabulary.action.getIRI());
			OWLAnnotation annotAct = fact.getOWLAnnotation(act, IRI.create("http://data.esante.gouv.fr/cnam/ccam/Action_" + action));
			OWLAxiom actAxiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotAct);
			manager.applyChange(new AddAxiom(onto, actAxiom));	
	        
	        // association acte/modeAcces
	        OWLObjectProperty aPourModeAcces = fact.getOWLObjectProperty(IRI.create("http://data.esante.gouv.fr/cnam/ccam/aPourModeAcces"));
	        OWLClass head2 = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ModeAcces_" + mode));
	        OWLClassExpression hasPartSomeModeAcces = fact.getOWLObjectSomeValuesFrom(aPourModeAcces, head2);
//	        OWLIndividual indivModeAcces = fact.getOWLNamedIndividual(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ModeAcces_" + mode));
//	        OWLObjectOneOf onlyModeAcces = fact.getOWLObjectOneOf(indivModeAcces);
	        OWLObjectAllValuesFrom hasOnlyModeAcces = fact.getOWLObjectAllValuesFrom(aPourModeAcces, head2);
//	        OWLSubClassOfAxiom ax3 = fact.getOWLSubClassOfAxiom(owlClass, hasPartSomeModeAcces);
//	        AddAxiom addAx2 = new AddAxiom(onto, ax3);
//	        manager.applyChange(addAx2);
			
	        OWLAnnotationProperty acces = new OWLAnnotationPropertyImpl(CCAMVocabulary.modeAcces.getIRI());
			OWLAnnotation annotacces = fact.getOWLAnnotation(acces, IRI.create("http://data.esante.gouv.fr/cnam/ccam/ModeAcces_" + mode));
			OWLAxiom accAxiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotacces);
			manager.applyChange(new AddAxiom(onto, accAxiom));
			
			OWLObjectIntersectionOf someTopActMod = fact.getOWLObjectIntersectionOf(hasPartSomeTopographie, hasPartSomeAction, hasPartSomeModeAcces);
			OWLSubClassOfAxiom ax3 = fact.getOWLSubClassOfAxiom(owlClass, someTopActMod);
	        AddAxiom addAx2 = new AddAxiom(onto, ax3);
	        manager.applyChange(addAx2);
	        
	        OWLObjectIntersectionOf onlyTopActMod = fact.getOWLObjectIntersectionOf(hasOnlyTop, hasOnlyAction, hasOnlyModeAcces);
			OWLSubClassOfAxiom ax2 = fact.getOWLSubClassOfAxiom(owlClass, onlyTopActMod);
	        AddAxiom addAx1 = new AddAxiom(onto, ax2);
	        manager.applyChange(addAx1);
			
			
			
			OWLAnnotationProperty propacteType = new OWLAnnotationPropertyImpl(CCAMVocabulary.typeActe.getIRI());
			OWLAnnotation annotacteType = fact.getOWLAnnotation(propacteType, IRI.create("http://data.esante.gouv.fr/cnam/ccam/TypeActe_" + acteType[acte.getActeType()]));
			OWLAxiom acteTypeAxiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotacteType);
			manager.applyChange(new AddAxiom(onto, acteTypeAxiom));
			
			OWLObjectProperty anntaPourTypeActe = fact.getOWLObjectProperty(IRI.create("http://data.esante.gouv.fr/cnam/ccam/aPourTypeActe"));
	        OWLClass head3 = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/TypeActe_" + acteType[acte.getActeType()]));
	        OWLClassExpression aPourTypeActe = fact.getOWLObjectSomeValuesFrom(anntaPourTypeActe, head3);
	        OWLSubClassOfAxiom ax4 = fact.getOWLSubClassOfAxiom(owlClass, aPourTypeActe);
	        AddAxiom addAx3 = new AddAxiom(onto, ax4);
	        manager.applyChange(addAx3);
			
			OWLAnnotationProperty ALTLABEL = new OWLAnnotationPropertyImpl(SKOSVocabulary.ALTLABEL.getIRI());
			OWLAnnotation annotALTLABEL = fact.getOWLAnnotation(ALTLABEL, fact.getOWLLiteral(acte.getLabel1(), "fr"));
			OWLAxiom ALTLABELAxiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotALTLABEL);
			manager.applyChange(new AddAxiom(onto, ALTLABELAxiom));
			
			OWLAnnotationProperty created = new OWLAnnotationPropertyImpl(DCTVocabulary.created.getIRI());
			OWLAnnotation annotcreated = fact.getOWLAnnotation(created, fact.getOWLLiteral(acte.getDt_creatio().toString()+ "T00:00:00", OWL2Datatype.XSD_DATE_TIME));
			OWLAxiom createdAxiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotcreated);
			manager.applyChange(new AddAxiom(onto, createdAxiom));
			
			OWLAnnotationProperty temporal = new OWLAnnotationPropertyImpl(DCTVocabulary.temporal.getIRI());
			OWLAnnotation annottemporal = fact.getOWLAnnotation(temporal, fact.getOWLLiteral(acte.getDt_effet().toString()+ "T00:00:00", OWL2Datatype.XSD_DATE_TIME));
			OWLAxiom temporalAxiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annottemporal);
			manager.applyChange(new AddAxiom(onto, temporalAxiom));
	        
	        
	        
			
		
			if(acteNoteListe.containsKey(code)) {
					ActeNote menu = acteNoteListe.get(code);

					String noteValue = "";
					String inclusionValue = "";
					String exclusionValue = "";
					String exempleValue = "";
					String definitionValue = "";
					String scopeNoteValue = "";

					for (Integer id_note : menu.getNotes().keySet()) {
						Note nt = menu.getNotes().get(id_note);
						if(acte.getDtModif().equals(nt.getAcdt_modif())) {
						switch(nt.getTypnot_cod()) {
						case 1:
							exclusionValue += nt.getTexte_note();
							for (String codeActe : acteListe.keySet()) {
								if(nt.getTexte_note().contains("("+codeActe+")")) {
									OWLAnnotationProperty exclusion = new OWLAnnotationPropertyImpl(CCAMVocabulary.acteExclus.getIRI());
					    			OWLAnnotation annot2 = fact.getOWLAnnotation(exclusion, IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + codeActe));
									OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + code), annot2);
									manager.applyChange(new AddAxiom(onto, axiom2));
									
									OWLClass disjClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + codeActe));
									OWLClass owlClass2 = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + code));
									OWLDisjointClassesAxiom disjointClassesAxiom = fact.getOWLDisjointClassesAxiom(owlClass2, disjClass);
							        manager.addAxiom(onto, disjointClassesAxiom);
								}
							}
							break;
						case 2:
							inclusionValue += nt.getTexte_note() + "\n";
							break;
						case 3:
							inclusionValue += nt.getTexte_note() + "\n";
							break;
						case 4:
							definitionValue += nt.getTexte_note()+ "\n";
							break;
						case 5:
							exempleValue += nt.getTexte_note()+ "\n";
							break;
						case 6:
							noteValue += nt.getTexte_note() + "\n";
							break;
						case 8:
							inclusionValue += nt.getTexte_note() + "\n";
							break;
						case 9:
							noteValue += nt.getTexte_note() + "\n";
							break;
						case 13:
							scopeNoteValue += nt.getTexte_note() + "\n";
							break;
						case 14:
							scopeNoteValue += nt.getTexte_note() + "\n";
							break;
						case 15:
							scopeNoteValue += nt.getTexte_note() + "\n";
							break;
						case 16:
							if(!nt.getTexte_note().isEmpty()) {
							OWLAnnotationProperty receuilProspectifDeDonnees = new OWLAnnotationPropertyImpl(CCAMVocabulary.receuilProspectifDeDonnees.getIRI());
					    	OWLAnnotation annot2 = fact.getOWLAnnotation(receuilProspectifDeDonnees, fact.getOWLLiteral(nt.getTexte_note(), "fr"));
							OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + code), annot2);
							manager.applyChange(new AddAxiom(onto, axiom2));
							}
							break;
						case 17:
							if(!nt.getTexte_note().isEmpty()) {
							OWLAnnotationProperty facturation = new OWLAnnotationPropertyImpl(CCAMVocabulary.facturation.getIRI());
					    	OWLAnnotation annot2 = fact.getOWLAnnotation(facturation, fact.getOWLLiteral(nt.getTexte_note(), "fr"));
							OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + code), annot2);
							manager.applyChange(new AddAxiom(onto, axiom2));
							}
							break;	
						}
					}
					}
					if (!StringUtils.isBlank(noteValue)) {
						OWLAnnotationProperty classNote = new OWLAnnotationPropertyImpl(SKOSVocabulary.NOTE.getIRI());
						OWLAnnotation annot = fact.getOWLAnnotation(classNote, fact.getOWLLiteral(noteValue, "fr"));
						OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(
								IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + code), annot);
						manager.applyChange(new AddAxiom(onto, axiom));
					}
					if (!StringUtils.isBlank(exempleValue)) {
						OWLAnnotationProperty classNote = new OWLAnnotationPropertyImpl(SKOSVocabulary.EXAMPLE.getIRI());
						OWLAnnotation annot = fact.getOWLAnnotation(classNote, fact.getOWLLiteral(exempleValue, "fr"));
						OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(
								IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + code), annot);
						manager.applyChange(new AddAxiom(onto, axiom));
					}
					if (!StringUtils.isBlank(definitionValue)) {
						OWLAnnotationProperty classNote = new OWLAnnotationPropertyImpl(SKOSVocabulary.DEFINITION.getIRI());
						OWLAnnotation annot = fact.getOWLAnnotation(classNote, fact.getOWLLiteral(definitionValue, "fr"));
						OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(
								IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + code), annot);
						manager.applyChange(new AddAxiom(onto, axiom));
					}
					if (!StringUtils.isBlank(inclusionValue)) {
						OWLAnnotationProperty classNote = new OWLAnnotationPropertyImpl(XSkosVocabulary.inclusionNote.getIRI());
						OWLAnnotation annot = fact.getOWLAnnotation(classNote, fact.getOWLLiteral(inclusionValue, "fr"));
						OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(
								IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + code), annot);
						manager.applyChange(new AddAxiom(onto, axiom));
					}
					if (!StringUtils.isBlank(exclusionValue)) {
						OWLAnnotationProperty classNote = new OWLAnnotationPropertyImpl(XSkosVocabulary.exclusionNote.getIRI());
						OWLAnnotation annot = fact.getOWLAnnotation(classNote, fact.getOWLLiteral(exclusionValue, "fr"));
						OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(
								IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + code), annot);
						manager.applyChange(new AddAxiom(onto, axiom));
					}
					if (!StringUtils.isBlank(scopeNoteValue)) {
						OWLAnnotationProperty scopeNote = new OWLAnnotationPropertyImpl(SKOSVocabulary.SCOPENOTE.getIRI());
						OWLAnnotation annot = fact.getOWLAnnotation(scopeNote, fact.getOWLLiteral(scopeNoteValue, "fr"));
						OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(
								IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + code), annot);
						manager.applyChange(new AddAxiom(onto, axiom));
					}
				}
			}
		logger.info("fin de la construction des actes de la terminologie");
		
	}
	
	
	
	private static void getTopographie() throws FileNotFoundException, OWLOntologyCreationException {
		logger.info("importer la liste des topographie de la terminologie");
		input = new FileInputStream(topographieOWLFile);
	    manager = OWLManager.createOWLOntologyManager();
	    OWLOntology topo = manager.loadOntologyFromOntologyDocument(input);
	    
	    topo.axioms().
	    forEach(ax -> {
	    	manager.applyChange(new AddAxiom(onto, ax));
	    	});
		
	}
	
	
	private static void getModeAcces() throws FileNotFoundException, OWLOntologyCreationException {
		logger.info("importer la liste des modes d'accès de la terminologie");
		input = new FileInputStream(modeAccesOWLFile);
	    manager = OWLManager.createOWLOntologyManager();
	    OWLOntology topo = manager.loadOntologyFromOntologyDocument(input);
	    
	    topo.axioms().
	    forEach(ax -> {
	    	manager.applyChange(new AddAxiom(onto, ax));
	    	});
	}
	
	private static void getAction() throws FileNotFoundException, OWLOntologyCreationException {
		logger.info("importer la liste des verbes d'action de la terminologie");
		input = new FileInputStream(actionOWLFile);
	    manager = OWLManager.createOWLOntologyManager();
	    OWLOntology topo = manager.loadOntologyFromOntologyDocument(input);
	    
	    topo.axioms().
	    forEach(ax -> {
	    	manager.applyChange(new AddAxiom(onto, ax));
	    	});
		
	}
	
	private static void createActivite() {
		logger.info("construction de la liste Activite");
		// add activite class
		GetActivite getAct = new GetActivite();
		activiteListe = getAct.getActiviteListe();

		for (String code : activiteListe.keySet()) {
			OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Activite_" + code));
			OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			manager.applyChange(new AddAxiom(onto, declare));
			
			OWLSubClassOfAxiom ax = fact.getOWLSubClassOfAxiom(owlClass,
					fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Activite")));
			manager.applyChange(new AddAxiom(onto, ax));
//			OWLSubClassOfAxiom ax2 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Activite")),
//					fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/racine")));
//			manager.applyChange(new AddAxiom(onto, ax2));
			
			OWLAnnotation annotActivite = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("Activite", "fr"));
			OWLAxiom axiomActivite = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Activite"), annotActivite);
			manager.applyChange(new AddAxiom(onto, axiomActivite));
			
			OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(notation, fact.getOWLLiteral(code));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot);
			manager.applyChange(new AddAxiom(onto, axiom));	
			
			OWLAnnotation annot1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(activiteListe.get(code), "fr"));
			OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot1);
			manager.applyChange(new AddAxiom(onto, axiom1));
		}
	}
	
	private static void createRegroupement() {
		logger.info("construction de la liste ActeActivite");
		// add regroupement class
		GetRegroupement getReg = new GetRegroupement();
		regroupementListe = getReg.getRegroupementListe();

		for (String code : regroupementListe.keySet()) {
			OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ActeActivite_" + code));
			OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			manager.applyChange(new AddAxiom(onto, declare));
			
			OWLSubClassOfAxiom ax = fact.getOWLSubClassOfAxiom(owlClass,
					fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ActeActivite")));
			manager.applyChange(new AddAxiom(onto, ax));
			
			
			
			OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(notation, fact.getOWLLiteral(code));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot);
			manager.applyChange(new AddAxiom(onto, axiom));	
			
			OWLAnnotation annot1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(regroupementListe.get(code), "fr"));
			OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot1);
			manager.applyChange(new AddAxiom(onto, axiom1));
		}
	}
	
	private static void createPhase() {
		logger.info("construction de la liste Phase");
		// add phase class
		GetPhase getPhase = new GetPhase();
		phaseListe = getPhase.getPhaseListe();

		for (Integer code : phaseListe.keySet()) {
			OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Phase_" + code));
			OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			manager.applyChange(new AddAxiom(onto, declare));
			
			OWLSubClassOfAxiom ax = fact.getOWLSubClassOfAxiom(owlClass,
					fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Phase")));
			manager.applyChange(new AddAxiom(onto, ax));
			
			OWLAnnotation annotPhase = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("Phase", "fr"));
			OWLAxiom axiomPhase = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Phase"), annotPhase);
			manager.applyChange(new AddAxiom(onto, axiomPhase));
			
			OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(notation, fact.getOWLLiteral(code));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot);
			manager.applyChange(new AddAxiom(onto, axiom));	
			
			OWLAnnotation annot1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(phaseListe.get(code), "fr"));
			OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot1);
			manager.applyChange(new AddAxiom(onto, axiom1));
		}
	}
	
	
	private static void createActeType() {
		logger.info("construction de la liste TypeActe");
		// add Type class
		GetActeType getActeType = new GetActeType();
		Map<Integer, String> acteTypeListe = getActeType.getActeTypeListe();
		String acteType[] = {"ActeIsole", "Procedure", "ActeComplementaire"};
 		for (Integer code : acteTypeListe.keySet()) {
			OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/TypeActe_" + acteType[code]));
			OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			manager.applyChange(new AddAxiom(onto, declare));
			
			OWLSubClassOfAxiom ax = fact.getOWLSubClassOfAxiom(owlClass,
					fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/TypeActe")));
			manager.applyChange(new AddAxiom(onto, ax));
			
			OWLAnnotation annotPhase = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("TypeActe", "fr"));
			OWLAxiom axiomPhase = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/TypeActe"), annotPhase);
			manager.applyChange(new AddAxiom(onto, axiomPhase));
			
			OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(notation, fact.getOWLLiteral(acteType[code]));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot);
			manager.applyChange(new AddAxiom(onto, axiom));	
			
			OWLAnnotation annot1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(acteTypeListe.get(code), "fr"));
			OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot1);
			manager.applyChange(new AddAxiom(onto, axiom1));
		}
	}
	
	private static void createDent() {
		logger.info("construction de la liste Dent");
		// add Dent class
		GetDent getDent = new GetDent();
		Map<Integer, String> dentListe = getDent.getDentListe();

		for (Integer code : dentListe.keySet()) {
			OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Dent_" + code));
			OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			manager.applyChange(new AddAxiom(onto, declare));
			
			OWLSubClassOfAxiom ax = fact.getOWLSubClassOfAxiom(owlClass,
					fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Dent")));
			manager.applyChange(new AddAxiom(onto, ax));
			
			OWLAnnotation annotPhase = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("Dent", "fr"));
			OWLAxiom axiomPhase = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Dent"), annotPhase);
			manager.applyChange(new AddAxiom(onto, axiomPhase));
			
			OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(notation, fact.getOWLLiteral(""+code));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot);
			manager.applyChange(new AddAxiom(onto, axiom));	
			
			OWLAnnotation annot1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(dentListe.get(code), "fr"));
			OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot1);
			manager.applyChange(new AddAxiom(onto, axiom1));
		}
	}
	
	
	
	private static void createActiviteActe() {
		logger.info("association entre Acte et ActeActivite");
		GetActiviteActe getactivActe = new GetActiviteActe();
		activiteActeListe = getactivActe.getActiviteActeListe();

		for (String code : activiteActeListe.keySet()) {
			ActeActivite acteActi = activiteActeListe.get(code);
			
			String acte = acteActi.getActe_cod();
			String acti = acteActi.getActiv_cod();
			String reg = acteActi.getRegrou_cod();
			Acte ac = acteListe.get(acte);
			if(acteActi.getAcdt_modif().equals(ac.getDtModif())) {
			OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + code));
			OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			manager.applyChange(new AddAxiom(onto, declare));
			
			
			
			OWLSubClassOfAxiom ax = fact.getOWLSubClassOfAxiom(owlClass,
					fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ActeActivite_" + reg)));
			manager.applyChange(new AddAxiom(onto, ax));
			
			OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(notation, fact.getOWLLiteral(code));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot);
			manager.applyChange(new AddAxiom(onto, axiom));
			
			String label = "";
			// traitement special pour les acitivités mentionnées dans les ActeNotes
			ActeNote acteNote = acteNoteListe.get(acte);
			if(acteNote != null) {
			for (Integer id_note : acteNote.getNotes().keySet()) {
				Note nt = acteNote.getNotes().get(id_note);
				if(acti.equals("1") && nt.getTypnot_cod() == 10) {
					if(nt.getTexte_note().startsWith("Activit")) {
						label = nt.getTexte_note();
					}
				}else if(acti.equals("2") && nt.getTypnot_cod() == 11) {
					if(nt.getTexte_note().startsWith("Activit")) {
						label = nt.getTexte_note();
					}
				}else if(acti.equals("3") && nt.getTypnot_cod() == 12) {
					if(nt.getTexte_note().startsWith("Activit")) {
						label = nt.getTexte_note();
					}
				}
				
			}
			}
			if(label.isEmpty()) {
				label = acteListe.get(acte).getLabel() + " - " + activiteListe.get(acti);
			}else {
				String lab = acteListe.get(acte).getLabel() + " - " + label;
				label = lab;
			}
			OWLAnnotation annot1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(label, "fr"));
			OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot1);
			manager.applyChange(new AddAxiom(onto, axiom1));
			
			OWLAnnotationProperty act = new OWLAnnotationPropertyImpl(CCAMVocabulary.acte.getIRI());
			OWLAnnotation annotAct = fact.getOWLAnnotation(act, IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + acte));
			OWLAxiom actAxiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotAct);
			manager.applyChange(new AddAxiom(onto, actAxiom));	
			
			OWLAnnotationProperty activ = new OWLAnnotationPropertyImpl(CCAMVocabulary.activite.getIRI());
			OWLAnnotation annotActiv = fact.getOWLAnnotation(activ, IRI.create("http://data.esante.gouv.fr/cnam/ccam/Activite_" + acti));
			OWLAxiom activAxiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotActiv);
			manager.applyChange(new AddAxiom(onto, activAxiom));
			
			OWLAnnotationProperty actActiv = new OWLAnnotationPropertyImpl(CCAMVocabulary.partieActeActivite.getIRI());
			OWLAnnotation annotactActiv = fact.getOWLAnnotation(actActiv, owlClass.getIRI());
			OWLAxiom actActivAxiom = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + acte), annotactActiv);
			manager.applyChange(new AddAxiom(onto, actActivAxiom));
			
			
			// association acte/activite
//	        OWLObjectProperty aPourPartieActeActivite = fact.getOWLObjectProperty(IRI.create("http://data.esante.gouv.fr/cnam/ccam/aPourPartieActeActivite"));
//	        OWLClass head1 = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + acte));
//	        OWLClassExpression hasPartExactActivite = fact.getOWLObjectExactCardinality(1, aPourPartieActeActivite, owlClass);
//	        OWLSubClassOfAxiom ax2 = fact.getOWLSubClassOfAxiom(head1, hasPartExactActivite);
//	        AddAxiom addAx1 = new AddAxiom(onto, ax2);
//	        manager.applyChange(addAx1);
	        
	     // association acte/activite2
//	        OWLObjectProperty anntfaitPartieDeActe = fact.getOWLObjectProperty(IRI.create("http://data.esante.gouv.fr/cnam/ccam/faitPartieDeActe"));
//	        OWLClass head2 = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + acte));
//	        OWLClassExpression faitPartieDeActe = fact.getOWLObjectSomeValuesFrom(anntfaitPartieDeActe, head2);
//	        OWLSubClassOfAxiom ax3 = fact.getOWLSubClassOfAxiom(owlClass, faitPartieDeActe);
//	        AddAxiom addAx2 = new AddAxiom(onto, ax3);
//	        manager.applyChange(addAx2);
			}
			
		}
	}
	
	private static void createPhaseActiviteActe() {
		logger.info("association entre  ActeActivite et ActeActivitePhase");
		GetPhaseActiviteActe getphaseActivActe = new GetPhaseActiviteActe();
		phaseActiviteActeListe = getphaseActivActe.getPhaseActiviteActeListe();

		for (String code : phaseActiviteActeListe.keySet()) {
			OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + code));
			OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			manager.applyChange(new AddAxiom(onto, declare));
			
			String acte = code.substring(0, code.length()-2);
			String phase = phaseActiviteActeListe.get(code).split(";")[1];
			String acti = phaseActiviteActeListe.get(code).split(";")[0].substring(7);
			String activite = code.substring(0, code.length()-1);
			
			OWLSubClassOfAxiom ax = fact.getOWLSubClassOfAxiom(owlClass,
					fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ActeActivitePhase_Phase_" + phase)));
			manager.applyChange(new AddAxiom(onto, ax));
			
			OWLAnnotation annot2 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("Phases d'Acte de Phase " + phase, "fr"));
			OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ActeActivitePhase_Phase_" + phase), annot2);
			manager.applyChange(new AddAxiom(onto, axiom2));
			
			OWLAnnotationProperty notationPAA = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
			OWLAnnotation annot3 = fact.getOWLAnnotation(notationPAA, fact.getOWLLiteral("Phase_" + phase));
			OWLAxiom axiom3 = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ActeActivitePhase_Phase_" + phase), annot3);
			manager.applyChange(new AddAxiom(onto, axiom3));
			
			OWLAnnotationProperty phasePAA = new OWLAnnotationPropertyImpl(CCAMVocabulary.phase.getIRI());
			OWLAnnotation annot4 = fact.getOWLAnnotation(phasePAA, IRI.create("http://data.esante.gouv.fr/cnam/ccam/Phase_" + phase));
			OWLAxiom axiom4 = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ActeActivitePhase_Phase_" + phase), annot4);
			manager.applyChange(new AddAxiom(onto, axiom4));
			
			OWLSubClassOfAxiom ax1 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ActeActivitePhase_Phase_" + phase)),
					fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ActeActivitePhase")));
			manager.applyChange(new AddAxiom(onto, ax1));
			
			OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(notation, fact.getOWLLiteral(code));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot);
			manager.applyChange(new AddAxiom(onto, axiom));	
			Integer idPhase = Integer.valueOf(phase);
			
			String label = "";
			// traitement special pour les acitivités mentionnées dans les ActeNotes
			ActeNote acteNote = acteNoteListe.get(acte);
			if(acteNote != null) {
			for (Integer id_note : acteNote.getNotes().keySet()) {
				Note nt = acteNote.getNotes().get(id_note);
				if(acti.equals("1") && phase.equals("1") && nt.getTypnot_cod() == 10) {
					if(nt.getTexte_note().startsWith("Phase")) {
						label = nt.getTexte_note();
					}
				}else if(acti.equals("1") && phase.equals("2") && nt.getTypnot_cod() == 11) {
					if(nt.getTexte_note().startsWith("Phase")) {
						label = nt.getTexte_note();
					}
				}else if(acti.equals("1") && phase.equals("3") && nt.getTypnot_cod() == 12) {
					if(nt.getTexte_note().startsWith("Phase")) {
						label = nt.getTexte_note();
					}
				}
				
			}
			}
			
			if(label.isEmpty()) {
				label = acteListe.get(acte).getLabel() + " - " + activiteListe.get(acti) + " - " + phaseListe.get(idPhase);
			}else {
				String lab = acteListe.get(acte).getLabel() + " - " + activiteListe.get(acti) + " - " + label;
				label = lab;
			}
			OWLAnnotation annot1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(label, "fr"));
			OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot1);
			manager.applyChange(new AddAxiom(onto, axiom1));
			
			OWLAnnotationProperty act = new OWLAnnotationPropertyImpl(CCAMVocabulary.acte.getIRI());
			OWLAnnotation annotAct = fact.getOWLAnnotation(act, IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + acte));
			OWLAxiom actAxiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotAct);
			manager.applyChange(new AddAxiom(onto, actAxiom));	
			
			OWLAnnotationProperty activ = new OWLAnnotationPropertyImpl(CCAMVocabulary.activite.getIRI());
			OWLAnnotation annotActiv = fact.getOWLAnnotation(activ, IRI.create("http://data.esante.gouv.fr/cnam/ccam/Activite_" + acti));
			OWLAxiom activAxiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotActiv);
			manager.applyChange(new AddAxiom(onto, activAxiom));
			
			OWLAnnotationProperty phas = new OWLAnnotationPropertyImpl(CCAMVocabulary.phase.getIRI());
			OWLAnnotation annotPhase = fact.getOWLAnnotation(phas, IRI.create("http://data.esante.gouv.fr/cnam/ccam/Phase_" + phase));
			OWLAxiom phaseAxiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotPhase);
			manager.applyChange(new AddAxiom(onto, phaseAxiom));
			
			OWLAnnotationProperty actActivPhase = new OWLAnnotationPropertyImpl(CCAMVocabulary.ActeActivitePhase.getIRI());
			OWLAnnotation annotactActivPhase = fact.getOWLAnnotation(actActivPhase, owlClass.getIRI());
			OWLAxiom actActivPhaseAxiom = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + activite), annotactActivPhase);
			manager.applyChange(new AddAxiom(onto, actActivPhaseAxiom));
			
			OWLAnnotationProperty actActiv = new OWLAnnotationPropertyImpl(CCAMVocabulary.acteActivite.getIRI());
			OWLAnnotation annotactActiv = fact.getOWLAnnotation(actActiv, IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + activite));
			OWLAxiom actActivAxiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotactActiv);
			manager.applyChange(new AddAxiom(onto, actActivAxiom));
			
			// association acte/phase
//	        OWLObjectProperty aPourPartieActeActivitePhase = fact.getOWLObjectProperty(IRI.create("http://data.esante.gouv.fr/cnam/ccam/aPourPartieActeActivitePhase"));
//	        OWLClass head1 = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + activite));
//	        OWLClassExpression hasPartExactActivite = fact.getOWLObjectExactCardinality(1, aPourPartieActeActivitePhase, owlClass);
//	        OWLSubClassOfAxiom ax2 = fact.getOWLSubClassOfAxiom(head1, hasPartExactActivite);
//	        AddAxiom addAx1 = new AddAxiom(onto, ax2);
//	        manager.applyChange(addAx1);
	        
	     // association acte/phase2
//	        OWLObjectProperty anntfaitPartieDeActe = fact.getOWLObjectProperty(IRI.create("http://data.esante.gouv.fr/cnam/ccam/faitPartieDeActe"));
//	        OWLClass head2 = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + acte));
//	        OWLClassExpression faitPartieDeActe = fact.getOWLObjectSomeValuesFrom(anntfaitPartieDeActe, head2);
//	        OWLSubClassOfAxiom ax3 = fact.getOWLSubClassOfAxiom(owlClass, faitPartieDeActe);
//	        AddAxiom addAx2 = new AddAxiom(onto, ax3);
//	        manager.applyChange(addAx2);
	        
	     // association acti/phase2
//	        OWLObjectProperty anntfaitPartieDeActeActivite = fact.getOWLObjectProperty(IRI.create("http://data.esante.gouv.fr/cnam/ccam/faitPartieDeActeActivite"));
//	        OWLClass head3 = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + activite));
//	        OWLClassExpression faitPartieDeActeActivite = fact.getOWLObjectSomeValuesFrom(anntfaitPartieDeActeActivite, head3);
//	        OWLSubClassOfAxiom ax4 = fact.getOWLSubClassOfAxiom(owlClass, faitPartieDeActeActivite);
//	        AddAxiom addAx3 = new AddAxiom(onto, ax4);
//	        manager.applyChange(addAx3);
		}
	}
	
	private static void createModificateur() {
		logger.info("construction de la liste Modificateur");
		// add modificateur class
		GetModificateur getMod = new GetModificateur();
		modificateurListe = getMod.getModificateurListe();

		for (String code : modificateurListe.keySet()) {
			OWLClass owlClass = fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Modificateur_" + code));
			OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			manager.applyChange(new AddAxiom(onto, declare));
			
			OWLSubClassOfAxiom ax = fact.getOWLSubClassOfAxiom(owlClass,
					fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Modificateur")));
			manager.applyChange(new AddAxiom(onto, ax));
			
			
			OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(notation, fact.getOWLLiteral(code));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot);
			manager.applyChange(new AddAxiom(onto, axiom));	
			
			OWLAnnotation annot1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(modificateurListe.get(code).getLabel(), "fr"));
			OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annot1);
			manager.applyChange(new AddAxiom(onto, axiom1));
		}
	}
	
	private static void createActiviteModificateur() {
		logger.info("association entre  ActeActivite et Modificateur");
		GetActiviteModificateur getActMod = new GetActiviteModificateur();
		activiteModificateurListe = getActMod.getActiviteModificateurListe();

		for (ActiviteModificateur am : activiteModificateurListe) {
			
			ActeActivite actActiv = activiteActeListe.get(am.getAa_code());
			if(am.getAadt_modif().equals(actActiv.getAcdt_modif())) {
			String modifURI = "http://data.esante.gouv.fr/cnam/ccam/Modificateur_" + am.getModifi_cod();
			String activURI = "http://data.esante.gouv.fr/cnam/ccam/" + am.getAa_code();
			
			OWLAnnotationProperty modificateur = new OWLAnnotationPropertyImpl(CCAMVocabulary.modificateur.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(modificateur, IRI.create(modifURI));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create(activURI), annot);
			manager.applyChange(new AddAxiom(onto, axiom));	
			
			// association modificateur/activite
//	        OWLObjectProperty anntaPourModificateur = fact.getOWLObjectProperty(IRI.create("http://data.esante.gouv.fr/cnam/ccam/aPourModificateur"));
//	        OWLClass head2 = fact.getOWLClass(IRI.create(modifURI));
//	        OWLClassExpression aPourModificateur = fact.getOWLObjectAllValuesFrom(anntaPourModificateur, head2);
//	        OWLSubClassOfAxiom ax3 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create(activURI)), aPourModificateur);
//	        AddAxiom addAx2 = new AddAxiom(onto, ax3);
//	        manager.applyChange(addAx2);
			}
		}
	}
	
	private static void createAssociations() {
		logger.info("Construction des associations entre  les Actes");
		// add Actes Associations
		GetActesAssociations getActesAssociations = new GetActesAssociations();
		actesAssociations = getActesAssociations.getActeAssociationListe();

		for (Association ass : actesAssociations) {
			
			String acte1 = ass.getAa_code1().substring(0, 7);
			String acte2 = ass.getAa_code2().substring(0, 7);
			Acte ac1 = acteListe.get(acte1);
			Acte ac2 = acteListe.get(acte2);
			if(ass.getDt_modif1().equals(ac1.getDtModif()) && ass.getDt_modif2().equals(ac2.getDtModif())) {
			OWLAnnotationProperty incompatibilite = new OWLAnnotationPropertyImpl(CCAMVocabulary.acteAssocie.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(incompatibilite, IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + acte2));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + acte1), annot);
			manager.applyChange(new AddAxiom(onto, axiom));	
			}
		}
	}
	
	private static void createActivitePhaseDent() {
		logger.info("association entre  dent et ActeActivitePhase");
		// add activite_phase_dent
		GetDentIncompatible getDentIncompatible = new GetDentIncompatible();
		List<String> dentIncompatible = getDentIncompatible.getDentIncompatibleListe();

		for (String code : dentIncompatible) {
			
			String aap = code.split(";")[0];
			String dent = code.split(";")[1];
			OWLAnnotationProperty incompatibilite = new OWLAnnotationPropertyImpl(CCAMVocabulary.dentIncompatible.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(incompatibilite, IRI.create("http://data.esante.gouv.fr/cnam/ccam/Dent_" + dent));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + aap), annot);
			manager.applyChange(new AddAxiom(onto, axiom));	
			
		}
	}
	
	private static void createIncompatibilites() {
		// add Actes Incompatibles
		GetActesIncompatibles getActesIncompatibles = new GetActesIncompatibles();
		actesIncompatibles = getActesIncompatibles.getActeIncompatibleListe();

		for (String code : actesIncompatibles) {
			
			String acte1 = code.split(";")[0];
			String acte2 = code.split(";")[1];
			OWLAnnotationProperty incompatibilite = new OWLAnnotationPropertyImpl(CCAMVocabulary.incompatibilite.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(incompatibilite, IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + acte2));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + acte1), annot);
			manager.applyChange(new AddAxiom(onto, axiom));	
			
		}
	}
	
	private static void createProcedures() {
		logger.info("association entre  Acte et Procedure");
		// add Actes Procedures
		GetActesProcedures getActesProcedures = new GetActesProcedures();
		actesProcedures = getActesProcedures.getActeProceduresListe();

		for (ActeProcedure actProc : actesProcedures) {
			
			String acte = actProc.getActe_cod();
			String proc = actProc.getProc_cod();
			Acte act = acteListe.get(acte);
			if(!acte.equals(proc) && actProc.getAcdt_modif().equals(act.getDtModif())) {
				OWLAnnotationProperty procedure = new OWLAnnotationPropertyImpl(CCAMVocabulary.procedure.getIRI());
				OWLAnnotation annot = fact.getOWLAnnotation(procedure, IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + proc));
				OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + acte), annot);
				manager.applyChange(new AddAxiom(onto, axiom));
				
				OWLAnnotationProperty acteDeLaProcedure = new OWLAnnotationPropertyImpl(CCAMVocabulary.acteDeLaProcedure.getIRI());
				OWLAnnotation annot1 = fact.getOWLAnnotation(acteDeLaProcedure, IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + acte));
				OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/" + proc), annot1);
				manager.applyChange(new AddAxiom(onto, axiom1));
			
			}
			
		}
	}
	
	public static void cleanning() {
		OWLSubClassOfAxiom ax1 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Action_Y")),
				fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/racine")));
		manager.applyChange(new AddAxiom(onto, ax1));
		
		OWLAnnotation annot1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("Action_Y", "fr"));
		OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Action_Y"), annot1);
		manager.applyChange(new AddAxiom(onto, axiom1));
		
		OWLSubClassOfAxiom ax2 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ModeAcces_X")),
				fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/racine")));
		manager.applyChange(new AddAxiom(onto, ax2));
		
		OWLAnnotation annotModeAcces_X = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("ModeAcces_X", "fr"));
		OWLAxiom axiomModeAcces_X = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ModeAcces_X"), annotModeAcces_X);
		manager.applyChange(new AddAxiom(onto, axiomModeAcces_X));
		
		OWLSubClassOfAxiom ax3 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ModeAcces_Y")),
				fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/racine")));
		manager.applyChange(new AddAxiom(onto, ax3));
		
		OWLAnnotation annotModeAcces_Y = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("ModeAcces_Y", "fr"));
		OWLAxiom axiomModeAcces_Y = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/ModeAcces_Y"), annotModeAcces_Y);
		manager.applyChange(new AddAxiom(onto, axiomModeAcces_Y));
		
		OWLSubClassOfAxiom ax4 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Topographie_YY")),
				fact.getOWLClass(IRI.create("http://data.esante.gouv.fr/cnam/ccam/racine")));
		manager.applyChange(new AddAxiom(onto, ax4));
		
		OWLAnnotation annotTopographie_YY = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("Topographie_YY", "fr"));
		OWLAxiom axiomTopographie_YY = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://data.esante.gouv.fr/cnam/ccam/Topographie_YY"), annotTopographie_YY);
		manager.applyChange(new AddAxiom(onto, axiomTopographie_YY));
	}

}

	
