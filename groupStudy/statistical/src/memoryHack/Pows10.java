package memoryHack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import memoryHack._hirata.P;

/**
 * Pows10クラス:Pクラスのショートコード化を支援するクラス。それをbooleanArray型に変換して出力したりする。<br>
 * Pクラス:10の冪を2進表現したものをできるだけ多く記憶するクラス
 *
 * @author 17ec084(http://github.com/17ec084)
 *
 */
public class Pows10
{
	/**
	 * 10のindex乗のfrom0to9倍をbooleanArrayとして返却する。<br>
	 * */
	public static booleanArray get(int index, byte from0to9)
	{
		boolean pathDetected = (new P().d) != 0;
		if(!pathDetected)
			new Pows10();
		int maxIndex = getMaxIndex(readPath());

		if(from0to9<0)
		{
			System.out.println("Pows10クラスのgetメソッドのfrom0to9が不正");
			return null;
		}
		if(index<=maxIndex)//所望の冪が既に計算済みの場合
		{
			BitsCalculator bc = new BitsCalculator(pow10(index));
			BitsCalculator oldBc = bc.clone();

			for(int i=0; i < from0to9-1; i++)
			{
				bc = new BitsCalculator(bc.plus(oldBc, true));
//booleanArray.newSetFromBytes(bc.getAsBytes()).dump();
			}
			if(from0to9 == 0)
			{
				bc = new BitsCalculator(bc.minus(oldBc));
			}
			return booleanArray.newSetFromBytes(bc.getAsBytes());
		}
		else//まだ計算していなかった場合
		{


			for(; maxIndex < index; maxIndex++)
			{

				BitsCalculator bc = new BitsCalculator(pow10(maxIndex));
				BitsCalculator oldBc = bc.clone();

				for(int i=0; i < 9; i++)
				{
					bc = new BitsCalculator(bc.plus(oldBc, true));
				}
				//ストリングに変換し配列として記録
				String str = (String)new BSC(bc.getAsBytes()).result;
				str = escapeKiller(str);//改行コード等を変換

				appendP(str);//P.javaに追加

			}
			return get(index, from0to9);//再試行すればよい。
		}
	}
		private static int getMaxIndex(String path)
		{
			//行数-3を返却すればよい。

			File f = new File(path);
			FileReader fr;
			try
			{
				fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				int rtn = -3;
				while(br.readLine() != null)
					rtn++;
				fr.close();
				return rtn;

			}
			catch (IOException e){e.printStackTrace();}

			return -4;

		}

		private static String pow10(int index)
		{
			try
			{
				File f = new File(readPath());
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				for(int i=-1; i < index; i++)
					br.readLine();
				fr.close();
				return 	escapeRevivaler(br.readLine());

			}
			catch (IOException e) {e.printStackTrace();}
			return null;
		}

		/**
		 * escapeKillerの逆をやることで、文字列を本来通りに取り出す。
		 * @param str
		 * @return
		 */
		private static String escapeRevivaler(String str)
		{

			int len = str.length();

			char tmp;

			for(int i=0; i < len; i++)
				if((tmp=str.charAt(0))=='\\')
					switch(str.charAt(1))
					{
						case 'b':
							str = str.substring(2) + "\b";
							i++;
						break;
						case 't':
							str = str.substring(2) + "\t";
							i++;
						break;
						case 'n':
							str = str.substring(2) + "\n";
							i++;
						break;
						case 'f':
							str = str.substring(2) + "\f";
							i++;
						break;
						case 'r':
							str = str.substring(2) + "\r";
							i++;
						break;
						case '\"':
							str = str.substring(2) + "\"";
							i++;
						break;
						case '\'':
							str = str.substring(2) + "\'";
							i++;
						break;
						case '\\':
							str = str.substring(2) + "\\";
							i++;
						break;
						default:
							str = str.substring(1) + "\\";
						break;
					}
				else
					str = str.substring(1) + tmp;

			return str;
		}

		/**
		 * P.javaへの保存の際、改行その他訳の分からないエスケープシーケンスが実行されると予期せぬバグの元となる。<br>
		 * 特に改行については必ずバグを引き起こす。(改行を元に管理しているため)<br>
		 * そこでエスケープ文字を無効にすべく、\の隣に1つずつ\を追加する。
		 * @param str
		 * @return
		 */
		private static String escapeKiller(String str)
		{
			/* 参考:
			 * エクリプスのエラーコメント「無効なエスケープ・シーケンス (有効なものは  \b  \t  \n  \f  \r  \"  \'  \\  です」
			 */
			int len = str.length();
			char tmp;
			//replaceAllはあまりにもややこしいので、1文字ずつ処理する。
			for(int i=0; i < len; i++)
			//リングを作る(回りくどい(本当に回っている)ことをするのはメモリ節約のため。)
				switch((tmp=str.charAt(0)))
				{
					case '\b':
						str = str.substring(1) + "\\b";
					break;
					case '\t':
						str = str.substring(1) + "\\t";
					break;
					case '\n':
						str = str.substring(1) + "\\n";
					break;
					case '\f':
						str = str.substring(1) + "\\f";
					break;
					case '\r':
						str = str.substring(1) + "\\r";
					break;
					case '\"':
						str = str.substring(1) + "\\\"";
					break;
					case '\'':
						str = str.substring(1) + "\\\'";
					break;
					case '\\':
						str = str.substring(1) + "\\\\";
					break;
					default:
						str = str.substring(1) + tmp;
					break;
				}

			return str;

		}

