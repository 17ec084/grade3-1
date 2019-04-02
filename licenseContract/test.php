<?php
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
            print isPlus1Prime($pow,$j);
            //$powから1引き算したものが素数かどうか言う
            $j++;
            //$jは、$powが$numの$j乗であることを示すためのもの。
            $pow=getNextPow($pow,$num);
            //次のべき数を求める。つまり$num倍する
        }
    }
//myFuncの説明ここまで

    //isPlus1Primeの説明
        function isPlus1Prime($pow,$j)
        /*
        $powから1引き算したものが素数かどうかprintする。
        また$jは、$powが$numの何乗であるかを示す。print時に使う。
        */
        {
            $p=$pow+1;
            if($p%2==0)
            {
            //素数でないとき
                $str=$p."=(".$j."乗に1を足したもの)は合成数。\n<br>";
                return $str;
            }
            for($i=3; $i*$i<=$p; $i+=2)
            {
                if($p%$i==0)
                {
                //素数でないとき
                    $str=$p."=(".$j."乗に1を足したもの)は合成数。\n<br>";
                    return $str;
                }
            }
            //素数だったとき
            $str="<strong>".$p."=(".$j."乗に1を足したもの)は素数。</strong>\n<br>";
            return $str;
        }
    //isPlus1Primeの説明ここまで

    //getNextPowの説明
        function getNextPow($pow,$num)
        {
            return $pow*$num;
        }
    //getNextPowの説明ここまで