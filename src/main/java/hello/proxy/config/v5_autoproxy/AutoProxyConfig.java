package hello.proxy.config.v5_autoproxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppV1Config.class, AppV2Config.class}) // V3는 Component 대상이라 자동 bean으로 등록이 된다. 그런데 여기서 하지 않고 스프링 메인 메서드에서 Import 에 같이 넣어도 된다.
public class AutoProxyConfig {

//    @Bean
    public Advisor advisor1(LogTrace logTrace) {
        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*"); // 3가지로 시작하는 모든 메서드들에게 다 적용되므로 의도치 않은 것들에도 적용됨.

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice); // advisor 등록
    }

//    @Bean
    public Advisor advisor2(LogTrace logTrace) {
        // pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..))"); // hello.proxy.app 패키지와 그 하위 패키지의 모든 메서드는 포인트컷의 매칭 대상, 그런데 no-log도 적용이 된다.

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice); // advisor 등록
    }

    @Bean
    public Advisor advisor3(LogTrace logTrace) {
        // pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))");

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice); // advisor 등록
    }

}
