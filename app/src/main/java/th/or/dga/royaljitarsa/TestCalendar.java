package th.or.dga.royaljitarsa;

import android.graphics.Color;
import android.net.ParseException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestCalendar extends AppCompatActivity {

    private String TAG = TestCalendar.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_calendar);
        final CompactCalendarView compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);

        // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT
        Event ev1 = new Event(Color.GREEN, 1433701251000L, "Some extra data that I want to store.");
        compactCalendarView.addEvent(ev1);

        // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
        Event ev2 = new Event(Color.GREEN, 1433704251000L);
        compactCalendarView.addEvent(ev2);

        // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
        //Event ev3 = new Event(Color.BLUE, convertDateToMilliSeconds("24-Dec-2018"));
        Event ev3 = new Event(Color.BLUE, convertDateToMilliSeconds("2018-11-18 00:00:00"));
        compactCalendarView.addEvent(ev3);

        // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
        //Event ev4 = new Event(Color.BLUE, convertDateToMilliSeconds("27-Dec-2018"));
        Event ev4 = new Event(Color.BLUE, convertDateToMilliSeconds("2018-12-16 00:00:00"));
        compactCalendarView.addEvent(ev4);

        // Query for events on Sun, 07 Jun 2015 GMT.
        // Time is not relevant when querying for events, since events are returned by day.
        // So you can pass in any arbitary DateTime and you will receive all events for that day.
        List<Event> events = compactCalendarView.getEvents(1433701251000L); // can also take a Date object

        // events has size 2 with the 2 events inserted previously
        Log.d(TAG, "Events: " + events);

        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });
    }

    private long convertDateToMilliSeconds(String date) {
        long timeInMilliseconds = 0L;
        //String givenDateString = "dd-MMM-yyy Apr 23 16:08:28 GMT+05:30 2013";
        //SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
        try {
            Date mDate = sdf.parse(date);
            timeInMilliseconds = mDate.getTime();
            Log.d(TAG, "Date in milli :: " + timeInMilliseconds);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return timeInMilliseconds;
    }

}
