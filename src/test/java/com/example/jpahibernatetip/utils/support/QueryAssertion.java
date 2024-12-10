package com.example.jpahibernatetip.utils.support;


import org.springframework.util.StopWatch;

import java.util.HashSet;

public class QueryAssertion {
    private final QueryStorage queryStorage;
    private final Double queryTotalTimeSeconds;

    public QueryAssertion(QueryStorage queryStorage, StopWatch stopWatch) {
        this.queryStorage = queryStorage;
        this.queryTotalTimeSeconds = stopWatch.getTotalTimeSeconds();
    }

    public QueryStorage getQueryStorage() {
        return queryStorage;
    }

    public double getExecuteTimeSeconds() {
        return queryTotalTimeSeconds;
    }

    public void isFast(QueryAssertion other) {
        double actualTime = getExecuteTimeSeconds();
        double otherTime = other.getExecuteTimeSeconds();
        if (actualTime > otherTime) {
            return;
        }
        throw new IllegalArgumentException(String.format("%s is not faster than %s", actualTime, otherTime));
    }

    public void isKindCountTo(int expected) {

        int queryCount = new HashSet<>(queryStorage.getQueryStrings()).size();

        if (queryCount != expected) {
            throw new IllegalArgumentException(String.format("""
                                        
                    Expected: %s
                    Actual: %s""", expected, queryCount));
        }
    }

    public void isExecuteCountTo(int expected) {

        int queryCount = queryStorage.getQueryStrings().size();
        if (queryCount != expected) {
            throw new IllegalArgumentException(String.format("""
                                        
                    Expected: %s
                    Actual: %s""", expected, queryCount));
        }
    }
}

