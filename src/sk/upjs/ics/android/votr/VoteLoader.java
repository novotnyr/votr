package sk.upjs.ics.android.votr;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;

public class VoteLoader extends AbstractObjectLoader<Vote> {
	private static final String SERVER_URL = "http://" + Constants.SERVER_IP + "/votr/votes/1?json";
	
	private static final String TAG = VoteLoader.class.getSimpleName();
	
	private VoteJsonSerializer voteJsonSerializer = new VoteJsonSerializer();
	
	public VoteLoader(Context context) {
		super(context);
	}

	@Override
	public Vote loadInBackground() {
		try {
			Log.i(TAG, "Loading votes from URL");
			
			URL url = new URL(SERVER_URL);
			InputStream in = url.openStream();
			
			return voteJsonSerializer.parseVote(in);
		} catch (MalformedURLException e) {
			// should never happen due to hardwired URL
			return null;
		} catch (IOException e) {
			Log.e(TAG, "Cannot load votes due to I/O error:" + e.getMessage(), e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, "Cannot parse votes from JSON", e);
			return null;
		}
	}

}
