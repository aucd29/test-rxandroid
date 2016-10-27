/*
 * Copyright (C) Hanwha S&C Ltd., 2016. All rights reserved.
 *
 * This software is covered by the license agreement between
 * the end user and Hanwha S&C Ltd., and may be
 * used and copied only in accordance with the terms of the
 * said agreement.
 *
 * Hanwha S&C Ltd., assumes no responsibility or
 * liability for any errors or inaccuracies in this software,
 * or any consequential, incidental or indirect damage arising
 * out of the use of the software.
 */

package com.example.rxandroid_e01;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final org.slf4j.Logger mLog = org.slf4j.LoggerFactory.getLogger(MainActivity.class);

    @BindView(R.id.hello)
    TextView mHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // https://realm.io/kr/news/rxandroid/

        // sender
        final Observable<String> observable = Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                // 개인적으로 생각되는 rx 의 구조
                mLog.debug("============================= on call <1>");
                try {
                    // 이곳에서 event 를 처리 하고

                    Thread.sleep(1000);

                    // 다음 결과를 전달
                    subscriber.onNext("Hello Android !!");
                } catch (Exception e) {
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });

        observable.observeOn(Schedulers.io());
        observable.subscribeOn(AndroidSchedulers.mainThread());

        // lambda
        observable.subscribe(s -> {
            // onNext
            mLog.debug("============================== on next <2-1>");

            // 결과를 기준으로 UI 를 변경해주고

        }, throwable -> {
            // ON ERROR
        }, () -> {
            // ON COMPLETED
        });

        observable.map(String::toUpperCase).subscribe(s -> {
            // onNext (uppercase)
            mLog.debug("============================== on next <2-2> : " + s);
            mHello.append(" " + s);
        });

        observable.map(String::length).subscribe(len -> {
            // onNext (to int)
            mLog.debug("============================== on next <2-3> : " + len);
            mHello.append(" " + len);
        }, throwable -> {} , () -> {
        });
    }
}
