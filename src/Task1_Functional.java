import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
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

    /*
    -------------------- EntryMap Tests -----------------------
     */

    @Test(expected = RuntimeException.class)
    public void storeFirstArgumentNull() {
        map.store(null, "Adam");
    }

    @Test(expected = RuntimeException.class)
    public void storeFirstArgumentEmpty() {
        map.store("", "Adam");
    }

    @Test(expected = RuntimeException.class)
    public void storeSecondArgumentNull() {
        map.store("name", null);
    }

    @Test
    public void storeValid() {
        map.store("name", "Adam");
        map.store("surname", "Dykes");
    }

    @Test(expected = RuntimeException.class)
    public void deleteNull() {
        map.store("name", "Adam");
        map.delete(null);
    }

    @Test(expected = RuntimeException.class)
    public void deleteEmpty() {
        map.store("name", "Adam");
        map.delete("");
    }

    @Test
    public void deleteNonExisting() {
        map.store("name", "Adam");
        map.delete("surname");
    }

    @Test
    public void deleteValid() {
        map.store("name", "Adam");
        map.delete("name");
    }

    @Test(expected = RuntimeException.class)
    public void updateNullFirstArgument() {
        map.store("name", "Adam");
        map.update(null, "Frank");
    }

    @Test(expected = RuntimeException.class)
    public void updateEmptyFirstArgument() {
        map.store("name", "Adam");
        map.update("", "Frank");
    }

    @Test(expected = RuntimeException.class)
    public void updateNullSecondArgument() {
        map.store("name", "Adam");
        map.update("name", null);
    }

    @Test
    public void updateValid() {
        map.store("name", "Adam");
        map.update("name", "Frank");
    }

//    @Test
//    public void updateNonExisting() {
//        map.store("name", "Adam");
//        map.update("surname", "Zaharia");
//    }

    /*
    ------------------ TemplateEngine Tests -----------------------
     */

    @Test
    public void evaluateNullTemplate() {
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate(null, map, matchingMode);
        assertEquals(null, result);
    }

    @Test
    public void evaluateEmptyTemplate() {
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("", map, matchingMode);
        assertEquals("", result);
    }

    @Test
    public void evaluateNullMap() {
        Integer matchingMode = TemplateEngine.DEFAULT;
        String expected = "This should remain the same";
        String result = engine.evaluate(expected, null, matchingMode);
        assertEquals(expected, result);
    }

    @Test
    public void evaluateMatchingModeZero() {
        map.store("name", "Adam");
        map.store("surname", "Dykes");
        Integer matchingMode = 0;
        String result = engine.evaluate("Hello ${name} ${SURNAME} (${age})", map, matchingMode);
        assertEquals("Hello Adam Dykes (${age})", result);
    }

    @Test
    public void evaluateMatchingModeNull() {
        map.store("name", "Adam");
        map.store("surname", "Dykes");
        String result = engine.evaluate("Hello ${name} ${SURNAME} (${age})", map, null);
        assertEquals("Hello Adam Dykes (${age})", result);
    }

    @Test
    public void evaluateMatchingModeDefault() { // keep unmatched + case insensitive + accurate search
        map.store("name", "Adam");
        map.store("surname", "Dykes");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("Hello ${name} ${SURNAME} (${age})", map, matchingMode);
        assertEquals("Hello Adam Dykes (${age})", result);
    }

    @Test
    public void evaluateMatchingModeDeleteUnmatched() { // delete unmatched
        map.store("name", "Adam");
        Integer matchingMode = TemplateEngine.DELETE_UNMATCHED;
        String result = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
        assertEquals("Hello Adam ", result);
    }

    @Test
    public void evaluateMatchingModeSensitiveBlur() { // case sensitive + blur search
        map.store("first name", "Adam");
        map.store("surname", "Dykes");
        Integer matchingMode = TemplateEngine.CASE_SENSITIVE | TemplateEngine.BLUR_SEARCH;
        String result = engine.evaluate("Hello ${firstname} ${Surname}", map, matchingMode);
        assertEquals("Hello Adam ${Surname}", result);
    }

    @Test
    public void evaluateMatchingModeAccurateSearch() { // accurate search + delete unmatched
        map.store("first name", "Adam");
        Integer matchingMode = TemplateEngine.ACCURATE_SEARCH | TemplateEngine.DELETE_UNMATCHED;
        String result = engine.evaluate("Hello ${firstname}", map, matchingMode);
        assertEquals("Hello ", result);
    }

    @Test
    public void evaluateMatchingModeContradictoryUnmatched() {
        map.store("name", "Adam");
        Integer matchingMode = TemplateEngine.DELETE_UNMATCHED | TemplateEngine.KEEP_UNMATCHED;
        String result = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
        assertEquals("Hello Adam ", result);
    }

    @Test
    public void evaluateMatchingModeContradictoryCase() {
        map.store("name", "Adam");
        map.store("Surname", "Dykes");
        Integer matchingMode = TemplateEngine.CASE_SENSITIVE | TemplateEngine.CASE_INSENSITIVE;
        String result = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
        assertEquals("Hello Adam ${surname}", result);
    }

    @Test
    public void evaluateMatchingModeContradictorySearch() {
        map.store("name", "Adam");
        map.store("Surname", "Dykes");
        Integer matchingMode = TemplateEngine.ACCURATE_SEARCH | TemplateEngine.BLUR_SEARCH;
        String result = engine.evaluate("Hello ${name} ${sur name}", map, matchingMode);
        assertEquals("Hello Adam Dykes", result);
    }

    /*
    -------------- SimpleTemplateEngine Tests----------------------
     */

}