    package fr.gouv.esante.pml.smt;


    import fr.gouv.esante.pml.smt.cim11.*;

    public class Launcher {
      public static void main(final String... args) throws Exception {
          if (args.length == 0) {
            System.out.println("Erreur! il manque des paramètres.");
            System.out.println("Mode d'emploi:");
            System.out.println("java -jar Jar-Name.jar streamICD (pour Streaming CIM11)");
            System.out
                    .println("java -jar Jar-Name.jar json2rdf (pour Converture de jsonld vers rdf/skos)");
            System.out.println("java -jar Jar-Name.jar skos2owl (pour Converture de rdf/skos vers owl)");
        }
        else if ("modelingMmsCim11".equals(args[0])) {
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
            Thread threadConct = new Thread(new ThreadConcatenateTwoOntologyCim11(args));
             Thread threadCorrect = new Thread(new ThreadCorrectionBlockIDOntologyCim11(args));
            Thread threadRemove = new Thread(new ThreadRemoveFile2(args));
            threadConct.start();
              try {
                  threadConct.join();
                } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                     threadCorrect.start();
                 try {
                     threadCorrect.join();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 threadRemove.start();
        }
        else if ("finalCim11".equals(args[0])) {
            CorrectionBlockIDOntologyCim11.main(args);
        }
        else if ("owlCim11Mms".equals(args[0])) {
             Thread threadGetICD = new Thread(new ThreadGetICDFromAPI(args));
             Thread threadJsonToRdf = new Thread(new ThreadJsonToRDFClient(args));
             Thread threadSkosToOwl = new Thread(new ThreadSKOSToOWL(args));
             Thread threadModeling = new Thread(new ThreadModelingOntologyMmsCim11(args));
             Thread tRmv = new Thread(new ThreadRemoveFile(args));
             threadGetICD.start();
            try {
                threadGetICD.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadJsonToRdf.start();
            try {
                threadJsonToRdf.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadSkosToOwl.start();
            try {
                threadSkosToOwl.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadModeling.start();
            try {
                threadModeling.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tRmv.start();
        }
        else {
          System.out.println("Erreur! mauvais paramètres.");
          System.out.println("Mode d'emploi:");
          System.out.println("java -jar Jar-Name.jar streamICD (pour Streaming CIM11)");
          System.out.println("java -jar Jar-Name.jar json2rdf (pour Converture de jsonld vers rdf)");
          System.out.println("java -jar Jar-Name.jar skos2owl (pour Converture de skos vers owl)");
        }

      }

    }
