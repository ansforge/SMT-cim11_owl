package fr.gouv.esante.pml.smt.ccam.models;

import java.util.Date;

public class Acte {
	
	private String cod_acte;
	private String label;
	private String label1;
	private Integer acteType;
	private Integer menu_cod;
	private Date dt_creatio;
	private Date dtModif;
	private Date dt_effet;
	
	/**
	 * @param cod_acte
	 * @param label
	 * @param label1
	 * @param menu_cod
	 * @param dtModif
	 */
	public Acte(String cod_acte, String label, String label1, Integer menu_cod, Date dtModif) {
		super();
		this.cod_acte = cod_acte;
		this.label = label;
		this.label1 = label1;
		this.menu_cod = menu_cod;
		this.dtModif = dtModif;
	}
	
	/**
	 * @param cod_acte
	 * @param label
	 * @param label1
	 * @param acteType
	 * @param menu_cod
	 * @param dt_creatio
	 * @param dtModif
	 * @param dt_effet
	 */
	public Acte(String cod_acte, String label, String label1, Integer acteType, Integer menu_cod, Date dt_creatio,
			Date dtModif, Date dt_effet) {
		super();
		this.cod_acte = cod_acte;
		this.label = label;
		this.label1 = label1;
		this.acteType = acteType;
		this.menu_cod = menu_cod;
		this.dt_creatio = dt_creatio;
		this.dtModif = dtModif;
		this.dt_effet = dt_effet;
	}

	/**
	 * @param cod_acte
	 * @param label
	 * @param menu_cod
	 */
	public Acte(String cod_acte, String label,String label1, Integer menu_cod) {
		super();
		this.cod_acte = cod_acte;
		this.label = label;
		this.label1 = label1;
		this.menu_cod = menu_cod;
	}
	
	
	/**
	 * @param cod_acte
	 * @param label
	 * @param label1
	 * @param acteType
	 * @param menu_cod
	 * @param dtModif
	 */
	public Acte(String cod_acte, String label, String label1, Integer acteType, Integer menu_cod, Date dtModif) {
		super();
		this.cod_acte = cod_acte;
		this.label = label;
		this.label1 = label1;
		this.acteType = acteType;
		this.menu_cod = menu_cod;
		this.dtModif = dtModif;
	}
	/**
	 * @return the cod_acte
	 */
	public String getCod_acte() {
		return cod_acte;
	}
	/**
	 * @param cod_acte the cod_acte to set
	 */
	public void setCod_acte(String cod_acte) {
		this.cod_acte = cod_acte;
	}
	
	/**
	 * @return the dt_creatio
	 */
	public Date getDt_creatio() {
		return dt_creatio;
	}
	/**
	 * @param dt_creatio the dt_creatio to set
	 */
	public void setDt_creatio(Date dt_creatio) {
		this.dt_creatio = dt_creatio;
	}
	/**
	 * @return the dtModif
	 */
	public Date getDtModif() {
		return dtModif;
	}
	/**
	 * @param dtModif the dtModif to set
	 */
	public void setDtModif(Date dtModif) {
		this.dtModif = dtModif;
	}
	/**
	 * @return the dt_effet
	 */
	public Date getDt_effet() {
		return dt_effet;
	}
	/**
	 * @param dt_effet the dt_effet to set
	 */
	public void setDt_effet(Date dt_effet) {
		this.dt_effet = dt_effet;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the label1
	 */
	public String getLabel1() {
		return label1;
	}
	/**
	 * @param label1 the label1 to set
	 */
	public void setLabel1(String label1) {
		this.label1 = label1;
	}
	/**
	 * @return the menu_cod
	 */
	public Integer getMenu_cod() {
		return menu_cod;
	}
	/**
	 * @param menu_cod the menu_cod to set
	 */
	public void setMenu_cod(Integer menu_cod) {
		this.menu_cod = menu_cod;
	}
	/**
	 * @return the acteType
	 */
	public Integer getActeType() {
		return acteType;
	}
	/**
	 * @param acteType the acteType to set
	 */
	public void setActeType(Integer acteType) {
		this.acteType = acteType;
	}
	
	

}
