# グルスタ 輪講資料(穴あき無し)

TDU 17ec084 平田智剛

## 前提

### 品詞

名詞、動詞:(説明を省く)  
形容詞:名詞を説明する飾り。なくても英文自体は壊れない。  
副詞:名詞以外を説明する飾り。なくても英文自体は壊れない。  
他:その都度説明する。  

### 語

S:名詞  
V:動詞  
C:形容詞か名詞  
O:名詞  
M:形容詞か副詞(か、前置詞＋名詞)  

### 文型

SV、SVC、SVO、SVOO、SVOCのいずれか。  
言ってしまえば、  
英語の正しい文=SV(C|(O(O|C)?))?  

構文解析するとき、Vは絶対に動詞。  
→動詞を知っておけば、動詞より左はS、動詞以降はOやらCやら、と(だいたい)わかる。  
さらに、SとOは絶対名詞だし、Cは形容詞か名詞であるが、名詞は種類が多すぎる。

### 節

S+Vの塊。文⊃節。  
1つの文の中に複数の節があったりする。  
それを接続するのが「従位接続詞」  
「主節」「従位接続詞」「従属節」  
あるいは  
「従位接続詞」「従属節」,「主節」の順番  
ちなみに、「Sの欠けた節」のことを「句」という。厳密にはそうじゃないけど気にしない。  

## What to predict?

何を予測するか

## To learn about building predictive models,

S:無  
V:無 
M:to learn(学ぶために)
M:about building pregdictive models(予測モデルについて)  
  
to+動詞はto不定詞  

不定詞の役割  
動詞のくせに、あたかも別の品詞であるかのように振舞う  
今回は副詞として。  
  
正しい文型にない→従属節  

訳:予測モデルについて学ぶため、

## let's go back to the Titanic data from earlier.

S:命令文なのでyouが省略されている  
V:let  
O:'s(us)  
C:go back 
M:to the Titanic data from earlier(先ほどのところから、タイタニック号のデータへ)

動詞が補語に来ている→原形不定詞  
  
原形不定詞が現れるのは  
1.助動詞の後ろ(例:I can swim.)  
2.「知覚動詞(hear,seeなど)＋目的語」の後ろ(I saw him run away.)  
3.「使役動詞(make,let,have,helpなど)+目的語」の後ろ  
  
今回は名詞としてふるまっている  

訳:先ほどのところから、タイタニック号のデータへ戻りましょう  

## This data is somewhat simpler and does not need a lot of preparation

andは「等位接続詞」  
「A and B」は文法上対等なAとBを結びつける。  

### andより前

S:this data  
V:is  
C:simpler  
M:somewhat(どういうわけか)  
  
andより前の訳:このデータはどういうわけか比較的単純だ  
  
### andの後

S:無  
V:does not need  
O:a lot of preparation  
a lot of→形容詞。preparationを修飾  

正しい文型にない→andの前から借りてくる(this data)  

andの後の訳:このデータはたくさんの準備を必要としない  

andを使わずに書いてみると次のとおり。  

This data is somewhat simpler. This data does not need a lot of preparation.

全体の訳:このデータは、なんか比較的単純で、面倒な準備が不要だ  

今回のA and Bは、  
A=is somewhat simpler  
B=does not need a lot of preparation  
だったわけだ。  

## but we still need to specify which column we want to predict

butも「等位接続詞」  

先ほどの「this data is ... preparation」は文として完結していた。  

従って今回は「文 but 文」と考えるのが妥当。  

S:we
V:need
O※:to specify which column we want to predict  
M:still(未だ、やっぱり)  

さらにO※を構文解析すると、  

S':無  
V':specify  
O':which column we want to predict  

> あるいは、次のような構文解析も可能。  
> S:we  
> V:need to specify  
> O:which column we want to predict
> M:still

いったん訳:「そうはいっても、やっぱりwhich column we want to predictを特定しておく必要がある」  

which column we want to predictについてみていこう。  

whichは関係代名詞または疑問代名詞、または疑問形容詞  

疑問詞なら、文の最後は.じゃなくて?のはず。  
例外:「間接疑問文」  

----

### 間接疑問文の説明(脱線)

