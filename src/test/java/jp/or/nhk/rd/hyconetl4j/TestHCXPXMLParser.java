//
// Test for XMLParser
//
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.*;

import jp.or.nhk.rd.hyconet4j.TVRCStatus;
import jp.or.nhk.rd.hyconet4j.TVRCDevinfo;
import jp.or.nhk.rd.hyconet4j.SSDPCP;
import jp.or.nhk.rd.hyconet4j.TVRC;
import jp.or.nhk.rd.hyconet4j.ActHCEX;
import jp.or.nhk.rd.hyconet4j.HCListener;


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

public class TestHCXPXMLParser {

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


		SSDPCP ssdpcp = new SSDPCP() {
			@Override
			public Boolean validateDDXML(String XMLString) throws Exception, SAXException{
				//validation process
				return true;
			}
		};
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
			//validationResult = ssdpcp.validateDDXML(testxml);
			validationResult = true;
		}catch(SAXException ex){
			System.out.println(ex);
		}catch(IOException e) {
			//throw new RuntimeException(e);
		}

		//Boolean validationResult = ssdpcp.validateDDXML(testxml);

		// then -- xUnit Naming TestFrame
		//// TestAssertion
		assertThat(validationResult ,is(true));

	}

	@Test
    public void validateDialAppinfoXML_IPTVFJ_STD0013_7_2() throws Exception {
		// IPTVFJ-STD0013-7.2.1.1.2 additionalData
		//               -J-2 DialApplicationInformationXML Schema
		// given -- xUnit Naming TestFrame

		Boolean validationResult = false;
		String basePath = "src/main/resources";
		String schemaFileName = "dialAppInfo_HybridcastConnect_schema_import.xsd";
		String stdSchemaPath= basePath + "/" + schemaFileName;

		ActHCEX hcex = new ActHCEX();
		//// testcase2
		final String testxml_namespace = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
						"<service xmlns=\"urn:dial-multiscreen-org:schemas:dial\" " +
							"xmlns:iptv=\"urn:iptv:HybridcastApplication:2020\" dialVer=\"1.7\">" +
							"<name>Hybridcast Application</name>" +
							"<options allowStop=\"false\"/>" +
							"<state>running</state>" +
							"<link rel=\"run\" href=\"run\"/>" +
							"<additionalData>" +
								"<iptv:X_Hybridcast_ProtocolVersion>2.1</iptv:X_Hybridcast_ProtocolVersion>" +
								"<iptv:X_Hybridcast_App2AppURL>ws://192.168.1.11:1234/84fa-9fd3-33a1-2481-9098-3ccd-de26-a223/hybridcast</iptv:X_Hybridcast_App2AppURL>" +
								"<iptv:X_Hybridcast_TVControlURL>http://192.168.1.11:1235/hybridcast/tvcontrol</iptv:X_Hybridcast_TVControlURL>" +
								"<iptv:X_Hybridcast_ServerInfo>CCS/1.0;2.1;000000;0000000000;This-is-comment</iptv:X_Hybridcast_ServerInfo>" +
								"<iptv:X_Hybridcast_ExternalLaunch allowBroadcastOrientedManagedApp=\"true\" allowBroadcastIndependentManagedApp=\"true\" />" +
							"</additionalData>" +
						"</service>"
		;

		final String testxml_nonamespace = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<service xmlns=\"urn:dial-multiscreen-org:schemas:dial\" " +
			"dialVer=\"1.7\">" +
			"<name>Hybridcast Application</name>" +
			"<options allowStop=\"false\"/>" +
			"<state>running</state>" +
			"<link rel=\"run\" href=\"run\"/>" +
			"<additionalData xmlns=\"urn:iptv:HybridcastApplication:2020\" >" +
				"<X_Hybridcast_ProtocolVersion>2.1</X_Hybridcast_ProtocolVersion>" +
				"<X_Hybridcast_App2AppURL>ws://192.168.1.11:1234/84fa-9fd3-33a1-2481-9098-3ccd-de26-a223/hybridcast</X_Hybridcast_App2AppURL>" +
				"<X_Hybridcast_TVControlURL>http://192.168.1.11:1235/hybridcast/tvcontrol</X_Hybridcast_TVControlURL>" +
				"<X_Hybridcast_ServerInfo>CCS/1.0;2.1;000000;0000000000;This-is-comment</X_Hybridcast_ServerInfo>" +
				"<X_Hybridcast_ExternalLaunch allowBroadcastOrientedManagedApp=\"true\" allowBroadcastIndependentManagedApp=\"true\" />" +
			"</additionalData>" +
		"</service>"
;
		// when -- xUnit Naming TestFrame
        //// Call validateMethodWithXMLSchama(appinfoXML.xsd)
		String baseAbsolutePath = "";
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		Source schemaFile = new StreamSource(getClass().getClassLoader().getResourceAsStream(schemaFileName));
		
		//factory.setResourceResolver(new ResourceResolver(baseAbsolutePath));
		factory.setResourceResolver(new LSResourceResolver(){
            @Override
            public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
				LSInput input = new DOMInputImpl();
                String filePath = baseAbsolutePath.endsWith("/") ? baseAbsolutePath : baseAbsolutePath + "/";
				InputStream stream = this.getClass().getClassLoader().getResourceAsStream(systemId);
				if (stream == null) {
					System.out.println("xsd file another path:" + filePath+ "/" + systemId);
					stream = this.getClass().getClassLoader().getResourceAsStream(filePath + systemId);
				} else {
					System.out.println("xsd file path: " + systemId);
					//pathHistory = getNormalizedPath(systemId);
					//stream = this.getClass().getClassLoader().getResourceAsStream(pathHistory + systemId);
				}
				try{
					input.setPublicId(publicId);
					input.setSystemId(systemId);
					input.setBaseURI(baseURI);
					input.setEncoding("UTF-8");        
					input.setCharacterStream(new InputStreamReader(stream));
					return input;
				}catch(Exception e){
					System.out.println(e);
					return null;
				}
            }

		});  

		try {
			// Schema インスタンスを生成
			//Schema schema = factory.newSchema(new File(stdSchemaPath));
			Schema schema = factory.newSchema(schemaFile);
			// Validator インスタンスを生成
			Validator validator = schema.newValidator();
	
			// 妥当性検証を実行
			System.out.println(testxml_namespace);
			validator.validate(new StreamSource(new StringReader(testxml_namespace)));

			System.out.println(testxml_nonamespace);
			//validator.validate(new StreamSource(new StringReader(testxml_nonamespace)));
	
			validationResult = true;
		}catch(SAXException ex){
			System.out.println(ex);
			throw new RuntimeException(ex);
		//}catch(IOException e) {
			//throw new RuntimeException(e);
		}

		// validationResult = hcex.validateDialAppinfoXML(testxml);

		// then -- xUnit Naming TestFrame
		//// TestAssertion
		assertThat(validationResult ,is(true));


	}


    @Test
    public void parseDialAppinfoXML_IPTVFJ_STD0013_7_2_InformativeDescription() throws Exception {
		// IPTVFJ-STD0013-7.2.1.1.2-graph7.5 informative description

		// given -- xUnit Naming TestFrame

		ActHCEX hcex = new ActHCEX();
		//// testcase2
		final String testxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
						"<service xmlns=\"urn:dial-multiscreen-org:schemas:dial\" " +
							"xmlns:iptv=\"urn:iptv:HybridcastApplication:2015\" dialVer=\"1.7\">" +
							"<name>Hybridcast Application</name>" +
							"<options allowStop=\"false\"/>" +
							"<state>running</state>" +
							"<additionalData>" +
								"<iptv:X_Hybridcast_App2AppURL>ws://192.168.1.11:1234/84fa-9fd3-33a1-2481-9098-3ccd-de26-a223/hybridcast</iptv:X_Hybridcast_App2AppURL>" +
								"<iptv:X_Hybridcast_TVControlURL>http://192.168.1.11:1235/hybridcast/tvcontrol</iptv:X_Hybridcast_TVControlURL>" +
								"<iptv:X_Hybridcast_ServerInfo>CCS/1.0;2.0;000000;0000000000;This-is-comment</iptv:X_Hybridcast_ServerInfo>" +
							"</additionalData>" +
						"</service>"
		;
		// when -- xUnit Naming TestFrame
        //// Call parseMethod
		Map<String,String> endpoints  = (Map<String,String>) this.doPrivateMethod(
			hcex, "parseDialAppinfoXMLXpath", new Class[]{String.class}, new Object[]{ testxml });


		// then -- xUnit Naming TestFrame
		//// TestAssertion

		//// ServerInfo
		String serverInfo = endpoints.get("SERVER_INFO");
		assertThat(serverInfo ,is("CCS/1.0;2.0;000000;0000000000;This-is-comment"));

		////  ProtocolVersion
		String protocolVersion = endpoints.get("PROTOCOL_VERSION");
		assertThat(protocolVersion ,is("2.0"));

		//// BaseUrl
		String restBaseUrl = endpoints.get("BASEURL");
		assertThat(restBaseUrl,is("http://192.168.1.11:1235/hybridcast/tvcontrol"));

		//// WebsocketURL
		String wsUrl = endpoints.get("WSURL");
		assertThat(wsUrl, is("ws://192.168.1.11:1234/84fa-9fd3-33a1-2481-9098-3ccd-de26-a223/hybridcast"));
	}

	@Test
	public void parseDialAppinfoXML_For_MakerPattern1() throws Exception {

		// given -- xUnit Naming TestFrame

		ActHCEX hcex = new ActHCEX();
		// testcase2
		final String testxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
						"<service xmlns=\"urn:dial-multiscreen-org:schemas:dial\" " +
							"dialVer=\"1.7\">" +
							"<name>Hybridcast Application</name>" +
							"<options allowStop=\"false\"/>" +
							"<state>running</state>" +
							"<link rel=\"run\" href=\"run\"/>" +
							"<additionalData xmlns=\"urn:iptv:HybridcastApplication:2015\">" +
								"<X_Hybridcast_App2AppURL>ws://10.229.229.29:4567</X_Hybridcast_App2AppURL>" +
								"<X_Hybridcast_TVControlURL>http://10.229.229.29:59000</X_Hybridcast_TVControlURL>" +
								"<X_Hybridcast_ServerInfo>tenxxxx/1.0;2.0;8888;8888;tenxxxx</X_Hybridcast_ServerInfo>" +
							"</additionalData>" +
						"</service>"
		;
		// when -- xUnit Naming TestFram
        // Call parseMethod
		Map<String,String> endpoints  = (Map<String,String>) this.doPrivateMethod(
			hcex, "parseDialAppinfoXMLXpath", new Class[]{String.class}, new Object[]{ testxml });

		// then -- xUnit Naming TestFrame
		//// TestAssertion

		// ServerInfo
		String serverInfo = endpoints.get("SERVER_INFO");
		assertThat(serverInfo ,is("tenxxxx/1.0;2.0;8888;8888;tenxxxx"));

		//  ProtocolVersion
		String protocolVersion = endpoints.get("PROTOCOL_VERSION");
		assertThat(protocolVersion ,is("2.0"));

		// BaseUrl
		String restBaseUrl = endpoints.get("BASEURL");
		assertThat(restBaseUrl,is("http://10.229.229.29:59000"));

		// WebsocketURL
		//String wsUrl = targetClass.getWSURL();
		String wsUrl = endpoints.get("WSURL");
		assertThat(wsUrl, is("ws://10.229.229.29:4567"));
	}


    @Test
    public void parseDialAppinfoXML_IPTVFJ_STD0013_7_2_InformativeDescription_29() throws Exception {
		// given -- xUnit Naming TestFrame

		ActHCEX hcex = new ActHCEX();
		//// testcase3
		final String testxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
						"<service xmlns=\"urn:dial-multiscreen-org:schemas:dial\" " +
							"xmlns:iptv=\"urn:iptv:HybridcastApplication:2015\" dialVer=\"1.7\">" +
							"<name>Hybridcast Application</name>" +
							"<options allowStop=\"false\"/>" +
							"<state>running</state>" +
							"<additionalData>" +
								"<iptv:X_Hybridcast_App2AppURL>ws://192.168.1.11:1234/84fa-9fd3-33a1-2481-9098-3ccd-de26-a223/hybridcast</iptv:X_Hybridcast_App2AppURL>" +
								"<iptv:X_Hybridcast_TVControlURL>http://192.168.1.11:1235/hybridcast/tvcontrol</iptv:X_Hybridcast_TVControlURL>" +
								"<iptv:X_Hybridcast_ServerInfo>CCS/1.0;2.1;000000;0000000000;This-is-comment</iptv:X_Hybridcast_ServerInfo>" +
								"<iptv:X_Hybridcast_ProtocolVersion>2.1</iptv:X_Hybridcast_ProtocolVersion>" + 
								"<iptv:X_Hybridcast_ExternalLaunch allowBroadcastOrientedManagedApp=\"true\" allowBroadcastIndependentManagedApp=\"false\"/>" +
							"</additionalData>" +
						"</service>"
		;
		// when -- xUnit Naming TestFrame
        //// Call parseMethod
		Map<String,String> endpoints  = (Map<String,String>) this.doPrivateMethod(
			hcex, "parseDialAppinfoXMLXpath", new Class[]{String.class}, new Object[]{ testxml });


		// then -- xUnit Naming TestFrame
		//// TestAssertion

		//// ServerInfo
		String serverInfo = endpoints.get("SERVER_INFO");
		assertThat(serverInfo ,is("CCS/1.0;2.1;000000;0000000000;This-is-comment"));

		////  ProtocolVersion
		String protocolVersion = endpoints.get("PROTOCOL_VERSION");
		assertThat(protocolVersion ,is("2.1"));

		//// BaseUrl
		String restBaseUrl = endpoints.get("BASEURL");
		assertThat(restBaseUrl,is("http://192.168.1.11:1235/hybridcast/tvcontrol"));

		//// WebsocketURL
		String wsUrl = endpoints.get("WSURL");
		assertThat(wsUrl, is("ws://192.168.1.11:1234/84fa-9fd3-33a1-2481-9098-3ccd-de26-a223/hybridcast"));

		// After Ver2.1
		//// ProtocolVersion
		String allowBroadcastOrientedManagedApp = endpoints.get("allowBroadcastOrientedManagedApp");
		assertThat(allowBroadcastOrientedManagedApp ,is("true"));
		String allowBroadcastIndependentManagedApp = endpoints.get("allowBroadcastIndependentManagedApp");
		assertThat(allowBroadcastIndependentManagedApp ,is("false"));
	}




		/**
     * 非staticメソッド呼び出し.
     *
     * @param obj テスト対象オブジェクト
     * @param name テスト対象メソッド名
     * @param types テスト対象メソッドの引数の型
     * @param args テスト対象メソッドの引数
     * @return 非staticメソッドの戻り値
     */

	private Object doPrivateMethod (
		Object obj, String name, Class[] parameterTypes, Object[] args) throws Exception {
			Method method = obj.getClass().getDeclaredMethod(name, parameterTypes);
			method.setAccessible(true);
			Object objst = method.invoke(obj, args);

			//System.out.println("doPrivateMethod: " + name);
			//System.out.println(objst.getClass());

			return objst;
	   }
}


