package fr.gouv.esante.pml.smt.ccam.models;

import java.util.SortedMap;
import java.util.TreeMap;

public class ActeNote {
	
	private String id_acte_note;
	private SortedMap<Integer, Note> notes = new TreeMap<Integer, Note>();

	/**
	 * @param id_menu_note
	 * @param notes
	 */
	public ActeNote(String id_acte_note, SortedMap<Integer, Note> notes) {
		super();
		this.id_acte_note = id_acte_note;
		this.notes = notes;
	}
	/**
	 * @return the id_menu_note
	 */
	public String getId_acte_note() {
		return id_acte_note;
	}
	/**
	 * @param id_menu_note the id_menu_note to set
	 */
	public void setId_acte_note(String id_acte_note) {
		this.id_acte_note = id_acte_note;
	}
	/**
	 * @return the notes
	 */
	public SortedMap<Integer, Note> getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(SortedMap<Integer, Note> notes) {
		this.notes = notes;
	}
	
	

}