間接疑問文  
⇔疑問*詞で始まる節が文全体の目的語  
⇔「非文A 疑問*詞 B」(非文Aは目的語が欠けている。)  
⇔「疑問*詞以下をitに置き換える」という実験ができる  
逆に間接疑問文ではないなら、wh以下をitに置き換えると、意味が通じなくなる。  
I don't know why you say good-bye and I say hello.  
(何故あなたと私はこうもすれ違うのかがわからん)
→I don't know it  
(それ(＝何故～すれ違うのか)がわからん)  
itに置き換えても意味が通じたので、間接疑問文  
This is the place where I born.  
(ここは私の出生地です)  
→This is the place it.  
(は？)  
意味が通じないので、間接疑問文ではない。  

----
  
今回は
We still need to specify it.  
で通じるので間接疑問文。よってwhichは疑問詞
  
疑問*詞の類は、単なる*詞である。  
(蛇足:関係*詞の類は、従位接続詞と*詞の両方の働きをするもので、「単なる*詞」とは言えない。  
ちなみに従位接続詞は主節と従属節を接続するものである。)  

which column we want to predict  

を構文解析すると、  

S:we  
V:want 
O:to predict  

となり、which columnのやり場に困る。  
実は、「疑問*詞+名詞」と来ている場合、この疑問*詞は疑問形容詞つまり形容詞となる。  
whichという形容詞が、確かにcolumnという名詞を修飾している。  
そして、疑問形容詞+名詞は「本来後ろに来るはずのものが前に出てきてしまっている」ということができる。  
従って、元に戻すと  

we want to predict which column  

となる。  
改めて構文解析しよう。  

S:we(私たち)  
V:want(足りない→したい)  
O:to predict which column(どのカラム(列)を予測すること)  

訳すと「私たちはどのカラムを予測したい」となる。  
ややややこしいので、次のように誤魔化しておこう。  
「私たちはwhichカラムを予測したい」  

全体の訳は次のとおり  
「そうはいっても、やっぱり『私たちが(which＝)どのカラムを予測したいか』を特定しておく必要がある」

## Add product details to transactions.

S:命令文なので省略  
V:add  
O:product details(プロダクトの詳細)  
M:to transactions(タスクへ)  

注意:productやtransactionを素直に日本語に訳すと、返って違和感がある。  
そこで、あえてカタカナに直すに留めたり、別の英単語を意味するカタカナを当てはめるなどした。  
productは「製品、結果、掛け算の積」という意味が、transactionには「処理、取引、議事録」という意味がある。  

訳:「プロダクトをタスクに追加する」  

## Drag the Titanic data into the process

S:命令文  
V:drag  
O:the Titanic data  
M:into the process  

訳:「タイタニックのデータを『Process』に追加します」  
processは「過程」などの意  

## Now, hover the mouse and ... and display some meta data about the Titanic dataset. 

nowは副詞。副詞は特に意味もないのに突然現れてきたりする。  
例えば英語の歌では詞が足りないときに「yes」で埋めたりする。  

蛇足:一つの文が長いししかも文中に等位接続詞が複数回出てくるのは、文法的に不親切。  
ライティングの参考にはすべきでない  

### andの前(hover ... operator)

S:命令文  
V:hover  
O:the mouse  
M:over the output port of the Retrieve operator  
  
