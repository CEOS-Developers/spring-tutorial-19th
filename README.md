# IoC(Inversion of Control)
제어의 역전이란 메서드나 객체의 호출 작업을 개발자가 아닌 스프링에게 제어권을 넘기는 것
스프링을 사용하게 되면 스프링 컨테이너에게 제어권을 넘겨 스프링 컨테이너가 흐름을 제어

# DI(Dependency Injection)
DI란 의존성 주입이라 부르며, 객체를 직접 생성하는 게 아닌 외부(IOC 컨테이너)에서 생성한 후 주입시켜주는 방식
의존성 주입 방식으로는 3가지 방법이 존재한다

- 생성자 주입 ( Constructor Injection )
- 수정자 주입 ( Setter Injection)
- 필드 주입 ( Field Injection )

## 생성자 주입 ( Constructor Injection )
생성자 주입이란 생성자를 통해 의존관계를 주입하는 방법으로 생성자를 호출 시에 딱 한 번만 호출되는 것을 보장한다.

    @Controller
    public class TestController {
    private final TestService testService;

    public TestController(TestService testService){
       this.testService = testService;
    }
}
## 수정자 주입 ( Setter Injection)
수정자 주입이란 영어 그대로 setter를 사용하여 의존관계를 주입하는 방법이다. 수정 가능성이 존재하는 의존관계일 때 사용하며 자바 빈 프로퍼티 규약의 setter메서드 방식을 사용한다. 수정자 주입의 경우 final 키워드 선언이 불가능하며, setter 메서드에 @Autowired를 붙여 사용한다.
    
    @Controller
    public class TestController {
    private TestService testService;

    @Autowired
    public void setTestService(TestService testService){
       this.testService = testService;
    }
}
## 필드 주입 ( Field Injection )
필드 주입이란 필드에 직접 의존관계를 주입하는 방법이다.이는 코드가 짧아지는 장점이 있지만 외부에서 변경이 불가능하고 테스트 코드를 작성하기 힘들다는 단점이 존재한다.또한 final 키워드 선언이 불가능해지고 의존관계를 파악하기 힘들어진다. 

    @Controller
      public class TestController {
      @Autowired
      private TestService testService;
      }

- 의존관계 설정이 되지 않으면 컨파일 타임에 알 수 있다.
- 의존성 주입이 필요한 필드를 final 키워드로 선언 가능하다
- 스프링에서 순환 참조 감지 기능을 제공하며 순환 참조 시 에러를 보여준다.
- 테스트 코드 작성이 용이하다.

# Bean Life Cycle
Spring Container는 자바 객체(Bean)의 생성과 소멸 같은 생명주기(Life Cycle)를 관리하며, 생성된 자바 객체들에게 추가적인 기능을 제공하는 역할

Spring Container는 이런 빈 객체의 생명주기를 컨테이너의 생명주기 내에서 관리하고 객체 생성이나 소멸 시 호출될 수 있는 콜백 메서드를 제공하고 있습니다.

• Spring Container
- 초기화 : Bean을 등록, 생성, 주입
- 종료 : Bean 객체들을 소멸

• 콜백
: 콜백함수를 부를 때 사용되는 용어이며 콜백함수를 등록하면 특정 이벤트가 발생했을 때 해당 메소드가 호출됨
: 즉, 조건에 따라 실행될 수도 실행되지 않을 수도 있는 개념
![img.png](img.png)
1 ) 스프링 컨테이너 생성
2 ) 스프링 빈 생성
3 ) 의존성 주입
4 ) 초기화 콜백 : 빈이 생성되고, 빈의 의존관계 주입이 완료된 후 호출
5 ) 사용
6 ) 소멸전 콜백 : 빈이 소멸되기 직전에 호출
7 ) 스프링 종료
스프링 빈은 생성과 의존성 주입이 끝난 후에 필요한 데이터를 사용할 준비가 됩니다. 또한 의존 관계 주입이 완료되는 시점과 종료되는 시점은 콜백 메서드를 통해 알 수 있다
![img_1.png](img_1.png)

