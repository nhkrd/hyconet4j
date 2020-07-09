//
// Test for XMLParser
//
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.*;


import org.xml.sax.SAXException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Source;
import javax.xml.XMLConstants;
import javax.xml.validation.*;

import org.w3c.dom.DOMException;
import org.w3c.dom.ls.*;
import org.apache.xerces.dom.DOMInputImpl;

// TestCaseCheck
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;

public class TestHCXMLValidate {

	@Test
    public void validateDDXML_IPTVFJ_STD0013_7_2() throws Exception {
		// IPTVFJ-STD0013-7.2.1.1.1 Device Description
		//               -J-1 DeviceDescriptionXML Schema
		// given -- xUnit Naming TestFrame

		Boolean validationResult = false;
		String basePath = "src/main/resources";
		//String schemaFileName = "deviceDescription_std0013r2.9.xsd";
		String schemaFileName = "ddxml_upnpv11_for_HCv2.9.xsd";

		String stdSchemaPath= basePath + "/" + schemaFileName;


		//// testcase2
		final String testxml_stdexample = "<?xml version=\"1.0\"?>" + 
								"<root xmlns=\"urn:schemas-upnp-org:device-1-0\">" +
									"<specVersion>" +
										"<major>1</major>" +
										"<minor>0</minor>"+
									"</specVersion>" +
									"<device>" + 
										"<deviceType>urn:dial-multiscreen-org:device:dial:1</deviceType>" + 
										"<friendlyName>Hogelin</friendlyName>" + 
										"<manufacturer>株式会社 XXXXX</manufacturer>" + 
										"<modelName>HogeTV-A123</modelName>" + 
										"<UDN>uuid:00000004-0000-1010-8000-d8d43c1923dc</UDN>" + 
									"</device>" +
								"</root>"
		;

		final String testxml_sony1 = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>0</minor></specVersion><URLBase>http://172.16.12.34:8008</URLBase><device><deviceType>urn:dial-multiscreen-org:device:dial:1</deviceType><friendlyName>KJ-43X8500G</friendlyName><manufacturer>Sony</manufacturer><modelName>BRAVIA 4K UR2</modelName><UDN>uuid:6f5d04a2-58fb-12fd-8297-3f35fc5a512b</UDN><iconList><icon><mimetype>image/png</mimetype><width>98</width><height>55</height><depth>32</depth><url>/setup/icon.png</url></icon></iconList><serviceList><service><serviceType>urn:dial-multiscreen-org:service:dial:1</serviceType><serviceId>urn:dial-multiscreen-org:serviceId:dial</serviceId><controlURL>/ssdp/notfound</controlURL><eventSubURL>/ssdp/notfound</eventSubURL><0URL>/ssdp/notfound</SCPDURL></service></serviceList></device></root>";
		final String testxml_sony2 = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>0</minor></specVersion><device><deviceType>urn:schemas-upnp-org:device:Basic:1</deviceType><friendlyName>KJ-43X8500G</friendlyName><manufacturer>Sony Corporation</manufacturer><manufacturerURL>http://www.sony.net/</manufacturerURL><modelName>KJ-43X8500G</modelName><UDN>uuid:193bfc3b-6e68-4b8c-bfea-2494b8aff09b</UDN><av:X_Telepathy_Enabled xmlns:av=\"urn:schemas-sony-com:av\" /><iconList><icon><mimetype>image/jpeg</mimetype><width>120</width><height>120</height><depth>24</depth><url>/sony/webapi/ssdp/icon/dlna_tv_120.jpg</url></icon><icon><mimetype>image/png</mimetype><width>120</width><height>120</height><depth>24</depth><url>/sony/webapi/ssdp/icon/dlna_tv_120.png</url></icon><icon><mimetype>image/jpeg</mimetype><width>60</width><height>60</height><depth>24</depth><url>/sony/webapi/ssdp/icon/dlna_tv_60.jpg</url></icon><icon><mimetype>image/png</mimetype><width>32</width><height>32</height><depth>24</depth><url>/sony/webapi/ssdp/icon/dlna_tv_32.png</url></icon><icon><mimetype>image/jpeg</mimetype><width>48</width><height>48</height><depth>24</depth><url>/sony/webapi/ssdp/icon/dlna_tv_48.jpg</url></icon><icon><mimetype>image/png</mimetype><width>48</width><height>48</height><depth>24</depth><url>/sony/webapi/ssdp/icon/dlna_tv_48.png</url></icon><icon><mimetype>image/jpeg</mimetype><width>32</width><height>32</height><depth>24</depth><url>/sony/webapi/ssdp/icon/dlna_tv_32.jpg</url></icon><icon><mimetype>image/png</mimetype><width>60</width><height>60</height><depth>24</depth><url>/sony/webapi/ssdp/icon/dlna_tv_60.png</url></icon></iconList><serviceList><service><serviceType>urn:schemas-sony-com:service:ScalarWebAPI:1</serviceType><serviceId>urn:schemas-sony-com:serviceId:ScalarWebAPI</serviceId><SCPDURL>/sony/webapi/ssdp/scpd/WebApiSCPD.xml</SCPDURL><controlURL>http://172.16.12.34/sony</controlURL><eventSubURL></eventSubURL></service><service><serviceType>urn:dial-multiscreen-org:service:dial:1</serviceType><serviceId>urn:dial-multiscreen-org:serviceId:dial</serviceId><SCPDURL>/DIALSCPD.xml</SCPDURL><controlURL>/upnp/control/DIAL</controlURL><eventSubURL></eventSubURL></service><service><serviceType>urn:schemas-sony-com:service:IRCC:1</serviceType><serviceId>urn:schemas-sony-com:serviceId:IRCC</serviceId><SCPDURL>http://172.16.12.34/sony/ircc/IRCCSCPD.xml</SCPDURL><controlURL>http://172.16.12.34/sony/ircc</controlURL><eventSubURL/></service></serviceList><av:X_ScalarWebAPI_DeviceInfo xmlns:av=\"urn:schemas-sony-com:av\"><av:X_ScalarWebAPI_Version>1.0</av:X_ScalarWebAPI_Version><av:X_ScalarWebAPI_BaseURL>http://172.16.12.34/sony</av:X_ScalarWebAPI_BaseURL><av:X_ScalarWebAPI_ServiceList><av:X_ScalarWebAPI_ServiceType>guide</av:X_ScalarWebAPI_ServiceType><av:X_ScalarWebAPI_ServiceType>avContent</av:X_ScalarWebAPI_ServiceType><av:X_ScalarWebAPI_ServiceType>system</av:X_ScalarWebAPI_ServiceType><av:X_ScalarWebAPI_ServiceType>recording</av:X_ScalarWebAPI_ServiceType><av:X_ScalarWebAPI_ServiceType>videoScreen</av:X_ScalarWebAPI_ServiceType><av:X_ScalarWebAPI_ServiceType>audio</av:X_ScalarWebAPI_ServiceType><av:X_ScalarWebAPI_ServiceType>encryption</av:X_ScalarWebAPI_ServiceType><av:X_ScalarWebAPI_ServiceType>broadcastLink</av:X_ScalarWebAPI_ServiceType><av:X_ScalarWebAPI_ServiceType>cec</av:X_ScalarWebAPI_ServiceType><av:X_ScalarWebAPI_ServiceType>accessControl</av:X_ScalarWebAPI_ServiceType><av:X_ScalarWebAPI_ServiceType>browser</av:X_ScalarWebAPI_ServiceType><av:X_ScalarWebAPI_ServiceType>appControl</av:X_ScalarWebAPI_ServiceType><av:X_ScalarWebAPI_ServiceType>dial/dd.xml</av:X_ScalarWebAPI_ServiceType><av:X_ScalarWebAPI_ServiceType>video</av:X_ScalarWebAPI_ServiceType></av:X_ScalarWebAPI_ServiceList></av:X_ScalarWebAPI_DeviceInfo><av:X_DIALEX_DeviceInfo xmlns:av=\"urn:schemas-sony-com:av\"><av:X_DIALEX_AppsListURL>http://172.16.12.34:80/DIAL/sony/applist</av:X_DIALEX_AppsListURL><av:X_DIALEX_DeviceType>Android_TV_DIAL_v2.0.0</av:X_DIALEX_DeviceType></av:X_DIALEX_DeviceInfo><av:X_IRCC_DeviceInfo xmlns:av=\"urn:schemas-sony-com:av\"><av:X_IRCC_Version>1.0</av:X_IRCC_Version><av:X_IRCC_CategoryList><av:X_IRCC_Category><av:X_CategoryInfo>AAEAAAAB</av:X_CategoryInfo></av:X_IRCC_Category><av:X_IRCC_Category><av:X_CategoryInfo>AAIAAACk</av:X_CategoryInfo></av:X_IRCC_Category><av:X_IRCC_Category><av:X_CategoryInfo>AAIAAACX</av:X_CategoryInfo></av:X_IRCC_Category><av:X_IRCC_Category><av:X_CategoryInfo>AAIAAAB3</av:X_CategoryInfo></av:X_IRCC_Category><av:X_IRCC_Category><av:X_CategoryInfo>AAIAAAAa</av:X_CategoryInfo></av:X_IRCC_Category></av:X_IRCC_CategoryList></av:X_IRCC_DeviceInfo><av:X_IRCCCodeList xmlns:av=\"urn:schemas-sony-com:av\"><av:X_IRCCCode command=\"Power\">AAAAAQAAAAEAAAAVAw==</av:X_IRCCCode></av:X_IRCCCodeList></device></root>";
		final String testxml_sumidenstb = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>0</minor></specVersion><URLBase>http://172.16.12.37:8008</URLBase><device><deviceType>urn:dial-multiscreen-org:device:dial:1</deviceType><friendlyName>C02AS5</friendlyName><manufacturer>SumitomoElectricIndustries</manufacturer><modelName>C02AS5</modelName><UDN>uuid:acf3a0e0-ef74-c5cc-8b22-21139274e828</UDN><iconList><icon><mimetype>image/png</mimetype><width>98</width><height>55</height><depth>32</depth><url>/setup/icon.png</url></icon></iconList><serviceList><service><serviceType>urn:dial-multiscreen-org:service:dial:1</serviceType><serviceId>urn:dial-multiscreen-org:serviceId:dial</serviceId><controlURL>/ssdp/notfound</controlURL><eventSubURL>/ssdp/notfound</eventSubURL><SCPDURL>/ssdp/notfound</SCPDURL></service></serviceList></device></root>";
		final String testxml_toshiba = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\" xmlns:r=\"urn:restful-tv-org:schemas:upnp-dd\"><specVersion><major>1</major><minor>0</minor></specVersion><device><deviceType>urn:schemas-upnp-org:device:tvdevice:1</deviceType><friendlyName>REGZA-43Z700X</friendlyName><manufacturer>TOSHIBA</manufacturer><modelName>REGZA-43Z700X</modelName><UDN>uuid:20121222-2359-0000-0000-ec21e539e50a</UDN></device></root>";
		final String testxml_lg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\" xmlns:dlna=\"urn:schemas-dlna-org:device-1-0\"><specVersion><major>1</major><minor>0</minor></specVersion><device><deviceType>urn:dial-multiscreen-org:service:dial:1</deviceType><friendlyName>[LG] webOS TV UF6900</friendlyName><manufacturer>LG Electronics</manufacturer><manufacturerURL>http://www.lge.com</manufacturerURL><modelDescription></modelDescription><modelName>LG Smart TV</modelName><modelURL>http://www.lge.com</modelURL><modelNumber>WEBOS</modelNumber><serialNumber></serialNumber><UDN>uuid:7e827043-a871-3a5c-9a4d-7a7741454364</UDN><serviceList><service><serviceType>urn:dial-multiscreen-org:service:dial:1</serviceType><serviceId>urn:dial-multiscreen-org:serviceId:dial</serviceId><SCPDURL>/WebOS_Dial/7e827043-a871-3a5c-9a4d-7a7741454364/scpd.xml</SCPDURL><controlURL>/WebOS_Dial/7e827043-a871-3a5c-9a4d-7a7741454364/control.xml</controlURL><eventSubURL>/WebOS_Dial/7e827043-a871-3a5c-9a4d-7a7741454364/event.xml</eventSubURL></service></serviceList></device></root>";
		final String testxml_panasonic = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\" xmlns:vli=\"urn:schemas-panasonic-com:vli\" xmlns:viera=\"urn:schemas-panasonic-com:viera\" xmlns:pxn=\"urn:schemas-panasonic-com:pxn\"><specVersion><major>1</major><minor>0</minor></specVersion><device><deviceType>urn:panasonic-com:device:p00RemoteController:1</deviceType><friendlyName>55GX850_B509</friendlyName><manufacturer>Panasonic</manufacturer><modelName>Panasonic VIErA</modelName><modelNumber>TH-55GX850</modelNumber><UDN>uuid:4D454930-0200-1000-8001-80C7558D5D8B</UDN><viera:X_DMSUDN>uuid:4D454930-0000-1000-8001-80C7558D5D8B</viera:X_DMSUDN><viera:X_DMRUDN>uuid:4D454930-0100-1000-8001-80C7558D5D8B</viera:X_DMRUDN><viera:X_NRCUDN>uuid:4D454930-0200-1000-8001-80C7558D5D8B</viera:X_NRCUDN><vli:X_MHC_DEVICE_ID>3015126510317666</vli:X_MHC_DEVICE_ID><viera:X_VERSION>NRC-4.00</viera:X_VERSION><viera:X_DEVICE_TYPE>DTV</viera:X_DEVICE_TYPE><viera:X_KEY_TYPE>JP-8,JP-7,JP-1</viera:X_KEY_TYPE><viera:X_PAD_TYPE>15-1</viera:X_PAD_TYPE><viera:X_NRC_ID>80C7558D5D8B</viera:X_NRC_ID><viera:X_NRCCAP>VR_POWER,VR_DMR,VR_DMS,VR_DRC,VR_VECTOR,VR_BROWSER,VR_LAUNCH,VR_RECDMS,VR_TUNERDMS,VR_MEDIADMS,VR_LVDMS,VR_VGADMS,VR_720PDMS,VR_UPDMS,VR_TUNER3,VR_WOL,VR_OWNPLAY,VR_XRC,VR_MES,VR_UPBROWSER,VR_MC,VR_AREKORE</viera:X_NRCCAP><pxn:X_DRC_VERSION>DRC-1.00</pxn:X_DRC_VERSION><pxn:X_DRC_PORT>80</pxn:X_DRC_PORT><pxn:HYC_CAP>HYC-1.00</pxn:HYC_CAP><iconList><icon><mimetype>image/png</mimetype><width>48</width><height>48</height><depth>24</depth><url>/nrc/dlna_icon_48.png</url></icon><icon><mimetype>image/png</mimetype><width>120</width><height>120</height><depth>24</depth><url>/nrc/dlna_icon_120.png</url></icon><icon><mimetype>image/jpeg</mimetype><width>48</width><height>48</height><depth>24</depth><url>/nrc/dlna_icon_48.jpg</url></icon><icon><mimetype>image/jpeg</mimetype><width>120</width><height>120</height><depth>24</depth><url>/nrc/dlna_icon_120.jpg</url></icon></iconList><serviceList><service><serviceType>urn:panasonic-com:service:p00NetworkControl:1</serviceType><serviceId>urn:upnp-org:serviceId:p00NetworkControl</serviceId><SCPDURL>/nrc/sdd_0.xml</SCPDURL><controlURL>/nrc/control_0</controlURL><eventSubURL>/nrc/event_0</eventSubURL></service></serviceList></device></root>";


		Source file_sony1 = new StreamSource(getClass().getClassLoader().getResourceAsStream("dd_sony1.xml"));
		Source file_sony2 = new StreamSource(getClass().getClassLoader().getResourceAsStream("dd_sony2.xml"));
		Source file_sumidenstb = new StreamSource(getClass().getClassLoader().getResourceAsStream("dd_sumidenstb.xml"));
		Source file_toshiba = new StreamSource(getClass().getClassLoader().getResourceAsStream("dd_toshiba.xml"));
		Source file_lg = new StreamSource(getClass().getClassLoader().getResourceAsStream("dd_lg.xml"));
		Source file_panasonic = new StreamSource(getClass().getClassLoader().getResourceAsStream("dd_panasonic.xml"));

		// when -- xUnit Naming TestFrame
		//// Call validateMethodWithXMLSchama(DDXML.xsd)
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			// Schema インスタンスを生成
			Schema schema = factory.newSchema(new File(stdSchemaPath));
			// Validator インスタンスを生成
			Validator validator = schema.newValidator();
			// 妥当性検証を実行
			validator.validate(new StreamSource(new StringReader(testxml_stdexample)));
			//validator.validate(new StreamSource(new StringReader(testxml_sony1)));
			validator.validate(file_sony1);
			validator.validate(file_sony2);
			validator.validate(file_sumidenstb);
			validator.validate(file_toshiba);
			//validator.validate(file_lg);
			validator.validate(file_panasonic);
			//validator.validate(new StreamSource(new StringReader(testxml_sony2)));
			//validator.validate(new StreamSource(new StringReader(testxml_sumidenstb)));
			//validator.validate(new StreamSource(new StringReader(testxml_toshiba)));
			//validator.validate(new StreamSource(new StringReader(testxml_lg)));
			//validator.validate(new StreamSource(new StringReader(testxml_panasonic)));
			//validationResult = ssdpcp.validateDDXML(testxml);
			validationResult = true;
		}catch(SAXException ex){
			System.out.println(ex);
		}catch(IOException e) {
			//throw new RuntimeException(e);
		}


		// then -- xUnit Naming TestFrame
		//// TestAssertion
		assertThat(validationResult ,is(true));

	}


}
