package iso.piotrowski.marek.nyndro.tools;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.regex.Pattern;

import iso.piotrowski.marek.nyndro.R;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by marek.piotrowski on 22/09/2017.
 */
public class DrawableMapperTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getDrawableId() throws Exception {
        assertEquals(DrawableMapper.getDrawableId(DrawableMapper.TypeOfImage.amitabha), R.drawable.amitabha);
    }

    @Test
    public void getDrawableLargeId() throws Exception {
        assertEquals(DrawableMapper.getDrawableLargeId(DrawableMapper.TypeOfImage.amitabha), R.drawable.amitabha_large);
    }

}