## 1. InitializingBean, DisposableBean callback interfaces

• InitializingBean
: 빈에 필요한 모든 속성이 컨테이너에 설정된 후에 빈의 초기화 작업을 수행하도록 합니다.

void afterPropertiesSet() throws Exception;
: afterPropertiesSet() 메서드로 초기화를 지원합니다.
: 의존 관계 주입이 끝난 후 초기화 진행

• DisposableBean
: 스프링 컨테이너가 빈을 소멸시키기 전에 콜백을 얻을 수 있다.

    void destroy() throws Exception;
: destory() 메소드로 소멸을 지원합니다.
: Bean 종료 전 마무리 작업 ex) 자원해제 close( ) 등

    import org.springframework.beans.factory.DisposableBean;
    import org.springframework.beans.factory.InitializingBean;
    
    public class DemoBean implements InitializingBean, DisposableBean
    {
    //Other bean attributes and methods

    @Override
    public void afterPropertiesSet() throws Exception
    {
    //Bean initialization code
    }
    
    @Override
    public void destroy() throws Exception
    {
    //Bean destruction code
    }
    }
단점
: 해당 인터페이스는 스프링 전용 인터페이스로 해당 코드가 인터페이스에 의존한다.
: 초기화, 소멸 메소드를 오버라이드 하기 때문에 메소드명을 변경할 수 없다.
: 코드를 고칠 수 없는 외부 라이브러리에 적용 불가능하다.

또한 인터페이스를 사용하는 초기화 및 종료 방법은 스프링 초창기에 나온 방법들이며, 지금은 거의 사용하지 않습니다.

## 2. 설정 정보에 사용자 정의 초기화 메서드, 종료 메서드 지정
위 같은 인터페이스를 구현하지 않고 @Bean 어노테이션에 initMethod, destroyMethod 속성을 사용하여 초기화, 소멸 메서드를 각각 지정할 수 있습니다.


    public class ExampleBean {
    public void init() throws Exception {
    //초기화 콜백
    }
    
        public void close() throws Exception {     
            // 소멸 전 콜백 
        }
    }

    @Configuration
    class LifeCycleConfig {
    @Bean(initMethod = "init", destroyMethod = "close")
    public ExampleBean exampleBean() {
    
         }
    }

• 이는 수정할 수 없는 외부 클래스, 정확히 위의 두 인터페이스를 구현시킬 수 없는 클래스의 객체 스프링 컨테이너에 등록할 때 유용합니다.
• 메소드 명을 자유롭게 부여할 수있고, 스프링 코드에 의존하지 않습니다.
• 설정 정보를 사용하기 때문에 코드를 고칠 수 없는 외부 라이브러리에도 초기화, 종료 메서드를 적용할 수 있습니다.

## 3. @PostConstruct, @PreDestroy 애노테이션
• @PostConstruct
: 기본 생성자를 사용하여 빈이 생성된 후 인스턴스가 요청 객체에 반환되기 직전에 주석이 달린 메서드가 호출됩니다.

• @PreDestroy
: @PreDestroy주석이 달린 메소드는 bean이 bean 컨테이너 내부 에서 파괴되기 직전에 호출됩니다 .

    import javax.annotation.PostConstruct;
    import javax.annotation.PreDestroy;
    
    public class DemoBean
    {
    @PostConstruct
    public void customInit()
    {
    System.out.println("Method customInit() invoked...");
    }

    @PreDestroy
    public void customDestroy()
        {
            System.out.println("Method customDestroy() invoked...");
        }
    }

# 어노테이션
## @Autowired
스프링 DI(Dependency Injection)에서 사용되는 어노테이션. 선언부 앞쪽에 이 어노테이션을 붙이면, 메서드 호출과 인스턴스 주입을 스프링이 자동으로 해 준다. 단, 이 어노테이션으로 호출되는 객체는 bean으로 등록되어있어야 한다.

    @Autowired private MemberServiceImpl member;
