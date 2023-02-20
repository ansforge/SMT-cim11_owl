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
import org.semanticweb.owlapi.vocab.PROVVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;
import org.xml.sax.SAXException;
import fr.gouv.esante.pml.smt.utils.CCVocabulary;
import fr.gouv.esante.pml.smt.utils.DCATVocabulary;
import fr.gouv.esante.pml.smt.utils.DCTVocabulary;
import fr.gouv.esante.pml.smt.utils.DOORVocabulary;
import fr.gouv.esante.pml.smt.utils.FOAFVocabulary;
import fr.gouv.esante.pml.smt.utils.MODVocabulary;
import fr.gouv.esante.pml.smt.utils.MetadataUtil;
import fr.gouv.esante.pml.smt.utils.OMVVocabulary;
import fr.gouv.esante.pml.smt.utils.PAVVocabulary;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.RDFVocabulary;
import fr.gouv.esante.pml.smt.utils.SchemaVocabulary;
import fr.gouv.esante.pml.smt.utils.VANNVocabulary;
import fr.gouv.esante.pml.smt.utils.VOAFVocabulary;
import fr.gouv.esante.pml.smt.utils.VOIDVocabulary;

public class CISP2ModMetadata {

  private static String cisp2modMetaDataFileName = PropertiesUtil.getProperties("cisp2modMetaDataFileName");
  
  private static OWLOntologyManager man = null;
  private static OWLOntology onto = null;
  private static OWLDataFactory fact = null;
  
  private static OWLNamedIndividual cabinet_medical = null;
  private static OWLNamedIndividual laboratoireMCS = null;
  private static OWLNamedIndividual laboratoireBiologieMedicale = null;
  private static OWLNamedIndividual prescripteurs = null;
//  private static OWLNamedIndividual distributionJsonLD = null;
  private static OWLNamedIndividual distributionOWL = null;
//  private static OWLNamedIndividual mediaTypeJsonLD = null;
  private static OWLNamedIndividual mediaTypeRDF = null;
  private static OWLNamedIndividual terminologie = null;
  
  private static OWLAnnotationProperty rdfType = null;
  private static OWLAnnotationProperty rdfValue = null;
  private static OWLAnnotationProperty priorVersion = null;
  private static OWLAnnotationProperty dctLicense = null;
  private static OWLAnnotationProperty dcatAccessURL = null;
  private static OWLAnnotationProperty dcatMediaType = null;
  private static OWLAnnotationProperty dctAlternative = null;
  private static OWLAnnotationProperty dctDescription = null;
  private static OWLAnnotationProperty dctAudience  = null;
  private static OWLAnnotationProperty dctIdentifier  = null;
  private static OWLAnnotationProperty dctLanguage  = null;
  private static OWLAnnotationProperty dctPublisher  = null;
  private static OWLAnnotationProperty dctRelation  = null;
  private static OWLAnnotationProperty dctTitle  = null;
  private static OWLAnnotationProperty skosHiddenLabel  = null;
  private static OWLAnnotationProperty ccUseGuidelines  = null;
  private static OWLAnnotationProperty dctAccessRights  = null;
  private static OWLAnnotationProperty dctContributor  = null;
  private static OWLAnnotationProperty dctCoverage  = null;
  private static OWLAnnotationProperty dctCreated  = null;
  private static OWLAnnotationProperty dctCreator  = null;
  private static OWLAnnotationProperty dctRightsHolder  = null;
  private static OWLAnnotationProperty dctSubject  = null;
  private static OWLAnnotationProperty foafHomepage  = null;
  private static OWLAnnotationProperty doorComesFromTheSameDomain  = null;
  private static OWLAnnotationProperty modBrowsingUI  = null;
  private static OWLAnnotationProperty modByteSize  = null;
  private static OWLAnnotationProperty modGroup  = null;
  private static OWLAnnotationProperty modVocabularyUsed  = null;
  private static OWLAnnotationProperty modHierarchyProperty  = null;
  private static OWLAnnotationProperty modPrefLabelProperty  = null;
  private static OWLAnnotationProperty omvAcronym  = null;
  private static OWLAnnotationProperty omvHasFormalityLevel  = null;
  private static OWLAnnotationProperty omvHasOntologyLanguage  = null;
  private static OWLAnnotationProperty omvHasOntologySyntax  = null;
  private static OWLAnnotationProperty omvKeyClasses  = null;
  private static OWLAnnotationProperty omvNumberOfAxioms  = null;
  private static OWLAnnotationProperty omvNumberOfClasses  = null;
  private static OWLAnnotationProperty omvResourceLocator  = null;
  private static OWLAnnotationProperty omvStatus  = null;
  private static OWLAnnotationProperty omvUsedOntologyEngineeringTool  = null;
  private static OWLAnnotationProperty pavCuratedBy  = null;
  private static OWLAnnotationProperty provWasGeneratedBy  = null;
  private static OWLAnnotationProperty schemaTranslator  = null;
  private static OWLAnnotationProperty vannChanges  = null;
  private static OWLAnnotationProperty vannPreferredNamespacePrefix  = null;
  private static OWLAnnotationProperty vannPreferredNamespaceUri  = null;
  private static OWLAnnotationProperty voafHasEquivalencesWith  = null;
  private static OWLAnnotationProperty voafSimilar  = null;
  private static OWLAnnotationProperty voidOpenSearchDescription  = null;
  private static OWLAnnotationProperty voidRootResource  = null;
  private static OWLAnnotationProperty voidUriLookupEndpoint  = null;
  private static OWLAnnotationProperty voidUriRegexPattern  = null;
  
  

