package iso.piotrowski.marek.nyndro.practice.ListOfPractice;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.DataSource.ConstantsData.Practice;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.tools.Fragments.FragmentsFactory;
import iso.piotrowski.marek.nyndro.tools.Fragments.NyndroFragment;

public class PracticeListFragment extends NyndroFragment {

    private OnListFragmentInteractionListener listener =null;

    public PracticeListFragment() {
    }

    public static PracticeListFragment getInstance(OnListFragmentInteractionListener listener){
        return new PracticeListFragment().setListener(listener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.practice_fragment_item_list, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new PracticeListRecyclerViewAdapter(Practice.practices, listener));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        setListener(null);
        super.onDetach();
    }

    public PracticeListFragment setListener(OnListFragmentInteractionListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public String getFragmentName() {
        return NyndroApp.getContext().getResources().getString(R.string.app_label_practice);
    }

    @Override
    public FragmentsFactory.TypeOfFragment getTypeOf() {
        return FragmentsFactory.TypeOfFragment.PracticeList;
    }

    @Override
    public boolean isButtonVisible() {
        return false;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Practice item);
    }

}
