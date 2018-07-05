package operator;

import java.util.concurrent.TimeUnit;

import Util.CommonUtil;
import Util.Log;
import Util.Shape;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

public class TransformingOperator {

	public static void main(String[] args) {
		TransformingOperator demo = new TransformingOperator();
		// demo.mapOne();
		// demo.mapTwo();
		// demo.mapThree();
		// demo.flatMapOne();
		// demo.concatMap();
		// demo.concatMapCompare();
		// demo.switchMap();
		// demo.switchMapCompare();
		// demo.groupByOne();
		// demo.groupByTwo();
		demo.scan();
	}
	
	public void mapOne() {
		String[] balls = {"1", "2", "3", "4", "5"};
		Observable<String> source = Observable.fromArray(balls)
				.map(ball -> ball + "D");
		source.subscribe(System.out::println);
	}
	
	public void mapTwo() {
		Function<String, String> getDiamond = ball -> ball + "D";
		String[] balls = {"1", "2", "3", "4", "5"};
		Observable<String> source = Observable.fromArray(balls)
				.map(getDiamond);
		source.subscribe(System.out::println);
	}
	
	public void mapThree() {
		Function<String, Integer> ballToIndex = ball -> {
			switch (ball) {
			case "RED":
				return 1;
			case "YELLOW":
				return 2;
			case "GREEN":
				return 3;
			case "BLUE":
				return 4;
			default:
				return -1;
			}
		};
		
		String[] balls = {"RED", "YELLOW", "GREEN", "BLUE"};
		Observable<Integer> source = Observable.fromArray(balls)
				.map(ballToIndex);
		source.subscribe(System.out::println);
	}
	
	public void flatMapOne() {
		// 함수를 별도로 정의 
		Function<String, Observable<String>> getDoubleDiamonds = 
				ball -> Observable.just(ball + "D", ball + "D");
				
		String[] balls = {"1", "2", "3"};
		Observable<String> source = Observable.fromArray(balls)
				.flatMap(getDoubleDiamonds);
		source.subscribe(System.out::println);
	}
	
	public void flatMapTwo() {
		String[] balls = {"1", "2", "3"};
		// 배열을 Observable로 1차 데이터 흐름 변환
		// flatMap Observable을 통해 2차 데이터 흐름 변
		Observable<String> source = Observable.fromArray(balls)
				.flatMap(ball -> Observable.just(ball + "D", ball + "D"));
		source.subscribe(System.out::println);
	}
	
	public void concatMap() {
		CommonUtil.exampleStart();
		
		String[] balls = {"1", "3", "5"};
		Observable<String> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
				// 데이터 흐름1 Integer로 변
				.map(Long::intValue)
				// 데이터 흐름2 배열에서 꺼내
				.map(indx -> balls[indx])
				// 배열이 3개이기 때문에 take()를쓰지 않으면 ArrayIndexOutOfBoundsException 에러 생
				// 데이터 흐름3 
				.take(balls.length)
				//.doOnNext(Log::dt)
				.concatMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
						// notUsed로 값이 0, 1, 2... 가 200L millisecond 마다 리턴된
						.map(notUsed -> ball + " <>")
						// 리턴되는 값은 0, 1이고 값은 리턴값과 상관 없이 ball+<> 이기 때문에 마치 중복처리처럼 보이는 것이
						.take(2)
				);
		source.subscribe(Log::it);
		// 계산 스레드에서 진행되는데 메인스레드에서 아무 일 하지 않으면 종료되기 때문에 
		// 메인 스레드에서 interval 실행시켜놓고 여기는 따로 스레드로 돌아가고 있으니까 메인 스레드가
		// 2-3초 멈춘 상태로 살아 있도록 한다.
		CommonUtil.sleep(2000);
	}
	
	public void concatMapCompare() {
		CommonUtil.exampleStart();
		
		String[] balls = {"1", "3", "5"};
		Observable<String> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
				.map(Long::intValue)
				.map(indx -> balls[indx])
				.take(balls.length)
				// .doOnNext(Log::dt)
				.flatMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
						.map(notUsed -> ball + " <>")
						.take(2)
				);
		source.subscribe(Log::it);
		CommonUtil.sleep(2000);
	}
	
	public void switchMap() {
		CommonUtil.exampleStart();
		
		String[] balls = {"1", "3", "5"};
		Observable<String> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
				.map(Long::intValue)
				.map(idx -> balls[idx])
				.take(balls.length)
				.switchMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
						.map(notUsed -> ball + "<>")
						.take(2)
				);
		source.subscribe(Log::it);
		CommonUtil.sleep(1000);			
	}
 
	public void switchMapCompare() {
		CommonUtil.exampleStart();
		
		String[] balls = {"1", "3", "5"};
		Observable<String> source = Observable.interval(100L, TimeUnit.MILLISECONDS)
				.map(Long::intValue)
				.map(idx -> balls[idx])
				.take(balls.length)
				.doOnNext(Log::dt)
				.switchMap(ball -> Observable.interval(200L, TimeUnit.MILLISECONDS)
						.map(notUsed -> ball + "<>")
						.take(2)
				);
		source.subscribe(Log::it);
		CommonUtil.sleep(1000);			
	}
	
	public void groupByOne() {
		String[] objs = {"6", "4", "2-T", "2", "6-T", "4-T"};
		Observable<GroupedObservable<String, String>> source = 
				// CommonUtil::getShape의 기준에 의해 그룹이 생성되고
				// GroupedObservable 리턴 타입의 Observable 데이터 흐름이 생성된
				Observable.fromArray(objs).groupBy(CommonUtil::getShape);
		source.subscribe(groupedObservable -> {
			groupedObservable.subscribe(value -> {
				System.out.println("GROUP:"+groupedObservable.getKey()+"\t Value:"+value);
			});
		});
	}
	
	public void groupByTwo() {
		String[] objs = {"6", "4", "2-T", "2", "6-T", "4-T"};
		Observable<GroupedObservable<String, String>> source = 
				Observable.fromArray(objs).groupBy(CommonUtil::getShape);
		source.subscribe(groupedObservable -> {
			groupedObservable
				.filter(value -> groupedObservable.getKey().equals(Shape.BALL))
				.subscribe(value -> {
					System.out.println("GROUP:"+groupedObservable.getKey()+"\t Value:"+value);
				});
		});
	}

	public void scan() {
		String[] balls = {"1", "3", "5"};
		Observable<String> source = Observable.fromArray(balls)
				.scan((ball1, ball2) -> ball2 + "(" + ball1 + ")");
		source.subscribe(Log::i);
	}
	
}
