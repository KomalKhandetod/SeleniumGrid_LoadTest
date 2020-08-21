package basicWD;

import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
//import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class LoadSession {

	private String _hubURL;
	//private String _firefoxhubURL;
	private String _serverAddress;
	private int _participantsPerSession;
	private int _participantsWihVideo;
	private int _waitTime;
	private int _sessionCount;

	public LoadSession() throws Exception {
		Properties prop = new Properties();
		FileInputStream ip = new FileInputStream("C:\\config.properties");
		prop.load(ip);
		this._hubURL = prop.getProperty("chromehubURL");
		this._serverAddress = prop.getProperty("serverAddress");
		this._participantsPerSession = Integer.parseInt(prop.getProperty("participantCount"));
		this._participantsWihVideo = Integer.parseInt(prop.getProperty("videoCount"));
		this._waitTime = Integer.parseInt(prop.getProperty("delaySeconds"));
		this._sessionCount = Integer.parseInt(prop.getProperty("sessionCount"));
		System.out.println();
		System.out.println("hub - " + this._hubURL);
		System.out.println("sessions - " + this._sessionCount);
		System.out.println("server - " + this._serverAddress);
		System.out.println("participant - " + this._participantsPerSession);
		System.out.println("video - " + this._participantsWihVideo);
		System.out.println("wait time - " + this._waitTime);
		System.out.println();
	}
	
	private void createSessionInstructor(String instructorURL) throws MalformedURLException, InterruptedException {

		System.out.println("Conference ID:" + instructorURL);

		// Define Desired capabilities
		DesiredCapabilities cap = new DesiredCapabilities();

		cap.setBrowserName("chrome");
		cap.setPlatform(Platform.WINDOWS);

		// Chrome Options definitions
		ChromeOptions option = new ChromeOptions();
		option.merge(cap);

		// use fake media stream
		option.addArguments("use-fake-device-for-media-stream");
		option.addArguments("use-fake-ui-for-media-stream");
		//option.addArguments("--use-fake-device-for-media-stream");
		//option.addArguments("--use-fake-ui-for-media-stream");
		//option.addArguments("--use-file-for-fake-video-capture=C:\\akiyo.y4m");

		// Set Hub URL
		// String _hubURL = "http://52.224.10.71:4444/wd/hub";
		WebDriver driver = new RemoteWebDriver(new URL(this._hubURL), option);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();

		driver.get(this._serverAddress + instructorURL);
	}

	private void createSessionStudent(String studentURL) throws MalformedURLException {

		System.out.println("Conference ID:" + studentURL);

		// Define Desired capabilities
		DesiredCapabilities cap = new DesiredCapabilities();

		cap.setBrowserName("chrome");
		cap.setPlatform(Platform.WINDOWS);

		// Chrome Options definitions
		ChromeOptions option = new ChromeOptions();
		option.merge(cap);

		// use fake media stream
		option.addArguments("use-fake-device-for-media-stream");
		option.addArguments("use-fake-ui-for-media-stream");

		// Set Hub URL
		// String hubURL = "http://10.0.1.4:4444/wd/hub";
		WebDriver driver1 = new RemoteWebDriver(new URL(this._hubURL), option);
		driver1.manage().timeouts().pageLoadTimeout(this._waitTime, TimeUnit.SECONDS);
		driver1.manage().timeouts().implicitlyWait(this._waitTime, TimeUnit.SECONDS);
		driver1.manage().window().maximize();
		driver1.manage().deleteAllCookies();

		driver1.get(this._serverAddress + studentURL);
	}

	public void runLoad() throws Exception {
		for(int s = 1; s<= this._sessionCount; s++) {
			long sessionId = Math.round(Math.random() * 1000000);
			int audioOnly = this._participantsPerSession - this._participantsWihVideo;

			int audioVideo = this._participantsWihVideo;
			System.out.println("========================================");

			//int count = 0;
			String instructorURL;
			String studentURL;

			for (int k = 1; k <= audioVideo; k++) {
				try {

					instructorURL = "elevate" + sessionId + "#config.channelLastN=2";

					this.createSessionInstructor(instructorURL);
					//count++;
					System.out.println("Audio/Video Participant no: " + k + " - Chrome");
					System.out.println("----------------------");
				} catch (TimeoutException t) {
					System.out.println("Time out exception occurred");
					continue;
				}
			}
		
			for (int i = 1; i <= audioOnly; i++) {
				try {
					studentURL = "elevate" + sessionId + "#config.channelLastN=1&config.startWithVideoMuted=true";
					this.createSessionStudent(studentURL);
					
					System.out.println("Student Participant no: " + i + " - Chrome");
					System.out.println("----------------------");
				} catch (TimeoutException t) {
					System.out.println("Time out exception occurred");
					continue;
				}

			}
			System.out.println("========================================");
		}
	}
}