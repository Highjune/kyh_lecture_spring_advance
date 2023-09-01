package hello.proxy.cglib;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {

    @Test
    void cglib() {
        ConcreteService target = new ConcreteService(); // 구체클래스

        // CGLIB는 구체클래스를 상속받아서 프록시를 생성할 수 있다. 어떤 구체 클래스를 상속 받을지 지정한다.
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ConcreteService.class);
        enhancer.setCallback(new TimeMethodInterceptor(target));
        ConcreteService proxy = (ConcreteService) enhancer.create(); // proxy 생성, setSuperclass로 ConcreteService 설정했으므로 타입캐스팅 가능
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass()); // ConcreteService를 상속받아서 CGLIB 생성한 것

        proxy.call();
    }
}
