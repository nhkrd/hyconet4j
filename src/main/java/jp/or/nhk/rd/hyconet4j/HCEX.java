package jp.or.nhk.rd.hyconet4j;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import org.json.JSONjava.JSONObject;
import org.xml.sax.SAXException;

/**
 * HCEX Abscract Class.
 * Abstract Class for the interfaces that defined in Hybridcast-Connect Documents and other utility interfaces.
 * ハイブリッドキャストコネクト仕様の抽象APIとユーティリティ抽象API
 */
abstract public class HCEX  implements HybridcastConnectInterface {
	protected String devType = null;	//"HCEX" or "HCEXEmulator"
	protected JSONObject appStatus = null;
	protected String dialAppResourceURL = null;

	protected Map<String, String> restApiPath = HybridcastConnectInterface.restApiPath;

	protected HCListener hclistener = null;
	public void setHCListener(HCListener hcl) {
		hclistener = hcl;
	}

	/**
	 * checkMedia
	 * @param media the mode of broadcastMedia. | 放送メディアのモード
	 * TD: telestrial(地上波), BS: Broadcast Satelite, CS: Communication Satelite, ALL: ALL Media(TD+BS+CS)
	 * 
	 * @return the result of availability wether specified mode of media is in the list.
	 * 放送メディアモードの指定が正しいかどうかの可否判定結果
	 */
	public Boolean checkMedia(String media) {
		String[] validmedia = { "ALL", "TD", "BS", "CS" };
		return Arrays.asList(validmedia).contains( media );
	}

	/**
	 * デバイスタイプ（プロファイル）の取得
	 * @return デバイスタイプ名の文字列
	 */
	public String getHCEXDevType() {
		return devType;
	}

	//For Internal Use

	/**
	 * baseURL設定
	 * ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのリストをセットする
	 * 
	 * @param url ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのprefixURL
	 * @throws Exception 例外
	 */
	public void setRestBaseURL(String url) throws Exception{
	}

	/**
	 * websocketURL設定。
	 * ハイコネ仕様における各受信機の提供するハイコネの連携端末通信用websocketのendpointURLをセットする。
	 * 
	 * @param url ハイコネ仕様における各受信機の提供するハイコネのwebsocketAPIのendpointURL
	 * @throws Exception 例外
	 */
	public void setWSURL(String url) throws Exception{
	}

	//For Internal Use

	/**
	 * baseURL取得。
	 * ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのbaseURL(prefix)を取得。
	 * 
	 * @return ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのbaseURL(prefix)
	 * @throws Exception 例外
	 */
	public String getRestBaseURL() throws Exception{
		return "";
	}

	/**
	 * WebsocketURL取得。
	 * ハイコネ仕様における各受信機の提供するハイコネの連携端末通信用websocketURLを取得。
	 * 
	 * @return ハイコネ仕様における各受信機の提供するハイコネの連携端末通信用websocketURL
	 * @throws Exception 例外
	 */
	public String getWSURL() throws Exception{
		return "";
	}

	/**
	 * XMLSchemaValidation for DIAL Application Information response.
	 * DIAL Application Information のXMLのschema検証.
	 *
	 * @param XMLString DIAL Application Information のXML
	 * @return result
	 * @throws Exception 例外
	 * @throws SAXException schemaValidationエラー
	 */
	//public String validateDialAppinfoXML(String XMLString) throws Exception;
	public Boolean validateDialAppinfoXML(String XMLString) throws Exception, SAXException{
		// validation
		return true;
	};

	/**
	 * XMLSchemaValidation for Device Description response in Device Discovery.
	 * DIALの参照するUPnPのSSDPにおけるDevice DescriptionのXMLのschema検証.
	 *
	 * @param XMLString
	 * @return result
	 */
	//public String validateDDXML(String XMLString) throws Exception;
	//private Boolean validateDDXML(String XMLString) throws Exception{
		//validation process
	//	return true;
	//};


	/**
	 * ハイコネプロトコル対応確認情報の取得先URL(ApplicationURL)の取得とDialAppResourceURLの生成。
	 * 
	 * @return dialAppResourceURL
	 */
	@Override
	public String getDialAppResourceURL(){return "";}

	/**
	 * ハイコネプロトコル対応確認情報の取得先URL(DialAppResourceURL)をApplicationURLから生成して取得する。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return Statusオブジェクトによる実行結果のレスポンス・失敗情報
	 * @throws Exception 例外
	 */
	public TVRCStatus getDialAppResourceURL(TVRCDevinfo devinfo) throws Exception {
		TVRCStatus status = new TVRCStatus();
		status.setStatus(TVRCStatus.Status.NotImplemented.code(), "{}", "");
		return status;
	}

