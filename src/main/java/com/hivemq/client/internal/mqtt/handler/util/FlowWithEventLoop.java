/*
 * Copyright 2018 dc-square and the HiveMQ MQTT Client Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hivemq.client.internal.mqtt.handler.util;

import com.hivemq.client.internal.mqtt.MqttClientConfig;
import io.netty.channel.EventLoop;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Silvio Giebl
 */
public abstract class FlowWithEventLoop {

    private static final int STATE_INIT = 0;
    private static final int STATE_NOT_DONE = 1;
    private static final int STATE_DONE = 2;
    private static final int STATE_CANCELLED = 3;

    private final @NotNull MqttClientConfig clientConfig;
    protected final @NotNull EventLoop eventLoop;
    private final @NotNull AtomicInteger doneState = new AtomicInteger(STATE_INIT);

    public FlowWithEventLoop(final @NotNull MqttClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        eventLoop = clientConfig.acquireEventLoop();
    }

    public boolean init() {
        if (doneState.getAndSet(STATE_NOT_DONE) == STATE_CANCELLED) {
            clientConfig.releaseEventLoop();
            return false;
        }
        return true;
    }

    protected boolean setDone() {
        if (doneState.compareAndSet(STATE_NOT_DONE, STATE_DONE)) {
            clientConfig.releaseEventLoop();
            return true;
        }
        return false;
    }

    public void cancel() {
        if (doneState.getAndSet(STATE_CANCELLED) == STATE_NOT_DONE) {
            onCancel();
            clientConfig.releaseEventLoop();
        }
    }

    protected void onCancel() {}

    public boolean isCancelled() {
        return doneState.get() == STATE_CANCELLED;
    }

    public boolean isDisposed() {
        final int doneState = this.doneState.get();
        return (doneState == STATE_DONE) || (doneState == STATE_CANCELLED);
    }

    public @NotNull EventLoop getEventLoop() {
        return eventLoop;
    }
}
