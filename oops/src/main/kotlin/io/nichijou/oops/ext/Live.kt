package io.nichijou.oops.ext

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData


fun <A, B, R> liveMediator(a: LiveData<A>, b: LiveData<B>, r: Live2<A, B, R>): MediatorLiveData<R> {
    return MediatorLiveData<R>().apply {
        var lastA: A? = null
        var lastB: B? = null
        addSource(a) {
            lastA = it ?: return@addSource
            if (lastA != null && lastB != null) {
                val apply = r.apply(lastA!!, lastB!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
        addSource(b) {
            lastB = it ?: return@addSource
            if (lastA != null && lastB != null) {
                val apply = r.apply(lastA!!, lastB!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
    }
}

fun <A, B, C, R> liveMediator(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>, r: Live3<A, B, C, R>): MediatorLiveData<R> {
    return MediatorLiveData<R>().apply {
        var lastA: A? = null
        var lastB: B? = null
        var lastC: C? = null
        addSource(a) {
            lastA = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
        addSource(b) {
            lastB = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
        addSource(c) {
            lastC = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
    }
}

fun <A, B, C, D, R> liveMediator(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>, d: LiveData<D>, r: Live4<A, B, C, D, R>): MediatorLiveData<R> {
    return MediatorLiveData<R>().apply {
        var lastA: A? = null
        var lastB: B? = null
        var lastC: C? = null
        var lastD: D? = null
        addSource(a) {
            lastA = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null && lastD != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!, lastD!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
        addSource(b) {
            lastB = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null && lastD != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!, lastD!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
        addSource(c) {
            lastC = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null && lastD != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!, lastD!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
        addSource(d) {
            lastD = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null && lastD != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!, lastD!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
    }
}

fun <A, B, C, D, E, R> liveMediator(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>, d: LiveData<D>, e: LiveData<E>, r: Live5<A, B, C, D, E, R>): MediatorLiveData<R> {
    return MediatorLiveData<R>().apply {
        var lastA: A? = null
        var lastB: B? = null
        var lastC: C? = null
        var lastD: D? = null
        var lastE: E? = null
        addSource(a) {
            lastA = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null && lastD != null && lastE != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!, lastD!!, lastE!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
        addSource(b) {
            lastB = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null && lastD != null && lastE != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!, lastD!!, lastE!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
        addSource(c) {
            lastC = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null && lastD != null && lastE != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!, lastD!!, lastE!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
        addSource(d) {
            lastD = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null && lastD != null && lastE != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!, lastD!!, lastE!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
        addSource(e) {
            lastE = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null && lastD != null && lastE != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!, lastD!!, lastE!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
    }
}

fun <A, B, C, D, E, F, R> liveMediator(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>, d: LiveData<D>, e: LiveData<E>, f: LiveData<F>, r: Live6<A, B, C, D, E, F, R>): MediatorLiveData<R> {
    return MediatorLiveData<R>().apply {
        var lastA: A? = null
        var lastB: B? = null
        var lastC: C? = null
        var lastD: D? = null
        var lastE: E? = null
        var lastF: F? = null
        addSource(a) {
            lastA = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null && lastD != null && lastE != null && lastF != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!, lastD!!, lastE!!, lastF!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
        addSource(b) {
            lastB = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null && lastD != null && lastE != null && lastF != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!, lastD!!, lastE!!, lastF!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
        addSource(c) {
            lastC = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null && lastD != null && lastE != null && lastF != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!, lastD!!, lastE!!, lastF!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
        addSource(d) {
            lastD = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null && lastD != null && lastE != null && lastF != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!, lastD!!, lastE!!, lastF!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
        addSource(e) {
            lastE = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null && lastD != null && lastE != null && lastF != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!, lastD!!, lastE!!, lastF!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
        addSource(f) {
            lastF = it ?: return@addSource
            if (lastA != null && lastB != null && lastC != null && lastD != null && lastE != null && lastF != null) {
                val apply = r.apply(lastA!!, lastB!!, lastC!!, lastD!!, lastE!!, lastF!!)
                if (value != apply) {
                    value = apply
                }
            }
        }
    }
}

interface Live2<A, B, R> {
    @NonNull
    @Throws(Exception::class)
    fun apply(@NonNull a: A, @NonNull b: B): R
}

interface Live3<A, B, C, R> {
    @NonNull
    @Throws(Exception::class)
    fun apply(@NonNull a: A, @NonNull b: B, @NonNull c: C): R
}

interface Live4<A, B, C, D, R> {
    @NonNull
    @Throws(Exception::class)
    fun apply(@NonNull a: A, @NonNull b: B, @NonNull c: C, @NonNull d: D): R
}

interface Live5<A, B, C, D, E, R> {
    @NonNull
    @Throws(Exception::class)
    fun apply(@NonNull a: A, @NonNull b: B, @NonNull c: C, @NonNull d: D, @NonNull e: E): R
}

interface Live6<A, B, C, D, E, F, R> {
    @NonNull
    @Throws(Exception::class)
    fun apply(@NonNull a: A, @NonNull b: B, @NonNull c: C, @NonNull d: D, @NonNull e: E, @NonNull f: F): R
}