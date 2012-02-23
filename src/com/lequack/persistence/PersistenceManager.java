package com.lequack.persistence;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public final class PersistenceManager {
	private static Logger logger = Logger.getLogger(PersistenceManager.class);
	
	public static void getLestvica(float osnova) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;

		try {
			db = dbf.newDocumentBuilder();
			Document dom = db.parse("WEB-INF/data/dohodnina.xml");
		} catch (SAXException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} catch (ParserConfigurationException e) {
			logger.error(e);
		}
	}
}