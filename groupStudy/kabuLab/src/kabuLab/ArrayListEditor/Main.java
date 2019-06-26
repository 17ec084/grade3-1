package kabuLab.ArrayListEditor;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import kabuLab.CSVReader.ParseException;


public class Main
{
	static ArrayList<ArrayList<String>> arrTable1 = new ArrayList<ArrayList<String>>();
	static ArrayList<ArrayList<String>> arrTable2 = new ArrayList<ArrayList<String>>();
	static ArrayList<ArrayList<String>> arrTable  = new ArrayList<ArrayList<String>>();
	static ArrayList<String> arrRow;

	public static void main(String[] args) throws FileNotFoundException, ParseException
	{
		/*
		 * arrTable1
		 * a b c
		 * d e f
		 * g h i
		 *
		 * arrTable2
		 *
		 * 1 2 3 4
		 * 5 6 7 8
		 * */

		arrRow = new ArrayList<String>();
		arrRow.add("a");
		arrRow.add("b");
		arrRow.add("c");
		arrTable1.add(arrRow);
		arrRow = new ArrayList<String>();
		arrRow.add("d");
		arrRow.add("e");
		arrRow.add("f");
		arrTable1.add(arrRow);
		arrRow = new ArrayList<String>();
		arrRow.add("g");
		arrRow.add("h");
		arrRow.add("i");
		arrTable1.add(arrRow);
		arrRow = new ArrayList<String>();
		arrRow.add("1");
		arrRow.add("2");
		arrRow.add("3");
		arrRow.add("4");
		arrTable2.add(arrRow);
		arrRow = new ArrayList<String>();
		arrRow.add("5");
		arrRow.add("6");
		arrRow.add("7");
		arrRow.add("8");
		arrTable2.add(arrRow);
		UpJoiner d = new UpJoiner(arrTable1, arrTable2);
		arrTable = d.getResult();
		Miscellaneous.show(arrTable);
		Miscellaneous rot = new Miscellaneous();
//		arrTable=rot.rotation(arrTable, RIGHT);
		Ensure ensure = new Ensure(arrTable,3,2, "あ");
		arrTable=ensure.ensure();
		Miscellaneous.show(arrTable);

	}

}
