package com.lequack.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import java.io.Serializable;

/**
 * Izracun zneskov za samostojne podjetnike.
 * 
 * @author David Sedlar
 */
@ManagedBean
@RequestScoped
public class IzracunBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private float urnaPostavka = 20;
	private float urMesecno = 168;
	private float urDnevno = 8;
	private float steviloDni = 261;
	private float steviloPrazniki = 12;
	private float steviloDopust = 25;
	private float steviloBolniska = 5;
	private float zasluzekPrispevki = 0;
	
	/**
	 * Izracun mesecnega zneska pri dolocenih mesecnih urah.
	 */
	public float getMesecniZnesekSkupni() {
		return urnaPostavka * urMesecno;
	}
	
	/**
	 * Izracun stevila delovnih dni.
	 */
	public float getSteviloDelavnikovSkupaj() {
		return steviloDni - steviloPrazniki - steviloBolniska - steviloDopust;
	}	
	
	/**
	 * Izracun letnega zneska pri dolocenih urah.
	 */
	public float getLetniZnesekSkupni() {
		return urnaPostavka * urDnevno * getSteviloDelavnikovSkupaj();
	}	
	
	/**
	 * Izracun mesecnega zneska pri dolocenih mesecnih urah. odsteti normirani stroski.
	 */
	public float getLetniZnesekSkupniNormiran() {
		return getLetniZnesekSkupni() * 0.75f;
	}	
	
	/*
	 * Getters and setters below
	 */
	public float getUrnaPostavka() {
		return urnaPostavka;
	}

	public void setUrnaPostavka(float urnaPostavka) {
		this.urnaPostavka = urnaPostavka;
	}

	public float getUrMesecno() {
		return urMesecno;
	}

	public void setUrMesecno(float urMesecno) {
		this.urMesecno = urMesecno;
	}

	public float getUrDnevno() {
		return urDnevno;
	}

	public void setUrDnevno(float urDnevno) {
		this.urDnevno = urDnevno;
	}

	public float getSteviloDni() {
		return steviloDni;
	}

	public void setSteviloDni(float steviloDni) {
		this.steviloDni = steviloDni;
	}

	public float getSteviloPrazniki() {
		return steviloPrazniki;
	}

	public void setSteviloPrazniki(float steviloPrazniki) {
		this.steviloPrazniki = steviloPrazniki;
	}

	public float getSteviloDopust() {
		return steviloDopust;
	}

	public void setSteviloDopust(float steviloDopust) {
		this.steviloDopust = steviloDopust;
	}

	public float getSteviloBolniska() {
		return steviloBolniska;
	}

	public void setSteviloBolniska(float steviloBolniska) {
		this.steviloBolniska = steviloBolniska;
	}

	public float getZasluzekPrispevki() {
		return zasluzekPrispevki;
	}

	public void setZasluzekPrispevki(float zasluzekPrispevki) {
		this.zasluzekPrispevki = zasluzekPrispevki;
	}
}