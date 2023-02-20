package fr.gouv.esante.pml.smt.ccam.models;

public class Activite {
	
	private String cod_activ;
	private String label;
	
	
	public Activite(String cod_activ, String label) {
		super();
		this.cod_activ = cod_activ;
		this.label = label;
	}


	/**
	 * @return the cod_activ
	 */
	public String getCod_activ() {
		return cod_activ;
	}


	/**
	 * @param cod_activ the cod_activ to set
	 */
	public void setCod_activ(String cod_activ) {
		this.cod_activ = cod_activ;
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

	
	

}
