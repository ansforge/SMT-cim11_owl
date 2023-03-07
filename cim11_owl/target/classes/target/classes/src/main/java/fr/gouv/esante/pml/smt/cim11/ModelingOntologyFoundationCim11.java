package fr.gouv.esante.pml.smt.cim11;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import com.google.common.collect.Lists;

import fr.gouv.esante.pml.smt.utils.ANSICD11Vocabulary;
import fr.gouv.esante.pml.smt.utils.DCVocabulary;
import fr.gouv.esante.pml.smt.utils.ICDVocabulary;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.SkosVocabulary;
import fr.gouv.esante.pml.smt.utils.XSkosVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

public class ModelingOntologyFoundationCim11 {
  
  
  
  private static OWLDataFactory fact = null;

  static InputStream input = null;
  private static final Map<String, String> genIdIncl = new HashMap<String, String>();
  private static final Map<String, String> InclusionNote = new HashMap<String, String>();
  private static final Map<String, String> InclusionNoteXML = new HashMap<String, String>();
  private static final Map<String, String> genIdExcl = new HashMap<String, String>();
  private static final Map<String, String> ExclusionNote = new HashMap<String, String>();
  private static final Map<String, String> ExclusionNoteXML = new HashMap<String, String>();
  
  private static final Map<String, String> entityLabel = new HashMap<String, String>();
  
  private static final Map<String, String> indexTerm = new HashMap<String, String>();
  private static final Map<String, String> foundationChild = new HashMap<String, String>();
  private static OWLOntologyManager manager = null;
  private static OWLOntology onto = null;
  static String OWL_FINAL_FILE_FR = "CGTS_SEM_ICD11-MMS-R202301-FR.owl";

  public static void main(final String[] args) throws Exception {
	    input = new FileInputStream(PropertiesUtil.getProperties("owlFileName"));
	    manager = OWLManager.createOWLOntologyManager();
	    onto = manager.loadOntologyFromOntologyDocument(input);
	    fact = onto.getOWLOntologyManager().getOWLDataFactory();
	    inclusionModeling();
	    exclusionModeling();
	    indexTermModeling();
	    altLabelModeling();
	    fondationModeling();
	    uriVersionningModeling();
	    postcoordinationModeling();
	 // postCoordinationMMSmodifing(); //pour MMS
	   fondationCleanning();  //Pour Fondation
	   addNotationAndTypeModeling();
	    final OutputStream fileoutputstream = new FileOutputStream(OWL_FINAL_FILE_FR);
	    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
	    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
	    manager.saveOntology(onto, ontologyFormat, fileoutputstream);
	  }
  
