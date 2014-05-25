package rs.pokretaci.hakaton.customviews;


import com.squareup.picasso.Picasso;

import net.ascho.pokretaci.beans.Activist;
import net.ascho.pokretaci.beans.Comment;
import rs.pokretaci.hakaton.MapActivity;
import rs.pokretaci.hakaton.ProfileActivity;
import rs.pokretaci.hakaton.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentView extends LinearLayout {
	private Context mContext;
	private ImageView mImage;
	private TextView mText;
	private TextView mAuthorDate;

	public CommentView(final Context context, ViewGroup parent, Comment comment) {
		super(context);
		mContext = context;
		inflate(context, R.layout.comment_item_advanced, this);
		
		mImage = (ImageView) findViewById(R.id.image);
		mText = (TextView) findViewById(R.id.text);
		mAuthorDate = (TextView) findViewById(R.id.author_and_date);
		mImage.setClickable(true);
		setView(comment);
	}
	
	public void setView(final Comment comment) {
		mText.setText(comment.content);
		Activist activist = comment.commenter;
		mAuthorDate.setText(activist.full_name + " | " + comment.parsed_date);
		if (activist.avatar != null) Picasso.with(mContext).load(activist.avatar).placeholder(R.drawable.avatar).into(mImage);
		mImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Activist activist = comment.commenter;
				Intent intent = new Intent();
				intent.setClass(mContext, ProfileActivity.class);
				intent.putExtra(MapActivity.ID_EXTRA, activist.id);
				intent.putExtra(MapActivity.FULL_NAME_EXTRA, activist.full_name);
				intent.putExtra(MapActivity.AVATAR_EXTRA, activist.avatar);
				//intent.putExtra("EXTRA_ID", "SOME DATAS");
				mContext.startActivity(intent);
			}
			
		});
	}

}
