package aop

import com.techcourse.pointcut.*
import com.techcourse.sample.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.aop.aspectj.AspectJExpressionPointcut
import org.springframework.aop.framework.ProxyFactory
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.aop.support.NameMatchMethodPointcut
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut

/**
 * 스프링 AOP
 *
 * 스프링 AOP는 순수 자바로 구현됩니다. 특별한 컴파일 프로세스가 필요하지 않습니다.
 * 스프링 AOP는 클래스 로더 계층 구조를 제어할 필요가 없으므로 서블릿 컨테이너 또는 애플리케이션 서버에서 사용하기에 적합합니다.
 *
 * 스프링 AOP의 AOP 접근 방식은 다른 AOP 프레임워크와 다릅니다.
 * 완벽한 AOP 구현을 제공하는 것이 목표가 아닙니다(스프링 AOP만으로도 충분한 기능을 제공합니다).
 * 그보다는 엔터프라이즈 애플리케이션의 일반적인 문제를 해결하는 데 도움이 되도록 AOP 구현과 스프링 IoC 간의 긴밀한 통합을 제공하는 것이 목표입니다.
 * 따라서, 스프링 프레임워크의 AOP 기능은 일반적으로 스프링 IoC 컨테이너와 함께 사용됩니다.
 * 애스펙트(Aspect)는 일반 빈 정의 구문을 사용하여 구성됩니다. 이것은 다른 AOP 구현과 중요한 차이점입니다.
 * 도메인 객체에 대한 어드바이스와 같은 일부 작업은 스프링 AOP로는 쉽고 효율적으로 수행할 수 없습니다.
 * 이러한 경우에는 AspectJ를 사용하세요.
 * 스프링 AOP는 엔터프라이즈 자바 애플리케이션의 대부분의 문제를 처리할 수 있는 솔루션을 제공합니다.
 *
 * 스프링 프레임워크의 핵심 철학 중 하나는 비침투성(non-invasiveness)입니다.
 * 이는 비즈니스 또는 도메인 모델에 프레임워크 클래스 및 인터페이스를 강제로 도입해서는 안 된다는 개념입니다.
 * 그러나 필요에 따라 코드베이스에 스프링 프레임워크의 특정 종속성을 도입할 수 있는 옵션을 제공합니다.
 * 이러한 옵션을 제공하는 이유는 특정 상황에서는 이 방식이 특정 기능을 읽거나 코딩하는 데 더 쉬울 수 있기 때문입니다.
 * 그러나 스프링 프레임워크는 거의 대부분 선택권을 제공합니다.
 * 특정 사용 사례나 시나리오에 가장 적합한 옵션을 개발자가 자유롭게 결정할 수 있습니다.
 *
 * AOP와 스프링 AOP
 * | 용어       | 개념                          | 인터페이스     |
 * |-----------|------------------------------|-------------|
 * | Target    | 부가기능(advice)을 적용할 대상    |             |
 * | Advice    | 특정 Joinpoint에 실행되는 코드    | Interceptor |
 * | Joinpoint | Advice를 적용할 위치            | Invocation  |
 * | Pointcut  | Advice를 적용할 Joinpoint를 선별 | Pointcut    |
 * | Aspect    | Pointcut + Advice            | Advisor     |
 */
