package net.ascho.pokretaci.backend.communication;

import net.ascho.pokretaci.backend.executors.goals.GoalFetcher;
import net.ascho.pokretaci.backend.executors.login.GoogleLogin;
import net.ascho.pokretaci.beans.Goal;
import android.app.Activity;

public final class TaskFactory {
	

	
/*	private static final Task createLoginTask() {
		return null;
	}*/
	
	/**
	 * {@link Task} koji se loguje na sajt koristeci prosledjeni google nalog. 
	 * @param userEmail - korisnikov gmail
	 * @param callingActivity - Aktivnost koristi GoogleLib u slucaju da korisnik prvi put daje aplikaciji
	 * pravo pristupa, otvara poseban access dialog gde daje dozvolu.
	 * @return
	 */
	public static final Task googleLoginTask(String userEmail, Activity callingActivity) {
		return new GoogleLogin(userEmail, callingActivity);
	}
	
	/**
	 * Dohvati Goals po user id ili goal id
	 * @param fetchType
	 * @param id
	 * @return
	 */
	public static final Task goalFetchTask(int fetchType, String id) {
		return new GoalFetcher(fetchType, id);
	}
	
	/**
	 * Dohvati Goals po filtru npr: {@link Goal.GOAL_FILTER.NEWEST}
	 * @param fetchType
	 * @param filter
	 * @return
	 */
	public static final Task goalFetchTask(int fetchType, int filter) {
		return new GoalFetcher(fetchType, filter);
	}
	
	public static final Task goalFetchTask(int fetchType, int filter, int category) {
		return new GoalFetcher(fetchType, filter, category);
	}
	
	/**
	 * Dohvata ciljeve u radijusu prosledjenom
	 * @param fetchType
	 * @param ne_lat
	 * @param ne_lon
	 * @param sw_lat
	 * @param sw_lon
	 * @return
	 */
	public static final Task goalFetchTask(int fetchType, float ne_lat, float ne_lon, float sw_lat, float sw_lon) {
		return new GoalFetcher(fetchType, ne_lat, ne_lon, sw_lat, sw_lon);
	}
	/*private static final Task facebookLooginTask() {
		return null;
	}*/
	
	private static final Task reportProblemTask() {
		return null;
	}
	
	private static final Task commentProblemTask() {
		return null;
	}

	private static final Task supportProblemTask() {
		return null;
	}
}
