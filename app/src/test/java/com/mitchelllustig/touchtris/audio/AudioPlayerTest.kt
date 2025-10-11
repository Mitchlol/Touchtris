package com.mitchelllustig.touchtris.audio

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test

class AudioPlayerTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var lifecycleRegistry: LifecycleRegistry


    companion object {
        lateinit var mockBridge: NativeAudioBridge

        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            mockBridge = mockk<NativeAudioBridge>(relaxed = true)
            AudioPlayer.getNativeAudioBridge = { mockBridge }
        }
    }

    @Before
    fun setUp() {

        lifecycleOwner = mockk()
        lifecycleRegistry = LifecycleRegistry(lifecycleOwner)
        every { lifecycleOwner.lifecycle } returns lifecycleRegistry
        lifecycleRegistry.addObserver(AudioPlayer)

        AudioPlayer.playerState = PlayerState.Stopped
        AudioPlayer.audioEnabled = true
        clearMocks(mockBridge)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onStart event calls startAudioStreamNative`() = runTest {

        AudioPlayer.dispatcher = StandardTestDispatcher(testScheduler)

        assert(AudioPlayer.playerState == PlayerState.Stopped)

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        advanceUntilIdle()

        assert(AudioPlayer.playerState == PlayerState.Started)

        verify(exactly = 1) { mockBridge.startAudioStreamNative() }
    }

    @Test
    fun `onStop event calls stopAudioStreamNative`() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)

        verify(exactly = 1) { mockBridge.stopAudioStreamNative() }
    }

    @Test
    fun `playerState is updated correctly by lifecycle events`() = runTest {

        AudioPlayer.dispatcher = StandardTestDispatcher(testScheduler)
        AudioPlayer.audioEnabled = true

        AudioPlayer.button()
        verify(exactly = 0) { mockBridge.buttonSoundNative() }

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        advanceUntilIdle()

        AudioPlayer.button()
        verify(exactly = 1) { mockBridge.buttonSoundNative() }

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        advanceUntilIdle()

        AudioPlayer.button()
        verify(exactly = 1) { mockBridge.buttonSoundNative() }
    }

    @Test
    fun `audioEnabled flag prevents sounds when false`() {
        AudioPlayer.audioEnabled = false
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)

        AudioPlayer.button()

        verify(exactly = 0) { mockBridge.buttonSoundNative() }
    }
}
