//
// Sample for Hybridcast Connect SDK(hyconet4j)
//
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.io.IOException;
import java.io.InputStream;
import java.io.*;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;
import org.json.*;



import jp.or.nhk.rd.hyconet4j.TVRCMan;
import jp.or.nhk.rd.hyconet4j.TVRCStatus;
import jp.or.nhk.rd.hyconet4j.TVRCDevinfo;
import jp.or.nhk.rd.hyconet4j.SSDPCP;
import jp.or.nhk.rd.hyconet4j.TVRCSetURL;
import jp.or.nhk.rd.hyconet4j.HCListener;

import org.cybergarage.util.Debug;

// TestCaseChecker
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

// Json-Schema-Validator
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.everit.json.schema.Validator;
import org.everit.json.schema.ValidationException;

public class TestHCEX  {
	private static void devinfo_disp(TVRCDevinfo devinfo) {
		try{
			if( devinfo.getTVRC() != null ) {
				System.out.print("  IP[" + devinfo.ipaddr + "]\t");
				System.out.print("  deviceProfile[" + devinfo.getTVRC().getProfile() + "]\t"); // 修正、getMakerではなくgetProfileへの変更を検討します
				System.out.print("  manufacturer[" + devinfo.manufacturer+ "]\t");  // 追加
				System.out.print("  ModelName[" + devinfo.modelName + "]\t");
				System.out.print("  FriendlyName[" +devinfo.friendlyName + "]");
				System.out.println("  DevType[" +devinfo.getDevType() + "]");
				System.out.print("\tLocation[" + devinfo.location + "]");
				System.out.println(" ApplicationURL[" + devinfo.applicationURL + "]");
		//		System.out.print("  UUID[" + devinfo.uuid + "]");
			}
		}catch(Exception e){
				System.out.println("Some Error Occurs in Displaying deviceinfo : " + e);
		}
	}

