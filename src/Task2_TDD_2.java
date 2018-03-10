import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import st.EntryMap;
import st.SimpleTemplateEngine;
import st.TemplateEngine;

public class Task2_TDD_2 {

    private EntryMap map;

    private TemplateEngine engine;

    private SimpleTemplateEngine simpleEngine;

    @Before
    public void setUp() {
        map = new EntryMap();
        engine = new TemplateEngine();
        simpleEngine = new SimpleTemplateEngine();
    }

}