import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ScreenshotTool {

    // Compare two images pixel by pixel
    public static boolean imagesAreEqual(File image1, File image2) throws IOException {

        BufferedImage img1 = ImageIO.read(image1);
        BufferedImage img2 = ImageIO.read(image2);

        if (img1.getWidth() != img2.getWidth() ||
                img1.getHeight() != img2.getHeight()) {
            return false;
        }

        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {

                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Website URL: ");
        String url = scanner.nextLine();
        if (!(url.startsWith("http://") || url.startsWith("https://"))) {
    System.out.println("\nInvalid URL!");
    System.out.println("Please enter a valid URL starting with http:// or https://");
    scanner.close();
    return;
}

        System.out.print("Enter Capture Interval (seconds): ");
int interval = scanner.nextInt();

if (interval <= 0) {
    System.out.println("\nInvalid Input!");
    System.out.println("Capture interval must be greater than 0 seconds.");
    scanner.close();
    return;
}

System.out.print("Enter Number of Screenshots: ");
int totalScreenshots = scanner.nextInt();

if (totalScreenshots <= 0) {
    System.out.println("\nInvalid Input!");
    System.out.println("Number of screenshots must be greater than 0.");
    scanner.close();
    return;
}

        // Setup Chrome
        WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();

        driver.get(url);

        // Create a unique folder for this run
        String runTime = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());

        File folder = new File("screenshots/Run_" + runTime);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        System.out.println("\n======================================");
        System.out.println("Recording Started...");
        System.out.println("Screenshots Folder : " + folder.getAbsolutePath());
        System.out.println("======================================\n");

        File previousImage = null;
        int savedCount = 0;

        for (int i = 1; i <= totalScreenshots; i++) {

            Thread.sleep(interval * 1000);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());

            // Temporary Screenshot
            File currentImage = new File(folder, "temp.png");

            File src = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            FileUtils.copyFile(src, currentImage);

            // First Screenshot
            if (previousImage == null) {

                savedCount++;

                File finalImage = new File(
                        folder,
                        "change_" + savedCount + "_" + timestamp + ".png");

                FileUtils.copyFile(currentImage, finalImage);

                previousImage = finalImage;

                currentImage.delete();

                System.out.println("[" + i + "/" + totalScreenshots + "] First Screenshot Saved.");

            } else {

                if (imagesAreEqual(previousImage, currentImage)) {

                    System.out.println("[" + i + "/" + totalScreenshots + "] No Visual Change.");

                } else {

                    savedCount++;

                    File finalImage = new File(
                            folder,
                            "change_" + savedCount + "_" + timestamp + ".png");

                    FileUtils.copyFile(currentImage, finalImage);

                    previousImage = finalImage;

                    System.out.println("[" + i + "/" + totalScreenshots + "] Visual Change Detected. Screenshot Saved.");
                }

                // Delete temporary screenshot
                if (currentImage.exists()) {
                    currentImage.delete();
                }
            }
        }

        driver.quit();
        scanner.close();

        System.out.println("\n======================================");
        System.out.println("Recording Finished");
        System.out.println("Total Attempts       : " + totalScreenshots);
        System.out.println("Screenshots Saved    : " + savedCount);
        System.out.println("Saved Location       : " + folder.getAbsolutePath());
        System.out.println("======================================");
    }
}