package fr.gouv.esante.pml.smt.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

  public static String getProperties(final String propertieName) {

    String versionString = null;

    // to load application's properties, we use this class
    final Properties mainProperties = new Properties();

    FileInputStream file;

    // the base folder is ./, the root of the main.properties file
    final String path = "./configuration.properties";

    // load the file handle for main.properties
    try {
      file = new FileInputStream(path);
      // load all the properties from this file
      mainProperties.load(file);
      // retrieve the property we are intrested, the app.version
      versionString = mainProperties.getProperty(propertieName);
      // we have loaded the properties, so close the file handle
      file.close();
    } catch (final FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }



    return versionString;
  }
  
  
  public static String getDbProperties(final String propertieName) {

	    String versionString = null;

	    // to load application's properties, we use this class
	    final Properties mainProperties = new Properties();

	    FileInputStream file;

	    // the base folder is ./, the root of the main.properties file
	    final String path = "./database.properties";

	    // load the file handle for main.properties
	    try {
	      file = new FileInputStream(path);
	      // load all the properties from this file
	      mainProperties.load(file);
	      // retrieve the property we are intrested, the app.version
	      versionString = mainProperties.getProperty(propertieName);
	      // we have loaded the properties, so close the file handle
	      file.close();
	    } catch (final FileNotFoundException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    } catch (final IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }



	    return versionString;
	  }
  
  public static String getCCAMProperties(final String propertieName) {

	    String versionString = null;

	    // to load application's properties, we use this class
	    final Properties mainProperties = new Properties();

	    FileInputStream file;

	    // the base folder is ./, the root of the main.properties file
	    final String path = "./ccam.properties";

	    // load the file handle for main.properties
	    try {
	      file = new FileInputStream(path);
	      // load all the properties from this file
	      mainProperties.load(file);
	      // retrieve the property we are intrested, the app.version
	      versionString = mainProperties.getProperty(propertieName);
	      // we have loaded the properties, so close the file handle
	      file.close();
	    } catch (final FileNotFoundException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    } catch (final IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }



	    return versionString;
	  }
  
  public static String getEMDNProperties(final String propertieName) {

	    String versionString = null;

	    // to load application's properties, we use this class
	    final Properties mainProperties = new Properties();

	    FileInputStream file;

	    // the base folder is ./, the root of the main.properties file
	    final String path = "./emdn.properties";

	    // load the file handle for main.properties
	    try {
	      file = new FileInputStream(path);
	      // load all the properties from this file
	      mainProperties.load(file);
	      // retrieve the property we are intrested, the app.version
	      versionString = mainProperties.getProperty(propertieName);
	      // we have loaded the properties, so close the file handle
	      file.close();
	    } catch (final FileNotFoundException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    } catch (final IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }



	    return versionString;
	  }
  
  public static String getATCProperties(final String propertieName) {

	    String versionString = null;

	    // to load application's properties, we use this class
	    final Properties mainProperties = new Properties();

	    FileInputStream file;

	    // the base folder is ./, the root of the main.properties file
	    final String path = "./atc.properties";

	    // load the file handle for main.properties
	    try {
	      file = new FileInputStream(path);
	      // load all the properties from this file
	      mainProperties.load(file);
	      // retrieve the property we are intrested, the app.version
	      versionString = mainProperties.getProperty(propertieName);
	      // we have loaded the properties, so close the file handle
	      file.close();
	    } catch (final FileNotFoundException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    } catch (final IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }



	    return versionString;
	  }
      
  public static String getEMAProperties(final String propertieName) {

	    String versionString = null;

	    // to load application's properties, we use this class
	    final Properties mainProperties = new Properties();

	    FileInputStream file;

	    // the base folder is ./, the root of the main.properties file
	    final String path = "./ema.properties";

	    // load the file handle for main.properties
	    try {
	      file = new FileInputStream(path);
	      // load all the properties from this file
	      mainProperties.load(file);
	      // retrieve the property we are intrested, the app.version
	      versionString = mainProperties.getProperty(propertieName);
	      // we have loaded the properties, so close the file handle
	      file.close();
	    } catch (final FileNotFoundException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    } catch (final IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }



	    return versionString;
	  }
  
  
  public static String getNCBIProperties(final String propertieName) {

	    String versionString = null;

	    // to load application's properties, we use this class
	    final Properties mainProperties = new Properties();

	    FileInputStream file;

	    // the base folder is ./, the root of the main.properties file
	    final String path = "./ncbi.properties";

	    // load the file handle for main.properties
	    try {
	      file = new FileInputStream(path);
	      // load all the properties from this file
	      mainProperties.load(file);
	      // retrieve the property we are intrested, the app.version
	      versionString = mainProperties.getProperty(propertieName);
	      // we have loaded the properties, so close the file handle
	      file.close();
	    } catch (final FileNotFoundException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    } catch (final IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }



	    return versionString;
	  }
  
  
  public static String getEdqmProperties(final String propertieName) {

	    String versionString = null;

	    // to load application's properties, we use this class
	    final Properties mainProperties = new Properties();

	    FileInputStream file;

	    // the base folder is ./, the root of the main.properties file
	    final String path = "./edqm.properties";

	    // load the file handle for main.properties
	    try {
	      file = new FileInputStream(path);
	      // load all the properties from this file
	      mainProperties.load(file);
	      // retrieve the property we are intrested, the app.version
	      versionString = mainProperties.getProperty(propertieName);
	      // we have loaded the properties, so close the file handle
	      file.close();
	    } catch (final FileNotFoundException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    } catch (final IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }



	    return versionString;
	  }
  

}
