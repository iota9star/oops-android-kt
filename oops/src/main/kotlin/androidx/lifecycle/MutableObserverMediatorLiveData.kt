package androidx.lifecycle

import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import androidx.arch.core.internal.SafeIterableMap

class MutableObserverMediatorLiveData<T> : MutableLiveData<T>() {
  private val mSources = SafeIterableMap<LiveData<*>, Source<*>>()

  @MainThread
  fun <S> addSource(source: LiveData<S>, onChanged: Observer<S>) {
    val e = Source(source)
    val existing = mSources.putIfAbsent(source, e)
    if (existing != null) {
      existing.addObserver(onChanged as Observer<Any?>)
      return
    }
    e.addObserver(onChanged)
    if (hasActiveObservers()) {
      e.plug()
    }
  }

  @MainThread
  fun <S> removeSource(toRemote: LiveData<S>) {
    mSources.remove(toRemote)?.unplug()
  }

  @CallSuper
  override fun onActive() {
    mSources.forEach {
      it.value.plug()
    }
  }

  @CallSuper
  override fun onInactive() {
    mSources.forEach {
      it.value.unplug()
    }
  }

  private class Source<V>(private val mLiveData: LiveData<V>) : Observer<V> {
    private var mVersion = START_VERSION
    private val mObservers = hashSetOf<Observer<in V>>()

    fun addObserver(observer: Observer<in V>) {
      mObservers.add(observer)
    }

    fun plug() {
      mLiveData.observeForever(this)
    }

    fun unplug() {
      mLiveData.removeObserver(this)
    }

    override fun onChanged(v: V?) {
      if (mVersion != mLiveData.version) {
        mVersion = mLiveData.version
        mObservers.forEach { it.onChanged(v) }
      }
    }
  }
}
