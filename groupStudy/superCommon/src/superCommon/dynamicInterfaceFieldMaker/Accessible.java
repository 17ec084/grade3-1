
package superCommon.dynamicInterfaceFieldMaker;

import java.util.Arrays;

import superCommon.forLambda.B;


/**
 * アクセス修飾子に対応する定数を定義。<br>次の表の通り(1=アクセス可能、0=アクセス不能)
 * <table border>
<tr>
<td>定数名</td>
<td>10進※</td>
<td>自クラス</td>
<td>自パッケージ</td>
<td>サブクラス</td>
<td>その他</td>
</tr>
<tr>
<td>PUBLIC</td>
<td>15</td>
<td>1</td>
<td>1</td>
<td>1</td>
<td>1</td>
</tr>
<tr>
<td>PROTECTED</td>
<td>14</td>
<td>1</td>
<td>1</td>
<td>1</td>
<td>0</td>
</tr>
<tr>
<td>PACKAGE</td>
<td>12</td>
<td>1</td>
<td>1</td>
<td>0</td>
<td>0</td>
</tr>
<tr>
<td>PRIVATE</td>
<td>8</td>
<td>1</td>
<td>0</td>
<td>0</td>
<td>0</td>
</tr>
</table>
 * ※実際には10進値を表現するENUMフィールドはなく、自クラス、自パッケージなどなどからのアクセス可能性を示す4要素のboolean配列がフィールドとなっている。
 * @author <a href=http://github.com/17ec084>Tomotaka Hirata(17ec084)</a>
 *
 */
public enum Accessible
{

	PUBLIC(15, "public"),
	PROTECTED(14, "protected"),
	PACKAGE(12, "package( *private)?"),
	PRIVATE(8, "private");

	private boolean[] accessibility = new boolean[4];
	private String nameRegex;

	private Accessible(int id, String nameRegex)
	{
		boolean[] accessibility = {(id/8)%2==1, (id/4)%2==1, (id/2)%2==1, (id/1)%2==1};
		this.accessibility = accessibility;
		this.nameRegex = nameRegex;
	}



	public static void main(String[] args)
	{

		int dummy = 0;
	}


	public String getNameRegex()
	{
		return this.nameRegex;
	}

	/**
	 * スタックトレースを使って呼び出し元クラスを特定し、呼び出し元クラスからDynamicなフィールドへアクセスを許してよいか判断。<br>
	 * 実際には「呼び出し元→Dynamicクラスのメソッド→このメソッド」となるので
	 *「呼び出し元のクラスの呼び出し元のクラスの呼び出し元のクラス」を特定する。
	 * このメソッドはDynamicクラスからの呼び出し専用である。それ以外から呼ばれた場合は例外を投げる。<br><br>
	 * このメソッド名はletの現在進行形をとっていて典型的な誤文であるが、命令的なメソッドではないことを明らかにする意味がある。
	 * @param declaringInterface
	 * @return
	 * @throws DynamicFieldAccessibilityCheckTimeError Dynamicクラス以外から呼ばれた場合
	 * @throws ClassNotFoundException
	 */

	boolean isLettingAccessOfCallerIfDeclarerIs(Class declaringInterface) throws DynamicFieldAccessibilityCheckTimeError
	{

		Searcher.setDeclaringInterface(declaringInterface);

		if(Arrays.equals(this.accessibility, PUBLIC.accessibility))
			return true;

		boolean
		isAvailableIfSameClass = this.accessibility[0],//true
		isAvailableIfSamePackage = this.accessibility[1],
		isAvailableIfExtend = this.accessibility[2],
		isAvailableIfAny = this.accessibility[3];//false

		StackTraceElement[] stes = new Throwable().getStackTrace();
		int i = 0;
		while(stes[i].getClassName().equals(this.getClass().getName()))i++;//ここを安全にできないか
		String nameOfDynamic = stes[i].getClassName();

		if(!nameOfDynamic.equals(Dynamic.class.getName()))
			throw new DynamicFieldAccessibilityCheckTimeError();

		while
		(
			stes[i].getClassName().equals(Dynamic.class.getName())
			||
			stes[i].getClassName().equals(String.class.getName()) //toString()実装の影響でStringが貯まりうる
			||
			stes[i].getClassName().matches("java\\..*")//面倒なのでjavaパッケージ内のクラスすべて無視
		)i++;
		String nameOfCallerClass = stes[i].getClassName();

												try
												{
		Class callerClass = Class.forName(nameOfCallerClass);

		B b = () ->
		{
		//boolean b.$() の定義

			B has_isA_relation = () ->
			{
			//boolean has_isA_relation.$() の定義

				if(!declaringInterface.isInterface())//dynamicフィールドを宣言しているのがインタフェースではなくてクラスの場合
				{
					Class callersSuperiorClass = callerClass;
					Compare compareObjectClass = new Compare(Object.class), compareDeclaringClass = new Compare(declaringInterface);

					while(true)
						if(compareDeclaringClass.to(callersSuperiorClass))return true;
						else if(compareObjectClass.to(callersSuperiorClass))return false;
						else callersSuperiorClass = callersSuperiorClass.getSuperclass();
				}
				else{return Searcher.search(callerClass);}
				//dynamicフィールドを宣言しているのが本来通りインタフェースの場合
			};

			boolean
			areSameClass = callerClass.getName().equals(declaringInterface.getName()),
			areSamePackage = callerClass.getPackageName().equals(declaringInterface.getPackageName()),
			extend = has_isA_relation.$(),
			any = true;

			return
			(areSameClass	&&isAvailableIfSameClass) ||
			(areSamePackage	&&isAvailableIfSamePackage)||
			(extend			&&isAvailableIfExtend)||
			(any			&&isAvailableIfAny);

		};

		return b.$();

												}
												catch(ClassNotFoundException e)
												{
													//javaにthreadを殺させるためthrowableの種類を変更
													throw new StackTraceParadoxError();
												}




	}

