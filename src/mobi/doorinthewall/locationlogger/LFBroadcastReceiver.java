package mobi.doorinthewall.locationlogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import mobi.doorithewall.locationlogger.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

public class LFBroadcastReceiver extends BroadcastReceiver {
	private Context context;

	@Override
	public void onReceive(Context c, Intent intent) {
		// The location broadcast has woken the app
		Log.d("LFBroadcastReceiver", "onReceive: received location update");
		context = c;

		final LocationInfo locationInfo = (LocationInfo) intent
				.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
		Map<String, String> uploadData = new HashMap<String, String>();
		uploadData.put("lat", "" + locationInfo.lastLat);
		uploadData.put("lon", "" + locationInfo.lastLong);
		uploadData.put("acc", "" + locationInfo.lastAccuracy);
		uploadData.put("timestamp", ""
				+ locationInfo.lastLocationUpdateTimestamp);
		String json = new GsonBuilder().create().toJson(uploadData, Map.class);
		Log.d("LFBroadcastReceiver", "Upload json: " + json);

		String stringUrl = "http://doorinthewall.co.za/locationlogger/update/";

		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			Log.d("LFBroadcastReceiver", "Starting location upload");
			new UploadLocationTask().execute(stringUrl, json);
		} else {
			Log.d("LFBroadcastReceiver", "No network connection available.");
		}

	}
	

	private class UploadLocationTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			// params comes from the execute() call: params[0] is the url.
			return makeRequest(urls[0], urls[1]);

		}

		@Override
		protected void onPostExecute(String result) {
			// Construct the notification.
			Notification notification = new Notification(
					R.drawable.notification, "Location update uploaded",
					System.currentTimeMillis());

			Intent contentIntent = new Intent(context, LoggerActivity.class);
			PendingIntent contentPendingIntent = PendingIntent.getActivity(
					context, 0, contentIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			notification.setLatestEventInfo(context,
					"Attempted location upload", "Server said: " + result,
					contentPendingIntent);

			// Trigger the notification.
			Log.d("LFBroadcastReceiver", "Triggering notification");
			((NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE)).notify(
					1234, notification);
		}

	}

	private static String makeRequest(String uri, String json) {
		try {
			HttpPost httpPost = new HttpPost(uri);
			httpPost.setEntity(new StringEntity(json));
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			HttpResponse response = new DefaultHttpClient().execute(httpPost);
			Log.e("LFBroadcastReceiver", "Upload response: "
					+ response.getStatusLine().getReasonPhrase());
			if (response.getStatusLine().getStatusCode() == 201) {
				return "Success!";
			} else {

				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String body = "";
				while ((body = rd.readLine()) != null) {
					Log.e("HttpResponse", body);
				}
				return "Upload failed with code "
						+ response.getStatusLine().getStatusCode();
			}

		} catch (UnsupportedEncodingException e) {
			Log.e("LFBroadcastReceiver",
					"Upload failed with UnsupportedEncodingException");
			e.printStackTrace();
			return "Upload failed with UnsupportedEncodingException";
		} catch (ClientProtocolException e) {
			Log.e("LFBroadcastReceiver",
					"Upload failed with ClientProtocolException");
			e.printStackTrace();
			return "Upload failed with ClientProtocolException";
		} catch (IOException e) {
			Log.e("LFBroadcastReceiver", "Upload failed with IOException");
			e.printStackTrace();
			return "Upload failed with IOException";
		}
	}

}