package jp.or.nhk.rd.hyconet4j;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.device.SearchResponseListener;
import org.cybergarage.upnp.ssdp.SSDPPacket;

import java.io.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.transform.stream.StreamSource;
import javax.xml.XMLConstants;
import javax.xml.validation.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * SSDP ControlPoint Class for Device Discovery that has the child of ControlPoint Class and implementation of SearchResponseListener.
 * SSDP is the part of the protocol in UPnP standardized at Device Consumer Consorsium.
 * For more detail, see Device Consumer Consorsium.
 */
public class SSDPCP extends ControlPoint implements SearchResponseListener {
	private _Logger logger = null;

	private List<String> urns = new ArrayList<String>();
	private String schemaPath = "./dd.xsd";

	/**
	 * Get detail device information in the XML response from DeviceDescriptonURL 
	 * that is the param in response header from SSDP request.
	 * 
	 * @param urn unique identifier of the device that consumer want to find.
	 * @param location the URL that the data of device information is written 
	 * @param usn unique identifier of the device, such as MACAddress, SerialNumber of the device, chip
	 */
	private void getDetail(String urn, String location, String usn) {
		try {
			URL obj = new URL(location);

			// TODO: getDDXML
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			//add reuqest header
			con.setRequestMethod("GET");
			con.connect();

			int status = con.getResponseCode();
			logger.trace("DDXML Status: " + status);

			String application_url = "" ;
			// TODO: getApplicationUrlFromHeader
			final Map<String, List<String>> headers = con.getHeaderFields();
			for (String key : headers.keySet()) {
				final List<String> valueList = headers.get(key);
				final StringBuilder values = new StringBuilder();
				for (String value : valueList) {
					values.append(value);
				}
//				logger.trace( key + " : " + values.toString());
				// TODO: isApplicationUrlFromHeader
				if( (key != null) && (key.toUpperCase()).equals("APPLICATION-URL") ) {
					application_url = values.toString();
					logger.info("APPLICATION-URL:" + application_url);
				}
			}

			if (status == HttpURLConnection.HTTP_OK) {
				String result = "";
				InputStreamReader isr = new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8);
				BufferedReader reader = new BufferedReader(isr) ;
				String line;
				while ((line = reader.readLine()) != null) {
					result = result + line;
				}

				// TODO: isValidDDXML = validateDDXML
				Boolean isValidDDXML = validateDDXML(result);

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = factory.newDocumentBuilder();
				Document document = documentBuilder.parse(new ByteArrayInputStream(result.getBytes("UTF-8")));

				Element root = document.getDocumentElement();
	//					logger.trace("XMLnodeRoot：" +root.getNodeName());
				NodeList rootChildren = root.getChildNodes();
	//					logger.trace("Num of chidren elements：" + rootChildren.getLength());
				// TODO: getParamsFromDeviceDescription(XMLString)


				for(int i=0; i < rootChildren.getLength(); i++) {
					Node node = rootChildren.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element)node;
						if (element.getNodeName().equals("device")) {
							NodeList deviceChildren = element.getChildNodes();

							String manufacturer = null;
							String friendlyName = null;
							String modelName = null;
							String device_urn = null;
							String udn = null;
							for(int j=0; j < deviceChildren.getLength(); j++) {
								Node devnode = deviceChildren.item(j);
								if (devnode.getNodeName().equals("manufacturer")) {
									manufacturer = devnode.getTextContent();
//									logger.trace("manufacturer: " + manufacturer);
								}
								if (devnode.getNodeName().equals("friendlyName")) {
									friendlyName = devnode.getTextContent();
//									logger.trace("friendlyName: " + friendlyName);
								}
								if (devnode.getNodeName().equals("modelName")) {
									modelName = devnode.getTextContent();
//									logger.trace("modelName：" + modelName);
								}
								if (devnode.getNodeName().equals("deviceType")) {
									device_urn = devnode.getTextContent();
									logger.debug("device_Type: " + device_urn);
								}
								if (devnode.getNodeName().equals("UDN")) {
									udn = devnode.getTextContent();
									logger.debug("UDN: " + udn);
								}
							}

							logger.debug("location: " + location);
							logger.debug("getDetail.usn: " + usn);

							//_devinfo( location, urn, device_urn, usn, manufacturer, friendlyName, modelName, application_url) ;
							logger.info("onDeviceInfomationPrepared in SSDP search and getDetail process");
							onDeviceInfoPrepared( location, urn, device_urn, usn, manufacturer, friendlyName, modelName, application_url) ;
						}
					}
				}
			}
			else {
			}

		}
		catch(ParserConfigurationException e){
			logger.error("ParserConfigurationException:" + e );
		}
		catch(SAXException e){
			logger.error("XMLSchema or ParserConfigurationException:" + e );
			logger.error( e );
		}
		catch(IOException e){
			logger.error( e );
		}
		catch(Exception e){
			logger.error( e );
		}
	}


	/**
	 * SSDPCP::SSDPCP
	 */
	public SSDPCP() {
		logger = new _Logger("SSDPCP");

		this.addSearchResponseListener(this);
		UPnP.setEnable(UPnP.USE_ONLY_IPV4_ADDR);
		UPnP.setDisable(UPnP.USE_ONLY_IPV6_ADDR);
	}

	
	/**
	 *  deviceSearchResponseReceived
	 * 
	 * @param pkt SSDP received packet
	 */
	public void deviceSearchResponseReceived(SSDPPacket pkt) {
		String usn = pkt.getUSN();
		String target = pkt.getST();
		String location = pkt.getLocation();
		String nt = pkt.getNT();
		String nts = pkt.getNTS();
		String man = pkt.getMAN();
//logger.trace( "[" + usn + "][" + target + "][" + location + "][" + nt + "][" + nts + "][" + man + "]");

		if( usn.equals("") ) {
			getDetail(null, location, usn);
		}
		else {
			if( urns == null) {
				getDetail(null, location, usn);
			}
			else {
				for (String st: urns) {
//logger.trace( target + " / " + st + " / " + usn + " / " + location);
					if( target.equals(st) ) {
						logger.debug("--------------------");
						logger.debug("usn(uuid): " + usn + " target: " + target + " location: " + location);

						getDetail(st, location, usn);

						break;
					}
				}
			}
		}
	}


	/**
	 * Device Inforamtion has just been prepared when getting DDXML and DIALAPPlicationURL in SSDP Process
	 * Need to implements Logic in this method as a override method when device information can be got.
	 *
	 * @param location the URL that the data of device information is written 
 	 * @param urn unique identifier of the device that consumer want to find.
	 * @param device_urn urn that identifies the device, in frequent use, the format becomes "URN:MACADDRESS"
	 * @param usn unique identifier of the device, such as MACAddress, SerialNumber of the device, chip 
	 * @param manufacturer the name of device manufacturer
	 * @param friendly_name the desceription of the device name
	 * @param model_name the model name
	 * @param application_url the URL that application information in device. see DIAL protocol
	 */
	public void _devinfo(String location, String urn, String device_urn, String usn, String manufacturer, String friendly_name, String model_name, String application_url) {
	}


	/**
	 * Device Inforamtion has just been prepared when getting DDXML and DIALAPPlicationURL in SSDP Process
	 * Need to implements Logic in this method as a override method when device information can be got.
	 *
	 * @param location the URL that the data of device information is written 
 	 * @param urn unique identifier of the device that consumer want to find.
	 * @param device_urn urn that identifies the device, in frequent use, the format becomes "URN:MACADDRESS"
	 * @param usn unique identifier of the device, such as MACAddress, SerialNumber of the device, chip 
	 * @param manufacturer the name of device manufacturer
	 * @param friendly_name the desceription of the device name
	 * @param model_name the model name
	 * @param application_url the URL that application information in device. see DIAL protocol
	 */
	public void onDeviceInfoPrepared(String location, String urn, String device_urn, String usn, String manufacturer, String friendly_name, String model_name, String application_url) {
		logger.info("onDeviceInfomationPrepared in SSDP search");
	}


	/**
	 * get Device Description XML from LocationURL in Device Discovery.
	 * DIALの参照するUPnPのSSDPにおけるDevice DescriptionのXMLの取得.
	 *
	 * @param LocationURL Device DescriptionのXMLのURL
	 * @return result
	 * @throws Exception 例外
	 * @throws SAXException schemaValidationエラー
	 */
	public String getDDXML(String LocationURL) throws Exception, SAXException{
		//validation process
		return "";
	};



	/**
	 * get ApplicationURL in the header of the response from device search.
	 * 
	 * @param headers the header of the response from device search
	 * @return applicationURL
	 */
	private String getApplicationUrlFromHeader(Map<String, List<String>> headers){
		String applicationURL = "http://localhost:8888/dd.xml";
		return applicationURL;
	}


	/**
	 * XMLSchemaValidation for Device Description response in Device Discovery.
	 * DIALの参照するUPnPのSSDPにおけるDevice DescriptionのXMLのschema検証.
	 *
	 * @param XMLString Device DescriptionのXML
	 * @return result
	 * @throws Exception 例外
	 * @throws SAXException schemaValidationエラー
	 */
	//public String validateDDXML(String XMLString) throws Exception;
	public Boolean validateDDXML(String XMLString) throws Exception, SAXException{
		//validation process
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		try {
			// Schema インスタンスを生成
			Schema schema = factory.newSchema(new File(this.schemaPath));
			// Validator インスタンスを生成
			Validator validator = schema.newValidator();
	
			// 妥当性検証を実行
			validator.validate(new StreamSource(new StringReader(XMLString)));
	
		} catch (SAXException | IOException e) {
			throw new RuntimeException(e);
		}
		return true;
	};

	
	/**
	 * set XMLSchema for Device Description response in Device Discovery.
	 * DIALの参照するUPnPのSSDPにおけるDevice DescriptionのXMLのschemaをセット.
	 *
	 * @param schemaPath Device DescriptionのXMLSchemaPath
	 * @throws Exception 例外
	 */
	public void setDDXMLSchema(String schemaPath) throws Exception{
		this.schemaPath = schemaPath;
	};


	//
	// setUrns
	//
	@Deprecated
	public void set_urns(List<String> urns) {
		this.urns = urns ;
	}


	/**
	 * set URNs as List for Device Discovery
	 * @param urns URNs for Device Discovery
	 */
	public void setUrns(List<String> urns) {
		this.urns = urns ;
	}

	//
	// searchStart
	//
	@Deprecated
	public void search_start() {
		logger.info("SEARCH_START");

		for (String st: urns) {
//logger.debug("searchStart: " + st);
			start( st );
			try{
				Thread.sleep(200);
			}
			catch(InterruptedException e){
				logger.error( e );
			}
		}
	}

	/**
	 * searchStart
	 * start device search for Device Discovery
	 */
	public void searchStart() {
		logger.info("SEARCH_START");

		for (String st: urns) {
//logger.debug("searchStart: " + st);
			start( st );
			try{
				Thread.sleep(200);
			}
			catch(InterruptedException e){
				logger.error( e );
			}
		}
	}

	//
	// searchStop
	//
	@Deprecated
	public void search_stop() {
		stop();
		logger.info("SEARCH_STOP");
	}

	/**
	 * searchStop
	 * stop device search for Device Discovery
	 */
	public void searchStop() {
		stop();
		logger.info("SEARCH_STOP");
	}
}
