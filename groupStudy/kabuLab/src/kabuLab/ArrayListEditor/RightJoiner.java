/**
 *
 */
package kabuLab.ArrayListEditor;

import java.util.ArrayList;

/**
 * 第1引数の表の下に第2引数の表を連結する。<br>
 * どちらの引数も、表の代わりに行を渡してもかまわない。<br>
 * 結果はgetResult();で得られる。
 * @author 17ec084(http://github.com/17ec084)
 * @see kabuLab.ArrayListEditor.Joiner
 */
public class RightJoiner<T, U> extends Joiner<T, U>
{
	private int cntOfC;
	private int cntOfR;
	private ArrayList<String> arrRow;
//	private ArrayList<ArrayList<String>> arrTable;

	public DownJoiner(ArrayList<T> table_or_row1, ArrayList<U> table_or_row2)
	{
		super(table_or_row1, table_or_row2);
	}

	@Override
	public void join(ArrayList<ArrayList<String>> arrTable1, ArrayList<ArrayList<String>> arrTable2)
	{
		int cntOfC_1 = super.getCntOfC_1();
		int cntOfC_2 = super.getCntOfC_2();
		int cntOfR_1 = super.getCntOfR_1();
		int cntOfR_2 = super.getCntOfR_2();

		cntOfC = (cntOfC_1<cntOfC_2) ? cntOfC_2 : cntOfC_1;
		cntOfR = cntOfR_1+cntOfR_2;

		arrTable = new ArrayList<ArrayList<String>>();

		for(int r=0; r<cntOfR; r++)
		{
			if(r<getCntOfR_1())
			//arrTable1
			{
				arrRow = new ArrayList<String>();
				arrRow = arrTable1.get(r);
				for(int i=getCntOfC_1(); i<getCntOfC_2(); i++)
				{
					arrRow.add("");
				}
			}
			else
			//arrTable2
			{
				arrRow = new ArrayList<String>();
				arrRow = arrTable2.get(r-cntOfR_1);
				for(int i=getCntOfC_2(); i<getCntOfC_1(); i++)
				{
					arrRow.add("");
				}
			}
			arrTable.add(arrRow);
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

}
