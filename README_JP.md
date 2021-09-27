# hyconet4j

"ハイコネライブラリ" for Java

[English](./README.md) 

## 概要

hyconet4jは、2018年9月にIPTV Forum Japanで標準規格化された「ハイブリッドキャストコネクト」(以後、ハイコネ)のプロトコルを使うためのリファレンスSDK（ハイコネライブラリのJava実装）です。ハイコネを利用すると、ハイブリッドキャスト対応テレビ（受信機）の一部の機能を、連携端末のアプリケーションから制御でき、その受信機および受信機上のHybridcastサービスのブラウザアプリケーションと通信もできます。本SDKはハイコネのプロトコルテストで利用されているツールの一部をOSS化したものです。

詳しくは、[About "Hybridcast-Connect"](./HybridcastConnect.md)を参照ください。本ソフトウェアの利用に関しては、LICENSEおよびNOTICEファイルを参照ください。

![Hybridcast-Connect Overview](./docs/imgs/hybridcast-connect-overview.png)

- Reference
    - ["Hybridcast-Connect" Overview](./HybridcastConnect.md)
    - [IPTVFJ STD-0013 "ハイブリッドキャスト運用規定"](https://www.iptvforum.jp/download/input.html)
    - [W3C TPAC2018 Media&Entertainment IG "RecentAchievementOfHybridcast in TPAC2018"](https://www.w3.org/2011/webtv/wiki/images/4/45/RecentAchievementHybridcast_TPAC20181022.pdf)
    - [W3C TPAC2019 Media&Entertainment IG "RecentAchievementOfHybridcast in TPAC2019"](https://www.w3.org/2011/webtv/wiki/images/d/d1/MediaTimedEventsInHybridcast_TPAC20190916.pdf)
    - [W3C TPAC2020 Media&Entertainment IG "RecentAchievementOfHybridcast in TPAC2020"](https://www.w3.org/2011/webtv/wiki/images/2/22/RecentUpdateHybridcast_TPAC20201021_%281%29.pdf)

## 環境

- Java8-SE(JDK8-191+)互換JDK
    - OpenJDK 8+
    - Amazon Corretto 8+
- Android0S 8+ に組込ライブラリとして動作
- gradle(4.10+)でのコンパイル

## ビルド

gradleを利用してjarを生成する。

```bash
# gradleでビルド
$ ./gradlew
# buildフォルダにjarを生成
$ ls ./build/libs
>> hyconet4j-x.y.z.jar
```

## 構成

### ./sample

- Test*.java (サンプルコード)
- build.gradle (ビルド設定)
- test.sh (サンプル実行用スクリプト)
- build_run.sh (サンプルコードのgradleビルドとtest.shサンプル実行までの一括スクリプト)

### ./libs

ビルド後にライブラリ一式がコピーされるディレクトリ

- Jar package as SDK Library | ソースコードからbuildしたライブラリ本体

    - hyconet4j-x.y.z.jar

- Dependencies

    - commons-codec-1.10.jar
    - netty-buffer-4.1.23.Final.jar
    - netty-codec-4.1.23.Final.jar
    - netty-codec-http-4.1.23.Final.jar
    - netty-common-4.1.23.Final.jar
    - netty-resolver-4.1.23.Final.jar
    - netty-transport-4.1.23.Final.jar
    - cybergarage-upnp-core-2.1.1.jar (NOTICE: see [License](#license))
    - JSON-java-20170220.jar (NOTICE: see [License](#license))

### ./build

ビルド時にgradleにより自動作成される。ビルド後は不要。


### ./docs

Reference Documentsのディレクトリ。実装の例は./sampleを参照。

- [apidocs.md](./docs/apidocs.md)

    ハイコネプロトコルSDKの提供するAPIとそのexample。
    詳細はgradleによるビルド時に生成されるjavadocを参照。(build/docs/javadoc/index.html)

---

## 使用方法

### 使用例


```java
import jp.or.nhk.rd.hyconet4j.TVRCMan;
import jp.or.nhk.rd.hyconet4j.TVRCStatus;
import jp.or.nhk.rd.hyconet4j.TVRCDevinfo;

//機器サーチと発見時のコールバック処理指定
TVRCMan tvrcman = new TVRCMan() {
        int devcount = 0;
        public void onDeviceRegistered(TVRCDevinfo devinfo) {
            System.out.print("Device Found[" + devcount++ + "]: " );
        }
}
// 7秒毎の機器サーチ実行。機器が見つかったら終了。
while(true){
    tvrcman.searchStart();
    try{
        Thread.sleep(7000);
    }catch(InterruptedException e){ }
    tvrcman.searchStop();
    // 機器が見つかったらサーチ終了。
    if (tvrcman.getTVRCDevList().size() >= 1){
        break;
    }
}

// サーチした機器からデバイスのオブジェクトを取得
ListTVRCDevinfo devlist = TVRCMan.getTVRCDevList();

// 実行対象のデバイスのオブジェクトを選択
TVRCDevinfo tvdev = TVRCMan.getTVRCDevinfo(int index);


///////////////// Hybridcast-Connect APIs //////////////////////
//
//////// 外部起動API
//
// 利用可能メディア(TD:地上波、BS、CS)
status = tvdev.getAvailableMedia();
// 受信機が選局可能な地上波のチャンネル（編成チャンネル）一覧
status = tvdev.getChannelInfo("TD");

// 選局・ハイブリッドキャスト起動のためのオブジェクト作成
JSONObject appInfo =
    new JSONObject()
        .put("resource", new JSONObject()  // 編成チャンネルリソースオブジェクト: 放送局とサービスメディアを特定
            .put("original_network_id", 32736)
            .put("transport_stream_id", 32736)
            .put("service_id", 1024))
        .put("hybridcast", new JSONObject()  // Hybridcastオブジェクト: 外部起動するHCアプリのリソース情報
            .put("orgid", 16)  // Hybridcastアプリに割り当てられたorgid
            .put("appid", 1)   // Hybridcastアプリに割り当てられたappid
            .put("aiturl", "http://example.com/ait/sample.ait")  // 起動したいHCアプリのAIT(ApplicationInformationTable)のURL--AITファイルにwebアプリのURLが記述されている
        );
// 選局要求のみ
status = tvdev.startAITControlledApp( "tune", appInfo.toString() );
// 選局+Hybridacst起動要求
status = tvdev.startAITControlledApp( "app", appInfo.toString() );

// 起動要求成否取得
status = tvdev.getTaskStatus();

// 受信機状態（ブラウザ起動状態など）取得
status = tvdev.getReceiverStatus();



//////// 連携端末プロトコルAPI

// websocketメッセージ受信用Listener -- 以下のcallback用Interfaceを実装してHCListenerにセット、
tvdev.setHCListener(new HCListener() {
    @Override
    public void wsDataReceived(String str) {
        System.out.println( "*** TestTVRC::wsDataReceived" );
        System.out.println( "websocketReceivedMessage=" + str );
    }
   }
});

// 端末連携のwebsocket接続
tvdev.connWebsocket();

// websocketでのメッセージ送信
tvdev.sendWebsocket("---message string---");

// 端末連携のwebsocket接続
tvdev.disconnWebsocket();
```

この他、[APIドキュメント](./docs/apidocs.md)またはサンプルコードを参照。


---

## サンプルコード

### 実行サンプル

機器探索->機器一覧取得->機器接続（選択）->各APIのコマンドラインを使った実行ができるサンプル。

```
.
├── TestHCEX.java              -- sample source. | サンプルソースコード
├── build.gradle               -- gradle setting file. | サンプルコードをgradleでビルドするための設定
├── test.sh                    -- runner of the sample. | サンプルコードの実行

```

- run the sample as like command-line tool

コマンドラインの入力で各APIを実行できる。

    - VK_の入力は省くようにサンプルは作っている
    - 入力できるコマンドは各サンプルのソースコードをみること。"help"コマンドを入力すると簡易的なコマンドリストを参照することもできる。cmdid.eauals("VK_1")など。

```bash
# サンプル実行時のコマンドライン入力待ち状態

--)  1
（VK_1に相当するコマンド。

--) getchannels
（VK_getchannelsに相当するコマンド。
```

### サンプルの使用方法

サンプルは以下で実行できる。

```bash
$ cd sample
$ ./gradlew
$ ./test.sh
```

# ライセンス

本ソフトウェアのライセンスについては[LICENSE.txt](./LICENSE.txt)および[NOTICE.txt](./NOTICE.txt)を参照。


---

なお、本リポジトリは以下理由によりOSSパッケージを含みます。


- cybergarage-upnp-core-2.1.1.jar

    - Repository: https://github.com/cybergarage/cybergarage-upnp
    - LICENSE: https://github.com/cybergarage/cybergarage-upnp/blob/master/LICENSE.txt

    [当OSSのドキュメントに記載のMavenRepositoryのリンク](http://www.cybergarage.org:8080/maven/repo/)が不安定なため、当OSSを`maven install`して生成したjar package "cybergarage-upnp-core-2.1.1.jar"を本"hyconet4j"リポジトリは同梱しています。

- JSON-java-20170220.java

    - Repository: https://github.com/stleary/JSON-java
    - LICENSE: https://github.com/stleary/JSON-java/blob/master/LICENSE

    JSONのjava実装のスタンダートのため利用。ただし、androidで利用する場合、[当OSSのpackageName "org.json"が競合する](https://github.com/stleary/JSON-java/wiki/JSON-Java-for-Android-developers)ため、本"hyconet4j"リポジトリにおいてはandroidでの利用を想定して、packageNameを"JSON-java"へ変更し、同梱しています。
