package fr.gouv.esante.pml.smt.metadata;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.tika.exception.TikaException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.xml.sax.SAXException;
import fr.gouv.esante.pml.smt.utils.ADMSVocabulary;
import fr.gouv.esante.pml.smt.utils.COREVocabulary;
import fr.gouv.esante.pml.smt.utils.DCATVocabulary;
import fr.gouv.esante.pml.smt.utils.DCTVocabulary;
import fr.gouv.esante.pml.smt.utils.FOAFVocabulary;
import fr.gouv.esante.pml.smt.utils.MetadataUtil;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.RDFVocabulary;
import fr.gouv.esante.pml.smt.utils.SchemaVocabulary;
import fr.gouv.esante.pml.smt.utils.VOAFVocabulary;

public class CIM11Metadata {

//  private static String owlFileName = PropertiesUtil.getProperties("owlFileName");
  private static final String owlMetaDataFileName = PropertiesUtil.getProperties("owlMetaDataFileName");
//  private static final Model m_model = ModelFactory.createDefaultModel();
//  final static String path = "./metadata.properties";

  private static OWLOntologyManager man = null;
  private static OWLOntology onto = null;
  private static OWLDataFactory fact = null;
  
  private static OWLNamedIndividual agent_ASIP = null;
  private static OWLNamedIndividual agent_OMS = null;
  private static OWLNamedIndividual cabinet_medical = null;
  private static OWLNamedIndividual laboratoireMCS = null;
  private static OWLNamedIndividual laboratoireBiologieMedicale = null;
  private static OWLNamedIndividual prescripteurs = null;
  private static OWLNamedIndividual distributionJsonLD = null;
  private static OWLNamedIndividual distributionOWL = null;
  private static OWLNamedIndividual licence = null;
  private static OWLNamedIndividual mediaTypeJsonLD = null;
  private static OWLNamedIndividual mediaTypeOwl = null;
  private static OWLNamedIndividual terminologie = null;
  
  private static OWLAnnotationProperty rdfType = null;
  private static OWLAnnotationProperty rdfValue = null;
  private static OWLAnnotationProperty dctLicense = null;
  private static OWLAnnotationProperty admsStatus = null;
  private static OWLAnnotationProperty dcatAccessURL = null;
  private static OWLAnnotationProperty dcatMediaType = null;
  private static OWLAnnotationProperty dctAlternative = null;
  private static OWLAnnotationProperty dctDescription = null;
  private static OWLAnnotationProperty foafName = null;
  private static OWLAnnotationProperty coreModifiedBy  = null;
  private static OWLAnnotationProperty dctAudience  = null;
  private static OWLAnnotationProperty dctIdentifier  = null;
  private static OWLAnnotationProperty dctIssued  = null;
  private static OWLAnnotationProperty dctLanguage  = null;
  private static OWLAnnotationProperty dctPublisher  = null;
//  private static OWLAnnotationProperty dctRelation  = null;
  private static OWLAnnotationProperty dctTitle  = null;
  private static OWLAnnotationProperty dctType  = null;
  private static OWLAnnotationProperty voafClassNumber  = null;
  private static OWLAnnotationProperty dcatDistribution  = null;
//  private static OWLAnnotationProperty dcatLandingPage  = null;
  private static OWLAnnotationProperty dcatTheme  = null;
//  private static OWLAnnotationProperty nsComplexity  = null;
  private static OWLAnnotationProperty schemaAuthor  = null;
//  private static OWLAnnotationProperty schemaPriceSpecification  = null;
  private static OWLAnnotationProperty schemaPrice  = null;
//  private static OWLAnnotationProperty skosHistoryNote  = null;

