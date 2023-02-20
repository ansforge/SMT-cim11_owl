package fr.gouv.esante.pml.smt.ccam.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import fr.gouv.esante.pml.smt.ccam.models.Menu;

public class GetActivite {

	public Map<String, String> getActiviteListe() {
		Map<String, String> activiteListe = new HashMap<String, String>();

		String SQL_SELECT = "SELECT * FROM public.r_activite";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				String cod_activ = resultSet.getString("cod_activ");
				String label = resultSet.getString("libelle");
				
				activiteListe.put(cod_activ, label);

			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activiteListe;
	}

}
