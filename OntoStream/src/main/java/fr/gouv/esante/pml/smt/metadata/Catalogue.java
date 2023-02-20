package fr.gouv.esante.pml.smt.metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import fr.gouv.esante.pml.smt.utils.MetadataUtil;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class Catalogue {
  
  private static String cim11modMetaDataFileName = PropertiesUtil.getProperties("cim11modMetaDataFileName");
  private static String owlMetaDataFileName = PropertiesUtil.getProperties("owlMetaDataFileName");
  private static String cisp2modMetaDataFileName = PropertiesUtil.getProperties("cisp2modMetaDataFileName");
  private static String CISP2DCatMetaDataFileName = PropertiesUtil.getProperties("CISP2DCatMetaDataFileName");
  private static String cladimedmodMetaDataFileName = PropertiesUtil.getProperties("cladimedmodMetaDataFileName");
  private static String cladimedDCatMetaDataFileName = PropertiesUtil.getProperties("cladimedDCatMetaDataFileName");

  public static void main(final String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {
    modCatalogue();
    dcatCatalogue();
  }
  
  public static void modCatalogue() throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {
	  OWLOntologyManager m = OWLManager.createOWLOntologyManager();
	    File cim11 = new File(cim11modMetaDataFileName);
	    File cisp2 = new File(cisp2modMetaDataFileName);
	    File cladimed = new File(cladimedmodMetaDataFileName);

	    OWLOntology onto = m.createOntology(IRI.create("https://esante.gouv.fr/terminologies-mod1.4-catalogue"));
	    OWLDataFactory factory = m.getOWLDataFactory();
	    OWLImportsDeclaration importCim11 = factory.getOWLImportsDeclaration(IRI.create(cim11.toURI()));
	    m.applyChange(new AddImport(onto, importCim11));
	    OWLImportsDeclaration importCisp2 = factory.getOWLImportsDeclaration(IRI.create(cisp2.toURI()));
	    m.applyChange(new AddImport(onto, importCisp2));
	    OWLImportsDeclaration importCladimed = factory.getOWLImportsDeclaration(IRI.create(cladimed.toURI()));
	    m.applyChange(new AddImport(onto, importCladimed));
	    m.saveOntology(onto, new RDFXMLDocumentFormat(), new FileOutputStream("C:\\Users\\agochath\\Documents\\cim11\\terminologies-mod1.4-catalogue.owl") );

  }
  
  public static void dcatCatalogue() throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {
	  OWLOntologyManager m = OWLManager.createOWLOntologyManager();
	    File cim11 = new File(owlMetaDataFileName);
	    File cisp2 = new File(CISP2DCatMetaDataFileName);
	    File cladimed = new File(cladimedDCatMetaDataFileName);

	    OWLOntology onto = m.createOntology(IRI.create("https://esante.gouv.fr/terminologies-dcat-catalogue"));
	    OWLDataFactory factory = m.getOWLDataFactory();
	    OWLImportsDeclaration importCim11 = factory.getOWLImportsDeclaration(IRI.create(cim11.toURI()));
	    m.applyChange(new AddImport(onto, importCim11));
	    OWLImportsDeclaration importCisp2 = factory.getOWLImportsDeclaration(IRI.create(cisp2.toURI()));
	    m.applyChange(new AddImport(onto, importCisp2));
	    OWLImportsDeclaration importCladimed = factory.getOWLImportsDeclaration(IRI.create(cladimed.toURI()));
	    m.applyChange(new AddImport(onto, importCladimed));
	    m.saveOntology(onto, new RDFXMLDocumentFormat(), new FileOutputStream("C:\\Users\\agochath\\Documents\\cim11\\terminologies-dcat-catalogue.owl") );

  }
}
