








package kabuLab;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import kabuLab.ArrayListEditor.Cut;
import kabuLab.ArrayListEditor.Miscellaneous;
import kabuLab.ArrayListEditor.RightJoiner;
import kabuLab.ArrayListEditor.UpJoiner;
import kabuLab.CSVReader.ParseException;



public class Main2
{

		//いろんなメソッドで共通して使うからフィールド化
		static ArrayList<String> metaData = new ArrayList<String>();
		static ArrayList<ArrayList<String>> arrTable, arrTable2;

		public static void main(String args[]) throws FileNotFoundException, ParseException
	    {
System.out.println("process started...");
			String pass="9007.csv";
			CSVToArray csv = new CSVToArray(pass);
			arrTable = csv.getter();
			arrTable = Cut.easyTrim(arrTable, 0, 0, 10000, 100);
			arrTable = Cut.survive(arrTable, "([0-9]+\\/)+[0-9]+");//日付のみ

			metaData.add("date");
			UpJoiner u = new UpJoiner(arrTable, metaData);
			arrTable = u.getResult();
			metaData = new ArrayList<String>();
			//以上、日付だけを取得
//Miscellaneous.showWithIndex(arrTable);
System.out.println("data road...");

			tablize("9007.csv", "odakyu");//arrTable2に小田急のが入る
			RightJoiner r = new RightJoiner(arrTable, arrTable2);
			arrTable = r.getResult();//右に連結
			arrTable2 = new ArrayList<ArrayList<String>>();
//Miscellaneous.showWithIndex(arrTable);
System.out.println("1/7");
			//小田急終わり


			tablize("9008.csv", "keio");//arrTable2に京王のが入る
			r = new RightJoiner(arrTable, arrTable2);
			arrTable = r.getResult();//右に連結
			arrTable2 = new ArrayList<ArrayList<String>>();
//Miscellaneous.showWithIndex(arrTable);
System.out.println("2/7");
			//京王終わり

			tablize("9005.csv", "tokyu");
			r = new RightJoiner(arrTable, arrTable2);
			arrTable = r.getResult();
			arrTable2 = new ArrayList<ArrayList<String>>();
//Miscellaneous.showWithIndex(arrTable);
System.out.println("3/7");

			tablize("9003.csv", "sotetsu");
			r = new RightJoiner(arrTable, arrTable2);
			arrTable = r.getResult();
			arrTable2 = new ArrayList<ArrayList<String>>();
//Miscellaneous.showWithIndex(arrTable);
System.out.println("4/7");

			tablize("9081.csv", "kanachu");
			r = new RightJoiner(arrTable, arrTable2);
			arrTable = r.getResult();
			arrTable2 = new ArrayList<ArrayList<String>>();
//Miscellaneous.showWithIndex(arrTable);
System.out.println("5/7");

			tablize("8275.csv", "forval");
			r = new RightJoiner(arrTable, arrTable2);
			arrTable = r.getResult();
			arrTable2 = new ArrayList<ArrayList<String>>();
//Miscellaneous.showWithIndex(arrTable);
System.out.println("6/7");

			tablize("9531.csv", "tempo");
			r = new RightJoiner(arrTable, arrTable2);
			arrTable = r.getResult();
			arrTable2 = new ArrayList<ArrayList<String>>();
//Miscellaneous.showWithIndex(arrTable);
System.out.println("7/7");
Miscellaneous.show(arrTable);
			//東ガス終わり
/**/
			//Miscellaneous.showWithIndex(arrTable);

	    }

			static void tablize(String pass, String company) throws FileNotFoundException, ParseException
			{
				CSVToArray csv = new CSVToArray(pass);
				arrTable2 = csv.getter();
				arrTable2 = Cut.easyTrim(arrTable2, 0, 0, 10000, 100);
				arrTable2 = Cut.survive(arrTable2, "[0-9]+(\\.[0-9]+)?");//日付を含まない。小数点を扱う
				setMetaData(company);
				UpJoiner u = new UpJoiner(arrTable2, metaData);
				arrTable2 = u.getResult();
				metaData = new ArrayList<String>();

			}

		    static void setMetaData(String company)
		    {
				metaData.add(company+"_open");
				metaData.add(company+"_high");
				metaData.add(company+"_low");
				metaData.add(company+"_close");
				metaData.add(company+"_volume");
				metaData.add(company+"_close2");
		    }
}
