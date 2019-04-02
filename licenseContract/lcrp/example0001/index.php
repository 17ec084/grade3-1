<?php

if(isset($_POST['licenseKey']) && authenticate($_POST['licenseKey']))
{
//ライセンスキーが認証されたとき
    print '<hr>uesr:'."\n".getWho($_POST['licenseKey'])."\n";
    print '<hr>totalSecondsFrom19700101GMT:'."\n".time()."\n";
    $args = makeArgsArray();
    $result = run($_GET['funcName'],$args);//実際に関数を動かしてみる。
    print '<hr>returnValue:'."\n".$result[0]."\n";
    print '<hr>operationCommand:'."\n".$result[1]."\n";
    print '<hr>status:'."\n".($result[2] ? $result[2] : 'ok')."\n";
    print '<hr>';
}
else
{
//ライセンスキーの認証に失敗したとき
    print '<hr>status:'."\n".'denied';
    print '<hr>';
}


    function authenticate($key)
    {
    /*
    本当はここでデータベースを参照し、ライセンスキーの照合に役立てるべきであるが、
    今回は正当なライセンスキーを「key」のみとする。
    */
        return $key=='key';
    }

    function getWho($key)
    {
    /*
    ライセンスキーから利用者のIDを割り出せる仕組みがあってもよいだろう。
    今回は簡単にするため、「unknown」を返すのみとする
    */
    //また、ユーザ名はhtmlタグを無効にすること
        return "unknown";
    }


    //time()はphpでもともと定義されている関数をそのまま利用

    function makeArgsArray()
    {
        $arr=[];
        for($i=1;isset($_POST['arg'.$i]);$i++)
        {
            array_push($arr,$_POST['arg'.$i]);
        }
        return $arr;
    }

    function run($funcName, $args)
    {
    /*
    長さ3の配列rtnを返却する。
    0番目はfuncNameの関数を実行した際の戻り値
    1番目はprintなどの操作
    2番目はエラーの有無
    */
        $rtn=[];
        $dirName="funcDef";

        if(findFunc($dirName,$funcName))
        //funcDefディレクトリにあるphpファイルに、
        //$funcNameと一致する名前のものがあった場合
        {
            include $dirName.'/'.$funcName.'.php';
            $rtn=lcrp_main($args);
        }
        else
        {
            $rtn[0]='';
            $rtn[1]='print "未定義の関数"';
            $rtn[2]='error: function "'.$funcName.'" does not exist';
        }

        return $rtn;
        
    }

        function findFunc($dir,$func)
        {
            foreach(glob($dir.'/*.php') as $file)
            {
                if(is_file($file) && $file==$dir.'/'.$func.'.php')
                {
                    return true;
                }
            }
            return false;
        }