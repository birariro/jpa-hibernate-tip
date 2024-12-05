package com.example.jpatipsample.utils;


import com.example.jpatipsample.utils.support.Counter;

/**
 * 쿼리의 실행 수를 테스트 할수있게 한다.
 */
public class QueryAssertions {

    private static final ThreadLocal<Counter> counter = new ThreadLocal<>();

    public static QueryAssertions assertThat(Runnable executable) {
        Counter counter = QueryAssertions.counter.get();
        counter.init();
        executable.run();
        return new QueryAssertions();
    }

    public static Counter getCounter() {
        counter.set(new Counter());
        return counter.get();
    }

    public void isCountTo(int expected) {

        Counter counter = QueryAssertions.counter.get();
        int queryCount = counter.getCount();

        QueryAssertions.counter.remove();

        if (queryCount != expected) {
            throw new IllegalArgumentException(String.format("""
                                        
                    Expected: %s
                    Actual: %s""", expected, queryCount));
        }
    }
}