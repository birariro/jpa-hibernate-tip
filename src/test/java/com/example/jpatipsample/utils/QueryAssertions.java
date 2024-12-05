package com.example.jpatipsample.utils;


import com.example.jpatipsample.utils.support.QueryAssertion;
import com.example.jpatipsample.utils.support.QueryStorage;
import org.springframework.util.StopWatch;

/**
 * 스레드별로 쿼리의 상태를 추적한다
 */
public class QueryAssertions {

    private static final ThreadLocal<QueryStorage> queryStorages = ThreadLocal.withInitial(QueryStorage::new);
    private static final ThreadLocal<StopWatch> stopWatchs = ThreadLocal.withInitial(StopWatch::new);

    public static QueryAssertion assertThat(Runnable executable) {

        StopWatch stopWatch = QueryAssertions.stopWatchs.get();
        QueryStorage queryStorage = QueryAssertions.queryStorages.get();
        queryStorage.clear();

        stopWatch.start();
        executable.run();
        stopWatch.stop();
        
        return new QueryAssertion(queryStorage, stopWatch);
    }

    public static QueryStorage getQueryStorage() {
        return QueryAssertions.queryStorages.get();
    }


}

