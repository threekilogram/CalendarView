package tech.threekilogram.calendarview;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import tech.threekilogram.calendarview.CalendarView.ViewComponent;
import tech.threekilogram.calendarview.CalendarView.WeekDay;

/**
 * @author Liujin 2019/2/21:12:16:29
 */
public class LinearWeekBar extends ViewGroup implements ViewComponent {

      private static final String TAG = LinearWeekBar.class.getSimpleName();

      private CalendarView mParent;

      public LinearWeekBar ( Context context, CalendarView parent ) {

            super( context );
            mParent = parent;
            init();
      }

      private void init ( ) {

            if( getChildCount() != 0 ) {
                  removeAllViews();
            }
            addChildren( mParent );
      }

      @Override
      protected void onMeasure ( int widthMeasureSpec, int heightMeasureSpec ) {

            int widthSize = MeasureSpec.getSize( widthMeasureSpec );
            int heightSize = MeasureSpec.getSize( heightMeasureSpec );

            int widthCellSize = widthSize / 7;

            int widthCellSpec = MeasureSpec.makeMeasureSpec( widthCellSize, MeasureSpec.EXACTLY );
            int heightCellSpec = MeasureSpec.makeMeasureSpec( heightSize, MeasureSpec.AT_MOST );

            int childCount = getChildCount();
            int heightResult = 0;
            for( int i = 0; i < childCount; i++ ) {
                  View child = getChildAt( i );
                  child.measure( widthCellSpec, heightCellSpec );
                  int measuredHeight = child.getMeasuredHeight();
                  if( measuredHeight > heightResult ) {
                        heightResult = measuredHeight;
                  }
            }
            setMeasuredDimension( widthSize, heightResult );
      }

      @Override
      protected void onLayout ( boolean changed, int l, int t, int r, int b ) {

            int widthUsed = 0;
            int childCount = getChildCount();
            for( int i = 0; i < childCount; i++ ) {
                  View child = getChildAt( i );
                  int width = child.getMeasuredWidth();
                  child.layout( widthUsed, 0, widthUsed + width, child.getMeasuredHeight() );
                  widthUsed += width;
            }
      }

      @Override
      public View getView ( ) {

            return this;
      }

      private void addChildren ( CalendarView parent ) {

            for( int i = 0; i < 7; i++ ) {
                  WeekDay weekDay = parent.getWeekDay( i );
                  View view = generateItemView( i, weekDay );
                  addView( view );
            }
      }

      protected View generateItemView ( int index, WeekDay weekDay ) {

            TextView textView = new TextView( getContext() );
            textView.setGravity( Gravity.CENTER );
            textView.setText( weekDay.toString().substring( 0, 3 ) );
            int color = ColorUtil.getColor( index );
            textView.setBackgroundColor( color );
            return textView;
      }
}
