# spring-tutorial-19th
CEOS 19th BE Study - Spring Tutorial



## 의존의 개념

~~~java
public class Car{
	private Tire tire;
	
	public Car(Tire tire){
		this.tire = tire;
	}
}
~~~

Car에는 Tire가 속해있다. 결국 Tire의 변화는 Car에 영향을 미치게 된다. 이때 Car는 Tire에 의존한다고 한다.



## 스프링 핵심 가치 3요소

**1. Ioc**

Ioc(제어의 역전)은 객체의 생성 및 관리의 책임을 개발자가 아닌 프레임워크에게 전가하는 것을 의미한다. 스프링에서 개발자는 필요한 빈을 생성하고 등록한다. 그러나 해당 빈들은 이후 더 이상 개발자에 의해서 관리되는 것이 아닌 스프링 프레임워크에 의해서 관리된다.

라이브러리와 프레임워크의 차이도 IoC의 여부에 의해 결정된다. 라이브러리는 IoC가 없이 개발자가 필요에 의해 필요 모듈을 호출하는 방식으로 제어권을 개발자가 가지나 프레임워크에선 객체들의 제어권을 개발자가 아닌 프레임워크가 가진다. 



***구현 방법***

- 팩토리 클래스
  
  객체의 생성을 팩토리 클래스에 전가하는 방식.
  
  ```java
  EntityManagerFactory entityManagerFactory = new EntityManagerFactory("hello");
  EntityManager entityManager = entityManagerFactory.createEntityManager();
  ```

- 템플릿 메서드

  상위 클래스에서 추상화를 통해서 알고리즘의 골격을 정의하고, 하위 클래스에서 상위 클래스의 구조를 오버라이딩하여 구체화하는 패턴. 하위 클래스를 구현하는 부분에 대한 IoC가 발생함. 

  HttpServlet -> FrameworkServlet -> DispetchServlet

  ```java
    public abstract class FrameworkServlet extends HttpServletBean implements ApplicationContextAware {
      ...중략...
      protected final void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
          ...중략...
  
          initContextHolders(request, localeContext, requestAttributes);
  
          try {
              doService(request, response); // template method pattern 이용
          }
          ...중략...
      }
      ...중략...
  
      protected abstract void doService(HttpServletRequest request, HttpServletResponse response) throws Exception; // subClass에게 위임
  
      ...중략...
  }
  
  public class DispatcherServlet extends FrameworkServlet {
  
      ...
  
      @Override
      protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
          logRequest(request);
  
          ...중략...
  
      }
  
      ...
  }
  ```

- 서비스 로케이터

  직접 인스턴스를 생성 및 관리하는 거 대신 로케이터를 통해서 서비스를 찾아서 사용하는 패턴이다. 스프링에서는 ApplicationContext를 사용해서 서비스를 로케이팅 할 수 있다.

  ```java
  import org.springframework.context.ApplicationContext;
  import org.springframework.context.support.ClassPathXmlApplicationContext;
  
  public class MyApp {
      public static void main(String[] args) {
          ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
          
          // myService라는 이름으로 등록된 Bean을 로케이터로 찾아와 사용
          MyService service = (MyService) context.getBean("myService");
          service.doSomething();
      }
  }
  ```

