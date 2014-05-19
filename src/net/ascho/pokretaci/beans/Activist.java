package net.ascho.pokretaci.beans;

import java.util.List;

public class Activist {
	
	
	public static Activist UserProfile = new Activist();
	private static Object syncObj = new Object();
	
    public static Activist getUserProfile() {
    	synchronized (syncObj) {
			if (UserProfile == null) {
				UserProfile = new Activist();
				UserProfile.id = "-1";
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
	public int commentsCount, goalsCount, votesCount, supportCount;
	public List<Goal> goals;
	
	public void setUserGoals(List<Goal> list) {
		goals = list;
	}
	
	public List<Goal> getUserGoals() {
		return goals;
	}
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
