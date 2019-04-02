<?php
$productNumber='example0001';
$lcrpServer='rights-for.men/lcrp';//今後[lcrpサーバ]と呼ぶ
$query=array();//post送信したいデータ


function function_get_return()
{
//準備
$args= func_get_args();
$url=$args[0];
$licenseKey=$args[1];
if(count($args)>2)
{
    $printHeader=$args[2];
}
$printHeader=isset($printHeader);
//$printHeaderは「指定されたか否か」の論理値に変換してしまう
//準備ここまで

/*
function function_get_return($url, $licenseKey [, $printHeader])
{
*/


    global $query;
    $query=array_merge($query,array('licenseKey'=>$licenseKey));
    //$queryにライセンスキーを追加

    if(strpos($url,'lcrp://')!==false && strpos($url,'lcrp://')==0)
    {
    //$urlの先頭が'lcrp://'の場合
        $url=lcrp2http($url);
        /*
        lcrp2httpは、引数に与えられた(lcrpの)urlをhttpに変換する
        例:
        before
            lcrp://testFunc($a,$b)  但し$a=4,$b=6
        ↓
        after
            http://[lcrpサーバ]/example0001/index.php?funcName=testFunc&arg1=4&arg2=6
        */

        if($printHeader)
        {
            print "url=".$url."<br>クエリ(配列)=";
            print_r($query);
        }
        
        if($printHeader)
        {
            print $result=goToLcrpServer($url,$query);
            //lcrpサーバに問い合わせる
        }
        else
        {
            $result=goToLcrpServer($url,$query);
            //lcrpサーバに問い合わせる
        }
        if(isStatusOK($result))
        {
            //run(getOpeCommand($result));
                //printなど、戻り値で表せない内容に関しては
                //今回は取り扱わない
            return getReturnValue($result);
        }
        else
        {
            return "error: status isn't ok<hr><hr>".$result."<hr><hr>";
        }
    }
    else
    {
        print file_get_contents($url);
    }
}

    function lcrp2http($url)
    //lcrp://testFunc($a,$b)  但し$a=4,$b=6
    //→
    //http://[lcrpサーバ]/example0001/index.php?funcName=testFunc&arg1=4&arg2=6
    //(引数をget送信する場合)
    {

        global $lcrpServer,$query;

        $funcName=getFuncName($url);//関数名を取り出す
            //$funcName=
            //'testFunc'
        $args=getArgsAsArray($url);//引数全部を配列として取り出す
            //$args=
            //[4,6]
                //蛇足:['testFunc',4,6]とするのは、後に便利だが冗長の上、型の問題有り
        $url=
        'http://'.$lcrpServer.'/example0001/index.php?funcName='.$funcName;
        //$url=
        //'http://[lcrpサーバ]/example0001/index.php?funcName=testFunc'

        $query=array_merge($query,
        postSendArgs($args)
        );
        //$url=getSendArgs($args,$url);
          //$url=
          //'http://[lcrpサーバ]/example0001/index.php?funcName=testFunc&arg1=4&arg2=6'

        return $url;

    }

        function getFuncName($url)
        //lcrp://testFunc(4,6)
        //→
        //testFunc
        {
            //問題:
            //引数に括弧がある場合にエラーが起こる
            //引数に'lcrp://'を含めるとエラーが起こる

            $rtn = $url;
            $rtn = str_replace('lcrp://','',$rtn);
                //testFunc(4,6)
            $start = strpos($rtn, '(');
            $str = substr($rtn, $start);
                //(4,6)
            $rtn = str_replace($str,'',$rtn);
                //testFunc
            return $rtn;
        }

        function getArgsAsArray($url)
        //lcrp://testFunc(4,6)
        //→
        //[4,6]
        {
            //問題:
            //引数に括弧または','がある場合にエラーが起こる
            //引数に'lcrp://'を含めるとエラーが起こる

            $str = $url;
            $str = str_replace('lcrp://','',$str);
                //testFunc(4,6)
            $start = strpos($str, '(');
            $str = substr($str, $start);
                //(4,6)
            $str = str_replace('(','',$str);
            $str = str_replace(')','',$str);
                //4,6
            $i=0;
            $rtn=[];
            while(($start=strpos($str,','))!==false)
            //$strに','が存在する間(場所は$start番目)
            {
                $tmp=substr($str,$start); 
                    //,6
                $tmp=str_replace($tmp,'',$str);
                //引数を取り出す
                    //4
                array_push($rtn,$tmp);
                //配列に引数を入れる
                $tmp=$tmp.',';
                $str=str_replace($tmp,'',$str);
                //引数を削除する
                    //'4,6'から'4,'を取り除く
            }
            //この時点で$strは最後の引数のみをもつ
                //6
            array_push($rtn,$str);
            return $rtn;
        }
        

        function postSendArgs($args)
        {
            $arr=array();
            for($i=0; $i<count($args); $i++)
            {
                $arr=array_merge($arr,
                array('arg'.($i+1)=>urlencode($args[$i]))
                );
                //$i+1としたのは、c言語のコマンドライン引数の0番目が引数でなく関数名になっていることに倣い、1オリジンにしたかったから。
            }
            return $arr;
        }
            

        function getSendArgs($args,$url)
        {
            $rtn=$url;
            for($i=0; $i<count($args); $i++)
            {
                $rtn=$rtn.'&arg'.($i+1).'='.urlencode($args[$i]);
                //$i+1としたのは、c言語のコマンドライン引数の0番目が引数でなく関数名になっていることに倣い、1オリジンにしたかったから。
            }
            return $rtn;
        }

    function goToLcrpServer($url,$query)
    {
        //参考(というか引用):https://taitan916.info/blog/?p=2665

        $content = http_build_query($query, '', '&');
        $header = array(
	'Content-Type: application/x-www-form-urlencoded', //form送信の際、クライアントがWebサーバーに送信するコンテンツタイプ
	'Content-Length: '.strlen($content) //メッセージ本文の長さ
        );
 
        $context = array(
            'http' => array(
                'ignore_errors' => true, //ステータスコードが失敗を意味する場合でもコンテンツを取得
                'method' => 'POST', //メソッド。デフォルトはGET
                'header' => implode("\r\n", $header), //ヘッダ設定
                'content' => $content //送信したいデータ
            )
        );
        $res = file_get_contents($url, false, stream_context_create($context));
        return "\n".'start from here<hr>'."\n".$res;
    }

    function isStatusOK($result)
    {
        $i = getMatchLine("<hr>status:",$result);
        //"<hr>status:"に完全一致するのは何行目か。部分一致不可
        //(改行コード含めるな)
        if($i==-1)
        {
            return false;
        }
        $i++;
        return (getTextByLine($i,"<hr>",$result)=="ok");
        //i行目から、先頭に"<hr>"が出てくる行までが「ok」
        //最後の改行コードは不要
    }

        function getMatchLine($needle,$hayStack)
        {
        //$hayStackの中から\n$needle\nを探し、
        //その行を返却(0オリジン)
            /*
            $hayStack=
            "aaaaa\n
            bbbbb\n
            ccccc\n
            dd$needleddd\n
            $needle\n
            fffff\n
            ggggg\n
            hhhhh"
            */
            if(($tmp=strpos($hayStack,"\n".$needle."\n"))===false)
            {
                return -1;
            }
            $hayStack=str_replace($match=substr($hayStack,$tmp),'',$hayStack);
            //str_replaceは今回は暴走しえない
            /*
            $hayStack=
            "aaaaa\n
            bbbbb\n
            ccccc\n
            dd$needleddd"
            $match=
            "\n
            $needle\n
            fffff\n
            ggggg\n
            hhhhh"
            */
            return substr_count($hayStack,"\n")+1;//\nの個数+1→行数+1 今回は4
            
        }

        function getTextByLine($i,$needle,$hayStack)
        {
        //$hayStackの$i行目以降から\n$needle以降を取り除き、返却
            /*
            $hayStack=
            "aaaaa\n
            bbbbb\n
            ccccc\n
            ddxxxxxxddd\n
            xxxxxx\n//$i行目
            fffff\n
            $needleggg\n
            hhhhh"
            */
            for($i; $i>0; $i--)
            {
                $tmp=strpos($hayStack,"\n");
                $hayStack=substr($hayStack,$tmp+1);
            }
            /*
            $hayStack=
            "fffff\n//$i行目
            $needleggg\n
            hhhhh"
            */
            $tmp=strpos($hayStack,"\n".$needle);
            $hayStack=str_replace(substr($hayStack,$tmp),'',$hayStack);
            /*
            $hayStack="fffff"
            */
            return $hayStack;
        }


    function getOpeCommand($result)
    {
        $i = getMatchLine("<hr>operationCommand:",$result);
        //"<hr>operationCommand:"が何行目にあるか
        if($i==-1)
        {
            return "error: Operation Commands aren't found.";
        }
        $i++;
        return getTextByLine($i,"<hr>",$result);
        //i行目から、先頭に"<hr>"が出てくる行まで
    }

    function getReturnValue($result)
    {
        $i = getMatchLine("<hr>returnValue:",$result);
        //"<hr>returnValue:"が何行目にあるか
        if($i==-1)
        {
            return "error: Return Value isn't found.";
        }
        $i++;
        return getTextByLine($i,"<hr>",$result);
    }




/*

$code=
"
if(strpos($url,'lcrp://')!==false && strpos($url,'lcrp://')==0)
{
//$urlの先頭が'lcrp://'の場合
    $url=str_replace('lcrp://','http://rights-for.men/lcrp/'.$productNumber.')
";
runkit_function_redefine('file_get_contents','$url',$code);
file_get_contents();


*/