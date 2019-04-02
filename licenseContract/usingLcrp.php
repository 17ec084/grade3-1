<?php
//準備(システム)
include 'lcrp.php';
//準備(システム)ここまで

$licenseKey="key";//ここにライセンスキーを入力

//準備
$num = $_GET['num'];
//準備ここまで

//メインプログラム
    myFunc($num,9999999999999);
    //9999999999999以下のべき数を考える
//メインプログラムここまで

//myFuncの説明
    function myFunc($num,$max)
    /*
    $numのべき数のうち、$max未満のものを求め、
    それらから1を引き算したものが素数になるかを調べ、
    結果をprintする
    */
    {
        global $licenseKey;
        if($num<2)
        //2未満お断り
        {
            print "エラー 2以上の整数を指定しなさい";
            return 1;
        }
        $j=1;
        $pow=$num;
        while($pow<$max)
        {
            print function_get_return("lcrp://isPlus1Prime(".$pow.",".$j.")",$licenseKey);
            //$powから1引き算したものが素数かどうか言う
            $j++;
            //$jは、$powが$numの$j乗であることを示すためのもの。
            $pow= function_get_return("lcrp://getNextPow(".$pow.",".$num.")",$licenseKey);
            //次のべき数を求める。つまり$num倍する
        }
    }
//myFuncの説明ここまで