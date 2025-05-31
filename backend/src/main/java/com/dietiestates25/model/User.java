package com.dietiestates25.model;

import java.math.BigDecimal;

public class User {

	private String email;
	private String password;
	
	//sono davvero metodi?
	public void register(String email, String password) {}
	public void login(String email, String password) {}
	
	//potrebbe utilizzare il costruttore di Research per creare una nuova ricerca ogni volta
	public void searchAd() {}
	
	//potrebbe utilizzare il costruttore di Offer per creare una nuova offerta
	public void makeOffer(BigDecimal price) {}
	
	//potrebbe utilizzare il costruttore di Appointment per creare una nuova visita
	public void bookAppointment() {}
	
	//potrebbe utilizzare dei setter di una Notification per disabilitare una categoria (?, non si dovrebbe fare in Appointment)
	public void disableNotificationCategory() {}
	
	public void showResearches() {}
	public void showOfferHistory() {}
}
