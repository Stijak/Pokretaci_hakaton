package net.ascho.pokretaci.backend;

/**
 * Konfiguracioni parametri kao i neke globalne konstante
 * @author Bojan
 *
 */
public class Config {
	
	public static final String PARAM = "$param";
	
	public static final String BASE_URL = "http://www.pokretaci.rs/";
	
	/**
	 * $param = google_token
	 */
	public static final String LOGIN_BASE_URL = BASE_URL + "auth/google/" + PARAM + "/access";
	public static final String LOGOUT_URL = BASE_URL + "logout";
	public static final String CHECK_LOGIN_URL = BASE_URL + "api/activists/logged";
	/**
	 * $param = user_id
	 */
	public static final String USER_INFO_URL = BASE_URL + "api/activists/" + PARAM;
	public static final String GOAL_DATA_FOR_USER_URL = BASE_URL + "api/activists/" + PARAM + "/goals?type=support";
	
	public static final String GOAL_DATA_BY_ALL = BASE_URL + "api/goals/all";
	public static final String GOAL_DATA_BY_ID_URL = BASE_URL + "api/goals/" + PARAM;
	public static final String GOAL_DATA_BY_FILTER = BASE_URL + "api/goals/filter/";
	public static final String GOAL_DATA_BY_FILTER_CATEGORY = BASE_URL + "api/goals/filter/";
	public static final String GOAL_ACTIVITIES_URL = BASE_URL + "api/goals/" + PARAM + "/activities";
	
	public static final String COMMENT_NEW_COMMENT_INTERACTION_URL = BASE_URL + "/api/discussions/";
	public static final String COMMENT_LIKE_INTERACTION_URL = BASE_URL + "/api/discussions/"+ PARAM + "/like";
	public static final String COMMENT_FLAG_INTERACTION_URL = BASE_URL + "/api/discussions/"+ PARAM + "/flag";
	
	
	public static final String GOAL_DATA_BY_RADIUS = BASE_URL + "api/locations?ne_lat=" + PARAM + "&ne_lng=" + PARAM + "&sw_lat=" + PARAM + "&sw_lng=" + PARAM;
	
	
	public static final class GOAL_FILTERS_URL {
		public static final String NEWEST = "newest";
		public static final String TOP_RATED = "top_rated";
		public static final String TRENDING = "trending";
		public static final String MOST_DISCUSSED = "most_discussed";
	}
	
	public static final String GOAL_SUPPORT_INTERACTION = BASE_URL + "api/goals/" + PARAM + "/support";
	public static final String GOAL_UNSUPPORT_INTERACTION = BASE_URL + "api/goals/" + PARAM + "/unsupport";
	public static final String GOAL_REPORT_INTERACTION = BASE_URL + "api/goals/" + PARAM + "/disapprove";
	public static final String GOAL_EDIT_INTERACTION = BASE_URL + "api/goals/" + PARAM;
	public static final String GOAL_NEW_GOAL_INTERACTION = BASE_URL + "api/goals";
	
	
	public static final String COMMENT_ACTION_URL = BASE_URL + "";
	
	
	public static final int RETRY_DELAY = 450, RETRY_ATTEMPTS = 6, RESPONSE_TIMEOUT = 10000, READ_TIMEOUT = 10000; 
}