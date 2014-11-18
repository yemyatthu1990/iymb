package com.yemyatthu.iymb;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import io.fabric.sdk.android.Fabric;
import java.util.HashMap;

/**
 * Created by yemyatthu on 11/18/14.
 */
public class Iymb extends Application{
  HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

  @Override
  public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());
    }

  public synchronized Tracker getTracker(TrackerName trackerId) {
    if (!mTrackers.containsKey(trackerId)) {

      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
      Tracker t = analytics.newTracker(R.xml.app_tracker);
      mTrackers.put(trackerId, t);
    }
    return mTrackers.get(trackerId);
  }

  /**
   * Enum used to identify the tracker that needs to be used for tracking.
   *
   * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
   * storing them all in Application object helps ensure that they are created only once per
   * application instance.
   */
  public enum TrackerName {
    APP_TRACKER, // Tracker used only in this app.
  }
}
