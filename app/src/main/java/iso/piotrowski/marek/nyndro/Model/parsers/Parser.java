package iso.piotrowski.marek.nyndro.Model.parsers;

import java.util.LinkedList;
import java.util.List;

import iso.piotrowski.marek.nyndro.Model.DataSection;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;

/**
 * Created by marek.piotrowski on 01/09/2017.
 */

public class Parser {
    private static int InitialValueOutOfScope = -1;

    public static DataSection<HistoryModel> convertHistoryModelToSectioned (List<HistoryModel> historyModelList) {

        LinkedList<DataSection<HistoryModel>> dataSections = new LinkedList<>();
        DataSection<HistoryModel> section = null;
        long practiceId = InitialValueOutOfScope;

        for (HistoryModel history : historyModelList) {

            if (practiceId != history.getPracticeId()) {
                practiceId = history.getPracticeId();
                if (section != null) {
                    dataSections.add(section);
                }
                section = new DataSection<>(history.getPractice().getName(), history.getPractice().getPracticeImageId(), false);
            }
            if (section !=null) {
                section.addChaild(history);
            }
        }
        if ((section != null) && (section.getChildList() != null) && (!section.getChildList().isEmpty())){
            section.setExpanded(true);
            dataSections.add(section);
        }
        DataSection<HistoryModel> historySectioned = new DataSection<>("Main", 0, false);
        historySectioned.setSectionList(dataSections);
        return historySectioned;
    }

}
