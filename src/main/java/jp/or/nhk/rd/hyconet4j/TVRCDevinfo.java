package jp.or.nhk.rd.hyconet4j;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.json.JSONjava.JSONObject;


import org.json.JSONjava.JSONArray;
import org.json.JSONjava.*;


import org.apache.commons.codec.binary.Base64; // Java7

import java.lang.ClassNotFoundException;
import java.lang.InstantiationException;
import java.lang.IllegalAccessException;

/**
 * Device Inforamaction Class. 
 * This class object includes the APIs by which can control and communicate with these device information between TV Set device and other client device.
 * 受信機情報オブジェクトとその情報を利用した受信機制御用APIを扱うクラス。
 */
public class TVRCDevinfo  implements Hyconet4jInterface {
	private _Logger logger = null;

	private TVRC tvrc ;
	private HCEX hcex ;
	public String createdAt;
	public String ipaddr ;
	public String clientid ;
	public String location ;
	public String applicationURL ;
	public String restBaseURL ;
	public String wsURL ;
	public String uuid ;
	public String manufacturer ;
	public String friendlyName ;
	public String modelName ;
	public SessionInfo sessInfo ;

	/**
	 * Device Inforamaction Class Constructor
	 * This class object includes the APIs by which can control and communicate with these device information between TV Set device and other client device.
	 * 受信機情報オブジェクトとその情報を利用した受信機制御用APIを扱うクラスオブジェクトのコンストラクター
	 */
	public TVRCDevinfo() {
		logger = new _Logger("TVRCDevinfo");

		this.tvrc = null;
		this.hcex = null;
		setCreatedAt();
		this.ipaddr = "";
		this.clientid = "";
		this.location = "";
		this.applicationURL = "";
		this.restBaseURL = "";
		this.wsURL = "";
		this.uuid = "";
		this.manufacturer = "";
		this.friendlyName = "";
		this.modelName = "";
		this.sessInfo = new SessionInfo();
	}

	/**
	 * Device Inforamaction Class Constructor with makerProfile, ipaddress, uuid
	 * This class object includes the APIs by which can control and communicate with these device information between TV Set device and other client device.
	 * 受信機情報オブジェクトとその情報を利用した受信機制御用APIを扱うクラスオブジェクトのコンストラクター
	 * 
	 * @param makerProfile the profile name of a maker
	 * @param ipaddr ipaddress of the device
	 * @param uuid UUID of the device
	 */
	public TVRCDevinfo(String makerProfile, String ipaddr, String uuid) {
		logger = new _Logger("TVRCDevinfo");

		setTVRCProfile( makerProfile);
		setHCEX( new ActHCEX() );
		setCreatedAt();
		setIPAddress( ipaddr ) ;
		setUUID( uuid ) ;
		
		this.location = "";
		this.applicationURL = "";
		this.restBaseURL = "";
		this.wsURL = "";
		this.manufacturer = "";
		this.friendlyName = "";
		this.modelName = "";
		this.sessInfo = new SessionInfo();
	}

