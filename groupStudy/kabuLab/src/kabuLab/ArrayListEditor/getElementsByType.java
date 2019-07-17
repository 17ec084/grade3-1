/**
 *
 */
package kabuLab.ArrayListEditor;

import static kabuLab.ArrayListEditor.Miscellaneous.*;

import java.util.ArrayList;

/**
 * @author 17ec084(http://github.com/17ec084)
 * DPPPLを作るとき、javaのコードを呼ばずにクラスやメソッドの呼び出しだけで済むようにしたい。<br>
 * そのために作ったメソッドのうち、分類の難しいものを集めたクラスである。<br>
 * static参照を前提としている。<br>
 * 必要な機能は適宜ここに追加したい。また理由がなければpublic staticとする。
 */
public class getElementsByType
{
	/**
	 * 表データを受け取り、10進実数値(指数表現含む)データが入っているセル、その値を求める。<br>
	 * 但し指数表現はeを用いたものとする
	 * @return ArrayList&lt;ArrayList&lt;String&gt;&gt;型。0要素目は行、1要素目は列、3番目はその数値を示す。
	 */
	public static ArrayList<ArrayList<String>> real(ArrayList<ArrayList<String>> arrTable)
	{
		return getElementByReg(arrTable, "[+-]?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)([eE][+-]?[0-9]+)?");
		//参考:https://qiita.com/hitsumabushi845/items/1b1a4921d515f662b416

	}


	/**
	 * (内部利用用)
	 */
	private static ArrayList<ArrayList<String>> getElementByReg(ArrayList<ArrayList<String>> arrTable, String reg)
	{
		ArrayList<ArrayList<String>> arrRtn = new ArrayList<ArrayList<String>>();
		int[] tmp = getArrSize(arrTable);
		int cntOfR = tmp[0];
		int cntOfC = tmp[1];
		tmp = null;
		ArrayList<String> tmp2 = new ArrayList<String>();
		arrRtn.add(tmp2);
		tmp2 = new ArrayList<String>();
		arrRtn.add(tmp2);
		tmp2 = new ArrayList<String>();
		arrRtn.add(tmp2);
		for(int i=0; i<cntOfR; i++)
		{
			for(int j=0; j<cntOfC; j++)
			{
				if(arrTable.get(i).get(j).matches(reg))
				{
					arrRtn.get(0).add(""+i);
					arrRtn.get(1).add(""+j);
					arrRtn.get(2).add(arrTable.get(i).get(j));
				}
			}
		}
		return arrRtn;
	}

	/**
	 * 表データを受け取り、regの正規表現にマッチするデータが入っているセル、その値を求める。<br>
	 * ユーザがデータ型を定義して、そのデータ型とみなせるセルやその値を求めるものともいえる
	 * @return ArrayList&lt;ArrayList&lt;String&gt;&gt;型。0要素目は行、1要素目は列、3番目はその数値を示す。
	 */
	public static ArrayList<ArrayList<String>> UDDT(String reg, ArrayList<ArrayList<String>> arrTable)
	{
		return getElementByReg(arrTable, reg);
	}

}
