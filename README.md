# Spring 의 특징

스프링이 어떠한 특징을 가지고 있길래 그토록 많이 쓰는지 찾아본다.

> ## IoC/DI



### IoC란

```Inversion of Control``` 의 줄임말로 **제어의 역전**이라고 한다.
_제어의 역전_ 은 _**객체들간의 관계 및 호출을 개발자가 아니라 스프링 프레임 워크에 맡기는 것**_을 뜻한다.


전통적인 프로그래밍을 통해서 우리는  객체의 생성 및 초기화 등등을 직접 관리하였다.

하지만 Ioc 를 통한다면 **프레임워크가 프로그램의 흐름을 통제하고 사용자가 작성한 코드를 호출**한다.





### DI란

```Dependency Injection``` 의 줄임말로 **의존성 주입**이라고 한다.


객체지향에서 **의존성**이란  **각 객체들이 서로에게 영향을 받고 그에 따라 결과가
서로에게 영향을 받고 그에 따라 달라지는 것**을 의미한다.

예를 들어서 **매개변수나 리턴값으로 다른 객체에 영향을 미치거나 받는 것을 의존성**이라고 한다.
특정 객체가 변할 경우 그 변경의 영향이 다른 객체에게 많은 영향을 준다면 **의존성이 높다**라고 한다.



### Ioc/DI 예제

예를 들어 아래와 같은 상황을 들어 이 부분을 이해해 보겠다.

_LoginService 는 현재 A 기능으로 개발하고 있으나, 나중에 B 기능으로 바뀔수도 있다_

```
public interface LoginService {

      String Login();

}

```

Login 기능이 여러가지가 나올 수 있으므로 interface로 설정하였다.

```
public class ALoginServiceImpl implements LoginService{

    @Override
    public String Login() {
        return "A Login 기능으로 전환";
    }
}
```

위는 그  interface 를 구현한 로그인 기능이다.

```
@RestController
@RequestMapping(value = "/login")
public class LoginController {
     LoginService loginService=new ALoginServiceImpl();

     @GetMapping
     public String Login(){
         return loginService.Login();
     }
}

```

시간에 지남에 따라 B로그인 기능이 개발된다.


```
public class BLoginServiceImpl implements LoginService{

    @Override
    public String Login() {
        return "B Login 기능으로 전환";
    }
}

```

하지만 이에 따라서** ```controller``` 단의 코드를 변경해 주어야 한다!! **


아래와 같이 말이다.

```
@RestController
@RequestMapping(value = "/login")
public class LoginController {
     LoginService loginService=new BLoginServiceImpl();

     @GetMapping
     public String Login(){
         return loginService.Login();
     }
}
```

이는 특정 객체(LoginService)가 변했기에 그 변경의 영향으로 다른 코드(LoginController)에 영향을 주기 때문에 의존성이 높다라고 볼 수 있다.


이를 스프링에서는 어떻게 방지하는가?? **```@Bean```으로 등록해서 이 부분을 해소**할 수 있다.

```
@Component
public class BLoginServiceImpl implements LoginService{

    @Override
    public String Login() {
        return "B Login 기능으로 전환";
    }
}
```
<li>B 로그인 Impl 부분을 Bean 으로 등록한다.</li>


```
@RestController
@RequestMapping(value = "/login")
public class LoginController {

     @Autowired
     LoginService loginService;

     @GetMapping
     public String Login(){
         return loginService.Login();
     }
}
```
<li>필드 주입을 통해서 빈으로 등록된 객체를 LoginService에 넣어준다</li>


이를 통해서 **ALogin 기능으로 바꾸든 ,BLogin 기능으로 바꾸든 우리는 LoginController 의 코드를 바꿀 필요가 없다. 이를 통해서 의존성을 낮출** 수 있다.


또한 이는 외부에서 **LoginService 에 BLoginServiceImpl 을 넣어줌으로써 DI(의존성 주입) 을 실현하고 또한 로직의 흐름을 직접 제어 하는 것이 아니라, 외부에서 관리하기에 Ioc(제어의 역전) 을 실현**하는 듯 하다!!



