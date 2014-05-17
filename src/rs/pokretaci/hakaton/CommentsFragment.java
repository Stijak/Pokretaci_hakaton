package rs.pokretaci.hakaton;

import java.util.Arrays;

import rs.pokretaci.hakaton.customviews.CommentAdapter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;


public class CommentsFragment extends ListFragment {
	private CommentAdapter mCommentAdapter;
	
	private final String[] EXAMPLE_DATA = new String[] {
            "Phasellus vel porta lacus. Cras fermentum lectus nec orci pulvinar ornare. Morbi scelerisque, diam in pharetra gravida, ligula ante sodales urna, mollis iaculis nisi nisi id arcu. Aliquam molestie nisl vel fermentum fringilla. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut aliquam luctus lectus. Vivamus tempor convallis dui, quis.",
            "Duis eu diam aliquam, faucibus velit ut, sollicitudin enim. Suspendisse consequat porta leo, in congue sapien vehicula at. Etiam semper tristique ligula nec tempus. Sed non nisl sit amet lacus vestibulum sagittis. Suspendisse viverra placerat pellentesque. Donec fringilla, magna sed rutrum fermentum, eros ipsum varius mauris, a mollis enim odio.",
            "Nam mollis arcu neque, nec convallis erat tincidunt et. Ut vitae dapibus ante, vel dapibus eros. Maecenas ut risus ut ante fringilla facilisis dapibus sed augue. Suspendisse potenti. Ut dictum dolor urna, auctor aliquet lectus hendrerit sit amet. Vestibulum egestas laoreet ligula ut pretium. Sed at arcu quis justo laoreet.",
            "Pellentesque faucibus porttitor magna a aliquet. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aliquam sed pharetra enim. Vestibulum vitae tortor ac turpis ultricies aliquam. Fusce varius ipsum purus, eu pellentesque libero ultricies vel. Phasellus eget pretium quam. Quisque feugiat risus dolor, at eleifend neque.",
            "Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nunc eu rutrum erat, lacinia mollis ligula. Fusce sollicitudin nisi mi, ut mollis ligula varius dapibus. Nulla fringilla mollis purus at tempor. Aliquam erat volutpat. Aenean porta metus quis bibendum accumsan. Curabitur et aliquet risus. Pellentesque habitant morbi.",
            "Suspendisse eget tincidunt tortor. Etiam sed convallis mauris. Sed quis bibendum mi. Mauris at elit sit amet orci tristique ultrices at vel sem. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vivamus magna quam, volutpat id tempor quis, iaculis ac odio. Proin mattis tristique libero nec.",
            "Vivamus sit amet massa volutpat nunc tincidunt ultrices. Ut dapibus turpis non ante aliquet, et volutpat enim varius. Mauris nisi nibh, malesuada eu elit in, blandit volutpat purus. Vivamus volutpat risus mi, rhoncus interdum purus sodales at. Nulla volutpat eros quis nisl cursus porta. Mauris mattis at tortor at consequat.",
            "Sed a mi neque. Donec a nulla lectus. Integer ullamcorper, dolor sit amet laoreet vestibulum, mauris mi ultricies orci, et porttitor magna neque sed elit. Etiam ut mi venenatis, feugiat dolor at, gravida justo. Phasellus consectetur viverra mi quis facilisis. Integer sit amet urna consequat, viverra nisi vel, congue tortor.",
            "Quisque non metus nec risus rhoncus luctus. Duis hendrerit sed odio vitae tempor. Ut accumsan eu diam at bibendum. Vestibulum vel ultricies augue. Sed euismod ligula vitae eros facilisis mollis. Donec aliquam at neque adipiscing pellentesque. Donec vestibulum velit vel elementum pretium. \r\nUt congue, libero et fermentum ullamcorper, mauris enim pellentesque dolor, ut blandit orci tellus vel nisi. Nam sit amet arcu erat. Mauris quis massa accumsan urna condimentum blandit. Pellentesque eu gravida eros, id iaculis lorem. Praesent odio diam, scelerisque at feugiat vitae, rhoncus vitae risus. Aliquam consectetur lectus eu condimentum cursus. Suspendisse vehicula posuere quam, non condimentum lectus.",
            "Morbi in libero at arcu hendrerit mattis. Nulla volutpat mi enim, sed mattis turpis venenatis vitae. Aenean facilisis laoreet diam. Aliquam pharetra libero gravida lacus placerat blandit. Nullam ac mattis est. Sed et pharetra magna. Quisque imperdiet, risus sit amet faucibus interdum, lacus metus eleifend metus, eget iaculis ligula purus vitae lectus. Sed vulputate sed elit sed tincidunt. Cras congue nibh et faucibus vehicula. In hac habitasse platea dictumst. Suspendisse rutrum rhoncus enim varius mollis. Nunc vitae turpis aliquet, convallis enim vehicula, cursus lectus. Cras ut nunc eget velit convallis aliquam. Cras vitae aliquet nunc. Phasellus viverra nunc a arcu.",
            "Curabitur dapibus nisi id enim ultricies convallis. Sed luctus sed mi id tempor. Aliquam in turpis quis odio iaculis placerat. Nunc mauris est, adipiscing at tincidunt nec, rhoncus a ipsum. Aenean sit amet tempus augue. Morbi facilisis nunc sed tortor tincidunt, ut gravida augue dignissim. Curabitur facilisis convallis porttitor. Aliquam erat volutpat. Donec fringilla, metus a tincidunt faucibus, justo nibh faucibus quam, ac pretium lacus massa vel velit. Nulla fringilla, sapien in placerat vestibulum, sem massa rhoncus metus, quis ultricies ligula metus id metus. Mauris sed nisl sem. Morbi faucibus adipiscing molestie. Sed mollis erat ut dignissim sodales. Morbi ultricies.",
            "Phasellus at blandit turpis. Integer porttitor interdum sapien vel hendrerit. Pellentesque vehicula magna at dui dapibus sagittis. Donec feugiat rhoncus accumsan. Vestibulum laoreet urna in nunc molestie rutrum. Donec fermentum augue facilisis, fringilla justo vel, pellentesque risus. Ut vehicula placerat metus, eu cursus erat interdum semper. Vivamus vitae vestibulum augue, a pellentesque tellus. Sed et felis nulla. Phasellus interdum mauris sit amet ullamcorper pharetra. Aliquam vitae magna tincidunt, ullamcorper tortor non, congue ligula. Vestibulum dignissim velit risus, adipiscing malesuada tellus auctor a. Proin eu ante non nulla molestie iaculis consequat eget tortor. Donec eu quam pellentesque, rhoncus risus eget, congue."
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCommentAdapter = new CommentAdapter(this.getActivity(), Arrays.asList(EXAMPLE_DATA));
		this.setListAdapter(mCommentAdapter);
		//this.setListAdapter(new ArrayAdapter(this.getActivity(), R.layout.comment_item, EXAMPLE_DATA));
		//this.setEmptyText("No comments");
	}
	
	/*@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		return inflater.inflate(R.layout.comments_fragment, container, false);
	}*/

}