	@Test
	public static void main(String[] args) throws Exception {

System.out.println("Start");

		TVRCDevinfo tvdev = null;
		Scanner scan = new Scanner(System.in);

		TVRCMan tvrcman = new TVRCMan() {
			private int devcount = 0 ;
			//探索結果を逐次受け取る処理のOverride
	//		public void devinfo(TVRCDevinfo devinfo) {
		//		System.out.print("Device Found[" + devcount++ + "]: " );
			//	devinfo_disp(devinfo);
		//	}
			public void onDeviceRegistered(TVRCDevinfo devinfo) {
				System.out.print("##Device Registered[" + devcount++ + "]: " );
				devinfo_disp(devinfo);
			}
		};

		if( 2 == args.length ) {
			//メーカー名、IPAddressを直接指定する場合
			String maker  = args[0] ;
			String ipaddr = args[1] ;
			String uuid = "";
			tvdev = new TVRCDevinfo(maker, ipaddr, "");
			if( tvdev == null ) {
				System.out.println("Please Check Parameters.");
				return ;
			}
		}
		else {
			//探索用再設定
			tvrcman = new TVRCMan(new String[] {
					"HCXPGenericTVRC"
				}) {

				private int devcount = 0 ;
				//探索結果を逐次受け取る処理のOverride
				public void onDeviceRegistered(TVRCDevinfo devinfo) {
					System.out.print("--------------Device Registered[" + devcount++ + "]: " );
					devinfo_disp(devinfo);
				}
			};

			//探索
			testSearch(tvrcman);
			// 探索結果表示
			//showDeviceList(tvrcman);

			//機器の選択
			int dev_index = (-1) ;
			List<TVRCDevinfo> devList = Arrays.asList();
			devList = tvrcman.getTVRCDevList();
			while( (dev_index < 0) || (devList.size() <= dev_index) ) {
				try {
					System.out.print("番号(0から" + (devList.size()-1) + ")を選択して下さい: ");
					String index_str = scan.nextLine();
					dev_index = Integer.parseInt(index_str);

					if( (0 <= dev_index) && (dev_index < devList.size()) ) {
						System.out.print("\n選択された番号:" + index_str);
						tvdev = tvrcman.getTVRCDevinfo(dev_index);
						devinfo_disp(tvdev);
					}
				}
				catch(NumberFormatException e) {
				}
			}
		}

		//set CallbackListener
		tvdev.setHCListener(
			new HCListener(){
				@Override
				public void setUrlReceived(TVRCSetURL seturl) {
					System.out.println( "*** TestTVRC::setURLReceived" );
					//System.out.println( "eventName=" + seturl.eventName );
					System.out.println( "url=" + seturl.url );
					System.out.println( "title=" + seturl.title );
					System.out.println( "desc=" + seturl.desc );
					System.out.println( "open=" + seturl.isAutoOpen );
				}

				@Override
				public void sendTextReceived(String str) {
					System.out.println( "*** TestTVRC::sendTextReceived" );
					System.out.println( "sendTextReceived=" + str );
					assertNotNull(str);
				}

				@Override
				public void wsDataReceived(String str) {
					System.out.println( "*** TestTVRC::wsDataReceived" );
					System.out.println( "wsDataReceived=" + str );
					assertNotNull(str);
				}
			}
		);


		Debug.getDebug().off();


		//機器へのコマンド送出
		while(true) {
			System.out.print("\nコマンドを入力して下さい（VK_を除く）: ");
			String[] inputstr = scan.nextLine().split(" ");
			String cmdid = "VK_" + inputstr[0];
			String inputparam = "";
			String inputparam2 = "";
			String inputparam3 = "";
			String inputparam4 = "";
			String inputparam5 = "";
			String inputparam6 = "";
			String inputparam7 = "";
			if( 1 < inputstr.length ) {
				inputparam = inputstr[1];
				if(inputstr.length >= 3) inputparam2 = inputstr[2];
				if(inputstr.length >= 4) inputparam3 = inputstr[3];
				if(inputstr.length >= 5) inputparam4 = inputstr[4];
				if(inputstr.length >= 6) inputparam5 = inputstr[5];
				if(inputstr.length >= 7) inputparam6 = inputstr[6];
				if(inputstr.length >= 8) inputparam7 = inputstr[7];
			}
			// 通常コマンドもうけつけられるようにする
			if( cmdid.equals("VK_") ) {
				continue;
			}
			TVRCStatus status = null;


			switch(cmdid){

			case "VK_help": // コマンドと引数の関係などわかりにくいのでコマンドヘルプを作成
				showCommandList();
				break;
			case "VK_search": //機器サーチ
				testSearch(tvrcman);
				break;
			case "VK_showDeviceList": //機器選択
				showDeviceList(tvrcman);
				break;
			case "VK_select": //機器選択
				tvdev = testSelect(tvrcman, inputparam);
				break;
			case "VK_setbase": //機器選択
				String baseurl = tvdev.ipaddr + inputparam;
				tvdev.setRestBaseURL(baseurl);
				System.out.println(tvdev.restBaseURL);
				assertEquals(baseurl, tvdev.restBaseURL);
				break;
			case "VK_directselect": //機器選択(直接指定）
				tvdev = testDirectSelect(tvrcman, inputparam, inputparam2, inputparam3, inputparam4, inputparam5);
				break;
			//
			// HCEXProtocol APIs
			//
			case "VK_getmedia":
				status = tvdev.getAvailableMedia();
				System.out.println( "Code: " + status.status);
				System.out.println( "Body: " + status.body);
				System.out.println( "Error: " + status.err);
				assertNotNull(status);
				assertThat(status.status, isIn(Arrays.asList((-1), 200, 300, 400, 403, 404, 500, 503, 505, 50200, 50300, 50400, 50500)));
				jsonSchemaValidator("./hcxp-json-schema/availableMedia_schema.json", status);
				break;

			case "VK_getchannels":
				status = tvdev.getChannelInfo( inputparam );
				System.out.println( "Code: " + status.status);
				System.out.println( "Body: " + status.body);
				System.out.println( "Error: " + status.err);
				assertNotNull(status);
				assertThat(status.status, isIn(Arrays.asList((-1), 200, 300, 400, 403, 404, 500, 503, 505, 50200, 50300, 50400, 50500)));
				jsonSchemaValidator("./hcxp-json-schema/channels_schema.json", status);
				break;

			case "VK_startapp1":
				if(inputparam.equals("")) inputparam = "http://example.com/xml.ait";
				if(inputparam2.equals("")) inputparam2 = "1";
				if(inputparam3.equals("")) inputparam3 = "1";
				JSONObject chobj = new JSONObject();
				try{
					chobj.put("resource", new JSONObject()
							.put("original_network_id", 32736)
							.put("transport_stream_id", 32736)
							.put("service_id", 1024))
						.put("hybridcast", new JSONObject()
							.put("orgid", Integer.parseInt(inputparam2))
							.put("appid", Integer.parseInt(inputparam3))
							.put("aiturl", inputparam)
						);
				}catch(Exception e){
					System.out.println(e);
				}
				status = tvdev.startAITControlledApp( "app", chobj.toString() );
				System.out.println( "Code: " + status.status);
				System.out.println( "Body: " + status.body);
				System.out.println( "Error: " + status.err);
				assertNotNull(status);
				assertThat(status.status, isIn(Arrays.asList((-1), 200, 201, 300, 400, 403, 404, 500, 503, 505, 50200, 50300, 50400, 50500)));
				jsonSchemaValidator("./hcxp-json-schema/startAIT_response_schema.json", status);
				break;

			case "VK_startapp2":
				if(inputparam.equals("")) inputparam = "http://example.com/xml.ait";
				if(inputparam2.equals("")) inputparam2 = "1";
				if(inputparam3.equals("")) inputparam3 = "1";
				JSONObject chobj2 = new JSONObject();
				try{
					chobj2.put("resource", new JSONObject()
						.put("original_network_id", 32736)
						.put("transport_stream_id", 32736)
						.put("service_id", 1024))
					.put("hybridcast", new JSONObject()
						.put("orgid", Integer.parseInt(inputparam2))
						.put("appid", Integer.parseInt(inputparam3))
						.put("aiturl", inputparam)
					);
				}catch(Exception e){
					System.out.println(e);
				}
				status = tvdev.startAITControlledApp( "app", chobj2.toString() );
				System.out.println( "Code: " + status.status);
				System.out.println( "Body: " + status.body);
				System.out.println( "Error: " + status.err);
				assertNotNull(status);
				assertThat(status.status, isIn(Arrays.asList((-1), 200, 201, 300, 400, 403, 404, 500, 503, 505, 50200, 50300, 50400, 50500)));
				jsonSchemaValidator("./hcxp-json-schema/startAIT_response_schema.json", status);
				break;

			case "VK_tune1":
				JSONObject chobj3 = new JSONObject();
				try{
					chobj3.put("resource", new JSONObject()
						.put("original_network_id", 32736)
						.put("transport_stream_id", 32736)
						.put("service_id", 1024))
					.put("hybridcast", new JSONObject()
						.put("orgid", 0)
						.put("appid", 0)
						.put("aiturl", "")
					);
				}catch(Exception e){
					System.out.println(e);
				}
				status = tvdev.startAITControlledApp( "tune", chobj3.toString() );
				System.out.println( "Code: " + status.status);
				System.out.println( "Body: " + status.body);
				System.out.println( "Error: " + status.err);
				assertNotNull(status);
				assertThat(status.status, isIn(Arrays.asList((-1), 200, 201, 300, 400, 403, 404, 500, 503, 505, 50200, 50300, 50400, 50500)));
				jsonSchemaValidator("./hcxp-json-schema/startAIT_response_schema.json", status);
				break;
			case "VK_tune2":
				JSONObject chobj4 = new JSONObject();
				try{
					chobj4.put("resource", new JSONObject()
						.put("original_network_id", 32736)
						.put("transport_stream_id", 32736)
						.put("service_id", 1024))
					.put("hybridcast", new JSONObject()
						.put("orgid", 0)
						.put("appid", 0)
						.put("aiturl", "")
					);
				}catch(Exception e){
					System.out.println(e);
				}
				status = tvdev.startAITControlledApp( "tune", chobj4.toString() );
				System.out.println( "Code: " + status.status);
				System.out.println( "Body: " + status.body);
				System.out.println( "Error: " + status.err);
				assertNotNull(status);
				assertThat(status.status, isIn(Arrays.asList((-1), 200, 201, 300, 400, 403, 404, 500, 503, 505, 50200, 50300, 50400, 50500)));
				jsonSchemaValidator("./hcxp-json-schema/startAIT_response_schema.json", status);
				break;
			case "VK_startbia":
				if(inputparam.equals("")) inputparam = "http://127.0.0.1:8887/ait/testbia.ait";
				if(inputparam2.equals("")) inputparam2 = "0";
				if(inputparam3.equals("")) inputparam3 = "0";
				JSONObject chobj5 = new JSONObject();
				try{
					chobj5.put("resource", new JSONObject()
						.put("original_network_id", 32736)
						.put("transport_stream_id", 32736)
						.put("service_id", 1024))
					.put("hybridcast", new JSONObject()
						.put("orgid", inputparam2)
						.put("appid", inputparam3)
						.put("aiturl", inputparam)
					);
				}catch(Exception e){
					System.out.println(e);
				}
				status = tvdev.startAITControlledApp( "bia", chobj5.toString() );
				System.out.println( "Code: " + status.status);
				System.out.println( "Body: " + status.body);
				System.out.println( "Error: " + status.err);
				assertNotNull(status);
				assertThat(status.status, isIn(Arrays.asList((-1), 200, 201, 300, 400, 403, 404, 500, 503, 505, 50200, 50300, 50400, 50500)));
				jsonSchemaValidator("./hcxp-json-schema/startAIT_response_schema.json", status);
				break;
			case "VK_tune4k8k":
				JSONObject chobj6 = new JSONObject();
				try{
					chobj6.put("resource", new JSONObject()
						.put("original_network_id", 100)
						.put("tlv_stream_id", 101)
						.put("service_id", 1024))
					.put("hybridcast", new JSONObject()
						.put("orgid", 0)
						.put("appid", 0)
						.put("aiturl", inputparam)
					);
				}catch(Exception e){
					System.out.println(e);
				}
				status = tvdev.startAITControlledApp( "tune", chobj6.toString() );
				System.out.println( "Code: " + status.status);
				System.out.println( "Body: " + status.body);
				System.out.println( "Error: " + status.err);
				assertNotNull(status);
				assertThat(status.status, isIn(Arrays.asList((-1), 200, 201, 300, 400, 403, 404, 500, 503, 505, 50200, 50300, 50400, 50500)));
				jsonSchemaValidator("./hcxp-json-schema/startAIT_response_schema.json", status);
				break;
			case "VK_startapp4k8k":
				if(inputparam.equals("")) inputparam = "http://127.0.0.1:8887/ait/testbia.ait";
				if(inputparam2.equals("")) inputparam2 = "1";
				if(inputparam3.equals("")) inputparam3 = "1";
				JSONObject chobj7 = new JSONObject();
				try{
					chobj7.put("resource", new JSONObject()
						.put("original_network_id", 100)
						.put("tlv_stream_id", 101)
						.put("service_id", 1024))
					.put("hybridcast", new JSONObject()
						.put("orgid", Integer.parseInt(inputparam2))
						.put("appid", Integer.parseInt(inputparam3))
						.put("aiturl", inputparam)
					);
				}catch(Exception e){
					System.out.println(e);
				}
				status = tvdev.startAITControlledApp( "app", chobj7.toString() );
				System.out.println( "Code: " + status.status);
				System.out.println( "Body: " + status.body);
				System.out.println( "Error: " + status.err);
				assertNotNull(status);
				assertThat(status.status, isIn(Arrays.asList((-1), 200, 201, 300, 400, 403, 404, 500, 503, 505, 50200, 50300, 50400, 50500)));
				jsonSchemaValidator("./hcxp-json-schema/startAIT_response_schema.json", status);
				break;

			case "VK_gettaskstatus":
				status = tvdev.getTaskStatus();
				System.out.println( "Code: " + status.status);
				System.out.println( "Body: " + status.body);
				System.out.println( "Error: " + status.err);
				assertNotNull(status);
				assertThat(status.status, isIn(Arrays.asList((-1), 200, 300, 400, 403, 404, 500, 503, 505, 50200, 50300, 50400, 50500)));
				jsonSchemaValidator("./hcxp-json-schema/taskStatus_schema.json", status);
				break;

			case "VK_getinfo":
				status = tvdev.getDialAppResourceURL();
				status = tvdev.getDialAppInfo();
				System.out.println( "Code: " + status.status);
				System.out.println( "Body: " + status.body);
				System.out.println( "Error: " + status.err);
				break;

			case "VK_getstatus":
				status = tvdev.getReceiverStatus();
				System.out.println( "Code: " + status.status);
				System.out.println( "Body: " + status.body);
				System.out.println( "Error: " + status.err);
				assertNotNull(status);
				assertThat(status.status, isIn(Arrays.asList((-1), 200, 300, 400, 403, 404, 500, 503, 505, 50200, 50300, 50400, 50500)));
				jsonSchemaValidator("./hcxp-json-schema/receiverStatus_schema.json", status);
				break;

			case "VK_connws":
				status = tvdev.connWebsocket();
				System.out.println( "Code: " + status.status);
				System.out.println( "Body: " + status.body);
				System.out.println( "Error: " + status.err);
				assertNotNull(status);
				assertThat(status.status, isIn(Arrays.asList((-1), 200, 300, 400, 500, 50200, 50300, 50400)));
				break;

			case "VK_disconnws":
				status = tvdev.disconnWebsocket();
				System.out.println( "Code: " + status.status);
				System.out.println( "Body: " + status.body);
				System.out.println( "Error: " + status.err);
				assertNotNull(status);
				assertThat(status.status, isIn(Arrays.asList((-1), 200, 300, 400, 403, 404, 500, 503, 505, 50200, 50300, 50400, 50500)));
				break;

			case "VK_sendws":
				status = tvdev.sendWebsocket( "{\"message\":{\"devid\":\"hcex\", \"sendTextToHostDevice\":{\"text\":\"" + inputparam + "\"}}}" );
				System.out.println( "Code: " + status.status);
				System.out.println( "Body: " + status.body);
				System.out.println( "Error: " + status.err);
				assertNotNull(status);
				assertThat(status.status, isIn(Arrays.asList((-1), 200, 300, 400, 403, 404, 500, 503, 505, 50200, 50300, 50400, 50500)));
				break;

			case "VK_requrl":
				status = tvdev.sendWebsocket( "{\"control\":{\"devid\":\"hcex\", \"request\":{\"command\":\"setURLForCompanionDevice\"}}}" );
				System.out.println( "Code: " + status.status);
				System.out.println( "Body: " + status.body);
				System.out.println( "Error: " + status.err);
				assertNotNull(status);
				assertThat(status.status, isIn(Arrays.asList((-1), 200, 300, 400, 403, 404, 500, 503, 505, 50200, 50300, 50400, 50500)));
				break;
			case "VK_resession":
				// devinfo export as String(JSON)
				String devSessionJSONString = tvdev.export();
				System.out.println("Export Devinfo JSON String: " + devSessionJSONString);

				// Devinfo再作成
				TVRCDevinfo reGenDevInfo = new TVRCDevinfo().load(devSessionJSONString);

				assertThat(reGenDevInfo.applicationURL, is(tvdev.applicationURL)); // 過去のデバイスのデバイスAPIのbaseurlが一致する

				if(tvdev.applicationURL.equals(reGenDevInfo.applicationURL)) { // 過去のデバイスのデバイスAPIのbaseurlが一致したらdevinfoを置き換える
					tvdev = reGenDevInfo;
					status = tvdev.getDialAppResourceURL();//過去のデバイスのデバイスAPIの存在確認
					status = tvdev.getDialAppInfo(); //過去のデバイスのデバイスAPIの存在確認とパラメタ設定
					System.out.println("replace new Devinfo from devSession: " + reGenDevInfo.export());
				}

				break;
			default:
					System.out.println("error : 有効なコマンドではありません。tvdev = null !");
			}
		}
	}

