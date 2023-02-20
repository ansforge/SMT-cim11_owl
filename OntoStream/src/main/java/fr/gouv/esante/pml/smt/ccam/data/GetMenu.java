package fr.gouv.esante.pml.smt.ccam.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import fr.gouv.esante.pml.smt.ccam.models.Menu;

public class GetMenu {

	public Map<Integer, Menu> getMenuListe() {
		Map<Integer, Menu> menuListe = new HashMap<Integer, Menu>();

		String SQL_SELECT = "SELECT * FROM public.r_menu order by cod_menu";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				Integer id_menu = resultSet.getInt("cod_menu");
				String label = resultSet.getString("libelle");
				Integer id_menu_sup = resultSet.getInt("cod_pere");
				Integer rang = resultSet.getInt("rang");

				Menu obj = new Menu(id_menu, rang, label, id_menu_sup);

				menuListe.put(id_menu, obj);

			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menuListe;
	}

}
