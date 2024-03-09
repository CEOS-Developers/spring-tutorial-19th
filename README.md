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