- DI

  Dependency Injection의 약자로 의존성 주입을 말한다. 외부에서 이미 등록된 객체를 찾아서 주입해주는 방식으로 이를 구현하기 위한 세개의 방법이 존재한다.

  1. Setter 주입

     setter 메서드를 사용해서 객체를 주입해주는 방식이다.

     ~~~java
     @Service
     public class MyService{
     	private MyRepository myRepository;
     	
       @Autowired
     	public MyService setMyService(MyRepository myRepository){
     		this.myReposiotry = myRepository;
     	}
     }
     ~~~

  2. 생성자 주입(추천)

     객체를 생성할 때 생성자를 통해서 의존성을 주입하는 형식이다. 생성자를 통해서만 의존성 주입이 가능하므로 중간에 다른 객체로 변경할 수 없다. 이러한 특징 때문에 안전성이 높다.

     ~~~java
     @Service
     public class MyService{
     	private MyRepository myRepository;
     	
     	public MyService(MyRepository myRepository){
     		this.myReposiotry = myRepository;
     	}
     }
     ~~~

     이처럼 생성자가 하나만 존재하는 경우 @Autowired 어노테이션을 생략 가능하다.

     ~~~java
     @Service
     @RequiredArgsConstructor
     public class MyService{
     	private final MyRepository myRepository;
     }
     ~~~

  3. 필드 주입

     ~~~java
     @Service
     public class MyService{
     	@Autowired
     	private MyRepository myrepository;
     }
     ~~~

     가장 간단하게 DI를 진행할 수 있는 방식이나, 의존관계가 눈에 보이지 않아서 추상적이다. 컨테이너와 결합이 지나치게 강해져 외부 사용이 용의하지 않다. 한번 정해진 의존관계가 변경되지 않는 경우에 사용된다.




**2. AOP**

관점 지향 프로그래밍(Asoect Oriented Programming)의 약자로 핵심 기능과 부가 기능을 분리하는 방식이다. Spring에서 AOP를 사용하기 위해서는 @Aspect 어노테이션을 사용한다. 이후 @Bean으로 등록하여 사용한다.

- 핵심 기능(= 핵심 관심사): 비즈니스 로직을 포함하는 기능
- 부가 기능(= 횡단 관심사): 핵심 기능을 보조하는 기능(로깅, 보안, 트랜잭션 등...)

부가기능은 여러 곳에서 중복적으로 사용될 수 있는데 기존의 oop(객체지향프로그래밍) 방식으로는 프로그래밍 한 경우에는 여러 곳에서 공통적으로 부가 기능의 중복 코드가 발생한다. 결론적으로 AOP는 OOP에서 부가 기능의 불필요한 반복을 해결하기 위한 방법이다. AOP의 모듈화 단위는 OOP의 클래스와는 달리 관점이다.



**3. PSA**

SA(Service Abstraction) 서비스 추상화는 서비스의 내부 구조를 모르더라도 해당 서비스를 자유롭게 사용할 수 있는것을 의미한다.

PSA(Portable Service Abstraction)는 환경의 변화와 관계없이 일관된 방식의 기술로의 접근 환경을 제공하는 추상화 구조이다.  쉽게 말해서 잘 만든 하나의 인터페이스로 여러 기술들을 다룰 수 있는 것을 의미한다. 

예) 처음에 JDBC driver로 MySql을 사용하다가 나중에 H2 데이터베이스를 사용하더라도 모든 JDBC driver는 추상화된 공통의 인터페이스를 가지고 있어 프로젝트의 비즈니스 로직에는 영향을 주지 않는다. 

1. 개발자가 서비스의 내부 구현을 구체적으로 알지 못하더라도 사용이 가능.
2. 해당 추상화 계층을 구현하는 또 다른 서비스로 언제든지 교체가 가능.

