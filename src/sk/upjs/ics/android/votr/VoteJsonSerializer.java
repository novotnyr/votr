package sk.upjs.ics.android.votr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class VoteJsonSerializer {
	private static final String TAG = VoteLoader.class.getSimpleName();
	
	public List<Vote> parseVotes(InputStream in) throws JSONException, IOException {
		return toVotesList(new JSONArray(toString(in)));
	}
	
	public Vote parseVote(InputStream in) throws JSONException, IOException {
		return toVote(new JSONObject(toString(in)));
	}	
	
	public List<Vote> toVotesList(JSONArray votesArray) throws JSONException {
		List<Vote> votes = new ArrayList<Vote>(votesArray.length());
		for (int i = 0; i < votesArray.length(); i++) {
			JSONObject jsonVote = votesArray.getJSONObject(i);
			votes.add(toVote(jsonVote));
		}
		return votes;
	}

	public Vote toVote(JSONObject jsonVote) throws JSONException {
		Vote vote = new Vote();
		vote.setId(UUID.fromString(jsonVote.getString("id")));
		vote.setName(jsonVote.getString("name"));
		vote.setDescription(jsonVote.getString("description"));
		return vote;
	}

	protected String toString(InputStream in) throws IOException {
		BufferedReader reader = null;
		try {
			StringBuilder buffer = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			return buffer.toString();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Log.w(TAG, "Cannot close the reader", e);
				}
			}
		}
	}

}
