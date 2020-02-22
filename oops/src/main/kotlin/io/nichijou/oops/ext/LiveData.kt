package io.nichijou.oops.ext

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableObserverMediatorLiveData
import androidx.lifecycle.Observer

fun <A, B, R> mediateLiveDataNonNull(a: LiveData<A>, b: LiveData<B>, r: Live2NonNull<A, B, R>): MutableObserverMediatorLiveData<R> {
  return MutableObserverMediatorLiveData<R>().apply {
    var lastA: A? = null
    var lastB: B? = null
    addSource(a, Observer {
      lastA = it ?: return@Observer
      combineNonNull(lastA, lastB, r)
    })
    addSource(b, Observer {
      lastB = it ?: return@Observer
      combineNonNull(lastA, lastB, r)
    })
  }
}

private fun <A, B, R> MutableObserverMediatorLiveData<R>.combineNonNull(lastA: A?, lastB: B?, r: Live2NonNull<A, B, R>) {
  if (lastA != null && lastB != null) {
    val apply = r.apply(lastA, lastB)
    if (value != apply) {
      value = apply
    }
  }
}

fun <A, B, C, R> mediateLiveDataNonNull(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>, r: Live3NonNull<A, B, C, R>): MutableObserverMediatorLiveData<R> {
  return MutableObserverMediatorLiveData<R>().apply {
    var lastA: A? = null
    var lastB: B? = null
    var lastC: C? = null
    addSource(a, Observer {
      lastA = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, r)
    })
    addSource(b, Observer {
      lastB = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, r)
    })
    addSource(c, Observer {
      lastC = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, r)
    })
  }
}

private fun <A, B, C, R> MutableObserverMediatorLiveData<R>.combineNonNull(lastA: A?, lastB: B?, lastC: C?, r: Live3NonNull<A, B, C, R>) {
  if (lastA != null && lastB != null && lastC != null) {
    val apply = r.apply(lastA, lastB, lastC)
    if (value != apply) {
      value = apply
    }
  }
}

fun <A, B, C, D, R> mediateLiveDataNonNull(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>, d: LiveData<D>, r: Live4NonNull<A, B, C, D, R>): MutableObserverMediatorLiveData<R> {
  return MutableObserverMediatorLiveData<R>().apply {
    var lastA: A? = null
    var lastB: B? = null
    var lastC: C? = null
    var lastD: D? = null
    addSource(a, Observer {
      lastA = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, lastD, r)
    })
    addSource(b, Observer {
      lastB = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, lastD, r)
    })
    addSource(c, Observer {
      lastC = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, lastD, r)
    })
    addSource(d, Observer {
      lastD = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, lastD, r)
    })
  }
}

fun <A, B, C, D, R> mediateLiveData(a: LiveData<A>?, b: LiveData<B>?, c: LiveData<C>?, d: LiveData<D>?, r: Live4<A, B, C, D, R>): MutableObserverMediatorLiveData<R> {
  return MutableObserverMediatorLiveData<R>().apply {
    var lastA: A? = null
    var lastB: B? = null
    var lastC: C? = null
    var lastD: D? = null

    if (a != null) {
      addSource(a, Observer {
        lastA = it
        combineNullable(lastA, lastB, lastC, lastD, r)
      })
    }
    if (b != null) {
      addSource(b, Observer {
        lastB = it
        combineNullable(lastA, lastB, lastC, lastD, r)
      })
    }
    if (c != null) {
      addSource(c, Observer {
        lastC = it
        combineNullable(lastA, lastB, lastC, lastD, r)
      })
    }
    if (d != null) {
      addSource(d, Observer {
        lastD = it
        combineNullable(lastA, lastB, lastC, lastD, r)
      })
    }
  }
}

private fun <A, B, C, D, R> MutableObserverMediatorLiveData<R>.combineNullable(lastA: A?, lastB: B?, lastC: C?, lastD: D?, r: Live4<A, B, C, D, R>) {
  val apply = r.apply(lastA, lastB, lastC, lastD)
  if (value != apply) {
    value = apply
  }
}

private fun <A, B, C, D, R> MutableObserverMediatorLiveData<R>.combineNonNull(lastA: A?, lastB: B?, lastC: C?, lastD: D?, r: Live4NonNull<A, B, C, D, R>) {
  if (lastA != null && lastB != null && lastC != null && lastD != null) {
    val apply = r.apply(lastA, lastB, lastC, lastD)
    if (value != apply) {
      value = apply
    }
  }
}

fun <A, B, C, D, E, R> mediateLiveDataNonNull(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>, d: LiveData<D>, e: LiveData<E>, r: Live5NonNull<A, B, C, D, E, R>): MutableObserverMediatorLiveData<R> {
  return MutableObserverMediatorLiveData<R>().apply {
    var lastA: A? = null
    var lastB: B? = null
    var lastC: C? = null
    var lastD: D? = null
    var lastE: E? = null
    addSource(a, Observer {
      lastA = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, lastD, lastE, r)
    })
    addSource(b, Observer {
      lastB = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, lastD, lastE, r)
    })
    addSource(c, Observer {
      lastC = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, lastD, lastE, r)
    })
    addSource(d, Observer {
      lastD = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, lastD, lastE, r)
    })
    addSource(e, Observer {
      lastE = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, lastD, lastE, r)
    })
  }
}

