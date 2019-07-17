/**
 *
 */
package kabuLab.ArrayListEditor;

import java.util.ArrayList;

/**
 * 表を受け取り、正規表現regに合致するセルをすべて見つける。<br>
 * getR().get(i)行getC().get(i)列がi番目に合致したセル。<br>
 * またgetResult(); は 0番目にgetR()を、1番目にgetC()を持つArrayListとする。<br>
 * getCnt()は条件に合致したセルの総数
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class SearchCells
{
    //フィールド
	private int cntOfR;
	private int cntOfC;
	private int cnt = 0;
	private ArrayList<Integer> R = new ArrayList<Integer>();
	private ArrayList<Integer> C = new ArrayList<Integer>();
	private ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();

	//コンストラクタ
	SearchCells(ArrayList<ArrayList<String>> arrTable, String reg)
	{
		cntOfR = arrTable.size();
		for(int r = 0; r < cntOfR; r++)
		{
			cntOfC = arrTable.get(r).size();
			for(int c = 0; c < cntOfC; c++)
			{
				if(isMatch(r, c, arrTable, reg))
				{
					R.add(r);
					C.add(c);
				}
			}
		}
		result.add(R);
		result.add(C);
		cnt = R.size();

	}

	//メソッド
	public int getCnt()
	{
		return cnt;
	}

	public ArrayList<Integer> getR()
	{
		return R;
	}

	public ArrayList<Integer> getC()
	{
		return C;
	}

	public ArrayList<ArrayList<Integer>> getResult()
	{
		return result;
	}


	public boolean isMatch(int r, int c, ArrayList<ArrayList<String>> arrTable, String reg)
	{
		return arrTable.get(r).get(c).matches(reg);
	}
}
