package fr.gouv.esante.pml.smt.ccam.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import fr.gouv.esante.pml.smt.ccam.models.MenuNote;
import fr.gouv.esante.pml.smt.ccam.models.Note;

public class GetMenuNote {

	public Map<Integer, MenuNote> getMenuNoteListe() {
		Map<Integer, MenuNote> notesListe = new HashMap<Integer, MenuNote>();

		String SQL_SELECT = "SELECT * FROM public.r_note_menu order by ordre_note";
		DBconnexion app = new DBconnexion();

		// auto close connection and preparedStatement
		try (PreparedStatement preparedStatement = app.connect().prepareStatement(SQL_SELECT)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {

				Integer menu_cod = resultSet.getInt("menu_cod");
				String note = resultSet.getString("texte_note");
				String note0 = resultSet.getString("texte_not0");
				String note1 = resultSet.getString("texte_not1");
				String note2 = resultSet.getString("texte_not2");
				String note3 = resultSet.getString("texte_not3");
				String note4 = resultSet.getString("texte_not4");
				String note5 = resultSet.getString("texte_not5");
				String note6 = resultSet.getString("texte_not6");
				String note7 = resultSet.getString("texte_not7");
				Integer ordre_note = resultSet.getInt("ordre_note");
				Integer typnot_cod = resultSet.getInt("typnot_cod");
				
				MenuNote menuNote;
				
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
				if(note5 != null && !note5.isEmpty()) {
					note += note5;
				}
				if(note6 != null && !note6.isEmpty()) {
					note += note6;
				}
				if(note7 != null && !note7.isEmpty()) {
					note += note7;
				}


				if(notesListe.containsKey(menu_cod)) {
					menuNote = notesListe.get(menu_cod);
					SortedMap<Integer, Note> notes = menuNote.getNotes();
					Note nt = new Note(ordre_note, typnot_cod, note);
					notes.put(ordre_note, nt);
					menuNote.setNotes(notes);
				}else {
					SortedMap<Integer, Note> notes = new TreeMap<Integer, Note>();
					Note nt = new Note(ordre_note, typnot_cod, note);
					notes.put(ordre_note, nt);
					menuNote = new MenuNote(menu_cod, notes);
				}
				
				notesListe.put(menu_cod, menuNote);
			}

		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return notesListe;
	}

}
