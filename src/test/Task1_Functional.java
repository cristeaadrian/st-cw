package test;/*
Software Testing Coursework 1 - Part 1: Functional Testing (20 Marks)
Collaborators: Adrian Cristea (s1449640) & Andra Zaharia (s1402967)

Specification:
The repository provides the implementation as a JAR file, ST_Coursework.jar, so you can execute your JUnit tests
and observe test results. The specification is described in detail, with helpful examples where necessary in the
Specifications.pdf file.

Functional testing is a black box testing technique, so use the specification file to derive tests and not the source code.
The jar file under the jar directory can be used to execute the tests derived from the specification.
We have also provided a sample JUnit test case, test.TemplateEngineTest.java file, to illustrate a typical test case for
the implementation in ST_Coursework.jar.

In giving a grade for this part of the practical I will take into account the performance of your test set
on a collection of variants/mutants of the specification.
 */

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
    public void setUp() throws Exception {
        map = new EntryMap();
        engine = new TemplateEngine();
        simpleEngine = new SimpleTemplateEngine();
    }

    //EntryMap.store() tests
    @Test (expected=RuntimeException.class)
    public void Test1() {
        map.store(null, "Adam");
    }

    @Test (expected=RuntimeException.class)
    public void Test2() {
        map.store("", "Adam");
    }

    @Test (expected=RuntimeException.class)
    public void Test3() {
        map.store("name", null);
    }

    @Test ()
    public void Test6() {
        map.store("name", "Adam");
        ArrayList<EntryMap.Entry> entries = map.getEntries();
        for (Map.Entry entr)
    }


//    public void Test4() {
//        EntryMap map_2 = new EntryMap();
//        assertSame(map_2, map);
//        map_2.store("name", null);
//    }

}