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


		// then -- xUnit Naming TestFrame
		//// TestAssertion
		assertThat(validationResult ,is(true));

	}


}
