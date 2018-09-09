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

package org.mqttbee.api.mqtt.mqtt5.advanced;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mqttbee.api.mqtt.mqtt5.advanced.qos1.Mqtt5IncomingQos1Interceptor;
import org.mqttbee.api.mqtt.mqtt5.advanced.qos1.Mqtt5OutgoingQos1Interceptor;
import org.mqttbee.api.mqtt.mqtt5.advanced.qos2.Mqtt5IncomingQos2Interceptor;
import org.mqttbee.api.mqtt.mqtt5.advanced.qos2.Mqtt5OutgoingQos2Interceptor;
import org.mqttbee.mqtt.advanced.MqttAdvancedClientData;
import org.mqttbee.util.FluentBuilder;

import java.util.function.Function;

/**
 * @author Silvio Giebl
 */
public class Mqtt5AdvancedClientDataBuilder<P> extends FluentBuilder<Mqtt5AdvancedClientData, P> {

    private @Nullable Mqtt5IncomingQos1Interceptor incomingQos1Interceptor;
    private @Nullable Mqtt5OutgoingQos1Interceptor outgoingQos1Interceptor;
    private @Nullable Mqtt5IncomingQos2Interceptor incomingQos2Interceptor;
    private @Nullable Mqtt5OutgoingQos2Interceptor outgoingQos2Interceptor;

    public Mqtt5AdvancedClientDataBuilder(final @Nullable Function<? super Mqtt5AdvancedClientData, P> parentConsumer) {
        super(parentConsumer);
    }

    public @NotNull Mqtt5AdvancedClientDataBuilder incomingQos1Interceptor(
            final @Nullable Mqtt5IncomingQos1Interceptor incomingQos1Interceptor) {

        this.incomingQos1Interceptor = incomingQos1Interceptor;
        return this;
    }

    public @NotNull Mqtt5AdvancedClientDataBuilder outgoingQos1Interceptor(
            final @Nullable Mqtt5OutgoingQos1Interceptor outgoingQos1Interceptor) {

        this.outgoingQos1Interceptor = outgoingQos1Interceptor;
        return this;
    }

    public @NotNull Mqtt5AdvancedClientDataBuilder incomingQos2Interceptor(
            final @Nullable Mqtt5IncomingQos2Interceptor incomingQos2Interceptor) {

        this.incomingQos2Interceptor = incomingQos2Interceptor;
        return this;
    }

    public @NotNull Mqtt5AdvancedClientDataBuilder outgoingQos2Interceptor(
            final @Nullable Mqtt5OutgoingQos2Interceptor outgoingQos2Interceptor) {

        this.outgoingQos2Interceptor = outgoingQos2Interceptor;
        return this;
    }

    @Override
    public @NotNull Mqtt5AdvancedClientData build() {
        return new MqttAdvancedClientData(incomingQos1Interceptor, outgoingQos1Interceptor, incomingQos2Interceptor,
                outgoingQos2Interceptor);
    }

}
