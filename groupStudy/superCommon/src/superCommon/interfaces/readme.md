# javaでGUIを作ったときのメモ

## 実際に作っているもの
[抽象GUI](https://github.com/17ec084/grade3-1/blob/master/groupStudy/superCommon/src/superCommon/interfaces/AbstractGUI.java]) を作っています。
[Configインタフェース](https://github.com/17ec084/grade3-1/blob/master/groupStudy/superCommon/src/superCommon/interfaces/Config.java])を実装するクラスは、この抽象GUIを継承して具体化するとよい(別に強制ではない)。

## 最初にお断り
本稿では「クラスとはプログラムのことである」と説明する。<br>
そしてインスタンスは「プログラムの実行命令」と考える。<br>
これは筆者なりの「オブジェクト指向をできるだけ簡単に考える方法の前提」である。<br>
例えば最もシンプルなjavaプログラムは<br>
<xmp>
public class Main
{
	public static void main(String[] args)
	{
		System.out.println("hello, world");
	}
}
</xmp>


## GUIの仕組み
CUIとGUIはいろいろと明らかに異なるが、その違いを説明できなくてはGUIのプログラムもかけるわけがない。<br>
そして、CUIとGUIの違いを具体的に洗い出すには、「CUIを不思議に思わない人」が「GUIを不思議に思う点」を挙げればよい。
したがって、このメモでは次のような方針でGUIを説明していく。<br><br>
「CUIが動くことを不思議に思わない人」が「GUIが動くことを不思議に思わなくなる」ように説明をしていく。<br>

### CUIを不思議に思わない人がGUIを不思議に思う点
<ol>
<li>明らかにいろいろ設定しなきゃいけないけど、ごちゃごちゃにならないの？</li>
<li>画面やらボタンやらメニューやらってどうやって表示しているの？</li>
<li>ボタンを表示したとしても、押したときの動作ってどうやって決めてるの？</li>
<li>入出力ってどうやってやってるの？</li>
<li>メタい説明ってどうやるの？全部書かなきゃいけないの？(ウィンドウの大きさの設定、大きさが可変かなど。)</li>
<li>処理の途中で閉じるボタンが押されたらどうするの？</li>
</ol>

#### 疑問1 明らかにいろいろ設定しなきゃいけないけど、ごちゃごちゃにならないの？

GUIの仕組みを理解しないと、つまり「どういう理屈でこのコードがここで出てくるか」がわからなければ、せっかくオブジェクト指向プログラミングをしているのにDRY原則を守れなくなる(≒冗長になる)などのことが起こりうる。<br>
コードに出てくる各変数やその型、型の役務や継承関係を知らなければ、例え既存のGUIプログラムを見よう見まねで改造したりつなぎ合わせたりしても、確かにごちゃごちゃで意味不明なプログラムしか書けない。<br>
逆に言えば「明らかにいろいろ」が具体的に何なのか、体系立てて知れば、コードの長さは別として、少なくともごちゃごちゃにはならない努力をすることが可能になる。

#### 疑問3 ボタンを押したときの動作ってどうやって決めてるの？

<!--
疑問2よりも先に疑問3を解決しよう。<br>
人間の感覚ではやりたいことがあり、GUIがあり、ボタンがあり、ボタンの動作があり、プログラムがある。<br>つまり感覚的には、次の列を左から右の順番で考えてしまう。<br>
「やりたいこと, GUI, ボタン, ボタンの動作, プログラム」<br>
しかし、プログラムを書く場合、プログラムのほうから見てやる必要があるので、同じ列を右から左の順で考えなければいけない。<br>
だからボタンの表示を考える疑問2よりも先に、ボタンの動作を考える疑問3を先に考えたいのである。<br>
<br>
さて、-->GUIにはイベントとかリスナとかいうのがある。<br>
結論から言うと、ボタンが押されるとイベントが発生し、リスナがそれを感じ取って処理を行う。<br>
「イベントとリスナ」の関係を一言でいうのなら、「タッチとタッチセンサ」、「光と光センサ」、「人と人感センサ」のように「検知されるものとセンサ」の関係である。
##### イベントとは
イベントとは、「何かされたときに発生するもの」であ<!--り、その「何か」の種類はスーパクラスやインタフェースが用意してくれてい-->る。<!--<br>
したがって「ボタンが押されたときに発生するイベント」を作るためには、予め用意されたものを実装なり継承なりするだけでよい。--><br>
##### リスナとは
リスナは[専用のインタフェース](https://docs.oracle.com/javase/jp/8/docs/api/java/awt/event/ActionListener.html)を実装したクラスで actionPerformedメソッドを具体化するだけで作れる。<br>
それだけでそのクラスはイベントを感知できるようになり、イベントを感じ取るとactionPerformedメソッドが呼び出されるという仕様になっている。

## 実際に書いてみる

### 最も簡単なGUI
極論を言えば、「なんでもいいからとりあえずGUIを作りたい」という要望には次の1行コードで答えることができる。<br>
<code>
class T{{new java.awt.Frame().setVisible(true);}}
</code><br>
このコードをコピーし、T.javaと名付けて保存、コンパイルし、外部から<code>new T();</code>するだけで、図1のような小さくて何もないウィンドウが開く。<br>
(eclipseなどパッケージ宣言を省略できない場合はパッケージ宣言も書く必要がある)<br>
![](easiest.png)<br>図1 最も簡単なGUI<br>
<br>
<!-- 蛇足:匿名クラスを利用して、何らかの処理の間に次のように追加すると、処理の途中でGUIが起動する。<br>
<code>new java.awt.Frame(){}.setVisible(true);</code> -->


実際には<code>java.awt.Frame</code>クラスを継承し、いろいろ加えることでGUIに機能や表示を追加することができる。<br>

### 今回の主なクラス

但し今回はSwingでGUIを作ることとする。Swingを利用する場合、<code>java.awt.Frame</code>クラスではなくその子クラスである<code>javax.swing.JFrame</code>クラスを継承する必要がある。<br>
したがって、今回主として設計するクラスは次の通りである。<br>
<xmp>
import javax.swing.JFrame;
class ConfigGUIframe extends JFrame{}
</xmp>


### ウィンドウサイズとウィンドウタイトルの設定

ウィンドウサイズは簡単に設定できる。次のようにすればよい。<br>
<xmp>
import javax.swing.JFrame;
class ConfigGUIframe extends JFrame
{{
	setSize(1000, 50);
	setTitle("15両編成の電車はこれよりもさらに細長い");
}}

</xmp>
この例で<code>new ConfigGUIframe().setVisible(true)</code>すると、図2のように幅1000px、高さ50pxの超横長なウィンドウを生成し、タイトルとして「15両編成の電車はこれよりもさらに細長い」と表示される。<br>
![](size_title.png) <br>
図2 サイズとタイトルを設定したGUI<br>
(ブロックの波括弧が2重になっているのはConfigGUIframeクラスのブロックの内側にそのインスタンスイニシャライザのブロックがあるからである。)<br>
<br>
この設定を行った2つのメソッド<code>setSize(int, int)</code>と<code>setTitle(String)</code>はいずれもJFrameが継承するクラスから継承されたものである。<br>
これらについてのドキュメントを読むと、他にもどんなことができるかが自ずとわかる。
<br>
<br>
さらに環境にあわせて大きさをするなら<code>java.awt.Toolkit.getDefaultToolkit().getScreenSize()</code>を使う。<br>
例えば次のようにすると、(モニタの大きさがどうであれ、モニタに対して)高さは最大で、幅は最大の半分の大きさのウィンドウが生成される。
<xmp>
import javax.swing.JFrame;
class ConfigGUIframe extends JFrame
{{
	java.awt.Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

	setSize((int)size.getWidth()/2, (int)size.getHeight());
	setTitle("幅半分を占拠します");
}}
</xmp>
<br>これを呼び出した結果は図3のようになる。<br>
![](half.png) <br>
図3 高さ最大、幅半分のウィンドウ<br>

### ボタン生成

このコードにボタンを追加してみよう。<br>
機能は「押されると自己増殖する」とでもしよう。<br>

#### ボタンの表示

次のコードで出来る。<br>
<xmp>
import javax.swing.*;
class ConfigGUIframe extends JFrame
{{
	java.awt.Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

	setSize((int)size.getWidth()/3, (int)size.getHeight()/3);//大きさ変えた
	setTitle("ボタンを追加しました");

	//画像付きボタン「zombieBootButton」を作る
	JButton zombieBootButton = new JButton(new ImageIcon("icon.png"));

	//ボタンを表示するためのパネルを作る
	JPanel panel = new JPanel();

	//パネルにボタンを追加する(＝パネルにボタンを乗せる)
	panel.add(zombieBootButton);

	//フレームの「コンテントペイン(コンテンツ区画)」にパネルを乗せる
	this.getContentPane().add(panel, java.awt.BorderLayout.CENTER);
}}
</xmp>
<br>
コードを実行した結果は図4のようになる。<br>
![](button.png)
図4 ボタンを追加したウィンドウ<br>
<br>
フレームに直接パネルを乗せる(つまり、「getContentPane().」を取り除く)実験もしてみたが、この段階では全く同じように表示された。<br>
このことについて、[公式ドキュメント](https://docs.oracle.com/javase/jp/6/api/javax/swing/JFrame.html) では次のように書かれている。(引用する)<br>
<quote>
JFrame は JRootPane を唯一の子として保持します。(中略) 便宜上、add とその派生メソッド、remove および setLayout は、必要に応じて contentPane に転送されるようにオーバーライドされています。つまり、次のように記述できます。

       frame.add(child);
</quote>
つまり、「getContentPane().」が省略されても「getContentPane().」があった場合と同じ処理を内部でしているのである。<br>

#### ボタンの機能

ボタン「zombieBootButton」に機能を追加するためには、リスナを作ってzombieBootButtonに追加すれ(＝ボタンにセンサをしかけれ)ばよい。<br>
「どんな種類のイベントが発生したか調べて、イベントがボタン押下を意味するのか条件分岐する必要があるのでは？」という疑問もあるだろうが、<br>
その疑問については [こちらのサイト](https://www.javadrive.jp/tutorial/event/index2.html) で解決できる。<br>
同サイトの説明によれば、ボタンのイベントはボタン押下の時に起こるのだから、ボタンからイベントを受け取ったのなら、ボタン押下があったのだと考えてよい。<br>
リスナはjava.awt.event.ActionListenerインタフェースを実装し、actionPerformedメソッドに処理内容を書けばよいから、<br>
次のようなコードで実現できる。<br>
<xmp>
import javax.swing.*;
import java.awt.event.*;
class ConfigGUIframe extends JFrame
{{
	java.awt.Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

	setSize((int)size.getWidth()/3, (int)size.getHeight()/3);//大きさ変えた
	setTitle("ボタンを追加しました");

	//画像付きボタン「zombieBootButton」を作る
	JButton zombieBootButton = new JButton(new ImageIcon("icon.png"));

	//ボタンを表示するためのパネルを作る
	JPanel panel = new JPanel();

	//パネルにボタンを追加する(＝パネルにボタンを乗せる)
	panel.add(zombieBootButton);

	//フレームの「コンテントペイン(コンテンツ区画)」にパネルを乗せる
	this.getContentPane().add(panel, java.awt.BorderLayout.CENTER);

	//ボタンにアクションリスナを追加する(＝ボタンにセンサを仕掛ける)
	zombieBootButton.addActionListener(new ZombieBooter());
}}

class ZombieBooter implements ActionListener
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		new ConfigGUIframe().setVisible(true);
	}
}
</xmp>
筆者の環境でも試したが、確かにボタンを押せば押すほど、同じウィンドウが増えることとなった。<br>

##### 蛇足1 匿名クラス
上の例で、ZombieBooterのインスタンス<small><small>(＝クラスというプログラムの実行命令)</small></small>はたった一か所(addActionListenerの引数)でしか使われないのに、いちいちクラス<small><small>(=プログラム)</small></small>を別の場所に書くのは煩わしい。<br>
そこでjavaは、インスタンスとクラスを一緒に書ける<small><small>(クラスというプログラムをその場で書いてその場で実行する)</small></small>ような仕組みを提供している。

##### ラムダ式

さて、[公式ドキュメント](https://docs.oracle.com/javase/jp/8/docs/api/java/awt/event/ActionListener.html)でActionListenerインタフェースの説明を見てみると、<br>
「抽象メソッドが1つしかない」ということがわかる。そしてその「唯一の抽象メソッド」(のシグネチャ)が以下のどれでもない場合※、それは「関数型インタフェース」と呼ばれる特別なインタフェースである。<br>
<ul>
<li>
hashCode()
</li>
<li>
equals(Object)
</li>
<li>
toString()
</li>
</ul>
<small><small>※正確にいうと、あらゆるクラスの先祖クラスと言える「java.lang.Object」の持つpublicメソッドをオーバライドしたものは、抽象メソッドとは数えないということである。<br>
これは、Objectのメソッドを継承したものを「自動で」具体化してしまうから、本質的に抽象メソッドとなり得ないということだろう。<br>
そこで筆者が独自にObjectクラスの持つpublicメソッドの内、finalでないものを取り上げた。<br>
(finalメソッドは子孫クラスでオーバライドすることができない。したがってどのクラスでもObjectのfinalメソッドと同じシグネチャ(例としてgetClass()などがある)のメソッドを作ることができない。<br>試しに適当なクラスでgetClass()メソッドの定義を試みるとよい。)<br></small></small>

関数型インタフェースではその条件から明らかなとおり、「必ず実装すべきメソッドが必ず1つだけ存在する」。(0個でもダメ、2個以上ダメ、1個だけだからいい＝one and only)<br>
したがって実装するクラスが「インタフェースのメソッドを実装する」と言えば、どのメソッドなのかが自ずとわかる。<br>
<small><small>(難しく考える必要はない。もしもあなたが浮気していなくて、彼(女｜氏)がいるならば、あなたが「彼(女｜氏)とデートした」と言うだけで、あなたのデート相手を誰でも特定可能である。まさにabstract method is functional interface's one and only＝「抽象メソッドは関数型インタフェースの最愛の人」である)</small></small>
したがって、インタフェースを実装するクラスメソッドを実装するときにはメソッド名を書かなくてもよさそうなものである。これを可能にしたのがラムダ式である。





