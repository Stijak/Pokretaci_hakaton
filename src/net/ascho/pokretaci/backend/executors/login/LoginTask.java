package net.ascho.pokretaci.backend.executors.login;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;

import net.ascho.pokretaci.backend.beans.ServerResponseObject;
import net.ascho.pokretaci.backend.beans.TransportObject;
import net.ascho.pokretaci.backend.communication.Task;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;


public abstract class LoginTask extends Task {
	
	/**
	 * Request code za intent koji se koristi pri startActivityForResult u slucaju kada korisnik treba da dozvoli nasoj aplikaciji da pristupa njegovim podacima na google-u
	 */
	public static final int AUTH_LOGIN_REQUEST = 0x99;
	
	private AccountManager mAccountManager;
	private Activity mActivity;
	
	public LoginTask(Activity activity) {
		mActivity = activity;
	}
	
	@Override
	protected ServerResponseObject executeWork() throws GooglePlayServicesAvailabilityException, ClientProtocolException, UserRecoverableAuthException, IOException, GoogleAuthException, Exception  {
		return performLogin();
	}
	
	public AccountManager getAccountManager() {
		return mAccountManager;
	}

	public Activity getActivity() {
		return mActivity;
	}
	
	
	/**
	 * Konkretna login logika
	 * @return
	 * @throws GoogleAuthException 
	 * @throws IOException 
	 * @throws UserRecoverableAuthException 
	 * @throws ClientProtocolException 
	 * @throws GooglePlayServicesAvailabilityException 
	 * @throws Exception 
	 */
	protected abstract ServerResponseObject performLogin() throws GooglePlayServicesAvailabilityException, ClientProtocolException, UserRecoverableAuthException, IOException, GoogleAuthException, Exception;

	/**
	 * Spisak naloga odredjenog tipa iz device-a
	 * @param accountType - tip naloga koji se dohvata, npr "com.google" je za google naloge
	 * @return
	 */
	public String[] getAccountNames(String accountType) {
	    mAccountManager = AccountManager.get(getContext());
	    Account[] accounts = mAccountManager.getAccountsByType(accountType);	
	    String[] names = new String[accounts.length];
	    for (int i = 0; i < names.length; i++) {
	        names[i] = accounts[i].name;
	    }
	    return names;
	}
}
