package fr.gouv.esante.pml.smt.ccam.models;

import java.util.Date;

public class Association {
	
	private String aa_code1;
	private String aa_code2;
	private Date dt_modif1;
	private Date dt_modif2;
	/**
	 * @param aa_code1
	 * @param aa_code2
	 * @param dt_modif1
	 * @param dt_modif2
	 */
	public Association(String aa_code1, String aa_code2, Date dt_modif1, Date dt_modif2) {
		super();
		this.aa_code1 = aa_code1;
		this.aa_code2 = aa_code2;
		this.dt_modif1 = dt_modif1;
		this.dt_modif2 = dt_modif2;
	}
	/**
	 * @return the aa_code1
	 */
	public String getAa_code1() {
		return aa_code1;
	}
	/**
	 * @param aa_code1 the aa_code1 to set
	 */
	public void setAa_code1(String aa_code1) {
		this.aa_code1 = aa_code1;
	}
	/**
	 * @return the aa_code2
	 */
	public String getAa_code2() {
		return aa_code2;
	}
	/**
	 * @param aa_code2 the aa_code2 to set
	 */
	public void setAa_code2(String aa_code2) {
		this.aa_code2 = aa_code2;
	}
	/**
	 * @return the dt_modif1
	 */
	public Date getDt_modif1() {
		return dt_modif1;
	}
	/**
	 * @param dt_modif1 the dt_modif1 to set
	 */
	public void setDt_modif1(Date dt_modif1) {
		this.dt_modif1 = dt_modif1;
	}
	/**
	 * @return the dt_modif2
	 */
	public Date getDt_modif2() {
		return dt_modif2;
	}
	/**
	 * @param dt_modif2 the dt_modif2 to set
	 */
	public void setDt_modif2(Date dt_modif2) {
		this.dt_modif2 = dt_modif2;
	}
	
	

}
