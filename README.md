#Overview

A Java Selenium automation tool that captures website screenshots at user-defined intervals. The tool compares each new screenshot with the previously saved screenshot using pixel-by-pixel image comparison and saves a new screenshot whenever a visual difference is detected.

##Features
Automatic website screenshot capture
User-defined capture interval
User-defined number of screenshots
Pixel-by-pixel image comparison
Saves screenshots only when visual changes are detected
Separate folder created for each execution
Input validation


33Technologies
Java
Selenium WebDriver
Maven
WebDriverManager
Apache Commons IO
How to Run
Clone the repository.
Open the project in VS Code or IntelliJ IDEA.
Install Maven dependencies.
Run ScreenshotTool.java.
Enter the website URL, capture interval, and number of screenshots.