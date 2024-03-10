# Spring이 지원하는 기술 
## IoC/DI
### IoC(Inverse of Control, 제어의 역전)
- 정의: 개발자가 객체 관리, 호출 등을 맡는 기존의 개발 방식이 아닌, 외부의 프레임워크(ex. 스프링)가 그 제어권을 갖음으로써 프로그램의 제어 흐름이 역전되는 구조
- 대표적인 구현 방법
    - DI(Dependency Injection, 의존성 주입): 객체를 내부에서 직접 생성하는 것이 아닌, 외부에서 객체를 생성한 후 주입
    - DL(Dependency Lookup, 의존성 검색): Bean에 접근하기 위해 컨테이너에서 제공하는 API를 이용하여 Bean을 개발자가 직접 검색하는 것

### DI
- 정의: IoC를 구현하는 방법 중 하나로, IoC 컨테이너가 객체인 Bean을 생성하고 관리하며 어플리케이션은 외부에서 생성된 Bean을 주입받는 방식
- 구현 방법
    - constructor 주입
    - setter 주입
    - field 주입
    
- 예제코드1 (contructor 주입)  
```java
public class ApplepieRecipe{
    Apple apple;
    public ApplepieRecepie() {
        apple = new GreenApple();
    }
}
```
위와 같이 작성하면 ApplepieRecipe 클래스 내부에서 생성자를 통해 apple을 생성하기 때문에 모든 apple은 GreenApple만 가능하다.
만일 apple을 다른 클래스인 RedApple로 바꾸고 싶다면 ApplepieRecipe 클래스에서 생성자를 직접 아래와 같이 수정해야 하기 때문에 코드의 결합성이 높아서 확장성이 떨어진다. 
```java
public class ApplepieRecipe{
    Apple apple;
    public ApplepieRecepie() {
        apple = new RedApple();
    }
}
```
하지만, 아래와 같이 'constructor 주입' 방식으로 구현하면, 외부에서 생성자의 인자를 결정함으로써 의존성을 주입할 수 있다.
```jav
public class ApplepieRecipe{
    Apple apple;
    
    public ApplepieRecipe(Apple apple) {
        this.apple = apple;
    }
}

//외부
Apple apple = new RedApple();
ApplepieRecipe applepierecipe = new ApplepieRecipe(apple); //생성자 주입
```

- 예제코드2 (setter 주입)  
아래와 같이 작성하면 setter를 통해 외부에서 원하는 클래스로 결정함으로써 의존성을 주입할 수 있다. 
```java
public class ApplepieRecipe{
    Apple apple;
    public void setApple(Apple apple){
        this.apple = apple;
    }
    
}
//외부
ApplepieRecipe applepierecipe = new ApplepieRecipe();
Apple apple = new RedApple; //여기서 원하는 apple 결정 가능
applepierecipe.setApple(apple); //수정자 주입
```

- 예제코드3 (field 주입)  
Spring에서 @Autowired이라는 어노테이션을 통해 field 주입 방식으로 DI 구현이 가능하다.
@Autowired는 해당 어노테이션이 붙은 field에 Bean 객체를 매핑하여 자동으로 의존성을 주입해주는 역할을 한다.(단, final로 선언된 field에는 @Autowired 사용이 불가하다) 
```java
import org.springframework.beans.factory.annotation.Autowired;

public class ApplepieRecipe {
    @Autowired //자동으로 의존성 주입
    Apple apple;

}

//외부
import org.springframework.stereotype.Component;

@Bean
public class GreenApple implements Apple {
    // ...
}
```
이 방법은 간편하다는 장점이 있지만, 의존관계가 외부로 명확히 드러나지 않는다는 단점이 있어서 한번 주입된 의존성을 변경하지 않고 유지하는 경우에 적합하다. 