イディオム:hover the mouse over A→「Aの上にカーソルをのせる」  
訳:「レトリブオペレータ(Retrieve operator、取得演算子)の出力ポートにカーソルをのせてください」  
retrieveは取得などの意味。operatorは演算子が最適。  
![](https://docs.rapidminer.com/latest/studio/operators/img/operators/retrieve.svg)  
https://docs.rapidminer.com/latest/studio/operators/img/operators/retrieve.svg  

### andの間(wait ... pop up)

S:無  
V:wait  
M:for a small window to pop up  

訳:「小さなウィンドウが出てくるのを待ってください」  
Sが欠けているので、hover ... operator から借りてくる。  
つまり、hover ... operator と wait ... pop upの間のandは、命令 and 命令 だったわけだ。  

ここまでを「andを使わず」書いてみると、次の通り。  

Now, hover the mouse over the output port of the Retrieve operator.  
Now, wait for a small window to pop up.

### andの後(display some meta data about the Titanic dataset)

S:無
V:display  
M:some meta ... dataset  

訳:「タイタニックのメタデータを見せる」  

Sが欠けているから hover ... operator から借りてこよう。  

すると、これを命令文とみなし、改めて  

「タイタニック号のメタデータを見せろ」と訳すことも出来る。  
間違いではない気もするが、より適切なのは、  
wait for a small window to pop up. Wait for a small window to display some meta ... dataset.  
とすることではなかろうか。メタデータを見せてくれるのは、ラピッドマイナラーではなく、ウィンドウだ(と考えたほうがもっとしっくりくる)。  

ここまでを、andを使わずに書くと、次のとおり。  

Now, hover the mouse over the output port of the Retrieve operator.  
Now, wait for a small window to pop up.  
Now, wait for a small window to display some meta about the Titanic dataset.  

全体の訳:「レトリブオペレータの出力ポートにカーソルをのせ、小さなウィンドウにタイタニックのメタデータが表示されるのを待ってください。」  

## Some of the information you know from the Statistics tab.

結論から言うと、倒置が起こっている。  
構文解析に慣れておくと、倒置が起こっていてもすぐに気が付ける。  

まず、倒置に気がつかず構文解析をすると、  

S:some of the information？それともyou？  
V:know  
(OまたはC):you？それともsome of the information？
M:from the Statistics tab(statistics→統計)  

となる。  
VとMだけ訳してしまうと次のとおり。  
「Sは、統計タブから(OまたはC)を知る。」  
後は常識で考えてしまえばいい。  
「あなたは、統計タブから一部の情報を得ることができる」  
倒置を元に戻した英文は次のとおり。  
You (will) know some of the information from the Statistics tab.

## Note the two columns Role and Type in the table at the bottom.

イディオム: Note で始まる文 → noteは「～を気に留める」という動詞。これの命令文。  

S:命令文  
V:note(気を付ける)  
O:two colums Role and Type(RoleカラムとTypeカラム)  
M:in the table at the bottom(表の一番下に)  

(分かると思うけど)andを使わずにこの文を書くと、  
Note the Role column in the table at the bottom.  
Note the Type column in the table at the bottom.  
となる。

訳:「RoleカラムとTypeカラムが表の一番下に来ていることを確認してください」  

## Each attribute ... (...)

<!-- Each attribute has a type which defines the possible values for the column (for example, if values can be nominal or numerical). -->

S:each attribute(それぞれの属性)  
V:has  
O:a type  
M:(which以下)  

とりあえず仮に訳すと、次のようになる。  
「それぞれの属性には、(which以下)なタイプがあります」  

次に、which以下について考えよう。  

itに置き換えると文が成立しなくなる。  
(「Each attribute has a type it.」は、おかしい)  
したがって、このwhichは関係代名詞である。  

関係代名詞は従位接続詞と代名詞両方の役割を果たす。  

従位接続詞は従属節の先頭に来て、従属節全体を副詞節や名詞節などにしてしまう。  

例えば、I believe that feminism will go out.  
なら、I believe が主節、  
feminism will go out が従属節である。  
従属節の先頭にthatをつけて、  
that feminism will go outとすると、名詞節になる。  
その証拠に、that feminism will go outをitなどの名詞に置き換え、  
I believe it. としても意味が通じるではないか。  

今回の文の場合は、which以下は副詞節となる(which以下を丸ごと消しても文は壊れない)。  

which以下を構文解析しよう。  

S:(代名詞としての)which  
V:defines  
O:the possible values  
M:for the column(for example, if values can be nominal or numerical)  

無理やり訳すと「whichはカラムの有効な値を決めます(例えば、値が質的変数なのか、量的変数なのかなど)」となる。  
(質的変数、量的変数は統計学の用語)  

全体の訳:「それぞれの属性には、カラムの有効な値を決めるタイプがあります(例えば値が質的変数なのか量的変数なのかなど)」  
ちょっと不自然なので手直ししよう。  
「それぞれの属性には、タイプ(質的変数／量的変数など)が決められています」  

## The role of an attribute describes how the column is used by machine learning operators

S:the 'role'  
V:describes  
O:(how以下)  
M:of an attribute  

とりあえず仮に訳す。  
「属性『role』は(how以下)を表します」  

では、how以下について考えよう。  

howは疑問副詞または関係副詞である。  
(実は名詞もある。(the howで方法、the whyで理由。)  
もっというと間投詞もあるにはあるけど、Wiktionary英語版に書かれていただけ。信ぴょう性は低い)  

how以下をitに置き換えて、  

The role of an attribute describes it.  
としても文はおかしくない。  

したがって、このhowは疑問副詞となる。  

how the column is used by machine learning operators  
を構文解析すると  

S:the column  
V:is used  
M:by machine learning operators(機械学習オペレータによって)  
M:how  

となる。ソースは俺。(ネットで調べると、受動態の文型については見解があまりにも分かれ過ぎていて複雑。)  

howを後ろに持ってくると、  

the column is used how by machine learning operators.  
あるいは  
the column is used by machine learning operators how.  
となる。  

訳:「カラムが機械学習オペレータによって、how使われる」  

全体の訳:「属性『role』は『カラムが機械学習オペレータによって、(how=)どのように使われる』を表します」  
「属性『role』は、カラムが機械学習オペレータによって、どのように使われるのかを表します」  

## Attributes without ... observations of data.

<!--
 Attributes without any role (also called "regular" attributes) are used as input for training while id attributes are usually ignored by modeling algorithms because they are only used as unique identifiers of observations of data.
-->

S:attributes  
V:are used  
M:without any 'role' (also ...)  
M:as input for training  
？:while以下  

とりあえず仮に訳すと、  
「'role'の存在しない属性(以下『正則属性』と呼びます)は訓練用の入力として使われます(while以下)」  

while以下について考えてみよう。  

<!--
 while id attributes are usually ignored by modeling algorithms because they are only used as unique identifiers of observations of data.
-->

まずwhileは接続詞または名詞である。  
動詞「～をダラダラ過ごす」はめったに使われない。  
従位接続詞として使われる場合は副詞節を作る(＝要はwhile以下が副詞的になる)  
名詞として使われる場合は加算名詞として「(短い)間」とか「暫時」等の意味を表す。  

従って、(ほぼ間違いなく)while以下は副詞節Mである。  

従位接続詞while自体の対訳はあまりにもいろいろある(強いて抽象的に一言でいうなら『そして』だろうか)ので、  
while以外をまず訳そう。  

構文解析をすると  
S:id attributes  
V:are usually ignored  
M:by modeling algorithms  
M:(because以下)  

訳:「(while、)id属性は通常、モデリングアルゴリズムでは無視されます(because以下)」  
自然にすると、  
「(while、)id属性は、モデル化の際には基本的に無視されます。(because以下)」  

because以下について考えよう。  
because以下は基本的に理由を表す名詞節あるいは副詞節となる。  
したがって、従位接続詞becauseそのものの意味は「～なので」などになると考えるべきである。  

S:they  
V:are only used  
M:as unique identifiers(一意識別子(データを一意に識別するための識別子のこと)として)  
M:of observations(観測の)  
M:of data(データの)  

訳:「それらはデータ探索に用いる一意識別子として使われるだけなので」  

全体の訳:「'role'の存在しない属性(以下『正則属性』と呼びます)は訓練用の入力として使われます『(while、)id属性は通常、モデル化の際には基本的に無視されます。【それらはデータ探索に用いる一意識別子として使われるだけなので】』)」  
自然にしてみる。  
「'role'の存在しない属性(以下『正則属性』と呼びます)は訓練用の入力として使われます(while=)が、id属性は通常、データ探索に用いる一意識別子として使われるだけなのでモデル化の際には基本的に無視されます。」  

