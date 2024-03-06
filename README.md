# spring-tutorial-19th
CEOS 19th BE Study - Spring Tutorial

# Week 1

## DI: Dependency Injection
**먼저 이해하면 좋은 마인드**
- 의존성 주입? 의존성이라는 특별한 개념을 추가하는 것인가?
  
  -> No. 예전에는 설계할 때 의존성이라는 risky한 성질을 고려하지 못 하는 경우가 많다.(유지보수 및 동작 risk)
  
  -> 이러한 의존성이라는 성질을 **"필요할 때만"** 주입해준다.

### Dependency
- 의존성이 있다?
  
  -> 한 객체가 다른 객체를 사용한다.

  -> Controlle(API 호출)가 Service(data 연산)를 호출해서 기능하는 예시


```java
public class Controller {
    private AlphaService alphaService

    public Controller() {
        this.alphaService = new AlphaService();
    }
}
```

위의 예시는 다음과 같은 문제점을 가진다.
1. Controller와 Alphaservice가 강하게 결합되어 있다.
    - 만약 Controller가 AlphaService가 아닌 다른 클래스를 연동하고자 한다면, Controller의 생성자 변경이 필요하다.
    -> 유연성이 떨어진다.
    - 상속은 제약이 많고 확장성이 떨어지므로 피하는 것이 좋다.

2. <U>**객체**들 간의 관계가 아니라 **클래스** 간의 관계가 맺어졌다.</U>
    - 올바른 OOP 설계라면 객체들 간의 관계가 맺어져야 한다.
    - 객체들 간의 관계가 맺어졌다면, 다른 객체의 구체 클래스(AlphaService, BetaService 등)을 알지 못 하더라도, 인터페이스(Service)의 타입으로 사용할 수 있다.



### 의존성 주입(Dependency Injection)을 통한 문제 해결
- 위와 같은 문제를 해결하기 위해서 **다형성**이 필요하다.
  
  다형성 : 같은 자료형에 여러 가지 객체를 대입하여 다양한 결과를 얻어내는 성질

- Serivce라는 Interface를 통해 해결한다(Controller에서 구체 클래스에 의존하지 않게 된다).
```java
public interface Service {

}

public class AlphaService implements Service {

}
```
- 이제 Controller와 AlphaService가 강하게 결합되어 있는 부분을 Injection으로 변경해준다.

```java
public class Controller {
    private Service service;

    public Controller(Service service) {
        this.servcie = service;
    }
}
```

- Service라는 interface가 컨트롤러에 할당되어 있지만, Alpha, Beta 등 다양한 서비스가 들어올 수 있다!
  
  (Service 자리만 맡아놓고, Service에 해당하면 모든 클래스 자유롭게 탈부착 가능)

### Spring이라는 DI Container가 필요한 이유
- 그래도 변경사항이 생기면 여전히 클래스마다 돌아다니면서 다 바꿔줘야 하는 거 아님?
  
  -> 그것도 Spring으로 관리해보자.

- 어플리케이션 실행 시점에 필요한 객체(**Bean**)을 생성하고, 의존성이 있는 두 객체를 연결하기 위해 한 객체를 다른 객체로 주입시킨다.
  
  *Bean : Spring이 관리하는 객체

```java
public class BeanFactory {

    public void controller() {
        // Bean의 생성
        Product AlpahService = new Service();
    
        // 의존성 주입
        Controller controller = new Controller(AlphaService);
    }
    
}
```

- 그러면 Controller와 Service를 연결&변경 할 때 마다 일일이 클래스 파일 뒤져갈 필요 없이 Bean들의 연결 상태만 확인해주면 되겠네!
  
  (클래스마다 생성, 주입이 있는 것이 아니라, Bean들의 연결을 관리하는 클래스 하나에서 생성, 주입을 전부 확인할 수 있다.)

## Code에서의 적용

- @Controller 어노테이션이 있으면, 스프링 컨테이너가 해당 클래스를 bean으로 관리한다.
- service, repository  등은 하나만 생성해서 공통으로 가져다 쓰면 된다(new로 새로 만들 필요 없다).

### 정리하면: 의존성 주입이란?
- 외부에서 두 객체 간의 관계를 결정해주는 디자인 패턴
- 인터페이스를 사이에 둬서, 클래스 레벨에서는 의존관계가 고정되지 않도록 하고 런타임 시에 관계를 동적으로 주입
  
  -> 유연성 확보하고 결합도를 낮춘다.


- Controller 코드에서 constructor로 Service 연결 부분 작성하고 **@Autowired**로 스프링이 연결 관리.
    - **Issue**: 이 때 Service가 Spring에 의해 관리되지 않는 순수한 Java class라면 에러가 발생한다.
    - **Solve**: Service와 Reposisory도 각각 어노테이션(@)으로 스프링 bean 등록.
- DI에는 필드 주입, setter 주입, 생성자 주입 [3가지 방법](https://ceos19th.notion.site/7e4ac16678c44de4a6035bf799a786ae)이 있다.
- 의존관계가 실행중에 동적으로 변하는 경우는 거의 없으므로 생성자 주입을 권장한다.
- 실무에서는 주로 정형화된 컨트롤러, 서비스, 리포지토리 같은 코드는 컴포넌트 스캔을 사용한다.
- 정형화 되지 않거나, 상황에 따라 구현 클래스를 변경해야 하면 설정을 통해 스프링 빈으로 등록한다.
    - ex. 비즈니스 로직만 있을 때

## 그럼 Spring Bean은 어떻게 등록하죠?
크게 2가지 방법이 있다. 하나는 **직접 등록**하는 것이고, 하나는 **Componenet Scan**이다.
직접 등록하는 경우도 있지만, 대부분 Componenet Scan으로 커버된다.

### 1. Componenet Scan
- **@Component** 애노테이션이 있으면 스프링 빈으로 자동 등록된다.
- **@Component**를 포함하는 다음 애노테이션도 스프링 빈으로 자동 등록된다.
    - **@Controller**
    - **@Service**
    - **@Repository** 

### 2. 직접 등록
- **@Configuration** 어노테이션으로 설정 파일에 등록

```java
package hello.hellospring; 

import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

## Bean Life Cycle
그럼 Bean은 어떻게 Spring에서 동작하는가?

<div align="center">
  <img src="imgs/bean_life_cycle.png" alt="drawing" width=500"/>
</div>

Bean 생성 및 소멸시 다양한 콜백이 있지만 일단,
**Spring이 시작시 Bean들이 생성 및 등록되어서 사용(DI)된 후 Spring이 종료되기 전에 소멸된다.**
정도로 이해하면 충분하다.

## IoC: Inversion of Control
제어의 역전 역시 어렵지 않다.
- 우리(개발자)가 일일이 객체를 생성하고 언제 종료할지 정하지 않아도 된다.
  - Spring이 알아서 Bean life cycle 관리해주고 Componenet들도 DI로 관리
  - Spring 철학대로만 개발하면 객체들은 Spring이 관리해준다.
  - 제어를 일일이 우리가 하지 않아도 되네? == 제어의 역전

## AOP

## PSA

## Spring Bean

## Annotation

## Unit Test & Integration Test



