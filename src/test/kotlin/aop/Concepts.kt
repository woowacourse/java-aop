package aop

import com.techcourse.aop.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec

/**
 * AOP 기본 개념
 *
 * 🌟AOP(Aspect-Oriented Programming)란 무엇일까요?
 * 애플리케이션의 핵심적인 기능에서 부가적인 기능을 분리하여 애스펙트라는 모듈로 만들어 설계하는 개발 방법을 애스펙트 지향 프로그래밍(AOP)이라고 부릅니다.
 *
 * 객체지향 기술의 설계 방법으로는 독립적인 모듈화가 불가능한 경우가 있습니다.
 * 예를 들면, 트랜잭션 경계설정과 같은 부가기능은 객체지향 기술로 모듈화하기 어렵습니다.
 * 부가기능이란 핵심 비즈니스 로직을 수행하는 메서드에 추가적으로 수행되어야 하는 기능을 말합니다.
 * AOP는 이러한 부가기능을 핵심 비즈니스 로직과 분리하여 모듈화하는 기술입니다.
 * 부가기능 모듈은 객체가 아닌 🌟애스펙트(Aspect)라고 부릅니다.
 * AOP는 OOP를 돕고 보완하는 기술입니다. OOP를 대체하는 기술이 아닙니다.
 * AOP는 OOP로 해결하기 어려운 문제를 애스펙트로 분리함으로써 객체지향적인 설계를 지킬 수 있도록 도와줍니다.
 */
class Concepts : FreeSpec({

    """
    AOP 종류
    객체지향 언어인 자바에서 AOP 기술을 구현하려면 어떻게 해야 할까요?
    AOP를 구현하기 위한 기술은 🌟동적(Dynamic) AOP와 🌟정적(Static) AOP로 나눌 수 있습니다.
    둘 중 한 가지 방법만 선택하지 않아도 됩니다. 두 가지 방법을 혼합하여 사용할 수 있습니다.
    요구사항에 따라 스프링 AOP가 이미 제공하는 동적 AOP 기반 솔루션을 사용하여 애플리케이션을 구축하고,
    성능이 중요한 부분은 정적 AOP인 🌟AspectJ를 사용할 수 있습니다.
    여기서는 동적 AOP를 살펴보겠습니다. 정적 AOP는 @AspectJ를 참고하세요.
    """ - {

        """
        동적(Dynamic) AOP
        런타임에 애플리케이션 코드의 특정 지점에 부가기능을 추가하는 방법입니다.
        스프링 AOP는 프록시를 사용하여 AOP를 구현합니다.
        프록시가 런타임에 생성되기 때문에 동적 AOP라고 부릅니다.
        동적 AOP는 정적 AOP만큼 성능이 나오지 않지만, 성능이 꾸준히 개선되고 있습니다.
        그리고 다시 컴파일하지 않아도 애플리케이션에서 전체 애스펙트를 적용할 수 있는 장점이 있습니다.
        
        동적 AOP 구현체도 여러 방식으로 나눌 수 있습니다.
        스프링 AOP에서 주로 사용하는 JDK Proxy와 CGLIB를 살펴보겠습니다.
        
        ❗️프록시(Proxy)와 프록시 패턴(Proxy pattern)은 다른 개념입니다.
        프록시는 클라이언트가 사용하려는 실제 타깃인 것처럼 위장해서 클라이언트 요청을 대신 받아줍니다. 대리인과 같은 역할입니다.
        프록시를 쓰는 목적은 1. 클라이언트가 타깃에 접근하는 방법을 제어하거나 2. 타깃에 부가기능을 부여하기 위해 사용합니다.
        디자인 패턴에서는 두 가지 목적을 다른 패턴으로 구분합니다.
        타깃에 접근하는 방법을 제어하는 패턴은 프록시 패턴(Proxy pattern)이고,
        타깃에 부가기능을 부여하는 패턴은 데코레이터 패턴(Decorator pattern)이라 부릅니다.
        """ - {

            """
            🌟JDK Proxy
            동적 AOP는 JDK가 제공하는 Proxy 클래스를 사용하여 구현할 수 있습니다.
            스프링에서 사용할 수 있는 가장 기본적인 프록시 타입입니다.
            JDK 프록시는 클래스가 아닌 인터페이스 기반의 프록시를 생성합니다.
            ❗️JDK 프록시를 사용하는 객체는 반드시 인터페이스를 구현해야 합니다.
            """ {
                // SecureBean 인터페이스를 구현한 프록시 객체를 생성
                val jdkDynamicAopProxy = JdkDynamicAopProxy()
                jdkDynamicAopProxy.setInterfaces(SecureBean::class.java);
                jdkDynamicAopProxy.addAdvice(
                    SecurityInvocationHandler(
                        SecureBeanTarget()
                    )
                )
                val secureBean = jdkDynamicAopProxy.proxy as SecureBean

                // 인증된 사용자로 로그인하여 메시지를 출력
                val securityManager = SecurityManager()
                securityManager.login("gugu", "password")
                secureBean.writeSecureMessage()
                securityManager.logout()

                // 인증되지 않은 사용자
                shouldThrow<IllegalStateException> {
                    secureBean.writeSecureMessage()
                }

                // invalid user 사용자
                shouldThrow<IllegalStateException> {
                    securityManager.login("invalid user", "password")
                    secureBean.writeSecureMessage()
                }
            }

            """
            🌟CGLIB(Code Generator Library) Proxy
            서드파티 코드나 수정할 수 없는 레거시 코드로 작업할 때 인터페이스를 사용할 수 없는 경우가 있습니다.
            이때는 CGLIB 프록시를 사용해야 합니다.
            CGLIB가 동적으로 새 클래스에 대한 바이트코드를 생성하고, 이미 생성된 클래스를 사용할 수 있다면 재사용합니다.
            이때 생성되는 프록시 타입은 대상 클래스의 하위 클래스가 됩니다.
            CGLIB 프록시는 타깃 메서드에 적절한 바이트코드를 생성하여 프록시로 인한 성능 오버헤드를 줄입니다.
            매번 invoke()를 호출하는 JDK 프록시와 비교하면 CGLIB는 한 번만 수행되기에 성능이 더 좋습니다.
            ❗️주의할 점은 상속을 사용하여 프록시를 생성하므로 ❌final 클래스나 ❌final 메서드, ❌private 메서드는 프록시할 수 없습니다.
            Enhancer 클래스를 사용하여 CGLIB 프록시를 생성할 수 있습니다.
            """ {
                val cglibAopProxy = CglibAopProxy()
                cglibAopProxy.addAdvice(SecurityMethodInterceptor())
                cglibAopProxy.setTargetClass(SecureBeanTarget::class.java)
                val secureBean = cglibAopProxy.proxy as SecureBeanTarget

                // 인증된 사용자로 로그인하여 메시지를 출력
                val securityManager = SecurityManager()
                securityManager.login("gugu", "password")
                secureBean.writeSecureMessage()
                securityManager.logout()

                // 인증되지 않은 사용자
                shouldThrow<IllegalStateException> {
                    secureBean.writeSecureMessage()
                }

                // invalid user 사용자
                shouldThrow<IllegalStateException> {
                    securityManager.login("invalid user", "password")
                    secureBean.writeSecureMessage()
                }
            }
        }
    }
})
