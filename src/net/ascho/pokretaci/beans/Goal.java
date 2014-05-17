package net.ascho.pokretaci.beans;

import java.util.ArrayList;
import java.util.List;

public class Goal {
	
	public String id, 
			created_at,
			description,
			location_name,
			state,
			status,
			title,
			image;
	/**
	 * Parsiran datum u formatu : pre 7 min....
	 */
	public String parsed_date;
	/**
	 * Tekst koji ide ispod problema tipa: Masa Misic, bla bla i jos njih 3 podrzavaju ovu inicijativu.
	 * @deprecated
	 */
	public String supportersList;
	public double lon, lat;
	public int discussions_count, supporters_count;
	public List<String> categories = new ArrayList<String>();
	public Activist creator;
	public List<Comment> comments;
	
	
	//Konstante, tipovi, filteri
	
	/**
	 * Odredjuje parametre na osnovu kojih ce da se dohvate problemi npr. {@link #BY_GOAL_ID}, {@link #BY_RADIUS}, {@link #BY_FILTER} ...
	 * @author bojancv
	 *
	 */
	public static final class GOAL_FETCH_TYPE {
		/**
		 * Dovata problem na osnovu id-a
		 */
		public static final int BY_GOAL_ID = 1;
		/**
		 * Dovata problem za ulogovanog korisnika
		 */
		public static final int BY_USER = 2;
		
		/**
		 * Dovata problem za prosledjeni radius
		 */
		public static final int BY_RADIUS = 4;
		/**
		 * Dovata problem po odredjenom filtru
		 */
		public static final int BY_FILTER =5;
	}
	
	/**
	 * Dohvata problem na osnovu filtra npr. GOAL_FILTER.NEWEST
	 * @author bojancv
	 *
	 */
	public static final class GOAL_FILTER {
		/**
		 * Najnovije u okruzenju odakle se pristupa sajtu ili korisnikovom okruzenju ako je ulogovan
		 */
		public static final int NEWEST = 1;
		/**
		 * Najpopularnije inicijative
		 */
		public static final int TOP_RATED = 2;
		/**
		 * Inicijative u trendu
		 */
		public static final int TRENDING = 3;
		/**
		 * Problemi u razresavanju
		 */
		public static final int MOST_DISCUSSED = 4;
		/**
		 * Specificne kategorije iz {@link GOAL_CATEGORY}
		 */
		public static final int CATEGORY = 6;
		//....
	}
	
	public static final class GOAL_CATEGORY {
		
	}
}