  public static void main(final String[] args) throws IOException, SAXException, TikaException,
      ParserConfigurationException, OWLOntologyCreationException, OWLOntologyStorageException {

//    final Map<String, Object> properties = new HashMap<String, Object>();
//    properties.put("WARN_BAD_NAME", "EM_IGNORE");
    // Put a properties object into the Context.
//    final Context cxt = new Context();
//    cxt.set(SysRIOT.sysRdfReaderProperties, properties);
//    final InputStream in = new FileInputStream(owlFileName);
    final OutputStream fileoutputstream = new FileOutputStream(cisp2modMetaDataFileName);
//    final OWLOntologyLoaderConfiguration config = man.getOntologyLoaderConfiguration();
//    man.setOntologyLoaderConfiguration(
//        config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT));
    man = OWLManager.createOWLOntologyManager();
    onto = man.createOntology(IRI.create(MetadataUtil.getProperties("terminologie_profil_IRI", "CISP2")));
    fact = onto.getOWLOntologyManager().getOWLDataFactory();

    rdfType = fact.getOWLAnnotationProperty(OWLRDFVocabulary.RDF_TYPE.getIRI());
    priorVersion = fact.getOWLAnnotationProperty(OWLRDFVocabulary.OWL_PRIOR_VERSION.getIRI());
    rdfValue = fact.getOWLAnnotationProperty(RDFVocabulary.value.getIRI());
    dctLicense = fact.getOWLAnnotationProperty(DCTVocabulary.license.getIRI());
    dcatAccessURL = fact.getOWLAnnotationProperty(DCATVocabulary.accessURL.getIRI());
    dcatMediaType = fact.getOWLAnnotationProperty(DCATVocabulary.mediaType.getIRI());
    dctAlternative = fact.getOWLAnnotationProperty(DCTVocabulary.alternative.getIRI());
    dctDescription = fact.getOWLAnnotationProperty(DCTVocabulary.description.getIRI());
    dctAudience = fact.getOWLAnnotationProperty(DCTVocabulary.audience.getIRI());
    dctIdentifier = fact.getOWLAnnotationProperty(DCTVocabulary.identifier.getIRI());
    dctLanguage = fact.getOWLAnnotationProperty(DCTVocabulary.language.getIRI());
    dctPublisher = fact.getOWLAnnotationProperty(DCTVocabulary.publisher.getIRI());
    dctRelation = fact.getOWLAnnotationProperty(DCTVocabulary.relation.getIRI());
    dctTitle = fact.getOWLAnnotationProperty(DCTVocabulary.title.getIRI());
    skosHiddenLabel = fact.getOWLAnnotationProperty(SKOSVocabulary.HIDDENLABEL.getIRI());
    
    ccUseGuidelines  = fact.getOWLAnnotationProperty(CCVocabulary.useGuidelines.getIRI());;
    dctAccessRights  = fact.getOWLAnnotationProperty(DCTVocabulary.accessRights.getIRI());;
    dctContributor  = fact.getOWLAnnotationProperty(DCTVocabulary.contributor.getIRI());;
    dctCoverage  = fact.getOWLAnnotationProperty(DCTVocabulary.coverage.getIRI());;
    dctCreated  = fact.getOWLAnnotationProperty(DCTVocabulary.created.getIRI());;
    dctCreator  = fact.getOWLAnnotationProperty(DCTVocabulary.creator.getIRI());
    dctRightsHolder  = fact.getOWLAnnotationProperty(DCTVocabulary.rightsHolder.getIRI());;
    dctSubject  = fact.getOWLAnnotationProperty(DCTVocabulary.subject.getIRI());;
    foafHomepage  = fact.getOWLAnnotationProperty(FOAFVocabulary.homepage.getIRI());;
    doorComesFromTheSameDomain  = fact.getOWLAnnotationProperty(DOORVocabulary.comesFromTheSameDomain.getIRI());;
    modBrowsingUI  = fact.getOWLAnnotationProperty(MODVocabulary.browsingUI.getIRI());;
    modByteSize  = fact.getOWLAnnotationProperty(MODVocabulary.byteSize.getIRI());;
    modGroup  = fact.getOWLAnnotationProperty(MODVocabulary.group.getIRI());;
    modVocabularyUsed  = fact.getOWLAnnotationProperty(MODVocabulary.vocabularyUsed.getIRI());
    modHierarchyProperty  = fact.getOWLAnnotationProperty(MODVocabulary.hierarchyProperty.getIRI());
    modPrefLabelProperty  = fact.getOWLAnnotationProperty(MODVocabulary.prefLabelProperty.getIRI());
    omvAcronym  = fact.getOWLAnnotationProperty(OMVVocabulary.acronym.getIRI());;
    omvHasFormalityLevel  = fact.getOWLAnnotationProperty(OMVVocabulary.hasFormalityLevel.getIRI());;
    omvHasOntologyLanguage  = fact.getOWLAnnotationProperty(OMVVocabulary.hasOntologyLanguage.getIRI());;
    omvHasOntologySyntax  = fact.getOWLAnnotationProperty(OMVVocabulary.hasOntologySyntax.getIRI());
    omvKeyClasses  = fact.getOWLAnnotationProperty(OMVVocabulary.keyClasses.getIRI());;
    omvNumberOfAxioms  = fact.getOWLAnnotationProperty(OMVVocabulary.numberOfAxioms.getIRI());;
    omvNumberOfClasses  = fact.getOWLAnnotationProperty(OMVVocabulary.numberOfClasses.getIRI());;
    omvResourceLocator  = fact.getOWLAnnotationProperty(OMVVocabulary.resourceLocator.getIRI());;
    omvStatus  = fact.getOWLAnnotationProperty(OMVVocabulary.status.getIRI());;
    omvUsedOntologyEngineeringTool  = fact.getOWLAnnotationProperty(OMVVocabulary.usedOntologyEngineeringTool.getIRI());;
    pavCuratedBy  = fact.getOWLAnnotationProperty(PAVVocabulary.curatedBy.getIRI());;
    provWasGeneratedBy  = fact.getOWLAnnotationProperty(PROVVocabulary.WAS_GENERATED_BY.getIRI());;
    schemaTranslator  = fact.getOWLAnnotationProperty(SchemaVocabulary.translator.getIRI());;
    vannChanges  = fact.getOWLAnnotationProperty(VANNVocabulary.changes.getIRI());;
    vannPreferredNamespacePrefix  = fact.getOWLAnnotationProperty(VANNVocabulary.preferredNamespacePrefix.getIRI());;
    vannPreferredNamespaceUri  = fact.getOWLAnnotationProperty(VANNVocabulary.preferredNamespaceUri.getIRI());;
    voafHasEquivalencesWith  = fact.getOWLAnnotationProperty(VOAFVocabulary.hasEquivalencesWith.getIRI());;
    voafSimilar  = fact.getOWLAnnotationProperty(VOAFVocabulary.similar.getIRI());;
    voidOpenSearchDescription  = fact.getOWLAnnotationProperty(VOIDVocabulary.openSearchDescription.getIRI());;
    voidRootResource  = fact.getOWLAnnotationProperty(VOIDVocabulary.rootResource.getIRI());;
    voidUriLookupEndpoint  = fact.getOWLAnnotationProperty(VOIDVocabulary.uriLookupEndpoint.getIRI());;
    voidUriRegexPattern  = fact.getOWLAnnotationProperty(VOIDVocabulary.uriRegexPattern.getIRI());;
    
    
    
    addMediatypeRDF();
//    addMediatypeJsonLD();
    
    addDistributionOWL();
//    addDistributionJsonLD();
    
    addCabinet_medical();
    addLaboratoireMCS();
    addLaboratoireBiologieMedicale();
    addPrescripteurs();
    
    addTerminologie();
    
    
    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
    ontologyFormat.setPrefix("cc", "http://creativecommons.org/ns#");
    ontologyFormat.setPrefix("dc", "http://purl.org/dc/elements/1.1/");
    ontologyFormat.setPrefix("dct", "http://purl.org/dc/terms/");
    ontologyFormat.setPrefix("omv", "http://omv.ontoware.org/2005/05/ontology#");
    ontologyFormat.setPrefix("door", "http://kannel.open.ac.uk/ontology#");
    man.saveOntology(onto, ontologyFormat, fileoutputstream);
    fileoutputstream.close();
    System.out.println("Done.");
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
  
 
  
  public static void addMediatypeRDF() {
    mediaTypeRDF = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("ontologySyntaxRDFXML")));
    
