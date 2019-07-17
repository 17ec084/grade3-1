/**
 *
 */
package kabuLab.ArrayListEditor;

import static kabuLab.ArrayListEditor.Miscellaneous.*;

import java.util.ArrayList;

/**
 * 第1引数の表の左に第2引数の表を連結する。<br>
 * どちらの引数も、表の代わりに行を渡してもかまわない。<br>
 * 結果はgetResult();で得られる。
 * @author 17ec084(http://github.com/17ec084)
 * @see kabuLab.ArrayListEditor.Joiner
 */
public class LeftJoiner<T, U> extends Joiner<T, U>
{

	private int cntOfC;
	private int cntOfR;
	private ArrayList<String> arrRow;
	private ArrayList<ArrayList<String>> arrTable = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> arrTable2 = new ArrayList<ArrayList<String>>();
	private DownJoiner
	<
		ArrayList<ArrayList<String>>,
		ArrayList<ArrayList<String>>
	> d;

	public LeftJoiner(ArrayList<T> table_or_row1, ArrayList<U> table_or_row2)
	{
		super(table_or_row1, table_or_row2);
		arrTable = Miscellaneous.tablize(table_or_row1);
		arrTable2 = Miscellaneous.tablize(table_or_row2);
		kabuLab.ArrayListEditor.DownJoiner d = new kabuLab.ArrayListEditor.DownJoiner
		(
			Miscellaneous.rotation(arrTable, LEFT)
			,
			Miscellaneous.rotation(arrTable2, LEFT)
		);
		arrTable = new ArrayList<ArrayList<String>>();
		arrTable = d.getResult();
		arrTable = Miscellaneous.rotation(arrTable, RIGHT);
		cntOfR = arrTable.size();
		cntOfC = 0;
		for(int r=0; r<cntOfR; r++)
		{
			if(cntOfC<arrTable.get(r).size())
			{
				cntOfC = arrTable.get(r).size();
			}
		}

	}

	@Override
	public int getCntOfC()
	{
		return cntOfC;
	}

	@Override
	public int getCntOfR()
	{
		return cntOfR;
	}

	@Override
	public ArrayList<ArrayList<String>> getResult()
	{
		return arrTable;
	}

	@Override
	public void join(ArrayList<ArrayList<String>> arrTable1, ArrayList<ArrayList<String>> arrTable2) {
		// TODO 自動生成されたメソッド・スタブ

	}
}
