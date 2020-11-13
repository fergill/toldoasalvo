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

import android.os.AsyncTask;
import android.util.Log;

import java.io.EOFException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class FeedbackLampManager extends AsyncTask<IFeedbackLampCommnads, Void, String> {
	
	private String CLASSNAME = this.getClass().getName();

	@Override
	protected String doInBackground(IFeedbackLampCommnads... params) {


		try {
			IFeedbackLampCommnads Ifcc = params[0];
			Log.d(CLASSNAME, "Lunch command "+Ifcc.toString());
			run(Ifcc);
			Log.d(CLASSNAME, "Command launched! "+Ifcc.toString());


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "Executed "+params[0].getAction();
	}

	
	private void run(IFeedbackLampCommnads cmd) {
		
		HttpURLConnection connection = null;

		try {

			connection = (HttpURLConnection) cmd.getUrlCommand().openConnection();
			connection.setRequestMethod(cmd.getHttpMethod());
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty( "Accept-Encoding", "" );
			
			
	        if (cmd.hasParams()){
		        
		        connection.setRequestProperty("Content-Type", "application/json");
		        connection.setRequestProperty("Accept", "application/json");
		        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
		        osw.write(cmd.getParams());
		        osw.flush();
		        osw.close();
		        
	        }
			
//	        if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) { 
//        		connection.setRequestProperty("Connection", "close"); 
//	        }
	        
			connection.connect();
			connection.getResponseCode();

		} catch (EOFException e) {
			Log.e(CLASSNAME, "There seems to be some bug reading stream from webservice");
	        if (connection != null) {
	        	connection.disconnect();
	        }
			
	    } catch (Exception e) {
	        if (connection != null) {
	        	connection.disconnect();
	        }
			e.printStackTrace();
		}finally {
	        if (connection != null) {
	        	connection.disconnect();
	        }
	    }
		

	}
	
	
	

	@Override
	protected void onPostExecute(String result) {
		System.out.println("On poste execute: " + result);
	}

	@Override
	protected void onPreExecute() {
		System.out.println("On pre execute.");
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		System.out.println("On progress update.");
	}
}