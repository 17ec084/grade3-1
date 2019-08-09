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
		Object[] mAA = {"see",o};
		privateSee(mAA);
	}

	public static void see()
	{
		Object[] mAA = {"see"};
		privateSee(mAA);
	}

	public static void seeln(Object o)
	{
		Object[] mAA = {"seeln",o};
		privateSee(mAA);
	}

	public static void seeln()
	{
		Object[] mAA = {"seeln"};
		privateSee(mAA);
	}

	public static void s(Object o)
	{
		Object[] mAA = {"s",o};
		privateSee(mAA);
	}

	public static void s()
	{
		Object[] mAA = {"s"};
		privateSee(mAA);
	}

	public static void sln(Object o)
	{
		Object[] mAA = {"sln",o};
		privateSee(mAA);
	}

	public static void sln()
	{
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
			System.out.print( cname+"#"+mname+" called."+o + (methodName.matches(".*ln")?"\n":"") );
		}

	public static void c(Object o)
	{
	    System.out.print(o);
	}

	public static void c()
	{
	    System.out.println();
	}

	public static void cln(Object o)
	{
	    System.out.println(o);
	}

	public static void cln()
	{
	    System.out.println();
	}

	//さらに発展させたsee系
	public static String sif(boolean bool, String condSymbol)
	{
		if(bool)
		{
			Object[] mAA = {"ln", condSymbol+" is true."};
			privateSee(mAA);
		}
		else
		{
			Object[] mAA = {"ln", condSymbol+" is false."};
			privateSee(mAA);
		}
		return "";
	}

	public static String seeif(boolean bool, String condSymbol)
	{
		if(bool)
		{
			Object[] mAA = {"ln", condSymbol+" is true."};
			privateSee(mAA);
		}
		else
		{
			Object[] mAA = {"ln", condSymbol+" is false."};
			privateSee(mAA);
		}
		return "";
	}

	public static String cif(boolean bool, String condSymbol)
	{
		if(bool)
		{
			System.out.println(condSymbol + " is true.");
		}
		else
		{
			System.out.println(condSymbol + " is false.");
		}
		return "";
	}

	public static String sif(boolean bool, Object whenTrue, Object whenFalse)
	{
		if(bool)
		{
			Object[] mAA = {"ln", whenTrue};
				privateSee(mAA);
		}
		else
		{
			Object[] mAA = {"ln", whenFalse};
				privateSee(mAA);
		}
		return "";
	}

	public static String seeif(boolean bool, Object whenTrue, Object whenFalse)
	{
		if(bool)
		{
			Object[] mAA = {"ln", whenTrue};
				privateSee(mAA);
		}
		else
		{
			Object[] mAA = {"ln", whenFalse};
				privateSee(mAA);
		}
		return "";
	}

	public static String cif(boolean bool, Object whenTrue, Object whenFalse)
	{
		if(bool)
		{
			System.out.println(whenTrue);
		}
		else
		{
			System.out.println(whenFalse);
		}
		return "";
	}
}
