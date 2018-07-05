package operator;

import Util.Log;
import hu.akarnokd.rxjava2.math.MathFlowable;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class MathematicalAndAggregateOperator {

	public static void main(String[] args) {
		MathematicalAndAggregateOperator demo = new MathematicalAndAggregateOperator();
		demo.count();
		demo.max();
		demo.min();
		demo.sum();
		demo.average();
	}
	
	Integer[] data = {1,2,3,4};
	
	public void count() {
		Single<Long> source = Observable.fromArray(data)
				.count();
		source.subscribe(count -> Log.i("count is "+count));
	}
	
	public void max() {
		Flowable.fromArray(data)
			.to(MathFlowable::max)
			.subscribe(max -> Log.i("max is "+max));
	}
	
	public void min() {
		Flowable.fromArray(data)
			.to(MathFlowable::min)
			.subscribe(min -> Log.i("min is "+min));
	}
	
	public void sum() {
		Flowable<Integer> flowable = Flowable.fromArray(data)
				.to(MathFlowable::sumInt);
		flowable.subscribe(sum -> Log.i("sum is "+sum));
	}
	
	public void average() {
		Flowable<Double> flowable = Observable.fromArray(data)
				.toFlowable(BackpressureStrategy.BUFFER)
				.to(MathFlowable::averageDouble);
		flowable.subscribe(avg -> Log.i("average is "+avg));
	}
	
}
