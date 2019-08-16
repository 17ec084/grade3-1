/**
 *
 */
package xsd2Composition;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * <a href="https://gist.github.com/horikawa-bemax/2716895">horikawa-bemax氏のXMLTreeクラス</a>のほぼ丸コピ。<br>
 * リフレクションを用いてstaticメソッドのオーバーライドを可能にし、子クラスでツリーのデザインを柔軟に設計可能にした。
 *
 * @author <a href="http://github.com/17ec084">Tomotaka Hirata(17ec084)</a>
 *
 */
public class SimpleTree extends XMLTree
{
	public static String myGetNode(Node n, String str, SimpleTree x)
	{
		String rtn = "";
		if(n.getNodeType() != n.TEXT_NODE)
		{
			rtn += str + n.getNodeName();
			rtn +=
			(
				(
					n.getNodeType() == n.ELEMENT_NODE
					&&
					((Element)n).hasAttribute("id")
				)
				?
				""+ " id="+((Element)n).getAttribute("id")
				:
				""
			);
			return rtn+"\n";
		}



		return "";
	}

}
