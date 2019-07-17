/**
 *
 */
package kabuLab;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import kabuLab.CSVReader.ParseException;

/**
 *
 *
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class CSVToArray extends ReadCSV
{

	CSVToArray(String pass, String newRow, String newColumn) throws FileNotFoundException, ParseException
	{
		super(pass, true, newRow, newColumn);
	}

	CSVToArray(String pass) throws FileNotFoundException, ParseException
	{
		super(pass, true);
	}

	public ArrayList<ArrayList<String>> getter()
	{
		return super.arrTable;
	}
}
