package com.dietiestates25.model;

import java.math.BigDecimal;

public class EstateAgent {

	//potrebbe utilizzare il costruttore di RealEstate per creare un nuovo annuncio
	public void createRealEstate() {}
	
	//potrebbe utilizzare i setter di realEstate per modificare un annuncio esistente
	public void modifyRealEstate() {}
	
	public void deleteRealEstate() {}
	
	public void acceptOffer() {}
	
	public void declineOffer() {}
	
	//potrebbe utilizzare il costruttore di Offer per creare una nuova offerta
	public void makeCounterOffer(BigDecimal price) {}
	
	//potrebbe utilizzare il costruttore di Offer per creare una nuova offerta
	public void insertOffer(BigDecimal price) {}
	
	public void acceptAppointment() {}
	
	public void declineAppointment() {}
	
	public void showOfferHistory() {}
}
