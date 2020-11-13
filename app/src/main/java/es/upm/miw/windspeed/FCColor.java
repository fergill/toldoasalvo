package es.upm.miw.windspeed;

import java.net.MalformedURLException;
import java.net.URL;

public class FCColor implements IFeedbackLampCommnads {
	/**
	 * * > PUT /ring/color/ HTTP/1.1           : Changes the color of the LED strip
	 */
	private static final String WS_PATH = "/ring/color/";
	private String ipAdress = "";
	private String sColorRed_DECIMAL = "0";
	private String sColorGreen_DECIMAL = "0";
	private String sColorBlue_DECIMAL = "0";
	
	
	public FCColor(String sIp, String sRedDecimal, String sGreenDecimal, String sBlueDecimal){
		ipAdress = sIp;
		
		sColorRed_DECIMAL = sRedDecimal;
		sColorGreen_DECIMAL = sGreenDecimal;
		sColorBlue_DECIMAL = sBlueDecimal;
	}
	
	private String getCommand(){
		
		return FeedbackLampConfig.URL_PREFIX + ipAdress + WS_PATH;
	}
	
	
	public URL getUrlCommand(){
		try {
			return new URL(getCommand());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@Override
	public String toString(){
		return "COMMAND ON: URL["+getUrlCommand().toString()+"] COMMAND["+getCommand()+"] HAS PARAMS:["+hasParams()+"] PARAMS:["+getParams()+"] METHOD:["+getHttpMethod()+"]";
	}
	

	@Override
	public boolean hasParams() {
		return true;
	}

	
	@Override
	public String getParams() {
		
		String sJson = "{\"r\":" + sColorRed_DECIMAL +
				",\"g\":" + sColorGreen_DECIMAL + 
				",\"b\":" + sColorBlue_DECIMAL + 
				"}";
				
		return sJson;
	}

	@Override
	public String getHttpMethod() {
		return IFeedbackLampCommnads.HTTP_METHOD_PUT;
	}
	
	@Override
	public String getAction() {
		return ACTION_COLOR;
	}	
	
	@Override
	public String getWSPath() {
		return WS_PATH;
	}	

}
