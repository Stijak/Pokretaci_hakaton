package net.ascho.pokretaci.backend.communication;

import net.ascho.pokretaci.backend.executors.goals.CommentInteraction;
import net.ascho.pokretaci.backend.executors.goals.GoalFetcher;
import net.ascho.pokretaci.backend.executors.goals.GoalInteraction;
import net.ascho.pokretaci.backend.executors.login.GoogleLogin;
import net.ascho.pokretaci.backend.executors.login.LogOutTask;
import net.ascho.pokretaci.beans.Comment;
import net.ascho.pokretaci.beans.Goal;
import net.ascho.pokretaci.beans.Goal.GOAL_FETCH_TYPE;
import android.app.Activity;


/**
 * Fabrika za taskove <p>Primer koriscenja taskova koji dohvataju probleme: </p>
 * <code>
 * 		Task testTask1 = TaskFactory.getAllGoals();<br>
		Task testTask2 = TaskFactory.getGoalsByFilter(Goal.GOAL_FILTER.NEWEST);<br>
		Task testTask3 = TaskFactory.getGoalsByFilter(Goal.GOAL_FILTER.TOP_RATED);<br>
		Task testTask4 = TaskFactory.getGoalsByFilter(Goal.GOAL_FILTER.MOST_DISCUSSED);<br>
		Task testTask5 = TaskFactory.getGoalByID("53788e237b83fa8817000006");<br>
		Task testTask6 = TaskFactory.geGoalsForUser(mUserId);<br></code>
		
 * <br>Primer za interakcju sa problemima:<br><br>
 * <code>Task like = TaskFactory.supportGoalTask("5381cd507b83fa35b4000006");<br>
	     Task unlike = TaskFactory.unsupportGoalTask("5381cd507b83fa35b4000006");<br>
	     Task flag = TaskFactory.reportGoalTask("5381cd507b83fa35b4000006");<br><br>
	     
	     Comment com = new Comment();<br>
	     com.content = "Andorid komentar";<br>
	     Task commentTask = TaskFactory.commentGoalTask(com, "5381cd507b83fa35b4000006");</code>
	     <p>New/Edit problema: prosledjujes objekat tipa Goal u kome podesis sva potrebna polja bez goal id. Kada radis edit opet isto samo sada imas i goal id. Ugasio sam za sada upload slika.</p> <code>TaskFactory.newGoalTask(goal);<br>
	     TaskFactory.editGoalTask(goal);</code>
 * @author bojancv
 *
 */
public final class TaskFactory {
	
	/**
	 * Log out korisnika
	 * @return
	 */
	public static final Task logOutTask() {
		return new LogOutTask();
	}
	
/*	private static final Task createLoginTask() {
		return null;
	}*/
	
	/**
	 * {@link Task} koji se loguje na sajt koristeci prosledjeni google nalog. 
	 * <p>Kada se korisnik uloguje automatski se i ucitaju njegovi problemi kao i komentari na te probleme</p>
	 * @param userEmail - korisnikov gmail
	 * @param callingActivity - Aktivnost koristi GoogleLib u slucaju da korisnik prvi put daje aplikaciji
	 * pravo pristupa, otvara poseban access dialog gde daje dozvolu.
	 * @return
	 */
	public static final Task googleLoginTask(String userEmail, Activity callingActivity) {
		return new GoogleLogin(userEmail, callingActivity);
	}
	
	
	/**
	 * Dohvata sve probleme sa sajta. Takodje dohvata i sve komentare na te probleme.
	 * @return
	 */
	public static final Task getAllGoals() {
		return new GoalFetcher(Goal.GOAL_FETCH_TYPE.ALL_GOALS, null);
	}
	
	/**
	 * Dohvata probleme za odredjenog korisnika. Takodje dohvata i sve komentare na te probleme.
	 * @param userID - korisnik ID
	 * @return
	 */
	public static final Task geGoalsForUser(String userID) {
		return new GoalFetcher(Goal.GOAL_FETCH_TYPE.BY_USER, userID);
	}
	
	/**
	 * Dohvata jedan problem po ID-u, Takodje dohvata i sve komentare na taj problem.
	 * @param goalID
	 * @return
	 */
	public static final Task getGoalByID(String goalID) {
		return new GoalFetcher(Goal.GOAL_FETCH_TYPE.BY_GOAL_ID, goalID);
	}
	
	/**
	 * Dohvata probleme sa sajta na osnovu prosledjenog filtera: <br>
	 * <br>  Goal.GOAL_FILTER.NEWEST             -> Najnovije u mom kraju
	 * <br>  Goal.GOAL_FILTER.TOP_RATED          -> Najpopularnije inicijative 
	 * <br>  Goal.GOAL_FILTER.MOST_DISCUSSED     -> U akciji resavanja
	 * <br>  Goal.GOAL_FILTER.TRENDING           -> Trenutno je uklonjena ova opcija na sajtu
	 * <p> Takodje dohvata i sve komentare na te probleme.</p>
	 * @param goalFilter
	 * @return
	 */
	public static final Task  getGoalsByFilter(int goalFilter) {
		return new GoalFetcher(Goal.GOAL_FETCH_TYPE.BY_FILTER, goalFilter);
	}
	
	/**
	 * Tek cemo dodati kategorije, treba sa ivanom da se cujem za vikend da dinamicki ucitavamo te kategorije sa sajta
	 * da se ne bi posle smarali kada budu bile promene u nekoj buducnosti.
	 * @param goalCategory
	 * @return
	 */
	public static final Task  getGoalsByCategory(int goalCategory) {
		return new GoalFetcher(Goal.GOAL_FETCH_TYPE.BY_FILTER, goalCategory);
	}
	
