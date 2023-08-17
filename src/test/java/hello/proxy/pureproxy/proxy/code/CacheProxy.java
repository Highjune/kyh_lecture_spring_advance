package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheProxy implements Subject {

    private Subject target; // 실제 객체(target이라고 함. 프록시가 호출하는 실제 객체)
    private String cacheValue; // 캐싱할 데이터

    public CacheProxy(Subject target) {
        this.target = target;
    }

    @Override
    public String operation() {
        log.info("프록시 호출");
        if (cacheValue == null) {
            cacheValue = target.operation(); // null이 아니라면 실제 객체 호출 안함 => 접근 제어
        }
        return cacheValue;
    }
}
