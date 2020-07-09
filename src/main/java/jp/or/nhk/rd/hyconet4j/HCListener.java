package jp.or.nhk.rd.hyconet4j;

/**
 * HybridcastConnect仕様における連携端末プロトコルに従ったWebsocket通信のデータ受信時のcallbackListenerのInterface
 */
public interface HCListener  {
	/**
	 * websocket通信による受信した文字列の中に"setURLForCompanionDevice"プロパティが含まれている場合に発動するCallbackListener
	 * @param seturl {"control": {"setURLForCompanionDevice": SETURL_JSON_STRING}}におけるSETURL_JSON_STRINGの文字列
	 * @throws Exception 例外
	 */
	public void setUrlReceived(TVRCSetURL seturl) throws Exception;


	/**
	 * websocket通信による受信した文字列の中に"sendTextToCompanionDevice"プロパティが含まれている場合に発動するCallbackListener
	 * @param str {"message": {"sendTextToCompanionDevice": {"text": TEXTVALUE}}}におけるTEXTVALUEの文字列データ
	 * @throws Exception 例外
	 */
	public void sendTextReceived(String str) throws Exception;


	/**
	 * websocket通信により受信した文字列データのCallbackListener
	 * @param str 受信した文字列データ
	 * @throws Exception 例外
	 */
	public void wsDataReceived(String str) throws Exception;
}
