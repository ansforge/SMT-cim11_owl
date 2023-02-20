package fr.gouv.esante.pml.smt.ccam.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.gouv.esante.pml.smt.ccam.models.ActiviteModificateur;
import fr.gouv.esante.pml.smt.ccam.models.Menu;

public class GetActiviteModificateur {

	public List<ActiviteModificateur> getActiviteModificateurListe() {
		List<ActiviteModificateur> activiteModificateurListe = new ArrayList<ActiviteModificateur>();

		String SQL_SELECT = "SELECT * FROM public.r_activite_modificateur";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				String aa_code = resultSet.getString("aa_code");
				String modifi_cod = resultSet.getString("modifi_cod");
				Date aadt_modif = resultSet.getDate("aadt_modif");
				
				ActiviteModificateur activMod = new ActiviteModificateur(aa_code, modifi_cod, aadt_modif);
				
				activiteModificateurListe.add(activMod);

			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activiteModificateurListe;
	}

}
