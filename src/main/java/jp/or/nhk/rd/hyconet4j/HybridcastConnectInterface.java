package jp.or.nhk.rd.hyconet4j;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import org.json.JSONjava.JSONObject;

/**
 * HCEX Interface Class.
 * Interface Class for the HCEXCore that defined in Hybridcast-Connect Documents and other utility interfaces.
 * ハイブリッドキャストコネクト仕様の抽象APIとユーティリティ抽象API
 */
public interface HybridcastConnectInterface extends DIALInterface {


	public static final Map<String, String> restApiPath = new HashMap<String, String>() {
		{
		put("AvailableMedia",      "/media");
		put("ChannelsInfo",   "/channels");
		put("ReceiverStatus",     "/status");
		put("TaskStatus", "/hybridcast");
		put("StartAIT", "/hybridcast");
		}
	};

	public void setHCListener(HCListener hcl);

	/**
	 * checkMedia
	 * @param media the mode of broadcastMedia. | 放送メディアのモード
	 * TD: telestrial(地上波), BS: Broadcast Satelite, CS: Communication Satelite, ALL: ALL Media(TD+BS+CS+ABS+ACS+NCS)
	 * ABS: 高度BS, AVS: 高度広帯域CS, NCS: 狭帯域CSおよび高度狭帯域CS
	 *
	 * @return the result of availablity wether specified mode of media is in the list.
	 * 放送メディアモードの指定が正しいかどうかの可否判定結果
	 */
	public Boolean checkMedia(String media);

	/**
	 * デバイスタイプ（プロファイル）の取得
	 * @return デバイスタイプ名の文字列
	 */
	public String getHCEXDevType();

	//For Internal Use

	/**
	 * baseURL設定
	 * ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのリストをセットする
	 *
	 * @param url ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのprefixURL
	 * @throws Exception 例外
	 */
	public void setRestBaseURL(String url) throws Exception;

	/**
	 * websocketURL設定。
	 * ハイコネ仕様における各受信機の提供するハイコネの連携端末通信用websocketのendpointURLをセットする。
	 *
	 * @param url ハイコネ仕様における各受信機の提供するハイコネのwebsocketAPIのendpointURL
	 * @throws Exception 例外
	 */
	public void setWSURL(String url) throws Exception;

	//For Internal Use

	/**
	 * baseURL取得。
	 * ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのbaseURL(prefix)を取得。
	 *
	 * @return ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのbaseURL(prefix)
	 * @throws Exception 例外
	 */
	public String getRestBaseURL() throws Exception;

	/**
	 * WebsocketURL取得。
	 * ハイコネ仕様における各受信機の提供するハイコネの連携端末通信用websocketURLを取得。
	 *
	 * @return ハイコネ仕様における各受信機の提供するハイコネの連携端末通信用websocketURL
	 * @throws Exception 例外
	 */
	public String getWSURL() throws Exception;

	/**
	 * ハイコネプロトコル対応確認情報の取得先URL(ApplicationURL)の取得。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return Statusオブジェクトによる実行結果のレスポンス・失敗情報
	 * @throws Exception 例外
	 */
	public TVRCStatus getDialAppResourceURL(TVRCDevinfo devinfo) throws Exception;

	/**
	 * ハイコネ仕様の受信機が提供するハイコネプロトコル情報(APIEndpoint/serverinfo/version)の取得。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getDialAppInfo(TVRCDevinfo devinfo) throws Exception;

	/**
	 * WebSocketの接続。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus connWebsocket(TVRCDevinfo devinfo) throws Exception;

	/**
	 * WebSocketの切断。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus disconnWebsocket(TVRCDevinfo devinfo) throws Exception;

	/**
	 * WebSocketでのテキストメッセージ送信。
	 * 任意のメッセージフォーマットをWS経由で送信するAPI。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param text websocketで送信する任意の文字列データ
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus sendWebsocket(TVRCDevinfo devinfo, String text) throws Exception;

	/**
	 * sendTextToHostDeviceOverWS.
	 * ハイコネ仕様のメッセージフォーマットをWS経由で送信するAPI。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param sendtextStr websocketで送信するHybridcastHTMLへ送信する文字列データ
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus sendTextToHostDeviceOverWS(TVRCDevinfo devinfo, String sendtextStr) throws Exception;

	/**
	 * requerstUrlOverWS.
	 * ハイコネ仕様のメッセージフォーマットで受信機が保有するsetURLデータのリクエストメッセージをWS経由で送信するAPI。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus requestUrlOverWS(TVRCDevinfo devinfo) throws Exception;

	/**
	 * requerstUrlOverWS with Listener.
	 * ハイコネ仕様のメッセージフォーマットで受信機が保有するsetURLデータのリクエストメッセージをWS経由で送信するAPI。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param listener 送信と同時に仕掛けたいListener(HCListenerのインスタンス)
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 *
	 */
	public TVRCStatus requestUrlOverWS(TVRCDevinfo devinfo, HCListener listener)throws Exception;

