package sk.upjs.ics.android.votr;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


public class SubmitVoteIntentService extends IntentService {
	private static final String TAG = SubmitVoteIntentService.class.getSimpleName();
	
	public static final String URL = "http://" + Constants.SERVER_IP + "/votr/votes";
	
	public static final String INTENT_EXTRA_VOTE = "VOTE";
	private static final String DEFAULT_USER_AGENT = null;

	private AndroidHttpClient httpClient;
	
	public SubmitVoteIntentService() {
		super("SubmitVote");

		// http client is thread-safe
		httpClient = AndroidHttpClient.newInstance(DEFAULT_USER_AGENT);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			boolean vote = intent.getBooleanExtra(INTENT_EXTRA_VOTE, false);
			
			HttpPost httpPost = new HttpPost(URL);
			httpPost.setEntity(new StringEntity(vote ? "yes" : "no"));
			
			httpClient.execute(httpPost);
			
			broadcastVote(vote);
		} catch (UnsupportedEncodingException e) {
			// won't happen
			Log.wtf(TAG, e);
		} catch (IOException e) {
			Log.e(TAG, "Cannot vote due to I/O error", e);
		}
	}
	
	private void broadcastVote(boolean vote) {
		LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
		Intent intent = new Intent(Vote.class.getName());
		broadcastManager.sendBroadcast(intent);
	}

	@Override
	public void onDestroy() {
		httpClient.close();
		super.onDestroy();
	}
	
}
