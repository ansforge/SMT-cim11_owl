package fr.gouv.esante.pml.smt.ema;

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
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.SetOntologyID;

import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.SkosVocabulary;




public class XLSXOWLTransformer {
	
	//*********
	private static String xlsxEmaFileName = PropertiesUtil.getEMAProperties("xlsxEmaFile");
	  private static String owlEmaFileName = PropertiesUtil.getEMAProperties("owlEmaFileName");

	  private static OWLOntologyManager man = null;
	  private static OWLOntology onto = null;
	  private static OWLDataFactory fact = null;
	  
	  private static OWLAnnotationProperty skosNotation  = null;
	  private static OWLAnnotationProperty rdfsLabel  = null;
	  private static OWLAnnotationProperty skosAltLabel  = null;
	  
	
	public static void main(String[] args) throws Exception {
		
		ChargeMapping.chargeEMAExcelConceptToList(xlsxEmaFileName);
		  
		final OutputStream fileoutputstream = new FileOutputStream(owlEmaFileName);
		 man = OWLManager.createOWLOntologyManager();
		 onto = man.createOntology(IRI.create(PropertiesUtil.getEMAProperties("terminologie_IRI")));
		 fact = onto.getOWLOntologyManager().getOWLDataFactory();
		
		 skosNotation =  fact.getOWLAnnotationProperty(SkosVocabulary.NOTATION.getIRI());
		 rdfsLabel =  fact.getOWLAnnotationProperty(fr.gouv.esante.pml.smt.utils.OWLRDFVocabulary.RDFS_LABEL.getIRI());
		 skosAltLabel =  fact.getOWLAnnotationProperty(SkosVocabulary.ALTLABEL.getIRI());
		 
		 
		    OWLClass owlClass = null;
		    
		    
		    createPrincipalNoeud();
		    

		    
		    for(String id: ChargeMapping.listConceptsEma.keySet()) {
		    	final String about = PropertiesUtil.getEMAProperties("terminologie_URI") + id;
		        owlClass = fact.getOWLClass(IRI.create(about));
		        OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
		        man.applyChange(new AddAxiom(onto, declare));
		        
		        
		        
		        String aboutSubClass = null;
		        aboutSubClass = PropertiesUtil.getEMAProperties("URI_parent") ;
		        OWLClass subClass = fact.getOWLClass(IRI.create(aboutSubClass));
		        OWLAxiom axiom = fact.getOWLSubClassOfAxiom(owlClass, subClass);
		        man.applyChange(new AddAxiom(onto, axiom));
		        
		        
		       

		        
		        
		       addLateralAxioms(skosNotation, id, owlClass);
		       
		       //if (!ChargerMapping.listConceptsEma.get(id).get(0).isEmpty()) {
		         //addLateralAxioms(rdfsLabel, ChargerMapping.listConceptsEma.get(id).get(0), owlClass, "N/A");
		         //addLateralAxioms(rdfsLabel, ChargerMapping.listConceptsEma.get(id).get(0), owlClass, "fr");
		         //addLateralAxioms(rdfsLabel, ChargerMapping.listConceptsEma.get(id).get(0), owlClass, "en");
		         addLateralAxioms(rdfsLabel, ChargeMapping.listConceptsEma.get(id).get(0), owlClass); //Substance Preferred Name
		         
		      // }
		       
		       if (!ChargeMapping.listConceptsEma.get(id).get(1).isEmpty()) {//Substance Alias
		         
		    	    String[] listeAlias =  ChargeMapping.listConceptsEma.get(id).get(1).split("\\|"); 
		    	 
                     for(int j=0; j<listeAlias.length;j++) {
		    		 
		    		      if(!listeAlias[j].equals(ChargeMapping.listConceptsEma.get(id).get(0)))
				    	      addLateralAxioms(skosAltLabel, listeAlias[j], owlClass);
		    		 
		    	     }
		    	   
		         //if(!ChargerMapping.listConceptsEma.get(id).get(1).equals(ChargerMapping.listConceptsEma.get(id).get(0)))
		    	   
		           //addLateralAxioms(skosAltLabel, ChargerMapping.listConceptsEma.get(id).get(1), owlClass);
		         //if(!ChargerMapping.listConceptsEma.get(id).get(1).equals(ChargerMapping.listConceptsEma.get(id).get(0)))
		          // addLateralAxioms(skosAltLabel, ChargerMapping.listConceptsEma.get(id).get(1), owlClass, "en");
		       }
		       
		       if (!ChargeMapping.listConceptsEma.get(id).get(2).isEmpty()) {
		         //addLateralAxioms(rdfsLabel, ChargerMapping.listConceptsEma.get(id).get(2), owlClass, "fr");
		    	   
		    	 String[] listeTranslateFR =  ChargeMapping.listConceptsEma.get(id).get(2).split("\\|");
		    	   
		    	 for(int j=0; j<listeTranslateFR.length;j++) {
		    		 
		    		 if(!listeTranslateFR[j].equals(ChargeMapping.listConceptsEma.get(id).get(0)))
				    	   addLateralAxioms(skosAltLabel, listeTranslateFR[j], owlClass, "fr");
		    		 
		    	 }
		    	 
		    	 // if(!ChargerMapping.listConceptsEma.get(id).get(2).equals(ChargerMapping.listConceptsEma.get(id).get(0)))
		    	   //addLateralAxioms(skosAltLabel, ChargerMapping.listConceptsEma.get(id).get(2), owlClass, "fr");
		       }
		       
		       if (!ChargeMapping.listConceptsEma.get(id).get(3).isEmpty()) {
		         //addLateralAxioms(rdfsLabel, ChargerMapping.listConceptsEma.get(id).get(3), owlClass, "en");
		    	   
		    	   String[] listeTranslateEN =  ChargeMapping.listConceptsEma.get(id).get(3).split("\\|");
		    	   
		    	   for(int j=0; j<listeTranslateEN.length;j++) {
			    		 
			    		 if(!listeTranslateEN[j].equals(ChargeMapping.listConceptsEma.get(id).get(0)))
					    	   addLateralAxioms(skosAltLabel, listeTranslateEN[j], owlClass, "en");
			    		 
			    	 }
		    	   
		    	  //if(!ChargerMapping.listConceptsEma.get(id).get(3).equals(ChargerMapping.listConceptsEma.get(id).get(0))) 
		    	    //addLateralAxioms(skosAltLabel, ChargerMapping.listConceptsEma.get(id).get(3), owlClass, "en");
		       }
		        
		    }
		    
		    
		    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
		   // ontologyFormat.setPrefix("dct", "http://purl.org/dc/terms/");
		    
		    
		    IRI iri = IRI.create(PropertiesUtil.getEMAProperties("terminologie_IRI"));
		    man.applyChange(new SetOntologyID(onto,  new OWLOntologyID(iri)));
		   
		  //  addPropertiesOntology();
		    
		    man.saveOntology(onto, ontologyFormat, fileoutputstream);
		    fileoutputstream.close();
		    System.out.println("Done.");
		
		

	}
	
