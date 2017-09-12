package com.balancenotifier.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Configuration
@PropertySources({@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true),
	   @PropertySource(value = "file:${conf.file}", ignoreResourceNotFound = true)})
@Service	
public class ConfigUtil {

	private static String VERSION = "Version: 1.0.1";
	
	@Autowired
	private Environment env;
	
	public Environment getEnvironment()
	{
		return env;
	}
	
	public String getAppVersion(){
		return VERSION;
	}
}