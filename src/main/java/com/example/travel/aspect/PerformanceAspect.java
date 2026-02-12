package com.example.travel.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PerformanceAspect {

  @Around("@annotation(LogExecutionTime)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    // 메서드 정보
    String methodName = joinPoint.getSignature().toShortString();

    // 시작 시간
    long startTime = System.currentTimeMillis();
    log.info("[시작] {}", methodName);

    Object result;
    try {
      // 실제 메서드 실행
      result = joinPoint.proceed();
    } catch (Throwable e) {
      throw e;
    } finally {
      long endTime = System.currentTimeMillis();
      long duration = endTime - startTime;
      log.info("[종료] {} - {} ms", methodName, duration);
    }

    return result;
  }

}
