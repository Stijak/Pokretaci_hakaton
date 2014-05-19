package net.ascho.pokretaci.backend.communication;

import net.ascho.pokretaci.backend.executors.goals.CommentInteraction;
import net.ascho.pokretaci.backend.executors.goals.GoalFetcher;
import net.ascho.pokretaci.backend.executors.goals.GoalInteraction;
import net.ascho.pokretaci.backend.executors.login.GoogleLogin;
import net.ascho.pokretaci.beans.Comment;
import net.ascho.pokretaci.beans.Goal;
import net.ascho.pokretaci.beans.Goal.GOAL_FETCH_TYPE;
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
	 * Dohvati Goals po user id ili goal id. FetchType je {@link Goal.GOAL_FETCH_TYPE}
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
	public static final Task editGoalTask(Goal goal) {
		return new GoalInteraction(Goal.GOAL_INTERACTION_TYPE.EDIT_GOAL, goal);
	}
	
	public static final Task newGoalTask(Goal goal) {
		return new GoalInteraction(Goal.GOAL_INTERACTION_TYPE.NEW_GOAL, goal);
	}
	
	public static final Task reportGoalTask(Goal goal) {
		return new GoalInteraction(Goal.GOAL_INTERACTION_TYPE.FLAG_GOAL, goal);
	}
	

	public static final Task supportGoalTask(Goal goal) {
		return new GoalInteraction(Goal.GOAL_INTERACTION_TYPE.SUPPORT_GOAL, goal);
	}
	
	public static final Task unsupportGoalTask(Goal goal) {
		return new GoalInteraction(Goal.GOAL_INTERACTION_TYPE.UNSUPPORT_GOAL, goal);
	}
	
	public static final Task commentGoalTask(Comment comment, String goal_id) {
		return new CommentInteraction(Comment.COMMENTS_INTERACTION_TYPE.NEW_COMMENT, comment, goal_id);
	}
}
