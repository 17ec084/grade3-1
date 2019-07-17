package kabuLab.ArrayListEditor;

import static kabuLab.ArrayListEditor.Miscellaneous.*;

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
		arrRow.add("12");
		arrRow.add("22");
		arrRow.add("31");
		arrRow.add("14");
		arrTable2.add(arrRow);
		arrRow = new ArrayList<String>();
		arrRow.add("51");
		arrRow.add("65");
		arrRow.add("72");
		arrRow.add("81");
		arrTable2.add(arrRow);
		RightJoiner l = new RightJoiner(arrTable1, arrTable2);
		arrTable = l.getResult();
		System.out.println(l.getCntOfC()+" should be 7");
		System.out.println(l.getCntOfR()+" should be 3");
		Miscellaneous.show(arrTable);
		Miscellaneous.showWithIndex(arrTable);
		arrTable = Miscellaneous.rotation(arrTable, -41);
		arrTable = Miscellaneous.replacer(arrTable, "a", "あいうえ4お");
		Miscellaneous.showWithIndex(arrTable);
		SearchCells search = new SearchCells(arrTable, ".*[0-9]+.*");
		ArrayList<ArrayList<Integer>> searchResult = search.getResult();
		for(int i = 0; i < search.getCnt(); i++)
		{
			System.out.println(searchResult.get(0).get(i)+"行"+searchResult.get(1).get(i)+"列");
		}
		ArrayList<ArrayList<String>> arrTable_;
		arrTable_ = getElementsByType.real(arrTable);
		Miscellaneous.show(arrTable_);
		arrTable_ = Miscellaneous.switchRC(arrTable_);
		Miscellaneous.show(arrTable_);
		int i=LEFT;
//		arrTable=Miscellaneous.switchRC(arrTable, true);
		Ensure ensure = new Ensure(10,10, "日本語にも対応しました笑");
		arrTable=ensure.ensure();
//		Miscellaneous.show(arrTable);
		arrTable.get(2).set(1, "1");
		arrTable.get(5).set(8, "2");
		arrTable.get(5).set(0, "3");
		arrTable.get(7).set(2, "4");
		Miscellaneous.show(arrTable);
//		arrTable = Cut.leaveNoSpace(arrTable, ".*日本.*");
		arrTable = Cut.easyTrim(arrTable, 2, 0, 7, 8);
		Miscellaneous.show(arrTable);
//		Scanner scanner = new Scanner(System.in);
//		System.out.println(scanner.nextLine().matches(".*[\\-\\+\\*\\[\\(\\?].*"));

	}

}
