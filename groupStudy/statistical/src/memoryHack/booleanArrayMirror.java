/**
 *
 */
package memoryHack;

/**
 * boolean[]の代わり。<br>
 * booleanはそもそもbyteにより実現しているらしく、メモリ効率が悪いがbooleanArrayMirrorではbyte1つあたりに8つのbooleanを書き込むので無駄が最大7bit敷か生じない(はず)<br>
 * boolean[] bools = new boolean[10];の代わりに<br>
 * booleanArrayMirror bools = new booleanArrayMirror(10);とする。<br>
 * その他の宣言方法についてはサポートしないが、コンストラクタに自分自身あるいは booleanArrayの型の変数を渡せばそれをコピーする。<br>
 * また、文字列として10進整数をコンストラクタに渡すと、それを符号付き2進整数に変換したかのようなbit列が得られる。<br>
 * この場合、符号ビットは必ず2の(8の倍数-1)の位となる。<br>
 * ゲッタやセッタにおけるインデックス番号は、直観的なイメージに合わせ、最も左が0となるようにしている。<br>
 * 最も右が0となるようにしたい場合、親クラス<a href='eclipse-javadoc:%E2%98%82=statistical/src%3Cstatistical%7BbooleanArrayMirror.java%E2%98%83booleanArrayMirror%E2%98%82booleanArray'>booleanArray</a>を使うこと。
 * @author 17ec084(http://github.com/17ec084)
 * @see booleanArray
 *
 */
public class booleanArrayMirror extends booleanArray
{


	booleanArrayMirror(int size) {super(size);}

	booleanArrayMirror(booleanArrayMirror ba)
	{
		super((booleanArray)ba);
	}


	booleanArrayMirror(booleanArray ba)
	{
		super(ba);
		super.mirror();
	}

	booleanArrayMirror(){super();}

	public boolean get(int index)
	{
		return super.get(super.length-1-index);
	}

	public void set(int index, boolean whatToset)
	{
		super.set(super.length-1-index, whatToset);
	}

	public static booleanArrayMirror newSetAsBytes(byte[] bytes)
	{
		return new booleanArrayMirror(booleanArray.newSetFromBytes(bytes));
	}

	public void dump()
	{
		super.mirror();
		super.dump();
		super.mirror();
	}

	public byte[] getAsBytes()
	{
		return getReversedBytes();
	}

	private byte[] getReversedBytes()
	{
		int j = 0;
		byte[] original = super.getAsBytes();
		byte[] reversed = new byte[original.length];
		for(int i=reversed.length-1; i >= 0; i--)
		{
			reversed[j] = original[i];
			j++;
		}
		return reversed;
	}

}
