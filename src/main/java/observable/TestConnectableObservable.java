package observable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

public class TestConnectableObservable {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void connectableObservable() {
		String[] dt = {"1", "3", "5"};
		Observable<String> balls = Observable.interval(100L, TimeUnit.MILLISECONDS)
				.map(Long::intValue)
				.map(i -> dt[i])
				.take(dt.length);
		ConnectableObservable<String> source = balls.publish();
		source.subscribe(data -> System.out.print("Subscriber #1 => "+data));
		source.subscribe(data -> System.out.print("Subscriber #2 => "+data));
		source.connect();
		
		try {
			Thread.sleep(250);
			source.subscribe(data -> System.out.print("Subscriber #3 => "+data));
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
