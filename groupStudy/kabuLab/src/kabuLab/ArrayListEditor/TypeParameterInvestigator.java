/**
 *
 */
package kabuLab.ArrayListEditor;
import java.util.ArrayList;
/**
 * 型パラメータTがArrayList&lt;String&gt;なのか、<br>
 * 又はStringなのかを調査する。<br><br>
 * ArrayList&lt;T&gt;型がArrayList&lt;ArrayList&lt;String&gt;&gt;なのか、<br>
 * 又はArrayList&lt;String&gt;なのかを調べるのに用いる。<br><br>
 * getResult()メソッドは、ArrayList(や、その中身のArrayList)のサイズが0でない、かつ0(,0)番目がnullでないとき、<br>
 * 前者の場合は文字列"arrTable"を、<br>
 * 後者の場合は文字列"arrRow"を、<br>
 * どちらでもないがArrayList型の場合は文字列"else"を返却する。<br>
 * 本来arrTableまたはarrRowを返すはずの場合も0(,0)番目がnullの場合は文字列"else"を返却されるので要注意。<br>
 * そもそもArrayList型ですらない場合はコンパイルエラーか実行時エラーのいずれかになる(はず)。<br>
 * サイズが0のときは文字列"size0"を返し、判定は行わない。
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class TypeParameterInvestigator<T>
{
	//フィールド
	String result="";

	//コンストラクタ
	TypeParameterInvestigator(ArrayList<T> c)
	{
		if(c.size()==0)
		{
			result="size0";
//			System.out.println("サイズ0のものはjoin出来ません");
			return;
		}
		else
		{
			if(c.get(0) instanceof String)
			{
				result="arrRow";
//				System.out.println("受け取ったものはArrayList<String>型です。すなわちarrRowの仲間です。");
				return;
			}
			else
			if(c.get(0) instanceof ArrayList<?>)
			{
				ArrayList<?> d = (ArrayList<?>) c.get(0);
				if(d.size()==0)
				{
					result="size0";
//					System.out.println("サイズ0のものはjoin出来ません");
					return;
				}
				else
				{
					if(d.get(0) instanceof String)
					{
						result="arrTable";
//						System.out.println("受け取ったものはArrayList<ArrayList<String>>型です。すなわちarrTable(やarrColumn)の仲間です。");
						return;
					}
				}
			}
		}
		result="else";
//		System.out.println("受け取ったものはArrayList<String>型でもArrayList<ArrayList<String>>型でもありませんでした。");
	}

	//メソッド
	public String getResult()
	{
		return result;
	}
}


