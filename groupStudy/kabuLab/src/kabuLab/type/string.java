/**
 *
 */
package kabuLab.type;

import java.util.ArrayList;

/**
 * 文字列型の機能を提供するクラス<br>
 * 文字列型はすべての型の抽象としたため、このクラスは他のすべての機能提供クラスから継承される。<br>
 * <!-- そのため一番堅苦しく書く必要がある -->
 * 非static参照を前提に設計される。<br>
 * <p><b>newContent</b><br>更新後のセルの内容。</p>
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class string extends Universe
{

	//フィールド
	protected String newContent, oldContent;

	public string(String cellContent)
	{
		newContent = cellContent;
		if(!accept())
		//cellContent(セル内容)が型に解決可能でない場合
		{
			newContent = "";
		}

	}
		protected boolean accept()
		{
			int[] arr = {};
			return tmp(arr);
		}
			protected boolean tmp(int[] arr)
			{
				Detect d = new Detect(newContent);
				int autoCastNum = d.autoCast();
				boolean isSuperAccept = super.accept();

				if(isSuperAccept)
				{
					//親集合が解決する場合、

					for(int i:arr)
						if(autoCastNum == i)
						//親(にあって自分に無い要素(型))が最も具体的に解決するのなら
							return false;
							//自分は解決できない
					return true;
					//そうでないなら自分が解決できる
				}
				return false;
				//親が解決できないものを自分は解決できない
			}


	//メソッド
	/**
	 * 変換可能な型を列挙(サブクラスでのオーバーライド不要)
	 */
	public ArrayList<Integer> getConvertableTypes()
	{
		ArrayList<Integer> rtn = new ArrayList<Integer>();
		for(int i=0; i<Detect.types.length; i++)
			if(newContent.matches(Detect.types[i]))
				rtn.add(i);
		return rtn;
	}



}