	////////////////////// Test or Util Method //////////////////////////

	@Test
	private static void testSearch(TVRCMan Man){
		//探索
		while(true){
			Man.searchStart();
			try{
				Thread.sleep(7000);
			}
			catch(InterruptedException e){
			}
			Man.searchStop();

			if (Man.getTVRCDevList().size() >= 1){
				break;
			}
		}
		//探索結果の表示
		showDeviceList(Man);
	}


	@Test
	private static void showDeviceList(TVRCMan Man){
		List<TVRCDevinfo> devList = Arrays.asList();
		devList = Man.getTVRCDevList();
		for( int i=0; i < devList.size(); i++ ) {
			TVRCDevinfo tvdevinfo = (TVRCDevinfo)devList.get(i);
			System.out.print("DevInfo: " + i + "\n");
			devinfo_disp(tvdevinfo);
		}
	}


	@Test
	private static void showCommandList(){
		String cmdlistText = ""
		+ ">>HCEXAPI" + "\n"
		+ "------------" + "\n"
		+ "VK_getmedia [MEDIA:ALL/TD/BS/CS]" + "\n"
		+ "VK_getchannels" + "\n"
		+ "VK_startapp1 [AITURL]" + "\n"
		+ "VK_startapp2 [AITURL]" + "\n"
		+ "VK_tune1" + "\n"
		+ "VK_tune2" + "\n"
		+ "VK_startbia [AITURL]" + "\n"
		+ "VK_tune4k8k" + "\n"
		+ "VK_startapp4k8k [AITURL]" + "\n"
		+ "VK_gettaskstatus" + "\n"
		+ "VK_getstatus" + "\n"
		+ "VK_connws" + "\n"
		+ "VK_disconnws" + "\n"
		+ "VK_sendws" + "\n"
		+ "VK_requrl" + "\n"
		+ "" ;

		System.out.println( "help commandlist:\n" + cmdlistText);
	}


