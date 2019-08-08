/**
 *
 */
package memoryHack;

import java.util.Scanner;

/**
 * boolean[]の代わり。<br>
 * booleanはそもそもbyteにより実現しているらしく、メモリ効率が悪いがbooleanArrayではbyte1つあたりに8つのbooleanを書き込むので無駄が最大7bit敷か生じない(はず)<br>
 * boolean[] bools = new boolean[10];の代わりに<br>
 * booleanArray bools = new booleanArray(10);とする。<br>
 * その他の宣言方法についてはサポートしないが、コンストラクタに自分自身あるいは booleanArrayMirrorの型の変数を渡せばそれをコピーする。<br>
 * また、何もコンストラクタに渡さない場合、ストリームが10進整数の入力を促し(任意のタイミングで区切って入力可能)、それを符号付き2進整数に変換したかのようなbit列が得られる。<br>
 * この場合、符号ビットは必ず2の(8の倍数-1)の位となる。<br>
 * ゲッタやセッタにおけるインデックス番号は、ビット列を2進整数とみたとき「2の何乗に当たるか」を表すものとしている。<br>
 * 従って最も右が0となる。<br>
 * 最も左が0となるようにしたい場合、子クラス<a href='eclipse-javadoc:%E2%98%82=statistical/src%3Cstatistical%7BbooleanArray.java%E2%98%83booleanArray%E2%98%82booleanArrayMirror'>booleanArrayMirror</a>を使うこと。
 * @author 17ec084(http://github.com/17ec084)
 * @see booleanArrayMirror
 *
 */
public class booleanArray
{
	public final int length;
	public final int lengthAsBytes;
	private byte[] eightBoolss;

	booleanArray(int size)
	{
		eightBoolss = new byte[size>0?((size-1)/8 + 1):size];
		//?:演算子は、要素数が負や0のとき、配列のエラーをそのまま発生させるための工夫

		length = size;
		lengthAsBytes = size>0?((size-1)/8 + 1):size;
	}

	booleanArray(booleanArray ba)
	{
		this.length = ba.length;
		this.lengthAsBytes = ba.lengthAsBytes;
		this.eightBoolss = ba.eightBoolss;
	}

	booleanArray(booleanArrayMirror ba)
	{
		this.length = ba.length;
		this.lengthAsBytes = ba.lengthAsBytes;
		ba.mirror();

		int j = 0;
		byte[] original = ba.getAsBytes();
		byte[] reversed = new byte[original.length];
		for(int i=reversed.length-1; i >= 0; i--)
		{
			reversed[j] = original[i];
			j++;
		}
		this.eightBoolss = reversed;
	}

	booleanArray()
	{
		boolean isMinus = false;

		System.out.println("10進数を入力し、それに対応するbit列を得るためのコンストラクタbooleanArray()が呼ばれました。");
		System.out.println("まず「0以外の」10進桁数を入力してください。(負号は数えないが、左に0を詰めている場合はそれらも数える)");
		Scanner scan = new Scanner(System.in);
		int digit = scan.nextInt(), count = 0;
		System.out.println("桁数は"+ digit + "と把握しました。\nでは次に、実際にその10進整数を入力してください。\n長い場合は途中の桁でいったん入力し、後から続きを入力することも出来ます。");
		String str;
		booleanArray bools = new booleanArray(digit);
		while(count < digit)
		{
			//方針:最初のみ負号の個数も調べる。それ以外は[0-9]にマッチする文字数を桁数として加算していく。
			str = scan.next();
			if(count == 0)//最初のみ
			{
				if
				(
					str.length() - str.replaceAll("\\-", "").length()
					//負号の個数を調べ、
					%2 == 1
				)//その偶奇によっては
					isMinus = !isMinus;
					//負数かどうかを入れ替える
			}

			str = str.replaceAll("\\D+","");//半角数字以外を空白に置き換える
			bools = boolsMaker(digit, count, str, bools);
			count += str.length();

		}

		/*
		 * 負数の場合は絶対値だけ取り出して、最後に負号総入れ替えしてインクリメントする。
		 */
		if(isMinus)
		{
			for(int i=0; i < bools.length; i++)
				bools.inverter(i);
System.out.println
(
new BitsCalculator
(
	bools.getAsBytes()
).increment(true).getAsBytes()[0]
);

			bools =
			booleanArray.newSetFromBytes
			(
				new BitsCalculator
				(
					bools.getAsBytes()
				).increment(true).getAsBytes()
			);
		}


		length = bools.length;
		lengthAsBytes = bools.lengthAsBytes;
		eightBoolss = bools.getAsBytes();


	}

