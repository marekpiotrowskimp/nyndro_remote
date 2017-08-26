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

    private static int drawablePractice[] = {
            R.drawable.s_refuge,
            R.drawable.refuge,
            R.drawable.vajrasattva,
            R.drawable.amitabha,
            R.drawable.mandala,
            R.drawable.guru_yoga,
            R.drawable.z_chenrizg,
            R.drawable.mahakala};

    private static int drawablePracticeLarge[] = {
            R.drawable.s_refuge_large,
            R.drawable.refuge_large,
            R.drawable.vajrasattva_large,
            R.drawable.amitabha_large,
            R.drawable.mandala_large,
            R.drawable.guru_yoga_large,
            R.drawable.z_chenrizg_large,
            R.drawable.mahakala_large};

    public static int getDrawableId(TypeOfImage typeOfImage) {
        return drawablePractice[typeOfImage.getValue()];
    }

    public static int getDrawableLargeId(TypeOfImage typeOfImage) {
        return drawablePracticeLarge[typeOfImage.getValue()];
    }
}