	/**
	 * extensionsCommandOverWS.
	 * ハイコネ仕様のメッセージフォーマットで受信機制御のためのリクエストメッセージ（コマンド）をWS経由で送信するAPI。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param cmdstr 制御コマンド
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus extensionCommandOverWS(TVRCDevinfo devinfo, String cmdstr) throws Exception;

	/**
	 * WebSocketのListenerの追加アップデート。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param listenerName 登録するwebsocket通信の受信データ処理のためのcallbackListenerの名前
	 * @param listener 登録するListenerのHClistenerとしてのインスタンス
	 * @return 追加したListenerName
	 * @throws Exception 例外
	 */
	public String addWSListener(TVRCDevinfo devinfo, String listenerName, HCListener listener) throws Exception;

	/**
	 * WebSocketのListenerの削除。
	 *
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param listenerName 削除するwebsocket通信の受信データ処理のためのcallbackListenerの名前
	 * @return 削除したlistenerName
	 * @throws Exception 例外
	 */
	public String removeWSListener(TVRCDevinfo devinfo, String listenerName) throws Exception;

	/**
	 * WebSocketのListenerのListener名リスト
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 登録されているwebsocketのListenerNameリスト
	 * @throws Exception 例外
	 */
	public List<String> getListenerList(TVRCDevinfo devinfo) throws Exception;

	// Utility Interface

	/**
	 * ハイコネプロトコル仕様のRESTAPIのRequestBodyのエンコード処理を実装するAPI。
	 * 各デバイスとのセッションごとに処理を実装できる。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param str エンコード対象の文字列
	 * @return エンコード処理後の文字列
	 * @throws Exception 例外
	 */
	public String encodeBodyString(TVRCDevinfo devinfo, String str) throws Exception;

	/**
	 * ハイコネプロトコル仕様のRESTAPIのResponseBodyのデコード処理を実装するAPI。
	 * 各デバイスとのセッションごとに処理を実装できる。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param str エンコード対象の文字列
	 * @return エンコード処理後の文字列
	 * @throws Exception 例外
	 */
	public String decodeBodyString(TVRCDevinfo devinfo, String str) throws Exception;

	/**
	 * ハイコネプロトコル仕様の連携端末通信websocketを使って送信する文字列のエンコード処理を実装するAPI。
	 * 各デバイスとのセッションごとに処理を実装できる。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param str エンコード対象の文字列
	 * @return エンコード処理後の文字列
	 * @throws Exception 例外
	 */
	public String encodeWSBodyString(TVRCDevinfo devinfo, String str) throws Exception;

	/**
	 * ハイコネプロトコル仕様の連携端末通信websocketを使って受信した文字列のデコード処理を実装するAPI。
	 * 各デバイスとのセッションごとに処理を実装できる。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param str エンコード対象の文字列
	 * @return エンコード処理後の文字列
	 * @throws Exception 例外
	 */
	public String decodeWSBodyString(TVRCDevinfo devinfo, String str) throws Exception;

	/**
	 * ハイコネプロトコル仕様のRESTAPIのRequestHeaderを取得する処理を実装するAPI.
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 取得・処理したRequestHeaderのHashリスト
	 * @throws Exception 例外
	 */
	public Map<String, String> getRequestHeader(TVRCDevinfo devinfo) throws Exception;

	//public Hybridcast-Connect REST Interface

	/**
	 * メディア利用可否情報の取得。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getAvailableMedia(TVRCDevinfo devinfo) throws Exception;

	/**
	 * 編成チャンネル情報の取得。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param media 取得したい放送メディア（TD/BS/CS）
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getChannelInfo(TVRCDevinfo devinfo, String media) throws Exception;

	/**
	 * 受信機状態の取得。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getReceiverStatus(TVRCDevinfo devinfo) throws Exception;

	/**
	 * ハイブリッドキャスト選局・アプリケーション起動。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param mode tune: 選局、app: 選局+ハイブリッドキャスト起動
	 * @param appinfo 選局とハイブリッドキャスト起動のリソース指定のためのJSON文字列
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus startAITControlledApp(TVRCDevinfo devinfo, String mode, String appinfo) throws Exception;

	/**
	 * 起動アプリケーション可否情報の取得。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getTaskStatus(TVRCDevinfo devinfo) throws Exception;

}