![image](https://github.com/riceCakeSsamanKo/spring-tutorial-19th/assets/121627245/baedf9f4-215e-49c1-add8-9178e8c048a4)


여러 비즈니스 로직들이 모두 **PlatformTransactionManager** 인터페이스를 공통으로 사용한다. 따라서 사용자의 경우에는 해당 인터페이스 하나만을 선언하여 이용하고 구현체를 변경이 가능하다.



## Spring Bean

스프링 빈(Bean)은 스프링 컨테이너에 의해 관리되는 자바 인스턴스를 의미한다. **configuration 관련 빈은 @Bean 어노테이션을 또는 @Compenent 어노테이션**을 사용하여 컨테이너에 등록한다.

**MVC관련 빈은 @Repository, @Service, @Contoller 어노테이션**으로 빈을 등록한다.

- @Bean: 개발자가 컨트롤 할 수 없는 외부 라이브러리를 등록할 때 주로 사용. **메소드 레벨**에서 반환되는 객체를 개발자가 수동으로 빈으로 등록.

~~~java
public class SpringConfig{

  @Bean  // 1. 외부 라이브러리 등록
  public ObjectMapper objectMapper(){
    return new ObjectMapper;
  }

  @Bean  // 2. 내부 클래스 등록
  public MemberRepository memberRepository(){
    return new MemberRepository();
  }
}
~~~

ObjectMapper는 이미 선언된 라이브러리로 해당 라이브러리 내부에 @Compenent 어노테이션을 붙일 수 없다. 이럴 때 @Bean을 사용한다.



- @Compenent: 개발자가 직접 개발한 컨트롤 가능한 내부 클래스를 빈으로 등록할 때 사용한다. **클래스 단위**로 사용한다.

~~~java
@Component
public class AppConfig{
	...
}
~~~

 클래스 단위의 스프링 빈(@Component, @Repository, @Service, @Controller)은 컴포넌트 스캔을 통해 등록이된다. 생성된 빈들은 Application Context에서 보관된다.



@Repository, @Service와 @Controller는 @Componenet의 기능을 가지나 더 구체적인 쓰임새를 위한 명시이다. 

~~~java
@Component //@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    @Transactional(readOnly = true)
    public List<Test> findAllTest() {
        return testRepository.findAll();
    }
}
~~~

빈의 라이프 사이클은 다음의 일련의 과정을 거친다.
1. 스프링이 시작되며 컨테이너가 생성된다.
2. 컴포넌트 어노테이션이 붙은 컴포넌트들을 ioc 컨테이너에 빈으로 등록한다.
3. 의존 관계를 설정할 객체를 생성한다.
4. DI를 진행한다. (생성자 주입은 객체의 생성과 동시에 주입이 진행. setter, 필드 주입은 객체의 생성과 DI의 라이프 사이클이 분리되어 있다.)
5. 초기화 콜백 함수가 호출된다. (@PostConstruct)
6. 사용
7. 소멸전 콜백 함수 호출 (@PreDestory)
8. 빈 소멸 및 스프링 종료

- 콜백함수 
~~~java
// 스프링 빈
public class MyBean {

    public void logic() {
        System.out.println("MyBean.logic()");
    }

    @PostConstruct
    public void construct() {
        System.out.println("스프링 빈 생성");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("스프링 빈 제거");
    }
}
~~~
~~~java
// 빈 등록
@Configuration
public class AppConfig {

    @Bean
    public MyBean myBean() {
        return new MyBean();
    }
}
~~~
~~~java
// 테스트 코드
@SpringBootTest
public class CallBackTest {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    public void test() {
        MyBean bean = context.getBean(MyBean.class);
        bean.logic();
        context.close();
    }
}

결과>>
스프링 빈 생성
MyBean.logic()
스프링 빈 제거
~~~

## 어노테이션
 스프링 어노테이션은 코드사이에서 주석처럼 쓰이면서 특별한 의마와 기능을 수행하도록 하는 기술로 프로그램에게 추가적인 정보를 제공하는 메타데이터이다.

 1. 컴파일러에게 코드 작성 문법 에러를 체크하도록 함.
 2. 빌드나 배치 시 코드를 자동으로 생성할 수 있도록 정보 제공.
 3. 런타임 시 특정 기능을 실행하도록 정보 제공.(주로 사용)

  ### 어노테이션 선언
~~~java
@Target({ElementType.[적용대상]})
@Retention({RetentionPolicy.[정보유지대상]})
public @interface [어노테이션이름]{
    ...
}
~~~

**ElementType** enum에 들어가는 값들은 다음과 같다.
- ANNOTATION_TYPE: 어노테이션 선언

- CONSTRUCTOR :생성자 선언

- FIELD: 필드 선언 (열거 형 상수 포함)

- LOCAL_VARIABLE: 지역 변수 선언

- METHOD: 메서드 선언

- PACKAGE: 패키지 선언

- PARAMETER: public parameter

- TYPE: 클래스, 인터페이스 (주석 유형 포함) 또는 열거 형 선언

