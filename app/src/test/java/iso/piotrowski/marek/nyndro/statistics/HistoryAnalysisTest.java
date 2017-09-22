package iso.piotrowski.marek.nyndro.statistics;

import com.activeandroid.Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;

import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;

import static org.junit.Assert.*;

/**
 * Created by marek.piotrowski on 22/09/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class HistoryAnalysisTest {
    private HistoryAnalysis analysis;

    @Mock
    PracticeModel practiceModel;

    @Before
    public void setUp() throws Exception {
        analysis = new HistoryAnalysis(new LinkedList<>());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void emptyAnalysis() throws Exception {
        assertTrue(analysis.getAnalysisResult().isEmpty());
    }

    @Test
    public void emptyResult() throws Exception {
//        practiceModel = new PracticeModel("name", "description", 1, 2, 3, 4, true);
//        analysis.setEmptyAnalysis(practiceModel);
//        assertTrue(analysis.getAnalysisResult().get(HistoryAnalysis.PRACTICE_NAME).toString().contentEquals("name"));

    }

}