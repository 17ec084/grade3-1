<?php

function lcrp_main($args)
//「関数全体」の名前は必ずlcrp_mainとすること。
//仮引数はその有無や個数に関わらず$argsとする必要がある。
{
//準備
$pow=$args[0];
$num=$args[1];
$return=[];
$return[1]='';
$return[2]=false;
//準備ここまで

/*
function getNextPow($pow, $num)
{
  //$powから1引き算したものが素数かどうかを述べる。
  //また$jは、$powが$numの何乗であるかを示す。(print時に使う。)
*/

    $return[0] = $pow*$num;
    return $return;
}