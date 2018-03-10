import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import st.EntryMap;
import st.SimpleTemplateEngine;
import st.TemplateEngine;

public class Task2_Mutation {

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
    -------------------- Mutation06 Tests -----------------------
     */

    @Test
    public void replaceTemplateThatDoesNotAppearInString() {
        map.store("a", "Adam");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("My name is ${}.", map, matchingMode);
        assertEquals("My name is ${}.", result);
    }

    /*
    -------------------- Mutation07 Tests -----------------------
     */

    @Test
    public void deleteNonExisting() {
        map.store("name", "Adam");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result1 = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
        map.delete("surname");
        String result2 = engine.evaluate("Hello ${name} ${surname}", map, matchingMode);
        assertEquals(result1, result2);
    }

    /*
    -------------------- Mutation08 Tests -----------------------
     */

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
    public void simpleEvaluateCaseSensitiveAndWholeWordSearch() {
        String template = "localVARIABLE int localId = local; globalVARIABLE int LOCAL = global;";
        String pattern = "LOCAL";
        String value = "globalId";
        Integer matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH | SimpleTemplateEngine.CASE_SENSITIVE;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("localVARIABLE int localId = local; globalVARIABLE int globalId = global;", result);
    }

    /*
    -------------------- Mutation09 Tests -----------------------
     */

    @Test
    public void templateOrder() {
        map.store("lm", " ");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${fgijk${lm}nopqr}", map, matchingMode);
        assertEquals("${fgijk nopqr}", result);
        map.store("fgijk nopqr", "Hello world");
        result = engine.evaluate("${fgijk${lm}nopqr}", map, matchingMode);
        assertEquals("Hello world", result);
    }

    /*
    -------------------- Mutation10 Tests -----------------------
     */

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
    public void simpleEvaluateNoRecursion() {
        String template = "defabc";
        String pattern = "abc";
        String value = "abcabc";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("defabcabc", result);
    }

    @Test
    public void simpleEvaluateWholeFirstWordSearch() {
        String template = "local VARIABLE int localId = local";
        String pattern = "local";
        String value = "global";
        Integer matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("global VARIABLE int localId = global", result);
    }
}
