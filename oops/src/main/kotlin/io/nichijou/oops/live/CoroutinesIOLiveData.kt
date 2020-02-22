package io.nichijou.oops.live

import androidx.lifecycle.LiveData
import io.nichijou.oops.ext.loge
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class CoroutinesIOLiveData<T> : LiveData<T>(), CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler { _, e ->
      loge("Oops!!!", e)
    }

  override fun onInactive() {
    cancel()
  }
}
