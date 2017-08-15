package iso.piotrowski.marek.nyndro.practice.Details;


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

import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.DataSource.DataSource;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.UIComponents.BuddaProgressBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PracticeDetailFragment extends Fragment implements PracticeDetailContract.IViewer{

    public static final int STANDARD_VIEW = 1;
    public static final int EDIT_VIEW = 2;
    private static String PRACTICE_ID = "practice_id";
    private int editMode = STANDARD_VIEW;

    private PracticeModel practice;
    private PracticeDetailContract.IPresenter presenter;
    private long practiceId;

    @BindView(R.id.practice_image_detail) ImageView practiceImage;
    @BindView(R.id.practice_name_detail) TextView practiceName;
    @BindView(R.id.practice_status_progress_detail) TextView practiceStatusProgress;
    @BindView(R.id.practice_status_maxrepetition_detail) TextView practiceStatusMaxRepetiton;
    @BindView(R.id.practice_repetition_detail) TextView practiceRepetition;
    @BindView(R.id.practice_date_last_detail) TextView practiceDateLast;
    @BindView(R.id.practice_date_next_detail) TextView practiceDateNext;
    @BindView(R.id.practice_description_detail) TextView practiceDescription;
    @BindView(R.id.practice_progress_detail) BuddaProgressBar practiceProgress;
    @BindView(R.id.practice_name_edit) EditText practiceNameEdit;
    @BindView(R.id.practice_status_progress_edit) EditText practiceStatusProgressEdit;
    @BindView(R.id.practice_status_maxrepetition_edit) EditText practiceStatusMaxRepetitonEdit;
    @BindView(R.id.practice_repetition_edit) EditText practiceRepetitionEdit;
    @BindView(R.id.practice_description_edit) EditText practiceDescriptionEdit;


    public PracticeDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new PracticeDetailPresenter(this, DataSource.getInstance());
        View practiceDetailView =inflater.inflate(R.layout.fragment_practice_detail, container, false);
        if (savedInstanceState!=null)
        {
            practiceId  = savedInstanceState.getLong(PRACTICE_ID);
            presenter.loadPracticeData(practiceId);
        }
        setHasOptionsMenu(true);
        ButterKnife.bind(this, practiceDetailView);
        return practiceDetailView;
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
        viewPractice(getEditMode());
    }

    private void viewPractice (int typeViewPractice)
    {
        practiceImage.setContentDescription(practice.getName());
        practiceImage.setImageDrawable(NyndroApp.getContect().getResources().getDrawable(practice.getPracticeImageId()));
        practiceProgress.setMaxProgress(practice.getMaxRepetition());
        practiceProgress.setProgress(practice.getProgress());


        Calendar calendar = Calendar.getInstance();
        long lastDate = presenter.getLastHistoryOfPractice(practice.getID());
        if (lastDate == -1) {
            practiceDateLast.setText(String.format("%s %s", NyndroApp.getContect().getResources().getString(R.string.last_practice_date),
                    NyndroApp.getContect().getResources().getString(R.string.NoDateToShow)));
        } else {
            calendar.setTimeInMillis(lastDate);
            practiceDateLast.setText(String.format("%s %s", NyndroApp.getContect().getResources().getString(R.string.last_practice_date),
                    String.format(" %tD", calendar)));
        }
        long nextDate = presenter.getNextPlanedOfPractice(practice.getID());
        if (nextDate == -1) {
            practiceDateNext.setText(String.format("%s %s", NyndroApp.getContect().getResources().getString(R.string.next_practice_date),
                    NyndroApp.getContect().getResources().getString(R.string.NoDateToShow)));
        } else{
            calendar.setTimeInMillis(nextDate);
            practiceDateNext.setText(String.format("%s %s", NyndroApp.getContect().getResources().getString(R.string.next_practice_date),
                    String.format(" %tD", calendar)));
        }

        if (typeViewPractice==STANDARD_VIEW) {
            practiceName.setText(getFormatTextWithPracticeData(R.string.name_practice, practice.getName()));
            practiceStatusProgress.setText(getFormatTextWithPracticeData(R.string.progress_name, String.valueOf(practice.getProgress())));
            practiceStatusMaxRepetiton.setText(getFormatTextWithPracticeData(R.string.max_repetition_name, String.valueOf(practice.getMaxRepetition())));
            practiceRepetition.setText(getFormatTextWithPracticeData(R.string.repetiton_name, String.valueOf(practice.getRepetition())));
            practiceDescription.setText(getFormatTextWithPracticeData(R.string.description_name, practice.getDescription()));

            setEditVisibility(View.INVISIBLE);

            practiceNameEdit.setText("");
            practiceStatusProgressEdit.setText("");
            practiceStatusMaxRepetitonEdit.setText("");
            practiceRepetitionEdit.setText("");
            practiceDescriptionEdit.setText("");

        }

        if (typeViewPractice==EDIT_VIEW) {
            setEditVisibility(View.VISIBLE);

            practiceName.setText(getFormatEditFromResources(R.string.name_practice));
            practiceNameEdit.setText(practice.getName());

            practiceStatusProgress.setText(getFormatEditFromResources(R.string.progress_name));
            practiceStatusProgressEdit.setText(String.valueOf(practice.getProgress()));

            practiceStatusMaxRepetiton.setText(getFormatEditFromResources(R.string.max_repetition_name));
            practiceStatusMaxRepetitonEdit.setText(String.valueOf(practice.getMaxRepetition()));

            practiceRepetition.setText(getFormatEditFromResources(R.string.repetiton_name));
            practiceRepetitionEdit.setText(String.valueOf(practice.getRepetition()));

            practiceDescription.setText(getFormatEditFromResources(R.string.description_name));
            practiceDescriptionEdit.setText(practice.getDescription());
        }
    }

    private String getFormatTextWithPracticeData(int resourcesId, String practiceData) {
        return String.format("%s%s", getFormatEditFromResources(resourcesId),
                practiceData);
    }

    private void setEditVisibility(int visible) {
        practiceNameEdit.setVisibility(visible);
        practiceStatusProgressEdit.setVisibility(visible);
        practiceStatusMaxRepetitonEdit.setVisibility(visible);
        practiceRepetitionEdit.setVisibility(visible);
        practiceDescriptionEdit.setVisibility(visible);
    }

    private String getFormatEditFromResources(int resourcesId) {
        return String.format("%s ", NyndroApp.getContect().getResources().getText(resourcesId));
    }

    @Override
    public void setPractice(PracticeModel practice) {
        this.practice = practice;
        this.practiceId = practice.getID();
    }

    @Override
    public void showPractice() {
        viewPractice(getEditMode());
    }

    public void addRepetition (boolean typeAddSubtrack)
    {
        int repetition = practice.getProgress() * (typeAddSubtrack ? 1 : -1);
        presenter.addProgressToPractice(practice, repetition);
        presenter.addHistoryForPractice(practice, repetition);
        viewPractice(getEditMode());
    }

    void editionEnd ()
    {
        int oldProgress = practice.getProgress();
        presenter.updatePractice(practice.setName(practiceNameEdit.getText().toString())
                .setDescription(practiceDescriptionEdit.getText().toString())
                .setProgress(getIntegerValue(practiceStatusProgressEdit, practice.getProgress()))
                .setMaxRepetition(getIntegerValue(practiceStatusMaxRepetitonEdit, practice.getMaxRepetition()))
                .setRepetition(getIntegerValue(practiceRepetitionEdit, practice.getRepetition())));
        int repetition = practice.getProgress() - oldProgress;
        presenter.addHistoryForPractice(practice, repetition);
        viewPractice(getEditMode());
    }

    private Integer getIntegerValue(EditText fromEdit, Integer oldValue) {
        String textValue = fromEdit.getText().toString();
        return textValue.isEmpty() ? oldValue : Integer.valueOf(textValue);
    }

    public void setEdit()
    {
        if (getEditMode() == STANDARD_VIEW) {
            editMode = EDIT_VIEW;
        } else {
            editMode=STANDARD_VIEW;
            editionEnd();
        }
        viewPractice(getEditMode());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(PRACTICE_ID, practiceId);
    }

    public int getEditMode() {
        return editMode;
    }

    public String getPracticeName(){
        return practice.getName();
    }

    @Override
    public void setPresenter(PracticeDetailContract.IPresenter presenter) {
        this.presenter = presenter;
    }
}