	/**
	 * サービスのプロトコル情報をDialAppResourceURLのレスポンスDialAppInfoXMLから取得。
	 * 
	 * @return Information of protocol
	 */
	@Override
	public String getDialAppInfo() {return "";}

	/**
	 * ハイコネ仕様の受信機が提供するハイコネプロトコル情報(APIEndpoint/serverinfo/version)をDialAppResourceURLのレスポンスDialAppInfoXMLから取得。
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getDialAppInfo(TVRCDevinfo devinfo) throws Exception {
		TVRCStatus status = new TVRCStatus();
		status.setStatus(TVRCStatus.Status.NotImplemented.code(), "{}", "");
		return status;
	}

	/**
	 * WebSocketの接続。
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus connWebsocket(TVRCDevinfo devinfo) throws Exception {
		TVRCStatus status = new TVRCStatus();
		status.setStatus(TVRCStatus.Status.NotImplemented.code(), "{}", "");
		return status;
	}

	/**
	 * WebSocketの切断。
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus disconnWebsocket(TVRCDevinfo devinfo) throws Exception {
		TVRCStatus status = new TVRCStatus();
		status.setStatus(TVRCStatus.Status.NotImplemented.code(), "{}", "");
		return status;
	}

	/**
	 * WebSocketでのテキストメッセージ送信。
	 * 任意のメッセージフォーマットをWS経由で送信するAPI。
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param text websocketで送信する任意の文字列データ
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus sendWebsocket(TVRCDevinfo devinfo, String text) throws Exception {
		TVRCStatus status = new TVRCStatus();
		status.setStatus(TVRCStatus.Status.NotImplemented.code(), "{}", "");
		return status;
	}

	/**
	 * sendTextToHostDeviceOverWS.
	 * ハイコネ仕様のメッセージフォーマットをWS経由で送信するAPI。
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param sendtextStr websocketで送信するHybridcastHTMLへ送信する文字列データ
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus sendTextToHostDeviceOverWS(TVRCDevinfo devinfo, String sendtextStr) throws Exception {
		TVRCStatus status = new TVRCStatus();
		status.setStatus(TVRCStatus.Status.NotImplemented.code(), "{}", "");
		return status;
	}

	/**
	 * requerstUrlOverWS.
	 * ハイコネ仕様のメッセージフォーマットで受信機が保有するsetURLデータのリクエストメッセージをWS経由で送信するAPI。
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus requestUrlOverWS(TVRCDevinfo devinfo) throws Exception {
		TVRCStatus status = new TVRCStatus();
		status.setStatus(TVRCStatus.Status.NotImplemented.code(), "{}", "");
		return status;
	}

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
	public TVRCStatus requestUrlOverWS(TVRCDevinfo devinfo, HCListener listener)throws Exception {
		TVRCStatus status = new TVRCStatus();
		status.setStatus(TVRCStatus.Status.NotImplemented.code(), "{}", "");
		return status;
	}

	/**
	 * extensionsCommandOverWS.
	 * ハイコネ仕様のメッセージフォーマットで受信機制御のためのリクエストメッセージ（コマンド）をWS経由で送信するAPI。
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param cmdstr 制御コマンド
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus extensionCommandOverWS(TVRCDevinfo devinfo, String cmdstr) throws Exception {
		TVRCStatus status = new TVRCStatus();
		status.setStatus(TVRCStatus.Status.NotImplemented.code(), "{}", "");
		return status;
	}

	/**
	 * WebSocketのListenerの追加アップデート。
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param listenerName 登録するwebsocket通信の受信データ処理のためのcallbackListenerの名前
	 * @param listener 登録するListenerのHClistenerとしてのインスタンス
	 * @return 追加したListenerName
	 * @throws Exception 例外
	 */
	public String addWSListener(TVRCDevinfo devinfo, String listenerName, HCListener listener) throws Exception {
		return "";
	}

	/**
	 * WebSocketのListenerの削除。
	 * 
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param listenerName 削除するwebsocket通信の受信データ処理のためのcallbackListenerの名前
	 * @return 削除したlistenerName
	 * @throws Exception 例外
	 */
	public String removeWSListener(TVRCDevinfo devinfo, String listenerName) throws Exception {
		return "";
	}

