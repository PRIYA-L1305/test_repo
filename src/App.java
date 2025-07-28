

// import java.sql.Connection;
// import java.sql.Statement;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
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
        driver.get("https://www.amazon.in/s?k=shoes&i=shoes&rh=n%3A1571283031%2Cp_123%3A198664%257C232840%257C256097%257C627141&dc&crid=1ID1OYOAUH73A&qid=1753711251&rnid=91049095031&sprefix=%2Caps%2C351&ref=sr_nr_p_123_7&ds=v1%3AykfNgrL3SXrIjzopGlqFlXU74qctpzMHy4DFPsawzQE");
        List<WebElement> products = driver.findElements(By.xpath("//div[@class='a-row a-size-base a-color-secondary']"));
        System.out.println("Total products found: " + products.size());
        System.out.println("----------------------------------------");

        List<WebElement> productprices = driver.findElements(By.xpath("//div[@class=\"a-section a-spacing-small aok-align-center\"]"));

        for(int i=0;i<products.size();i++) {
            System.out.println(products.get(i).getText() + " - " + productprices.get(i).getText());
        }
        Thread.sleep(2000); // Wait for results to load
        //Scrape Data
        /*List<WebElement> products = driver.findElements(By.cssSelector("div.s-main-slot div[data-component-type='s-search-result']"));
        //Store in Temporary DB
        Connection conn = DriverManager.getConnection("jdbc:sqlite:products.db");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS products (name TEXT, price REAL, imageUrl TEXT)");

        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO products VALUES (?, ?, ?)");

        // Loop and scrape + insert
        System.out.println("Scraping products...");
        for (WebElement product : products) {
            try {
                String title = product.findElement(By.cssSelector("h2 a")).getText();
                String priceStr = product.findElement(By.cssSelector(".a-price-whole")).getText();
                String imageUrl = product.findElement(By.cssSelector("img")).getAttribute("src");

                double price = Double.parseDouble(priceStr.replace(",", ""));

                pstmt.setString(1, title);
                pstmt.setDouble(2, price);
                pstmt.setString(3, imageUrl);
                pstmt.executeUpdate();
            } catch (Exception e) {
                // Skip any malformed product
                System.out.println("Skipping a product due to missing data: " + e.getMessage());
            }
        }

        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
        System.out.println("Data saved to database successfully.");
        ResultSet rs = stmt.executeQuery("SELECT * FROM products ORDER BY price ASC");
        driver.quit();
        //Sort and Display
        System.out.println("Products sorted by price:");
        while(rs.next()) {
            System.out.println(rs.getString("name") + " - ₹" + rs.getDouble("price"));
            System.out.println("Image: " + rs.getString("imageUrl"));
        }*/
    }
}
