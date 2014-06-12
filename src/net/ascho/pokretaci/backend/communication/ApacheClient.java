package net.ascho.pokretaci.backend.communication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;




import net.ascho.pokretaci.backend.Config;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class ApacheClient {

    private static ApacheClient instance;
    private static Object syncObj = new Object();
    
    
    private DefaultHttpClient mHttpClient;
    private HttpParams mHttpParams;
    
    /*
    public static final int SINGLE_REQUEST_MODE = 1;
    public static final int SESSION_MODE = 2;
    
    
    private int mode = SESSION_MODE;
    */
    
    private ApacheClient() {
    	getHttpClient();
    }
    
    

    public static ApacheClient getInstance() {
    	synchronized (syncObj) {
			if (instance == null) {
				instance = new ApacheClient();
			}
            return instance;
		}
    } 
    
    public static void removeInstance() {
    	synchronized (syncObj) {
			instance = null;
		}
    }
	
    public DefaultHttpClient getHttpClient() {
		if (mHttpClient == null) {
			mHttpClient = new DefaultHttpClient();
		}
		
		if (mHttpParams == null) {
			mHttpParams = new BasicHttpParams();
			
		}
		//Default params
		setTimeout(Config.RESPONSE_TIMEOUT, Config.READ_TIMEOUT);
		HttpClientParams.setRedirecting(mHttpParams, false);	
		if (mHttpClient.getParams() != mHttpParams) {
			mHttpClient.setParams(mHttpParams);
		}
		return mHttpClient;

    } 
    
    
    
    
    public CookieStore getCookieStore() {
    	return mHttpClient.getCookieStore();
    }
    
    public void setCookieStore(CookieStore cookieStore) {
    	mHttpClient.setCookieStore(cookieStore);
    }

    public List<Cookie> getCookies() {
    	return mHttpClient.getCookieStore().getCookies();
    }
    
    
    
    
    public Cookie getCookie(String name) {
    	List<Cookie> cookies = getCookies();
    	if (cookies == null) {
    		return null;
    	}
    	
    	int size = cookies.size();
    	Cookie cookie = null;
    	for (int i=0; i<size; i++) {
    		cookie = cookies.get(i);
    		if (cookie.getName().compareTo(name)==0) {
    			return cookie;
    		}
    	}
    	return null;
    }
    
    
    
    public Cookie getPHPSessionId() {
    	return getCookie("PHPSESSID");
    }
    
    public void clearCookies() {
    	mHttpClient.getCookieStore().getCookies().clear();
    }
    
    
    
    
    public void setHttpParams(HttpParams params) {
    	mHttpParams = params;
    }
    
    
    
    public void setTimeout(Integer connectTimeout, Integer soTimeout) {
    	if (connectTimeout == null && soTimeout == null) 
    		return;
    	
    	
    	if (mHttpParams == null) {
    		mHttpParams = new BasicHttpParams();
    	}
    	
    	if (connectTimeout != null)
    		HttpConnectionParams.setConnectionTimeout(mHttpParams, connectTimeout);//a timeout for how long to wait to establish a TCP connection
    	if (soTimeout != null) 
    		HttpConnectionParams.setSoTimeout(mHttpParams, soTimeout);//timeout for how long to wait for a subsequent byte of data
    }
    
    
    
    

    
    
    
    
    /**
     * Post request sa prosto mapiranim parametrima npr: $_POST['username'] = ....
     * @param url
     * @param params
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public HttpResponse postRequest(String url, Map<String, String> params) throws ClientProtocolException, IOException {
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        Entry<String, String> param = null;
        
        while (iterator.hasNext()) {
            param = iterator.next();
            nvps.add(new BasicNameValuePair(param.getKey(), param.getValue()));
        }
        
        return postRequest(url, new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
    }
    
    /**
     * Post request sa parametrima mapiranim u 'data' niz npr: $data = $_POST['data'], $data['username'] = ....
     * @param url
     * @param params
     * @return
     * @throws ClientProtocolException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public HttpResponse postRequestDataWrapped(String url, Map<String, String> params) throws ClientProtocolException, UnsupportedEncodingException, IOException {
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        Entry<String, String> param = null;
        
        while (iterator.hasNext()) {
            param = iterator.next();
            nvps.add(new BasicNameValuePair("data["+param.getKey()+"]", param.getValue()));
        }
        
        return postRequest(url, new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
    }
    
    
    /**
     * 
     * content type : application/json
      * <pre> {@code
     	// Build the JSON object to pass parameters
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("username", username);
		jsonObj.put("apikey", apikey);
		// Create the POST object and add the parameters
		StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
     * } </pre>
     * 
     * 
     * 
     * @param url
     * @param entity
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    
    
    public HttpResponse postRequest(String url, HttpEntity entity) throws ClientProtocolException, IOException {
    	DefaultHttpClient httpclient = getHttpClient();
    	
    	HttpPost httpost = new HttpPost(url);
        
        httpost.setEntity(entity);

        return httpclient.execute(httpost);

    }
    
    
    
    
    
    
    
    public HttpResponse getRequest(String url) throws ClientProtocolException, IOException {
    	DefaultHttpClient httpclient = getHttpClient();
    	
    	HttpGet httpget = new HttpGet(url);
    	
        return httpclient.execute(httpget);

    }
	
	
	
}