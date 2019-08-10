package myDebugger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DBG
{

	//ポーズ系
	public static void pause()
	{
		java.util.Scanner s = new java.util.Scanner(System.in);
		s.nextLine();
	}

	public static void p()
	{
		pause();
	}

	//see系
	public static void see(Object o)
	{
		indenter();
		Object[] mAA = {"see",o};
		protectedSee(mAA);
	}

	public static void see()
	{
		indenter();
		Object[] mAA = {"see"};
		protectedSee(mAA);
	}

	public static void seeln(Object o)
	{
		indenter();
		Object[] mAA = {"seeln",o};
		protectedSee(mAA);
	}

	public static void seeln()
	{
		indenter();
		Object[] mAA = {"seeln"};
		protectedSee(mAA);
	}

	public static void s(Object o)
	{
		indenter();
		Object[] mAA = {"s",o};
		protectedSee(mAA);
	}

	public static void s()
	{
		indenter();
		Object[] mAA = {"s"};
		protectedSee(mAA);
	}

	public static void sln(Object o)
	{
		indenter();
		Object[] mAA = {"sln",o};
		protectedSee(mAA);
	}

	public static void sln()
	{
		indenter();
		Object[] mAA = {"sln"};
		protectedSee(mAA);
	}

		protected static void protectedSee(Object[] methodAndArg)
		{
			StackTraceElement ste = Thread.currentThread().getStackTrace()[extendsLevel+3];
			String cname = ste.getClassName();
			String mname = ste.getMethodName();
			String methodName = (String)methodAndArg[0];
			Object o = ((methodAndArg.length==2) ? methodAndArg[1] : "");
			System.out.print
			(
				indenter
				(
					(Object)
					(cname+"#"+mname+" called."+o + (methodName.matches(".*ln")?"\n":""))
				)
			);
		}

	public static void c(Object o)
	{
		indenter();
	    System.out.print(indenter(o));
	}

	public static void c()
	{
		indenter();
	    System.out.println();
	}

	public static void cln(Object o)
	{
		indenter();
	    System.out.println(indenter(o));
	}

	public static void cln()
	{
		indenter();
	    System.out.println();
	}

	//さらに発展させたsee系
	public static String sif(boolean bool, String condSymbol)
	{
		if(bool)
		{
			indenter();
			Object[] mAA = {"ln", condSymbol+" is true."};
			protectedSee(mAA);
		}
		else
		{
			indenter();
			Object[] mAA = {"ln", condSymbol+" is false."};
			protectedSee(mAA);
		}
		return "";
	}

	public static String seeif(boolean bool, String condSymbol)
	{
		if(bool)
		{
			indenter();
			Object[] mAA = {"ln", condSymbol+" is true."};
			protectedSee(mAA);
		}
		else
		{
			indenter();
			Object[] mAA = {"ln", condSymbol+" is false."};
			protectedSee(mAA);
		}
		return "";
	}

	public static String cif(boolean bool, String condSymbol)
	{
		if(bool)
		{
			indenter();
			System.out.println(condSymbol + " is true.");
		}
		else
		{
			indenter();
			System.out.println(condSymbol + " is false.");
		}
		return "";
	}

	public static String sif(boolean bool, Object whenTrue, Object whenFalse)
	{
		if(bool)
		{
			indenter();
			Object[] mAA = {"ln", whenTrue};
				protectedSee(mAA);
		}
		else
		{
			indenter();
			Object[] mAA = {"ln", whenFalse};
				protectedSee(mAA);
		}
		return "";
	}

	public static String seeif(boolean bool, Object whenTrue, Object whenFalse)
	{
		if(bool)
		{
			indenter();
			Object[] mAA = {"ln", whenTrue};
				protectedSee(mAA);
		}
		else
		{
			indenter();
			Object[] mAA = {"ln", whenFalse};
				protectedSee(mAA);
		}
		return "";
	}

	public static String cif(boolean bool, Object whenTrue, Object whenFalse)
	{
		if(bool)
		{
			indenter();
			System.out.println(whenTrue);
		}
		else
		{
			indenter();
			System.out.println(whenFalse);
		}
		return "";
	}

	//ループカウンタ(1回目は何も表示せず、二回目以降に回数を報告)
	//slcまたはclc
	//ループの外でnewする必要がある。
	//インスタンス変数をループ内で複数回呼ぶとそれだけカウントして誤作動する。
	//どうしてもやりたいのならslc、clcでなく、sl,clを呼ぶこと。

	//フィールド
	long cnt = 0;
	String loopName;

	//コンストラクタ
	public DBG(String loopName){this.loopName = loopName;}

	public String slc()
	{
		if(cnt != 0)
		{
			indenter();
			Object[] mAA = {"ln", "\""+ loopName + "\" has been looped "+ (cnt+1) + " times."};
			protectedSee(mAA);
		}
		cnt++;
		return "";
	}

	public String clc()
	{

		if(cnt != 0)
		{
			indenter();
			System.out.println("\"" + loopName + "\" has been looped "+ (cnt+1) + " times.");
		}
		cnt++;
		return "";
	}

	public String sl()
	{
		if(cnt != 0)
		{
			indenter();
			Object[] mAA = {"ln", "\""+ loopName + "\" has been looped "+ (cnt+1) + " times."};
			protectedSee(mAA);
		}
		return "";
	}

	public String cl()
	{
		if(cnt != 0)
		{
			indenter();
			System.out.println("\"" + loopName + "\" has been looped "+ (cnt+1) + " times.");
		}
		return "";
	}

	//コンソールへの表示にインデントを付ける
	protected static int i = 0;

	public static void goRight()
	{
		writeI(readI()+1);
	}

	public static void goLeft()
	{

		writeI(readI()-1);
	}

	public static void setI(int indent)
	{
		writeI(indent);
	}

		protected static void writeI(int j)
		{
			/*
			try
			{
				FileWriter fw = new FileWriter(new File("_hirata_i"));
				fw.write((j+'0'));
				fw.close();
			}
			catch (IOException e){e.printStackTrace();}
			// */i = j;

		}

	public static int readI()
	{
		/*
		try
		{
			FileReader fr = new FileReader(new File("_hirata_i"));
			int rtn = Integer.parseInt(new BufferedReader(fr).readLine());
			fr.close();
			if(rtn >= 0)
				return rtn;
			else
				return 0;
		}
		catch (IOException e){e.printStackTrace();}

		return 0;
		// */return i;
	}

	protected static void indenter()
	{
		int i = readI();
		for(int j=0; j < i; j++)
			System.out.print(" ");
	}

	protected static String indenter(Object o)
	{
		int i = readI();
		boolean endWithReturn = false;
		String newReturn = "\n", oldReturnRegex = "(\\\r|\\\n|(\\\r\\\n))";
		String str = o.toString();
		if(str.matches(".*"+oldReturnRegex))
		{
			endWithReturn = true;
			while(str.matches(".*"+oldReturnRegex))
				str = str.substring(0, str.length()-1);
		}
		for(int j=0; j < i; j++)
			newReturn += " ";

		return str.replaceAll(oldReturnRegex, newReturn)+(endWithReturn?"\n":"");
	}

	public static void cI()//see Indentの意味。
	{
		for(int j=0; j < i; j++)
			System.out.print(" ");
	}

	//スリープ
	public static void sleep(long millisec, boolean leftTimeView)
	{
		try
		{
			while(leftTimeView && millisec >= 1000)
			{
				cln("sleep left "+ (millisec/1000) + " sec.");
				Thread.sleep(1000);
				millisec -= 1000;
			}
			Thread.sleep(millisec);
		}catch (InterruptedException e) {e.printStackTrace();}

	}

	public static void slp(long millisec, boolean leftTimeView)
	{
		sleep(millisec, leftTimeView);
	}

	public static void sleep(long millisec)
	{
		sleep(millisec, true);
	}

	public static void slp(long millisec)
	{
		sleep(millisec, true);
	}

	//DBGの撤去
	/**
	 * 「/*DBG」と「DBG*&#047;」で囲まれたところ、そして
	 * DBGのメソッド呼び出しをソースコードから取り除く。<br>
	 * またDBGのメソッド呼び出しを含むif文、for文、[do-]while文、switch文、try-((catch)|(finally))文、メソッド宣言
	 * については、コンソール上でユーザに確認しながら取り除く。<br>
	 * (この機能を使うためにはブロックの省略をしてはならない)<br>
	 * ※javaファイルはクラス名.javaというファイルをプロジェクトに対応するディレクトリから順番に探索することで特定する。<br>
	 * したがって、別パッケージの同名クラスのjavaファイルに対して処理を行ってしまうことがある。<br>
	 * これを防ぐためには、同名クラスのjavaファイルに「//DBG don't kill me」から始まる行を追加すること。
	 *
	 */
	public static void killDBG()
	{
		File f = new File(findJavaFile());

		String source = readSource(f);
		//javaファイルを全行読み込む
System.out.println(source);

	}

		protected static String findJavaFile()
		{
			File f = new File("").getAbsoluteFile();
			while(!(f.getName().equals("statistical")))
			{
				f = f.getParentFile();
			}
			String javaFileName = Thread.currentThread().getStackTrace()[extendsLevel+3].getClassName();
			//例:memoryHack.Main
			javaFileName = javaFileName.replaceAll(".*\\.", "");
			//例:Main
			javaFileName += ".java";
			//例:Main.java
			return search(f, javaFileName).replaceAll("\\\\_hirata_breakRecursion", "");
		}

			protected static String search(File dir, String javaFileName)
			{
				File[] childlen = dir.listFiles();
				String rtn;
		        for(int i=0; i<childlen.length; i++)
		        {
		        	String split = "( |\t|\r|\n|(\r\n))*";
		        	String split_must = "( |\\t|\\r|\\n|(\\r\\n))+";
		        	if
		        	(
		        			childlen[i].isDirectory()
		        		&&	has(childlen[i], javaFileName)
		        		&&	hasRegex(childlen[i].getPath() + javaFileName, "import" + ".*" + "(myDebugger\\.)?" + myClassName + ".*" + ";")
		        		&&	!hasRegex(childlen[i].getPath() + javaFileName, "//DBG" +split_must+ "((don\\'t)|(do"+split_must+"not))" +split_must+ "kill" +split_must+ "me")
		        	)
		        		return childlen[i].getPath() + "\\"+javaFileName+"\\_hirata_breakRecursion";
		        	else
		        	if(childlen[i].isDirectory())
		                if((rtn=search(childlen[i], javaFileName)).matches(".*_hirata_breakRecursion"))
		                	return rtn;
		        }
		        return javaFileName + " (which ) not found";
		    }

				protected static boolean has(File dir, String child)
				{
					File[] files = dir.listFiles();
					for(int i=0; i < files.length; i++)
						if(files[i].getName().equals(child))
							return true;
					return false;
				}

				protected static boolean hasRegex(String fileName, String regex)
				{
					return (readSource(new File(fileName)).matches(".*("+regex+").*"));
				}

		/**
		 * 参考:https://qiita.com/penguinshunya/items/353bb1c555f337b0cf6d
		 * @param f
		 * @return
		 */
		protected static String readSource(File f)
		{
		    StringBuilder sb = new StringBuilder();

		    try
		    {
		    	FileReader fr = new FileReader(f);
		    	BufferedReader br = new BufferedReader(fr);

		        String str = br.readLine();
		        while (str != null)
		        {
		            sb.append(str + System.getProperty("line.separator"));
		            str = br.readLine();
		        }
			    return sb.toString();
		    }
		    catch (IOException e){e.printStackTrace();}

		    return null;


		}

		//DBGクラスのリネーム(他のデバッガとのクラス名前空間の衝突を回避)

		//継承により実現させるから、see系では注意が必要。そこでextendsLevelというフィールドを導入する。

		protected static int extendsLevel = 0;

		protected static String myClassName = getDBGClassName();

		protected static void setDBGClassName(String dbgClassName)
		{
			try
			{
				FileWriter fw;
				fw = new FileWriter (new File("_hirata_dbgClassName"));
				fw.write(dbgClassName);
				fw.close();

				File f = new File("").getAbsoluteFile();
				while(!(f.getName().equals("statistical")))
				{
					f = f.getParentFile();
				}
				f = new File(search(f, "DBG.java").replaceAll("DBG\\.java", myClassName+".java").replaceAll("\\\\_hirata_breakRecursion", ""));
				if(!f.exists())
					for(int i=0; !f.createNewFile(); i++)
						if(i>100)
						{
							cln("DBG couldn't be renamed to " + dbgClassName + ".java");
							break;
						}
				for(int i=0; !f.setWritable(true); i++)
					if(i>100)
					{
						cln(dbgClassName + ".java couldn't be writable");
						break;
					}
				FileWriter fr = new FileWriter(f);
				fr.write
				(
"package myDebugger;"+ 													"\r\n" +
"" + 																	"\r\n" +
"public class " + myClassName + " extends DBG" +						"\r\n" +
"{" +																	"\r\n" +
"	protected static int extendsLevel = 1;" +							"\r\n" +
"	public " + myClassName + "(String loopName){super(loopName);}" +	"\r\n" +
"}"
						);
				fr.close();
				cln(dbgClassName + ".java is created in the directory of DBG.java .\n move it if needed and compile it.");
			}
			catch (IOException e){e.printStackTrace();}
		}

		protected static String getDBGClassName()
		{
			try
			{
				FileReader fr;
				fr = new FileReader(new File("_hirata_dbgClassName"));
				BufferedReader br = new BufferedReader(fr);
				String rtn = br.readLine();
				fr.close();
				return rtn;
			}
			catch (IOException e){e.printStackTrace();}
			return null;

		}

		public static void renameDBGto(String newName)
		{
			setDBGClassName(newName);
		}
}