  public static void main(final String[] args) throws IOException, SAXException, TikaException,
      ParserConfigurationException, OWLOntologyCreationException, OWLOntologyStorageException {

//    final Map<String, Object> properties = new HashMap<String, Object>();
//    properties.put("WARN_BAD_NAME", "EM_IGNORE");
    // Put a properties object into the Context.
//    final Context cxt = new Context();
//    cxt.set(SysRIOT.sysRdfReaderProperties, properties);
//    final InputStream in = new FileInputStream(owlFileName);
    final OutputStream fileoutputstream = new FileOutputStream(owlMetaDataFileName);
//    final OWLOntologyLoaderConfiguration config = man.getOntologyLoaderConfiguration();
//    man.setOntologyLoaderConfiguration(
//        config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT));
    man = OWLManager.createOWLOntologyManager();
    onto = man.createOntology(IRI.create(MetadataUtil.getProperties("terminologie_profil_IRI", "ICD11")));
    fact = onto.getOWLOntologyManager().getOWLDataFactory();

    rdfType = fact.getOWLAnnotationProperty(OWLRDFVocabulary.RDF_TYPE.getIRI());
    rdfValue = fact.getOWLAnnotationProperty(RDFVocabulary.value.getIRI());
    dctLicense = fact.getOWLAnnotationProperty(DCTVocabulary.license.getIRI());
    admsStatus = fact.getOWLAnnotationProperty(ADMSVocabulary.status.getIRI());
    dcatAccessURL = fact.getOWLAnnotationProperty(DCATVocabulary.accessURL.getIRI());
    dcatMediaType = fact.getOWLAnnotationProperty(DCATVocabulary.mediaType.getIRI());
    dctAlternative = fact.getOWLAnnotationProperty(DCTVocabulary.alternative.getIRI());
    dctDescription = fact.getOWLAnnotationProperty(DCTVocabulary.description.getIRI());
    foafName = fact.getOWLAnnotationProperty(FOAFVocabulary.name.getIRI());
    coreModifiedBy = fact.getOWLAnnotationProperty(COREVocabulary.modifiedBy.getIRI());
    dctAudience = fact.getOWLAnnotationProperty(DCTVocabulary.audience.getIRI());
    dctIdentifier = fact.getOWLAnnotationProperty(DCTVocabulary.identifier.getIRI());
    dctIssued = fact.getOWLAnnotationProperty(DCTVocabulary.issued.getIRI());
    dctLanguage = fact.getOWLAnnotationProperty(DCTVocabulary.language.getIRI());
    dctPublisher = fact.getOWLAnnotationProperty(DCTVocabulary.publisher.getIRI());
//    dctRelation = fact.getOWLAnnotationProperty(DCTVocabulary.relation.getIRI());
    dctTitle = fact.getOWLAnnotationProperty(DCTVocabulary.title.getIRI());
    dctType = fact.getOWLAnnotationProperty(DCTVocabulary.type.getIRI());
    voafClassNumber = fact.getOWLAnnotationProperty(VOAFVocabulary.classNumber.getIRI());
    dcatDistribution = fact.getOWLAnnotationProperty(DCATVocabulary.distribution.getIRI());
//    dcatLandingPage = fact.getOWLAnnotationProperty(DCATVocabulary.landingPage.getIRI());
    dcatTheme = fact.getOWLAnnotationProperty(DCATVocabulary.theme.getIRI());
//    nsComplexity = fact.getOWLAnnotationProperty(NSVocabulary.complexity.getIRI());
    schemaAuthor = fact.getOWLAnnotationProperty(SchemaVocabulary.author.getIRI());
//    schemaPriceSpecification = fact.getOWLAnnotationProperty(SchemaVocabulary.priceSpecification.getIRI());
    schemaPrice = fact.getOWLAnnotationProperty(SchemaVocabulary.price.getIRI());
//    skosHistoryNote = fact.getOWLAnnotationProperty(SKOSVocabulary.HISTORYNOTE.getIRI());
    
    
    
    addLicence();
    addMediatypeOWL();
    addMediatypeJsonLD();
    addDistributionOWL();
    addDistributionJsonLD();
    addAgentASIP();
    addAgentOMS();
    addCabinet_medical();
    addLaboratoireMCS();
    addLaboratoireBiologieMedicale();
    addPrescripteurs();
    addTerminologie();
    
    
    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
    ontologyFormat.setPrefix("dct", "http://purl.org/dc/terms/");
    man.saveOntology(onto, ontologyFormat, fileoutputstream);
    fileoutputstream.close();
    System.out.println("Done.");
  }

public static void addAgentASIP() {
    
  agent_ASIP = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("agent_ASIP")));
  
  OWLDeclarationAxiom declarationAxiom = fact
      .getOWLDeclarationAxiom(agent_ASIP);
