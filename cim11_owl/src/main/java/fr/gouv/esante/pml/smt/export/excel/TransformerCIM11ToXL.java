package fr.gouv.esante.pml.smt.export.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import fr.gouv.esante.pml.smt.utils.PropertiesUtil;


public class TransformerCIM11ToXL {
  
  
  private static String owlFileName = PropertiesUtil.getProperties("cim11OWL");
  private static SortedMap<String, String> labeFrListe = new TreeMap<String, String>();
  private static SortedMap<String, String> labeEnListe = new TreeMap<String, String>();
  private static SortedMap<String, String> notationListe = new TreeMap<String, String>();
  private static SortedMap<String, String> definitionFRListe = new TreeMap<String, String>();
  private static SortedMap<String, String> definitionEnListe = new TreeMap<String, String>();
  private static SortedMap<String, String> typeListe = new TreeMap<String, String>();
  private static SortedMap<String, String> indexTermFrListe = new TreeMap<String, String>();
  private static SortedMap<String, String> indexTermEnListe = new TreeMap<String, String>();
  private static SortedMap<String, String> scaleEntityListe = new TreeMap<String, String>();
  private static SortedMap<String, String> browserUrlListe = new TreeMap<String, String>();
  
  private static SortedMap<String, String> classKindListe = new TreeMap<String, String>();
  private static SortedMap<String, String> codeListe = new TreeMap<String, String>();
  private static SortedMap<String, String> foundationChildListe = new TreeMap<String, String>();
  private static SortedMap<String, String> exclusionListe = new TreeMap<String, String>();
  
  private static SortedMap<String, String> releaseListe = new TreeMap<String, String>();
  private static SortedMap<String, String> sourceListe = new TreeMap<String, String>();
  private static SortedMap<String, String> icd10Liste = new TreeMap<String, String>();
  private static SortedMap<String, String> fullySpecifiedNameFrListe = new TreeMap<String, String>();
  private static SortedMap<String, String> fullySpecifiedNameEnListe = new TreeMap<String, String>();
  private static SortedMap<String, String> postcoordinationScaleListe = new TreeMap<String, String>();
  private static SortedMap<String, String> PostCoordinationSourceEntityListe = new TreeMap<String, String>();
  
  private static SortedMap<String, String> allowMultipleValuesListe = new TreeMap<String, String>();
  private static SortedMap<String, String> axisNameListe = new TreeMap<String, String>();












 
 
  static InputStream input1 = null;
  
  public static void main(final String[] args) throws Exception {
    
    input1 = new FileInputStream(owlFileName);
     
    final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLOntology onto = manager.loadOntologyFromOntologyDocument(input1);
    
    onto.axioms().filter(ax -> ax.isOfType(AxiomType.ANNOTATION_ASSERTION)).
    filter(ax -> ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label") || 
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#notation>") ||
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#definition>") ||
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://purl.org/dc/elements/1.1/type>") ||
            
            
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/classKind>") ||
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/code>") ||
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/release>") ||
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/source>") ||
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/fullySpecifiedName>") ||
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/postcoordinationScale>") ||
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#PostCoordinationSourceEntity>") ||
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/allowMultipleValues>") ||
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/axisName>") ||




            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#indexTerm>") ||
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/scaleEntity>") ||
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/browserUrl>") ||
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#foundationChild>") ||
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#exclusion>") ||
            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#icd10>") ||






            ((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#altLabel>")).

   
    
  

    
    forEach(ax -> {
    	if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("rdfs:label")) {
    	

    		String rawValue = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
    		
    		if (rawValue.endsWith("@fr")) {
    			
        		String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString() ;

    			
    			String label = rawValue.substring(1, rawValue.lastIndexOf("\""));

    			
    			labeFrListe.put(code, label);
        		
    			
    		}
    		
            if (rawValue.endsWith("@en")) {
    			
        		String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString() ;

    			
    			String label = rawValue.substring(1, rawValue.lastIndexOf("\""));

    			
    			labeEnListe.put(code, label);
        		
    			
    		}
    		
		} else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#notation>")){
			String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
			OWLLiteral literal = (OWLLiteral) ((OWLAnnotationAssertionAxiom) ax).getValue();
			String notation = literal.getLiteral();
			notationListe.put(code, notation);
		}
    	
		else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/classKind>")){
			String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
			OWLLiteral literal = (OWLLiteral) ((OWLAnnotationAssertionAxiom) ax).getValue();
			String classkind = literal.getLiteral();
			classKindListe.put(code, classkind);
		}
		else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/allowMultipleValues>")){
			String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
			OWLLiteral literal = (OWLLiteral) ((OWLAnnotationAssertionAxiom) ax).getValue();
			String allowMultipleValues = literal.getLiteral();
			allowMultipleValuesListe.put(code, allowMultipleValues);
		}
    	
		else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/code>")){
			String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
			OWLLiteral literal = (OWLLiteral) ((OWLAnnotationAssertionAxiom) ax).getValue();
			String icd_code = literal.getLiteral();
			codeListe.put(code, icd_code);
		}
    	
    	
		 else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/scaleEntity>")){
			    String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
				String scaleEntity = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
				String scale;
				if(scaleEntityListe.containsKey(code)) {
					scale =  scaleEntityListe.get(code) + "; " + scaleEntity;
					scaleEntityListe.put(code, scale);
				}else {
					scaleEntityListe.put(code, scaleEntity);
				}
			}
    	
