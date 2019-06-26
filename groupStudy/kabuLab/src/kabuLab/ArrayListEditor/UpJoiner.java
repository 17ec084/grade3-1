package kabuLab.ArrayListEditor;

import java.util.ArrayList;
/**
 * DownJoinerクラスのコンストラクタに渡す表の順番を逆にしただけのクラス<br>
 * 第1引数の表の<strong>上に</strong>第2引数の表を連結する。<br>
 * どちらの引数も、表の代わりに行を渡してもかまわない。<br>
 * 結果はgetResult();で得られる。
 * @author 17ec084(http://github.com/17ec084)
 * @see kabuLab.ArrayListEditor.DownJoiner
 */
public class UpJoiner<T, U> extends DownJoiner<T, U>
{
	public UpJoiner(ArrayList<U> table_or_row1, ArrayList<T> table_or_row2)
	{
		super(table_or_row2, table_or_row1);
	}
}
