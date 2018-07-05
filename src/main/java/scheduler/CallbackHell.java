package scheduler;

import java.io.IOException;

import Util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CallbackHell {

	private static final String URL = "https://api.github.com/zen";
	private final OkHttpClient client = new OkHttpClient();
	
	public static void main(String[] args) {
		CallbackHell cbh = new CallbackHell();
		cbh.run();
	}
	
	private Callback callback = new Callback() {

		@Override
		public void onFailure(Call call, IOException e) {
			e.printStackTrace();
			
		}

		@Override
		public void onResponse(Call call, Response response) throws IOException {
			Log.i(response.body().string());
		}
		
	};
	
	public void run() {
		Request request = new Request.Builder()
				.url(URL)
				.build();
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {
				e.printStackTrace();
				
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				Log.i(response.body().string());
				
				Request request = new Request.Builder()
						.url(URL)
						.build();
				client.newCall(request).enqueue(callback);
			}
			
		});
	}
	
}
