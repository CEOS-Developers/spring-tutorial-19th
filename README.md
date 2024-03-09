# spring-tutorial-19th
CEOS 19th BE Study - Spring Tutorial

# IoC(Inversion of Control)
프로그램의 제어 흐름을 직접 제어하는 것이 아니라 외부에서 관리하는 것을 IoC(제어의 역전)라고 합니다. 
IoC는 디자인 패턴이 아니라 원칙입니다. IoC는 기본적으로 프레임워크를 정의하는 특성입니다.

```java
public class PhoenStore {
		private Phone phone;
		
		public PhoneStore() {
				this.phone = new Galaxy();
		}
}
```

`PhoneStore`은 직접 객체를 생성하여 코드를 제어합니다.

```java
public class PhoenStore {
		private Phone phone;
		
		public PhoneStore(Phone phone) {
				this.phone = phone;
		}
}
```

반면, 이렇게 작성된 코드는 프로그램의 제어 흐름을 직접 제어하는 것이 아니라 외부에서 관리합니다. 
이처럼 프로그램의 제어 흐름을 직접 제어하는 것이 아니라 외부에서 관리하는 것을 제어를 역전시켰다고 말할 수 있습니다.

## 프레임워크 vs 라이브러리
프레임워크와 라이브러리를 비슷하다고 생각할 수 있는데, 이 둘은 완전히 다릅니다. 간단하게 프레임워크와 라이브러리의 차이에 대해 알아볼게요.

프레임워크를 사용하면 흐름이 프레임워크에 의해 제어됩니다. 프레임워크는 코드를 넣을 위치를 지시하지만 필요에 따라 코드를 호출합니다. 
다시 말해 프레임워크에서 개발자가 작성한 코드를 호출하는 것이 프레임워크의 코드입니다. 이와 반대로 라이브러리를 사용하면 개발자가 프로그램 흐름을 제어할 수 있습니다.

<img src=https://github.com/leeedohyun/library-app/assets/116694226/6a09016b-5099-4621-8225-3cbe06fc7140 width="500">

## IoC의 장점
- 작업 실행과 구현 분리
- 다양한 구현 간 전환을 더 쉽게 만듭니다.
- 프로그램의 모듈성 향상
- 컴포넌트를 격리하거나 해당 종속성을 mocking하고 컴포넌트가 프로그램을 더욱 쉽게 테스트할 수 있습니다.

# DI(Dependency Injection)
DI는 의존성 주입이라 하고, IoC를 구현하는 데 사용할 수 있는 패턴입니다. 
객체를 다른 객체와 연결하거나 객체를 다른 객체에 주입하는 것은 객체 자체가 아닌 어셈블러에 의해 수행됩니다. 
쉽게 말하면 의존성을 외부에서 결정하고 주입하는 것이 DI입니다.

# DI 방법
## 생성자 주입
```java
public class PhoenStore {
		private Phone phone;
		
		public PhoneStore(Phone phone) {
				this.phone = phone;
		}
}
```

```java
public class PhoneStoreOwner {
		private PhoneStore phoneStore = new PhoneStore(new Galaxy());
		
		public PhoneStoreOwner() {
				phoneStore = new PhoneStore(new IPhone());
		}
}
```

## setter 주입
setter라 불리는 필드의 값을 변경하는 수정자 메서드를 통해서 의존성을 주입하는 방법입니다.
**선택, 변경** 가능성이 있는 의존성에 사용되며, 자바빈 프로퍼티 규약(getter/setter)의 수정자 메서드 방식을 사용하는 방법입니다.

```java
public class PhoenStore {
		private Phone phone = new Galaxy();
		
		public void setPhone(Phone phone) {
				this.phone = phone;
		}
}
```

```java
public class PhoneStoreOwner {
		private PhoneStore phoneStore = new PhoneStore();
		
		public void changePhone() {
				phone.setPhone(new IPhone());
		}
}
```

## 필드 주입
```java
public class PhoenStore {
		@Autowired
		private Phone phone;
}
```

# PSA(Portable Service Abstraction)
환경의 변화와 관계없이 일관된 방식의 기술로의 접근 환경을 제공하는 추상화 구조를 말합니다. 
즉 잘 만든 인터페이스를 의미하는데 PSA가 적용된 코드라면 코드를 수정하지 않고 다른 기술로 바꿀 수 있도록 확장성이 좋고, 기술에 특화되어 있지 않습니다.

## 스프링 웹 MVC
### 일반적인 서블릿
```java
public class CreateServlet extends HttpServlet {
		// GET
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) {
				super.doGet(request, response);
		}
		
		//POST
		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response) {
				super.doPost(request, response);
		}
}
```

### Spring Web MVC
```java
@Controller
public class Controller {

		@GetMapping
		public String createForm(Map<String, Object> model) {
				return VIEWS_CREATE_OR_UPDATE_FORM;
		}
		
		@PostMapping
		public String processCreateForm(@Valid Data data, BindingResult result) {
				if (result.hasError()) {
						return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
				}
				return "redirect:/";
		}
}
```

