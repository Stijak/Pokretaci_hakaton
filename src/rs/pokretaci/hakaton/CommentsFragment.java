package rs.pokretaci.hakaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ascho.pokretaci.beans.Comment;
import rs.pokretaci.hakaton.customviews.CommentAdapter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;


public class CommentsFragment extends ListFragment implements OnClickListener {
	private CommentAdapter mCommentAdapter;
	private List<Comment> mComments = new ArrayList<Comment>();
	private EditText mCommentEdit;
	Button mLeaveCommentButton;
	private boolean mEditCommentShown = false;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCommentAdapter = new CommentAdapter(this.getActivity(), mComments);
		this.setListAdapter(mCommentAdapter);
		//this.setListAdapter(new ArrayAdapter(this.getActivity(), R.layout.comment_item, EXAMPLE_DATA));
		//this.setEmptyText("No comments");
	}
	
	public void setComments(List<Comment> list) {
		mComments.clear();
		if (list != null) mComments.addAll(list);
		mCommentAdapter.notifyDataSetChanged();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.comments_fragment, container, false);
		mLeaveCommentButton = (Button) v.findViewById(R.id.leave_comment_button);
		mLeaveCommentButton.setOnClickListener(this);
		mCommentEdit = (EditText) v.findViewById(R.id.comment_edit);
		return v;
	}

	@Override
	public void onClick(View v) {
		if (!mEditCommentShown) {
			mCommentEdit.setVisibility(View.VISIBLE);
			mLeaveCommentButton.setText(getString(R.string.send_comment));
		} else {
			//sendCommentToServer
		}
		
	}

}
