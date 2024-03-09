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

![image-20240308155455495](/Users/min/Library/Application Support/typora-user-images/image-20240308155455495.png)

여러 비즈니스 로직들이 모두 **PlatformTransactionManager** 인터페이스를 공통으로 사용한다. 따라서 사용자의 경우에는 해당 인터페이스 하나만을 선언하여 이용하고 구현체를 변경이 가능하다.



## Spring Bean

스프링 빈(Bean)은 스프링 컨테이너에 의해 관리되는 자바 인스턴스를 의미한다. **configuration 관련 빈은 @Bean 어노테이션을 또는 @Compenent 어노테이션**을 사용하여 컨테이너에 등록한다.

MVC관련 빈은 @Repository, @Service, @Contoller 어노테이션으로 빈을 등록한다.

- @Bean: 개발자가 컨트롤 할 수 없는 외부 라이브러리를 등록할 때 주로 사용. **메소드 레벨**에서 반환되는 객체를 개발자가 수동으로 빈으로 등록.

~~~java
public class SpringConfig{
	@Bean
	public ObjectMapper objectMapper(){
		return new ObjectMapper;
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

 클래스 단위의 스프링 빈(@Component, @Repository,@Service,@Controller)은 컴포넌트 스캔을 통해 등록이된다. 생성된 빈들은 Application Context에서 보관된다.



@Repository, @Service와 @Controller는 @Componenet의 더 구체적인 쓰임새를 위한 명시이다. 
