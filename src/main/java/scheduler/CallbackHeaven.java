package scheduler;

import Util.CommonUtil;
import Util.Log;
import Util.OkHttpHelper;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class CallbackHeaven {

	private static final String URL = "https://api.github.com/zen";
	
	public static void main(String[] args) {
		CallbackHeaven cbh = new CallbackHeaven();
		cbh.concatRequest();
		// cbh.zipRequest();
	}
	
	public void concatRequest() {
		CommonUtil.exampleStart();
		Observable<String> source = Observable
				.just(URL)
				.subscribeOn(Schedulers.io())
				.map(OkHttpHelper::get)
				.concatWith(Observable.just(URL).map(OkHttpHelper::get));
		source.subscribe(Log::it);
		CommonUtil.sleep(5000);
	}
	
	public void zipRequest() {
		CommonUtil.exampleStart();
		Observable<String> source1 = Observable.just(URL)
				.subscribeOn(Schedulers.io())
				.map(OkHttpHelper::get);
		Observable<String> source2 = Observable.just(URL)
				.subscribeOn(Schedulers.io())
				.map(OkHttpHelper::get);
		
		Observable<String> source = Observable.zip(
				source1,
				source2,
				(a, b) -> "\n" + a + "\n" + b
		);
		source.subscribe(Log::it);
		CommonUtil.sleep(5000);
	}

}
