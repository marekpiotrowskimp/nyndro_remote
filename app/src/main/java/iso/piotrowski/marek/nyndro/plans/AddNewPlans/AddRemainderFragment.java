package iso.piotrowski.marek.nyndro.plans.AddNewPlans;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import iso.piotrowski.marek.nyndro.DataSource.DataSource;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.R;

public class AddRemainderFragment extends Fragment implements AddRemainderContract.IViewer {

    @BindView(R.id.plans_date_picker) DatePicker datePicker;
    @BindView(R.id.plans_time_picker) TimePicker timePicker;
    @BindView(R.id.plans_add_button) Button plansAddButton;
    @BindView(R.id.switch_repeater) SwitchCompat switchRepeater;
    @BindView(R.id.rb_daily) RadioButton rbDaily;
    @BindView(R.id.rb_weekly) RadioButton rbWeekly;
    @BindView(R.id.rb_monthly) RadioButton rbMonthly;
    @BindView(R.id.remainder_practice_recicler_view) RecyclerView recyclerViewRemainder;
    private AddRemainderAdapter addRemainderAdapter;
    private AddRemainderContract.IPresenter presenter;
    private ReminderModel reminder;

    public AddRemainderFragment (){
    }

    public static AddRemainderFragment getInstance (ReminderModel reminder) {
        AddRemainderFragment addRemainderFragment = new AddRemainderFragment();
        addRemainderFragment.setReminder(reminder);
        return addRemainderFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new AddRemainderPresenter(this, DataSource.getInstance());
        LinearLayout plansLayout = (LinearLayout) inflater.inflate(R.layout.fragment_add_remainder, container, false);
        ButterKnife.bind(this, plansLayout);
        return plansLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.loadPracticeData();
        setUpAddRemainderView();
    }

    private void setUpAddRemainderView() {
        if (reminder != null) {
            plansAddButton.setText(getActivity().getResources().getText(R.string.modify_remainder_button));
            int repeate = reminder.getRepeater();
            switchRepeater.setChecked(repeate > 0);
            rbDaily.setChecked(repeate == 1);
            rbWeekly.setChecked(repeate == 2);
            rbMonthly.setChecked(repeate == 3);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(reminder.getPracticeDate());
            datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
            timePicker.setCurrentHour(calendar.get(Calendar.HOUR));
            timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        }
    }

    @OnClick(R.id.plans_add_button)
    public void onAddNewRemainder(View view) {
        if (view.getId() == R.id.plans_add_button) {
            addRemainder();
        }
    }

    void addRemainder() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);

        int repeater = 0;
        if (switchRepeater.isChecked()) {
            if (rbDaily.isChecked()) repeater = 1;
            if (rbWeekly.isChecked()) repeater = 2;
            if (rbMonthly.isChecked()) repeater = 3;
        }

        if (reminder == null) {
            presenter.addReminder(calendar.getTime().getTime(),repeater, addRemainderAdapter.getPractice());
        } else {
            presenter.updateReminder(reminder, calendar.getTime().getTime(),repeater, addRemainderAdapter.getPractice());
        }
        getFragmentManager().popBackStack();
    }

    @Override
    public void setPresenter(AddRemainderContract.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setUpPracticeRecyclerView(List<PracticeModel> practiceList) {
        addRemainderAdapter = new AddRemainderAdapter(practiceList);
        recyclerViewRemainder.setAdapter(addRemainderAdapter);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        recyclerViewRemainder.setLayoutManager(linearLayout);
    }

    public void setReminder(ReminderModel reminder) {
        this.reminder = reminder;
    }
}
