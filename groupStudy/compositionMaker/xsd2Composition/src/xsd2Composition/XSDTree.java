/**
 *
 */
package xsd2Composition;

import static superCommon.CommonRegexes.*;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * <a href="https://gist.github.com/horikawa-bemax/2716895">horikawa-bemax氏のXMLTreeクラス</a>を改造し、XSDクラスがどんなXMLを受理するか、またXSDファイルに書かれたコメントを表示するプログラムを作った。<br>
 * XSDファイルに書かれたコメントに従い、xmlファイルを生成していくことなどに応用可能。
 *
 * @author <a href="http://github.com/17ec084">Tomotaka Hirata(17ec084)</a>
 *
 */
public class XSDTree extends XMLTree
{
	private static String nameSpace = "";

	public static Object xmlTree;

	private static boolean hasBeenSchemaDone = false;

	private static boolean isUnneeded = false;

	/**
	 * ルートノードを扱っている場合、空文字(<code>""</code>)となる。<br>
	 * ルートノード直下の最初(0番目)のノードを扱っている場合、<code>"0"</code>となる。<br>
	 * ルートノード直下のn番目のノードで、その中のm番目のノードを扱っている場合、<code>"n-m"</code>となる。
	 */
	private static String currentAddress = null;

	public static String myGetNode(Node n, String str, XSDTree x) throws DescendantOfXMLTreeException
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
			rtn = processing(rtn, n);
			return rtn+(rtn.equals("")?"":"\n");
		}
	//else
