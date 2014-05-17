package rs.pokretaci.hakaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ascho.pokretaci.beans.Comment;
import net.ascho.pokretaci.beans.Goal;
import rs.pokretaci.hakaton.customviews.CommentAdapter;
import rs.pokretaci.hakaton.customviews.GoalsAdapter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;


public class GoalsListFragment extends ListFragment {
	private GoalsAdapter mGoalsAdapter;
	private List<Goal> mGoals = new ArrayList<Goal>();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGoalsAdapter = new GoalsAdapter(this.getActivity(), mGoals);
		this.setListAdapter(mGoalsAdapter);
		//this.setListAdapter(new ArrayAdapter(this.getActivity(), R.layout.comment_item, EXAMPLE_DATA));
		//this.setEmptyText("No comments");
	}
	
	public void setGoals(List<Goal> list) {
		mGoals.clear();
		mGoals.addAll(list);
		mGoalsAdapter.notifyDataSetChanged();
	}
	
	/*@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		return inflater.inflate(R.layout.comments_fragment, container, false);
	}*/

}
