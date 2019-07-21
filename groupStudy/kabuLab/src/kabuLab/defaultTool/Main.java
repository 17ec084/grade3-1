package kabuLab.defaultTool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kabuLab.ArrayListEditor.Miscellaneous;

public class Main
{

	public static void main(String[] args){main_();}

	public static boolean main_()
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

}


