package org.liumingyi.swiper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  //private Swiper swiper;
  private AutoSwiper swiper;
  private Indicator indicator;
  //private TextView gotoTv;
  private List<NetImage> images = new ArrayList<>();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    //swiper = (Swiper) findViewById(R.id.swiper);
    swiper = (AutoSwiper) findViewById(R.id.autoSwiper);
    indicator = (Indicator) findViewById(R.id.indicator);
    //gotoTv = (TextView) findViewById(R.id.gotoTv);
    //gotoTv.setText(String.format(getString(R.string.gotoIndex), 0));
    initNetImages();
    swiper.setOnPageItemClickListener(new Swiper.PageItemClickListener() {
      @Override public void onItemClicked(int position, NetImage netImage) {
        jumpToWebActivity(netImage);
      }
    });
    //swiper.setOnPageItemClickListener(new Swiper.PageItemClickListener() {
    //  @Override public void onItemClicked(int position, NetImage netImage) {
    //
    //  }
    //});
  }

  private class ZhNetImage extends NetImage {

    private String name;

    public ZhNetImage(String name, String picUrl, String linkUrl, String title) {
      super(picUrl, linkUrl, title);
      this.name = name;
    }
  }

  private void jumpToWebActivity(NetImage netImage) {
    String linkUrl = netImage.getLinkUrl();
    if (TextUtils.isEmpty(linkUrl)) {
      return;
    }
    Intent intent = new Intent(MainActivity.this, WebActivity.class);
    intent.putExtra(WebActivity.INTENT_KEY_URL, linkUrl);
    startActivity(intent);
  }

  public void showImages(View view) {
    //indicator.reset(images.size());
    swiper.setNetImages(images);
    swiper.setIndicator(indicator);
    swiper.startSlide();
  }

  public void goNext(View view) {
    //indicator.next();
    swiper.next();
  }

  public void goPrevious(View view) {
    swiper.previous();
  }

  @Override protected void onStart() {
    super.onStart();
    swiper.onStart();
  }

  @Override protected void onStop() {
    super.onStop();
    swiper.onStop();
  }

  //public void gotoIndex(View view) {
  //  int index = (int) (Math.random() * 5);
  //  gotoTv.setText(String.format(getString(R.string.gotoIndex), index));
  //  indicator.setIndex(index);
  //}

  private void initNetImages() {
    NetImage image =
        new NetImage("https://ws1.sinaimg.cn/large/610dc034gy1fhvf13o2eoj20u011hjx6.jpg",
            "http://sczhcloud.com", "");
    NetImage image1 =
        new NetImage("https://ws1.sinaimg.cn/large/610dc034gy1fhupzs0awwj20u00u0tcf.jpg",
            "http://www.youku.com", "");
    NetImage image2 =
        new NetImage("https://ws1.sinaimg.cn/large/610dc034ly1fhovjwwphfj20u00u04qp.jpg",
            "http://baidu.com", "");
    NetImage image3 =
        new NetImage("https://ws1.sinaimg.cn/large/610dc034ly1fh7hwi9lhzj20u011hqa9.jpg",
            "http://hencoder.com/ui-1-1/", "");
    NetImage image4 =
        new NetImage("https://ws1.sinaimg.cn/large/610dc034ly1fgi3vd6irmj20u011i439.jpg",
            "http://gank.io/2017/06/12", "");
    images.add(image);
    images.add(image1);
    images.add(image2);
    images.add(image3);
    images.add(image4);
  }
}
