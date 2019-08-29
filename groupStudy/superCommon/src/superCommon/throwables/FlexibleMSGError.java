package superCommon.throwables;

public class FlexibleMSGError extends Error {

	public FlexibleMSGError(Object...objects)
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
	public FlexibleMSGError() {}
	public FlexibleMSGError(String message) {super(message);}
	public FlexibleMSGError(Throwable cause) {super(cause);}
	public FlexibleMSGError(String message, Throwable cause) {super(message, cause);}
	public FlexibleMSGError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {super(message, cause, enableSuppression, writableStackTrace);}

}
