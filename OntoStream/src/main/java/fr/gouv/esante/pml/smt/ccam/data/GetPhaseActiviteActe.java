package fr.gouv.esante.pml.smt.ccam.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import fr.gouv.esante.pml.smt.ccam.models.Menu;

public class GetPhaseActiviteActe {

	public Map<String, String> getPhaseActiviteActeListe() {
		Map<String, String> phaseActiviteActeListe = new HashMap<String, String>();

		String SQL_SELECT = "SELECT * FROM public.r_acte_ivite_phase order by dt_modif";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				String cod_aap = resultSet.getString("cod_aap");
				String aa_cod = resultSet.getString("aa_cod");
				String phase_cod = resultSet.getString("phase_cod");
				phaseActiviteActeListe.put(cod_aap, aa_cod + ";" + phase_cod);

			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phaseActiviteActeListe;
	}

}