		private static void appendP(String str)
		{
			try
			{
				File f = new File(readPath());
				File tmpF = new File(readPath().replaceFirst("P\\.java", "_hirata_Pows10_Append"));
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				FileWriter fw = new FileWriter(tmpF);
				String readStr = "";
				while(readStr!=null)
				{
					while
					(
						(
							(readStr=br.readLine())!=null
							&&
							!readStr.matches("\\*/")
						)
					)
					{
						fw.write(readStr+"\r\n");
					}
					if(readStr != null)
					{
						fw.write(str+"\r\n"+"*/");
						break;
					}
				}

				int i = 0;

				fw.close();//クローズしないと書き込まれない
				fr.close();//クローズしないと消せない

				while(!f.delete())
				{
					if(i > 100)
					{
						System.out.println("P.java cannot be deleted.");
						return;
					}
					i++;
				}
				i = 0;
				while(!tmpF.renameTo(f))
				{
					if(i > 100)
					{
						System.out.println("P.java cannot be regenerated.");
						return;
					}
					i++;
				}
			}
			catch (IOException e) {e.printStackTrace();}

		}






	/**
	 * 参考: http://java.it-manual.com/advanced/file-update.html
	 */
	Pows10()
	{

		boolean pathDetected = (new P().d) != 0;

		String path;
		if(!pathDetected)
		{
			path = findP();
			String str="";
			savePath(path);
			if(path.matches(".*P\\.java not found.*"))
			{
				System.out.println(path);
				return;
			}
			try
			{
				String tmpPath = path.replaceFirst("P\\.java", "_hirata_Pows10");
				File f = new File(path), tmpF = new File(tmpPath);
				FileReader fr = new FileReader(f);//frを省略すると閉じられなくなる。
				BufferedReader br = new BufferedReader(fr);
				FileWriter fw = new FileWriter(tmpF);
				boolean isFirst = true;

				while(str!=null)
				{
					while
					(
						(
							(str=br.readLine())!=null
							&&
							!str.matches
							(".*" + " d=0" + ".*")
						)
					)
					{
						if(isFirst)isFirst=false; else fw.write("\r\n");
						fw.write(str);
					}
					if(str != null)
					{
						str = str.replaceAll(" d=0", " d=1");
						if(isFirst)isFirst=false; else fw.write("\r\n");
						fw.write(str);
					}
				}

				int i = 0;

				fw.close();//クローズしないと書き込まれない
				fr.close();//クローズしないと消せない

				while(!f.delete())
				{
					if(i > 100)
					{
						System.out.println("P.java cannot be deleted.");
						return;
					}
					i++;
				}
				i = 0;
				while(!tmpF.renameTo(f))
				{
					if(i > 100)
					{
						System.out.println("P.java cannot be regenerated.");
						return;
					}
					i++;
				}

			}
			catch (FileNotFoundException e)	{e.printStackTrace();}catch (IOException e) {e.printStackTrace();}
		}
		else//pathが既にわかっている
		{
			//何もしなくてよい
		}

	}

	public String findP()
	{
		File f = new File("").getAbsoluteFile();
		while(!(f.getName().equals("statistical")))
			f = f.getParentFile();

		return search(f).replaceAll("\\\\_hirata_breakRecursion", "");
	}

		private String search(File dir)
		{
			File[] childlen = dir.listFiles();
			String rtn;
	        for(int i=0; i<childlen.length; i++)
	        {
//System.out.println(childlen[i].getName());
	        	if
	        	(
	        			childlen[i].isDirectory()
	        		&& 	childlen[i].getName().equals("_hirata")
	        		&& 	has(childlen[i],"P.java")
	        	)
	        		return childlen[i].getPath() + "\\P.java"+"\\_hirata_breakRecursion";
	        	else
	        	if(childlen[i].isDirectory())
	                if((rtn=search(childlen[i])).matches(".*_hirata_breakRecursion"))
	                	return rtn;
	        }
	        return "P.java not found";
	    }

			private boolean has(File dir, String child)
			{
				File[] files = dir.listFiles();
				for(int i=0; i < files.length; i++)
					if(files[i].getName().equals(child))
						return true;
				return false;
			}

		private void savePath(String path)
		{
			File f = new File("_hirata_pathInfo");
			try
			{
				FileWriter fw = new FileWriter(f);
				fw.write(path);
				fw.close();
			}catch (IOException e){e.printStackTrace();}

		}

		private static String readPath()
		{
			File f = new File("_hirata_pathInfo");
			try
			{
				FileReader fr = new FileReader(f);
				String rtn = new BufferedReader(fr).readLine();
				fr.close();
				return rtn;
			}catch (IOException e){e.printStackTrace();}
			return null;

		}

/*
	java.io.File file = new java.io.File(f.getPath() + "\\_hirata\\Pows10.java");
	 // ファイルが存在するかどうかを判定
    if ( !file.exists() ) {
        // ファイルが存在しない場合は処理終了
        System.out.println( "ファイルが存在しない" );
        return;
    }

    // 指定されたパスがファイルかどうかを判定
    if ( !file.isFile() ) {
        // ディレクトリを指定した場合は処理終了
        System.out.println( "ファイル以外を指定" );
        return;
    }

    // ファイルが読み込み可能かどうかを判定
    if ( !file.canRead() ) {
        // ファイルが読み込み不可の場合は処理終了
        System.out.println("ファイルが読み込み不可");
        return;
    }

    // ファイルが書き込み可能かどうかを判定
    if ( !file.canWrite() ) {
        // ファイルが読み込み不可の場合は処理終了
        System.out.println("ファイルが読み込み不可");
        return;
    }
*/
}
