# xsd2Composition(compositionMaker)

## 目的
型を決めて作文や意見文を簡単に作る。

## 実現方法
xsd (xml schema)により型を定め、xsdファイル内のコメント `<!-- -->` により命令を定め、javaのファイルでxsdファイルを読み込むとコンソール上で対話(あるいはGUI併用)しながら作文や意見文などを作っていく。

## xsdの書式に加える制限
xsdの書式は参考文献・資料<sup>[1](readme.md#ref1)</sup>




## 参考文献・資料

<span id="ref1">
[[1]XML スキーマリファレンス](http://memopad.bitter.jp/w3c/schema/schema_elements_ref.html)
著者不明だが、体系的に整理されていて、xsdのルールを網羅的に知るのに最適
</span>


[[2]XML Schema schema for XML Schemas](https://www.w3.org/2001/XMLSchema.xsd)
xsdの提供元団体であるw3cが管理するページであるため、「XML Schemaの公式ドキュメント」と同等の信頼を寄せてよいだろう。が、非常に難解。
