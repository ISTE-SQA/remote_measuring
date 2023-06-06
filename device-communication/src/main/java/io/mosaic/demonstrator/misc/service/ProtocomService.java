package io.mosaic.demonstrator.misc.service;

import org.palladiosimulator.protocom.resourcestrategies.activeresource.AbstractDemandStrategy;
import org.palladiosimulator.protocom.resourcestrategies.activeresource.DegreeOfAccuracyEnum;
import org.palladiosimulator.protocom.resourcestrategies.activeresource.cpu.FibonacciDemand;
import org.springframework.stereotype.Component;

@Component
public class ProtocomService {

	private final AbstractDemandStrategy resourceDemand = new FibonacciDemand();

	public void init() {
		//TODO:: store calibration data as container volume
		resourceDemand.initializeStrategy(DegreeOfAccuracyEnum.HIGH, 1000);
		resourceDemand.ensureCalibrationExists();
	}
	
	public void cpuDemand(long demand) {
		// emulate data tagging
		resourceDemand.consume(demand);
	}
	
}
