/**
 *
 */
package memoryHack;

/**
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class BitsCalculator implements Cloneable
//クローナブルインタフェースは複製可能を示すマーカインタフェース
{
	private byte[] b;

	public BitsCalculator(BSC bsc)
	{
		b = bsc.getAsBytes();
	}

		public BitsCalculator(byte[] bytes)	{this(new BSC(bytes));}
		public BitsCalculator(String str) 		{this(new BSC(str));}
		public BitsCalculator(long l) 			{this(new BSC(l));}
		public BitsCalculator(int i) 			{this(new BSC(i));}
		public BitsCalculator(short sh) 		{this(new BSC(sh));}
		public BitsCalculator(double d) 		{this(new BSC(d));}
		public BitsCalculator(float f) 		{this(new BSC(f));}


	public BSC plus(BitsCalculator addend, boolean isSigned)
	{
		if(this==addend)
			addend = this.clone();
			//必要かどうかはよく考えていないが、念のため。
		short sh;
		byte[] bytes;

		BitsCalculator augend = this;
		boolean carry;
		//桁数は足される数のほうが大きいものとしよう。
		if(augend.b.length < addend.b.length)
			return addend.plus(augend, isSigned);
		bytes = new byte[augend.b.length];
		addend = fillTheGap(augend, addend, isSigned);//オペランドの配列長を合わせる

		byte OVCA = seekOVCA(augend.b[0], addend.b[0]);//2*OV+CA

		for(short i=(short) (addend.b.length-1); i>=0; i--)
		{
			carry = isCarry(sh = (short)(augend.b[i] + addend.b[i]));
			bytes[i] = (byte)sh;
			bytes = dealCarry(addend.b, carry, bytes, i, isSigned, OVCA);
		}
		return new BSC(bytes);

	}
		private BitsCalculator fillTheGap(BitsCalculator bigger, BitsCalculator smaller, boolean isSigned)
		{
			byte[] enlarged = new byte[bigger.b.length];
			short gap = (short) (bigger.b.length - smaller.b.length);
			short biggerLength = (short) bigger.b.length;
			byte fill = (smaller.b[0]<0)?(byte)-1:(byte)0;
			for(short i=0; i < gap; i++)
				enlarged[i] = fill;//符号拡張
			for(short i=gap; i < biggerLength; i++)
				enlarged[i] = smaller.b[i-gap];//smallerの内容をenlargedの下位bitへ
			return new BitsCalculator(enlarged);
		}
		private boolean isCarry(short aByte)
		{
			return aByte >= 256;
		}

		private byte seekOVCA(byte a, byte b)
		{
			boolean signA = a<0, signB = b<0, signAPlusB = a+b<0;
			boolean OV = (signA && signB && !signAPlusB) || (!signA && !signB && signAPlusB);
			boolean CA = (short)a+(a<0?256:0) + (short)b+(b<0?256:0) > 255;
			return (byte)((OV?2:0)+(CA?1:0));
		}

		private byte[] dealCarry(byte[] addend, boolean carry, byte[] bytes, short i, boolean isSigned, byte OVCA)
		{
			if(i==0)//於 符号ビット(=MSB)
				if(isSigned)//符号付き演算
					//符号付き演算ではキャリはどうでもよく、問題になるのはオーバフロー
					if(OVCA/2 == 1)//オーバフロー発生
						return dealOV(bytes);
					else//オーバフロー無し
						return bytes;
				else//符号なし演算
					//符号なし演算ではオーバフローはどうでもよく、問題になるのはキャリ
					if(OVCA%2 == 1)//キャリ発生
						return dealCA(bytes);
					else//キャリ無し
						return bytes;
			else
			{
				if(carry)//キャリ発生
					return this.plus(pow2((short) (i+1)), false).getAsBytes();
					//このthisは、ほとんどnew BitsCalculator(augend)のような意味
				else
				return bytes;
			}
		}

			private byte[] dealOV(byte[] bytes)
			{
				short length = (short) bytes.length;
				byte[] rtn = new byte[length+1];
				rtn[0] =(bytes[0]<0)?(byte)0:(byte)-1;
				for(short i=0; i<length; i++)
					rtn[i+1] = bytes[i];
				return rtn;
			}

			private byte[] dealCA(byte[] bytes)
			{
				short length = (short) bytes.length;
				byte[] rtn = new byte[length+1];
				rtn[0] = 1;
				for(short i=0; i<length; i++)
					rtn[i+1] = bytes[i];
			/*	rtn[length] = 1;
				for(short i=0; i<length; i++)
					rtn[i] = bytes[i];*/
				return rtn;
			}

			private BitsCalculator pow2(short idx)
			{
				BitsCalculator bc =  new BitsCalculator(new BSC());
				bc.setBitByIndex(idx, true, false);
				return bc;
			}

	//引き算では必ず符号あり
	public BSC minus(BitsCalculator subtrahend)
	{

		return this.plus(new BitsCalculator(subtrahend.signChange().getAsBytes()), true);

	}

		public BSC signChange()
		{
			short length;
			byte[] bytes = new byte[length = (short)b.length];
			for(short i=0; i < length; i++)
				bytes[i] = b[i];

			//1と0を入れ替える
			for(short i=0; i < bytes.length; i++)
				bytes[i] = (byte)(255-bytes[i]);
			//1加算
			bytes = new BitsCalculator(bytes).increment(true).getAsBytes();
			return new BSC(bytes);
		}

	public BSC increment(boolean isSigned)
	{
		return this.plus(new BitsCalculator(1), isSigned);
	}

	public BSC decrement(boolean isSigned)
	{
		return this.minus(new BitsCalculator(1));
	}


	/**
	 * 二つのBitsCalculator型変数を入力し、これらを符号あり整数とみなす。第一引数の示す数字のほうが大きいときは1を、第2引数の示す数字のほうが大きいときは-1を、どちらも同じときは0を返却する。
	 * @param a
	 * @param b
	 * @return
	 */
	public static byte sizeComparison(BitsCalculator a, BitsCalculator b)
	{
		byte[] bytes = a.minus(b).getAsBytes();

		for(short i=0; i < bytes.length; i++)
		{
			if(bytes[i] != 0)
			{
				return (byte) (bytes[i]/Math.abs(bytes[i]));
			}
		}
		return 0;

	}

	//setter

	public void setBitByIndex(int index, boolean bool, boolean isSigned)
	{
		short gap = (short) ((b.length - 1) - (index/8));
		//bの最大インデックスとindexのbyteでのインデックスを比較
		short length = (short) b.length;
		if(gap < 0)
		{
			gap *= -1;
			byte fill = (isSigned&&b[0]<0)?(byte)-1:(byte)0;
			byte[] bytes = new byte[index/8 + 1];
			short i;
			for(i=0; i < gap; i++)
				bytes[i] = fill;
			for(; i < length+gap; i++)
				bytes[i] = b[i-gap];
			b = bytes;
			bytes = null;
		}

		if(BSC.getBitFromBytes(b, index) == bool)//既に所望のものがセットされていたら
		{}//何もしない
		else//そうでなければ
		{
			short cntOfNeeded0 = (short) ((index/8)-1);
			//byte配列に必要な0の個数;
			byte[] bytes = new byte[cntOfNeeded0+1];
			bytes[0] = (byte)BSC.longPow(2,index%8);

			if(bool)//1をセットしたいところに0がセットされていた場合
			{}//何もしない
			else//0をセットしたいところに1がセットされていた場合
			{
				//1と0を入れ替える
				for(short i=0; i < bytes.length; i++)
					bytes[i] = (byte)(255-bytes[i]);
				//1加算
				bytes = new BitsCalculator(bytes).increment(isSigned).getAsBytes();
			}

			b = new BitsCalculator(b).plus(new BitsCalculator(bytes), isSigned).getAsBytes();
		}


	}

	public byte[] getAsBytes()
	{
		return this.plus(new BitsCalculator(0), false).getAsBytes();
	}


	//cloneメソッド
	/**
	 * 参考:https://qiita.com/SUZUKI_Masaya/items/8da8c0038797f143f5d3
	 */
	public BitsCalculator clone()
	{
		BitsCalculator bc;
		try
		{
			bc = (BitsCalculator)super.clone();
			//Objectクラスのprotected clone()を呼ぶ
			//protectedなので、superしないと呼び出せない。
			bc.b = this.b.clone();
			return bc;
		}
		catch(Exception e){e.printStackTrace();}
		return null;
	}


}


