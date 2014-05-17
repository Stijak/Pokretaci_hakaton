package net.ascho.pokretaci.backend.beans;

public class TransportObject {
	public static final int EXCEPTION = -101;
	/**
	 * JSON poruka dobijena od servera
	 */
	private final String mResponseMessage;
	/**
	 * Status JSON poruke ako ga ima
	 */
	//private final boolean mActionStatus;
	/**
	 * Response code dobijen od servera, npr 404.
	 */
	private final int mResponseCode;
	/**
	 * Exception koji se desio, ako se desio. Ocekivani tipovi IOException, ClientProtocolException, JSONException...
	 */
	private Exception mException;
	
	/**
	 * Ako je server uspesno odgovorio, prosledjuju se JSON poruka i responseCode i action status
	 * @param rm
	 * @param rc
	 * @param as
	 */
	public TransportObject(String ResponseMessage, int ResponseCode) {
		mResponseMessage = ResponseMessage;
		//mActionStatus = actionStatus;
		mResponseCode = ResponseCode;
		mException = null;
	}
	
	/**
	 * Ako je doslo do exception-a
	 * @param e
	 */
	public TransportObject(Exception e) {
		mResponseMessage = null;
	//	mActionStatus = false;
		mResponseCode = EXCEPTION;
		mException = e;
	}
	
	/**
	 * Da li je akcija uspesno odradjena na serveru
	 * @return
	 */
	public boolean getActionStatus() {
	//	return mActionStatus;
		return true;
	}
	
	/**
	 * Da li je server odgovorio u validnom formatu, to jest da nije doslo do neke neocekivane greske, Exception-a itd...
	 * @return
	 */
	public boolean isResponseValid() { 
		return mResponseCode != EXCEPTION;
	}
	
	/**
	 * Response code
	 * @return
	 */
	public int getResponseCode() {
		return mResponseCode;
	}
	
	/**
	 * JSON poruka servera
	 * @return
	 */
	public String getResponseMsg() {
		return mResponseMessage;
	}
	
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
}
