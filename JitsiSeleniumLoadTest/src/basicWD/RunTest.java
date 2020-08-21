package basicWD;

import java.net.MalformedURLException;

//import org.openqa.selenium.TimeoutException;

public class RunTest {
	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		try {
			LoadSession obj;
			//if(args.length == 7)
			//	obj = new LoadSession(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
			//else
				obj = new LoadSession();
			obj.runLoad();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}