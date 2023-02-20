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
import fr.gouv.esante.pml.smt.ccam.models.ActeProcedure;
import fr.gouv.esante.pml.smt.ccam.models.Menu;

public class GetActesProcedures {

	public List<ActeProcedure> getActeProceduresListe() {
		List<ActeProcedure> actesProcedures = new ArrayList<ActeProcedure>();

		String SQL_SELECT = "SELECT * FROM public.r_procedure order by acdt_modif desc";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				String acte_cod = resultSet.getString("acte_cod");
				String proc_cod = resultSet.getString("proc_cod");
				Date acdt_modif = resultSet.getDate("acdt_modif");
				
				ActeProcedure actProc = new ActeProcedure(acte_cod, proc_cod, acdt_modif);

				actesProcedures.add(actProc);
				

			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actesProcedures;
	}

}
