package com.route4me.sdk.queryconverter;

import org.apache.http.NameValuePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    /** Returns the parameters as {@code name=value} strings, in order, for readable assertions. */
    private static List<String> pairs(List<NameValuePair> params) {
        List<String> pairs = new ArrayList<>();
        for (NameValuePair param : params) {
            pairs.add(param.getName() + "=" + param.getValue());
        }
        return pairs;
    }

    @Test
    public void bracketedListsUseEmptyBrackets() {
        assertEquals(Arrays.asList("ids[]=1", "ids[]=2"),
                pairs(QueryConverter.toBracketedParameters("ids", Arrays.asList("1", "2"))));
        assertEquals("one value is still sent as a list", Collections.singletonList("ids[]=1"),
                pairs(QueryConverter.toBracketedParameters("ids", Collections.singletonList("1"))));
        assertEquals(Arrays.asList("ids[]=a", "ids[]=b"),
                pairs(QueryConverter.toBracketedParameters("ids", new String[]{"a", null, "b"})));
        assertEquals(Arrays.asList("ids[]=1", "ids[]=2"),
                pairs(QueryConverter.toBracketedParameters("ids", new long[]{1L, 2L})));
        assertTrue(QueryConverter.toBracketedParameters("ids", Collections.emptyList()).isEmpty());
    }

    @Test
    public void bracketedMapsUseTheirKeys() {
        Map<String, Object> filters = new LinkedHashMap<>();
        filters.put("status", Arrays.asList(1, 2));
        filters.put("search", "x");
        filters.put("skipped", null);
        Map<String, Object> range = new LinkedHashMap<>();
        range.put("start", 10);
        filters.put("range", range);

        assertEquals(Arrays.asList("filters[status][]=1", "filters[status][]=2", "filters[search]=x",
                        "filters[range][start]=10"),
                pairs(QueryConverter.toBracketedParameters("filters", filters)));
    }

    @Test
    public void bracketedNestedValuesAreIndexed() {
        List<List<String>> orderBy = Arrays.asList(Arrays.asList("name", "asc"), Arrays.asList("id", "desc"));
        Map<String, String> byName = Collections.singletonMap("name", "asc");

        assertEquals(Arrays.asList("order_by[0][]=name", "order_by[0][]=asc", "order_by[1][]=id", "order_by[1][]=desc"),
                pairs(QueryConverter.toBracketedParameters("order_by", orderBy)));
        assertEquals(Collections.singletonList("order_by[0][name]=asc"),
                pairs(QueryConverter.toBracketedParameters("order_by", Collections.singletonList(byName))));
    }

    @Test
    public void bracketedScalarsKeepTheirNameAndFlagsAreOneOrZero() {
        assertEquals(Collections.singletonList("page=2"), pairs(QueryConverter.toBracketedParameters("page", 2)));
        assertEquals(Collections.singletonList("on=1"), pairs(QueryConverter.toBracketedParameters("on", true)));
        assertEquals(Collections.singletonList("off=0"), pairs(QueryConverter.toBracketedParameters("off", false)));
        assertEquals(Arrays.asList("flags[]=1", "flags[]=0"),
                pairs(QueryConverter.toBracketedParameters("flags", Arrays.asList(true, false))));
        assertTrue(QueryConverter.toBracketedParameters("none", null).isEmpty());
    }

    @Test
    public void bracketedObjectConversionReadsTheAnnotatedFields() throws IllegalAccessException {
        TestPojo pojo = new TestPojo();
        pojo.name = "test";
        pojo.inactive = false;
        pojo.tags = new String[]{"a"};
        pojo.items = Arrays.asList("x", "y");
        pojo.noAnnotation = "skipped";

        assertEquals(Arrays.asList("name=test", "inactive=0", "tags[]=a", "items[]=x", "items[]=y"),
                pairs(QueryConverter.convertObjectToBracketedParameters(pojo)));
    }

    @Test
    public void testAllNullsReturnsEmpty() throws IllegalAccessException {
        TestPojo pojo = new TestPojo();
        List<NameValuePair> params = QueryConverter.convertObjectToParameters(pojo);
        assertTrue(params.isEmpty());
    }
}
