package fr.gouv.esante.pml.smt.ncbi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.io.IRIDocumentSource;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.util.AxiomSubjectProviderEx;

import fr.gouv.esante.pml.smt.utils.ChargeMapping;
import fr.gouv.esante.pml.smt.utils.ClientNCBIHttp;
import fr.gouv.esante.pml.smt.utils.CreateTmpFile;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.SkosVocabulary;

//import cz.cvut.kbss.owldiff.change.AxiomChanges;



public class NCBIOWLTransformer {
	
	 private static OWLOntologyManager man = null;
	 private static OWLOntology originalO = null;
	 private static OWLDataFactory fact = null;

	 private static OWLAnnotationProperty skosExactMatch  = null;
	  private static OWLAnnotationProperty skosnarrowMatch  = null;
	 private static OWLAnnotationProperty skosbroadMatch = null;
	 private static OWLAnnotationProperty skosrelatedMatch = null;
	 private static OWLAnnotationProperty skosnotation = null;
	 private static OWLAnnotationProperty skosaltLabel = null;
	 private static OWLAnnotationProperty rdfsLabel = null;
	 private static final String IRICIM11 = "http://id.who.int/icd/release/11/mms/";
	 private static final String IRINCIT = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#";
	 private static final String IRIEMDN = "http://data.esante.gouv.fr/ec/emdn/";
	 private static final String IRILOINC = "http://loinc.org/";
	 private static Logger logger = Logger.getLogger(NCBIOWLTransformer.class);

