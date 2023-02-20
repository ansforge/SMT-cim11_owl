package fr.gouv.esante.pml.smt.ccam.models;

import java.util.Date;

public class ActeProcedure {
	
	private String acte_cod;
	private String proc_cod;
	private Date acdt_modif;
	/**
	 * @param acte_cod
	 * @param proc_cod
	 * @param acdt_modif
	 */
	public ActeProcedure(String acte_cod, String proc_cod, Date acdt_modif) {
		super();
		this.acte_cod = acte_cod;
		this.proc_cod = proc_cod;
		this.acdt_modif = acdt_modif;
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
	 * @return the proc_cod
	 */
	public String getProc_cod() {
		return proc_cod;
	}
	/**
	 * @param proc_cod the proc_cod to set
	 */
	public void setProc_cod(String proc_cod) {
		this.proc_cod = proc_cod;
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
