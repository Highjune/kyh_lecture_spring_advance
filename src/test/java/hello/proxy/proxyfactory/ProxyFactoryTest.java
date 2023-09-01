package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target); // 이 인스턴스에 인터페이스가 있으면 JDK 동적 프록시를 기본 사용, 구체 클래스만 있다면 CGLIB 통해서 동적 프록시 생성
        proxyFactory.addAdvice(new TimeAdvice()); // 직접 만들어준 TimeAdivce 넣어줌. 프록시 통해서 만든 프록시가 사용할 부가 기능 로직 설정
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass()); // targetClass=class hello.proxy.common.service.ServiceImpl
        log.info("proxyClass={}", proxy.getClass()); // proxyClass=class com.sun.proxy.$Proxy13 --> JDK 동적 프록시

        proxy.save();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue(); // ProxyFactory를 통해서 만들었을 경우에만 확인가능 (내가 직접 만든 프록시는 안됨)
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue(); // 상동
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse(); // 상동
    }

    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB 사용")
    void concreteProxy() {
        ConcreteService target = new ConcreteService();
        ProxyFactory proxyFactory = new ProxyFactory(target); // 구체클래스 사용 -> CGLIB 생성
        proxyFactory.addAdvice(new TimeAdvice());
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass()); // proxyClass=class hello.proxy.common.service.ConcreteService$$EnhancerBySpringCGLIB$$dd59b073 => CGLIB 사용

        proxy.call();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }

    @Test
    @DisplayName("ProxyTargetClass 옵션을 사용하면 인터페이스가 있더라도 CGLIB를 사용하고, 클래스 기반 프록시 사용")
    void proxyTargetClass() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); // class를 기반으로 만든다는 말. 즉 CGLIB를 통해서 만들겠다.라는 것.
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass()); // proxyClass=class hello.proxy.common.service.ServiceImpl$$EnhancerBySpringCGLIB$$fd702a1. 구체클래스(ServiceImpl)를 상속받아서 프록시 생성
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }


}
