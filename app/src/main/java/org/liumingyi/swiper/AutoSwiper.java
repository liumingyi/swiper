package org.liumingyi.swiper;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

/**
 * 自动播放的轮播图<br>
 * Created by liumingyi on 2017/7/27.
 */

public class AutoSwiper extends Swiper {

  private static final int INTERVAL_TIME = 2000;
  private ZhTimer timer;

  public AutoSwiper(Context context) {
    this(context, null);
  }

  public AutoSwiper(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public AutoSwiper(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    initListener();
    startSlide();
  }

  private void initListener() {
    setOnItemTouchListener(new PageItemTouchListener() {
      @Override public void onTouchDown() {
        stopSlide();
      }

      @Override public void onTouchUp() {
        startSlide();
      }
    });
  }

  /**
   * 开始滑动
   */
  public void startSlide() {
    if (viewPager.getChildCount() <= 1) {
      return;
    }
    if (timer == null) {
      timer = new ZhTimer(new Runnable() {
        @Override public void run() {
          Log.d("AutoSwipe ------ ", "----------run---------- ");
          next();
        }
      }, INTERVAL_TIME, true);
    } else if (timer.isPaused()) {
      timer.startTimer();
    }
  }

  /**
   * 暂停滑动
   */
  public void stopSlide() {
    if (timer == null) {
      return;
    }
    timer.stopTimer();
  }

  /**
   * 销毁计时器
   */
  private void destroyTimer() {
    if (timer == null) {
      return;
    }
    timer.destroy();
  }

  //////////////////////////Life Cycle Methods////////////////////////////////////////

  @Override public void onStart() {
    super.onStart();
    startSlide();
  }

  @Override public void onStop() {
    super.onStop();
    stopSlide();
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    destroyTimer();
  }
}
