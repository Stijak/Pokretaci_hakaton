package rs.pokretaci.hakaton.customviews;

import java.util.List;

import net.ascho.pokretaci.beans.Comment;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CommentAdapter extends BaseAdapter {
	Context mContext;
	List<Comment> mComments; //promjeniti u goal
	
	public CommentAdapter(Context context, List<Comment> comments) {
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
		CommentView commentView;
		if (convertView == null) {
			commentView = new CommentView(mContext, parent, mComments.get(position));
		} else {
			commentView = (CommentView) convertView;
			commentView.setView(mComments.get(position));
		}
		return commentView;
	}

}
