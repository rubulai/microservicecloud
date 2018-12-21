package com.atguigu.rule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.loadbalancer.IRule;

@Configuration
public class MySelfRuleConfig {
	
	@Bean
	public IRule myRule(){
		return new MySelfRule();//自定义负载均衡算法
	}
}
