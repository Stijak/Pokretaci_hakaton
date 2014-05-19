package net.ascho.pokretaci;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.ascho.pokretaci.backend.beans.ServerResponseObject;
import net.ascho.pokretaci.backend.communication.ApacheClient;
import net.ascho.pokretaci.backend.communication.Task;
import net.ascho.pokretaci.backend.communication.TaskFactory;
import net.ascho.pokretaci.backend.communication.TaskListener;
import net.ascho.pokretaci.backend.cookies.CookieManager;
import net.ascho.pokretaci.backend.executors.goals.CommentInteraction;
import net.ascho.pokretaci.backend.executors.goals.GoalInteraction;
import net.ascho.pokretaci.backend.executors.login.GoogleLogin;
import net.ascho.pokretaci.backend.util.Util;
import net.ascho.pokretaci.beans.Comment;
import net.ascho.pokretaci.beans.Goal;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends Activity implements TaskListener {
    protected AccountManager accountManager;
    protected Intent intent;
    
    /** Called when the activity is first created. */
    @Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    ApacheClient.getInstance().setCookieStore(
	    		CookieManager.getInstance().restoreCookiesFromSharedPreferences(
	    				getApplicationContext()
	    				));
	    
	     //Ovo gore dohvati prvi mail iz niza, ali bi kao trebali prikazati korsiniku sve akkaunte pa da jedan izabere
	     String email = Util.getAccountNames(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE, getApplicationContext())[0];
	     File file = new File("//sdcard//DCIM//2013-05-13_13-52-22.png");
	    
	     
	     Task googleLogin = new GoogleLogin(email, MainActivity.this);
	     googleLogin.executeTask(getApplicationContext(), this);
	     
	     Goal mGoal = new Goal();
	     
	 //    mGoal.id = "537885897b83fa3e1c000002";
	   /*  mGoal.title = "Najnoviji test sa DESERTOM";
	     mGoal.description = "aRadi sa uploadom slike DESERTOM";
	     mGoal.people = "Mi smo pravi ljudi za vas!";
	     mGoal.lat = 19.463063;
	     mGoal.lon = 44.591535;
	     mGoal.location_name = "Radi sa uploadom slike bez slike DESERTOM";
	     mGoal.image = null;
	     mGoal.categories = new ArrayList<String>();
	     mGoal.categories.add("ruglo");
	     mGoal.categories.add("vandalizam");
	     mGoal.categories.add("nemoral");
	     Task newGoal = new GoalInteraction(Goal.GOAL_INTERACTION_TYPE.NEW_GOAL, mGoal);
	     newGoal.executeTask(getApplicationContext(), this);*/
	     
	    /* Task goalById =  TaskFactory.goalFetchTask(Goal.GOAL_FETCH_TYPE.BY_GOAL_ID, "5376ce2d7b83fa1a64000000");
	     goalById.executeTask(getApplicationContext(), this);  */                       
	     
	  /*  */
	     
	     
/*	     	Goal g =new Goal();
	     	g.id = "53714ce47b83fad91b000000";
	        Task t1 = TaskFactory.reportGoalTask(g);
			t1.executeTask(getApplicationContext(), this);*/
	  
	    
	    /* //Trending filter
	     Task trending =  TaskFactory.goalFetchTask(Goal.GOAL_FETCH_TYPE.BY_FILTER, Goal.GOAL_FILTER.TRENDING);
	     trending.executeTask(getApplicationContext(), this);
	     
	     //DOhvati problem po ID-u
	     Task goalById =  TaskFactory.goalFetchTask(Goal.GOAL_FETCH_TYPE.BY_GOAL_ID, "53714ce47b83fad91b000000");
	     goalById.executeTask(getApplicationContext(), this);
	     
	     

	     
	     //DOhvati probleme za korisnika
	     Task goalByUser =  TaskFactory.goalFetchTask(Goal.GOAL_FETCH_TYPE.BY_USER, "530d342628add9a7cf000000");
	     goalById.executeTask(getApplicationContext(), this);
	     
	     Task newestGoals =  TaskFactory.goalFetchTask(Goal.GOAL_FETCH_TYPE.BY_FILTER, Goal.GOAL_FILTER.NEWEST);
	     newestGoals.executeTask(getApplicationContext(), this);
	     
	     
	     Task toprated =  TaskFactory.goalFetchTask(Goal.GOAL_FETCH_TYPE.BY_FILTER, Goal.GOAL_FILTER.TOP_RATED);
	     toprated.executeTask(getApplicationContext(), this);
	     
	     
	     Task discussed =  TaskFactory.goalFetchTask(Goal.GOAL_FETCH_TYPE.BY_FILTER, Goal.GOAL_FILTER.MOST_DISCUSSED);
	     discussed.executeTask(getApplicationContext(), this);
	     */
	     
	     
//	     Task radius =  TaskFactory.goalFetchTask(Goal.GOAL_FETCH_TYPE.BY_RADIUS, ne_lat, ne_lon, sw_lat, sw_lon);
//	     trending.executeTask(getApplicationContext(), this);  
	     
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == GoogleLogin.GOOGLE_AUTH_REQUEST_CODE) {
			if(resultCode == Activity.RESULT_OK) {
				//oporavio se od greske pri google login-u, treba opet okinuti task za login
			} else {
				//ne moze da se uloguje, moze da nastavi da koristi samo aplikaciju
			}
		}
	}

	

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		 CookieManager.getInstance().saveCookiesToSharedPreferences(getApplicationContext(), ApacheClient.getInstance().getCookieStore());
	}



	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public void onResponse(ServerResponseObject taskResponse) {
		
		if(taskResponse != null) { //Nije doslo do neocekivanog sranja
			if(taskResponse.isResponseValid()) { //Nije doslo do exception-a
				
				
				List list = taskResponse.getData();
				List<Goal> goals = (List<Goal>) list; //npr ako je Task dohvatao Goals
				
				
				Goal g = goals.get(0);
				
				
			/*	for(Goal g: goals) {
					if(g.comments != null) {
						for(Comment c: g.comments) {
					
						Log.d(g.title, c.content);
					}
					}
					
				}*/
				
			} else {
				//Doslo je do exception, mozes da dohvatis gresku sa
				taskResponse.getExceptionMsg(); // i dalje sta dizajner kaze :P
				
				//Ali ako je bio google login task poveravaj sledece
				Exception e = taskResponse.getException();
				if(e instanceof GooglePlayServicesAvailabilityException) {
					
					GooglePlayServicesAvailabilityException gEx = (GooglePlayServicesAvailabilityException) e;
					
			        Dialog alert = GooglePlayServicesUtil.getErrorDialog(gEx.getConnectionStatusCode(), this, GoogleLogin.GOOGLE_AUTH_REQUEST_CODE);
			        if(alert != null) { //Od ovog exceptiona mozemo da se oporavimo
			        	alert.show();
			        } else { //Ovde nzn obavestimo ga da ne moze samo da gleda ciljeve a da se ne loguje
			        	//neki dialog ili toast ("Vas uredjaj ne poseduje GooglePlayServices i nazalost vas ne mozemo ulogovati kroz aplikaciju. I dalje mozete koristiti aplikaciju ali ne mozete postavljati nove probleme...");
			        }
				}  else if(e instanceof UserRecoverableAuthException) {
					UserRecoverableAuthException ue = (UserRecoverableAuthException) e;
					//Od ovog exceptiona mozemo da se oporavimo
					startActivityForResult(ue.getIntent(), GoogleLogin.GOOGLE_AUTH_REQUEST_CODE);
				} 
			}
		} else {
			//Boga pitaj sta se desilo :) ..ovde ne bi trebao nikad da udje ali cisto nek mu iskace error
		}
		
		
	}

}
