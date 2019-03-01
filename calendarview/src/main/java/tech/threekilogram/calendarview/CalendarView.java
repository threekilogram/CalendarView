package tech.threekilogram.calendarview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.Date;
import tech.threekilogram.calendarview.month.MonthLayout;
import tech.threekilogram.calendarview.week.LinearWeekBar;

/**
 * 显示日期
 *
 * @author Liujin 2019/2/21:12:04:54
 */
public class CalendarView extends ViewGroup {

      private static final String TAG = CalendarView.class.getSimpleName();

      /**
       * 头部星期条
       */
      private LinearWeekBar         mWeekBar;
      /**
       * 月视图
       */
      private MonthLayout           mMonthLayout;
      /**
       * 布局策略
       */
      private MeasureLayoutStrategy mLayoutStrategy;
      /**
       * 每周的起始是不是周一
       */
      private boolean               isFirstDayMonday = true;

      public CalendarView ( Context context ) {

            this( context, null, 0 );
      }

      public CalendarView ( Context context, AttributeSet attrs ) {

            this( context, attrs, 0 );
      }

      public CalendarView ( Context context, AttributeSet attrs, int defStyleAttr ) {

            super( context, attrs, defStyleAttr );
            init( context );
      }

      private void init ( Context context ) {

            mLayoutStrategy = new VerticalLinearMeasureLayoutStrategy();

            MonthLayout monthLayout = new MonthLayout( context );
            setMonthLayout( monthLayout );

            LinearWeekBar weekBar = new LinearWeekBar( context );
            setWeekBar( weekBar );
      }

      void setWeekBar ( LinearWeekBar weekBar ) {

            if( mWeekBar != weekBar ) {
                  if( mWeekBar != null ) {
                        removeView( mWeekBar.getView() );
                  }
                  mWeekBar = weekBar;
                  weekBar.bindParent( this );
                  addView( weekBar.getView() );
                  requestLayout();
            }
      }

      public LinearWeekBar getWeekBar ( ) {

            return mWeekBar;
      }

      void setMonthLayout ( MonthLayout monthLayout ) {

            if( mMonthLayout != monthLayout ) {
                  if( mMonthLayout != null ) {
                        removeView( mMonthLayout.getView() );
                  }
                  mMonthLayout = monthLayout;
                  monthLayout.bindParent( this );
                  addView( monthLayout.getView() );
                  requestLayout();
            }
      }

      public MonthLayout getMonthLayout ( ) {

            return mMonthLayout;
      }

      /**
       * 设置当前页面日期
       */
      public void setDate ( Date date ) {

            mMonthLayout.setDate( date );
      }

      /**
       * 获取基准日期,即:默认日期
       */
      public Date getBaseDate ( ) {

            return mMonthLayout.getDate();
      }

      /**
       * 获取当前页面日期
       */
      public Date getCurrentPageDate ( ) {

            return mMonthLayout.getCurrentPageDate();
      }

      public boolean isMonthMode ( ) {

            return mMonthLayout.isMonthMode();
      }

      public void setMonthMode ( boolean isMonthMode ) {

            mMonthLayout.setMonthMode( isMonthMode );
      }

      public void expandToMonthMode ( ) {

            mMonthLayout.expandToMonthMode();
      }

      public void foldToWeekMode ( ) {

            mMonthLayout.foldToWeekMode();
      }

      /**
       * 设置每周的第一天是否是周一,true:周一
       */
      public void setFirstDayMonday ( boolean firstDayMonday ) {

            if( isFirstDayMonday != firstDayMonday ) {

                  isFirstDayMonday = firstDayMonday;
                  mWeekBar.notifyFirstDayIsMondayChanged( firstDayMonday );
                  mMonthLayout.notifyFirstDayIsMondayChanged( firstDayMonday );
            }
      }

      public boolean isFirstDayMonday ( ) {

            return isFirstDayMonday;
      }

      @Override
      protected void onMeasure ( int widthMeasureSpec, int heightMeasureSpec ) {

            int widthMode = MeasureSpec.getMode( widthMeasureSpec );
            int widthSize = MeasureSpec.getSize( widthMeasureSpec );

            int heightMode = MeasureSpec.getMode( heightMeasureSpec );
            int heightSize = MeasureSpec.getSize( heightMeasureSpec );

            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int paddingRight = getPaddingRight();
            int paddingBottom = getPaddingBottom();

            // 移除padding尺寸,构造尺寸spec
            widthMeasureSpec = MeasureSpec
                .makeMeasureSpec( widthSize - paddingLeft - paddingRight, widthMode );
            heightMeasureSpec = MeasureSpec
                .makeMeasureSpec( heightSize - paddingTop - paddingBottom, heightMode );

            // 使用策略测量组件
            mLayoutStrategy.measureComponent(
                this,
                widthMeasureSpec, heightMeasureSpec,
                mWeekBar, mMonthLayout
            );

            // 设置尺寸信息
            int barHeight = mWeekBar.getView().getMeasuredHeight();
            int monthHeight = mMonthLayout.getView().getMeasuredHeight();
            int finalHeight = barHeight
                + monthHeight
                + paddingTop + paddingBottom;

            setMeasuredDimension( widthSize, finalHeight );
      }