- TYPE_PARAMETER: Type 파라미터 선언 (Java 8에 추가)

- TYPE_USE: Type이 사용되는 곳 (Java 8에 추가)

**RetentionPolicy** enum에 들어가는 값은 다음과 같다.
- CLASS: 컴파일러에 의해 클래스 파일에 기록되지만 런타임에는 유지되지 않는다.

- RUNTIME: 컴파일러에 의해 클래스 파일에 기록되고 런타임에 유지된다.

- SOURCE: 소스에만 반영되어 컴파일러에 의해 삭제된다.


예)
~~~java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Controller {
    @AliasFor(
        annotation = Component.class
    )
    String value() default "";
}
~~~

~~~java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PostConstruct {
}
~~~

### 컴포넌트 스캔
@Componentscan 어노테이션은 @Component어노테이션 및 streotype(@Service, @Repository, @Controller.)어노테이션이 부여된 Class들을 자동으로 Scan하여 Bean으로 등록해준다.

@Configuration을 통해 수동으로 빈을 등록해줄 경우, @Bean을 메소드에 매핑하여 의존성을 주입해줬다. 
하지만 Component Scan에선 클래스의 생성자에 @Autowired를 통해 의존성을 주입해준다.

~~~java
@Configuration
@ComponentScan(basePackages = {"패키지명"}, basePackageClasses = 클래스명.class)  // 스캔 범위 지정
public class AppConfig{
    ...
}
~~~

일반적으로 @ComponentScan 어노테이션을 따로 사용하진 않는다.

왜냐하면 스프링 어플리케이션을 생성시 최상단에 자동으로 생성되는 Application 클래스의 @SpringBootApplication 내부에 이미 @ComponentScan이 포함되어 있기때문이다.
~~~java
@SpringBootApplication  // 여기에 이미 들어있음
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
~~~

컴포넌트 스캔시 생성되는 빈의 이름은 맨 앞글자는 소문자인 클래스명이다.
~~~java
@Component
public class MyBean{
    ...
}

-> 빈이름: myBean
~~~

## 단위 테스트 vs 통합테스트
- 단위 테스트: 메서드, 작은 단위를 테스트. 하나의 컴포넌트만을 다룸
- 통합 테스트: 독립적인 컴포넌트들 간의 상호작용을 테스트

### 테스트 방법
junit 라이브러리를 이용

~~~java
@SpringBootTest
// @RunWith(SpringRunner.class) junit4 이하에서 사용
@AutoConfigureMockMvc  //테스트에서 MockMvc를 사용할 때 사용 
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;  // 당장 구현하기 어려운 객체를 대신해 가짜로 생성해 놓은 객체

    @DisplayName("DisplayName : 테스트 이름을 설정할 수 있습니다")
    @Test
    public void getHello() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/")  // 엔드포인트('/')로 get 요청 날림. 
                .accept(MediaType.APPLICATION_JSON))  // 컨텐츠 타입은 json
                .andExpect(status().isOk())  // 반환된 Http status가 ok인지 확인
                .andExpect(content().string(equalTo("Greetings from Spring Bood!"))); // 반환된 컨텐츠의 내용이 Greetings from Spring Bood!인지 확인
                .andDo(print())  // 결과 출력
    }
}
~~~

***MockMvc 테스트 메서드***

1. perform(RequestBuilder builder)
   - RequestBuilder에 요청 정보(http 메서드, url, 쿠키 등등)를 담아 해당 요청을 보낸다.
   - 결과로 ResultActions을 반환하며, andExpect()로 검증할 수 있다.
  
2. andExpect(ResultMatcher matcher)
   - 실행 결과를 검증한다.
   - ResultMatcher로 상태코드, 헤더, 쿠키, content등을 검증할 수 있다.

3. andExpectAll(ResultMatcher... matchers) 
   - 한번에 검증 내용을 작성할 수 있다.

4. andDo(ResultHandler handler)
   - handler에 따라 결과를 출력한다.
   - ResultHandler에는 log() : 로그 출력, print(): System.out 출력 이 있다.
