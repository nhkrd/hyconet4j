package jp.or.nhk.rd.hyconet4j;

import java.util.Map;
import java.util.HashMap;
import org.json.JSONjava.JSONObject;
import org.json.JSONjava.JSONException;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * DIAL Rest Service Handler Interface.
 * Hybridcast Connect Refers DIAL Protocol.
 * See "IPTVFJ STD-0013 7.2.1 Discovery" and DIALProtocol Specification version1.7.2 in http://www.dial-multiscreen.org/dial-protocol-specification "
 */
public interface DIALInterface {


	public static final String applicationName = "Hybridcast";  // Registry in IANA, For Hybridcast Connect.
	public static final String dialApplicationURLBasePath = "/apps"; // Dont include suffix "/"
	//public static final String dialApplicationResourceURL = dialApplicationBasePathURL + "/" + applicationName;

	public static final String mimetype = "text/xml";
	public static final String charset = "charset=UTF-8";
	public static final String mimeParameter = mimetype + "; " + charset;
	public static final String accessControlAllowOrigin = "";

	public static final String devType = "DIAL";

	/**
	 * DIAL Rest Service APIs
	 */

	/**
	 * get Application-Resource-URL(RestAPI endpointURL).
	 * DIAL Application ServiceのRESTAPIのendpointURL(=Application Resource URL)の取得.
	 *
	 * @return Application Resource URL
	 */
	public String getDialAppResourceURL();


	/**
	 * get Application-Infomation(RestAPI endpointURL) From DialAppResourceURL.
	 * DIAL Application ServiceのRESTAPIのendpointURL(=Application Resource URL)をDialAppResourceURLから取得.
	 *
	 * @return Application Information as XML
	 */
	public String getDialAppInfo();

	/**
	 * XMLdata parser for DIAL Application Information response.
	 * DIAL Application Information のXMLをパースするメソッド.
	 *
	 * @param XMLString
	 * @return Map
	 */
	//public Map<String,String> parseDialAppinfoXML(String XMLString);


	/**
	 * XMLSchemaValidation for DIAL Application Information response.
	 * DIAL Application Information のXMLのschema検証.
	 *
	 * @param XMLString
	 * @return result
	 */
	//public String validateDialAppinfoXML(String XMLString) throws Exception;
	//public Boolean validateDialAppinfoXML(String XMLString) throws Exception;

	/**
	 * XMLSchemaValidation for Device Description response in Device Discovery.
	 * DIALの参照するUPnPのSSDPにおけるDevice DescriptionのXMLのschema検証.
	 *
	 * @param XMLString
	 * @return result
	 */
	//public String validateDDXML(String XMLString) throws Exception;
	//public Boolean validateDDXML(String XMLString) throws Exception;

	/**
	 * DIAL Rest Service Response Status
	 */


	public final static class DialApplicationState {
		final static String Running = "running";
		final static String Stopped = "stopped";
		final static String Installable = "installable";
	}

	public final static class DialAppInfoResponseStatus {
		final static HttpResponseStatus OK = new HttpResponseStatus( 200, "OK");
		final static HttpResponseStatus NotFound = new HttpResponseStatus( 404, "Not Found");
	}

}
