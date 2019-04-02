<?php

function lcrp_main($args)
//「関数全体」の名前は必ずlcrp_mainとすること。
//仮引数はその有無や個数に関わらず$argsとする必要がある。
{
//準備
$pow=$args[0];
$j=$args[1];
$return=[];
$return[1]='';
$return[2]=false;
//準備ここまで

/*
function isPlus1Prime($pow, $j)
{  
  //$powから1引き算したものが素数かどうかを述べる。
  //また$jは、$powが$numの何乗であるかを示す。(print時に使う。)
*/
    $pow=(int)$pow;
    $p=$pow+1;
    if($p%2==0)
    {
    //素数でないとき
        $str=$p."=(".$j."乗に1を足したもの)は合成数。\n<br>";
        $return[0] = $str;
        return $return;
    }
    for($i=3; $i*$i<=$p; $i+=2)
    {
        if($p%$i==0)
        {
        //素数でないとき
            $str=$p."=(".$j."乗に1を足したもの)は合成数。\n<br>";
            $return[0] = $str;
            return $return;
        }
    }
    //素数だったとき
    $str="<strong>".$p."=(".$j."乗に1を足したもの)は素数。</strong>\n<br>";
    $return[0] = $str;    
    return $return;
}