		 else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/axisName>")){
			    String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
				String axisName = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
				String axis;
				if(axisNameListe.containsKey(code)) {
					axis =  axisNameListe.get(code) + "; " + axisName;
					axisNameListe.put(code, axis);
				}else {
					axisNameListe.put(code, axisName);
				}
			}
    	
		 else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/postcoordinationScale>")){
			    String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
				String postcoordinationScale = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
				String postcoordination;
				if(postcoordinationScaleListe.containsKey(code)) {
					postcoordination =  postcoordinationScaleListe.get(code) + "; " + postcoordinationScale;
					postcoordinationScaleListe.put(code, postcoordination);
				}else {
					postcoordinationScaleListe.put(code, postcoordinationScale);
				}
			}
    	
		 else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#PostCoordinationSourceEntity>")){
			    String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
				String PostCoordinationSourceEntity = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
				String PostCoordEntity;
				if(PostCoordinationSourceEntityListe.containsKey(code)) {
					PostCoordEntity =  PostCoordinationSourceEntityListe.get(code) + "; " + PostCoordinationSourceEntity;
					PostCoordinationSourceEntityListe.put(code, PostCoordEntity);
				}else {
					PostCoordinationSourceEntityListe.put(code, PostCoordinationSourceEntity);
				}
			}
    	
		 else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#foundationChild>")){
			    String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
				String foundationChild = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
				String foundation;
				if(foundationChildListe.containsKey(code)) {
					foundation =  foundationChildListe.get(code) + "; " + foundationChild;
					foundationChildListe.put(code, foundation);
				}else {
					foundationChildListe.put(code, foundationChild);
				}
			}
    	
		 else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#icd10>")){
			    String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
				String icd10 = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
				String icd;
				if(icd10Liste.containsKey(code)) {
					icd =  icd10Liste.get(code) + "; " + icd10;
					icd10Liste.put(code, icd);
				}else {
					icd10Liste.put(code, icd10);
				}
			}
    	
    	   
    	
		 else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#exclusion>")){
			    String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
				String exclusion = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
				String excl;
				if(exclusionListe.containsKey(code)) {
					excl =  exclusionListe.get(code) + "; " + exclusion;
					exclusionListe.put(code, excl);
				}else {
					exclusionListe.put(code, exclusion);
				}
			}
    	
		 else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/release>")){
			    String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
				String release = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
				String reles;
				if(releaseListe.containsKey(code)) {
					reles =  releaseListe.get(code) + "; " + release;
					releaseListe.put(code, reles);
				}else {
					releaseListe.put(code, release);
				}
			}
    	
		 else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/source>")){
			    String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
				String source = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
				String src;
				if(sourceListe.containsKey(code)) {
					src =  sourceListe.get(code) + "; " + source;
					sourceListe.put(code, src);
				}else {
					sourceListe.put(code, source);
				}
			}
    	
		 else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/browserUrl>")){
			    String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
				String browserUrl = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
				String browser;
				if(browserUrlListe.containsKey(code)) {
					browser =  browserUrlListe.get(code) + "; " + browserUrl;
					browserUrlListe.put(code, browser);
				}else {
					browserUrlListe.put(code, browserUrl);
				}
			}
		 else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://purl.org/dc/elements/1.1/type>")){
				String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();
				OWLLiteral literal = (OWLLiteral) ((OWLAnnotationAssertionAxiom) ax).getValue();
				String type = literal.getLiteral();
				typeListe.put(code, type);
		}
		 
		else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://www.w3.org/2004/02/skos/core#definition>")){
			
    		String rawValue = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
			String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();

            
    		if (rawValue.endsWith("@fr")) {
    			
    			OWLLiteral literal = (OWLLiteral) ((OWLAnnotationAssertionAxiom) ax).getValue();
    			String definition = literal.getLiteral();

    			
    			definitionFRListe.put(code, definition);
        		
    			
    		}
    		
            if (rawValue.endsWith("@en")) {
    			

    			
        		OWLLiteral literal = (OWLLiteral) ((OWLAnnotationAssertionAxiom) ax).getValue();
    			String definition = literal.getLiteral();

    			
    			definitionEnListe.put(code, definition);
        		
    			
    		}
			
		
		}
    	
    	
	else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://data.esante.gouv.fr/ans-icd11#indexTerm>")){
			
    		String rawValue = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
			String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();

            
    		if (rawValue.endsWith("@fr")) {
    			
    			OWLLiteral literal = (OWLLiteral) ((OWLAnnotationAssertionAxiom) ax).getValue();
    			String indexTerm = literal.getLiteral();

    			
    			indexTermFrListe.put(code, indexTerm);
        		
    			
    		}
    		
            if (rawValue.endsWith("@en")) {
    			

    			
        		OWLLiteral literal = (OWLLiteral) ((OWLAnnotationAssertionAxiom) ax).getValue();
    			String indexTerm = literal.getLiteral();

    			
    			indexTermEnListe.put(code, indexTerm);
        		
    			
    		}
			
		
		}
    	
	else if(((OWLAnnotationAssertionAxiom) ax).getProperty().toString().equals("<http://id.who.int/icd/schema/fullySpecifiedName>")){
		
		String rawValue = ((OWLAnnotationAssertionAxiom) ax).getValue().toString();
		String code = ((OWLAnnotationAssertionAxiom) ax).getSubject().toString();

        
		if (rawValue.endsWith("@fr")) {
			
			OWLLiteral literal = (OWLLiteral) ((OWLAnnotationAssertionAxiom) ax).getValue();
			String indexTerm = literal.getLiteral();

			
			fullySpecifiedNameFrListe.put(code, indexTerm);
    		
			
		}
		
        if (rawValue.endsWith("@en")) {
			

			
    		OWLLiteral literal = (OWLLiteral) ((OWLAnnotationAssertionAxiom) ax).getValue();
			String indexTerm = literal.getLiteral();

			
			fullySpecifiedNameEnListe.put(code, indexTerm);
    		
			
		}
		
	
	}
					
    	
    });
    
	

    
    
    FileOutputStream out = new FileOutputStream(new File("D:\\cim11\\mms\\2026\\cim11_2026.xlsx"));
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("CIM11");
    int rownum=0;
    Row arow = sheet.createRow(rownum);
    Cell acell1 = arow.createCell(0);
    Cell acell2 = arow.createCell(1);
    Cell acell3 = arow.createCell(2);
    Cell acell4 = arow.createCell(3);
    Cell acell5 = arow.createCell(4);
    Cell acell6 = arow.createCell(5);
    Cell acell7 = arow.createCell(6);
    Cell acell8 = arow.createCell(7);
    Cell acell9 = arow.createCell(8);
    Cell acell10 = arow.createCell(9);
    Cell acell11 = arow.createCell(10);
    Cell acell12 = arow.createCell(11);
    Cell acell13 = arow.createCell(12);
    Cell acell14 = arow.createCell(13);
    Cell acell15 = arow.createCell(14);
    Cell acell16 = arow.createCell(15);
    Cell acell17 = arow.createCell(16);
    Cell acell18 = arow.createCell(17);
    Cell acell19 = arow.createCell(18);
    Cell acell20 = arow.createCell(19);
    Cell acell21 = arow.createCell(20);
    Cell acell22 = arow.createCell(21);
    Cell acell23 = arow.createCell(22);
    Cell acell24 = arow.createCell(23);




	




 
    acell1.setCellValue("URI");
    acell2.setCellValue("Libellé Francais");
    acell3.setCellValue("Libellé Anglais");
    acell4.setCellValue("Notation");
    acell5.setCellValue("Définition_FR");
    acell6.setCellValue("Définition_EN");
    acell7.setCellValue("Type");
    acell8.setCellValue("indexTermFr");
    acell9.setCellValue("indexTermEn");
    acell10.setCellValue("sacleEntity");
    acell11.setCellValue("browserUrl");
    
    acell12.setCellValue("classKind");
    acell13.setCellValue("icd_code");
    acell14.setCellValue("foundationChild");
    acell15.setCellValue("exclusion");
    acell16.setCellValue("source");
    acell17.setCellValue("release");
    acell18.setCellValue("icd-10");
    acell19.setCellValue("fullySpecifiedNameFr");
    acell20.setCellValue("fullySpecifiedNameEn");
    acell21.setCellValue("postcoordinationScale");
    acell22.setCellValue("PostCoordinationSourceEntity");
    acell23.setCellValue("allowMultipleValues");
    acell24.setCellValue("axisName");




	



   
    

    
    for(String code : labeFrListe.keySet()) {
    	rownum++;
    	Row row = sheet.createRow(rownum);
        Cell cell1 = row.createCell(0);
        Cell cell2 = row.createCell(1);
        Cell cell3 = row.createCell(2);
        Cell cell4 = row.createCell(3);
        Cell cell5 = row.createCell(4);
        Cell cell6 = row.createCell(5);
        Cell cell7 = row.createCell(6);
        Cell cell8 = row.createCell(7);
        Cell cell9 = row.createCell(8);
        Cell cell10 = row.createCell(9);
        Cell cell11 = row.createCell(10);
        Cell cell12 = row.createCell(11);
        Cell cell13 = row.createCell(12);
        Cell cell14 = row.createCell(13);
        Cell cell15 = row.createCell(14);
        Cell cell16 = row.createCell(15);
        Cell cell17 = row.createCell(16);
        Cell cell18 = row.createCell(17);
        Cell cell19 = row.createCell(18);
        Cell cell20 = row.createCell(19);
        Cell cell21 = row.createCell(20);
        Cell cell22 = row.createCell(21);
        Cell cell23 = row.createCell(22);
        Cell cell24 = row.createCell(23);




        
		


        
        
        cell1.setCellValue(code); // Code
        
       // if(superListe.containsKey(code)) {
    	//	cell2.setCellValue(superListe.get(code)); //Parent immédiat
    	//}
        
        cell2.setCellValue(labeFrListe.get(code)); //Label Francais
        
        cell3.setCellValue(labeEnListe.get(code)); //Label Anglais
        
        cell4.setCellValue(notationListe.get(code)); //Label Anglais
        
        cell5.setCellValue(definitionFRListe.get(code)); //Label Anglais
        
        cell6.setCellValue(definitionEnListe.get(code)); //Label Anglais
        
        cell7.setCellValue(typeListe.get(code)); //Category
        
        cell8.setCellValue(indexTermFrListe.get(code)); //indexTermFr

        cell9.setCellValue(indexTermEnListe.get(code)); //indexTermEn
        
        cell10.setCellValue(scaleEntityListe.get(code)); //scaleEntity
        
        cell11.setCellValue(browserUrlListe.get(code)); //browserUrl
        
        cell12.setCellValue(classKindListe.get(code)); //classKind
        
        cell13.setCellValue(codeListe.get(code)); //icd_code
        
        cell14.setCellValue(foundationChildListe.get(code)); //foundationChild
        
        cell15.setCellValue(exclusionListe.get(code)); //exclusion
        
        cell16.setCellValue(sourceListe.get(code)); //source

        cell17.setCellValue(releaseListe.get(code)); //release
        
        cell18.setCellValue(icd10Liste.get(code)); //icd-10
        
        cell19.setCellValue(fullySpecifiedNameFrListe.get(code)); //fullySpecifiedNameFr
        
        cell20.setCellValue(fullySpecifiedNameEnListe.get(code)); //fullySpecifiedNameEn
        
        cell21.setCellValue(postcoordinationScaleListe.get(code)); //postcoordinationScale
        
        cell22.setCellValue(PostCoordinationSourceEntityListe.get(code)); //PostCoordinationSourceEntity
        
        cell23.setCellValue(allowMultipleValuesListe.get(code)); //allowMultipleValues

        cell24.setCellValue(axisNameListe.get(code));








        
		
    	
    } 
    
    workbook.write(out);
    out.close();
    workbook.close();
  }

}
