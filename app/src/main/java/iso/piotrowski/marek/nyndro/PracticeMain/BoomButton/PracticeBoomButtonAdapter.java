package iso.piotrowski.marek.nyndro.PracticeMain.BoomButton;

import iso.piotrowski.marek.nyndro.DataSource.ConstantsData.Practice;
import iso.piotrowski.marek.nyndro.tools.DrawableMapper;

/**
 * Created by marek.piotrowski on 05/09/2017.
 */

public class PracticeBoomButtonAdapter implements IBoomButtonAdapter {

    @Override
    public int getSize() {
        return Practice.practices.length;
    }

    @Override
    public int getIconResourcesId(int position) {
        return DrawableMapper.getDrawableId(DrawableMapper.TypeOfImage.values()[Practice.practices[position].getImageResourcesId()]);
    }

    @Override
    public String getName(int position) {
        return Practice.practices[position].getName();
    }
}
