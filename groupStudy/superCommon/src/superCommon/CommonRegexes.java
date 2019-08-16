/**
 *
 */
package superCommon;

/**
 * よく使う正規表現をまとめた。<br>static参照を想定。
 * @author <a href="http://github.com/17ec084">Tomotaka Hirata(17ec084)</a>
 *
 */
public class CommonRegexes
{
	private static final byte
	none = 0,
	tab = 1,
	rtn = 2,
	tab_rtn = 3;


	/**
	 * spaceFamily[i*3+j]を指定し、空白に当てはまる正規表現を得ることができる。<br>
	 * 但し、iやjは次のようなものである。<br>
	 * <dl>
	 * <dt>
	 * i
	 * </dt>
	 * <dd>
	 * i=0なら0回または1回<br>
	 * i=1なら0回以上の繰り返し<br>
	 * i=2なら1回以上の繰り返し
	 * </dd>
	 * <dt>
	 * j
	 * </dt>
	 * <dd>
	 * j=0なら半／全角スペースのみ<br>
	 * j=1ならタブを、<br>
	 * j=2なら改行を追加<br>
	 * j=3なら両方追加
	 * </dd>
	 * </dl>
	 *
	 *
	 */
	public static final String[] spaceFamily = getSpaceFamily();

	public static String space(byte option)
	{
		String regex = "( |　)";
		switch(option)
		{
			case none:
			break;
			case tab:
				regex = "(" + regex + "|\t)";
			break;
			case rtn:
				regex = "(" + regex + "|\r|\n|(\r\n))";
			break;
			case tab_rtn:
				regex = "(" + regex + "|\t|\r|\n|(\r\n))";
			default:
			break;
		}

		return regex;
	}

	public static String repeat0(String regex)
	{
		return "("+regex+")*";
	}

	public static String repeat1(String regex)
	{
		return "("+regex+")+";
	}

	public static String option(String regex)
	{
		return "("+regex+")?";
	}

	private static String[] getSpaceFamily()
	{
		int cntI = 3, cntJ = 4;
		String[] rtn = new String[cntI*cntJ];
		for(int i=0; i < cntI; i++)
			for(int j=0; j < cntJ; j++)
				switch(i)
				{
					case 1:
						rtn[i*cntJ+j] = option(space((byte) j));
					break;
					case 2:
						rtn[i*cntJ+j] = repeat0(space((byte) j));
					break;
					case 3:
						rtn[i*cntJ+j] = repeat1(space((byte) j));
					break;
					default:
					break;
				}
		return rtn;
	}


}
