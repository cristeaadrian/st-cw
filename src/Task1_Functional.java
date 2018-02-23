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
    public void setUp() {
        map = new EntryMap();
        engine = new TemplateEngine();
        simpleEngine = new SimpleTemplateEngine();
    }

    @Test (expected=RuntimeException.class)
    public void storeFirstArgumentNull() {
        map.store(null, "Adam");
    }

    @Test (expected=RuntimeException.class)
    public void storeFirstArgumentEmpty() {
        map.store("", "Adam");
    }

    @Test (expected=RuntimeException.class)
    public void storeSecondArgumentNull() {
        map.store("name", null);
    }

    @Test
    public void storeEverythingValid() {
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

    @Test
    public void deleteNonExisting() {
        map.store("name", "Adam");
        map.delete("Frank");
    }

    @Test (expected=RuntimeException.class)
    public void updateNullFirstArgument() {
        map.store("name", "Adam");
        map.update(null, "Frank");
    }

    @Test (expected=RuntimeException.class)
    public void updateEmptyFirstArgument() {
        map.store("name", "Adam");
        map.update("", "Frank");
    }

    @Test (expected=RuntimeException.class)
    public void updateNullSecondArgument() {
        map.store("name", "Adam");
        map.update("name", null);
    }

//    @Test
//    public void updateNonExisting() {
//        map.store("name", "Adam");
//        map.update("surname", "Zaharia");
//    }

}