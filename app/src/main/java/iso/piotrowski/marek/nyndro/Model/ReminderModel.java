package iso.piotrowski.marek.nyndro.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by marek.piotrowski on 07/08/2017.
 */
@Table(name = "REMAINDER", id = "_id")
public class ReminderModel extends Model {
    @Column(name = "_id", unique = true, onUniqueConflict = Column.ConflictAction.ABORT)
    private Integer _id;
    @Column(name = "PRACTICE_ID")
    private Integer practiceId;
    @Column(name = "PRACTICE_DATE")
    private Integer practiceDate;
    @Column(name = "REPETITION")
    private Integer repetition;
    @Column(name = "ACTIVE")
    private boolean active;
    @Column(name = "REPEATER")
    private Integer repeater;

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

    public Integer getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(Integer practiceId) {
        this.practiceId = practiceId;
    }

    public Integer getPracticeDate() {
        return practiceDate;
    }

    public void setPracticeDate(Integer practiceDate) {
        this.practiceDate = practiceDate;
    }

    public Integer getRepetition() {
        return repetition;
    }

    public void setRepetition(Integer repetition) {
        this.repetition = repetition;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getRepeater() {
        return repeater;
    }

    public void setRepeater(Integer repeater) {
        this.repeater = repeater;
    }

    public Integer getID() {
        return _id;
    }
}
