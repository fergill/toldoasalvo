/*******************************************************************************
 * Copyright (C) 2014 Open University of The Netherlands
 * Author: Bernardo Tabuenca Archilla
 * Lifelong Learning Hub project 
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.upm.miw.windspeed;

import java.net.MalformedURLException;
import java.net.URL;

public class FCOff implements IFeedbackLampCommnads {
	/**
	 * > PUT /ring/on/ HTTP/1.1 : Turns the LED strip off
	 */
	private static final String WS_PATH = "/ring/off/";
	private String ipAdress = "";
	
	
	public FCOff(String sIp){
		ipAdress = sIp;
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
		return "COMMAND OFF: URL["+getUrlCommand().toString()+"] COMMAND["+getCommand()+"] HAS PARAMS:["+hasParams()+"] PARAMS:["+getParams()+"] METHOD:["+getHttpMethod()+"]";
	}
	
	
	@Override
	public boolean hasParams() {
		return false;
	}

	
	@Override
	public String getParams() {
		return "";
	}
	
	@Override
	public String getHttpMethod() {
		return IFeedbackLampCommnads.HTTP_METHOD_PUT;
	}

	@Override
	public String getAction() {
		return ACTION_OFF;
	}	
	
	@Override
	public String getWSPath() {
		return WS_PATH;
	}	

}
