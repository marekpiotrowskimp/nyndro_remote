package iso.piotrowski.marek.nyndro.PracticeMain.BoomButton;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import iso.piotrowski.marek.nyndro.DataSource.IDataSource;
import iso.piotrowski.marek.nyndro.PracticeMain.PracticeMainContract;

/**
 * Created by marek.piotrowski on 05/09/2017.
 */

public class BoomButtonFactory {

    public static IBoomButtonAdapter getBoomButtonAdapter (PracticeMainContract.TypeOfBoomButton typeOfBoomButton, IDataSource dataSourceDelegate) {
        switch (typeOfBoomButton) {
            case BasicPractice:
                return new PracticeBoomButtonAdapter();
            case AddedPractice:
                return new AddedPracticesBoomButtonAdapter(dataSourceDelegate);
        }

        return new IBoomButtonAdapter() {
            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getIconResourcesId(int position) {
                return 0;
            }

            @Override
            public String getName(int position) {
                return null;
            }
        };
    }

    public static PiecePlaceEnum getPlaceEnum(int size) {
        switch (size) {
            case 0:
                return PiecePlaceEnum.Unknown;
            case 1:
                return PiecePlaceEnum.DOT_1;
            case 2:
                return PiecePlaceEnum.DOT_2_1;
            case 3:
                return PiecePlaceEnum.DOT_3_1;
            case 4:
                return PiecePlaceEnum.DOT_4_1;
            case 5:
                return PiecePlaceEnum.DOT_5_1;
            case 6:
                return PiecePlaceEnum.DOT_6_1;
            case 7:
                return PiecePlaceEnum.DOT_7_1;
            case 8:
                return PiecePlaceEnum.DOT_8_1;
            case 9:
                return PiecePlaceEnum.DOT_9_1;
            default:
                return PiecePlaceEnum.Unknown;
        }
    }

    public static ButtonPlaceEnum getButtonPlaceEnum(int size) {
        switch (size) {
            case 0:
                return ButtonPlaceEnum.Unknown;
            case 1:
                return ButtonPlaceEnum.SC_1;
            case 2:
                return ButtonPlaceEnum.SC_2_1;
            case 3:
                return ButtonPlaceEnum.SC_3_1;
            case 4:
                return ButtonPlaceEnum.SC_4_1;
            case 5:
                return ButtonPlaceEnum.SC_5_1;
            case 6:
                return ButtonPlaceEnum.SC_6_1;
            case 7:
                return ButtonPlaceEnum.SC_7_1;
            case 8:
                return ButtonPlaceEnum.SC_8_1;
            case 9:
                return ButtonPlaceEnum.SC_9_1;
            default:
                return ButtonPlaceEnum.Unknown;
        }
    }

}
