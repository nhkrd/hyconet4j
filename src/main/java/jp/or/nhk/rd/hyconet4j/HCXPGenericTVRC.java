package jp.or.nhk.rd.hyconet4j;

//
// HCXP GenericTVRC
//
public class HCXPGenericTVRC extends TVRC  {
	private String maker_name  = "HCXPGenericTVRC" ;
	private String profile_name  = "HCXPGenericTVRC" ;
	private String service_urn = "urn:dial-multiscreen-org:service:dial:1";
	private String device_urn  = "urn:dial-multiscreen-org:device:dial:1";

	//private HCListener hclistener = null;

	//private String DIALServiceBaseURL = null;
	//private String dialAppResourceURL = null;
	//private String serverInfo = null;
	//private String serverInfoProtocolVersion = null;

	//private boolean companionConn = false;


	public HCXPGenericTVRC() {
	}
	@Override
	public String getProfile() {
		return (profile_name);
	}
	@Override
	public String get_maker() {
		return (maker_name);
	}
	@Override
	public String get_service_urn() {
		return (service_urn);
	}
	@Override
	public String get_device_urn() {
		return (device_urn);
	}
}
