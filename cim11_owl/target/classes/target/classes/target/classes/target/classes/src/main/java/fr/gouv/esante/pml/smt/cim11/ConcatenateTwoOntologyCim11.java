package fr.gouv.esante.pml.smt.cim11;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class ConcatenateTwoOntologyCim11 {
 
  static InputStream input1 = null;
  static InputStream input2 = null;
  private static OWLOntologyManager manager = null;
  private static OWLOntology onto1 = null;
  private static OWLOntology onto2 = null;
  static String OWL_FINAL_FILE_FR_EN = "CGTS_SEM_ICD11-MMS-R202202-EN-FR-V2.owl";
  static String OWL_FINAL_FILE_FR = "CGTS_SEM_ICD11-MMS-R202301-FR.owl";
  static String OWL_FINAL_FILE_EN = "CGTS_SEM_ICD11-MMS-R202301-EN.owl";

	public static void main(final String[] args) throws Exception {

	  Options options = new Options();
	  options.addOption("owl_fr", "owl_fr", true, "owl file en Francais");
	  options.addOption("owl_en", "owl_en", true, "owl file en Anglais");
	  options.addOption("owl_complet", "owl_complet", true, "owl file en Francais Anglais");


	  CommandLineParser parser = new DefaultParser();
	  CommandLine line = parser.parse(options, args);
	  
	  String  owlFileFr = line.getOptionValue("owl_fr");
	  String  owlFileEn = line.getOptionValue("owl_en");
	  String owlFileFREN = OWL_FINAL_FILE_FR_EN;

	  if(owlFileFr==null) {
		  System.out.println("Le lien vers le fichier des concepts en français est manquant, le fichier "+OWL_FINAL_FILE_FR+" sera utilisé ");
		  owlFileFr = OWL_FINAL_FILE_FR;
	  }
	  if(owlFileEn==null) {
		  System.out.println("Le lien vers le fichier des concepts en anglais est manquant, le fichier "+OWL_FINAL_FILE_EN+" sera utilisé ");
		  owlFileEn = OWL_FINAL_FILE_EN;
	  }

	 // if(owlFileFREN ==  null){
		//  System.out.println("Le lien vers le fichier output est manquant, le fichier "+OWL_FINAL_FILE_FR_EN+" sera utilisé ");
	  //}

	  input1 = new FileInputStream(owlFileFr);
	  input2 = new FileInputStream(owlFileEn);

		System.out.println(owlFileFr);
		System.out.println(owlFileEn);
	    
	    manager = OWLManager.createOWLOntologyManager();
	    onto1 = manager.loadOntologyFromOntologyDocument(input1);
		//System.out.println(onto1.getOntologyID());
	    onto2 = manager.loadOntologyFromOntologyDocument(input2);
		//System.out.println(onto2.getOntologyID());

		onto1.axioms().forEach(ax -> {
			manager.applyChange(new AddAxiom(onto2, ax));
	    	});
	    
	    final OutputStream fileoutputstream = new FileOutputStream(owlFileFREN);
	    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
	    ontologyFormat.setPrefix("icd", "http://id.who.int/icd/schema/");
	      
	    manager.saveOntology(onto2, ontologyFormat, fileoutputstream);
	    
	    fileoutputstream.close();

	  }
  
}
