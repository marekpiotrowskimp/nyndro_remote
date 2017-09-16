package iso.piotrowski.marek.nyndro.plans.PlansList;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.R;

/**
 * Created by Marek on 10.08.2016.
 */
public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.PlansHolder> {

    private OnRemainderItemClickListener onRemainderItemClickListener;
    private List<ReminderModel> reminderList;
    private String[] typeOfRepeater = NyndroApp.getContext().getResources().getStringArray(R.array.frequents);

    public void setOnRemainderItemClickListener(OnRemainderItemClickListener onRemainderItemClickListener) {
        this.onRemainderItemClickListener = onRemainderItemClickListener;
    }

    public interface OnRemainderItemClickListener{
        void OnClick(View view, int position, ReminderModel reminder);
    }

    PlansAdapter(List<ReminderModel> reminderList) {
        this.reminderList = reminderList;
    }

    public class PlansHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.plans_image) ImageView plansPracticeImageId;
        @BindView(R.id.plans_practice_name) TextView plansPracticeName;
        @BindView(R.id.plans_date) TextView plansDate;
        @BindView(R.id.autorenew_image) ImageView repeaterImage;
        @BindView(R.id.autorenew_text) TextView repeaterName;
        CardView cardView;
        private ReminderModel reminder;
        PlansHolder (CardView cardView){
            super(cardView);
            this.cardView = cardView;
            ButterKnife.bind(this, this.cardView);
        }

        public ReminderModel getReminder() {
            return reminder;
        }

        public void setReminder(ReminderModel reminder) {
            this.reminder = reminder;
        }
    }

    @Override
    public PlansHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_plans_cardview,parent,false);
        return new PlansHolder(cardView);
    }

    @Override
    public void onBindViewHolder(PlansHolder holder, final int position) {
        ReminderModel reminder = reminderList.get(position);
        holder.setReminder(reminder);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(reminder.getPracticeDate());
        holder.plansDate.setText(String.format("Time: %tT", calendar));

        holder.plansPracticeImageId.setImageDrawable(NyndroApp.getContext().getResources().getDrawable(reminder.getPractice().getPracticeImageId()));
        holder.plansPracticeName.setText(reminder.getPractice().getName());
        holder.repeaterName.setText(typeOfRepeater[reminder.getRepeater()]);
        holder.repeaterImage.setVisibility(reminder.getRepeater() > 0 ? View.VISIBLE : View.INVISIBLE);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onRemainderItemClickListener!=null){
                    onRemainderItemClickListener.OnClick(view, position, reminder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

}
