package net.ascho.pokretaci.beans;

import net.ascho.pokretaci.backend.communication.ApacheClient;

public class Activist {
	
	
	public static Activist UserProfile = new Activist();
	private static Object syncObj = new Object();
	
    public static Activist getUserProfile() {
    	synchronized (syncObj) {
			if (UserProfile == null) {
				UserProfile = new Activist();
			}
            return UserProfile;
		}
    } 
    
    
	public String id,
				first_name,
				full_name,
				email,
				avatar;
	public Pois POIS;
	public int comments, goals, votes, support;
	
	/**
	 * Trougao koji predstavlja korisnikovu "okolinu", parovi(lat,lon) su tacke smestene u 3 lat i 3 lon vrednosti u 2 niza
	 * @author Bojan
	 *
	 */
	public class Pois {
		public double[] lat = new double[3];
		public double[] lon = new double[3];
	}
}
