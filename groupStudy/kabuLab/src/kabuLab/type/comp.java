/**
 *
 */
package kabuLab.type;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 10進実数型の機能を提供するクラス<br>
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class comp extends string
{

	public final static int
	real = 0,
	image = 1,
	abs = 0,
	arg = 1;

	public comp(String cellContent)
	{
		super(cellContent);

	}
	protected boolean accept()
	{
		int[] arr = {1,17,21,22,23,24};
		if(super.accept())for(int i:arr)if(new Detect(newContent).autoCast()==i)return false;else return true;return false;
	}

	double[] getAsDoubles()
	{
		real re;
		purei im;

		double[] complex = new double[2];
		Detect d = new Detect(newContent);
		int autoCastNum = d.autoCast();
		String imReg = "(i|j|(([sS][qQ][rR][tT]|√)(\\(\\-1\\)|(-1))))";
		if(autoCastNum == 19)
		//虚部のみ
		{
			complex[real] = 0;
			if(newContent.matches("\\+?"+imReg))
				complex[image] = 1;
			else
			if(newContent.matches("\\-"+imReg))
				complex[image] = -1;
			else
			{
				im = new purei(newContent);
				complex[image] = im.getPartAsDouble(newContent);
			}
		}
		else
		if(autoCastNum == 20)
		//実部と虚部両方存在
		{
			Pattern p = Pattern.compile("(.+ )(\\+|\\-)(.*)"+imReg+"(.*)");
			Matcher m = p.matcher(newContent);
			if(m.matches())
			{
				re = new real(m.group(1));
				im = new purei(m.group(2)+m.group(3)+"i");
				complex[real] = re.getAsDouble();
				complex[image] = im.getPartAsDouble();
			}

		}
		else
		//実部のみ
		{
			re = new real(newContent);
			complex[real] = re.getAsDouble();
			complex[image] = 0;
		}

		return complex;
	}
	/*
	int getAsInt(boolean forseToBeRadix10)
	{
		if(forseToBeRadix10)
			return Integer.parseInt(newContent, 10);
		else
			return Integer.parseInt(newContent);
	}
	int getAsInt(int radix)
	{
		real.getAsDouble();
		return Integer.parseInt(newContent, radix);
	}
	*/
	//以下、四則演算、指数関数、対数関数を定義。指数関数で三角関数、べき乗が定義可能。微積近似も可能だろうからフーリエ変換もできよう
	/**
	 * 複素数型を加算し、doublesで返す
	 * @param operand
	 * @return
	 */
	public double[] addThenDoubles(String operand)
	{
		double[] op1 = this.getAsDoubles();
		comp tmp = new comp(operand);
		double[] op2 = tmp.getAsDoubles();
		op1[real] = op1[real]+op2[real];
		op1[image] = op1[image]+op2[image];
		return op1;

	}


	public double[] subThenDoubles(String operand, boolean isReverse)
	{
		double[] op1 = this.getAsDoubles();
		comp tmp = new comp(operand);
		double[] op2 = tmp.getAsDoubles();
		op1[0] = op1[0]-op2[0];
		op1[1] = op1[1]-op2[1];
		if(isReverse)
		{
			op1[real] = -op1[real];
			op1[image] = -op1[image];
		}
		return op1;

	}

	public double[] mulThenDoubles(String operand)
	{
//		(a+bj)(c+dj)=(ac-bd)+(bc+ad)j
		double[] op1 = this.getAsDoubles();
		comp tmp = new comp(operand);
		double[] op2 = tmp.getAsDoubles();
		double[] res = new double[2];

		res[real] = op1[real]*op2[real]-op1[image]*op2[image];
		res[image] = op1[image]*op2[real]+op1[real]*op2[image];

		return res;
	}

	public double[] divThenDoubles(String operand, boolean isReverse)
	{
		//(a+bj)/(c+dj)=(a+bj)(c-dj)/(c^2+d^2)だけだと0除算になり得る
		comp tmp = new comp(operand);
		double[] res = new double[2], op1, op2;
		if(isReverse)
		{
			op2 = this.getAsDoubles();
			op1 = tmp.getAsDoubles();
		}
		else
		{
			op1 = this.getAsDoubles();
			op2 = tmp.getAsDoubles();
		}
		if(op2[real]==0 && op2[image]==0)
		{
			System.out.println("compクラスで0除算発生");
			return null;
		}
		else
		{
			res = new comp
			(
				op1[real]
				+
				op1[image]>0 ? "+" : "-"
				+
				Math.abs(op1[image])
			)
			.mulThenDoubles
			(
				op2[real]
				+
				op2[image]>0 ? "-" : "+"
				+
				Math.abs(op2[image])
			);
			res[real] = res[real] / (op2[real]*op2[real]+op2[image]*op2[image]);
			res[image] = res[image] / (op2[real]*op2[real]+op2[image]*op2[image]);
			return res;
		}
	}


	private double[] absAndArg(double[] op)
	{
		//https://ja.wikipedia.org/wiki/%E8%A4%87%E7%B4%A0%E6%95%B0%E3%81%AE%E5%81%8F%E8%A7%92
		double[] res = new double[2];
		res[abs] = Math.sqrt(op[real]*op[real]+op[image]*op[image]);
		if(op[image] != 0)
		{
			res[arg] = 2*atan0to2pi((res[abs]-op[real])/op[image]);
		}
		else
		if(res[abs] == 0)
		{
			res[arg] = Double.NaN;
		}
		else
		{
			res[abs] = (op[real]>0) ? 0 : Math.PI ;
		}
		return res;
	}

		private double atan0to2pi(double x)
		{
			double res = Math.atan(x);
			if(res < 0)
			{
				res = res + 2*Math.PI;
			}
			return res;
		}

	public double[] absAndArg(){return absAndArg(this.getAsDoubles());}

	/**
	 * 底、真数共に複素数の対数を求める。<br>
	 * <b>参考</b>:http://www.ee.t-kougei.ac.jp/tuushin/lecture/math1/htdocs/complex/log/index.html<br><br>
	 * <b>仕様</b>:<br>本来多価関数であるlogz1 z2だが、途中の計算(参考サイト通り)における偏角は[0,2π]とした。<br><br>
	 * <b>公式</b>:<br>logz1 z2 = (ln|z2|+arg(z2)j)/(ln|z1|+arg(z1)j)
	 *
	 * @param operand 真数となる複素数
	 * @param isReverse 真数と底を入れ替える
	 * @return log(comp型の実体) operand
	 */
	public double[] logThenDoubles(String operand, boolean isReverse)
	{
		//参考http://www.ee.t-kougei.ac.jp/tuushin/lecture/math1/htdocs/complex/log/index.html
		comp tmp = new comp(operand);
		double[] op1, op2;
		if(isReverse)
		{
			op2 = this.getAsDoubles();
			op1 = tmp.getAsDoubles();
		}
		else
		{
			op1 = this.getAsDoubles();
			op2 = tmp.getAsDoubles();
		}
		if(op2[real]==0 && op2[image]==0)
		{
			System.out.println("compクラスでlog計算中に真数0発生");
			return null;
		}
		else
		if(op1[real]==0 && op1[image]==0)
		{
			//底が0→対数法則より分母が-∞な分数→答えは0
			return op1;
		}
		else
		{
			comp comp_lnz2 = new comp(Math.log(absAndArg(op2)[abs]) + "+ " + absAndArg(op2)[arg] + "j");
			String str_lnz1 = Math.log(absAndArg(op1)[abs]) + "+ " + absAndArg(op1)[arg] + "j";
			return comp_lnz2.divThenDoubles(str_lnz1, false);
		}
	}

	/**
	 * 複素数comp実体の複素数new comp(operand)乗を求める。<br>
	 * <b>参考</b>:https://fermiumbay13.hatenablog.com/entry/2017/12/03/021235<br><br>
	 * <b>仕様</b>:<br>本来多価関数であるz1^z2だが、計算(参考サイト通り)における偏角は[0,2π]とした。<br><br>
	 * <b>公式</b>:<br> z1^z2 = (exp(Re(z2)ln|z1|-Im(z2)arg(z1)))(cos(Im(z2)ln|z1|+Re(z2)arg(z1)) + jsin(Im(z2)ln|z1|+Re(z2)arg(z1)))
	 *
	 * @param operand 指数となる複素数
	 * @param isReverse 基数と指数を入れ替える
	 * @return (comp型の実体)^operand
	 */
	public double[] powThenDoubles(String operand, boolean isReverse)
	{
		//参考http://www.ee.t-kougei.ac.jp/tuushin/lecture/math1/htdocs/complex/log/index.html
		comp tmp = new comp(operand);
		double[] op1, op2;
		if(isReverse)
		{
			op2 = this.getAsDoubles();
			op1 = tmp.getAsDoubles();
		}
		else
		{
			op1 = this.getAsDoubles();
			op2 = tmp.getAsDoubles();
		}
		if(op2[real]==0 && op2[image]==0 && op2[real]==0 && op2[image]==0)
		{
			System.out.println("compクラスでpow計算中に0^0発生");
			return null;
		}
		else
		if(op1[real]==0 && op1[image]==0)
		{
			return op1;
		}
		else
		if(op2[real]==0 && op2[image]==0)
		{
			op1[real] = 1;
			op1[image] = 0;
			return op1;
		}
		else
		{
			double alpha // 実数
			=
			op2[real]*Math.log(absAndArg(op1)[abs])
			-
			op2[image]*(absAndArg(op1)[arg])
			;
			double beta // 実数
			=
			op2[image]*Math.log(absAndArg(op1)[abs])
			+
			op2[real]*(absAndArg(op1)[arg])
			;

			op1[real] = Math.exp(alpha)*Math.cos(beta);
			op1[image] = Math.exp(alpha)*Math.sin(beta);
			return op1;
		}
	}

	public comp cos()
	{
		comp x = this;
		comp ix = x.mul("1j");
		comp mix = x.mul("-1j");
		comp eix = ix.pow("exp", true);
		comp emix = mix.pow("exp", true);

		comp eixpemix = eix.add(doublesToStr(emix.getAsDoubles()));

		return eixpemix.div("2", false);

	}

	public comp sin()
	{
		comp x = this;
		comp ix = x.mul("1j");
		comp mix = x.mul("-1j");
		comp eix = ix.pow("exp", true);
		comp emix = mix.pow("exp", true);

		comp eixmemix = eix.sub(doublesToStr(emix.getAsDoubles()));

		return eixmemix.div("2j", false);

	}

	public comp tan()
	{
		comp s = sin();
		comp c = cos();
		return s.div(doublesToStr(c.getAsDoubles()),true);
	}

	public comp cosh()
	{
		comp x = this;
		comp mx = x.mul("-1");
		comp ex = x.pow("exp", true);
		comp emx = mx.pow("exp", true);
		comp expemx = ex.add(doublesToStr(emx.getAsDoubles()));

		return expemx.div("2", false);

	}

	public comp sinh()
	{
		comp x = this;
		comp mx = x.mul("-1");
		comp ex = x.pow("exp", true);
		comp emx = mx.pow("exp", true);
		comp exmemx = ex.sub(doublesToStr(emx.getAsDoubles()));

		return exmemx.div("2", false);

	}

	public comp tanh()
	{
		comp s = sinh();
		comp c = cosh();
		return s.div(doublesToStr(c.getAsDoubles()),true);

	}

	/** @see #addThenDoubles*/public comp add(String operand){return doublesToComp(addThenDoubles(operand));}
	/** @see #subThenDoubles*/public comp sub(String operand, boolean b){return doublesToComp(subThenDoubles(operand,b));}
	/** @see #mulThenDoubles*/public comp mul(String operand){return doublesToComp(mulThenDoubles(operand));}
	/** @see #divThenDoubles*/public comp div(String operand, boolean b){return doublesToComp(divThenDoubles(operand,b));}
	/** @see #logThenDoubles*/public comp log(String operand, boolean b){return doublesToComp(logThenDoubles(operand,b));}
	/** @see #powThenDoubles*/public comp pow(String operand, boolean b){return doublesToComp(powThenDoubles(operand,b));}
	/** @see #cos*/public double[] cosThenDoubles(){return cos().getAsDoubles();}
	/** @see #sin*/public double[] sinThenDoubles(){return sin().getAsDoubles();}
	/** @see #tan*/public double[] tanThenDoubles(){return tan().getAsDoubles();}
	/** @see #cosh*/public double[] coshThenDoubles(){return cosh().getAsDoubles();}
	/** @see #sinh*/public double[] sinhThenDoubles(){return sinh().getAsDoubles();}
	/** @see #tanh*/public double[] tanhThenDoubles(){return tanh().getAsDoubles();}

	public String doublesToStr(double[] val)
	{
		String str = "";
		if(val[real]!=0)
		{
			str = str + val[real];
		}
		if(val[image]!=0)
		{
			str = str + " " + (val[image]>0 ? "+" : "-" );
			str = str + Math.abs(val[image]);
			str = str + "j";
		}
		if(val[real]==0 && val[image]==0)
		{
			str = "0";
		}

		return str;
	}

	public comp doublesToComp(double[] val){return new comp(doublesToStr(val));}



}