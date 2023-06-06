package io.mosaic.demonstrator.devicecommunication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.mosaic.demonstrator.devicecommunication.dto.DisplayData;


@RestController
@RequestMapping("user-rest")
public class UserController {

	@GetMapping(produces="application/json")
    public DisplayData getDataForDisplay() {
		DisplayData displayData = new DisplayData("sample");

        return displayData;
    }
	
	
}
