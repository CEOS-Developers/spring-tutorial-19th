# spring-tutorial-19th
* * *

## 1. Spring이 지원하는 기술들

### 1.1) DI
DI(Dependency Injection)
- 객체를 직접 생성하지 않고, 외부에서 생성한 후 주입시키는 디자인 패턴
- 실행 시점에 객체간의 관계를 결정하여, 결합도를 낮추고 유연성을 확보

#### a) 생성자 주입
```java
@Service
public class UserService {

  private UserRepository userRepository;
  private MemberService memberService;

  public UserService(UserRepository userRepository, MemberService memberService) {
    this.userRepository = userRepository;
    this.memberService = memberService;
  }
}
```
> Spring에서는 생성자 주입을 권한다. 
  - 주입받는 객체가 변하지 않거나, 반드시 객체의 주입이 필요할 때 강제할 수 있음.
  - 의존성 주입이 필요한 필드를 final 키워드로 선언 가능
  - 테스트 코드 작성이 용이
  - 스프링에서 순환 참조 시 에러 보여줌.
  - 의존관계 설정이 되지 않으면 컴파일 타임에 알 수 있기 때문

#### b) setter 주입
```java
public class A {
    private B b;
    
    public void setB(B b) {
        this.b = b;
    }
}
```
- 수정 가능성이 존재하는 의존 관계일 때 사용

#### c) 필드 주입
```java
@Controller
public class TestController {
    
    @Autowired
    private TestService testService;
}
```
- 클래스에 선언된 필드에 직접 의존관계를 주입하는 방법
- 코드가 짧아지는 장점이 있지만, 외부에서 변경이 불가능하고 테스트 코드를 작성하기 힘듬.
- final 키워드 선언이 불가능해지고 의존관계를 파악하기 힘듬.

***
### 1.2) IoC
IoC(Inversion of Control) 
- 제어의 역전, 즉 메소드나 객체의 호출 작업을 개발자가 결정하는 것이 아니라 외부에서 결정
- 객체의 의존성을 역전시켜, 객체 간이 결합도를 줄이고 유연한 코드를 작성할 수 있도록 함.
- 가독성, 코드 중복, 유지 보수의 편의성을 지원.

* * *
### 1.3) AOP
#### AOP(Aspect Oriented Programming)
- 관점을 기준으로 핵심 기능과 부가 기능으로 기능들을 분리
- 코드의 간결성을 높이고, 유연한 변경과 무한한 확장이 가능하도록 하는 것이 '관점 지향 프로그래밍'의 목적
- 핵심 기능(Core Concern): 우리가 적용하고자 하는 핵심 비즈니스 로직
- 부가 기능(Cross-Cutting Concern): 핵심 기능을 실행하기 위해 행하는 DB, 로깅, 파일 입출력 등이 있음.
- AOP가 필요한 이유
  - 부가 기능처럼 특정 로직을 애플리케이션 전반에 적용하는 문제는, 일반적인 OOP 방식으로는 해결이 어렵기 때문
  - 각각의 모듈에 수정이 필요하면 다른 모듈의 수정 없이 해당 로직만 변경하면 되기 때문
- 특징: 스프링 빈에만 AOP 적용 가능.


#### AOP 용어
- Aspect : 기능적인 역할을 하는 모듈. Aspect는 Advice와 PointCut을 모듈화한 애플리케이션의 횡단 기능
- Advice : AOP에서 실제 실행되는 코드. 언제 공통 관심 기능을 핵심 로직에 적용할지 정의.
  - 어노테이션에는 포인트 컷을 알려줘야 함.
  - @Before : target 메서드가 실행되기 전에 Advice가 실행
  - @After : target 메서드가 실행된 후에 Advice가 실행
  - @Around : traget 메서드를 감싸는 Advice
  -  @AfterReturning : target 메서드가 정상적으로 끝낫을 경우 실행
  -  @AfterThrowing : target 메서드에서 throwing이 발생했을 때 실행
