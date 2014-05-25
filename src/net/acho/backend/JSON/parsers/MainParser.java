package net.acho.backend.JSON.parsers;

import java.util.ArrayList;
import java.util.List;

import net.ascho.pokretaci.beans.Activist;
import net.ascho.pokretaci.beans.Comment;
import net.ascho.pokretaci.beans.Goal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainParser {
	
	/**
	 * Parsira korisnikov profil
	 * @param json
	 * @return
	 * @throws JSONException 
	 */
	public static List<Activist> parseUserProfile(String json) throws JSONException {
		if(json == null)
			return null;
		
		Activist activist = Activist.getUserProfile();
		JSONObject tempObject;
		List<Activist> activists = new ArrayList<Activist>();
		
		JSONObject job = new JSONObject(json);
		tempObject = job.optJSONObject("data");
		
		if(tempObject == null)
			return null;
		
		activist.id = tempObject.optJSONObject("_id").getString("$id");
		
		activist.first_name = tempObject.optString("name");
		activist.avatar = tempObject.optString("avatar");
		
		if(tempObject.optJSONObject("balance") != null) {
			activist.votesCount = tempObject.optJSONObject("balance").optInt("votes");
			activist.goalsCount = tempObject.optJSONObject("balance").optInt("goals");
			activist.supportCount = tempObject.optJSONObject("balance").optInt("support");
			activist.commentsCount = tempObject.optJSONObject("balance").optInt("comments");
		}
		
		activist.email = tempObject.optString("email");
		activist.full_name = tempObject.optString("full_name");
		
		return activists;
	}
	
	/**
	 * Da li je korisnik ulogovan. Nazalost posle ovoga se mora opet dohvatiti korisnikov profil sa interneta jer ovo ne vraca
	 * sve podatke. *Note: Moze da se prepravi na sajtu da taj url vraca iste podatke kao pri dohvatanju profila
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Activist parseLoggedIn(String json) throws JSONException {
		if(json == null)
			return null;
		
		Activist activist = Activist.getUserProfile();
		JSONObject tempObject;

		
		JSONObject job = new JSONObject(json);
		tempObject = job.optJSONObject("data");
		if(tempObject == null)
			return null;
		
		//Ne koristim json opt, ako nema Id ili name itd, doslo je do greske na serveru 
		activist.id = tempObject.getJSONObject("user_id").getString("$id");
		activist.first_name = tempObject.getString("name");
		activist.avatar = tempObject.getString("avatar");
		activist.votesCount = tempObject.getJSONObject("balance").optInt("votes");
		
		return activist;
		
	}
	
	
	
	public static Activist parseActivist(String json) throws JSONException {
		if(json == null)
			return null;
		
		Activist activist = new Activist();
		JSONObject tempObject;
		
		JSONObject job = new JSONObject(json);
		tempObject = job.optJSONObject("data");
		
		if(tempObject == null)
			return null;
		
		activist.id = tempObject.optJSONObject("_id").getString("$id");
		
		activist.first_name = tempObject.optString("name");
		activist.avatar = tempObject.optString("avatar");
		
		if(tempObject.optJSONObject("balance") != null) {
			activist.votesCount = tempObject.optJSONObject("balance").optInt("votes");
			activist.goalsCount = tempObject.optJSONObject("balance").optInt("goals");
			activist.supportCount = tempObject.optJSONObject("balance").optInt("support");
			activist.commentsCount = tempObject.optJSONObject("balance").optInt("comments");
		}
		
		activist.email = tempObject.optString("email");
		activist.full_name = tempObject.optString("full_name");
		
		return activist;
	}
	
	public static Activist parseActivist(JSONObject user) throws JSONException {
		if(user == null)
			return null;
		Activist activist = new Activist();
		JSONObject tempObject;
		
		
		tempObject = user;
		
		activist.id = tempObject.optJSONObject("_id").getString("$id");
		
		activist.first_name = tempObject.optString("name");
		activist.avatar = tempObject.optString("avatar");
		
		if(tempObject.optJSONObject("balance") != null) {
			activist.votesCount = tempObject.optJSONObject("balance").getInt("votes");
			activist.goalsCount = tempObject.optJSONObject("balance").getInt("goals");
			activist.supportCount = tempObject.optJSONObject("balance").getInt("support");
			activist.commentsCount = tempObject.optJSONObject("balance").getInt("comments");
		}
		
		
		activist.email = tempObject.optString("email");
		activist.full_name = tempObject.optString("full_name");
		
		return activist;
	}
	
	/**
	 * Parsira problem dohvacen po id-u
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static List<Goal> parseParticularGoal(String json) throws JSONException {
		Goal tmpGoal;
		JSONObject job, tmpJob;
		JSONObject geo;
		JSONArray jar;
		
		tmpJob = new JSONObject(json);
		job = tmpJob.optJSONObject("data");
		
		List<Goal> goals = new ArrayList<Goal>();
		
		if(job != null) {
			

	    		tmpGoal =  new Goal();
	    		
	    		JSONObject problem = job;
	    		/*tmpGoal.id = problem.optJSONObject("_id").getString("$id");
	    		tmpGoal.title = problem.optString("title");
	    		tmpGoal.description = problem.optString("description");
	    		geo = problem.optJSONObject("location").optJSONObject("geo");
	    		tmpGoal.lat = geo.optDouble("lon");
	    		tmpGoal.lon = geo.optDouble("lat");
	    		
	    		if(problem.optJSONObject("parsed_date") != null) {
	    			tmpGoal.parsed_date = problem.getJSONObject("parsed_date").getString("text");
	    		}
	    		
	    		tmpGoal.created_at = problem.optString("created_at");
	    		tmpGoal.discussions_count = problem.optInt("discussions_count");
	    		
	    		
	    		tmpGoal.location_name = problem.optJSONObject("location").optString("name");
	    		tmpGoal.image = problem.optString("image");
	    		tmpGoal.supporters_count = problem.optInt("supporters_count");
	    		
	    		tmpGoal.supportable = problem.getBoolean("supportable");
	    		tmpGoal.unsupportable = problem.getBoolean("unsupportable");
	    		tmpGoal.dissaproved = problem.getBoolean("dissaproved");
	    		tmpGoal.supported = problem.getBoolean("supported");
	    		
	    		jar = problem.optJSONArray("topics");
	    		if(jar != null) {
		    		for(int x = 0; x < jar.length(); x++ ) {
		    			tmpGoal.categories.add(jar.getString(x));
		    		}
	    		}
	    		tmpGoal.creator = new Activist();
	    		tmpGoal.creator = parseActivist(problem.optJSONObject("user"));*/
	    		goals.add(parseGoal(problem));
	    		
	    		return goals;
		} else {
			return null;
		}
		
	}
	
	/**
	 * Parsira dohvacene probleme
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static List<Goal> parseGoals(String json) throws JSONException {
		if(json == null)
			return null;
		Goal tmpGoal;
		JSONObject job, tmpJob;
		JSONObject geo;
		JSONArray jar, tmpJar;
		
		tmpJob = new JSONObject(json);
		job = tmpJob.optJSONObject("data");
		List<Goal> goals = new ArrayList<Goal>();
		
		if(job != null) {
			
			
			JSONArray array = job.names();
	    	for (int i = 0; i < array.length(); i++) {
	    		tmpGoal =  new Goal();
	    		String name = array.getString(i);
	    		
	    		JSONObject problem = job.getJSONObject(name);
	   /* 		tmpGoal.id = problem.optJSONObject("_id").getString("$id");
	    		tmpGoal.title = problem.optString("title");
	    		tmpGoal.description = problem.optString("description");
	    		geo = problem.optJSONObject("location").optJSONObject("geo");
	    		tmpGoal.lat = geo.optDouble("lon");
	    		tmpGoal.lon = geo.optDouble("lat");
	    		
	    		if(problem.optJSONObject("parsed_date") != null) {
	    			tmpGoal.parsed_date = problem.getJSONObject("parsed_date").getString("text");
	    		}
	    		
	    		tmpGoal.created_at = problem.optString("created_at");
	    		tmpGoal.discussions_count = problem.optInt("discussions_count");
	    		
	    		
	    		tmpGoal.location_name = problem.optJSONObject("location").optString("name");
	    		tmpGoal.image = problem.optString("image");
	    		tmpGoal.supporters_count = problem.optInt("supporters_count");
	    		
	    		tmpGoal.supportable = problem.getBoolean("supportable");
	    		tmpGoal.unsupportable = problem.getBoolean("unsupportable");
	    		tmpGoal.dissaproved = problem.getBoolean("dissaproved");
	    		tmpGoal.supported = problem.getBoolean("supported");
	    		
	    		jar = problem.optJSONArray("topics");
	    		if(jar != null) {
		    		for(int x = 0; x<jar.length(); x++ ) {
		    			tmpGoal.categories.add(jar.getString(x));
		    		}
	    		}
	    		tmpGoal.creator = new Activist();
	    		tmpGoal.creator = parseActivist(problem.optJSONObject("user"));*/
	    		goals.add(parseGoal(problem));
	    	} 
		} else {
	    		
	    		tmpJar = tmpJob.getJSONArray("data");
	    		//JSONArray array = job.names();
		    	for (int i = 0; i < tmpJar.length(); i++) {
		    		tmpGoal =  new Goal();
		    		//String name = array.getString(i);
		    		JSONObject problem = tmpJar.getJSONObject(i);
		    		
		    		goals.add(parseGoal(problem));
	    	}
		}
		
    	return goals;
		
	}

	/**
	 * Parsira probleme sa korisnikovog profila
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static List<Goal> parseUserProfileGoals(String json) throws JSONException {
		if(json == null)
			return null;
		Goal tmpGoal;
		JSONObject job, tmpJob;
		JSONObject geo;
		JSONArray jar, tmpJar;
		
		tmpJob = new JSONObject(json);
		job = tmpJob.optJSONObject("data");
		List<Goal> goals = new ArrayList<Goal>();
		
		if(job != null) {
			
			
			JSONArray array = job.names();
	    	for (int i = 0; i < array.length(); i++) {
	    		tmpGoal =  new Goal();
	    		String name = array.getString(i);
	    		
	    		JSONObject problem = job.getJSONObject(name);
	   /* 		tmpGoal.id = problem.optJSONObject("_id").getString("$id");
	    		tmpGoal.title = problem.optString("title");
	    		tmpGoal.description = problem.optString("description");
	    		geo = problem.optJSONObject("location").optJSONObject("geo");
	    		tmpGoal.lat = geo.optDouble("lon");
	    		tmpGoal.lon = geo.optDouble("lat");
	    		
	    		if(problem.optJSONObject("parsed_date") != null) {
	    			tmpGoal.parsed_date = problem.getJSONObject("parsed_date").getString("text");
	    		}
	    		
	    		tmpGoal.created_at = problem.optString("created_at");
	    		tmpGoal.discussions_count = problem.optInt("discussions_count");
	    		
	    		
	    		tmpGoal.location_name = problem.optJSONObject("location").optString("name");
	    		tmpGoal.image = problem.optString("image");
	    		tmpGoal.supporters_count = problem.optInt("supporters_count");
	    		
	    		tmpGoal.supportable = problem.getBoolean("supportable");
	    		tmpGoal.unsupportable = problem.getBoolean("unsupportable");
	    		tmpGoal.dissaproved = problem.getBoolean("dissaproved");
	    		tmpGoal.supported = problem.getBoolean("supported");
	    		
	    		jar = problem.optJSONArray("topics");
	    		if(jar != null) {
		    		for(int x = 0; x<jar.length(); x++ ) {
		    			tmpGoal.categories.add(jar.getString(x));
		    		}
	    		}
	    		tmpGoal.creator = new Activist();
	    		tmpGoal.creator = parseActivist(problem.optJSONObject("user"));*/
	    		goals.add(parseGoal(problem));
	    	} 
		} else {
	    		
	    		tmpJar = tmpJob.getJSONArray("data");
	    		//JSONArray array = job.names();
		    	for (int i = 0; i < tmpJar.length(); i++) {
		    		tmpGoal =  new Goal();
		    		//String name = array.getString(i);
		    		JSONObject problem = tmpJar.getJSONObject(i);
		    		
		    		goals.add(parseGoal(problem));
	    	}
		}
		
    	return goals;
		
	}

	/**
	 * Parsira jedan problem
	 * @param problem
	 * @return
	 * @throws JSONException
	 */
	public static Goal parseGoal(JSONObject problem) throws JSONException {
		JSONObject geo;
		JSONArray jar, tmpJar;
		Goal tmpGoal = new Goal();
		
		tmpGoal.id = problem.optJSONObject("_id").getString("$id");
		tmpGoal.title = problem.optString("title");
		tmpGoal.description = problem.optString("description");
		JSONObject location = problem.optJSONObject("location");
		if(location != null) {
			geo = location.optJSONObject("geo");
			tmpGoal.lat = geo.optDouble("lon");
			tmpGoal.lon = geo.optDouble("lat");
		}
		
		tmpGoal.location_image = location.optString("image");
		

		tmpGoal.supported = problem.optBoolean("supports");
		
		if(problem.optJSONObject("parsed_date") != null) {
			tmpGoal.parsed_date = problem.getJSONObject("parsed_date").getString("text");
		}
		
		tmpGoal.created_at = problem.optString("created_at");
		tmpGoal.discussions_count = problem.optInt("discussions_count");
		tmpGoal.state = problem.optString("state");
		
		tmpGoal.location_name = problem.optJSONObject("location").optString("name");
		tmpGoal.image = problem.optString("default_image");
		tmpGoal.supporters_count = problem.optInt("supporters_count");
		
		tmpGoal.supportable = problem.optBoolean("supportable");
		tmpGoal.unsupportable = problem.optBoolean("unsupportable");
		tmpGoal.dissaproved = problem.optBoolean("dissaproves");
		
		tmpGoal.editable = problem.optBoolean("editable");
		
		
		jar = problem.optJSONArray("topics");
		if(jar != null) {
    		for(int x = 0; x<jar.length(); x++ ) {
    			tmpGoal.categories.add(jar.getString(x));
    		}
		}
		tmpGoal.creator = new Activist();
		tmpGoal.creator = parseActivist(problem.optJSONObject("user"));
		
		return tmpGoal;
	}
	
	
	public static List<Comment> parseCommentsForGoal(String json) throws JSONException {
		JSONObject job, tmpJob;
		JSONArray jar;
		Comment tempComment;
		List<Comment> comments = new ArrayList<Comment>();
		
		job = new JSONObject(json);
		jar = job.optJSONArray("data");
		
		if(jar == null || jar.length() == 0) 
			return null;
		
		for(int i = 0; i < jar.length(); i++) {
			tmpJob = jar.getJSONObject(i);
			if(tmpJob.optString("activity_type").compareTo("new_discussion") == 0 ) {
				tempComment = new Comment();
				tempComment.content = tmpJob.optString("content");
				tempComment.created_at = tmpJob.optString("created_at");
				if(tmpJob.optJSONObject("parsed_date") != null) {
					tempComment.parsed_date = tmpJob.optJSONObject("parsed_date").optString("text");
				}
				
				
				if(tmpJob.optJSONObject("types") != null) {
					if(tmpJob.optJSONObject("types").optString("image") != null) {
						tempComment.image = tmpJob.optJSONObject("types").optString("image");
					}
						
					if(tmpJob.optJSONObject("types").optJSONObject("link") != null) {
						// tmpJob.optJSONObject("types").optJSONObject("link").optString("value") != null
						tempComment.link = tmpJob.optJSONObject("types").optJSONObject("link").optString("value");
					}
					if(tmpJob.optJSONObject("types").optJSONObject("meeting") != null) {
						tempComment.meeting_location = tmpJob.optJSONObject("types").optJSONObject("meeting").optString("location");
						tempComment.meeting_date = tmpJob.optJSONObject("types").optJSONObject("meeting").optString("date");
					}
				
				}
				

				
					
				
				
				
				tempComment.commenter = parseActivist(tmpJob.optJSONObject("user"));
				comments.add(tempComment);
			}
		}
		return comments;
		
	}
	
	public static Comment parseComment(String json) throws JSONException {
		JSONObject tmpJob;
		Comment tempComment;
		
		tmpJob = new JSONObject(json);
		tmpJob = tmpJob.optJSONObject("data");
		
		if(tmpJob == null) 
			return null;
		
		tempComment = new Comment();
		tempComment.content = tmpJob.getString("content");
		tempComment.created_at = tmpJob.getString("created_at");
		if(tmpJob.optJSONObject("parsed_date") != null) {
			tempComment.parsed_date = tmpJob.optJSONObject("parsed_date").optString("text");
		}
		
		
		if(tmpJob.optJSONObject("types") != null) {
			if(tmpJob.optJSONObject("types").optString("image") != null) {
				tempComment.image = tmpJob.optJSONObject("types").optString("image");
			}
				
			if(tmpJob.optJSONObject("types").optJSONObject("link") != null) {
				// tmpJob.optJSONObject("types").optJSONObject("link").optString("value") != null
				tempComment.link = tmpJob.optJSONObject("types").optJSONObject("link").optString("value");
			}
			
			if(tmpJob.optJSONObject("types").optJSONObject("meeting") != null) {
				tempComment.meeting_date = tmpJob.optJSONObject("meeting").optString("location");
				tempComment.meeting_date = tmpJob.optJSONObject("meeting").optString("date");
			}
		
		}
		
		
		tempComment.commenter = parseActivist(tmpJob.optJSONObject("user"));
			
		return tempComment;
	}
	/*//unsuport

	{"data":{"message":"Problem je vracen na pocetno stanje.","success":true,"auth":{"status":true}},"auth":{"status":true,"logged_in":true}}

	//support
	{"data":{"success":{"status":true},"auth":{"status":true}},"auth":{"status":true,"logged_in":true}}

	//dissaprove
	{"data":{"success":"1","auth":{"status":true}},"auth":{"status":true,"logged_in":true}}*/
	
	/**
	 * Parsira odgovor pri odpodrzavanju problema
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static final String parseUnsupportGoal(String json) throws JSONException {
		JSONObject job;
		
		if(json == null) 
			return null;
		
		job = new JSONObject(json);
		job = job.optJSONObject("data");
		
		if(job == null) {
			return null;
		}
		
		boolean status = job.getBoolean("success");
		
		if(status) {
			return "Više ne podržavate ovaj problem";
		} else {
			return null;
		}
		
		
		
	}
	
	
	/**
	 * Parsira odgovor pri podrzavanju problema
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static final String parseSupportGoal(String json) throws JSONException {
		JSONObject job;
		
		if(json == null) 
			return null;
		
		job = new JSONObject(json);
		job = job.optJSONObject("data");
		
		if(job == null) {
			return null;
		}
		
		boolean status = job.getJSONObject("success").getBoolean("status");
		
		if(status) {
			return "Problem uspešno podržan!";
		} else {
			return null;
		}
		
		
		}
	
	/**
	 * Parsira odgovor pri flago-ovanju problema
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static final String parseFlagGoal(String json) throws JSONException {
		JSONObject job;
		
		if(json == null) 
			return null;
		
		job = new JSONObject(json);
		job = job.optJSONObject("data");
		
		if(job == null) {
			return null;
		}
		
		int status = job.getInt("success");
		
		if(status == 1) {
			return "Hvala Vam na prijavi, neko od admina će pogledati ovaj problem";
		} else {
			return null;
		}
	}


	
}
