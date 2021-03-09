package jp.or.nhk.rd.hyconet4j;

//
// TVRC Manager
//
import java.util.List;

import org.xml.sax.SAXException;

import java.util.ArrayList;

import java.lang.ClassNotFoundException;
import java.lang.InstantiationException;
import java.lang.IllegalAccessException;

/**
 * TV Control Manager Class for Hybridcast-Connect.
 * Hybridcast-Connectのリモートコントロール対象デバイスや適用するデバイスプロファイルの管理をするClass。
 * 
 */
public class TVRCMan {
	private _Logger logger = null;

	// TVRCManが管理するリモートデバイスプロファイル(TVRCオブジェクト）のリスト
	private List<TVRC> tvrc = new ArrayList<TVRC>() {
		{
			add( new HCXPGenericTVRC() );
		}
	};

	// TVRCManが管理するサーチして利用可能なデバイス情報オブジェクトのリスト
	private List<TVRCDevinfo> devList = new ArrayList<TVRCDevinfo>();

	// SSDPControlPointによるサーチ処理用インスタンス
	private SSDPCP ssdpcp = new SSDPCP() {
		// implement logic when device found in search process
		@Override
		public void onDeviceInfoPrepared(String location, String urn, String device_urn, String usn, String manufacturer, String fname, String mname, String application_url) {
			String ipaddr = location.split("/")[2].split(":")[0] ;

			// サーチした結果、IPが同じでもサービスが異なるケースは、application-URLが異なる場合があるので、別devinfo生成する。
			TVRCDevinfo dev = getTVRCDevinfoByIpaddrUrn( ipaddr, urn ) ;

			if(dev!=null){
				logger.debug("onDeviceInfoPrepared in TVRCMan: " + ((dev.location!=null)?dev.location:"null") + "  " + ((location!=null)?location:"null") );
			}

			// locationが異なる場合、URN/manufacturerパターンが一致しない場合は、別devinfo生成
			if( ((dev == null) && (getTVRCbyURN(urn, device_urn, manufacturer) != null)) || ((dev != null) && !dev.location.equals(location)) ) {
				dev = new TVRCDevinfo();
				dev.setTVRC( getTVRCbyURN(urn, device_urn, manufacturer) );
				dev.setHCEX( new ActHCEX() );
				dev.setIPAddress( location.split("/")[2].split(":")[0] ) ;
				dev.manufacturer = manufacturer ;
				dev.friendlyName = fname ;
				dev.modelName = mname ;
				dev.applicationURL = application_url;

				devList.add(dev);
			}

			if(dev != null) {
logger.trace("onDeviceInfoPrepared in TVRCMan: usn:" + usn );
				// USNが存在するときは正常なサーチ
				if( (usn != null) && (!usn.equals("")) ) {
					dev.location = location ;
					dev.uuid = usn.split("::")[0].split(":")[1] ;
				}
				else {// USNがないケース: may be bug
					logger.debug("check Application-URL");
					if( application_url != null ) {
						dev.applicationURL = application_url;

						//Get HCEX APIInfo
						try {
							logger.debug("Get HCEX DIAL INFOAPI");
							dev.getDialAppInfo();
						}
						catch(Exception e) {
							logger.error("Get HCEX DIAL NG");
						}
					}
				}
				// HCEXチェックは一番最後
				//Check HCEXEmulatorかどうかをチェック
				// Antwappの場合は:8887/apps/antwappを狙い撃ちしてAppinfoを取得できればdevtype=HCEmulater
				try {
					logger.debug("Check device Type: HCEX");
					dev.getDialAppResourceURL();
					dev.getDialAppInfo();
					//devinfo( dev ); // deprecated?
					onDeviceRegistered(dev);
				}
				catch(Exception e) {
					logger.error("Check device Type: HCEX NG " + e);
				}
			logger.trace("onDeviceInfoPrepared in TVRCMan: " + dev.getTVRC().get_maker() + " " + location + " " + urn + " " + dev.uuid + " " + fname + " " + mname);
			}
		}
		@Override
		public Boolean validateDDXML(String XMLString) throws Exception, SAXException{
			//validation process
			
			return true;
		}
		@Override
		public void setDDXMLSchema(String schemaPath) throws Exception{
			// setSchema
		}
	};

