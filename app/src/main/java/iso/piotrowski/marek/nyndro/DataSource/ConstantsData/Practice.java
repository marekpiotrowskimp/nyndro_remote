package iso.piotrowski.marek.nyndro.DataSource.ConstantsData;

import java.util.Date;

import iso.piotrowski.marek.nyndro.MainPracticsActivity;
import iso.piotrowski.marek.nyndro.R;

/**
 * Created by Marek on 22.07.2016.
 */
public class Practice {
    private String name;
    private String description;
    private int imageResourcesId;
    private int progress;
    private int maxRepetition;
    private long lastPracticeDate;
    private long nextPracticeDate;
    private int repetition;


    public static final Practice practices[] = {
            new Practice(MainPracticsActivity.resourcesApp.getString(R.string.s_refuge), R.drawable.s_refuge,11000,
                    0, MainPracticsActivity.resourcesApp.getString(R.string.s_refuge_desc), new Date().getTime(), new Date().getTime(), 108),
            new Practice(MainPracticsActivity.resourcesApp.getString(R.string.refuge), R.drawable.refuge,111111,
                    0, MainPracticsActivity.resourcesApp.getString(R.string.refuge_desc), new Date().getTime(), new Date().getTime(), 108),
            new Practice(MainPracticsActivity.resourcesApp.getString(R.string.vajrasattva), R.drawable.vajrasattva,111111,
                    0, MainPracticsActivity.resourcesApp.getString(R.string.vajrasattva_desc), new Date().getTime(), new Date().getTime(), 108),
            new Practice(MainPracticsActivity.resourcesApp.getString(R.string.amitabha),R.drawable.amitabha,100000,
                    0, MainPracticsActivity.resourcesApp.getString(R.string.amitabha_desc), new Date().getTime(), new Date().getTime(), 108),
            new Practice(MainPracticsActivity.resourcesApp.getString(R.string.mandala),R.drawable.mandala,111111,
                    0, MainPracticsActivity.resourcesApp.getString(R.string.mandala_desc), new Date().getTime(), new Date().getTime(), 108),
            new Practice(MainPracticsActivity.resourcesApp.getString(R.string.guru_yoga),R.drawable.guru_yoga,111111,
                    0, MainPracticsActivity.resourcesApp.getString(R.string.guru_yoga_desc), new Date().getTime(), new Date().getTime(), 108),
            new Practice(MainPracticsActivity.resourcesApp.getString(R.string.chenrizg),R.drawable.z_chenrizg,111111,
                    0, MainPracticsActivity.resourcesApp.getString(R.string.chenrizg_desc), new Date().getTime(), new Date().getTime(), 108),
            new Practice(MainPracticsActivity.resourcesApp.getString(R.string.other_practice),R.drawable.mahakala,111111,
                    0, MainPracticsActivity.resourcesApp.getString(R.string.other_practice_desc), new Date().getTime(), new Date().getTime(), 108),
    };

    Practice (String name, int imageResourcesId, int maxRepetition){
        this.name=name;
        this.imageResourcesId=imageResourcesId;
        this.maxRepetition=maxRepetition;
    }

    Practice (String name, int imageResourcesId, int maxRepetition, int progress, String description, long lastPracticeDate, long nextPracticeDate, int repetition){
        this(name,imageResourcesId,maxRepetition);
        this.description = description;
        this.progress = progress;
        this.lastPracticeDate = lastPracticeDate;
        this.nextPracticeDate=nextPracticeDate;
        this.repetition = repetition;
    }

    public String getName() {
        return name;
    }

    public int getImageResourcesId() {
        return imageResourcesId;
    }

    public int getProgress() {
        return progress;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxRepetition() {
        return maxRepetition;
    }

    public long getLastPracticeDate() {
        return lastPracticeDate;
    }

    public long getNextPracticeDate() {
        return nextPracticeDate;
    }

    public int getRepetition() {
        return repetition;
    }

    @Override
    public String toString() {
        return name;
    }
}
