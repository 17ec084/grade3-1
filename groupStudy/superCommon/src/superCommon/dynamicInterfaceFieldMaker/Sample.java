package superCommon.dynamicInterfaceFieldMaker;




//切り替え型コメントの参考:https://qiita.com/Bong/items/436a09e65717c7ded744

//*/ 切り替え型コメント。従来の失敗例をみたい場合は「/x/」に、Dynamic化による成功例をみたい場合は「//x/」に。(「x」は「*」で置き換える)
interface TestIF
{
	public static final Dynamic<Integer> i = new Dynamic<>(0);
	//new Dynamic<>()の第2(変数)引数にtrueを指定するとfinalizeできる(後からどこからでもfinalizeは解除できるので要注意)


}
class TestCLS implements TestIF
{{
	System.out.println(i);
	i.set(1);

	System.out.println(i);

	System.out.println(i.get());

}}

/*/
interface TestIF
{
	public static final int i = 0;
	//当たり前だがpublic static finalを付けなくても全く同じこと
}
class TestCLS implements TestIF
{{
	System.out.println(i);
	i = 1;//コンパイル時エラー
	System.out.println(i);
}}




//*/

class Sample
{
	public static void main(String...args)
	{
		new TestCLS();
	}
}