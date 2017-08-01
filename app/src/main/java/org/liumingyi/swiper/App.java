package org.liumingyi.swiper;

import android.app.Application;
import com.orhanobut.logger.Logger;

public class App extends Application {

  private static final String TAG = "Swiper";

  @Override public void onCreate() {
    super.onCreate();
    Logger.init(TAG);
  }
}
