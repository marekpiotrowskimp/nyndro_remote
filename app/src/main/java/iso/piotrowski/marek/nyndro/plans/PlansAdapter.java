package iso.piotrowski.marek.nyndro.plans;

import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import iso.piotrowski.marek.nyndro.R;

/**
 * Created by Marek on 10.08.2016.
 */
public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.PlansHolder> {
    public static final int REMAINDER_ID=0;
    public static final int REMAINDER_PRACTICE_ID=1;
    public static final int REMAINDER_DATE=2;
    public static final int REMAINDER_REPETITON=3;
    public static final int REMAINDER_DONE=4;
    public static final int REMAINDER_REPEATER=5;
    public static final int REMAINDER_PRACTICE_NAME=6;
    public static final int REMAINDER_PRACTICE_IMAGE_ID=7;
    public static final int REMAINDER_PRACTICE_MAX_REPETITION=8;

    
    private Cursor cursorPlans;
    private OnRemainderItemClickListener onRemainderItemClickListener;

    public void setOnRemainderItemClickListener(OnRemainderItemClickListener onRemainderItemClickListener) {
        this.onRemainderItemClickListener = onRemainderItemClickListener;
    }

    public static interface OnRemainderItemClickListener{
        public void OnClick(View view, int position);
    }

    public void setCursorPlans(Cursor cursorPlans) {
        this.cursorPlans = cursorPlans;
    }

    public class PlansHolder extends RecyclerView.ViewHolder{
        CardView cv;
        PlansHolder (CardView cv){
            super(cv);
            this.cv=cv;
        }
    }
    
    @Override
    public PlansHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_plans_cardview,parent,false);
        return new PlansHolder(cv);
    }


    @Override
    public void onBindViewHolder(PlansHolder holder, final int position) {
        ImageView plansPracticeImageId = (ImageView)holder.cv.findViewById(R.id.plans_image);
        TextView plansPracticeName = (TextView) holder.cv.findViewById(R.id.plans_practice_name);
        TextView plansDate = (TextView) holder.cv.findViewById(R.id.plans_date);

        ImageView repeaterImage = (ImageView)holder.cv.findViewById(R.id.autorenew_image);
        TextView repeaterName = (TextView) holder.cv.findViewById(R.id.autorenew_text);


        cursorPlans.moveToPosition(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cursorPlans.getLong(REMAINDER_DATE));
        plansDate.setText(String.format("Data : %tD %tT",calendar,calendar));

        plansPracticeImageId.setImageDrawable(holder.cv.getResources().getDrawable(cursorPlans.getInt(REMAINDER_PRACTICE_IMAGE_ID)));
        plansPracticeName.setText(cursorPlans.getString(REMAINDER_PRACTICE_NAME));

        switch (cursorPlans.getInt(REMAINDER_REPEATER))
        {
            case 0:
                repeaterImage.setVisibility(View.INVISIBLE);
                repeaterName.setText("");
                break;
            case 1:
                repeaterImage.setVisibility(View.VISIBLE);
                repeaterName.setText("D");
                break;
            case 2:
                repeaterImage.setVisibility(View.VISIBLE);
                repeaterName.setText("W");
                break;
            case 3:
                repeaterImage.setVisibility(View.VISIBLE);
                repeaterName.setText("M");
                break;
        }

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRemainderItemClickListener!=null){
                    onRemainderItemClickListener.OnClick(view,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursorPlans.getCount();
    }

}
