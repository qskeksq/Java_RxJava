package operator;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Predicate;

public class FilterOperator {
	
	private Integer[] numbers = {100, 200, 300, 400, 500};
	private Single<Integer> single;
	private Observable<Integer> source;

	public static void main(String[] args) {
		FilterOperator demo = new FilterOperator();
		// demo.filterOne();
		// demo.filterTwo();
		demo.first();
		demo.last();
		demo.takeN();
		demo.takeLast();
		demo.skipN();
		demo.skipLast();
	}
	
	public void filterOne() {
		String[] objs = {"1 CIRCLE", "2 DIAMOND", "3 TRIANGLE",
				"4 DIAMOND", "5 CIRCLE", "6 HEXAGON"};
		Observable<String> source = Observable.fromArray(objs)
				.filter(obj -> obj.endsWith("CIRCLE"));
		source.subscribe(System.out::println);
	}
	
	public void filterTwo() {
		Predicate<Integer> remaining = num -> num % 2 == 0;
		
		Integer[] data = {100, 34, 27, 99, 50};
		Observable<Integer> source = Observable.fromArray(data)
				.filter(remaining);
		source.subscribe(System.out::println);
	}
	
	public void first() {
		single = Observable.fromArray(numbers).first(-1);
		single.subscribe(data -> System.out.println("first() value = "+data));
	}

	public void last() {
		single = Observable.fromArray(numbers).last(999);
		single.subscribe(data -> System.out.println("last() value = "+data));
	}
	
	public void takeN() {
		source = Observable.fromArray(numbers).take(3);
		source.subscribe(data -> System.out.println("take(3) value = "+data));
	}
	
	public void takeLast() {
		source = Observable.fromArray(numbers).takeLast(3);
		source.subscribe(data -> System.out.println("takeLast(3) value = "+data));
	}
	
	public void skipN() {
		source = Observable.fromArray(numbers).skip(2);
		source.subscribe(data -> System.out.println("skip(2) value = "+data));
	}
	
	public void skipLast() {
		source = Observable.fromArray(numbers).skipLast(2);
		source.subscribe(data -> System.out.println("skipLast(2) value = "+data));
	}
	
}
