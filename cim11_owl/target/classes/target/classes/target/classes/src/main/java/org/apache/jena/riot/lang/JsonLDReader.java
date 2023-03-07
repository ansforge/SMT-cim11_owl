/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.riot.lang ;

import java.io.IOException ;
import java.io.InputStream ;
import java.io.Reader ;
import java.util.List ;
import java.util.Map ;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.jena.atlas.io.IO ;
import org.apache.jena.atlas.lib.InternalErrorException ;
import org.apache.jena.atlas.web.ContentType ;
import org.apache.jena.datatypes.RDFDatatype ;
import org.apache.jena.datatypes.xsd.XSDDatatype ;
import org.apache.jena.graph.Node ;
import org.apache.jena.graph.NodeFactory ;
import org.apache.jena.graph.Triple ;
import org.apache.jena.riot.*;
import org.apache.jena.riot.system.*;
import org.apache.jena.sparql.core.Quad ;
import org.apache.jena.sparql.util.Context ;

import com.fasterxml.jackson.core.JsonLocation ;
import com.fasterxml.jackson.core.JsonProcessingException ;
import com.github.jsonldjava.core.DocumentLoader;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.core.JsonLdTripleCallback;
import com.github.jsonldjava.core.RDFDataset;
import com.github.jsonldjava.utils.JsonUtils ;

/**
 * Note: it is possible to override jsonld's "@context" value by providing one,
 * using a {@link org.apache.jena.sparql.util.Context}, and setting the {@link RIOT#JSONLD_CONTEXT} Symbol's value
 * to the data expected by JSON-LD java API (a {@link Map}).
 */
public class JsonLDReader implements ReaderRIOT
{
    private /*final*/ ErrorHandler errorHandler = ErrorHandlerFactory.getDefaultErrorHandler() ;
    private final /*final*/ ParserProfile profile;
    
    public JsonLDReader(Lang lang, ParserProfile profile, ErrorHandler errorHandler) {
        this.profile = profile;
        this.errorHandler = errorHandler;
    }
    
