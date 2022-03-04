package com.facebook.crawler.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CrawlerService {

	private static final String URL = "https://www.facebook.com/marketplace";
	private static final String URL_BASE = "https://www.facebook.com";
	private static final String DETAIL_HEAD_CLASS = "dati1w0a qt6c0cv9 hv4rvrfc discj3wi";
	private final ChromeDriver driver;

	@PostConstruct
	void postContruct() {
		try {
			login();
			crawlerFacebookMarketPlace();
		} catch (Exception e) {
			System.err.println("Exception:" + e.getMessage());
		}
	}

	public void login() throws Exception {
		driver.get(URL);
		driver.findElement(By.xpath(".//input[@name='email']")).sendKeys("zuyfun.facebook@gmail.com");
		WebElement pass = driver.findElement(By.xpath(".//input[@name='pass']"));
		pass.sendKeys("Gnart 14022020");
		pass.sendKeys(Keys.RETURN);
	}

	public void crawlerFacebookMarketPlace() throws Exception {
		Thread.sleep(5000);
		driver.get(URL);
		final String pageSource = driver.getPageSource();
		getAllCategoryLinks(pageSource);
		List<String> itemLinks = getrAllItemLinks(pageSource);

		itemLinks.forEach(urlDetail -> {
			driver.get(urlDetail);
			getDetailInformation(driver.getPageSource());
		});
		Thread.sleep(5000);
//		driver.quit();
	}

	public List<String> getrAllItemLinks(String htmlSource) {
		try {
			final Document source = Jsoup.parse(htmlSource);
			final Elements itemLinks = source.getElementsByAttributeValueStarting("href", "/marketplace/item");
			if (itemLinks.isEmpty())
				return null;
			List<String> urlDetails = new ArrayList<String>();
			itemLinks.forEach(item -> {
				urlDetails.add(URL_BASE + item.attr("href"));
			});
			return urlDetails;
		} catch (Exception e) {
			System.err.println("Exeption from getrAllItemLinks: " + e);
			return null;
		}
	}

	public void getDetailInformation(String htmlSource) {
		Document source = Jsoup.parse(htmlSource);
		Element head = source.selectXpath("//div[contains(@class, \"" + DETAIL_HEAD_CLASS + "\")]").first();
		System.out.println("Tiêu đề: " + head.getElementsByTag("span").first().text());
	}

	public void getAllCategoryLinks(String htmlSource) {
		try {
			Document source = Jsoup.parse(htmlSource);
			Elements categoryLinks = source.getElementsByAttributeValueStarting("href",
					"https://www.facebook.com/marketplace");
			System.out.println("CategoryLink: "+categoryLinks.toString());
		} catch (Exception e) {
			System.err.println("Exeption from getAllCategoryLinks: " + e);
		}
	}
}
