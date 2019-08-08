package memoryHack;
/**
 *
 */

/**
 * コンストラクタがStringを受け取ったときはbyte[]に、<br>
 * byte[]を受け取ったときはStringに変換する。<br>
 * int,short,longを受け取ったときは、その整数に対応する数値を示すStringに変換する。<br>
 * float,doubleを受け取ったときは、その浮動小数点に対応する数値を示すStringに変換する。<br>
 * 何も受け取らなかった場合は、空のStringを返却する。
 * フィールドObject resultに変換結果が格納される。(要キャスト)
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class BytesStrConverter
{

	final public Object result;
	final private byte[] b;//配列をfinalしても値の書き換えは可能なので要注意
	final private String s;

	public BytesStrConverter()
	{
		b = new byte[0];
		s = "";
		result = s;
	}

	public BytesStrConverter(byte[] bytes)
	{
		char c;
		String str ="";
		int idx = 0;
		if(bytes.length %2 == 1)//bytesの要素数が奇数だった場合
		{
			str += (char)bytes[0]+"";
			idx = 1;
		}

		for(; idx < bytes.length-1; idx += 2)
		{
			c = (char) bytes[idx];
			c *= 256;
			c += bytes[idx+1];
			str += c;
		}
		result = str;
		b = bytes;
		s = str;
	}
	public BytesStrConverter(String str)
	{
		char c;
		byte[] bytes;
		int idx;
		c = str.charAt(0);
		if(c > 255)
		{
			bytes = new byte[2*str.length()];
			bytes[0] = (byte) (c/256);
			bytes[1] = (byte) (c%256);
			idx=2;
		}
		else
		{
			bytes = new byte[2*str.length()-1];
			bytes[0] = (byte)c;
			idx=1;
		}

		for(; idx < bytes.length-1; idx += 2)
		{
			bytes[idx] = (byte) (str.charAt((idx+1)/2)/256);
			bytes[idx+1] = (byte) (str.charAt((idx+1)/2)%256);
		}
		result = bytes;
		b = bytes;
		s = str;
	}

	public BytesStrConverter(long l)
	{
		b = longToBytes(l);
		s = (String) new BytesStrConverter(b).result;
		result = s;
	}
		public BytesStrConverter(int i){this((long)i);}
		public BytesStrConverter(short sh){this((long)sh);}

	public BytesStrConverter(double d)
	{
		s = (String)new BytesStrConverter(Double.doubleToLongBits(d)).result;
		b = (byte[])new BytesStrConverter(s).result;
		result = s;
	}
		public BytesStrConverter(float f){this((double)f);}

	/**
	 * コンストラクタが受け取ったものの示す数値をbyte[]で返却
	 * @return
	 */
	public byte[] getAsBytes()
	{
		return b;
	}

	/**
	 * コンストラクタが受け取ったものの示す数値をStringで返却
	 * @return
	 */
	public String getAsString()
	{
		return s;
	}

	/**
	 * コンストラクタが受け取ったものの示す数値をbyte[]で表現した時の要素数(=256進桁数)
	 * @return
	 */
	public int getLength()
	{
		return b.length;
	}

	/**
	 * コンストラクタが受け取ったものの示す数値を符号なし整数としてlongに変換
	 * @return
	 */
	public long unsignedLong()
	{

		long rtn = 0;

		short i=(short) (b.length-1);
		for(long radix=1; canContinue(radix,i); radix*=256, i--)
		{
			rtn += b[i]*radix;
//System.out.println("b[i]="+b[i]+"radix"+radix+", rtn=" + rtn);
		}

		if(i>=0)
		{
			//radix==((Long.MAX_VALUE)/128)+1となっている。
			//(厳密にはradix==((Long.MAX_VALUE+1)/128))と書きたいが、そうするとあふれる。
			//→128よりも小さければよい。
			if(i==0 && b[i]<128){/*問題なし*/}
			else
			{
				System.out.println("警告: BytesStrConverterクラスで情報の切り捨てが起こりました。");
			}
//System.out.println(rtn);
			byte b_ = (byte) (b[i] % 128);
			rtn += (long)b_*((Long.MAX_VALUE)/128+1);

		}
		return rtn;

	}
		private boolean canContinue(long radix, short i)
		{
			return i>=0 && radix<(Long.MAX_VALUE)/256;
		}

	/**
	 * コンストラクタが受け取ったものの示す数値を符号付き整数としてlongに変換<br>
	 * 符号ビットの位置は引数sbidxで指定できる。(2^sbidxの位を符号ビットとする。0以上63以下)<br>
	 * 符号ビットがbit列の途中となった場合、符号ビットおよびそれより左のビットの情報は失われる
	 * @param sbidx 符号ビットの位置(0～63)
	 * @return
	 */
	public long signedLong(short sbidx)
	{
		long rtn = unsignedLong();
		if(sbidx == 63)
			if(b[0] / 128 == 0)//符号ビット正
				return rtn;
			else//符号ビット負
				return rtn + Long.MIN_VALUE;
		else
		if(sbidx < 63)
		{
			rtn %= longPow(2,(sbidx+1));
			if(rtn / longPow(2,(sbidx)) == 1)//符号ビット正
				return rtn;
			else//符号ビット負
				return rtn - longPow(2,sbidx);

		}
		else
		{
			System.out.println("BytesStrConverterクラスでエラー。符号bitの位置が不正です(大きすぎます)。");
			return (Long) null;
		}


	}

		public long signedLong(){return signedLong((short) 63);}
		//符号ビット位置のデフォルト


	/**
	 * longの整数をbyte[]に変換する。
	 */
	private byte[] longToBytes(long l)
	{
		short sh,sh_;
		byte[] bytes = new byte[sh_ = cntBytes(l)];
		sh = (short) (sh_ - 1);
		sh_--;
		if(l >= 0)//符号ビット正の場合
			for(; sh >= 0; sh--)
			{
				bytes[sh_ - sh] = (byte) (l / longPow(256,sh));
				l %= longPow(256,sh);
			}
		else//符号ビット負の場合
		{
			//まず-1倍を求める
			l *= -1;
			for(; sh >= 0; sh--)
			{
				bytes[sh_ - sh] = (byte) (l / longPow(256,sh));
				l %= longPow(256,sh);
			}

			//全ビットを反転させ1を加算(反転とは111...1からの減算に等価)
			//→「反転は」bytesの各要素について、255から減算すればよい
			for(short i=0; i < bytes.length; i++)
				bytes[i] = (byte) (255 - bytes[i]);

			//1を加算
			bytes = new BitsCalculator(bytes).increment(true).getAsBytes();
		}
		return bytes;

	}
		private short cntBytes(long num)
		{
			if(num >= longPow(2,55) || num < 0)
				return 8;
			else
			{
				/*
				long test=256;
				short rtn=1;
				for(; test <= num; test*=256,rtn++);
				return rtn;
				*/
				return (short)digitCounter(256, num);
			}
		}

			/**
			 * n進自然数における、必要な桁数を求める。
			 * @param radix 基数n
			 * @param num 自然数
			 */
			public static int digitCounter(int radix, long num)
			{
				int test = radix;
				int rtn = 1;
				for(; test <= num; test*=radix,rtn++);
				return rtn;
			}

	/**
	 *
	 *
	 *
	 * @param S 右から0オリジン何bit目が符号部か
	 * @param endOfE 右から0オリジン何bit目が指数部の終わりか(この一つ右が仮数部の始まりとなる)
	 * @param endOfM 仮数部の終わり。省略すると0となる。
	 * @return byte[][] <br>0要素目:符号部<br>1要素目:指数部<br>2要素目:仮数部
	 */
	public byte[][] floatingPoint(int S, int endOfE, int endOfM)
	{
		boolean isMinus;
		/*short idxInAByte = (short) (7 - S%8);
		//1byteのbit列を取り出した時の2^idxInAByteの位が符号部
		isMinus = (b[S/8] % longPow(2,idxInAByte+1)) / longPow(2,idxInAByte) == 1;
		*/


		isMinus = getBitFromBytes(b, S);
		byte[] S_= {bitsToBytes(b, S, S)[0],8,8};
//System.out.println(S-1+","+endOfE);
//System.out.println(new BytesStrConverter(bitsToBytes(b, S-1, endOfE)).getAsBits());
		byte[] E = bitsToBytes(b, S-1, endOfE);
		byte[] M = bitsToBytes(b, endOfE-1, endOfM);

		S_[1] = (byte) (8 - ((S-1) - endOfE + 1)%8);
		if(S_[1] == 8)S_[1]=(byte)0;
		S_[2] = (byte) (8 - ((endOfE-1) - endOfM + 1)%8);
		if(S_[2] == 8)S_[2]=(byte)0;

		byte[][] rtn = new byte[3][];
		rtn[0] = S_;
		rtn[1] = E;
		rtn[2] = M;
		return rtn;
				/*
		short bitCntEMod8 = (short) ((S-endOfE+1)%8);
		byte[] E = new byte[(S-endOfE+1)/8 + ((bitCntEMod8==0)? 0: 1)];
		E[0] = 0;
		for(short i = 0; i < bitCntEMod8; i++)
		{

		}*/
	}

		public byte[][] floatingPoint(int S, int endOfE){return floatingPoint(S, endOfE, 0);}
		public byte[][] floatingPoint(double anytn){return floatingPoint(63, 52);}
		public byte[][] floatingPoint(float anytn){return floatingPoint(31, 23);}
		/**
		 * byte[]配列から所望の1bitを得る。
		 *
		 * @param bytes byte[]配列
		 * @param idx bytes全体を2進数とみたとき、2の何乗の位を取得するか指定する。
		 * @return true→1, false→0
		 */
		public static boolean getBitFromBytes(byte[] bytes, int idx)
		{
			short idxInTheByte = (short)(idx%8);
			//idxを含む1byteのbit列を取り出した時の2^idxInAByteの位がidx
			return ((bytes[bytes.length-1-(idx/8)] % longPow(2, idxInTheByte+1)) / longPow(2, idxInTheByte))==1;

		}


		/**
		 * bit(boolean)の配列をbyte[]に変換する。<br>
		 * 入力の要素数が8を超える場合、戻り値のbyte[]の要素数は1を超える
		 * @param bools
		 * @return
		 */
		private static byte[] bitsToBytes(boolean[] bools)
		{
			short idxBools = 0, idxBytes = 0;
			boolean[] bools_;

			byte[] bytes;
			if(bools.length%8 == 0)
			{
				if(bools.length==8)
				{
					bytes = new byte[1];
					bytes[0] = 0;
					for(short i=0; i < 8; i++)
						if(bools[i])
							bytes[0] += longPow(2, 7-i);

					return bytes;

				}
				else
					bytes = new byte[bools.length/8];
			}
			else
			if(bools.length > 8)
			{
				bytes = new byte[bools.length/8 + 1];
				bools_ = new boolean[bools.length%8];
				for(short i=0; idxBools < bools.length%8; idxBools++, i++)
					bools_[i] = bools[idxBools];
				bytes[0] = bitsToBytes(bools_)[0];
				idxBytes = 1;
			}
			else
			{
				bytes = new byte[1];
				bytes[0] = 0;
				for(short i=0; i < bools.length; i++)
					if(bools[i])
						bytes[0] += longPow(2, bools.length-1-i);

				return bytes;
			}

			for(; idxBytes < bytes.length; idxBytes++)
			{
				bools_ = new boolean[8];

				for(short i=0; i < 8; idxBools++, i++)
					bools_[i] = bools[idxBools];

				bytes[idxBytes] = bitsToBytes(bools_)[0];
			}

			return bytes;
		}

			public static byte[] bitsToBytes(byte[] bits)
			{
				boolean[] bln = new boolean[bits.length];
				for(short i=0; i < bits.length; i++)
					bln[i] = (bits[i]!=0);
				return bitsToBytes(bln);
			}
		/**
		 * 開始/終了インデックス(右端を1の位として2の何乗の位か)を指定して、byte配列からbit列を取り出す。<br>
		 * 取り出したbit列はbyte[]として返却する。<br>
		 * 開始インデックスと終了インデックスは逆でも問題ない
		 * @param bytes
		 * @param startIdx
		 * @param endIdx
		 * @return
		 */
		public static byte[] bitsToBytes(byte[] bytes, int startIdx, int endIdx)
		{
			if(endIdx < startIdx)
			{
				int tmp = endIdx;
				endIdx = startIdx;
				startIdx = tmp;
			}
			boolean[] bools = new boolean[endIdx-startIdx+1];
			for(int i=startIdx; i < endIdx+1; i++)
				bools[bools.length-1-(i-startIdx)] = getBitFromBytes(bytes, i);
			return bitsToBytes(bools);
		}

	/**
	 * floatingPoint(int S, int endOfE)で得たbyte[][]を入力されると、対応するdouble値を返却する<br>
	 * 情報の一部が切り捨てられることがあるので注意(warnWhenKilled = trueならコンソールに警告文を表示する)
	 * @param bytess <a href='eclipse-javadoc:%E2%98%82=statistical/src%3Cstatistical%7BBytesStrConverter.java%E2%98%83BytesStrConverter~floatingPoint%E2%98%82%E2%98%82floatingPoint%E2%98%82int%E2%98%82int'>floatingPoint(int S, int endOfE)</a>における返却値
	 * @param warnWhenKilled trueなら仮数部の一部が切り取られたときに警告文をコンソールに出力。省略可(その場合はfalse)
	 * @return 対応するdouble値
	 * @see #floatingPoint(int S, int endOfE)
	 */
	public double floatingPoint(byte[][] bytess, boolean warnWhenKilled)
	{
		/*
		 * java double
		 * S = 63		1
		 * E = 62～52	11
		 * M = 51～0	52
		 */
		boolean isMinus = (bytess[0][0] != 0);
		short lenOfE = (short) (bytess[1].length*8 - bytess[0][1]);
		short lenOfM = (short) (bytess[2].length*8 - bytess[0][2]);
		if(lenOfE>11)
		{
			System.out.println("BytesStrConverterクラスでエラー: floatingPointメソッドにて、指数部のbitが大きすぎるため、この値"+stringFloatingPoint(bytess)+"をdoubleに変換できません。");
			return Double.NaN;
		}
		if(warnWhenKilled && lenOfM>52)
			System.out.println("BytesStrConverterクラスで警告: floatingPointメソッドにて、仮数部が大きすぎたため、その一部は0捨1入されました。");

		long biasOfE = (longPow(2,lenOfE-1)-1);
		long E = new BytesStrConverter(bytess[1]).unsignedLong()-biasOfE;
		long M;
		if(lenOfM>52)
		{
			byte[] tmp = new byte[7];//bytess[2]から下位7バイトを得ればよい
			for(byte i = 0; i < 7; i++)
				tmp[7-1-i] = bytess[2][bytess[2].length-1-i];
			M = new BytesStrConverter(tmp).unsignedLong()+longPow(2, 52);
		}
		else
		{
			M = new BytesStrConverter(bytess[2]).unsignedLong();
		}
		long bits = (isMinus?1:0)*longPow(2, 63) + E*longPow(2, 52) + M;
		return Double.longBitsToDouble(bits);



	}
		public double floatingPoint(byte[][] bytess){return floatingPoint(bytess, false);}

		public double floatingPoint(boolean warnWhenKilled)
		{
			double dbl = 0;
			BytesStrConverter bsc = new BytesStrConverter(s);
			return floatingPoint(bsc.floatingPoint(dbl), warnWhenKilled);
		}

		public double floatingPoint()
		{
			double dbl = 0;
			BytesStrConverter bsc = new BytesStrConverter(s);
			return floatingPoint(bsc.floatingPoint(dbl));
		}

		public String stringFloatingPoint(byte[][] bytess)
		{
			String rtn = ((bytess[0][0] != 0)?"-":"");
			rtn += "1.";
			short lenOfE = (short) (bytess[1].length*8 - bytess[0][1]);
			short lenOfM = (short) (bytess[2].length*8 - bytess[0][2]);

			for(int i=0; i < lenOfM; i++)
				rtn += getBitFromBytes(bytess[2], lenOfM-1-i)?"1":"0";

			rtn += "(2)×2^";
			long biasOfE = (longPow(2,lenOfE-1)-1);
			rtn += (new BytesStrConverter(bytess[1]).unsignedLong()-biasOfE) +"";

			return rtn;
		}

	/**
	 * byte配列を受け取り、bit列(0と1)の文字列に変換する。
	 */
	public static String getAsBits(byte[] bytes)
	{
		byte tmp;
		String rtn = "";
		for(int i=0; i < bytes.length; i++)
		{
			tmp = bytes[i];
			if(tmp>=0)//-2^7の位(byte型符号bit)
			{//正の場合
				rtn += "0";
			}
			else
			{//負の場合
				/* -2^7の位が1
				 * →この位を0にするには、2^7を足す
				 */
				rtn += "1";
				tmp += 128;
			}
			rtn += ((tmp/64)%2);//2^6の位
			rtn += ((tmp/32)%2);//2^5の位
			rtn += ((tmp/16)%2);//2^4の位
			rtn += ((tmp/8)%2);//2^3の位
			rtn += ((tmp/4)%2);//2^2の位
			rtn += ((tmp/2)%2);//2^1の位
			rtn += ((tmp/1)%2);//2^0の位
		}
		return rtn;
	}


	public String getAsBits()
	{
		return getAsBits(b);
	}

	/**
	 * ビット長を求める。
	 * @param bsc ビット長を求めたい数字。BytesStrConverter型以外にはdouble型、float型、long型、int型、short型、String型、byte[]型を受け付ける。
	 * @param pack0 (戻り値の説明を参照されたい)
	 * @return pack0がfalseなら必要なbit数、trueならそのbit数以上の最近の8の冪(byte配列の0番目に0を詰めた桁数)
	 */
	public static int getBitLength(BytesStrConverter bsc, boolean pack0)
	{
		if(pack0)return 8*bsc.b.length;
		String str = bsc.getAsBits();
		int rtn = str.length(), maxI = rtn;
		for(int i=0; i < maxI; i++)
		{
			if(str.charAt(i) == '1')
				return rtn;
			rtn--;
		}
		return rtn;
	}

		public static int getBitLength(double d,		boolean pack0){return getBitLength(new BytesStrConverter(d),		pack0);}
		public static int getBitLength(float f,		boolean pack0){return getBitLength(new BytesStrConverter(f),		pack0);}
		public static int getBitLength(long l,		boolean pack0){return getBitLength(new BytesStrConverter(l),		pack0);}
		public static int getBitLength(int i,			boolean pack0){return getBitLength(new BytesStrConverter(i),		pack0);}
		public static int getBitLength(short sh,		boolean pack0){return getBitLength(new BytesStrConverter(sh),	pack0);}
		public static int getBitLength(String str,		boolean pack0){return getBitLength(new BytesStrConverter(str),	pack0);}
		public static int getBitLength(byte[] bytes,	boolean pack0){return getBitLength(new BytesStrConverter(bytes),	pack0);}

	/**
	 * 文字列として入力されたbit列をbyte配列に変換する
	 * @param str
	 */
	public static byte[] strBitsToBytes(String str)
	{
		booleanArrayMirror bools = new booleanArrayMirror(str.length());
		for(short i=0; i < str.length(); i++)
			bools.set(i, str.charAt(i)!='0');
		return bools.getAsBytes();
	}

	public static long longPow(long base, long idx)
	{
		long rtn = 1;
		for(long i=0; i<idx; i++)
			rtn *= base;
		return rtn;
	}



}
