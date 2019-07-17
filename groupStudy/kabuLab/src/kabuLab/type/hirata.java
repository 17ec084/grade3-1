/**
 *
 */
package kabuLab.type;

/**
 * 平田型の機能を提供するクラス<br>
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class hirata extends string
{
	public hirata(String cellContent)
	{
		super(cellContent);

	}
	protected boolean accept()
	{
		return newContent.matches(Detect.types[0]);
	}

	//TODO javaccの呼び出しができると面白い。
}
