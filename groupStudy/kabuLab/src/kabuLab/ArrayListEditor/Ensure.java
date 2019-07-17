/**
 *
 */
package kabuLab.ArrayListEditor;

import java.util.ArrayList;

/**
 * ArrayList&lt;ArrayList&lt;String&gt;&gt;型arrTableのサイズを定義し、その分を確保する。各セルはすべてから文字列が入る<br>
 * <p>使い方1:<br>Ensure ensure = new Ensure(3,2);<br>arrTable=ensure.ensure();<br>とすると、arrTableは3行2列となり、各列はすべて半角スペース1つが入る。</p>
 * <p>使い方2:<br>Ensure ensure = new Ensure(3,2,"hello world");<br>arrTable=ensure.ensure();<br>とすると、arrTableは3行2列となり、各列はすべて"hello world"が入る。</p>
 * @param r 行数(コンストラクタのパラメータ)
 * @param c 列数(コンストラクタのパラメータ)
 * @param arrTable 表(コンストラクタのパラメータ)
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class Ensure
{
	//フィールド
	private int r;
	private int c;
//	private ArrayList<ArrayList<String>> arrTable;
	private String str;

	//コンストラクタ
	Ensure(/* ArrayList<ArrayList<String>> arrTable, */int r, int c)
	{
		this.r=r;
		this.c=c;
//		this.arrTable=arrTable;
		this.str=" ";

	}
	Ensure(/*ArrayList<ArrayList<String>> arrTable, */int r, int c, String str)
	{
		this.r = r;
		this.c = c;
//		this.arrTable = arrTable;
		this.str = str;
	}

	//メソッド
	public ArrayList<ArrayList<String>> ensure()
	{
		ArrayList<String> arrRow;
		ArrayList<ArrayList<String>> arrTable = new ArrayList<ArrayList<String>>();
		for(int i=0; i<r; i++)
		{
			arrRow = new ArrayList<String>();
			for(int j=0; j<c; j++)
			{
				arrRow.add(str);
			}
			if(str.length()==0)
			{
				//CSVReaderの都合で、最終列が空文字の場合、その列は消されてしまうので、1つ余分に列をとる。
				arrRow.add(str);
			}
			arrTable.add(arrRow);
		}
		return arrTable;
	}
}
