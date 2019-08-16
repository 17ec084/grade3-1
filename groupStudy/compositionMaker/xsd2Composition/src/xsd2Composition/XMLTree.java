package xsd2Composition;

import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.parsers.DocumentBuilder;
//XMLドキュメントを構築するためのクラス群
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
//XMLドキュメントの各ノードを表すクラス
import org.w3c.dom.Node;
/**
 * 拾い物。(myGetNodeメソッドや「仮想抽象化」や「仮想オーバライド」は筆者による。)<br>
 * <dl><dt>仮想抽象化:</dt><dd>
 * staticなメソッドは抽象メソッドにできず、また子クラスでのオーバライドができないが、それでは柔軟性に欠けるため、abstract宣言したかったメソッドが呼ばれたらVirtualAbstractException「仮想抽象例外」を投げるように工夫した。<br>
 * さらにリフレクションを応用し、オーバーライド同様、各子クラスで異なる中身を持つメソッドの呼び出しを実現。<br>
 * これにより、「子クラスで具体的に似非オーバライドしない限り例外を投げる、staticな抽象メソッド」に近いものが実現。(仮想抽象メソッド)</dd>
 * <dt>仮想抽象化の仕組み:</dt><dd>
 * <ol>
 * <li>
 * (publicな＝外部から呼ばれ得る)すべてのメソッドに、呼び出し元クラスのダミーインスタンスをObject型引数として持たせる。
 * </li>
 * ダミーインスタンスの代わりに、クラスの情報をClass型引数で渡す仕様でもよいだろう。(今回はそうしていない)
 * <li>
 * 親クラスの仮想抽象メソッドの中身で、ダミーインスタンスの入った引数からそのクラスが、親クラス(仮想抽象クラス)か子クラス(仮想抽象メソッドを具体的に定義したクラス)かを特定する。
 * </li>
 * 仮想抽象メソッドは本当の抽象メソッドではないため、中身を持つ。<br>
 * ここでいうクラスの特定では、is-a関係を判断するに過ぎない<code>instanceof 親クラス</code>演算子は使えない。確実に判断させるためには<br>
 * <code>(インスタンス).getClass().toString().equals((new 親クラス()).getClass().toString())</code>
 * とする。
 * <li>
 * ダミーインスタンスが親クラスのものであったなら仮想抽象例外VirtualAbstractExceptionを投げ、そうでないのならリフレクションを用いて子クラスのメソッドを呼び出す。
 * </li>
 * どういうわけかjavaにおけるリフレクションを解説するサイトではクラス名を完全に動的に指定する手法に触れないものが多く、<br>
 * クラス名をプログラムが自動で選んでインスタンスを作る、メンバにアクセスすることは不可能なのかと錯覚するほどである。<br>
 * しかしこれは<code>java.lang.ClassLoader</code>クラスを利用し、<br>
 * <code>ClassLoader.getSystemClassLoader().loadClass((インスタンスを生成したいクラス名の文字列))</code>というClass&lt;?&gt;型変数を作れば容易にできることである。
 * <li>
 * 仮想抽象メソッドを具体化する子クラスでは、本来の引数の一番最後に自分自身の型の変数を受け付ける仕様とする。
 * </li>
 * この工夫により、確かにメソッドは親クラスと子クラスで共通であるが、それらが子クラスと1対1対応しながらオーバーロードされることになる。<br>
 * これがオーバーライドと同じようなことを可能にしている。
 * <li>
 * 外部から子クラスを呼ぶときは<br>
 * <code>子クラス.メソッド((引数,)* new 子クラス());</code><br>
 * のようにする。
 * </li>無論、<code>new 子クラス()</code>はダミーインスタンスである。ここを<code>new 親クラス()</code>に変えれば例外が投げられる。<br>
 * 例:XSDTree( extends XMLTree)クラスでmyGetNodeメソッドを具体化し、setDocFromPathメソッドを呼びたいなら、<br><code>XSDTree.setDocFromPath(n,str, new XSDTree());</code><br>とする必要がある。
 * @author <a href="https://gist.github.com/horikawa-bemax/2716895">horikawa-bemax氏</a> (原ソース)
 * @author <a href="http://github.com/17ec084">Tomotaka Hirata</a> (myGetNodeと「仮想抽象化」だけ)
 *
 */
public class XMLTree
{

	static String tree="";

	public static String setDocFromPath(String path, Object clazz)
	{
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new FileInputStream(path));

			// XMLドキュメントのルート要素を取得 => root
			Node root = doc.getDocumentElement();

			// root要素以降のNodeNameをツリー形式で表示する
			putsNodeName(root, "・", clazz);
			return tree;

		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static String setDocFromNode(Node n, Object clazz)
	{
		Document doc = n.getOwnerDocument();

		// XMLドキュメントのルート要素を取得 => root
		Node root = doc.getDocumentElement();

		// root要素以降のNodeNameをツリー形式で表示する
		putsNodeName(root, "・", clazz);
		return tree;
	}

	private static boolean catchedException = false;

	public static String putsNodeName(Node n, String str, Object clazz){


		try
		{
			// Node名表示
			tree += myGetNode(n, str, clazz);
		}
		catch (DescendantOfXMLTreeException e)
		{
			e.printStackTrace();
			catchedException = true;
			return null;
		}


		// Node n の最初の子Nodeを取得 => ch
		Node ch = n.getFirstChild();

		// chがnull（子Nodeが取得できないか、NextSiblingがない）でない間ループ
		while(ch!=null){

			// 自メソッドを再帰的に呼び出す
			putsNodeName(ch, "   " + str, clazz);

			// Node ch の弟Nodeを取得 => ch
			ch = ch.getNextSibling();
			if(catchedException)
				break;
		}
		return tree;

	}

	public static String myGetNode(Node n, String str, Object clazz_) throws DescendantOfXMLTreeException
	{
		if(clazz_.getClass().toString().equals((new XMLTree()).getClass().toString()))
		{
			new VirtualAbstractException("myGetNode").printStackTrace();
			return null;
		}
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		Class<?> clazz;
		String clazz_str;
		try
		{
			if(clazz_.getClass().toString().startsWith("class "))
			{
				clazz_str = clazz_.getClass().toString().substring(6);
			}
			else
			{
				clazz_str = clazz_.getClass().toString();
			}

			clazz = loader.loadClass(clazz_str);
			//clazz.getDeclaredConstructor(Node.class, String.class, clazz_.getClass()).newInstance(n,str,clazz_);
			Method m = clazz.getDeclaredMethod("myGetNode", Node.class, String.class, clazz_.getClass());
			return (String)m.invoke(null, n, str, clazz_);//staticなら第一引数はnull


		}
		catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException e){throw new RuntimeException(e);}
		catch (InvocationTargetException e){throw new DescendantOfXMLTreeException(e);}
	}

}

class VirtualAbstractException extends Exception
{
	private static final long serialVersionUID = 1L;

	public VirtualAbstractException(String virtualAbstractMethod)
	{
		super("このクラスは「仮想抽象クラス」です。(子クラスで具体的に"+virtualAbstractMethod+"メソッドをオーバーライドし、それを呼ぶ必要があります。");
	}
}

/**
 * XMLTreeを継承するクラスでの独自例外はこのクラスを継承すること。
 * @author <a href="http://github.com/17ec084">Tomotaka Hirata(17ec084)</a>
 *
 */
class DescendantOfXMLTreeException extends Exception
{
	private static final long serialVersionUID = 1L;

	public 	DescendantOfXMLTreeException(String str)
	{
		super(str);
	}
	public 	DescendantOfXMLTreeException(Exception e)
	{
		super(e.getCause());
	}
}