		private booleanArray boolsMaker(int digit, int count, String str, booleanArray bools)
		{
			//TODO 負数の場合がまだ

			int index = digit - count - 1;

System.out.println(index);

			booleanArray boolsToBeAdded;

			BitsCalculator bools_ = new BitsCalculator(0), boolsToBeAdded_;

			for(int i=0; i < str.length(); i++)
			{
				if('0' <= str.charAt(i) && str.charAt(i) <= '9')
				{
					boolsToBeAdded = Pows10.get(index, (byte)(str.charAt(i)-'0'));
					//10のindex乗の(str.charAt(i)が示す数字)倍をbooleanArrayにしたもの。
					bools_ = new BitsCalculator(bools.getAsBytes());
					boolsToBeAdded_ = new BitsCalculator(boolsToBeAdded.getAsBytes());
					bools_ = new BitsCalculator(bools_.plus(boolsToBeAdded_, true));

				}

			}

			return newSetFromBytes(bools_.getAsBytes());

		}



	//getter
	public boolean get(int index)
	{
		if(length-1 < index || index < 0)
		{
			byte[] bytes = new byte[0];
			return bytes[bytes.length]==0;
			//不正なindexが与えられた場合、エラーを発生させる
		}
		else
		{
			short indexAsBytes = (short) (index/8);
			short indexInAByte = (byte) (index%8);
			byte eightBools = eightBoolss[indexAsBytes];
			if(indexInAByte != 7)
			{
				eightBools += (eightBools<0)?128:0;
				return ((((short)eightBools)/BSC.longPow(2, indexInAByte))%2==1);
			}
			else
				return eightBools < 0;
		}

	}

	public byte[] getAsBytes()
	{
		return eightBoolss;
	}

	//setter
	public void set(int index, boolean whatToset)
	{
		if(length-1 < index || index < 0)
		{
			byte[] bytes = new byte[0];
			bytes[bytes.length]=0;
			//不正なindexが与えられた場合、エラーを発生させる
		}
		else
		{
			short indexAsBytes = (short) (index/8);
			short indexInAByte = (byte) (index%8);

			byte eightBools = eightBoolss[indexAsBytes];
			boolean original;
			if(indexInAByte != 7)
			{
				byte tmp = (byte) ((eightBools<0)?128:0);
				eightBools += tmp;
				original = ((((short)eightBools)/BSC.longPow(2, indexInAByte))%2==1);
				eightBools -= tmp;
			}
			else
				original = (eightBools < 0);

			if(original != whatToset)
			{
				if(whatToset)
				{//0だったところを1に変える→1を足す
					eightBools += (byte)BSC.longPow(2, indexInAByte);
				}
				else
				{//1だったところを0に変える→1を引く
					eightBools -= (byte)BSC.longPow(2, indexInAByte);
				}
				eightBoolss[indexAsBytes] = eightBools;
			}

		}

	}

	public void inverter(int index)
	{
		set(index, !get(index));
	}

	//newSetter(newした上で初期値をセットする)
	public static booleanArray newSetFromBytes(byte[] bytes)
	{
		booleanArray bools = new booleanArray(bytes.length*8);
		int indexAsBools = 0;
		boolean isMsb1;
		for(int indexAsBytes=0; indexAsBytes<bytes.length; indexAsBytes++)
			for(int indexInAByte=0; indexInAByte < 8; indexInAByte++)
			{
				isMsb1 = (bytes[indexAsBytes] < 0);

				if(indexInAByte == 7)
					bools.set(indexAsBools, isMsb1);
				else
				{
					byte bool = bytes[indexAsBytes];

					bool += isMsb1?128:0;
					bool /= BSC.longPow(2, indexInAByte);
					bool %= 2;

					bools.set(indexAsBools, bool == 1);
				}
				indexAsBools++;
			}
		return bools;
	}

	//余計な機能

	public void dump()
	{
		byte i = 0, mod = (byte) (8-(length%8));
		if(mod == 8)mod = 0;
		for(; i < mod; i++)
			System.out.print("x");
		for(; i < length+mod; i++)
		{
			System.out.print(/*i +"," + */(get(length+mod-1-i)?"1":"0"));
			if(i%8 == 7)
				System.out.print(" ");
		}
		System.out.println();
	}



	public void dumpAsBytes()
	{
		for(int i=eightBoolss.length-1; i >= 1; i--)
			System.out.print(eightBoolss[i]+",");
		if(eightBoolss.length >= 1)
			System.out.println(eightBoolss[0]);
	}

	public void mirror()
	{
		boolean tmp;
		int iMax = length/2 - 1;
		for(int i=0; i <= iMax; i++)
		{
			tmp = get(i);
			set(i, get(length - 1 - i));
			set(length - 1 - i, tmp);
		}

	}


}

class bATester
{
	public static void main(String[] args)
	{

	}
}
