package vote;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class Vote {

	public List<String> nameList = new ArrayList<String>();
	public List<String> usedNameList = new ArrayList<>();

	@Test
	public void fetchNames() throws FileNotFoundException
	{
		Scanner inputFile = new Scanner(new File("./utils/names.txt"));
		while(inputFile.hasNext())
		{
			String name = inputFile.next();
			nameList.add(name);
		}

		Scanner usedNameFile = new Scanner(new File("C:\\Drive\\TestLeaf\\Workspace\\Maven\\MyCBabyComp\\utils\\usedNames.txt"));
		while(usedNameFile.hasNext())
		{
			String name = usedNameFile.next();
			usedNameList.add(name);
		}

		for (int i = 0; i < usedNameList.size(); i++) {
			if(nameList.contains(usedNameList.get(i)))
			{
				nameList.remove(usedNameList.get(i));
				System.out.println("Skipping Name "+usedNameList.get(i));
			}
		}
	}

	@Test(dependsOnMethods = "fetchNames")
	public void CastVote() throws InterruptedException
	{
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		System.setProperty("webdriver.chrome.silentOutput", "true");
		ChromeDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://mycutebaby.in/contest/participant/?n=5f37f5ed82c7f");
		List<WebElement> nameField = driver.findElementsById("v");
		if(nameField.size()>0)
		{
			WebElement ele = driver.findElementById("v");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);",ele);
			System.out.println("Voting with the name: "+nameList.get(0));
			driver.findElementById("v").sendKeys(nameList.get(0));
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
			JavascriptExecutor js = (JavascriptExecutor) driver;
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

	@Test(dependsOnMethods = "fetchNames")
	public void writeNameToNotePad() throws IOException
	{
		FileWriter fw = new FileWriter("C:\\Drive\\TestLeaf\\Workspace\\Maven\\MyCBabyComp\\utils\\usedNames.txt", true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);
		
		pw.println(nameList.get(0));
		pw.flush();
		pw.close();
		bw.close();
		fw.close();
		System.out.println("Name "+nameList.get(0)+" written in the file");
	}
}
