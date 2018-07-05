package observable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;

/**
 * 
 */
public class TestObservable {

	public static void main(String[] args) {
		TestObservable demo = new TestObservable();
		 demo.just();
		 // demo.isDisposed();
		 demo.create();
		 demo.fromIterableOne();
		 demo.fromIterableTwo();
		 demo.fromIterableThree();
		 demo.fromFuture();
		 demo.fromCallable();
		 demo.fromPublisher();
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

}