  public static void uriVersionningModeling() {
	  IRI cimIRI = IRI.create("http://id.who.int/icd/release/11/ontology");
	  manager.applyChange(new SetOntologyID(onto, new OWLOntologyID(cimIRI)));
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.DECLARATION)).
	    forEach(ax -> {
	    	ax.classesInSignature().findFirst().ifPresent(clacc -> {
	    	String iri = clacc.getIRI().getIRIString();
	    	OWLClass owlClass = fact.getOWLClass(IRI.create(iri.replace("release/11/2023-01/mms", "release/11/mms")));
			OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			manager.applyChange(new AddAxiom(onto, declare));
	    	});
//	    	System.out.println(ax);
	      });

	    
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.DISJOINT_CLASSES)).
	    forEach(ax -> {
	    	String firstIRI = Lists.newArrayList(ax.classesInSignature().iterator()).get(0).getIRI().getIRIString();
	    	String secondIRI = Lists.newArrayList(ax.classesInSignature().iterator()).get(1).getIRI().getIRIString();

			OWLClass disjClass = fact.getOWLClass(IRI.create(firstIRI.replace("release/11/2023-01/mms", "release/11/mms")));
			OWLClass owlClass = fact.getOWLClass(IRI.create(secondIRI.replace("release/11/2023-01/mms", "release/11/mms")));
			OWLDisjointClassesAxiom disjointClassesAxiom = fact.getOWLDisjointClassesAxiom(owlClass, disjClass);
	        manager.addAxiom(onto, disjointClassesAxiom);
	    	
	      });
	    
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.DISJOINT_CLASSES)).
	    forEach(ax -> {
	    	String firstIRI = ax.classesInSignature().findFirst().get().getIRI().getIRIString();
	    	if(firstIRI.contains("release/11/2023-01/mms")) {
    			manager.applyChange(new RemoveAxiom(onto, ax));
    		}
	    	
	      });
	    
	    
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.SUBCLASS_OF)).
	    forEach(ax -> {
	    	String iriSub = ((OWLSubClassOfAxiom) ax).getSubClass().asOWLClass().getIRI().getIRIString().replace("release/11/2023-01/mms", "release/11/mms");
	    	String iriSup = ((OWLSubClassOfAxiom) ax).getSuperClass().asOWLClass().getIRI().getIRIString().replace("release/11/2023-01/mms", "release/11/mms");
	    	
	    	OWLSubClassOfAxiom ax1 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create(iriSub)), fact.getOWLClass(IRI.create(iriSup)));
			manager.applyChange(new AddAxiom(onto, ax1));
	    	
	      });
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.SUBCLASS_OF)).
	    forEach(ax -> {
	    	if(((OWLSubClassOfAxiom) ax).getSubClass().asOWLClass().getIRI().getIRIString().contains("release/11/2023-01/mms")) {
    			manager.applyChange(new RemoveAxiom(onto, ax));
    		}
	      });
	    
		  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    forEach(ax -> {
	    	if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#narrower>")) {
    			manager.applyChange(new RemoveAxiom(onto, ax));
    		}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#exclusion>")) {
    			OWLAnnotation annot1 = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(), IRI.create(((OWLAnnotationAssertionAxiom) ax).getValue().toString().replace("release/11/2023-01/mms", "release/11/mms")));
        		OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(IRI.create(((OWLAnnotationAssertionAxiom) ax).getSubject().toString().replace("release/11/2023-01/mms", "release/11/mms")), annot1);
        		manager.applyChange(new AddAxiom(onto, axiom1));
    		}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#foundationChild>")) {
    			OWLAnnotation annot1 = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(), IRI.create(((OWLAnnotationAssertionAxiom) ax).getValue().toString().replace("release/11/2023-01/mms", "release/11/mms")));
        		OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(IRI.create(((OWLAnnotationAssertionAxiom) ax).getSubject().toString().replace("release/11/2023-01/mms", "release/11/mms")), annot1);
        		manager.applyChange(new AddAxiom(onto, axiom1));
    		}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#prefLabel>")){
    			OWLAnnotationProperty release = new OWLAnnotationPropertyImpl(ICDVocabulary.release.getIRI());
	    		OWLAnnotation annot = fact.getOWLAnnotation(release,IRI.create(((OWLAnnotationAssertionAxiom) ax).getSubject().toString() ));
	    		OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create(((OWLAnnotationAssertionAxiom) ax).getSubject().toString().replace("release/11/2023-01/mms", "release/11/mms")), annot);
	    		manager.applyChange(new AddAxiom(onto, axiom));
	    		
	    		OWLAnnotation annot2 = fact.getOWLAnnotation(fact.getRDFSLabel(), ((OWLAnnotationAssertionAxiom) ax).getValue());
	    		OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create(((OWLAnnotationAssertionAxiom) ax).getSubject().toString().replace("release/11/2023-01/mms", "release/11/mms")), annot2);
	    		manager.applyChange(new AddAxiom(onto, axiom2));
	    		
    		}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/classKind>")){
    			if(((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("chapter")) {
    				String iriSup = "http://id.who.int/icd/release/11/mms";
    		    	String iriSub = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString().replace("release/11/2023-01/mms", "release/11/mms");
    		    	
    		    	OWLSubClassOfAxiom ax1 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create(iriSub)), fact.getOWLClass(IRI.create(iriSup)));
    				manager.applyChange(new AddAxiom(onto, ax1));
    			}
	    		
	    		OWLAnnotation annot2 = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(), ((OWLAnnotationAssertionAxiom) ax).getValue());
	    		OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create(((OWLAnnotationAssertionAxiom) ax).getSubject().toString().replace("release/11/2023-01/mms", "release/11/mms")), annot2);
	    		manager.applyChange(new AddAxiom(onto, axiom2));
	    		
    		}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/postcoordinationScale>")){
	    		
	    		OWLAnnotation annot2 = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(), IRI.create(((OWLAnnotationAssertionAxiom) ax).getValue().toString().replace("release/11/2023-01/mms", "release/11/mms")));
	    		OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create(((OWLAnnotationAssertionAxiom) ax).getSubject().toString().replace("release/11/2023-01/mms", "release/11/mms")), annot2);
	    		manager.applyChange(new AddAxiom(onto, axiom2));
	    		
