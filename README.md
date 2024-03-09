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
