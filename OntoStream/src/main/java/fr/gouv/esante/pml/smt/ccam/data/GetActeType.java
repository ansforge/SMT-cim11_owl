package fr.gouv.esante.pml.smt.ccam.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import fr.gouv.esante.pml.smt.ccam.models.Menu;

public class GetActeType {

	public Map<Integer, String> getActeTypeListe() {
		Map<Integer, String> acteTypeListe = new HashMap<Integer, String>();

		String SQL_SELECT = "SELECT * FROM public.r_type";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				Integer cod_type = resultSet.getInt("cod_type");
				String label = resultSet.getString("libelle");
				
				acteTypeListe.put(cod_type, label);

			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return acteTypeListe;
	}

}
