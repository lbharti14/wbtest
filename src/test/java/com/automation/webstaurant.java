package com.automation;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class webstaurant {

	@Test
//	public static void main(String[] args) throws InterruptedException {

	public void main() {
		WebDriverManager.chromedriver().setup();
		String myProperty = System.getProperty("myProperty");
		System.setProperty("webdriver.chrome.driver", myProperty);

		// Setup
		WebDriver driver = new ChromeDriver();

		// Step 1. Go to https://www.webstaurantstore.com/
		driver.get("https://www.webstaurantstore.com/");
		driver.manage().window().maximize();

		// Step 2. Search for 'stainless work table'
		driver.findElement(By.id("searchval")).sendKeys("stainless work table");
		driver.findElement(By.cssSelector(".hidden #banner-search-group .text-white")).click();

		// Assert on URL after search criteria execution
		String searchLandingURL = driver.getCurrentUrl();
		String actualSearchLandingURL = "https://www.webstaurantstore.com/search/stainless-work-table.html";
		Assert.assertEquals(actualSearchLandingURL, searchLandingURL);

		// Step 3.Check the search result ensuring every product has the word 'Table' in
		// its title

		List<WebElement> items = driver.findElements(By.xpath("//div[@id='ProductBoxContainer']/div/a/span"));

		for (WebElement item : items) {
			String title = item.getText();
			if (!title.contains("Table"))

			{
				System.out.println("Test Aborted because a product without 'Table' in its title is found- " + title);
				return;
			}

		}

		// Step 4.Add the last of found items to Cart : Get the last pagination link ,
		// navigate to the last page and add the last item to the cart

		List<WebElement> pagination = driver.findElements(By.xpath(" //div[@id='paging']/nav/ul/li"));

		if (pagination.size() > 1) {
			WebElement secondLastPageLink = pagination.get(pagination.size() - 2);
			secondLastPageLink.click();
		}

		WebElement lastFoundItem = driver
				.findElement(By.xpath("(//div[@id='ProductBoxContainer']/div/a/span)[last()]"));
		WebElement lastAddToCartButton = driver.findElement(By.xpath("(//input[@name='addToCartButton'])[last()]"));
		String itemName = lastFoundItem.getText();
		System.out.println("Last Found item in the cart is:" + itemName);
		lastAddToCartButton.click();

		// 5.Empty Cart
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement closeButton = driver.findElement(By.cssSelector(".duration-300 > svg"));
		js.executeScript("arguments[0].scrollIntoView(true)", closeButton);
		driver.findElement(By.id("td")).click();
		driver.findElement(By.id("cartItemCountSpan")).click();
		driver.findElement(By.cssSelector(".emptyCartButton")).click();
		driver.findElement(By.cssSelector(".text-shadow-black-60")).click();

		// Assert that cart is empty
		String className = "empty-cart__text";
		List<WebElement> elements = driver.findElements(By.className(className));
		Assert.assertTrue(elements.isEmpty(), "Test failed because page did not have empty cart text class !");

		// TearDown
		driver.close();

	}

}
