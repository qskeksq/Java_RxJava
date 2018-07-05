package excercise;
import io.reactivex.Observable;

public class FirstExample {

	public static void main(String[] args) {
		FirstExample demo = new FirstExample();
		demo.justOne();
	}
	
	public void justOne() {
		Observable
			.just("Hello", "RxJava2")
			.subscribe(System.out::println);
	}
}
