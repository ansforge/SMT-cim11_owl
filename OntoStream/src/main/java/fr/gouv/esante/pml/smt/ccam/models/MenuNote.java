package fr.gouv.esante.pml.smt.ccam.models;

import java.util.SortedMap;
import java.util.TreeMap;

public class MenuNote {
	
	private Integer id_menu_note;
	private SortedMap<Integer, Note> notes = new TreeMap<Integer, Note>();
	/**
	 * @param id_menu_note
	 * @param notes
	 */
	public MenuNote(Integer id_menu_note, SortedMap<Integer, Note> notes) {
		super();
		this.id_menu_note = id_menu_note;
		this.notes = notes;
	}
	/**
	 * @return the id_menu_note
	 */
	public Integer getId_menu_note() {
		return id_menu_note;
	}
	/**
	 * @param id_menu_note the id_menu_note to set
	 */
	public void setId_menu_note(Integer id_menu_note) {
		this.id_menu_note = id_menu_note;
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
