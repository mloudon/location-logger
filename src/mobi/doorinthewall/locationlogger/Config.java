package mobi.doorinthewall.locationlogger;

import java.util.Arrays;
import java.util.Vector;

public class Config {

	// would use https, but android doesn't recognize startssl CA dammit
	public static final Vector<String> updateUrls = new Vector<String>(Arrays.asList(new String[]{
			"http://mrgris.com/littlefluffylatitude/update",
			"http://doorinthewall.co.za/locationlogger/update/"}));

	public static final int locQueryInterval = 60; // seconds
	public static final int locForceUpdateInterval = 180; // seconds

	public static final String user = PrivateConfig.user;
	public static final String bearerToken = PrivateConfig.bearerToken;

}
