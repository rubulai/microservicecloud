package com.atguigu.rule;

import java.util.List;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

public class MySelfRule extends AbstractLoadBalancerRule {

	private Integer totalAccess = 0;
	private Integer currentIndex = 0;

	@Override
	public Server choose(Object key) {// 主要是重写该方法完成负载均衡的自定义
		// 该组件由容器注入,见父类
		ILoadBalancer loadBalancer = getLoadBalancer();
		if (loadBalancer == null)
			return null;
		Server server = null;// 具体的某一台服务器
		while (server == null) {
			if (Thread.interrupted())
				return null;
			// 获取与微服务应用对应的所有注册至Eureka的服务提供者列表(Eureka实例)
			List<Server> allServers = loadBalancer.getAllServers();
			// 获取所有与微服务应用对应的可用的服务提供者列表
			List<Server> reachableServers = loadBalancer.getReachableServers();
			if (allServers.size() == 0)
				return null;

			if (totalAccess < 5) {
				// 获取到具体的某台机器
				server = reachableServers.get(currentIndex);
				totalAccess++;
			} else {
				totalAccess = 0;
				currentIndex++;
				if (currentIndex >= reachableServers.size())
					currentIndex = 0;
			}
			if (server == null) {
				Thread.yield();
				continue;
			}
			if (server.isAlive())
				return server;
			server = null;
			Thread.yield();
		}
		return server;
	}

	@Override
	public void initWithNiwsConfig(IClientConfig config) {
		// TODO Auto-generated method stub
	}
}
