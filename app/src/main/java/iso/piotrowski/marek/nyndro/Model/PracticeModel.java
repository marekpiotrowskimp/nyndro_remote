package iso.piotrowski.marek.nyndro.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by marek.piotrowski on 06/08/2017.
 */

@Table(name = "PRACTICE", id = "_id")
public class PracticeModel extends Model {
    @Column(name = "_id", unique = true, onUniqueConflict = Column.ConflictAction.ABORT)
    private Integer _id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PRACTICE_IMAGE_ID")
    private Integer practiceImageId;
    @Column(name = "PROGRESS")
    private Integer progress;
    @Column(name = "MAX_REPETITION")
    private Integer maxRepetition;
    @Column(name = "REPETITION")
    private Integer repetition;
    @Column(name = "ACTIVE")
    private boolean active;

    public PracticeModel(){
        super();
    }

    public PracticeModel(String name, String description, Integer practiceImageId, Integer progress, Integer maxRepetition, Integer repetition, boolean active) {
        super();
        this.setName(name);
        this.setDescription(description);
        this.setPracticeImageId(practiceImageId);
        this.setProgress(progress);
        this.setMaxRepetition(maxRepetition);
        this.setRepetition(repetition);
        this.setActive(active);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPracticeImageId() {
        return practiceImageId;
    }

    public void setPracticeImageId(Integer practiceImageId) {
        this.practiceImageId = practiceImageId;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getMaxRepetition() {
        return maxRepetition;
    }

    public void setMaxRepetition(Integer maxRepetition) {
        this.maxRepetition = maxRepetition;
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

