package net.ascho.pokretaci.backend.executors.goals;

import org.apache.http.HttpResponse;

import android.util.Log;
import net.ascho.pokretaci.backend.Config;
import net.ascho.pokretaci.backend.beans.ServerResponseObject;
import net.ascho.pokretaci.backend.communication.ApacheClient;
import net.ascho.pokretaci.backend.communication.Task;
import net.ascho.pokretaci.backend.util.Util;
import net.ascho.pokretaci.beans.Goal;

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
		HttpResponse httpResponse = apache.getRequest(url);
		String JSONresponse = Util.inputStreamToString(httpResponse.getEntity().getContent());
		
		
		return null;
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