//		System.out.println(str + n.getNodeValue());


		return "";
	}

	private static String processing(String nodeStr, Node n) throws DescendantOfXMLTreeException
	{
		if(!hasBeenSchemaDone)
		{
			fieldReset();
		}
		//最初にフィールドをリセット(前回のものが残っているため)

		String nodeStrWithoutNS = null;
		if(nameSpace.equals(""))//まだ名前空間が未調査の場合
			setNameSpaceName(nodeStr, n);
		else//名前空間把握済みの場合
			nodeStrWithoutNS = killNS(nodeStr);
		if(unneeded(nodeStrWithoutNS, nodeStr))
		{
			nodeStr = "";
			nodeStrWithoutNS = killNS(nodeStr);
			isUnneeded = true;
		}

		if(hasBeenSchemaDone && !n.getNodeName().matches(".*:?schema"))
		//schema要素が既に処理されているのなら
			getXmlRootName(n);

		if(currentAddress!=null && !isUnneeded)
		{
			nodeStr = setNodeInfo(n, nodeStr);
			nodeStrWithoutNS = killNS(nodeStr);
		}

		if(last(n))
		{
			hasBeenSchemaDone = false;
		}

		isUnneeded = false;

		return output(nodeStrWithoutNS, nodeStr);

	}
		private static void setNameSpaceName(String nodeStr, Node n)
		{
			if(!hasBeenSchemaDone)
				if(n.getNodeType() == n.ELEMENT_NODE)
					if(nodeStr.endsWith(":schema"))//要素「schema」が名前空間付きだったら
					{
						nameSpace = nodeStr.substring(1, nodeStr.length()-7);
						hasBeenSchemaDone = true;
					}
					else
					if(nodeStr.equals("schema"))//要素「schema」に名前空間が無かったら
					{
						nameSpace = null;
						hasBeenSchemaDone = true;
					}

		}

		private static String killNS(String nodeStr)
		{
			if(nameSpace != null)
				return nodeStr.replaceFirst(nameSpace+":", "");
			return nodeStr;
		}


		/**
		 * 無視できるXSD要素を無視し、あってはならない要素があった場合は例外を投げる。(このjavadocでスローとして示されている例外は、実際に投げられる例外の親である)
		 * <dl>
		 * <dt>
		 * schemaのコンテンツモデル(schemaに続くべきXSD要素)
		 * </dt>
		 * <dd>
		 * <code>(((include)|(import)|(redefine)|(annotation))*((((((simpleType)|(complexType)|(group)|(attributeGroup)))|(element)|(attribute)|(notation)))(annotation)*)*)</code>
		 * </dd>
		 * <dt>
		 * elementのコンテンツモデル
		 * </dt>
		 * <dd>
		 * <code>((annotation)?((simpleType)|(complexType))?(((unique)|(key)|(keyref)))*)</code>
		 * </dd>
		 * </dl>
		 * @author <a href="http://github.com/17ec084">Tomotaka Hirata(17ec084)</a>
		 */
		private static boolean unneeded(String nodeStrWithoutNS, String nodeStr) throws DescendantOfXMLTreeException
		{
			//「schema以前のすべての要素を捨てる」ので注意

			if(nodeStrWithoutNS != null)
			{
				String[] forbiddenElements =
				{
					"group",
					"attributeGroup",
					"notation",
					"unique",
					"key",
					"keyref"
				};
				for(String str : forbiddenElements)
					if(nodeStrWithoutNS.matches(" *・"+str))
						throw new CompositionMakerXsdFormatException(str+"要素は現在禁止しています。");


				String[] unneededElements =
				{
					"include",
					"import",
					"redfine",
					"annotation"
				};
				for(String str : unneededElements)
					if(nodeStrWithoutNS.matches(" *・"+str))
						return true;
				return false;
			}
			else
			{
				return !(nodeStr.matches(".+schema"));
			}
		}

		private final static byte
		CHILD_ELEMENTS_IDX		= 0,
		NODE_NAME_IDX 				= 1,
		ATTRIBUTES_IDX 			= 2,
		COMMAND_IDX 				= 3,
		OTHER_INFO_IDX 			= 4,
		NUM_OF_APPEARANCES_IDX	= 5;

		private static void getXmlRootName(Node n)
		{

			if(xmlTree==null && n.getNodeType()==Node.ELEMENT_NODE)
			{
				Object[] childElements = null; Object[] attributes = null; String command = null; String otherInfo = ""; int[] numOfAppearances = null;
				Object[] tree =
				TreeSetter.set
				(
					childElements,
					((Element)n).getAttribute("name"),
					attributes,
					command,
					otherInfo,
					numOfAppearances
				);

				/*
				tree[CHILD_ELEMENTS_IDX]		= childElements;
				tree[NODE_NAME_IDX]				= ((Element)n).getAttribute("name");
				tree[ATTRIBUTES_IDX]			= attributes;
				tree[COMMAND_IDX]				= command;
				tree[OTHER_INFO_IDX]			= otherInfo;
				tree[NUM_OF_APPEARANCES_IDX]	= numOfAppearances;
				*/

				currentAddress = "";

				xmlTree = tree;
			}

		}

			private static class TreeSetter
			{
				public static Object[] set(Object...objects)
				{
					return objects;

				}
			}


		private static String expectedTyper = null;

		private static String setNodeInfo(Node n, String nodeStr) throws DescendantOfXMLTreeException
		{

			Object[] my_n = getNodeByAddress(currentAddress);
			String indent = getIndent(nodeStr);
			String indentWithoutBullet = indent.replaceAll("・", "  ");
			String space = spaceFamily[1*3+3];
			String simpleOrComplex;
			switch(n.getNodeType())
			{
				case Node.ELEMENT_NODE:
					if(isXmlElement(n))
					{
						my_n[NODE_NAME_IDX]=((Element)n).getAttribute("name");
						int min = 1, max = 1;
						if(((Element)n).hasAttribute("minOccurs"))
						{
							String min_str = ((Element)n).getAttribute("minOccurs");
							if(min_str.matches(space+"[0-9]+"+space))
								min = Integer.parseInt(((Element)n).getAttribute("minOccurs"));
							else
								throw new CompositionMakerXsdFormatException("minOccursが自然数でありません。");
						}

						if(((Element)n).hasAttribute("maxOccurs"))
						{
							String max_str = ((Element)n).getAttribute("maxOccurs");

							if(max_str.matches(space+"unbounded"+space))
								max = Integer.MAX_VALUE;
							else
							if(max_str.matches(space+"[0-9]+"+space))
								max = Integer.parseInt(((Element)n).getAttribute("maxOccurs"));
							else
								throw new CompositionMakerXsdFormatException("maxOccursが\"unbounded\"でもなければ自然数でもありません。");
						}
						if(max<min)
							throw new CompositionMakerXsdFormatException("maxOccursがminOccursより小さいのはおかしいです。");
						int[] numOfAppearances = {min, max};
						my_n[NUM_OF_APPEARANCES_IDX]= numOfAppearances;

						String[] forbiddenAttributes
						=
						{
							"ref",
							"default",
							"fixed",
							"form",
							"abstract",
							"block",
							"final"
						};
						for(String str : forbiddenAttributes)
							if(((Element)n).hasAttribute(str))
								throw new CompositionMakerXsdFormatException("現在elementの"+str+"属性に未対応です。");

						simpleOrComplex = getSimpleOrComplex((Element)n);



						return
						indent + "要素「"+ my_n[NODE_NAME_IDX] + "」\n"+
						indentWithoutBullet+"多重度="+min+","+max+"\n"+
						indentWithoutBullet+" ・"+simpleOrComplex;
					}
					else
					if(isTyper(n))//complexTypeあるいはsimpleType
					{
						if(isExpectedTyper(n))
							if(isComplex(n))
							{
								if(isMixed((Element) n))
								{
									my_n[OTHER_INFO_IDX] += "complexType mixed=true ";
									return indentWithoutBullet + "文字データ含む";
								}
								else
								{
									my_n[OTHER_INFO_IDX] += "complexType mixed=false ";
									return indentWithoutBullet + "文字データ含まない";
								}
							}
							else
								return "";
						else
							throw new CompositionMakerXsdFormatException("complexTypeあるいはsimpleTypeは(存在するとしたら)elementの子要素として1回(ずつ)のみ許可しています。\nそれ以外の書き方についてはサポートしていません。");
					}
					else
					{
						break;
					}
				case Node.COMMENT_NODE:
					my_n[COMMAND_IDX] = n.getNodeValue();
				return indent + my_n[COMMAND_IDX];
				default :
				break;

			}
			throw new CompositionMakerXsdFormatException();

		}
			private static Object[] getNodeByAddress(String address)
			//getNodeByAddressという名前だが、DOMのNode型ではなく、xmlTreeの(配列内)配列を取り出すだけ。
			{

				int childRelativeAddress;
				String tmp;

				Object[] rtn = (Object[]) xmlTree;


				if(address.equals(""))
				{
					//ルートノードをあつかっている場合は、xmlTree全体を返す。
					return rtn;
				}

				//以下、その他の場合([0-9]+ か [0-9]+\\-[0-9]+ に必ずマッチ)

				while(address.matches("[0-9]+\\-[0-9]+"))
				{
					//例:address = "2-17-6"
					tmp = address.replaceAll("\\-[0-9]+", "");
					//例:tmp = "2"
					childRelativeAddress = Integer.parseInt(tmp);
					//例:childRelativeAddress = 2

					rtn = (Object[])((Object[])rtn[0])[childRelativeAddress];
					//rtn[0]はrtnの子要素を集めた配列。
					//rtn[0][c]はrtnのc番目の子要素

					address = address.replaceFirst("tmp"+"\\-", "");
					//例:address = 17-6
				}
				//addressに「-」が含まれなくなると、whileを抜ける。
				if(address.matches("[0-9]+"))
				//addressが数字になっていたら
				{
					childRelativeAddress = Integer.parseInt(address);
					return (Object[])((Object[])rtn[0])[childRelativeAddress];
				}
				else
				{

					class XSDTreeアドレス管理エラー extends DescendantOfXMLTreeException
					{
						XSDTreeアドレス管理エラー()
						{
							super("XSDTreeクラスにおいて、currentAddressによるノード番号管理が異常です。");
						}
					}

					try{throw new XSDTreeアドレス管理エラー();	}
					catch (XSDTreeアドレス管理エラー e)
					{
						e.printStackTrace();
					}

					return null;

				}



			}

			private static String getIndent(String nodeStr)
			{
				//例: nodeStr = "\t\t・element"
				String content = nodeStr.replaceFirst("(   )*・", "");
				return nodeStr.replaceFirst(content, "");
				//戻り値に「(   )*・」が得られる
			}



			/**
			 * <dl>
			 * <dt>
			 * 引用
			 * </dt>
			 * <dd>
			 * http://memopad.bitter.jp/w3c/schema/el_element.html
			 * </dd>
			 * </dl>
			 * xsdのelement要素は次の表にまとめられた属性を持つ。表は引用。<br>
			 * <table class="reference">  <tr>    <th valign="top" align="left" width="25%">属性</th>    <th valign="top" align="left" width="75%">説明</th>  </tr>  <tr>    <td valign="top">id</td>    <td valign="top">オプション。 要素へユニークな ID を指定する</td>  </tr>  <tr>    <td valign="top">name</td>    <td valign="top">オプション。要素の名前を指定する。親要素が schema 要素の場合、この属性は必須</td>  </tr>  <tr>    <td valign="top">ref</td>    <td valign="top">オプション。他の要素の名前を参照する。 ref 属性は、名前空間接頭辞を含めることができる。    親要素が schema 要素の場合、この属性は使用できない</td>  </tr>  <tr>    <td valign="top">type</td>    <td valign="top">オプション。組込みデータ型の名前か、simpleType または     complexType 要素の名前のいずれかを指定する。</td>  </tr>  <tr>    <td valign="top">substitutionGroup</td>    <td valign="top">オプション。この要素で置換することができる要素の名前を指定する。    親要素が schema 要素でない場合、この属性は使用できない</td>  </tr>  <tr>    <td valign="top">default</td>    <td valign="top">オプション。要素のデフォルト値を指定する    （要素の内容が単純型またはテキストのみの場合にだけ使用することができる）</td>  </tr>  <tr>    <td valign="top">fixed</td>    <td valign="top">オプション。要素の固定値を指定する    （要素の内容が単純型またはテキストのみの場合にだけ使用することができる）</td>  </tr>  <tr>    <td valign="top">form</td>    <td valign="top">オプション。要素の形式を指定する。&quot;unqualified&quot; は、    この属性は名前空間接頭辞で修飾する必要がないことを示す。    &quot;qualified&quot; は、この属性は名前空間接頭辞で修飾する必要があることを示す。    デフォルト値は、スキーマ要素の elementFormDefault 属性の値となる。    親要素が schema 要素である場合、この属性は使用できない</td>  </tr>  <tr>    <td valign="top">maxOccurs</td>    <td valign="top">オプション。この要素が親要素で出現できる最大値を指定する。    値は、任意の数値 &gt;= 0 にすることができる。また、最大数で上限を設定しない場合は、    値に &quot;unbounded&quot; を使用する。デフォルト値は 1。    親要素が schema 要素である場合、この属性は使用できない</td>  </tr>  <tr>    <td valign="top">minOccurs</td>    <td valign="top">オプション。この要素が親要素で出現できる最小値を指定する。     値は、任意の数値 &gt;= 0 にすることができる。デフォルト値は 1。     親要素が schema 要素である場合、この属性は使用できない</td>  </tr>  <tr>    <td valign="top">nillable</td>    <td valign="top">オプション。明示的な NULL 値を要素に割り当てることができるかどうかを指定する。    True は、要素のインスタンスが null 属性をもつために true へ設定することを可能にする。    null 属性は、インスタンスのXMLスキーマ名前空間の一部として定義される。デフォルトは false</td>  </tr>  <tr>    <td valign="top">abstract</td>    <td valign="top">オプション。要素がインスタンスドキュメント内で使用できるかどうかを指定する。    Trueの場合、要素がインスタンスドキュメントに出現できないを示す。    代わりに、この要素の修飾名（QName）が含まれている substitutionGroup 属性の他の要素は、    この要素の位置に出現する必要がある。デフォルトは false</td>  </tr>  <tr>    <td valign="top">block</td>    <td valign="top">オプション。派生の指定した型をもつ要素を、この要素の代わりに使用されることを防ぐ。    この値は、#all または、拡張、制約、または equivClass のサブセットであるリストを含めることができる:<ul>      <li>extension - 拡張により派生した要素の使用を防ぐ</li>      <li>restriction - 制約により派生した要素の使用を防ぐ</li>      <li>substitution - 置換により派生した要素の使用を防ぐ</li>      <li>#all - すべての派生した要素の使用を防ぐ</li>    </ul>    </td>  </tr>  <tr>    <td valign="top">final</td>    <td valign="top">オプション。element 要素の final 属性のデフォルト値を設定する。    親要素が schema 要素でない場合、この属性は使用できない。    この値は、#all または、拡張あるいは制約のサブセットであるリストを含めることができる:<ul>      <li>extension - 拡張により派生した要素を防ぐ</li>      <li>restriction - 制約により派生した要素を防ぐ</li>      <li>#all - すべての派生した要素を防ぐ</li>    </ul>    </td>  </tr>  <tr>    <td valign="top"><i>any attributes</i></td>    <td valign="top">オプション。非スキーマ名前空間を持つ他の属性を指定する</td>  </tr>  </table>
			 * @param n
			 * @return
			 */
			private static boolean isXmlElement(Node n)
			{
				//nが(XSDの)Elementであることは前提
				return killNS(((Element)n).getNodeName()).equals("element");
			}

			private static String getSimpleOrComplex(Element elm) throws CompositionMakerXsdFormatException
			{
				String simpleOrComplex;
				if(!elm.hasChildNodes())
				{
					//単純型
					simpleOrComplex = "単純型で、非独自データ型";
					if(elm.hasAttribute("type"))
						simpleOrComplex += elm.getAttribute(killNS("type"));
					else
						simpleOrComplex += "string";
				}
				else
				{
					Node ch = elm.getFirstChild();
					while(ch != null)
					{

						if(!ch.getNodeName().matches("(.+:)?((simple)|(complex))(Type)"))
							ch = ch.getNextSibling();
						else
						if(ch.getNodeName().matches("(.+:)?(simple)(Type)"))
						{
							expectedTyper = "simpleType";
							return "単純型で、独自データ型";
						}
						else
						if(ch.getNodeName().matches("(.+:)?(complex)(Type)"))
						{
							expectedTyper = "complexType";
							return "複雑型(子要素あるいは属性またはその両方を持つ)";
						}
					}
					simpleOrComplex = "単純型で、非独自データ型";
					if(elm.hasAttribute("type"))
						simpleOrComplex += elm.getAttribute(killNS("type"));
					else
						simpleOrComplex += "string";

				}
				return simpleOrComplex;
			}

			private static boolean isTyper(Node n)
			{
				return killNS(n.getNodeName()).matches("((complex)|(simple))Type");
			}

			private static boolean isExpectedTyper(Node n)
			{
				if(expectedTyper == null)
					return false;
				else
				{
					boolean rtn = killNS(n.getNodeName()).equals(expectedTyper);
					expectedTyper = null;
					return rtn;
				}
			}

			private static boolean isComplex(Node n)
			{
				//isExpelctedTyper((Node)elm)==trueは前提
				return killNS(n.getNodeName()).equals("complexType");
			}

			private static boolean isMixed(Element elm)
			{
				//isComplex((Node)elm)==trueは前提
				boolean rtn = elm.hasAttribute("mixed");
				rtn = rtn && Boolean.parseBoolean(elm.getAttribute("mixed"));
				return rtn;
			}

		private static boolean last(Node n)
		{
			boolean
			rtn = n.getNodeType()==Node.COMMENT_NODE;
			rtn = rtn && n.getNodeValue().matches("( |\t|\r|\n|(\r\n)|　)*end( |\t|\r|\n|(\r\n)|　)*");
			return rtn;
		}

		private static void fieldReset()
		{
			nameSpace = "";
			xmlTree = null;
			hasBeenSchemaDone = false;
			currentAddress = null;
		}


		private static String output(String nodeStrWithoutNS, String nodeStr)
		{
			String rtn;
			if(nodeStrWithoutNS==null)
			{
				rtn = nodeStr.replaceFirst(nameSpace+":", "");
				rtn += "\t名前空間は「"+nameSpace+"」";
			}
			else
			{
				rtn = nodeStrWithoutNS;
			}
			return rtn;
		}




}

class CompositionMakerXsdFormatException extends DescendantOfXMLTreeException
{
	CompositionMakerXsdFormatException()
	{
		super("compositionMakerで受け付けないxsdファイルです。");
	}
	CompositionMakerXsdFormatException(String msg)
	{
		super("compositionMakerを適用したいxsdファイルで、次のような形式間違いがありました。\n"+msg);
	}
}
