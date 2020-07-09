package jp.or.nhk.rd.hyconet4j;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


import java.lang.ClassNotFoundException;
import java.lang.InstantiationException;
import java.lang.IllegalAccessException;

/**
 * Hyconet4j APIs, that are expected to be exposed Interfaces for Device Inforamaction/Control.
 * 受信機情報オブジェクトとその情報を利用した受信機制御用のInterfaceを扱うクラス。
 */
public interface Hyconet4jInterface  {

	/**
	 * TVRC(Device/MakerProfile)の取得
	 * 
	 * 
	 * @return TVRC Device/MakerProfileに相当するクラスオブジェクト
	 */
	public TVRC getTVRC();

	/**
	 * TVRC(Device/MakerProfile)の指定
	 * @param arg TVRC Device/MakerProfile Object
	 */
	public void setTVRC( TVRC arg );

	/**
	 * HybridcastConnect実装クラスの指定
	 * @param arg HybridcastConnectの実装クラスのインスタンスObject
	 */
	public void setHCEX( HCEX arg );

	/**
	 * baseURL設定
	 * ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのリストをセットする
	 * 
	 * @param url ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのprefixURL
	 */
	public void setRestBaseURL(String url);

	/**
	 * baseURL取得。
	 * ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのbaseURL(prefix)を取得。
	 *
 	 * @return ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのbaseURL(prefix)
	 * @throws Exception 例外 
	 */
	public String getRestBaseURL() throws Exception;

	/**
	 * websocketURL設定。
	 * ハイコネ仕様における各受信機の提供するハイコネの連携端末通信用websocketのendpointURLをセットする。
	 * 
	 * @param url ハイコネ仕様における各受信機の提供するハイコネのwebsocketAPIのendpointURL
	 */
	public void setWSURL(String url);

	/**
	 * WebsocketURL取得。
	 * ハイコネ仕様における各受信機の提供するハイコネの連携端末通信用websocketURLを取得。
	 * 
	 * @return ハイコネ仕様における各受信機の提供するハイコネの連携端末通信用websocketURL
	 * @throws Exception 例
	 */
	public String getWSURL() throws Exception ;

	/**
	 * 適用するデバイスプロファイル(Class)の設定
	 * 
	 * @param maker デバイスプロファイル名(Class名)
	 */
	public void setTVRCMaker( String maker ) ;

	public void setIPAddress( String arg ) ;
	public void setUUID( String arg ) ;

	public void setClientId( String arg ) ;

	public void setHCListener(HCListener hcl);

	public String getDevType();

	//
	// TVRC Wrapping
	//
	/**
	 * デバイスプロファイルからのmanufacture名取得
	 * 
	 * @return manufacture名文字列
	 */
	public String getMaker();

	/**
	 * 適用されているデバイスプロファイルのUSN取得
	 * @return USN文字列
	 */
	public String get_service_urn();

	/**
	 * 適用されているデバイスプロファイルのURN取得
	 * 
	 * @return URN文字列
	 */
	public String get_device_urn() ;

	//
	// HCEX Exposed API  
	//

	/**
	 * ハイコネプロトコル対応確認情報の取得先URL(ApplicationURL)の取得。
	 * 
	 * @return Statusオブジェクトによる実行結果のレスポンス・失敗情報
	 * @throws Exception 例外
	 */
	public TVRCStatus getDialAppResourceURL() throws Exception ;

	/**
	 * ハイコネ仕様の受信機が提供するハイコネプロトコル情報(APIEndpoint/serverinfo/version)の取得。
	 * 
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getDialAppInfo() throws Exception ;

	/**
	 * WebSocketの接続。
	 * 
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus connWebsocket() throws Exception ;

	/**
	 * WebSocketの切断。
	 * 
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus disconnWebsocket() throws Exception ;

	/**
	 * WebSocketでのテキストメッセージ送信。任意のメッセージフォーマットをWS経由で送信するAPI。
	 * 
	 * @param text websocketで送信する任意の文字列データ
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus sendWebsocket(String text) throws Exception ;

	/**
	 * sendTextToHostDeviceOverWS.
	 * ハイコネ仕様のメッセージフォーマットをWS経由で送信するAPI。
	 * 
	 * @param sendtextStr websocketで送信するHybridcastHTMLへ送信する文字列データ
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus sendTextToHostDeviceOverWS(String sendtextStr) throws Exception ;

	/**
	 * requerstUrlOverWS.
	 * ハイコネ仕様のメッセージフォーマットで受信機が保有するsetURLデータのリクエストメッセージをWS経由で送信するAPI。
	 * 
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus requestUrlOverWS() throws Exception ;

	/**
	 * requerstUrlOverWS with Listener.
	 * ハイコネ仕様のメッセージフォーマットで受信機が保有するsetURLデータのリクエストメッセージをWS経由で送信するAPI。
	 * 
	 * @param listener 送信と同時に仕掛けたいListener(HCListenerのインスタンス)
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 *
	 */
	public TVRCStatus requestUrlOverWS(HCListener listener) throws Exception ;

	/**
	 * extensionsCommandOverWS.
	 * ハイコネ仕様のメッセージフォーマットで受信機制御のためのリクエストメッセージ（コマンド）をWS経由で送信するAPI。
	 * 
	 * @param cmdstr 制御コマンドのJSON文字列
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus extensionCommandOverWS(String cmdstr) throws Exception ;

	/**
	 * WebSocketのListenerの追加アップデート。
	 * 
	 * @param listenerName 登録するwebsocket通信の受信データ処理のためのcallbackListenerの名前
	 * @param listener 登録するListenerのHClistenerとしてのインスタンス
	 * @return 追加が完了したListenerName
	 * @throws Exception 例外
	 */
	public String addWSListener(String listenerName, HCListener listener) throws Exception ;

	/**
	 * WebSocketのListenerの削除。
	 * 
	 * @param listenerName 削除するwebsocket通信の受信データ処理のためのcallbackListenerの名前
	 * @return 削除したlistenerName
	 * @throws Exception 例外
	 */
	public String removeWSListener(String listenerName) throws Exception ;

	/**
	 * WebSocketのListenerのListener名リスト
	 * 
	 * @return 登録されているwebsocketのListenerNameのリスト
	 * @throws Exception 例外
	 */
	public List<String> getListenerList() throws Exception ;


	//Public Expose Hybridcast-Connect V2 APIs

	/**
	 * メディア利用可否情報の取得。
	 * 
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getAvailableMedia() throws Exception ;

	/**
	 * 編成チャンネル情報の取得。
	 * 
	 * @param media 取得したい放送メディア（TD/BS/CS）
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getChannelInfo(String media) throws Exception ;

	/**
	 * 受信機状態の取得。
	 * 
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getReceiverStatus() throws Exception ;
	
	/**
	 * ハイブリッドキャスト選局・アプリケーション起動。
	 * 
	 * @param mode tune: 選局、app: 選局+ハイブリッドキャスト起動
	 * @param appinfo 選局とハイブリッドキャスト起動のリソース指定のためのJSON文字列
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus startAITControlledApp(String mode, String appinfo) throws Exception ;

	/**
	 * 起動アプリケーション可否情報の取得。
	 * 
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getTaskStatus() throws Exception ;

}
