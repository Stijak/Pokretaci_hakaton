package net.ascho.pokretaci.backend.executors.goals;

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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;

public class GoalInteraction extends Task {
	
	private int mInteraction;
	private Goal mGoal;
	
	
	public GoalInteraction(int interactionType, Goal goal) {
		mInteraction = interactionType;
		mGoal = goal;
	}
	
	@Override
	protected ServerResponseObject executeWork() throws Exception {
		
		
		ApacheClient apache = ApacheClient.getInstance();
		
		ServerResponseObject sob = new ServerResponseObject();
		
		String url = buildInteractionUrl();
		Log.d("odgovor", "goal interaction url: " + url);
		String JSONresponse;
		HttpResponse httpResponse;
		
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
			case Goal.GOAL_INTERACTION_TYPE.EDIT_GOAL:
				/*
				 * Radi pri insertu goal-a ali samo sa jednim topicom? Sjebe se pri update.
				 * 
				 * httpResponse = apache.postRequest(url, generateGoalHashMap(mGoal));
				JSONresponse = Util.inputStreamToString(httpResponse.getEntity().getContent());
				JSONObject job = new JSONObject(JSONresponse);
				Goal goal = MainParser.parseGoal(job.getJSONObject("data"));*/
				 List <NameValuePair> nvps = new ArrayList <NameValuePair>();
				 
				 nvps.add(new BasicNameValuePair("title", mGoal.title));
				 nvps.add(new BasicNameValuePair("description", mGoal.description));
				 nvps.add(new BasicNameValuePair("location[name]", mGoal.location_name));
				 nvps.add(new BasicNameValuePair("location[geo][lon]", Double.toString(mGoal.lon)));
				 nvps.add(new BasicNameValuePair("location[geo][lat]", Double.toString(mGoal.lat)));
				 if(mGoal.image != null)
					 nvps.add(new BasicNameValuePair("image", mGoal.image));
				 if(mGoal.id != null)
					 nvps.add(new BasicNameValuePair("id", mGoal.id));
				
				 
					if(mGoal.categories != null) {
						if(mGoal.categories.size() != 0) {
							for(String category : mGoal.categories) {
								nvps.add(new BasicNameValuePair("topics[]", category));
							}
						}
					}
				
					httpResponse = apache.postRequest(url, generateGoalHashMap(mGoal));
					JSONresponse = Util.inputStreamToString(httpResponse.getEntity().getContent());
					JSONObject job = new JSONObject(JSONresponse);
					Goal goal = MainParser.parseGoal(job.getJSONObject("data"));	
					
					
				break;
			
			default:
				break;
		}
		
		
	//	sob.setData(success);
		
		
		return null;
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
				url = Config.GOAL_SUPPORT_INTERACTION.replace(Config.PARAM, mGoal.id);
				break;
				
			case Goal.GOAL_INTERACTION_TYPE.UNSUPPORT_GOAL:
				url = Config.GOAL_UNSUPPORT_INTERACTION.replace(Config.PARAM, mGoal.id);
				break;
			
			case Goal.GOAL_INTERACTION_TYPE.FLAG_GOAL:
				url = Config.GOAL_REPORT_INTERACTION.replace(Config.PARAM, mGoal.id);
				break;
				
			case Goal.GOAL_INTERACTION_TYPE.EDIT_GOAL:
				url = Config.GOAL_EDIT_INTERACTION.replace(Config.PARAM, mGoal.id);
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
