package org.liumingyi.swiper.swiper;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import org.liumingyi.swiper.R;

/**
 * 滑块视图控件<br>
 * Created by liumingyi on 2017/7/26.
 */

public class Swiper extends LinearLayout {

  /**
   * 实现滑动的ViewPager.
   */
  protected ViewPager viewPager;
  /**
   * ViewPager's adapter.
   */
  protected SwiperAdapter adapter;
  /**
   * 角标指示器
   */
  protected Indicator indicator;

  /**
   * 触摸点的 x 轴.
   */
  private float touchX;
  /**
   * 触摸点的 y 轴.
   */
  private float touchY;
  /**
   * 触摸点的允许误差，x,y轴均在此误差内则认为是同一个点
   */
  private static final int TOLERANCE = 15;

  /**
   * viewPager滑动监听
   */
  private ViewPager.OnPageChangeListener pagerChangeListener =
      new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override public void onPageSelected(int position) {
          if (indicator != null) {
            int effectCount = adapter.getEffectCount();
            int startIndex = adapter.getStartIndex();
            if (position == effectCount + startIndex) {
              position = startIndex;
            } else if (position == 0) {
              position = effectCount;
            }
            indicator.setIndex(position - 1);
          }
        }

        @Override public void onPageScrollStateChanged(int state) {
          //Log.d("AutoSwipe", "onPageScrollStateChanged >>>> : " + state);
          if (state == ViewPager.SCROLL_STATE_IDLE) {
            int index = viewPager.getCurrentItem();
            int effectCount = adapter.getEffectCount();
            int startIndex = adapter.getStartIndex();
            if (index == effectCount + startIndex) {
              viewPager.setCurrentItem(startIndex, false);
            } else if (index == 0) {
              viewPager.setCurrentItem(effectCount, false);
            }
          }
        }
      };

  /**
   * viewPager touch监听
   */
  private OnTouchListener pagerTouchListener = new OnTouchListener() {
    @Override public boolean onTouch(View view, MotionEvent event) {
      int action = event.getAction();
      switch (action) {
        case MotionEvent.ACTION_MOVE:
          //This is important, if you return TRUE the action of swipe will not take place.
          return false;
        case MotionEvent.ACTION_DOWN:
          postTouchDownEvent();
          touchX = event.getX();
          touchY = event.getY();
          break;
        case MotionEvent.ACTION_UP:
          boolean sameX = Math.abs(touchX - event.getX()) < TOLERANCE;
          boolean sameY = Math.abs(touchY - event.getY()) < TOLERANCE;
          if (sameX && sameY) {
            // the user "clicked" certain point in the screen
            // or just returned to the same position an raised the finger
            postClickedEvent();
          } else {
            postTouchUpEvent();
          }
          break;
      }
      return false;
    }
  };

  private SwiperAdapter.DataChangedListener adapterDataChangedListener =
      new SwiperAdapter.DataChangedListener() {
        @Override public void onDataChanged() {
          //如果只有一页，不显示指示器
          if (adapter.getEffectCount() == 1) {
            return;
          }
          indicator.reset(adapter.getEffectCount());
          viewPager.setCurrentItem(adapter.getStartIndex(), false);
        }
      };

  public Swiper(Context context) {
    this(context, null);
  }

  public Swiper(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public Swiper(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView(context);
  }

  private void initView(Context context) {
    View inflate = inflate(context, R.layout.swiper_layout, this);
    viewPager = inflate.findViewById(R.id.viewpager);
    viewPager.addOnPageChangeListener(pagerChangeListener);
    viewPager.setOnTouchListener(pagerTouchListener);
  }

  /**
   * 通知点击事件被触发
   */
  private void postClickedEvent() {
    if (itemClickListener == null) {
      postTouchUpEvent();
      return;
    }
    int position = viewPager.getCurrentItem();
    NetImage netImage = adapter.getItem(position);
    if (netImage != null && TextUtils.isEmpty(netImage.getLinkUrl())) {
      postTouchUpEvent();
    } else {
      // ignore this warning
      itemClickListener.onItemClicked(position, adapter.getItem(position));
    }
  }

  /**
   * 通知 touch down 触发
   */
  private void postTouchDownEvent() {
    if (itemTouchListener != null) {
      itemTouchListener.onTouchDown();
    }
  }

  /**
   * 通知 touch up 触发
   */
  private void postTouchUpEvent() {
    if (itemTouchListener != null) {
      itemTouchListener.onTouchUp();
    }
  }

  public void setAdapter(final SwiperAdapter adapter) {
    this.adapter = adapter;
    this.viewPager.setAdapter(this.adapter);
    this.adapter.setOnDataChangedListener(adapterDataChangedListener);
  }

  /**
   * 设置角标指示器
   * {@link Indicator}
   */
  public void setIndicator(Indicator indicator) {
    this.indicator = indicator;
  }

  /**
   * 跳到下一页,无限循环
   */
  public void next() {
    int index = viewPager.getCurrentItem();
    index++;
    viewPager.setCurrentItem(index);
  }

  /**
   * 跳到上一页,无限循环
   */
  public void previous() {
    int index = viewPager.getCurrentItem();
    index--;
    viewPager.setCurrentItem(index);
  }

  /////////////////////////////lifeCycle Method////////////////////////////////////////////////////////////

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    onDestroy();
  }

  private void onDestroy() {
    if (adapter != null) {
      adapter.onDestroy();
    }
  }

  public void onStart() {
    if (adapter != null) {
      adapter.onStart();
    }
  }

  public void onStop() {
    if (adapter != null) {
      adapter.onStop();
    }
  }

  //////////////////////////提供给外部的itemClick，touch接口/////////////////////////////////////

  private PageItemClickListener itemClickListener;

  public <T extends NetImage> void setOnPageItemClickListener(
      PageItemClickListener<T> itemClickListener) {
    this.itemClickListener = itemClickListener;
  }

  public interface PageItemClickListener<T extends NetImage> {
    void onItemClicked(int position, T netImage);
  }

  private PageItemTouchListener itemTouchListener;

  public void setOnItemTouchListener(PageItemTouchListener itemTouchListener) {
    this.itemTouchListener = itemTouchListener;
  }

  interface PageItemTouchListener {
    void onTouchDown();

    void onTouchUp();
  }
}
