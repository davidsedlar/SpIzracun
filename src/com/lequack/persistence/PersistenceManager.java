package com.lequack.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;

import com.lequack.controller.IzracunBean;

/**
 * Manages the persistent data stored in Excel files.
 * 
 * @author David Sedlar
 */
public final class PersistenceManager {
	private static Logger logger = Logger.getLogger(PersistenceManager.class);
	private static Workbook wbPrispevki;
	
	/**
	 * Read the Excel file with social security brackets and set the bean values accordingly.
	 * 
	 * @param bean The JSF managed bean
	 */
	public static void getPrispevki(IzracunBean bean) 
	{	
	    Workbook wb = getWorkbookPrispevki();
	    
	    // Get named range
	    Name namedRange = wb.getName("prispevki");
	    AreaReference aref = new AreaReference(namedRange.getRefersToFormula());
	    
	    // Get the first and last cell
	    CellReference firstCell = aref.getFirstCell();
	    CellReference lastCell = aref.getLastCell();
	         
	    // Get the first row in the range
	    Sheet sheet = wb.getSheet(firstCell.getSheetName());
        Row r = sheet.getRow(firstCell.getRow());
        
        // Number pattern and number format locale
        Pattern p = Pattern.compile("[\\d.,]+");
        NumberFormat nf = NumberFormat.getInstance(new Locale("sl"));

	    // Search for the appropriate income bracket
        int bracket = 0;
	    for (int i = firstCell.getCol(); i <= lastCell.getCol(); ++i)
	    {
	    	// Get the cell value
	    	Cell cell = r.getCell(i);
	    	String stringCellValue = cell.getStringCellValue();
	    	
	    	// Extract the numbers from the cell value
			Matcher m = p.matcher(stringCellValue);
			float osnova = 0;
			while (m.find()) {
				try {
					Number n = nf.parse(m.group());
					osnova = n.floatValue();
				} 
				catch (NumberFormatException e) {
					logger.info(e);
				}
				catch (ParseException e) {
					logger.info(e);
				}
			}
			
			// Compare the yearly sum to the bracket value
			if(bean.getZasluzekPrispevki() <= osnova || i == lastCell.getCol())
			{
				bracket = i;
				break;
			}
	    }
	    
	    // Set the appropriate values
	    bean.setPrispevkiPokojninsko((float) sheet.getRow(firstCell.getRow() + 5).getCell(bracket).getNumericCellValue());
	    bean.setPrispevkiZdravstveno((float) sheet.getRow(firstCell.getRow() + 9).getCell(bracket).getNumericCellValue());
	    bean.setPrispevkiStarsevsko((float) (sheet.getRow(firstCell.getRow() + 10).getCell(bracket).getNumericCellValue() +
	    		sheet.getRow(firstCell.getRow() + 11).getCell(bracket).getNumericCellValue()));
	    bean.setPrispevkiZaposlovanje((float) (sheet.getRow(firstCell.getRow() + 12).getCell(bracket).getNumericCellValue() +
	    		sheet.getRow(firstCell.getRow() + 13).getCell(bracket).getNumericCellValue()));
	}
	
	/**
	 * Get the Excel workbook.
	 * 
	 * @return The Excel workbook.
	 */
	private static Workbook getWorkbookPrispevki()
	{
		if(wbPrispevki == null)
		{
		    try {
		    	InputStream inp =  FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/data/prispevki.xls");
				wbPrispevki = WorkbookFactory.create(inp);
			} catch (InvalidFormatException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		
		return wbPrispevki;
	}
}