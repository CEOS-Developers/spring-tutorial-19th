# spring-tutorial-19th
CEOS 19th BE Study - Spring Tutorial
***
# WEEK 1
### 1. Spring에서 지원하는 기술들
#### 1.1 IoC
IoC는 '제어의 역전' 이라는 의미로, 메소드나 객체의 호출 작업을 개발자가 직접 하는 것이 아닌, 외부에 의해서 하는 것이다.  
객체의 의존성을 역전 시키면 객체간의 **결합도** 를 줄이고 유연한 코드를 작성할 수 있다고 한다.  
Controller, Service 같은 객체들의 동작을 우리가 직접 구현하기는 하지만, 해당 객체들이 어느 시점에 호출될 지는 신경쓰지 않는다. 단지 프레임워크가 요구하는대로 객체를 생성하면, 프레임워크가 해당 객체들을 가져다가 생성하고, 메서드를 호출하고, 소멸시킨다. 프로그램의 제어권이 역전된 것이다.  

#### 1.2 DI
DI는 위에서 설명했던 IoC를 구현하기 위한 한가지 방법으로 이해할 수 있다.  
DI는 객체를 직접 생성하는 것이 아니라 **외부**에서 생성 한 후 **주입** 해주는 것이다.  
```java
public class kimin {
    Ceos ceos;

    public kimin() {
        ceos = new Ceos();
    }
}
```  
위의 예시에서 kimin 클래스는 Ceos 클래스에 의존한다.  
이때 kimin 클래스는 클래스 내부에서 직접 의존성을 주입하고 있다. 이렇게 되면 다음의 문제가 생긴다.  
1. 두 클래스가 강하게 결합되어있다.  
   - 두 클래스 사이의 유연함이 떨어진다. 확장성이 떨어진다고 볼 수 있기 때문에 문제가 된다.
   - 따라서 다른 의존 관계를 사용하고 싶다면 클래스 내부의 코드를 직접 수정해야 한다.
2. 모든 kimin 클래스는 Ceos 클래스만을 의존한다. 
   - 생성되는 모든 kimin 클래스는 자동으로 Ceos 클래스를 의존하게 된다.  

따라서 의존성을 외부에서 주입하는 방식을 채택해야 한다. 방법은 크게 3가지로 나뉜다.  
1. 생성자 주입 방식
```java
public class kimin {
    private Ceos ceos;
    
    public kimin(Ceos ceos) {
        this.ceos = ceos;
    }
}
```
위와 같이 생성자에서 parameter로 의존성을 주입 받아 사용하는 방식이다. 이렇게 되면 Kimin 클래스는 다양한 클래스에 의존할 수 있다.
2. Setter 방식
```java
public class kimin {
    private Ceos ceos;
    
    public void setter(Ceos ceos) {
        this.ceos = ceos;
    }
}
```
별도의 Setter 메서드를 호출하여 의존성을 주입하는 방식이다.
3. 필드 주입 방식
```java
import org.springframework.beans.factory.annotation.Autowired;

public class kimin {
    @Autowired
    Ceos ceos;
}
```
```java
import org.springframework.stereotype.Component;

@Bean
public class Ceos implements IT {
    /*...*/
}
```
Spring에서 제공하는 **@Autowired** 어노테이션을 통해 kimin 클래스에 의존성을 주입할 수 있다.  
4. 필드 주입 방식 (with Lombok)
```java
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class kimin {
    private final Ceos ceos;
}
```
위의 코드에서는 lombok 에서 제공하는 **@RequiredArgsConstructor** 어노테이션을 통해 의존성을 주입한다.  
**@RequiredArgsConstructor** 이 어노테이션은 final 로 되어있는 필드에 자동으로 의존성을 주입해주는 역할을 한다.
---
#### 1.3 AOP
AOP란 Aspect Oriented Programming의 약자로, **관점을 기준으로 다양한 기능을 분리하여 보는 프로그래밍이다.**

AOP는 기존의 OOP, 즉 객체 지향 프로그래밍을 보완하기 위해 등장한 개념이다.
>AOP의 목적은 횡단 관심사의 코드를 핵심 비즈니스 로직의 코드와 분리하여, 코드의 간결성을 높이고 변경에 유연함과 무한한 확장이 가능하도록 하는 것이다.

예를 들어, 로그인/회원가입 을 진행하는 시간을 측정해야 한다면 다음과 같이 할 수 있다.
```java
public void join(JoinRequest joinRequest){
    long beginTime = System.currentTimeMillis();
    try{
        memberRepository.save(joinRequest.toMember());    
    } finally {
        log.info(System.currentTimeMillis() - beginTime);    
    }
}
```
이 코드에서 사실상 memberRepository.save() 부분이 핵심 비즈니스 로직에 해당되고, 나머지 코드는 모두 횡단 관심사에 해당하는 인프라 로직으로 이해할 수 있다.  
**하지만 위의 코드가 모든 Service 코드에 반영되어야 한다면???**  

위의 문제를 해결하기 위해 횡단 관심사의 코드와 핵심 비즈니스 로직의 코드를 분리하는 것이다.

```java
@Component
@Aspect
public class Time {
    @Around("execution(* 적용할 범위 설정.*(..))")
    public Object checkTime(ProceedingJoinPoint pjp) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object retVal = pjp.proceed(); 
        System.out.println(System.currentTimeMillis() - begin); 
        return retVal; 
    }
}
```
**@Aspect** 어노테이션을 통해 이 클래스가 Aspect 를 나타내는 클래스라고 명시하고, **@Component** 어노테이션을 통해 스프링 빈으로 등록한다.
**@Around** 는 어드바이스가 타겟 메서드를 감싸서 호출 전과 후에 기능을 수행 하도록 한다.
---
#### 1.4 PSA
PSA란 환경의 변화와 관계없이 일관된 방식의 기술로의 접근 환경을 제공하는 추상화 구조를 말한다.
따라서 PSA가 적용된 코드는 개발자의 기존에 작성된 코드를 수정하지 않으면서 확장할 수 있다.
<img width="595" alt="스크린샷 2024-03-08 오전 1 17 16" src="https://github.com/CEOS-Developers/spring-tutorial-19th/assets/97235034/385c68bb-15a6-45ed-b3ea-15bb8193869c">

JdbcConnector 인터페이스가 애플리케이션에서 이용하는 하나의 서비스가 되고, 각각 여러개의 데이터베이스가 DbClient 클래스와 간접적으로 연결되어 있다.  
데이터베이스의 변경이 있을때에도, 인터페이스와의 연결을 통해서 갈아끼우면 되는 것이므 getConnection() 메서드를 통해(원래 사용하던 코드를 통해) 변경할 수 있다.
```java
public interface JdbcConnector {
    Connection getConnection();
}
```
```java
public class MariaDBJdbcConnector implements JdbcConnector {
    @Override
    public Connection getConnection() {
        // ...
    }
}
```
```java
public class OracleJdbcConnector implements JdbcConnector {
    @Override
    public Connection getConnection() {
        // ...
    }
}
```