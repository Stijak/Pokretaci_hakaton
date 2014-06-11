package net.ascho.pokretaci.backend.executors.goals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.acho.backend.JSON.parsers.MainParser;
import net.ascho.pokretaci.backend.Config;
import net.ascho.pokretaci.backend.beans.ServerResponseObject;
import net.ascho.pokretaci.backend.communication.ApacheClient;
import net.ascho.pokretaci.backend.communication.Task;
import net.ascho.pokretaci.backend.util.Util;
import net.ascho.pokretaci.beans.Goal;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class GoalInteraction extends Task {
	
	private int mInteraction;
	private Goal mGoal;
	private String mGoalId;
	
	public GoalInteraction(int interactionType, Goal goal) {
		mInteraction = interactionType;
		mGoal = goal;
		if(goal.id != null) 
			mGoalId = goal.id;
	}
	
	public GoalInteraction(int interactionType, String goal_id) {
		mInteraction = interactionType;
		mGoalId = goal_id;
	}
	
	@Override
	protected ServerResponseObject executeWork() throws Exception {
		
		
		ApacheClient apache = ApacheClient.getInstance();
		
		ServerResponseObject sob = new ServerResponseObject();
		
		String tmpMsg = null;
		
		String url = buildInteractionUrl();
		Log.d("odgovor", "goal interaction url: " + url);
		String JSONresponse;
		HttpResponse httpResponse;
		List<Goal> goals = new ArrayList<Goal>();
		String success = null;
		
		switch(mInteraction) {
		
			case Goal.GOAL_INTERACTION_TYPE.SUPPORT_GOAL:
				httpResponse = apache.postRequest(url, new StringEntity("random_string"));
				JSONresponse = Util.inputStreamToString(httpResponse.getEntity().getContent());
				success = MainParser.parseSupportGoal(JSONresponse);
				break;
				
			case Goal.GOAL_INTERACTION_TYPE.UNSUPPORT_GOAL:
				httpResponse = apache.postRequest(url, new StringEntity("random_string"));
				JSONresponse = Util.inputStreamToString(httpResponse.getEntity().getContent());
				success = MainParser.parseUnsupportGoal(JSONresponse);
				break;
			
			case Goal.GOAL_INTERACTION_TYPE.FLAG_GOAL:
				httpResponse = apache.postRequest(url, new StringEntity("random_string"));
				JSONresponse = Util.inputStreamToString(httpResponse.getEntity().getContent());
				success = MainParser.parseSupportGoal(JSONresponse);
				break;
			
			case Goal.GOAL_INTERACTION_TYPE.NEW_GOAL: 
				mGoal.id = null; //just in case
			case Goal.GOAL_INTERACTION_TYPE.EDIT_GOAL:
				
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			    
			    //builder.setCharset(Charset.forName("UTF-8"));
				/*
				 * Radi pri insertu goal-a ali samo sa jednim topicom? Sjebe se pri update.
				 * 
				 * httpResponse = apache.postRequest(url, generateGoalHashMap(mGoal));
				JSONresponse = Util.inputStreamToString(httpResponse.getEntity().getContent());
				JSONObject job = new JSONObject(JSONresponse);
				Goal goal = MainParser.parseGoal(job.getJSONObject("data"));*/
//				 List <NameValuePair> nvps = new ArrayList <NameValuePair>();
//				 
//				 nvps.add(new BasicNameValuePair("title", mGoal.title));
//				 nvps.add(new BasicNameValuePair("description", mGoal.description));
//				 nvps.add(new BasicNameValuePair("people", mGoal.people));
//				 nvps.add(new BasicNameValuePair("location[name]", mGoal.location_name));
//				 nvps.add(new BasicNameValuePair("location[geo][lon]", Double.toString(mGoal.lon)));
//				 nvps.add(new BasicNameValuePair("location[geo][lat]", Double.toString(mGoal.lat)));
				 
				 if(mGoal.image != null) {
					//ako je upitanju url sa servera ne treba nista da se uradi
					if(!mGoal.image.startsWith("http") && !mGoal.image.startsWith("www")) {
						
						
						File file = new File(mGoal.image);
						String contentType = Util.getContentType(mGoal.image);
						
						/**
						 * Ovo je stari kod koji je bio u aplikaciji
						 * 
						 * 
					     * builder.addBinaryBody("image", file, ContentType.create(contentType), file.getName());
						 * 
						 * 
						 */

						
					    
					    
					     InputStream fis = new FileInputStream(mGoal.image);
					     builder.addBinaryBody("image", fis, ContentType.create(contentType), file.getName());
					   
						
						
					} else {
						Log.d("dkosad", "ne treba da radi upload");
					}
					
				 }
				 
				
				 	/**
				 	 * ********************************************
				 	 * 
				 	 * 
				 	 * Ovaj kod ucita sliku iz assetsa i uradi upload lepo i isti je kao onaj gore sto bi trebao da probas :)
				 	 
				    InputStream fis =getContext().getAssets().open("desert.jpg");
				    builder.addBinaryBody("image", fis, ContentType.create("jpg"), "desert.jpg");
				 	*/
				 if(mGoal.id != null) {
//					 nvps.add(new BasicNameValuePair("id", mGoal.id));
					 builder.addTextBody("id", mGoal.id);
				 }
				 
				 String topics = "";
					if(mGoal.categories != null) {
						if(mGoal.categories.size() != 0) {
							for(int i = 0; i < mGoal.categories.size(); i++) {
								if(i != mGoal.categories.size()-1)
									topics += mGoal.categories.get(i) + ",";
								else 
									topics += mGoal.categories.get(i);
							}
						}
					}
				
			//		 nvps.add(new BasicNameValuePair("topics", topics));
					 
					 //Builder Opcija
					if(mGoal.title != null) { 
						builder.addTextBody("title", mGoal.title);
					}
					
					if(mGoal.description != null) {
						builder.addTextBody("description", mGoal.description);
					}
					
					if(mGoal.people != null) {
						builder.addTextBody("people", mGoal.people);
					}
					 
					if(mGoal.location_name != null) {
						builder.addTextBody("location[name]", mGoal.location_name);
					}
					
					if(mGoal.lon != 0.0d && mGoal.lat != 0.0d) {
						 builder.addTextBody("location[geo][lon]", Double.toString(mGoal.lon));
						 builder.addTextBody("location[geo][lat]", Double.toString(mGoal.lat));
					}
					
					 if(!topics.isEmpty()) {
						 builder.addTextBody("topics", topics);
					 }
					
					 HttpEntity reqEntity = builder.build();
					 
					//httpResponse = apache.postRequest(url, new UrlEncodedFormEntity(nvps));
					httpResponse = apache.postRequest(url, reqEntity);
					JSONresponse = Util.inputStreamToString(httpResponse.getEntity().getContent());
					JSONObject job = new JSONObject(JSONresponse);
					try {
						Goal goal = MainParser.parseGoal(job.getJSONObject("data"));
						//Ako je doslo do greske
						if(goal == null) { //Izvuci specificnu poruku sa servera ako je ima
							tmpMsg = job.getJSONObject("data").optString("message");
							success = null;
						} else {
							goals.add(goal);
							success = "Uspešno postavljen problem.";
						}
						
					} catch(JSONException e) {
						success = null;
					}
					
				break;
			
			default:
				break;
		}
		
		
		List<Object> lob = new ArrayList<Object>(goals);
		sob.setData(lob);
		
		if(success != null) {
			sob.setActionSuccess(true);
		} else {
			sob.setActionSuccess(false);
			
			if(tmpMsg == null) {
				success = "Došlo je do greške prilikom akcije, molimo Vas pokušajte opet.";
			} else {//AKo smo dobili specificnu poruku od servera, npr da nije unese naslov pri dodavanju problema.
				success = tmpMsg;
			}
		}
		sob.setResponseMessage(success);
		
		return sob;
	}
	
	private HashMap<String, String> generateGoalHashMap(Goal goal) {
		HashMap<String, String> params = new HashMap<String, String>();
		
		params.put("title", goal.title);
		params.put("description", goal.description);
		params.put("location[name]", goal.location_name);
		params.put("location[geo][lon]", Double.toString(goal.lon));
		params.put("location[geo][lat]", Double.toString(goal.lat));
		if(goal.image != null)
			params.put("image", goal.image);
		if(goal.id != null)
			params.put("id", goal.id);
		if(goal.categories != null) {
			if(goal.categories.size() != 0) {
				for(String category : goal.categories) {
					params.put("topics[]", category);
				}
			}
		}
		
		return params;
	}
	
	
	private String buildInteractionUrl() {
		
		String url = null;
		
		switch(mInteraction) {
		
			case Goal.GOAL_INTERACTION_TYPE.SUPPORT_GOAL:
				url = Config.GOAL_SUPPORT_INTERACTION.replace(Config.PARAM, mGoalId);
				break;
				
			case Goal.GOAL_INTERACTION_TYPE.UNSUPPORT_GOAL:
				url = Config.GOAL_UNSUPPORT_INTERACTION.replace(Config.PARAM, mGoalId);
				break;
			
			case Goal.GOAL_INTERACTION_TYPE.FLAG_GOAL:
				url = Config.GOAL_REPORT_INTERACTION.replace(Config.PARAM, mGoalId);
				break;
				
			case Goal.GOAL_INTERACTION_TYPE.EDIT_GOAL:
				url = Config.GOAL_EDIT_INTERACTION.replace(Config.PARAM, mGoalId);
				break;
				
			case Goal.GOAL_INTERACTION_TYPE.NEW_GOAL:
				url = Config.GOAL_NEW_GOAL_INTERACTION;
				break;
			
			default:
				break;
		}
		return url;
	}

}
