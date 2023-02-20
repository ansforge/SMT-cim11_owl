package fr.gouv.esante.pml.smt.utils;


import java.io.IOException;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


public class ClientNCBIHttp {
	
	

		
		public static int checkConceptId(String conceptid, String terminologyId) throws ClientProtocolException, IOException  {
		
		
			
		  HttpClient client = new HttpClient();
	      HttpMethod method = new GetMethod("https://smt.esante.gouv.fr/api/concepts/detail?terminologyId="+terminologyId+"&conceptId="
			+conceptid);
		
		
	      
		 
		  HostConfiguration config = client.getHostConfiguration();
	      
		  if("yes".equals(PropertiesUtil.getProperties("activeProxy"))) {
		     config.setProxy("grpxy-vip.grita.fr", 3128);
		  }

	      
	      client.executeMethod(method);
	      
	   
        
      return method.getStatusCode();
		
	
	}

}
