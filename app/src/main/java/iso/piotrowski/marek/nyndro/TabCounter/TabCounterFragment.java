package iso.piotrowski.marek.nyndro.TabCounter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.tools.Fragments.FragmentsFactory;
import iso.piotrowski.marek.nyndro.tools.Fragments.NyndroFragment;

/**
 * Created by marek.piotrowski on 26/08/2017.
 */

public class TabCounterFragment extends NyndroFragment implements View.OnClickListener {

    private int count = 0;
    private TextView counter;

    public static TabCounterFragment getInstance(){
        return new TabCounterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = getLayout(getActivity());
        return layout;
    }

    private LinearLayout getLayout(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setBackgroundResource(R.drawable.amitabha_large);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.getBackground().setAlpha(30);
        linearLayout.setOnClickListener(this);

        counter = new TextView(context);
        counter.setText(getCount(count));
        counter.setTextSize(120);
        counter.setAlpha(1f);
        counter.setGravity(Gravity.CENTER);
        linearLayout.addView(counter);
        return linearLayout;
    }

    private String getCount(int count) {
        return String.format("%04d",count);
    }

    @Override
    public FragmentsFactory.TypeOfFragment getTypeOf() {
        return FragmentsFactory.TypeOfFragment.TapCounter;
    }

    @Override
    public String getFragmentName() {
        return NyndroApp.getContect().getResources().getString(R.string.tap_counter_name);
    }

    @Override
    public void onClick(View v) {
        counter.setText(getCount(++count));
    }
}
