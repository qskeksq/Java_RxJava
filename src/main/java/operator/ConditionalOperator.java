package operator;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Util.CommonUtil;
import Util.Log;
import Util.Shape;
import io.reactivex.Observable;
import io.reactivex.Single;

public class ConditionalOperator {
	
	public static void main(String[] args) {
		ConditionalOperator demo = new ConditionalOperator();
		// demo.amb();
		// demo.takeUntil();
		// demo.skipUntil();
		demo.all();
	}
	
	public void amb() {
		String[] data1 = {"1", "3", "5"};
		String[] data2 = {"2-R", "4-R"};
		
		List<Observable<String>> sources = Arrays.asList(
				Observable.fromArray(data1)
				.doOnComplete(()-> Log.d("Observable #1 : onComplete()")),
				Observable.fromArray(data2)
				.delay(100L, TimeUnit.MILLISECONDS)
				.doOnComplete(()-> Log.d("Observable #2 : onComplete()"))
		);
		Observable.amb(sources)
			.doOnComplete(()-> Log.d("Observable #1 : onComplete()"))
			.subscribe(Log::i);
		CommonUtil.sleep(1000);
	}
	
	public void takeUntil() {
		CommonUtil.exampleStart();
		String[] data = {"1", "2", "3", "4", "5", "6"};
		
		Observable<String> source = Observable.fromArray(data)
				.zipWith(Observable.interval(100L, TimeUnit.MILLISECONDS), (val, notUsed) -> val)
				// 500ms 이후 값을 1회 발
				.takeUntil(Observable.timer(500, TimeUnit.MILLISECONDS));
		source.subscribe(Log::it);
		CommonUtil.sleep(1000);
	}
	
	public void skipUntil() {
		CommonUtil.exampleStart();
		String[] data = {"1", "2", "3", "4", "5", "6"};
		
		Observable<String> source = Observable.fromArray(data)
				.zipWith(Observable.interval(100L, TimeUnit.MILLISECONDS), (val, notUsed) -> val)
				.skipUntil(Observable.timer(500L, TimeUnit.MILLISECONDS));
		source.subscribe(Log::it);
		CommonUtil.sleep(1000);
	}
	
	public void all() {
		String[] data = {"1", "2", "3", "4"};
		Single<Boolean> source = Observable.fromArray(data)
				.map(Shape::getShape)
				.all(Shape.BALL::equals);
				// .all(val -> Shape.BALL.equals(Shape.getShape(val)));
		source.subscribe(Log::i);
	}

}
