package aop

import com.techcourse.aspectj.AopConfig
import com.techcourse.sample.World
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest

/**
 * AspectJ
 *
 * 스프링 AOP는 일반적인 문제를 해결하기 위해 간단한 AOP 구현을 제공하는 것을 목표로 합니다.
 * 스프링 AOP는 완전한 솔루션을 제공하지 않습니다. 스프링 컨테이너에서 관리하는 Bean에만 적용할 수 있습니다.
 * AspectJ는 완전한 AOP 솔루션 제공을 목표로 합니다. 스프링 AOP보다 더 다양한 기능을 제공합니다.
 *
 * AspectJ와 스프링 AOP의 가장 큰 차이점은 위빙(weaving) 시점입니다.
 * 위빙이란 애플리케이션 코드의 적절한 위치에 애스펙트(Advisor)를 적용하는 과정을 말합니다.
 * 스프링 AOP는 프록시 메커니즘을 사용해 런타임 시점에 위빙을 수행합니다.
 * 반면 AspectJ는 컴파일 시점(CTW)이나 로딩 시점(Load-Time Weaving, LTW)에 위빙을 수행합니다.
 * AspectJ는 자바만으로 잘 해결되지 않는 횡단 관심사(crosscutting concerns)를 해결하기 위해 사용됩니다.
 *
 * 스프링 AOP와 AspectJ의 지원하는 Joinpoint의 차이점은 다음과 같습니다.
 *
 * | Joinpoint                    | Spring AOP | AspectJ |
 * | ---------------------------- | ---------- | ------- |
 * | Method Call                  | No         | Yes     |
 * | Method Execution             | Yes        | Yes     |
 * | Constructor Call             | No         | Yes     |
 * | Constructor Execution        | No         | Yes     |
 * | Static initializer execution | No         | Yes     |
 * | Object initialization        | No         | Yes     |
 * | Field reference              | No         | Yes     |
 * | Field assignment             | No         | Yes     |
 * | Handler execution            | No         | Yes     |
 * | Advice execution             | No         | Yes     |
 */
@SpringBootTest(classes = [AopConfig::class])
class AspectJ(
    private val world: World
) : FreeSpec({

    """
    @AspectJ를 사용하여 AspectJ AOP를 적용합니다.
    AnnotatedAdvice 클래스에 @Aspect를 사용하여 AspectJ AOP를 적용합니다.
    @Component를 붙여야 스프링 빈으로 등록됩니다.
    AopConfig 클래스에 @EnableAspectJAutoProxy를 사용하여 AspectJ AOP를 활성화합니다.
    SpringAOP 테스트에서 직접 구현했던 advice, pointcut, advisor를 애너테이션으로 대체하면 코드가 간결해집니다.
    """ - {
        world.message shouldBe "Hello, World!"
    }
})
