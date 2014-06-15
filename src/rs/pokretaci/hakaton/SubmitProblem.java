package rs.pokretaci.hakaton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.ascho.pokretaci.backend.beans.ServerResponseObject;
import net.ascho.pokretaci.backend.communication.ApacheClient;
import net.ascho.pokretaci.backend.communication.Task;
import net.ascho.pokretaci.backend.communication.TaskListener;
import net.ascho.pokretaci.backend.cookies.CookieManager;
import net.ascho.pokretaci.backend.executors.goals.GoalInteraction;
import net.ascho.pokretaci.backend.executors.login.GoogleLogin;
import net.ascho.pokretaci.beans.Goal;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class SubmitProblem extends Activity implements TaskListener {
	private static final int LOCATION_REQUEST = 1;
	private static final int PICTURE_REQUEST = 2;
	protected static final String LOCATION_EXTRA = "LOCATION_EXTRA";
	private LatLng mLocation;

	private String mCurrentPhotoPath;
	private ImageView mTakePhotoImageView;
	private ProgressDialog mDialog;



	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ApacheClient.getInstance().setCookieStore(
				CookieManager.getInstance().restoreCookiesFromSharedPreferences(
						getApplicationContext()));
		setContentView(R.layout.submit_problem);
		ImageView iv = (ImageView) findViewById(R.id.location_image);
		Picasso.with(this).load("http://www.pokretaci.rs/images/maps/44.809852x20.452938.png").resize(500, 200).centerCrop().into(iv);
		mTakePhotoImageView = (ImageView) findViewById(R.id.take_photo);
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
			if (requestCode == PICTURE_REQUEST) {
				setPic();
			}
		}
	}



	public void submitProblem(View v) {
		//new LongOperation().execute();

		Goal goal = new Goal();
		EditText edit;
		CheckBox box;
		
		edit = (EditText) findViewById(R.id.title);
		goal.title = edit.getText().toString();
		
		edit = (EditText) findViewById(R.id.description);
		goal.description = edit.getText().toString();
		if (goal.description.equals("") || goal.title.equals("")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.you_must_have_title_and_description);
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
			return;
		}
		
		edit = (EditText) findViewById(R.id.people);
		goal.people = edit.getText().toString();
		
		if (mLocation == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.you_must_submit_location);//.setMessage(R.string.choose_account_details);
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					setMarker(null);
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
			return;
		}
		goal.lat = mLocation.longitude; //longitude and latitude are switched in Goal
		goal.lon = mLocation.latitude;
		
		edit = (EditText) findViewById(R.id.location_name);
		goal.location_name = edit.getText().toString();
		
		goal.image = mCurrentPhotoPath;
		goal.categories = new ArrayList<String>();
		
		box = (CheckBox) findViewById(R.id.ruglo);
		if (box.isChecked()) goal.categories.add("ruglo");
		
		box = (CheckBox) findViewById(R.id.vandalizam);
		if (box.isChecked()) goal.categories.add("vandalizam");
		
		box = (CheckBox) findViewById(R.id.nemoral);
		if (box.isChecked()) goal.categories.add("nemoral");
		
		box = (CheckBox) findViewById(R.id.cables);
		if (box.isChecked()) goal.categories.add("žice&cevi");
		
		box = (CheckBox) findViewById(R.id.trafic);
		if (box.isChecked()) goal.categories.add("saobraćaj");
		
		box = (CheckBox) findViewById(R.id.construction);
		if (box.isChecked()) goal.categories.add("gradnja");
		
		box = (CheckBox) findViewById(R.id.improvement);
		if (box.isChecked()) goal.categories.add("unapređenje");
		
		box = (CheckBox) findViewById(R.id.flora_and_fauna);
		if (box.isChecked()) goal.categories.add("flora&fauna");
		
		box = (CheckBox) findViewById(R.id.sport);
		if (box.isChecked()) goal.categories.add("sport&rekreacija");
		
		box = (CheckBox) findViewById(R.id.criminal);
		if (box.isChecked()) goal.categories.add("kriminal");
		
		box = (CheckBox) findViewById(R.id.dangerous);
		if (box.isChecked()) goal.categories.add("opasno");
		
		
		Task newGoal = new GoalInteraction(Goal.GOAL_INTERACTION_TYPE.NEW_GOAL, goal);
		newGoal.executeTask(getApplicationContext(), this);
		mDialog = ProgressDialog.show(SubmitProblem.this, "", SubmitProblem.this.getString(R.string.submiting) , true);
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "POKRETACI_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
				);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath =  image.getAbsolutePath();
		return image;
	}

	public void takePicture(View v) {
		dispatchTakePictureIntent();
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
				//TODO
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, PICTURE_REQUEST);
			}
		}
	}

	private void setPic() {
		// Get the dimensions of the View
		int targetW = mTakePhotoImageView.getWidth();

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = photoW/targetW;

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		mTakePhotoImageView.setImageBitmap(bitmap);
	}

	@Override
	public void onResponse(ServerResponseObject taskResponse) {
		if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
		if (taskResponse != null) {
			if (taskResponse.isResponseValid() && taskResponse.isActionSuccessful()) {
				Toast.makeText(SubmitProblem.this, R.string.submit_sucess, Toast.LENGTH_SHORT).show();
				this.finish();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.submit_failure);
				builder.setCancelable(false);
				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	}
}
