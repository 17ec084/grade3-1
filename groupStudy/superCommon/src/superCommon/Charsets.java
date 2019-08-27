/**
 *
 */
package superCommon;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

/**
 *
 * javaがサポートする文字コードの一覧は、<br>
 * https://docs.oracle.com/javase/jp/1.5.0/guide/intl/encoding.doc.html<br>
 * から閲覧できる。<br>
 * でも、これらを表すstatic final定数は見当たらなかった。<br>
 * 定数があればeclipseなどのエディタで選択が表示されるので便利になる。<br>
 * そこで、作ってみた。<br>
 * ついでなので、適当な文字列と、UTF-8でその文字列を正しく表したものを入力すると文字コードを調べるメソッドdetectや、<br>
 * String.getByteの第2引数として利用できる、文字コードを表す文字列に変換するメソッド<br>
 * <code>Charsets(文字コードを表すObject[]定数).toString(void)</code><br>
 * なども用意。<br>
 * <!-- 廃止 また<code>isSupported(String 文字列,Object[] クラス定数)</code>とすると、<br>
 * 文字コードがその文字列をサポートしているかがわかる。-->
 * <dl>
 * <dt>
 * 前提:
 * </dt>
 * <dd>
 * {@link String#getBytes()}が{@link String#getBytes(String) String.getBytes("UTF-8")}に等価であること。<br>
 * つまりデフォルトエンコーディングがBOM無しUTF-8であること。この前提を満たさない場合はコンストラクタあるいはstaticイニシャライザが例外を投げる。
 * </dd>
 * </dl>
 * @author <a href=http://github.com/17ec084>Tomotaka Hirata(17ec084)</a>
 *
 */
public class Charsets
{

	private static String systemCharset = System.getProperty("file.encoding");

	/**
	 * 各文字コードの言語の定数クラス。<br>
	 * 外部からの利用に当たっては、Charsetsクラスをstaticインポートし、言語クラスはstaticインポートしないことを勧める。<br>
	 * Charsetsクラスをstaticインポートすることで定数利用の際の冗長さを(少しだけ)改善できるが、言語クラスをstaticインポートしてしまうと、
	 * eclipseなどで入力補助を利用しようとしても、コードクラスの定数と重なり、混乱を招く。
	 * @author <a href=http://github.com/17ec084>Tomotaka Hirata(17ec084)</a>
	 *
	 */
	public static class 言語
	{
		private static byte origin = 0, i = origin;
		//変数とインクリメントを使うことで、順番の変化・追加に対して柔軟になる。
		public static final byte
		日本語 = i++,
		ラテン文字 = i++,
		アラビア語 = i++,
		ヘブライ語 = i++,
		キリル文字 = i++,
		ギリシャ文字 = i++,
		ロシア語 = i++,
		東欧 = i++, ウクライナ語 = 東欧, クロアチア語 = 東欧, ルーマニア語 = 東欧, トルコ語 = 東欧,
		バルト諸語 = i++,エストニア語 = バルト諸語, ラトビア語 = バルト諸語, リトアニア語 = バルト諸語,
		中国語_繁体字 = i++,
		中国語_簡体字 = i++,
		韓国語 = i++,
		タイ語 = i++,
		西欧 = i++, EU加盟国のほとんど = 西欧,
		北欧 = i++, アイスランド語 = 北欧,
		イスラム圏 = i++,
		インド語 = i++,
		ベトナム語 = i++,
		不明あるいは全世界 = i++,
		総数 = (byte) ((i++)-origin);
		private 言語(){}

	}

	/**
	 * 各文字コードのコードの定数クラス。<br>
	 * 外部からの利用に当たっては、Charsetsクラスをstaticインポートし、コードクラスはstaticインポートしないことを勧める。<br>
	 * Charsetsクラスをstaticインポートすることで定数利用の際の冗長さを(少しだけ)改善できるが、コードクラスをstaticインポートしてしまうと、
	 * eclipseなどで入力補助を利用しようとしても、言語クラスの定数と重なり、混乱を招く。
	 * @author <a href=http://github.com/17ec084>Tomotaka Hirata(17ec084)</a>
	 *
	 */
	public static class コード
	{
		private static byte origin = (byte) (言語.総数), i = origin;
		public static final byte
		ISO = i++,
		KOI8 = i++,
		ASCII = i++,
		UTF = i++,
		windows = i++,
		Big5 = i++,
		EUC = i++,
		GB = i++, GBK = GB,
		IBM = i++,
		TIS = i++,
		ISC = i++,
		Mac = i++,
		未分類 = i++,
		総数 = (byte)((i++)-origin);
		private コード(){}

	}


	/**
	 * 補足情報の定数クラス
	 *
	 */
	public static class 補足
	{
		private static byte origin = (byte) (言語.総数+コード.総数), i = origin;
		public static final byte
		Unicodeからの変換のみ = i++,
		Unicodeへの変換のみ = i++,
		総数 = (byte)((i++)-origin);
		private 補足(){}

	}

