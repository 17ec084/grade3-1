/**
 *
 */
package kabuLab;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import kabuLab.CSVReader.ParseException;
/**
 * CSVを読み込み、2次元ArrayList&lt;String&gt;にする。<br>
 * <p><strong>仕様1</strong>:\u0000(eof)、\u001d(改行)、\u001e(次の列)の入力を禁ずる。</p>
 * <p><strong>仕様2</strong>:処理の単純化の都合上、<font color="red">列の最後が空文字の場合、その列は削除される</font>。<br>
 * 例:<br>
 * a,a,a<br>
 * a,a,a<br>
 * a,a,a<br>は3行3列として解析されるが、<br>
 * ,,<br>
 * ,,<br>
 * ,,<br>は3行<font color="red">2列</font>と認識される。</p>
 * 最低限modeパラメータとCSVのパス(mode=true)あるいはCSV形式テキスト(mode=false)が必要だが、<br>
 * そのような場合は改行コードは「\r\n」、区切り文字列は「,」とみなされる。<br>
 * 但し改行コードと区切り文字はオプションで変更可<br>
 * また、
 * @author 17ec084(http://github.com/17ec084)
 * @see kabuLab.CSVReader.CSVReader kabuLab.CSVReader.CSVReader(列が削除される理由を解説(英語))
 */
public class ReadCSV
{
    //フィールド
    private String pass;
    private String text;
    private String newRow;
    private String newColumn;
    final public char newR=29;
    final public char newC=30;
    final public char eof=0;
    protected ArrayList<ArrayList<String>> arrTable;
    private ArrayList<String> arrRow;
    private int cntOfR;
    private int cntOfC;

	//コンストラクタ
    /**
     * readCSVメソッドに、CSV形式の文字列を取得させている。<br>
     * ファイルからの読み込みも可能
     * 参考:https://www.sejuku.net/blog/20924
     * @param passOrText
     * @param mode
     * @throws ParseException
     * @throws FileNotFoundException
     * @see #readCSV
     */
    public ReadCSV(String passOrText, boolean mode) throws FileNotFoundException, ParseException
    {
    	newRow="\r\n";
    	newColumn=",";
    	readCSV(passOrText, mode);
    	csvToArray();
    	rectanglize();//長方形化(各行において列数を統一にする)
    }

    public ReadCSV(String passOrText, boolean mode, String newRow, String newColumn) throws FileNotFoundException, ParseException
    {
    	this.newRow=newRow;
    	this.newColumn=newColumn;
    	readCSV(passOrText, mode);
    	csvToArray();
    	rectanglize();//長方形化(各行において列数を統一にする)
    }

    //メソッド
    /**
     * 本来コンストラクタがやるべき処理をこのメソッドで行っている。<br>
     * (改行文字などのオプションに、コンストラクタが柔軟な対応をできるようにするため)<br>
     * また、2バイト文字は
     * @see kabuLab.ReadCSV#ReadCSV
     */
    public void readCSV(String passOrText, boolean mode)
    {
    	text="";
    	if(mode)
    	{
    		pass=passOrText;
	        try
	        {
    		    // 1.ファイルのパスを指定する
    		    File file = new File(pass);

    		    // 2.ファイルが存在しない場合に例外が発生するので確認する
    		    if (!file.exists())
                {
    		        System.out.print("ファイルが存在しません");
    		        return;
    		    }

    		    // 3.FileReaderクラスとreadメソッドを使って1文字ずつ読み込み表示する
    		    FileReader fileReader = new FileReader(file);
    		    int data;
    		    while ((data = fileReader.read()) != -1)
    		    {
    		        text+=String.valueOf((char) data);
    		    }


    		    // 4.最後にファイルを閉じてリソースを開放する
    		    fileReader.close();

    		}
	        catch (IOException e)
	        {
    		    e.printStackTrace();
    		}
    	}
    	else
    	{
    		text=passOrText;
    	}

//    	System.out.println("text(ReadCSV.java.104)="+text);
    	text+=String.valueOf(eof);
    	text=text.replaceAll(newRow,String.valueOf(newR));
    	text=text.replaceAll(newColumn,String.valueOf(newC));
//    	System.out.println("text(ReadCSV.java.108)="+text);
/*
		for(int i=0; i<text.length(); i++)
		{
			System.out.println(text.charAt(i)+"="+(int)text.charAt(i));
		}

*/
    }


