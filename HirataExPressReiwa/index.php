<?php

if(isset($_POST['licenseKey']) && isLicensed($key=$_POST['licenseKey']))
{
//クライアントがライセンス認証されている場合
    $userName=getUserName($key);//ユーザ名
    $contents=getAvailableContents($key);//そのユーザが利用可能な問題集(の番号)を配列として取得

}
else
{
//クライアントがライセンス認証を受けていない場合
    askLogin();//ログインを促し
    $userName="guest";
    $contents=getAvailableContents("guest");//ゲスト用のコンテンツを用意する
}

print dumpContents($key,$contents);


