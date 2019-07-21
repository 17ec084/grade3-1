/**
 *
 */
package kabuLab.defaultTool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kabuLab.ArrayListEditor.Miscellaneous;

/**
 *
 * LeastSquare("y=Hoge.f(x\\|a[,b]*)");<br>
 * (ここでは説明のため引数を正規表現で示しているが実際には<b>正規表現不可</b>)<br>
 * <br>
 * ただしHogeは呼び出しを行うクラスの名前である必要があるため、このコンストラクタ呼び出しは次のように書き替えるができる。<br>
 * String className = new Object(){}.getClass().getEnclosingClass().getName();<br>
 * LeastSquare("y="+className+".f(x|a[,b]*)");<br>
 * <br>
 * 指定されたモデル関数f (compArrColumnY=f(compArrColumnX)となるようなf)で最小二乗法を適用し、パラメータa,b,...を求める。<br>
 * モデル関数はfという名前(「.」と「(」の間に示した名前)でHogeのクラスに定義されている必要がある。<br>
 * <!-- モデル関数はArrayList&lt;comp&gt;型引数を受け取り、ArrayList&lt;comp&gt;型の戻り値を返す関数とする。<br> 仮廃案中 -->
 * モデル関数はcomp型引数を受け取り、comp型の戻り値を返す関数とする。<br>
 * また、x (「(」と「|」の間の文字)やy (「=」の前の文字)はcomp[]型の実測値配列とする。<br>
 * 例えばf(x)=a√x+bxとする場合、次のようにする。<br>
 * <!-- 但しxはArrayList&lt;comp&gt;型compArrColumnX<br>
 * aやbはcomp型 -->
 * <p style="padding-left:2em;">
 *
 * <table><tbody><tr><td><code><xmp>
 *comp f(comp x, comp a, comp b)
 *{
 *	comp bx = b.mul(x.getAsStr());
 *	comp a_sqrtX = x.exp("0.5", false).mul(a.getAsStr());
 *	return a_sqrtX.add(bx.getAsStr());
 *}
 *
 *
 *comp[] x = {new comp("1"), new comp("2"), new comp("3")};
 *comp[] y = {new comp("2"), new comp("3.5"), new comp("4.9")};
 *String className = new Object(){}.getClass().getEnclosingClass().getName();
 *LeastSquare ls = new LeastSquare(className+"f(x|a,b)");
 *
 * </xmp></code></td></tr></tbody></table>
 * </p>
 *
 * コンストラクタ呼出し後は、getParams()で、求められたパラメータのArrayListを得ることができる。<br>
 * また、predict(comp x)で、求められた近似曲線にxを代入したものが求められる。
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class LeastSquare
{
	private String y;
	private String f;
	private String x;
	private String[] Params;

	LeastSquare(String str)
	{
		if(reader(str))
		{

		}
		else
		{
			System.out.println("LeastSquareクラスの実行時にエラー:文法が誤っています");
		}
	}

	boolean reader(String str)
	{
		String var = Miscellaneous.javaNameRoleAsReg();
		String cla = var+"(\\."+var+")*( *< *"+var+"( *,"+var+" *)* *>";
//		Pattern p = Pattern.compile(" *"+ var + " *= *"+ cla + " *\\( *"+var+" *\\| *"+var+"( *,"+var+" *)* *\\) *");		
		Pattern p = Pattern.compile(" *"+ var + " *= *"+ cla + " *\\( *"+var+" *\\| *"+var+"( *,"+var+" *)* *\\) *");
		Matcher m = p.matcher(str);
		String str1 = "y=Hoge.f(x\\|a[,b]*)";

	}
}
