package jp.or.nhk.rd.hyconet4j;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.ConnectException;
import java.net.SocketException;
import java.lang.NullPointerException;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONjava.JSONObject;
import org.json.JSONjava.JSONArray;
import org.json.JSONjava.XML;
import org.json.JSONjava.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;

import java.net.URI;

import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * Actual HCEX -- implementation of HCEX abstract class.
 * 抽象クラスであるHCEXの実装クラス。
 */
public class ActHCEX extends HCEX {
	private _Logger logger = null;

	public class wsSendListener implements WebSocketClient.FrameListener {
		@Override
		public void onFrame(WebSocketFrame frame) {
			//logger.debug("wsSendListener::onFrame():" + ((TextWebSocketFrame) frame).text());
		}
		@Override
		public void onError(Throwable t) {
			//logger.debug("wsSendListener::onError()");
		}
	}

	private Map<String, String> restApiEndpoints = new HashMap<String, String>() ;

	private WebSocketClient wsc = null;

	private static final String typeHyconetProtocol = "HCC";
	private static final String typeHyconetProtocolAntwapp = "HCC(Antwapp)";

	/**
	 * ハイコネプロトコル仕様のRESTAPIのRequestBodyのエンコード処理を実装するAPI。
	 * 各デバイスとのセッションごとに処理を実装できる。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param str エンコード対象の文字列
	 * @return エンコード処理後の文字列
	 * @throws Exception 例外
	 */
	@Override
	public String encodeBodyString(TVRCDevinfo devinfo, String str) throws Exception {
		// if needed, change processing of encoding text string when sending message to TV
		return str;
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
	@Override
	public String decodeBodyString(TVRCDevinfo devinfo, String str) throws Exception {
		// if needed, change processing of encoding text string when receiving message from TV
		return str;
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
	@Override
	public String encodeWSBodyString(TVRCDevinfo devinfo, String str) throws Exception, RuntimeException {
		// if needed, change processing of encoding text string when sending message to TV
		return str;
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
	@Override
	public String decodeWSBodyString(TVRCDevinfo devinfo, String str) throws Exception, RuntimeException {
		// if needed, change processing of encoding text string when receiving message from TV
		return str;
	}

	/**
	 * ハイコネプロトコル仕様のRESTAPIのRequestHeaderを取得する処理を実装するAPI。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 取得・処理したRequestHeaderのHashリスト
	 * @throws Exception 例外
	 */
	@Override
	public Map<String, String> getRequestHeader(TVRCDevinfo devinfo) throws Exception {
		return devinfo.sessInfo.getRequestHeader();
	}


	/**
	 * ActHCEX Constructor
	 * Create instance of ActHCEX class, and initialize settings in the instance.
	 * ActHCEXクラスのインスタンスの生成。インスタンス内の設定の初期化。
	 */
	public ActHCEX() {
		logger = new _Logger("ActHCEX");
	}

	/**
	 * Generic HTTPRequest for hyconet4j.
	 * ハイコネRestAPIで利用する汎用的なHTTPRequestAPI
	 *
	 * @param method method name in HTTP
	 * @param devinfo デバイス情報オブジェクト
	 * @param url request対象のURL
	 * @param postData POST methodの時のBodyString
	 * @return Statusオブジェクトによる実行結果のレスポンス・失敗情報
	 * @throws Exception 例外
	 */
	private TVRCStatus HTTPRequest(String method, TVRCDevinfo devinfo, String url, String postData ) throws Exception {
		TVRCStatus status = new TVRCStatus();
		status.setStatus(TVRCStatus.Status.OK.code(), "", "");
		Map<String, String> headers = new HashMap<String, String>();

		if( Objects.nonNull(devinfo) && !devinfo.ipaddr.equals("") ) {
			try {
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();

				//add params in request header for each request
				con.setRequestMethod( method );
				headers = getRequestHeader(devinfo);
				if(Objects.nonNull(headers)){
					for(Map.Entry<String, String> entry : headers.entrySet()) {
						con.setRequestProperty(entry.getKey(), entry.getValue());
					}
				}

				if( method.equals( "GET" ) ) {
				}
				else if( method.equals( "POST" ) || method.equals( "PUT" ) ) {
					con.setDoOutput(true);
					con.setRequestProperty("Content-Type", "application/json");

					DataOutputStream wr = new DataOutputStream(con.getOutputStream());
					byte[] bytes = postData.getBytes();
					for (int i=0;i<bytes.length;i++){
						wr.writeByte(bytes[i]);
					}
logger.debug("finalRequestBody: " + new String(bytes, "UTF-8"));

					wr.flush();
					wr.close();
				}

				int responseCode = con.getResponseCode();
				String err = "";
				StringBuffer response = new StringBuffer();
				if(  responseCode >= 200 && responseCode < 400) {
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();

					String res = response.toString();
logger.debug("Raw 200 Res: " + res + " url: " + url);
					// for each device and each session, change decode processing.
					res = decodeBodyString(devinfo, res);
					status.setStatus( responseCode, res, err);
				}
				else { // 200/201以外の時もbodyがあれば返す。getErrorStreamになることに注意
					InputStream errsin = con.getErrorStream();
					if( errsin != null ) {
						BufferedReader in = new BufferedReader(new InputStreamReader(errsin));
						if( in != null ) {
							String inputLine;
							while ((inputLine = in.readLine()) != null) {
								response.append(inputLine);
							}
							in.close();

							String res = response.toString();
logger.debug("Raw Err Res: " + res + " url: " + url);
							// for each device and each session, change decode processing.
							res = decodeBodyString(devinfo, res);
							status.setStatus(responseCode, res, String.format("%s", con.getResponseMessage()) );
						}
						else {
							status.setStatus(responseCode, "", "" );
						}
					}
				}
			}
			catch(ConnectException e) {
				status.setStatus(TVRCStatus.Status.ServiceUnavailable.code(), "", "Internal Connection Error: "+ e);
			}catch(SocketException e) {
				status.setStatus(TVRCStatus.Status.ServiceUnavailable.code(), "", "Internal Connection Error: "+ e);
			}
		}
		else {
			// paramsCheck internally => internal badrequest 50400
			status.setStatus(TVRCStatus.Status.BadRequestInternalProcessing.code(), "", "Internal Parameter Error: No devinfo or No ipaddress specified");
		}

		return status;
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
	@Override
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
	//@Override
	//private Boolean validateDDXML(String XMLString) throws Exception{
	//	//validation process
	//	return true;
	//};

	/**
	 * ハイコネプロトコル対応確認情報の取得先URL(DialAppResourceURL)をApplicationURLから生成して取得する。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return Statusオブジェクトによる実行結果のレスポンス・失敗情報
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus getDialAppResourceURL(TVRCDevinfo devinfo) throws Exception {
		TVRCStatus status = new TVRCStatus();

		if(!devinfo.getMaker().toUpperCase().equals("HCXPGenericTVRC")){
//			devType = devinfo.getMaker().toUpperCase(); // メーカープロトコルclassのmaker_name
		}

		// dev.applicationURL(baseurl)あれば、DIAL対応による発見とみなして、
		///dialAppResourceURLの変数にHCEXProtocolのdialAppResourceURLを格納
		if(Objects.nonNull(devinfo.applicationURL) && !devinfo.applicationURL.equals("")) {
			if(devinfo.applicationURL.endsWith("/")){ // URLの末尾が/の場合
				dialAppResourceURL = devinfo.applicationURL + DIALInterface.applicationName ;
			}else{
				dialAppResourceURL = devinfo.applicationURL + "/" + DIALInterface.applicationName;
			}
//			devType = devType + "/HCEXProtocol" ;
			devType = DIALInterface.devType + "/" + typeHyconetProtocol;

			// success connection to antwapp emulator => 200OK
			status.setStatus(TVRCStatus.Status.OK.code(), dialAppResourceURL, "") ;
		}

		//Antwapp Emulator確認と追加設定
		// antwappは"ipaddr:8887/apps/antwapp"を叩けば動作のステータス確認ができるため。
		// 直接たたいて200 OKでなければ、devTypeはTVRCに戻す
		String emuAppStatusURL = "http://" + devinfo.ipaddr + ":8887/apps/antwapp" ;
		TVRCStatus status_emu = this.HTTPRequest( "GET", devinfo, emuAppStatusURL, null ) ;
		if( status_emu.status == TVRCStatus.Status.OK.code() ) {
//			devType = devType + "/HCEXEmulator:Antwapp" ;
			devType = DIALInterface.devType + "/" + typeHyconetProtocolAntwapp;
			dialAppResourceURL = emuAppStatusURL;
			devinfo.applicationURL = emuAppStatusURL;
			// success connection to antwapp emulator => 200OK
			status.setStatus(TVRCStatus.Status.OK.code(), emuAppStatusURL, "") ;
		}
		else{
			//ignore
		}

//logger.debug("dialAppResourceURL:" + dialAppResourceURL );
		// No applicationURL result to No support for hybridcast connect.
		if( dialAppResourceURL == null ) {
			// paramsCheck internally => internal badrequest 50400
			status.setStatus(TVRCStatus.Status.BadRequestInternalProcessing.code(), "", "ApplicationURL in XML of DIALProtocol Not Found, and no support for HybridcastConnect");
			devType = DIALInterface.devType;
		}

		return status;
	}



	/**
	 * ハイコネ仕様の受信機が提供するハイコネプロトコル情報(APIEndpoint/serverinfo/version)をDialAppResourceURLのレスポンスDialAppInfoXMLから取得。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getDialAppInfo(TVRCDevinfo devinfo) throws Exception {
		TVRCStatus status = new TVRCStatus();
		Map<String,String> baseEndPoints = new HashMap<String,String>();

		if( devinfo.ipaddr.equals("") ) {
			// paramsCheck internally => internal badrequest 50400
			status.setStatus(TVRCStatus.Status.BadRequestInternalProcessing.code(), "", "Parameter Error: No IPAddress specified");
		}
		else if( dialAppResourceURL == null ) {
			// paramsCheck internally => internal badrequest 50400
			status.setStatus(TVRCStatus.Status.BadRequestInternalProcessing.code(), "", "ApplicationURL in DIALProtocol Not Found");
			devType = DIALInterface.devType;
		}
		else { // HCEXProtocolのDIAL InfoAPIによるデバイス情報存在確認
			logger.debug("ApplicationResourceURL request: " + dialAppResourceURL);
			status = this.HTTPRequest( "GET", devinfo, dialAppResourceURL, null ) ;

			if( status.status == TVRCStatus.Status.OK.code() ) {
				try{
					if(validateDialAppinfoXML(status.body)){
////					baseEndPoints = parseDialAppinfoXML(status.body); // getEndpoints from dialAppInfoXML
						baseEndPoints = parseDialAppinfoXMLXpath(status.body); // getEndpoints from dialAppInfoXML
						if( baseEndPoints != null ) {
							if( baseEndPoints.get("HYBRIDCAST_PROTOCOL_VERSION") != null ) {//after Ver2.1
								this.hybridcastProtocolVersion = baseEndPoints.get("HYBRIDCAST_PROTOCOL_VERSION");
								this.serverInfo = baseEndPoints.get("SERVER_INFO");
								this.serverInfoProtocolVersion = this.hybridcastProtocolVersion;
								setRestBaseURL(baseEndPoints.get("BASEURL"));
								setWSURL(baseEndPoints.get("WSURL"));
								this.allowBroadcastOrientedManagedApp = (baseEndPoints.get("allowBroadcastOrientedManagedApp").equals("true"))? true : false ;
								this.allowBroadcastIndependentManagedApp = (baseEndPoints.get("allowBroadcastIndependentManagedApp").equals("true"))? true : false ;
							}
							else {//before Ver2.1
								this.serverInfo = baseEndPoints.get("SERVER_INFO");
								this.serverInfoProtocolVersion = baseEndPoints.get("PROTOCOL_VERSION");
								setRestBaseURL(baseEndPoints.get("BASEURL"));
								setWSURL(baseEndPoints.get("WSURL"));
							}
							// success to get info about HybridcastConnectInformation from TV Set.
							status.setStatus(TVRCStatus.Status.OK.code(), status.body, "") ;
						}
						else {
							status.setStatus(TVRCStatus.Status.BadRequestInternalProcessing.code(), status.body, "") ;
							devType = DIALInterface.devType;
						}
					}else{
						status.setStatus(TVRCStatus.Status.BadRequestInternalProcessing.code(), status.body, "") ;
						devType = DIALInterface.devType;
					}
				}catch (JSONException e) {
					logger.error("JsonException:  NotMatch DIALAPIinfomation");
					logger.error("JsonException in getAppInfo: " + e);
					// in JSONException, fail to get info about HybridcastConnectInformation from TV Set => Internal BadRequest
					status.setStatus(TVRCStatus.Status.BadRequestInternalProcessing.code(), "", "NotMatch DIALAPIinfomation");
					devType = DIALInterface.devType;
				}catch(SAXException e){
					logger.error("applicationURL Error: Application Information Schema Error");
					logger.error("GetAppInfoError: " + e);
					// in general Exception, fail to get info about HybridcastConnectInformation from TV Set => Internal BadRequest
					status.setStatus(TVRCStatus.Status.BadRequestInternalProcessing.code(), "", "NotMatch DIALAPIinfomation");
//					devType = devinfo.getMaker().toUpperCase();
					devType = DIALInterface.devType;
				}catch(Exception e){
					logger.error("applicationURL Error:  NotMatch DIALAPIinfomation");
					logger.error("GetAppInfoError: " + e);
					// in general Exception, fail to get info about HybridcastConnectInformation from TV Set => Internal BadRequest
					status.setStatus(TVRCStatus.Status.BadRequestInternalProcessing.code(), "", "NotMatch DIALAPIinfomation");
//					devType = devinfo.getMaker().toUpperCase();
					devType = DIALInterface.devType;
				}
			}else{
//				devType = devinfo.getMaker().toUpperCase();
				devType = DIALInterface.devType;
				status.setStatus(status.status, "", "Error in requesting ApplicationURL");
			}
		}

		return status;
	}

	/**
	 *  XMLparseAndSet.
	 *
	 * @param XMLString
	 * @return ハイコネ仕様の受信機ごとに異なるversionやendpointのbaseURL、informationのリスト
	 * @throws Exception 例外
	 * @throws JSONException JSON処理における例外
	 */
	private Map<String,String> parseDialAppinfoXMLXpath(String XMLString) throws Exception{
		Map<String, String> basepoints = new HashMap<String, String>();
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document document = documentBuilder.parse(new ByteArrayInputStream(XMLString.getBytes("UTF-8")));
			Element root = document.getDocumentElement();
			NodeList rootChildren = root.getChildNodes();
			String namespace = null;

			if( root.getNodeName().equals("service") ) {
				NamedNodeMap smap = root.getAttributes();
//				logger.debug("SSS :" + smap.getNamedItem("xmlns:iptv") );
				namespace =  (smap.getNamedItem("xmlns:iptv")!=null)? "iptv:" : "";
			}

			for(int i=0; i < rootChildren.getLength(); i++) {
				Node node = rootChildren.item(i);
//				logger.debug("AAA node:" + node.getNodeName() );

				if( node.getNodeName().equals("additionalData") ) {
					NodeList deviceChildren = node.getChildNodes();
					for(int j=0; j < deviceChildren.getLength(); j++) {
						Node devnode = deviceChildren.item(j);
//						logger.debug("BBB dev:" + devnode.getNodeName() );
						if (devnode.getNodeName().equals(namespace + "X_Hybridcast_ServerInfo")) {
							String serverInfo = devnode.getTextContent();
							String serverInfoProtocolVersion = serverInfo.split(";")[1];
							basepoints.put("SERVER_INFO", serverInfo);
							basepoints.put("PROTOCOL_VERSION", serverInfoProtocolVersion);
							logger.debug("SERVER_INFO : " + serverInfo );
							logger.debug("PROTOCOL_VERSION : " + serverInfoProtocolVersion );
						}
						else if (devnode.getNodeName().equals(namespace + "X_Hybridcast_TVControlURL")) {
							String restBaseURL = devnode.getTextContent();
							basepoints.put("BASEURL", restBaseURL );
							logger.debug("BASEURL : " + restBaseURL );
						}
						else if (devnode.getNodeName().equals(namespace + "X_Hybridcast_App2AppURL")) {
							String websocketUrl = devnode.getTextContent();
							basepoints.put("WSURL", websocketUrl);
							logger.debug("WSURL : " + websocketUrl );
						}
						else if (devnode.getNodeName().equals(namespace + "X_Hybridcast_ProtocolVersion")) {
							String hybridcastProtocolVersion = devnode.getTextContent();
							basepoints.put("HYBRIDCAST_PROTOCOL_VERSION", hybridcastProtocolVersion);
							logger.debug("HYBRIDCAST_PROTOCOL_VERSION : " + hybridcastProtocolVersion );
						}
						else if (devnode.getNodeName().equals(namespace + "X_Hybridcast_ExternalLaunch")) {
							NamedNodeMap mmap = devnode.getAttributes();
							Node bonode = mmap.getNamedItem("allowBroadcastOrientedManagedApp");
							if( bonode != null ) {
								basepoints.put("allowBroadcastOrientedManagedApp", bonode.getNodeValue().toLowerCase());
							}
							Node binode = mmap.getNamedItem("allowBroadcastIndependentManagedApp");
							if( binode != null ) {
								basepoints.put("allowBroadcastIndependentManagedApp", binode.getNodeValue().toLowerCase());
							}
							logger.debug("allowBroadcastOrientedManagedApp : " + bonode.getNodeValue().toLowerCase() );
							logger.debug("allowBroadcastIndependentManagedApp : " + binode.getNodeValue().toLowerCase() );
						}
					}
				}
			}
		}catch(Exception e){
			logger.error("parseDialAppinfoXMLXpath: " + e);
			basepoints = null;
		}
		return basepoints;
	}

	/**
	 *  XMLparseAndSet.
	 *
	 * @param XMLString
	 * @return ハイコネ仕様の受信機ごとに異なるversionやendpointのbaseURL、informationのリスト
	 * @throws Exception 例外
	 * @throws JSONException JSON処理における例外
	 */
/*****
	private Map<String,String> parseDialAppinfoXML(String XMLString) throws Exception, JSONException{
		Map<String, String> basepoints = new HashMap<String, String>();
		try{
			JSONObject appStatus  = XML.toJSONObject(XMLString);
			JSONObject addData = null;
			String namespace = null;
			String websocketUrl = null;
			String restBaseURL = null;

			// namespace check on top level
			namespace = appStatus.getJSONObject("service").has("xmlns:iptv") ? "iptv" : null;
			// additionalData
			if( appStatus.getJSONObject("service").has("additionalData")){
				addData = appStatus.getJSONObject("service").getJSONObject("additionalData");
				namespace = addData.has("xmlns:iptv") ? "iptv" : namespace;
			} else {
				logger.debug("additionalData:" + appStatus.getJSONObject("service").toString() );
				throw new UnsupportedOperationException("No additionalData field in applicationInfoXML");
			}

			logger.debug("additionalData:" + addData.toString() );

			// ServerInfo の存在確認と取得処理とエラー処理
			serverInfo =  getXMLJSONDataWithNamespace(addData, namespace, "X_Hybridcast_ServerInfo");
			logger.debug("serverInfo: " + serverInfo );
			serverInfoProtocolVersion = serverInfo.split(";")[1];
			basepoints.put("SERVER_INFO", serverInfo);
			basepoints.put("PROTOCOL_VERSION", serverInfoProtocolVersion);

			logger.debug("serverInfoProtocolVersion: " + serverInfoProtocolVersion );

			// App2AppURL の存在確認と取得処理とエラー処理
			websocketUrl = getXMLJSONDataWithNamespace(addData, namespace, "X_Hybridcast_App2AppURL");
			basepoints.put("WSURL", websocketUrl);

//			if(serverInfoProtocolVersion.equals("2.0")) // version2.0以上のみTVControlのbaseurlがある
			if(serverInfoProtocolVersion.equals("2.0") ||  serverInfoProtocolVersion.equals("2.1")) // version2.0以上のみTVControlのbaseurlがある
			{
				// TVControlURL の存在確認と取得処理とエラー処理
				restBaseURL =  getXMLJSONDataWithNamespace(addData, namespace, "X_Hybridcast_TVControlURL");
				logger.debug("BASEURL:" + restBaseURL );
				basepoints.put("BASEURL", restBaseURL );
			}
		}catch (JSONException e) {
			logger.error("JsonException:  NotMatch DIALAPIinfomation");
			logger.error("JsonException in getAppInfo: " + e);
			basepoints = null;
		}catch(Exception e){
			logger.error("applicationURL Error:  NotMatch DIALAPIinfomation");
			logger.error("GetAppInfoError: " + e);
			basepoints = null;
		}
		return basepoints;
	}
****/

	/**
	 * Namespace入りのXMLtoJSONしたデータのフィールド抽出。
	*/
/****
	private String getXMLJSONDataWithNamespace(JSONObject JSData, String namespace, String fieldname) throws Exception, JSONException {
		// fieldnameの存在確認と取得処理とエラー処理
		String fieldData = "";
		if(!Objects.isNull(namespace)  && !namespace.equals("")){
			if(JSData.has(namespace + ":" + fieldname)){
				fieldData = JSData.getString(namespace + ":" + fieldname) ;
			}
		} else if(JSData.has(fieldname)){
			fieldData = JSData.getString(fieldname) ;
		} else {
			logger.debug("additionalData:" + JSData.toString() );
			throw new UnsupportedOperationException("No " + fieldname  + " field Found");
		}
		return fieldData;
	}
****/

	/**
	 * baseURL設定。
	 * ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのリストをセットする。
	 *
	 * @param restBaseURL ハイコネ仕様における各受信機の提供するハイコネのRestAPIのendpointURLのprefixURL
	 */
	@Override
	public void setRestBaseURL(String restBaseURL){
		try{
			baseURL = restBaseURL;
			this.restApiEndpoints.put("AvailableMedia",         restBaseURL + restApiPath.get("AvailableMedia") );
			this.restApiEndpoints.put("ChannelsInfo",      restBaseURL + restApiPath.get("ChannelsInfo") );
			this.restApiEndpoints.put("ReceiverStatus",        restBaseURL + restApiPath.get("ReceiverStatus") );
			this.restApiEndpoints.put("TaskStatus",    restBaseURL + restApiPath.get("TaskStatus") );
			this.restApiEndpoints.put("StartAIT",    restBaseURL + restApiPath.get("StartAIT") );
			logger.debug("Change baseURL:" + restBaseURL );
		}catch(Exception e){
			logger.error("setRestBaseURL Error:" + restBaseURL);
			logger.error("setRestBaseURL Exception:" + e);
		}
	}
	@Override
	public String getRestBaseURL(){
		return this.baseURL;
	}
	@Override
	public void setWSURL(String wsURL){
		this.wsURL = wsURL;
		logger.debug("Change wsURL:" + wsURL );
	}
	@Override
	public String getWSURL(){
		return this.wsURL;
	}

	/**
	 * メディア利用可否情報の取得。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus getAvailableMedia(TVRCDevinfo devinfo) throws Exception {
		String url = restApiEndpoints.get("AvailableMedia");	//logger.debug("AvailableMedia:" + url);
		return this.HTTPRequest( "GET", devinfo, url, null ) ;
	}

	/**
	 * 編成チャンネル情報の取得。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param media 取得したい放送メディア（TD/BS/CS）
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus getChannelInfo(TVRCDevinfo devinfo, String media) throws Exception {
		TVRCStatus status = new TVRCStatus();
		String url = "";

		if( !checkMedia( media ) ) {
				url = restApiEndpoints.get("ChannelsInfo");
			if(media.equals("null")){ //プロトコルのテスト用機能としてnull文字列を指定するとクエリーパラメターを挿入しない
			}else if (media.equals("")){//mediaを自分で指定しない場合のテスト用
				url = url +  "?media=" ;
			}else{
				url = url +  "?media=" + media ;
			}

			logger.debug("Warning: iregular query parameter" + media);
				status = this.HTTPRequest( "GET", devinfo, url, null ) ;
			//status.setStatus(TVRCStatus.Status.BadRequest.code(), "", "TVCTRL4j Parameter Error:" + media );
		}
		else {
			url = restApiEndpoints.get("ChannelsInfo") + "?media=" + media ;
			status = this.HTTPRequest( "GET", devinfo, url, null ) ;
		}
		logger.debug("HCEX_getmedia: " + url);
		return status ;
	}

	/**
	 * 受信機状態の取得。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getReceiverStatus(TVRCDevinfo devinfo) throws Exception {
		String url = restApiEndpoints.get("ReceiverStatus");
		logger.debug("ReceiverStatus:" + url);
		return this.HTTPRequest( "GET", devinfo, url, null ) ;
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
	public TVRCStatus startAITControlledApp(TVRCDevinfo devinfo, String mode, String appinfo ) throws Exception {
		String url = restApiEndpoints.get("StartAIT");
		if(mode.equals("null")){ //プロトコルのテスト用機能としてnull文字列を指定するとクエリーパラメターを挿入しない
		}else if (mode.equals("")){//modeを自分で指定しない場合のテスト用
			url = url +  "?mode=" ;
		}else{
			url = url +  "?mode=" + mode ;
		}
		logger.debug("StartAIT:" + url);

		// for each device and each session, change encode processing.
		String appInfo = encodeBodyString(devinfo, appinfo);

		return this.HTTPRequest( "POST", devinfo, url,  appInfo ) ;
	}

	/**
	 * 起動アプリケーション可否情報の取得。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus getTaskStatus(TVRCDevinfo devinfo) throws Exception {
		String url = restApiEndpoints.get("TaskStatus");
		logger.debug("TaskStatus:" + url);
		return this.HTTPRequest( "GET", devinfo, url, null ) ;
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
		logger.debug("connWebsocket()");

		URI wsuri = null;
		String strWSurl = this.wsURL;
		if( strWSurl.equals("") ) {
			// URLチェックNG => InternalBadRequest
			status.setStatus(TVRCStatus.Status.BadRequestInternalProcessing.code(), "", "BadRequest: Websocket ServerURL cannot be specfied");
		}
		else {
			try {
				wsuri = new URI( strWSurl );

				logger.debug("connWebsocket: " + wsuri);
				wsc = new WebSocketClient(WebSocketVersion.V13 , wsuri, devinfo);
				if( wsc != null ) {
					if( wsc.connect( hclistener ) != null ) {
						// success connection => OK
						status.setStatus(TVRCStatus.Status.OK.code(), "{\"head\":" + TVRCStatus.Status.OK.code() + ", \"message\":\"OK\"}", "");
					}
					else {
						// fail ws connection => internal error?
						status.setStatus(TVRCStatus.Status.InternalError.code(), "{\"head\":" + TVRCStatus.Status.InternalError.code() + ", \"message\":\"\"}", "connect Error");
					}
				}
				else {
					// wsobject cannot be constructed => BadRequestInternal
					status.setStatus(TVRCStatus.Status.BadRequestInternalProcessing.code(), "{\"head\":" + TVRCStatus.Status.BadRequestInternalProcessing.code() + ", \"message\":\"\"}", "websocket client object cannot be constructed properly");
				}
			}catch(NullPointerException e){
				wsc = null;
				System.out.println("connWebsocket NullPointerExceptionError");
				String status_body = "{\"head\": " + TVRCStatus.Status.DenyInternalProcessing.code() + ", \"message\":\"Connect Websocket NullPointerException\"}";
				status.setStatus(TVRCStatus.Status.DenyInternalProcessing.code(), status_body, "NullPointerException");
			}catch(Exception e) {
logger.error("connWebsocket Exception: " + e);
				wsc = null;
				if(Objects.nonNull(e.getMessage())){
					String excMsg = e.getMessage();
					String statuscode = String.valueOf(TVRCStatus.Status.DenyInternalProcessing.code()); // default 50500
					String statusmsg = "Internal Error"; // default InternalError
					if( !excMsg.equals("") ) {
						// able to connect server, but get error response and bad status/message in exception message
logger.error("connWebsocket http/ws connection exception message found: [" + excMsg + "]");
						String httpstatus = e.getMessage().split(":")[1];
						statuscode = httpstatus.split(" ")[1];
						statusmsg = httpstatus.split(" ")[2];
						logger.error("connWebsocketExceptionError : " + httpstatus);
					}
					status.setStatus(Integer.parseInt(statuscode), "{\"head\":{\"code\":" + statuscode + ", \"message\":\"" + statusmsg +  "\"},\"body\":{}",statusmsg);
				}else{
					// connection error(wscnull, other crash error) => no exception messsage => internal error 500?
					status.setStatus(TVRCStatus.Status.InternalError.code(), "{\"head\":" + TVRCStatus.Status.InternalError.code() + ", \"message\":\"" + e.getMessage() + "\"}", e.getMessage());
				}

			}
		}

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
		logger.debug("addWebsocketListner()");
		String lsname = "";
		WebSocketClient wsct = null;
		if(wsc!=null){

			wsct = wsc.addWSListener(listenerName, new WebSocketClient.FrameListener(){
				@Override
				public void onFrame(WebSocketFrame wf){
					String msg = ((TextWebSocketFrame) wf).text();
					try{
							listener.wsDataReceived( decodeWSBodyString(devinfo, msg));
					}catch(RuntimeException e){
						logger.error("Runtime Error Occuered: " + e);
					}catch(Exception e){
						logger.error("Some Error Occuered: " + e);
					}
				}
				@Override
				public void onError(Throwable t) {
					logger.error("Some Error Occuered: " + t);
				}
			});
		}
		return (wsct!=null)? listenerName : "";
	}


	/**
	 * WebSocketのListenerの削除。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param listenerName 削除するwebsocket通信の受信データ処理のためのcallbackListenerの名前
	 * @return 削除したlistenerName
	 * @throws Exception 例外
	 */
	public String removeWSListener(TVRCDevinfo devinfo, String listenerName ) throws Exception {
		logger.debug("removeWebsocketListner(): " + listenerName);
		WebSocketClient wsct = null;
		if(wsc!=null){
			wsct = wsc.removeWSListener(listenerName);
		}
		return (wsct!=null)? listenerName : "";
	}


	/**
	 * WebSocketのListenerのListener名リスト。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @return 登録されているwebsocketのListenerNameリスト
	 * @throws Exception 例外
	 */
	public List<String> getListenerList(TVRCDevinfo devinfo) throws Exception {
		logger.debug("getListenerList()");
		List<String> listenerList = null;
		if(wsc!=null){
			listenerList = wsc.getListenerList();
		}else{
			listenerList = null;
		}
		return listenerList;
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
//logger.debug("disconnWebsocket()");
		try{
			if( wsc != null ) {
				wsc.destroy();
				wsc = null;
//				status.setStatus(50200, "{\"head\":50200, \"message\":\"Disconnect Websocket OK\"}", "");
				status.setStatus(TVRCStatus.Status.OKInternalProcessing.code(), "{\"head\":" + TVRCStatus.Status.OKInternalProcessing.code() + ", \"message\":\"Disconnect Websocket OK\"}", "");
			}else{
				wsc = null;
//				status.setStatus(50300, "{\"head\":50300, \"message\":\"Already Disconnect Websocket\"}", "");
				status.setStatus(TVRCStatus.Status.AlreadyActiveInternalProcessing.code(), "{\"head\":" + TVRCStatus.Status.AlreadyActiveInternalProcessing.code() + ", \"message\":\"Already Disconnect Websocket\"}", "");
			}
		}catch(NullPointerException e){
			wsc = null;
			logger.debug("disconnWebsocket NullPointerExceptionError");
			throw new NullPointerException("Disconnect Websocket : NullPointerException");
		}catch(Exception e){
			wsc = null;
			logger.debug( "Disconnect Websocket Exception: " + e);
			throw new Exception(e);
		}
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

		if( wsc == null ) {
			status.setStatus(TVRCStatus.Status.InternalError.code(), "", "websocket not connected");
		}
		else {
			String sendtext = text ;
			logger.debug("sendingTextString Over Websocket : " + sendtext);
			sendtext = encodeBodyString(devinfo, sendtext) ;
			wsc.send( new TextWebSocketFrame( sendtext ), new wsSendListener());
			// success sending message => internally ok => 50200
			status.setStatus(TVRCStatus.Status.OKInternalProcessing.code(), "{\"head\":" + TVRCStatus.Status.OKInternalProcessing.code() + ", \"message\":\"OK\"}", "");
		}

		return status;
	}

	/**
	 * WebSocketでの送信（リスナー付き）。
	 * 任意のメッセージフォーマットをWS経由で送信するAPI。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param text websocketで送信する任意の文字列データ
	 * @param listener 送信と同時に仕掛けたいListener(HCListenerのインスタンス)
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus sendWebsocket(TVRCDevinfo devinfo, String text, HCListener listener) throws Exception {
		TVRCStatus status = new TVRCStatus();

		if( wsc == null ) {
			status.setStatus(TVRCStatus.Status.InternalError.code(), "", "websocket not connected");
		}
		else {
			String sendtext = text ;
			logger.debug("sendingTextString Over Websocket : " + sendtext);
			sendtext = encodeBodyString(devinfo, sendtext);

			if(listener != null){
				wsc.send( new TextWebSocketFrame( sendtext ), new WebSocketClient.FrameListener(){
					@Override
					public void onFrame(WebSocketFrame wf){
						String msg = ((TextWebSocketFrame) wf).text();
						try{
							listener.wsDataReceived(decodeWSBodyString(devinfo, msg)) ;
						}catch(RuntimeException e){
							logger.error("Runtime Error Occuered");
						}catch(Exception e) {
							logger.error("Error Occuered");
						}
					}
					@Override
					public void onError(Throwable t) {

					}
				});
			}else{
				wsc.send( new TextWebSocketFrame( sendtext ), new wsSendListener());
			}
			// success sending message => internally ok => 50200
			status.setStatus(TVRCStatus.Status.OKInternalProcessing.code(), "{\"head\":" + TVRCStatus.Status.OKInternalProcessing.code() + ", \"message\":\"OK\"}", "");
		}

		return status;
	}


	/**
	 * sendTextToHostDeviceOverWS。
	 * ハイコネ仕様のメッセージフォーマットをWS経由で送信するAPI。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param sendtextStr websocketで送信するHybridcastHTMLへ送信する文字列データ
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	@Override
	public TVRCStatus sendTextToHostDeviceOverWS(TVRCDevinfo devinfo, String sendtextStr) throws Exception {
		logger.debug("HCEX_sendTextToHostDeviceOverWebsockiet:");
		TVRCStatus status = new TVRCStatus();
		JSONObject sendtextFmtJson = new JSONObject()
			.put("message", new JSONObject()
				.put("devid",devinfo.sessInfo.devID)
				.put("sendTextToHostDevice", new JSONObject()
					.put("text", sendtextStr)
				)
			);
		status = sendWebsocket(devinfo, sendtextFmtJson.toString());
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
		logger.debug("HCEX_requestUrlOverWebsocket:");
		TVRCStatus status = new TVRCStatus();
		JSONObject reqUrlFmtJson = new JSONObject()
			.put("control", new JSONObject()
				.put("devid",devinfo.sessInfo.devID)
				.put("request", new JSONObject()
					.put("command", "setURLForCompanionDevice")
				)
			);
		status = sendWebsocket(devinfo, reqUrlFmtJson.toString());
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
	 */
	public TVRCStatus requestUrlOverWS(TVRCDevinfo devinfo, HCListener listener) throws Exception {
		logger.debug("HCEX_requestUrlOverWebsocket:");
		TVRCStatus status = new TVRCStatus();
		JSONObject reqUrlFmtJson = new JSONObject()
			.put("control", new JSONObject()
				.put("devid",devinfo.sessInfo.devID)
				.put("request", new JSONObject()
					.put("command", "setURLForCompanionDevice")
				)
			);
		status = sendWebsocket(devinfo, reqUrlFmtJson.toString(), listener);
		return status;
	}


	/**
	 * extensionsCommandOverWS.
	 * ハイコネ仕様のメッセージフォーマットで受信機制御のためのリクエストメッセージ（コマンド）をWS経由で送信するAPI。
	 *
	 * @param devinfo 実行対象のDeviceinfoオブジェクト
	 * @param commandStr 制御コマンド
	 * @return 実行結果のレスポンス・失敗情報を含むStatusオブジェクト
	 * @throws Exception 例外
	 */
	public TVRCStatus extensionCommandOverWS(TVRCDevinfo devinfo, String commandStr) throws Exception {
		logger.debug("HCEX_extensionCommandOverWebsockiet:");
		TVRCStatus status = new TVRCStatus();
		JSONObject hcExCmdFmtJson = new JSONObject()
			.put("control", new JSONObject()
				.put("devid",devinfo.sessInfo.devID)
				.put("extensions", new JSONObject()
					.put("vendor", commandStr)
				)
			);
		status = sendWebsocket(devinfo, hcExCmdFmtJson.toString());
		return status;
	}

}
