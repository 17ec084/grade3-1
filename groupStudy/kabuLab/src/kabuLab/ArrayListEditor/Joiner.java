/**
 *
 */
package kabuLab.ArrayListEditor;

import java.util.ArrayList;

/**
 * 表や行、列を連結するすべてのクラスが継承する抽象クラス<br>
 * 型パラメータの型の判断はTPIクラスで出来る。
 * @author 17ec084(http://github.com/17ec084)
 * @param <T> Stringまたは ArrayList&lt;String&gt;
 * @param <U> Stringまたは ArrayList&lt;String&gt;
 * @see kabuLab.ArrayListEditor.TPI
 *
 */
public abstract class Joiner<T, U>
{
	//フィールド
	/* cntOfRやcntOfCはprivateとすべきだが、
	 * privateなメンバは具体クラスに継承されない。
	 * 参考: https://docs.oracle.com/javase/specs/jls/se8/html/jls-8.html#jls-8.2
	 * したがって、抽象クラスのフィールドにかくべきではないため、記述しない。
	 * また、これらのgetterは抽象化しておく
	 */
	private static int cntOfR_1;
	private static int cntOfR_2;
	private static int cntOfC_1;
	private static int cntOfC_2;
	private ArrayList<ArrayList<String>> arrTable1 = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> arrTable2 = new ArrayList<ArrayList<String>>();
	protected ArrayList<ArrayList<String>> arrTable = new ArrayList<ArrayList<String>>();

	//コンストラクタ
	public Joiner(ArrayList<T> table_or_row1, ArrayList<U> table_or_row2)
	{
		TPI a = new TPI(table_or_row1);
		String typeOf1 = a.getResult();
		a = new TPI(table_or_row2);
		String typeOf2 = a.getResult();

		Boolean isToBRoT
		//2つのtable_or_rowがどちらも(arrRowまたはarrTableのいずれか)
		//(isTypeOfBothRowOrTableの略)
		=
		(
			typeOf1.equals("arrRow")
			||
			typeOf1.equals("arrTable")
		)
		&&
		(
			typeOf2.equals("arrRow")
			||
			typeOf2.equals("arrTable")
		);
		if(!isToBRoT)
		{
			System.out.println
			(
					"エラー。\nJoinerの継承クラスにまったく別の型が渡されたか、"
				+	"先頭にnullのあるものが渡されたかサイズ0のものが渡されたか、"
				+	"いずれかが発生しました。"
			);
			return;
		}
		if(typeOf1.equals("arrRow"))
		{
			arrTable1.add((ArrayList<String>)table_or_row1);
		}
		if(typeOf2.equals("arrRow"))
		{
			arrTable2.add((ArrayList<String>)table_or_row2);
		}
		if(typeOf1.equals("arrTable"))
		{
			arrTable1=(ArrayList<ArrayList<String>>) table_or_row1;
		}
		if(typeOf2.equals("arrTable"))
		{
			arrTable2=(ArrayList<ArrayList<String>>) table_or_row2;
		}
		counter();
		join(arrTable1, arrTable2);
	}


	//メソッド

	/**
	 * 具体クラスで処理を記述するための抽象メソッド<br>
	 * 戻り値は処理の施された表。
	 */
	abstract public void join(ArrayList<ArrayList<String>> arrTable1, ArrayList<ArrayList<String>> arrTable2);

	/**
	 * 具体クラスにおいて定義されるべき、結合後の表の列数のgetter
	 */
	abstract public int getCntOfC();

	/**
	 * 具体クラスにおいて定義されるべき、結合後の表の行数のgetter
	 */
	abstract public int getCntOfR();

	/**
	 * cntOfC_1～cntOfR_2に適切な値を代入するメソッド。<br>
	 * ゲッタで直接処理するより、ここでまとめてコンストラクタによる自動呼出し専用にしておいた方が、<br>
	 * 無駄な処理が省けるのではなかろうかという考えから。
	 */
	private void counter()
	{
		int max=0;
		for(int r=0; r<arrTable1.size(); r++)
		{
			if(arrTable1.get(r).size()>max)
			{
				max=arrTable1.get(r).size();
			}
		}
		cntOfC_1=max;


		max=0;
		for(int r=0; r<arrTable2.size(); r++)
		{
			if(arrTable2.get(r).size()>max)
			{
				max=arrTable2.get(r).size();
			}
		}
		cntOfC_2=max;

		cntOfR_1=arrTable1.size();
		cntOfR_2=arrTable2.size();
	}

	public ArrayList<ArrayList<String>> getResult()
	{
		return arrTable;
	}

	public int getCntOfC_1()
	{
		return cntOfC_1;
	}

	public int getCntOfC_2()
	{
		return cntOfC_2;
	}

	public int getCntOfR_1()
	{
		return cntOfR_1;
	}

	public int getCntOfR_2()
	{
		return cntOfR_2;
	}
}
