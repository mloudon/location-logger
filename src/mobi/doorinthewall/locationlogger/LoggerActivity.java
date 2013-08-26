package mobi.doorinthewall.locationlogger;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

public class LoggerActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();

		// cancel any notification we may have received from
		// LFBroadcastReceiver
		((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
				.cancel(1234);
	}

	@Override
	public void onPause() {
		super.onPause();

	}
}