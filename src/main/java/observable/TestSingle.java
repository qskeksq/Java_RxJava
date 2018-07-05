package observable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class TestSingle {

	public static void main(String[] args) {
		TestSingle demo = new TestSingle();
		demo.just();
		demo.fromObservable();
		demo.justSingle();
		demo.first();
		demo.emptySingle();
		demo.take();
	}
	
	public void just() {
		Single<String> source = Single.just("Hello Single");
		source.subscribe(System.out::println);
	}
	
	public void fromObservable() {
		Observable<String> source = Observable.just("Hello Single");
		Single
			.fromObservable(source)
			.subscribe(System.out::println);
	}
	
	public void justSingle() {
		Observable
			.just("Hello Single")
			.single("default item")
			.subscribe(System.out::println);
	}
	
	public void first() {
		String[] colors = {"Red", "Blue", "Gold"};
		Observable
			.fromArray(colors)
			.first("default value")
			.subscribe(System.out::println);
	}
	
	public void emptySingle() {
		Observable
			.empty()
			.single("default item")
			.subscribe(System.out::println);
	}
	
	public void take() {
		Observable
			.just(new Order("ORE-1"), new Order("ORE-2"))
			.take(1)
			.single(new Order("default order"))
			.subscribe(System.out::println);
		
	}

}
