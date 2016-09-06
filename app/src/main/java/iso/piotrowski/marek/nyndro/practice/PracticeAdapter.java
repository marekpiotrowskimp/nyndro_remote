package iso.piotrowski.marek.nyndro.practice;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;

import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.tools.SQLHelper;

/**
 * Created by Marek on 25.07.2016.
 * Adapter to practices
 */

public class PracticeAdapter extends RecyclerView.Adapter<PracticeAdapter.ViewPracticeHolder> {

    public static final int _ID = 0;
    public static final int NAME_ID = 1;
    public static final int DESCRIPTION_ID = 2;
    public static final int PRACTICE_IMAGE_ID_ID = 3;
    public static final int PROGRESS_ID = 4;
    public static final int MAX_REPETITION_ID = 5;
    public static final int REPETITION_ID = 6;
    public static final int ACTIVE_ID = 7;

    public static final int STANDARD_TYPE = 1;
    public static final int END_TYPE = 2;

    private SQLiteDatabase db;
    private Cursor cursorPractices;
    private CardViewListener cardViewListener=null;
    private CardViewListener imageButtonListener=null;

    public void setCardViewListener(CardViewListener cardViewListener) {
        this.cardViewListener = cardViewListener;
    }

    public void setImageButtonListener(CardViewListener imageButtonListener) {
        this.imageButtonListener = imageButtonListener;
    }

    public interface CardViewListener {
         void onClick (View view, int position);
    }


    public PracticeAdapter (Cursor cursorPractices, SQLiteDatabase db){
        this.cursorPractices = cursorPractices;
        this.db=db;
    }

    public static class ViewPracticeHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewPracticeHolder (CardView cv){
            super(cv);
            cardView =cv;
        }

    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (cursorPractices.getCount()>position) {
            type = STANDARD_TYPE;
        } else {
            type = END_TYPE;
        }
        return type;
    }

    @Override
    public ViewPracticeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv;
        switch (viewType) {
            case STANDARD_TYPE:
                cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_practice, parent, false);
                break;
            case END_TYPE:
                cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_practice_blank, parent, false);
                break;
            default:
                cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_practice, parent, false);
        }
        return new ViewPracticeHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewPracticeHolder holder, final int position) {
        CardView cv = holder.cardView;
        if (getItemViewType(position)==STANDARD_TYPE) {
            ImageView practiceImage = (ImageView) cv.findViewById(R.id.practice_image);
            TextView practiceName = (TextView) cv.findViewById(R.id.practice_name);
            TextView practiceStatus = (TextView) cv.findViewById(R.id.practice_status);
            TextView practiceRepetition = (TextView) cv.findViewById(R.id.practice_repetition);
            TextView practiceDateLast = (TextView) cv.findViewById(R.id.practice_date_last);
            TextView practiceDateNext = (TextView) cv.findViewById(R.id.practice_date_next);
            TextView practiceDescription = (TextView) cv.findViewById(R.id.practice_description);
            ImageButton practiceRapetitionAdd = (ImageButton) cv.findViewById(R.id.practice_repetition_add);
            ProgressBar practiceProgress = (ProgressBar) cv.findViewById(R.id.practice_progress);

            if (cursorPractices.moveToPosition(position)) {
                practiceImage.setContentDescription(cursorPractices.getString(NAME_ID));
                practiceImage.setImageDrawable(cv.getResources().getDrawable(cursorPractices.getInt(PRACTICE_IMAGE_ID_ID)));

                String name = cursorPractices.getString(NAME_ID);
                name = name.replace("\n", " ");
//                name = name.substring(0, name.length() <= 32 ? name.length() : 32);
//                name += name.length() < 32 ? "" : "...";

                practiceName.setText(name); //cv.getResources().getText(R.string.name_practice) +" "+
                practiceProgress.setMax(cursorPractices.getInt(MAX_REPETITION_ID));
                practiceProgress.setProgress(cursorPractices.getInt(PROGRESS_ID));
                practiceStatus.setText(String.valueOf(cursorPractices.getInt(PROGRESS_ID)) + " / " + String.valueOf(cursorPractices.getInt(MAX_REPETITION_ID)));
                practiceRepetition.setText(String.valueOf(cursorPractices.getInt(REPETITION_ID)));
                String description = cursorPractices.getString(DESCRIPTION_ID);
                description = description.replace("\n", " ");
//                description = description.substring(0, description.length() <= 45 ? description.length() : 45);
//                description += description.length() < 45 ? "" : "...";
                practiceDescription.setText(description);

                Calendar calendar = Calendar.getInstance();
                long lastDate = SQLHelper.lastPractice(db, cursorPractices.getInt(_ID));
                if (lastDate == -1) {
                    practiceDateLast.setText(cv.getResources().getString(R.string.last_practice_date) + " -----------");
                } else {
                    calendar.setTimeInMillis(lastDate); //cursorPractices.getLong(LAST_PRACTICE_DATE_ID));
                    practiceDateLast.setText(cv.getResources().getString(R.string.last_practice_date) + String.format(" %tD", calendar));
                }
                long nextDate = SQLHelper.nextPractice(db, cursorPractices.getInt(_ID));
                if (nextDate == -1) {
                    practiceDateNext.setText(cv.getResources().getString(R.string.next_practice_date) + " -----------");
                } else{
                    calendar.setTimeInMillis(nextDate);  //cursorPractices.getLong(NEXT_PRACTICE_DATE_ID));
                    practiceDateNext.setText(cv.getResources().getString(R.string.next_practice_date) + String.format(" %tD", calendar));
                }

                cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cardViewListener != null) {
                            cardViewListener.onClick(view, position);
                        }
                    }
                });

                practiceRapetitionAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (imageButtonListener != null) {
                            imageButtonListener.onClick(view, position);
                        }

                    }
                });

            }
        }

    }

    @Override
    public int getItemCount() {
        return cursorPractices.getCount()+1;
    }
}
