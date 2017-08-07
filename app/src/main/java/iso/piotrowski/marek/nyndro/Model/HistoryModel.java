package iso.piotrowski.marek.nyndro.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by marek.piotrowski on 07/08/2017.
 */

@Table(name = "HISTORY", id = "_id")
public class HistoryModel extends Model {

    @Column(name = "_id", unique = true, onUniqueConflict = Column.ConflictAction.ABORT)
    private Integer _id;
    @Column(name = "PRACTICE_ID")
    private Integer practiceId;
    @Column(name = "PROGRESS")
    private Integer progress;
    @Column(name = "PRACTICE_DATE")
    private Integer practiceData;
    @Column(name = "REPETITION")
    private Integer repetition;
    @Column(name = "ACTIVE")
    private boolean active;

    public HistoryModel(){
        super();
    }

    public HistoryModel(Integer practiceId, Integer progress, Integer practiceData, Integer repetition, boolean active){
        super();
        this.setPracticeId(practiceId);
        this.setProgress(progress);
        this.setPracticeData(practiceData);
        this.setRepetition(repetition);
        this.setActive(active);
    }

    public Integer getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(Integer practiceId) {
        this.practiceId = practiceId;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getPracticeData() {
        return practiceData;
    }

    public void setPracticeData(Integer practiceData) {
        this.practiceData = practiceData;
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

    public Integer getID() {
        return _id;
    }
}
