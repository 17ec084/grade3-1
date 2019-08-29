<big><big><big><big><a href="https://raw.githubusercontent.com/17ec084/grade3-1/master/easyHack.md">こちら</a></big></big></big></big>


①https://www.eeb.co.jp/wordpress/?p=476 を参考にjavaのnativeメソッドtest()をC言語で実装する。
printf("warning: you've been hacked!!");

②test()の呼び出しを行うjavaファイルを次のように書く
/**/native /**/ test(); 
///* 
void test(){System.out.println("test");} 
//*/

③
「*/native /*」 をユニコードエスケープする
④
さらに///*の/*と//*/の*/をユニコードエスケープし、その左に改行のエスケープも入れる

⑤ここまででみかけは次のようになる。(?はユニコードエスケープ)

/*?????????*/ test();
//?????????
void  test(){System.out.println("test");}
//?????????

コメントを取り除くと
test();
void test(){System.out.println("test");}

⑥だがユニコードエスケープより、本当は次のようになっている
/**/native /**/ test(); 
//
/* 
void test(){System.out.println("test");} 
//
*/

コメントを取り除くと
native test();

⑦以上の理論により、
「test」と表示されそうなところが「warning: you've been hacked!!」と表示される

Eclipseでは動くんだろうね。(根拠: http://blogs.wankuma.com/nagise/archive/2008/04/02/131242.aspx)
悪用すればEclipseからウイルスを呼ぶこともできちゃうだろうし金銭とか扱うプログラムでやられれば実害も出そう。
冗談ネタが冗談ではなくなってきてるなと思ったのでまとめてみた。確認はしていない。