man.addAxiom(onto, declarationAxiom);
  
    final OWLAnnotation labelAnno =
        fact.getOWLAnnotation(rdfType, IRI.create("http://xmlns.com/foaf/0.1/Agent"));
    final OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(agent_ASIP.getIRI(), labelAnno);
    man.applyChange(new AddAxiom(onto, ax));
    
    final OWLAnnotation labelAnno1 =
        fact.getOWLAnnotation(dctAlternative, fact.getOWLLiteral(MetadataUtil.getProperties("agent_ASIP_alternative_fr"), "fr"));
    final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(agent_ASIP.getIRI(), labelAnno1);
    man.applyChange(new AddAxiom(onto, ax1));
    
    final OWLAnnotation labelAnno2 =
        fact.getOWLAnnotation(dctDescription, fact.getOWLLiteral(MetadataUtil.getProperties("agent_ASIP_description_fr"), "fr"));
    final OWLAxiom ax2 = fact.getOWLAnnotationAssertionAxiom(agent_ASIP.getIRI(), labelAnno2);
    man.applyChange(new AddAxiom(onto, ax2));
    
    final OWLAnnotation labelAnno3 =
        fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(MetadataUtil.getProperties("agent_ASIP_label_fr"), "fr"));
    final OWLAxiom ax3 = fact.getOWLAnnotationAssertionAxiom(agent_ASIP.getIRI(), labelAnno3);
    man.applyChange(new AddAxiom(onto, ax3));
    
    final OWLAnnotation labelAnno4 =
        fact.getOWLAnnotation(foafName, fact.getOWLLiteral(MetadataUtil.getProperties("agent_ASIP_name")));
    final OWLAxiom ax4 = fact.getOWLAnnotationAssertionAxiom(agent_ASIP.getIRI(), labelAnno4);
    man.applyChange(new AddAxiom(onto, ax4));
  }

public static void addAgentOMS() {
  
  agent_OMS = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("agent_OMS")));
  
  OWLDeclarationAxiom declarationAxiom = fact
      .getOWLDeclarationAxiom(agent_OMS);
man.addAxiom(onto, declarationAxiom);
    final OWLAnnotation labelAnno =
        fact.getOWLAnnotation(rdfType, IRI.create("http://xmlns.com/foaf/0.1/Agent"));
    final OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(agent_OMS.getIRI(), labelAnno);
    man.applyChange(new AddAxiom(onto, ax));
    
    final OWLAnnotation labelAnno1 =
        fact.getOWLAnnotation(dctAlternative, fact.getOWLLiteral(MetadataUtil.getProperties("agent_OMS_alternative_fr"), "fr"));
    final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(agent_OMS.getIRI(), labelAnno1);
    man.applyChange(new AddAxiom(onto, ax1));
    
    final OWLAnnotation labelAnno2 =
        fact.getOWLAnnotation(dctDescription, fact.getOWLLiteral(MetadataUtil.getProperties("agent_OMS_description_fr"), "fr"));
    final OWLAxiom ax2 = fact.getOWLAnnotationAssertionAxiom(agent_OMS.getIRI(), labelAnno2);
    man.applyChange(new AddAxiom(onto, ax2));
    
    final OWLAnnotation labelAnno3 =
        fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(MetadataUtil.getProperties("agent_OMS_label_fr"), "fr"));
    final OWLAxiom ax3 = fact.getOWLAnnotationAssertionAxiom(agent_OMS.getIRI(), labelAnno3);
    man.applyChange(new AddAxiom(onto, ax3));
    
    final OWLAnnotation labelAnno4 =
        fact.getOWLAnnotation(foafName, fact.getOWLLiteral(MetadataUtil.getProperties("agent_OMS_name")));
    final OWLAxiom ax4 = fact.getOWLAnnotationAssertionAxiom(agent_OMS.getIRI(), labelAnno4);
    man.applyChange(new AddAxiom(onto, ax4));
  }

public static void addLaboratoireMCS() {
  
  laboratoireMCS = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("audience_laboratoireMCS")));
  OWLDeclarationAxiom declarationAxiom = fact
      .getOWLDeclarationAxiom(laboratoireMCS);
man.addAxiom(onto, declarationAxiom);
  
  final OWLAnnotation labelAnno =
      fact.getOWLAnnotation(rdfType, IRI.create("http://purl.org/dc/terms/AgentClass"));
  final OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(laboratoireMCS.getIRI(), labelAnno);
  man.applyChange(new AddAxiom(onto, ax));
  
  final OWLAnnotation labelAnno1 =
      fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(MetadataUtil.getProperties("audience_laboratoireMCS_label_fr"), "fr"));
  final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(laboratoireMCS.getIRI(), labelAnno1);
  man.applyChange(new AddAxiom(onto, ax1));
  
}

