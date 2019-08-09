package myDebugger;

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
		privateSee(mAA);
	}

	public static void see()
	{
		indenter();
		Object[] mAA = {"see"};
		privateSee(mAA);
	}

	public static void seeln(Object o)
	{
		indenter();
		Object[] mAA = {"seeln",o};
		privateSee(mAA);
	}

	public static void seeln()
	{
		indenter();
		Object[] mAA = {"seeln"};
		privateSee(mAA);
	}

	public static void s(Object o)
	{
		indenter();
		Object[] mAA = {"s",o};
		privateSee(mAA);
	}

	public static void s()
	{
		indenter();
		Object[] mAA = {"s"};
		privateSee(mAA);
	}

	public static void sln(Object o)
	{
		indenter();
		Object[] mAA = {"sln",o};
		privateSee(mAA);
	}

	public static void sln()
	{
		indenter();
		Object[] mAA = {"sln"};
		privateSee(mAA);
	}

		private static void privateSee(Object[] methodAndArg)
		{
			StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
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
			privateSee(mAA);
		}
		else
		{
			indenter();
			Object[] mAA = {"ln", condSymbol+" is false."};
			privateSee(mAA);
		}
		return "";
	}

	public static String seeif(boolean bool, String condSymbol)
	{
		if(bool)
		{
			indenter();
			Object[] mAA = {"ln", condSymbol+" is true."};
			privateSee(mAA);
		}
		else
		{
			indenter();
			Object[] mAA = {"ln", condSymbol+" is false."};
			privateSee(mAA);
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
				privateSee(mAA);
		}
		else
		{
			indenter();
			Object[] mAA = {"ln", whenFalse};
				privateSee(mAA);
		}
		return "";
	}

	public static String seeif(boolean bool, Object whenTrue, Object whenFalse)
	{
		if(bool)
		{
			indenter();
			Object[] mAA = {"ln", whenTrue};
				privateSee(mAA);
		}
		else
		{
			indenter();
			Object[] mAA = {"ln", whenFalse};
				privateSee(mAA);
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
			privateSee(mAA);
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
			privateSee(mAA);
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
	private static int i = 0;

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

		private static void writeI(int j)
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

	private static void indenter()
	{
		int i = readI();
		for(int j=0; j < i; j++)
			System.out.print(" ");
	}

	private static String indenter(Object o)
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

}
