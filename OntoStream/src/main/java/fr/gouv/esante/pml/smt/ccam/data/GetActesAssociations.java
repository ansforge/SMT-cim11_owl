package fr.gouv.esante.pml.smt.ccam.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.gouv.esante.pml.smt.ccam.models.Acte;
import fr.gouv.esante.pml.smt.ccam.models.ActeActivite;
import fr.gouv.esante.pml.smt.ccam.models.Association;
import fr.gouv.esante.pml.smt.ccam.models.Menu;

public class GetActesAssociations {

	public List<Association> getActeAssociationListe() {
		List<Association> actesAssociations = new ArrayList<Association>();

		String SQL_SELECT = "SELECT * FROM public.r_associations";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				String cod_acte1 = resultSet.getString("aa_code1");
				String cod_acte2 = resultSet.getString("aa_code2");
				Date dt_modif1 = resultSet.getDate("dt_modif1");
				Date dt_modif2 = resultSet.getDate("dt_modif2");
				
				Association association = new Association(cod_acte1, cod_acte2, dt_modif1, dt_modif2);
				
				actesAssociations.add(association);
				

			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actesAssociations;
	}

}
