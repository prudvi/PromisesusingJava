package multiThreadingTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.riversun.promise.Action;
import org.riversun.promise.Func;
import org.riversun.promise.Promise;

public class asyncPromise {
	

	public static void main(String[] args) {
		promiseExecute();
	}
	
	private static void sendPOST() throws IOException {
		URL obj = new URL("");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		//con.setRequestProperty("User-Agent", USER_AGENT);

		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		 String POST_PARAMS = "userName=prudvi";
		os.write(POST_PARAMS.getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
		}
	}

	
	private static String sendGET(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		//con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		StringBuffer response = new StringBuffer();
		//System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
			
		} else {
			System.out.println("GET request not worked");
		}
		return response.toString();

	}

	private static void promiseExecute() {
    // Asynchronous operation 1
    Func function1 = new Func() {
		@Override
		public void run(Action action, Object arg1) throws Exception {
			//Promise.sleep(2000);
			System.out.println("func1 running");
			String obj = sendGET("http://localhost:3001/heroes");
			//action.resolve("func1-result");
			action.resolve(obj);
			
		}
	};
    		
    		

    // Asynchronous operation 2
    Func function2 = new Func() {
		@Override
		public void run(Action action, Object arg1) throws Exception {
			http://localhost:3000/about
            System.out.println("func2 running");
			String obj = sendGET("http://localhost:3000/about");
            action.resolve(obj);
			
		}
	};
    		

    // Asynchronous operation 3
    Func function3 = new Func() {
		@Override
		public void run(Action action, Object arg1) throws Exception {
			Promise.sleep(5000);
            System.out.println("func3 running");
            //String obj = sendGET("http://localhost:3001/powers");
			//action.resolve("func1-result");
			//action.resolve(obj);
            action.resolve("func3-result");
			
		}
	};
    
	String[] urlS = {"http://localhost:3001/heroes", "http://localhost:3000/about" , "http://localhost:3001/powers"};
	Func[] funcArray = new Func[urlS.length];
	for (int i=0; i<urlS.length; i++) {
		final Integer innerMi = new Integer(i);
		 Func f1 = new Func() {
				@Override
				public void run(Action action, Object arg1) throws Exception {
					
					System.out.println("func"+innerMi+" running");
					
					if (innerMi%4 == 0) {
						Promise.sleep(5000);
						action.resolve("func"+innerMi+"-result");
					} else {
						String obj = sendGET(urlS[innerMi]);
			            action.resolve(obj);
					}
				}
			};
			funcArray[i] = f1;
	}
    
    long start = System.nanoTime();
    // Operation to receive final result
    Func function4 = new Func() {
		@Override
		public void run(Action action, Object data) throws Exception {
    	
	        System.out.println("Received the final result.");
	        List<Object> resultList = (List<Object>) data;
	        for (int i = 0; i < resultList.size(); i++) {
	            Object result = resultList.get(i);
	            System.out.println("The result of async operation" + (i + 1) + " is " + result);
	        }
	        System.out.println("Total Time taken  " + (System.nanoTime() - start) / 1000000);
	
	        action.resolve();
		}
    };

    

//    Promise.all(function1, function2, function3)
//            .always(function4)
//            .start();// start asynchronous operations
//	}
	
	Promise.all(funcArray)
    .always(function4)
    .start();// start asynchronous operations
}
	
	
    
}