	public static void main(String[] args) throws Exception {
		
		
		logger.info("Début Création de fihcier temporaire NCBI");
				
		CreateTmpFile.convertFile("");
		
		logger.info("Fin Création de fihcier temporaire NCBI");
		
		
		logger.info("Début mappig PartLoinc codeLoinc NCBI");
		
		ChargeMapping.convertNCBIFile("");
		
		logger.info("Fin mappig PartLoinc codeLoinc NCBI");
		
		
		
		logger.info("Début crétion Fichier NCBI");
		
		 
		 OWLClass owlClass = null;
		
		man = OWLManager.createOWLOntologyManager();
		
		//Fichier Final OWL à generer 
		String outputFile = PropertiesUtil.getNCBIProperties("owlOutputFileNcbi"); 
		final OutputStream fileoutputstream = new FileOutputStream(outputFile);
		 
		//Fichier temporiare 
		 String oldNcbi =  PropertiesUtil.getNCBIProperties("owlNcbiFileTmp"); 
	
		
		 URI oldNcbiFile = null;
	       
	        if (new File(oldNcbi).exists()) {
	        	oldNcbiFile = new File(oldNcbi).getAbsoluteFile().toURI();
	        }
		
		
	       // final OWLOntology 
	        originalO =
	        		man.loadOntologyFromOntologyDocument(new IRIDocumentSource(
	                    IRI.create(oldNcbiFile)));
	        
	        fact = originalO.getOWLOntologyManager().getOWLDataFactory();
	        
	        skosExactMatch =  fact.getOWLAnnotationProperty(org.semanticweb.owlapi.vocab.SKOSVocabulary.EXACTMATCH.getIRI());
	        skosnarrowMatch =  fact.getOWLAnnotationProperty(org.semanticweb.owlapi.vocab.SKOSVocabulary.NARROWMATCH.getIRI());
	        skosbroadMatch =  fact.getOWLAnnotationProperty(org.semanticweb.owlapi.vocab.SKOSVocabulary.BROADMATCH.getIRI());
	        skosrelatedMatch =  fact.getOWLAnnotationProperty(org.semanticweb.owlapi.vocab.SKOSVocabulary.RELATEDMATCH.getIRI());
	        skosnotation =  fact.getOWLAnnotationProperty(SkosVocabulary.NOTATION.getIRI());
	        skosaltLabel = fact.getOWLAnnotationProperty(SkosVocabulary.ALTLABEL.getIRI());
	        rdfsLabel =  fact.getOWLAnnotationProperty(fr.gouv.esante.pml.smt.utils.OWLRDFVocabulary.RDFS_LABEL.getIRI());

	        
	        
	        
          for (OWLAxiom axioms : originalO.getAxioms()) {
	        	
        	  
        	  
        	  
        	  
        	  axioms.accept(new OWLAxiomVisitor() {
    	    	  
    	    	  
     	    	 public void visit(OWLSubClassOfAxiom arg0) {
     	              
     	    		 

     	            }
     	    	  
     	    	  
     	    	  
     	    	  public void visit(OWLDeclarationAxiom arg0) {
     	    		  
     	    		
      	    		 
     	            }
     	    	  
     	    	  
     	    	  public void visit(OWLAnnotationAssertionAxiom arg0) {
     	    		  
     	    		 
     	    		  
     	    		 
     	    		    
     	    		OWLAnnotation annotation1 = arg0.getAnnotation();
     	    		
     	    		// System.out.println(annotation1.getProperty().toString());
     	    		
     	    		 if("<http://www.w3.org/2004/02/skos/core#notation>".equals(annotation1.getProperty().toString()))
    	    		  {
    	    			String conceptIri = AxiomSubjectProviderEx.getSubject(axioms).toString();
     	    			String propValue = annotation1.getValue().toString();
    	    			man.applyChange(new RemoveAxiom(originalO, axioms));
    	    			addLateralAxiomss(skosnotation, propValue.split("\\@")[0].replaceAll("\"", ""), fact.getOWLClass(IRI.create(conceptIri)));
    	    		  
    	    		  }
     	    		 
     	    	//modif skos altLabel	 
     	    		if("<http://www.w3.org/2004/02/skos/core#altLabel>".equals(annotation1.getProperty().toString()))
  	    		  {
  	    			String conceptIri = AxiomSubjectProviderEx.getSubject(axioms).toString();
   	    			String propValue = annotation1.getValue().toString();
  	    			man.applyChange(new RemoveAxiom(originalO, axioms));
  	    			addLateralAxiomss(skosaltLabel, propValue.split("\\@")[0].replaceAll("\"", ""), fact.getOWLClass(IRI.create(conceptIri)));
  	    		  
  	    		  }
     	    		
     	    		//modif rdfs:label
     	    		if("rdfs:label".equals(annotation1.getProperty().toString()))
    	    		  {
    	    			String conceptIri = AxiomSubjectProviderEx.getSubject(axioms).toString();
     	    			String propValue = annotation1.getValue().toString();
    	    			man.applyChange(new RemoveAxiom(originalO, axioms));
    	    			addLateralAxiomss(rdfsLabel, propValue.split("\\@")[0].replaceAll("\"", ""), fact.getOWLClass(IRI.create(conceptIri)));
    	    		  
    	    		  }
     	    		
     	    		
     	    		
     	    		 if("<https://data.esante.gouv.fr/chu-rouen/NCBI_taxonomy/AUTO_MAPPING>".equals(annotation1.getProperty().toString()))
     	    		  {
     	    			man.applyChange(new RemoveAxiom(originalO, axioms));
     	    		  
     	    		  }
     	    		 
     
     	    		
     	    	if("<https://data.esante.gouv.fr/chu-rouen/NCBI_taxonomy/EXACT_MAPPING>".equals(annotation1.getProperty().toString()))
   	    		   {
     	    			String propValue = annotation1.getValue().toString();
     	    			String valueprop =propValue.replaceAll("\"", "").split("\\^\\^")[0];
     	    			
     	    			String conceptIri = AxiomSubjectProviderEx.getSubject(axioms).toString(); // URI de classe
     	    			if("CIM-11".equals(valueprop.split(":")[0])) {
     	    				
     	    			   String codeCim11 = valueprop.split(":")[1];
     	    			        	    			   
     	    			   try {
							int status =  ClientNCBIHttp.checkConceptId(IRICIM11+codeCim11, "terminologie-cim11-mms");
							if(status==200) {
							addLateralAxioms(skosExactMatch, IRICIM11+codeCim11, fact.getOWLClass(IRI.create(conceptIri)));
							}
							else {
								   logger.warn(codeCim11+" n'existe pas dans le SMT");
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
     	    			   
     	    			   
     	    			}
     	    			if("NCIT".equals(valueprop.split(":")[0])) {
         	    			String codeNcit = valueprop.split(":")[1];
         	    			addLateralAxioms(skosExactMatch, IRINCIT+codeNcit, fact.getOWLClass(IRI.create(conceptIri)));
         	    		}
     	    			if("EMDN".equals(valueprop.split(":")[0])) {
         	    			String codeEmdn = valueprop.split(":")[1];         	    			
         	    			 try {
     							int status =  ClientNCBIHttp.checkConceptId(IRIEMDN+codeEmdn, "terminologie-emdn");
     							if(status==200) {
     	         	    		 addLateralAxioms(skosrelatedMatch, IRIEMDN+codeEmdn, fact.getOWLClass(IRI.create(conceptIri)));

     							}
     							else {
     								logger.warn(codeEmdn+" n'existe pas dans le SMT");
     							}
     						} catch (IOException e) {
     							// TODO Auto-generated catch block
     							e.printStackTrace();
     						}
         	    			
         	    			
         	    			
         	    		}
     	    		
     	    			
     	    			if("LOINC".equals(valueprop.split(":")[0])) {
     	    				
     	    				String codePartLoinc = valueprop.split(":")[1];
     	    				addLateralAxiomXml(skosExactMatch, IRILOINC+codePartLoinc, codePartLoinc, fact.getOWLClass(IRI.create(conceptIri)));
         	    			
								  try { 
									  Iterator<String> it =
								  ChargeMapping.listNCBIConcepts.get(codePartLoinc).iterator();
								   while(it.hasNext()) {
								     String value = it.next();
								     addLateralAxiomXml(skosrelatedMatch, IRILOINC+value, value, fact.getOWLClass(IRI.create(conceptIri))); } 
								  
								 }catch(NullPointerException e) {
								  
								  
									  logger.warn(codePartLoinc+" n'existe pas dans le projet LOINC");
								   
								 }
								 
         	    			  
         	    			
         	    		}
     	    			
   	    			man.applyChange(new RemoveAxiom(originalO, axioms));
   	    		  
   	    		   }
     	    		
     	    		
     	    		if("<https://data.esante.gouv.fr/chu-rouen/NCBI_taxonomy/BTNT_MAPPING>".equals(annotation1.getProperty().toString()))
    	    		   {
      	    			
     	    			String propValue = annotation1.getValue().toString();
     	    			String valueprop =propValue.replaceAll("\"", "").split("\\^\\^")[0];
     	    			String conceptIri = AxiomSubjectProviderEx.getSubject(axioms).toString(); // URI de classe
     	    			if("CIM-11".equals(valueprop.split(":")[0])) {
     	    				
      	    			   String codeCim11 =valueprop.split(":")[1];
      	    			         	    			   
      	    			   try {
 							int status =  ClientNCBIHttp.checkConceptId(IRICIM11+codeCim11, "terminologie-cim11-mms");
 							if(status==200) {
 							addLateralAxioms(skosnarrowMatch, IRICIM11+codeCim11, fact.getOWLClass(IRI.create(conceptIri)));
 							}
 							else {
 								logger.warn(codeCim11+" n'existe pas dans le SMT");
 							}
 						} catch (IOException e) {
 							// TODO Auto-generated catch block
 							e.printStackTrace();
 						}
      	    			   
      	    			   
      	    			}
      	    			if("NCIT".equals(valueprop.split(":")[0])) {
      	    				
          	    			String codeNcit = valueprop.split(":")[1];
          	    			addLateralAxioms(skosnarrowMatch, IRINCIT+codeNcit, fact.getOWLClass(IRI.create(conceptIri)));
          	    		}
      	    			if("EMDN".equals(valueprop.split(":")[0])) {
          	    			String codeEmdn = valueprop.split(":")[1];
          	    					propValue.split("\\^\\^")[0].substring(6, propValue.split("\\^\\^")[0].length()-1 );	
          	    			
          	    			try {
     							int status =  ClientNCBIHttp.checkConceptId(IRIEMDN+codeEmdn, "terminologie-emdn");
     							if(status==200) {
     	          	    	     addLateralAxioms(skosnarrowMatch, IRIEMDN+codeEmdn, fact.getOWLClass(IRI.create(conceptIri)));


     							}
     							else {
     								logger.warn(codeEmdn+" n'existe pas dans le SMT");
     							}
     						} catch (IOException e) {
     							// TODO Auto-generated catch block
     							e.printStackTrace();
     						}
          	    		}
      	    			
      	    			
      	    			if("LOINC".equals(valueprop.split(":")[0])) {
      	    				String codePartLoinc = valueprop.split(":")[1];	
      	    		     	addLateralAxiomXml(skosnarrowMatch, IRILOINC+codePartLoinc, codePartLoinc, fact.getOWLClass(IRI.create(conceptIri)));
          	    			
								
								  try { Iterator<String> it =
								    ChargeMapping.listNCBIConcepts.get(codePartLoinc).iterator();
								    while(it.hasNext()) {
								    	String value = it.next();
								    addLateralAxiomXml(skosrelatedMatch, IRILOINC+value, value,
								  fact.getOWLClass(IRI.create(conceptIri))); 
								    
								    } }
								  
								  
								  catch(NullPointerException e) {
								  
									  logger.warn(codePartLoinc+" n'existe pas dans le projet LOINC");
								  
								 }
								 
          	    			
          	    	}
      	    			
    	    			
      	    			 man.applyChange(new RemoveAxiom(originalO, axioms));
    	    		   }
     	    		
     	    		
     	    		if("<https://data.esante.gouv.fr/chu-rouen/NCBI_taxonomy/NTBT_MAPPING>".equals(annotation1.getProperty().toString()))
 	    		   {
   	    			
     	    		  String propValue = annotation1.getValue().toString();
   	    			 String valueprop =propValue.replaceAll("\"", "").split("\\^\\^")[0];
   	    			  String conceptIri = AxiomSubjectProviderEx.getSubject(axioms).toString(); // URI de classe
   	    			if("CIM-11".equals(valueprop.split(":")[0])) {
 	    				
   	    			   String codeCim11 = valueprop.split(":")[1];
 
   	    			   try {
							int status =  ClientNCBIHttp.checkConceptId(IRICIM11+codeCim11, "terminologie-cim11-mms");
							if(status==200) {
							addLateralAxioms(skosbroadMatch, IRICIM11+codeCim11, fact.getOWLClass(IRI.create(conceptIri)));
							}
							else {
								logger.warn(codeCim11+" n'existe pas dans le SMT");
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
   	    			   
   	    			   
   	    			}
   	    			if("NCIT".equals(valueprop.split(":")[0])) {
       	    			String codeNcit = valueprop.split(":")[1];
       	    			addLateralAxioms(skosbroadMatch, IRINCIT+codeNcit, fact.getOWLClass(IRI.create(conceptIri)));
       	    		}
   	    			if("EMDN".equals(valueprop.split(":")[0])) {
       	    			String codeEmdn = valueprop.split(":")[1];
       	    			try {
 							int status =  ClientNCBIHttp.checkConceptId(IRIEMDN+codeEmdn, "terminologie-emdn");
 							if(status==200) {
 		       	    			addLateralAxioms(skosbroadMatch, IRIEMDN+codeEmdn, fact.getOWLClass(IRI.create(conceptIri)));

 							}
 							else {
 								logger.warn(codeEmdn+" n'existe pas dans le SMT");
 							}
 						} catch (IOException e) {
 							// TODO Auto-generated catch block
 							e.printStackTrace();
 						}
       	    		}
   	    			
   	    			
   	    			if("LOINC".equals(valueprop.split(":")[0])) {
   	    				String codePartLoinc = valueprop.split(":")[1];	
   	    				addLateralAxiomXml(skosbroadMatch, IRILOINC+codePartLoinc, codePartLoinc, fact.getOWLClass(IRI.create(conceptIri)));
       	    			
								
							  try { 
								  Iterator<String> it =ChargeMapping.listNCBIConcepts.get(codePartLoinc).iterator();
							  
								  while(it.hasNext()) {
								    String value = it.next();
								  addLateralAxiomXml(skosrelatedMatch, IRILOINC+value, value,fact.getOWLClass(IRI.create(conceptIri))); 
								  
								  } 
								  
							  }
							  
							 catch(NullPointerException e) {

								  logger.warn(codePartLoinc+" n'existe pas dans le projet LOINC");
								  
							  }
								 
       	    			
       	    		}
 	    			
   	    			  man.applyChange(new RemoveAxiom(originalO, axioms));
 	    		   }
     	    		
     	    		if("<https://data.esante.gouv.fr/chu-rouen/NCBI_taxonomy/RT_MAPPING>".equals(annotation1.getProperty().toString()))
  	    		   {
     	    			String propValue = annotation1.getValue().toString();
     	    			String valueprop =propValue.replaceAll("\"", "").split("\\^\\^")[0];
    	    			String conceptIri = AxiomSubjectProviderEx.getSubject(axioms).toString(); // URI de classe
    	    			
    	    			if("CIM-11".equals(valueprop.split(":")[0])) {
     	    				
    	   	    			   String codeCim11 = valueprop.split(":")[1];
    	   	    			   
    	   	    			   
    	   	    			   try {
    								int status =  ClientNCBIHttp.checkConceptId(IRICIM11+codeCim11, "terminologie-cim11-mms");
    								if(status==200) {
    								addLateralAxioms(skosrelatedMatch, IRICIM11+codeCim11, fact.getOWLClass(IRI.create(conceptIri)));
    								}
    								else {
    									logger.warn(codeCim11+" n'existe pas dans le SMT");
    								}
    							} catch (IOException e) {
    								// TODO Auto-generated catch block
    								e.printStackTrace();
    							}
    	   	    			   
    	   	    			   
    	   	    			}
    	   	    			if("NCIT".equals(valueprop.split(":")[0])) {
    	       	    			String codeNcit = valueprop.split(":")[1];
    	       	    			addLateralAxioms(skosrelatedMatch, IRINCIT+codeNcit, fact.getOWLClass(IRI.create(conceptIri)));
    	       	    		}
    	   	    			if("EMDN".equals(valueprop.split(":")[0])) {
    	       	    			String codeEmdn = valueprop.split(":")[1];
    	       	    			try {
    	 							int status =  ClientNCBIHttp.checkConceptId(IRIEMDN+codeEmdn, "terminologie-emdn");
    	 							if(status==200) {
    	 		       	    			addLateralAxioms(skosrelatedMatch, IRIEMDN+codeEmdn, fact.getOWLClass(IRI.create(conceptIri)));

    	 							}
    	 							else {
    	 								logger.warn(codeEmdn+" n'existe pas dans le SMT");
    	 							}
    	 						} catch (IOException e) {
    	 							// TODO Auto-generated catch block
    	 							e.printStackTrace();
    	 						}
    	       	    		}
    	   	    			
    	   	    			
    	   	    			
    	   	    			if("LOINC".equals(valueprop.split(":")[0])) {
    	   	    				String codePartLoinc = valueprop.split(":")[1];	
    	   	    				addLateralAxiomXml(skosrelatedMatch, IRILOINC+codePartLoinc, codePartLoinc, fact.getOWLClass(IRI.create(conceptIri)));
    	       	    			
								
								  try { 
									  Iterator<String> it = ChargeMapping.listNCBIConcepts.get(codePartLoinc).iterator();
								   while(it.hasNext()) {
									  
									  String value = it.next();
								      addLateralAxiomXml(skosrelatedMatch, IRILOINC+value, value, fact.getOWLClass(IRI.create(conceptIri)));
								  
								  }
								  
								  }catch(NullPointerException e) {
								  
									  logger.warn(codePartLoinc+" n'existe pas dans le projet LOINC"); 
								  
								  }
								 
    	       	    		}
  	    			
    	    			 man.applyChange(new RemoveAxiom(originalO, axioms));
  	    		   }
     	    		
     	    		
     	    		 
     	    		    
     	            }
     	    	  
     	    	  
     	    	  
     	    	  
     	    	
     	    	 
     	    	  
 			});
        	  
        	  
	            
	        }
	        
	        
	        final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
		    //ontologyFormat.setPrefix("NCBI_Taxonomy", "https://data.esante.gouv.fr/chu-rouen/NCBI_taxonomy/");
		   // ontologyFormat.setPrefix("NCBI_Taxonomy", "https://data.esante.gouv.fr/chu-rouen/NCBI_taxonomy#");
		    
		    
		    IRI iri = IRI.create("https://data.esante.gouv.fr/chu-rouen/NCBI_taxonomy/");
		    //man.applyChange(new SetOntologyID(originalO,  new OWLOntologyID(iri)));
		   
		   // addPropertiesOntology();
		    
		   man.saveOntology(originalO, ontologyFormat, fileoutputstream);
		   fileoutputstream.close();
		   
		    System.out.println("Done.");
		   logger.info("Fin crétion Fichier NCBI");

         
	}

	
	
	public static void addLateralAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass) {
	    //final OWLAnnotation annotation =
	      //  fact.getOWLAnnotation(prop, fact.getOWLLiteral(val,OWL2Datatype.));
	    //final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    //man.applyChange(new AddAxiom(originalO, axiom));
	    
	    
	   // final String about = "http://chu-rouen.fr/cismef/NCBI_Taxonomy#10";
      //  owlClass = fact.getOWLClass(IRI.create(about));;
	   // final OWLAnnotation annotation =  fact.getOWL
	    //final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
        //man.applyChange(new AddAxiom(originalO, axiom));
        
	    IRI iri_creator = IRI.create(val);
	   
	    OWLAnnotationProperty prop_creator =fact.getOWLAnnotationProperty(prop.getIRI());
	    
	    OWLAnnotation annotation = fact.getOWLAnnotation(prop_creator, iri_creator);
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(originalO, axiom));
	  }
	
	
	
	public static void addLateralAxiomss(OWLAnnotationProperty prop, String val, OWLClass owlClass) {
	    final OWLAnnotation annotation =fact.getOWLAnnotation(prop, fact.getOWLLiteral(val));
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(originalO, axiom));
	    
	    
	
	  }
	
	
	public static void addLateralAxiomXml(OWLAnnotationProperty prop, String iri, String val, OWLClass owlClass) {
	   // final OWLAnnotation annotation =fact.getOWLAnnotation(prop, fact.getOWLLiteral("<a href=\\\""+iri+"/\\"+"\">"+val+"</a>^^rdf:XMLLiteral"));
	    final OWLAnnotation annotation =fact.getOWLAnnotation(prop, fact.getOWLLiteral("<http://loinc.org/"+val+"/>"));
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(originalO, axiom));
	    
	    
	
	  }
	
	
	
	
	

}