- PointCut : Advice가 적용될 Join Point를 선정하는 방법. 모든 Join Point에 Advice를 적용하는 것이 아닌 특정 Join Point에 Advice를 적용하는것이 일반적.
- JoinPoint : Advice를 실행하고자 하는 위치. Spring AOP에서 모든 메서드는 JoinPoint가 될 수 있음.
- Target : Advice가 적용되는 기존 메서드, 클래스 등  

<img width="596" alt="Screenshot 2024-03-08 at 5 53 05 PM" src="https://github.com/parking0/TeenTalk_Server/assets/67892502/7c4c8d75-768c-4858-924a-34f210a4eeb7">


* * *
### 1.4) OOP
OOP(Object Oriented Programming)
- 공통된 목적을 띈 데이터와 동작을 묶어 하나의 객체로 정의하는 것이 목적
- 객체를 적극적으로 활용함으로써 기능을 재사용할 수 있는 것이 큰 장점

* * *
### 1.5) PSA
PSA (Portable Service Abstraction) 
- 추상화 계층을 추가해 서비스를 추상화하고 여러 서비스를 비즈니스 로직을 수정하지 않고 교체할 수 있도록 하는 것을 의미.
- 하나의 추상화로 여러 서비스를 묶어둔 것을 뜻함
- JDBC Driver의 종류를 비즈니스 로직의 수정 없이 언제든지 변경할 수 있는 것을 의미함. 즉, MySQL Driver 를 사용하다가 Oracle Driver로 변경해도 프로젝트의 비즈니스 로직에 변화가 없음.
- 모든 JDBC Driver 는 공통적인 인터페이스를 가지고 있기 때문에 프로젝트에 영향이 없음.
- 서비스를 추상화함으로써 개발자가 실제 구현부를 몰라도 해당 기능을 사용할 수 있음. 즉, 추상화 계층인 인터페이스 API의 정보를 활용해 해당 서비스의 모든 기능을 이용함.

<img width="676" alt="Screenshot 2024-03-08 at 6 05 23 PM" src="https://github.com/parking0/TeenTalk_Server/assets/67892502/726384d6-8be2-4347-8172-2102187362b3">


* * *
## 2. Spring Bean
### 2.1) Bean
- Bean : 스프링 컨테이너에 의해 관리되는 재사용 가능한 소프트웨어 컴포넌트. 즉, 스프링 컨테이너가 관리하는 자바 객체를 의미
- Spring Bean : 스프링 컨테이너에 등록된 객체. @Bean 어노테이션을 통해 메서드로부터 반환된 객체를 스프링 컨테이너에 등록


### 2.2) Spring Bean LifeCycle
#### 빈 생명 주기
- 해당 객체가 언제 어떻게 생성되어 소멸되기 전까지 어떤 작업을 수행하고, 언제, 어떻게 소멸되는지를 나타내는 일련의 과정
- Spring Container가 이런 빈 생명주기를 관리하고, 생성이나 소멸시 호출될 수 있는 콜백 메서드를 제공함
- 콜백 : 콜백 함수를 등록하면 특정 이벤트가 발생했을 떄 해당 메소드가 호출됨. 따라서 조건에 따라 실행될 수도, 않을 수도 있음.

#### Spring 의존 관계 주입 과정  
>스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 사용 -> 소멸 전 콜백 -> 스프링 종료
-  @Configuration, @Controller, @Service 등등 Bean 으로 등록할 수 있는 어노테이션들과 설정파일들을 읽어 IoC 컨테이너 안에 Bean 으로 등록
- 의존 관계 주입 전, 객체가 생성됨.
- 코드에 작성된 의존 관계를 보고 IoC 컨테이너에서 의존성을 주입함.
- 스프링은 의존관계 주입이 완료되면, 스프링 빈에게 콜백 메소드를 통해서 초기화 시점을 알려줌. 
- 또한 스프링 컨테이너가 종료되기 직전에 소멸 콜백을 알려줌.


* * *
## 3. Spring Annotation

