package iso.piotrowski.marek.nyndro.Model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by marek.piotrowski on 01/09/2017.
 */

public class DataSection<DataModel> {

    private static int SECTION = 0;
    private static int POSITION = 1;
    private List<DataSection<DataModel>> sectionList;
    private List<DataModel> childList;
    private int practiceImageId;
    private String name;
    private boolean expanded;

    public enum TypeOfData {
        Section(0), Data(1);
        private int value;
        TypeOfData(int value) {
            this.value = value;
        }
        public int getValue(){
            return value;
        }
    }

    public DataSection(String name, int practiceImageId, boolean expanded) {
        this.practiceImageId = practiceImageId;
        this.name = name;
        this.expanded = expanded;
        this.setSectionList(new LinkedList<>());
        this.childList = new LinkedList<DataModel>();
    }

    public int getPracticeImageId() {
        return practiceImageId;
    }

    public String getName() {
        return name;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public void addChaild (DataModel child){
        childList.add(child);
    }

    public void addSection (DataSection dataSection){
        getSectionList().add(dataSection);
    }

    public List<DataModel> getChildList() {
        return childList;
    }

    public void setChildList(List<DataModel> childList) {
        this.childList = childList;
    }

    public TypeOfData getType (int position) {
        return getSectionAndPosition(position)[POSITION] == 0 ? TypeOfData.Section : TypeOfData.Data;
    }

    private int[] getSectionAndPosition(int position){
        int countedPosition = position;
        int sectionCounter = 0;
        for (DataSection<DataModel> dataSection : getSectionList()){
            if (dataSection.isInThisSection(countedPosition)){
                return new int[]{sectionCounter, countedPosition};
            } else {
                countedPosition = dataSection.getPositionInNextSection(countedPosition);
                sectionCounter++;
            }
        }
        return new int[]{0, 0};
    }

    private boolean isInThisSection(int position){
        return position < getSize();
    }

    private int getPositionInNextSection(int position) {
        return isInThisSection(position) ? position : position - getSize();
    }

    public List<DataSection<DataModel>> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<DataSection<DataModel>> sectionList) {
        this.sectionList = sectionList;
    }

    public SectionResult<DataModel> get (int position){
        int[] sectionAndPosition = getSectionAndPosition(position);

        DataSection<DataModel> section = sectionList.get(sectionAndPosition[SECTION]);
        return new SectionResult<DataModel>().setTypeOfData(getType(position))
                        .setData(getType(position) == TypeOfData.Data ? section.getChildList().get(sectionAndPosition[POSITION] - 1) : null)
                        .setSection(section);
    }

    public int getSize(){
        int size = isExpanded() ? childList.size() : 0;
        size += childList.size() > 0 ? 1 : 0;
        for (DataSection dataSection : getSectionList()){
            size += dataSection.getSize();
        }
        return size;
    }

    public class SectionResult<DataModel> {
        private TypeOfData typeOfData;
        private DataModel data;
        private DataSection section;

        public TypeOfData getTypeOfData() {
            return typeOfData;
        }

        public SectionResult<DataModel> setTypeOfData(TypeOfData typeOfData) {
            this.typeOfData = typeOfData;
            return this;
        }

        public DataModel getData() {
            return data;
        }

        public SectionResult<DataModel> setData(DataModel data) {
            this.data = data;
            return this;
        }

        public DataSection getSection() {
            return section;
        }

        public SectionResult<DataModel> setSection(DataSection section) {
            this.section = section;
            return this;
        }
    }
}
