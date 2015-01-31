package com.lequack.controller;

/**
 * Konstantne vrednosti, ki jih uporabljamo pri izracunih.
 * 
 * @author David Sedlar
 */
public class IzracunConstants {

	/**
	 * Tip izracuna.
	 */
	public enum TipVnosa {
		ure_dnevno, ure_mesecno, pavsal_mesecno;
	}

	static final int STEVILO_MESECEV = 12;
	static final int STEVILO_DELOVNIH_DNI = 21;
	
	static final float NORMIRANI_STROSKI_DELEZ_OLD = 0.70f;
	static final float NORMIRANI_STROSKI_DELEZ = 0.80f;
	
	static final float DOHODNINA_FIKSNI_DELEZ = 0.20f;

	static final float PRISPEVKI_OSNOVA_MIN = 822.52f;
	static final float PRISPEVKI_OSNOVA_MAX = 5331.13f;
	
	static final float PRISPEVKI_ZZ_OSNOVA_MIN = 913.908f;
	static final float PRISPEVKI_ZZ_OSNOVA_MAX = PRISPEVKI_OSNOVA_MAX;
	
	static final float PRISPEVKI_PIZ_ZAVAROVANEC = 15.50f / 100;
	static final float PRISPEVKI_PIZ_DELODAJALEC = 8.85f / 100;
	
	static final float PRISPEVKI_ZZ_ZAVAROVANEC = 6.36f / 100;
	static final float PRISPEVKI_ZZ_DELODAJALEC = 6.56f / 100;
	static final float PRISPEVKI_ZZ_POSKODBE_PRI_DELU = 0.53f / 100;
	
	static final float PRISPEVKI_STAR_VARSTVO_ZAVAROVANEC = 0.10f / 100;
	static final float PRISPEVKI_STAR_VARSTVO_DELODAJALEC = 0.10f / 100;
	
	static final float PRISPEVKI_ZAPOSL_ZAVAROVANEC = 0.14f / 100;
	static final float PRISPEVKI_ZAPOSL_DELODAJALEC = 0.06f / 100;
}
