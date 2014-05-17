package net.ascho.pokretaci.backend.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

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

	
}
