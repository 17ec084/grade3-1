/**
 *
 */
package superCommon.dynamicInterfaceFieldMaker;

//import static superCommon.dynamicInterfaceFieldMaker.Accessible.*;
import static superCommon.dynamicInterfaceFieldMaker.Accessible.*;

/**
 * @see superCommon.dynamicInterfaceFieldMaker 説明はパッケージに付しました(こちら)
 * @author <a href=http://github.com/17ec084>Tomotaka Hirata(17ec084)</a>
 *
 */
//コード読者の方へ、ジェネリック利用が初めてなので、型名、変数名に日本語を使っています。お見苦しくてごめんなさい。
public class Dynamic<任意のクラス型>
{
	//field
	private 任意のクラス型 任意のクラス型の変数;
	private boolean finalized;//本当に書き換え禁止をしたいときのため
	final private Class declaringInterface;

	final private Accessible accessible;//可視性

	//constructor
	public Dynamic(任意のクラス型 任意のクラス型の変数, boolean finalize)
	{
		this.finalized = finalize;
		this.任意のクラス型の変数 = 任意のクラス型の変数;
		StackTraceElement[] stes = new Throwable().getStackTrace();
		int i = 0;
		while(stes[i].getClassName().equals(this.getClass().getName()))i++;
									try
									{
		this.declaringInterface = Class.forName(stes[i].getClassName());//コンストラクタを呼んだメンバの所属するクラス
									}
									catch(ClassNotFoundException e)
									{
										throw new StackTraceParadoxError();
									}
		this.accessible = PACKAGE;
	}

	public Dynamic(任意のクラス型 任意のクラス型の変数){this(任意のクラス型の変数, false);	}


	public Dynamic(任意のクラス型 任意のクラス型の変数, String accessible, boolean finalize)
	{
		this.finalized = finalize;
		Accessible[] modifiers = {PUBLIC, PROTECTED, PACKAGE, PRIVATE};
		for(Accessible modifier : modifiers)
			if(accessible.matches(modifier.getNameRegex()))
			{
				this.accessible = modifier;
				this.任意のクラス型の変数 = 任意のクラス型の変数;
				StackTraceElement[] stes = new Throwable().getStackTrace();
				int i = 0;
				while(stes[i].getClassName().equals(this.getClass().getName()))i++;
											try
											{
				this.declaringInterface = Class.forName(stes[i].getClassName());
											}
											catch(ClassNotFoundException e)
											{
												throw new StackTraceParadoxError();
											}
				return;
			}
		throw new DynamicFieldAccessibilityException();

	}

	public Dynamic(任意のクラス型 任意のクラス型の変数, String accessible){this(任意のクラス型の変数, accessible, false);}




	public Dynamic(任意のクラス型 任意のクラス型の変数, Accessible modifier, boolean finalize)
	{
		this.finalized = finalize;
		this.accessible = modifier;
		this.任意のクラス型の変数 = 任意のクラス型の変数;
		StackTraceElement[] stes = new Throwable().getStackTrace();
		int i = 0;
		while(stes[i].getClassName().equals(this.getClass().getName()))i++;
									try
									{
		this.declaringInterface = Class.forName(stes[i].getClassName());
									}
									catch(ClassNotFoundException e)
									{
										throw new StackTraceParadoxError();
									}
	}

	public Dynamic(任意のクラス型 任意のクラス型の変数, Accessible modifier){this(任意のクラス型の変数, modifier, false);}


	//method (g|s)etter
	public 任意のクラス型 get() throws DynamicFieldCannotAccessException
	{
		if(accessible.isLettingAccessOfCallerIfDeclarerIs(declaringInterface))
			return 任意のクラス型の変数;
		else
		{
			StackTraceElement[] stes = new Throwable().getStackTrace();
			new CallerGetterIgnoreJavaPacks(declaringInterface, accessible, stes);
			//toStringメソッド実装のため、Stringがスタックトレースに貯まって誤作動のでその補正もしている。
			return null;//コンパイラのおしゃぶり
		}

	}

	public void set(任意のクラス型 newした時と同じクラス型の変数) throws DynamicFieldCannotOverwriteException, DynamicFieldCannotAccessException
	{
		if(accessible.isLettingAccessOfCallerIfDeclarerIs(declaringInterface))
		{
			if(finalized)
				throw new DynamicFieldCannotOverwriteException();
			else
				this.任意のクラス型の変数 = newした時と同じクラス型の変数;
		}
		else
		{
			StackTraceElement[] stes = new Throwable().getStackTrace();
			new CallerGetterIgnoreJavaPacks(declaringInterface, accessible, stes);
			//toStringメソッド実装のため、Stringやjavaパッケージなどがスタックトレースに貯まって誤作動のでその補正もしている。

		}

	}

	//method (an)?others?
	public void setFinal(boolean finalize)
	{
		this.finalized = finalize;
	}

	@Override
	public String toString()
	{
		return get().toString();
	}

}

class CallerGetterIgnoreJavaPacks
{
	CallerGetterIgnoreJavaPacks(Class declaringInterface, Accessible accessible, StackTraceElement[] stes)
	{
		int i = 0;
		while
		(
			stes[i].getClassName().equals(this.getClass().getName())
			||
			stes[i].getClassName().equals(String.class.getName())
			||
			stes[i].getClassName().matches("java\\..*")
		)i++;
		String nameOfCallerClass = stes[i].getClassName();

		throw new DynamicFieldCannotAccessException(declaringInterface, accessible, nameOfCallerClass);
	}
}




class DynamicFieldCannotAccessException extends RuntimeException
{
	DynamicFieldCannotAccessException(Class declaringInterface, Accessible accessible, String nameOfCallerClass)
	{
		super(declaringInterface.getName() + "\nで宣言されたこのDynamic化フィールドはアクセス修飾子が"+accessible.name()+"なので、\n" + nameOfCallerClass + "からアクセスすることはできません。\n尚、アクセス修飾子は(リフレクションでもしない限り)後から動的に変更することはできません。");
	}
	DynamicFieldCannotAccessException()
	{
		super("アクセス修飾子による制限のため、このDynamic化フィールドへアクセスできません。\\n尚、アクセス修飾子は(リフレクションでもしない限り)後から動的に変更することはできません。");
	}

}

class DynamicFieldCannotOverwriteException extends RuntimeException
{
	DynamicFieldCannotOverwriteException()
	{
		super("このDynamic化フィールドは現在finalizeされています。\n(このフィールド).setFinal(false)を実行してください。");
	}

}