## Define attribute types and roles

訳:「属性のタイプとルールの決定」  

## Add a 'Discretize by Binning operator' and connect it. In its Parameters:

訳:「『Discretize by Binningオペレータ』を追加し、接続してください。'Parameterts'で、次の操作をしてください。」  
discretizeは「離散化」、binningは「まとめ」など  

In its Parameters:  
は、2～4で、説明が続いていることを意味する。  
コンマの右には、補足説明の列挙が続くことが多い。  

## Set the attribute ... から Set number of bins to 3

略。そのままだから。  

## 'Binning' is a common technique to transform the type from numerical to polynominal (a nominal with more than 2 values).

S:'Binning'  
V:is  
C:a common thchnique  
M:to transform the type(タイプを変換するための)  
M:from numerical to polynominal(数値から多項式へ)    
M:(a nominal with more than 2 values)2項以上の式  

訳:「Binning」は数値から多項式(2項以上の式)へとタイプを変換するための、基本的な方法です。  

## Here, we create three "bins" converting the  complete range of values.

S:we  
V:create  
O:three "bins"  
M:converting the complete range of values(値の範囲全体を変換する)

訳:では、値の範囲全体を変換する3つの「bin」sを作っていきましょう。  

## The operator then replaces the original numerical value with the name of the bin the value belongs to

