package net.ascho.pokretaci.backend.communication;

import java.io.IOException;

import net.ascho.pokretaci.backend.Config;
import net.ascho.pokretaci.backend.beans.ServerResponseObject;
import android.content.Context;
import android.os.AsyncTask;

public abstract class Task {
	
	//Task Types
	public static final int 
			GOOGLE_LOGIN = 0x01, 
			LOG_OUT = 0x02, 
			GET_GOAL_DETAILS = 0x03,
			GET_USER_PROFILE = 0x04, 
			GET_GOALS = 0x05,
			GET_GOAL_ACTIVITIES = 0x06,
			SUBMIT_GOAL = 0x10,
			GOAL_INTERACTION = 0x20,
			SUBMIT_COMMENT = 0x30,
			COMMENT_INTERACTION = 0x31;
			
	
	
	
	
	private TaskListener mResponseListener = null;
	private TaskExecutor mTaskExecutor = null;
	private Context mContext = null;
	
	
	public void executeTask(Context context, TaskListener responseListener) {
		mContext = context;
		mResponseListener = responseListener;
		mTaskExecutor = new TaskExecutor();
		mTaskExecutor.execute();
	}
	
	protected abstract ServerResponseObject executeWork() throws Exception;
	
	public Context getContext() {
		return mContext;
	}
	
	/**
	 * Stop current task
	 */
	public void cancelTask() {
		mTaskExecutor.cancel(true);
	}
	
	
	private class TaskExecutor extends AsyncTask<Void, Void, ServerResponseObject> {
		
	/*	@Override
		protected void onCancelled(TransportObject result) {
			// TODO Auto-generated method stub
			super.onCancelled(result);
		}
	*/
		@Override
		protected void onPostExecute(ServerResponseObject result) {
			
			if (mResponseListener != null && !isCancelled()) {
				mResponseListener.onResponse(result);
			}
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected ServerResponseObject doInBackground(Void... params) {
			
			try {
				ServerCommunicator sec = new ServerCommunicator(Config.RETRY_DELAY, Config.RETRY_ATTEMPTS) {
					
					@Override
					public Object work(Object... data) throws IOException, Exception {
						
						return executeWork();
					}
				};
				
				return (ServerResponseObject) sec.communicate();
				
			} catch (Exception e) {
			//	e.printStackTrace();
				return new ServerResponseObject(e);
			}
		}

	}
	
}
