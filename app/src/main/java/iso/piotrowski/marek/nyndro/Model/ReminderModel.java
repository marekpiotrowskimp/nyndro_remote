package iso.piotrowski.marek.nyndro.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by marek.piotrowski on 07/08/2017.
 */
@Table(name = "REMAINDER", id = "_id")
public class ReminderModel extends Model {

    @Column(name = "PRACTICE_ID")
    private long practiceId;
    @Column(name = "PRACTICE_DATE")
    private long practiceDate;
    @Column(name = "REPETITION")
    private Integer repetition;
    @Column(name = "ACTIVE")
    private boolean active;
    @Column(name = "REPEATER")
    private Integer repeater;
    @Column(name = "PRACTICE")
    private PracticeModel practice;

    public ReminderModel(){
        super();
    }

    public ReminderModel(Integer practiceId, Integer practiceDate, Integer repetition, boolean active, Integer repeater) {
        super();
        this.setPracticeId(practiceId);
        this.setPracticeDate(practiceDate);
        this.setRepetition(repetition);
        this.setActive(active);
        this.setRepeater(repeater);
    }

    public long getPracticeId() {
        return practiceId;
    }

    public ReminderModel setPracticeId(long practiceId) {
        this.practiceId = practiceId;
        return this;
    }

    public long getPracticeDate() {
        return practiceDate;
    }

    public ReminderModel setPracticeDate(long practiceDate) {
        this.practiceDate = practiceDate;
        return this;
    }

    public Integer getRepetition() {
        return repetition;
    }

    public ReminderModel setRepetition(Integer repetition) {
        this.repetition = repetition;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public ReminderModel setActive(boolean active) {
        this.active = active;
        return this;
    }

    public Integer getRepeater() {
        return repeater;
    }

    public ReminderModel setRepeater(Integer repeater) {
        this.repeater = repeater;
        return this;
    }

    public long getID() {
        return super.getId();
    }

    public PracticeModel getPractice() {
        return practice;
    }

    public ReminderModel setPractice(PracticeModel practice) {
        this.practice = practice;
        return this;
    }
}