## AOP(Aspect-Oriented Programming, 관점 지향 프로그래밍)
- 정의: 각 모듈 별 핵심 로직에 해당하는 '핵심 관심사'에서 여러 모듈에 공통적으로 나타나는 '횡단 관심사(Crosscutting  Concerns)'별로 로직을 모듈화하는 것. 즉, 관점별로 모듈화하고 '핵심 비즈니스 로직'에서 분리하여 코드의 간결성을 높이고 확장하는 것이 AOP의 취지이다. 
- AOP 적용방식
    - 컴파일 시점: 코드에 '횡단 관심사' 삽입
        - AspectJ와 같은 AOP 전용 개발 도구를 사용해서 컴파일 시점에 '횡단 관심사' 코드를 삽입
    - 클래스 로딩 시점(로드 타임): 바이트 코드에 '횡단 관심사' 삽입
        - AspectJ와 같은 AOP 전용 개발 도구를 사용해서 클래스를 로딩하는 시점에 '횡단 관심사' 코드를 삽입
    - 런타임: 프록시 객체를 생성해서 '횡단 관심사' 삽입
        - 앞선 두 방식과 달리 이 방식은 Spring AOP에서 지원 가능
        - 프록시 객체 생성 -> 그 안에 실제 객체 삽입 -> 실제 객체를 통해 '핵심 관심사'를 실행하고 프록시 객체를 통해 '횡단 관심사'를 처리

- Spring AOP
    - '프록시 패턴 기반'의 AOP 구현체로, target을 감싸는 프록시는 런타임 시점에 생성하여 제공한다. 
    - 프록시가 target 객체의 호출을 가로채 Advice(AOP에서 실제로 적용하는 기능, '횡단 관심사'를 언제 '핵심 관심사'에 적용할지를 정의) 수행 전후 '핵심 관심사' 로직 호출
        - Advice 어노테이션에는 동작시점에 따른 세분화가 필요
            - @Before: target의 메소드 실행 전에 '횡단 관심사' 적용
            - @After: target의 메소드 실행 후에 '횡단 관심사' 적용
            - @AfterReturning: target의 메소드 실행 후 정상 종료시(예외 발생X) '횡단 관심사' 적용
            - @AfterThrowing: target의 메소드 실행 후 예외 발생시 '횡단 관심사' 적용
            - @Around: target의 메소드 실행 전/후, 예외 발생 시점 등 다양한 시점에서 '횡단 관심사' 적용 -> 주로 사용됨!
            
- 예시코드  
아래는 '핵심 관심사' 로직을 구현한 코드이다.
```java
public interface HelloWorld {
    void print();
}

@Component
public class Hello implements HelloWorld {
    @Override
    public void print(){
        System.out.println("Hello");
    }
}
@Component
public class World implements HelloWorld {
    @Override
    public void print(){
        System.out.println("World!");
    }
}
```
아래는 '횡단 관심사' 로직에 해당하는 Aspect 클래스를 구현한 코드이다.
```java
@Component //Aspect 클래스를 Bean으로 등록
@Aspect //해당 클래스가 Aspect임을 명시
public class AspectTest{
    
    //PointCut(실제 Advice가 적용되는 JoinPoint) 설정
    @Pointcut("execution(public void HelloWorld.print())") //System.out.println("World!");
    private void Target() {}
    
    //Advice 설정
    @Around("Target()") 
    //ProceedingJoinPoint: Advice가 적용되는 대상을 가리키는 클래스
    //proceed(): 실제 객체의 메소드를 실행시키는 메소드
    public Object measure(ProceedingJoinPoint joinPoint) throws Throwable{ 
        System.out.println("이전");
        Object result = joinPoint.proceed(); //'횡단 관심사'로직 실행
        System.out.println("이후");
        return result;
    }
}
```
위 코드를 실행시키면 아래와 같은 출력 결과가 나온다.
```
이전
Hello!
이후
이전
World!
이후
```
    
