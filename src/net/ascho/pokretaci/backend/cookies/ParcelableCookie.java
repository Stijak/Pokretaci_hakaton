package net.ascho.pokretaci.backend.cookies;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Objekat koji enkapsulira {@link Cookie} i serijalizuje i deserijalizuje njegove vrednosti.
 * @author milan
 *
 */
public class ParcelableCookie implements Parcelable {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 7256968990649584306L;

	@SuppressLint("SimpleDateFormat")
	private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
	
	
	private Cookie mCookie;
	
	
	private ParcelableCookie() {}
	
	public ParcelableCookie(Cookie cookie) {
		mCookie = cookie;
	}
	
	public Cookie getCookie() {
		return mCookie;
	}
	
	
	
    public static final Parcelable.Creator<ParcelableCookie> CREATOR = new Parcelable.Creator<ParcelableCookie>() {
        public ParcelableCookie createFromParcel(Parcel source) {
            final ParcelableCookie f = new ParcelableCookie();
            f.readFromParcel(source);
            return f;
        }

        public ParcelableCookie[] newArray(int size) {
            throw new UnsupportedOperationException();
        }

    };
	
	public ParcelableCookie(Parcel in) {
		readFromParcel(in);
	}
	
	
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if (mCookie != null) {
			dest.writeString(mCookie.getName() != null ? mCookie.getName() : "");
			dest.writeString(mCookie.getValue() != null ? mCookie.getValue() : "");

			boolean[] boolArray = new boolean [2];
			boolArray[0] = mCookie.isPersistent();
			boolArray[1] = mCookie.isSecure();
			dest.writeBooleanArray(boolArray);

			dest.writeInt(mCookie.getVersion());
			//dest.writeIntArray(mCookie.getPorts());
			dest.writeString(mCookie.getPath() != null ? mCookie.getPath() : "");
			dest.writeString(mCookie.getExpiryDate() != null ? formatter.format(mCookie.getExpiryDate()) : "");
			dest.writeString(mCookie.getDomain() != null ? mCookie.getDomain() : "");
			//dest.writeString(mCookie.getCommentURL());
			dest.writeString(mCookie.getComment() != null ? mCookie.getComment() : "");
		}
		
	}
	
	private void readFromParcel (Parcel in) {
		String name = in.readString();
		String value = in.readString();
		
		BasicClientCookie cookie = new BasicClientCookie(name, value);
		
		boolean[] boolArray = new boolean [2];		
		in.readBooleanArray(boolArray);
		cookie.setSecure(boolArray[1]);
		cookie.setVersion(in.readInt());
		
		String path = in.readString();
		String expiryDate = in.readString();
		String domain = in.readString();
		String comment = in.readString();
		
		if (path.length() > 0) {
			cookie.setPath(path);
		}
		
		if (expiryDate.length() > 0) {
			try {
				cookie.setExpiryDate(formatter.parse(expiryDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (domain.length() > 0) {
			cookie.setDomain(domain);
		}
		if (comment.length() > 0) {
			cookie.setComment(comment);
		}
		
		mCookie = cookie;
		
	}
	
}
