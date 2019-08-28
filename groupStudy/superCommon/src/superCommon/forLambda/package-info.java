
/**
 * lambda記法を柔軟に実現するために、プリミティブの様々な型やObject型の抽象メソッドを一つずつもつインターフェースをこのパッケージの直下においておく。<br>
 * 例:匿名クラスを用いてスロアブルなメソッドの戻り値をフィールドに格納するもの
 * <xmp> int field
 * =
 * new Object()
 * {
 *     public int getInt()
 *     {
 *         try
 *         {
 *             return someIntFuncThatMayThrowException();
 *         }
 *         catch(Exception e)
 *         {
 *             e.printStackTrace;
 *             return null;
 *         }
 *     }
 * }.getInt();
 * </xmp><br>
 * はlambda記法では次のように書ける。(前提: <code>import superCommon.forLambda.L;</code>)<br>
 * <xmp> private L l
 * =
 * () ->
 * {
 *     try
 *     {
 *         return someIntFuncThatMayThrowException();
 *     }
 *     catch(Exception e)
 *     {
 *         e.printStackTrace;
 *         return null;
 *     }
 * };
 * int field = l.$();
 * </xmp><br>
 * インターフェース型のフィールドが余分に必要になるのが嫌な場合、次のようにイニシャライザを使用する。<br>
 * <xmp> int field;
 * {
 *     L l
 *     = () ->
 *     {
 *         try
 *         {
 *             return someIntFuncThatMayThrowException();
 *         }
 *         catch(Exception e)
 *         {
 *             e.printStackTrace;
 *             return null;
 *         }
 *     }
 *     field = l.$();
 * }</xmp><br>
 * また、名前が「_」で終わるインタフェースは引数を受け付ける。引数についてはObject型の可変長引数としているため、すべての引数はObject型として認識される。(どのクラス配列型を渡した場合も)<br>
 * さらに、引数を渡すときは<code>-></code>の左に直接渡すと認識されないので、次のようにすること。<br>
 * <xmp>
 * String[] a = {"hello", "world"}, b = {"good", "bye"};
 *
 * O_ o = a -> (String)a[0]+" "+(String)a[1];
 * String str = (String)o.$(a) +"\n"+ (String)o.$(b);
 * </xmp><br>
 * 上のコードではラムダ式の定義の際のaはもっぱら配列のインデックスを表現することのみに利用されている(つまり中身に意味はない)。
 * 実行するとstrの中身は<br>
 * <xmp>
 * herro world
 * good bye
 * </xmp><br>
 * となる。<code>a</code>の使い方が気に食わないのであれば、多少冗長になるが次のようにする。<br>
 * <xmp>
 * String[] args = new String[2],
 * a = {"hello", "world"}, b = {"good", "bye"};
 *
 * O_ o = args -> (String)args[0]+" "+(String)a[1];//this is $ method
 * String str = (String)o.$(a) +"\n"+ (String)o.$(b);
 * </xmp>
 * (2019年8月28日追記)あるいは次のようにもできることに気がついた。(コンパイル済み、未実行)<br>
 * <xmp>
 * String[] a = {"hello", "world"}, b = {"good", "bye"};
 *
 * O_ o = (Object[] args) -> (String)args[0]+" "+(String)a[1];//this is $ method
 * String str = (String)o.$(a) +"\n"+ (String)o.$(b);
 * </xmp>
 *
 *
 * @author <a href=http://github.com/17ec084>Tomotaka Hirata(17ec084)</a>
 * @see superCommon.forLambda.B B - boolean型を返却させる場合
 * @see superCommon.forLambda.C C - char型を返却させる場合
 * @see superCommon.forLambda.L L - 整数型を返却させる場合(long以外は要キャスト)
 * @see superCommon.forLambda.D D - 浮動小数点型を返却させる場合(floatは要キャスト)
 * @see superCommon.forLambda.O O - 参照型を返却させる場合(Object以外は要キャスト)
 * @see superCommon.forLambda.V V - 返却値を持たせない場合
 * @see superCommon.forLambda.B_ B_ - boolean型を返却させる場合(引数あり)
 * @see superCommon.forLambda.C_ C_ - char型を返却させる場合(引数あり)
 * @see superCommon.forLambda.L_ L_ - 整数型を返却させる場合(long以外は要キャスト)(引数あり)
 * @see superCommon.forLambda.D_ D_ - 浮動小数点型を返却させる場合(floatは要キャスト)(引数あり)
 * @see superCommon.forLambda.O_ O_ - 参照型を返却させる場合(Object以外は要キャスト)(引数あり)
 * @see superCommon.forLambda.V_ V_ - 返却値を持たせない場合(引数あり)
 *
 */
package superCommon.forLambda;


