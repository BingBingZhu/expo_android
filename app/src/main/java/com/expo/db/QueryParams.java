package com.expo.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryParams {

    private List<Selection> projections = new ArrayList<>();
    private List<Selection> restrictions = new ArrayList<>();
    private static final List<String> restrictionTypes;
    private static final List<String> projectionTypes;

    static {
        restrictionTypes = Arrays.asList("orderBy", "groupBy", "limit", "distinct", "having");
        projectionTypes = Arrays.asList("and", "or", "eq", "ne", "ge", "gt", "le", "lt", "like", "between", "in", "notIn", "null", "notNull");
    }

    public QueryParams add(String type, String columnName, Object value1, Object value2) {
        if (restrictionTypes.contains(type)) {
            restrictions.add(new Selection(type, columnName, value1, value2));
        } else if (projectionTypes.contains(type)) {
            projections.add(new Selection(type, columnName, value1, value2));
        } else {
            throw new UnsupportedOperationException("Unsupported operation:" + type);
        }
        return this;
    }

    public QueryParams add(QueryParams queryParams) {
        if (queryParams.restrictions != null && !queryParams.restrictions.isEmpty())
            this.restrictions.addAll(queryParams.restrictions);
        if (queryParams.projections != null && !queryParams.projections.isEmpty())
            this.projections.addAll(queryParams.projections);
        return this;
    }

    public QueryParams add(String type, String columnName, Object value1) {
        return add(type, columnName, value1, null);
    }

    public QueryParams add(String type, Object value1) {
        return add(type, null, value1, null);
    }

    public QueryParams add(String type, Object value1, Object value2) {
        return add(type, null, value1, value2);
    }

    public QueryParams add(String type, String columnName) {
        return add(type, columnName, null, null);
    }

    public QueryParams add(String type) {
        return add(type, null, null, null);
    }

    public List<Selection> getProjections() {
        return projections;
    }

    public List<Selection> getRestrictions() {
        return restrictions;
    }

    public class Selection {
        public String type;
        public String columnName;
        public Object value1;
        public Object value2;

        Selection(String type, String columnName, Object value1, Object value2) {
            this.type = type;
            this.columnName = columnName;
            this.value1 = value1;
            this.value2 = value2;
        }
    }
}
