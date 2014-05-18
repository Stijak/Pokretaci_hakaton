package rs.pokretaci.hakaton.customviews;


import com.squareup.picasso.Picasso;

import net.ascho.pokretaci.beans.Activist;
import net.ascho.pokretaci.beans.Comment;
import rs.pokretaci.hakaton.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentView extends LinearLayout {
	private Context mContext;
	private ImageView mImage;
	private TextView mText;
	private TextView mAuthorDate;

	public CommentView(Context context, Comment comment) {
		super(context);
		mContext = context;
		((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.comment_item_advanced, this, true);
		
		mImage = (ImageView) findViewById(R.id.image);
		mText = (TextView) findViewById(R.id.text);
		mAuthorDate = (TextView) findViewById(R.id.author_and_date);
		setView(comment);
	}
	
	public void setView(Comment comment) {
		mText.setText(comment.content);
		Activist activist = comment.commenter;
		mAuthorDate.setText(activist.full_name + " | " + comment.parsed_date);
		if (activist.avatar != null) Picasso.with(mContext).load(activist.avatar).placeholder(R.drawable.avatar).into(mImage);
	}

}