    OWLDeclarationAxiom declarationAxiom = fact
        .getOWLDeclarationAxiom(mediaTypeRDF);
  man.addAxiom(onto, declarationAxiom);
    
    final OWLAnnotation labelAnno1 =
        fact.getOWLAnnotation(rdfValue, fact.getOWLLiteral(MetadataUtil.getProperties("ontologySyntaxRDFXML_value")));
    final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(mediaTypeRDF.getIRI(), labelAnno1);
    man.applyChange(new AddAxiom(onto, ax1));
    
    final OWLAnnotation labelAnno2 =
        fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(MetadataUtil.getProperties("ontologySyntaxRDFXML_label")));
    final OWLAxiom ax2 = fact.getOWLAnnotationAssertionAxiom(mediaTypeRDF.getIRI(), labelAnno2);
    man.applyChange(new AddAxiom(onto, ax2));
  }
  
//  public static void addMediatypeJsonLD() {
//    mediaTypeJsonLD = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("ontologySyntaxJsonLD")));
//    OWLDeclarationAxiom declarationAxiom = fact
//        .getOWLDeclarationAxiom(mediaTypeJsonLD);
//  man.addAxiom(onto, declarationAxiom);
//    
//    
//    final OWLAnnotation labelAnno1 =
//        fact.getOWLAnnotation(rdfValue, fact.getOWLLiteral(MetadataUtil.getProperties("ontologySyntaxJsonLD_value")));
//    final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(mediaTypeJsonLD.getIRI(), labelAnno1);
//    man.applyChange(new AddAxiom(onto, ax1));
//    
//    final OWLAnnotation labelAnno2 =
//        fact.getOWLAnnotation(fact.getRDFSLabel(), fact.getOWLLiteral(MetadataUtil.getProperties("ontologySyntaxJsonLD_label")));
//    final OWLAxiom ax2 = fact.getOWLAnnotationAssertionAxiom(mediaTypeJsonLD.getIRI(), labelAnno2);
//    man.applyChange(new AddAxiom(onto, ax2));
//  }
  
  public static void addDistributionOWL() {
    distributionOWL = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("ontologyLanguageOWL")));
    OWLDeclarationAxiom declarationAxiom = fact
        .getOWLDeclarationAxiom(distributionOWL);
  man.addAxiom(onto, declarationAxiom);
    
    final OWLAnnotation labelAnno3 =
        fact.getOWLAnnotation(dcatAccessURL, IRI.create(MetadataUtil.getProperties("ontologyLanguageOWL_accessURL")));
    final OWLAxiom ax3 = fact.getOWLAnnotationAssertionAxiom(distributionOWL.getIRI(), labelAnno3);
    man.applyChange(new AddAxiom(onto, ax3));
    
    final OWLAnnotation labelAnno4 =
        fact.getOWLAnnotation(dcatMediaType, mediaTypeRDF.getIRI());
    final OWLAxiom ax4 = fact.getOWLAnnotationAssertionAxiom(distributionOWL.getIRI(), labelAnno4);
    man.applyChange(new AddAxiom(onto, ax4));
  }
  
