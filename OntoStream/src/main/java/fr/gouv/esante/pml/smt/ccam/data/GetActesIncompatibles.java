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
import fr.gouv.esante.pml.smt.ccam.models.Menu;

public class GetActesIncompatibles {

	public List<String> getActeIncompatibleListe() {
		List<String> actesIncompatibles = new ArrayList<String>();

		String SQL_SELECT = "SELECT * FROM public.r_incompatibilites order by dt_modif1 desc";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				String cod_acte1 = resultSet.getString("acte_cod1");
				String cod_acte2 = resultSet.getString("acte_cod2");

				actesIncompatibles.add(cod_acte1+";"+cod_acte2);
				

			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actesIncompatibles;
	}

}