## PSA (Portable Service Abstraction, 일관성 있는 서비스 추상화)
- 정의: 개발 환경의 변화와 상관없이 일관된 방식의 기술로의 접근 환경을 제공하는 추상화 구조 -> 특정 클래스가 추상화된 상위 클래스를 일관되게 바라보며 하위 클래스의 기능을 사용하는 것이 기본 개념
    - cf) 서비스의 추상화란? 개발자에게 필요한 부분만 드러내어 개발자가 실제 구현부를 알지 못하더라도 해당 기능을 쉽게 사용할 수 있는 편의성을 제공하는 것
- 특징: POJO 원칙을 철저히 따른 Spring의 기능으로, Spring에서 동작할 수 있는 library들은 POJO원칙을 지키게끔 PSA 형태의 추상화가 되어있음
- 사례: DB 접근 툴에는 JDBC, ORM 기반의 JPA 등이 있다. 하지만 어떤 기술을 사용하더라도 @Transactional 을 이용하면 일관된 방식으로 DB에 접근하도록 하는 인터페이스가 제공된다. 이와 같이 추상화 계층을 추가하여 서비스를 추상화하고 여러 서비스를 직접 비즈니스 로직을 수정하지 않아도 교체할 수 있다. 

---
# Spring Bean
## Spring Bean이란
- 정의: Spring 컨테이너가 생성하고 관리하는 객체이다. Bean이 필요한 가장 큰 이유는 스프링 간 객체가 의존관계를 관리하도록 하기 위함이다. 객체가 의존관계를 등록할 때 스프링 컨테이너에서 해당하는 빈을 찾고, 그 빈과 의존성을 만든다. 
- 등록 방식: Spring은 Bean을 컨테이너에 등록하기 위해 XML 파일에서 설정하거나, 어노테이션으로 등록한다. 
    - 클래스를 Bean으로 등록: @Component -> 자동으로 클래스를 스캔하여 Bean으로 등록
    - 메소드를 Bean으로 등록: @Bean -> 개발자가 명시적으로 Bean의 생성과 구성을 제어하고자 할 때
    
## Bean의 라이프사이클
- lifecycle: Spring IoC 컨테이너 생성 -> Spring Bean 생성 -> 의존성 주입 -> 초기화 콜백 메소드 호출 -> 사용 -> 소멸 전 콜백 메소드 호출 -> Spring 종료
    - 초기화 콜백: Bean이 생성되고 의존성 주입된 이후에 호출되는 메소드
    - 소멸 전 콜백: Bean이 소멸되기 전에 직전에 호출되는 메소드
- Bean의 생성과 초기화가 분리되어야 하는 이유: 생성자는 필수 정보(파라미터)를 받은 다음, 메모리를 할당해서 객체를 생성하는 역할을 한다. 반면, 초기화는 이렇게 생성된 값들을 활용하여 외부 커넥션을 연결하는 등 무거운 동작을 수행한다. 따라서, 생성자 내부에서 무거운 초기화 작업을 함께하는 것보다는 그 둘을 나누는 것이 유지 보수 관점에서 좋다. 

- Bean lifecycle 콜백 방법
    - 인터페이스(InitializingBean, DisposableBean)
    - 설정 정보에 초기화 메소드, 종료 메소드 지정
    - @PostConstruct, @PreDestroy 어노테이션 지원

- 예제코드1 (인터페이스)  
InitializingBean, DisposableBean 인터페이스를 사용하는 초기화 및 종료 방법은 스프링 초창기에 나온 방법들이기 때문에 지금은 거의 사용하지 않는다.
```java
@Component
public class TestComponent implements InitializingBean, DisposableBean {

    @Override
    public void afterPropertiesSet() throws Exception {  // 의존성 주입이 끝나면 호출해주는 콜백
        System.out.println("초기화 콜백 테스트");
    }

    @Override
    public void destroy() throws Exception {//소멸전 호출되는 콜백            
        System.out.println("소멸전 콜백 테스트");
    }
}
```

