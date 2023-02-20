package fr.gouv.esante.pml.smt.ccam.models;

import java.util.Date;

public class ActiviteModificateur {
	
	private String aa_code;
	private String modifi_cod;
	private Date aadt_modif;
	/**
	 * @param aa_code
	 * @param modifi_cod
	 * @param aadt_modif
	 */
	public ActiviteModificateur(String aa_code, String modifi_cod, Date aadt_modif) {
		super();
		this.aa_code = aa_code;
		this.modifi_cod = modifi_cod;
		this.aadt_modif = aadt_modif;
	}
	/**
	 * @return the aa_code
	 */
	public String getAa_code() {
		return aa_code;
	}
	/**
	 * @param aa_code the aa_code to set
	 */
	public void setAa_code(String aa_code) {
		this.aa_code = aa_code;
	}
	/**
	 * @return the modifi_cod
	 */
	public String getModifi_cod() {
		return modifi_cod;
	}
	/**
	 * @param modifi_cod the modifi_cod to set
	 */
	public void setModifi_cod(String modifi_cod) {
		this.modifi_cod = modifi_cod;
	}
	/**
	 * @return the aadt_modif
	 */
	public Date getAadt_modif() {
		return aadt_modif;
	}
	/**
	 * @param aadt_modif the aadt_modif to set
	 */
	public void setAadt_modif(Date aadt_modif) {
		this.aadt_modif = aadt_modif;
	}
	
	

}
