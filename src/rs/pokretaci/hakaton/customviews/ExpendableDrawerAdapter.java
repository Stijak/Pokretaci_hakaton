package rs.pokretaci.hakaton.customviews;

import java.util.ArrayList;

import rs.pokretaci.hakaton.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unchecked")
public class ExpendableDrawerAdapter extends BaseExpandableListAdapter {

	public ArrayList<String> groupItem, tempChild;
	public ArrayList<Object> Childtem = new ArrayList<Object>();
	public LayoutInflater minflater;
	public Activity activity;
	private final Context context;

	public ExpendableDrawerAdapter(Context context,ArrayList<String> grList, ArrayList<Object> childItem) {
		this.context = context;
		groupItem = grList;
		this.Childtem = childItem;
	}

	public void setInflater(LayoutInflater mInflater, Activity act) {
		this.minflater = mInflater;
		activity = act;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<String> childArray = (ArrayList<String>) Childtem.get(groupPosition);
		return childArray.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) { 
		return 0;
	}

	
	
	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		tempChild = (ArrayList<String>) Childtem.get(groupPosition);
		if (convertView == null) {
			convertView = (TextView) ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.drawer_element, parent, false);
		}
		TextView text = (TextView) convertView;
		text.setText("   "+tempChild.get(childPosition));
//		convertView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(activity, tempChild.get(childPosition),
//						Toast.LENGTH_SHORT).show();
//			}
//		});
		text.setTag(tempChild.get(childPosition));
		text.setBackgroundColor(0x00999999);
		text.invalidate();
		return text;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = (TextView) ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.drawer_element, parent, false);
		}
		((TextView) convertView).setText(groupItem.get(groupPosition)); 
		convertView.setTag(groupItem.get(groupPosition));
		return convertView;
	}

	

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<String>) Childtem.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return groupItem.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}



	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
