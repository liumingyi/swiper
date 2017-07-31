package org.liumingyi.swiper.swiper;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for Swiper{@link Swiper}
 * Created by liumingyi on 2017/7/31.
 */

public class SwiperAdapter<T extends NetImage> extends PagerAdapter {

  private Context context;
  private List<ImageView> imageViews = new ArrayList<>();
  private List<T> images = new ArrayList<>();

  // FIXME: 2017/7/31 用来做是否无限切换的开关(目前不支持)
  private int startIndex = 1;

  public SwiperAdapter(Context context) {
    this.context = context;
  }

  int getStartIndex() {
    return startIndex;
  }

  public void setImageViews(List<T> images) {
    this.images.clear();
    this.images.addAll(images);
    for (int i = 0, count = images.size(); i <= count + startIndex; i++) {
      ImageView imageView = new ImageView(context);
      imageView.setLayoutParams(
          new ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
              LinearLayout.LayoutParams.MATCH_PARENT));
      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      imageViews.add(imageView);
    }

    notifyDataSetChanged();

    if (dataChangedListener != null) {
      dataChangedListener.onDataChanged();
    }
  }

  private DataChangedListener dataChangedListener;

  void setOnDataChangedListener(DataChangedListener dataChangedListener) {
    this.dataChangedListener = dataChangedListener;
  }

  interface DataChangedListener {
    void onDataChanged();
  }

  T getItem(int position) {
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
    T netImage;
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