	private final static Object[][] charsets;//static{}内のリファクタリング処理にて書き込まれる。

	private static int i = 0;

	/**
	 * <code>{通し番号(, (言語ジャンル|コードジャンル))*,java.nio API用の正準名文字列(, (言語ジャンル|コードジャンル))*, java.ioおよびjava.lang API用の正準名文字列, (言語ジャンル|コードジャンル)*}</code><br>
	 *
	 */
	public static final Object[]
	//javaでサポートされないもの
	unsupported
	=
	{
		-1,
		null,
		null
	},

		//基本エンコーディングセット(lib/rt.jarに含まれている)
		ISO_8859_1
		=
		{
			i++,
			言語.ラテン文字,
			コード.ISO,
			"ISO-8859-1",
			"ISO8859_1"
		},
		ISO_8859_2
		=
		{
			i++,
			言語.ラテン文字,
			コード.ISO,
			"ISO-8859-2",
			"ISO8869_2"
		},
		//ISO_8859_3は後のほうで定義されている
		ISO_8859_4
		=
		{
			i++,
			言語.ラテン文字,
			コード.ISO,
			"ISO-8859-4",
			"ISO8859_4"
		},
		ISO_8859_5
		=
		{
			i++,
			言語.ラテン文字,
			言語.キリル文字,
			コード.ISO,
			"ISO-8859-5",
			"ISO8869_5"
		},
		//ISO_8859_6は後のほうで定義されている
		ISO_8859_7
		=
		{
			i++,
			言語.ラテン文字,
			言語.ギリシャ文字,
			コード.ISO,
			"ISO-8859-7",
			"ISO8869_7"
		},
		//ISO_8859_8は後のほうで定義されている
		ISO_8859_9
		=
		{
			i++,
			言語.ラテン文字,
			コード.ISO,
			"ISO-8859-9",
			"ISO8869_9"
		},
		/*ISO_8859_10 = unsupported,
		ISO_8859_11 = unsupported,
		ISO_8859_12 = unsupported,*/
		ISO_8859_13
		=
		{
			i++,
			言語.ラテン文字,
			コード.ISO,
			"ISO-8859-13",
			"ISO8859_13"
		},
		//ISO_8859_14 = unsupported,
		ISO_8859_15
		=
		{
			i++,
			言語.ラテン文字,
			コード.ISO,
			"ISO-8859-15",
			"ISO8859_15"
		},
		KOI8_R
		=
		{
			i++,
			言語.ロシア語,
			コード.KOI8,
			"KOI8-R",
			"KOI8_R"
		},
		US_ASCII
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.ASCII,
			"US-ASCII",
			"ASCII"
		},
		UTF_8N//ボム無し。これは実験により確認済み
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.UTF,
			"UTF-8",
			"UTF8"
		},
		UTF_16WithBom//ボム有り。これは実験により確認済み
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.UTF,
			"UTF-16",
			"UTF-16"
		},
		UTF_16NBE//ボム無し。これは実験により確認済み
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.UTF,
			"UTF-16BE",
			"UnicodeBigUnmarked"
		},
		UTF_16NLE//ボム無し。これは実験により確認済み
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.UTF,
			"UTF-16LE",
			"UnicodeLittleUnmarked"
		},
		windows_1250
		=
		{
			i++,
			言語.東欧,
			コード.windows,
			"windows-1250",
			"Cp1250"
		},
		windows_1251
		=
		{
			i++,
			言語.キリル文字,
			コード.windows,
			"windows-1251",
			"Cp1251"
		},
		windows_1252
		=
		{
			i++,
			言語.ラテン文字,
			コード.windows,
			"windows-1252",
			"Cp1252"
		},
		windows_1253
		=
		{
			i++,
			言語.ギリシャ文字,
			コード.windows,
			"windows-1253",
			"Cp1253"
		},
		windows_1254
		=
		{
			i++,
			言語.トルコ語,
			コード.windows,
			"windows-1254",
			"Cp1254"
		},
		//windows_1255 = unsupported,
		//windows_1256 = unsupported,
		windows_1257
		=
		{
			i++,
			言語.バルト諸語,
			コード.windows,
			"windows-1257",
			"Cp1257"
		},
		UTF_16BEWithBom
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.UTF,
			"利用不可",
			"UnicodeBig"
		},
		UTF_16LEWithBom
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.UTF,
			"利用不可",
			"UnicodeLittle"
		},



		//拡張エンコーディングセット (lib/charsets.jar に含まれている)


		Big5
		=
		{
			i++,
			言語.中国語_繁体字,
			コード.Big5,
			"Big5",
			"Big5"
		},
		Big5_HKSCS
		=
		{
			i++,
			言語.中国語_繁体字,
			コード.Big5,
			"Big5-HKSCS",
			"Big5_HKSCS"
		},
		EUC_JP
		=
		{
			i++,
			言語.日本語,
			コード.EUC,
			"EUC-JP",
			"EUC_JP"
		},
		EUC_KR
		=
		{
			i++,
			言語.韓国語,
			コード.EUC,
			"EUC-KR",
			"EUC_KR"
		},
		GB18030
		=
		{
			i++,
			言語.中国語_簡体字,
			コード.GB,
			"GB18030",
			"GB18030",
		},
		GB2312
		=
		{
			i++,
			言語.中国語_簡体字,
			コード.GB,
			コード.EUC,
			"GC2312",
			"EUC_CN"
		},
		GBK
		=
		{
			i++,
			言語.中国語_簡体字,
			コード.GBK,
			"GBK",
			"GBK"
		},
		IBM_Thai
		=
		{
			i++,
			言語.タイ語,
			コード.IBM,
			"IBM-Thai",
			"Cp838"
		},
		IBM00858
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.IBM,
			"IBM00858",
			"Cp858"
		},
		IBM01140
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.IBM,
			"IBM01140",
			"Cp1140"
		},
		IBM01141
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.IBM,
			"IBM01141",
			"Cp1141"
		},
		IBM01142
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.IBM,
			"IBM01142",
			"Cp1142"
		},
		IBM01143
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.IBM,
			"IBM01143",
			"Cp1143"
		},
		IBM01144
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.IBM,
			"IBM01144",
			"Cp1144"
		},
		IBM01145
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.IBM,
			"IBM01145",
			"Cp1145"
		},
		IBM01146
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.IBM,
			"IBM01146",
			"Cp1146"
		},
		IBM01147
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.IBM,
			"IBM01147",
			"Cp1147"
		},
		IBM01148
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.IBM,
			"IBM01148",
			"Cp1148"
		},
		IBM01149
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.IBM,
			"IBM01149",
			"Cp1149"
		},
		IBM037
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.IBM,
			"IBM037",
			"Cp037"
		}
		,
		IBM1026
		=
		{
			i++,
			言語.ラテン文字,
			言語.トルコ語,
			コード.IBM,
			"IBM1026",
			"Cp1026"
		},
		IBM1047
		=
		{
			i++,
			言語.ラテン文字,
			コード.IBM,
			"IBM1047",
			"Cp1047"
		},
		IBM273
		=
		{
			i++,
			言語.EU加盟国のほとんど,
			コード.IBM,
			"IBM273",
			"Cp273"
		},
		IBM277
		=
		{
			i++,
			言語.EU加盟国のほとんど,
			コード.IBM,
			"IBM277",
			"Cp273"
		},
		IBM278
		=
		{
			i++,
			言語.EU加盟国のほとんど,
			コード.IBM,
			"IBM278",
			"Cp278"
		},
		IBM280
		=
		{
			i++,
			言語.EU加盟国のほとんど,
			コード.IBM,
			"IBM280",
			"Cp280"
		},
		IBM284
		=
		{
			i++,
			言語.EU加盟国のほとんど,
			コード.IBM,
			"IBM284",
			"Cp284"
		},
		IBM285
		=
		{
			i++,
			言語.EU加盟国のほとんど,
			コード.IBM,
			"IBM285",
			"Cp285"
		},
		IBM297
		=
		{
			i++,
			言語.EU加盟国のほとんど,
			コード.IBM,
			"IBM297",
			"Cp297"
		},
		IBM420
		=
		{
			i++,
			言語.アラビア語,
			コード.IBM,
			"IBM420",
			"Cp420"
		},
		IBM424
		=
		{
			i++,
			言語.ヘブライ語,
			コード.IBM,
			"IBM424",
			"Cp424"
		},
		IBM437
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.IBM,
			"IBM437",
			"Cp437"
		},
		IBM500
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.IBM,
			"IBM500",
			"Cp500"
		},
		IBM775
		=
		{
			i++,
			言語.バルト諸語,
			コード.IBM,
			"IBM775",
			"Cp775"
		},
		IBM850
		=
		{
			i++,
			言語.ラテン文字,
			コード.IBM,
			"IBM850",
			"Cp850"
		},
		IBM852
		=
		{
			i++,
			言語.ラテン文字,
			コード.IBM,
			"IBM852",
			"Cp852"
		},
		IBM855
		=
		{
			i++,
			言語.キリル文字,
			コード.IBM,
			"IBM855",
			"Cp855",
		},
		IBM857
		=
		{
			i++,
			言語.トルコ語,
			コード.IBM,
			"IBM857",
			"Cp857"
		},
		IBM860
		=
		{
			i++,
			言語.EU加盟国のほとんど,
			コード.IBM,
			"IBM860",
			"Cp860"
		},
		IBM861
		=
		{
			i++,
			言語.EU加盟国のほとんど,
			コード.IBM,
			"IBM861",
			"Cp861"
		},
		IBM862
		=
		{
			i++,
			言語.ヘブライ語,
			コード.IBM,
			"IBM862",
			"Cp862"
		},
		IBM863
		=
		{
			i++,
			言語.EU加盟国のほとんど,
			コード.IBM,
			"IBM863",
			"Cp863"
		},
		IBM864
		=
		{
			i++,
			言語.アラビア語,
			コード.IBM,
			"IBM864",
			"Cp864"
		},
		IBM865
		=
		{
			i++,
			言語.北欧,
			コード.IBM,
			"IBM865",
			"Cp865"
		},
		IBM866
		=
		{
			i++,
			言語.ロシア語,
			コード.IBM,
			"IBM866",
			"Cp866"
		},
		IBM868
		=
		{
			i++,
			言語.イスラム圏,
			コード.IBM,
			"IBM868",
			"Cp868"
		},
		IBM869
		=
		{
			i++,
			言語.ギリシャ文字,
			コード.IBM,
			"IBM869",
			"Cp869"
		},
		IBM870
		=
		{
			i++,
			言語.ラテン文字,
			コード.IBM,
			"IBM870",
			"Cp870"
		},
		IBM871
		=
		{
			i++,
			言語.北欧,
			コード.IBM,
			"IBM871",
			"Cp871"
		},
		IBM918
		=
		{
			i++,
			言語.イスラム圏,
			コード.IBM,
			"IBM918",
			"Cp918"
		},
		ISO_2022_CN//参考:http://kanji.zinbun.kyoto-u.ac.jp/~yasuoka/kanjibukuro/china.html
		=
		{
			i++,
			言語.中国語_簡体字,
			言語.中国語_繁体字,
			コード.ISO,
			補足.Unicodeへの変換のみ,
			"ISO-2022-CN",
			"ISO2022CN"
		},
		ISO_2022_JP
		=
		{
			i++,
			言語.日本語,
			コード.ISO,
			"ISO-2022-JP",
			"ISO2022JP"
		},
		ISO_2022_KR
		=
		{
			i++,
			言語.韓国語,
			コード.ISO,
			"ISO-2022-KR",
			"ISO2022KR"
		},
		ISO_8859_3
		=
		{
			i++,
			言語.ラテン文字,
			コード.ISO,
			"ISO-8859-3",
			"ISO8859_3"
		},
		ISO_8859_6
		=
		{
			i++,
			言語.ラテン文字,
			言語.アラビア語,
			コード.ISO,
			"ISO-8859-6",
			"ISO8859_6"
		},
		ISO_8859_8
		=
		{
			i++,
			言語.ラテン文字,
			言語.ヘブライ語,
			コード.ISO,
			"ISO-8859-8",
			"ISO8859_8"
		},
		Shift_JIS
		=
		{
			i++,
			言語.日本語,
			コード.未分類,
			"Shift_JIS",
			"SJIS"
		},
		TIS_620
		=
		{
			i++,
			言語.タイ語,
			コード.TIS,
			"TIS-620",
			"TIS620"
		},
		windows_1255
		=
		{
			i++,
			言語.ヘブライ語,
			コード.windows,
			"windows-1255",
			"Cp1255"
		},
		windows_1256
		=
		{
			i++,
			言語.アラビア語,
			コード.windows,
			"windows-1256",
			"Cp1256"
		},
		windows_1258
		=
		{
			i++,
			言語.ベトナム語,
			コード.windows,
			"windows-1258",
			"Cp1258"
		},
		windows_31j
		=
		{
			i++,
			言語.日本語,
			コード.windows,
			"windows-31j",
			"MS932"
		},
		x_Big5_Solaris
		=
		{
			i++,
			言語.中国語_繁体字,
			コード.Big5,
			"x-Big5_Solaris",
			"Big5_Solaris"
		},
		x_euc_jp_linux
		=
		{
			i++,
			言語.日本語,
			コード.EUC,
			"x-euc-jp-linux",
			"EUC_JP_LINUX"
		},
		x_EUC_TW
		=
		{
			i++,
			言語.中国語_繁体字,
			コード.EUC,
			"x-EUC-TW",
			"EUC_TW"
		},
		x_eucJP_Open
		=
		{
			i++,
			言語.日本語,
			コード.EUC,
			"x-eucJP-Open",
			"EUC_JP_Solaris"
		},
		x_IBM1006
		=
		{
			i++,
			言語.イスラム圏,
			コード.IBM,
			"x-IBM1006",
			"Cp1006"
		},
		x_IBM1025
		=
		{
			i++,
			言語.キリル文字,
			コード.IBM,
			"x-IBM1025",
			"Cp1025"
		},
		x_IBM1046
		=
		{
			i++,
			言語.アラビア語,
			コード.IBM,
			"x-IBM1046",
			"Cp1046"
		},
		x_IBM1097
		=
		{
			i++,
			言語.イスラム圏,
			コード.IBM,
			"x-IBM1097",
			"Cp1097"
		},
		x_IBM1098
		=
		{
			i++,
			言語.イスラム圏,
			コード.IBM,
			"x-IBM1098",
			"Cp1098"
		},
		x_IBM1112
		=
		{
			i++,
			言語.バルト諸語,
			コード.IBM,
			"x-IBM1112",
			"Cp1112"
		},
		x_IBM1122
		=
		{
			i++,
			言語.バルト諸語,
			コード.IBM,
			"x-IBM1122",
			"Cp1122"
		},
		x_IBM1123
		=
		{
			i++,
			言語.東欧,
			コード.IBM,
			"x-IBM1123",
			"Cp1123"
		},
		x_IBM1124
		=
		{
			i++,
			言語.東欧,
			コード.IBM,
			"x-IBM1124",
			"Cp1124"
		},
		x_IBM1381
		=
		{
			i++,
			言語.中国語_簡体字,
			コード.IBM,
			"x-IBM1381",
			"Cp1381"
		},
		x_IBM1383
		=
		{
			i++,
			言語.中国語_簡体字,
			コード.IBM,
			"x-IBM1383",
			"Cp1383"
		},
		x_IBM33722
		=
		{
			i++,
			言語.日本語,
			コード.IBM,
			"x-IBM33722",
			"Cp33722"
		},
		x_IBM737
		=
		{
			i++,
			言語.ギリシャ文字,
			コード.IBM,
			"x-IBM737",
			"Cp737"
		},
		x_IBM856
		=
		{
			i++,
			言語.ヘブライ語,
			コード.IBM,
			"x-IBM856",
			"Cp856"
		},
		x_IBM874
		=
		{
			i++,
			言語.タイ語,
			コード.IBM,
			"x-IBM874",
			"Cp874"
		},
		x_IBM875
		=
		{
			i++,
			言語.ギリシャ文字,
			コード.IBM,
			"x-IBM875",
			"Cp875"
		},
		x_IBM921
		=
		{
			i++,
			言語.バルト諸語,
			コード.IBM,
			"x-IBM921",
			"Cp921"
		},
		x_IBM922
		=
		{
			i++,
			言語.バルト諸語,
			コード.IBM,
			"x-IBM922",
			"Cp922"
		},
		x_IBM930
		=
		{
			i++,
			言語.日本語,
			コード.IBM,
			"x-IBM930",
			"Cp930"
		},
		x_IBM933
		=
		{
			i++,
			言語.韓国語,
			コード.IBM,
			"x-IBM933",
			"Cp933"
		},
		x_IBM935
		=
		{
			i++,
			言語.中国語_簡体字,
			コード.IBM,
			"x-IBM935",
			"Cp935"
		},
		x_IBM937
		=
		{
			i++,
			言語.中国語_繁体字,
			コード.IBM,
			"x-IBM937",
			"Cp937"
		},
		x_IBM939
		=
		{
			i++,
			言語.ラテン文字,
			コード.IBM,
			"x-IBM939",
			"Cp939"
		},
		x_IBM942
		=
		{
			i++,
			言語.日本語,
			コード.IBM,
			"x-IBM942",
			"Cp942"
		},
		x_IBM942C
		=
		{
			i++,
			言語.日本語,
			コード.IBM,
			"x-IBM942C",
			"Cp942C"
		},
		x_IBM943
		=
		{
			i++,
			言語.日本語,
			コード.IBM,
			"x-IBM943",
			"Cp943"
		},
		x_IBM943C
		=
		{
			i++,
			言語.日本語,
			コード.IBM,
			"x-IBM943C",
			"Cp943C"
		},
		x_IBM948
		=
		{
			i++,
			言語.中国語_繁体字,
			コード.IBM,
			"x-IBM948",
			"Cp948"
		},
		x_IBM949
		=
		{
			i++,
			言語.韓国語,
			コード.IBM,
			"x-IBM949",
			"Cp949"
		},
		x_IBM949C
		=
		{
			i++,
			言語.韓国語,
			コード.IBM,
			"x-IBM949C",
			"Cp949C"
		},
		x_IBM950
		=
		{
			i++,
			言語.中国語_繁体字,
			コード.IBM,
			"x-IBM850",
			"Cp850"
		},
		x_IBM964
		=
		{
			i++,
			言語.中国語_繁体字,
			コード.IBM,
			"x-IBM964",
			"Cp964"
		},
		x_IBM970
		=
		{
			i++,
			言語.韓国語,
			コード.IBM,
			"x-IBM970",
			"Cp970"
		},
		x_ISCII91
		=
		{
			i++,
			言語.インド語,
			コード.ISC,
			"x-ISCII91",
			"ISCII91"
		},
		x_ISO2022_CN_CNS
		=
		{
			i++,
			言語.中国語_繁体字,
			コード.ISO,
			補足.Unicodeからの変換のみ,
			"x-ISO2022-CN-CNS",
			"ISO2022_CN_CNS"
		},
		x_ISO2022_CN_GB
		=
		{
			i++,
			言語.中国語_簡体字,
			コード.ISO,
			補足.Unicodeからの変換のみ,
			"x-ISO2022-CN-GB",
			"ISO2022_CN_GB"
		},
		x_iso_8859_11
		=
		{
			i++,
			言語.ラテン文字,
			言語.タイ語,
			コード.ISO,
			"x-iso-8859-11",
			"x-iso-8859-11"
		},
		x_JISAutoDetect
		=
		{
			i++,
			言語.日本語,
			補足.Unicodeへの変換のみ,
			"x-JISAutoDetect",
			"JISAutoDetect"
		},
		x_Johab
		=
		{
			i++,
			言語.韓国語,
			コード.未分類,
			"x-Johab",
			"x-Johab"
		},
		x_MacArabic
		=
		{
			i++,
			言語.アラビア語,
			コード.Mac,
			"x-MacArabic",
			"MacArabic"
		},
		x_MacCentralEurope
		=
		{
			i++,
			言語.ラテン文字,
			コード.Mac,
			"x-MacCentralEurope",
			"MacCentralEurope"
		},
		x_MacCroation
		=
		{
			i++,
			言語.クロアチア語,
			コード.Mac,
			"x-MacCroation",
			"MacCroation"
		},
		x_MacCyrillic
		=
		{
			i++,
			言語.キリル文字,
			コード.Mac,
			"x-MacCyrillic",
			"MacCyrillic"
		},
		x_MacDingbat
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.Mac,
			"x-MacDingbat",
			"MacDingbat"
		},
		x_MacGreek
		=
		{
			i++,
			言語.ギリシャ文字,
			コード.Mac,
			"x-MacGreek",
			"MacGreek"
		},
		x_MacHebrew
		=
		{
			i++,
			言語.ヘブライ語,
			コード.Mac,
			"x-MacHebrew",
			"MacHebrew"
		},
		x_MacIceland
		=
		{
			i++,
			言語.北欧,
			コード.Mac,
			"x-MacIceland",
			"MacIceland"
		},
		x_MacRoman
		=
		{
			i++,
			言語.ギリシャ文字,
			コード.Mac,
			"x-MacRoman",
			"MacRoman"
		},
		x_MacRomania
		=
		{
			i++,
			言語.東欧,
			コード.Mac,
			"x-MacRomania",
			"MacRomania"
		},
		x_MacSymbol
		=
		{
			i++,
			言語.不明あるいは全世界,
			コード.Mac,
			"x-MacSymbol",
			"MacSymbol"
		},
		x_MacThai
		=
		{
			i++,
			言語.タイ語,
			コード.Mac,
			"x-MacThai",
			"MacThai"
		},
		x_MacTurkish
		=
		{
			i++,
			言語.トルコ語,
			コード.Mac,
			"x-MacTurkish",
			"MacTurkish"
		},
		x_MacUkraine
		=
		{
			i++,
			言語.東欧,
			コード.Mac,
			"x-MacUkraine",
			"MacUkraine"
		},
		x_MS950_HKSCS
		=
		{
			i++,
			言語.中国語_繁体字,
			コード.windows,
			"x-MS950_HKSCS",
			"MS950_HKSCS"
		},
		x_mswin_936
		=
		{
			i++,
			言語.中国語_簡体字,
			コード.windows,
			"x-mswin-936",
			"MS936"
		},
		x_PCK
		=
		{
			i++,
			言語.日本語,
			コード.未分類,
			"x-PCK",
			"PCK"
		},
		x_windows_874
		=
		{
			i++,
			言語.タイ語,
			コード.windows,
			"x-windows-874",
			"MS874"
		},
		x_windows_949
		=
		{
			i++,
			言語.韓国語,
			コード.windows,
			"x-windows-949",
			"MS949"
		},
		x_windows_950
		=
		{
			i++,
			言語.中国語_繁体字,
			コード.windows,
			"x-windows-950",
			"MS950"
		};

	private static final short 総数 = (short) (i+1);
	//1足すのはunsupportedの-1の分
	//byteでは足りなかった(驚)

	private Object[] instanceCharSet = null;

	//*切り替え型コメント
	public Charsets(Object[] charset) throws Exception
	{
		class Charsetsクラスのコンストラクタに文字コードを意味しない定数が渡されました extends Exception{}
		try
		{
			short charsetNum = (short)(int)(Integer)charset[0];
			boolean
			isIllegalNum = (charsetNum < 0 || 総数-1 <= charsetNum),
			isIllegal = isIllegalNum || !(java.util.Arrays.equals(charset, charsets[charsetNum]));

			if(isIllegal)
				throw new Exception();
			this.instanceCharSet = charset;
		}
		catch(Exception e)
		{
			throw new Charsetsクラスのコンストラクタに文字コードを意味しない定数が渡されました();
		}

	}
	/*/
	//デフォルトコンストラクタをprivateにすることで、インスタンスの生成を抑制。
	private Charsets(){}
	//*/

	/**
	 * 第1引数に任意の文字コードでの文字列に対応するバイト列、第二引数にその文字列を「このシステムのデフォルトエンコーディング」で表現した文字列を入力すると、<br>第1引数の文字コードを
	 * java.nio API用の正準名やjava.ioおよびjava.lang API用の正準名で返却する。
	 * @param byteArrayByAnyCharset
	 * @param sameStringBySystemCharset
	 * @return 0番目:java.nio API用正準名<br>1番目:java.ioやjava.lang API用正準名
	 * @throws Exception
	 */
	public static String[] detect(byte[] byteArrayByAnyCharset, String sameStringBySystemCharset) throws Exception
	{

		int idx = 0;

		class TryMatching
		{
			int idx;
			Object[] charset;
			String[] strs;
			byte[] bytesByAnyCharset;
			byte[] bytesBySystemCharset;
			int j = 0;

			TryMatching(int idx) throws Exception
			{
				this.charset = (Object[]) charsets[idx];
				this.idx = idx;
				String zero, one;
				while(!(charset[j] instanceof String))
					j++;
				zero = (String) charset[j];
				if(zero.equals("利用不可"))
					throw new Exception("この文字コードは利用不可です");


				j++;

				while(!(charset[j] instanceof String))
					j++;
				one = (String) charset[j];

				String[] strs = {zero, one};
				this.strs = strs;

				this.bytesByAnyCharset = byteArrayByAnyCharset;
				this.bytesBySystemCharset = sameStringBySystemCharset.getBytes();
				j = 0;
			}


			boolean isSucceeded() throws Exception
			{

				return
				//isSupported(stringByAnyCharset, charset)
				//&&
				//isSupported(sameStringByUTF8, charset)
				//&&
				//java.util.Arrays.equals(new String(bytesByAnyCharset,strs[0]).getBytes("UTF-8"), bytesByUTF8);
				//new Stringをすると置き換え先のルールで勝手に変換されたりして壊れる。「new String(文字列, 本来通りではない文字コ―ド)」避けるべき。
				//但し、bytesByUTF8は、もともと文字コードがわかっているのだから、new Stringしてもよい(但しutf8以外を指定するな)
				new String(bytesByAnyCharset, strs[0]).equals(new String(bytesBySystemCharset));

			}
			String[] getStrs()
			{
				return strs;

			}
		}


		TryMatching t;

		for(idx=0; idx < 総数-1; idx++)
		{
			try
			{
				if((t=new TryMatching(idx)).isSucceeded())
					return t.getStrs();
			}
			catch(Exception e)
			{
				continue;
			}
		}

		throw new Exception("適切な文字コードが見つかりませんでした");

	}

	@Override
	public String toString()
	{
		try
		{
			return toString(this.instanceCharSet)[0];
		}catch (Exception e){
			e.printStackTrace();
			return "例外";
		}

	}

	/**
	 * プラットフォームのデフォルトエンコーディングを返却する。
	 * @return デフォルトエンコーディングの文字コードを表すObject[]型定数
	 * @throws Exception
	 */
	public static Object[] getSystemCharset() throws Exception
	{
		return getCharsetByName(systemCharset);
	}

	/**
	 * 文字列で文字コード名を指定するとそれに対応する文字コード定数を返却する。<br>
	 * <dl>
	 * <dt>
	 * アルゴリズム:
	 * </dt>
	 * <dd>
	 * {@link java.nio.charset.Charset#forName(String charsetName)}へ引数を代入し、その結果を文字コード定数へ変換する。<br>
	 * 例外を受け取った場合、そのまま投げる。
	 * </dd>
	 * </dl>
	 * @param charsetName 文字コード名
	 * @return 文字コード定数
	 * @throws Exception forNameメソッドは非検査例外で、new Charsets(Object[])は今回例外が起こり得ないので実際には発生しない。
	 * @throws Error forNameメソッドが例外を投げず、かつ文字コードが見つからなかった場合
	 */
	public static Object[] getCharsetByName(String charsetName) throws Exception
	{

		for(Object[] charset : charsets)
			if(new Charsets(charset).toString().equals(java.nio.charset.Charset.forName(charsetName).name()))
				return charset;

		throw new Error("発生しないはずのエラー。プログラムに問題あり。");

	}

	/**
	 * クラス定数としての文字コードを選択して引数として入力すると<br>
	 * java.nio API用の正準名やjava.ioおよびjava.lang API用の正準名を返却する。<br>
	 * IDEなどの環境でクラス定数選択画面から利用するのが便利。<br>
	 * 但し、ややこしくなるので隠した。外部からは{@link Charsets#toString()}から利用可能(0番目のみ)
	 * @param charset CharsetsのObject[]型クラス定数
	 * @return java.nio API用正準名<br>1番目:java.ioやjava.lang API用正準名
	 * @throws Exception
	 */
	protected static String[] toString(Object[] charset) throws Exception
	{
		short num = (short)((int)charset[0]);
		if(num < 0 || 総数-1 < num)
			throw new Exception("不正な参照です");

		charset = charsets[num];

		int j = 0;
		String zero, one;

		while(!(charset[j] instanceof String))
			j++;
		zero = (String) charset[j];
		j++;
		while(!(charset[j] instanceof String))
			j++;
		one = (String) charset[j];

		//*/
		String[] strs = {zero, one};
		return strs;
		/*/
		return zero;
		//*/

	}


	/**
	 * 文字列を指定された文字コードでurlエンコードする。結果は正規表現<code>(%([0-9A-F][0-9A-F]))*</code>にマッチする(はず)
	 * @param sentenceBySystemCharset urlエンコードしたい文字列
	 * @param charsetTo urlエンコードする際の文字コード
	 * @return urlエンコードされた文字列<code>(%([0-9A-F][0-9A-F]))*</code>
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static String urlEncode(String sentenceBySystemCharset, Object[] charsetTo) throws UnsupportedEncodingException, Exception
	{
		class CharsetsクラスメソッドurlEncodeを実行中にバイト列の長さに異常が発生しました extends Exception{}

		byte[] bytes = sentenceBySystemCharset.getBytes(new Charsets(charsetTo).toString());
		String rtn = "";
		String hex_str;
		for(short byt : bytes)
		{
			rtn += "%";
			byt += (byt<0 ? 256 : 0);
			hex_str = Integer.toHexString(byt).toUpperCase(java.util.Locale.ENGLISH);
			if(hex_str.length() == 1)
				rtn += ("0"+hex_str);
			else
			if(hex_str.length() == 2)
				rtn += hex_str;
			else
				throw new CharsetsクラスメソッドurlEncodeを実行中にバイト列の長さに異常が発生しました();
		}
		return rtn;

	}

	/**
	 * 文字列を指定された文字コードに変換し、バイト列として返却するメソッド<br>
	 * @param sentenceBySystemCharset 変換したい文字列
	 * @param charsetTo 変換後の文字コード
	 * @return 変換されたバイト列
	 * @throws Exception
	 */
	public static byte[] transrate(String sentenceBySystemCharset, Object[] charsetTo) throws Exception
	{
		byte[] rtn = sentenceBySystemCharset.getBytes(new Charsets(charsetTo).toString());
		detect(rtn, sentenceBySystemCharset);
		return rtn;
	}


