package rs.pokretaci.hakaton;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import net.ascho.pokretaci.beans.Goal;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsFragment extends Fragment {
	private TextView mTitle;
	private TextView mDescription;
	private ImageView mImage;
	private ImageView mLocationImage;
	private ImageView mAvatarImage;
	private TextView mFullName;
	private TextView mDate;
	
	Transformation transformation = new Transformation() {

        @Override public Bitmap transform(Bitmap source) {
            int targetWidth = mImage.getWidth();

            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) {
                // Same bitmap is returned if sizes are the same
                source.recycle();
            }
            return result;
        }

        @Override public String key() {
            return "transformation" + " desiredWidth";
        }
    };
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.details_fragment, container, false);
		mTitle = (TextView) view.findViewById(R.id.details_title);
		mDescription = (TextView) view.findViewById(R.id.details_content);
		mImage = (ImageView) view.findViewById(R.id.image);
		mLocationImage = (ImageView) view.findViewById(R.id.location_image);
		mAvatarImage = (ImageView) view.findViewById(R.id.avatar_image);
		mFullName = (TextView) view.findViewById(R.id.full_name);
		mDate = (TextView) view.findViewById(R.id.details_date);
		return view;
	}
	
	
	protected void setContent(Goal goal) {
		mTitle.setText(goal.title);
		mDescription.setText(goal.description+"                                                                                  ");
		if (goal.image != null && !goal.image.equals("")) Picasso.with(getActivity()).load(goal.image).into(mImage);
		if (goal.location_image != null && !goal.location_image.equals("")) Picasso.with(getActivity()).load(goal.location_image).transform(transformation)
        .into(mLocationImage);
		if (goal.creator.avatar != null && !goal.creator.avatar.equals("")) Picasso.with(getActivity()).load(goal.creator.avatar).placeholder(R.drawable.avatar).into(mAvatarImage);
		mFullName.setText(goal.creator.full_name);
		mDate.setText(goal.created_at);
	}

}