//	    		OWLAnnotationProperty type = new OWLAnnotationPropertyImpl(OWLRDFVocabulary.RDF_TYPE.getIRI());
//	    		OWLAnnotation annot = fact.getOWLAnnotation(type, IRI.create("http://data.esante.gouv.fr/ans-icd11#PostCoordinationScaleInfo"));
//	    		OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create(((OWLAnnotationAssertionAxiom) ax).getValue().toString().replace("release/11/2023-01/mms", "release/11/mms")), annot);
//	    		manager.applyChange(new AddAxiom(onto, axiom));
	    		
    		}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/scaleEntity>")){
	    		
	    		OWLAnnotation annot2 = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(), IRI.create(((OWLAnnotationAssertionAxiom) ax).getValue().toString().replace("release/11/2023-01/mms", "release/11/mms")));
	    		OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create(((OWLAnnotationAssertionAxiom) ax).getSubject().toString().replace("release/11/2023-01/mms", "release/11/mms")), annot2);
	    		manager.applyChange(new AddAxiom(onto, axiom2));
	    		
	    		manager.applyChange(new RemoveAxiom(onto, ax));
	    		
	    		
    		}else {
		    	OWLAnnotation annot = fact.getOWLAnnotation(((OWLAnnotationAssertionAxiom) ax).getProperty(), ((OWLAnnotationAssertionAxiom) ax).getValue());
	    		OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create(((OWLAnnotationAssertionAxiom) ax).getSubject().toString().replace("release/11/2023-01/mms", "release/11/mms")), annot);
	    		manager.applyChange(new AddAxiom(onto, axiom));
    		}
	    	
	      });
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    forEach(ax -> {
	    		if(((OWLAnnotationAssertionAxiom) ax).getSubject().toString().contains("release/11/2023-01/mms")) {
	    			manager.applyChange(new RemoveAxiom(onto, ax));
	    		}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#notation>") ||
	    				((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/code>")) {
	    			String code = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^^xsd")-1);
	    	    	if(code.isEmpty()) {
	    				manager.applyChange(new RemoveAxiom(onto, ax));
	    			}
	    		}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#prefLabel>") 
	    				|| ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#broader>")) {
	    			manager.applyChange(new RemoveAxiom(onto, ax));
	    		}
	      });
	    
	    
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.DECLARATION)).
	    forEach(ax -> {
	    	ax.classesInSignature().findFirst().ifPresent(clacc -> {
		    	String iri = clacc.getIRI().getIRIString();
		    	if(iri.contains("release/11/2023-01/mms")) {
	    			manager.applyChange(new RemoveAxiom(onto, ax));
	    		}
		    	});
	    	
	    	
	      });
	    
