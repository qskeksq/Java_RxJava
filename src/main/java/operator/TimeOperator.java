package operator;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import Util.CommonUtil;
import Util.Log;
import io.reactivex.Observable;
import io.reactivex.schedulers.Timed;

public class TimeOperator {
	
	Iterator<String> colors = Arrays.asList("1", "2", "3", "4", "5").iterator();

	public static void main(String[] args) {
		TimeOperator demo = new TimeOperator();
		// demo.interval();
		// demo.timer();
		// demo.defer();
		// demo.delay();
		demo.timeInterval();
	}
	
	public void interval() {
		CommonUtil.exampleStart();
		Observable<Long> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
				// 처음 100 millisecond를 쉬고 시작한
				// data로 0부터 100 millisecond 초 단위로 1씩 증가한 값이 넘어온
				.map(data -> (data+1)*100)
				// 최초 5
				.take(5);
		source.subscribe(Log::it);
		CommonUtil.sleep(1000);
	}
	
	public void timer() {
		CommonUtil.exampleStart();
		Observable<String> source = Observable.timer(500L, TimeUnit.MILLISECONDS)
				// 0L이 발행되긴 하지만 실제 map()에 사용되지는 않
				.map(notUsed -> 
					new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		source.subscribe(Log::it);
		CommonUtil.sleep(1000);
	}
	
	public void intervalRange() {
		Observable<Long> source = Observable.intervalRange(1, 5, 100L, 100L, TimeUnit.MILLISECONDS);
		source.subscribe(Log::i);
		CommonUtil.sleep(1000);
	}
	
	public void defer() {
		Callable<Observable<String>> supplier = () -> getObservable();
		Observable<String> source = Observable.defer(supplier);
		source.subscribe(val -> Log.i("Subscriber #1 : "+val)); 
		source.subscribe(val -> Log.i("Subscriber #2 : "+val)); 
		source.subscribe(val -> Log.i("Subscriber #3 : "+val));
	}
	
	private Observable<String> getObservable() {
		if (colors.hasNext()) {
			String color = colors.next();
			return Observable.just(
						color+"-B",
						color+"-R",
						color+"-P");
		}
		return Observable.empty();
	}
	
	public void delay() {
		CommonUtil.exampleStart();
		String[] data = {"1", "7", "2", "3", "4"};
		Observable<String> source = Observable.fromArray(data)
				.delay(100L, TimeUnit.MILLISECONDS);
		source.subscribe(Log::it);
		CommonUtil.sleep(1000);
	}
	
	public void timeInterval() {
		String[] data = {"1", "3", "7"};
		CommonUtil.exampleStart();
		Observable<Timed<String>> source = Observable.fromArray(data)
				.delay(item -> {
					CommonUtil.doSomething();
					return Observable.just(data);
				})
				.timeInterval();
		source.subscribe(Log::it);
		CommonUtil.sleep(1000);
	}
	

}
