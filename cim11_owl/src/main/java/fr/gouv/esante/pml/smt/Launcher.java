package fr.gouv.esante.pml.smt;


import fr.gouv.esante.pml.smt.cim11.ConcatenateTwoOntologyCim11;
import fr.gouv.esante.pml.smt.cim11.CorrectionBlockIDOntologyCim11;
import fr.gouv.esante.pml.smt.cim11.GetICDFromAPI;
import fr.gouv.esante.pml.smt.cim11.JsonToRDFClient;
import fr.gouv.esante.pml.smt.cim11.ModelingOntologyFoundationCim11;
import fr.gouv.esante.pml.smt.cim11.ModelingOntologyMmsCim11;
import fr.gouv.esante.pml.smt.cim11.SKOSToOWL;

public class Launcher {
  public static void main(final String... args) throws Exception {

    if (args.length == 0) {
      System.out.println("Erreur! il manque des paramètres.");
      System.out.println("Mode d'emploi:");
      System.out.println("java -jar Jar-Name.jar streamICD (pour Streaming CIM11)");
      System.out
          .println("java -jar Jar-Name.jar json2rdf (pour Converture de jsonld vers rdf/skos)");
      System.out.println("java -jar Jar-Name.jar skos2owl (pour Converture de rdf/skos vers owl)");
//      System.out.println("java -jar Jar-Name.jar setMetadata (pour ajouter des metadonnées à l'ontologie)");
//      System.out.println("java -jar Jar-Name.jar getMetadata (pour afficher les metadonnées de l'ontologie)");
    } else if ("ccam".equals(args[0])) {
    	//ModelingOntologyCCAM.main(args);
    } else if ("ccamVerbeAction".equals(args[0])) {
    	//CodeActionOntologie.main(args);
    } else if ("ccamModeAcces".equals(args[0])) {
    	//ModeAccesOntologie.main(args);
    } else if ("ccamTopographie".equals(args[0])) {
    	//TopographieOntologie.main(args);
    }else if ("modelingMmsCim11".equals(args[0])) {
    	ModelingOntologyMmsCim11.main(args);
    } else if ("skos2owl".equals(args[0])) {
        SKOSToOWL.main(args);
      } else if ("json2rdf".equals(args[0])) {
      JsonToRDFClient.main(args);
    } else if ("streamICD".equals(args[0])) {
      GetICDFromAPI.main(args);
    }else if ("modelingFoundCim11".equals(args[0])) {
    	ModelingOntologyFoundationCim11.main(args);
    }
    else if ("concatenateCim11".equals(args[0])) {
    	ConcatenateTwoOntologyCim11.main(args);
    }
    else if ("finalCim11".equals(args[0])) {
    	CorrectionBlockIDOntologyCim11.main(args);
    }
    
    
    /*else if ("setMetadata".equals(args[0])) {
      CIM11Metadata.main(args);
    }else if ("getMetadata".equals(args[0])) {
      GetMetadata.main(args);
    } */else {
      System.out.println("Erreur! mauvais paramètres.");
      System.out.println("Mode d'emploi:");
      System.out.println("java -jar Jar-Name.jar streamICD (pour Streaming CIM11)");
      System.out.println("java -jar Jar-Name.jar json2rdf (pour Converture de jsonld vers rdf)");
      System.out.println("java -jar Jar-Name.jar skos2owl (pour Converture de skos vers owl)");
//      System.out.println("java -jar Jar-Name.jar setMetadata (pour ajouter des metadonnées à l'ontologie)");
//      System.out.println("java -jar Jar-Name.jar getMetadata (pour afficher les metadonnées de l'ontologie)");
    }

  }

}