      @Override
      protected void onLayout ( boolean changed, int l, int t, int r, int b ) {

            mLayoutStrategy.layoutComponent( this, mWeekBar, mMonthLayout );
      }

      /**
       * calendar的view组件
       */
      public interface ViewComponent {

            /**
             * 获取组件
             *
             * @return view组件
             */
            View getView ( );

            /**
             * 绑定父组件
             *
             * @param calendarView 父组件
             */
            void bindParent ( CalendarView calendarView );

            /**
             * 通知每周的第一天是否是周一改变了
             *
             * @param isFirstDayMonday 每周第一天是否是周一
             */
            void notifyFirstDayIsMondayChanged ( boolean isFirstDayMonday );
      }

      /**
       * calendar布局策略
       */
      public interface MeasureLayoutStrategy {

            /**
             * 测量组件
             *
             * @param widthMeasureSpec 宽度
             * @param heightMeasureSpec 高度
             * @param weekBar 星期条组件
             * @param monthLayout 显示月份每一天的组件
             *
             * @return 布局占用的尺寸, int[0]表示宽度, int[1]表示高度
             */
            void measureComponent (
                CalendarView parent,
                int widthMeasureSpec,
                int heightMeasureSpec,
                ViewComponent weekBar,
                ViewComponent monthLayout
            );

            void layoutComponent (
                CalendarView parent, ViewComponent weekBar, ViewComponent monthLayout );
      }

      /**
       * 默认布局策略
       */
      protected class VerticalLinearMeasureLayoutStrategy implements MeasureLayoutStrategy {

            @Override
            public void measureComponent (
                CalendarView parent,
                int widthMeasureSpec,
                int heightMeasureSpec,
                ViewComponent weekBar,
                ViewComponent monthLayout ) {

                  int widthSize = MeasureSpec.getSize( widthMeasureSpec );
                  int heightSize = MeasureSpec.getSize( heightMeasureSpec );

                  int widthExactSpec = MeasureSpec
                      .makeMeasureSpec( widthSize, MeasureSpec.EXACTLY );

                  // weekBar 高度包裹住自己
                  int weekBarHeightSpec = MeasureSpec
                      .makeMeasureSpec( heightSize, MeasureSpec.AT_MOST );
                  View view = weekBar.getView();
                  view.measure( widthExactSpec, weekBarHeightSpec );
                  int weekBarMeasuredHeight = view.getMeasuredHeight();

                  // monthLayout 高度包裹住自己
                  int leftHeight = heightSize - weekBarMeasuredHeight;
                  int monthHeightSpec = MeasureSpec.makeMeasureSpec(
                      leftHeight,
                      MeasureSpec.AT_MOST
                  );
                  View layoutView = monthLayout.getView();
                  layoutView.measure( widthExactSpec, monthHeightSpec );
            }

            @Override
            public void layoutComponent (
                CalendarView parent,
                ViewComponent weekBar,
                ViewComponent monthLayout ) {

                  int paddingLeft = parent.getPaddingLeft();
                  int paddingTop = parent.getPaddingTop();

                  View view = weekBar.getView();
                  int offset = paddingTop + view.getMeasuredHeight();
                  view.layout(
                      paddingLeft,
                      paddingTop,
                      paddingLeft + view.getMeasuredWidth(),
                      offset
                  );

                  View layoutView = monthLayout.getView();
                  layoutView.layout(
                      paddingLeft,
                      offset,
                      paddingLeft + layoutView.getMeasuredWidth(),
                      offset + layoutView.getMeasuredHeight()
                  );
            }
      }

      public interface OnDateChangeListener {

            /**
             * 当页面选中后页面日期
             *
             * @param date 页面日期
             */
            void onNewPageSelected ( Date date );

            /**
             * 当新的日期选择后
             *
             * @param newDate 新的选中的日期
             */
            void onNewDateClick ( Date newDate );

            /**
             * 重设日期后的回调
             */
            void onNewDateSet ( Date date );
      }

      public void setOnDateChangeListener ( OnDateChangeListener onDateChangeListener ) {

            mMonthLayout.setOnDateChangeListener( onDateChangeListener );
      }

      public OnDateChangeListener getOnDateChangeListener ( ) {

            return mMonthLayout.getOnDateChangeListener();
      }
}