public static void addLaboratoireBiologieMedicale() {
  
  laboratoireBiologieMedicale = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("audience_Laboratoire_Biologie_Medicale")));
  OWLDeclarationAxiom declarationAxiom = fact
      .getOWLDeclarationAxiom(laboratoireBiologieMedicale);
man.addAxiom(onto, declarationAxiom);
  
  final OWLAnnotation labelAnno =
      fact.getOWLAnnotation(rdfType, IRI.create("http://purl.org/dc/terms/AgentClass"));
  final OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(laboratoireBiologieMedicale.getIRI(), labelAnno);
  man.applyChange(new AddAxiom(onto, ax));
  
  final OWLAnnotation labelAnno1 =
      fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(MetadataUtil.getProperties("audience_Laboratoire_Biologie_Medicale_label_fr"), "fr"));
  final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(laboratoireBiologieMedicale.getIRI(), labelAnno1);
  man.applyChange(new AddAxiom(onto, ax1));
  
}

public static void addPrescripteurs() {
  
  prescripteurs = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("audience_Prescripteurs")));
  OWLDeclarationAxiom declarationAxiom = fact
      .getOWLDeclarationAxiom(prescripteurs);
man.addAxiom(onto, declarationAxiom);
  
  final OWLAnnotation labelAnno =
      fact.getOWLAnnotation(rdfType, IRI.create("http://purl.org/dc/terms/AgentClass"));
  final OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(prescripteurs.getIRI(), labelAnno);
  man.applyChange(new AddAxiom(onto, ax));
  
  final OWLAnnotation labelAnno1 =
      fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(MetadataUtil.getProperties("audience_Prescripteurs_label_fr"), "fr"));
  final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(prescripteurs.getIRI(), labelAnno1);
  man.applyChange(new AddAxiom(onto, ax1));
  
}

public static void addCabinet_medical() {
  
  cabinet_medical = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("audience_cabinet_medical")));
  OWLDeclarationAxiom declarationAxiom = fact
      .getOWLDeclarationAxiom(cabinet_medical);
