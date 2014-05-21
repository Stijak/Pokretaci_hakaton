package net.ascho.pokretaci.backend.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.client.ClientProtocolException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

public class Util {
	
	
	/**
     * Converts input stream to string.
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStreamToString(InputStream is) throws IOException {
		StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String line = bufferedReader.readLine();
        while(line != null){
            inputStringBuilder.append(line);inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
        
        return inputStringBuilder.toString();
    }
    
    /**
	 * Spisak naloga odredjenog tipa iz device-a
	 * @param accountType - tip naloga koji se dohvata, npr "com.google" je za google naloge
	 * @return
	 */
	public static String[] getAccountNames(String accountType, Context context) {
		AccountManager mAccountManager;
	    mAccountManager = AccountManager.get(context);
	    Account[] accounts = mAccountManager.getAccountsByType(accountType);	
	    String[] names = new String[accounts.length];
	    for (int i = 0; i < names.length; i++) {
	        names[i] = accounts[i].name;
	    }
	    return names;
	}

	
	
	/**
	 * Vrsi upload file-a na server. Za verify account ili wire proof dokumenta.<br><br>
	 * <h1>Vazno!!!</h1><b>Postoji vise nacina za upload fajla, pogotovo ako uz fajla treba poslati i neke druge parametre ili vise fajlova. Implementacija funckije je pogodna za nas slucaj. 
	 * Za druge slucajeve(npr kada se salje vise fajlova ili ogromni fajlovi) pogledati zakomentarisan kod i linkove:</b> 
	 * <p>http://stackoverflow.com/questions/2935946/sending-images-using-http-post</p>
	 * <p>http://stackoverflow.com/questions/18964288/upload-a-file-through-an-http-form-via-multipartentitybuilder-with-a-progress</p>
	 * <br><br>
	 * @param filePath - putanja do fajla
	 * @return true/false zavisno od uspesnosti
	 * @throws IOException, 
	 * @throws ClientProtocolException, 
	 * @throws Exception 
	 */
	public void upload(String filePath, int uploadType) {
		/* 
		File file = new File(filePath);
		InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), -1);
		
		reqEntity.setContentType("binary/octet-stream");
		reqEntity.setChunked(true); // Send in multiple parts if needed
		
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

	  //  File file = new File(filePath);
	  //  FileBody fb = new FileBody(file);
	    
		File file = new File(filePath);
		//InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), -1);
	    //builder.addPart("wire_proof", fb);
		String contentType = getContentType(filePath);

	    builder.addBinaryBody("wire_proof", file, ContentType.create(contentType), file.getName());
	    HttpEntity reqEntity = builder.build();
	    
		HttpResponse response = null;
		
		switch(uploadType) {
			case WIRE_PROOF_UPLOAD:
				response = mApacheClient.postRequest(Config.WIRE_UPLOAD_URL, reqEntity);
				break;
				
			case VERIFY_ACCOUNT_UPLOAD:
				response = mApacheClient.postRequest(Config.VERIFY_UPLOAD_URL, reqEntity);
				break;			
		}
		
		if(response == null) {
			throw new Exception("Ne postoji takav uploadType ili je nastalo neko cudno sranje da response bude null!!!");
		}
		
		
		HttpEntity entity = response.getEntity();
		
		String responseContent = inputStreamToString(entity.getContent());
		responseContent = responseContent.trim();
		
		return JSONParser.decodeJSONResponse(responseContent);*/
		
		
		 /**
		  *
		  * --Jedan od primera slanja vise fajlova sa nekim parametrima
		  *
		  	HttpPost httpost = new HttpPost("URL_WHERE_TO_UPLOAD");
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("myString", new StringBody("STRING_VALUE"));
			entity.addPart("myImageFile", new FileBody(imageFile));
			entity.addPart("myAudioFile", new FileBody(audioFile));
			httpost.setEntity(entity);
			HttpResponse response;
			response = httpclient.execute(httpost);
		  * 
		  * 
		  * 
		  	httppost = new HttpPost(URL);
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("title", new StringBody("position.csv", Charset.forName("UTF-8")));
			File myFile = new File(Environment.getExternalStorageDirectory(), file);
			FileBody fileBody = new FileBody(myFile);
			entity.addPart("file", fileBody);
			httppost.setEntity(entity);
			httppost.getParams().setParameter("project", id);
		  * 
		  * 
		  * 
		  **/
	}
	//Pomocne funkcije
		public static String getContentType(String filePath) {
			String ext = getFileExt(filePath);
			ext = ext.toLowerCase();
			if(ext.compareTo("jpg") == 0 || ext.compareTo("jpeg") == 0) {
				ext = "image/jpeg";
			} else {
				ext = "image/" + ext;
			}
			return ext;
		}
		/**
	     * Vraca extenziju fajla ako je ima
	     * @param s
	     * @return
	     */
	    public static String getFileExt(String s) {
	    	int pos = s.lastIndexOf(".");
	  
	    	if(pos != -1) {
	    		return s.substring(pos+1, s.length());
	    	} else {
	    		return null;
	    	}
	    }
	    
	    
	    /**
	     * Check if external storage is built-in or removable.
	     *
	     * @return True if external storage is removable (like an SD card), false
	     *         otherwise.
	     */
	    @TargetApi(9)
	    public static boolean isExternalStorageRemovable() {
	        if (hasGingerbread()) {
	            return Environment.isExternalStorageRemovable();
	        }
	        return true;
	    }

	    /**
	     * Get the external app cache directory.
	     *
	     * @param context The context to use
	     * @return The external cache dir
	     */
	    @TargetApi(8)
	    public static File getExternalCacheDir(Context context) {
	        if (hasFroyo()) {
	            return context.getExternalCacheDir();
	        }

	        // Before Froyo we need to construct the external cache dir ourselves
	        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
	        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
	    }
	    
	    
	    
	    /**
	     * Get a usable cache directory (external if available, internal otherwise).
	     *
	     * @param context The context to use
	     * @param uniqueName A unique directory name to append to the cache dir
	     * @return The cache dir
	     */
	    public static String getDiskCacheDir(Context context, String uniqueName) {
	        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
	        // otherwise use internal cache dir
	        final String cachePath =
	                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
	                        !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
	                                context.getCacheDir().getPath();

	        return cachePath + File.separator + uniqueName;
	    }
	    
	    public static boolean hasFroyo() {
	        // Can use static final constants like FROYO, declared in later versions
	        // of the OS since they are inlined at compile time. This is guaranteed behavior.
	        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	    }

	    public static boolean hasGingerbread() {
	        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	    }

	    public static boolean hasHoneycomb() {
	        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	    }

	    public static boolean hasHoneycombMR1() {
	        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	    }

	   /* public static boolean hasJellyBean() {
	        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	    }*/
}
