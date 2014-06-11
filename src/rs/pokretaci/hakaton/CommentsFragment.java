package rs.pokretaci.hakaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ascho.pokretaci.backend.beans.ServerResponseObject;
import net.ascho.pokretaci.backend.communication.Task;
import net.ascho.pokretaci.backend.communication.TaskListener;
import net.ascho.pokretaci.backend.executors.goals.CommentInteraction;
import net.ascho.pokretaci.backend.executors.goals.GoalInteraction;
import net.ascho.pokretaci.beans.Comment;
import net.ascho.pokretaci.beans.Goal;
import rs.pokretaci.hakaton.customviews.CommentAdapter;
import android.app.ProgressDialog;
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
import android.widget.Toast;


public class CommentsFragment extends ListFragment implements OnClickListener, TaskListener {
	private CommentAdapter mCommentAdapter;
	private String mGoalId;
	private List<Comment> mComments = new ArrayList<Comment>();
	private EditText mCommentEdit;
	Button mLeaveCommentButton;
	private boolean mEditCommentShown = false;
	private ProgressDialog mDialog;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCommentAdapter = new CommentAdapter(this.getActivity(), mComments);
		this.setListAdapter(mCommentAdapter);
		//this.setListAdapter(new ArrayAdapter(this.getActivity(), R.layout.comment_item, EXAMPLE_DATA));
		//this.setEmptyText("No comments");
	}
	
	public void setComments(String goalId, List<Comment> list) {
		mGoalId = goalId;
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
			mEditCommentShown = true;
		} else {
			Comment comment = new Comment();
			comment.content = mCommentEdit.getText().toString();
			if (comment.content.length() < 5) {
				Toast.makeText(getActivity(), R.string.comment_too_short, Toast.LENGTH_SHORT).show();
				return;
			}
			mDialog = ProgressDialog.show(getActivity(), "", getString(R.string.submiting) , true);
			Task newGoal = new CommentInteraction(Comment.COMMENTS_INTERACTION_TYPE.NEW_COMMENT, comment, mGoalId);
			newGoal.executeTask(getActivity().getApplicationContext(), this);
		}
		
	}

	@Override
	public void onResponse(ServerResponseObject taskResponse) {
		if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
		if (taskResponse != null) {
			if (taskResponse.isResponseValid()) {
				Toast.makeText(getActivity(), R.string.submit_sucess, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), R.string.submit_failure, Toast.LENGTH_LONG).show();
			}
		}
	}

}
