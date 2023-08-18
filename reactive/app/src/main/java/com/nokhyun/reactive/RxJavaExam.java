package com.nokhyun.reactive;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;

public class RxJavaExam implements IExam {

    private final ArrayList<Integer> list = new ArrayList<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public RxJavaExam() {
        for (int i = 0; i <= 100; i++) {
            list.add(i);
        }
    }

    @Override
    public void start() {
        defaultObservable();
    }

    private void defaultObservable() {
        Consumer<List<Integer>> onNext = integers -> logger(() -> "[onNext] value:" + integers);
        Consumer<Throwable> onError = throwable -> logger(() -> "[onError] value:" + throwable.getMessage());
        Action onComplete = () -> logger(() -> "[onComplete] Done!");

        compositeDisposable.add(
                Observable.fromIterable(list)
                        .filter(value -> value % 2 == 0)
                        .buffer(5)
                        .doOnSubscribe(disposable -> logger(() -> "RxJava doOnSubscribe"))
                        .subscribe(onNext, onError, onComplete)
        );
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner lifecycleOwner, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            compositeDisposable.dispose();
        }
    }

    private void logger(Supplier<Object> log) {
        Log.e("Log", log.get().toString());
    }
}
