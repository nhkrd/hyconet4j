# APIDOCS for hyconet4j

hyconet4jが提供するAPIとそのExample。

利用が想定されるAPIの簡易的な説明のみ記載しているため、詳細についてはビルド時に生成される[javadoc](../build/docs/javadoc/index.html)を参照。
(../build/docs/javadoc/index.html)


Simple docs of the hyconet4j APIs and its example. 

For more details on APIs, see [javadoc](../build/docs/javadoc/index.html) generated in building sourcecode. (../build/docs/javadoc/index.html)


## API List

hyconet4jが提供するAPI群。利用最低限のもののみ。

Simple list of the hyconet4j APIs.

---

### constructor

| class | type | args | option | response_type | description |
| --- | --- | --- | --- | --- | --- |
| TVRCMan | instance | None | option/enable-override devinfo(TVRCDevinfo) | TVRCMan | デバイスサーチ・管理 |
| TVRCDevinfo | instance | String maker, String ipaddr, String uuid | option | TVRCDevinfo | デバイスオブジェクト |
| TVRCDevinfo | instance | String maker, String ipaddr, String uuid, String location, String applicationURL | option | TVRCDevinfo | デバイスオブジェクト |
| TVRCStatus | instance | None | option | TVRCStatus | 通信やAPIステータス |

### classmethods/properties

#### TVRCMan