	/**
	 * Dohvata ciljeve u prosledjenom radijusu. 
	 * @param fetchType
	 * @param ne_lat
	 * @param ne_lon
	 * @param sw_lat
	 * @param sw_lon
	 * @return
	 */
	public static final Task goalFetchTask(float ne_lat, float ne_lon, float sw_lat, float sw_lon) {
		return new GoalFetcher(Goal.GOAL_FETCH_TYPE.BY_RADIUS, ne_lat, ne_lon, sw_lat, sw_lon);
	}
	
	
	
	/**
	 * Dohvati Goals po user id ili goal id. FetchType je {@link Goal.GOAL_FETCH_TYPE}
	 * @param fetchType
	 * @param id
	 * @return
	 * @deprecated Napravljene su funkcije {@link #getAllGoals()}, {@link #getGoalByID(String)}, {@link #getGoalsByCategory(int)}, {@link #getGoalsByFilter(int)}, {@link #getUserGoals(String)}
	 */
	public static final Task goalFetchTask(int fetchType, String id) {
		return new GoalFetcher(fetchType, id);
	}
	
	/**
	 * 
	 * Dohvati Goals po filtru npr: {@link Goal.GOAL_FILTER.NEWEST}
	 * @param fetchType
	 * @param filter
	 * @return
	 *  @deprecated Napravljene su funkcije {@link #getAllGoals()}, {@link #getGoalByID(String)}, {@link #getGoalsByCategory(int)}, {@link #getGoalsByFilter(int)}, {@link #getUserGoals(String)}
	 */
	public static final Task goalFetchTask(int fetchType, int filter) {
		return new GoalFetcher(fetchType, filter);
	}
	
	/**
	 *  @deprecated Napravljene su funkcije {@link #getAllGoals()}, {@link #getGoalByID(String)}, {@link #getGoalsByCategory(int)}, {@link #getGoalsByFilter(int)}, {@link #getUserGoals(String)}
	 * @param fetchType
	 * @param filter
	 * @param category
	 * @return
	 */
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
	 *  @deprecated Napravljene su funkcije {@link #getAllGoals()}, {@link #getGoalByID(String)}, {@link #getGoalsByCategory(int)}, {@link #getGoalsByFilter(int)}, {@link #getUserGoals(String)}
	 */
	public static final Task goalFetchTask(int fetchType, float ne_lat, float ne_lon, float sw_lat, float sw_lon) {
		return new GoalFetcher(fetchType, ne_lat, ne_lon, sw_lat, sw_lon);
	}
	
	/**
	 * Editovanje Goal-a
	 * @param goal 
	 * @return
	 */
	public static final Task editGoalTask(Goal goal) {
		return new GoalInteraction(Goal.GOAL_INTERACTION_TYPE.EDIT_GOAL, goal);
	}
	
	/**
	 * New Goal
	 * @param goal
	 * @return
	 */
	public static final Task newGoalTask(Goal goal) {
		return new GoalInteraction(Goal.GOAL_INTERACTION_TYPE.NEW_GOAL, goal);
	}
	
	/**
	 * Flag Goal
	 * @param goal
	 * @return
	 */
	public static final Task reportGoalTask(Goal goal) {
		return new GoalInteraction(Goal.GOAL_INTERACTION_TYPE.FLAG_GOAL, goal);
	}
	
	/**
	 * Support Goal (podrzavam problem)
	 * @param goal
	 * @return
	 */
	public static final Task supportGoalTask(Goal goal) {
		return new GoalInteraction(Goal.GOAL_INTERACTION_TYPE.SUPPORT_GOAL, goal);
	}
	
	
	
	/**
	 * Unsuport Goal (prestajem da podrzavam problem)
	 * @param goal
	 * @return
	 */
	public static final Task unsupportGoalTask(Goal goal) {
		return new GoalInteraction(Goal.GOAL_INTERACTION_TYPE.UNSUPPORT_GOAL, goal);
	}
	
	
	/**
	 * Flag Goal
	 * @param goal
	 * @return
	 */
	public static final Task reportGoalTask(String goal_id) {
		Goal goal = new Goal();
		goal.id = goal_id;
		return new GoalInteraction(Goal.GOAL_INTERACTION_TYPE.FLAG_GOAL, goal);
	}
	
	/**
	 * Support Goal (podrzavam problem)
	 * @param goal
	 * @return
	 */
	public static final Task supportGoalTask(String goal_id) {
		Goal goal = new Goal();
		goal.id = goal_id;
		return new GoalInteraction(Goal.GOAL_INTERACTION_TYPE.SUPPORT_GOAL, goal);
	}
	
	
	
	/**
	 * Unsuport Goal (prestajem da podrzavam problem)
	 * @param goal
	 * @return
	 */
	public static final Task unsupportGoalTask(String goal_id) {
		Goal goal = new Goal();
		goal.id = goal_id;
		return new GoalInteraction(Goal.GOAL_INTERACTION_TYPE.UNSUPPORT_GOAL, goal);
	}
	
	
	
	/**
	 * Comment Goal 
	 * @param comment - Komentar koji se postavlja na problem. Nije mu potrebno podesavati lokaciju ili datum sastanka, kaze Ivona da nece toga biti u app
	 * @param goal_id
	 * @return
	 */
	public static final Task commentGoalTask(Comment comment, String goal_id) {
		return new CommentInteraction(Comment.COMMENTS_INTERACTION_TYPE.NEW_COMMENT, comment, goal_id);
	}
}