> ## AOP

### AOP 정의

AOP 란 Aspect-Oriented Programming 의 줄임말로
관점 지향 프로그래밍을 의미한다.

**관점지향 프로그래밍**이란
**메소드나 객체의 기술을 핵심관심사와 공통 관심사로 분리하여 프로그래밍 하는 것**을 의미한다.

**핵심 관심사란 객체가 가져야할 본래의 기능**이며, **공통 관심사는 여러 객체에서 공통적으로 사용되는 코드**이다.

흔히 공통적으로 사용되는 부분은 로깅, 트랜잭션 ,보안 등등인데
개발자 필요에 따라서 자신이 필요한 부분을  정의해서 사용할 수 있다.

이러한 AOP 는


```
 @Async
    @AfterReturning(pointcut = "annotationPointcut()", returning = "result")
    public void checkValue(JoinPoint joinPoint, Object result) throws Throwable {
        NotifyInfo notifyProxy = (NotifyInfo) result;
        notifyService.send(
                notifyProxy.getReceiver(),
                notifyProxy.getNotificationType(),
                NotifyMessage.YATA_NEW_REQUEST.getMessage(),
                "/api/v1/yata/" + (notifyProxy.getGoUrlId())
        );
        log.info("result = {}", result);
    }
```

이렇게
![](https://velog.velcdn.com/images/dionisos198/post/5836ce0b-2602-43a9-97cf-2b5531109999/image.png)

어노테이션으로 알림 기능이 필요한 메서드마다 쉽게 알림 기능을 AOP 로 활용할 수도 있고,

출처:https://develoyummer.tistory.com/106

동시성문제를 해결하기 위해서 ```Lock```을 사용할 때도

아래 코드와 같이

```
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final RedissonClient redissonClient;

    @Transactional
    public void changeStatus throws(Status desiredStatus) {
        // 레디스 락 데이터 생성 후, 3초 락
        RLock lock = redissonClient.getLock("key 이름");
        
        try {
            boolean isLocked = lock.tryLock(2, 3, TimeUnit.SECONDS);
            if (!isLocked) {
                // 락 획득에 실패했으므로 예외 처리
                throw new Error( ... );
            }
        
            // 새로운 심사 생성 로직
            
        } catch (InterruptedException e) {
	    // 쓰레드가 인터럽트 될 경우의 예외 처리        
        } finally {
            // 락 해제
            lock.unlock();
        }
       	
    }
}
```
설정할 수 있으나,

```changeStatus``` 라는 메서드는 핵심 로직이 따로 있는데 중간에 ```Lock``` 에 대한 코드가 들어가게 되므로 관심사가 분리가 안되고 있다고 생각한다. 뿐만 아니라 ```Lock```을 여러 코드에도 적용할 수 있으므로 ```Aop```를 적용하면 훨씬 깔끔하게 이를 다룰 수 있다.




```
  @Around("@annotation(band.gosrock.domain.common.aop.redissonLock.RedissonLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);
        String baseKey = redissonLock.LockName();

        String dynamicKey =
                generateDynamicKey(
                        redissonLock.identifier(),
                        joinPoint.getArgs(),
                        redissonLock.paramClassType(),
                        signature.getParameterNames());

        RLock rLock = redissonClient.getLock(baseKey + ":" + dynamicKey);

        log.info("redisson 키 설정" + baseKey + ":" + dynamicKey);

        long waitTime = redissonLock.waitTime();
        long leaseTime = redissonLock.leaseTime();
        TimeUnit timeUnit = redissonLock.timeUnit();
        try {
            boolean available = rLock.tryLock(waitTime, leaseTime, timeUnit);
            if (!available) {
                throw NotAvailableRedissonLockException.EXCEPTION;
            }
            log.info(
                    "redisson 락 안으로 진입 "
                            + baseKey
                            + ":"
                            + dynamicKey
                            + "쓰레드 아이디"
                            + Thread.currentThread().getId());
            return callTransactionFactory
                    .getCallTransaction(redissonLock.needSameTransaction())
                    .proceed(joinPoint);
        } catch (DuDoongCodeException | DuDoongDynamicException | TransactionTimedOutException e) {
            throw e;
        } finally {
            try {
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.error(e + baseKey + dynamicKey);
                throw e;
            }
        }
    }
 ```

위 코드를 통해 aop 를 설정하고

 ```
 
    @RedissonLock(LockName = "쿠폰", identifier = "couponId")
    public Long execute(Long userId, Long issuedCouponId) {
  
```

위 코드와 같이 단순히 어노테이션으로 관심사를 분리할 수 있다.



출처: https://github.com/Gosrock/DuDoong-Backend/blob/dev/DuDoong-Domain/src/main/java/band/gosrock/domain/common/aop/redissonLock/RedissonLockAop.java


> ## 스프링 빈

### 스프링 빈이란?

빈은 **스프링 컨테이너에 의해 관리되는 재사용가능한 소프트웨어 컴포넌트**이다.
빈은 인스턴스화된 객체를 의미하며 new 키워드 대신 사용한다고 보면 된다.


스프링 빈의 생명주기는
**스프링 IoC 컨테이너 생성 → 스프링 빈 생성 → 의존관계 주입 → 초기화 콜백 메소드 호출 → 사용 → 소멸 전 콜백 메소드 호출 → 스프링 종료** 의 라이프 사이클을 갖는다.

실제로 디버깅을 통해 스프링 빈이 생성되고 의존관계가 주입되는 것을 확인할 수 있다.

<스프링 빈 생성 이후 호출>
![](https://velog.velcdn.com/images/dionisos198/post/8a14b1ff-e39b-4c78-ae7d-be23eb17c9eb/image.png)

이후 의존관계 주입
![](https://velog.velcdn.com/images/dionisos198/post/7cf91077-5248-43ac-886e-b09a476cef3e/image.png)


> ## 스프링 어노테이션 분석


### 어노테이션이란?

메타데이터란** 애플리케이션이 처리해야 할 데이터가 아니라 컴파일 과정과 실행 과정에서 코드를 어떻게 처리해야 하는지를 알려주기 위한 추가 정보**이다.

자바의 어노테이션은 소스코드에 추가해서 사용할 수 있는 메타데이터의 일종이다.


### 어노테이션 활용 스프링 빈 등록


어노테이션을 활용하여 스프링 빈을 등록 할 수 있는 방법은 2가지가 존재한다.

하나는 @Configuration 과 @Bean 을 사용해서 빈을 수동으로 등록하는 방법이 존재하고 다른 하나는 @Component 를 이용, 자동으로 빈을 등록하는 방법이다.

#### @Configuration + @Bean

```
@Configuration
public class ConfigureTest {

    @Bean
    public Hello hello(){
        return new Hello();
    }

}
```

위와 같이 코드를 작성, 빈을 등록할 수 있다.
hello 라는 빈 이외에도 ConfigureTest 라는 빈도 함께 생성된다.

#### @Component

```
@Component
public class BLoginServiceImpl implements LoginService{

    @Override
    public String Login() {
        return "B Login 기능으로 전환";
    }

    @PostConstruct
    public void start() {
        System.out.println("B Login 빈 시작합니다");
    }

    @PreDestroy
    public void end() {
        System.out.println("B Login 빈 사라집니다");
    }
}
```

위와 같이 ```Component```를 사용해서 빈으로 등록할 수도 있다.

위 ```Configuration```이 Bean 으로 등록되었었던 이유는

아래 사진과 같이 Configuration 에도 @Component가 존재하기 때문이다.

![](https://velog.velcdn.com/images/dionisos198/post/74487a8e-b4ac-4019-af54-e378f3adbb1a/image.png)

마찬가지로 흔히 사용하는 ```@Controller```, ```Service``` 등등 모두 ```@Component``` 가 존재하기 때문에 빈으로 등록되어서 사용된다.


![](https://velog.velcdn.com/images/dionisos198/post/8f289baf-de51-411b-9e2f-586ffc548792/image.png)

![](https://velog.velcdn.com/images/dionisos198/post/abb986f4-0b64-4de7-a88f-5241021d1a8b/image.png)

#### @Component vs (@Configuration + @Bean)
간단하게 차이점을 알아본다.

첫 번째 차이점은 선언위치이다.

```@Bean``` 같은 경우 Target의 ElementType이 METHOD 임을 알 수 있고
![](https://velog.velcdn.com/images/dionisos198/post/e676b56d-9c6c-47cc-a1d3-6e4dd481df47/image.png)

![](https://velog.velcdn.com/images/dionisos198/post/dd3e08bf-4093-43cf-9943-97a5213a02f1/image.png)

위 내용을 통해서 @Bean은 메서드위에만 붙여야 한다는 것을 알 수 있다.

![](https://velog.velcdn.com/images/dionisos198/post/d3020ba6-7142-40d3-a6f0-9f93eb9d378f/image.png)


반면 Component 의 경우 ElementType이 Type이기에 클래스 위에 선언해야 한다는 것을 파악할 수 있다.

![](https://velog.velcdn.com/images/dionisos198/post/3b78e025-cda5-4273-b9be-8a38ab1a3520/image.png)

두 번째 차이점은 사용용도이다.

외부 라이브러리 클래스의 경우 우리가 그 위에다가 직접 Component를 달 수 없으므로, @Bean 을 통해 빈 등록이 가능하다 .

예를 들면

![](https://velog.velcdn.com/images/dionisos198/post/41340c1e-d966-4bd5-84e0-c75002492c48/image.png)


Swagger는 외부라이브러리 이므로 @Bean 으로 등록한다!!





### 스프링에서 자동으로 ComponentScan이 이루어지는 과정


Springboot application 에는 @ComponentScan 이 존재한다.

```
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
```

```ComponentScanAnnotationParser``` 클래스에서 	``` Set<BeanDefinitionHolder> parse(AnnotationAttributes componentScan, String declaringClass)``` 의 메서드를 통해 빈에 대한 ```doScan```을 하여 스캐닝을 하는 걸로 보이며
이를 통해 ```@Component``의 어노테이션을 가진 객체를 식별하여 빈으로 등록하기 위한 스캔을 하는 것으로 생각된다.

![](https://velog.velcdn.com/images/dionisos198/post/28c1ee43-1136-400b-8438-02040a30f304/image.png)

또한 그 스캔된 결과의 빈들이

![](https://velog.velcdn.com/images/dionisos198/post/522b81dc-4240-4ea2-a262-68a7e24476db/image.png)

위와 같이 scannedBeanDefinitions 를 통해 넘어오는 것을 볼수 있었다.

참조:https://minkukjo.github.io/framework/2020/07/09/Spring-133/

### 단위 테스트와 통합 테스트

개념을 먼저 알아본다.

**단위 테스트**란 **하나의 기능 또는 메서드 를 뜻하는 모듈을 기준으로 독립적으로 진행**하는 테스트이다.

반면 **통합 테스트**란 **실제 운영 환경에서 사용될 클래스들을 통합하여 테스트**이다.


#### 단위 테스트 특징

<li>통합 테스트는 여러 컴포넌트들 간의 상호작용을 테스트 하기 때문에 모든 컴포넌트들이 구동된 상태에서 테스트해야 한다. 이에 따라 테스트 시간이 길어질 수 있는데 단위 테스트는 이와 다른 특성을 갖는다</li>

<li>단위 테스트는 해당 기능에 독립적인 테스트 이기 때문에 가짜 데이터(Mock) 을 넣어서 진행한다</li>



#### 통합 테스트 특징

<li>독립적인 기능보다 전체적인 연관 기능과 웹 페이지로 부터 API를 호출하여 올바르게 동작하는지 확인</li>

<li>운영 환경과 유사한 테스트를 작성할 수 있다</li>

<li>테스트 단위가 커서 디버깅이 어려울 수 있다</li>






