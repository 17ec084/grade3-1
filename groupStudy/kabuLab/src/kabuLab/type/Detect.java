package kabuLab.type;

/**
*
*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* 指定された文字列、セル、行が、DPPPLの何型であるか調べる<br>
* (DPPPLのデータ型はここで定義する)<br>
* 仕様:複素数は(実部)(半角スペース1つ以上)(虚部)(虚数記号)とせよ。それ以外は受け付けん。
* @author 17ec084(http://github.com/17ec084)
*
*/
public class Detect
{
	private String str;
	public int type;
	protected HashMap<Integer,Object> numToType = new HashMap<Integer,Object>();

	protected static String[] types =
	{
	//0→hirata
	" *<hirata>(.*)</hirata>.*",
	//1→space
	" ?",
	//2→bool2
	"[TF10真偽tf(はい)(いいえ)]|((N|n)(O|o)?)|(Y|y)((E|e)(S|s))?",
	//3→bool4
	"[TF!\\?1032真偽(矛盾)(不明)tf(はい)(いいえ)]|((N|n)(O|o)?)|(Y|y)((E|e)(S|s))?",
	//4→bits
	"[01]+",
	//5→int4
	"[q|Q]?[0-3]+",
	//6→int8
	"[0-7]+",
	//7→int16
	"(0[xX])?[0-9a-f]+",
	//8→int36
	"0((a[0-9a-z]+)|(A[0-9A-Z]+))",
	//9→int62
	"0(a|A)[0-9a-zA-Z]+",
	//10→uint
	"\\+?[0-9]+",
	//11→sint
	"(\\-?0|(\\-[0-9]+))",
	//12→integ
	//0のときはintegとみる
	"[\\+\\-]?[0-9]+",
	//13→fract
	"(\\-|\\+)?[0-9]+/(\\-|\\+)?[0-9]+",
	//14→point
	"(\\-|\\+)?([0-9]*\\.[0-9]+)|([0-9]+)",
	//15→sttc
	"([Pp][Ii])|π|(3\\.14\\.\\.\\.)|([Ee]([Xx][Pp])?)|(2\\.7(1|2)?\\.\\.\\.)",
	//16→exp
	"(\\-|\\+)?[1-9](\\.[0-9]+)?[eE][\\+\\-]([0-9]+)",
	//17→expN
	"(\\-|\\+)?[1-9a-zA-Z](\\.[0-9a-zA-Z]+)?\\*(((0)?[2-9])|([1-5][0-9])|(6[01]))\\^[0-9]+",
	//18→real
	"",
	//19→purei
	"",
	//20→comp
	"",
	//21→date
	"(19[0-9]{2}|20[0-9]{2})(/|年)([1-9]|1[0-2])(/|月)([1-9]|[12][0-9]|3[01])(日)?",
	//22→time
	"((午[前後])|([AP]M))? *[0-9]{2}[時:][0-9]{2}(分)?([:][0-9]{2}[秒])?",
	//23→dandt
	""
	};
	//24→string

	//以下、メタ部でのキャスト用
	//0→measured
	//100→theoretical
	//1000→placeOnEarth
	//2000→comment
	//3000→label
	//4000→survive

	void prepare()
	{
   	//real = "(("+integ+")|("+exp+")|("+sttc+")|("+point+")|("+fract+"))";
   	types[18] =  "(("+types[12]+")|("+types[16]+")|("+types[15]+")|("+types[14]+")|("+types[13]+"))";
   	//purei = (real|+|-)+"(i|j|(([sS][qQ][rR][tT]|√)(\\(\\-1\\)|(-1))))";
   	types[19] = "("+types[18]+"|\\+|\\-)?"   +    "(i|j|(([sS][qQ][rR][tT]|√)(\\(\\-1\\)|(-1))))";
   	//comp = real+"[\\+\\-]"+purei;
   	types[20] = types[18]+" +[\\+\\-]"+types[19];
   	//dandt = ".*"+date+".*"+time+".*";
   	types[23] = ".*"+types[21]+".*"+types[22]+".*";

	}

   Detect(String str)
   {
   	this.str = str;
   	prepare();


   	type = autoCast();
   }



   Detect(ArrayList<ArrayList<String>> arrTable, int r, int c, int metaR)
   {
   	this.str = arrTable.get(r).get(c);
   	prepare();


   	type = autoCast1(arrTable,metaR,c);
   }

   Detect(ArrayList<ArrayList<String>> arrTable, int r, int c)
   {
   	this(arrTable, r, c, 0);
   }

   Detect(ArrayList<String> arrRow)
   {
	   prepare();
	   type = autoCast2(arrRow);
   }

