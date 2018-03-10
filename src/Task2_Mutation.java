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
     * All Tests pass
     */



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

}
