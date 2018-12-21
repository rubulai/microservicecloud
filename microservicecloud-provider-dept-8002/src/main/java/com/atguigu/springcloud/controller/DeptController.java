package com.atguigu.springcloud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.springcloud.entities.Dept;
import com.atguigu.springcloud.service.DeptService;

@RestController
public class DeptController {
	@Autowired
	private DeptService service;

	@Autowired
	private DiscoveryClient client;

	@RequestMapping(value = "/dept/discovery", method = RequestMethod.GET)
	public Object discovery() {
		//获取所有的微服务
		List<String> services = client.getServices();
		System.out.println(">>>>" + services);
		//获取具体名称的微服务:相同名称的微服务可能有多个——即所谓的分布式集群
		List<ServiceInstance> instances = client.getInstances("MICROSERVICECLOUD-DEPT");
		for (ServiceInstance inst : instances) {
			System.out.println(
					inst.getServiceId() + "\t" + inst.getHost() + "\t" + inst.getPort() + "\t" + inst.getUri());
		}
		return this.client;
	}

	@RequestMapping(value = "/dept/add", method = RequestMethod.POST)
	public boolean add(@RequestBody Dept dept) {
		return service.add(dept);
	}

	@RequestMapping(value = "/dept/get/{id}", method = RequestMethod.GET)
	public Dept get(@PathVariable("id") Long id) {
		return service.get(id);
	}

	@RequestMapping(value = "/dept/list", method = RequestMethod.GET)
	public List<Dept> list() {
		return service.list();
	}

}
