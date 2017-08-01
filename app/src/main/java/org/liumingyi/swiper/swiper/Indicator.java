package org.liumingyi.swiper.swiper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import org.liumingyi.swiper.DensityUtils;
import org.liumingyi.swiper.R;

/**
 * 远点角标指示器<br>
 * Created by liumingyi on 2017/7/27.
 */

public class Indicator extends View {

  private static final int DEFAULT_STROKE_WIDTH = 8;//dp
  private static final int DEFAULT_INTERVAL_WIDTH = 16;//dp

  private int count = 0;
  private Paint paint;
  private int normalColor;
  private int selectedColor;

  private int strokeWidth;
  private int intervalWidth;

  private int viewWidth;
  private int viewHeight;

  private int index;

  public Indicator(Context context) {
    this(context, null);
  }

  public Indicator(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public Indicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Indicator);
    strokeWidth = (int) typedArray.getDimension(R.styleable.Indicator_strokeWidth,
        DensityUtils.dip2px(context, DEFAULT_STROKE_WIDTH));
    intervalWidth = (int) typedArray.getDimension(R.styleable.Indicator_intervalWidth,
        DensityUtils.dip2px(context, DEFAULT_INTERVAL_WIDTH));
    normalColor = typedArray.getColor(R.styleable.Indicator_normalColor, Color.WHITE);
    selectedColor = typedArray.getColor(R.styleable.Indicator_selectedColor, Color.GREEN);
    typedArray.recycle();

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

  public void setIndex(int index) {
    this.index = index;
    invalidate();
  }
}
