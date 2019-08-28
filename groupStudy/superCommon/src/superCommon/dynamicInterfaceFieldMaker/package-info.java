/**
 * インタフェースのフィールドは勝手にpublic static finalになってしまう。<br>
 * つまりインタフェースのフィールドを書き換えることはできない。<br>
 * それじゃ困る場合、例えば<br><code>int interfaceFieldThatWannaBeOverwrittern = 0</code><br>を1に変更したい場合、<br>
 * このパッケージ内の<code>Dinamic</code>クラスを使って次のようにする。<br>
 * (インタフェース内)<br>
 * <code>Dinamic&lt;Integer> interfaceFieldThatWannaBeOverwritten = new Dinamic&lt;Integer>(0);</code><br>
 * (1に書き換えるとき)<br>
 * <code>interfaceFieldThatWannaBeOverwritten.set(1);</code><br>
 * (値を読み取るとき)<br>
 * <code>interfaceFieldThatWannaBeOverwritten.get();</code><br>
 * そのほか、本当に書き換えを禁止したいときのためにsetFinal(boolean)メソッドも用意。(引数trueで固定、falseで固定解除)<br>
 * コンストラクタに第二引数を渡すと、可視性も設定できるようにした。正規表現<br>
 * <code>"p((ublic)|(r(otected)|(ivate))|(ackage))"</code><br>にマッチする文字列か、Dynamicクラスの定数を利用すること。
 * @author <a href=http://github.com/17ec084>Tomotaka Hirata(17ec084)</a>
 */
package superCommon.dynamicInterfaceFieldMaker;

