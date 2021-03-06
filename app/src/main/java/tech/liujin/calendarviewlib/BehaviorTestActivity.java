package tech.liujin.calendarviewlib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.Locale;
import tech.liujin.calendar.CalendarView;
import tech.liujin.calendar.behavior.CalendarBehaviors;
public class BehaviorTestActivity extends AppCompatActivity {

      private static final String TAG = BehaviorTestActivity.class.getSimpleName();

      private CalendarView      mCalendar;
      private RecyclerView      mRecycler;
      private MockEvent         mMockEvent = new MockEvent();
      private CalendarBehaviors mBehaviors = new CalendarBehaviors();

      public static void start ( Context context ) {

            Intent starter = new Intent( context, BehaviorTestActivity.class );
            context.startActivity( starter );
      }

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            setContentView( R.layout.activity_behavior_test );
            initView();
      }

      private void initView ( ) {

            mCalendar = findViewById( R.id.calendar );
            mRecycler = findViewById( R.id.recycler );
            mRecycler.setLayoutManager( new LinearLayoutManager( this ) );
            mRecycler.setAdapter( new Adapter() );

            mBehaviors.setUpWith( mCalendar, mRecycler );
      }

      private class Holder extends ViewHolder {

            TextView mTextView;

            private Holder ( @NonNull View itemView ) {

                  super( itemView );
                  mTextView = itemView.findViewById( R.id.text );
            }

            private void bind ( int position ) {

                  mTextView.setText( String.format( Locale.CHINA, "item %d", position ) );
            }
      }

      private class Adapter extends RecyclerView.Adapter<Holder> {

            @NonNull
            @Override
            public Holder onCreateViewHolder ( @NonNull ViewGroup parent, int viewType ) {

                  View view = getLayoutInflater().inflate( R.layout.item_recycler, parent, false );
                  return new Holder( view );
            }

            @Override
            public void onBindViewHolder ( @NonNull Holder holder, int position ) {

                  holder.bind( position );
            }

            @Override
            public int getItemCount ( ) {

                  return 20;
            }
      }
}
