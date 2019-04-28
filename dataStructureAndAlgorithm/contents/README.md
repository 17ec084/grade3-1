# detaStructureAndAlgorithm

## 第3回

### 決定性オートマトンとプログラム  

非決定性オートマトンは ア: <!-- hole イプシロン遷移 --> といって、何もしなくても複数通りに進みうるなど、次の状態への遷移が曖昧である。  
これに対して、決定性オートマトンは次の状態への遷移が曖昧でない。  
したがって、プログラムに変換することができる。  

[イ](http://edu.net.c.dendai.ac.jp/ad2/2019/3/daaabc.png) の決定性オートマトンをプログラムに直すと、  
次のようになる。  


```
import java.util.*;
import java.util.regex.*;
import java.io.*;
class Rei2 {
    public static void main(String arg[]) throws IOException {
	final BufferedReader br = new BufferedReader(
                                    new InputStreamReader(System.in));
	String buf;
	final Pattern p = Pattern.compile("[1-9][0-9]*");
	Matcher m;
	while((カ)){
	    (キ);
	    while((ク)){
		(ケ);
	    }	
	}
    }
}
```



自然数を受理する決定性オートマトンを作ると、 [ウ](img/automatonN1.png) のようになる。  

### 決定性オートマトンと非決定性オートマトンとの等価性 

![](http://edu.net.c.dendai.ac.jp/ad2/2019/3/na.png)  
は、状態1のときにaが入力されたとき、1にいくのか2にいくのかわからないので非決定性オートマトンである。  

「,」を「または」と読み替えて考えれば、このオートマトンでは次の表の通りの状態遷移が起こる。  

[ア](img/stateList1.png)  

この「～または～」自体を状態と考えてしまえば、次のような決定性オートマトンに変換できる。  

[イ](http://edu.net.c.dendai.ac.jp/ad2/2019/3/da.png)  


### オートマトンの出力 
文字列から、「正規表現にマッチする部分列」を抽出することを考えよう。  
抽出したい部分を ア:<!-- hole ()で括る --> のが一般的である。  
例えば自然数を取り出したいときは、 イ: <!-- hole .*([1-9][0-9]*) --> のように表現する。  
イのようにして、文字列「a12345b」から文字列を抽出したい場合、 ウ: <!-- hole 最長一致 --> となる 12345が抽出されることが多い。  
このようなオートマトンは [エ](http://edu.net.c.dendai.ac.jp/ad2/2019/3/1023.png) のように表される。  

javaで正規表現を扱うオートマトンは、 オ: <!-- hole java.util.regex --> ライブラリに用意されている。  

javaで「キーボード入力から自然数を取り出す」プログラムを作ると、次のようになる。  

```

import java.util.*;
import java.util.regex.*;
import java.io.*;
class Rei2 {
    public static void main(String arg[]) throws IOException {
	final BufferedReader br = new BufferedReader(
                                    new InputStreamReader(System.in));
	String buf;
	final Pattern p = Pattern.compile("[1-9][0-9]*");
	Matcher m;
	while((カ)){
	    (キ);
	    while((ク)){
		(ケ);
	    }	
	}
    }
}

```

カ: <!-- hole (buf=br.readLine())!=null -->  
キ: <!-- hole m= p.matcher(buf) -->  
ク: <!-- hole m.find() -->  
ケ: <!-- hole System.out.println(m.group()) -->  


### 正規表現の限界

ア: <!-- hole 「(」と「)」 --> をアルファベットとし、ルールを イ: <!-- hole かっこが過不足なく閉じている --> とする言語をカッコ言語という。  

正規表現は、つまり決定性オートマトンはカッコ言語を受理しない。これを証明しよう。  
カッコ言語を受理する決定性オートマトンが存在すると仮定して、矛盾を導こう。  
決定性オートマトンMの状態数がnであるとする。  
開きカッコがn+1個続き、その後閉じカッコがn+1個続くカッコの列sを考えよう。  
仮定によりsをMは受理する。  
状態数nよりも多い個数n+1だけ開きカッコがあるわけで、  
ということは、ウ: <!-- hole 1つの状態が2回以上現れる --> ということが必ず起こる。  
この状態をqとする。  
qが出現するのがi番目とj番目の開きカッコを入力した後であるとしよう。  
i＜j  
ここで、エ: <!-- hole i番目のあとにj+1番目の入力 --> を与えてみよう。  
(このような文字列をs'と呼ぼう)  
オ: <!-- hole「j+1番目の文字」は「状態qの次の入力」に等しい --> ため、 カ: <!-- hole i+1番目以降をj+1番目以降で置き換えて --> も、  
sを受理する決定性オートマトンは必ずs'を受理してしまう。  
しかしs'はi回開きカッコを繰り返した後、  
カ <!-- hole n+1-j --> 回開きカッコを繰り返し、  
キ <!-- hole n+1 --> 回閉じカッコを繰り返すというものになっているため、  
ク <!-- hole 開きカッコと閉じカッコの個数が一致しない --> 。  
それなのに受理してしまった。  
このようにして、カッコ言語を決定性オートマトンで表現することは不可能であるということが導かれる。  

### P=NP問題
P=NP問題とは、ア <!-- hole 決定性オートマトンで多項式回の状態遷移で計算できる問題の集合をA、非決定性〃をBとするとき、A=Bであるか否か --> という問題である。  


 
