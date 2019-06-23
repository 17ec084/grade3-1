/**
 *
 */
package kabuLab;

import java.io.FileWriter;
import java.io.IOException;
//import java.util.ArrayList;
import java.util.List;

/**
 * ArrayList&lt;String&gt;を列データCSVに変換します。<br>
 * コンストラクタでは最低限ArrayList&lt;String&gt; list <br>
 * を必要としますが、<br>
 * オプションで改行コードを指定できます
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class ListToCSV
{
    //フィールド
	private List<String> list;
	private int size;
	private String str;
	private String newRow;

	//コンストラクタ
	public ListToCSV(List<String> list)
	{
		this.list=list;
		newRow="\r\n";
	}
	public ListToCSV(List<String> list, String R)
	{
		this.list=list;
		newRow=R;
	}
	//メソッド
	/**
	 *
	 * @return CSVファイルに対応する文字列
	 */
	public String putCSV()
	{
        size=list.size();
        str="";
        for(int i=0; i<size-1; i++)
        {
        	str+=list.get(i);
        	str+=newRow;
        }
        str+=list.get(size-1);
        return str;
	}

	/**
	 * 参考:https://eng-entrance.com/java-file-output  <br>
	 * passに指定されたアドレスにCSVファイルを生成
	 */
	public void putCSV(String pass)
	{
        try
        {
            FileWriter fw = new FileWriter(pass);
            fw.write(putCSV());
            fw.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
	}
}
