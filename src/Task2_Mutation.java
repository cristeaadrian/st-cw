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
    ---------------------- Mutation 1 ----------------------
     * 2 Tests fail in total
     */

    @Test
    public void evaluateMatchingModeAccurateSearch() { // accurate search + delete unmatched
        map.store("first name", "Adam");
        Integer matchingMode = TemplateEngine.ACCURATE_SEARCH | TemplateEngine.DELETE_UNMATCHED;
        String result = engine.evaluate("Hello ${firstname}", map, matchingMode);
        assertEquals("Hello ", result);
    }

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
    ---------------------- Mutation 2 ----------------------
     * 2 Tests fail in total
     */

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

    /*
    ---------------------- Mutation 3 ----------------------
     * 4 Tests fail in total
     */

    @Test
    public void evaluateAccurateSearch() {
        map.store("middlename", "Peter");
        Integer matchingMode = TemplateEngine.ACCURATE_SEARCH | TemplateEngine.DELETE_UNMATCHED;
        String result = engine.evaluate("${middlename} has a son ${middlename} and a brother ${middle  name}", map, matchingMode);
        assertEquals("Peter has a son Peter and a brother ", result);
    }

    @Test
    public void templateAllInputPossibilitiesDefaultMode() {
        String result;
        Integer matchingMode = TemplateEngine.DEFAULT;
        map.store("name", "Andra");
        map.store("surname", "Zaharia");
        map.store("fullname", "Andra Zaharia");

        // Test 1: Multiple Levels of Input Brackets
        result = engine.evaluate("${${${fullname}}}", map, matchingMode);
        assertEquals("${${Andra Zaharia}}", result);
        matchingMode = TemplateEngine.DELETE_UNMATCHED;
        result = engine.evaluate("${${${fullname}}}", map, matchingMode);
        assertEquals("", result);
        matchingMode = TemplateEngine.DEFAULT;

        // Test 2: One dollar sign
        result = engine.evaluate("${fullname} $", map, matchingMode);
        assertEquals("Andra Zaharia $", result);
        result = engine.evaluate("$ ${fullname}", map, matchingMode);
        assertEquals("$ Andra Zaharia", result);

        // Test 3: Multiple dollar signs
        result = engine.evaluate("${fullname} $ $", map, matchingMode);
        assertEquals("Andra Zaharia $ $", result);
        result = engine.evaluate("$ $ ${fullname}", map, matchingMode);
        assertEquals("$ $ Andra Zaharia", result);
        result = engine.evaluate("$ ${fullname} $", map, matchingMode);
        assertEquals("$ Andra Zaharia $", result);

        // Test 4: One Open Bracket
        result = engine.evaluate("${fullname} {", map, matchingMode);
        assertEquals("Andra Zaharia {", result);
        result = engine.evaluate("{ ${fullname}", map, matchingMode);
        assertEquals("{ Andra Zaharia", result);

        // Test 5: Multiple Open Brackets
        result = engine.evaluate("${fullname} { {{", map, matchingMode);
        assertEquals("Andra Zaharia { {{", result);
        result = engine.evaluate("{{ { ${fullname}", map, matchingMode);
        assertEquals("{{ { Andra Zaharia", result);
        result = engine.evaluate("{ {{ ${fullname} {{ {", map, matchingMode);
        assertEquals("{ {{ Andra Zaharia {{ {", result);

        // Test 6: One Closing Bracket
        result = engine.evaluate("${fullname} }", map, matchingMode);
        assertEquals("Andra Zaharia }", result);
        result = engine.evaluate("} ${fullname}", map, matchingMode);
        assertEquals("} Andra Zaharia", result);

        // Test 7: Multiple Closing Brackets
        result = engine.evaluate("${fullname} } }}", map, matchingMode);
        assertEquals("Andra Zaharia } }}", result);
        result = engine.evaluate("}} } ${fullname}", map, matchingMode);
        assertEquals("}} } Andra Zaharia", result);
        result = engine.evaluate("} }} ${fullname} }} }", map, matchingMode);
        assertEquals("} }} Andra Zaharia }} }", result);

        // Test 8: Combinations of open, closing brackets and dollar signs
        result = engine.evaluate("}{ :{ :} $ $: :$: $} } { ${fullname} ${ ${: bla", map, matchingMode);
        assertEquals("}{ :{ :} $ $: :$: $} } { Andra Zaharia ${ ${: bla", result);

        // Test 9: Empty template with no match
        result = engine.evaluate("${}", map, matchingMode );
        assertEquals("${}", result);
    }

    @Test
    public void evaluateBlurSearch() {
        map.store("middlename", "Peter");
        Integer matchingMode = TemplateEngine.BLUR_SEARCH;
        String result = engine.evaluate("${middlename} has a son ${middle name} and a brother ${middle  name}", map, matchingMode);
        assertEquals("Peter has a son Peter and a brother Peter", result);
    }

    /*
    ---------------------- Mutation 4 ----------------------
     * 1 Test fails
     */

    @Test
    public void longTemplate() {
        map.store("${abcdefghijklmnopqrstuvwxyz0123456789}", "A very long template");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("${abcdefghijklmnopqrstuvwxyz0123456789}", map, matchingMode);
        assertEquals(result, "");
    }

    /*
    ---------------------- Mutation 5 ----------------------
     * 3 Tests fail in total
     */

    @Test
    public void storeOrderPreservation() {
        map.store("name", "Adam");
        map.store("Name", "Frank");
        Integer matchingMode = TemplateEngine.CASE_SENSITIVE;
        String result = engine.evaluate("Hello ${Name}, ${name}", map, matchingMode);
        assertEquals("Hello Frank, Adam", result);
        matchingMode = TemplateEngine.CASE_INSENSITIVE;
        result = engine.evaluate("Hello ${Name}, ${name}", map, matchingMode);
        assertEquals("Hello Adam, Adam", result);
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

    /*
    ---------------------- Mutation 6 ----------------------
     */

    @Test
    public void replaceTemplateThatDoesNotAppearInString() {
        map.store("a", "Adam");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("My name is ${}.", map, matchingMode);
        assertEquals("My name is ${}.", result);
    }

    /*
    ---------------------- Mutation 7 ----------------------
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
    ---------------------- Mutation 8 ----------------------
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
    ---------------------- Mutation 9 ----------------------
     */

//    @Test
//    public void templateOrder() {
//        map.store("lm", " ");
//        Integer matchingMode = TemplateEngine.DEFAULT;
//        String result = engine.evaluate("${fgijk${lm}nopqr}", map, matchingMode);
//        assertEquals("${fgijk nopqr}", result);
//        map.store("fgijk nopqr", "Hello world");
//        result = engine.evaluate("${fgijk${lm}nopqr}", map, matchingMode);
//        assertEquals("Hello world", result);
//    }

    /*
    ---------------------- Mutation 10 ----------------------
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
