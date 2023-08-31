package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkDynamicProxyTest {
    // 아래 dynamicA(), dynamicB() 두 테스트를 봤을 때는 각각의 프록시를 다 따로 만들어주는 것이 아니라
    // JDK 동적 프록시를 사용해서 TimeInvocationHandler 하나만 만들어서 다 처리하고 있는 것을 알 수 있다.

    @Test
    void dynamicA() {
        AInterface target = new AImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // 자바 언어차원에서 제공해주는 프록시 기술임
        AInterface proxy = (AInterface) Proxy.newProxyInstance(
                AInterface.class.getClassLoader(),  // 어떤 클래스를 로드할지
                new Class[]{AInterface.class}, // 어떤 인터페이스 기반으로 프록시 만들지
                handler); // 프록시가 사용해야 하는 로직

        proxy.call(); // handler의 invoke()를 호출해준다. 그리고 TimeInvocationHandler의 invoke() 안에서는 target의 call()을 호출함
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass()); // proxy 는 jdk 에서 만들어서 반환해주는 프록시
    }

    @Test
    void dynamicB() {
        BInterface target = new BImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        // 자바 언어차원에서 제공해주는 프록시 기술임
        BInterface proxy = (BInterface) Proxy.newProxyInstance(
                BInterface.class.getClassLoader(),  // 어떤 클래스를 로드할지
                new Class[]{BInterface.class}, // 어떤 인터페이스 기반으로 프록시 만들지
                handler); // 프록시가 사용해야 하는 로직

        proxy.call(); // handler의 invoke()를 호출해준다.
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass()); // proxy 는 jdk 에서 만들어서 반환해주는 프록시
    }
}
