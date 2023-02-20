package fr.gouv.esante.pml.smt.ccam.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import fr.gouv.esante.pml.smt.ccam.models.Menu;

public class GetPhase {

	public Map<Integer, String> getPhaseListe() {
		Map<Integer, String> phaseListe = new HashMap<Integer, String>();

		String SQL_SELECT = "SELECT * FROM public.r_phase";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				Integer cod_phase = resultSet.getInt("cod_phase");
				String label = resultSet.getString("libelle");
				
				phaseListe.put(cod_phase, label);

			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phaseListe;
	}

}