- 예제코드2 (설정 정보에 초기화 메소드, 종료 메소드 지정)  
메소드명을 자유롭게 부여 가능하고, 설정 정보를 사용하기 때문에 코드를 커스터마이징 할 수 없는 외부라이브러리에서도 적용 가능하다는 장점이 있다. 반면, Bean 지정시 initMethod와 destoryMethod를 직접 지정해야 하기에 번거롭다는 단점도 있다.
```java
public class ExampleBean {     public void initialize() throws Exception {        
    // 초기화 콜백 (의존관계 주입이 끝나면 호출)    
}     
                          
public void close() throws Exception {        
    // 소멸 전 콜백 (메모리 반납, 연결 종료와 같은 과정)    
}} 
@Configuration
class LifeCycleConfig {     
    @Bean(initMethod = "initialize", destroyMethod = "close")    
    public ExampleBean exampleBean() {        
        // 생략    
    }
}
```

- 예제코드3 (@PostConstruct, @PreDestory 어노테이션)  
어노테이션만 붙이면 되므로 매우 편리하고, 최신 Spring에서 가장 많이 사용되는 방법이다. 유일한 단점은 외부 라이브러리에는 적용하지 못한다는 것이다. 외부 라이브러리를 초기화하거나 종료해야 하면 @Bean의 기능을 사용하면 된다.
```java
@Component
public class TestComponent {

    @PostConstruct
    public void initTest() {
        System.out.println("초기화");
    }

    @PreDestroy
    public void destoryTest() {
        System.out.println("종료");
    }
}
```
---
# Spring Annotation
## 어노테이션이란 무엇이며, Java에서 어떻게 구현될까요?
- 정의: Java 소스코드에 추가하여 사용할 수 있는 메타데이터의 일종이다. 클래스 파일에 임베디드되어 컴파일러에 의해 생성된 후 JVM에 포함되어 작동한다.

- 역할
    - 컴파일러에게 문법 에러를 체크하도록 정보를 제공한다.
    - 프로그램을 빌드할 때 코드를 자동으로 생성할 수 있도록 정보를 제공한다.
    - 런타임에 특정 기능을 실행하도록 정보를 제공한다.

- 종류
    - 표준(내장) 어노테이션: Java가 기본적으로 제공해주는 어노테이션 (ex. @Override, @Deprecated), @FunctionalInterface, @SuppressWarnings)
    - 메타 어노테이션: 어노테이션을 위한 어노테이션 (ex. @Target, @Retention, @Documented, @Inherited, @Repeatable)
    - 사용자정의 어노테이션: 사용자가 직접 정의하는 어노테이션
    
- 사용자정의 어노테이션 생성하는 법
```java
//기본 형식
@interface 이름{
    타입 요소 이름(); // 어노테이션의 요소를 선언
    ...
}

//예제
@interface DateTime{
    String yymmdd();
    String hhmmss();
}

@interface TestInfo{
    int count() default 1;
    String testedBy();
    TestType testType();
    DateTime testDate();
}


@TestInfo{
    testedBy="Kim",
    testTools={"JUnit", "AutoTester"},
    testType=TestType.FIRST,
    testDate=@DateTime(yymmdd="210922", hhmmss="211311")
)// count를 생략했으므로 default인 "count=1"이 적용된다.
```

- 어노테이션 규칙
    - 요소의 타입은 기본형 데이터 타입, String, enum, 어노테이션, Class만 허용된다.
    - 괄호()안에 매개변수를 선언할 수 없다.
    - 예외를 선언할 수 없다.
    - 요소의 타입을 매개변수로 정의할 수 없다.
```java
@interface AnnoConfigTest{
    int id = 100; // 기본형 데이터 타입인 상수는 가능
    String major(int i, int j) //매개변수 불가
    String minor() throws Exception; // 예외 선언 불가
    ArrayList<T> list(); // 요소의 타입을 매개변수오 정의 불가
```

