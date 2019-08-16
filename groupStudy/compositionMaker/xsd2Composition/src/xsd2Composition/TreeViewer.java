/**
 *
 */
package xsd2Composition;

/**
 * そのxsdファイルがどのようなxmlを作り得るのか、およびコメントによる指示をツリーで表示する。<br>
 * @author <a href="http://github.com/17ec084">Tomotaka Hirata(17ec084)</a>
 * 但しXSDTreeの親であるXMLTreeクラスは<br>https://gist.github.com/horikawa-bemax/2716895から借りてきた。
 */
public class TreeViewer
{
	public TreeViewer(String xsdPath)
	{
		String str =  XSDTree.setDocFromPath(xsdPath, new XSDTree());
		System.out.println(str);
	}


}

