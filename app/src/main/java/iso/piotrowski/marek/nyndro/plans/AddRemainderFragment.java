package iso.piotrowski.marek.nyndro.plans;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.practice.PracticeDatabaseHelper;
import iso.piotrowski.marek.nyndro.tools.SQLHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddRemainderFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerViewRemainder;
    private SQLiteDatabase db;
    private Cursor cursor;
    private AddRemainderAdapter addRemainderAdapter;
    private int plan_id = -1;


    public AddRemainderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout plansLayout = (LinearLayout) inflater.inflate(R.layout.fragment_add_remainder, container, false);
        return plansLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            PracticeDatabaseHelper practiceDatabaseHelper = new PracticeDatabaseHelper(getActivity());
            db = practiceDatabaseHelper.getWritableDatabase();
            cursor = SQLHelper.getCursorPractice(db);
            recyclerViewRemainder = (RecyclerView) getActivity().findViewById(R.id.remainder_practice_recicler_view);
            addRemainderAdapter = new AddRemainderAdapter();
            addRemainderAdapter.setCursorPractice(cursor);
            recyclerViewRemainder.setAdapter(addRemainderAdapter);
            LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            recyclerViewRemainder.setLayoutManager(linearLayout);

            getActivity().findViewById(R.id.plans_add_button).setOnClickListener(this);
            Bundle arguments = getArguments();
            if (arguments != null) {
                plan_id = arguments.getInt("plan_id");
                int practice_id = arguments.getInt("practice_id");
                long date = arguments.getLong("date");
                int repeate = arguments.getInt("repeat");
                DatePicker datePicker = (DatePicker) getActivity().findViewById(R.id.plans_date_picker);
                TimePicker timePicker = (TimePicker) getActivity().findViewById(R.id.plans_time_picker);

                SwitchCompat switchRepeater = (SwitchCompat) getActivity().findViewById(R.id.switch_repeater);
                RadioButton rbDaily = (RadioButton) getActivity().findViewById(R.id.rb_daily);
                RadioButton rbWeekly = (RadioButton) getActivity().findViewById(R.id.rb_weekly);
                RadioButton rbMonthly = (RadioButton) getActivity().findViewById(R.id.rb_monthly);
                if (repeate > 0) {
                    switchRepeater.setChecked(true);
                    switch (repeate) {
                        case 1:
                            rbDaily.setChecked(true);
                            break;
                        case 2:
                            rbWeekly.setChecked(true);
                            break;
                        case 3:
                            rbMonthly.setChecked(true);
                            break;
                    }
                }

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(date);
                datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
                timePicker.setCurrentHour(calendar.get(Calendar.HOUR));
                timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
                if (cursor != null) {
                    cursor.moveToFirst();
                    while (cursor.moveToNext()) {
                        if (cursor.getInt(0) == practice_id) {
                            addRemainderAdapter.setSelectedPosition(cursor.getPosition());
                        }
                    }
                }
            }
        } catch (SQLiteException e) {
        }
    }

    @Override
    public void onDestroy() {
        if (cursor != null) cursor.close();
        if (db != null) db.close();
        super.onDestroy();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.plans_add_button) {
            addRemainder();
        }
    }

    void addRemainder() {
        try {
            if ((cursor != null) && (cursor.getCount() > 0)) {
                if (cursor.moveToPosition(addRemainderAdapter.getSelectedPosition())) {
                    int id = cursor.getInt(0);
                    DatePicker datePicker = (DatePicker) getActivity().findViewById(R.id.plans_date_picker);
                    TimePicker timePicker = (TimePicker) getActivity().findViewById(R.id.plans_time_picker);

                    SwitchCompat switchRepeater = (SwitchCompat) getActivity().findViewById(R.id.switch_repeater);
                    RadioButton rbDaily = (RadioButton) getActivity().findViewById(R.id.rb_daily);
                    RadioButton rbWeekly = (RadioButton) getActivity().findViewById(R.id.rb_weekly);
                    RadioButton rbMonthly = (RadioButton) getActivity().findViewById(R.id.rb_monthly);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);

                    int repeater = 0;
                    if (switchRepeater.isChecked()) {
                        if (rbDaily.isChecked()) repeater = 1;
                        if (rbWeekly.isChecked()) repeater = 2;
                        if (rbMonthly.isChecked()) repeater = 3;
                    }

                    if (plan_id >= 0) {
                        SQLHelper.updateRemainder(db, id, calendar.getTime().getTime(), 108, repeater, plan_id);
                    } else {
                        SQLHelper.insertRemainder(db, id, calendar.getTime().getTime(), 108, repeater);
                    }
                }
            }
        } catch (SQLiteException e) {
        }
        getFragmentManager().popBackStack();
    }

}