//	    OWLAnnotation annot = fact.getOWLAnnotation(fact.getRDFSLabel(),
//				fact.getOWLLiteral("International Classification of Diseases 11th Revision - Mortality and Morbidity Statistics", "fr"));
//	    OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create("http://id.who.int/icd/release/11/mms"), annot);
//		manager.applyChange(new AddAxiom(onto, axiom));
//		
//		OWLAnnotationProperty hasTopConcept = fact.getOWLAnnotationProperty(SKOSVocabulary.HASTOPCONCEPT.getIRI());
//		OWLAnnotation annothasTopConcept = fact.getOWLAnnotation(hasTopConcept, IRI.create("http://id.who.int/icd/release/11/mms"));
//		manager.applyChange(new AddOntologyAnnotation(onto, annothasTopConcept));
//		
  }
   
  public static void inclusionModeling() {
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/inclusion>") ).
	    forEach(ax -> {
	    	
	    	genIdIncl.put(((OWLAnnotationAssertionAxiom) ax).getValue().toString(), ((OWLAnnotationAssertionAxiom) ax).getSubject().toString());
	      });
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    forEach(ax -> {
	    	if(genIdIncl.containsKey(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()) && ((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("@"+PropertiesUtil.getProperties("icd_language"))) {
	    		
	    		OWLAnnotationProperty altLabel = new OWLAnnotationPropertyImpl(SKOSVocabulary.ALTLABEL.getIRI());
	    		OWLAnnotation annot = fact.getOWLAnnotation(altLabel, fact.getOWLLiteral(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1), PropertiesUtil.getProperties("icd_language")));
	    		OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create(genIdIncl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), annot);
	    		manager.applyChange(new AddAxiom(onto, axiom));
	    		
	    		OWLAnnotationProperty ansInclusion = new OWLAnnotationPropertyImpl(ANSICD11Vocabulary.inclusion.getIRI());
	    		OWLAnnotation annot2 = fact.getOWLAnnotation(ansInclusion, fact.getOWLLiteral(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1), PropertiesUtil.getProperties("icd_language")));
	    		OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create(genIdIncl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), annot2);
	    		manager.applyChange(new AddAxiom(onto, axiom2));
	    		manager.applyChange(new RemoveAxiom(onto, ax));
	    		
	    		if(InclusionNote.containsKey(genIdIncl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()))) {
	    			String note = InclusionNote.get(genIdIncl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()));
	    			note += "\n  - " + ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1);
	    			InclusionNote.put(genIdIncl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()), note);
	    			
	    			String noteXml = InclusionNoteXML.get(genIdIncl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()));
	    			noteXml = noteXml.substring(0, noteXml.length()-11) +"<li>" + ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1)+ "</li></ul></div>";
	    			InclusionNoteXML.put(genIdIncl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()), noteXml);
	    			
	    		}else {
	    			String note = "Includes :\n  - " + ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1);
	    			InclusionNote.put(genIdIncl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()), note);
	    			
	    			String noteXML = "<div xml:lang=\""+ PropertiesUtil.getProperties("icd_language") +"\">Includes : <ul><li>" + ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1) + "</li></ul></div>";
	    			InclusionNoteXML.put(genIdIncl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()), noteXML);
	    		}
	    		
