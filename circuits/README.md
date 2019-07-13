# circuits
電子回路の挙動をphpで再現する

## 動機
電子回路は複雑怪奇。  
プログラムのコードも抽象、具体を意識して書かれていないものは複雑怪奇。  
ということは、電子回路も抽象、具体を意識して書けばある程度読みやすくなるのではなかろうか。  
そんな疑問を解決すべく、あたかもプログラムであるかのように電子回路を設計するためのプラットフォームをつくりたかった。  

## 設計思想

### 物理量
プログラムでは「電流」をもっとも主要なテーマとして扱うことにする。  
そしてその電流は本来、電荷の移動により生じる物理量であるが、このプログラムではそれを無視し、単に「電圧印加により電流が流れる」と考える。  
即ち、電流の原因を電荷ではなく電圧と(まず)みなす。  
電圧は2地点間の電位の差である。  
即ち、電流の原因を「区間両端の電位」とみなす。

### 座標
座標は2次元平面とする。直交座標系で、(整数, 整数)で表される地点の位置エネルギーを指定、測定するものとする。  

### 電位と電流の関係
電位から電流が求められる時、次の情報が全て揃っていなければならない。  

- 区間の両端の電位

- 途中の抵抗値

このことから、ある地点を流れる電流を求めるための3つの条件がいえる。  

1. ある地点の電流を求めたい場合、その地点を含む区間を発見する必要がある。  
2. 区間とは、導体の連続により構成されていて、かつ両端の電位がわかっているものをいう。  
3. 区間の抵抗値は明確にわかっている必要がある。

## プログラム

### インクルード

プログラムは複雑になることが予想されるため、外部ファイルに処理の一部を書き置きする「インクルード」を用いる。  
ここでは、インクルードの照会をする  

[findSection.php](include/findSection.php) 指定された地点を含む区間を発見し、電位の高い端から低い端への順で、区間を構成する座標を返す。  
[drowConductor.php](include/drowConductor.php) 直進可能な2地点に導線を張る。3つ以上の地点を指定された場合、順に張る。  
[isIllegalIntersection.php](include/isIllegalIntersection.php) 指定された座標が不正な交点であるかどうか判断。  
不正な交点とは、予め(意味のある)交点として定めていないのに、導線が交差してしまった場合の(意味のない)交点のことである。