	/**
	 * TV Remote Control Manager Constructor
	 * Hybridcast-Connectのリモートコントロール対象デバイスや適用するデバイスプロファイルの管理をするためのconstructor/initializer。
	 * 
	 */
	public TVRCMan() {
		logger = new _Logger("TVRCMan");

		List<String> urns = new ArrayList<String>();

		// HCEX Protocol should have urn as "DIAL"(UPnP)

		for( TVRC tv : tvrc ) {
			String service_urn = tv.get_service_urn();
			if( !service_urn.equals("") ) {
				boolean registerd = false;
				for( String urn : urns ) {
					if( urn.equals(service_urn) ) {
						registerd = true;
						break;
					}
				}

				if( !registerd ) {
					urns.add( service_urn );
				}
			}
		}

		ssdpcp.setUrns(urns);
	}

	/**
	 * TV Remote Control Manager Class Constructor by setting device profile.
	 * Hybridcast-Connectのリモートコントロール対象デバイスや適用するデバイスプロファイルの管理をするためのconstructor/initializer。
	 * 
	 * @param devclasses the classname defined device profile
	 */
	public TVRCMan( String[] devclasses ) {
		logger = new _Logger("TVRCMan");

		List<String> urns = new ArrayList<String>();

		// HCEX Protocol should have urn as "DIAL"
		//urns.add("urn:dial-multiscreen-org:service:dial:1");


		tvrc.clear();
		for( String classname : devclasses ) {
//logger.debug("classname: " + classname );
			try {
				Class DevClass = Class.forName( "jp.or.nhk.rd.hyconet4j." + classname );
				tvrc.add( (TVRC)DevClass.newInstance() );
			}
			catch(ClassNotFoundException e) {
				logger.error("ClassNotFoundException: " + e );
			}
			catch(InstantiationException e) {
				logger.error("InstantiationException: " + e );
			}
			catch(IllegalAccessException e) {
				logger.error("IllegalAccessException: " + e );
			}
		}

		for( TVRC tv : tvrc ) {
			String service_urn = tv.get_service_urn();
			if( !service_urn.equals("") ) {
				boolean registerd = false;
				for( String urn : urns ) {
					if( urn.equals(service_urn) ) {
						registerd = true;
						break;
					}
				}

				if( !registerd ) {
					urns.add( service_urn );
				}
			}
//		logger.debug(e.getKey() + " : " + e.getValue());
		}

		ssdpcp.setUrns(urns);
	}

	//
	// Device Info(Deprecated)
	// Create Devinfo instance
	//
	public void devinfo(TVRCDevinfo devinfo) {
	}

	/**
	 * Callback Lister that devinfo instance for each Device Inforamation has just been Registered in DeviceList of DeviceInfos.
	 * サーチ処理中にデバイスが発見されて対応デバイスリストに登録された時に発動するCallbackListener
	 * 
	 * @param devinfo 実行対象のDeviceinfo
	 */
	public void onDeviceRegistered(TVRCDevinfo devinfo) {
		// It assumes that the method be implemented by SDK user.
	}

	/**
	 * getTVRCDevList.
	 * サーチして発見・登録されたデバイス情報オブジェクトdevinfoのリストの取得
	 * 
	 * @return List devinfoオブジェクトのリスト
	 */
	public List<TVRCDevinfo> getTVRCDevList() {
		return devList ;
	}

	/**
	 * getTVRCbyURN by searching with URN.
	 *  
 	 * @param urn unique identifier of the device that consumer want to find.
	 * @param device_urn urn that identifies the device, in frequent use, the format becomes "URN:MACADDRESS"
	 * @param manufacturer the name of device manufacturer
	 * @return TVRC TVRCObject
	 */
	public TVRC getTVRCbyURN( String urn, String device_urn, String manufacturer ) {
		TVRC retc = null;

//logger.debug( "getTVRCbyURN() " + urn + " " + device_urn + " " + manufacturer);
		if( urn != null ) {
			for( TVRC tv : tvrc ) {
				if( urn.equals(tv.get_service_urn())  ) {
//logger.debug( "getTVRCbyURN(): discover protocol " + urn + " " + device_urn + " " + manufacturer);
					retc = tv ;
					break;
				}
			}
		}

//logger.debug( "getTVRCbyURN(): " + urn + " " + device_urn + " " + manufacturer + ((retc==null)?" null":" exist"));
		return retc ;
	}