	/**
	 * Device Inforamaction Class Constructor with makerProfile, ipaddress, uuid, locationURL, applicationURL
	 * This class object includes the APIs by which can control and communicate with these device information between TV Set device and other client device.
	 * 受信機情報オブジェクトとその情報を利用した受信機制御用APIを扱うクラスオブジェクトのコンストラクター
	 * 
	 * @param makerProfile the profile name of a maker
	 * @param ipaddr ipaddress of the device
	 * @param uuid UUID of the device
	 * @param locationURL the URL that the data of device information is written 
	 * @param applicationURL the URL that application information in device. see DIAL protocol
	 */
	public TVRCDevinfo(String makerProfile, String ipaddr, String uuid, String locationURL, String applicationURL) {
		logger = new _Logger("TVRCDevinfo");

		setTVRCProfile( makerProfile);
		setHCEX( new ActHCEX() );
		setCreatedAt();
		setIPAddress( ipaddr ) ;
		setUUID( uuid ) ;
		this.location = locationURL;
		this.applicationURL = applicationURL;
		this.restBaseURL = "";
		this.wsURL = "";
		this.manufacturer = "";
		this.friendlyName = "";
		this.modelName = "";
		this.sessInfo = new SessionInfo();
	}
	/**
	 * Device Inforamaction Class Constructor with makerProfile, ipaddress, uuid, locationURL, applicationURL, restBaseURL, wsURL
	 * This class object includes the APIs by which can control and communicate with these device information between TV Set device and other client device.
	 * 受信機情報オブジェクトとその情報を利用した受信機制御用APIを扱うクラスオブジェクトのコンストラクター
	 * 
	 * @param makerProfile the profile name of a maker
	 * @param ipaddr ipaddress of the device
	 * @param uuid UUID of the device
	 * @param locationURL the URL that the data of device information is written 
	 * @param applicationURL the URL that application information in device. see DIAL protocol
	 * @param restBaseURL the URL that is the prefix of the RESTAPI endpoint URLs defined in Hybridcast-Connect Standardization.
	 * @param wsURL the URL that is the websocket communication endpointURL defined in Hybridcast-Connect Standardization.
	 */
	public TVRCDevinfo(String makerProfile, String ipaddr, String uuid, String locationURL, String applicationURL, String restBaseURL,String wsURL) {
		logger = new _Logger("TVRCDevinfo");

		setTVRCProfile( makerProfile);
		setHCEX( new ActHCEX() );
		setCreatedAt();
		setIPAddress( ipaddr ) ;
		setUUID( uuid ) ;
		this.location = locationURL;
		this.applicationURL = applicationURL;
		setRestBaseURL(restBaseURL);
		setWSURL(wsURL);
		this.manufacturer = "";
		this.friendlyName = "";
		this.modelName = "";
		this.sessInfo = new SessionInfo();
	}

