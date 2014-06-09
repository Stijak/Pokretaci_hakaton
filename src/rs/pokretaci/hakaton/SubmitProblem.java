package rs.pokretaci.hakaton;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class SubmitProblem extends Activity {
	private static final int LOCATION_REQUEST = 1;
	private static final int PICTURE_REQUEST = 2;
	protected static final String LOCATION_EXTRA = "LOCATION_EXTRA";
	private LatLng mLocation;
	
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_problem);
		ImageView iv = (ImageView) findViewById(R.id.location_image);
		Picasso.with(this).load("http://www.pokretaci.rs/images/maps/44.809852x20.452938.png").resize(500, 200).centerCrop().into(iv);
	}
	
	public void setMarker(View view) {
		Intent intent = new Intent(this, SetMarkerActivity.class);
		//intent.putExtra("EXTRA_ID", "SOME DATAS");
		if (mLocation != null) {
			intent.putExtra(LOCATION_EXTRA, mLocation);
		}
		this.startActivityForResult(intent, LOCATION_REQUEST);
	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == LOCATION_REQUEST) {
				mLocation =  (LatLng) data.getParcelableExtra(LOCATION_EXTRA);
			}
		}
	}

	private class LongOperation extends AsyncTask<Void, Void, Void> {
		private ProgressDialog dialog;
        @Override
        protected Void doInBackground(Void... params) {
        	try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        	if (dialog != null) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
        	Toast.makeText(SubmitProblem.this, R.string.submit_sucess, Toast.LENGTH_LONG).show();;
        	SubmitProblem.this.finish();
        }
        
       

        @Override
        protected void onPreExecute() {
        	dialog = ProgressDialog.show(SubmitProblem.this, "", SubmitProblem.this.getString(R.string.submiting) , true);
        	
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
	
	 public void submitProblem(View v) {
		 new LongOperation().execute();
     }
}
