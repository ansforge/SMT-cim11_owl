package fr.gouv.esante.pml.smt.ccam.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import fr.gouv.esante.pml.smt.ccam.models.Acte;
import fr.gouv.esante.pml.smt.ccam.models.Menu;
import fr.gouv.esante.pml.smt.ccam.models.Modificateur;

public class GetModificateur {

	public Map<String, Modificateur> getModificateurListe() {
		Map<String, Modificateur> modificateurListe = new HashMap<String, Modificateur>();

		String SQL_SELECT = "SELECT * FROM public.r_tb11 order by dt_debut desc";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				String cod_modifi = resultSet.getString("cod_modifi");
				String label = resultSet.getString("libelle");
				Double coef = resultSet.getDouble("coef");
				Double forfait = resultSet.getDouble("forfait");

				if(!modificateurListe.containsKey(cod_modifi)) {
					Modificateur modificateur = new Modificateur(cod_modifi, label, coef,forfait);
					modificateurListe.put(cod_modifi, modificateur);
				}
				

			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modificateurListe;
	}

}
