package iso.piotrowski.marek.nyndro.DataSource.ConstantsData;

import android.support.annotation.NonNull;

import java.util.Date;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.PracticeMain.MainPracticeActivity;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.tools.DrawableMapper;

/**
 * Created by Marek on 22.07.2016.
 */
public class Practice {
    private String name;
    private String description;
    private int imageResourcesId;
    private int progress;
    private int maxRepetition;
    private int repetition;


    public static final Practice[] practices = {
            new Practice(getResourcesString(R.string.s_refuge), DrawableMapper.TypeOfImage.s_refuge.getValue() ,11000,
                    0, getResourcesString(R.string.s_refuge_desc), 108),
            new Practice(getResourcesString(R.string.refuge), DrawableMapper.TypeOfImage.refuge.getValue(),111111,
                    0, getResourcesString(R.string.refuge_desc), 108),
            new Practice(getResourcesString(R.string.vajrasattva), DrawableMapper.TypeOfImage.vajrasattva.getValue(),111111,
                    0, getResourcesString(R.string.vajrasattva_desc), 108),
            new Practice(getResourcesString(R.string.amitabha),DrawableMapper.TypeOfImage.amitabha.getValue(),100000,
                    0, getResourcesString(R.string.amitabha_desc), 108),
            new Practice(getResourcesString(R.string.mandala),DrawableMapper.TypeOfImage.mandala.getValue(),111111,
                    0, getResourcesString(R.string.mandala_desc), 108),
            new Practice(getResourcesString(R.string.guru_yoga),DrawableMapper.TypeOfImage.guru_yoga.getValue(),111111,
                    0, getResourcesString(R.string.guru_yoga_desc), 108),
            new Practice(getResourcesString(R.string.chenrizg),DrawableMapper.TypeOfImage.z_chenrizg.getValue(),111111,
                    0, getResourcesString(R.string.chenrizg_desc), 108),
            new Practice(getResourcesString(R.string.other_practice),DrawableMapper.TypeOfImage.mahakala.getValue(),111111,
                    0, getResourcesString(R.string.other_practice_desc), 108),
    };

    @NonNull
    private static String getResourcesString(int s_refuge) {
        return NyndroApp.getContect().getResources().getString(s_refuge);
    }

    Practice (String name, int imageResourcesId, int maxRepetition){
        this.name=name;
        this.imageResourcesId=imageResourcesId;
        this.maxRepetition=maxRepetition;
    }

    Practice (String name, int imageResourcesId, int maxRepetition, int progress, String description, int repetition){
        this(name,imageResourcesId,maxRepetition);
        this.description = description;
        this.progress = progress;
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

    public int getRepetition() {
        return repetition;
    }

    @Override
    public String toString() {
        return name;
    }
}