	@Test
	private static TVRCDevinfo testSelect(TVRCMan Man, String strNumber){
		if(strNumber == "") strNumber = "0";
		int dev_index = (-1) ;

		TVRCDevinfo tvDevInfo = null;
		List<TVRCDevinfo> devList = Arrays.asList();
		devList = Man.getTVRCDevList();
		try {
			dev_index = Integer.parseInt(strNumber);
			if((dev_index >= 0) && (devList.size() > dev_index)){
				System.out.print("\nselected number:" + dev_index);
				tvDevInfo = Man.getTVRCDevinfo(dev_index);
				devinfo_disp(tvDevInfo);
			}else{
				System.out.println("Enter in the range [0]-[" + (devList.size()-1) + "]");
			}

		}catch(NumberFormatException e) {
			System.out.println("error : enter a number.");
		}
		return tvDevInfo;
	}

	@Test
	private static TVRCDevinfo testExportImport(TVRCMan Man) {
		TVRCDevinfo tvDevInfo = null;
		TVRCDevinfo reGenDevInfo = null;
		String _jsonFileName = "devsession_exported.json";
		String exportedJsonStr = "";
		try{
			if( !"".equals(_jsonFileName)) {
				// ファイルexportする場合のファイル名
				// まず通常通りDevinfo作成
				tvDevInfo = new TVRCDevinfo("HCXPGenericTVRC", "192.168.0.99", "aaaa-bbbb-cccc-dddd", "http://192.168.0.99:8887/dd.xml", "http://192.168.0.99:8887/app/Hybridcast");
				// 外部保存用文字列出力
				exportedJsonStr = tvDevInfo.export();
				// ファイル保存

				writeFile(_jsonFileName, exportedJsonStr);
				String reSessionParams = readFile(_jsonFileName);

				// Devinfo再作成
				reGenDevInfo = new TVRCDevinfo().load(reSessionParams);
				//reGenDevInfo = new TVRCDevinfo().load(exportedJsonStr);

				// Devinfoが再セットされているか検証(actual, expected)
				assertThat(reGenDevInfo.ipaddr, is(tvDevInfo.ipaddr));
				assertThat(reGenDevInfo.uuid, is(tvDevInfo.uuid));
				assertThat(reGenDevInfo.location, is(tvDevInfo.location));
				assertThat(reGenDevInfo.applicationURL, is(tvDevInfo.applicationURL));
				assertThat(reGenDevInfo.sessInfo.appName, is(tvDevInfo.sessInfo.appName));

			}else{
				System.out.println("Please Check Parameters.");
			}
		}catch(Exception e){
				System.out.println("Some Exception Occuer in DevSession Export Test");
		}
		return reGenDevInfo;
	}


