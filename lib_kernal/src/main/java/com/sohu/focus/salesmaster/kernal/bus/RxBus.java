package com.sohu.focus.salesmaster.kernal.bus;


import com.sohu.focus.salesmaster.kernal.log.Logger;

import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * usage :
 *  1.在activity、fragment中注册
 *  eg : Subscription subscription =
 *             RxBus.get().subscribe(String.class, new RxBus.OnHandleEvent<String>() {
 *                  @Override
 *                  public void onEvent(String s) {
 *                      reloadUserInfo();
 *                  }
 *               });
 *  2.在需要发送通知的类中 RxBus.get().post("something");
 *  3.在注册类关闭时解绑
 *  if(rxBusSubscription != null && !rxBusSubscription.isUnsubscribed()) {
 *      rxBusSubscription.unsubscribe();
 *  }
 * Created by qiangzhao on 2016/12/28.
 *
 * warning:
 * 同一个类的事件，会被fragment优先消费，现在在{com.sohu.focus.live.base.view.FocusBaseFragmentActivity#globalRxBus}中统一进行注册
 * ，如果没有成功消费调用{com.sohu.focus.live.base.view.FocusBaseFragmentActivity#dispatchRxEvent(RxEvent)}进行分发到每一个该activity
 * 管理的fragment中进行消费
 */

public class RxBus {

    public static final String TAG = RxBus.class.getSimpleName();

    private static volatile RxBus defaultInstance;

    private final Subject<Object, Object> bus;
    //粘性事件缓存
    private final ConcurrentHashMap<Class<?>, Object> mStickyEventMap;

    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    private RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
        mStickyEventMap = new ConcurrentHashMap<>();
    }

    public static RxBus get() {
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new RxBus();
                }
            }
        }
        return defaultInstance ;
    }
    // 发送一个新的事件
    public void post (Object o) {
        bus.onNext(o);
    }
    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public <T> Observable<T> toObservable (Class<T> eventType) {
        return bus.ofType(eventType);
    }

    //直接订阅，最简单的使用
    public <T> Subscription subscribe(final Class<T> eventType, final OnHandleEvent<T> onHandleEvent) {
       return bus.ofType(eventType).subscribe(new Subscriber<T>() {
//           int i = 0;
           @Override
           public void onCompleted() {

           }

           @Override
           public void onError(Throwable e) {
               Logger.ZQ().e(TAG, "rx event error : " + e);
//               //失败会进行重试，最多4次
//               if(++i < 5) {
//                   subscribe(eventType, onHandleEvent);
//               }
           }

           @Override
           public void onNext(T t) {
               try {
                   onHandleEvent.onEvent(t);
               }catch (Exception e) {
                   Logger.ZQ().e("rx event error : " + new Throwable(e));
               }
           }
       });
    }

    /**
     * 判断是否有订阅者
     */
    public boolean hasObservers() {
        return bus.hasObservers();
    }

    public void reset() {
        defaultInstance = null;
    }

    /**
     * Stciky 相关
     */

    /**
     * 发送一个新Sticky事件
     */
    public void postSticky(Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.putIfAbsent(event.getClass(), event);
        }
        post(event);
    }

    /**
     * 订阅粘性事件
     */
    public <T> Subscription subscribeSticky(final Class<T> eventType, final OnHandleEvent<T> onHandleEvent) {
        synchronized (mStickyEventMap) {
            Observable<T> observable = bus.ofType(eventType);
            final Object event = mStickyEventMap.get(eventType);

            if (event != null) {
                observable =  observable.mergeWith(Observable.create(new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        subscriber.onNext(eventType.cast(event));
                    }
                }));
            }
            return observable.subscribe(new Subscriber<T>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Logger.ZQ().e(TAG, e.getMessage());
                }

                @Override
                public void onNext(T t) {
                    try {
                        onHandleEvent.onEvent(t);
                    }catch (Exception e) {
                        Logger.ZQ().e("rx event sticky error : " + e.getMessage());
                    }
                }
            });
        }
    }

    /**
     * 根据eventType获取Sticky事件
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.get(eventType));
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }


    public interface OnHandleEvent<T> {
        void onEvent(T t);
    }
}
