package observable;

import io.reactivex.Observable;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

public class TestSubject {


	public static void main(String[] args) {
		TestSubject demo = new TestSubject();
		// demo.asyncSubject();
		// demo.asyncSubjectSubscriber();
		// demo.behaviorSubject();
		// demo.publishSubject();
		demo.replaySubject();
	}
	
	/**
	 * 발행자 AsyncSubject
	 */
	public void asyncSubject() {
		AsyncSubject<String> subject = AsyncSubject.create();
		// data를 어떻게 처리할지 정의한 것 - print 하겠
		subject.subscribe(data -> System.out.println("Subscriber #1 => "+data));
		// 안 넘어옴
		subject.onNext("1");
		// 안 넘어옴
		subject.onNext("3");
		subject.subscribe(data -> System.out.println("Subscriber #2 => "+data));
		// data로 5 전달
		subject.onNext("5");
		subject.onComplete();
		// onComplete 이후로는 onNext 함수 무시
		subject.onNext("7");
		// onComplete 
		subject.subscribe(data -> System.out.println("Subscriber #3 => "+data));
		subject.subscribe(data -> System.out.println("Subscriber #4 => "+data));

	}
	
	/**
	 * 구독자 AsyncSubject
	 */
	public void asyncSubjectSubscriber() {
		Float[] temperature = {10.1f, 13.4f, 12.5f};
		Observable<Float> source = Observable.fromArray(temperature);
		
		AsyncSubject<Float> subject = AsyncSubject.create();
		subject.subscribe(data -> System.out.println("Subscriber #1 => " + data ));
		
		// AsyncSubject가 Observer(Observable과 헷갈리지 말 것)를 구현했기 때문에 가 
		source.subscribe(subject);
	}
	
	public void behaviorSubject() {
		BehaviorSubject<String> subject = BehaviorSubject.createDefault("6");
		subject.subscribe(data -> System.out.println("Subscriber #1 => "+data));
		subject.onNext("1");
		subject.onNext("3");
		subject.subscribe(data -> System.out.println("Subscriber #2 => "+data));
		subject.onNext("5");
		subject.onComplete();
	}
	
	public void publishSubject() {
		PublishSubject<String> subject = PublishSubject.create();
		subject.subscribe(data -> System.out.println("Subscriber #1 => "+data));
		subject.onNext("1");
		subject.onNext("3");
		subject.subscribe(data -> System.out.println("Subscriber #2 => "+data));
		subject.onNext("5");
		subject.onComplete();
	}
	
	public void replaySubject() {
		ReplaySubject<String> subject = ReplaySubject.create();
		subject.subscribe(data -> System.out.println("Subscriber #1 => "+data));
		subject.onNext("1");
		subject.onNext("3");
		subject.subscribe(data -> System.out.println("Subscriber #2 => "+data));
		subject.onNext("5");
		subject.onComplete();
	}

}
