/**
 *
 */
package superCommon.throwables;

/**
 * Exceptionのすべてのスーパーコンストラクタを引き継いだほか、各引数に対してtoString()を呼ぶことでSystem.out.print系と同様の柔軟性を実現。
 * @author <a href=http://github.com/17ec084>Tomotaka Hirata(17ec084)</a>
 *
 */
public class FlexibleMSGException extends Exception
{

	public FlexibleMSGException(Object...objects)
	{
		super(prints(objects));
		//Super prints objects.
		//スーパーコンストラクタが可変長引数を出力する
	}

	static String prints(Object...objects)
	{
		String theStr = "";
		for(Object eachObject : objects)
		{
			theStr /*adds to right*/ += eachObject./*converted*/toString();
			//the str adds to right each Object converted to string.
			//各引数を文字列化したものを右に追加する
		}
		return theStr;
		//return the str
		//文字列を返却
	}



	//スーパーコンストラクタたち
	public FlexibleMSGException() {}
	public FlexibleMSGException(String message) {super(message);}
	public FlexibleMSGException(Throwable cause) {super(cause);}
	public FlexibleMSGException(String message, Throwable cause) {super(message, cause);}
	public FlexibleMSGException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {super(message, cause, enableSuppression, writableStackTrace);}

}
