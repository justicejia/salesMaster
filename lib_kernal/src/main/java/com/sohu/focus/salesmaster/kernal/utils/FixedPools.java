package com.sohu.focus.salesmaster.kernal.utils;

/**
 * Created by zhaoqiang on 2017/6/21.
 */

public final class FixedPools {

    private FixedPools() {
        /* do nothing - hiding constructor */
    }

    /**
     * Interface for managing a pool of objects.
     *
     * @param <T> The pooled type.
     */
    public static interface Pool<T> {

        /**
         * @return An instance from the pool if such, null otherwise.
         */
        public T acquire();

        /**
         * Release an instance to the pool.
         *
         * @param instance The instance to release.
         * @return Whether the instance was put in the pool.
         * @throws IllegalStateException If the instance is already in the pool.
         */
        public boolean release(T instance);

        /**
         * release all object
         */
        public void destroy();
    }

    /**
     * Simple (non-synchronized) pool of objects.
     *
     * @param <T> The pooled type.
     */
    public static class SimplePool<T> implements FixedPools.Pool<T> {
        private final Object[] mPool;

        private int mPoolSize;

        private boolean isDestroy = false;

        /**
         * Creates a new instance.
         *
         * @param maxPoolSize The max pool size.
         * @throws IllegalArgumentException If the max pool size is less than zero.
         */
        public SimplePool(int maxPoolSize) {
            if (maxPoolSize <= 0) {
                throw new IllegalArgumentException("The max pool size must be > 0");
            }
            mPool = new Object[maxPoolSize];
        }

        @Override
        @SuppressWarnings("unchecked")
        public T acquire() {
            isDestroy = false;
            if (mPoolSize > 0) {
                final int lastPooledIndex = mPoolSize - 1;
                T instance = (T) mPool[lastPooledIndex];
                mPool[lastPooledIndex] = null;
                mPoolSize--;
                return instance;
            }
            return null;
        }

        @Override
        public boolean release(T instance) {
            if(isDestroy)
                return false;
            if (isInPool(instance)) {
                throw new IllegalStateException("Already in the pool!");
            }
            if (mPoolSize < mPool.length) {
                mPool[mPoolSize] = instance;
                mPoolSize++;
                return true;
            }
            return false;
        }

        @Override
        public void destroy() {
            isDestroy = true;
            if (mPool.length > 0) {
                for (int i = 0; i < mPool.length; i++) {
                    mPool[i] = null;
                }
                mPoolSize = 0;
            }
        }

        private boolean isInPool(T instance) {
            for (int i = 0; i < mPoolSize; i++) {
                if (mPool[i] == instance) {
                    return true;
                }
            }
            return false;
        }

        public boolean isDestroy() {
            return isDestroy;
        }
    }

    /**
     * Synchronized) pool of objects.
     *
     * @param <T> The pooled type.
     */
    public static class SynchronizedPool<T> extends FixedPools.SimplePool<T> {
        private final Object mLock = new Object();

        /**
         * Creates a new instance.
         *
         * @param maxPoolSize The max pool size.
         * @throws IllegalArgumentException If the max pool size is less than zero.
         */
        public SynchronizedPool(int maxPoolSize) {
            super(maxPoolSize);
        }

        @Override
        public T acquire() {
            synchronized (mLock) {
                return super.acquire();
            }
        }

        @Override
        public boolean release(T element) {
            synchronized (mLock) {
                return super.release(element);
            }
        }

        @Override
        public void destroy() {
            synchronized (mLock) {
                super.destroy();
            }
        }
    }
}
