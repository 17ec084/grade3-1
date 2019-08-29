package superCommon.throwables;

public class FlexibleMSGRuntimeException extends RuntimeException {


	public FlexibleMSGRuntimeException(Object...objects)
	{
		super(prints(objects));
		//Super prints objects.
		//スーパーコンストラクタが可変長引数を出力する
	}

	static String prints(Object...objects)
	{
		return FlexibleMSGRuntimeException.prints(objects);
	}

	//スーパーコンストラクタたち
	public FlexibleMSGRuntimeException() {}
	public FlexibleMSGRuntimeException(String message) {super(message);}
	public FlexibleMSGRuntimeException(Throwable cause) {super(cause);}
	public FlexibleMSGRuntimeException(String message, Throwable cause) {super(message, cause);}
	public FlexibleMSGRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {super(message, cause, enableSuppression, writableStackTrace);}


}
