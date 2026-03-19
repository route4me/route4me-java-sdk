package com.route4me.sdk.queryconverter;

import org.apache.http.NameValuePair;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class QueryConverterTest {

    static class TestPojo {
        @QueryParameter("name")
        String name;

        @QueryParameter("active")
        Boolean active;

        @QueryParameter("inactive")
        Boolean inactive;

        @QueryParameter("count")
        Integer count;

        @QueryParameter("big")
        Long big;

        @QueryParameter("tags")
        String[] tags;

        @QueryParameter("items")
        List<String> items;

        String noAnnotation;

        @QueryParameter("nullable")
        String nullable;
    }

    @Test
    public void testStringField() throws IllegalAccessException {
        TestPojo pojo = new TestPojo();
        pojo.name = "test";
        List<NameValuePair> params = QueryConverter.convertObjectToParameters(pojo);
        assertTrue(params.stream().anyMatch(p -> p.getName().equals("name") && p.getValue().equals("test")));
    }

    @Test
    public void testNullFieldSkipped() throws IllegalAccessException {
        TestPojo pojo = new TestPojo();
        pojo.nullable = null;
        List<NameValuePair> params = QueryConverter.convertObjectToParameters(pojo);
        assertTrue(params.stream().noneMatch(p -> p.getName().equals("nullable")));
    }

    @Test
    public void testBooleanTrueConvertsToOne() throws IllegalAccessException {
        TestPojo pojo = new TestPojo();
        pojo.active = true;
        List<NameValuePair> params = QueryConverter.convertObjectToParameters(pojo);
        assertTrue(params.stream().anyMatch(p -> p.getName().equals("active") && p.getValue().equals("1")));
    }

    @Test
    public void testBooleanFalseConvertsToFalse() throws IllegalAccessException {
        TestPojo pojo = new TestPojo();
        pojo.inactive = false;
        List<NameValuePair> params = QueryConverter.convertObjectToParameters(pojo);
        assertTrue(params.stream().anyMatch(p -> p.getName().equals("inactive") && p.getValue().equals("false")));
    }

    @Test
    public void testArrayFieldMultipleParameters() throws IllegalAccessException {
        TestPojo pojo = new TestPojo();
        pojo.tags = new String[]{"a", "b", "c"};
        List<NameValuePair> params = QueryConverter.convertObjectToParameters(pojo);
        long count = params.stream().filter(p -> p.getName().equals("tags")).count();
        assertEquals(3, count);
    }

    @Test
    public void testIterableFieldMultipleParameters() throws IllegalAccessException {
        TestPojo pojo = new TestPojo();
        pojo.items = Arrays.asList("x", "y");
        List<NameValuePair> params = QueryConverter.convertObjectToParameters(pojo);
        long count = params.stream().filter(p -> p.getName().equals("items")).count();
        assertEquals(2, count);
    }

    @Test
    public void testFieldWithoutAnnotationSkipped() throws IllegalAccessException {
        TestPojo pojo = new TestPojo();
        pojo.noAnnotation = "should be skipped";
        List<NameValuePair> params = QueryConverter.convertObjectToParameters(pojo);
        assertTrue(params.stream().noneMatch(p -> p.getName().equals("noAnnotation")));
    }

    @Test
    public void testIntegerConversion() throws IllegalAccessException {
        TestPojo pojo = new TestPojo();
        pojo.count = 42;
        List<NameValuePair> params = QueryConverter.convertObjectToParameters(pojo);
        assertTrue(params.stream().anyMatch(p -> p.getName().equals("count") && p.getValue().equals("42")));
    }

    @Test
    public void testLongConversion() throws IllegalAccessException {
        TestPojo pojo = new TestPojo();
        pojo.big = 123456789L;
        List<NameValuePair> params = QueryConverter.convertObjectToParameters(pojo);
        assertTrue(params.stream().anyMatch(p -> p.getName().equals("big") && p.getValue().equals("123456789")));
    }

    @Test
    public void testAllNullsReturnsEmpty() throws IllegalAccessException {
        TestPojo pojo = new TestPojo();
        List<NameValuePair> params = QueryConverter.convertObjectToParameters(pojo);
        assertTrue(params.isEmpty());
    }
}
