import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class MainTest {
	private WebDriver wd;
	private String url;
	private ArrayList<Map<String, Object>> data;
	
	@Before
	public void setUp() throws IOException{
		String chDriver = new File(new File(".").getCanonicalPath()+"\\" 
				+ "driver/chromedriver.exe").getCanonicalPath();
		System.setProperty("webdriver.chrome.driver", chDriver);
		System.setProperty("webdriver.chrome.bin", "C:\\Program Files (x86)"
				+ "\\Google\\Chrome\\Application\\chrome.exe");
		url = "http://www.ncfxy.com/";
		
		wd = new ChromeDriver();
		wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		data = new ArrayList<Map<String, Object>>();
		

		try {  
            BufferedReader reader = new BufferedReader(new FileReader("file/info.csv")); 
            String line = null;  
            while((line = reader.readLine()) != null){  
            	Map<String, Object> map = new HashMap<String, Object>();
            	
            	String username = (String) line.subSequence(0, line.indexOf(","));
            	map.put("username", username);
            	map.put("password", username.substring(username.length() - 6));
            	map.put("email", line.subSequence(line.indexOf(",") + 1, line.length()));
                data.add(map); 
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}
	
	@Test
	public void testLogin(){
		wd.get(url);
		
		for(int i = 0; i < data.size(); i++){
			WebElement usernameElement = wd.findElement(By.id("name"));
			usernameElement.sendKeys(data.get(i).get("username").toString());
			
			WebElement passwordElement = wd.findElement(By.id("pwd"));
			passwordElement.sendKeys(data.get(i).get("password").toString());
			
			WebElement buttonElement = wd.findElement(By.id("submit"));
			buttonElement.click();
			
			ArrayList<WebElement> trElements = wd.findElements(By.cssSelector("#table-main tr"));
			ArrayList<WebElement> tdElements = trElements.get(0).findElements(By.cssSelector("td"));
			assertEquals(data.get(i).get("email"), tdElements.get(1).getText());
			
			wd.navigate().back();
			wd.navigate().refresh();
		}
	}

}
