package fr.gouv.esante.pml.smt.ccam.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.gouv.esante.pml.smt.ccam.models.ActeActivite;
import fr.gouv.esante.pml.smt.ccam.models.Menu;

public class GetActiviteActe {

	public Map<String, ActeActivite> getActiviteActeListe() {
		Map<String, ActeActivite> activiteActeListe = new HashMap<String, ActeActivite>();

		String SQL_SELECT = "SELECT * FROM public.r_acte_ivite order by acdt_modif asc";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				String cod_aa = resultSet.getString("cod_aa");
				String acte_cod = resultSet.getString("acte_cod");
				String activ_cod = resultSet.getString("activ_cod");
				String regrou_cod = resultSet.getString("regrou_cod");
				Date acdt_modif = resultSet.getDate("acdt_modif");
				
				ActeActivite acteActi = new ActeActivite(cod_aa, acte_cod, activ_cod, regrou_cod, acdt_modif);
				
				activiteActeListe.put(cod_aa, acteActi);

			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activiteActeListe;
	}

}
