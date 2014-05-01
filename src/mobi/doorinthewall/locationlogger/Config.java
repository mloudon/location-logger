package mobi.doorinthewall.locationlogger;

public class Config {
	
	// would use https, but android doesn't recognize startssl CA dammit
	public static final String updateUrl = "http://mrgris.com/littlefluffylatitude/update";

	public static final int locQueryInterval = 60; // seconds
	public static final int locForceUpdateInterval = 180; // seconds
	
	public static final String user = PrivateConfig.user;
	public static final String bearerToken = PrivateConfig.bearerToken;
	
}
