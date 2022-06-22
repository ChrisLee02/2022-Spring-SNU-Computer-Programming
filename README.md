# 2022-Spring-SNU-Computer-Programming
2022학년도 봄학기 자료구조(이재욱 교수님) github 저장소입니다.

## 세부 정리

**JAVA 문법**

- ‘==’을 reference type에서 쓰면, 실제로 가리키는 객체가 같은지만 비교하게 된다. 따라서 equals을 써주어야 한다. (대소비교의 경우도 마찬가지다.)
- 자바는 몇 안 되는  primitive type을 제외하고는 모두 reference type이다. ‘.’으로 호출하는 것이 c++에서의 →에 해당하는 셈이다.
- public class는 파일당 딱 하나, class 파일명과 일치하는 이름을 갖도록 선언한다.
- static / dynamic binding → 코드에서 말하는 함수/변수가 실제로 무엇을 가리키는지 잡아주는 것, static은 컴파일 타임에 잡아주고(private, final, static 등 정적인 친구들), dynamic binding은 러닝타임에 JVM이 잡아준다.(overriden function)
- 상속된 클래스의 생성자에서는 super()를 이용해 최상위 클래스까지 생성자 호출이 chaning된다. 만약 상위 클래스의 생성자가 인자를 요구하면, 명시적으로 이를 표시해주어야 함.
- (c++과 공통) 상속 시 specifier를 이용해서 부모 클래스 멤버 변수의 access modifier를 지정해줄 수 있다.

**c++ 문법**

- 자바와 달리, 함수 호출도 캐스팅 타입을 따르게 된다. 이를 방지하기 위해 virtual function으로 선언해주면 dynamic binding된다.
- virtual void A() = 0;과 같은 방식으로 pure virtual function 선언, 하나라도 포함하면 해당 클래스는 abstract class가 된다. (C++은 다중 상속을 지원하므로, 이걸로 자바에서의 인터페이스를 사용하면 됨.
- 소멸자 역시 메소드이므로, 캐스팅 타입을 따르게 됨. virtual function으로 소멸자를 지정해주면, 캐스팅 타입 실수로 인한 memory leak을 방지할 수 있다.
- 연산자 오버로딩은 member or nonmember로 선언 가능, nonmerber일 경우 일반적으로 private에 접근이 불가능하므로, friend 키워드를 이용한다.
- c++은 super()를 생성자 내부에 쓸 수 없음. 명시적인 부모생성자가 필요하면 콜론 뒤에 붙여서 이용한다.
- copy constructor / copy assignment → 둘 다 ‘=’에 관련된 것이다
    - 전자는 첫 선언, 함수의 인자를 pass by value로 건네줄 때, 함수의 리턴타입이 value일 때 호출된다.
    - 후자는 연산자에 해당해서, 위에서 언급한 오버로딩을 이용한다.
- operator()을 이용해서 해당 객체, 구조체를 callable하게 만들 수 있다.
- 함수 포인터로 함수를 가리킬 수 있음.
- lambda function으로 임시로 사용할 간단한 함수 객체를 정의할 수 있다.
- operator(), 함수 포인터, lambda function은 인자로 함수를 전달하고 싶을 때 쓴다. 함수 포인터와 달리 함수 객체는 state를 포함하므로, 조금 더 유용하다. Template에서도 사용 가능함. 예를 들어 인자로 받을 함수를 “사칙연산” 중 하나로 제한하는 등이 가능함.
- sizeof 연산자로는 포인터에 할당된 크기를 알 수 없다. _msize는 윈도우에서만 사용 가능한 함수이며, 일반적인 방법은 없다고 한다.. size변수를 포함해서 wrapping하는 코드만 보임.

**디자인 패턴**

- 변수명 명확하게 / 간결한 함수 / 높은 재사용성 / 주석에 의존하지 않음 / 각 함수&클래스를 그때그때 테스트
- SOLID 원칙 - SRP, OCR, LSP, ISP, DIP
    - Single Responsibility → 한 클래스에는 하나의 기능을 담아라
    - Open - Closed → 클래스를 만드는 것에 있어, 확장성은 열어두고(Open) 수정하는 일은 없도록(Closed) 한다.
    - Liskov Substitution → 상속 관계에 대한 원칙, 자식은 부모의 속성을 모두 substitutable하게 구현한다. 예시: 새-펭귄의 관계에서, 새에다가 fly를 구현해서는 안 된다는 것이다.

      나는 새/못 나는 새로 먼저 나눈 뒤, 나는 새에다가 fly를 구현한다.  TMI: Liskov씨가 발표한 내용이라고 한다.

    - Interface Segregation → 용도에 따라 인터페이스 구현을 세분화한다. 메소드의 구현을 강제하는 것은 꼭 필요한 부분으로만 한정시키라는 것
    - Dependency Inversion → high-level module이 low-level module에 의존해서는 안 된다. 조금 더 구체적으로는, high-level module을 짤 때 low-level module의 세세한 구현에 영향을 받아서는 안 된다는 것이다. 이를 위해 interface를 이용하여 low-level module의 대략적인 기능만 정의해둔 뒤, 그것에 맞게  high-level module을 구현한다.

      참고:[https://dev.to/tamerlang/understanding-solid-principles-dependency-inversion-1b0f](https://dev.to/tamerlang/understanding-solid-principles-dependency-inversion-1b0f)


**OOP: Object - Oriented - Programming**

“OOP is a way of looking at a software system as a collection of interactive object”

위에서 설명한 문법들 역시 Encapsulation, Inheritance, Polymorphism을 구현하기 위한 문법이다.

**Encapsulation: 내부 구현을 잘 숨겨서 사용자는 public method로 해당 객체의 기능에 집중하게 함**

- 클래스 단위에서의 Encapsulate
    - Access modifier(접근 제어자) - private; default; protected; public
    - Getter & Setter - private 변수의 접근을 명시적으로 통제한다. 특히 setter 내부의 로직을 통해 private 변수의 수정에 제한을 둘 수 있음.
- Package: 비슷한 역할의 클래스들을 하나로 묶음
    - protected, default를 통해 보조 기능을 하는 클래스들을 선언하여 패키지 내부에서만 이용되도록 할 수 있음
    - 이름 충돌을 막는다

**Inheritance: 객체들 간의 관계를 tree로 표현, 기능을 상속받고 추가한다.**

- 이름이 같을 때, 변수는 Hiding & 함수는 Overriding

**Polymorphism: 하나의 동작을 상황에 따라(전달받는 인자나 반환 타입/클래스 종류) 다르게 동작하도록 구현한다.**

- 메소드 override
- 함수 overload
- 객체 type casting / generic → wildcard ‘?’ with bound ( ? extends ~ / ? super ~ )

2022.06.22