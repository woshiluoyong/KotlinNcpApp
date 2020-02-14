package com.stephen.kotlin.testjava;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOperator;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableOperator;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.operators.single.SingleToObservable;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

public class MyJava {
    public static void main(String[] args) {
        /*Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> observableEmitter) throws Exception {
                observableEmitter.onNext("msg1");
                observableEmitter.onNext("msg2");
                observableEmitter.onComplete();
            }
        }).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                System.out.println("=============onSubscribe======>");
            }

            @Override
            public void onNext(Object o) {
                System.out.println("============onNext=======>"+o);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("=============onError======>");
            }

            @Override
            public void onComplete() {
                System.out.println("=============onComplete======>");
            }
        });*/
        Integer[] ary = new Integer[]{12,244,5,6,5};
        /*Observable.fromArray(ary).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("===================>"+integer);
            }
        });*/

        /*Observable observable = Observable.intervalRange(1, 10, 1, 2, TimeUnit.SECONDS);

        Consumer observer = new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("===================>"+o);
            }
        };

        observable.subscribe(observer);*/

        /*class Man{
            private String name;
            private List<Plan> plans;

            public Man() {
            }

            public Man(String name, List<Plan> plans) {
                this.name = name;
                this.plans = plans;
            }

            class Plan{
                private String time;
                private List<Action> actions;

                public Plan(String time, List<Action> actions) {
                    this.time = time;
                    this.actions = actions;
                }
            }

            class Action{
                private String name;

                public Action(String name) {
                    this.name = name;
                }
            }
        }

        List<Man> mans = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            List<Man.Plan> plans = new ArrayList<>();
            for (int k = 0; k < 3; k++) {
                List<Man.Action> actions = new ArrayList<>();
                for (int l = 0; l < 3; l++) {
                    actions.add(l, (new Man()).new Action("addd"+l));
                }
                plans.add((new Man()).new Plan("2012-01-0"+j, actions));
            }
            mans.add(j, new Man("name"+j, plans));
        }*/
        class Bean{
            private String name;

            public Bean(String name) {
                this.name = name;
            }
        }

        List<Bean> beans = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            beans.add(new Bean("stephen"+j));
        }
        /*

        Observable.just(1,2,3,4,5,6).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer%2 == 0;
            }
        }).doOnTerminate(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("============doOnTerminate=======>");
            }
        }).subscribe(new Observer(){
            @Override
            public void onSubscribe(Disposable disposable) {
                System.out.println("=============onSubscribe======>");
            }

            @Override
            public void onNext(Object o) {
                System.out.println("============onNext=======>"+o);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("=============onError======>");
            }

            @Override
            public void onComplete() {
                System.out.println("=============onComplete======>");
            }
        });*/

        Maybe.just(10).lift(new SQOperator()).compose(new TestTransformer()).subscribe(new MaybeObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onSuccess(Integer integer) {
                System.out.println("===================>"+integer);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public static class SQOperator implements MaybeOperator<Integer, Integer> {

        Disposable disposable;

        @Override
        public MaybeObserver<? super Integer> apply(final MaybeObserver<? super Integer> observer) throws Exception {
            return new MaybeObserver<Integer>() {
                @Override
                public void onSubscribe(Disposable d) {
                    disposable = d;
                    observer.onSubscribe(d);
                }

                @Override
                public void onSuccess(Integer integer) {
                    observer.onSuccess(integer * integer);
                }

                @Override
                public void onError(Throwable throwable) {
                    observer.onError(throwable);
                }

                @Override
                public void onComplete() {
                    observer.onComplete();
                }
            };
        }
    }

    public static class TestTransformer implements MaybeTransformer<Integer, Integer>{
        @Override
        public MaybeSource<Integer> apply(Maybe<Integer> maybe) {
            return maybe.map(new Function<Integer, Integer>() {
                @Override
                public Integer apply(Integer integer) throws Exception {
                    return integer * integer;
                }
            });
        }
    }
}