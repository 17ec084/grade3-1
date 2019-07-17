/**
 *
 */
package kabuLab.ArrayListEditor;

import java.util.ArrayList;

/**
 * コンストラクタに渡された、空白等を含む表データから他に完全包含されない長方形を抽出する。<br>
 * 但し、最上辺及び最左辺の欠損はこれを許さず、このような欠損がある場合は予め引数に指示する必要がある。<br>
 * FindRectangle(arrTable, ">=24", 50)<br>
 * のようにすると面積が24以上の長方形を大きい順に最大50個抽出する<br>
 * FindRectangle(arrTable, "==24")<br>
 * あるいは<br>
 * FindRectangle(arrTable, "24")<br>
 * のようにすると、面積が24に完全一致する長方形を大きい順に最大100(＝デフォルト値)個抽出する。<br>
 * 但し<br>
 * FindRectangle(arrTable, 24)<br>
 * のようにすると、面積が1以上の長方形を大きい順に最大24個抽出するので注意。<br>
 * 単に<br>
 * FindRectangle(arrTable)<br>
 * としたなら、面積1以上の長方形を大きい順に最大100個抽出する<br>
 * FindRectangle(arrTable, "6,4")<br>
 * のようにすると、6行4列の長方形を抽出する。<br>
 * 欠損値のセル番号を指定する場合は<br>
 * FindRectangle(arrTable, "6,>0" [,100], RArr, CArr)<br>
 * とする。但しn番目の欠損値インデックスはRArr[n]行CArr[n]列とする。<br>
 * これは6行で0列以上(すなわち列の大きさは指定しない)の長方形を抽出する。<br>
 * アルゴリズムはfindRectangleメソッドについてのdocで解説。
 *
 * @see #findRectangle(ArrayList, String, int, int[], int[]) アルゴリズムの説明
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class FindRectangle
{
	//フィールド
	private ArrayList<ArrayList<String>> arrTable;
//	private ArrayList<ArrayList<String>> arrTableTmp;

	private int area;
	private boolean areaBigger, areaSmaller, areaContain;

	private int row;
	private boolean rowBigger, rowSmaller, rowContain;

	private int column;
	private boolean columnBigger, columnSmaller, columnContain;

	private int maxCnt;
	private int[] RArr;
	private int[] CArr;


	private ArrayList<ArrayList<ArrayList<String>>> rectangles;
	private boolean allEmpty;
	private int cntOfFoundRectangle;

	private String areaCond, rowCond, columnCond;

	private int intArr[] = Miscellaneous.getArrSize(arrTable);
	private int cntOfR = intArr[0];
	private int cntOfC = intArr[1];

	private ArrayList<ArrayList<Integer>> leftUpIndexes;

	//コンストラクタ
	public FindRectangle(ArrayList<ArrayList<String>> arrTable)
	{
		findRectangle(arrTable, ">=1", 100, new int[0], new int[0]);
	}
	public FindRectangle(ArrayList<ArrayList<String>> arrTable, String cond)
	{
		findRectangle(arrTable, cond, 100, new int[0], new int[0]);
	}
	public FindRectangle(ArrayList<ArrayList<String>> arrTable, int maxCnt)
	{
		findRectangle(arrTable, ">=1", maxCnt, new int[0], new int[0]);
	}
	public FindRectangle(ArrayList<ArrayList<String>> arrTable, String cond, int maxCnt)
	{
		findRectangle(arrTable, cond, maxCnt, new int[0], new int[0]);
	}
	public FindRectangle(ArrayList<ArrayList<String>> arrTable, int[] RArr, int[] CArr)
	{
		findRectangle(arrTable, ">=1", 100, RArr, CArr);
	}
	public FindRectangle(ArrayList<ArrayList<String>> arrTable, String cond, int[] RArr, int[] CArr)
	{
		findRectangle(arrTable, cond, 100, RArr, CArr);
	}
	public FindRectangle(ArrayList<ArrayList<String>> arrTable, int maxCnt, int[] RArr, int[] CArr)
	{
		findRectangle(arrTable, ">=1", maxCnt, RArr, CArr);
	}
	public FindRectangle(ArrayList<ArrayList<String>> arrTable, String cond, int maxCnt, int[] RArr, int[] CArr)
	{
		findRectangle(arrTable, cond, maxCnt, RArr, CArr);
	}

	/**
     * 長方形を実際に取り出すメソッド。<br><br>
     * 生の表データが、次の図0のようになっているとしよう。<br>白は空白であり、黒は非空白のセルである。<br>
     * <center><img src="img/findRectangle/0.png" width="250"><br>図0 長方形検出前の表データ</center><br>
     * まずfindMinimumメソッドが「左上」を検知する。すると、図1で鮮やかさ0以外の色で示したインデックスがマークされる。<br><br><br>
     * <center><img src="img/findRectangle/1.png" width="250"><br>図1 findMinimumによる左上端検出動作</center><br>
     * 現在注目しているインデックスが「左上のインデックス」であることは<br>
     * ①自分自身のインデックスにデータが存在し、<br>
     * ②左隣や上隣にデータが存在せず、<br>
     * ③かつ、真下や真右にcondの条件を満たすかそれ以上の数だけ、同じデータ型のデータの入ったセルが連続<br>
     * することの必要十分条件である。(∵すべてそろえば左上といえるし、どれか1つでも欠ければ左上として不適切)<br><br>
     * 次にfindEdgeメソッドがデータ型が連続するだけ上辺あるいは左辺をなぞる。すると例えば図2のようになる。<br><br><br>
     * <center><img src="img/findRectangle/2.png" width="250"><br>図2 findEdgeによる長方形辺検出動作</center><br>
     * 基本的にはデータ型の違いにより、重なる部分がどちらの長方形に属するかを判断できるが、<br>
     * それぞれの辺のデータ型が一致してしまった場合、判断がつかない(図2の緑及び青)。<br>
     * この場合は、プログラムはエラーで重なる部分の左上端のインデックス(図2の×)を示し、行(青)優先か列(緑)優先かを明示するように要求する。<br>
     * ここでは行優先となったとしよう。<br>
     * また、赤い左上端の点で示したものは、明らかに検出ミスである。<br>
     * この場合はプログラムは検出ミスを知ることも出来ないため、ユーザが自発的に、そのセルだけ空白で置き換えるなど対応する。<br>
     * すると、findRecが図3のように長方形を検出する。<br><br><br>
     * <center><img src="img/findRectangle/3.png" width="250"><br>図3 findRecによる長方形検出動作</center><br>
     * そして、compareメソッドがこれらの長方形の内、condの条件を満たすものだけArrayListなどにその情報を入れ、後は捨てる。<br>
     * 図4のようなデータが「残る」が、この「残り」を再び処理し、データがなくなるか、maxCntに到達するまで繰り返す。<br><br><br>
     * <center><img src="img/findRectangle/4.png" width="250"><br>図4 findMinimumによる長方形検出動作における「残り」のデータ</center><br>	 *
     *
	 * @param arrTable
	 * @param cond
	 * @param maxCnt
	 * @param RArr
	 * @param CArr
	 */
	//メソッド
	private void findRectangle(ArrayList<ArrayList<String>> arrTable, String cond, int maxCnt, int[] RArr, int[] CArr)
	{
		leftUpIndexes = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		leftUpIndexes.add(tmp);
		leftUpIndexes.add(tmp);
		tmp = null;

		//各メソッド(findMinimum, findEdgeなど)に引数をいちいち渡すのは冗長なのでフィールドへ。
		this.arrTable = arrTable;
		condReader(cond);
		this.maxCnt = maxCnt;
		this.RArr = RArr;
		this.CArr = CArr;

		rectangles = new ArrayList<ArrayList<ArrayList<String>>>();

		allEmpty = false;
		cntOfFoundRectangle = 0;

		boolean isNoError = true;

		while(!allEmpty && cntOfFoundRectangle<this.maxCnt && isNoError)
		{
//			Ensure ensure = new Ensure(cntOfR, cntOfC);
//			arrTableTmp = ensure.ensure();
			findMinimum();
			if(isNoError = findEdge())//findEdgeはエラーが存在しないことの真偽値を返す
			{
			    findRec();
			    compare();
			}
		}

	}

		private void condReader(String cond)
		{
			/*
			 * >=1
			 * ==1
			 * 6,4
			 */

			if(cond.matches(".*,.*"))
			{
				//行と列のみを指定

				area = 1;
				areaBigger = true;
				areaSmaller = false;
				areaContain = true;

				int indexOfComma=cond.indexOf(",");
				rowCond = cond.substring(0,indexOfComma);
				columnCond = cond.substring(indexOfComma+1);

				reader("row", rowCond);
				reader("column", columnCond);
			}
			else
			{
				reader("area", areaCond);
			}
		}

			public void reader(String R_C_or_A, String cond)
			{

				String RorCorACond = cond;
				int RorCorA = 0;
				boolean RorCorABigger=false,RorCorASmaller=false, RorCorAContain=false;

				if(RorCorACond.matches(" *[0-9]+ *"))
				{
					RorCorA = Integer.parseInt(rowCond);
					RorCorABigger = false;
					RorCorASmaller = false;
					RorCorAContain = true;
				}
				else
				if(RorCorACond.matches(" *< *[0-9]+ *"))
				{
					RorCorA = Integer.parseInt(rowCond);
					RorCorABigger = false;
					RorCorASmaller = true;
					RorCorAContain = false;
				}
				else
				if(RorCorACond.matches(" *<= *[0-9]+ *"))
				{
					RorCorA = Integer.parseInt(rowCond);
					RorCorABigger = false;
					RorCorASmaller = true;
					RorCorAContain = true;
				}
				else
				if(RorCorACond.matches(" *> *[0-9]+ *"))
				{
					RorCorA = Integer.parseInt(rowCond);
					RorCorABigger = true;
					RorCorASmaller = false;
					RorCorAContain = false;
				}
				else
				if(RorCorACond.matches(" *>= *[0-9]+ *"))
				{
					RorCorA = Integer.parseInt(rowCond);
					RorCorABigger = true;
					RorCorASmaller = false;
					RorCorAContain = true;
				}
				else
				{
					System.out.println("Syntax Error in findRectangle");
				}

				if(R_C_or_A.matches("row"))
				{
					row = RorCorA;
					rowBigger = RorCorABigger;
					rowSmaller = RorCorASmaller;
					rowContain = RorCorAContain;
				}
				else
				if(R_C_or_A.matches("column"))
				{
					column = RorCorA;
					columnBigger = RorCorABigger;
					columnSmaller = RorCorASmaller;
					columnContain = RorCorAContain;
				}
				else
				if(R_C_or_A.matches("area"))
				{
					area = RorCorA;
					areaBigger = RorCorABigger;
					areaSmaller = RorCorASmaller;
					areaContain = RorCorAContain;
				}

			}


		private void findMinimum()
		{
			findLUNodata();
		}
			private void findLUNodata()
			{
				String reg = " ?";


				for(int r=0; r<cntOfR; r++)
				{
					for(int c=0; c<cntOfC; c++)
					{
						if
						(
							(
								arrTable.get(r).get(c).matches(reg)
								&&
								(c==0 ||!(arrTable.get(r).get(c-1).matches(reg)))
								&&
								(r==0 ||!(arrTable.get(r-1).get(c).matches(reg)))
							)
							||
							(

							)
						{
							leftUpIndexes.get(0).add(r);
							leftUpIndexes.get(1).add(c);
						}
					}
				}
			}
		private boolean findEdge()
		{

			return true;
		}
		private void findRec()
		{
		}
		private void compare()
		{

		}
}