	@Test
	private static TVRCDevinfo testDirectSelect(TVRCMan Man, String strMaker, String strIPAddr, String strAppUrl, String strUuid, String strLocationURL) {
		TVRCDevinfo tvDevInfo = null;
		if( !"".equals(strMaker) && !"".equals(strIPAddr) && !"".equals(strAppUrl)) {
			//メーカー名、IPAddressを直接指定する場合
			tvDevInfo = new TVRCDevinfo(strMaker, strIPAddr, strUuid, strLocationURL, strAppUrl);
		}else{
			System.out.println("Please Check Parameters.");
		}
		return tvDevInfo;
	}


	// GreenRedMessage
	private static void GreenRedPrintln(String msg, String color){
		String colorGreen = "\u001b[00;32m";
		String colorRed = "\u001b[00;31m";
		String colorYellow = "\u001b[00;33m";
		String colorSkyblue = "\u001b[00;36m";
		String endOfStyle = "\u001b[00m";
		String printcolor = "\u001b[00m";
		switch(color){
			case "green": printcolor = colorGreen; break;
			case "red": printcolor = colorRed; break;
			case "yellow": printcolor = colorYellow; break;
			case "skyblue": printcolor = colorSkyblue; break;
			default : printcolor = endOfStyle;
		}
		System.out.println( printcolor + msg + endOfStyle);
	}

