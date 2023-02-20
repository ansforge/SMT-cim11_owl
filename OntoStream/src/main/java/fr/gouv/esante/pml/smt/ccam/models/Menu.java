package fr.gouv.esante.pml.smt.ccam.models;

public class Menu {
	
	private Integer id_menu;
	private String label;
	private Integer rang;
	private Integer id_menu_sup;
	
	public Menu(Integer id_menu, Integer rang, String label, Integer id_menu_sup) {
		super();
		this.id_menu = id_menu;
		this.rang = rang;
		this.label = label;
		this.id_menu_sup = id_menu_sup;
	}

	public Integer getId_menu() {
		return id_menu;
	}

	public void setId_menu(Integer id_menu) {
		this.id_menu = id_menu;
	}
	

	public Integer getRang() {
		return rang;
	}

	public void setRang(Integer rang) {
		this.rang = rang;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getId_menu_sup() {
		return id_menu_sup;
	}

	public void setId_menu_sup(Integer id_menu_sup) {
		this.id_menu_sup = id_menu_sup;
	}
	
	

}
