package com.dietiestates25.model;

public class Admin {

	private String password;
	
	public Admin(String password){
		this.password = password;
	}
	
	//potrebbe utilizzare il costruttore di Admin per creare un nuovo admin
	public Admin createAdminAccount(String password) {
		return new Admin(password);
	}
	
	//potrebbe utilizzare i setter di Admin per cambiare password
	public void changePassword(String password) {
		//effettuare i dovuti controlli
		this.password = password;
	}
	
	//potrebbe cambiare un RealEstate tramite i setter
	public void modifyRealEstate(RealEstate realEstate) {
		//codice per modificare un annuncio
	}
	
	public void deleteRealEstate(RealEstate realEstate) {
		//codice per eliminare un immobile
	}
}
