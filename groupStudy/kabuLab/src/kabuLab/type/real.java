/**
 *
 */
package kabuLab.type;

/**
 * 10進実数型の機能を提供するクラス<br>
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class real extends comp
{
	public real(String cellContent)
	{
		super(cellContent);

	}
	protected boolean accept()
	{
		int[] arr = {19};
		if(super.accept())for(int i:arr)if(new Detect(newContent).autoCast()==i)return false;else return true;return false;
	}

	double getAsDouble()
	{
		int autoCastNum = new Detect(newContent).autoCast();
		Object o = 123.5;

		Object o;
		Hashmap;

		switch(autoCastNum)
		{
			case 16:
				return exp.getAsDouble(newContent);
			case 15:
				return sttc.getAsDouble(newContent);
			case 13:
				return fract.getAsDouble(newContent);
			case 9:
				return int62.getAsDouble()

		}


	}
	int getAsInt(boolean forseToBeRadix10)
	{
		if(forseToBeRadix10)
			return Integer.parseInt(newContent, 10);
		else
			return Integer.parseInt(newContent);
	}
	int getAsInt(int radix)
	{
		return Integer.parseInt(newContent, radix);
	}

}