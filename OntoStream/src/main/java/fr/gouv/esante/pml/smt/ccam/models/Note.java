package fr.gouv.esante.pml.smt.ccam.models;

import java.util.Date;

public class Note {
	
	private Integer ordre_note;
	private Integer typnot_cod;
	private String texte_note;
	private Date acdt_modif;
	/**
	 * @param ordre_note
	 * @param typnot_cod
	 * @param texte_note
	 */
	public Note(Integer ordre_note, Integer typnot_cod, String texte_note) {
		super();
		this.ordre_note = ordre_note;
		this.typnot_cod = typnot_cod;
		this.texte_note = texte_note;
	}
	
	/**
	 * @param ordre_note
	 * @param typnot_cod
	 * @param texte_note
	 * @param acdt_modif
	 */
	public Note(Integer ordre_note, Integer typnot_cod, String texte_note, Date acdt_modif) {
		super();
		this.ordre_note = ordre_note;
		this.typnot_cod = typnot_cod;
		this.texte_note = texte_note;
		this.acdt_modif = acdt_modif;
	}

	/**
	 * @return the ordre_note
	 */
	public Integer getOrdre_note() {
		return ordre_note;
	}
	/**
	 * @param ordre_note the ordre_note to set
	 */
	public void setOrdre_note(Integer ordre_note) {
		this.ordre_note = ordre_note;
	}
	/**
	 * @return the typnot_cod
	 */
	public Integer getTypnot_cod() {
		return typnot_cod;
	}
	/**
	 * @param typnot_cod the typnot_cod to set
	 */
	public void setTypnot_cod(Integer typnot_cod) {
		this.typnot_cod = typnot_cod;
	}
	/**
	 * @return the texte_note
	 */
	public String getTexte_note() {
		return texte_note;
	}
	/**
	 * @param texte_note the texte_note to set
	 */
	public void setTexte_note(String texte_note) {
		this.texte_note = texte_note;
	}
	/**
	 * @return the acdt_modif
	 */
	public Date getAcdt_modif() {
		return acdt_modif;
	}
	/**
	 * @param acdt_modif the acdt_modif to set
	 */
	public void setAcdt_modif(Date acdt_modif) {
		this.acdt_modif = acdt_modif;
	}
	
	

}
