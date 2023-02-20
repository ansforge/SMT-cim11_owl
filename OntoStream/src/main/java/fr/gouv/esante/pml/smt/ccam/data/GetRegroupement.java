package fr.gouv.esante.pml.smt.ccam.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import fr.gouv.esante.pml.smt.ccam.models.Menu;

public class GetRegroupement {

	public Map<String, String> getRegroupementListe() {
		Map<String, String> regroupementListe = new HashMap<String, String>();

		String SQL_SELECT = "SELECT * FROM public.r_regroupement";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				String cod_regrou = resultSet.getString("cod_regrou");
				String label = resultSet.getString("libelle");
				
				regroupementListe.put(cod_regrou, label);

			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regroupementListe;
	}

}
