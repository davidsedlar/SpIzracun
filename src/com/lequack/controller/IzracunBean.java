package com.lequack.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


/**
 * Izracun zneskov za samostojne podjetnike.
 * 
 * @author David Sedlar
 */
@ManagedBean
@ViewScoped
public class IzracunBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// Vnosna polja z nekimi smiselnimi zacetnimi vrednostmi
	private float urnaPostavka = 20;
	private float urDnevno = 8;
	private float urMesecno = 168;
	private float pavsalMesecno = 3000;
	private float steviloDni = 261;
	private float steviloPrazniki = 6;
	private float steviloDopust = 20;
	private float steviloBolniska = 5;
	private float zasluzekPrispevki = 0;
	private float malicaDnevno = 6.12f;
	private float prevozZnesek = 37;
	private float stroskiRacunovodstvaMesecno = 50;
	private float stroskiOstaliLetno = 0;
	private IzracunConstants.TipVnosa tipVnosa = IzracunConstants.TipVnosa.ure_dnevno;

	/**
	 * Method is invoked by a GET type reequest.
	 */
	public void onLoad()
	{
	}
	
	/**
	 * Izracun mesenega zneska pri dolocenih mesecnih urah.
	 */
	public float getMesecniZnesekSkupni() {
		switch (tipVnosa) {
			case ure_dnevno:
				return urnaPostavka * urDnevno * IzracunConstants.STEVILO_DELOVNIH_DNI;
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
				return urnaPostavka * (urMesecno / IzracunConstants.STEVILO_DELOVNIH_DNI) * getSteviloDelavnikovSkupaj();
			default:
				return pavsalMesecno * IzracunConstants.STEVILO_MESECEV;
		}
	}	
	
	/**
	 * Izracun mesecnega zneska pri dolocenih mesecnih urah. odsteti normirani stroski.
	 */
	public float getLetniZnesekSkupniNormiran() {
		return getLetniZnesekSkupni() * (1-IzracunConstants.NORMIRANI_STROSKI_DELEZ);
	}	
	
	/**
	 * Izracun vseh prispevkov.
	 */
	public float getPrispevkiSkupaj() {
		return getPrispevkiPokojninsko() + getPrispevkiZdravstveno() + getPrispevkiStarsevsko() + getPrispevkiZaposlovanje();	
	}	
	
	/**
	 * Izracun prispevkov za PIZ.
	 */
	public float getPrispevkiPokojninsko() {
		return getPrispevkiOsnova() * (IzracunConstants.PRISPEVKI_PIZ_ZAVAROVANEC + IzracunConstants.PRISPEVKI_PIZ_DELODAJALEC);
	}

	/**
	 * Izracun prispevkov za ZZ.
	 */
	public float getPrispevkiZdravstveno() {
		return getPrispevkiZZOsnova() * (IzracunConstants.PRISPEVKI_ZZ_ZAVAROVANEC + IzracunConstants.PRISPEVKI_ZZ_DELODAJALEC + IzracunConstants.PRISPEVKI_ZZ_POSKODBE_PRI_DELU);
	}
	
	/**
	 * Izracun prispevkov za zaposlovanje.
	 */
	public float getPrispevkiZaposlovanje() {
		return getPrispevkiOsnova() * (IzracunConstants.PRISPEVKI_ZAPOSL_ZAVAROVANEC + IzracunConstants.PRISPEVKI_ZAPOSL_DELODAJALEC);
	}

	/**
	 * Izracun prispevkov za starsevstvo.
	 */
	public float getPrispevkiStarsevsko() {
		return getPrispevkiOsnova() * (IzracunConstants.PRISPEVKI_STAR_VARSTVO_ZAVAROVANEC +  IzracunConstants.PRISPEVKI_STAR_VARSTVO_DELODAJALEC);
	}
	
	/**
	 * Osnova za izracun prispevkov, mesecna osnova.
	 */
	public float getPrispevkiOsnova() {
		float zasluzekPrispevkiMesecni = zasluzekPrispevki / IzracunConstants.STEVILO_MESECEV;
		
		if(zasluzekPrispevkiMesecni < IzracunConstants.PRISPEVKI_OSNOVA_MIN)
			return IzracunConstants.PRISPEVKI_OSNOVA_MIN;
		else if(zasluzekPrispevkiMesecni > IzracunConstants.PRISPEVKI_OSNOVA_MAX)
			return IzracunConstants.PRISPEVKI_OSNOVA_MAX;
		else
			return zasluzekPrispevkiMesecni;
	}
	
	/**
	 * Osnova za izracun prispevkov za ZZ, mesecna osnova. Drugacna spodnja meja, zato posebna metoda.
	 */
	public float getPrispevkiZZOsnova() {
		float zasluzekPrispevkiMesecni = zasluzekPrispevki / IzracunConstants.STEVILO_MESECEV;
		
		if(zasluzekPrispevkiMesecni < IzracunConstants.PRISPEVKI_ZZ_OSNOVA_MIN)
			return IzracunConstants.PRISPEVKI_ZZ_OSNOVA_MIN;
		else if(zasluzekPrispevkiMesecni > IzracunConstants.PRISPEVKI_ZZ_OSNOVA_MAX)
			return IzracunConstants.PRISPEVKI_ZZ_OSNOVA_MAX;
		else
			return zasluzekPrispevkiMesecni;
	}
	
	/**
	 * Izracun zneska za malico.
	 */
	public float getMalicaMesecno() {
		return malicaDnevno * IzracunConstants.STEVILO_DELOVNIH_DNI;	
	}		

	/**
	 * Izracun vseh materialnih stroskov.
	 */
	public float getMaterialniSkupaj() {
		return prevozZnesek + getMalicaMesecno();
	}	
	
	/**
	 * Izracun racunovodskih stroskov letno.
	 */
	public float getStroskiRacunovodstvaLetno() {
		return stroskiRacunovodstvaMesecno * IzracunConstants.STEVILO_MESECEV;
	}	
	
	/**
	 * Izracun vseh stroskov letno.
	 */
	public float getStroskiSkupaj() {
		return getStroskiRacunovodstvaLetno() + stroskiOstaliLetno;
	}	
	
	/**
	 * Dohodninsko izhodisce.
	 */
	public float getDohodninaIzhodisce() {
		return getLetniZnesekSkupni() / IzracunConstants.STEVILO_MESECEV;
	}	
	
	/**
	 * Normirani stroski mesecni.
	 */
	public float getDohodninaNormiraniStroski() {
		return getDohodninaIzhodisce() * IzracunConstants.NORMIRANI_STROSKI_DELEZ;
	}	
	
	/**
	 * Dohodninska osnova.
	 */
	public float getDohodninaOsnova() {
		float dohOsnova = getDohodninaIzhodisce() - getDohodninaNormiraniStroski();
		return (dohOsnova < 0) ? 0 : dohOsnova;
	}	
	
	/**
	 * Skupaj dohodnina, odnova pomnožena z fiksnim delezem.
	 */	
	public float getDohodninaSkupajFiksno() {
		return getDohodninaOsnova() * IzracunConstants.DOHODNINA_FIKSNI_DELEZ;
	}	
	
	/**
	 * Letni znesek brez ostalih stroskov.
	 */
	public float getLetniZnesekBrezStroskov() {
		return getLetniZnesekSkupni() - getStroskiSkupaj();
	}	
	
	/**
	 * Mesecni znesek brez ostalih stroskov.
	 */
	public float getMesecniZnesekBrezStroskov() {
		return getLetniZnesekBrezStroskov() / IzracunConstants.STEVILO_MESECEV;
	}
	
	/**
	 * Mesecni znesek neto skupaj z materialnimi stroski.
	 */
	public float getZnesekNetoMaterialni() {
		return getMesecniZnesekBrezStroskov() - getPrispevkiSkupaj() - getDohodninaSkupajFiksno();
	}	
	
	/**
	 * Mesecni znesek neto, brez vseh stroskov.
	 */
	public float getZnesekNetoKoncni() {
		return getZnesekNetoMaterialni() - getMaterialniSkupaj();
	}	
	
	/**
	 * Delez normiranih stroškov.
	 */
	public String getNormiraniStroskiDelez() {
		return String.format("%.0f", IzracunConstants.NORMIRANI_STROSKI_DELEZ * 100);
	}
	
	/**
	 * Delez normiranih stroškov (pred 2015).
	 */
	public String getNormiraniStroskiDelezOld() {
		return String.format("%.0f", IzracunConstants.NORMIRANI_STROSKI_DELEZ_OLD * 100);
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

	public float getPavsalMesecno() {
		return pavsalMesecno;
	}

	public void setPavsalMesecno(float pavsalMesecno) {
		this.pavsalMesecno = pavsalMesecno;
	}

	public IzracunConstants.TipVnosa getTipVnosa() {
		return tipVnosa;
	}

	public void setTipVnosa(IzracunConstants.TipVnosa tipVnosa) {
		this.tipVnosa = tipVnosa;
	}
}