//	    		System.out.println(ax + "---" +genId.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()));
	    	}
	    	else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/inclusion>")) {
	    		manager.applyChange(new RemoveAxiom(onto, ax));
	    	}
	    	
	      });
	    for(String subject : InclusionNote.keySet()) {
	    	OWLAnnotationProperty inclusionNote = new OWLAnnotationPropertyImpl(XSkosVocabulary.inclusionNote.getIRI());
  		OWLAnnotation annot = fact.getOWLAnnotation(inclusionNote, fact.getOWLLiteral(InclusionNote.get(subject), PropertiesUtil.getProperties("icd_language")));
  		OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create(subject), annot);
  		manager.applyChange(new AddAxiom(onto, axiom));
	    }
	    
	    for(String subject : InclusionNoteXML.keySet()) {
	    	OWLAnnotationProperty classInclusion = new OWLAnnotationPropertyImpl(XSkosVocabulary.inclusionNote.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(classInclusion,
					fact.getOWLLiteral(InclusionNoteXML.get(subject), OWL2Datatype.RDF_XML_LITERAL));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create(subject), annot);
			manager.applyChange(new AddAxiom(onto, axiom));
	    }
	    
  }
  
  public static void exclusionModeling() {

	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/exclusion>") ).
	    forEach(ax -> {
	    	genIdExcl.put(((OWLAnnotationAssertionAxiom) ax).getValue().toString(), ((OWLAnnotationAssertionAxiom) ax).getSubject().toString());
	      });
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2008/05/skos-xl#literalForm>") || 
//	    			((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/linearizationReference>") || 
	    			((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/foundationReference>") || 
	    			((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/exclusion>") ).
	    forEach(ax -> {
	    	
	    	if(genIdExcl.containsKey(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())) {
	    		
	    		if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/foundationReference>")) {
	    			
	    			OWLAnnotationProperty exclusion = new OWLAnnotationPropertyImpl(ANSICD11Vocabulary.exclusion.getIRI());
	    			OWLAnnotation annot2 = fact.getOWLAnnotation(exclusion, IRI.create(((OWLAnnotationAssertionAxiom) ax).getValue().toString()));
					OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create(genIdExcl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), annot2);
					manager.applyChange(new AddAxiom(onto, axiom2));
					
//					OWLClass disjClass = fact.getOWLClass(IRI.create(((OWLAnnotationAssertionAxiom) ax).getValue().toString()));
//					OWLClass owlClass = fact.getOWLClass(IRI.create(genIdExcl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())));
//					OWLDisjointClassesAxiom disjointClassesAxiom = fact.getOWLDisjointClassesAxiom(owlClass, disjClass);
//			        manager.addAxiom(onto, disjointClassesAxiom);
	    			
			        manager.applyChange(new RemoveAxiom(onto, ax));
	    			
	    		}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2008/05/skos-xl#literalForm>") && ((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("@"+PropertiesUtil.getProperties("icd_language"))) {
	    			
	    		
		    		if(ExclusionNote.containsKey(genIdExcl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()))) {
		    			String note = ExclusionNote.get(genIdExcl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()));
		    			note += "\n  - " + ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1);
		    			ExclusionNote.put(genIdExcl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()), note);
		    			
		    			String noteXml = ExclusionNoteXML.get(genIdExcl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()));
		    			noteXml = noteXml.substring(0, noteXml.length()-11) +"<li>" + ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1)+ "</li></ul></div>";
		    			ExclusionNoteXML.put(genIdExcl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()), noteXml);
		    			
		    		}else {
		    			String note = "Excludes :\n  - " + ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1);
		    			ExclusionNote.put(genIdExcl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()), note);
		    			
		    			String noteXML = "<div xml:lang=\""+ PropertiesUtil.getProperties("icd_language") +"\">Excludes : <ul><li>" + ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1) + "</li></ul></div>";
		    			ExclusionNoteXML.put(genIdExcl.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()), noteXML);
		    		}
		    		manager.applyChange(new RemoveAxiom(onto, ax));
	    		}
//	    		else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/foundationReference>")) {
//	    			manager.applyChange(new RemoveAxiom(onto, ax));
//	    		}
	    		
