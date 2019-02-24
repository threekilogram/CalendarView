package tech.threekilogram.calendarviewlib;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Liujin 2019/2/20:17:02:58
 */
public class Test {

      public static void main ( String[] args ) {

            int count = CalendarUtils.monthDayCount( 2019, 8 );
            int dayOfWeek = CalendarUtils.dayOfWeek( 2019, 8, 1 );

            System.out.println( count + " " + dayOfWeek );

            Calendar instance = Calendar.getInstance();
            for( int i = 0; i < 7; i++ ) {
                  instance.set( 2019, 8, i + 1 );
                  Date time = instance.getTime();
                  int i1 = instance.get( Calendar.DAY_OF_WEEK );
                  System.out.println( time + " " + i1 );
            }
      }

      public static class CalendarUtils {

            private static Calendar sCalendar = Calendar.getInstance();

            public static int monthDayCount ( int year, int month ) {

                  sCalendar.set( year, month + 1, 1 );
                  sCalendar.add( Calendar.DAY_OF_MONTH, -1 );
                  return sCalendar.get( Calendar.DAY_OF_MONTH );
            }

            public static int dayOfWeek ( int year, int month, int day ) {

                  sCalendar.set( year, month, day );
                  return sCalendar.get( Calendar.DAY_OF_WEEK );
            }

            public static int getYear ( Date date ) {

                  sCalendar.setTime( date );
                  return sCalendar.get( Calendar.YEAR );
            }

            public static int getMonth ( Date date ) {

                  sCalendar.setTime( date );
                  return sCalendar.get( Calendar.MONTH );
            }

            public static Date getMonthByStep ( int year, int month, int offset ) {

                  sCalendar.set( year, month, 1 );
                  sCalendar.add( Calendar.MONTH, offset );
                  return sCalendar.getTime();
            }
      }
}