## 스프링에서 어노테이션을 통해 Bean을 등록할 때, 어떤 일련의 과정이 일어나는지 탐구해보세요.
- @Bean을 통한 직접 등록
아래와 같이 @Configuration 구성 정보에 @Bean 어노테이션을 통해 Spring 컨테이너에 직접 Bean으로 등록할 수 있다.
Bean 객체로 등록하고 싶은 메서드의 위에 @Bean 어노테이션을 추가하면 된다.
```java
@Configuration
public class AppConfig {
    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
}
```



## @ComponentScan 과 같은 어노테이션을 사용하여 스프링이 컴포넌트를 어떻게 탐색하고 찾는지의 과정을 깊게 파헤쳐보세요.
- @ComponentScan과 @Component를 통한 자동으로 등록
@Configuration이 붙은 클래스에 @ComponentScan를 붙여 @Component가 붙은 객체를 찾아 자동으로 Bean 등록하는 방법도 있다.
1. AppConfig에 @ComponentScan 어노테이션을 추가로 붙인다. 
```java
@Configuration
@ComponentScan
public class AppConfig {
...
}
```

2. Bean 객체로 등록하고 싶은 클래스에 @Component 어노테이션을 추가한다. @Component 어노테이션이 추가되면, 컴포넌트 스캔의 대상이 된다.
```java
@Component
public class MemoryMemberRepository implements MemberRepository {
...
}
```
Spring은 @Configuration 어노테이션을 가진 클래스를 우선 찾는다. -> 해당 클래스에 @ComponentScan 어노테이션이 있다면 @Component 어노테이션이 있는 클래스들을 찾아 Spring 컨테이너에 Bean으로 등록한다.

---
# 단위 테스트와 통합 테스트 탐구
## 단위 테스트
- 정의: 하나의 모듈(각 계층에서의 하나의 기능 or 메소드)을 기준으로 독립적으로 진행되는 가장 작은 단위의 테스트
- 필요성
    - 실제 여러 컴포넌트 간 상호작용을 테스트하기 때문에 모든 컴포넌트들이 구동된 상태에서 실시해야 하는 '통합 테스트'와 달리, 단위 테스트는 테스트하고자 하는 부분만 독립적으로 테스트를 하는 것이기 때문에 특정 단위를 유지 보수 또는 리팩토링하더라도 문제 여부를 빠르게 확인 할 수 있다.

- 한계
    - 복잡한 상호작용이 이뤄지는 실제 어플리케이션에서는 특정 기능에 대한 테스트만 진행하는 '단위 테스트'는 한계점을 갖는다. 
    - 이 한계를 해결하기 위해서는 테스트하고자하는 기능과 연관된 모듈에서 더미 데이터나 정해진 반환값이 필요하다. 즉, 테스트하고자 하는 기능과 연관된 다른 모든 모듈들과의 연결이 단절되어야 비로소 독립적인 단위 테스트가 유의미해진다.

- FIRST 규칙
    - Fast: 테스트는 빠르게 동작하고 자주 가동이 가능해야 한다.
    - Independent: 각각의 테스트는 독립적어이야 하며, 서로에 대한 의존성이 없어야 한다.
    - Repeatable: 어느 환경에서도 반복 테스트가 가능해야 한다.
    - Self-Validating: 테스트는 성공 또는 실패 값으로 결과를 내어 자체적으로 검증되어야 한다.
    - Timely: 테스트하려는 실제 코드를 구현하기 직전에 테스트가 이뤄져야 한다.

## 통합 테스트
- 정의: 모듈을 통합하는 과정에서 모듈 간 호환성 및 상호작용을 확인하기 위한 테스트
- 필요성
    - 실제 어플리케이션에서는 컴포넌트들 간 데이터를 주고 받으며 복잡한 상호작용이 이뤄지므로, 연관된 객체들끼리 올바르게 동작하는지 확인하는 과정이 필요하다.
    - 독립적인 기능보다는 전체적인 연관된 기능과 웹 페이지로부터 API를 호출하여 올바르게 동작하는지 확인할 때 이뤄지는 테스트이다.
    