// MemberServiceImpl는 @Service 어노테이션이 붙은 객체.

## @Controller는 Web MVC 코드에 사용되는 어노테이션으로, 요청에 따른 처리방식을 @RequestMapping 어노테이션으로 정의한다. @RequestMapping는 @Controller 어노테이션이 붙은 클래스 안에서만 동작한다. 쉽게 말하면, 교통정리를 하는 클래스가 누구인지 알려주는 어노테이션이다.
    @Controller
    public class HomeController {
    
        private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
        
        /**
         * 자동생성되는 홈 컨트롤러.
         */
        @RequestMapping(value = "/", method = RequestMethod.GET)
        public String home(Locale locale, Model model) {
            logger.info("Welcome home! The client locale is {}.", locale);
            
            Date date = new Date();
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
            
            String formattedDate = dateFormat.format(date);
            
            model.addAttribute("serverTime", formattedDate );
            
            return "home";
        }
    
    }

## @RequestMapping
들어온 요청을 특정 메서드와 매핑하기 위해 사용하는 어노테이션이다. 많은 파라미터가 있지만 대표적으로 Value와 method 두 가지를 많이 사용한다.

    @RequestMapping(value = "/", method = RequestMethod.GET)
    // "/"주소가 GET방식으로 호출되었을 때 해당하는 메서드가 동작한다.
    // GET은 method 기본값이기 때문에 생략 가능하다.

## @Component
스프링에서 관리되는 객체임을 알리는 어노테이션. 해당 어노테이션을 붙이면 스프링이 bean으로서 관리한다. 가장 기본적인 형태이며, 관점(Aspects)에 따라 세분화하면 @Controller, @Service, @Repository 등으로 나뉜다.

root-context.xml의 context:component-scan은 base package로 지정된 위치를 스캔하며 해당 태그가 붙은 클래스(=bean)를 찾는다.

    @Component
    public class NoticePage extends PageVO {
    
        private List<NoticeVO> list;
    
        public List<NoticeVO> getList() {
            return list;
        }
    
        public void setList(List<NoticeVO> list) {
            this.list = list;
        }
    
    }
    
    // NoticePage.java
# 단위 테스트(Unit Test)
단위 테스트는 응용 프로그램에서 테스트 가능한 가장 작은 소프트웨어를 실행하여 예상대로 동작하는지 확인하는 테스트이다.


    @DisplayName("자동차가 전진한다")
        @Test
        public void moveCar() {
        // given
        Car car = new Car("dani");
        
            // when
            car.move(4);
        
            // then
            assertThat(car.getPosition()).isEqualTo(1);
        }
        
        @DisplayName("자동차가 멈춘다")
        @Test
        public void stopCar() {
        // given
        Car car = new Car("dani");
        
            // when
            car.move(3);
        
            // then
            assertThat(car.getPosition()).isEqualTo(0);
        }

# 통합 테스트(Integration Test)
통합 테스트는 단위 테스트보다 더 큰 동작을 달성하기 위해 여러 모듈들을 모아 이들이 의도대로 협력하는지 확인하는 테스트이다.

- 장점 1 : 단위 테스트에서 발견하기 어려운 버그를 찾을 수 있다는 점이다
- 단점 1 : 단위 테스트보다 더 많은 코드를 테스트하기 때문에 신뢰성이 떨어질 수 있다는 점
- 단점 2: 어디서 에러가 발생했는지 확인하기 쉽지 않아 유지보수하기 힘들다는 점
  @SpringBootTest
  class SubwayApplicationTests {
  @Test
  void contextLoads() {
  }
  }

### 참조
https://dodokwon.tistory.com/57
https://velog.io/@homil9876/Spring-%EC%9E%90%EC%A3%BC-%EC%93%B0%EC%9D%B4%EB%8A%94-Annotation-%EC%A0%95%EB%A6%AC
