package kabuLab.type;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class JSample2_1{

/*	    public static void main(String[] args) {
	        test("abc");
	        test("abc123");
	        String text = "abc123";
	        String imReg = "(i|j|(([sS][qQ][rR][tT]|√)(\\(\\-1\\)|(-1))))";
			Pattern p = Pattern.compile("(.+ )(\\+|\\-)(.*)"+imReg+"(.*)");
			Matcher m = p.matcher("Pi +1e-5j");
	        if (m.matches()) {
	        System.out.println(m.group(1));//実部
	        System.out.println(m.group(2)+m.group(3));//虚部
	        }
	    }
*/
	    private static void test(String text) {
	        Pattern pattern = Pattern.compile("([a-z]+)[0-9]+");
	        Matcher matcher = pattern.matcher(text);

	        System.out.println("[text=" + text + "]");
	        if (matcher.matches()) {
	            System.out.println("matches = true");
	            System.out.println("start = " + matcher.start());
	            System.out.println("end = " + matcher.end());
	            System.out.println("group = " + matcher.group(1));
	        } else {
	            System.out.println("matches = false");
	        }
	    }

/*	    public static void main(String[] args)
	    {
	    	//参考:https://www.itmedia.co.jp/enterprise/articles/0407/26/news035.html
	    	//http://www.woodensoldier.info/computer/javatips/22.htm
	    	//https://codeday.me/jp/qa/20181223/20245.html

	    	Integer i = new Integer("012");
	    	String typeName = null;
	    		    	Pattern p = Pattern.compile(".*\\.(.*)");
	    	Matcher m = p.matcher(i.getClass().toString());
	    	if(m.matches())
	    		typeName = m.group(1);
	    	Class myClass = null;
			try {
				myClass = (Class) Class.forName(typeName).getDeclaredConstructor(String.class).newInstance("37");
			} catch (InstantiationException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
	    	myClass.cast(i);
	    	System.out.println(i);






	    }
	    */
	    public static void main(String[] args)
	    {
/*	    	Object o = 123;
	    	System.out.println(o.getClass());
	    	o = 127.6;
*/




  			 //(於 DPPPLデータ型クラス)
	    	ArrayList<?> arr;
/*	    	ArrayList<Double> arrD;
	    	arrD = new ArrayList<Double>();
	    	arrD.add(3.13);
*/
	    	Dtct dtct = new Dtct();
	    	arr = dtct.tntt;

	    	System.out.println(arr.get(0));
	    	System.out.println("--");
	    	System.out.println(Double.parseDouble("      8.9"));
	    }

}




class Dtct
{
   	ArrayList tntt;
   	//type name to type (Detectクラスのフィールド)

   	Dtct()
   	{

			tntt = new ArrayList<Integer>();
			Integer tmp = new Integer(3364);
			tntt.add(tmp);
   	}
}