//  public static void addDistributionJsonLD() {
//    distributionJsonLD = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("ontologyLanguageJSONLD")));
//    OWLDeclarationAxiom declarationAxiom = fact
//        .getOWLDeclarationAxiom(distributionJsonLD);
//  man.addAxiom(onto, declarationAxiom);
//    
//    final OWLAnnotation labelAnno3 =
//        fact.getOWLAnnotation(dcatAccessURL, IRI.create(MetadataUtil.getProperties("ontologyLanguageJSONLD_accessURL")));
//    final OWLAxiom ax3 = fact.getOWLAnnotationAssertionAxiom(distributionJsonLD.getIRI(), labelAnno3);
//    man.applyChange(new AddAxiom(onto, ax3));
//    
//    final OWLAnnotation labelAnno4 =
//        fact.getOWLAnnotation(dcatMediaType, mediaTypeJsonLD.getIRI());
//    final OWLAxiom ax4 = fact.getOWLAnnotationAssertionAxiom(distributionJsonLD.getIRI(), labelAnno4);
//    man.applyChange(new AddAxiom(onto, ax4));
//  }
  
  
  public static void addTerminologie() {
    terminologie = fact.getOWLNamedIndividual(IRI.create(MetadataUtil.getProperties("terminologie_IRI", "CISP2")));
    OWLDeclarationAxiom declarationAxiom = fact
        .getOWLDeclarationAxiom(terminologie);
    man.addAxiom(onto, declarationAxiom);
    
    addIRIAxioms(rdfType, "terminologie_rdf_type");
    addLateralAxioms(ccUseGuidelines, "terminologie_useGuidelines_en", "en");
    addLateralAxioms(doorComesFromTheSameDomain, "terminologie_comesFromTheSameDomain");
    addLateralAxioms(dctAlternative, "terminologie_alternative_en", "en");
    addLateralAxioms(dctAlternative, "terminologie_alternative_fr", "fr");
    
    addIRIAxioms(dctAudience, laboratoireMCS);
    addIRIAxioms(dctAudience, laboratoireBiologieMedicale);
    addIRIAxioms(dctAudience, prescripteurs);
    addIRIAxioms(dctAudience, cabinet_medical);
    
    addLateralAxioms(dctDescription, "terminologie_description_fr", "fr");
    addLateralAxioms(dctIdentifier, "terminologie_identifier");
    addLateralAxioms(dctCreated, "terminologie_created", OWL2Datatype.XSD_DATE_TIME);
    
    final OWLAnnotation labelAnno11 =
        fact.getOWLAnnotation(dctLanguage, IRI.create(MetadataUtil.getProperties("terminologie_language_en", "CISP2"), "en"));
    final OWLAxiom ax11 = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), labelAnno11);
    man.applyChange(new AddAxiom(onto, ax11));
    final OWLAnnotation labelAnno12 =
        fact.getOWLAnnotation(dctLanguage, IRI.create(MetadataUtil.getProperties("terminologie_language_fr", "CISP2"), "fr"));
    final OWLAxiom ax12 = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), labelAnno12);
    man.applyChange(new AddAxiom(onto, ax12));
    
    addLateralAxioms(dctPublisher, "terminologie_publisher");
    addLateralAxioms(dctTitle, "terminologie_title_en", "en");
    addLateralAxioms(dctTitle, "terminologie_title_fr", "fr");
    addLateralAxioms(dctLicense, "terminologie_Licence_fr", "fr");
    addLateralAxioms(dctLicense, "terminologie_Licence_en", "en");
    addLateralAxioms(fact.getRDFSComment(), "terminologie_comment_fr", "fr");
    addLateralAxioms(priorVersion, "terminologie_priorVersion");
    addLateralAxioms(fact.getOWLVersionInfo(), "terminologie_versionInfo");
    addLateralAxioms(schemaTranslator, "terminologie_translator");
    
    for(String vocab : MetadataUtil.getProperties("terminologie_vocabularyUsed", "CISP2").split(";")) {
      final OWLAnnotation labelAnno2a =
          fact.getOWLAnnotation(modVocabularyUsed, IRI.create(vocab));
      final OWLAxiom ax2a = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), labelAnno2a);
      man.applyChange(new AddAxiom(onto, ax2a));
    }
    
    addLateralAxioms(dctAccessRights, "terminologie_accessRights_fr", "fr");
    addLateralAxioms(dctContributor, "terminologie_contributor");
    addLateralAxioms(dctCoverage, "terminologie_coverage_fr", "fr");
    addLateralAxioms(dctCoverage, "terminologie_coverage_en", "en");
    addLateralAxioms(dctCreator, "terminologie_creator");
    addLateralAxioms(dctRelation, "terminologie_relation");
    addLateralAxioms(dctRightsHolder, "terminologie_rightsHolder");
    addLateralAxioms(dctSubject, "terminologie_subject");
    addLateralAxioms(modBrowsingUI, "terminologie_browsingUI");
    addLateralAxioms(modByteSize, "terminologie_byteSize", OWL2Datatype.XSD_NON_NEGATIVE_INTEGER);
    addLateralAxioms(modGroup, "terminologie_group");
    
    for(String vocab : MetadataUtil.getProperties("terminologie_hierarchyProperty", "CISP2").split(";")) {
      final OWLAnnotation labelAnno2a =
          fact.getOWLAnnotation(modHierarchyProperty, IRI.create(vocab));
      final OWLAxiom ax2a = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), labelAnno2a);
      man.applyChange(new AddAxiom(onto, ax2a));
    }
    
    addIRIAxioms(modHierarchyProperty, "terminologie_hierarchyProperty");
    addIRIAxioms(modPrefLabelProperty, "terminologie_prefLabelProperty");
    addLateralAxioms(skosHiddenLabel, "terminologie_hiddenLabel_en", "en");
    addLateralAxioms(provWasGeneratedBy, "terminologie_wasGeneratedBy");
    addIRIAxioms(foafHomepage, "terminologie_homepage");
    addIRIAxioms(voidOpenSearchDescription, "terminologie_openSearchDescription");
    addIRIAxioms(voidRootResource, "terminologie_rootResource");
    addIRIAxioms(voidUriLookupEndpoint, "terminologie_uriLookupEndpoint");
    addLateralAxioms(voidUriRegexPattern, "terminologie_uriRegexPattern");
    addLateralAxioms(pavCuratedBy, "terminologie_curatedBy");
    addLateralAxioms(vannChanges, "terminologie_changes_en", "en");
    addLateralAxioms(vannPreferredNamespacePrefix, "terminologie_preferredNamespacePrefix");
    addIRIAxioms(vannPreferredNamespaceUri, "terminologie_preferredNamespaceUri");
    addLateralAxioms(voafHasEquivalencesWith, "terminologie_hasEquivalencesWith");
    addLateralAxioms(voafSimilar, "terminologie_similar");
    addLateralAxioms(omvAcronym, "terminologie_acronym");
    addLateralAxioms(omvNumberOfAxioms, "terminologie_numberOfAxioms", OWL2Datatype.XSD_NON_NEGATIVE_INTEGER);
    addLateralAxioms(omvNumberOfClasses, "terminologie_numberOfClasses", OWL2Datatype.XSD_NON_NEGATIVE_INTEGER);
    addLateralAxioms(omvStatus, "terminologie_status");
    addIRIAxioms(omvKeyClasses, "terminologie_keyClasses");
    addIRIAxioms(omvResourceLocator, "terminologie_resourceLocator");
    addIRIAxioms(omvUsedOntologyEngineeringTool, "terminologie_usedOntologyEngineeringTool");
    addIRIAxioms(omvHasFormalityLevel, "terminologie_hasFormalityLevel");
    
    final OWLAnnotation labelAnno55 =
          fact.getOWLAnnotation(omvHasOntologyLanguage, distributionOWL.getIRI());
      final OWLAxiom ax55 = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), labelAnno55);
      man.applyChange(new AddAxiom(onto, ax55));
