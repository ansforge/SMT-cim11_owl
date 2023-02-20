package fr.gouv.esante.pml.smt.ccam.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import fr.gouv.esante.pml.smt.ccam.models.Menu;

public class GetDent {

	public Map<Integer, String> getDentListe() {
		Map<Integer, String> dentListe = new HashMap<Integer, String>();

		String SQL_SELECT = "SELECT * FROM public.r_dent";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				Integer numero_den = resultSet.getInt("numero_den");
				String libelle = resultSet.getString("libelle");
				
				dentListe.put(numero_den, libelle);

			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dentListe;
	}

}
