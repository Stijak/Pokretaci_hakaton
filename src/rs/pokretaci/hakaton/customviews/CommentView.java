package rs.pokretaci.hakaton.customviews;


import rs.pokretaci.hakaton.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentView extends LinearLayout {
	private ImageView image;
	private TextView text;

	public CommentView(Context context, String comment) { //TODO dodati comment goal umjesto comment stringa
		super(context);
		
		((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.comment_item_advanced, this, true);
		
		image = (ImageView) findViewById(R.id.image);
		text = (TextView) findViewById(R.id.text);
		setView(comment);
	}
	
	public void setView(String comment) { //TODO dodati comment goal umjesto comment stringa
		text.setText(comment);
	}

}
