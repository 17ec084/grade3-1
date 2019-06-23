/**
 *
 */
package kabuLab;

/**
 * 「読み込んだCSVを、罫線素片を用いて表示するだけのメソッド」のようなクラス。<br>
 * ReadCSVクラスを継承しているが、オーバーライドは一切ないので、<br>
 * CSVの編集が目的ならReadCSVのドキュメントを参照されたい。<br><br>
 * また、すべてのメンバはprivateあるいはprotectedとされている。<br><br>
 * ShowCSV csv = new ShowCSV(String csvPass, true, int width, int start)<br>
 * とすると、最大半角width文字を1行に表示できるコンソールで、改行なく表示される。<br>
 * 但しそれをするのなら、何列目を一番左にするかを言及する必要があり、これがstartである。<br><br>
 * 左右のスクロールバーが表示され、自動改行によるレイアウト崩れの恐れがないコンソールの場合は<br>
 * ShowCSV csv = new ShowCSV(String csvPass, true)<br>
 * のようにできる
 * @see kabuLab.ReadCSV
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class ShowCSV extends ReadCSV
{
	//フィールド
	private int cntOfC;
	private int cntOfR;
	private final int MAX_INT=2147483647;


    //コンストラクタ
	public ShowCSV(String passOrText, boolean mode)
	{
		super(passOrText, mode);
		showCSV(MAX_INT,0);
	}

	public ShowCSV(String passOrText, boolean mode, int width, int start)
	{
		super(passOrText, mode);
		showCSV(width, start);

	}


	//メソッド
	/**
	 * コンストラクタのオーバーロードを実現するため、コンストラクタの処理の一部をこのメソッドへ移動した
	 * @param passOrText
	 * @param mode
	 * @param width
	 * @param start
	 */
	protected void showCSV(int width, int start)
	{
		/* メモ:super.getCell(r,c)で、
		 * r行c列目のセルを取得可*/

		int cntAccepted;
		int[] lens;
		int[] tmp;

		cntOfC=super.getCntColumn();
		cntOfR=super.getCntRow();
//		System.out.println("getCntAccepted called");
		tmp=getCntAccepted(width, start);
		cntAccepted=tmp[0];//何列分を1行の文字列で表示出来るか
		//tmp[1] (sumLen)は使わない
		lens= new int[(tmp.length)-2];
		for(int i=0; i<lens.length; i++)
		{
			lens[i]=tmp[i+2];
		}

        printTable(cntAccepted, start, lens);

	}

		/**
		 * 読み込んだcsvを、罫線素子を用いて表として表示するとき、<br>
		 * 以下に示すようなint[]型戻り値を求める。
		 * @return 0番目 cntAccepted 何列分を1行の文字列と出来るか<br>
		 * 1番目 sumLen(cntAccepted,start) そのとき、半角何文字分のスペースを要するか<br>
		 * 2番目以降 lens[] 各列は半角何文字分のスペースを要するか
		 */
		protected int[] getCntAccepted(int width, int start)
		{
			int cntAccepted=0;
			int i;

	        for(i=start;i<cntOfC && width>len(i,start); i++){}
	        if(width>=len(i,start))
	        {
	        	cntAccepted=i;//1行に収まるセル数
	        }
	        else
	        {
	        	cntAccepted=i-1;//1行に収まるセル数
	        }

			int[] res= new int[cntAccepted+2];

	        res[0]=cntAccepted;
	        res[1]=len(res[0],start);
//	        System.out.println("len called");
	        for(i=start; i<start+cntAccepted; i++)
	        {
//		        System.out.println("len called for column "+i);
	        	res[i-(start)+2]=len(i,i);
//	        	System.out.println("res["+(i-(start)+2)+"]="+res[i-(start)+2]);
	        }

	        return res;
		}

	    	/**
	    	 * columnStartIndex列目から
	    	 * columnLastIndex行目までを罫線を使って表現した時
    		 * 半角何文字になるかを返す
    		 */
    	    protected int len(int columnLastIndex, int columnStartIndex)
	        {
	            int res=2;//左端の罫線素片
	            int maxColLen;
	            int biLen=0;//半角1文字→2文字、5文字→6文字に変換(罫線素片のレイアウトのため)

			    /* super.getCell(r,c)で、
    			 * r行c列目のセルを取得可*/
    	        for(int columnIndex=columnStartIndex; columnIndex<=columnLastIndex; columnIndex++)
    	        //必要な各列について
    	        {
    	        	maxColLen=0;
    	        	for(int i=0;i<cntOfR;i++)
    	        	//それぞれの行で
    	        	{
    	        		//それぞれ何文字になるか知り
    	        		biLen=getHan1Zen2(super.getCell(i, columnIndex));
//    	        		System.out.println(i+"行目におけるbiLenは"+biLen+"であり、このときmaxColLen="+maxColLen);
    	        		if(biLen%2==1)
    	        		{
    	        			biLen++;
    	        		}
//    	        		System.out.println(super.getCell(i, columnIndex)+"でbiLen="+biLen);

    	        		//その最大値を求め、
    	        		if(maxColLen<biLen)
    	        		{
    	        			maxColLen=biLen;
    	        		}
    	        	}
    	        	//さらに間の罫線素片のため2を足す
    	            res+=maxColLen+2;
//        	        System.out.println("res="+res);
    	        }
//    	        System.out.println("最終的に、res="+res);
    	        return res;
    	    }
    	        /**
    	         * 文字列の文字数を取得する。但し全角文字は2文字と数える。<br>
    	         * 引用:https://blog.java-reference.com/common-get-zen2han1/
    	         * @return 文字数
    	         */
    	        protected int getHan1Zen2(String str)
    	        {
//    	        	System.out.println("str="+str);
    	        	//戻り値
    	        	int ret = 0;

	            	//全角半角判定
    	        	char[] c = str.toCharArray();
        	    	for(int i=0;i<c.length;i++)
            		{
//        	    		System.out.println("c["+i+"]="+c[i]+"="+(int)c[i]);
            		    if(String.valueOf(c[i]).getBytes().length <= 1)
            	    	{
            	    		ret += 1; //半角文字なら＋１
            	    		if(c[i]==newR || c[i]==newC || c[i]==eof)
            	    		//但し、もし区切りや改行を表す文字だった場合は
            	    		{
            	    			ret -= 1;
            	    		}
            	    	}
            		    else
            		    {
            		    	ret += 2; //全角文字なら＋２
            		    }
            		}

                	return ret;
            	}

    	 /**
    	  * 読み込んだcsvを、罫線素子を用いた表としてコンソールに表示する。
    	  * まず各列で最大の文字数のものを探し、記憶する
    	  * @param cntAccepted
    	  * @param start
    	  */
		protected void printTable(int cntAccepted, int start, int[] lens)
		{

//			System.out.println("["+lens[2]+"]");

			printUpSide(lens);//表の上辺を適切に表示
			for(int i=0; i<cntOfR-1; i++)//「最終行以外の」
			{
				printRow(i,lens, start);//各行を適切に表示(左右辺含む)
			}
			printDownSide(lens, start);//「最終行と、」表の下辺を適切に表示

			/*
			 * ┌─┬───┐by printUpSide
			 * │a │b     │by printRow
			 * ├─┼───┤by printRow
			 * │c │ddddd │by printRow
			 * ├─┼───┤by printRow
			 * │ee│f     │by printDownSide
			 * └─┴───┘by printDownSide
			 */
		}

			/**
			 * 表の上辺を適切に表示<br>
			 * <img src="img/printUpSide.png">
			 *
			 *
			 * @param lens 各列の最大文字列数 int[]
			 */
			protected void printUpSide(int[] lens)
			{
				System.out.print("┌");
				for(int i=0; i<(lens.length)-1; i++)
				{
					for(int j=0; j<lens[i]-4; j+=2)
					//4を引いているのは、左右の端の罫線素片の分
					{
						System.out.print("─");
					}
					System.out.print("┬");
				}
				for(int j=0; j<lens[(lens.length)-1]-4; j+=2)
				{
					System.out.print("─");
				}
				System.out.println("┐");
			}

			/**
			 * 各行を適切に表示(左右辺含む)<br>
			 *  <img src="img/printRow.png">
			 * @param lens 各列の最大文字列数 int[]
			 */
			protected void printRow(int rowIndex, int[] lens, int start)
			{
				System.out.print("│");
				for(int i=0; i<lens.length; i++)
				{
					System.out.print(getCell(rowIndex,start+i));
					for(int j=0; j<lens[i]-getHan1Zen2(getCell(rowIndex,start+i))-4; j++)
					{
						System.out.print(" ");
					}
					System.out.print("│");
				}
				System.out.println();
				System.out.print("├");
				for(int i=0; i<(lens.length)-1; i++)
				{
					for(int j=0; j<lens[i]-4; j+=2)
					{
						System.out.print("─");
					}
					System.out.print("┼");
				}
				for(int j=0; j<lens[(lens.length)-1]-4; j+=2)
				{
					System.out.print("─");
				}
				System.out.println("┤");
			}

			/**
			 * 表の下辺を適切に表示<br>
			 * <img src="img/printDownSide.png">
			 *
			 *
			 * @param lens 各列の最大文字列数 int[]
			 */
			protected void printDownSide(int[] lens, int start)
			{
				System.out.print("│");
				for(int i=0; i<lens.length; i++)
				{
					System.out.print(getCell(cntOfR-1,start+i));
					for(int j=0; j<lens[i]-getHan1Zen2(getCell(cntOfR-1,start+i))-4; j++)
					{
						System.out.print(" ");
					}
					System.out.print("│");
				}
				System.out.println();
				System.out.print("└");
				for(int i=0; i<(lens.length)-1; i++)
				{
					for(int j=0; j<lens[i]-4; j+=2)
					{
						System.out.print("─");
					}
					System.out.print("┴");
				}
				for(int j=0; j<lens[(lens.length)-1]-4; j+=2)
				{
					System.out.print("─");
				}
				System.out.println("┘");

			}
/*
		    private int getMaxLen(int cntAccepted, int start)
		    {
		    	int len;
		    	int max=0;
				for(int i=0; i<cntOfR; i++)
				{
					len=2;
					for(int j=start; j<start+cntAccepted; j++)
				    {
			    		len+=getHan1Zen2(super.getCell(i, j));
			    		len+=2;
		    		}
					if(len>max){max=len;}
				}
				return max;
		    }
*/

}
