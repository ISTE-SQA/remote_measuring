package io.mosaic.demonstrator.dataprovider;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "io.mosaic.demonstrator.dataprovider.controller" })
public class WebConfig {

}
