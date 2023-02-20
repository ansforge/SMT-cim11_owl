package fr.gouv.esante.pml.smt.ccam.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;

public class DBconnexion {
	
	String url = PropertiesUtil.getDbProperties("db.url");
    String user = PropertiesUtil.getDbProperties("db.user");
    String password = PropertiesUtil.getDbProperties("db.passwd");
	
	
	 public Connection connect() {
	        Connection conn = null;
	        try {
	            conn = DriverManager.getConnection(url, user, password);
//	            System.out.println("Connected to the PostgreSQL server successfully.");
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }

	        return conn;
	    }
	 public static void main(String[] args) {
		 DBconnexion app = new DBconnexion();
	        app.connect();
	    }
}
