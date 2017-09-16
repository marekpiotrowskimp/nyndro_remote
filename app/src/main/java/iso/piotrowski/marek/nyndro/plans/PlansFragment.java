package iso.piotrowski.marek.nyndro.plans;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desai.vatsal.mydynamiccalendar.MyDynamicCalendar;
import com.desai.vatsal.mydynamiccalendar.OnDateClickListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.DataSource.DataSource;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.plans.PlansList.PlansListFragment;
import iso.piotrowski.marek.nyndro.FragmentsFactory.FragmentsFactory;
import iso.piotrowski.marek.nyndro.Navigator.Navigator;
import iso.piotrowski.marek.nyndro.FragmentsFactory.NyndroFragment;

public class PlansFragment extends NyndroFragment implements PlansContract.IViewer {

    @BindView(R.id.dynamic_calendar) MyDynamicCalendar dynamicCalendar;

    public PlansFragment() {
    }

    private PlansContract.IPresenter getPresenter(){
        return (PlansContract.IPresenter)presenter;
    }

    public static PlansFragment getInstance(){
        return new PlansFragment();
    }

    @Override
    public void setPresenter(PlansContract.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPlans(List<ReminderModel> reminderList) {
        showPlans(reminderList, false);
    }

    @Override
    public void refreshPlans(List<ReminderModel> reminderList) {
        showPlans(reminderList, true);
    }

    public void showPlans(List<ReminderModel> reminderList, boolean refresh) {
        dynamicCalendar.deleteAllEvent();
        for (ReminderModel reminder : reminderList){
            Date date = new Date();
            date.setTime(reminder.getPracticeDate());
            addCalendarEvent(date, reminder.getPractice().getName());
        }
        dynamicCalendar.refreshCalendar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new PlanPresenter(this, DataSource.getInstance());
        View layout = inflater.inflate(R.layout.fragment_plans, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpDynamicCalendar(dynamicCalendar);
        getPresenter().loadPlansData();
    }

    private void setUpDynamicCalendar(MyDynamicCalendar myCalendar) {
        myCalendar.showMonthViewWithBelowEvents();
        myCalendar.setCalendarBackgroundColor(getColor(R.color.colorLightCoffeeDarker));
        myCalendar.setHeaderBackgroundColor(getColor(R.color.colorPrimaryDark));
        myCalendar.setHeaderTextColor(getColor(R.color.colorLightCoffee));
        myCalendar.setNextPreviousIndicatorColor(getColor(R.color.white));
        myCalendar.setWeekDayLayoutBackgroundColor(getColor(R.color.colorLightCoffeeDarker2));
        myCalendar.setWeekDayLayoutTextColor(getColor(R.color.whiteDark10));
        myCalendar.setDatesOfMonthBackgroundColor(getColor(R.color.colorGray));
        myCalendar.setCurrentDateBackgroundColor(getColor(R.color.colorLightCoffeeDarker));
        myCalendar.setCurrentDateTextColor(getColor(R.color.colorPrimaryDark));
        myCalendar.setEventCellBackgroundColor(getColor(R.color.colorLightCoffeeDarker));
        myCalendar.setEventCellTextColor(getColor(R.color.black));
        myCalendar.setBelowMonthEventTextColor(getColor(R.color.black));
        myCalendar.isSaturdayOff(true, getColor(R.color.colorPrimary), getColor(R.color.white));
        myCalendar.isSundayOff(true, getColor(R.color.colorPrimary), getColor(R.color.white));
        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(Date date) {
            }

            @Override
            public void onLongClick(Date date) {
                Navigator.getInstance().changeFragmentInContainer(PlansListFragment.getInstance(date), true);
            }
        });
    }

    private void addCalendarEvent(Date date, String practiceName) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String startHour = String.format(Locale.UK, "%tR", calendar);
        calendar.add(Calendar.HOUR, 1);
        String stopHour = String.format(Locale.UK, "%tR", calendar);
        dynamicCalendar.addEvent(String.format(Locale.UK, "%td-%tm-%tY", calendar, calendar, calendar),
                startHour,
                stopHour,
                practiceName, R.mipmap.ic_buddha_24);
    }

    private int getColor(int colorId) {
        return NyndroApp.getContext().getResources().getColor(colorId);
    }


    @Override
    public String getFragmentName() {
        return NyndroApp.getContext().getResources().getString(R.string.app_label_plans);
    }

    @Override
    public FragmentsFactory.TypeOfFragment getTypeOf() {
        return FragmentsFactory.TypeOfFragment.Plans;
    }

    @Override
    public boolean isButtonVisible() {
        return false;
    }

}
