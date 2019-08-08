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
	    System.out.print(o);
	}

	public static void cln()
	{
	    System.out.println();
	}


}
