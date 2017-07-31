package org.liumingyi.swiper;

import android.os.Handler;

/**
 * 计时器<br>
 * 时间单位：毫秒<br>
 * Created by liumingyi on 11/01/2017.
 */

public class ZhTimer {

  /**
   * 用来实现循环的handler
   */
  private Handler handler;

  /**
   * 是否暂停
   */
  private volatile boolean paused;

  public boolean isPaused() {
    return paused;
  }

  private int interval;

  private Runnable task = new Runnable() {
    @Override public void run() {
      if (!paused) {
        ZhTimer.this.runnable.run();
        ZhTimer.this.handler.postDelayed(this, interval);
      }
    }
  };

  /**
   * 用于实现定时的runnable
   */
  private Runnable runnable;

  /**
   * 获取当前的间隔时间
   *
   * @return 间隔时间
   */
  public int getInterval() {
    return interval;
  }

  /**
   * 设置间隔时间
   *
   * @param interval 间隔时间，单位：毫秒
   */
  public void setInterval(int interval) {
    this.interval = interval;
  }

  /**
   * 启动计时器（不是马上启动,会有一次间隔时间）
   *
   * 如需马上启动 {@link #startTimerNow()}
   */
  public void startTimer() {
    paused = false;
    handler.removeCallbacks(task);
    handler.postDelayed(task, interval);
  }

  /**
   * 停止计时器
   */
  public void stopTimer() {
    paused = true;
  }

  /**
   * 销毁计时器
   */
  public void destroy() {
    paused = true;
    if (handler != null) {
      handler.removeCallbacks(task);
      handler = null;
    }
    runnable = null;
  }

  /**
   * 构造方法
   *
   * @param runnable 定时的任务
   * @param interval 间隔时间
   * @param started 是否启动
   */
  public ZhTimer(Runnable runnable, int interval, boolean started) {
    handler = new Handler();
    this.runnable = runnable;
    this.interval = interval;
    if (started) startTimer();
  }

  /**
   * 构造方法
   *
   * @param runnable 定时任务
   * @param interval 间隔时间
   */
  public ZhTimer(Runnable runnable, int interval) {
    handler = new Handler();
    this.runnable = runnable;
    this.interval = interval;
  }

  /**
   * 立刻启动
   */
  public void startTimerNow() {
    paused = false;
    handler.post(task);
  }
}
