package net.ascho.pokretaci.backend.executors.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.acho.backend.JSON.parsers.MainParser;
import net.ascho.pokretaci.backend.Config;
import net.ascho.pokretaci.backend.beans.ServerResponseObject;
import net.ascho.pokretaci.backend.beans.TransportObject;
import net.ascho.pokretaci.backend.communication.ApacheClient;
import net.ascho.pokretaci.backend.util.Util;
import net.ascho.pokretaci.beans.Activist;
import net.ascho.pokretaci.beans.Goal;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.cookie.Cookie;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.is;

/**
 * Google login/auth task with user google account. 
 * @see AccountTask
 * @author bojancv
 *
 */
public class GoogleLogin extends LoginTask {
	
	public static final int GOOGLE_AUTH_REQUEST_CODE = 0x23;
	
	//GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE
	private String mScope = "oauth2:email profile"; //"oauth2:https://www.googleapis.com/auth/userinfo.profile";
	private String mEmail;
	private String mToken;
	
	/**
	 * Email korisnika sa kojim se loguje na sajt
	 * @param eMail - email korisnika
	 * @param acitivity - aktivnost koja poziva task. Neophodno jer u slucaju da se prvi put 
	 * odbrava pristup aplikaciji treba da iskoci dialog za dodelu permisiona uz pomoc Google intenta
	 */
	public GoogleLogin(String eMail, Activity activity) {
		super(activity);
		mEmail = eMail;
	}
	

	
	@Override
	protected ServerResponseObject performLogin() throws GooglePlayServicesAvailabilityException, ClientProtocolException, UserRecoverableAuthException, IOException, GoogleAuthException, Exception {
		
	//	try {
			//Acquire token
			if(checkIfAlreadyLoggedIn()) { //Vec je ulogovan
				ServerResponseObject sro = new ServerResponseObject();
				List<Activist> activists = new ArrayList<Activist>();
				getUserProfile();
				activists.add(Activist.getUserProfile());
				List<Object> lob = new ArrayList<Object>(activists);
				sro.setActionSuccess(true);
				sro.setResponseMessage("Ulogovali ste se.");
				sro.setData(lob);
				sro.setLoggedIn(true);
				return sro;
			} else {
				mToken = GoogleAuthUtil.getToken(getContext(), mEmail, mScope);
				return login();
				
			}
		/*} catch (GooglePlayServicesAvailabilityException playEx) {
	        Dialog alert = GooglePlayServicesUtil.getErrorDialog(playEx.getConnectionStatusCode(), getActivity(), GOOGLE_AUTH_REQUEST_CODE);
	        if(alert != null) { //Od ovog exceptiona mozemo da se oporavimo
	        	alert.show();
	        } else { 
	        	throw new Exception("Vas uredjaj ne poseduje GooglePlayServices i nazalost vas ne mozemo ulogovati kroz aplikaciju. I dalje mozete koristiti aplikaciju ali ne mozete postavljati nove probleme...");
	        	
		        	Moze da se desi da korisnik ne moze da skine Google Services ili nesto slicno,
		        	tada je ovaj dialog null.
	        	 
	        	Toast.makeText(getContext(), 
	        			"Vas uredjaj ne poseduje GooglePlayServices i nazalost vas ne mozemo ulogovati kroz aplikaciju. I dalje mozete koristiti aplikaciju ali ne mozete postavljati nove probleme...", 
	        			Toast.LENGTH_LONG)
	        			.show();
	        }
		}  catch (UserRecoverableAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); //Od ovog exceptiona mozemo da se oporavimo
			getActivity().startActivityForResult(e.getIntent(), GOOGLE_AUTH_REQUEST_CODE);
				
		} catch (GoogleAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	//	return null;
	}
	
	private boolean checkIfAlreadyLoggedIn() throws ClientProtocolException, IOException {
		ApacheClient apache = ApacheClient.getInstance();
		HttpResponse httpResponse = apache.getRequest(Config.CHECK_LOGIN_URL);
		String JSONresponse = Util.inputStreamToString(httpResponse.getEntity().getContent());
		Activist a;
		try {
			a = MainParser.parseLoggedIn(JSONresponse);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return false;
		}
		if(a != null)
			return true;
		else 
			return false;
	}
	
	private ServerResponseObject login() throws IOException, GooglePlayServicesAvailabilityException, GoogleAuthException, JSONException {
		
		ApacheClient apache = ApacheClient.getInstance();
		String getUrl = Config.LOGIN_BASE_URL.replace(Config.PARAM, mToken);
		HttpResponse httpResponse = apache.getRequest(getUrl);
		
		if(httpResponse.getStatusLine().getStatusCode() == 401) {
			//Bad token
			GoogleAuthUtil.clearToken(getContext(), mToken);
			//Lets try again
			mToken = GoogleAuthUtil.getToken(getContext(), mEmail, mScope);
			httpResponse = apache.getRequest(Config.LOGIN_BASE_URL.replace(Config.PARAM, mToken));
			
		}
		
		String JSONresponse = Util.inputStreamToString(httpResponse.getEntity().getContent());
		Log.d("odgovor", JSONresponse);
		//parse JSON
		
		ServerResponseObject sro = new ServerResponseObject();
		List<Activist> activists = new ArrayList<Activist>();
		
		if(checkIfAlreadyLoggedIn()) {
			getUserProfile();
			sro.setActionSuccess(true);
			sro.setResponseMessage("Ulogovali ste se.");
			activists.add(Activist.getUserProfile());
			sro.setLoggedIn(true);
		} else {
			activists.add(null);
		}
		
		List<Object> lob = new ArrayList<Object>(activists);
		sro.setData(lob);
//		sro.setLoggedIn(MainParser.parseAuthLoggedIn(JSONresponse));
		
		return sro;
		
/*		URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token="
		        + token);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		int serverCode = con.getResponseCode();
		
		//successful query
		if (serverCode == 200) {
		    InputStream is = con.getInputStream();
		    String name = Util.inputStreamToString(is);
		    Log.d("google", "Hello " + name + "!");
		    is.close();
		    return true;    
		//bad token, invalidate and get a new one
		} else if (serverCode == 401) {
		    GoogleAuthUtil.invalidateToken(getContext(), token);
		 
		    Log.d("google", "Server auth error: " + Util.inputStreamToString(con.getErrorStream()));
		    return false;
		//unknown error, do something else
		} else {
		    Log.e("Server returned the following error code: " + serverCode, null);
		    return false;
		}*/
		
		
	   // Do work with token.
	    /* if(server indicates token is invalid) {
	          // invalidate the token that we found is bad so that GoogleAuthUtil won't
	          // return it next time (it may have cached it)
	    	 GoogleAuthUtil.clearToken(getContext(), mToken);
	              // consider retrying getAndUseTokenBlocking() once more
	              return;
	      }*/
		
		
	}
	
	public void getUserProfile() throws ClientProtocolException, IOException, JSONException {
		ApacheClient apache = ApacheClient.getInstance();
		HttpResponse httpResponse = apache.getRequest(Config.USER_INFO_URL.replace(Config.PARAM, Activist.getUserProfile().id));
		String JSONresponse = Util.inputStreamToString(httpResponse.getEntity().getContent());
		
		MainParser.parseUserProfile(JSONresponse);
		
		httpResponse = apache.getRequest(Config.GOAL_DATA_FOR_USER_URL.replace(Config.PARAM, Activist.getUserProfile().id));
		JSONresponse = Util.inputStreamToString(httpResponse.getEntity().getContent());
		
		List<Goal> goals = MainParser.parseGoals(JSONresponse);
		Activist.getUserProfile().setUserGoals(goals);

	
	}
    
    
    
	
}
