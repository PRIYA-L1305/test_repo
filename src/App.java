
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;


public class App {

    public static void main(String[] args) throws Exception {
        //Automate Search with Selenium
        System.out.println("Starting web scraping...");
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.flipkart.com/search?q=shoes&otracker=search&otracker1=search&marketplace=FLIPKART&as-show=on&as=off&p%5B%5D=facets.brand%255B%255D%3DPUMA&p%5B%5D=facets.brand%255B%255D%3DADIDAS&p%5B%5D=facets.brand%255B%255D%3DBata");
        /*List<WebElement> products = driver.findElements(By.xpath("//div[@class=\"syl9yP\"]"));
        System.out.println("Total products found: " + products.size());
        System.out.println("----------------------------------------");

        List<WebElement> productprices = driver.findElements(By.xpath("//div[@class=\"Nx9bqj\"]"));

        for(int i=0;i<products.size();i++) {
            System.out.println(products.get(i).getText() + " - " +productprices.get(i).getText().replaceAll("[^\\d,]", " ") );
        }*/
        Thread.sleep(2000); // Wait for results to load
        //Scrape Data
        //List<WebElement> products = driver.findElements(By.cssSelector("div.s-main-slot div[data-component-type='s-search-result']"));
        //Store in Temporary DB
        Connection conn = DriverManager.getConnection("jdbc:sqlite:products.db");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS products (name TEXT, price REAL, imageUrl TEXT)");

        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO products VALUES (?, ?, ?)");

        // Loop and scrape + insert
        System.out.println("Scraping products...");
        List<WebElement> products = driver.findElements(By.xpath("//div[@class=\"syl9yP\"]"));
        for (WebElement product : products) {
            try {
                String title = product.findElement(By.xpath("//div[@class=\"syl9yP\"]")).getText();
                //String priceStr = product.findElement(By.xpath("//div[@class=\"Nx9bqj\"]")).getText().replaceAll("[^\\d,]", " ");
                //String imageUrl = product.findElement(By.cssSelector("img")).getAttribute("src");
                pstmt.setString(1, title);
                pstmt.executeUpdate();
            } catch (Exception e) {
                // Skip any malformed product
                System.out.println("Skipping a product due to missing data: " + e.getMessage());
            }
        }
        List<WebElement> productprices = driver.findElements(By.xpath("//div[@class=\"Nx9bqj\"]"));
        for (WebElement priceElement : productprices) {
            String priceStr = priceElement.getText().replaceAll("[^\\d,]", " ");
            double price = Double.parseDouble(priceStr.replace(",", ""));
            pstmt.setDouble(2, price);
            pstmt.executeUpdate();
        }
        pstmt.close();
        System.out.println("Data saved to database successfully.");
        ResultSet rs = stmt.executeQuery("SELECT * FROM products ORDER BY price ASC");
        //driver.quit();
        //Sort and Display
        System.out.println("Products sorted by price:");
        while(rs.next()) {
            System.out.println(rs.getString("name") + " - Price: " + rs.getDouble("price"));
            //System.out.println("Image: " + rs.getString("imageUrl"));
        }
        conn.close();
    }
}
