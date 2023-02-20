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

public class GetDentIncompatible {

	public List<String> getDentIncompatibleListe() {
		List<String> dentIncompatible = new ArrayList<String>();

		String SQL_SELECT = "SELECT * FROM public.r_activite_phase_dent";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				String aap_cod = resultSet.getString("aap_cod");
				Integer den_numero = resultSet.getInt("den_numero");

				dentIncompatible.add(aap_cod+";"+den_numero);
				

			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dentIncompatible;
	}

}
