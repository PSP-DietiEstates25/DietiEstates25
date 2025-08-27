package com.dietiestates.api.aspect;

import java.util.Arrays;
import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(1)
@Component
public class LoggingAspect {

	private Logger logger = Logger.getLogger(LoggingAspect.class.getName());
	
	@Around("execution(* services.*.*(..))")
	public void log(ProceedingJoinPoint joinPoint) throws Throwable {
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		
		logger.info("===================================");
		
		logger.info("Method executed: " + methodName);
		logger.info("With arguments: " + Arrays.asList(arguments));
		
		Object result = joinPoint.proceed();
		if(result != null) {
			logger.info("Returned: " + result);
		}
		
		logger.info("Method execution ends.");
		
		logger.info("===================================");
	}
}
