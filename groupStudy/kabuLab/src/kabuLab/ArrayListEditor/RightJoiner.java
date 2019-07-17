package kabuLab.ArrayListEditor;

import java.util.ArrayList;
/**
 * LeftJoinerクラスのコンストラクタに渡す表の順番を逆にしただけのクラス<br>
 * 第1引数の表の<strong>上に</strong>第2引数の表を連結する。<br>
 * どちらの引数も、表の代わりに行を渡してもかまわない。<br>
 * 結果はgetResult();で得られる。<br>
 * getCntOf[C|R]_n()ではC1とC2の順番が逆になるので、<br>
 * getCntOf[C|R]_n_fixed()で取得すること。
 * @author 17ec084(http://github.com/17ec084)
 * @see kabuLab.ArrayListEditor.LeftJoiner
 */
public class RightJoiner<T, U> extends LeftJoiner<T, U>
{
	public RightJoiner(ArrayList<U> table_or_row1, ArrayList<T> table_or_row2)
	{
		super(table_or_row2, table_or_row1);
	}

	public int getCntOfR_1_fixed()
	{
		return getCntOfR_2();
	}

	public int getCntOfR_2_fixed()
	{
		return getCntOfR_1();
	}

	public int getCntOfC_1_fixed()
	{
		return getCntOfC_2();
	}

	public int getCntOfC_2_fixed()
	{
		return getCntOfC_1();
	}
}
