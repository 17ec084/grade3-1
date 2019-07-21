/**
 *
 */
package kabuLab;

import kabuLab.type.comp;

/**
 * kabuLab.type.compで定義した数学関数を動的に呼び出す。<br>
 * これは積分や微分を実装するに役立つ。<br>
 * <br>
 * 使用例1:<br>
 * DynamicFuncCaller(op1, "div", op2);<br>
 * op1.div(op2.getAsStr)を実行する。<br><br>
 * 使用例2:<br>
 * DyamicFuncCaller("log" op1, op2)<br>
 * op1.log(op2.getAsStr)を実行する<br><br>
 * 実行結果はgetAs[(Comp)(Str)(Doubles)]で得られる。<br>
 * 逆複素[(三角)(双曲線)]関数については、getAs[(Comps)(Strs)(Doubless)]
 *
 * @author 17ec084(http://github.com/17ec084)
 * @see kabuLab.type.comp
 *
 */
public class DynamicFuncCaller
{
	DynamicFuncCaller(String func, comp op1, comp op2){dynamicFuncCaller(op1, func, op2);}
	DynamicFuncCaller(comp op1, String func, comp op2){dynamicFuncCaller(op1, func, op2);}

	private comp dynamicFuncCaller(comp op1, String func, comp op2)
	{

		return /*ここにリファクタリングのコード*/;

	}


/*
	private boolean main_()
	{
		String str = " y = labuLab.defaultTool.Main.f(x|a,b,c) ",y,f,x;
		String[] params;

		String var = "("+Miscellaneous.javaNameRoleAsReg()+")";
		String cla
		=
		"("
		+var
		+"("
		+ 	"\\."+var
		+")*"
		+"("
		+	" *< *"+var
		+	"("
		+		" *,"+var+" *"
		+	")* *>"
		+")*"
		+
		")";
		Pattern p = Pattern.compile(" *"+ var + " *= *("+var+".+)");
		Matcher m = p.matcher(str);
		if(m.matches()){y = m.group(1);str = m.group(2);}else{return false;}

		p = Pattern.compile(cla + " *\\( *("+var+".+)");
		m = p.matcher(str);
		if(m.matches()){f = m.group(1);str = m.group(9);}else{return false;}



		System.out.println("y=\""+y+"\"");
		System.out.println("f=\""+f+"\"");
		System.out.println("remain=\""+str+"\"");


		return true;

	}
	*/
}