### 3.1) Annotation(@)
- 사전적 의미 : 주석
- 자바에서 어노테이션은 클래스와 메서드에 추가하여 다양한 기능, 의미를 부여하는 기술
- 어노테이션을 사용하면 코드가 깔끔해지고 유지보수가 쉬워짐.
- 컴파일러에게 코드 작성 문법을 체크하도록 정보를 제공 
- SW 개발 툴이 빌드나 배치시 코드를 자동으로 생성할 수 있도록 정보를 제공
- 실행시 특정 기능을 실행하도록 정보를 제공
- 사용 방법 
  1) 어노테이션을 정의한다.
  2) 클래스에 어노테이션을 배치한다.
  3) 코드가 실행되는 중에 Reflection을 이용하여 추가 정보를 획득하여 기능을 실시한다.


### 3.2) 어노테이션을 통한 Bean 등록
#### 1) 컴포넌트 스캔과 자동 의존관계 설정 - @Component
- 스프링은 component scan을 사용하여, @Component 어노테이션이 있는 클래스들을 찾아서 자동으로 빈 등록함.
- @Component를 포함하는 @Controller, @Service, @Repository 애노테이션도 스프링 빈으로 자동 등록됨.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {

    @AliasFor(annotation = Component.class)
    String value() default "";

    boolean proxyBeanMethods() default true;
}
```
- @Configuration 안에 있는 @Component에 의해 설정 클래스 역시 자동으로 빈으로 등록이 되고, 그래서 @Bean이 있는 메소드를 통해 빈으로 등록


#### 2) 자바 코드로 직접 스프링 빈 등록 (@Configuration)
- @Configuration과 @Bean 애노테이션을 이용해 스프링 빈을 등록한다.
- @Configuration을 이용하면, Configuration 역할을 하는 Class를 지정할 수 있다.
- 중복된 bean 이름이 있지 않도록 주의한다.
- 스프링 컨테이너는 @Configuration이 붙어있는 클래스를 자동으로 빈으로 등록해두고, 해당 클래스를 파싱해서 @Bean이 있는 메소드를 찾아서 빈을 생성한다.
- @Bean을 사용하는 클래스에는 반드시 @Configuration 어노테이션을 활용하여 해당 클래스에서 Bean을 등록하고자 함을 명시한다.

```java
@Configuration
public class SpringConfig {

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
```


#### 3) @Bean, @Configuration, @Component 차이
- @Bean, @Configuration
  - 수동으로 빈을 등록
  - 개발자가 직접 제어할 수 없는 라이브러리를 빈으로 등록할 때 사용
  - 1개 이상의 @Bean을 제공하는 클래스의 경우, 반드시 @Configuration을 명시해야 싱글톤 보장

- @Component
    - 자동으로 빈을 등록
    - 스프링의 컴포넌트 스캔 기능이 해당 어노테이션이 있는 클래스를 자동으로 찾아서 빈으로 등록
    - 대부분의 경우 이 방식이 권장됨
    - @Component 하위 어노테이션으로 @Configuration, @Controller, @Service, @Repository 등이 있음



### 3.3) 스프링의 컴포넌트 탐색
#### @ComponentScan
- @Component 애노테이션이 붙은 클래스를 스캔해서 스프링 빈으로 등록

#### 1) 탐색할 패키지의 시작 위치 지정
   - 모든 자바 클래스를 컴포넌트 스캔하면 시간이 오래걸리므로, 필요한 위치부터 탐색하도록 시작 위치를 지정할 수 있음
   - basePackages : 탐색할 패키지의 시작 위치 지정. 이 패키지를 포함한 하위 패키지를 모두 탐색
   - basePackageClasses : 지정한 클래스의 패키지를 탐색 시작 위치로 지정
   - 시작 위치 지정하지 않은 경우, @ComponentScan이 붙은 설정 정보 클래스의 패키지가 시작 위치로 지정
  ```java
   package hello.core;
   @Configuration
   @ComponentScan	// 탐색 시작 위치 = hello.core
   public class AutoAppConfig {
   }
   ```

#### 2) 권장하는 방법
 - 탐색할 패키지의 시작 위치를 지정하지 않고, 설정 정보 클래스의 위치를 프로젝트 최상단에 두어, 하위 클래스들을 전부 탐색하도록 하기
 - 스프링 부트의 대표 시작 정보인 @SpringBootApplication을 이 프로젝트의 시작 루트 위치에 두는 것이 관례. (이 설정안에 @ComponentScan이 들어있음)
 - com.hello(프로젝트 최상단)에 AppConfig 같은 메인 설정 정보를 두고, @ComponentScan 을 붙이고, basePackages 지정은 생략
 - => com.hello를 포함한 하위 패키지는 모두 컴포넌트 스캔의 대상이 됨.
 - 프로젝트 메인 설정 정보는 프로젝트를 대표하는 정보이므로 시작 루트 위치에 두는 것이 좋음


#### 3) 컴포넌트 스캔 기본 대상
- @Component : 컴포넌트 스캔에서 사용
- @Controller : 스프링 MVC 컨트롤러에서 사용
- @Service : 스프링 비즈니스 로직에서 사용
- @Repository : 스프링 데이터 접근 계층에서 사용
- @Configuration : 스프링 설정 정보에서 사용


#### 4) 필터
- useDefaultFilters : 기본으로 켜져있음. 이 옵션을 끄면 기본 대상들이 제외됨.
- includeFilters: 컴포넌트 스캔 대상을 추가로 지정
- excludeFilters: 컴포넌트 스캔에서 제외할 대상을 지정


* * *
## 4. 단위 테스트와 통합 테스트
### 4.1) 단위 테스트
#### 단위 테스트 (Unit Test)
- 전체 코드 중 작은 부분을 테스트하는 것.
- 테스트에 외부 리소스(네트워크, 데이터베이스)가 포함된다면 유닛 테스트가 아님.
- ex) 테스트를 위한 입력 값을 주어서 그에 대한 함수의 출력 값이 정확한지 아닌지를 판단
- 매우 간단하고 명확해야 함.
- 코드의 설계가 좋지 않으면, 단위 테스트도 작성하기 어려움.
- 해당 부분만 독립적으로 테스트하기 때문에 어떤 코드를 리팩토링하여도 빠르게 문제 여부를 확인할 수 있음.


### 4.2) 통합 테스트
#### 통합 테스트 (Integration Teset)
- 각각의 시스템들이 서로 어떻게 상호작용하고 제대로 작동하는지 테스트
- 두 개의 다른 분리된 시스템끼리 잘 통신하고 있는지 증명하고 싶을 때 
- ex) 앱과 DB가 올바르게 상호작용하는지 테스트

<img width="537" alt="Screenshot 2024-03-09 at 6 06 39 PM" src="https://github.com/parking0/TeenTalk_Server/assets/67892502/1e98158d-2d14-48bf-b44c-954e460554cc">

### 4.3) 테스트 방법
#### 라이브러리
- JUnit : 자바 단위 테스트를 위한 프레임워크
- AssertJ : 자바 테스트를 돕기 위해, 다양한 문법을 지원하는 라이브러리

#### JUnit 어노테이션
- @Test : 테스트용 메소드를 표현
- @BeforeEach : 각 테스트 메소드가 시작되기 전에 실행돼야 하는 메소드 표현
- @AfterEach : 각 테스트 메소드가 시작된 후 실행돼야 하는 메소드 표현
- @BeforeAll : 테스트 시작 전에 실행돼야 하는 메소드 표현
- @AfterAll : 테스트 종료 후에 실행돼야 하는 메소드 표현

#### given/when/then 패턴
- given : 어떤 데이터가 주어졌을 때
- when : 어떤 함수를 실행하면
- then : 어떠한 결과가 나와야 함.

  ```java
  @Test
  @DisplayName("Test")
  void test() {
    // Given

    // When

    // Then
  }
  ```
  