	/**
	 * get TVRC instance by searching with makername.
	 * 
	 * @param maker makername
	 * @return TVRC TVRCObject
	 */
	public TVRC getTVRCbyMaker( String maker ) {
		if( maker != null ) {
			for(TVRC tv : tvrc){
				String tvrcMaker = tv.get_maker().toUpperCase();
//				if( tv.get_maker().equals(maker) ) {
				if( tvrcMaker.equals(maker.toUpperCase()) ) {
					return tv;
				}
			}
		}

		return null;
	}

	/**
	 * get TVRCDevinfo instance.
	 * デバイス情報オブジェクトDevinfoを登録デバイス情報オブジェクトリストからindex指定して１つ取得する（機器選択に相当）
	 * 
	 * @param index devinfoのリストのindex
	 * @return TVRCDevinfo 実行対象のDeviceinfo 実行対象のDeviceinfo
	 */
	public TVRCDevinfo getTVRCDevinfo( int index ) {
		return devList.get(index);
	}

	/**
	 * get Devinfo by searching with ipaddress.
	 * IPAddressが一致するデバイス情報オブジェクトをリストから探して取得する
	 * 
	 * @param ipaddr IPAddress
	 * @return TVRCDevinfo 実行対象のDeviceinfo:
	 */
	public TVRCDevinfo getTVRCDevinfoByIpaddr( String ipaddr ) {
		TVRCDevinfo devinfo = null;
		if(ipaddr != null ){
		for( TVRCDevinfo devtmp : devList ) {
			if( ipaddr.equals( devtmp.ipaddr ) ) {
				devinfo = devtmp ;
				break;
			}
		}
	}

		return devinfo;
	}

	/**
	 * get Devinfo by searching with urn.
	 * urnが一致するデバイス情報オブジェクトをリストから探して取得する
	 * 
	 * @param ipaddr IPAddress
 	 * @param urn unique identifier of the device that consumer want to find.
	 * @return TVRCDevinfo 実行対象のDeviceinfo 実行対象のDeviceinfo
	 */
	public TVRCDevinfo getTVRCDevinfoByIpaddrUrn( String ipaddr, String urn ) {
		TVRCDevinfo devinfo = null;
		if(ipaddr != null || urn != null){
			for( TVRCDevinfo devtmp : devList ) {
//logger.trace( "ipaddr: " + ((ipaddr!=null)?ipaddr:"null") );
//logger.trace( "devtmp.ipaddr: " + ((devtmp.ipaddr!=null)?devtmp.ipaddr:"null") );
//logger.trace( "urn: " + ((urn!=null)?urn:"null") );
//logger.trace( "devtmp.getTVRC(): " + ((devtmp.getTVRC()!=null)?"tvrc":"null") );
//logger.trace( "get_service_urn(): " + ((devtmp.getTVRC().get_service_urn()!=null)?"get_service_urn":"null") );

				if( (ipaddr != null) && (urn != null) && (devtmp.getTVRC() != null) )
					if( ipaddr.equals( devtmp.ipaddr ) && urn.equals(devtmp.getTVRC().get_service_urn())) {
						devinfo = devtmp ;
						break;
					}
				}
			}

		return devinfo;
	}

	/**
	 * Start search process for Device Discovery.
	 * 機器発見のためにサーチ処理を開始
	 * 
	 */
	public void searchStart() {
//		logger.debug( "TVRCMan Search START" );
		ssdpcp.searchStart();
	}

	/**
	 * Stop search process for Device Discovery.
	 * 機器発見のためにサーチ処理を停止
	 */
	public void searchStop() {
		ssdpcp.searchStop();
//		logger.debug( "TVRCMan Search STOP" );
	}
}