## 테스트 코드 작성시 유의사항
- 테스트 시 자주 사용되는 라이브러리: JUnit, AssertJ
- Given/When/Then 패턴
    - Given: 어떤 데이터가 주어지면
    - When: 어떤 기능이 실행될 때
    - Then: 어떤 결과를 기대하는지
    
```java
//Given/When/Then 패턴 형식
@Test
@DisplayName("Test")
void test() {
    // Given

    // When

    // Then
  }
```

## Mockito를 사용한 단위 테스트
- Mockito란?  개발자가 동작을 직접적으로 제어하도록 하는 더미 객체를 지원하는 테스트 프레임워크
- 여러 객체 간 의존성이 존재하는 Spring에서 Mockito를 이용하여 의존성을 단절시킨 뒤 단위 테스트를 실행하는 것을 용이하게 해준다.

- 예시코드  
아래는 Controller 계층을 단위 테스트하기 위해 작성된 코드이다.  
Controller와 의존 관계에 있는 객체는 MemberService와 MemberMapper이다.  
Controller 테스트를 위해 필요한 HTTP 호출은 @WebMvcTest 어노테이션으로 해결 가능하다.  
```java
@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class MemberControllerRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean //Controller와 의존 관계에 있는 MemberService에 더미 객체를 생성해줌
    private MemberService memberService;

    @MockBean //Controller와 의존 관계에 있는 MemberMapper에 더미 객체를 생성해줌
    private MemberMapper mapper;

    @Autowired
    private Gson gson;

    @Test
    @DisplayName("POST 회원 등록 컨트롤러 로직 확인")
    public void postMemberTest() throws Exception {
        // Given (어떤 데이터가 주어지면)
        MemberDto.Post post = new MemberDto.Post("hgd@gmail.com", "홍길동", "010-1234-5678");
        String content = gson.toJson(post);

        MemberDto.response responseDto =
                new MemberDto.response(1L,
                        "hgd@gmail.com",
                        "홍길동",
                        "010-1234-5678",
                        Member.MemberStatus.MEMBER_ACTIVE, new Stamp());
        
        //Controller에 의존하고 있는 객체에서 사용되는 메소드들의 반환값을 지정 -> 의존성 단절하여 단위 테스트 가능케 함
        //given(의존관계에 있는 객체의 메소드(파라미터)).willReturn(반환 값)
        given(mapper.memberPostToMember(Mockito.any(MemberDto.Post.class))).willReturn(new Member());
        given(memberService.createMember(Mockito.any(Member.class))).willReturn(new Member());
        given(mapper.memberToMemberResponse(Mockito.any(Member.class))).willReturn(responseDto);

        // When(어떤 기능이 실행될 때)
        //mockMvc의 perform 메소드를 통해 데이터와 함께 POST 요청 보내기
        //요청 정보에는 MockMvcRequestBuilders가 사용됨 -> 메소드 종류 요청 및 응답 데이터 타입, 바디의 데이터 등을 설정할 수 있다.
        //post(),accept(),contentType(),content()메소드 모두 MockMvcRequestBuilders 클래스의 메소드
        ResultActions actions =
                mockMvc.perform(
                        post("/members")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // Then(어떤 결과를 기대하는지)
        //status().isCreated()는 컨트롤러의 단위 테스트 대상 메소드가 응답으로 HttpStatus.CREATED를 반환하기 때문에 응답 코드를 검증하기 위한 메소드
        //응답 데이터 타입은 Json -> jsonPath()를 활용하여 테스트에서 '실제 응답 데이터'와 '기대하는 응답 데이터'를 비교 검증
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value(post.getEmail()))
                .andExpect(jsonPath("$.data.name").value(post.getName()))
                .andExpect(jsonPath("$.data.phone").value(post.getPhone()));
    }
}
```
