package org.liumingyi.swiper;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

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

  private int startIndex = 1;

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
            if (position == adapter.getEffectCount() + startIndex) {
              position = startIndex;
            } else if (position == 0) {
              position = adapter.getEffectCount();
            }
            indicator.setIndex(position - 1);
          }
        }

        @Override public void onPageScrollStateChanged(int state) {
          Log.d("AutoSwipe", "onPageScrollStateChanged >>>> : " + state);
          if (state == ViewPager.SCROLL_STATE_IDLE) {
            int index = viewPager.getCurrentItem();
            if (index == adapter.getEffectCount() + startIndex) {
              viewPager.setCurrentItem(startIndex, false);
            } else if (index == 0) {
              viewPager.setCurrentItem(adapter.getEffectCount(), false);
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
    adapter = new SwiperAdapter(context);
    viewPager.setAdapter(adapter);
    viewPager.addOnPageChangeListener(pagerChangeListener);
    viewPager.setOnTouchListener(pagerTouchListener);
  }

  /**
   * 通知点击事件被触发
   */
  private void postClickedEvent() {
    if (itemClickListener != null) {
      int position = viewPager.getCurrentItem();
      NetImage netImage = adapter.getItem(position);
      if (TextUtils.isEmpty(netImage.getLinkUrl())) {
        postTouchUpEvent();
      } else {
        itemClickListener.onItemClicked(position, netImage);
      }
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

  /**
   * 设置要展示的图片 list
   * {@link NetImage}
   */
  public void setNetImages(List<NetImage> images) {
    adapter.setImageViews(images);
    viewPager.setCurrentItem(startIndex, false);
  }

  /**
   * 设置角标指示器
   * {@link Indicator}
   */
  public void setIndicator(Indicator indicator) {
    // 如果只有一页，不现实指示器
    if (adapter.getEffectCount() == 1) {
      return;
    }
    this.indicator = indicator;
    this.indicator.reset(adapter.getEffectCount());
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

  public void setOnPageItemClickListener(PageItemClickListener itemClickListener) {
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

  /////////////////////////////ViewPager's adapter////////////////////////////////////////////

  public class SwiperAdapter extends PagerAdapter {

    private Context context;
    private List<ImageView> imageViews = new ArrayList<>();
    private List<NetImage> images = new ArrayList<>();

    public SwiperAdapter(Context context) {
      this.context = context;
    }

    void setImageViews(List<NetImage> images) {
      this.images.clear();
      this.images.addAll(images);
      for (int i = 0, count = images.size(); i <= count + startIndex; i++) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(
            new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageViews.add(imageView);
      }

      notifyDataSetChanged();
    }

    NetImage getItem(int position) {
      position -= startIndex;
      if (position < 0 || position >= images.size()) {
        return null;
      }
      return images.get(position);
    }

    @Override public int getCount() {
      if (images.size() > 1) {
        // 为了实现，向前向后的无限滑动，增加两个假页面
        return images.size() + 1 + startIndex;
      } else {
        return images.size();
      }
    }

    int getEffectCount() {
      return images.size();
    }

    @Override public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override public ImageView instantiateItem(ViewGroup container, final int position) {
      ImageView imageView = imageViews.get(position);
      NetImage netImage;
      if (position == 0) {
        netImage = images.get(images.size() - startIndex);
      } else if (position == getEffectCount() + startIndex) {
        netImage = images.get(0);
      } else {
        netImage = images.get(position - startIndex);
      }
      container.addView(imageView);
      Glide.with(context).load(netImage.getPicUrl()).into(imageView);
      return imageView;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }

    void onStart() {
      Glide.with(context).onStart();
    }

    void onStop() {
      Glide.with(context).onStop();
    }

    void onDestroy() {
      for (ImageView imageView : imageViews) {
        Glide.clear(imageView);
      }
      //Glide.with(context).onDestroy();
    }
  }
}
