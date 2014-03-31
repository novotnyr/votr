package sk.upjs.ics.android.votr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements LoaderCallbacks<Vote> {
	private static final int VOTE_LOADER_ID = 0;
	
	private TextView voteNameTextView;
	private TextView voteDescriptionTextView;

	private VoteBroadcastReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.activity_main);

	    setProgressBarIndeterminateVisibility(true);
	 	
		voteNameTextView = (TextView) findViewById(R.id.vote_name_text_view);
		voteDescriptionTextView = (TextView) findViewById(R.id.vote_description_text_view);
		
		getSupportLoaderManager().initLoader(VOTE_LOADER_ID, Bundle.EMPTY, this);
	}
	
	@Override
	protected void onResume() {
		IntentFilter filter = new IntentFilter(Vote.class.getName());
		receiver = new VoteBroadcastReceiver();
		
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
		
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		super.onPause();
	}	

	@Override
	public Loader<Vote> onCreateLoader(int loaderId, Bundle bundle) {
		if(loaderId == VOTE_LOADER_ID) {
			return new VoteLoader(this);
		} 
		
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Vote> loader, Vote vote) {
		voteNameTextView.setText(vote.getName());
		voteDescriptionTextView.setText(vote.getDescription());

		setProgressBarIndeterminateVisibility(false);
	}

	@Override
	public void onLoaderReset(Loader<Vote> loader) {
		// do nothing
	}

	public void onYesClick(View v) {
		submitVote(true);
	}
	
	public void onNoClick(View v) {
		submitVote(false);
	}

	private void submitVote(boolean vote) {
		Intent intent = new Intent(this, SubmitVoteIntentService.class);
		intent.putExtra(SubmitVoteIntentService.INTENT_EXTRA_VOTE, vote);
		startService(intent);
	}
	
	public class VoteBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(MainActivity.this, "Vïaka za hlas!",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	

}
