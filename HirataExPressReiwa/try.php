<?php
$sent=[];
$arr = array('cardNum','spellOfRevival');
if(isAllNeededSet($arr))
{
//必要な情報が送信されている
    foreach($arr as $str)
    //(post(優先)またはget)送信された情報を、非スーパーグローバルな変数に格納
    {
        $sent[$str]=(isset($_POST[$str]) ? $_POST[$str] : $_GET[$str] );
    }
    $sent['licenseKey']=$_POST['licenseKey'];

    if(auth($sent['cardNum'], $sent['licenseKey']))//未実装
    //そのライセンスキーで、そのカードを利用していいのか確認
    {
        $tmp=decodeSpell($spellOfRevival);//未実装
        //$tmp=array(始めた時間のシリアル値,暫定スコア,問題番号の配列,苦手問題の配列,苦手度の配列,乱数や変数の配列)
/*
        $startTime=$tmp[0];
        $score=$tmp[1];
        $QArr=$tmp[2];
        $Q=$QArr[0];//今回出す問題の問題番号
        $NigateArr=$tmp[3];
        $NigateLevelArr=$tmp[4];
*/
        if(isset($_POST['checkAnswer']))
        {
            checkAnswer($tmp);//未実装
            //答え合わせする
        }
        else
        {
            propose($tmp);//未実装
            //出題する
        }
    }
    else
    {
        putErrorHead();//未実装
        putDemand($card);//そのカードのライセンスを手に入れるのに必要な社会的価値
        putErrorfoot();//未実装
    }


}
else
{
//情報が正しく受信できていない
}

    function isAllNeededSet($arr)
    {
        $return = isset($_POST['licenseKey']);
        foreach($arr as $str)
        {
            if(!$return)
            {
                return false;
            }
            $return = (isset($_POST[$str]) || isset($_GET[$str]));
        }
        return $return;
    }

    function auth($card, $licenseKey)
    {
        $pubKey="rtgtsjogjbsrskvf34w9jfsrdgnvse0of349ofjepaofrkcghvaerpfjafjcsdivndfghea8ifhawdkiowfvmaosjfcvs9hbst89cbnpiajerspvjuibhs59p0fjcpvsoihbnaipovchvubh5esoivn sohe0kd-03rfkogbhvrfipfrq394tgtopnpfiut489ty9bu3q4r349tugjklbGBEbnshurfhivbjawrfdtsrfvsuiffzGsb";
        if(file('auth/.htaccess')!="Deny from all")
        {
            print "サーバの内容に致命的なエラー。サーバ管理者は至急確認せよ。利用者の方はどうかサーバ管理者にこのエラーの旨伝えてほしいです。";
            return false;
        }
        else
        {
            //暗号化されたファイルauth/auth.txtにアクセスし、照らし合わせることで認証する。
            //万が一設定ミスでこのtry.phpファイルに書いてある鍵がバレても大丈夫なように公開鍵暗号方式を使って暗号化されている。
            //参考:https://yuzu441.hateblo.jp/entry/2018/11/01/213255#f-03365a3e
            $msg=$licenseKey;
            openssl_public_encrypt($msg, $crypted, $pubKey);//(phpで予め定義されている関数)
            //$cryptedに「暗号化されたライセンスキー」が入る
            return isLicensed($card,$crypted);//$cardに対応するファイルの中に、暗号化されたライセンスキー「$crypted」が書かれているか(未実装)
        }
    }

    function propose($tmp)
    {
        $startTime=$tmp[0];
        $score=$tmp[1];
        $QArr=$tmp[2];
        $Q=$QArr[0];//今回出す問題の問題番号
        $NigateArr=$tmp[3];
        $NigateLevelArr=$tmp[4];

        putHeader();
        putQ($Q);//未実装(改行コード、画像挿入コードなど必要なので結構面倒になると思う)
        //問題を表示
        if(haveOptions($Q))//未実装
        {
            putOption($Q);//未実装
        }
        $timeLimit=getTimeLimit($Q);//未実装(0なら無制限)
        putA($Q,haveOptions($Q));//未実装(選択問題か否かで答えの表示の仕方が異なる)
        putFooter();//未実装
    }

        function putHeader()
        //認証エラーでも出題でも答え合わせでも共通
        {
            print 
'<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style.css">
<body>
<p class="logo">HirataExPressReiwa</p>
<p class="userName">'.getUserName($sent['licenseKey']).'として認証されています</p>
<p class="cardTitle">'.getCardTitle($sent['cardNum']).'</p>';
        }

        function putQ($Q)
        {
            getQAStr();//未実装
            //単語情報の書かれたファイル内容から、ライセンスキーの情報を取り除いた文字列を返す。
            //4n行目:第n+1問の問題
            //4n+1行目:第n+1問の制限時間(0なら無制限)
            //4n+2行目:第n+1問の選択肢
            //4n+3行目:第n+1問の答えと解説

            $question=getQuestion($Q);//未実装
            //4*($Q-1)行目を文字列として取得
            /*
            ここで問題文(及び解説文)の書式を定める。それに応じて変換し、printする。
            ・改行は<hir:br:ata>
            ・画像(./img/内のもの)は<hir:画像=ファイル名:ata>
            ・画像(絶対パス)は<hir:画像==絶対パス:ata>
            ・音声(./aud/内のもの)は<hir:音声=ファイル名:ata>
            ・音声(絶対パス)は<hir:音声==絶対パス:ata>
            ・(./aud/内のもの)は<hir:音声=ファイル名:ata>
            ・音声(絶対パス)は<hir:音声=絶対パス:ata>
            ・動画(youtube)は<hir:u2b=動画ID:ata>
            ・乱数は<hir:乱数「番号」=(最小値)to(最大値):ata>
              例:<hir:乱数「0」=2to5:ata>
            ・計算(乱数「番号」、四則演算、指数対数、三角関数、逆三角関数、四捨五入、切り捨て、切り上げのみ)
              <hir:(phpで変数に代入するのと同じやり方):ata>
              但し変数名は変数「番号」に限り、番号は乱数と重ならないようにする
              例:<hir:変数「1」=pow(乱数「0」,2):ata>
                 <hir:変数「2」=round(変数「1」):ata>
            ・htmlタグは無効 htmlspecialchars($str, ENT_QUOTES, 'UTF-8');
            */
            
        }