S:the operator  
V:replaces  
O:the original numerical value(元の数値を)  
M:with the name of the bin(binの名前で)  
M:the value belongs to  

イディオム:replace A with B → AをBで置き換える  

訳:(Discretize by Binning)オペレータは元の数値をその値が格納されているbinの名前で置き換えます。  

## Define which column to predict  

訳:どの列を予測するか決めてください

## In RapidMiner, we call the column which should be predicted label.  

イディオム:S call O C→SはOをCと呼ぶ  

S:we  
V:call  
O:the column  
C:label  
M:which should be predicted  

訳:「RapidMinerでは、予測に使う列を『ラベル』と呼びます」  

## It is sometimes also known as target or class

訳:「『ラベル』は(RapidMiner以外において？)ターゲットとかクラスとして呼ばれているものです」

## Add the Set Role operator and connect it  

### andの前

S:命令文  
V:add  
C:the Set Role operator

とりあえず仮に訳「Set Roleオペレータを追加してください」  

### andの後ろ

S:無  
V:connect  
C:it  

Sがないのでandの前と同じと考える。  

andを使わずに書くと、「Add the Set Role operator. Connect it.」  

全体の訳:「Set Roleオペレータを追加し、接続してください」  

## In Parameters, for attribute name Survived, change target role to label.  

S:命令文  
V:change  
O:target role  
M:to label  
M:in Parameters(Parametersで)  
M:for attribute name Surbived(属性値Survivedについて)  

訳:「Parametersで、属性値Survivedについて、target roleをlabelに変更しましょう」  

## Run the process and inspect the results. 

訳:「プロセスをRunし、結果を見ましょう」  
## Look at the Statistic tab in the Results view

訳「Results(結果)ビュー内の、Statistics(統計)タブを見てください」  

## Note that the role of Survived has changed to label.  

訳「Surivedのroleはlabelに置き換わったことにご留意ください」  

## The attribute Age ... by new names  

途中の「:」は「補足説明」などの意味。  

### 「:」の前

S:the attribute Age  
V:has  
O:a new type  

訳:「Age属性もまた新しいタイプを持っています」

### 「:」の後

S:the numerical values  
V:have been replaced  
M:by new names

訳:「Ageを表す数値は新しい名前に置き換わっています」  

## Congratulations!

おめでとう

## Great work!

よくやった

## Setting an attribute ... other attributes

まず、butは文と文を等位接続している。  

### Setting ... weights)  

S:settig an attribute role  
V:is  
C:useful   
M:for meny things  
M:e.g.(＝たとえば)以降  

訳「role属性を設定することは、いろんなことに役立ちます。(例えばIDのような属性だったりウエイトの例だったり)」

### but the most ... attributes.

S:the most frequent use  
V:is  
C:to define the label  
M:i.e.(＝つまり)以降  

useは動詞としてもっともよく知られるが、  
名詞として登場することもそこそこある。  

訳「でも、もっともよく使われるのは、ラベル、つまりどの属性が他の属性に基づいて予測されるべきかを決定する用途である」  

## challenge

無視します

## 参考

http://www.eibunpou.net/12/front.html

https://eigo-box.jp/grammar/coordinating-conjunction/

https://www.studyplus.jp/376

https://www.studyplus.jp/372

https://english.005net.com/yoten/kansetu.php (マニュアル的なことより、定義を解説してくれてる。ありがたい)

https://www.efjapan.co.jp/eigo-resources/english-grammar/determiners/ (マニュアル的なことよりry)  

辞典は 研究社 新英和中辞典