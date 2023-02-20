package fr.gouv.esante.pml.smt.cim11;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.semanticweb.owlapi.model.AxiomType;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import ru.avicomp.ontapi.internal.AxiomParserProvider;
import ru.avicomp.ontapi.jena.OntModelFactory;

public class OntAPI {
  
  private static String jsonFileName = PropertiesUtil.getProperties("jsonFileName");
  
  public static void main(final String[] args) throws FileNotFoundException, IOException {
 // Create a standard Jena Model:
    Model m = ModelFactory.createDefaultModel();
    // Load RDF data:
    try (InputStream in = new FileInputStream(jsonFileName)) {
        RDFDataMgr.read(m, in, Lang.JSONLD);
    }
    // list all OWLAxioms from Jena Model:
    AxiomType.AXIOM_TYPES.stream()
            .map(AxiomParserProvider::get)
            .forEach(t -> t.axioms(OntModelFactory.createModel(m.getGraph()))
                    .forEach(System.out::println)); 
  }

}
