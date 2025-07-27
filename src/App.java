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


public class App {
    public static void main(String[] args) throws Exception {
        //Automate Search with Selenium
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.amazon.in");
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        searchBox.sendKeys("handbags");
        searchBox.submit();
        Thread.sleep(2000); // Wait for results to load
        
        //Scrape Data
        List<WebElement> products = driver.findElements(By.cssSelector("div.s-main-slot div[data-component-type='s-search-result']"));
        for(WebElement product : products) {
            String title = product.findElement(By.cssSelector("h2 a")).getText();
            String price = product.findElement(By.cssSelector(".a-price-whole")).getText();
            String imageUrl = product.findElement(By.cssSelector("img")).getAttribute("src");
            // Store this in DB or list
        }
        
        //Store in Temporary DB
        Connection conn = DriverManager.getConnection("jdbc:sqlite:products.db");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS products (name TEXT, price REAL, imageUrl TEXT)");

        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO products VALUES (?, ?, ?)");
        pstmt.setString(1, title);
        pstmt.setDouble(2, Double.parseDouble(price.replace(",", "")) );
        pstmt.setString(3, imageUrl);
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
        driver.quit();
        System.out.println("Data saved to database successfully.");
        ResultSet rs = stmt.executeQuery("SELECT * FROM products ORDER BY price ASC");
        
        //Sort and Display
        while(rs.next()) {
            System.out.println(rs.getString("name") + " - ₹" + rs.getDouble("price"));
            System.out.println("Image: " + rs.getString("imageUrl"));
        }
    }
}