	// json schema validator
	private static boolean jsonSchemaValidator(String schemapath, TVRCStatus status){
		boolean teststatus = true;
		String expectedcode = "";

		//status.status(http headerのcode)と http bodyのhead->code が等しいかのチェック
		if (Objects.nonNull(status.body) && status.body != ""){
			try{
				JSONObject jsonObject = new JSONObject(status.body);
				Integer body_code = jsonObject.getJSONObject("head").getInt("code");

				if (status.status != body_code){
					System.out.println(">>>>>>>>>>>>     ERROR INFO    <<<<<<<<<<<<<<<<");
					GreenRedPrintln(">> status code mismatching in JSON : ERROR", "red");
					return false;
				}
			}catch (JSONException e) {
					System.out.println(">>>>>>>>>>>>     ERROR INFO    <<<<<<<<<<<<<<<<");
				GreenRedPrintln(">> status code missing in JSON : ERROR" , "red");
				return false;
			}
		}else{
			System.out.println(">>>>>>>>>>>>     ERROR INFO    <<<<<<<<<<<<<<<<");
			GreenRedPrintln(">> body is empty : ERROR", "red");
			System.out.println("===================================================");
			return false;
		}
		//schema check
		try (InputStream inputStream = TestHCEX.class.getResourceAsStream(schemapath)) {
			System.out.println( ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println( ">>>>>>>>>>>>         JSON-SCHEMA-VALIDATION   START       ");
			System.out.println( ">>>>> JSON-Schema:  " + schemapath );
			JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
			Schema schema = SchemaLoader.load(rawSchema);

			try {
				schema.validate(new JSONObject(status.body));

				System.out.println("============================================================");
				GreenRedPrintln("=======    json-schema validation result : PASS    =========", "green");
				//System.out.println( colorGreenStart + "=======    json-schema validation result : PASS    =========" + endOfStyle);
				System.out.println("============================================================");
				System.out.println("");
			} catch (ValidationException e) {
				GreenRedPrintln( "=======    json-schema-validation : ERROR    =======", "red");
				GreenRedPrintln(e.getMessage(), "red");
				e.getCausingExceptions().stream()
						.map(ValidationException::getMessage)
						.forEach(System.out::println);
				teststatus = false;
			}
		}catch(IOException e){
			System.out.println(">>>>>>>>>>>>     ERROR INFO    <<<<<<<<<<<<<<<<");
			//System.out.println( colorYellowStart +"read-json-schema-template : ERROR > " + endOfStyle);
			GreenRedPrintln( "read-json-schema-template : ERROR > ", "red");
			//System.out.println( "read-json-schema-template : ERROR > ");
			teststatus = false;
		}catch(NullPointerException e){
			System.out.println(">>>>>>>>>>>>     ERROR INFO    <<<<<<<<<<<<<<<<");
			GreenRedPrintln( "NullPointerException : ERROR > e ", "red");
		}finally{
		}
				System.out.println("=================================================================");
				System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		return teststatus;
	}


	private static void writeFile(String filepath, String text){
		try{
			File file = new File(filepath);
			FileWriter filewriter = new FileWriter(file);
			filewriter.write(text);
			filewriter.close();
		}catch(FileNotFoundException e){
			System.out.println(e);
		}catch(IOException e){
			System.out.println(e);
		}
	}

	private static String readFile(String filepath){
        try{
            File file = new File(filepath);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String data = "";
            String str = br.readLine();
            while(str != null){
                data += str;
                str = br.readLine();
            }
            br.close();
            return data;
        }catch(FileNotFoundException e){
            System.out.println(e);
            return null;
        }catch(IOException e){
            System.out.println(e);
            return null;
        }
    }
}
