package iso.piotrowski.marek.nyndro.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by marek.piotrowski on 07/08/2017.
 */

@Table(name = "HISTORY", id = "_id")
public class HistoryModel extends Model {

    @Column(name = "PRACTICE_ID")
    private long practiceId;
    @Column(name = "PROGRESS")
    private Integer progress;
    @Column(name = "PRACTICE_DATE")
    private long practiceData;
    @Column(name = "REPETITION")
    private Integer repetition;
    @Column(name = "ACTIVE")
    private boolean active;
    @Column(name = "PRACTICE")
    private PracticeModel practice;

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

    public long getPracticeId() {
        return practiceId;
    }

    public HistoryModel setPracticeId(long practiceId) {
        this.practiceId = practiceId;
        return this;
    }

    public Integer getProgress() {
        return progress;
    }

    public HistoryModel setProgress(Integer progress) {
        this.progress = progress;
        return this;
    }

    public long getPracticeData() {
        return practiceData;
    }

    public HistoryModel setPracticeData(long practiceData) {
        this.practiceData = practiceData;
        return this;
    }

    public Integer getRepetition() {
        return repetition;
    }

    public HistoryModel setRepetition(Integer repetition) {
        this.repetition = repetition;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public HistoryModel setActive(boolean active) {
        this.active = active;
        return this;
    }

    public long getID() {
        return super.getId();
    }

    public PracticeModel getPractice() {
        return practice;
    }

    public HistoryModel setPractice(PracticeModel practice) {
        this.practice = practice;
        return this;
    }
}
