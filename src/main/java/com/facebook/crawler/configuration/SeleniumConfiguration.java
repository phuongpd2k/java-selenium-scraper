package com.facebook.crawler.configuration;


import javax.annotation.PostConstruct;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class SeleniumConfiguration {
	
	@PostConstruct
	void postConstruct() {
		System.setProperty("webdriver.chrome.driver", new ClassPathResource("/src/main/resources/driver/chromedriver.exe").getPath());
	}
	
	@Bean
	public ChromeDriver driver () {
		ChromeOptions options = new ChromeOptions();
//		options.addArguments("--user-data-dir=PATH");
		options.addArguments("--disable-notifications");
		options.addArguments("--mute-audio");
		options.addArguments("--incognito");
//		options.addArguments("--proxy-server=40.122.44.51:3128");
		
		return new ChromeDriver(options);
	}
}
