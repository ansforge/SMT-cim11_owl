package fr.gouv.esante.pml.smt.ccam.models;

import java.util.Date;

public class ActeActivite {
	
	private String cod_aa;
	private String acte_cod;
	private String activ_cod;
	private String regrou_cod;
	private Date acdt_modif;
	/**
	 * @param cod_aa
	 * @param acte_cod
	 * @param activ_cod
	 * @param regrou_cod
	 * @param acdt_modif
	 */
	public ActeActivite(String cod_aa, String acte_cod, String activ_cod, String regrou_cod, Date acdt_modif) {
		super();
		this.cod_aa = cod_aa;
		this.acte_cod = acte_cod;
		this.activ_cod = activ_cod;
		this.regrou_cod = regrou_cod;
		this.acdt_modif = acdt_modif;
	}
	/**
	 * @return the cod_aa
	 */
	public String getCod_aa() {
		return cod_aa;
	}
	/**
	 * @param cod_aa the cod_aa to set
	 */
	public void setCod_aa(String cod_aa) {
		this.cod_aa = cod_aa;
	}
	/**
	 * @return the acte_cod
	 */
	public String getActe_cod() {
		return acte_cod;
	}
	/**
	 * @param acte_cod the acte_cod to set
	 */
	public void setActe_cod(String acte_cod) {
		this.acte_cod = acte_cod;
	}
	/**
	 * @return the activ_cod
	 */
	public String getActiv_cod() {
		return activ_cod;
	}
	/**
	 * @param activ_cod the activ_cod to set
	 */
	public void setActiv_cod(String activ_cod) {
		this.activ_cod = activ_cod;
	}
	/**
	 * @return the regrou_cod
	 */
	public String getRegrou_cod() {
		return regrou_cod;
	}
	/**
	 * @param regrou_cod the regrou_cod to set
	 */
	public void setRegrou_cod(String regrou_cod) {
		this.regrou_cod = regrou_cod;
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
