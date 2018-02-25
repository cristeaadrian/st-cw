import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import st.EntryMap;
import st.SimpleTemplateEngine;
import st.TemplateEngine;

public class Task1_Coverage {

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
    ------------------ TemplateEngine Tests -----------------------
     */

    @Test
    public void templateLessThanZeroMatchingMode() {
    		map.store("name", "Adam");
        map.store("surname", "Dykes");
        Integer matchingMode = -1;
        String result = engine.evaluate("Hello ${name} ${SURNAME}", map, matchingMode);
        assertEquals("Hello Adam Dykes", result);
    }

    @Test
    public void templateMoreThanSevenMatchingMode() {
    		map.store("name", "Adam");
        map.store("surname", "Dykes");
        Integer matchingMode = 8;
        String result = engine.evaluate("Hello ${name} ${SURNAME}", map, matchingMode);
        assertEquals("Hello Adam Dykes", result);
    }

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
        String result = engine.evaluate("${middlename} has a son ${middlename} and a brother ${middle  name}", map, matchingMode);
        assertEquals("Peter has a son Peter and a brother ", result);
    }

    @Test
    public void evaluateBoundary() { //probably useless
        map.store("name", "Adam");
        map.store("surname", "Dykes");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("Hello ${${name} }${surname}", map, matchingMode);
        assertEquals("Hello ${Adam }Dykes", result);
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

    /*
    -------------- SimpleTemplateEngine Tests ----------------------
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

    @Test
    public void simpleNullMatchingMode() {
    		String template = "David has a son David and a brother David.";
        String pattern = "David";
        String value = "Tom";
        Integer matchingMode = null;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("Tom has a son Tom and a brother Tom.", result);
    }

    @Test
    public void simpleNegativeMatchingMode() {
    		String template = "David has a son David and a brother David.";
        String pattern = "David";
        String value = "Tom";
        Integer matchingMode = -1;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("Tom has a son Tom and a brother Tom.", result);
    }

    @Test
    public void simpleLargePositiveMatchingMode() {
    		String template = "David has a son David and a brother David.";
        String pattern = "David";
        String value = "Tom";
        Integer matchingMode = 4;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("Tom has a son Tom and a brother Tom.", result);
    }

    @Test
    public void simpleEvaluateReplaceTenth() {
        String template = "David David David David David David David David David David.";
        String pattern = "David#10";
        String value = "Tom";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("David David David David David David David David David Tom.", result);
    }

    @Test
    public void simpleEvaluateInvalidReplacement() {
    		String template = "David has a son David and a brother David.";
        String pattern = "David#a";
        String value = "Tom";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals(template, result);
    }

    @Test
    public void simpleInvalidPattern() {
    		String template = "David has a son David and a brother David.";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template, "#", "Tom", matchingMode);
        assertEquals(template, result);
    }

    @Test
    public void simpleEvaluatePrefixedWholeWordSearch() {
        String template = "localVARIABLE int localId = alocal";
        String pattern = "local";
        String value = "global";
        Integer matchingMode = SimpleTemplateEngine.WHOLE_WORLD_SEARCH;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("localVARIABLE int localId = alocal", result);
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