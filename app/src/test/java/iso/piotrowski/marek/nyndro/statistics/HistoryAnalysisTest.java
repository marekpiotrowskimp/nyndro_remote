package iso.piotrowski.marek.nyndro.statistics;

import android.content.Context;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import java.util.LinkedList;
import java.util.List;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.BuildConfig;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.R;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by marek.piotrowski on 22/09/2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({NyndroApp.class, PracticeModel.class})
public class HistoryAnalysisTest {
    private HistoryAnalysis analysis;

    @Mock
    PracticeModel practiceModel;

    @Mock
    HistoryModel historyModel;

    @Mock
    HistoryModel historyModel2;

    @Mock
    Context context;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUp() throws Exception {
        practiceModel = mock(PracticeModel.class);
        when(practiceModel.getName()).thenReturn("name");
        when(practiceModel.getPracticeImageId()).thenReturn(999);
    }

    @Test
    public void emptyAnalysis() throws Exception {
        analysis = new HistoryAnalysis(new LinkedList<>());
        assertTrue(analysis.getAnalysisResult().isEmpty());
    }

    @Test
    public void emptyResult() throws Exception {
        analysis = new HistoryAnalysis(new LinkedList<>());

        analysis.setEmptyAnalysis(practiceModel);
        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.PRACTICE_NAME).toString().contentEquals("name"));
        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.PRACTICE_IMAGE_ID).toString().contentEquals("999"));
    }

    @Test
    public void historyResult() throws Exception {
        List<HistoryModel> historyModelList = new LinkedList<>();

        historyModel = mock(HistoryModel.class);
        when(historyModel.getPracticeId()).thenReturn(5L);
        when(historyModel.getPracticeData()).thenReturn(1506535866000L); //1506449466000
        when(historyModel.getID()).thenReturn(1L);
        when(historyModel.getPractice()).thenReturn(practiceModel);
        when(historyModel.getProgress()).thenReturn(1000);
        when(historyModel.getRepetition()).thenReturn(500);
        historyModelList.add(historyModel);

        historyModel2 = mock(HistoryModel.class);
        when(historyModel2.getPracticeId()).thenReturn(5L);
        when(historyModel2.getPracticeData()).thenReturn(1506449466000L); //1506449466000
        when(historyModel2.getID()).thenReturn(2L);
        when(historyModel2.getProgress()).thenReturn(500);
        when(historyModel2.getRepetition()).thenReturn(108);
        historyModelList.add(historyModel2);

        PowerMockito.mockStatic(NyndroApp.class);
        context = mock(Context.class);
        Mockito.when(NyndroApp.getContext()).thenReturn(context);
        Mockito.when(context.getString(R.string.undefined)).thenReturn("undefine");

        analysis = new HistoryAnalysis(historyModelList);

        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.PRACTICE_NAME).toString().contentEquals("name"));
        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.PRACTICE_IMAGE_ID).toString().contentEquals("999"));
        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.FINISH_PRACTICE_DATE).toString().contentEquals("undefine"));
        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.PRACTICE_DAYS).toString().contentEquals("2"));
        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.AVERAGE_DAYS).toString().contentEquals("608"));
        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.AVERAGE_MONTH).toString().contentEquals("608"));
        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.PREFIX_DAY + "2").toString().contentEquals("1"));
        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.PREFIX_DAY + "3").toString().contentEquals("1"));
        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.PREFIX_DAY + "1").toString().contentEquals("0"));
        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.PREFIX_MONTH + "8").toString().contentEquals("2"));
        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.PREFIX_MONTH + "1").toString().contentEquals("0"));
    }

}