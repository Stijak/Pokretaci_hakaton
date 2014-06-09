package rs.pokretaci.hakaton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class SubmitProblem extends Activity {
	private static final int LOCATION_REQUEST = 1;
	private static final int PICTURE_REQUEST = 2;
	protected static final String LOCATION_EXTRA = "LOCATION_EXTRA";
	private LatLng mLocation;
	
	private String mCurrentPhotoPath;
	private ImageView mTakePhotoImageView;



	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	    int scaleFactor = Math.min(photoW/targetW, photoW/targetW);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    mTakePhotoImageView.setImageBitmap(bitmap);
	}
}
