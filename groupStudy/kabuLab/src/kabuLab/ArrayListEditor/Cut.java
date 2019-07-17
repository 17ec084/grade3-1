/**
 *
 */
package kabuLab.ArrayListEditor;

import java.util.ArrayList;

/**
 * 表のトリミングなどを行う
 * @see #leaveNoSpace(ArrayList) <p>leaveNoSpace:<br>空白または半角スペース1つだけで構成された行、列を取り除く</p>
 * @see #easyTrim <p>easyTrim:<br>引数をテーブル, 最も上の行番号, 最も左の列番号, 最も下の行番号, 最も右の列番号の順で指定してトリミングする</p>
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class Cut
{
	/**
	 * 空白または半角スペース1文字のみのセル(以下「空白」)を切り捨て、左上に詰める<br>
	 * 具体的には、「列内すべてが空白」ならその列を消して左に詰め、<br>
	 * 「行内すべてが空白」ならその行を消して上に詰める。<br>
	 * オーバーロードされていて、第2引数に正規表現を渡すと、それにマッチするセルを空白に置き換える。
	 * @see #leaveNoSpace(ArrayList,String)
	 * @param arrTable
	 * @return
	 */
	public static ArrayList<ArrayList<String>> leaveNoSpace(ArrayList<ArrayList<String>> arrTable)
	{
		ArrayList<Integer> removeRowIndexes = new ArrayList<Integer>();
		int removeRowIndex,nextIndex;
		boolean existSpace = true;
		while(existSpace)
		{
			existSpace = false;
			for(int i=0; i<2; i++)
			{
				boolean isEmpty = true;
				int[] size = Miscellaneous.getArrSize(arrTable);
				//「行内すべてが空白」な行を探す(0回目)
				//「列内すべてが空白」な列を探す(1回目)
				for(int r=0; r<size[0]; r++)
				{
					for(int c=0; c<size[1] && isEmpty; c++)
					{
						isEmpty = arrTable.get(r).get(c).equals("") || arrTable.get(r).get(c).equals(" ");
					}
					if(isEmpty)
					//for文が終了しても、まだ空以外のセルが見つかってない
					//→行内すべてが空
					{
						arrTable.remove(r);
						size[0]--;
						r--;
						existSpace = true;
					}
					isEmpty = true;
				}
				arrTable = Miscellaneous.switchRC(arrTable);
				//行と列の入れ替え(2回実行されるから最後は元に戻る)
			}

		}
		return arrTable;

	}
	public static ArrayList<ArrayList<String>> leaveNoSpace(ArrayList<ArrayList<String>> arrTable, String spaceReg)
	{
		arrTable = Miscellaneous.replacerReg(arrTable, spaceReg, " ");
		return leaveNoSpace(arrTable);
	}

	public static ArrayList<ArrayList<String>> easyTrim(ArrayList<ArrayList<String>> arrTable, int rowMin, int columnMin, int rowMax, int columnMax)
	{
		int[] intArr = Miscellaneous.getArrSize(arrTable);
		rowMax = (rowMax <= intArr[0]) ? rowMax : intArr[0];
		columnMax = (columnMax <= intArr[0]) ? columnMax : intArr[0];
		for(int k=0; k<2; k++)
		{
			int tmp=arrTable.size()-1-rowMax;
			for(int i=0; i<rowMin; i++)
			{
				arrTable.remove(0);
			}
			for(int i=0; i<tmp; i++)
			{
				arrTable.remove(arrTable.size()-1);
			}

			rowMin = columnMin;
			rowMax = columnMax;
			arrTable = Miscellaneous.switchRC(arrTable);
		}
		return arrTable;
	}

	/**
	 * 条件に合致する行や列を取り除く、またはそれ以外を取り除く<br>
	 * 条件の指定の例:<br>
	 *・kill(2to5mod7)→インデックス番号を7で割った余りが2から5の行または列を取り除く<br>
	 * ・survive(0to1mod7 or 6mod7)→インデックス番号を7で割った余りが0,1,6のいずれかのもの以外を取り除く<br>
	 * (上記2つは等価である)<br>
	 *・kill(rate(" ?",>=0.75))→正規表現" ?"(つまり半角スペース1つだけか空文字)にマッチするセルが全体の75%以上を占めるなら取り除く<br>
	 *・survive(cnt(".*[0-9]+.*",>4))→数字を最低1文字含むセルが4つ以下のものを取り除く
	 * @param arrTable
	 * @param rowCondition 行の条件
	 * @param columnCondition 列の条件
	 * @return
	 */
/*	public static ArrayList<ArrayList<String>> compTrim(ArrayList<ArrayList<String>> arrTable, String rowCondition, String columnCondition)
	{

	}
*/

	/**
	 * 正規表現(将来的にはデータ型でも可)で指示される必要なセルのみを残しleaveNoSpaceする
	 * @see #leaveNoSpace(ArrayList)
	 * @param arrTable
	 * @param rowMin
	 * @param columnMin
	 * @param rowMax
	 * @param columnMax
	 * @return
	 */
	public static ArrayList<ArrayList<String>> survive(ArrayList<ArrayList<String>> arrTable, String regToSurvive)
	{
		String tmpMsg = "\"survive\" method will kill me.";
		int[] intArr = Miscellaneous.getArrSize(arrTable);
		int cntOfR = intArr[0];
		int cntOfC = intArr[1];

		for(int r=0; r<cntOfR; r++)
		{
			for(int c=0; c<cntOfC; c++)
			{
				if(!(arrTable.get(r).get(c).matches(regToSurvive)))
				{
					arrTable.get(r).set(c, tmpMsg);
				}
			}
		}

		return leaveNoSpace(arrTable, tmpMsg);
	}

}
