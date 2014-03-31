package sk.upjs.ics.android.votr;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;


public class VoteListLoader extends AbstractObjectLoader<List<Vote>>{
	private static final String SERVER_URL = "http://" + Constants.SERVER_IP + " /votr/votes";

	private static final String TAG = VoteListLoader.class.getSimpleName();

	private VoteJsonSerializer voteJsonSerializer = new VoteJsonSerializer();
	
    public VoteListLoader(Context context) {
		super(context);
	}

	/* Runs on a worker thread */
	@Override
	public List<Vote> loadInBackground() {
		try {
			Log.i(TAG, "Loading votes from URL");
			
			URL url = new URL(SERVER_URL);
			InputStream in = url.openStream();
			
			return voteJsonSerializer.parseVotes(in);
		} catch (MalformedURLException e) {
			// should never happen due to hardwired URL
			return Collections.emptyList();
		} catch (IOException e) {
			Log.e(TAG, "Cannot load votes due to I/O bug", e);
			return Collections.emptyList();
		} catch (JSONException e) {
			Log.e(TAG, "Cannot parse votes from JSON", e);
			return Collections.emptyList();
		}
	}



}
