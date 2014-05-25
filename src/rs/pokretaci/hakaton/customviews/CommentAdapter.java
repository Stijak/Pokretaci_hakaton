package rs.pokretaci.hakaton.customviews;

import java.util.List;

import com.squareup.picasso.Picasso;

import rs.pokretaci.hakaton.MapActivity;
import rs.pokretaci.hakaton.ProfileActivity;
import rs.pokretaci.hakaton.R;
import net.ascho.pokretaci.beans.Activist;
import net.ascho.pokretaci.beans.Comment;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	Context mContext;
	List<Comment> mComments; //promjeniti u goal
	
	public CommentAdapter(Context context, List<Comment> comments) { //TODO promjeniti u goal
		this.mContext = context;
		this.mComments = comments;
	}

	@Override
	public int getCount() {
		return mComments.size();
	}

	@Override
	public Object getItem(int position) {
		return mComments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.comment_item_advanced, parent, false);
		} else {

		}
		setView (mContext, convertView, mComments.get(position));
		return convertView;
	}
	
	private static void setView(final Context context, View commentView, final Comment comment) {
		ImageView image = (ImageView) commentView.findViewById(R.id.image);
		TextView text = (TextView) commentView.findViewById(R.id.text);
		TextView authorDate = (TextView) commentView.findViewById(R.id.author_and_date);
		image.setClickable(true);
		

		text.setText(comment.content);
		Activist activist = comment.commenter;
		authorDate.setText(activist.full_name + " | " + comment.parsed_date);
		if (activist.avatar != null) Picasso.with(context).load(activist.avatar).placeholder(R.drawable.avatar).into(image);
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Activist activist = comment.commenter;
				Intent intent = new Intent();
				intent.setClass(context, ProfileActivity.class);
				intent.putExtra(MapActivity.ID_EXTRA, activist.id);
				intent.putExtra(MapActivity.FULL_NAME_EXTRA, activist.full_name);
				intent.putExtra(MapActivity.AVATAR_EXTRA, activist.avatar);
				//intent.putExtra("EXTRA_ID", "SOME DATAS");
				context.startActivity(intent);
			}
			
		});
	
	}

}
