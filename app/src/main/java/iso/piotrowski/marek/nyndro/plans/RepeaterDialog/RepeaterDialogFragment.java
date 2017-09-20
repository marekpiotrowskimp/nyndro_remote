package iso.piotrowski.marek.nyndro.plans.RepeaterDialog;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.R;

/**
 * Created by marek.piotrowski on 07/09/2017.
 */

public class RepeaterDialogFragment extends DialogFragment {

    @BindView(R.id.toggle_switches) ToggleSwitch toggleSwitch;
    @BindView(R.id.remainder_time) TimePicker timePicker;
    private OnRemainderDetailsListener toggleSwitchListener;

    public interface OnRemainderDetailsListener {
        void onRemainderDetailsDone(int hours, int minutes, int position);
    }

    public static RepeaterDialogFragment newInstance(OnRemainderDetailsListener toggleSwitchListener) {
        RepeaterDialogFragment repeaterDialogFragment = new RepeaterDialogFragment();
        repeaterDialogFragment.toggleSwitchListener = toggleSwitchListener;
        return repeaterDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.remainder_dialog, container, false);
        ButterKnife.bind(this, linearLayout);
        return linearLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpToggleSwitch();
        getDialog().setTitle(R.string.repeater_title);
    }

    private void setUpToggleSwitch() {
        ArrayList<String> labels = new ArrayList<>(Arrays.asList(NyndroApp.getContext().getResources().getStringArray(R.array.frequents)));
        toggleSwitch.setLabels(labels);
        toggleSwitch.setCheckedTogglePosition(0);
    }

    @OnClick(R.id.repeater_button)
    public void onButtonClick(View view) {
        if (toggleSwitchListener != null) {
            toggleSwitchListener.onRemainderDetailsDone(timePicker.getCurrentHour(), timePicker.getCurrentMinute(), toggleSwitch.getCheckedTogglePosition());
        }
        dismiss();
    }
}