class SpringAOP : FreeSpec({

    """
    AOP 개념
    AOP에서 사용하는 개념과 용어를 이해하는 것이 중요합니다.
    하지만 설명만 보고 이해하기 어렵기 때문에 학습 테스트 코드로 설명하겠습니다.
    스프링 AOP를 사용한 예제를 통해 AOP 개념에 대해 알아보겠습니다.
    """ - {

        // AOP 프로세스에 의해 실행 흐름이 수정되는 객체를 🌟타깃(target)이라고 합니다.
        // advised object라고도 합니다.
        // 핵심 로직, 비즈니스 로직을 담고 있습니다.
        val target = World()

        """
        스프링 AOP를 이해하기 위해 간단한 Hello, World! 예제를 살펴보겠습니다.
        스프링 AOP는 기본적으로 동적 프록시를 사용하여 AOP를 구현합니다.
        스프링은 가능한 한 자체 정의한 인터페이스 대신 AOP 얼라이언스 인터페이스를 사용합니다.
        AOP 얼라이언스(alliance)는 AOP 구현체의 표준 인터페이스입니다.
        """ {
            // 스프링 AOP가 제공하는 ProxyFactory는 AOP 프록시를 생성하는 데 사용됩니다.
            val proxyFactory = ProxyFactory()
            proxyFactory.setTarget(target) // 프록시를 만들 타깃을 설정합니다.
            proxyFactory.addAdvice(HelloAroundAdvice()) // ❗️HelloAroundAdvice 클래스의 주석도 읽어보세요!

            // 타깃을 프록시로 만들 때 인터페이스가 아닌 클래스를 사용하도록 설정할 수 있습니다.
            // 기본값은 false이고, JDK 프록시를 사용합니다. 대상이 인터페이스를 구현하지 않으면 CGLIB 프록시를 사용합니다.
            // true로 설정하면 CGLIB 프록시를 사용합니다. 대상이 인터페이스를 구현하더라도 CGLIB 프록시를 사용합니다.
            proxyFactory.isProxyTargetClass = true

            val proxy = proxyFactory.proxy as World

            target.message shouldBe ""
            proxy.message shouldBe ""
        }

        """
        스프링 AOP 어드바이스(Advice)
        어드바이스는 타깃에 적용할 부가기능을 정의합니다.
        스프링의 어드바이스는 타깃의 메서드로 제한하고 있습니다.
        스프링이 제공하는 다양한 어드바이스는 어떤 종류가 있는지 살펴보겠습니다.
        어떤 어드바이스를 사용할지 선택할 때 애플리케이션의 요구사항에 따라 적절한 어드바이스 타입을 선택하는 것이 중요합니다.
        가장 구체적인 어드바이스 타입을 사용해야 코드의 의도를 명확하게 할 수 있고 에러가 발생할 가능성을 줄일 수 있습니다.
        """ - {

            """
            비포 어드바이스(Before Advice)
            비포 어드바이스는 메서드가 실행되기 전에 실행할 부가기능을 정의합니다.
            비포 어드바이스는 메서드에 전달된 인수를 변경하거나 예외를 발생시켜 메서드 실행을 막을 수 있습니다.
            """ {
                val proxyFactory = ProxyFactory()
                proxyFactory.setTarget(target)
                proxyFactory.addAdvice(HelloBeforeAdvice())

                val proxy = proxyFactory.proxy as World

                // proxy.message를 호출하면 HelloBeforeAdvice의 before 메서드가 실행됩니다.
                // 콘솔창 메시지를 확인하세요.
                proxy.message shouldBe ""
            }

            """
            애프터 리터닝 어드바이스(After Returning Advice)
            반환값을 수정할 수 없고 메서드 실행 후 처리를 추가할 수 있습니다.
            실행 후 반환값이 원하는 조건에 맞지 않을 경우 예외를 발생시킬 수 있습니다.
            """ {
                val proxyFactory = ProxyFactory()
                proxyFactory.setTarget(target)
                proxyFactory.addAdvice(HelloAfterReturningAdvice())

                val proxy = proxyFactory.proxy as World

                // proxy.message를 호출하면 HelloAfterReturningAdvice의 before 메서드가 실행됩니다.
                // 콘솔창 메시지를 확인하세요.
                proxy.message shouldBe ""
            }

            """
            스로우 어드바이스(Throws Advice)
            스로우 어드바이스를 사용하면 애플리케이션에서 예외 처리와 로깅을 한 곳으로 모을 수 있습니다.
            Exception 계층 구조에 따라 예외를 처리할 수 있습니다.
            애플리케이션 코드를 수정하지 않고 추가 로깅 코드를 추가하여 디버깅할 수 있습니다.
            """ {
                val errorBean = ErrorBean()

                val proxyFactory = ProxyFactory()
                proxyFactory.setTarget(errorBean)
                proxyFactory.addAdvice(ExceptionCaptureThrowsAdvice())

                val proxy = proxyFactory.proxy as ErrorBean

                val exception = shouldThrow<Exception> {
                    // 콘솔창 메시지를 확인하세요.
                    proxy.exception()
                }
                exception.message shouldBe ""

                val illegalArgumentException = shouldThrow<IllegalArgumentException> {
                    // 콘솔창 메시지를 확인하세요.
                    proxy.illegalArgumentException()
                }
                illegalArgumentException.message shouldBe ""
            }
        }

        """
        스프링 AOP 포인트컷(Pointcut)과 어드바이저(Advisor)
        어드바이스 적용을 모든 메서드가 아닌 특정 메서드로 제한하려면 포인트컷을 사용해야 합니다.
        어드바이스 자체에서 제한 조건을 검사할 수도 있지만 그렇게 되면 어드바이스가 복잡해지고 재사용성이 떨어집니다.
        어드바이스 적용을 제어하는 검사 로직은 포인트컷을 사용합시다.
        
        스프링 AOP가 제공하는 Pointcut 인터페이스를 살펴보면 MethodMatcher를 지원하는 것을 알 수 있습니다.
        MethodMatcher는 메서드 이름, 메서드 시그니처, 메서드 인수 등을 사용하여 메서드를 선택할 수 있습니다.
        """ - {

            """
            메서드 이름 매칭 포인트컷
            오직 메서드 이름만을 기준으로 포인트컷을 정의할 수 있습니다.
            간단하게 포인트컷을 정의할 때 사용합니다.
            """ {
                val methodPointcut = NameMatchMethodPointcut()
                methodPointcut.addMethodName("getMessage")
                val advice = SimpleAdvice()
                val advisor = DefaultPointcutAdvisor(methodPointcut, advice)

                val proxyFactory = ProxyFactory()
                proxyFactory.addAdvisor(advisor)
                proxyFactory.setTarget(target)
                val proxy = proxyFactory.proxy as World

                proxy.message shouldBe ""
            }

            """
            정적 포인트컷(StaticMethodMatcherPointcut)
            대상 메서드에 대해 한 번만 MethodMatcher의 matches() 메서드를 호출합니다.
            반환값은 캐싱하여 메서드를 호출하는 데 사용합니다.
            메서드를 호출할 때 추가적인 검사가 필요하지 않아서 동적 포인트컷보다 성능이 훨씬 좋습니다.
            """ {
                val pointcut = SimpleStaticPointcut()
                val advice = SimpleAdvice()
                val advisor = DefaultPointcutAdvisor(pointcut, advice)

                val proxyFactory = ProxyFactory()
                proxyFactory.addAdvisor(advisor)
                proxyFactory.setTarget(target)
                val proxy = proxyFactory.proxy as World

                proxy.message shouldBe ""
            }

            """
            동적 포인트컷(DynamicMethodMatcherPointcut)
            메서드를 호출하면 matches(Method, Class<T>) 메서드를 사용해 정적 검사를 합니다.
            여기서 true를 반환하면 matches(Method, Class<T>, Object[]) 메서드를 사용해 추가로 검사를 수행합니다.
            동적 MethodMatcher를 사용하면 메서드의 특정 호출을 기반으로 포인트컷 적용 여부를 결정할 수 있습니다.
            보통은 정적 포인트컷을 사용하는 것이 성능상 유리하지만 동적으로 어드바이스를 결정할 필요가 있을때 사용합시다.
            """ {
                val sampleBean = SampleBean()
                val pointcut = SimpleDynamicPointcut()
                val advice = SimpleAdvice()
                val advisor = DefaultPointcutAdvisor(pointcut, advice)

                val proxyFactory = ProxyFactory()
                proxyFactory.addAdvisor(advisor)
                proxyFactory.setTarget(sampleBean)
                val proxy = proxyFactory.proxy as SampleBean

                proxy.dynamicPointcut(1) shouldBe ""
                proxy.dynamicPointcut(100) shouldBe ""
            }

            """
            AspectJ 포인트컷 표현식
            스프링 AOP보다 많은 기능을 가진 AOP 구현체인 AspectJ를 사용하면 표현식으로 포인트컷을 정의할 수 있습니다.
            AspectJ를 사용하려면 build.gradle에 aspectjweaver, aspectjrt 의존성을 추가해야 합니다.
            """ {
                val pointcut = AspectJExpressionPointcut()
                pointcut.expression = "execution(* getMessage*(..))"
                val advice = SimpleAdvice()
                val advisor = DefaultPointcutAdvisor(pointcut, advice)

                val proxyFactory = ProxyFactory()
                proxyFactory.addAdvisor(advisor)
                proxyFactory.setTarget(target)
                val proxy = proxyFactory.proxy as World

                proxy.message shouldBe ""
            }

            """
            애너테이션 매칭 포인트컷
            커스텀 애너테이션이 적용된 메서드나 타입에 어드바이스를 적용하고 싶을 때 사용합니다.
            """ {
                val sampleBean = SampleBean()
                val pointcut = AnnotationMatchingPointcut.forMethodAnnotation(CustomAnnotation::class.java)
                val advice = SimpleAdvice()
                val advisor = DefaultPointcutAdvisor(pointcut, advice)

                val proxyFactory = ProxyFactory()
                proxyFactory.addAdvisor(advisor)
                proxyFactory.setTarget(sampleBean)
                val proxy = proxyFactory.proxy as SampleBean

                proxy.annotationPointcut() shouldBe ""
            }
        }
    }
})
