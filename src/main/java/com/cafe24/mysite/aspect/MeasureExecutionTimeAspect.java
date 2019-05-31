package com.cafe24.mysite.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
public class MeasureExecutionTimeAspect {

	@Around("execution(* *..repository.*.*(..)) || execution(* *..service.*.*(..)) || execution(* *..controller.*.*(..))")
	public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
		//bfo
		StopWatch sw = new StopWatch();
		sw.start();
		
		//메소드 실행
		Object result = pjp.proceed();

		//after
		sw.stop();
		Long totalTime = sw.getTotalTimeMillis();
		
		//본 메소드가 실행하는 객체의 클래스명 추출
		String className = pjp.getTarget().getClass().getName();
		String methodName = pjp.getSignature().getName();
		String taskName = className+"."+methodName;
		
		//System.out.println("[Execution Time] / ["+taskName+"]");
		//System.out.println("totalTime --> "+totalTime);
		
		return result;
	}
	
}