	/**
	 * WebSocketのListenerのListener名リスト
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 登録されているwebsocketのListenerNameリスト
	 * @throws Exception 例外
	 */
	public List<String> getListenerList(TVRCDevinfo devinfo) throws Exception {
		List<String> list = new ArrayList<String>();
		return list;
	}

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
	public String encodeBodyString(TVRCDevinfo devinfo, String str) throws Exception {
		String txt;
		if( devinfo.sessInfo.encodeMode.equals("normal") ) {
			txt = devinfo.sessInfo.encodeBody(str) ;	//Encode, Encrypt etc
		}else{
			txt = str;
		}
		return txt;
	}

	/**
	 * ハイコネプロトコル仕様のRESTAPIのResponseBodyのデコード処理を実装するAPI。
	 * 各デバイスとのセッションごとに処理を実装できる。
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param str エンコード対象の文字列
	 * @return エンコード処理後の文字列
	 * @throws Exception 例外
	 */
	public String decodeBodyString(TVRCDevinfo devinfo, String str) throws Exception {
		String txt;
		if( devinfo.sessInfo.decodeMode.equals("normal") ) {
			txt = devinfo.sessInfo.decodeBody(str) ;	//Decode, Decrypt etc
		}else{
			txt = str;
		}
		return txt;
	}

	/**
	 * ハイコネプロトコル仕様の連携端末通信websocketを使って送信する文字列のエンコード処理を実装するAPI。
	 * 各デバイスとのセッションごとに処理を実装できる。
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param str エンコード対象の文字列
	 * @return エンコード処理後の文字列
	 * @throws Exception 例外
	 */
	public String encodeWSBodyString(TVRCDevinfo devinfo, String str) throws Exception {
		String txt;
		if( devinfo.sessInfo.encodeMode.equals("normal") ) {
			txt = devinfo.sessInfo.encodeWSBody(str) ;	//Encode, Encrypt etc
		}else{
			txt = str;
		}
		return txt;
	}

	/**
	 * ハイコネプロトコル仕様の連携端末通信websocketを使って受信した文字列のデコード処理を実装するAPI。
	 * 各デバイスとのセッションごとに処理を実装できる。
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param str エンコード対象の文字列
	 * @return エンコード処理後の文字列
	 * @throws Exception 例外
	 */
	public String decodeWSBodyString(TVRCDevinfo devinfo, String str) throws Exception {
		String txt;
		if( devinfo.sessInfo.decodeMode.equals("normal") ) {
			txt = devinfo.sessInfo.decodeWSBody(str) ;	//Decode, Decrypt etc
		}else{
			txt = str;
		}
		return txt;
	}

	/**
	 * ハイコネプロトコル仕様のRESTAPIのRequestHeaderを取得する処理を実装するAPI.
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 取得・処理したRequestHeaderのHashリスト
	 * @throws Exception 例外
	 */
	public Map<String, String> getRequestHeader(TVRCDevinfo devinfo) throws Exception {
		return devinfo.sessInfo.getRequestHeader();
	}



	//public Hybridcast-Connect REST Interface

	/**
	 * メディア利用可否情報の取得。
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getAvailableMedia(TVRCDevinfo devinfo) throws Exception {
		TVRCStatus status = new TVRCStatus();
		status.setStatus(TVRCStatus.Status.NotImplemented.code(), "{}", "");
		return status;
	}

	/**
	 * 編成チャンネル情報の取得。
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param media 取得したい放送メディア（TD/BS/CS）
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getChannelInfo(TVRCDevinfo devinfo, String media) throws Exception {
		TVRCStatus status = new TVRCStatus();
		status.setStatus(TVRCStatus.Status.NotImplemented.code(), "{}", "");
		return status;
	}

	/**
	 * 受信機状態の取得。
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getReceiverStatus(TVRCDevinfo devinfo) throws Exception {
		TVRCStatus status = new TVRCStatus();
		status.setStatus(TVRCStatus.Status.NotImplemented.code(), "{}", "");
		return status;
	}

	/**
	 * ハイブリッドキャスト選局・アプリケーション起動。
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param mode tune: 選局、app: 選局+ハイブリッドキャスト起動
	 * @param appinfo 選局とハイブリッドキャスト起動のリソース指定のためのJSON文字列
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus startAITControlledApp(TVRCDevinfo devinfo, String mode, String appinfo) throws Exception {
		TVRCStatus status = new TVRCStatus();
		status.setStatus(TVRCStatus.Status.NotImplemented.code(), "{}", "");
		return status;
	}

	/**
	 * 起動アプリケーション可否情報の取得。
	 * 
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getTaskStatus(TVRCDevinfo devinfo) throws Exception {
		TVRCStatus status = new TVRCStatus();
		status.setStatus(TVRCStatus.Status.NotImplemented.code(), "{}", "");
		return status;
	}

}
