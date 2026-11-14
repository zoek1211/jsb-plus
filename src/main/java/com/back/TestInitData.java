package com.back;

import com.back.question.Question;
import com.back.question.QuestionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;


// 샘플 데이터를 만드는 클래스 (테스트 + 개발 환경).
// Creates sample data (for tests and for local development).
//
// @Profile({"dev", "test"}): dev 또는 test 프로필일 때만 이 클래스를 사용한다.
// 처음(006-02)에는 "test" 만이었지만, 웹 화면(011~)을 만들면서 bootRun(dev)에서도 볼 데이터가 필요해 dev 를 추가했다 (14163 27강).
// 운영(prod) 같은 다른 프로필에서는 실행되지 않는다. count() > 0 검사 덕분에 앱을 다시 켜도 중복 저장되지 않는다.
// @Profile({"dev", "test"}): use this class only when the dev or test profile is active.
// It started as "test" only (006-02); once we built web pages (011+), bootRun (dev) needed data to show, so dev was added (14163 lecture 27).
// It never runs in other profiles such as prod. The count() > 0 check keeps restarts from inserting duplicates.
@Profile({"dev", "test"})
// @Configuration: 스프링 설정 클래스. 스프링이 이 클래스의 객체를 만들고, 안의 @Bean 메서드들을 실행해 객체(빈)를 등록한다.
// @Configuration: a Spring configuration class. Spring creates an object of it and runs its @Bean methods to register objects (beans).
@Configuration
// @RequiredArgsConstructor (롬복): final 필드를 받는 생성자를 자동으로 만든다.
// 스프링은 그 생성자를 통해 QuestionRepository 를 넣어준다 ("생성자 주입" — @Autowired 필드 주입보다 권장되는 방식).
// @RequiredArgsConstructor (Lombok): generates a constructor that takes every final field.
// Spring passes in the QuestionRepository through that constructor ("constructor injection", preferred over @Autowired field injection).
@RequiredArgsConstructor
public class TestInitData {

    // ── self: 자기 자신을 가리키는 "프록시" ─────────────────────────────────────────
    // @Transactional 은 스프링이 이 객체를 감싼 "프록시(대리 객체)"를 통해 호출될 때만 동작한다.
    //   외부 → [프록시: 트랜잭션 시작] → work1() → [프록시: 트랜잭션 끝(커밋)]
    // 그런데 같은 클래스 안에서 this.work1() 로 부르면 프록시를 거치지 않아 @Transactional 이 무시된다.
    // 그래서 스프링이 만든 프록시(자기 자신)를 self 로 받아서 self.work1() 로 호출한다.
    // @Lazy: 아직 만들어지는 중인 자기 자신을 바로 넣을 수 없으므로(순환 참조), 실제로 쓸 때 꺼내 오는 "지연 객체"를 넣는다.
    // ── self: a "proxy" pointing at this same object ──────────────────────────────
    // @Transactional only works when the call goes through the "proxy" (a stand-in object) Spring wraps around this object:
    //   caller → [proxy: begin transaction] → work1() → [proxy: end transaction (commit)]
    // A plain this.work1() call inside the same class skips the proxy, so @Transactional is silently ignored.
    // So we receive Spring's proxy of ourselves as self and call self.work1().
    // @Lazy: we can't inject ourselves while we're still being created (a circular reference), so Spring injects a lazy stand-in that fetches the real proxy on first use.
    @Autowired
    @Lazy
    private TestInitData self;

    // 질문을 저장할 리포지터리. final + @RequiredArgsConstructor → 생성자 주입.
    // The repository used to save questions. final + @RequiredArgsConstructor → constructor injection.
    private final QuestionRepository questionRepository;

    // @Bean: 이 메서드가 반환하는 객체를 스프링 빈으로 등록한다.
    // ApplicationRunner: 앱(스프링)이 완전히 시작된 직후 한 번 실행되는 코드. 샘플 데이터를 넣기 딱 좋은 시점이다.
    // @Bean: registers the object this method returns as a Spring bean.
    // ApplicationRunner: code that runs once, right after the app (Spring) has fully started. The perfect moment to insert sample data.
    @Bean
    // @Order(1): 여러 ApplicationRunner 중 가장 먼저 실행. DevInitData(@Order(2)) 보다 먼저 기본 샘플 2개를 넣기 위함 (022-01).
    // @Order(1): runs first among the ApplicationRunners, so the 2 base samples go in before DevInitData (@Order(2)) (022-01).
    @Order(1)
    public ApplicationRunner testInitDataApplicationRunner() {
        // args -> { ... } 는 람다(이름 없는 함수). ApplicationRunner 의 run(args) 메서드 내용을 짧게 쓴 것이다.
        // args -> { ... } is a lambda (an unnamed function): a short way to write ApplicationRunner's run(args) method.
        return args -> {
            // this.work1() 이 아니라 self.work1() — 프록시를 거쳐야 @Transactional 이 적용된다 (위 설명 참고).
            // self.work1(), not this.work1(): going through the proxy is what makes @Transactional apply (see above).
            self.work1();
        };
    }

    // @Transactional: 이 메서드 안의 DB 작업을 하나의 "트랜잭션"으로 묶는다.
    // 모두 성공하면 한꺼번에 커밋(확정)하고, 중간에 예외가 나면 전부 롤백(취소)한다 → 질문이 1개만 저장되는 어중간한 상태를 막는다.
    // @Transactional: groups this method's database work into one "transaction".
    // If everything succeeds it commits (makes it permanent) all at once; if an exception happens midway, everything is rolled back (undone)
    // → prevents a half-done state where only one question got saved.
    @Transactional
    void work1() {
        // 이미 데이터가 있으면 다시 넣지 않는다. 같은 테스트 실행에서 스프링이 여러 번 뜨더라도 데이터가 중복되지 않게 한다.
        // Skip if data already exists, so the questions aren't duplicated even if Spring starts more than once in a test run.
        if (questionRepository.count() > 0) return;

        // new 로 만든 객체는 아직 DB와 무관한 평범한 자바 객체다. id 는 int 기본값 0.
        // An object made with new is just a plain Java object, not in the database yet. Its id is the int default, 0.
        Question q1 = new Question();
        // @Setter 덕분에 쓸 수 있는 메서드들 (006-01).
        // Methods available thanks to @Setter (006-01).
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        // save → INSERT SQL 실행. 저장 후 DB가 매긴 번호(1)가 q1.id 에 들어간다.
        // save → runs an INSERT. After saving, the number the database assigned (1) is put into q1.id.
        questionRepository.save(q1);

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        // 2번 질문에 샘플 답변 1개. cascade = PERSIST 덕분에 아래 save(q2) 할 때 답변도 함께 INSERT 된다.
        // One sample answer on question 2. Thanks to cascade = PERSIST, save(q2) below also INSERTs the answer.
        q2.addAnswer("네 자동으로 생성됩니다.");
        questionRepository.save(q2);
    }
}
