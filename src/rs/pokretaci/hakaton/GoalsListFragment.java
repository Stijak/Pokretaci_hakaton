package rs.pokretaci.hakaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ascho.pokretaci.beans.Comment;
import net.ascho.pokretaci.beans.Goal;
import rs.pokretaci.hakaton.customviews.CommentAdapter;
import rs.pokretaci.hakaton.customviews.GoalsAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;


public class GoalsListFragment extends ListFragment implements OnItemClickListener {
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
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setOnItemClickListener(this);
	}
	
	public void setGoals(List<Goal> list) {
		mGoals.clear();
		mGoals.addAll(list);
		mGoalsAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), ProblemDetailsActivity.class);
		intent.putExtra(MapActivity.ID_EXTRA, mGoals.get((int) id).id);
		//intent.putExtra("EXTRA_ID", "SOME DATAS");
		startActivity(intent);
		
	}
	
	/*@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		return inflater.inflate(R.layout.comments_fragment, container, false);
	}*/

}
