package net.ascho.pokretaci.backend.executors.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import net.acho.backend.JSON.parsers.MainParser;
import net.ascho.pokretaci.backend.Config;
import net.ascho.pokretaci.backend.beans.ServerResponseObject;
import net.ascho.pokretaci.backend.communication.ApacheClient;
import net.ascho.pokretaci.backend.communication.Task;
import net.ascho.pokretaci.backend.util.Util;
import net.ascho.pokretaci.beans.Activist;

public class LogOutTask extends Task {

	@Override
	protected ServerResponseObject executeWork() throws Exception {
		ServerResponseObject sob = new ServerResponseObject();
//		
		ApacheClient apache = ApacheClient.getInstance();
		String getUrl = Config.LOGOUT_URL;
		HttpResponse httpResponse = apache.getRequest(getUrl);
		//String JSONresponse = Util.inputStreamToString(httpResponse.getEntity().getContent());
		//Log.d("odgovor", JSONresponse);
		//MainParser.parseLogOut(JSONresponse);
		
		sob.setActionSuccess(true);
		//Video sam da proveras velicnu liste, pa da ne bude problema
		List<Object> lob = new ArrayList<Object>();
		sob.setData(lob);
		sob.setResponseMessage("Izlogovali ste se iz aplikacije.");

		return sob;
	}

}
