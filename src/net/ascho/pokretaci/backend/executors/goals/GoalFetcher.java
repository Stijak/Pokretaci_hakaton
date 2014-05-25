package net.ascho.pokretaci.backend.executors.goals;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.acho.backend.JSON.parsers.MainParser;
import net.ascho.pokretaci.backend.Config;
import net.ascho.pokretaci.backend.beans.ServerResponseObject;
import net.ascho.pokretaci.backend.communication.ApacheClient;
import net.ascho.pokretaci.backend.communication.Task;
import net.ascho.pokretaci.backend.util.Util;
import net.ascho.pokretaci.beans.Goal;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.util.Log;

public class GoalFetcher extends Task {
	
	private int mFetchType = -1, mFilter = -1, mCategory = -1;
	
	private String mId;
	private float ne_lat, ne_lon, sw_lat, sw_lon;
	
	public GoalFetcher(int fetchType, String id) {
		mFetchType = fetchType;
		mId = id;
	}
	
	public GoalFetcher(int fetchType, float ne_lat, float ne_lon, float sw_lat, float sw_lon) {
		mFetchType = fetchType;
		this.ne_lat = ne_lat;
		this.ne_lon = ne_lon;
		this.sw_lat = sw_lat;
		this.sw_lon = sw_lon;
	}
	
	public GoalFetcher(int fetchType, int filter) {
		mFetchType = fetchType;
		mFilter = filter;
	}
	
	public GoalFetcher(int fetchType, int filter, int category) {
		mFetchType = fetchType;
		mFilter = filter;
		mCategory = category;
	}
	
	@Override
	protected ServerResponseObject executeWork() throws ClientProtocolException, IOException, JSONException {
		ApacheClient apache = ApacheClient.getInstance();
		
		ServerResponseObject sob = new ServerResponseObject();
		
			String url = buildFetchUlr();
			Log.d("odgovor", "fetch url: " + url);
			HttpResponse httpResponse = apache.getRequest(url);
			String JSONresponse = Util.inputStreamToString(httpResponse.getEntity().getContent());
			
			List<Goal> goals;
			
			switch(mFetchType) {
				case Goal.GOAL_FETCH_TYPE.BY_GOAL_ID: 
					goals = MainParser.parseParticularGoal(JSONresponse);
					break;
				default: 
					goals = MainParser.parseGoals(JSONresponse);
					break;
			}
			
			
			
			for(int i = 0; i < goals.size(); i++) {
				httpResponse = apache.getRequest(Config.GOAL_ACTIVITIES_URL.replace(Config.PARAM, goals.get(i).id));
				JSONresponse = Util.inputStreamToString(httpResponse.getEntity().getContent());
				goals.get(i).comments = MainParser.parseCommentsForGoal(JSONresponse);
			}
			
			List<Object> lob = new ArrayList<Object>(goals);
			sob.setData(lob);
			sob.setActionSuccess(true);
			
			return sob;
			
	}
	
	
	private String buildFetchUlr() {
		String url = null;
		
		switch(mFetchType) {
			case Goal.GOAL_FETCH_TYPE.ALL_GOALS: 
				url = Config.GOAL_DATA_BY_ALL;
				return url;
			case Goal.GOAL_FETCH_TYPE.BY_GOAL_ID: 
				url = Config.GOAL_DATA_BY_ID_URL.replace(Config.PARAM, mId);
				return url;
			case Goal.GOAL_FETCH_TYPE.BY_USER:
				url = Config.GOAL_DATA_FOR_USER_URL.replace(Config.PARAM, mId);
				return url;
			case Goal.GOAL_FETCH_TYPE.BY_FILTER:
				url = Config.GOAL_DATA_BY_FILTER;
				break;
			case Goal.GOAL_FETCH_TYPE.BY_RADIUS:
				url = Config.GOAL_DATA_BY_RADIUS
					.replaceFirst(Config.PARAM, Float.toString(ne_lat))
					.replaceFirst(Config.PARAM, Float.toString(ne_lon))
					.replaceFirst(Config.PARAM, Float.toString(sw_lat))
					.replaceFirst(Config.PARAM, Float.toString(sw_lon));
				break;
			default:
				url += Config.GOAL_FILTERS_URL.NEWEST; 
				return url;
		}
		
		switch(mFilter) {
			case Goal.GOAL_FILTER.MOST_DISCUSSED:
				url += Config.GOAL_FILTERS_URL.MOST_DISCUSSED;
				return url;
			case Goal.GOAL_FILTER.NEWEST:
				url += Config.GOAL_FILTERS_URL.NEWEST;
				return url;
			case Goal.GOAL_FILTER.TOP_RATED:
				url += Config.GOAL_FILTERS_URL.TOP_RATED;
				return url;
			case Goal.GOAL_FILTER.TRENDING:
				url += Config.GOAL_FILTERS_URL.TRENDING;
				return url;
			case Goal.GOAL_FILTER.CATEGORY:
				url = Config.GOAL_DATA_BY_FILTER_CATEGORY;
			default:
				url += Config.GOAL_FILTERS_URL.NEWEST; 
				return url;
		}
		
	/*	switch(mCategory) {
			
		}*/
	}
}
