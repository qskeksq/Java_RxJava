package operator;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import Util.CommonUtil;
import Util.Log;
import Util.Shape;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;

public class CombiningOperator {

	public static void main(String[] args) {
		CombiningOperator demo = new CombiningOperator();
		// demo.reduceOne();
		// demo.reduceTwo();
		// demo.zip();
		// demo.zipNumber();
		// demo.zipInterval();
		// ElectricBill eb = demo.new ElectricBill();
		// eb.subscribe();
		// demo.zipWith();
		// demo.combineLatest();
		// demo.merge();
		demo.concat();
	}
	
	public void reduceOne() {
		BiFunction<String, String, String> mergeballs = 
				(ball1, ball2) -> ball2 + "("+ball1+")";
		
		String[] balls = {"1", "2", "3"};
		Maybe<String> source = Observable.fromArray(balls)
				.reduce(mergeballs);
		source.subscribe(System.out::println);
	}
	
	public void reduceTwo() {
		String[] balls = {"1", "2", "3"};
		Maybe<String> source = Observable.fromArray(balls)
				.reduce((ball1, ball2) -> ball2 + "("+ball1+")");
		source.subscribe(System.out::println);
	}
	
	public void zip() {
		String[] shapes = {"BALL", "PENTAGON", "STAR"};
		String[] coloredTriangles = {"2-T", "6-T", "4-T"};
		
		Observable<String> source = Observable.zip(
				Observable.fromArray(shapes).map(Shape::getSuffix),
				Observable.fromArray(coloredTriangles).map(Shape::getColor),
				(suffix, color) -> color + suffix
		);
		source.subscribe(Log::i);
	}
	
	public void zipNumber() {
		Observable<Integer> source = Observable.zip(
				Observable.just(100, 200, 300),
				Observable.just(10, 20, 30),
				// 마지막 Observable이 2개만 발행했기 때문에 2개만 리턴되고 나머지는 기다린
				Observable.just(1, 2),
				(a, b, c) -> a+b+c
		);
		source.subscribe(Log::i);
	}
	
	public void zipInterval() {
		Observable<String> source = Observable.zip(
				Observable.just("RED", "GREEN", "BLUE"),
				Observable.interval(200L, TimeUnit.MILLISECONDS),
				(value, i) -> value
		);
		CommonUtil.exampleStart();
		source.subscribe(Log::it);
		CommonUtil.sleep(1000);
	}
	
	public class ElectricBill {
		
		String[] data = {"100", "300"};
		private int index = 0;
		
		Observable<Integer> basePrice = Observable.fromArray(data)
				.map(Integer::parseInt)
				.map(val -> {
					if (val <= 200) return 910;
					if (val <= 400) return 1600;
					return 7300;
				});
		
		Observable<Integer> usagePrice = Observable.fromArray(data)
				.map(Integer::parseInt)
				.map(val -> {
					double series1 = Math.min(200, val) * 93.3;
					double series2 = Math.min(200, Math.max(val-200, 0)) * 187.9;
					double series3 = Math.min(0, Math.max(val-400, 0)) * 280.65;
					return (int) (series1+series2+series3);
 				});
		
		Observable<Integer> source = Observable.zip(
				basePrice,
				usagePrice,
				(v1, v2) -> v1+v2
		);
		
		public void subscribe() {
			source
				.map(val -> new DecimalFormat("#,###").format(val))
				.subscribe(val -> {
					StringBuilder sb = new StringBuilder();
					sb.append("Usage: "+data[index] + "kWh =>");
					sb.append("Price: "+val+"원");
					Log.i(sb.toString());
				});
		}
	}
	
	public void zipWith() {
		Observable<Integer> source = Observable.zip(
				Observable.just(100, 200, 300),
				Observable.just(10, 20, 30),
				(a, b) -> a+b)
				// 앞 뒤 Observable을 연결해준다
				.zipWith(Observable.just(1, 2, 3), (ab, c) -> ab + c);
		source.subscribe(Log::i);
	}
	
	public void combineLatest() {
		String[] data1 = {"6", "7", "4", "2"};
		String[] data2 = {"DIAMOND", "STAR", "PENTAGON"};
		
		Observable<String> source = Observable.combineLatest(
				Observable.fromArray(data1)
					.zipWith(Observable.interval(100L, TimeUnit.MILLISECONDS), 
						(shape, notUsed) -> Shape.getColor(shape)),
				Observable.fromArray(data2)
											// 일부로 발행 시간을 다르게 
					.zipWith(Observable.interval(150L, 200L, TimeUnit.MILLISECONDS),
						(shape, notUsed) -> Shape.getSuffix(shape)),
				(v1, v2) -> v1 + v2
		);
		source.subscribe(Log::it);
		CommonUtil.sleep(1000);
	}
	
	public void merge() {
		String[] data1 = {"1", "3"};
		String[] data2 = {"1", "3", "5"};
		
		Observable<String> source1 = Observable.interval(0L, 100L, TimeUnit.MILLISECONDS)
				.map(Long::intValue)
				.map(idx -> "[source1] " + data1[idx])
				.take(data1.length);
		
		Observable<String> source2 = Observable.interval(50L, TimeUnit.MILLISECONDS)
				.map(Long::intValue)
				.map(idx -> "[source2] " + data2[idx])
				.take(data2.length);
		
		Observable<String> source = Observable.merge(source1, source2);
		source.subscribe(Log::i);
		CommonUtil.sleep(1000);
	}
	
	Action onCompleteAction = () -> Log.d("onComplete");
	
	public void concat() {
		String[] data1 = {"1", "3", "5"};
		String[] data2 = {"2", "4", "6"};
		
		Observable<String> source1 = Observable.fromArray(data1)
				.doOnComplete(onCompleteAction);
		Observable<String> source2 = Observable.interval(200L, TimeUnit.MILLISECONDS)
				.map(Long::intValue)
				.map(idx -> data2[idx])
				.take(data2.length)
				.doOnComplete(onCompleteAction);
		
		Observable<String> source = Observable.concat(source1, source2)
				.doOnComplete(onCompleteAction);
		source.subscribe(Log::i);
		CommonUtil.sleep(1000);
	}
	
	
	
}
