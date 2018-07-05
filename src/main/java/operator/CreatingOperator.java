package operator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import Util.CommonUtil;
import Util.Log;
import Util.OkHttpHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;
import observable.Order;

public class CreatingOperator {
	

	public static void main(String[] args) {
		CreatingOperator demo = new CreatingOperator();
		// demo.just();
		// demo.isDisposed();
		// demo.create();
		// demo.fromIterableOne();
		// demo.fromIterableTwo();
		// demo.fromIterableThree();
		// demo.fromFuture();
		// demo.fromCallable();
		// demo.fromPublisher();
		// demo.intervalOne();
		demo.intervalTwo();
		// demo.timer();
		// demo.range();
		// demo.intervalRange();
		// demo.defer();
		// demo.deferCompare();
		// demo.repeat();
		// demo.heartbeat();
	}

	public void just() {
		Observable
			.just(1,2,3,4,5,6)
			.subscribe(System.out::println);
	}
	
	public void isDisposed() {
		Observable<String> source = Observable.just("Green", "Red", "Yellow");
		
		Disposable d = source.subscribe(
				v -> System.out.println("onNext : value : " + v),
				e -> System.err.println("onError : err : " + e.getMessage()),
				() -> System.out.println("onComplete()")
		);
		
		System.out.println("isDisposed() : " + d.isDisposed());
	}
	
	public void create() {
		Observable<Integer> source = Observable.create(
				(ObservableEmitter<Integer> emitter) -> {
					emitter.onNext(100);
					emitter.onNext(200);
					emitter.onNext(300);
					emitter.onComplete();
				}		
		);
		source.subscribe(System.out::println);
	}
	
	public void fromArray() {
		Integer[] arr = {100, 200, 300};
		Observable<Integer> source = Observable.fromArray(arr);
		source.subscribe(System.out::println);
	}
	
	public Integer[] toIntegerArray(int[] intArray) {
		// boxed 메서드는 int[]배열 각가의 요소를 Integer로 변환해 Integer[] 배열의 스트림으로 변환
		// toArray() 메서드로 스트림을 Integer[] 배열로 전
		return IntStream.of(intArray).boxed().toArray(Integer[]::new);
	}
	
	public void fromIterableOne() {
		List<String> names = new ArrayList<>();
		names.add("Jerry");
		names.add("William");
		names.add("Bob");
		Observable<String> source = Observable.fromIterable(names);
		source.subscribe(System.out::println);
	}
	
	public void fromIterableTwo() {
		Set<String> names = new HashSet<>();
		names.add("Seoul");
		names.add("London");
		names.add("Paris");
		Observable<String> source = Observable.fromIterable(names);
		source.subscribe(System.out::println);
	}
	
	public void fromIterableThree() {
		BlockingQueue<Order> names = new ArrayBlockingQueue<>(100);
		names.add(new Order("ORD-1"));
		names.add(new Order("ORD-2"));
		names.add(new Order("ORD-3"));
		Observable<Order> source = Observable.fromIterable(names);
		source.subscribe(order -> System.out.println(order.getId()));
	}
	
	public void fromCallable() {
		Observable<String> source = Observable.fromCallable(callable);
		source.subscribe(System.out::println);
	}
	
	Callable<String> callable = () -> {
		Thread.sleep(2000);
		return "Hello Callable";
	};

	public void fromFuture() {
		Observable<String> source = Observable.fromFuture(future);
		source.subscribe(System.out::println);
	}
	
	Future<String> future 
			= Executors.newSingleThreadExecutor().submit(() -> {
		Thread.sleep(1000);
		return "Hello Future";
	});
	
	public void fromPublisher() {
		Observable<String> source = Observable.fromPublisher(publisher);
		source.subscribe(System.out::println);
	}
	
	Publisher<String> publisher = (Subscriber<? super String> s) -> {
		s.onNext("Hello Observable.fromPublisher");
		s.onComplete();
	};
	
	public void intervalOne() {
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
	
	public void intervalTwo() {
		CommonUtil.exampleStart();
		// 초기 지연시간을 100ms 에서 0ms로 줄어든다
		Observable<Long> source = Observable.interval(0L, 100L, TimeUnit.MILLISECONDS)
				.map(val -> val + 100)
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
	
	public void range() {
		Observable<Integer> source = Observable.range(1, 10)
				.filter(number -> number % 2 == 0);
		source.subscribe(Log::i);
	}
	
	public void intervalRange() {
		Observable<Long> source = Observable.intervalRange(1, 5, 100L, 100L, TimeUnit.MILLISECONDS);
		source.subscribe(Log::i);
		CommonUtil.sleep(1000);
	}
	
	Iterator<String> colors = Arrays.asList("1", "2", "3", "4", "5").iterator();

	public void defer() {
		// Callable은 리턴 타입이 있는 Runnable, <T>는 리턴 타입
											// () -> return getObservable();에서 return 생략한 
		// () -> getObservable()은 Callable의 익명 객체로, Runnable이라고 생각하면 편하다
		// 그렇다는 것은 마치 subscribe 할 때 runnable을 실행하는 것과 비슷하다고 볼 수 있
		Callable<Observable<String>> supplier = () -> getObservable();
		// defer는 이후 just로 생성할 Observable을 예약하겠다는 것니까 결국은 Observable.just 인거임
		Observable<String> source = Observable.defer(supplier);
		
		// 어렵게 생각할 것 없이 결국 source로 사용되는 값은 getObservable()에서 리턴받은
		// Observable.just()이다. 즉, defer는 subscribe()를 호출하는 그 시점에
		// 만드려고 예약해 둔 Observable 객체를 만들어서 source로 사용하도록 리턴해 주는 것이다
		// 그렇다면 defer의 인자로 들어가는 Callable이 그러한 '지연'을 담당하는 것을 알 수 있다
		source.subscribe(val -> Log.i("Subscriber #1 : "+val)); 
		source.subscribe(val -> Log.i("Subscriber #2 : "+val)); 
		source.subscribe(val -> Log.i("Subscriber #3 : "+val));
	}
	
	public void deferCompare() {
		Observable<String> source = getObservable();
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
	
	public void repeat() {
		String[] balls = {"1", "3", "5"};
		Observable<String> source = Observable
				.fromArray(balls)
				// 인자를 입력하지 않을 경우 영원히 반
				.repeat(3);
		source
			// onComplete가 호출됬을 때 출
			.doOnComplete(() -> System.out.println("onComplete"))
			.subscribe(Log::i);
	}
	
	public void heartbeat() {
		CommonUtil.exampleStart();
		String serverUrl = "https://api.github.com/zen";
		
		Observable.timer(2, TimeUnit.SECONDS)
		.map(val -> serverUrl)
		.map(OkHttpHelper::get)
		.repeat()
		.subscribe(res -> Log.it("Ping Result : "+res));
		CommonUtil.sleep(1000);
	}
	
}
