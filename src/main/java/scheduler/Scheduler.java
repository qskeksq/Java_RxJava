package scheduler;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import Util.CommonUtil;
import Util.Log;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class Scheduler {

	public static void main(String[] args) {
		Scheduler demo = new Scheduler();
		// demo.mainThread();
		// demo.newThread();
		// demo.computationThread();
		// demo.ioThread();
		// demo.trampolineThread();
		// demo.singleThread();
		demo.executorThread();
	}
	
	public void mainThread() {
		Observable.just("Hello", "RxJava2").subscribe(Log::i);
	}
	
	public void newThread() {
		String[] orgs = {"1", "3", "5"};
		Observable.fromArray(orgs)
			.doOnNext(data -> Log.v("Original data : "+data))
			.map(data -> "<<"+data+">>")
			.subscribeOn(Schedulers.newThread())
			.subscribe(Log::i);
		// 두 함수가 동시 실행되는 것을 막아준다. 없으면 두 함수가 동시에 실행되는데
		// 너무 빨라서 순서대로 진행되는 것처럼 보인다. 여러번 실행하면 다른 결과가 나
		// CommonUtil.sleep(500);
		Observable.fromArray(orgs)
			.doOnNext(data -> Log.v("Original data : "+data))
			.map(data -> "##"+data+"##")
			.subscribeOn(Schedulers.newThread())
			.subscribe(Log::i);
		// Computation 스레드에서 동작하기 때문에 메인 스레드에서 작업이 없어진다 
		// 메인스레드에서 작업이 없으면 바로 종료되기 때문에 기다려
		CommonUtil.sleep(500);
	}
	
	public void computationThread() {
		String[] orgs = {"1", "3", "5"};
		Observable<String> source = Observable.fromArray(orgs)
				.zipWith(Observable.interval(100L, TimeUnit.MILLISECONDS), (a, b) -> a);
		
		source.map(item -> "<<"+item+">>")
			// 원래 interval은 Computation 스레드에서 동작하기 때문에 굳이 필요 없
			.subscribeOn(Schedulers.computation())
			.subscribe(Log::i);
		
		source.map(item -> "##"+item+"##")
			.subscribeOn(Schedulers.computation())
			.subscribe(Log::i);
		CommonUtil.sleep(500);
	}
	
	public void ioThread() {
		String root = "/Users/mac/documents/workspace/teaming";
		File[] files = new File(root).listFiles();
		Observable<String> source = Observable.fromArray(files)
				.filter(f -> f.isDirectory())
				.map(f -> f.getAbsolutePath())
				.subscribeOn(Schedulers.io());
		source.subscribe(Log::i);
		CommonUtil.sleep(1000);
	}
	
	public void trampolineThread() {
		String[] orgs = {"1", "3", "5"};
		Observable<String> source = Observable.fromArray(orgs);
		source
			.subscribeOn(Schedulers.trampoline())
			.map(item -> "<<"+item+">>")
			.subscribe(Log::i);
		source
			.subscribeOn(Schedulers.trampoline())
			.map(item -> "##"+item+"##")
			.subscribe(Log::i);
		CommonUtil.sleep(1000);
	}
	
	public void singleThread() {
		Observable<Integer> numbers = Observable.range(110, 5);
		Observable<String> chars = Observable.range(0, 5).map(CommonUtil::numberToAlphabet);
		
		numbers
			.subscribeOn(Schedulers.single())
			.subscribe(Log::i);
		chars
			.subscribeOn(Schedulers.single())
			.subscribe(Log::i);
		CommonUtil.sleep(1000);
	}
	
	public void executorThread() {
		final int THREAD_NUM = 10;
		String[] orgs = {"1", "3", "5"};
		Observable<String> source = Observable.fromArray(orgs);
		Executor executor = Executors.newFixedThreadPool(THREAD_NUM);
		
		source
			.subscribeOn(Schedulers.from(executor))
			.subscribe(Log::i);
		source
			.subscribeOn(Schedulers.from(executor))
			.subscribe(Log::i);
		CommonUtil.sleep(1000);
	}
}