	int autoCast()
	//オートキャスト①
	{
   	for(int i=0; i<=9;i++)if(str.matches(types[i]))return i;
   	//平田型からint62型まで
   	if(str.matches("0+"))return 12;
   	//0,00,000,...にマッチした場合はint

   	for(int i=10; i<=23;i++)if(str.matches(types[i]))return i;
   	//以降すべて

		return 24;

	}

   int autoCast1(ArrayList<ArrayList<String>> arrTable, int metaR, int c)
   //オートキャスト①
   {
   	int typeNum = 0;
   	String metaData = arrTable.get(metaR).get(c);
   	String regex = " *<hirata>(.*)cast *=(.*)</hirata>.*";
   	if(metaData.matches(regex))
   	{
   		Pattern p = Pattern.compile(regex);
   		Matcher m = p.matcher(metaData);
   		if(m.matches())
   		{
   			typeNum = Integer.parseInt(m.group(2));
   		}
   	}
   	if(typeNum % 100 != 0)
   		return typeNum;
   	return typeNum+autoCast();

   }


   int autoCast2(ArrayList<String> arrRow)
   //オートキャスト②
   {
   	int typeNum = 0;
   	String metaData = arrRow.get(0);
   	String regex = " *<hirata>(.*)cast *=(.*)</hirata>.*";
   	if(metaData.matches(regex))
   	{
   		Pattern p = Pattern.compile(regex);
   		Matcher m = p.matcher(metaData);
   		if(m.matches())
   		{
   			typeNum = Integer.parseInt(m.group(2));
   		}
   	}
   	if(typeNum % 100 != 0)
   		return typeNum;
   	return typeNum+cast(arrRow);

   }

   	int cast(ArrayList<String> arrRow)
   	{
   		int t = 24;
       	ArrayList<Integer> arrRow2 = new ArrayList<Integer>();
       	for(int i=0; i<arrRow.size(); i++)
       		if(!arrRow.get(i).matches(types[1]))
       		{
       			str = arrRow.get(i);
       			t = autoCast();
       			if(!arrRow2.contains(t))
       				arrRow2.add(t);
       		}
       	//空白要素を飛ばし、しかもオートキャスト①の結果で置き換える。(同じ型のものは飛ばす)

       	if(arrRow2.size()==0)
       		return 1;
       	//何も格納されなかったなら、すべて空白要素だったとわかる

       	Collections.sort(arrRow2);
       	//昇順に並び替える

       	String typesStr = "";
       	for(int i=0; i<arrRow2.size(); i++)
        		typesStr = typesStr + arrRow2.get(i) + ",";
       	//型の番号を並べた文字列。
       	//例えば平田型と空白型と文字列型なら「0,1,24,」

       	if(typesStr.matches("([07-9]|(17)),"))
       		return Integer.parseInt(typesStr.replace(",", ""));

       	/*
       	 * まず、平田型、日時型、(62>=)n進指数表記型、10進複素数型、16進整数型、36進自然数型、62進自然数型の7グループに分ける。
       	 * 要素すべてが同じ一つのグループの型に解決できる場合を除き、
       	 * string型とみなす。
       	 */

       	if
       	(
       		!
       		(
       			typesStr.matches("2[1-3],")
       			||
       			typesStr.matches("([1-6]|1[0-6]),")
       		)
       	)
       		return 24;

       	/* 出来た場合、
       	 * 平田型なら平田型。(62>=)n進指数表記型、16進整数型、36進自然数型、62進自然数型も同様
       	 * 日時型なら日付型か時刻型のどちらか一方の型で要素すべてを解決できたならその型、
       	 * そうでないなら日時型と判定。
       	 */

       	/*
       	 * 10進複素数型は
       	 * ①
       	 * 10進純虚数型を含み、さらにそれ以外も含むなら10進複素数型で確定。
       	 * 10進純虚数型だけなら10進純虚数型で確定。
       	 * 10進純虚数型を含まないなら10進実数型と置いてみる。
       	 */

       	if(typesStr.matches(".+,19,"))return 20;
       	if(typesStr.matches("19,"))return 19;

       	/*
       	 * ②
       	 * 10進実数型は、
       	 * 10進指数表記型、特定無理数型、10進小数型、10進分数型、それ以外を2種類以上含むなら
       	 * 10進実数型で確定。
       	 * 10進指数表記型、特定無理数型、10進小数型、10進分数型の1種類だけですべての要素が解決できるなら
       	 * その型で確定
       	 * どちらでもないなら10進整数型と置いてみる。
       	 */

       	if(typesStr.matches("(.+,)?1[3-6],(.*)"))
       	{
       		if(typesStr.matches("1[3-6],"))return Integer.parseInt(typesStr.replace(",", ""));
       		//1種類だけですべての要素解決
       		else return 18;
       		//10進実数型
       	}

       	/* ③
       	 * 10進整数型は、
       	 * 10進0以下整数型を含み、さらにそれ以外も含むなら10進整数型で確定。
       	 * 10進0以下整数型だけなら10進0以下整数型で確定。
       	 * 10進0以下整数型を含まないなら10進自然数型と置いてみる。
       	 */

       	if(typesStr.matches(".+,11,"))return 12;
       	if(typesStr.matches("11,"))return 11;

       	/* ④
       	 * 2値論理型ですべてが解決できるなら2値論理型と決定
       	 * ⑤
       	 * 4値論理型、bit列型、4進整数型、8進整数型、〃
       	 */
       	if(typesStr.matches("[2-6],"))
       		return Integer.parseInt(typesStr.replace(",", ""));

       	/* ⑥10進整数型と確定
       	 * */
       	return 12;

   	}