	static class Searcher
	{

		//継承と実装を両方調べる必要があるのでややこしい。
		//継承を調べていく(⑤)途中で(先に)実装を調べ、①
		//declaringInterfaceが見つかればtrueを返し、②
		//見つからなければそのまた実装(スーパーインタフェース)を調べればよい。③
		//最後まで見つからなかった(→ すべてgetIntrefaces().length=0)④
		//ならクラス継承を調べる。⑤
		//クラス継承元がObjectだったならfalseを返す。⑥

		//実装を調べる再帰はsearchIの匿名クラスの$メソッドが担当(①～④)する。

		static Class declaringInterface = null;

		static void setDeclaringInterface(Class di)
		{
			declaringInterface = di;
		}

		static boolean search(Class callerClass)
		{

			Class callersSuperiorClass = callerClass;
			Compare compareObjectClass = new Compare(Object.class);

			while(true)
				//searchI(Class callersSuperiorClass)・・・インタフェースの再帰的探索
				if(searchI(callersSuperiorClass))//①～④
					return true;
				else
				{
					callersSuperiorClass = callersSuperiorClass.getSuperclass();//⑤
					if(compareObjectClass.to(callersSuperiorClass));//⑥
						return false;
				}
		}

		//searchI(Class callersSuperiorClass)・・・インタフェースの再帰的探索
		static boolean searchI(Class callersSuperiorClass)
		{
			Class callersSuperiorInterface;

			Compare compareDeclaringInterface = new Compare(declaringInterface);

			while(true)
			{
				Class[] interfaces = callersSuperiorClass.getInterfaces();//①
				for(int j=0; j < interfaces.length; j++)
				{
					callersSuperiorInterface = interfaces[j];
					if(compareDeclaringInterface.to(callersSuperiorInterface))
						return true;//②
				}
				if(searchI(callersSuperiorClass))
				//searchI(Class callersSuperiorClass)・・・インタフェースの再帰的探索
					return true;//③
				else
				if(interfaces.length == 0)
					return false;//④

			}
		}

	}

	static class Compare{ Class class1; Compare(Class class1){this.class1 = class1;}boolean to(Class class2){	return (class1==null || class2==null)?false:(class1.getName().equals(class2.getName()));}}
	//new Compare(class1).to(class2)でclass1とclass2が同じものであることの真偽を返すだけのクラス
	//nullがあった場合はfalseを必ず返す



}

class DynamicFieldAccessibilityCheckTimeError extends Error
{
	DynamicFieldAccessibilityCheckTimeError()
	{
		super("Dynamic化されたフィールドの可視性を確認するためにスタックトレースをたどっていた際に矛盾が発生しました。\nDynamicクラス以外からAccessible#isAvailableInCallerClassメソッドへアクセスされた可能性があります。");
	}
}


class DynamicFieldAccessibilityException extends RuntimeException
{
	DynamicFieldAccessibilityException()
	{
		super("Dynamic化するフィールドに与えるべきアクセス修飾子が無効です。\npublic,\nprotected,\npackageまたはpackage private,\nprivate\nのみが許可されています。");
	}
}


class StackTraceParadoxError extends Error
{
	StackTraceParadoxError()
	{
		super("JVMスタックトレースによれば存在するはずのクラスが、探してみると見つかりませんでした。おかしいのでプログラムを強制停止します。");
	}
}



