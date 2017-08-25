package iso.piotrowski.marek.nyndro.tools;

import iso.piotrowski.marek.nyndro.R;

/**
 * Created by marek.piotrowski on 25/08/2017.
 */

public class DrawableMapper {

    public enum TypeOfImage {
        s_refuge(0),
        refuge(1),
        vajrasattva(2),
        amitabha(3),
        mandala(4),
        guru_yoga(5),
        z_chenrizg(6),
        mahakala(7);

        private int value;

        TypeOfImage(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    private static int drawable[] = {
            R.drawable.s_refuge,
            R.drawable.refuge,
            R.drawable.vajrasattva,
            R.drawable.amitabha,
            R.drawable.mandala,
            R.drawable.guru_yoga,
            R.drawable.z_chenrizg,
            R.drawable.mahakala};

    public static int getDrawableId(TypeOfImage typeOfImage) {
        return drawable[typeOfImage.getValue()];
    }
}
