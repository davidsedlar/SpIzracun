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
	private static Workbook wbDohodnina;
	
	/**
	 * Read the Excel file with social security brackets and set the bean values accordingly.
	 * 
	 * @param bean The JSF managed bean
	 */
	public static void getPrispevki(IzracunBean bean) 
	{	
	    Workbook wb = getWorkbook(wbPrispevki, "prispevki.xls");
	    
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
	    bean.setPrispevkiStarsevsko((float) (sheet.getRow(firstCell.getRow() + 12).getCell(bracket).getNumericCellValue()));
	    bean.setPrispevkiZaposlovanje((float) (sheet.getRow(firstCell.getRow() + 15).getCell(bracket).getNumericCellValue()));
	    bean.setPrispevkiDrugi((float) (sheet.getRow(firstCell.getRow() + 16).getCell(bracket).getNumericCellValue()));
	}
	
	
	/**
	 * Read the Excel file with tax brackets and set the bean values accordingly.
	 * 
	 * @param bean The JSF managed bean
	 */
	public static void getDohodnina(IzracunBean bean) 
	{	
	    Workbook wb = getWorkbook(wbDohodnina, "dohodnina.xls");
	    
	    // Get named ranges
	    Name namedRangeDohodnina = wb.getName("dohodnina");
	    Name namedRangeOlajsava = wb.getName("olajsava");
	    
	    AreaReference arefDohodnina = new AreaReference(namedRangeDohodnina.getRefersToFormula());
	    AreaReference arefOlajsava = new AreaReference(namedRangeOlajsava.getRefersToFormula());
	    
	    // Get the first and last cell
	    CellReference firstCellDohodnina = arefDohodnina.getFirstCell();
	    CellReference lastCellDohodnina = arefDohodnina.getLastCell();
	    CellReference firstCellOlajsava = arefOlajsava.getFirstCell();
	    CellReference lastCellOlajsava = arefOlajsava.getLastCell();
	         
	    // Get the sheet
	    Sheet sheet = wb.getSheet(firstCellDohodnina.getSheetName());
        
        // Number pattern
        Pattern p = Pattern.compile("\\d\\d");
        NumberFormat nf = NumberFormat.getInstance(new Locale("sl"));
        
	    // Search for the appropriate tax deduction bracket
        int bracketOlajsava = 0;
	    for (int i = firstCellOlajsava.getRow(); i <= lastCellOlajsava.getRow(); ++i)
	    {
	    	// Get the cell values
	    	Cell cellValue = sheet.getRow(i).getCell(firstCellOlajsava.getCol() + 1);
	    	
	    	float olajsava = 0;
	    	olajsava = (float) cellValue.getNumericCellValue();
			
			// Compare the tax base to the bracket value
			if(bean.getDohodninaIzhodisce() - bean.getDohodninaNormiraniStroski() <= olajsava || i == lastCellOlajsava.getRow())
			{
				bracketOlajsava = i;
				break;
			}
	    }   
	    
	    // Calculate the tax deduction bracket
	    bean.setDohodninaSplosnaOlajsava((float) sheet.getRow(bracketOlajsava).getCell(firstCellOlajsava.getCol() + 2).getNumericCellValue()); 
        
	    
	    // Search for the appropriate tax bracket
        int bracketDohodnina = 0;
	    for (int i = firstCellDohodnina.getRow(); i <= lastCellDohodnina.getRow(); ++i)
	    {
	    	// Get the cell values
	    	Cell cellValue = sheet.getRow(i).getCell(firstCellDohodnina.getCol() + 1);
	    	
	    	float osnova = 0;
	    	osnova = (float) cellValue.getNumericCellValue();
			
			// Compare the tax base to the bracket value
			if(bean.getDohodninaOsnova() <= osnova || i == lastCellDohodnina.getRow())
			{
				bracketDohodnina = i;
				break;
			}
	    }   
	    
	    // Get the tax bracket base
	    float bracketBase = (float) sheet.getRow(bracketDohodnina).getCell(firstCellDohodnina.getCol() + 2).getNumericCellValue();
	    
	    // Get the tax bracket percentage
	    float bracketPercentage = 0;
	    Cell cellProcent = sheet.getRow(bracketDohodnina).getCell(firstCellDohodnina.getCol() + 3);
	    
	    if(cellProcent.getCellType() == Cell.CELL_TYPE_NUMERIC || cellProcent.getCellType() == Cell.CELL_TYPE_FORMULA)
	    	bracketPercentage = (float) cellProcent.getNumericCellValue();
	    else
	    {
	    	String cellProcentString = cellProcent.getStringCellValue();
	    	Matcher m = p.matcher(cellProcentString);
	    	if(m.find())
	    	{
				try {
					Number n = nf.parse(m.group());
					bracketPercentage = n.floatValue() / 100;
				} 
				catch (NumberFormatException e) {
					logger.info(e);
				}
				catch (ParseException e) {
					logger.info(e);
				}
	    	}
	    }
	    
	    // Calculate the tax bracket value
	    bean.setDohodninaSkupaj(bracketBase + 
	    		(bean.getDohodninaOsnova() - (float) sheet.getRow(bracketDohodnina).getCell(firstCellDohodnina.getCol()).getNumericCellValue()) * bracketPercentage); 

	}
	
	
	/**
	 * Get the Excel workbook.
	 * 
	 * @param wb The workbook store.
	 * @param workbookName The name of the excel file.
	 * @return The Excel workbook.
	 */
	private static Workbook getWorkbook(Workbook wb, String workbookName)
	{
		if(wb == null)
		{
		    try {
		    	InputStream inp =  FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/WEB-INF/data/" + workbookName);
		    	wb = WorkbookFactory.create(inp);
			} catch (InvalidFormatException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		
		return wb;
	}
}