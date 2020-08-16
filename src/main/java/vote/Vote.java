package vote;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class Vote {

	@Test
	public void CastVote() throws InterruptedException
	{
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		System.setProperty("webdriver.chrome.silentOutput", "true");
		ChromeDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://mycutebaby.in/contest/participant/?n=5f37f5ed82c7f");
		WebElement ele = driver.findElementById("v");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);",ele);
		List<WebElement> nameField = driver.findElementsById("v");
		if(nameField.size()>0)
		{
			driver.findElementById("v").sendKeys("Mangalam");
		}
		else
		{
			System.out.println("NAme field not displayed");
			driver.close();
			System.exit(0);
		}
		driver.findElementById("vote_btn").click();
		Thread.sleep(2000);
		List<WebElement> popup = driver.findElementsByXPath("//button[text()='×']");
		if(popup.size()>0)
		{
			js.executeScript("arguments[0].click()",popup.get(0));
			System.out.println("pop up closed");
		}
		List<WebElement> voteMsg = driver.findElementsById("vote_msg");
		if(voteMsg.size()>0)
		{
			System.out.println("Vote Successful");
			driver.close();
		}
		else
		{
			System.out.println("Vote message not displayed. Vote unsuccessful probably");
			driver.close();
		}
	}
}
