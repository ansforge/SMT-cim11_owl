package fr.gouv.esante.pml.smt.ccam.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.gouv.esante.pml.smt.ccam.models.Acte;
import fr.gouv.esante.pml.smt.ccam.models.Menu;

public class GetActe {

	public Map<String, Acte> getActeListe() {
		Map<String, Acte> acteListe = new HashMap<String, Acte>();

		String SQL_SELECT = "SELECT * FROM public.r_acte order by dt_modif desc";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				String cod_acte = resultSet.getString("cod_acte");
				String nom_long = resultSet.getString("nom_long");
				String nom_long0 = resultSet.getString("nom_long0");
				String label = nom_long + nom_long0;
				String label1 = resultSet.getString("nom_court");
				Date dt_modif = resultSet.getDate("dt_modif");
				Date dt_effet = resultSet.getDate("dt_effet");
				Date dt_creatio = resultSet.getDate("dt_creatio");
				Integer type_cod = resultSet.getInt("type_cod");
				Integer menu_cod = resultSet.getInt("menu_cod");

				if(!acteListe.containsKey(cod_acte)) {
					Acte acte = new Acte(cod_acte, label, label1, type_cod, menu_cod, dt_creatio, dt_modif, dt_effet);
					acteListe.put(cod_acte, acte);
				}
				

			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return acteListe;
	}

}
