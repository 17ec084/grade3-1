/**
 *
 */
package kabuLab.ArrayListEditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import kabuLab.ShowCSV;
import kabuLab.CSVReader.ParseException;

/**
 * ArrayListに記憶した表に関する雑務をまとめたもの。<br>
 * ここでいう雑務とは、合目的的でない(データマイニングに直接寄与するとは考えにくい)操作であるが必要不可欠でかつ多用が予想される操作の総称である。<br>
 * 例えば表の「回転」は行と列が入れ替わる操作であり、そのような操作はデータマイニングにはほぼ不要である。<br>
 * しかし回転の操作は、他の重要な操作を援ける。<br>
 * 「2つの表を縦(列方向)に連結する」操作が kabuLab.ArrayListEditor.Miscellaneous.DownJoiner で実装されているが、<br>
 * これと回転を組み合わせることによって、容易に「2つの表を横(行方向)に連結する」ことができる。<br>
 * この回転ようなものが「雑務」である。<br>
 * 形式上はクラスであるが、実質的にはメソッドをまとめたもののように使うことを想定している。<br>
 * 例えば、ArrayListによる表arrTableを用意して、<br>
 * Miscellaneous.show(arrTable);<br>
 * とすれば、どのクラスからでも呼び出すことができる。<br>
 * 呼び出しに対応するメソッドはstatic化される必要がある。<br><br>
 * 「関連事項で※を示したもの」:作業用にCSVファイルを書き出す(一時ファイル)。パスはコンストラクタで指定
 * @author 17ec084(http://github.com/17ec084)
 * @see #show(ArrayList) <p>show(ArrayList arrTable)<br>(static参照用)<br>ArrayList&lt;ArrayList&lt;String&gt;&gt;に記憶した表arrTableを"toBeShown.csv"へ保存の上、表示する。</p>
 * @see #show(ArrayList,String) <p>show(ArrayList arrTable, String pass)<br>(static参照用)<br>ArrayList&lt;ArrayList&lt;String&gt;&gt;に記憶した表arrTableを指定されたpassへCSV形式で保存の上、表示する。</p>
 * @see #arrayToCSV(ArrayList, String) <p>arrayToCSV<br>(static参照用)<br>ArrayList&lt;ArrayList&lt;String&gt;&gt;に記憶した表arrTableを指定されたpassへCSV形式で保存する。</p>
 * @see #rotation(ArrayList, int) <p>rotation<br>(インスタンス必要)<br>表の回転を行う。<br>※</p>
 * @see #getArrSize <p>getArrSize<br>(static参照用)<br>表の大きさを 行数,列数 のように返却する。</p>
 * @see #switchRC <p>switchRC<br>(インスタンス必要)<br>行と列を入れ替える</p>
 *
 */
public class Miscellaneous
{

	//rotation用フラグ値フィールド
	public static final int RIGHT=1;
	public static final int UD=2;//upside down
	public static final int DOWN=2;
	public static final int LEFT=3;

	//一時ファイルのパスを示すフィールド
	private static String tempCSVpass;

	//コンストラクタ
	Miscellaneous()
	{
		Miscellaneous.tempCSVpass="temp.csv";
	}
	Miscellaneous(String tempCSVpass)
	{
		Miscellaneous.tempCSVpass=tempCSVpass;
	}

	/**
	 * arrTableを"toBeShown.csv"へ保存の上、表示する。
	 * @see #show(ArrayList,String) show(ArrayList arrTable, String pass): passを明示することでパスを変更可能
	 * @see #arrayToCSV arrayToCSV:保存に用いているメソッド
	 * @param arrTable
	 * @throws ParseException
	 * @throws FileNotFoundException
	 */
	public static void show(ArrayList<ArrayList<String>> arrTable) throws FileNotFoundException, ParseException
	{
		show(arrTable, "toBeShown.csv");
		//パスを明示し、オーバーロードされたもう一つのshowに一任。
	}


