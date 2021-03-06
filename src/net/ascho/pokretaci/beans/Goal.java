package net.ascho.pokretaci.beans;

import java.util.ArrayList;
import java.util.List;

public class Goal {
	
	public String id, 
			created_at,
			description,
			location_name,
			location_image,
			state,
			status,
			title,
			people,
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
	public double lon = 0.0d, lat = 0.0d;
	public int discussions_count, supporters_count;
	public List<String> categories = new ArrayList<String>();
	public Activist creator;
	public List<Comment> comments;
	
	public boolean supportable;
	public boolean unsupportable;
	public boolean supported;
	public boolean dissaproved;
	public boolean editable;
	
	public List<Comment> getComments() {
		return comments;
	}
	
	/**
	 * True u slucaju da problem moze da se podrzi i da ga korisnik nije vec podrzao. Ne proverava da li korisnik ima dovljno glasova
	 * @return
	 */
	public boolean canUserSupport() {
		return (supportable && !supported);
	}
	
	/**
	 * Da li problem moze da se odpodrzi i da li ga korisnik vec podrzava
	 * @return
	 */
	public boolean canUserUnsupport() {
		return (unsupportable && supported);
	}
	
	/**
	 * Dissaprove je flagovanje problema na sajtu. Da li moze korisnik da flaguje problem
	 * @return
	 */
	public boolean canUserDissaprove() {
		return (!dissaproved);
	}
	
	public boolean canUserEdit() {
		return editable;
	}
	
	/**
	 * {@link GOAL_STATES} : <br>
	 * Goal.GOAL_STATES.NEW_GOAL = 1 <br>
	 * Goal.GOAL_STATES.SUPPORTED_GOAL = 2 <br>
	 * Goal.GOAL_STATES.USER_GOAL = 3 <br>
	 * Goal.GOAL_STATES.RESOLVED_GOAL = 4
	 * @return
	 */
	public int mapPinType() {
		if(state == null) {
			return GOAL_STATES.NEW_GOAL; 
		}
		if(state.compareTo("resolved") == 0) {
			return Goal.GOAL_STATES.RESOLVED_GOAL;
		}
		if(creator.id.equals(Activist.getUserProfile().id)) {
			return GOAL_STATES.USER_GOAL;
		} else if(supported) {
			return GOAL_STATES.SUPPORTED_GOAL;
		} else {
			return GOAL_STATES.NEW_GOAL;
		}
		
	}
	//Konstante, tipovi, filteri
	
	/**
	 * Odredjuje parametre na osnovu kojih ce da se dohvate problemi npr. {@link #BY_GOAL_ID}, {@link #BY_RADIUS}, {@link #BY_FILTER} ...
	 * @author bojancv
	 *
	 */
	public static final class GOAL_FETCH_TYPE {
		
		public static final int ALL_GOALS = 77;
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
		public static final int BY_FILTER = 5;
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
	
	public static final class GOAL_INTERACTION_TYPE {
		public static final int SUPPORT_GOAL = 1;
		public static final int UNSUPPORT_GOAL = 2;
		public static final int FLAG_GOAL = 3;
		public static final int EDIT_GOAL = 4;
		public static final int NEW_GOAL = 5;
	}
	
	/**
	 * Stanja problema - pinovi na mapi
	 * @author Bojan
	 *
	 */
	public static final class GOAL_STATES {
		public static final int NEW_GOAL = 1;
		public static final int SUPPORTED_GOAL = 2;
		public static final int USER_GOAL = 3;
		public static final int RESOLVED_GOAL = 4;
	}
}