man.addAxiom(onto, declarationAxiom);
  
  final OWLAnnotation labelAnno =
      fact.getOWLAnnotation(rdfType, IRI.create("http://purl.org/dc/terms/AgentClass"));
  final OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(cabinet_medical.getIRI(), labelAnno);
  man.applyChange(new AddAxiom(onto, ax));
  
  final OWLAnnotation labelAnno1 =
      fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(MetadataUtil.getProperties("audience_cabinet_medical_label_fr"), "fr"));
  final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(cabinet_medical.getIRI(), labelAnno1);
  man.applyChange(new AddAxiom(onto, ax1));
  
}
  
  /**
   * add license
   */
  public static void addLicence() {
    
    licence = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("Licence")));
    OWLDeclarationAxiom declarationAxiom = fact
        .getOWLDeclarationAxiom(licence);
  man.addAxiom(onto, declarationAxiom);
    
    final OWLAnnotation labelAnno =
        fact.getOWLAnnotation(rdfType, IRI.create("http://purl.org/dc/terms/LicenseDocument"));
    final OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(licence.getIRI(), labelAnno);
    man.applyChange(new AddAxiom(onto, ax));
    
    final OWLAnnotation labelAnno1 =
        fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(MetadataUtil.getProperties("Licence_label_fr"), "fr"));
    final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(licence.getIRI(), labelAnno1);
    man.applyChange(new AddAxiom(onto, ax1));
    
    final OWLAnnotation labelAnno2 =
        fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(MetadataUtil.getProperties("Licence_label_en"), "en"));
    final OWLAxiom ax2 = fact.getOWLAnnotationAssertionAxiom(licence.getIRI(), labelAnno2);
    man.applyChange(new AddAxiom(onto, ax2));
  }
  
  public static void addMediatypeOWL() {
    mediaTypeOwl = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("mediaTypeOwl")));
    
    OWLDeclarationAxiom declarationAxiom = fact
        .getOWLDeclarationAxiom(mediaTypeOwl);
  man.addAxiom(onto, declarationAxiom);
    final OWLAnnotation labelAnno =
        fact.getOWLAnnotation(rdfType, IRI.create("http://purl.org/dc/terms/MediaTypeOrExtent"));
    final OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(mediaTypeOwl.getIRI(), labelAnno);
    man.applyChange(new AddAxiom(onto, ax));
    
    final OWLAnnotation labelAnno1 =
        fact.getOWLAnnotation(rdfValue, fact.getOWLLiteral(MetadataUtil.getProperties("mediaTypeOwl_value")));
    final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(mediaTypeOwl.getIRI(), labelAnno1);
    man.applyChange(new AddAxiom(onto, ax1));
    
    final OWLAnnotation labelAnno2 =
        fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(MetadataUtil.getProperties("mediaTypeOwl_label")));
    final OWLAxiom ax2 = fact.getOWLAnnotationAssertionAxiom(mediaTypeOwl.getIRI(), labelAnno2);
    man.applyChange(new AddAxiom(onto, ax2));
  }
  
  public static void addMediatypeJsonLD() {
    mediaTypeJsonLD = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("mediaTypeJsonLD")));
    OWLDeclarationAxiom declarationAxiom = fact
        .getOWLDeclarationAxiom(mediaTypeJsonLD);
  man.addAxiom(onto, declarationAxiom);
    
    final OWLAnnotation labelAnno =
        fact.getOWLAnnotation(rdfType, IRI.create("http://purl.org/dc/terms/MediaTypeOrExtent"));
    final OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(mediaTypeJsonLD.getIRI(), labelAnno);
    man.applyChange(new AddAxiom(onto, ax));
    
    final OWLAnnotation labelAnno1 =
        fact.getOWLAnnotation(rdfValue, fact.getOWLLiteral(MetadataUtil.getProperties("mediaTypeJsonLD_value")));
    final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(mediaTypeJsonLD.getIRI(), labelAnno1);
    man.applyChange(new AddAxiom(onto, ax1));
    
    final OWLAnnotation labelAnno2 =
        fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(MetadataUtil.getProperties("mediaTypeJsonLD_label")));
    final OWLAxiom ax2 = fact.getOWLAnnotationAssertionAxiom(mediaTypeJsonLD.getIRI(), labelAnno2);
    man.applyChange(new AddAxiom(onto, ax2));
  }
  
  public static void addDistributionOWL() {
    distributionOWL = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("distributionOWL")));
    OWLDeclarationAxiom declarationAxiom = fact
        .getOWLDeclarationAxiom(distributionOWL);
  man.addAxiom(onto, declarationAxiom);
    
    final OWLAnnotation labelAnno =
        fact.getOWLAnnotation(rdfType, IRI.create("http://www.w3.org/ns/dcat#Distribution"));
    final OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(distributionOWL.getIRI(), labelAnno);
    man.applyChange(new AddAxiom(onto, ax));
    
    final OWLAnnotation labelAnno1 =
        fact.getOWLAnnotation(dctLicense, licence.getIRI());
    final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(distributionOWL.getIRI(), labelAnno1);
    man.applyChange(new AddAxiom(onto, ax1));
    
    final OWLAnnotation labelAnno2 =
        fact.getOWLAnnotation(admsStatus, IRI.create(MetadataUtil.getProperties("distributionOWL_status")));
    final OWLAxiom ax2 = fact.getOWLAnnotationAssertionAxiom(distributionOWL.getIRI(), labelAnno2);
    man.applyChange(new AddAxiom(onto, ax2));
    
    final OWLAnnotation labelAnno3 =
        fact.getOWLAnnotation(dcatAccessURL, IRI.create(MetadataUtil.getProperties("distributionOWL_accessURL")));
    final OWLAxiom ax3 = fact.getOWLAnnotationAssertionAxiom(distributionOWL.getIRI(), labelAnno3);
    man.applyChange(new AddAxiom(onto, ax3));
    
    final OWLAnnotation labelAnno4 =
        fact.getOWLAnnotation(dcatMediaType, mediaTypeOwl.getIRI());
    final OWLAxiom ax4 = fact.getOWLAnnotationAssertionAxiom(distributionOWL.getIRI(), labelAnno4);
    man.applyChange(new AddAxiom(onto, ax4));
  }
  
  public static void addDistributionJsonLD() {
    distributionJsonLD = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("distributionJsonLD")));
    OWLDeclarationAxiom declarationAxiom = fact
        .getOWLDeclarationAxiom(distributionJsonLD);
  man.addAxiom(onto, declarationAxiom);
    
    final OWLAnnotation labelAnno =
        fact.getOWLAnnotation(rdfType, IRI.create("http://www.w3.org/ns/dcat#Distribution"));
    final OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(distributionJsonLD.getIRI(), labelAnno);
    man.applyChange(new AddAxiom(onto, ax));
    
    final OWLAnnotation labelAnno1 =
        fact.getOWLAnnotation(dctLicense, licence.getIRI());
    final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(distributionJsonLD.getIRI(), labelAnno1);
    man.applyChange(new AddAxiom(onto, ax1));
    
    final OWLAnnotation labelAnno2 =
        fact.getOWLAnnotation(admsStatus, IRI.create(MetadataUtil.getProperties("distributionJsonLD_status")));
    final OWLAxiom ax2 = fact.getOWLAnnotationAssertionAxiom(distributionJsonLD.getIRI(), labelAnno2);
    man.applyChange(new AddAxiom(onto, ax2));
    
    final OWLAnnotation labelAnno3 =
        fact.getOWLAnnotation(dcatAccessURL, IRI.create(MetadataUtil.getProperties("distributionJsonLD_accessURL")));
    final OWLAxiom ax3 = fact.getOWLAnnotationAssertionAxiom(distributionJsonLD.getIRI(), labelAnno3);
    man.applyChange(new AddAxiom(onto, ax3));
    
    final OWLAnnotation labelAnno4 =
        fact.getOWLAnnotation(dcatMediaType, mediaTypeJsonLD.getIRI());
    final OWLAxiom ax4 = fact.getOWLAnnotationAssertionAxiom(distributionJsonLD.getIRI(), labelAnno4);
    man.applyChange(new AddAxiom(onto, ax4));
  }
  
  
  public static void addTerminologie() {
    terminologie = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("terminologie_IRI", "ICD11")));
    OWLDeclarationAxiom declarationAxiom = fact
        .getOWLDeclarationAxiom(terminologie);
  man.addAxiom(onto, declarationAxiom);
    
    final OWLAnnotation labelAnno =
        fact.getOWLAnnotation(rdfType, IRI.create("https://data.esante.gouv.fr/profile/ns#Terminology"));
    final OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), labelAnno);
    man.applyChange(new AddAxiom(onto, ax));
    
    addIRIAxioms(coreModifiedBy, agent_ASIP);
    
    addLateralAxioms(dctAlternative, "terminologie_alternative_fr", "fr");
    
    addLateralAxioms(dctAlternative, "terminologie_alternative_en", "en");
    
    addIRIAxioms(dctAudience, laboratoireMCS);
    addIRIAxioms(dctAudience, laboratoireBiologieMedicale);
    addIRIAxioms(dctAudience, prescripteurs);
    addIRIAxioms(dctAudience, cabinet_medical);
    
    addLateralAxioms(dctDescription, "terminologie_description_fr", "fr");
    
    addLateralAxioms(dctIdentifier, "terminologie_identifier");
    
    addLateralAxioms(dctIssued, "terminologie_issued", OWL2Datatype.XSD_DATE_TIME);
    
    addIRIAxioms(dctLanguage, "terminologie_language_en");
    addIRIAxioms(dctLanguage, "terminologie_language_fr");
    
    addIRIAxioms(dctPublisher, agent_ASIP);
    
    addLateralAxioms(dctTitle, "terminologie_title_fr");
    
    addIRIAxioms(dctType, "terminologie_dct_type");
    
    addLateralAxioms(voafClassNumber, "terminologie_numberOfClasses", OWL2Datatype.XSD_INTEGER);
    
    addLateralAxioms(fact.getRDFSComment(), "terminologie_comment_fr", "fr");
    
    addLateralAxioms(fact.getRDFSLabel(), "terminologie_label");
    
    addLateralAxioms(fact.getOWLVersionInfo(), "terminologie_versionInfo");
    
    addIRIAxioms(dcatDistribution, distributionOWL);
    addIRIAxioms(dcatDistribution, distributionJsonLD);
    
    addIRIAxioms(dcatTheme, "terminologie_theme");
    
    addIRIAxioms(schemaAuthor, agent_OMS);
    
    final OWLAnnotation labelAnno24 =
        fact.getOWLAnnotation(schemaPrice, fact.getOWLLiteral("0"));
    final OWLAxiom ax24 = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), labelAnno24);
    man.applyChange(new AddAxiom(onto, ax24));
   
  }
  
  public static void addIRIAxioms(OWLAnnotationProperty prop, String iri) {
    final OWLAnnotation annotation =
        fact.getOWLAnnotation(prop, IRI.create(MetadataUtil.getProperties(iri, "ICD11")));
    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), annotation);
    man.applyChange(new AddAxiom(onto, axiom));
  }
  
  public static void addIRIAxioms(OWLAnnotationProperty prop, OWLNamedIndividual  indiv) {
    final OWLAnnotation annotation =
        fact.getOWLAnnotation(prop, indiv.getIRI());
    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), annotation);
    man.applyChange(new AddAxiom(onto, axiom));
  }
  
  public static void addLateralAxioms(OWLAnnotationProperty prop, String iri) {
    final OWLAnnotation annotation =
        fact.getOWLAnnotation(prop, fact.getOWLLiteral(MetadataUtil.getProperties(iri, "ICD11")));
    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), annotation);
    man.applyChange(new AddAxiom(onto, axiom));
  }
  
  public static void addLateralAxioms(OWLAnnotationProperty prop, String iri,  OWL2Datatype datatype) {
    final OWLAnnotation annotation =
        fact.getOWLAnnotation(prop, fact.getOWLLiteral(MetadataUtil.getProperties(iri, "ICD11"), datatype));
    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), annotation);
    man.applyChange(new AddAxiom(onto, axiom));
  }
  
  public static void addLateralAxioms(OWLAnnotationProperty prop, String iri, String lang) {
    final OWLAnnotation annotation =
        fact.getOWLAnnotation(prop, fact.getOWLLiteral(MetadataUtil.getProperties(iri, "ICD11"), lang));
    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), annotation);
    man.applyChange(new AddAxiom(onto, axiom));
  }

