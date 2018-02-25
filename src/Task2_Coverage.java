/*
Software Testing Coursework 1 - Part 2: Coverage Analysis (20 Marks)
Collaborators: Adrian Cristea (s1449640) & Andra Zaharia (s1402967)

Specification:
1. Take the JUnit tests you developed in "Task 1 Functional Testing", and measure the branch coverage achieved.
Run the JUnit tests on the source code of the implementation, which is available in the folder "src/st" in the Github repository.
Submit a screenshot showing the coverage achieved by tests developed in "Part 1 Functional Testing", as reported by a coverage measurement tool.

2. Attempt to improve the branch coverage achieved over the source code to "maximum possible" by adding more tests.
You can look at the source code and its structure to guide the development of additional tests.

Please note that "maximum possible" branch coverage may be less than 100% since there may be parts of the code that
are unreachable in the provided implementation.

Re-assess branch coverage achieved with these additional tests along with tests from Part 1.

Please submit all the JUnit tests used to achieve maximum coverage, and a screenshot showing the improved branch coverage
as reported by a coverage measurement tool like EclEmma.
 */

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import st.EntryMap;
import st.SimpleTemplateEngine;
import st.TemplateEngine;

public class Task2_Coverage {

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

    /*
    -------------- SimpleTemplateEngine Tests ----------------------
     */
    
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
        assertEquals("localVARIABLE int localId = aglobal", result);
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