## 스프링 트랜잭션
Low Level로 트랜잭션을 처리하는 코드를 살펴봅시다.

```java
public class TransactionExample {

		private static final String SQL_INSERT = "INSERT INTO EMPLOYEE (NAME, SALARY, CREATED_DATE) VALUES (?,?,?)";
    private static final String SQL_UPDATE = "UPDATE EMPLOYEE SET SALARY=? WHERE NAME=?";
    private static final String SQL_TABLE_CREATE = "CREATE TABLE EMPLOYEE"
            + "("
            + " ID serial,"
            + " NAME varchar(100) NOT NULL,"
            + " SALARY numeric(15, 2) NOT NULL,"
            + " CREATED_DATE timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,"
            + " PRIMARY KEY (ID)"
            + ")";
    private static final String SQL_TABLE_DROP = "DROP TABLE EMPLOYEE";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/test", "postgres", "password");
             Statement statement = conn.createStatement();
             PreparedStatement psInsert = conn.prepareStatement(SQL_INSERT);
             PreparedStatement psUpdate = conn.prepareStatement(SQL_UPDATE)) {

            statement.execute(SQL_TABLE_DROP);
            statement.execute(SQL_TABLE_CREATE);

            // Run list of insert commands
            psInsert.setString(1, "mkyong");
            psInsert.setBigDecimal(2, new BigDecimal(10));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();

            psInsert.setString(1, "kungfu");
            psInsert.setBigDecimal(2, new BigDecimal(20));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();

            // Run list of update commands

            // below line caused error, test transaction
            // org.postgresql.util.PSQLException: No value specified for parameter 1.
            psUpdate.setBigDecimal(2, new BigDecimal(999.99));
            
			//psUpdate.setBigDecimal(1, new BigDecimal(999.99));
            psUpdate.setString(2, "mkyong");
            psUpdate.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

이 코드를 단순하게 `@Transactional`을 사용해서 트랜잭션을 처리할 수 있습니다. 이 또한 PSA로써 다양한 기술 스택으로 구현체를 바꿀 수 있습니다.
예를 들어, JDBC를 사용하는 DatasourceTransactionManager, JPA를 사용하는 JpaTransactionManager,
Hibernate를 사용하는 HibernateTransactionManager를 유연하게 바꿔서 사용할 수 있습니다.

즉,기존 코드는 변경하지 않은 채로 트랜잭션을 실제로 처리하는 구현체를 사용 기술에 따라 바꿀 수 있는 것입니다.

## 스프링 캐시
- @Cacheable, @CacheEvict ..
- Cache도 마찬가지로 JCacheManager, ConcurrentMapCacheManager, EhCacheCacheManager와 같은 여러가지 구현체를 사용할 수 있습니다.

스프링은 이렇게 특정 기술에 직접적 영향을 받지 않게끔 객체를 POJO 기반으로 한번씩 더 추상화한 Layer를 갖고 있으며,
이를통해 일관성있는 Servic Abstraction(서비스 추상화)를 만들어 냅니다.
덕분에 코드는 더 견고해지고 기술이 바뀌어도 유연하게 대처할 수 있게 됩니다.

# AOP(Aspect-oriented Programming)
AOP란 관점 지향 프로그래밍이라고도 부릅니다. 
관점 지향이란 쉽게 말해 어떤 로직을 기준으로 핵심적인 관점, 부가적인 관점으로 나누어서 보고 그 관점을 기준으로 각각 모듈화하겠다는 것입니다.
즉 AOP는 프로그램 구조에 대한 또 다른 사고 방식을 제공하여 객체 지향 프로그래밍(OOP)을 보완합니다.

### AOP는 언제 사용할까요?
- 로깅
- 성능 분석
- 예외 처리
- 메서드 호출 시간 측정
- 공통 관심 사항과 핵심 관심 사항 분리

## AOP 적용
```java
@Component
@Aspect
public class TimeTraceAop {
		@Around("execution(* hello.hellospring..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
		    long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());
        try {
		        return joinPoint.proceed();
		    } finally {
				    long finish = System.currentTimeMillis();
				    long timeMs = finish - start;
				    System.out.println("END: " + joinPoint.toString()+ " " + timeMs + "ms");
				}
		}
}
```

# Spring Bean
스프링 IoC 컨테이너가 관리하는 자바 객체를 말합니다.

```java
public class PhoneStoreTest {
		@Autowired
		ApplicationContext applicationContext;
		
