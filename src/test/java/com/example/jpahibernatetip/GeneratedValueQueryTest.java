package com.example.jpahibernatetip;

import com.example.jpahibernatetip.domain.simple.PersistableSimple;
import com.example.jpahibernatetip.domain.simple.PersistableSimpleRepository;
import com.example.jpahibernatetip.domain.simple.Simple;
import com.example.jpahibernatetip.domain.simple.SimpleRepository;
import com.example.jpahibernatetip.utils.QueryAssertions;
import com.example.jpahibernatetip.utils.QueryAssertionsConfig;
import com.example.jpahibernatetip.utils.support.QueryAssertion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryAssertionsConfig.class})
class GeneratedValueQueryTest {

    @Autowired
    private SimpleRepository simpleRepository;
    @Autowired
    private PersistableSimpleRepository persistableSimpleRepository;

    @Test
    @DisplayName("isNew 메소드를 오버라이딩 하여 쿼리가 1번 발생")
    @Transactional
    void should_1query_then_isNew_Override() {
        QueryAssertion queryAssertion = QueryAssertions.assertThat(() -> {
            persistableSimpleRepository.save(new PersistableSimple("1"));
            persistableSimpleRepository.flush();
        });
        queryAssertion.isExecuteCountTo(1);
    }

    @Test
    @DisplayName("id를 직접 할당 하는경우 쿼리가 2번 발생.")
    @Transactional
    void should_2query_then_NotGeneratedValue() {

        QueryAssertion queryAssertion = QueryAssertions.assertThat(() -> {
            simpleRepository.save(new Simple("1"));
            simpleRepository.flush();
        });
        queryAssertion.isExecuteCountTo(2);
    }
}
