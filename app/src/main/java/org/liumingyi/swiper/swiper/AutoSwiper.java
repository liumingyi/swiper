package org.liumingyi.swiper.swiper;

import android.content.Context;
import android.util.AttributeSet;
import com.orhanobut.logger.Logger;
import org.liumingyi.swiper.ZhTimer;

/**
 * 自动播放的轮播图<br>
 *
 * <p>
 * - 在Swiper{@link Swiper}基础上，增加了一个Timer作为计时器来实现定时翻页；
 * - 当只有一张图片时，不会启动Timer；
 * - 在手指触摸页面时，暂停自动翻页，手指离开，继续翻页。
 * </p>
 *
 * Created by liumingyi on 2017/7/27.
 */

public class AutoSwiper extends Swiper {

  private static final int INTERVAL_TIME = 2000;

  private ZhTimer timer;

  private PageItemTouchListener pagerItemTouchListener = new PageItemTouchListener() {
    @Override public void onTouchDown() {
      stopSlide();
    }

    @Override public void onTouchUp() {
      startSlide();
    }
  };

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
  }

  private void initListener() {
    setOnItemTouchListener(pagerItemTouchListener);
  }

  /**
   * 开始滑动
   */
  public void startSlide() {
    // 如果没有或者只有一页，则不设置计时器进行自动滑动。
    if (adapter.getEffectCount() <= 1) {
      return;
    }
    if (timer == null) {
      timer = new ZhTimer(new Runnable() {
        @Override public void run() {
          Logger.d("------Timer run------");
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
