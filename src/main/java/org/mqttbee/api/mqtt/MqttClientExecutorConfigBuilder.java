/*
 * Copyright 2018 The MQTT Bee project
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

package org.mqttbee.api.mqtt;

import com.google.common.base.Preconditions;
import io.netty.channel.MultithreadEventLoopGroup;
import io.reactivex.Scheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mqttbee.mqtt.MqttClientExecutorConfigImpl;
import org.mqttbee.mqtt.ioc.MqttBeeComponent;
import org.mqttbee.util.FluentBuilder;

import java.util.concurrent.Executor;
import java.util.function.Function;

import static org.mqttbee.api.mqtt.MqttClientExecutorConfig.DEFAULT_APPLICATION_SCHEDULER;

/**
 * @author Silvio Giebl
 */
public class MqttClientExecutorConfigBuilder<P> extends FluentBuilder<MqttClientExecutorConfig, P> {

    private @Nullable Executor nettyExecutor;
    private int nettyThreads = MqttClientExecutorConfigImpl.DEFAULT_NETTY_THREADS;
    private @Nullable Scheduler applicationScheduler = DEFAULT_APPLICATION_SCHEDULER;

    public MqttClientExecutorConfigBuilder(
            final @Nullable Function<? super MqttClientExecutorConfig, P> parentConsumer) {

        super(parentConsumer);
    }

    public @NotNull MqttClientExecutorConfigBuilder<P> nettyExecutor(final @NotNull Executor nettyExecutor) {
        Preconditions.checkNotNull(nettyExecutor, "Netty executor must not be null.");
        this.nettyExecutor = nettyExecutor;
        return this;
    }

    public @NotNull MqttClientExecutorConfigBuilder<P> nettyThreads(final int nettyThreads) {
        Preconditions.checkArgument(nettyThreads > 0, "Number of Netty threads must be bigger than 0. Found: %s.",
                nettyThreads);
        this.nettyThreads = nettyThreads;
        return this;
    }

    public @NotNull MqttClientExecutorConfigBuilder<P> applicationScheduler(
            final @Nullable Scheduler applicationScheduler) {

        this.applicationScheduler = applicationScheduler;
        return this;
    }

    @Override
    public @NotNull MqttClientExecutorConfig build() {
        final MultithreadEventLoopGroup eventLoopGroup =
                MqttBeeComponent.INSTANCE.nettyEventLoopProvider().getEventLoopGroup(nettyExecutor, nettyThreads);
        return new MqttClientExecutorConfigImpl(eventLoopGroup, applicationScheduler);
    }

}
