package iso.piotrowski.marek.nyndro.Model;

/**
 * Created by marek.piotrowski on 18/08/2017.
 */
public class AnalysisInfo {
    private String text;
    private int number;
    private TypeOfAnalysisInfo type;

    private enum TypeOfAnalysisInfo {
        Text, Number, Both
    }

    public AnalysisInfo(String text) {
        this.text = text;
        type = TypeOfAnalysisInfo.Text;
    }

    public AnalysisInfo(int number) {
        this.number = number;
        type = TypeOfAnalysisInfo.Number;
    }

    public AnalysisInfo(String text, int number) {
        this.text = text;
        this.number = number;
        type = TypeOfAnalysisInfo.Both;
    }

    @Override
    public String toString() {
        String result;
        switch (type) {
            case Text:
                result = text;
                break;
            case Number:
                result = Integer.toString(getNumber());
                break;
            default:
                result = text + ": " + Integer.toString(getNumber());
                break;
        }
        return result;
    }

    public int getNumber() {
        return number;
    }
}