//	    		System.out.println(ax + "---" +genId.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()));
	    	}
	    	else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/exclusion>")) {
	    		manager.applyChange(new RemoveAxiom(onto, ax));
	    	}
	    	
	      });
	    for(String subject : ExclusionNote.keySet()) {
	    	OWLAnnotationProperty exclusionNote = new OWLAnnotationPropertyImpl(XSkosVocabulary.exclusionNote.getIRI());
  		OWLAnnotation annot = fact.getOWLAnnotation(exclusionNote, fact.getOWLLiteral(ExclusionNote.get(subject), PropertiesUtil.getProperties("icd_language")));
  		OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create(subject), annot);
  		manager.applyChange(new AddAxiom(onto, axiom));
	    }
	    
	    for(String subject : ExclusionNoteXML.keySet()) {
	    	OWLAnnotationProperty classInclusion = new OWLAnnotationPropertyImpl(XSkosVocabulary.exclusionNote.getIRI());
			OWLAnnotation annot = fact.getOWLAnnotation(classInclusion,
					fact.getOWLLiteral(ExclusionNoteXML.get(subject), OWL2Datatype.RDF_XML_LITERAL));
			OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create(subject), annot);
			manager.applyChange(new AddAxiom(onto, axiom));
	    }
  }
  
  public static void indexTermModeling() {
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/indexTerm>") ).
	    forEach(ax -> {
	    	indexTerm.put(((OWLAnnotationAssertionAxiom) ax).getValue().toString(), ((OWLAnnotationAssertionAxiom) ax).getSubject().toString());
	      });
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2008/05/skos-xl#literalForm>") ||
	    		((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/indexTerm>") ||
	    		((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/foundationReference>")).
	    forEach(ax -> {
	    	if(indexTerm.containsKey(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())) {
	    		
	    		if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2008/05/skos-xl#literalForm>") && ((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("@"+PropertiesUtil.getProperties("icd_language"))) {
//	    			System.out.println(((OWLAnnotationAssertionAxiom) ax).getValue().toString());
	    		OWLAnnotationProperty indexTermAnno = new OWLAnnotationPropertyImpl(ANSICD11Vocabulary.indexTerm.getIRI());
	    		OWLAnnotation annot2 = fact.getOWLAnnotation(indexTermAnno, fact.getOWLLiteral(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1), PropertiesUtil.getProperties("icd_language")));
	    		OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create(indexTerm.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), annot2);
	    		manager.applyChange(new AddAxiom(onto, axiom2));
		    		
	    		manager.applyChange(new RemoveAxiom(onto, ax));
	    	}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/foundationReference>")) {
	    		manager.applyChange(new RemoveAxiom(onto, ax));
	    	}
//	    		System.out.println(ax + "---" +genId.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()));
	    	}
	    	else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/indexTerm>")) {
	    		manager.applyChange(new RemoveAxiom(onto, ax));
	    	}
	    	
	      });
	    
	    
	    
	    
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/foundationChildElsewhere>") ).
	    forEach(ax -> {
	    	foundationChild.put(((OWLAnnotationAssertionAxiom) ax).getValue().toString(), ((OWLAnnotationAssertionAxiom) ax).getSubject().toString());
	      });
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/linearizationReference>") ||
	    		((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/foundationReference>")||
	    		(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2008/05/skos-xl#literalForm>") && ((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("@"+PropertiesUtil.getProperties("icd_language")) )||
	    		((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/foundationChildElsewhere>")).
	    forEach(ax -> {
	    	if(foundationChild.containsKey(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())) {
	    		
	    		if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/linearizationReference>")) {
	    		OWLAnnotationProperty foundationChildAnno = new OWLAnnotationPropertyImpl(ANSICD11Vocabulary.foundationChild.getIRI());
	    		OWLAnnotation annot2 = fact.getOWLAnnotation(foundationChildAnno, IRI.create(((OWLAnnotationAssertionAxiom) ax).getValue().toString()));
	    		OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create(foundationChild.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), annot2);
	    		manager.applyChange(new AddAxiom(onto, axiom2));
	    		manager.applyChange(new RemoveAxiom(onto, ax));
	    		}else {
	    			manager.applyChange(new RemoveAxiom(onto, ax));
	    		}
		    		
	    		
//	    		System.out.println(ax + "---" +genId.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()));
	    	}else {
	    		manager.applyChange(new RemoveAxiom(onto, ax));
	    	}
	    	
	      });
  }
  
  public static void altLabelModeling() {
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2008/05/skos-xl#altLabel>") ).
	    forEach(ax -> {
	    	indexTerm.put(((OWLAnnotationAssertionAxiom) ax).getValue().toString(), ((OWLAnnotationAssertionAxiom) ax).getSubject().toString());
	      });
	    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2008/05/skos-xl#literalForm>") ||
	    		((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2008/05/skos-xl#altLabel>") ).
	    forEach(ax -> {
	    	if(indexTerm.containsKey(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())) {
	    		
	    		if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2008/05/skos-xl#literalForm>") && ((OWLAnnotationAssertionAxiom) ax).getValue().toString().contains("@"+PropertiesUtil.getProperties("icd_language"))) {
//	    			System.out.println(((OWLAnnotationAssertionAxiom) ax).getValue().toString());
	    		OWLAnnotationProperty indexTermAnno = new OWLAnnotationPropertyImpl(SKOSVocabulary.ALTLABEL.getIRI());
	    		OWLAnnotation annot2 = fact.getOWLAnnotation(indexTermAnno, fact.getOWLLiteral(((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1), PropertiesUtil.getProperties("icd_language")));
	    		OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create(indexTerm.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString())), annot2);
	    		manager.applyChange(new AddAxiom(onto, axiom2));
		    		
	    		manager.applyChange(new RemoveAxiom(onto, ax));
	    	}
//	    		System.out.println(ax + "---" +genId.get(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()));
	    	}
	    	else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2008/05/skos-xl#altLabel>")) {
	    		manager.applyChange(new RemoveAxiom(onto, ax));
	    	}
	    	
	      });
	     
  }
  
  public static void postcoordinationModeling() {
	  
	  OWLClass owlClassPostcoordination = fact.getOWLClass(IRI.create("https://data.esante.gouv.fr/oms/cim11/schema#PostCoordinationScaleInfo"));
	  OWLAxiom declarePostcoordination = fact.getOWLDeclarationAxiom(owlClassPostcoordination);
	  manager.applyChange(new AddAxiom(onto, declarePostcoordination));
	  
	  OWLSubClassOfAxiom ax3 = fact.getOWLSubClassOfAxiom(owlClassPostcoordination, fact.getOWLClass(IRI.create("http://id.who.int/icd/release/11/mms")));
		manager.applyChange(new AddAxiom(onto, ax3));
	  
	  OWLAnnotation annot1 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral("PostCoordinationScaleInfo", PropertiesUtil.getProperties("icd_language")));
	  OWLAxiom axiom1 = fact.getOWLAnnotationAssertionAxiom(IRI.create("https://data.esante.gouv.fr/oms/cim11/schema#PostCoordinationScaleInfo"), annot1);
	  manager.applyChange(new AddAxiom(onto, axiom1));
	  
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.CLASS_ASSERTION)).
	  	forEach(ax -> {
	    	manager.applyChange(new RemoveAxiom(onto, ax));
	      });
	  
	  onto.axioms().
	  filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label")).
	    forEach(ax -> {
	    	if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label")) {
	    		String label = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().split("@")[0].length()-1);
		    	String key = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
		    	entityLabel.put(key, label);
	    		}
	    	
	    });
	    	
	  onto.axioms().
	  filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/axisName>")).
	    forEach(ax -> {
	    	if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/axisName>")) {
	    		String axe = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("schema")+7);
	    		String iri = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
		    	OWLClass owlClass = fact.getOWLClass(IRI.create(iri));
				OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
				manager.applyChange(new AddAxiom(onto, declare));
				
				OWLSubClassOfAxiom ax1 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create(iri)), fact.getOWLClass(IRI.create("https://data.esante.gouv.fr/oms/cim11/schema#PostCoordinationScaleInfo-"+axe)));
				manager.applyChange(new AddAxiom(onto, ax1));
				
				OWLSubClassOfAxiom ax2 = fact.getOWLSubClassOfAxiom(fact.getOWLClass(IRI.create("https://data.esante.gouv.fr/oms/cim11/schema#PostCoordinationScaleInfo-"+axe)), fact.getOWLClass(IRI.create("https://data.esante.gouv.fr/oms/cim11/schema#PostCoordinationScaleInfo")));
				manager.applyChange(new AddAxiom(onto, ax2));
				
				
				String key = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
				String Label = "Post-Coordination : " 
				+ entityLabel.get(key.substring(0, key.indexOf("postcoordinationScale")-1))
				+ " "
				+ axe;
				OWLAnnotation annot2 = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(Label, PropertiesUtil.getProperties("icd_language")));
	    		OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create(iri), annot2);
	    		manager.applyChange(new AddAxiom(onto, axiom2));
	    		
	    		OWLAnnotation annot = fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(axe, PropertiesUtil.getProperties("icd_language")));
	    		OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(IRI.create("https://data.esante.gouv.fr/oms/cim11/schema#PostCoordinationScaleInfo-"+axe), annot);
	    		manager.applyChange(new AddAxiom(onto, axiom));
	    	}
	    	
	      });
	    
  }
  
  public static void addNotationAndTypeModeling() {
	  	
	  onto.axioms().
	  filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/code>") 
	    		|| ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/codingRange>") 
	    		|| ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/classKind>")).
	    forEach(ax -> {
	    	if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/code>") 
		    		|| ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/codingRange>")) {
	    		
	    		OWLAnnotationProperty notation = new OWLAnnotationPropertyImpl(SkosVocabulary.notation.getIRI());
				OWLAnnotation annot = fact.getOWLAnnotation(notation, ((OWLAnnotationAssertionAxiom) ax).getValue());
				OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), annot);
				manager.applyChange(new AddAxiom(onto, axiom));	
	    	}else {
	    		OWLAnnotationProperty type = new OWLAnnotationPropertyImpl(DCVocabulary.type.getIRI());
				OWLAnnotation annot = fact.getOWLAnnotation(type, ((OWLAnnotationAssertionAxiom) ax).getValue());
				OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), annot);
				manager.applyChange(new AddAxiom(onto, axiom));	
	    	}
	    	
	      });
	    
  }

  public static void cleanning() {
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#notation>") ||
				((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/codingRange>") ||
				((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/blockId>")).
	    forEach(ax -> {
	    		String code = ((OWLAnnotationAssertionAxiom) ax).getValue().toString().substring(1, ((OWLAnnotationAssertionAxiom) ax).getValue().toString().indexOf("^^xsd")-1);
	    	    if(code.isEmpty()) {
	    			manager.applyChange(new RemoveAxiom(onto, ax));
	    		}
	    		
	      });
  }
  
  public static void cleanning2() {
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.DISJOINT_CLASSES)).
	    forEach(ax -> {
	    		manager.applyChange(new RemoveAxiom(onto, ax));
	    		
	    		
	      });
  }

  public static void fondationModeling() {
	  
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    forEach(ax -> {
	    	if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#broader>") ||
	    			((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#narrower>")) {
    			manager.applyChange(new RemoveAxiom(onto, ax));
    		}else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#prefLabel>") ) {
	    			
    		    OWLAnnotation annot = fact.getOWLAnnotation(fact.getRDFSLabel(),((OWLAnnotationAssertionAxiom) ax).getValue());
    		    OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), annot);
    			manager.applyChange(new AddAxiom(onto, axiom));
    			
    			//String url = "https://icd.who.int/dev11/f/en#/" + ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
    			//OWLAnnotationProperty browserUrl = new OWLAnnotationPropertyImpl(ICDVocabulary.browserUrl.getIRI());
	    		//OWLAnnotation annot2 = fact.getOWLAnnotation(browserUrl, fact.getOWLLiteral(url));
	    		//OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(((OWLAnnotationAssertionAxiom) ax).getSubject(), annot2);
	    		//manager.applyChange(new AddAxiom(onto, axiom2));
	    		
    			manager.applyChange(new RemoveAxiom(onto, ax));
	    		}
	      });
  }

  public static void postCoordinationMMSmodifing() {
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/postcoordinationScale>")).
	    forEach(ax -> {
	    	OWLAnnotationProperty PostCoordinationSourceEntity = new OWLAnnotationPropertyImpl(ANSICD11Vocabulary.PostCoordinationSourceEntity.getIRI());
    		OWLAnnotation annot2 = fact.getOWLAnnotation(PostCoordinationSourceEntity, IRI.create(((OWLAnnotationAssertionAxiom) ax).getSubject().toString()));
    		OWLAxiom axiom2 = fact.getOWLAnnotationAssertionAxiom(IRI.create(((OWLAnnotationAssertionAxiom) ax).getValue().toString()), annot2);
    		manager.applyChange(new AddAxiom(onto, axiom2));
	    		
	      });
	  
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#definition>")).
	    forEach(ax -> {
	    	String def = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
	    	if(def.contains("[No translation available]")) {
    		manager.applyChange(new RemoveAxiom(onto, ax));
    		}
	    		
	      });
  }
  
  public static void fondationCleanning() { //pour fondation
	  
	  onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
	    forEach(ax -> {
	    	String def = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
	    	if(def.contains("[No translation available]")) {
    		manager.applyChange(new RemoveAxiom(onto, ax));
    		}
	    		
	      });
  }

}