		@Test
		void 빈과_일반_객체의_차이() {
				PhoneStore phoneStore = new PhoneStore(new IPhone());
				
				PhoneStore bean = applicationContext(PhoneStore.class);
				
				assertThat(bean).isNotNull();
		}
}
```

자바의 객체와 스프링 빈의 가장 큰 차이점은 `applicationContext`가 알고 있는 객체인지 여부입니다.

## 스프링 빈 등록하는 방법
- @ComponentScan: @Component 애노테이션이 붙은 모든 클래스를 찾아서 스프링 빈으로 등록
- 직접 XML이나 자바 설정 파일에 등록

## 스프링 빈의 라이프 사이클
- 객체 생성 → 의존성 주입
- 스프링 IoC 컨테이너 생성 → 스프링 빈 생성 → 의존관계 주입 → 초기화 콜백 메소드 호출 → 사용 → 소멸 전 콜백 메소드 호출 → 스프링 종료

# 스프링 애노테이션
## 애노테이션이란?
애노테이션은 소스 코드에서 사용되는 클래스, 인터페이스, 메소드 또는 필드와 인터프리터 및 JVM에서 사용되는 일부 추가 정보와 관련된 구문 메타데이터를 나타내는 데 사용되며, `@`를 붙여서 사용합니다.

## 애노테이션을 사용하는 이유
- 컴파일러에 대한 정보: 컴파일러는 애노테이션을 사용하여 오류를 감지하거나 경고를 억제할 수 있습니다.
- 컴파일 시간 및 배포 시간 처리: 소프트웨어 도구는 주석 정보를 처리하여 코드, XML 파일 등을 생성할 수 있습니다.
- **런타임 처리:** 일부 주석은 런타임 시 검사할 수 있습니다.

## 나만의 애노테이션은 어떻게 만들 수 있을까?
애노테이션을 만들기 위해서는 메타 애노테이션이라는 것은 사용해야 합니다. 
메타 애노테이션에는 다음 4가지가 존재합니다.

- @Target
- @Retention
- @Documented
- @Inherited

각 애노테이션에 대해 알아봅시다.

### @Target
애노테이션을 어떤 것에 적용할지를 선언할 때 사용합니다. 
`@Target` 애노테이션은 다음과 같이 사용할 수 있습니다.

```java
@Target(ElementType.METHOD)    
```

이처럼 괄호 안에 적용 대상을 지정할 수 있는데, 그 대상 목록은 다음과 같습니다.

| 요소 타입 | 대상 |
| --- | --- |
| CONSTRUCTOR | 생성자 선언 시 |
| FIELD | enum 상수를 포함한 필드 값 선언 시 |
| LOCAL_VARIABLE | 지역 변수 선언 시 |
| METHOD | 메서드 선언 시 |
| PACKAGE | 패키지 선언 시 |
| PARAMETER | 매개 변수 선언 시 |
| TYPE | 클래스, 인터페이스, enum 등 선언 시 |

### @Retention
`@Retention`을 사용해서 얼마나 오래 애노테이션 정보가 유지되는지 설정할 수 있습니다.

```java
@Rention(RentionPolicy.RUNTIME)
```

|  | 대상 |
| --- | --- |
| SOURCE | 애노테이션 정보가 컴파일 시 사라짐 |
| CLASS | 클래스 파일에 있는 애노테이션 정보가 컴파일러에 의해서 참조 가능함. 하지만, 가상 머신에서는 사라짐 |
| RUNTIME | 실행 시 애노테이션 정보가 가상 머신에 의해서 참조 가능 |

### @Documented
해당 애노테이션에 대한 정보가 Javadocs(API) 문서에 포함된다는 것을 선언합니다.

### @Inherited
모든 자식 클래스에서 부모 클래스의 애노테이션을 사용 가능하다는 것을 선언합니다.

### 애노테이션을 선언해보자.
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAnnotation {
}
```

## 애노테이션을 통해 스프링 빈 등록 과정
```java
@Configuration
public class AppConfig {
		@Bean
		public MemberRepository memberRepository() {
				return new MemoryMemberRepository();
		}
}
```

1. 스프링 컨테이너를 생성
2. 스프링 컨테이너는 파라미터로 넘어온 설정 클래스 정보를 사용해서 스프링 빈을 등록
    1. 빈을 등록할 때는 항상 다른 이름으로 등록해야 합니다.
3. 스프링 빈 의존 관계 설정

## @ComponentScan
- @Component가 붙은 모든 클래스를 스프링 빈으로 등록합니다.
- 이때 스프링 빈의 기본 이름은 클래스명을 사용하되 맨 앞글자만 소문자를 사용합니다.
    - 빈 이름 기본 전략: MemberService 클래스 → memberService
    - 직접 지정할 수도 있습니다.
- `@SpringBootApplication`에 기본적으로 @ComponentScan이 들어있어 프로젝트 시작 루트 위치부터 탐색해나갑니다.
- 기본적으로 탐색할 패키지의 시작 위치를 지정할 수 있고, 컴포넌트 스캔 대상의 추가 및 제외 대상을 지정할 수 있습니다.