   	//特別な機能(ハッシュマップは、コンストラクタが特殊なシグネチャで呼ばれたときのみ実行
   	//(そうじゃないと、ハッシュマップ要らないとき応答が無駄に遅くなる))

   	protected ArrayList tntt;
   	//type name to type
	Detect(Boolean wantHashMap, String typeNameOrNum)
	{
		String t = typeNameOrNum;

									if(t.matches(
		"(hirata)|0"				)){tntt = new ArrayList<hirata>();
		hirata					 	u = new
		hirata						(t); tntt.add(u);return;}

									if(t.matches(
		"(space)|1"					)){tntt = new ArrayList<hirata>();
		space					 	u = new
		space						(t); tntt.add(u);return;}

									if(t.matches(
		"(bool2)|2"					)){tntt = new ArrayList<hirata>();
		bool2					 	u = new
		bool2						(t); tntt.add(u);return;}

									if(t.matches(
		"(bool4)|3"					)){tntt = new ArrayList<hirata>();
		bool4					 	u = new
		bool4						(t); tntt.add(u);return;}

									if(t.matches(
		"(bits)|4"					)){tntt = new ArrayList<hirata>();
		bits					 	u = new
		bits						(t); tntt.add(u);return;}

									if(t.matches(
		"(int4)|5"					)){tntt = new ArrayList<hirata>();
		int4					 	u = new
		int4						(t); tntt.add(u);return;}

									if(t.matches(
		"(int8)|6"					)){tntt = new ArrayList<hirata>();
		int8					 	u = new
		int8						(t); tntt.add(u);return;}

									if(t.matches(
		"(int16)|7"					)){tntt = new ArrayList<hirata>();
		int16					 	u = new
		int16						(t); tntt.add(u);return;}

									if(t.matches(
		"(int36)|8"					)){tntt = new ArrayList<hirata>();
		int36					 	u = new
		int36						(t); tntt.add(u);return;}

									if(t.matches(
		"(int62)|9"					)){tntt = new ArrayList<hirata>();
		int62					 	u = new
		int62						(t); tntt.add(u);return;}

									if(t.matches(
		"(uint)|10"					)){tntt = new ArrayList<hirata>();
		uint					 	u = new
		uint						(t); tntt.add(u);return;}

									if(t.matches(
		"(sint)|11"					)){tntt = new ArrayList<hirata>();
		sint					 	u = new
		sint						(t); tntt.add(u);return;}

									if(t.matches(
		"(int(eg(er)?)?)|12"		)){tntt = new ArrayList<hirata>();
		integ					 	u = new
		integ						(t); tntt.add(u);return;}

									if(t.matches(
		"(fract)|13"				)){tntt = new ArrayList<hirata>();
		fract					 	u = new
		fract						(t); tntt.add(u);return;}

									if(t.matches(
		"(point)|14"				)){tntt = new ArrayList<hirata>();
		point					 	u = new
		point						(t); tntt.add(u);return;}

									if(t.matches(
		"(sttc)|15"					)){tntt = new ArrayList<hirata>();
		sttc					 	u = new
		sttc						(t); tntt.add(u);return;}

									if(t.matches(
		"(exp)|16"					)){tntt = new ArrayList<hirata>();
		exp						 	u = new
		exp							(t); tntt.add(u);return;}

									if(t.matches(
		"(expN)|17"					)){tntt = new ArrayList<hirata>();
		expN					 	u = new
		expN						(t); tntt.add(u);return;}

									if(t.matches(
		"(real)|18"					)){tntt = new ArrayList<hirata>();
		real					 	u = new
		real						(t); tntt.add(u);return;}


	}




}

