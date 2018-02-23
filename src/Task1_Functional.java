import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import st.EntryMap;
import st.SimpleTemplateEngine;
import st.TemplateEngine;

import java.util.ArrayList;
import java.util.Map;

public class Task1_Functional {

    private EntryMap map;

    private TemplateEngine engine;

    private SimpleTemplateEngine simpleEngine;

    @Before
    public void setUp() throws Exception {
        map = new EntryMap();
        engine = new TemplateEngine();
        simpleEngine = new SimpleTemplateEngine();
    }

    @Test (expected=RuntimeException.class)
    public void firstArgumentNullStore() {
        map.store(null, "Adam");
    }

    @Test (expected=RuntimeException.class)
    public void firstArgumentEmptyStore() {
        map.store("", "Adam");
    }

    @Test (expected=RuntimeException.class)
    public void secondArgumentNullStore() {
        map.store("name", null);
    }

    @Test
    public void everythingValidStore() {
        /*
        Test for the case in which the store function gets correct inputs
        and the map exists, has no duplicates and is ordered
         */
        map.store("name", "Adam");
        map.store("surname", "Dykes");
    }

    @Test (expected=RuntimeException.class)
    public void deleteNull() {
        map.store("name", "Adam");
        map.delete(null);
    }

    @Test (expected=RuntimeException.class)
    public void deleteEmpty() {
        map.store("name", "Adam");
        map.delete("");
    }

}