package net.ascho.pokretaci.backend.beans;
import java.util.List;

import net.ascho.pokretaci.beans.Activist;
import net.ascho.pokretaci.beans.Comment;
import net.ascho.pokretaci.beans.Goal;

/**
 * Omotac oko isparsiranih JSON podataka koji su stigli kao odgovor od servera na neku akciju.
 * <p>Obavezno pre obrade podataka proveriti {@link #isResponseValid()}, ako je false obraditi gresku.</p>
 * <p>Podaci se dohvataju sa {@link #getData()} koja vraca Listu objekata ({@link Goal}, {@link Activist}
 * ,{@link Comment}) ili null ako nema podataka u odgvoru. Npr u slucaju GoogleLogin-a Lista ce da sadrzati samo jedan objekat tipa Activist gde ce biti
 * zapisane informacije o ulogovanom korisniku.
 * </p>
 * @author Bojan
 *
 */
public class ServerResponseObject {
	
	public static final int EXCEPTION = -101;
	/**
	 * Status JSON poruke ako ga ima
	 */
	//private final boolean mActionStatus;
	/**
	 * Response code dobijen od servera, npr 404.
	 */
	private int mResponseCode;
	/**
	 * Exception koji se desio, ako se desio. Ocekivani tipovi IOException, ClientProtocolException, JSONException...
	 */
	private Exception mException;
	
	/**
	 * Da li je akcija poslata na server uspesno odradjeno 
	 */
	private boolean actionSuccess = false;
	
	/**
	 * Tekstualni odgovor servera vezan za akciju, ako ga ima
	 */
	private String responseMessage;
	
	/**
	 * Da li je ulogovan korisnik
	 */
	private boolean loggedIn = false;
	
	

	private List<Object> mData = null; 
	
	
	
	
	public ServerResponseObject() {
		mException = null;
	}
	
	
/*	*//**
	 * @param responseCode
	 *//*
	public ServerResponseObject(int responseCode) {
		mResponseCode = responseCode;
		mException = null;
	}*/
	
	/**
	 * Ako je doslo do exception-a
	 * @param e
	 */
	public ServerResponseObject(Exception e) {
		mResponseCode = EXCEPTION;
		mException = e;
	}
	
	
	public void setData(List<Object> parsedData) {
		mData = parsedData;
	}
	
	/**
	 * Vraca podatke koje treba kastovati u odgovarajuci format
	 * @return
	 */
	public List<Object> getData() {
		return mData;
	}
	
	/**
	 * @deprecated Nije implementirana
	 * @return
	 */
	public Class<? extends Object> getDataType() {
		
		
		return Comment.class;
	}
	
	/**
	 * Da li je server odgovorio u validnom formatu, to jest da nije doslo do neke neocekivane greske, Exception-a itd...
	 * @return
	 */
	public boolean isResponseValid() { 
		return mResponseCode != EXCEPTION;
	}
	
/*	*//**
	 * Response code
	 * @return
	 *//*
	public int getResponseCode() {
		return mResponseCode;
	}*/
	
	/**
	 * Exception ako ga je bilo
	 * @return
	 */
	public Exception getException() {
		return mException;
	}
	
	/**
	 * Poruka Exception-a
	 * @return
	 */
	public String getExceptionMsg() {
		if(mException != null) {
			return mException.getMessage();
		} else {
			return null;
		}
		
	}
	
	
	public void setActionSuccess(boolean bool) {
		actionSuccess = bool;
	}
	
	public void setResponseMessage(String msg) {
		responseMessage = msg;
	}
	
	/**
	 * Da li je sama akcija na serveru bila uspesno. Kao npr da li je problem podrzan ili mozda nije podrzan jer korisnik nema dovoljno bodova
	 * @return
	 */
	public boolean isActionSuccessful() {
		return actionSuccess;
	}
	
	/**
	 * Vraca tekstualnu poruku servera za uradjenu akciju ako je ima. Npr ako je problem podrzan poruka za korisnika da je uspesno pordzan.
	 * @return
	 */
	public String getResonseMessage() {
		return responseMessage;
	}
	
	/**
	 * Da li je korisnik ulogovan
	 * @return
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}


	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
}