| type | method | args | option | response_type | description |
| --- | --- | --- | --- | --- | --- |
| method | [search_start](#search_start) | --- | --- | --- | デバイス検索開始 |
| method | [search_stop](#search_stop) | --- | --- | --- | デバイス検索停止 |
| method | [onDeviceRegistered](#ondeviceregistered) | TVRCDevinfo | --- | --- | サーチレスポンスのcallbackListener |
| method | [getTVRCDevList](#gettvrcdevlist) | --- | --- | ListTVRCDevinfo | デバイスリスト |
| method | [getTVRCDevinfo](#gettvrcdevinfo) | int | required | TVRCDevinfo | 単体デバイス情報 |


#### TVRCDevinfo

| type | name | args | option | response_type | description |
| --- | --- | --- | --- | --- | --- |
| property | ipaddr | --- | --- | String | デバイスのIP |
| property | location | --- | --- | String | デバイスのlocation |
| property | uuid | --- | --- | String | デバイスのuuid |
| property | modelName | --- | --- | String | デバイスのモデル名 |
| property | friendlyName | --- | --- | String | デバイスの表記名 |
| property | applicationURL | --- | --- | String | DIALInfoAPIのベースURL名 |
| method | get_maker | --- | --- | String | デバイスメーカー名 |
| method | [getHCEXInfoURL](#gethcexinfourl) |--- | --- | TVRCStatus | （連携端末プロトコルv2.0）EndPoint情報取得用URLの取得|
| method | [getHCEXInfo](#gethcexinfo) |--- | --- | TVRCStatus | （連携端末プロトコルv2.0）EndPoint情報取得|
| method | [getAvailableMedia](#getavailablemedia) |--- | --- | TVRCStatus | （連携端末プロトコルv2.0）メディア可否情報取得|
| method | [getChannelInfo](#getchannelinfo) | String media| --- | TVRCStatus | （連携端末プロトコルv2.0）選局可能チャンネル一覧取得|
| method | [startAITControlledApp](#startaitcontrolledapp) | String mode, String(JSON) appinfo| ---, required | TVRCStatus | （連携端末プロトコルv2.0）HCアプリ起動要求|
| method | [getTaskStatus](#gettaskstatus) |--- | --- | TVRCStatus | （連携端末プロトコルv2.0）HCアプリ起動要求状態取得|
| method | [getReceiverStatus](#getreceiverstatus) |--- | --- | TVRCStatus | （連携端末プロトコルv2.0）受信機状態取得|
| method | [connWebsocket](#connwebsocket) |--- | --- | TVRCStatus | （連携端末プロトコルv1.0/v2.0）連携端末通信websocket接続|
| method | [disconnWebsocket](#disconnwebsocket) |--- | --- | TVRCStatus | （連携端末プロトコルv1.0/v2.0）連携端末通信websocket切断|
| method | [sendWebsocket](#sendwebsocket) | String text| --- | TVRCStatus | （連携端末プロトコルv1.0/v2.0）連携端末通信websocketメッセージ送信|
| method | [setHCListener](#sethclistener) | HCListener | required | --- | （連携端末プロトコルv1.0/v2.0）連携端末通信Websocketの受信メッセージのWS接続時のリスナー設定 |
| method | [addWSListener](#addwslistener) | String, HCListener | required | --- | （連携端末プロトコルv1.0/v2.0）連携端末通信Websocketの受信メッセージ用リスナー追加 |
| method | [removeWSListener](#removewslistener) | String | required | --- | （連携端末プロトコルv1.0/v2.0）連携端末通信Websocketの受信メッセージ用リスナー追加 |
| method | [getWSListenerList](#getwslistenerlist) | --- | --- | List\<String\> | （連携端末プロトコルv1.0/v2.0）連携端末通信Websocketの受信メッセージ用リスナー一覧 |

#### HCListener

端末連携のメッセージ受信のために事前に設定するためのインターフェースクラス

| type | name | args | option | response_type | description |
| --- | --- | --- | --- | --- | --- |
| method | [wsDataReceived](#hclistener) | String | --- | void | （連携端末）連携端末通信における受信機からのwebsocket経由によるデータ受信時の処理を実装するためのListenerAPI|
| method | [setUrlReceived](#hclistener) | String | --- | void | （連携端末）連携端末通信における受信機からのwebsocket経由によるデータ受信時にsetURLForCompanionDeviceが入っていた時の処理を実装するためのListenerAPI|
| method | [sendTextReceived](#hclistener) | String | --- | void | （連携端末）連携端末通信における受信機からのwebsocket経由によるデータ受信時にsendTextToHostDeviceが入っていた時の処理を実装するためのListenerAPI|


#### TVRCStatus

| type | name | args | option | response_type | description |
| --- | --- | --- | --- | --- | --- |
| property | status | --- | --- | int | 通信ステータスコード |
| property | body | --- | --- | String | 通信レスポンス |
| property | error | --- | --- | String | 通信時のエラーコード・メッセージ |

- StatusCodeList // TODO

| statusCode | description |
| --- | --- |
| 200 | status OK over Communication |
| 400 | status BadRequest over Communication |
| 500 | status SomeError over Communication |
| 50200 | internal status OK in SDK |
| 50400 | internal status BadRequest in SDK |
| 50500 | internal status SomeError in SDK |

---

# example

## DiscoveryAPIs

機器発見と機器選択に関するAPI群

---

### SearchCallback

機器発見の初期化と機器発見時の処理をセットする。

```java
TVRCMan tvrcman = new TVRCMan() {
    private int devcount = 0 ;
    //探索結果を逐次受け取る処理のOverride
    public void onDeviceRegistered(TVRCDevinfo devinfo) {
        System.out.print("Device Found[" + devcount++ + "]: " );
        try{
            if( devinfo.getTVRC() != null ) {
                System.out.print("  IP[" + devinfo.ipaddr + "]\t");
                System.out.print("  Maker[" + devinfo.getTVRC().get_maker() + "]\t");
                System.out.print("  ModelName[" + devinfo.modelName + "]\t");
                System.out.print("  FriendlyName[" +devinfo.friendlyName + "]");
                System.out.println("  DevType[" +devinfo.getDevType() + "]");
                System.out.print("\tLocation[" + devinfo.location + "]");
                System.out.println(" ApplicationURL[" + devinfo.applicationURL + "]");
        		System.out.print("  UUID[" + devinfo.uuid + "]");
            }
        }catch(Exception e){

        }
    }
};
```

### Search_start

```java
TVRCMan.search_start();
```
### Search_stop

```java
TVRCMan.search_stop();
```

## Deviceinfo

---

### getTVRCDevList

Get DeviceList after searching process and discover the matched devices.

```java
ListTVRCDevinfo devlist = TVRCMan.getTVRCDevList();
```

### getTVRCDevinfo

Get devinfo object selected in current session.

```java
TVRCDevinfo tvdev = TVRCMan.getTVRCDevinfo(int index);

// サーチが必要ない時は直接受信機メーカーとIP、ApplicationURLなどを指定し、applicationURLからendpoint情報を取得する
// TVRCDevinfo tvdev = TVRCDevinfo(maker, ipaddr, uuid, locationURL, applicationURL);
// tvdev.getHCEXInfoURL;  // emulator接続したい場合は必須
// tvdev.getHCEXInfo;
```

---

## DeviceSettingsAPIs

受信機デバイスの設定情報に関するAPI群

The APIs that shows device settings information of a TV Set.


---

### getAvailableMedia

利用可能メディア情報取得のためのAPI。

MediaAvailabilityAPI.
Check availability of Terrestrial/Satellite tuner on a TV set


```java
status = tvdev.getAvailableMedia();

// status.body
/*
{
    "head": {
        "code": 200,
        "message": "OK"
    },
    "body": {
        "created_at": "2018-01-01T00:00:00Z",  // 日時
        "TD": "Available",     // 地上波
        "BS": "NotAvailable",  // BS
        "CS": "NotAvailable"   // CS
    }
}
*/  
```


### getChannelInfo

編成チャンネル情報取得API。
受信機でスキャンされた選局可能な編成サービス情報一覧取得。

ChannelsInfoAPI.
Get list of channel information and tuning parameters.


```java
status = tvdev.getChannelInfo( "ALL" ); // paramsは、[ALL | TD | BS | CS]のいずれか

// status.body
/*
{
    "head": {
        "code": 200,
        "message": "OK"
    },
    "body": {
        "created_at": "2018-01-01T00:00:00Z",  // 日時
        "media": [
            {
                "type": "TD",
                "channels": [
                    {
                        "logical_channel_number": "011", // 論理チャンネル番号
                        "resource": {  // 編成サービスオブジェクト: ARIBにて割り当てられている３つのデータセット
                            "original_network_id": 32726,
                            "transport_stream_id": 32726,
                            "service_id": 1024
                        },
                        "broadcast_channel_name": "NHK総合・東京"
                    }
                ]
            },
            {
                "type": "BS",
                "channels": [
                ]
            },
            {
                "type": "CS",
                "channels": [
                ]
            }
        ]
    }
}
*/
```

---

## Launching App APIs

起動制御に関するAPI群

The APIs that launches TV tuner and tune channel and launches HTML browser.

---

### startAITControlledApp

外部起動要求API。
選局およびHCアプリ起動要求をするためのAPI。

StartAITAPI(tuneAPI).
Tunes channel of a TV set and launches Hybridcast app(HTML) on the TV set.


```java
JSONObject appInfo =
    new JSONObject()
        .put("resource", new JSONObject()  // 編成サービスリソースオブジェクト: 放送局とサービスメディアを特定
            .put("original_network_id", 32736)
            .put("transport_stream_id", 32736)
            .put("service_id", 1024))
        .put("hybridcast", new JSONObject()  // Hybridcastオブジェクト: 外部起動するHCアプリのリソース情報
            .put("orgid", 10)  // Hybridcastアプリに割り当てられたorgid
            .put("appid", 1)   // Hybridcastアプリに割り当てられたappid
            .put("aiturl", "http://127.0.0.1:8887/ait/sample.ait")  // 起動したいHCアプリのAIT(ApplicationInformationTable)のURL--AITファイルにwebアプリのURLが記述されている
        );

// 選局要求のみ | For tune only
status = tvdev.startAITControlledApp( "tune", appInfo.toString() );
// 選局+Hybridacst起動要求 | For tune and launch app
status = tvdev.startAITControlledApp( "app", appInfo.toString() );

// status.body
/*
{
    "head": {
        "code": 200,
        "message": "OK"
    },
    "body": { "taskid": "15180375" }  // 起動リクエストのタスクに付与されたID
}
*/
```

---

## DeviceStatusAPIs

受信機デバイスの状態に関するAPI群

The APIs that get the device status on a TV Set(Device).

---

### getTaskStatus

外部起動要求に対する選局・HCアプリ起動要求成否状態取得API。
外部起動要求時のレスポンスに含まれるtaskidに対する外部起動の進行状態を取得できる。

TaskStatusAPI.
Get status of startAITAPI request.


```java
status = tvdev.getTaskStatus();

// status.body
/*
{
    "head": {
        "code": 200,
        "message": "OK"
    },
    "body": {
        "taskid": "15180375",  //  外部起動要求のタスクで割り当てられたID
        "result": {
            "status": "Done",  // 外部起動要求に対する起動タスクの進行状況
            "code": 200,
            "message": "OK"
        }
    }
}
*/
```

### getReceiverStatus

受信機状態取得。
ハイブリッドキャストブラウザの起動状態、コンパニオンデバイス接続数、選局中の編成サービス情報を取得できる。

ReceiverStatusAPI.
Get status of HTML5 browser,the number of communicating apps on companion devices, and current channel.


```java
status = tvdev.getReceiverStatus();

//status.body
/*
{
    "head": {
        "code": 200,
        "message": "OK"
        },
    "body": {
        "status": {
            "hybridcast": "Running",  // Hybridcastブラウザの起動状態
            "companion_apps": 1,  // Hybridcastブラウザとの連携端末通信接続台数
            "resource": {  // 編成サービスリソースオブジェクト：現在の編成サービス
                "original_network_id": 32727,
                "transport_stream_id": 32727,
                "service_id": 1032
            }
        }
    }
}
*/
```

---

## CompanionCommunicationAPIs

連携端末通信に関するAPI群

The APIs that can communication between TV Set and another device , and can realize companion service with messages.

---


### connWebsocket

連携端末通信Websocket接続API。

websocket connetion request API.

```java
status = tvdev.connWebsocket();
```

### disconnWebsocket

連携端末通信websocket切断API。

websocket disconnection request API.

```java
status = tvdev.disconnWebsocket();
```

### sendWebsocket

連携端末通信websocketによるメッセージ送信API。

The API that Send general text message over websocket between Hybridcast HTML5 App and application outside of a tuner such as mobile app.

```java
status = tvdev.sendWebsocket("---message string---")
```

### setHCListener

連携端末通信websocket接続時の受信メッセージcallback設定API。

The API is able to set callback listener function triggered when the message over websocket has been received.

```java
tvdev.setHCListener(
    new HCListener(){
        @Override
        public void wsDataReceived(String str) {
            // ALL received data over websocket can trigger this method.
            System.out.println( "*** TestTVRC::wsDataReceived" );
            System.out.println( "wsDataReceived=" + str );
        }
        @Override
        public void setUrlReceived(String str) {
            // if required, implements some processing
        }
        @Override
        public void sendTextReceived(String str) {
            // if required, implements some processing
        }
    }
)
```

### HCListener

Hybridcastの連携端末通信websocketのListenerのためのインターフェース。
wsDataReceived(String)をoverrideしてwebsocketで受信したデータ取得しハンドリングする。
特定の文字列(sendText/setURL)を受信した際の処理は個別にsendTextReceived/setUrlReceivedへ実装することも可能。

The interface of methods for received message over websocket.

```java
new HCListener(){
    @Override
    public void wsDataReceived(String str) {
        // ALL received data over websocket can trigger this method.
        System.out.println( "*** TestTVRC::wsDataReceived" );
        System.out.println( "wsDataReceived=" + str );
    }
    @Override
    public void setUrlReceived(String str) {
        // if required, implements some processing
    }
    @Override
    public void sendTextReceived(String str) {
        // if required, implements some processing
}
```

### addWSListener

連携端末通信websocket用Listenerの追加API。Listenerの識別子とcallbackのインターフェースHCListenerを指定する。

The API that another callback listener function can be added in the list of websocket listener pool.

```java
String ListenerName = "requestURLHandler";

tvdev.addWSListener(ListenerName, new HCListener(){
    @Override
    public void wsDataReceived(String strSendtext) {
        Println( "*** Test"+ ListenerName + "::wsDataReceived");
        boolean result = jsonSchemaValidatorWS("ws_requestSetURL_schema.json", strSendtext);
        assertTrue(result, is(True));
    }
});
```

### removeWSListener

連携端末通信websocket用Listenerの削除API。削除したいListenerの識別子を指定する。

The API that specified callback listener function can be removed in the list of websocket listener pool.

```java
String ListenerName = "requestURLHandler";

dev.removeWSListener(ListenerName);
```

### getWSListenerList


連携端末通信websocket用Listenerの登録済みリストを返すAPI。

The API can get list of websocket listener that are already registered.

```java
    // WSListenerリストにすでにListenerが登録されているか確認するメソッド
	private static boolean isListenerRegisted(TVRCDevinfo dev, String listenerName) throws Exception{
		boolean isRegisted = false;
		List<String> ListenerList = dev.getListenerList();
		for( int i=0; i < ListenerList.size(); i++ ) {
			String lisName = (String)ListenerList.get(i);
			System.out.print("Listener: " + i + "  Name: " + lisName + "\n");
			if(lisName.equals(listenerName)) isRegisted = true;
		}
		return isRegisted;
    }
    
    String listenerName = "sendtextHandler";
    String isRegisted = isListenerRegisted(tvdev, listenerName);
    assrtThat(isRegisted), is(true));
```