	/**
	 * arrTableをpassで指定されたパスへ保存の上、表示する。
	 * @see #show(ArrayList) show(ArrayList arrTable): passを省略した場合、パスは"toBeShown.csv"となる
	 * @see #arrayToCSV arrayToCSV:保存に用いているメソッド
	 * @param arrTable
	 * @param pass
	 * @throws ParseException
	 * @throws FileNotFoundException
	 */
	public static void show(ArrayList<ArrayList<String>> arrTable, String pass) throws FileNotFoundException, ParseException
	{
		arrayToCSV(arrTable, pass);
		ShowCSV csv = new ShowCSV(pass, true);

	}


	public static void arrayToCSV(ArrayList<ArrayList<String>> arrTable, String pass)
	{
		int max=0;
		int cntOfC;
		int cntOfR;

		for(int r=0; r<arrTable.size(); r++)
		{
			if(arrTable.get(r).size()>max)
			{
				max=arrTable.get(r).size();
			}
		}

		cntOfC=max;

		cntOfR=arrTable.size();

		int r=0;
		int c=0;

		try
		{
			File file = new File(pass);
			FileWriter filewriter = new FileWriter(file);

			for(r=0; r<cntOfR-1; r++)
			{
				for(c=0; c<cntOfC-1; c++)
				{
					filewriter.write(arrTable.get(r).get(c));
					filewriter.write(",");
				}
				filewriter.write(arrTable.get(r).get(c));
				filewriter.write("\r\n");
			}

				for(c=0; c<cntOfC-1; c++)
				{
					filewriter.write(arrTable.get(r).get(c));
					filewriter.write(",");
				}
				filewriter.write(arrTable.get(r).get(c));

			filewriter.close();

		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}

	/**
	 * 第一引数にArrayListで与えられた表を回転させる。
	 * @param arrTable
	 * @param count 右へ回転する回数。<br>Miscellaneousクラスをimport staticしておけば、フラグ値として<br>
	 * RIGHT=1,UD=DOWN=2,LEFT=3が利用できる。
	 * @throws ParseException
	 * @throws FileNotFoundException
	 * @return 回転された表
	 */
	public ArrayList<ArrayList<String>> rotation(ArrayList<ArrayList<String>> arrTable, int count) throws FileNotFoundException, ParseException
	{
		for(int i=0; i<count; i++)
		{
			arrTable=rotation(arrTable);
		}
		return arrTable;

	}
	private static ArrayList<ArrayList<String>> rotation(ArrayList<ArrayList<String>> arrTable) throws FileNotFoundException, ParseException
	{
		//TODO 空のArrayListにいきなりsetすると、怒られる→空文字を必要数だけ詰めるクラスEnsureを作る必要
		int[] intArr = getArrSize(arrTable);
		int cntOfR=intArr[0];
		int cntOfC=intArr[1];
		String str;
		arrayToCSV(arrTable, tempCSVpass);
		kabuLab.ReadCSV readCSV = new kabuLab.ReadCSV(tempCSVpass, true);

		//行数と列数がarrTableと同じ表arrTable2を作る
		ArrayList<ArrayList<String>> arrTable2 = new ArrayList<ArrayList<String>>();
		ArrayList<String> arrRow = new ArrayList<String>();
		arrRow.set(cntOfC, "");
		arrTable2.set(cntOfR-1, arrRow);

		//行数と列数がarrTableに対して互い違いな表arrTable3を作る
		ArrayList<ArrayList<String>> arrTable3 = new ArrayList<ArrayList<String>>();
		arrRow = new ArrayList<String>();
		arrRow.set(cntOfR, "");
		arrTable3.set(cntOfC-1, arrRow);

		for(int r=0; r<cntOfR; r++)
		{
			arrRow = new ArrayList<String>();
			for(int c=0; c<cntOfC; c++)
			{
				str=readCSV.getCell(r, c);

				//setCell(c,(cntOfR-1)-r)っぽい処理
				/*
				 * 効率重視のため、
				 * setCell((cntOfR-1)-r,c)っぽい処理をしてから
				 * 行、列の入れ替えをする
				 */
				arrRow.set(c, str);

			}
			arrTable2.set((cntOfR-1)-r, arrRow);
		}
		arrTable3 = switchRC(arrTable2);

		return arrTable3;

	}


	/**
	 * 表の行と列を入れ替える。<br>
	 * 不可視である。<br>
	 * これはMiscellaneousクラスの設計時にも利用されるため、staticを付けたが、<br>
	 * インスタンスが必要な「一時ファイルの読み込み」も避けられなかったため、<br>
	 * インスタンス無しで外部からのアクセスを可能にすることを許すわけにいかず、<br>
	 * private staticと余儀なくされたためである。<br>
	 * 可視バージョンがオーバーロードされている。<br>
	 * @see #switchRC(ArraList, boolean) 可視なswitchRC
	 * @param arrTable
	 * @return 行と列の入れ替わった表
	 * @throws FileNotFoundException
	 * @throws ParseException
	 */
	private static ArrayList<ArrayList<String>> switchRC(ArrayList<ArrayList<String>> arrTable) throws FileNotFoundException, ParseException
	{
		//2は行数と列数を入れ替えて設定
		int[] intArr = getArrSize(arrTable);
		int cntOfR_2 = intArr[1];
		int cntOfC_2 = intArr[0];
		int cntOfR_1 = intArr[0];
		int cntOfC_1 = intArr[1];

		//与えられた行列をreadCSVに読み込む
		arrayToCSV(arrTable, tempCSVpass);
		kabuLab.ReadCSV readCSV = new kabuLab.ReadCSV(tempCSVpass, true);

		//設定どおりの大きさを確保
		ArrayList<ArrayList<String>> arrTable2 = new ArrayList<ArrayList<String>>();
		ArrayList<String> arrRow = new ArrayList<String>();;
		arrRow.set(cntOfC_2, "");
		arrTable2.set((cntOfR_2)-1, arrRow);

		for(int c=0; c<cntOfC_1; c++)
		{
			arrRow = new ArrayList<String>();
			for(int r=0; c<cntOfR_1; r++)
			{
				//与えられた行列のr行目c列目を、rを変化させながら1行に読み込むことで、
				//与えられた行列のc列目が1行に並ぶ(列が行に変換される)
				arrRow.set(r, readCSV.getCell(r, c));
			}
			//与えられた行列のc列目を1行にしたものを、
			//新しい行列ではc行目として扱う
			arrTable2.set(c, arrRow);
		}

		return arrTable2;

	}

	/**
	 * 表の行と列を入れ替える。<br>
	 * オーバライドについては関連事項参照のこと。
	 * @see #switchRC(ArraList) 不可視なswitchRC
	 * @param arrTable 行と列を入れ替えたい表
	 * @param b おとり。内部利用のswitchRCとオーバーロードするためのもの。trueでもfalseでもどちらでも同じ
	 * @return 行と列の入れ替わった表
	 * @throws FileNotFoundException
	 * @throws ParseException
	 */
	public ArrayList<ArrayList<String>> switchRC(ArrayList<ArrayList<String>> arrTable, boolean b) throws FileNotFoundException, ParseException
	{
		return switchRC(arrTable);
	}

	public static int[] getArrSize(ArrayList<ArrayList<String>> arrTable)
	{
		int cntOfR=arrTable.size();
		int max=0;
		for(int r=0; r<cntOfR; r++)
		{
			if(max<arrTable.get(r).size())
			{
				max=arrTable.get(r).size();
			}
		}
		int cntOfC=max;
		int[] intArr = new int[2];
		intArr[0]=cntOfR;
		intArr[1]=cntOfC;

		return intArr;
	}
}