//  public static void setHeaderMetadata()
//      throws OWLOntologyStorageException, OWLOntologyCreationException, IOException {
//
//    System.out.println("Metadata Loaded...");
//
//    final Map<String, String> metadata = new HashMap<String, String>();
//    try (InputStream input = SetMetadata.class.getClassLoader().getResourceAsStream(path)) {
//
//      Properties prop = new Properties();
//
//      if (input == null) {
//        System.out.println("Sorry, unable to find " + path);
//        System.exit(0);
//      }
//
//      prop.load(input);
//
//      // Java 8 , print key and values
//      prop.forEach((key, value) -> metadata.put((String) key, (String) value));
//
//      if (metadata.size() < 1) {
//        System.out.println("Sorry, no metadata to add in " + path);
//        System.exit(0);
//      }
//
//    } catch (IOException ex) {
//      ex.printStackTrace();
//    }
//
//
//
//    final Map<String, Object> properties = new HashMap<String, Object>();
//    properties.put("WARN_BAD_NAME", "EM_IGNORE");
//    // Put a properties object into the Context.
//    final Context cxt = new Context();
//    cxt.set(SysRIOT.sysRdfReaderProperties, properties);
//    final InputStream in = new FileInputStream(owlFileName);
//    final OntModel m = ModelFactory.createOntologyModel();
//    // Build and run a parser
//    RDFParser.create().lang(Lang.RDFXML).source(in).context(cxt).parse(m);
//
//    Ontology ont = m.getOntology("http://www.w3.org/2002/07/owl");
//    metadata.forEach((key, value) -> ont
//        .addProperty(m_model.createProperty("http://purl.org/dc/terms/" + key), value));
//    // ont.addProperty( DCTerms.creator, "ASIP Sant�" );
//    final ByteArrayOutputStream out = new ByteArrayOutputStream();
//    m.write(out, "RDF/JSON");
//    in.close();
//    final InputStream input = new ByteArrayInputStream(out.toByteArray());
//    final OutputStream fileoutputstream = new FileOutputStream(owlMetaDataFileName);
//
//    final OWLOntologyLoaderConfiguration config = man.getOntologyLoaderConfiguration();
//    man.setOntologyLoaderConfiguration(
//        config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT));
//    onto = man.loadOntologyFromOntologyDocument(input);
//
//
//    System.out.println("Metadata Saving... ");
//
//    man.saveOntology(onto, new RDFXMLDocumentFormat(), fileoutputstream);
//    fileoutputstream.close();
//    System.out.println("Done.");
//
//  }
}
