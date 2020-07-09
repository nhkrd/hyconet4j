package jp.or.nhk.rd.hyconet4j;

import java.nio.ByteBuffer;
import org.json.JSONjava.JSONObject;

//
// setURLforCompanionDevice
//
public class TVRCSetURL  {
	public String eventName;
	public String url;
	public String title;
	public String desc;
	public boolean isAutoOpen;

	public TVRCSetURL() {
		this.eventName = "close";
		this.url = null;
		this.title = null;
		this.desc = null;
		this.isAutoOpen = false;
	}

	public TVRCSetURL(JSONObject obj) {
		//From HCEX
		if(obj.has("control")){
			JSONObject ctrlObj = (JSONObject)obj.get("control");
			if (ctrlObj.has("setURLForCompanionDevice")) {
				JSONObject seturlObj  = (JSONObject)ctrlObj.get("setURLForCompanionDevice");
				JSONObject optionsObj = (JSONObject)seturlObj.get("options");
				url        = seturlObj.getString("url");
				title      = optionsObj.getString("app_title");
				desc       = optionsObj.getString("app_desc");
				isAutoOpen = optionsObj.getBoolean("auto_start");
				eventName  = (isAutoOpen)? "open":"close";
			}
		}
	}
}
