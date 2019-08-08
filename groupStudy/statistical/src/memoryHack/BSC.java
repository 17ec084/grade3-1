package memoryHack;

/**
 * 単にBytesStrConverterのクラス名が長すぎるからBSCと短くしたコピークラス。
 * @author 17ec084(http:/github.com/17ec084)
 *
 */
public class BSC extends BytesStrConverter
{
	public BSC()				{super();}
	public BSC(byte[] bytes) 	{super(bytes);}
	public BSC(String str) 		{super(str);}
	public BSC(long l) 		{super(l);}
	public BSC(int i) 			{super(i);}
	public BSC(short sh) 		{super(sh);}
	public BSC(double d) 		{super(d);}
	public BSC(float f) 		{super(f);}

}
