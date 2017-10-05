package iso.piotrowski.marek.nyndro.Model.parsers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.LinkedList;
import java.util.List;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.BuildConfig;
import iso.piotrowski.marek.nyndro.Model.DataSection;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by marek.piotrowski on 01/10/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest(NyndroApp.class)
public class ParserTest {

    @Mock
    PracticeModel practiceModelParserTest;

    @Mock
    PracticeModel practiceModelParserTest2;

    @Mock
    HistoryModel historyModelParserTest;

    @Mock
    HistoryModel historyModelParserTest2;

    private DataSection<HistoryModel> dataSection;
    private final String practice1 = "practice1";
    private final String practice2 = "practice2";


    @Before
    public void setUp() throws Exception {
        List<HistoryModel> historyModelList = new LinkedList<>();

        practiceModelParserTest = mock(PracticeModel.class);
        when(practiceModelParserTest.getName()).thenReturn(practice1);
        when(practiceModelParserTest.getPracticeImageId()).thenReturn(999);
        when(practiceModelParserTest.getID()).thenReturn(1L);
        practiceModelParserTest2 = mock(PracticeModel.class);

        when(practiceModelParserTest2.getName()).thenReturn(practice2);
        when(practiceModelParserTest2.getPracticeImageId()).thenReturn(1001);
        when(practiceModelParserTest2.getID()).thenReturn(2L);

        historyModelParserTest = mock(HistoryModel.class);
        when(historyModelParserTest.getPracticeId()).thenReturn(5L);
        when(historyModelParserTest.getPracticeData()).thenReturn(1506535866000L); //1506449466000
        when(historyModelParserTest.getID()).thenReturn(1L);
        when(historyModelParserTest.getPractice()).thenReturn(practiceModelParserTest);
        when(historyModelParserTest.getPracticeId()).thenReturn(1L);
        when(historyModelParserTest.getProgress()).thenReturn(1000);
        when(historyModelParserTest.getRepetition()).thenReturn(500);
        historyModelList.add(historyModelParserTest);

        historyModelParserTest2 = mock(HistoryModel.class);
        when(historyModelParserTest2.getPracticeId()).thenReturn(5L);
        when(historyModelParserTest2.getPracticeData()).thenReturn(1506449466000L); //1506449466000
        when(historyModelParserTest2.getID()).thenReturn(2L);
        when(historyModelParserTest2.getPractice()).thenReturn(practiceModelParserTest2);
        when(historyModelParserTest2.getPracticeId()).thenReturn(2L);
        when(historyModelParserTest2.getProgress()).thenReturn(500);
        when(historyModelParserTest2.getRepetition()).thenReturn(108);
        historyModelList.add(historyModelParserTest2);

        dataSection = Parser.convertHistoryModelToSectioned(historyModelList);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void expandedDataSectionTest() throws Exception {
        assertTrue(dataSection.getSize() == 3);
        dataSection.getSectionList().get(0).setExpanded(true);
        assertTrue(dataSection.getSize() == 4);
    }

    @Test
    public void sectionNames() throws Exception {
        assertTrue(dataSection.getSectionList().get(0).getName().contentEquals(practice1));
        assertTrue(dataSection.getSectionList().get(1).getName().contentEquals(practice2));
    }

    @Test
    public void sectionResult() throws Exception {
        dataSection.getSectionList().get(0).setExpanded(true);
        assertTrue(dataSection.get(0).getTypeOfData().equals(DataSection.TypeOfData.Section));
        assertTrue(dataSection.get(1).getTypeOfData().equals(DataSection.TypeOfData.Data));
        assertTrue(dataSection.get(2).getTypeOfData().equals(DataSection.TypeOfData.Section));
        assertTrue(dataSection.get(3).getTypeOfData().equals(DataSection.TypeOfData.Data));
    }

    @Test
    public void sectionResultData() throws Exception {
        dataSection.getSectionList().get(0).setExpanded(true);
        assertTrue(dataSection.get(1).getTypeOfData().equals(DataSection.TypeOfData.Data));
        assertTrue(dataSection.get(1).getData().getID() == 1);
        assertTrue(dataSection.get(1).getData().getProgress() == 1000);
        assertTrue(dataSection.get(1).getData().getRepetition() == 500);
        assertTrue(dataSection.get(3).getTypeOfData().equals(DataSection.TypeOfData.Data));
        assertTrue(dataSection.get(3).getData().getID() == 2);
        assertTrue(dataSection.get(3).getData().getProgress() == 500);
        assertTrue(dataSection.get(3).getData().getRepetition() == 108);
    }

    @Test
    public void sectionResultSection() throws Exception {
        dataSection.getSectionList().get(0).setExpanded(true);
        assertTrue(dataSection.get(0).getTypeOfData().equals(DataSection.TypeOfData.Section));
        assertTrue(dataSection.get(0).getSection().isExpanded());
        assertTrue(dataSection.get(0).getSection().getName().contentEquals(practice1));
        assertTrue(dataSection.get(0).getSection().getSize() == 2);
        assertTrue(dataSection.get(0).getSection().getPracticeImageId() == 999);
        assertTrue(dataSection.get(0).getSection().getSectionList().isEmpty());
        assertTrue(dataSection.get(2).getTypeOfData().equals(DataSection.TypeOfData.Section));
        assertTrue(dataSection.get(2).getSection().isExpanded());
        assertTrue(dataSection.get(2).getSection().getName().contentEquals(practice2));
        assertTrue(dataSection.get(2).getSection().getSize() == 2);
        assertTrue(dataSection.get(2).getSection().getPracticeImageId() == 1001);
        assertTrue(dataSection.get(2).getSection().getSectionList().isEmpty());
    }
}