//      final OWLAnnotation labelAnno1 =
//          fact.getOWLAnnotation(omvHasOntologyLanguage, distributionJsonLD.getIRI());
//      final OWLAxiom ax1 = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), labelAnno1);
//      man.applyChange(new AddAxiom(onto, ax1));  
    
    final OWLAnnotation labelAnno5 =
          fact.getOWLAnnotation(omvHasOntologySyntax, mediaTypeRDF.getIRI());
      final OWLAxiom ax5 = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), labelAnno5);
      man.applyChange(new AddAxiom(onto, ax5));
//    final OWLAnnotation labelAnno =
//          fact.getOWLAnnotation(omvHasOntologySyntax, mediaTypeJsonLD.getIRI());
//      final OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), labelAnno);
//      man.applyChange(new AddAxiom(onto, ax));
//    
    
   
  }

  public static void addIRIAxioms(OWLAnnotationProperty prop, String iri) {
    final OWLAnnotation annotation =
        fact.getOWLAnnotation(prop, IRI.create(MetadataUtil.getProperties(iri, "CISP2")));
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
        fact.getOWLAnnotation(prop, fact.getOWLLiteral(MetadataUtil.getProperties(iri, "CISP2")));
    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), annotation);
    man.applyChange(new AddAxiom(onto, axiom));
  }
  
  public static void addLateralAxioms(OWLAnnotationProperty prop, String iri,  OWL2Datatype datatype) {
    final OWLAnnotation annotation =
        fact.getOWLAnnotation(prop, fact.getOWLLiteral(MetadataUtil.getProperties(iri, "CISP2"), datatype));
    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), annotation);
    man.applyChange(new AddAxiom(onto, axiom));
  }
  
  public static void addLateralAxioms(OWLAnnotationProperty prop, String iri, String lang) {
    final OWLAnnotation annotation =
        fact.getOWLAnnotation(prop, fact.getOWLLiteral(MetadataUtil.getProperties(iri, "CISP2"), lang));
    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(terminologie.getIRI(), annotation);
    man.applyChange(new AddAxiom(onto, axiom));
  }
  
}
