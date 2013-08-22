package com.lequack.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.lequack.persistence.PersistenceManager;

/**
 * Izracun zneskov za samostojne podjetnike.
 * 
 * @author David Sedlar
 */
@ManagedBean
@ViewScoped
public class IzracunBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final int STEVILO_MESECEV = 12;
	private static final int STEVILO_DELOVNIH_DNI = 21;
	private static final float NORMIRANI_STROSKI_DELEZ = 0.70f;
 
	private float urnaPostavka = 20;
	private float urDnevno = 8;
	private float urMesecno = 168;
	private float pavsalMesecno = 3000;
	private float steviloDni = 261;
	private float steviloPrazniki = 11;
	private float steviloDopust = 20;
	private float steviloBolniska = 5;
	private float zasluzekPrispevki = 0;
	private float malicaDnevno = 6.12f;
	private float prevozZnesek = 37;
	private float stroskiRacunovodstvaMesecno = 50;
	private float stroskiOstaliLetno = 0;
	private TipVnosa tipVnosa = TipVnosa.ure_dnevno;
	
	private float prispevkiPokojninsko = 0;
	private float prispevkiZdravstveno = 0;
	private float prispevkiZaposlovanje = 0;
	private float prispevkiStarsevsko = 0;
	private float prispevkiDrugi = 0;
	
	private float dohodninaSplosnaOlajsava = 0;
	private float dohodninaSkupaj = 0;
	
	public enum TipVnosa {
		ure_dnevno, ure_mesecno, pavsal_mesecno;
	};

	/**
	 * Method is invoked by a GET type reequest.
	 */
	public void onLoad()
	{
		PersistenceManager.getPrispevki(this);
		PersistenceManager.getDohodnina(this);
	}
	
	/**
	 * Izracun mesenega zneska pri dolocenih mesecnih urah.
	 */
	public float getMesecniZnesekSkupni() {
		switch (tipVnosa) {
			case ure_dnevno:
				return urnaPostavka * urDnevno * STEVILO_DELOVNIH_DNI;
			case ure_mesecno:
				return urnaPostavka * urMesecno;
			default:
				return pavsalMesecno;
		}
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
		switch (tipVnosa) {
			case ure_dnevno:
				return urnaPostavka * urDnevno * getSteviloDelavnikovSkupaj();
			case ure_mesecno:
				return urnaPostavka * (urMesecno / STEVILO_DELOVNIH_DNI) * getSteviloDelavnikovSkupaj();
			default:
				return pavsalMesecno * STEVILO_MESECEV;
		}
	}	
	
	/**
	 * Izracun mesecnega zneska pri dolocenih mesecnih urah. odsteti normirani stroski.
	 */
	public float getLetniZnesekSkupniNormiran() {
		return getLetniZnesekSkupni() * (1-NORMIRANI_STROSKI_DELEZ);
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
		return malicaDnevno * STEVILO_DELOVNIH_DNI;	
	}		

	/**
	 * Izracun vseh materialnih stro�kov.
	 */
	public float getMaterialniSkupaj() {
		return prevozZnesek + getMalicaMesecno();
	}	
	
	/**
	 * Izracun racunovodskih stroskov letno.
	 */
	public float getStroskiRacunovodstvaLetno() {
		return stroskiRacunovodstvaMesecno * STEVILO_MESECEV;
	}	
	
	/**
	 * Izracun vseh stro�kov letno.
	 */
	public float getStroskiSkupaj() {
		return getStroskiRacunovodstvaLetno() + stroskiOstaliLetno;
	}	
	
	/**
	 * Dohodninsko izhodisce.
	 */
	public float getDohodninaIzhodisce() {
		return getLetniZnesekSkupni() / STEVILO_MESECEV;
	}	
	
	/**
	 * Normirani stroski mesecni.
	 */
	public float getDohodninaNormiraniStroski() {
		return getDohodninaIzhodisce() * NORMIRANI_STROSKI_DELEZ;
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
		return getLetniZnesekBrezStroskov() / STEVILO_MESECEV;
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
	
	public float getPrispevkiDrugi() {
		return prispevkiDrugi;
	}

	public void setPrispevkiDrugi(float prispevkiDrugi) {
		this.prispevkiDrugi = prispevkiDrugi;
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

	public float getPavsalMesecno() {
		return pavsalMesecno;
	}

	public void setPavsalMesecno(float pavsalMesecno) {
		this.pavsalMesecno = pavsalMesecno;
	}

	public TipVnosa getTipVnosa() {
		return tipVnosa;
	}

	public void setTipVnosa(TipVnosa tipVnosa) {
		this.tipVnosa = tipVnosa;
	}
}