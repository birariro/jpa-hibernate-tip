package com.example.jpahibernatetip.utils.support;

import java.util.ArrayList;
import java.util.List;

public class QueryStorage {

    private List<String> queryStrings;

    public void pushQueryString(String query) {
        if (queryStrings != null) {
            this.queryStrings.add(query);
        }
    }

    public List<String> getQueryStrings() {
        return queryStrings;
    }

    public void clear() {
        queryStrings = new ArrayList<>();
    }
}