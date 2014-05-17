//package com.example.drawerlayouttest;
//
//import java.util.ArrayList;
//
//import rs.pokretaci.hakaton.customviews.ExpendableDrawerAdapter;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.v4.app.ActionBarDrawerToggle;
//import android.support.v4.widget.DrawerLayout;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ExpandableListView;
//import android.widget.ExpandableListView.OnChildClickListener;
//import android.widget.Toast;
//
//@SuppressLint("NewApi")
//public class DrawerLayoutTest extends Activity implements OnChildClickListener {
//
//	private DrawerLayout drawer;
//	private ExpandableListView drawerList;
//	private ActionBarDrawerToggle actionBarDrawerToggle;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_drawer_layout_test);
//
//		setGroupData();
//		setChildGroupData();
//
//		initDrawer();
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.drawer_layout_test, menu);
//		return true;
//	}
//
//	private void initDrawer() {
//		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//		drawerList = (ExpandableListView) findViewById(R.id.left_drawer);
//
//		drawerList.setAdapter(new ExpendableDrawerAdapter(this, groupItem, childItem));
//
//		drawerList.setOnChildClickListener(this);
//
//		// actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,
//		// R.drawable.ic_drawer, R.string.open_drawer,
//		// R.string.close_drawer) {
//		// public void onDrawerClosed(View view) {
//		// getActionBar().setSubtitle("open");
//		// }
//		//
//		// /** Called when a drawer has settled in a completely open state. */
//		// public void onDrawerOpened(View drawerView) {
//		// getActionBar().setSubtitle("close");
//		// }
//		//
//		// };
//		//
//		// drawer.setDrawerListener(actionBarDrawerToggle);
//
//	}
//
//	public void setGroupData() {
//		groupItem.add("TechNology");
//		groupItem.add("Mobile");
//		groupItem.add("Manufacturer");
//		groupItem.add("Extras");
//	}
//
//	ArrayList<String> groupItem = new ArrayList<String>();
//	ArrayList<Object> childItem = new ArrayList<Object>();
//
//	public void setChildGroupData() {
//		/**
//		 * Add Data For TecthNology
//		 */
//		ArrayList<String> child = new ArrayList<String>();
//		child.add("Java");
//		child.add("Drupal");
//		child.add(".Net Framework");
//		child.add("PHP");
//		childItem.add(child);
//
//		/**
//		 * Add Data For Mobile
//		 */
//		child = new ArrayList<String>();
//		child.add("Android");
//		child.add("Window Mobile");
//		child.add("iPHone");
//		child.add("Blackberry");
//		childItem.add(child);
//		/**
//		 * Add Data For Manufacture
//		 */
//		child = new ArrayList<String>();
//		child.add("HTC");
//		child.add("Apple");
//		child.add("Samsung");
//		child.add("Nokia");
//		childItem.add(child);
//		/**
//		 * Add Data For Extras
//		 */
//		child = new ArrayList<String>();
//		child.add("Contact Us");
//		child.add("About Us");
//		child.add("Location");
//		child.add("Root Cause");
//		childItem.add(child);
//	}
//
//	@Override
//	public boolean onChildClick(ExpandableListView parent, View v,
//			int groupPosition, int childPosition, long id) {
//		Toast.makeText(this, "Clicked On Child" + v.getTag(),
//				Toast.LENGTH_SHORT).show();
//		return true;
//	}
//
//}
