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

import java.net.URL;

public interface IFeedbackLampCommnads {
	
	public static final String HTTP_METHOD_PUT = "PUT";

	public static final String ACTION_COLOR = "ACTION_COLOR";
	public static final String ACTION_OFF = "ACTION_OFF";
	public static final String ACTION_ON = "ACTION_ON";

	public URL getUrlCommand();

	public String toString();
	
	public boolean hasParams();
	
	public String getParams();

	public String getHttpMethod();

	public String getWSPath();

	public String getAction();
	

}
