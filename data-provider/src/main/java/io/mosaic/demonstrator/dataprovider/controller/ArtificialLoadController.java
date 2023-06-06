package io.mosaic.demonstrator.dataprovider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.mosaic.demonstrator.misc.service.ProtocomService;

@RestController
@RequestMapping("load")
public class ArtificialLoadController {

	@Autowired
	private ProtocomService protocomService;
	
	@GetMapping
	public ResponseEntity<String> cpuDemand(){
		protocomService.cpuDemand(50);
		return new ResponseEntity<String>("done",HttpStatus.OK);
	}
	
}
