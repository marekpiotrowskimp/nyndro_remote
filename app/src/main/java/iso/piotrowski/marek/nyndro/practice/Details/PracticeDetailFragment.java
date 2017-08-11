package iso.piotrowski.marek.nyndro.practice.Details;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;

import iso.piotrowski.marek.nyndro.DataSource.PracticeDatabaseHelper;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.UIComponents.BuddaProgressBar;
import iso.piotrowski.marek.nyndro.practice.PracticeAdapter;
import iso.piotrowski.marek.nyndro.tools.SQLHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class PracticeDetailFragment extends Fragment {

    public static final int STANDARD_VIEW = 1;
    public static final int EDIT_VIEW = 2;
    private int editMode = STANDARD_VIEW;

    private SQLiteDatabase db;
    private Cursor cursorPractice;
    private int position;

    public PracticeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout =inflater.inflate(R.layout.fragment_practice_detail, container, false);
        if (savedInstanceState!=null)
        {
            position  = savedInstanceState.getInt("position");
        }
        setHasOptionsMenu(true);
        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.practice_menu_detail,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_toolbar:
                if (getEditMode() == EDIT_VIEW) {
                    getFragmentManager().popBackStack();
                } else {
                    item.setIcon(getResources().getDrawable(R.mipmap.ic_edit_black_48dp));
                }
                setEdit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        pripareView(getView());
    }

    void pripareView (View layout){
        try {
            PracticeDatabaseHelper practiceDatabaseHelper = new PracticeDatabaseHelper(getActivity());
            db = practiceDatabaseHelper.getReadableDatabase();
            cursorPractice = SQLHelper.getCursorPractice(db);
            if (cursorPractice.moveToPosition(position)) {
                viewPractice(getEditMode());
            }
        }catch (SQLiteException e){}
    }

    private void viewPractice (int typeViewPractice)
    {
        View mainAct = (View) getActivity().findViewById(R.id.fragment_detail_layout);

        ImageView practiceImage = (ImageView)mainAct.findViewById(R.id.practice_image_detail);
        TextView practiceName = (TextView)mainAct.findViewById(R.id.practice_name_detail);
        TextView practiceStatusProgress = (TextView)mainAct.findViewById(R.id.practice_status_progress_detail);
        TextView practiceStatusMaxRepetiton = (TextView)mainAct.findViewById(R.id.practice_status_maxrepetition_detail);
        TextView practiceRepetition = (TextView)mainAct.findViewById(R.id.practice_repetition_detail);
        TextView practiceDateLast = (TextView)mainAct.findViewById(R.id.practice_date_last_detail);
        TextView practiceDateNext = (TextView)mainAct.findViewById(R.id.practice_date_next_detail);
        TextView practiceDescription = (TextView)mainAct.findViewById(R.id.practice_description_detail);
        BuddaProgressBar practiceProgress = (BuddaProgressBar)mainAct.findViewById(R.id.practice_progress_detail);

        practiceImage.setContentDescription(cursorPractice.getString(PracticeAdapter.NAME_ID));
        practiceImage.setImageDrawable(mainAct.getResources().getDrawable(cursorPractice.getInt(PracticeAdapter.PRACTICE_IMAGE_ID_ID)));
        practiceProgress.setMaxProgress(cursorPractice.getInt(PracticeAdapter.MAX_REPETITION_ID));
        practiceProgress.setProgress(cursorPractice.getInt(PracticeAdapter.PROGRESS_ID));


        Calendar calendar = Calendar.getInstance();
        long lastDate = SQLHelper.lastPractice(db, cursorPractice.getInt(PracticeAdapter._ID));
        if (lastDate == -1) {
            practiceDateLast.setText(mainAct.getResources().getString(R.string.last_practice_date) + " -----------");
        } else {
            calendar.setTimeInMillis(lastDate); //cursorPractices.getLong(LAST_PRACTICE_DATE_ID));
            practiceDateLast.setText(mainAct.getResources().getString(R.string.last_practice_date) + String.format(" %tD", calendar));
        }
        long nextDate = SQLHelper.nextPractice(db, cursorPractice.getInt(PracticeAdapter._ID));
        if (nextDate == -1) {
            practiceDateNext.setText(mainAct.getResources().getString(R.string.next_practice_date) + " -----------");
        } else{
            calendar.setTimeInMillis(nextDate);  //cursorPractices.getLong(NEXT_PRACTICE_DATE_ID));
            practiceDateNext.setText(mainAct.getResources().getString(R.string.next_practice_date) + String.format(" %tD", calendar));
        }

        if (typeViewPractice==STANDARD_VIEW) {
            practiceName.setText(mainAct.getResources().getText(R.string.name_practice) + " " + cursorPractice.getString(PracticeAdapter.NAME_ID));
            practiceStatusProgress.setText(mainAct.getResources().getText(R.string.progress_name) + " " + String.valueOf(cursorPractice.getInt(PracticeAdapter.PROGRESS_ID)));
            practiceStatusMaxRepetiton.setText(mainAct.getResources().getText(R.string.max_repetition_name) + " " + String.valueOf(cursorPractice.getInt(PracticeAdapter.MAX_REPETITION_ID)));
            practiceRepetition.setText(mainAct.getResources().getText(R.string.repetiton_name) + " " + String.valueOf(cursorPractice.getInt(PracticeAdapter.REPETITION_ID)));
            practiceDescription.setText(mainAct.getResources().getText(R.string.description_name) + " " + cursorPractice.getString(PracticeAdapter.DESCRIPTION_ID));

            EditText practiceNameEdit = (EditText) mainAct.findViewById(R.id.practice_name_edit);
            EditText practiceStatusProgressEdit = (EditText) mainAct.findViewById(R.id.practice_status_progress_edit);
            EditText practiceStatusMaxRepetitonEdit = (EditText) mainAct.findViewById(R.id.practice_status_maxrepetition_edit);
            EditText practiceRepetitionEdit = (EditText) mainAct.findViewById(R.id.practice_repetition_edit);
            EditText practiceDescriptionEdit = (EditText) mainAct.findViewById(R.id.practice_description_edit);

            practiceNameEdit.setVisibility(View.INVISIBLE);
            practiceStatusProgressEdit.setVisibility(View.INVISIBLE);
            practiceStatusMaxRepetitonEdit.setVisibility(View.INVISIBLE);
            practiceRepetitionEdit.setVisibility(View.INVISIBLE);
            practiceDescriptionEdit.setVisibility(View.INVISIBLE);

            practiceNameEdit.setText("");
            practiceStatusProgressEdit.setText("");
            practiceStatusMaxRepetitonEdit.setText("");
            practiceRepetitionEdit.setText("");
            practiceDescriptionEdit.setText("");

        }

        if (typeViewPractice==EDIT_VIEW) {
            EditText practiceNameEdit = (EditText) mainAct.findViewById(R.id.practice_name_edit);
            EditText practiceStatusProgressEdit = (EditText) mainAct.findViewById(R.id.practice_status_progress_edit);
            EditText practiceStatusMaxRepetitonEdit = (EditText) mainAct.findViewById(R.id.practice_status_maxrepetition_edit);
            EditText practiceRepetitionEdit = (EditText) mainAct.findViewById(R.id.practice_repetition_edit);
            EditText practiceDescriptionEdit = (EditText) mainAct.findViewById(R.id.practice_description_edit);

            practiceNameEdit.setVisibility(View.VISIBLE);
            practiceStatusProgressEdit.setVisibility(View.VISIBLE);
            practiceStatusMaxRepetitonEdit.setVisibility(View.VISIBLE);
            practiceRepetitionEdit.setVisibility(View.VISIBLE);
            practiceDescriptionEdit.setVisibility(View.VISIBLE);

            practiceName.setText(mainAct.getResources().getText(R.string.name_practice) + " ");
            practiceNameEdit.setText(cursorPractice.getString(PracticeAdapter.NAME_ID));

            practiceStatusProgress.setText(mainAct.getResources().getText(R.string.progress_name) + " ");
            practiceStatusProgressEdit.setText(String.valueOf(cursorPractice.getInt(PracticeAdapter.PROGRESS_ID)));

            practiceStatusMaxRepetiton.setText(mainAct.getResources().getText(R.string.max_repetition_name) + " ");
            practiceStatusMaxRepetitonEdit.setText(String.valueOf(cursorPractice.getInt(PracticeAdapter.MAX_REPETITION_ID)));

            practiceRepetition.setText(mainAct.getResources().getText(R.string.repetiton_name) + " ");
            practiceRepetitionEdit.setText(String.valueOf(cursorPractice.getInt(PracticeAdapter.REPETITION_ID)));

            practiceDescription.setText(mainAct.getResources().getText(R.string.description_name) + " ");
            practiceDescriptionEdit.setText(cursorPractice.getString(PracticeAdapter.DESCRIPTION_ID));
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void addRepetition (boolean typeAddSubtrack)
    {
        int progressAdd=0;
        int repetition=0;
        if (typeAddSubtrack) {
            repetition = cursorPractice.getInt(PracticeAdapter.REPETITION_ID);
        } else
        {
            repetition = -1* cursorPractice.getInt(PracticeAdapter.REPETITION_ID);
        }
        progressAdd = cursorPractice.getInt(PracticeAdapter.PROGRESS_ID) + repetition ;

        SQLHelper.updatePractice(db,cursorPractice.getInt(0),progressAdd);

        SQLHelper.insertHistory(db,cursorPractice.getInt(0),progressAdd,new Date().getTime(), repetition);


        cursorPractice =SQLHelper.getCursorPractice(db);
        cursorPractice.moveToPosition(position);
        viewPractice(getEditMode());

    }

    void editionEnd ()
    {
        EditText practiceNameEdit = (EditText) getActivity().findViewById(R.id.practice_name_edit);
        EditText practiceStatusProgressEdit = (EditText) getActivity().findViewById(R.id.practice_status_progress_edit);
        EditText practiceStatusMaxRepetitonEdit = (EditText) getActivity().findViewById(R.id.practice_status_maxrepetition_edit);
        EditText practiceRepetitionEdit = (EditText) getActivity().findViewById(R.id.practice_repetition_edit);
        EditText practiceDescriptionEdit = (EditText) getActivity().findViewById(R.id.practice_description_edit);

        SQLHelper.updatePracticeAll (db, cursorPractice.getInt(0), practiceNameEdit.getText().toString(),
                practiceDescriptionEdit.getText().toString(), practiceStatusProgressEdit.getText().toString(),
                practiceStatusMaxRepetitonEdit.getText().toString(), practiceRepetitionEdit.getText().toString());

        String progress = practiceStatusProgressEdit.getText().toString();
        int progressAdd = 0;
        if (!progress.equals("")) progressAdd = Integer.parseInt(progress);
        int progressOld = cursorPractice.getInt(PracticeAdapter.PROGRESS_ID);
        if (progressAdd!=progressOld) {
            int repetition = progressAdd - progressOld;
            SQLHelper.insertHistory(db, cursorPractice.getInt(0), progressAdd, new Date().getTime(), repetition);
        }
        cursorPractice= SQLHelper.getCursorPractice(db);
        cursorPractice.moveToPosition(position);

    }

    public void setEdit()
    {
        if (getEditMode() ==STANDARD_VIEW) {
            editMode = EDIT_VIEW;
        } else
        {
            editMode=STANDARD_VIEW;
            editionEnd();
        }
        viewPractice(getEditMode());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position",position);
    }

    @Override
    public void onDestroy() {
        if (cursorPractice!=null) cursorPractice.close();
        if (db!=null) db.close();
        super.onDestroy();
    }

    public int getEditMode() {
        return editMode;
    }

    public String getPracticeName(){
        String name="";
        if (cursorPractice!=null)
        {
            cursorPractice.moveToPosition(position);
            name = cursorPractice.getString(PracticeAdapter.NAME_ID);
        }
        return name;
    }
}
