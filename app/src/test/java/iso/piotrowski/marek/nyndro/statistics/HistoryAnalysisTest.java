package iso.piotrowski.marek.nyndro.statistics;

import com.activeandroid.Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.R;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by marek.piotrowski on 22/09/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class HistoryAnalysisTest {
    private HistoryAnalysis analysis;

    @Mock
    PracticeModel practiceModel;

    @Mock
    HistoryModel historyModel;

    @Mock
    HistoryModel historyModel2;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void emptyAnalysis() throws Exception {
        analysis = new HistoryAnalysis(new LinkedList<>());
        assertTrue(analysis.getAnalysisResult().isEmpty());
    }

    @Test
    public void emptyResult() throws Exception {
        analysis = new HistoryAnalysis(new LinkedList<>());
        practiceModel = mock(PracticeModel.class);
        when(practiceModel.getName()).thenReturn("name");
        when(practiceModel.getPracticeImageId()).thenReturn(999);
        analysis.setEmptyAnalysis(practiceModel);
        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.PRACTICE_NAME).toString().contentEquals("name"));
        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.PRACTICE_IMAGE_ID).toString().contentEquals("999"));
    }

    @Test
    public void historyResult() throws Exception {
//        practiceModel = mock(PracticeModel.class);
//        when(practiceModel.getName()).thenReturn("name");
//        when(practiceModel.getPracticeImageId()).thenReturn(999);
//
//        List<HistoryModel> historyModelList = new LinkedList<>();
//
//        historyModel = mock(HistoryModel.class);
//        when(historyModel.getPracticeId()).thenReturn(5L);
//        when(historyModel.getPracticeData()).thenReturn(1506535866000L); //1506449466000
//        when(historyModel.getID()).thenReturn(1L);
//        when(historyModel.getPractice()).thenReturn(practiceModel);
//        when(historyModel.getProgress()).thenReturn(1000);
//        when(historyModel.getRepetition()).thenReturn(500);
//        historyModelList.add(historyModel);
//
//        historyModel2 = mock(HistoryModel.class);
//        when(historyModel2.getPracticeId()).thenReturn(5L);
//        when(historyModel2.getPracticeData()).thenReturn(1506449466000L); //1506449466000
//        when(historyModel2.getID()).thenReturn(2L);
//        when(historyModel2.getProgress()).thenReturn(500);
//        when(historyModel2.getRepetition()).thenReturn(108);
//        historyModelList.add(historyModel2);
//
////        nyndroApp = mock(NyndroApp.class);
////        when(nyndroApp.getContext().getString(R.string.undefined)).thenReturn("nothing");
////        PowerMockito.mockStatic(NyndroApp.class);
//        analysis = new HistoryAnalysis(historyModelList);
//
//        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.PRACTICE_NAME).toString().contentEquals("name"));
//        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.PRACTICE_IMAGE_ID).toString().contentEquals("999"));
    }

}