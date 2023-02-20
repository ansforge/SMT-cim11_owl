package fr.gouv.esante.pml.smt.ccam.models;

public class Modificateur {
	
	private String cod_modifi;
	private String label;
	private Double coef;
	private Double forfait;
	/**
	 * @param cod_modifi
	 * @param label
	 * @param coef
	 * @param forfait
	 */
	public Modificateur(String cod_modifi, String label, Double coef, Double forfait) {
		super();
		this.cod_modifi = cod_modifi;
		this.label = label;
		this.coef = coef;
		this.forfait = forfait;
	}
	/**
	 * @return the cod_modifi
	 */
	public String getCod_modifi() {
		return cod_modifi;
	}
	/**
	 * @param cod_modifi the cod_modifi to set
	 */
	public void setCod_modifi(String cod_modifi) {
		this.cod_modifi = cod_modifi;
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
	 * @return the coef
	 */
	public Double getCoef() {
		return coef;
	}
	/**
	 * @param coef the coef to set
	 */
	public void setCoef(Double coef) {
		this.coef = coef;
	}
	/**
	 * @return the forfait
	 */
	public Double getForfait() {
		return forfait;
	}
	/**
	 * @param forfait the forfait to set
	 */
	public void setForfait(Double forfait) {
		this.forfait = forfait;
	}
	
	

}
