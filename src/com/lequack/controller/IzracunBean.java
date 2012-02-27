package com.lequack.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.lequack.persistence.PersistenceManager;

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
	private float malicaDnevno = 6.12f;
	private float prevozZnesek = 37;
	private float stroskiRacunovodstvaMesecno = 50;
	private float stroskiOstaliLetno = 0;
	
	private float prispevkiPokojninsko = 0;
	private float prispevkiZdravstveno = 0;
	private float prispevkiZaposlovanje = 0;
	private float prispevkiStarsevsko = 0;
	
	private float dohodninaSplosnaOlajsava = 0;
	private float dohodninaSkupaj = 0;

	/**
	 * Method is invoked by a GET type reequest.
	 */
	public void onLoad()
	{
		PersistenceManager.getPrispevki(this);
		PersistenceManager.getDohodnina(this);
	}
	
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
	
	/**
	 * Izracun vseh prispevkov.
	 */
	public float getPrispevkiSkupaj() {
		return prispevkiPokojninsko + prispevkiZdravstveno + prispevkiStarsevsko + prispevkiZaposlovanje;	
	}	
	
	/**
	 * Izracun zneska za malico.
	 */
	public float getMalicaMesecno() {
		return urMesecno / urDnevno * malicaDnevno;	
	}		

	/**
	 * Izracun vseh materialnih stroškov.
	 */
	public float getMaterialniSkupaj() {
		return prevozZnesek + getMalicaMesecno();
	}	
	
	/**
	 * Izracun racunovodskih stroskov letno.
	 */
	public float getStroskiRacunovodstvaLetno() {
		return stroskiRacunovodstvaMesecno * 12;
	}	
	
	/**
	 * Izracun vseh stroškov letno.
	 */
	public float getStroskiSkupaj() {
		return getStroskiRacunovodstvaLetno() + stroskiOstaliLetno;
	}	
	
	/**
	 * Dohodninsko izhodisce.
	 */
	public float getDohodninaIzhodisce() {
		return getLetniZnesekSkupni() / 12;
	}	
	
	/**
	 * Normirani stroski mesecni.
	 */
	public float getDohodninaNormiraniStroski() {
		return getDohodninaIzhodisce() * 0.25f;
	}	
	
	/**
	 * Dohodninska osnova.
	 */
	public float getDohodninaOsnova() {
		float dohOsnova = getDohodninaIzhodisce() - getDohodninaNormiraniStroski() - dohodninaSplosnaOlajsava - getPrispevkiSkupaj();
		return (dohOsnova < 0) ? 0 : dohOsnova;
	}	
	
	/**
	 * Letni znesek brez ostaligh stroskov.
	 */
	public float getLetniZnesekBrezStroskov() {
		return getLetniZnesekSkupni() - getStroskiSkupaj();
	}	
	
	/**
	 * Mesecni znesek brez ostaligh stroskov.
	 */
	public float getMesecniZnesekBrezStroskov() {
		return getLetniZnesekBrezStroskov() / 12;
	}
	
	/**
	 * Mesecni znesek neto skupaj z materialnimi stroski.
	 */
	public float getZnesekNetoMaterialni() {
		return getMesecniZnesekBrezStroskov() - getPrispevkiSkupaj() - getDohodninaSkupaj();
	}	
	
	/**
	 * Mesecni znesek neto, brez vseh stroskov.
	 */
	public float getZnesekNetoKoncni() {
		return getZnesekNetoMaterialni() - getMaterialniSkupaj();
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

	public float getPrispevkiPokojninsko() {
		return prispevkiPokojninsko;
	}

	public void setPrispevkiPokojninsko(float prispevkiPokojninsko) {
		this.prispevkiPokojninsko = prispevkiPokojninsko;
	}

	public float getPrispevkiZdravstveno() {
		return prispevkiZdravstveno;
	}

	public void setPrispevkiZdravstveno(float prispevkiZdravstveno) {
		this.prispevkiZdravstveno = prispevkiZdravstveno;
	}

	public float getPrispevkiZaposlovanje() {
		return prispevkiZaposlovanje;
	}

	public void setPrispevkiZaposlovanje(float prispevkiZaposlovanje) {
		this.prispevkiZaposlovanje = prispevkiZaposlovanje;
	}

	public float getPrispevkiStarsevsko() {
		return prispevkiStarsevsko;
	}

	public void setPrispevkiStarsevsko(float prispevkiStarsevsko) {
		this.prispevkiStarsevsko = prispevkiStarsevsko;
	}

	public float getMalicaDnevno() {
		return malicaDnevno;
	}

	public void setMalicaDnevno(float malicaDnevno) {
		this.malicaDnevno = malicaDnevno;
	}

	public float getPrevozZnesek() {
		return prevozZnesek;
	}

	public void setPrevozZnesek(float prevozZnesek) {
		this.prevozZnesek = prevozZnesek;
	}

	public float getStroskiRacunovodstvaMesecno() {
		return stroskiRacunovodstvaMesecno;
	}

	public void setStroskiRacunovodstvaMesecno(float stroskiRacunovodstvaMesecno) {
		this.stroskiRacunovodstvaMesecno = stroskiRacunovodstvaMesecno;
	}

	public float getStroskiOstaliLetno() {
		return stroskiOstaliLetno;
	}

	public void setStroskiOstaliLetno(float stroskiOstaliLetno) {
		this.stroskiOstaliLetno = stroskiOstaliLetno;
	}

	public float getDohodninaSplosnaOlajsava() {
		return dohodninaSplosnaOlajsava;
	}

	public void setDohodninaSplosnaOlajsava(float dohodninaSplosnaOlajsava) {
		this.dohodninaSplosnaOlajsava = dohodninaSplosnaOlajsava;
	}

	public float getDohodninaSkupaj() {
		return dohodninaSkupaj;
	}

	public void setDohodninaSkupaj(float dohodninaSkupaj) {
		this.dohodninaSkupaj = dohodninaSkupaj;
	}
}