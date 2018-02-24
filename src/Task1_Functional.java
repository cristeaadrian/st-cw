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
    public void storeExisting() {
        map.store("name", "Adam");
        map.store("name", "Frank");
        Integer matchingMode = TemplateEngine.CASE_INSENSITIVE;
        String result = engine.evaluate("Hello ${name}", map, matchingMode);
        assertEquals("Hello Adam", result);
        map.delete("name");
        result = engine.evaluate("Hello ${name}", map, matchingMode);
        assertEquals("Hello ${name}", result);
    }

    @Test
    public void storeOrderPreservation() {
        map.store("name", "Adam");
        map.store("Name", "Frank");
        Integer matchingMode = TemplateEngine.CASE_SENSITIVE;
        String result = engine.evaluate("Hello ${Name}, ${name}", map, matchingMode);
        assertEquals("Hello Frank Adam", result);
        matchingMode = TemplateEngine.CASE_INSENSITIVE;
        result = engine.evaluate("Hello ${Name}, ${name}", map, matchingMode);
        assertEquals("Hello Adam Adam", result);
    }

    @Test
    public void storeValid() {
        map.store("name", "Adam");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
        assertEquals("Hello Adam ${surname}", result);
        map.store("surname", "Dykes");
        result = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
        assertEquals("Hello Adam Dykes", result);
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
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result1 = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
        map.delete("surname");
        String result2 = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
        assertEquals(result1, result2);
    }

    @Test
    public void deleteOrderPreservation() {
        map.store("name", "Adam");
        map.store("Name", "Andra");
        Integer matchingMode = TemplateEngine.CASE_SENSITIVE;
        String result = engine.evaluate("Hello ${name}, ${Name}", map, matchingMode);
        assertEquals("Hello Adam, Andra", result);
        map.delete("name");
        map.store("name", "Frank");
        matchingMode = TemplateEngine.CASE_INSENSITIVE;
        result = engine.evaluate("Hello ${name}, ${Name}", map, matchingMode);
        assertEquals("Hello Andra, Andra", result);
        matchingMode = TemplateEngine.CASE_SENSITIVE;
        result = engine.evaluate("Hello ${name}, ${Name}", map, matchingMode);
        assertEquals("Hello Frank, Andra", result);
    }

    @Test
    public void deleteValid() {
        map.store("name", "Adam");
        map.store("surname", "Dykes");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
        assertEquals("Hello Adam Dykes", result);
        map.delete("surname");
        result = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
        assertEquals("Hello Adam ${surname}", result);
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
    public void updateNonExisting() {
        map.store("name", "Adam");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result1 = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
        map.update("surname", "Dykes");
        String result2 = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
        assertEquals(result1, result2);
    }

    @Test
    public void updateOrderPreservation() {
        map.store("name", "Adam");
        map.store("Name", "Andra");
        Integer matchingMode = TemplateEngine.CASE_SENSITIVE;
        String result = engine.evaluate("Hello ${name}, ${Name}", map, matchingMode);
        assertEquals("Hello Adam, Andra", result);
        map.update("name", "Joe");
        matchingMode = TemplateEngine.CASE_INSENSITIVE;
        result = engine.evaluate("Hello ${name}, ${Name}", map, matchingMode);
        assertEquals("Hello Joe, Joe", result);
        matchingMode = TemplateEngine.CASE_SENSITIVE;
        result = engine.evaluate("Hello ${name}, ${Name}", map, matchingMode);
        assertEquals("Hello Joe, Andra", result);
    }

    @Test
    public void updateValid() {
        map.store("name", "Adam");
        map.store("surname", "Dykes");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
        assertEquals("Hello Adam Dykes", result);
        map.update("surname", "Frank");
        result = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
        assertEquals("Hello Adam Frank", result);
    }

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
    public void evaluateMatchingModeContradictoryCase() { // also tests spec6 - case_insensitive
        map.store("name", "Adam");
        map.store("Surname", "Dykes");
        Integer matchingMode = TemplateEngine.CASE_SENSITIVE | TemplateEngine.CASE_INSENSITIVE;
        String result = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
        assertEquals("Hello Adam ${surname}", result);
    }

    @Test
    public void evaluateMatchingModeContradictorySearch() { // also tests spec6 - case_sensitive
        map.store("name", "Adam");
        map.store("Surname", "Dykes");
        Integer matchingMode = TemplateEngine.ACCURATE_SEARCH | TemplateEngine.BLUR_SEARCH;
        String result = engine.evaluate("Hello ${name} ${sur name}", map, matchingMode);
        assertEquals("Hello Adam Dykes", result);
    }

    @Test
    public void evaluateBlurSearch() {
        map.store("middlename", "Peter");
        Integer matchingMode = TemplateEngine.BLUR_SEARCH;
        String result = engine.evaluate("${middlename} has a son ${middle name} and a brother ${middle  name}", map, matchingMode);
        assertEquals("Peter has a son Peter and a brother Peter", result);
    }

    @Test
    public void evaluateAccurateSearch() {
        map.store("middlename", "Peter");
        Integer matchingMode = TemplateEngine.ACCURATE_SEARCH | TemplateEngine.DELETE_UNMATCHED;
        String result = engine.evaluate("${middlename} has a son ${middle name} and a brother ${middle  name}", map, matchingMode);
        assertEquals("Peter has a son  and a brother ", result);
    }

    @Test
    public void evaluateBoundary() { //probably useless
        map.store("name", "Adam");
        map.store("surname", "Dykes");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("Hello ${${name} }${surname}", map, matchingMode);
        assertEquals("Hello ${Adam }Dykes", result);
    }

    /*
    -------------- SimpleTemplateEngine Tests----------------------
     */

    @Test
    public void simpleEvaluateNullTemplate() {
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(null, "David", "Tom", matchingMode);
        assertEquals(null, result);
    }

    @Test
    public void simpleEvaluateEmptyTemplate() {
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate("", "David", "Tom", matchingMode);
        assertEquals("", result);
    }

    @Test
    public void simpleEvaluateNullPattern() {
        String template = "Hello, this is DAVID. David is 25 years old.";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template,null, "Tom", matchingMode);
        assertEquals(template, result);
    }

    @Test
    public void simpleEvaluateEmptyPattern() {
        String template = "Hello, this is DAVID. David is 25 years old.";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template,"", "Tom", matchingMode);
        assertEquals(template, result);
    }

    @Test
    public void simpleEvaluateNullValue() {
        String template = "Hello, this is DAVID. David is 25 years old.";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template,"David", null, matchingMode);
        assertEquals(template, result);
    }

    @Test
    public void simpleEvaluateEmptyValue() {
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String template = "Hello, this is DAVID. David is 25 years old.";
        String result = simpleEngine.evaluate(template,"David", "", matchingMode);
        assertEquals(template, result);
    }

    @Test
    public void simpleEvaluateReplaceAll() {
        String template = "David has a son David and a brother David.";
        String pattern = "David";
        String value = "Tom";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("Tom has a son Tom and a brother Tom.", result);
    }

    @Test
    public void simpleEvaluateReplaceThird() {
        String template = "David has a son David and a brother David.";
        String pattern = "David#3";
        String value = "Tom";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("David has a son David and a brother Tom.", result);
    }

    @Test
    public void simpleEvaluateReplaceNonexistent() {
        String template = "David has a son David and a brother David.";
        String pattern = "David#4";
        String value = "Tom";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals(template, result);
    }

    @Test
    public void simpleEvaluateReplaceHash() {
        String template = "David #2nd has a son, David #3rd.";
        String pattern = "David ##";
        String value = "Tom #";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("Tom #2nd has a son, Tom #3rd.", result);
    }

    @Test
    public void simpleEvaluateReplaceSecondHash() {
        String template = "David #2nd has a son, David #3rd.";
        String pattern = "David ###2";
        String value = "Tom #";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("David #2nd has a son, Tom #3rd.", result);
    }

    @Test
    public void simpleEvaluateReplaceEarlyHash() {
        String template = "David has a son David and a brother David.";
        String pattern = "Davi#3d";
        String value = "Donal";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("David has a son David and a brother Donald.", result);
    }

    @Test
    public void simpleEvaluateCaseSensitive() {
        String template = "Hello, this is DAVID. David is 25 years old.";
        String pattern = "DAVID";
        String value = "Tom";
        Integer matchingMode = SimpleTemplateEngine.CASE_SENSITIVE;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("Hello, this is Tom. David is 25 years old.", result);
    }

    @Test
    public void simpleEvaluateWholeWordSearchEnabled() {
        String template = "localVARIABLE int localId = local";
        String pattern = "local";
        String value = "global";
        Integer matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("localVARIABLE int localId = global", result);
    }

    @Test
    public void simpleEvaluateWholeWordSearchDisabled() {
        String template = "localVARIABLE int localId = local";
        String pattern = "local";
        String value = "global";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("globalVARIABLE int globalId = global", result);
    }

    @Test
    public void simpleEvaluateCaseSensitiveAndWholeWordSearch() {
        String template = "localVARIABLE int localId = local; globalVARIABLE int LOCAL = global;";
        String pattern = "LOCAL";
        String value = "globalId";
        Integer matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH | SimpleTemplateEngine.CASE_SENSITIVE;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("localVARIABLE int localId = local; globalVARIABLE int globalId = global;", result);
    }

    @Test
    public void simpleEvaluateNoRecursion() {
        String template = "defabc";
        String pattern = "abc";
        String value = "abcabc";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("defabcabc", result);
    }

}