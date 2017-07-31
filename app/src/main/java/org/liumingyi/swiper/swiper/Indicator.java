package org.liumingyi.swiper.swiper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import static android.content.ContentValues.TAG;

/**
 * 远点角标指示器<br>
 * Created by liumingyi on 2017/7/27.
 */

public class Indicator extends View {

  private static final int DEFAULT_STROKE_WIDTH = 20;
  private static final int DEFAULT_INTERVAL_WIDTH = 50;

  private int count = 0;
  private Paint paint;
  private int normalColor = Color.WHITE;
  private int selectedColor = Color.GREEN;

  private int strokeWidth = DEFAULT_STROKE_WIDTH;
  private int intervalWidth = DEFAULT_INTERVAL_WIDTH;

  private int viewWidth;
  private int viewHeight;

  private int index;
  boolean isAscend;

  public Indicator(Context context) {
    this(context, null);
  }

  public Indicator(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public Indicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(normalColor);
    paint.setStrokeWidth(strokeWidth);
    paint.setStrokeCap(Paint.Cap.ROUND);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    viewWidth = getRealWidth(widthMeasureSpec);
    viewHeight = getRealHeight(heightMeasureSpec);
  }

  private int getRealHeight(int measureSpec) {
    int result;

    int mode = MeasureSpec.getMode(measureSpec);
    int size = MeasureSpec.getSize(measureSpec);

    if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
      //自己计算
      result = strokeWidth;
    } else {
      result = size;
    }

    return result;
  }

  private int getRealWidth(int measureSpec) {

    int result;

    int mode = MeasureSpec.getMode(measureSpec);
    int size = MeasureSpec.getSize(measureSpec);

    if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
      //自己计算
      result = (intervalWidth + strokeWidth) * count + intervalWidth;
    } else {
      result = size;
    }

    return result;
  }

  @Override protected void onDraw(Canvas canvas) {
    if (count <= 0) {
      return;
    }

    int margin = (viewWidth - (intervalWidth + strokeWidth) * count - intervalWidth) / 2;

    Log.d(TAG, "onDraw: " + viewWidth + " , " + viewHeight);
    for (int i = 0; i < count; i++) {
      if (i == index) {
        paint.setColor(selectedColor);
      } else {
        paint.setColor(normalColor);
      }
      canvas.drawPoint(margin + intervalWidth * (i + 1) + strokeWidth * i + strokeWidth / 2,
          viewHeight / 2, paint);
    }
  }

  public void reset(int count) {
    this.count = count;
    invalidate();
  }

  public void next() {
    if (index == 0) {
      isAscend = true;
    } else if (index == count - 1) {
      isAscend = false;
    }

    if (isAscend) {
      index++;
    } else {
      index--;
    }

    invalidate();
  }

  public void setIndex(int index) {
    this.index = index;
    invalidate();
  }
}
