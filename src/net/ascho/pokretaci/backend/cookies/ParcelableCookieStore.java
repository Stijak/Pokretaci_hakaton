package net.ascho.pokretaci.backend.cookies;

import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Objekat koji enkapsulira {@link CookieStore} i serijalizuje i deserijalizuje sve objekte {@link Cookie} u njemu.
 * @author milan
 *
 */
public class ParcelableCookieStore implements Parcelable {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 7256968990649584306L;
	
	
	private CookieStore mCookieStore;
	
	
	private ParcelableCookieStore() {}
	
	public ParcelableCookieStore(CookieStore cookieStore) {
		mCookieStore = cookieStore;
	}
	
	public CookieStore getCookieStore() {
		return mCookieStore;
	}
	
	
	
   public static final Parcelable.Creator<ParcelableCookieStore> CREATOR = new Parcelable.Creator<ParcelableCookieStore>() {
       public ParcelableCookieStore createFromParcel(Parcel source) {
           final ParcelableCookieStore f = new ParcelableCookieStore();
           f.readFromParcel(source);
           return f;
       }

       public ParcelableCookieStore[] newArray(int size) {
           throw new UnsupportedOperationException();
       }

   };
	
	public ParcelableCookieStore(Parcel in) {
		readFromParcel(in);
	}
	
	
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if (mCookieStore != null && mCookieStore.getCookies() != null) {
			List<Cookie> cookies = mCookieStore.getCookies();
			int size = cookies.size();
			dest.writeInt(size);
			for (int i=0; i < size; i++) {
				dest.writeValue(new ParcelableCookie(cookies.get(i)));
			}
		}
		
	}
	
	private void readFromParcel (Parcel in) {
		int size = in.readInt();
		if (size > 0) {
			BasicCookieStore cookieStore = new BasicCookieStore();
			ParcelableCookie recoveredCookie;
			for (int i = 0; i < size; i++) {
				recoveredCookie = (ParcelableCookie) in.readValue(ParcelableCookie.class.getClassLoader());
				cookieStore.addCookie(recoveredCookie.getCookie());
			}
			mCookieStore = cookieStore;
		} else {
			mCookieStore = null;
		}
	}
	
}