		/**
	 * Device Inforamaction Class Constructor from JsonString
	 * This class object includes the APIs by which can control and communicate with these device information between TV Set device and other client device.
	 * 受信機情報オブジェクトとその情報を利用した受信機制御用APIを扱うクラスオブジェクトのJsonStringのパラメタで作成するコンストラクタ
	 * 
	 * @param jsonparams the json formatted parameters that is the websocket communication endpointURL defined in Hybridcast-Connect Standardization.
	 * @throws Exception 例外
	 */
	public TVRCDevinfo(JSONObject jsonparams) throws Exception {
		logger = new _Logger("TVRCDevinfo");
		loadJsonObject(jsonparams);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	// method
	/////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 受信機情報オブジェクトとその情報の外部保存のための文字列化メソッド
	 * @return jsonstring
	 * @throws Exception 例外  
	 */
    public String export() throws Exception {
		logger = new _Logger("TVRCDevinfo String Export");
		String exportedStr = "";
		try{
			exportedStr = this.toJson().toString();	
		}catch(JSONException e){
			logger = new _Logger("TVRCDevinfo Json Import Exception Error");
		}
		return exportedStr;
	}

	/**
	 * 外部保存した受信機情報オブジェクトとその情報のJsonStringをロードしてDevinfoを生成するメソッド
	 * @param jsonstring 外部保存した受信機情報オブジェクトとその情報のJsonString
	 * @return TVRCDevinfo
	 * @throws Exception 例外  
	 */
    public TVRCDevinfo load(String jsonstring) throws Exception {
		logger = new _Logger("TVRCDevinfo importor from string as json");
		JSONObject jsonparams = new JSONObject();
		TVRCDevinfo reGenDevinfo = new TVRCDevinfo();
		try{
			jsonparams = new JSONObject(jsonstring);
			reGenDevinfo = loadJsonObject(jsonparams);
			
		}catch(JSONException e){
			logger = new _Logger("TVRCDevinfo Json Importer Exception Error");
		}
		return reGenDevinfo;
	}

		/**
	 * 外部保存した受信機情報オブジェクトとその情報のJsonStringをロードしてDevinfoを生成するメソッド
	 * @param jsonObjParams 外部保存した受信機情報オブジェクトとその情報のJsonString
	 * @return TVRCDevinfo
	 * @throws Exception 例外  
	 */
    public TVRCDevinfo loadJsonObject(JSONObject jsonObjParams) throws Exception {
		logger = new _Logger("TVRCDevinfo importor from string as json");
		TVRCDevinfo reGenDevinfo = new TVRCDevinfo();
		try{
			if(jsonObjParams.has("maker_profile")){this.setTVRCProfile(jsonObjParams.getString("maker_profile"));}
			if(jsonObjParams.has("created_at")){this.createdAt = jsonObjParams.getString("created_at");}
			if(jsonObjParams.has("ipaddr")){this.setIPAddress(jsonObjParams.getString("ipaddr"));}
			if(jsonObjParams.has("uuid")){this.setUUID(jsonObjParams.getString("uuid"));}
			if(jsonObjParams.has("friendly_name")){this.friendlyName = jsonObjParams.getString("friendly_name");}
			if(jsonObjParams.has("location_url")){this.location = jsonObjParams.getString("location_url");}
			if(jsonObjParams.has("application_url")){this.applicationURL = jsonObjParams.getString("application_url");}
			if(jsonObjParams.has("restbase_url")){this.restBaseURL = jsonObjParams.getString("restbase_url");}
			if(jsonObjParams.has("wsurl")){this.wsURL = jsonObjParams.getString("wsurl");}
			if(jsonObjParams.has("session_info")){this.sessInfo = new SessionInfo(jsonObjParams.getJSONObject("session_info"));}

			setHCEX( new ActHCEX());

			reGenDevinfo = this;
			
		}catch(JSONException e){
			logger = new _Logger("TVRCDevinfo load Json Object Importer Exception Error");
		}
		return reGenDevinfo;
	}

	/**
	 * 受信機情報オブジェクトとその情報の文字列化メソッド
	 * @return string
	 * @throws Exception 例外  
	 */
    public String toJSONString() throws Exception {
		logger = new _Logger("TVRCDevinfo toString");
		String Str = "";
		try{
			Str = this.toJson().toString();	
		}catch(JSONException e){
			logger = new _Logger("TVRCDevinfo String Exception Error");
		}
		return Str;	
	}

	/**
	 * 受信機情報オブジェクトとその情報のJSONObject化メソッド
	 * @return JSONObject
	 * @throws Exception 例外 
	 */
	public JSONObject toJson() throws Exception {
		JSONObject newjson = new JSONObject()
								.put("maker_profile", getMaker())
								.put("created_at", this.createdAt)
								.put("ipaddr", this.ipaddr)
								.put("uuid", this.uuid)
								.put("friendly_name", this.friendlyName)
								.put("location_url", this.location)
								.put("application_url", this.applicationURL)
								.put("restbase_url", this.restBaseURL)
								.put("wsurl", this.wsURL)
								.putOpt("session_info", this.sessInfo.toJson());
		return newjson;
	}

	/**
	 * Devinfo専用の受信機情報オブジェクトとその情報のJSONObject化メソッド
	 * @param jsonstring JSONObject化したい文字列
	 * @return JSONObject
	 * @throws Exception 例外 
	 */
	public JSONObject JSONObject(String jsonstring) throws Exception {
		JSONObject newjson = new JSONObject(jsonstring);
		return newjson;
	}


	@Override
	public TVRC getTVRC() {
		return this.tvrc;
	}
	@Override
	public void setTVRC( TVRC arg ) {
		this.tvrc = arg ;
	}
	@Override
	public void setHCEX( HCEX arg ) {
		this.hcex = arg ;
	}

	/**
	 * baseURL設定
	 * ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのリストをセットする
	 * 
	 * @param url ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのprefixURL
	 */
	@Override
	public void setRestBaseURL(String url){
		try{
			this.restBaseURL = url;
			hcex.setRestBaseURL(url);
		}catch(Exception e){
			logger.error( "setRestURLError: " + e);
		}
	}

	/**
	 * baseURL取得。
	 * ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのbaseURL(prefix)を取得。
	 *
 	 * @return ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのbaseURL(prefix)
	 * @throws Exception 例外 
	 */
	@Override
	public String getRestBaseURL() throws Exception {
		return this.hcex.getRestBaseURL();
	}

	/**
	 * websocketURL設定。
	 * ハイコネ仕様における各受信機の提供するハイコネの連携端末通信用websocketのendpointURLをセットする。
	 * 
	 * @param url ハイコネ仕様における各受信機の提供するハイコネのwebsocketAPIのendpointURL
	 */
	@Override
	public void setWSURL(String url){
		try{
			this.wsURL = url;
			hcex.setWSURL(url);
		}catch(Exception e){
			logger.error( "setWSURLError: " + e);
		}
	}

	/**
	 * WebsocketURL取得。
	 * ハイコネ仕様における各受信機の提供するハイコネの連携端末通信用websocketURLを取得。
	 * 
	 * @return ハイコネ仕様における各受信機の提供するハイコネの連携端末通信用websocketURL
	 * @throws Exception 例
	 */
	@Override
	public String getWSURL() throws Exception {
		return this.hcex.getWSURL();
	}

		/**
	 * 適用するデバイスプロファイル(Class)の設定
	 * 
	 * @param makerProfile デバイスプロファイル名(Class名)
	 */
	//@Override
	public void setTVRCProfile( String makerProfile) {
		String _makerProfile = makerProfile;
		try {
			// check Profile class name prefix and suffix
			if(!_makerProfile.startsWith("HCXP")) _makerProfile = "HCXP" + makerProfile;
			if(!_makerProfile.endsWith("TVRC")) _makerProfile = makerProfile + "TVRC";
			// load maker profile Class on demand
			Class DevClass = Class.forName( "jp.or.nhk.rd.hyconet4j." + _makerProfile);
			this.tvrc = (TVRC)DevClass.newInstance() ;
		}
		catch(ClassNotFoundException e) {
			logger.error( ">>>Error:: No Protocol Specified: " + e);
			logger.error( "-------Please set Maker(Protocol) below-------");
			logger.error( "HCXPGenericTVRC" );
		}
		catch(InstantiationException e) {
			logger.error( ">>>Error: " + e);
		}
		catch(IllegalAccessException e) {
			logger.error( ">>>Error: " + e);
		}
	}

	/**
	 * 適用するデバイスプロファイル(Class)の設定
	 * 
	 * @param makerProfile デバイスプロファイル名(Class名)
	 */
	@Deprecated
	@Override
	public void setTVRCMaker( String makerProfile) {
		String _makerProfile = makerProfile;
		try {
			// check Profile class name prefix and suffix
			if(!_makerProfile.startsWith("HCXP")) _makerProfile = "HCXP" + makerProfile;
			if(!_makerProfile.endsWith("TVRC")) _makerProfile = makerProfile + "TVRC";
			// load maker profile Class on demand
			Class DevClass = Class.forName( "jp.or.nhk.rd.hyconet4j." + _makerProfile);
			this.tvrc = (TVRC)DevClass.newInstance() ;
		}
		catch(ClassNotFoundException e) {
			logger.error( ">>>Error:: No Protocol Specified: " + e);
			logger.error( "-------Please set Maker(Protocol) below-------");
			logger.error( "HCXPGenericTVRC" );
		}
		catch(InstantiationException e) {
			logger.error( ">>>Error: " + e);
		}
		catch(IllegalAccessException e) {
			logger.error( ">>>Error: " + e);
		}
	}


	public void setCreatedAt() {
		this.createdAt = String.valueOf((new Date()).getTime());
	}


	@Override
	public void setIPAddress( String arg ) {
		this.ipaddr = arg ;
		this.clientid = arg + ":" + (new Date()).getTime();
	}
	@Override
	public void setUUID( String arg ) {
		this.uuid = arg ;
	}

	@Override
	public void setClientId( String arg ) {
		this.clientid = arg ;
	}

	@Override
	public void setHCListener(HCListener hcl) {
		hcex.setHCListener(hcl);
	}
	@Override
	public String getDevType() {
		String devtype = hcex.getHCEXDevType();
		if( devtype == null ) {
			devtype = "TVRC";
		}
		return devtype;
	}

	//
	// TVRC Wrapping
	//
	/**
	 * デバイスプロファイルからのmanufacture名取得
	 * 
	 * @return manufacture名文字列
	 */
	//@Override
	public String getMaker() {
		return tvrc.get_maker();
	}

	/**
	 * 適用されているデバイスプロファイルのUSN取得
	 * @return USN文字列
	 */
	//@Override
	public String getServiceUrn() {
		return tvrc.get_service_urn();
	}

	/**
	 * 適用されているデバイスプロファイルのURN取得
	 * 
	 * @return URN文字列
	 */
	//@Override
	public String getDeviceUrn() {
		return tvrc.get_device_urn();
	}

		/**
	 * デバイスプロファイルからのmanufacture名取得
	 * 
	 * @return manufacture名文字列
	 */
	@Deprecated
	public String get_maker() {
		return tvrc.get_maker();
	}

	/**
	 * 適用されているデバイスプロファイルのUSN取得
	 * @return USN文字列
	 */
	@Deprecated
	@Override
	public String get_service_urn() {
		return tvrc.get_service_urn();
	}

	/**
	 * 適用されているデバイスプロファイルのURN取得
	 * 
	 * @return URN文字列
	 */
	@Deprecated
	@Override
	public String get_device_urn() {
		return tvrc.get_device_urn();
	}

	//
	// HCEX Exposed API  
	//

	/**
	 * ハイコネプロトコル対応確認情報の取得先URL(ApplicationURL)の取得。
	 * 
	 * @return Statusオブジェクトによる実行結果のレスポンス・失敗情報
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus getDialAppResourceURL() throws Exception {
		return hcex.getDialAppResourceURL(this);
	}

	/**
	 * ハイコネ仕様の受信機が提供するハイコネプロトコル情報(APIEndpoint/serverinfo/version)の取得。
	 * 
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus getDialAppInfo() throws Exception {
		return hcex.getDialAppInfo(this);
	}

	/**
	 * WebSocketの接続。
	 * 
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus connWebsocket() throws Exception {
		return hcex.connWebsocket(this);
	}

	/**
	 * WebSocketの切断。
	 * 
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus disconnWebsocket() throws Exception {
		return hcex.disconnWebsocket(this);
	}

	/**
	 * WebSocketでのテキストメッセージ送信。任意のメッセージフォーマットをWS経由で送信するAPI。
	 * 
	 * @param text websocketで送信する任意の文字列データ
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus sendWebsocket(String text) throws Exception {
		return hcex.sendWebsocket(this, text);
	}

	/**
	 * sendTextToHostDeviceOverWS.
	 * ハイコネ仕様のメッセージフォーマットをWS経由で送信するAPI。
	 * 
	 * @param sendtextStr websocketで送信するHybridcastHTMLへ送信する文字列データ
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus sendTextToHostDeviceOverWS(String sendtextStr) throws Exception {
		return hcex.sendTextToHostDeviceOverWS(this, sendtextStr);
	}

	/**
	 * requerstUrlOverWS.
	 * ハイコネ仕様のメッセージフォーマットで受信機が保有するsetURLデータのリクエストメッセージをWS経由で送信するAPI。
	 * 
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus requestUrlOverWS() throws Exception {
		return hcex.requestUrlOverWS(this);
	}

	/**
	 * requerstUrlOverWS with Listener.
	 * ハイコネ仕様のメッセージフォーマットで受信機が保有するsetURLデータのリクエストメッセージをWS経由で送信するAPI。
	 * 
	 * @param listener 送信と同時に仕掛けたいListener(HCListenerのインスタンス)
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 *
	 */
	@Override
	public TVRCStatus requestUrlOverWS(HCListener listener) throws Exception {
		return hcex.requestUrlOverWS(this,listener);
	}

	/**
	 * extensionsCommandOverWS.
	 * ハイコネ仕様のメッセージフォーマットで受信機制御のためのリクエストメッセージ（コマンド）をWS経由で送信するAPI。
	 * 
	 * @param cmdstr 制御コマンドのJSON文字列
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus extensionCommandOverWS(String cmdstr) throws Exception {
		return hcex.extensionCommandOverWS(this, cmdstr);
	}

	/**
	 * WebSocketのListenerの追加アップデート。
	 * 
	 * @param listenerName 登録するwebsocket通信の受信データ処理のためのcallbackListenerの名前
	 * @param listener 登録するListenerのHClistenerとしてのインスタンス
	 * @return 追加が完了したListenerName
	 * @throws Exception 例外
	 */
	@Override
	public String addWSListener(String listenerName, HCListener listener) throws Exception {
		return hcex.addWSListener(this, listenerName, listener);
	}

	/**
	 * WebSocketのListenerの削除。
	 * 
	 * @param listenerName 削除するwebsocket通信の受信データ処理のためのcallbackListenerの名前
	 * @return 削除したlistenerName
	 * @throws Exception 例外
	 */
	@Override
	public String removeWSListener(String listenerName) throws Exception {
		return hcex.removeWSListener(this, listenerName);
	}

	/**
	 * WebSocketのListenerのListener名リスト
	 * 
	 * @return 登録されているwebsocketのListenerNameのリスト
	 * @throws Exception 例外
	 */
	@Override
	public List<String> getListenerList() throws Exception {
		return hcex.getListenerList(this);
	}


	//
	//public Expose Hybridcast-Connect V2 APIs

	/**
	 * メディア利用可否情報の取得。
	 * 
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus getAvailableMedia() throws Exception {
		return hcex.getAvailableMedia(this);
	}

	/**
	 * 編成チャンネル情報の取得。
	 * 
	 * @param media 取得したい放送メディア（TD/BS/CS）
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus getChannelInfo(String media) throws Exception {
		return hcex.getChannelInfo(this, media);
	}

	/**
	 * 受信機状態の取得。
	 * 
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus getReceiverStatus() throws Exception {
		return hcex.getReceiverStatus(this);
	}
	
	/**
	 * ハイブリッドキャスト選局・アプリケーション起動。
	 * 
	 * @param mode tune: 選局、app: 選局+ハイブリッドキャスト起動
	 * @param appinfo 選局とハイブリッドキャスト起動のリソース指定のためのJSON文字列
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus startAITControlledApp(String mode, String appinfo) throws Exception {
		return hcex.startAITControlledApp(this, mode, appinfo);
	}

	/**
	 * 起動アプリケーション可否情報の取得。
	 * 
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus getTaskStatus() throws Exception {
		return hcex.getTaskStatus(this);
	}

	// Hybridcast-Connect Protocol Session ///////////////////////////////////////////////

	/**
	 * Session information Class in deviceinfo object.
	 * デバイスごとのセッション情報クラス。
	 */
	public class SessionInfo {
		public String appName;			// Application Name as description on the Display
		public String devID;			// deviceID：the ID is expected to assigned by TVReceiver for identifying each device
		public String encodeMode;		// expected to use in methods : encodeBody/decodeBody/encodeWSBody/decodeWSBody
		public String decodeMode;       // expected to use in methods : encodeBody/decodeBody/encodeWSBody/decodeWSBody

		public Map<String, String> headers;

		/**
		 * Session information Object Constructor
		 * デバイスごとのセッション情報オブジェクトのコンストラクタ
		 */
		public SessionInfo() {
			logger = new _Logger("sessInfo");
			appName = "";
			devID = "";
			encodeMode = "normal";
			decodeMode = "normal";
			headers = new HashMap<String, String>();
		}

		/**
		 * Session information Object Constructor from JSONObject
		 * デバイスごとのセッション情報オブジェクトをJSONObjectのパラメタから生成するコンストラクタ
		 * @param jsonparams sessionInfoParams
		 */
		public SessionInfo(JSONObject jsonparams) {
			logger = new _Logger("sessInfo");
			appName = "";
			devID = "";
			encodeMode = "normal";
			decodeMode = "normal";
			headers = new HashMap<String, String>();
				if(jsonparams.has("app_name")){this.appName = jsonparams.getString("app_name");}
				if(jsonparams.has("devid")){this.devID = jsonparams.getString("devid");}
		}


		/**
		 * Session information Object Stringfier
		 * デバイスごとのセッション情報オブジェクトを保持するための文字列化メソッド
		 * @return json文字列化されたsessionInfo
		 * @throws Exception 例外
		 */
		public String toJSONString() throws Exception {
			return this.toJson().toString();
		}

		/**
		 * Session information Object jsonify
		 * デバイスごとのセッション情報オブジェクトのjsonオブジェクト化メソッド
		 * @return jsonオブジェクト化されたsessionInfo
		 * @throws Exception 例外
		 */
		public JSONObject toJson() throws Exception {
			JSONObject newjson = new JSONObject()
									.put("app_name", this.appName)
									.put("devid", this.devID)
									.put("headers", this.headers);
			return newjson;
		}

		/**
		 * ハイコネプロトコル仕様のRESTAPIのRequestBodyのエンコード処理を実装するAPI。
		 * 
		 * @param str エンコード対象の文字列
		 * @return エンコード処理後の文字列
		 * @throws Exception 例外
		 */
		public String encodeBody(String str) throws Exception {
			// if needed, encode or encrypt in this method for each app(devid)
			return str;
		}

		/**
		 * ハイコネプロトコル仕様のRESTAPIのResponseBodyのデコード処理を実装するAPI。
		 * 
		 * @param str エンコード対象の文字列
		 * @return エンコード処理後の文字列
		 * @throws Exception 例外
		 */
		public String decodeBody(String str) throws Exception {
			// if needed, decode or decrypt in this method for each app(devid)
			return str;
		}

		/**
		 * ハイコネプロトコル仕様の連携端末通信websocketを使って送信する文字列のエンコード処理を実装するAPI。
		 * 
		 * @param str エンコード対象の文字列
		 * @return エンコード処理後の文字列
		 * @throws Exception 例外
		 */
		public String encodeWSBody(String str) throws Exception {
			// if needed, decode or decrypt in this method for each app(devid)
			return str;
		}

		/**
		 * ハイコネプロトコル仕様の連携端末通信websocketを使って受信した文字列のデコード処理を実装するAPI。
		 * 
		 * @param str エンコード対象の文字列
		 * @return エンコード処理後の文字列
		 * @throws Exception 例外
		 */
		public String decodeWSBody(String str) throws Exception {
			// if needed, decode or decrypt in this method for each app(devid)
			return str;
		}

		/**
		 * ハイコネプロトコル仕様のRESTAPIのRequestHeaderの設定処理を実装するAPI.
		 * 
		 * @param headerParams RequestHeaderのHashリスト
		 * @throws Exception 例外
		 */
		public void setRequestHeader(Map<String,String> headerParams) throws Exception {
			this.headers = headerParams;
		}

		/**
		 * ハイコネプロトコル仕様のRESTAPIのRequestHeaderを取得する処理を実装するAPI.
		 * 
		 * @return 取得・処理したRequestHeaderのHashリスト
		 * @throws Exception 例外
		 */
		public Map<String, String> getRequestHeader() throws Exception {
			return this.headers;
		}
	}
}