    @Override
    public void read(Reader reader, String baseURI, ContentType ct, StreamRDF output, Context context) {
        try {
            Object jsonObject = JsonUtils.fromReader(reader) ;
            read$(jsonObject, baseURI, ct, output, context) ;
        }
        catch (JsonProcessingException ex) {    
            // includes JsonParseException
            // The Jackson JSON parser, or addition JSON-level check, throws up something.
            JsonLocation loc = ex.getLocation() ;
            errorHandler.error(ex.getOriginalMessage(), loc.getLineNr(), loc.getColumnNr()); 
            throw new RiotException(ex.getOriginalMessage()) ;
        }
        catch (IOException e) {
            errorHandler.error(e.getMessage(), -1, -1); 
            IO.exception(e) ;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void read(InputStream in, String baseURI, ContentType ct, StreamRDF output, Context context) {
        try {
            Object jsonObject = JsonUtils.fromInputStream(in) ;
            
            if (context != null) {
                Object jsonldCtx = context.get(RIOT.JSONLD_CONTEXT);
                if (jsonldCtx != null) {
                    if (jsonObject instanceof Map) {
                        ((Map) jsonObject).put("@context", jsonldCtx);
                    } else {
                        errorHandler.warning("Unexpected: not a Map; unable to set JsonLD's @context",-1,-1);
                    }
                }
            }
            read$(jsonObject, baseURI, ct, output, context) ;
        }
        catch (JsonProcessingException ex) {    
            // includes JsonParseException
            // The Jackson JSON parser, or addition JSON-level check, throws up something.
            JsonLocation loc = ex.getLocation() ;
            errorHandler.error(ex.getOriginalMessage(), loc.getLineNr(), loc.getColumnNr()); 
            throw new RiotException(ex.getOriginalMessage()) ;
        }
        catch (IOException e) {
            errorHandler.error(e.getMessage(), -1, -1); 
            IO.exception(e) ;
        }
    }
    
    private void read$(Object jsonObject, String baseURI, ContentType ct, final StreamRDF output, Context context) {
        output.start() ;
        try {           
            JsonLdTripleCallback callback = new JsonLdTripleCallback() {
                @Override
                public Object call(RDFDataset dataset) {
                    
                    // Copy across namespaces
                    for (Entry<String, String> namespace : dataset.getNamespaces().entrySet()) {
                        output.prefix(namespace.getKey(), namespace.getValue());
                    }
                    
                    // Copy triples and quads
                    for ( String gn : dataset.keySet() ) {
                        Object x = dataset.get(gn) ;
                        if ( "@default".equals(gn) ) {
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> triples = (List<Map<String, Object>>)x ;
                            for ( Map<String, Object> t : triples ) {
                                Node s = createNode(t, "subject") ;
                                Node p = createNode(t, "predicate") ;
                                Node o = createNode(t, "object") ;
                                Triple triple = profile.createTriple(s, p, o, -1, -1) ;
                                output.triple(triple) ;
                            }
                        } else {
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> quads = (List<Map<String, Object>>)x ;
                            Node g = createURI(gn) ; 
                            for ( Map<String, Object> q : quads ) {
                                Node s = createNode(q, "subject") ;
                                Node p = createNode(q, "predicate") ;
                                Node o = createNode(q, "object") ;
                                Quad quad = profile.createQuad(g, s, p, o, -1, -1) ;
                                output.quad(quad) ;
                            }
                        }
                    }
                    return null ;
                }
            } ;
            JsonLdOptions options = new JsonLdOptions(baseURI);
            options.useNamespaces = true;
            
         // Inject a context document into the options as a literal string
            DocumentLoader dl = new DocumentLoader();
            // ... the contents of "contexts/example.jsonld"
            String contextForTopLevel = "{\r\n" + 
                "  \"@context\": {\r\n" + 
                "    \"title\": \"http://www.w3.org/2004/02/skos/core#prefLabel\",\r\n" + 
                "    \"definition\": \"http://www.w3.org/2004/02/skos/core#definition\",\r\n" + 
                "    \"child\": \"http://www.w3.org/2004/02/skos/core#narrowerTransitive\",\r\n" + 
                "    \"releaseDate\": \"http://id.who.int/icd/schema/releaseDate\",\r\n" + 
                "    \"releaseId\": \"http://id.who.int/icd/schema/releaseId\",\r\n" + 
                "    \"browserUrl\": \"http://id.who.int/icd/schema/browserUrl\"\r\n" + 
                "  }\r\n" + 
                "}";
            String contextForICD10Entity = "{\r\n" + 
                "  \"@context\": {\r\n" + 
                "    \"title\": \"http://www.w3.org/2004/02/skos/core#prefLabel\",\r\n" + 
                "    \"label\": \"http://www.w3.org/2000/01/rdf-schema#label\",\r\n" +
                "    \"definition\": \"http://www.w3.org/2004/02/skos/core#definition\",\r\n" + 
                "    \"parent\": \"http://www.w3.org/2004/02/skos/core#broaderTransitive\",\r\n" + 
                "    \"child\": \"http://www.w3.org/2004/02/skos/core#narrowerTransitive\",\r\n" + 
                "    \"inclusion\": \"http://id.who.int/icd/schema/inclusion\",\r\n" + 
                "    \"exclusion\": \"http://id.who.int/icd/schema/exclusion\",\r\n" + 
                "    \"code\": \"http://id.who.int/icd/schema/code\",\r\n" + 
                "    \"codingHint\": \"http://id.who.int/icd/schema/codingHint\",\r\n" + 
                "    \"classKind\": \"http://id.who.int/icd/schema/classKind\",\r\n" + 
                "    \"note\": \"http://id.who.int/icd/schema/codingNote\",\r\n" + 
                "    \"browserUrl\": \"http://id.who.int/icd/schema/browserUrl\"\r\n" + 
                "  }\r\n" + 
                "}";
            String contextForFoundationEntity = "{\r\n" + 
                "  \"@context\": {\r\n" + 
                "    \"title\": \"http://www.w3.org/2004/02/skos/core#prefLabel\",\r\n" +
                "    \"label\": \"http://www.w3.org/2000/01/rdf-schema#label\",\r\n" +
                "    \"definition\": \"http://www.w3.org/2004/02/skos/core#definition\",\r\n" + 
                "    \"longDefinition\": \"http://id.who.int/icd/schema/longDefinition\",\r\n" + 
                "    \"parent\": \"http://www.w3.org/2004/02/skos/core#broaderTransitive\",\r\n" + 
                "    \"child\": \"http://www.w3.org/2004/02/skos/core#narrowerTransitive\",\r\n" + 
                "    \"synonym\": \"http://www.w3.org/2004/02/skos/core#altLabel\",\r\n" + 
                "    \"fullySpecifiedName\": \"http://id.who.int/icd/schema/fullySpecifiedName\",\r\n" + 
                "    \"narrowerTerm\": \"http://id.who.int/icd/schema/narrowerTerm\",\r\n" + 
                "    \"exclusion\": \"http://id.who.int/icd/schema/exclusion\",\r\n" + 
                "    \"inclusion\": \"http://id.who.int/icd/schema/inclusion\",\r\n" + 
                "    \"browserUrl\": \"http://id.who.int/icd/schema/browserUrl\",\r\n" + 
                "    \"foundationReference\": \"http://id.who.int/icd/schema/foundationReference\"\r\n" + 
                "  }\r\n" + 
                "}";
            String contextForLinearizationEntity ="{\r\n" + 
            		"  \"@context\": {\r\n" +
            		"  \"parent\": \"http://www.w3.org/2004/02/skos/core#broaderTransitive\",\r\n" +
            		"  \"child\": \"http://www.w3.org/2004/02/skos/core#narrowerTransitive\",\r\n" +
            		"  \"definition\": \"http://www.w3.org/2004/02/skos/core#definition\",\r\n" +
            		"  \"longDefinition\": \"http://id.who.int/icd/schema/longDefinition\",\r\n" +
            		"  \"code\": \"http://id.who.int/icd/schema/code\",\r\n" +
            		"  \"title\": \"http://www.w3.org/2004/02/skos/core#prefLabel\",\r\n" +
            		"  \"fullySpecifiedName\": \"http://id.who.int/icd/schema/fullySpecifiedName\",\r\n" +
            		"  \"source\": \"http://id.who.int/icd/schema/source\",\r\n" +
            		"  \"inclusion\": \"http://id.who.int/icd/schema/inclusion\",\r\n" +
            		"  \"exclusion\": \"http://id.who.int/icd/schema/exclusion\",\r\n" +
            		"  \"indexTerm\": \"http://id.who.int/icd/schema/indexTerm\",\r\n" +
            		"  \"classKind\": \"http://id.who.int/icd/schema/classKind\",\r\n" +
            		"  \"browserUrl\": \"http://id.who.int/icd/schema/browserUrl\",\r\n" +
            		"  \"foundationChildElsewhere\": \"http://id.who.int/icd/schema/foundationChildElsewhere\",\r\n" +
            		"  \"postcoordinationScale\": \"http://id.who.int/icd/schema/postcoordinationScale\",\r\n" +
            		"  \"axisName\": \"http://id.who.int/icd/schema/axisName\",\r\n" +
            		"  \"requiredPostcoordination\": \"http://id.who.int/icd/schema/requiredPostcoordination\",\r\n" +
            		"  \"allowMultipleValues\": \"http://id.who.int/icd/schema/allowMultipleValues\",\r\n" +
            		"  \"scaleEntity\": \"http://id.who.int/icd/schema/scaleEntity\",\r\n" +
            		"  \"codingNote\": \"http://id.who.int/icd/schema/codingNote\",\r\n" +
            		"  \"codeRange\": \"http://id.who.int/icd/schema/codingRange\",\r\n" +
            		"  \"blockId\": \"http://id.who.int/icd/schema/blockId\",\r\n" +
            		"  \"foundationReference\": \"http://id.who.int/icd/schema/foundationReference\",\r\n" +
            		"  \"linearizationReference\": \"http://id.who.int/icd/schema/linearizationReference\"\r\n" +
            			"  }\r\n" +
            			"  }";
            
            String contextForFoundationEntity2 = "{\r\n"
            		+ "  \"@context\": {\r\n"
            		+ "    \"title\": \"http://www.w3.org/2004/02/skos/core#prefLabel\",\r\n"
            		+ "    \"definition\": \"http://www.w3.org/2004/02/skos/core#definition\",\r\n"
            		+ "    \"longDefinition\": \"http://id.who.int/icd/schema/longDefinition\",\r\n"
            		+ "    \"parent\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://www.w3.org/2004/02/skos/core#broader\"\r\n"
            		+ "    },\r\n"
            		+ "    \"child\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://www.w3.org/2004/02/skos/core#narrower\"\r\n"
            		+ "    },\r\n"
            		+ "    \"synonym\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://www.w3.org/2008/05/skos-xl#altLabel\"\r\n"
            		+ "    },\r\n"
            		+ "    \"fullySpecifiedName\": \"http://id.who.int/icd/schema/fullySpecifiedName\",\r\n"
            		+ "    \"narrowerTerm\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/narrowerTerm\"\r\n"
            		+ "    },\r\n"
            		+ "    \"inclusion\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/inclusion\"\r\n"
            		+ "    },\r\n"
            		+ "    \"exclusion\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/exclusion\"\r\n"
            		+ "    },\r\n"
            		+ "    \"browserUrl\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/browserUrl\"\r\n"
            		+ "    },\r\n"
            		+ "    \"foundationReference\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/foundationReference\"\r\n"
            		+ "    },\r\n"
            		+ "    \"label\" : \"http://www.w3.org/2008/05/skos-xl#literalForm\"\r\n"
            		+ "  }\r\n"
            		+ "}";
            String contextForTopLevel2 = "{\r\n"
            		+ "  \"@context\": {\r\n"
            		+ "    \"title\": \"http://www.w3.org/2004/02/skos/core#prefLabel\",\r\n"
            		+ "    \"definition\": \"http://www.w3.org/2004/02/skos/core#definition\",\r\n"
            		+ "    \"child\": {\r\n"
            		+ "    	\"@type\" : \"@id\",\r\n"
            		+ "    	\"@id\" : \"http://www.w3.org/2004/02/skos/core#narrower\"\r\n"
            		+ "    },\r\n"
            		+ "    \"releaseDate\": {\r\n"
            		+ "    	\"@type\" : \"http://www.w3.org/2001/XMLSchema#date\",\r\n"
            		+ "    	\"@id\" : \"http://id.who.int/icd/schema/releaseDate\"\r\n"
            		+ "    },\r\n"
            		+ "    \"releaseId\": \"http://id.who.int/icd/schema/releaseId\",\r\n"
            		+ "    \"browserUrl\": {\r\n"
            		+ "    	\"@type\" : \"@id\",\r\n"
            		+ "    	\"@id\" : \"http://id.who.int/icd/schema/browserUrl\"\r\n"
            		+ "    }\r\n"
            		+ "  }\r\n"
            		+ "}";		
            String contextForLinearizationEntity2 = "{\r\n"
            		+ "  \"@context\": {\r\n"
            		+ "    \"parent\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://www.w3.org/2004/02/skos/core#broader\"\r\n"
            		+ "    },\r\n"
            		+ "    \"child\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://www.w3.org/2004/02/skos/core#narrower\"\r\n"
            		+ "    },\r\n"
            		+ "    \"definition\": \"http://www.w3.org/2004/02/skos/core#definition\",\r\n"
            		+ "    \"longDefinition\": \"http://id.who.int/icd/schema/longDefinition\",\r\n"
            		+ "    \"code\": \"http://id.who.int/icd/schema/code\",\r\n"
            		+ "    \"title\": \"http://www.w3.org/2004/02/skos/core#prefLabel\",\r\n"
            		+ "    \"fullySpecifiedName\": \"http://id.who.int/icd/schema/fullySpecifiedName\",\r\n"
            		+ "    \"source\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/source\"\r\n"
            		+ "    },\r\n"
            		+ "    \"inclusion\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/inclusion\"\r\n"
            		+ "    },\r\n"
            		+ "    \"exclusion\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/exclusion\"\r\n"
            		+ "    },\r\n"
            		+ "    \"indexTerm\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/indexTerm\"\r\n"
            		+ "    },\r\n"
            		+ "    \"classKind\": \"http://id.who.int/icd/schema/classKind\",\r\n"
            		+ "    \"browserUrl\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/browserUrl\"\r\n"
            		+ "    },\r\n"
            		+ "    \"foundationChildElsewhere\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/foundationChildElsewhere\"\r\n"
            		+ "    },\r\n"
            		+ "    \"postcoordinationScale\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/postcoordinationScale\"\r\n"
            		+ "    },\r\n"
            		+ "    \"axisName\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/axisName\"\r\n"
            		+ "    },\r\n"
            		+ "    \"requiredPostcoordination\": {\r\n"
            		+ "        \"@type\" : \"http://www.w3.org/2001/XMLSchema#boolean\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/requiredPostcoordination\"\r\n"
            		+ "    },\r\n"
            		+ "    \"allowMultipleValues\": {\r\n"
            		+ "        \"@type\" : \"http://www.w3.org/2001/XMLSchema#string\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/allowMultipleValues\"\r\n"
            		+ "    },\r\n"
            		+ "    \"scaleEntity\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/scaleEntity\"\r\n"
            		+ "    },\r\n"
            		+ "    \"codingNote\": \"http://id.who.int/icd/schema/codingNote\",\r\n"
            		+ "    \"codeRange\": \"http://id.who.int/icd/schema/codingRange\",\r\n"
            		+ "    \"blockId\": \"http://id.who.int/icd/schema/blockId\",\r\n"
            		+ "    \"foundationReference\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/foundationReference\"\r\n"
            		+ "    },\r\n"
            		+ "    \"linearizationReference\": {\r\n"
            		+ "        \"@type\" : \"@id\",\r\n"
            		+ "        \"@id\" : \"http://id.who.int/icd/schema/linearizationReference\"\r\n"
            		+ "    },\r\n"
            		+ "    \"label\" : \"http://www.w3.org/2008/05/skos-xl#literalForm\"\r\n"
            		+ "  }\r\n"
            		+ "}";
            String contextForMultiVersion = "{\r\n"
            		+ "  \"@context\": {\r\n"
            		+ "    \"title\": \"http://www.w3.org/2004/02/skos/core#prefLabel\",\r\n"
            		+ "    \"latestVersion\": {\r\n"
            		+ "    	\"@type\" : \"@id\",\r\n"
            		+ "    	\"@id\" : \"http://id.who.int/icd/schema/latestVersion\"\r\n"
            		+ "    },\r\n"
            		+ "    \"version\": {\r\n"
            		+ "    	\"@type\" : \"@id\",\r\n"
            		+ "    	\"@id\" : \"http://id.who.int/icd/schema/version\"\r\n"
            		+ "    },\r\n"
            		+ "    \"latestRelease\": {\r\n"
            		+ "    	\"@type\" : \"@id\",\r\n"
            		+ "    	\"@id\" : \"http://id.who.int/icd/schema/latestRelease\"\r\n"
            		+ "    },\r\n"
            		+ "    \"release\": {\r\n"
            		+ "    	\"@type\" : \"@id\",\r\n"
            		+ "    	\"@id\" : \"http://id.who.int/icd/schema/release\"\r\n"
            		+ "    }\r\n"
            		+ "  }\r\n"
            		+ "}";
            
            dl.addInjectedDoc("http://id.who.int/icd/contexts/contextForTopLevel.json",  contextForTopLevel2);
//            dl.addInjectedDoc("http://id.who.int/icd/contexts/contextForICD10Entity.json",  contextForICD10Entity);
            dl.addInjectedDoc("http://id.who.int/icd/contexts/contextForFoundationEntity.json",  contextForFoundationEntity2);
            dl.addInjectedDoc("http://id.who.int/icd/contexts/contextForLinearizationEntity.json",  contextForLinearizationEntity2);
//            dl.addInjectedDoc("http://id.who.int/icd/contexts/contextForMultiVersionEntity.json",  contextForMultiVersion);
            options.setDocumentLoader(dl);
            
            
            JsonLdProcessor.toRDF(jsonObject, callback, options) ;
        }
        catch (JsonLdError e) {
            errorHandler.error(e.getMessage(), -1, -1); 
            throw new RiotException(e) ;
        }
        output.finish() ;
    }

    public static String LITERAL    = "literal" ;
    public static String BLANK_NODE = "blank node" ;
    public static String IRI        = "IRI" ;

    private Node createNode(Map<String, Object> tripleMap, String key) {
        @SuppressWarnings("unchecked")
        Map<String, Object> x = (Map<String, Object>)(tripleMap.get(key)) ;
        return createNode(x) ;
    }

    private static final String xsdString = XSDDatatype.XSDstring.getURI() ;
    
    private Node createNode(Map<String, Object> map) {
        String type = (String)map.get("type") ;
        String lex = (String)map.get("value") ;
        if ( type.equals(IRI) )
            return createURI(lex) ;
        else if ( type.equals(BLANK_NODE) )
            return createBlankNode(lex);
        else if ( type.equals(LITERAL) ) {
            String lang = (String)map.get("language") ;
            String datatype = (String)map.get("datatype") ;
            if ( Objects.equals(xsdString, datatype) )
                // In RDF 1.1, simple literals and xsd:string are the same.
                // During migration, we prefer simple literals to xsd:strings. 
                datatype = null ;
            if ( lang == null && datatype == null )
                return profile.createStringLiteral(lex,-1, -1) ;
            if ( lang != null )
                return profile.createLangLiteral(lex, lang, -1, -1) ;
            RDFDatatype dt = NodeFactory.getType(datatype) ;
            return profile.createTypedLiteral(lex, dt, -1, -1) ;
        } else
            throw new InternalErrorException("Node is not a IRI, bNode or a literal: " + type) ;
    }

    private Node createBlankNode(String str) {
        if ( str.startsWith("_:") )
            str = str.substring(2);
        return profile.createBlankNode(null, str, -1,-1);
    }

    private Node createURI(String str) {
        if ( str.startsWith("_:") )
            return createBlankNode(str);
        else
            return profile.createURI(str, -1, -1) ;
    }
}
