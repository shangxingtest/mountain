package com.mountainframework.registry.zookeeper.service;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mountainframework.registry.ServiceRegistry;
import com.mountainframework.registry.model.RegistryUrl;
import com.mountainframework.registry.zookeeper.bean.ZookeeperConstant;

/**
 * 基于 ZooKeeper 的服务注册接口实现
 * 
 * @author yafeng.cai {@link}https://github.com/AaronCai0
 * @since 1.0
 */
public class ZooKeeperServiceRegistry implements ServiceRegistry {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZooKeeperServiceRegistry.class);

	private final ZkClient zkClient;

	public ZooKeeperServiceRegistry(String zkAddress) {
		// 创建 ZooKeeper 客户端
		zkClient = new ZkClient(zkAddress, ZookeeperConstant.ZK_SESSION_TIMEOUT,
				ZookeeperConstant.ZK_CONNECTION_TIMEOUT);
		LOGGER.debug("connect zookeeper");
	}

	@Override
	public void register(RegistryUrl url) {
		// 创建 registry 节点（持久）
		String registryPath = ZookeeperConstant.ZK_REGISTRY_PATH;
		if (!zkClient.exists(registryPath)) {
			zkClient.createPersistent(registryPath);
			LOGGER.debug("create registry node: {}", registryPath);
		}
		// 创建 service 节点（持久）
		String servicePath = registryPath + "/" + url.getServiceName();
		if (!zkClient.exists(servicePath)) {
			zkClient.createPersistent(servicePath);
			LOGGER.debug("create service node: {}", servicePath);
		}
		// 创建 address 节点（临时）
		String addressPath = servicePath + ZookeeperConstant.SERVICE_TEMP_NODE_PREFIX;
		String addressNode = zkClient.createEphemeralSequential(addressPath, url.getServiceAddress());
		LOGGER.debug("create address node: {}", addressNode);
	}

	@Override
	public void unregister(RegistryUrl url) {

	}
}