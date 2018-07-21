package com.mountainframework.config.init.netty;

import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.mountainframework.common.Constants;
import com.mountainframework.common.StringPatternUtils;
import com.mountainframework.common.bean.AddressSplitResult;
import com.mountainframework.config.ProtocolConfig;
import com.mountainframework.config.ServiceReferenceConfig;
import com.mountainframework.config.init.InitializingService;
import com.mountainframework.config.init.context.MountainApplicationContext;
import com.mountainframework.config.init.context.MountainConfigContainer;
import com.mountainframework.registry.ServiceDiscovery;
import com.mountainframework.registry.model.RegistryUrl;
import com.mountainframework.remoting.netty.NettyExecutors;

public class NettyConsumerInitializer implements InitializingService {

	private final Set<ServiceReferenceConfig> referenceConfigs = MountainConfigContainer.getContainer()
			.getServiceReferenceConfigs();

	@Override
	public void init(MountainApplicationContext context) {
		ProtocolConfig protocolConfig = context.getConsumerProtocol();
		String protocolName = protocolConfig.getName();
		String protocolHost = protocolConfig.getHost();
		Integer protocolPort = protocolConfig.getPort();
		String serializeProtocolName = protocolConfig.getSerialize();

		Set<String> serviceDiscoveryAddress = Sets.newHashSet();
		Set<ServiceDiscovery> registries = context.getConsumerRegistry();
		for (ServiceDiscovery serviceRegistry : registries) {
			for (ServiceReferenceConfig referenceConfig : referenceConfigs) {
				RegistryUrl url = new RegistryUrl();
				url.setServiceName(referenceConfig.getInterfaceName());
				String serviceAddress = serviceRegistry.substribe(url);
				serviceDiscoveryAddress.add(serviceAddress);
			}
		}
		for (String address : serviceDiscoveryAddress) {
			AddressSplitResult splitResult = StringPatternUtils.splitAddress(address, Constants.PROTOCOL_DELIMITER);
			AddressSplitResult addressResult = StringPatternUtils.splitAddress(splitResult.getRight(),
					Constants.ADDRESS_DELIMITER);
			String serviceProtocol = splitResult.getLeft();
			String host = addressResult.getLeft();
			String port = addressResult.getRight();
			Preconditions.checkNotNull(host, "Registry get host is null");
			Preconditions.checkNotNull(host, "Registry get port is null");
			Integer portInt = Integer.parseInt(port);
			if (host.equals(protocolHost) && portInt.equals(protocolPort) && serviceProtocol.equals(protocolName)) {
				NettyExecutors.clientExecutor().start(host, Integer.valueOf(port), serializeProtocolName);
			}
		}
	}

}