    /**
     * CSV形式の文字列を2次元ArrayList&lt;String&gt;に変換。
     * @throws ParseException
     * @throws FileNotFoundException
     * @see kabuLab.CSVToArray
     */
    public void csvToArray() throws FileNotFoundException, ParseException
    {
    	kabuLab.CSVReader.CSVReader arr = new kabuLab.CSVReader.CSVReader(text);
    	arrTable=arr.getArrTable();
    	cntOfR=arr.getCntOfR();
    	cntOfC=arr.getCntOfC();

    	/*
    	arrTable= new ArrayList<ArrayList<String>>();
    	arrRow= new ArrayList<String>();
    	int i=0;
    	int j=0;

    	boolean isEof=false;
    	//ループを止める条件「text.charAt(i)==eof」がそろったとき、連鎖的にbreakするため

    	cntOfR=0;
    	cntOfC=0;
    	int maxC=0;
    	while(text.charAt(i)!=eof)
    	{
    		if(maxC<cntOfC){maxC=cntOfC;}
    		cntOfC=0;
    		while(text.charAt(i)!=eof && (i==0||(i!=0 && text.charAt(i-1)!=newR)))
    		{
                while(text.charAt(i)!=newC && text.charAt(i)!=newR)
                {
                	if(isEof || text.charAt(i)==eof)
                	{
                		isEof=true;
                        break;
                	}
					System.out.println("text.charAt("+i+") (ReadCSV.java.145)="+text.charAt(i)+"="+(int)text.charAt(i));
                	i++;
                }
                cntOfC++;
                isEof=putCell(i,j,isEof);
                //arrRow(行を表すArrayList)にセル内容を追加
            	if(isEof || text.charAt(i)==eof)
            	{
            		isEof=true;
                    break;
            	}
                j=i;
                i++;
                System.out.println("i="+i);

    		}
    		cntOfR++;

//    		System.out.println("cntOfR (ReadCSV.java.157)="+cntOfR);
    		//System.out.println(arrRow.get(0));
    		arrTable.add(arrRow);
    		arrRow= new ArrayList<String>();
    		//重要:これをarrRow.clear();で代用しようとすると、
    		//(arrTableには参照(が値)渡しされているので)
    		//arrTable内の行が消されることになってしまう。

    		if(text.charAt(i)!=eof && (i==0||(i!=0 && text.charAt(i-1)==newR)))
    		{

    		}

    		if(isEof || text.charAt(i)==eof)
        	{
        		isEof=true;
                break;
        	}
    		i++;
    	}
		if(maxC<cntOfC){maxC=cntOfC;}
    	cntOfC=maxC;
    	//cntOfR--;

    	*/
    }

/*
        private boolean putCell(int i, int j, boolean isEof)
        {
            if(i!=j)
            {
            	arrRow.add(text.substring(j, i));
            	System.out.println("text.substring("+j+", "+i+") ReadCSV.java.182 ="+text.substring(j, i).replaceAll(String.valueOf(newR),";").replaceAll(String.valueOf(newC),","));
        	    if(isEof || text.charAt(i)==eof)
            	{
            		isEof=true;
            	}
//        	    System.out.println("arrRow="+arrRow.get(0));
            }
            else
            {
            	arrRow.add("");
        	    if(isEof || text.charAt(i)==eof)
            	{
            		isEof=true;
            	}
            }
            return isEof;
	    }
*/


		/**
         * 行数。<br>
         *
         */
        public int getCntRow()
        {
        	return cntOfR;

        	//参考:https://asmblr.hatenadiary.org/entry/20101013/1286989502
        	//return (text.length() - text.replaceAll(newRow,"").length() / newRow.length());
        }

        /**
         * 列数。
         */
        public int getCntColumn()
        {

        	return cntOfC;
        }

    /**
     * 各行において、列数を最大のものに統一化する
     * (長方形化)
     */
    public void rectanglize()
    {
        ArrayList<String> arr = new ArrayList<String>();
        for(int i=0; i<cntOfR; i++)
        {
            arr = arrTable.get(i);
        	//cntOfC-arr.size()は各行の列数(要素数)
            for(int j=0; j<cntOfC-arr.size(); j++)
            {
                arr.add("");
            }
        }
    }



    /**
     * 指定された行と列にある文字列を返す
     * @param rowIndex 行インデックス
     * @param columnInde 列インデックス
     * @return 取り出された文字列
     */
    public String getCell(int rowIndex, int columnIndex)
    {
    	boolean isExist=arrTable.size()>rowIndex;
    	isExist=isExist && arrTable.get(rowIndex).size()>columnIndex;
    	if(isExist)
    	{
    	    return arrTable.get(rowIndex).get(columnIndex);
    	}
    	else
    	{
    		return "";
    	}
    }

    /**
     * 指定された行を1次元ArrayList&lt;String&gt;として取り出す
     * @param rowIndex 行インデックス
     * @return 取り出された1次元ArrayList&lt;String&gt;
     */
    public ArrayList<String> getRow(int rowIndex)
    {
    	return arrTable.get(rowIndex);
    }

    /**
     * 指定された列を2次元ArrayList&lt;String&gt;として取り出す
     * @param columnIndex 列インデックス
     * @return 取り出された2次元ArrayList&lt;String&gt;
     */
    public ArrayList<ArrayList<String>> getColumn(int columnIndex)
    {
    	ArrayList<ArrayList<String>> arrColumn = new ArrayList<ArrayList<String>>();
    	ArrayList<String> arr = new ArrayList<String>();
    	for(int i=0; i<cntOfR; i++)
        //列の要素数(Columnのためのループ回数)は行数(cntOfR)に等しい
    	{
    		arr.add(getCell(i, columnIndex));
            arrColumn.set(i, arr);
            arr.clear();
    	}
    	return arrColumn;
    }

}
