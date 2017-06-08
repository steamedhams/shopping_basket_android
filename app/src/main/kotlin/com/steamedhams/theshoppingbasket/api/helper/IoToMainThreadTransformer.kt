package com.steamedhams.theshoppingbasket.api.helper

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by richard on 16/02/17.
 */
class IoToMainThreadTransformer<T> : Observable.Transformer<T, T> {

    override fun call(observable: Observable<T>?) : Observable<T>? {
        return observable?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
    }
}