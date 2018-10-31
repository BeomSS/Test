package com.example.kimkyeongbeom.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by a7med on 28/06/2015.
 */
public class CustomCalendarView extends LinearLayout {
    public static LinearLayout layout;
    // for logging
    private static final String LOGTAG = "Calendar View";

    // how many days to show, defaults to six weeks, 42 days
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // date format
    private String dateFormat;

    // current displayed month
    private Calendar currentDate = Calendar.getInstance();

    //event handling
    private EventHandler eventHandler = null;

    // internal components
    private LinearLayout header;
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
    private GridView grid;

    // seasons' rainbow
    int[] rainbow = new int[]{
            R.color.summer,
            R.color.fall,
            R.color.winter,
            R.color.spring
    };

    // month-season association (northern hemisphere, sorry australia :)
    int[] monthSeason = new int[]{2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};

    DBHelper dbHelper;
    SQLiteDatabase sqlDB;
    SimpleDateFormat fm;
    SimpleDateFormat sfm;

    Calendar selectedDay = Calendar.getInstance();
    String selectedDayStr = "";

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CustomCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    /**
     * Load control xml layout
     */
    @SuppressLint("SimpleDateFormat")
    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_calendar, this);

        layout = this;

        loadDateFormat(attrs);
        assignUiElements();
        assignClickHandlers();

        updateCalendar();

        dbHelper = new DBHelper(context);
        sqlDB = dbHelper.getWritableDatabase();
        dbHelper.onCreate(sqlDB);
        fm = new SimpleDateFormat("yyyy년 MM월 dd일");
        sfm = new SimpleDateFormat("yyyy-MM-dd");
        selectedDayStr = sfm.format(selectedDay.getTime());
    }

    private void loadDateFormat(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCalendarView);

        try {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.CustomCalendarView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        } finally {
            ta.recycle();
        }
    }

    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        header = (LinearLayout) findViewById(R.id.calendar_header);
        btnPrev = (ImageView) findViewById(R.id.calendar_prev_button);
        btnNext = (ImageView) findViewById(R.id.calendar_next_button);
        txtDate = (TextView) findViewById(R.id.calendar_date_display);
        grid = (GridView) findViewById(R.id.calendar_grid);
    }

    private void assignClickHandlers() {
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                updateCalendar();
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                updateCalendar();
            }
        });

        // long-pressing a day
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> view, View cell, int position, long id) {
                // handle long-press
                if (eventHandler == null)
                    return false;

                eventHandler.onDayLongPress((Date) view.getItemAtPosition(position));
                return true;
            }
        });
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar() {
        updateCalendar(null);
    }

    /**
     * Display dates correctly in grid
     */
    @SuppressLint("SimpleDateFormat")
    public void updateCalendar(HashSet<Date> events) {
        ArrayList<Date> cells = new ArrayList<>();            //날짜 담는 셀(어레이 리스트)
        Calendar calendar = (Calendar) currentDate.clone();    //캘린더정보 복사

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);                //1일로 지정
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;    //1일 전 같은 주의 저번달 날짜 중 첫번째 날짜 저장(달력의 첫번째 날짜)

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);            //달력 첫째 날 정보를 캘린더에 추가(그 달 1일 - 달력 첫째 넣을 날짜 = ? 를 추가)

        // fill cells
        while (cells.size() < DAYS_COUNT)                                    //셀 사이즈를 42칸 만큼 반복
        {
            cells.add(calendar.getTime());                                    //셀에 캘린더 해당 날짜 입력
            calendar.add(Calendar.DAY_OF_MONTH, 1);                        //캘린더 하루 증가
        }

        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells, events));    //만든 셀을 그리드뷰 어댑터로 설정

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        txtDate.setText(sdf.format(currentDate.getTime()));                    //달력에 월, 일 출력

        // set header color according to current season
        int month = currentDate.get(Calendar.MONTH);
        int season = monthSeason[month];
        int color = rainbow[season];

        header.setBackgroundColor(getResources().getColor(color));
    }

    private class CalendarAdapter extends ArrayAdapter<Date> {
        // days with events
        private HashSet<Date> eventDays;

        // for view inflation
        private LayoutInflater inflater;

        public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays) {
            super(context, R.layout.control_calendar_day, days);
            this.eventDays = eventDays;
            inflater = LayoutInflater.from(context);
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(int position, View view, @NonNull ViewGroup parent) {
            // day in question
            Date date = getItem(position);
            final Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int day = cal.get(Calendar.DATE);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);

            // today
            Date today = new Date();
            Calendar todayCal = Calendar.getInstance();
            todayCal.setTime(today);

            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.control_calendar_day, parent, false);

            TextView tvDay = view.findViewById(R.id.tvDay);
            TextView tvDayTitle = view.findViewById(R.id.tvDayTitle);
            TextView tvDayContent = view.findViewById(R.id.tvDayContent);
            ImageView ivMaking = view.findViewById(R.id.ivMarking);

            ivMaking.setVisibility(View.GONE);

            // if this day has an event, specify event image
            tvDay.setBackgroundResource(0);
            if (eventDays != null) {
                for (Date eventDate : eventDays)        //Date 형의 eventDate 변수 생성, eventDays 의 값을 하나씩 끝날때까지 넣어가며 반복 - enhanced for loop
                {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(eventDate);
                    if (calendar.get(Calendar.DATE) == day &&
                            calendar.get(Calendar.MONTH) == month &&
                            calendar.get(Calendar.YEAR) == year) {
                        // mark this day for event
                        tvDay.setBackgroundResource(R.drawable.reminder);
                        break;
                    }
                }
            }

            // clear styling
            tvDay.setTypeface(null, Typeface.NORMAL);
            tvDay.setTextColor(Color.BLACK);

            if (month != currentDate.get(Calendar.MONTH) || year != currentDate.get(Calendar.YEAR)) {
                // if this day is outside current month, grey it out
                tvDay.setTextColor(getResources().getColor(R.color.greyed_out));
            } else if (day == todayCal.get(Calendar.DATE) && month == todayCal.get(Calendar.MONTH) && year == todayCal.get(Calendar.YEAR)) {
                // if it is today, set it to blue/bold
                tvDay.setTypeface(null, Typeface.BOLD);
                tvDay.setTextColor(getResources().getColor(R.color.today));
                view.setBackground(getResources().getDrawable(R.drawable.today_day));
            }
            if (day == selectedDay.get(Calendar.DATE) && month == selectedDay.get(Calendar.MONTH) && year == selectedDay.get(Calendar.YEAR)) {
                view.setBackground(getResources().getDrawable(R.drawable.select_day_background));
            }

            // set text
            tvDay.setText(String.valueOf(cal.get(Calendar.DATE)));

            String dayStr = sfm.format(cal.getTime());
            String title = "";
            String content = "";
            Cursor cursor = sqlDB.rawQuery("select title, content from diary where date is '" + dayStr + "';", null);
            while (cursor.moveToNext()) {
                title = cursor.getString(0);
                content = cursor.getString(1);
            }
            cursor.close();
            if (title.length() > 5) {
                title = title.substring(0, 4);
                title += "..";
            }
            if (content.length() > 5) {
                content = content.substring(0, 4);
                content += "..";
            }
            tvDayTitle.setText(title);
            tvDayContent.setText(content.replace("\n", "⤶"));
            if (!(tvDayContent.getText().equals("") && tvDayTitle.getText().equals("")))
                ivMaking.setVisibility(View.VISIBLE);

            view.setOnClickListener(new OnClickListener() {
                @SuppressLint("SetTextI18n, SimpleDateFormat")
                @Override
                public void onClick(View view) {
                    selectedDay.setTime(cal.getTime());
                    selectedDayStr = sfm.format(selectedDay.getTime());
                    String dayStr = sfm.format(cal.getTime());

                    Cursor cursor = sqlDB.rawQuery("select title, content from diary where date is '" + dayStr + "';", null);
                    if (cursor.getCount() <= 0) {
                        ((RightCalendarActivity) RightCalendarActivity.context).tvTitle.setText("");
                        ((RightCalendarActivity) RightCalendarActivity.context).tvContent.setText("");
                        Intent intent = new Intent(getContext(), AddDiaryActivity.class);
                        intent.putExtra("addDay", sfm.format(selectedDay.getTime()));
                        getContext().startActivity(intent);
                    } else {
                        cursor.moveToNext();
                        ((RightCalendarActivity) RightCalendarActivity.context).tvTitle.setText(cursor.getString(0));
                        ((RightCalendarActivity) RightCalendarActivity.context).tvContent.setText(cursor.getString(1));
                    }
                    cursor.close();
                    updateCalendar();
                }
            });

            return view;
        }
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler {
        void onDayLongPress(Date date);
    }
}