fun <A, B, C, D, E, R> mediateLiveData(a: LiveData<A>?, b: LiveData<B>?, c: LiveData<C>?, d: LiveData<D>?, e: LiveData<E>?, r: Live5<A, B, C, D, E, R>): MutableObserverMediatorLiveData<R> {
  return MutableObserverMediatorLiveData<R>().apply {
    var lastA: A? = null
    var lastB: B? = null
    var lastC: C? = null
    var lastD: D? = null
    var lastE: E? = null

    if (a != null) {
      addSource(a, Observer {
        lastA = it
        combineNullable(lastA, lastB, lastC, lastD, lastE, r)
      })
    }
    if (b != null) {
      addSource(b, Observer {
        lastB = it
        combineNullable(lastA, lastB, lastC, lastD, lastE, r)
      })
    }
    if (c != null) {
      addSource(c, Observer {
        lastC = it
        combineNullable(lastA, lastB, lastC, lastD, lastE, r)
      })
    }
    if (d != null) {
      addSource(d, Observer {
        lastD = it
        combineNullable(lastA, lastB, lastC, lastD, lastE, r)
      })
    }
    if (e != null) {
      addSource(e, Observer {
        lastE = it
        combineNullable(lastA, lastB, lastC, lastD, lastE, r)
      })
    }
  }
}

private fun <A, B, C, D, E, R> MutableObserverMediatorLiveData<R>.combineNullable(lastA: A?, lastB: B?, lastC: C?, lastD: D?, lastE: E?, r: Live5<A, B, C, D, E, R>) {
  val apply = r.apply(lastA, lastB, lastC, lastD, lastE)
  if (value != apply) {
    value = apply
  }
}

private fun <A, B, C, D, E, R> MutableObserverMediatorLiveData<R>.combineNonNull(lastA: A?, lastB: B?, lastC: C?, lastD: D?, lastE: E?, r: Live5NonNull<A, B, C, D, E, R>) {
  if (lastA != null && lastB != null && lastC != null && lastD != null && lastE != null) {
    val apply = r.apply(lastA, lastB, lastC, lastD, lastE)
    if (value != apply) {
      value = apply
    }
  }
}

fun <A, B, C, D, E, F, R> mediateLiveDataNonNull(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>, d: LiveData<D>, e: LiveData<E>, f: LiveData<F>, r: Live6NonNull<A, B, C, D, E, F, R>): MutableObserverMediatorLiveData<R> {
  return MutableObserverMediatorLiveData<R>().apply {
    var lastA: A? = null
    var lastB: B? = null
    var lastC: C? = null
    var lastD: D? = null
    var lastE: E? = null
    var lastF: F? = null
    addSource(a, Observer {
      lastA = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, lastD, lastE, lastF, r)
    })
    addSource(b, Observer {
      lastB = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, lastD, lastE, lastF, r)
    })
    addSource(c, Observer {
      lastC = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, lastD, lastE, lastF, r)
    })
    addSource(d, Observer {
      lastD = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, lastD, lastE, lastF, r)
    })
    addSource(e, Observer {
      lastE = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, lastD, lastE, lastF, r)
    })
    addSource(f, Observer {
      lastF = it ?: return@Observer
      combineNonNull(lastA, lastB, lastC, lastD, lastE, lastF, r)
    })
  }
}

private fun <A, B, C, D, E, F, R> MutableObserverMediatorLiveData<R>.combineNonNull(lastA: A?, lastB: B?, lastC: C?, lastD: D?, lastE: E?, lastF: F?, r: Live6NonNull<A, B, C, D, E, F, R>) {
  if (lastA != null && lastB != null && lastC != null && lastD != null && lastE != null && lastF != null) {
    val apply = r.apply(lastA, lastB, lastC, lastD, lastE, lastF)
    if (value != apply) {
      value = apply
    }
  }
}

interface Live2NonNull<A, B, R> {
  @NonNull
  fun apply(@NonNull a: A, @NonNull b: B): R
}

interface Live3NonNull<A, B, C, R> {
  @NonNull
  fun apply(@NonNull a: A, @NonNull b: B, @NonNull c: C): R
}

interface Live4NonNull<A, B, C, D, R> {
  @NonNull
  fun apply(@NonNull a: A, @NonNull b: B, @NonNull c: C, @NonNull d: D): R
}

interface Live4<A, B, C, D, R> {
  @NonNull
  fun apply(a: A?, b: B?, c: C?, d: D?): R
}

interface Live5NonNull<A, B, C, D, E, R> {
  @NonNull
  fun apply(@NonNull a: A, @NonNull b: B, @NonNull c: C, @NonNull d: D, @NonNull e: E): R
}

interface Live5<A, B, C, D, E, R> {
  @NonNull
  fun apply(a: A?, b: B?, c: C?, d: D?, e: E?): R
}

interface Live6NonNull<A, B, C, D, E, F, R> {
  @NonNull
  fun apply(@NonNull a: A, @NonNull b: B, @NonNull c: C, @NonNull d: D, @NonNull e: E, @NonNull f: F): R
}
