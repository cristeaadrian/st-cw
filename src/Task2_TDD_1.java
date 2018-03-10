import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import st.EntryMap;
import st.SimpleTemplateEngine;
import st.TemplateEngine;

import java.util.Calendar;

public class Task2_TDD_1 {

    private EntryMap map;

    private TemplateEngine engine;

    private SimpleTemplateEngine simpleEngine;

    @Before
    public void setUp() {
        map = new EntryMap();
        engine = new TemplateEngine();
        simpleEngine = new SimpleTemplateEngine();
    }

    /* Spec 1:
     * If X is not a number or a negative number then there should be no special treatment of the template ${year}.
     */
    @Test
    public void notNumber() {
        map.store("year", "in asdf years");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("I was born in ${year}", map, matchingMode);
        assertEquals("I was born in in asdf years", result);

        map.update("year", "asdf years ago");
        result = engine.evaluate("I was born in ${year}", map, matchingMode);
        assertEquals("I was born in asdf years ago", result);
    }

    @Test
    public void negativeNumber() {
        map.store("year", "in -10 years");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("I was born in ${year}", map, matchingMode);
        assertEquals("I was born in in -10 years", result);

        map.update("year", "-10 years ago");
        result = engine.evaluate("I was born in ${year}", map, matchingMode);
        assertEquals("I was born in -10 years ago", result);
    }

    /* Spec 2:
     * If X is zero then the replacement value should be the current year.
     */

    @Test
    public void valueIsZero() {
        Calendar now = Calendar.getInstance();
        Integer currentYear = now.get(Calendar.YEAR);

        map.store("year", "in 0 years");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("I was born in ${year}", map, matchingMode);
        assertEquals("I was born in " + Integer.toString(currentYear), result);

        map.update("year", "0 years ago");
        result = engine.evaluate("I was born in ${year}", map, matchingMode);
        assertEquals("I was born in " + Integer.toString(currentYear), result);
    }

    /* Spec 3:
     * If the EntryMap class used contains the template ${base year} with a positive integer as a replacement value,
     * then this number should be used to calculate the replacement value of ${year} instead of the current year.
     * In all other cases, the referenced year is the current year.
     */

    @Test
    public void containsBaseYearNegative() {
        Calendar now = Calendar.getInstance();
        Integer currentYear = now.get(Calendar.YEAR);

        map.store("year", "in 2 years");
        map.store("base_year", "-1990");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("I was born in ${year}", map, matchingMode);
        assertEquals("I was born in " + Integer.toString(currentYear + 2), result);

        map.update("year", "2 years ago");
        result = engine.evaluate("I was born in ${year}", map, matchingMode);
        assertEquals("I was born in " + Integer.toString(currentYear - 2), result);
    }

    @Test
    public void containsBaseYearEmpty() {
        Calendar now = Calendar.getInstance();
        Integer currentYear = now.get(Calendar.YEAR);

        map.store("year", "in 2 years");
        map.store("base_year", "");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("I was born in ${year}", map, matchingMode);
        assertEquals("I was born in " + Integer.toString(currentYear + 2), result);

        map.update("year", "2 years ago");
        result = engine.evaluate("I was born in ${year}", map, matchingMode);
        assertEquals("I was born in " + Integer.toString(currentYear - 2), result);
    }

    @Test
    public void containsBaseYearNull() {
        Calendar now = Calendar.getInstance();
        Integer currentYear = now.get(Calendar.YEAR);

        map.store("year", "in 2 years");
        map.store("base_year", null);
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("I was born in ${year}", map, matchingMode);
        assertEquals("I was born in " + Integer.toString(currentYear + 2), result);

        map.update("year", "2 years ago");
        result = engine.evaluate("I was born in ${year}", map, matchingMode);
        assertEquals("I was born in " + Integer.toString(currentYear - 2), result);
    }

    @Test
    public void containsBaseYearOther() {
        Calendar now = Calendar.getInstance();
        Integer currentYear = now.get(Calendar.YEAR);

        map.store("year", "in 2 years");
        map.store("base_year", "something");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("I was born in ${year}", map, matchingMode);
        assertEquals("I was born in " + Integer.toString(currentYear + 2), result);

        map.update("year", "2 years ago");
        result = engine.evaluate("I was born in ${year}", map, matchingMode);
        assertEquals("I was born in " + Integer.toString(currentYear - 2), result);
    }

    /* Check that all usual input values work correctly
     * TODO: Check special cases
     */

    @Test
    public void exampleOne() {
        Calendar now = Calendar.getInstance();
        Integer currentYear = now.get(Calendar.YEAR);

        map.store("year", "5 years ago");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("My team won the cup in ${year}", map, matchingMode);
        assertEquals("My team won the cup in " + Integer.toString(currentYear - 5), result);
    }

    @Test
    public void exampleTwo() {
        map.store("year", "in 2 years");
        map.store("base_year", "1990");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("I was born in ${year}", map, matchingMode);
        assertEquals("I was born in 1992", result);
    }

    @Test
    public void exampleThree() {
        map.store("year", "in 2 years");
        map.store("base_year", "1990");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("I was born in ${year} ${base_year}", map, matchingMode);
        assertEquals("I was born in 1992 1990", result);
    }

}