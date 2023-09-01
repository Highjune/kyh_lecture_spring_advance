package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class TimeAdvice implements MethodInterceptor {

    // target을 굳이 만들어서 넣어줄 필요 없다. ProxyFactory를 생성할 때 target을 미리 넣어준다.

    /**
     * Advice ? => 프록시에서 사용하는 부가기능(즉 내가 넣고 싶은 기능. ex) 로그))
     * 프록시의 부가 기능 로직도 특정 기술에 종속적이지 않게 Advice 하나로 편리하게 사용할 수 있다.
     * 이것은 프록시 팩토리가 내부에서 JDK 동적 프록시인 경우 InvocationHandler 가 Advice 를 호출하도록 개발해두고,
     * CGLIB인 경우 MethodInterceptor 가 Advice 를 호출하도록 기능을 개발해두었기 때문이다.
     */

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        // target 클래스의 정보는 이미 MethodInvocation 안에 포함되어 있다.
        Object result = invocation.proceed();// 안에서 스스로 알아서 target을 찾아서 실제를 호출해준다.

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime={}", resultTime);
        return result;
    }
}
