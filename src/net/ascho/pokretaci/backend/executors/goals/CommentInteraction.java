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
import net.ascho.pokretaci.beans.Comment;

import org.apache.http.HttpResponse;

import android.util.Log;

public class CommentInteraction extends Task {
	
	private int mInteractionType;
	private Comment mComment;
	private String mGoalId;
	
	public CommentInteraction(int interactionType, Comment com, String goal_id) {
		mInteractionType = interactionType;
		mComment = com;
		mGoalId = goal_id;
	}
	
	public CommentInteraction(int interactionType, String comment_id, String goal_id) {
		mInteractionType = interactionType;
		mComment = new Comment();
		mComment.id = comment_id;
		mGoalId = goal_id;
	}
	
	@Override
	protected ServerResponseObject executeWork() throws Exception {
		ApacheClient apache = ApacheClient.getInstance();
		
		ServerResponseObject sob = new ServerResponseObject();
		
		String url = buildInteractionUrl();
		Log.d("odgovor", "goal interaction url: " + url);
		String JSONresponse;
		HttpResponse httpResponse;
		List<Comment> comments = new ArrayList<Comment>();
		
		switch(mInteractionType) {
		
			case Comment.COMMENTS_INTERACTION_TYPE.NEW_COMMENT:
				httpResponse = apache.postRequest(url, generateCommentHashMap(mGoalId));
				JSONresponse = Util.inputStreamToString(httpResponse.getEntity().getContent());
				
				comments.add(MainParser.parseComment(JSONresponse));
				
				break;
				
			case Comment.COMMENTS_INTERACTION_TYPE.LIKE_COMMENT:
				
				break;
			
			case Comment.COMMENTS_INTERACTION_TYPE.FLAG_COMMENT:
				
				break;
				
			default:
				break;
		}
	
		List<Object> lob = new ArrayList<Object>(comments);
		sob.setData(lob);
		sob.setActionSuccess(true);
		sob.setResponseMessage("Komentar postavljen.");
		
		return sob;
		
	}

	private HashMap<String, String> generateCommentHashMap(String goal_id) {
		HashMap<String, String> params = new HashMap<String, String>();
		
		params.put("content", mComment.content);
	//	params.put("media_link", mComment.link); 
		params.put("goal_id", goal_id);
	
		
/*   ---Parametri koje salje komentar.....nisu sad implementirani jer nece biti vremena da se vide na frontednu ali samo se dopuni kao sto je uradnjeo u GoalInteraction
		 ------WebKitFormBoundary1aBS6hoBC9whj06B
Content-Disposition: form-data; name="content"

komentar sa sajta
------WebKitFormBoundary1aBS6hoBC9whj06B
Content-Disposition: form-data; name="media_link"

Dodaj link
------WebKitFormBoundary1aBS6hoBC9whj06B
Content-Disposition: form-data; name="location"

Dodaj mesto
------WebKitFormBoundary1aBS6hoBC9whj06B
Content-Disposition: form-data; name="date"

Dodaj vreme
------WebKitFormBoundary1aBS6hoBC9whj06B
Content-Disposition: form-data; name="image"; filename=""
Content-Type: application/octet-stream


------WebKitFormBoundary1aBS6hoBC9whj06B
Content-Disposition: form-data; name="goal_id"

5378649c7b83fa7096000002
------WebKitFormBoundary1aBS6hoBC9whj06B
Content-Disposition: form-data; name="content"

komentar sa sajta
------WebKitFormBoundary1aBS6hoBC9whj06B
Content-Disposition: form-data; name="value_media"


------WebKitFormBoundary1aBS6hoBC9whj06B
Content-Disposition: form-data; name="value_meeting"


------WebKitFormBoundary1aBS6hoBC9whj06B
Content-Disposition: form-data; name="value_date"


------WebKitFormBoundary1aBS6hoBC9whj06B
Content-Disposition: form-data; name="goal_id"

5378649c7b83fa7096000002
------WebKitFormBoundary1aBS6hoBC9whj06B--
		 
		 */
		
		return params;
	}
	
	
	private String buildInteractionUrl() {
		
		String url = null;
		
		switch(mInteractionType) {
		
			case Comment.COMMENTS_INTERACTION_TYPE.NEW_COMMENT:
				url = Config.COMMENT_NEW_COMMENT_INTERACTION_URL;
				break;
				
			case Comment.COMMENTS_INTERACTION_TYPE.LIKE_COMMENT:
				url = Config.COMMENT_LIKE_INTERACTION_URL.replace(Config.PARAM, mComment.id);
				break;
			
			case Comment.COMMENTS_INTERACTION_TYPE.FLAG_COMMENT:
				url = Config.COMMENT_FLAG_INTERACTION_URL.replace(Config.PARAM, mComment.id);
				break;
				
			default:
				break;
		}
		return url;
	}

	
	
}
