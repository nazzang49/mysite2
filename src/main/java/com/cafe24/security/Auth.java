package com.cafe24.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {

	//애노테이션 사용 시 활용할 수 있는 속성 생성
	//String value() default "USER";
	//int test() default 0;
	public enum Role{
		USER,
		ADMIN
	}
	public Role role() default Role.USER;
	
}
