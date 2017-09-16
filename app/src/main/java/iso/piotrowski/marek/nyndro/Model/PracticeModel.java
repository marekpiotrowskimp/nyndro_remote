package iso.piotrowski.marek.nyndro.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import iso.piotrowski.marek.nyndro.tools.DrawableMapper;

/**
 * Created by marek.piotrowski on 06/08/2017.
 */

@Table(name = "PRACTICE", id = "_id")
public class PracticeModel extends Model {

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

    public PracticeModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PracticeModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getPracticeImageId() {
        return DrawableMapper.getDrawableId(DrawableMapper.TypeOfImage.values()[practiceImageId]);
    }
    public Integer getRawPracticeImageId() {
        return practiceImageId;
    }

    public PracticeModel setPracticeImageId(Integer practiceImageId) {
        this.practiceImageId = practiceImageId;
        return this;
    }

    public Integer getProgress() {
        return progress;
    }

    public PracticeModel setProgress(Integer progress) {
        this.progress = progress;
        return this;
    }

    public Integer getMaxRepetition() {
        return maxRepetition;
    }

    public PracticeModel setMaxRepetition(Integer maxRepetition) {
        this.maxRepetition = maxRepetition;
        return this;
    }

    public Integer getRepetition() {
        return repetition;
    }

    public PracticeModel setRepetition(Integer repetition) {
        this.repetition = repetition;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public PracticeModel setActive(boolean active) {
        this.active = active;
        return this;
    }

    public long getID() {
        return super.getId();
    }
}