/*
	public static boolean isSupported(String str, Object[] charset) throws Exception
	{
		String charset_str = toString(charset)[0];
		return new String(str.getBytes(charset_str), charset_str).equals(str);
	}
*/


	static
	{
		charsets = new Object[総数][];

		//大量に定義した文字コードのObject配列をcharsetsへ取得するため、自分自身をリフレクト
		try
		{
			Class<Charsets> clazz = Charsets.class;
			short count = (short)(clazz.getDeclaredField("総数").get(null));
			Field[] fields = clazz.getDeclaredFields();//この時点で順番は約束されていない。

			for(Field f : fields)
			{
				try //インスタンス生成を可にしてしまったため、get(null)で例外が発生するのでcontinueさせる
				{
					if(f.getType().equals(Object[].class) && !f.getName().equals("charsets"))
					//Object[]型のものだけ取り出す。さらにcharsets除外
					{
						int idx
						=
						(int)
						//③それをint型にキャストする
						(
							(
								(Object[])f.get(null)
								//①文字コードの情報を入れた各フィールドはObjectとしてgetされるので、
								//Object[]にキャストする
							)
							[0]
							//②Object[]フィールドの0番目に通し番号が入っている。
						);
						if((int)idx != -1)
						//unsupported以外は
						{
							charsets[idx] = (Object[]) f.get(null);
							//通し番号を基準にcharsetsへ書き込み。
							//(finalされた配列では、要素の書き換えは可能。)
						}
						else
						//unsupportedは
						{
							charsets[総数-1] = (Object[]) f.get(null);
							//一番最後へ書き込み
						}
						//System.out.println(f.getName()+"="+((Object[])f.get(null))[0]);

					}

				}
				catch(NullPointerException e)
				{
					continue;
				}

			}
			//System.out.println(count);


		}
		catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e)
		{
			e.printStackTrace();
		}


	}


/*
	public static String[] detect(String testStr, String testStrAsUtf8) throws Exception
	{

		for(byte i=0; !testStr.matches(testStrAsUtf8); i++)
		{
			if(i >= 総数)
			{
				throw new Exception("「"+testStr+"」は、どの文字コードとみなしても、UTF-8に変換したとき「"+testStrAsUtf8+"」に一致しませんでした。");
			}

			Object
			xBytes = testStr.getBytes(),
			utf8Bytes = testStrAs;

		}
	}
*/



	/*
	/**
	 * 参考:
	 * https://qiita.com/kazuhsat5/items/1c3ebf5a186e0d041ba5<br>
	 * http://sjc-p.obx21.com/word/es/staticinitializer.html
	 *
	 * /
	static
	{
		ISO_8859_1 = {0,};

	}
	*/



}