	public static void addLateralAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass) {
	    final OWLAnnotation annotation =
	        fact.getOWLAnnotation(prop, fact.getOWLLiteral(val));
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(onto, axiom));
	  }
  
  public static void addLateralAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass, String lang) {
	    final OWLAnnotation annotation =
	        fact.getOWLAnnotation(prop, fact.getOWLLiteral(val, lang));
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(onto, axiom));
	  }
  
  
  
  
  
  
  public static void createPrincipalNoeud() {
	  
	  // String noeud_parent = PropertiesUtil.getProperties("noeud_parent");
	   String noeud_parent_label=PropertiesUtil.getEMAProperties("label_noeud_parent");
	   String noeud_parent_notation=PropertiesUtil.getEMAProperties("notation_noeud_parent");
	    
	   final String aboutSubClass1 = PropertiesUtil.getEMAProperties("URI_parent") ;
	   OWLClass subClass1 = fact.getOWLClass(IRI.create(aboutSubClass1));
       addLateralAxioms(skosNotation, noeud_parent_notation, subClass1);
       addLateralAxioms(rdfsLabel, noeud_parent_label, subClass1, "fr");
       addLateralAxioms(rdfsLabel, noeud_parent_label, subClass1, "en");
	  
  }
  
  
  
  

}
