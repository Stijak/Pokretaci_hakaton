package net.ascho.pokretaci.backend.cookies;


import net.ascho.pokretaci.backend.util.TypeConvertor;

import org.apache.http.client.CookieStore;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.preference.PreferenceManager;

/**
 * Manager kojim mogu da se svi cookie-i koji su dobijeni od servera sacuvaju u sharedPreferences i restoriraju
 * na pokretanje aplikacije.
 * @author milan
 *
 */
public class CookieManager {

	private static CookieManager cookieManagerInstance;
	private static Object syncObj = new Object();
	
	
	private CookieManager() {}
	
	public static CookieManager getInstance() {
		synchronized (syncObj) {
			if (cookieManagerInstance == null) {
				cookieManagerInstance = new CookieManager();
			}
			return cookieManagerInstance;
		}
	}
	
	
	/**
	 * Cuva prosledjene cookie-e u shared preferences. Pogodno da se uradi neposredno pred 
	 * izlazenje iz aplikacije.
	 * @param context
	 * @param cookieStore
	 */
	public void saveCookiesToSharedPreferences(Context context, CookieStore cookieStore) {
		Parcel parcel = Parcel.obtain();
		
		byte[] bytes = null;
		if (cookieStore != null) {
		    parcel.writeValue(new ParcelableCookieStore(cookieStore));
		    bytes = parcel.marshall();
		}

	    saveCookies(context, TypeConvertor.byteArrayToString(bytes));
	    
	    parcel.recycle();
	    
	}
	
	/**
	 * Restaurira sacuvane cookie-e iz shared preferences. Pogodno da se pozove na pokretanje aplikacije.
	 * @param context
	 * @return
	 */
	public CookieStore restoreCookiesFromSharedPreferences(Context context) {
		Parcel parcel = Parcel.obtain();
		
		ParcelableCookieStore recoveredCookieStore = null;
		byte[] bytes = TypeConvertor.stringToByteArray(restoreCookies(context));
		
		if (bytes != null) {
		    parcel.unmarshall(bytes, 0, bytes.length);
		    parcel.setDataPosition(0);
		    recoveredCookieStore = (ParcelableCookieStore) parcel.readValue(ParcelableCookie.class.getClassLoader());
		}

	    parcel.recycle();
	    
	    return recoveredCookieStore != null ? recoveredCookieStore.getCookieStore() : null;
	}
	
	
	
	public void deleteCookiesFromCharedPreferences(Context context) {
		saveCookiesToSharedPreferences(context, null);
	}
	
	
	
	
	
	
	private String getCookiesKey(Context context) {
		String packageName = context.getPackageName();
		return packageName + ":cookies";
	}
	
    private String restoreCookies(Context context) {
		SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(context);
		
		return mPref.getString(getCookiesKey(context), null);
    }
    
    private void saveCookies(Context context, String value) {
    	SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(context);
    	
    	SharedPreferences.Editor editor = mPref.edit();
    	if (value != null) {
    	    editor.putString(getCookiesKey(context), value);
    	} else {
    	    editor.remove(getCookiesKey(context));
    	}
	    editor.commit();
    }
	
    
    
    
	
	
}
