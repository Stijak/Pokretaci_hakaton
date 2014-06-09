package rs.pokretaci.hakaton.customviews;

import java.util.List;

import net.ascho.pokretaci.beans.Comment;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CommentAdapter extends BaseAdapter {
	Context context;
	List<Comment> comments; //promjeniti u goal
	
	public CommentAdapter(Context context, List<Comment> comments) { //TODO promjeniti u goal
		this.context = context;
		this.comments = comments;
	}

	@Override
	public int getCount() {
		return comments.size();
	}

	@Override
	public Object getItem(int position) {
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommentView commentView;
		if (convertView == null) {
			commentView = new CommentView(context, comments.get(position));
		} else {
			commentView = (CommentView) convertView;
			commentView.setView(comments.get(position));
		}
		return commentView;
	}

}
