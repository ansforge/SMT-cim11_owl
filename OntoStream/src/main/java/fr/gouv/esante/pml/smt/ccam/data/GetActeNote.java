package fr.gouv.esante.pml.smt.ccam.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import fr.gouv.esante.pml.smt.ccam.models.ActeNote;
import fr.gouv.esante.pml.smt.ccam.models.Note;

public class GetActeNote {

	public Map<String, ActeNote> getActeNoteListe() {
		Map<String, ActeNote> notesListe = new HashMap<String, ActeNote>();

		String SQL_SELECT = "SELECT * FROM public.r_note_acte order by acdt_modif";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				String acte_cod = resultSet.getString("acte_cod");
				String note = resultSet.getString("texte_note");
				String note0 = resultSet.getString("texte_not0");
				String note1 = resultSet.getString("texte_not1");
				String note2 = resultSet.getString("texte_not2");
				String note3 = resultSet.getString("texte_not3");
				String note4 = resultSet.getString("texte_not4");
				Integer ordre_note = resultSet.getInt("ordre_note");
				Integer typnot_cod = resultSet.getInt("typnot_cod");
				Date acdt_modif = resultSet.getDate("acdt_modif");
				
				ActeNote acteNote;
				if(note0 != null && !note0.isEmpty()) {
					note += note0;
				}
				if(note1 != null && !note1.isEmpty()) {
					note += note1;
				}
				if(note2 != null && !note2.isEmpty()) {
					note += note2;
				}
				if(note3 != null && !note3.isEmpty()) {
					note += note3;
				}
				if(note4 != null && !note4.isEmpty()) {
					note += note4;
				}

				if(notesListe.containsKey(acte_cod)) {
					acteNote = notesListe.get(acte_cod);
					SortedMap<Integer, Note> notes = acteNote.getNotes();
					Note nt = new Note(ordre_note, typnot_cod, note, acdt_modif);
					notes.put(ordre_note, nt);
					acteNote.setNotes(notes);
				}else {
					SortedMap<Integer, Note> notes = new TreeMap<Integer, Note>();
					Note nt = new Note(ordre_note, typnot_cod, note, acdt_modif);
					notes.put(ordre_note, nt);
					acteNote = new ActeNote(acte_cod, notes);
				}
				
				notesListe.put(acte_cod, acteNote);
			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return notesListe;
	}

}
