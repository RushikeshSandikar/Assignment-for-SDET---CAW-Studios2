package Project2.Project2;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TableAutomation {

    public static void main(String[] args) {
    	
        // Set the path to the ChromeDriver executable
    	WebDriverManager.chromedriver().setup();

        // Create a new instance of the Chrome driver
        WebDriver driver = new ChromeDriver();

        // Open the browser and navigate to the URL
        driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");

        try {
            // Step 2: Click on the Table Data button
            WebElement tableDataButton = driver.findElement(By.xpath("//summary[normalize-space()='Table Data']"));
            tableDataButton.click();

            // Step 3: Insert the given data and click on Refresh Table button
            WebElement inputTextBox = driver.findElement(By.xpath("//textarea[@id='jsondata']"));
            JsonArray data = new JsonArray();
            data.add(createJsonObject("Bob", 20, "male"));
            data.add(createJsonObject("George", 42, "male"));
            data.add(createJsonObject("Sara", 42, "female"));
            data.add(createJsonObject("Conor", 40, "male"));
            data.add(createJsonObject("Jennifer", 42, "female"));

            inputTextBox.clear();
            inputTextBox.sendKeys(data.toString());
            
            //Find refresh button
            WebElement refreshButton = driver.findElement(By.xpath("//button[@id='refreshtable']"));
            refreshButton.click();

            // Allow some time for the table to refresh
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

            // Step 5: Assert the data in the UI table matches the entered data
            
            List<WebElement> tableRows = driver.findElements(By.xpath("//table[@id='testTable']/tbody/tr"));
            for (int i = 0; i < tableRows.size(); i++) {
                List<WebElement> columns = tableRows.get(i).findElements(By.tagName("td"));
                assertEqual(columns.get(0).getText(), data.get(i).getAsJsonObject().get("name").getAsString());
                assertEqual(Integer.parseInt(columns.get(1).getText()), data.get(i).getAsJsonObject().get("age").getAsInt());
                assertEqual(columns.get(2).getText(), data.get(i).getAsJsonObject().get("gender").getAsString());
            }

            System.out.println("Test passed! Data in UI table matches the entered data.");

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        
        } finally {
        	
            // Close the browser
            driver.quit();
        }
    }

    private static JsonObject createJsonObject(String name, int age, String gender) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("age", age);
        jsonObject.addProperty("gender", gender);
        return jsonObject;
    }

    private static void assertEqual(Object actual, Object expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("Assertion failed: Expected [" + expected + "], but got [" + actual + "]");
        }
    }
}

