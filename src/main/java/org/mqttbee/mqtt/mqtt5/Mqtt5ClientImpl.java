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

package org.mqttbee.mqtt.mqtt5;

import com.google.common.base.Preconditions;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import org.jetbrains.annotations.NotNull;
import org.mqttbee.api.mqtt.MqttGlobalPublishFlowType;
import org.mqttbee.api.mqtt.mqtt5.Mqtt5Client;
import org.mqttbee.api.mqtt.mqtt5.message.connect.Mqtt5Connect;
import org.mqttbee.api.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import org.mqttbee.api.mqtt.mqtt5.message.disconnect.Mqtt5Disconnect;
import org.mqttbee.api.mqtt.mqtt5.message.publish.Mqtt5Publish;
import org.mqttbee.api.mqtt.mqtt5.message.publish.Mqtt5PublishResult;
import org.mqttbee.api.mqtt.mqtt5.message.subscribe.Mqtt5Subscribe;
import org.mqttbee.api.mqtt.mqtt5.message.subscribe.Mqtt5SubscribeResult;
import org.mqttbee.api.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck;
import org.mqttbee.api.mqtt.mqtt5.message.unsubscribe.Mqtt5Unsubscribe;
import org.mqttbee.api.mqtt.mqtt5.message.unsubscribe.unsuback.Mqtt5UnsubAck;
import org.mqttbee.mqtt.MqttClientData;
import org.mqttbee.mqtt.handler.auth.MqttReAuthCompletable;
import org.mqttbee.mqtt.handler.connect.MqttConnAckSingle;
import org.mqttbee.mqtt.handler.disconnect.MqttDisconnectCompletable;
import org.mqttbee.mqtt.handler.publish.MqttIncomingAckFlowable;
import org.mqttbee.mqtt.handler.publish.incoming.MqttGlobalIncomingPublishFlowable;
import org.mqttbee.mqtt.handler.publish.incoming.MqttSubscriptionFlowable;
import org.mqttbee.mqtt.handler.subscribe.MqttSubAckSingle;
import org.mqttbee.mqtt.handler.subscribe.MqttUnsubAckSingle;
import org.mqttbee.mqtt.message.connect.MqttConnect;
import org.mqttbee.mqtt.message.disconnect.MqttDisconnect;
import org.mqttbee.mqtt.message.publish.MqttPublish;
import org.mqttbee.mqtt.message.subscribe.MqttSubscribe;
import org.mqttbee.mqtt.message.unsubscribe.MqttUnsubscribe;
import org.mqttbee.rx.FlowableWithSingle;
import org.mqttbee.rx.FlowableWithSingleSplit;
import org.mqttbee.util.MustNotBeImplementedUtil;

/**
 * @author Silvio Giebl
 */
public class Mqtt5ClientImpl implements Mqtt5Client {

    private static final @NotNull Function<Mqtt5Publish, MqttPublish> PUBLISH_MAPPER =
            publish -> MustNotBeImplementedUtil.checkNotImplemented(publish, MqttPublish.class);

    private final @NotNull MqttClientData clientData;

    public Mqtt5ClientImpl(final @NotNull MqttClientData clientData) {
        this.clientData = clientData;
    }

    @Override
    public @NotNull Single<Mqtt5ConnAck> connect(final @NotNull Mqtt5Connect connect) {
        final MqttConnect mqttConnect = MustNotBeImplementedUtil.checkNotImplemented(connect, MqttConnect.class);

        return new MqttConnAckSingle(clientData, mqttConnect).observeOn(
                clientData.getExecutorConfig().getApplicationScheduler());
    }

    @Override
    public @NotNull Single<Mqtt5SubAck> subscribe(final @NotNull Mqtt5Subscribe subscribe) {
        final MqttSubscribe mqttSubscribe =
                MustNotBeImplementedUtil.checkNotImplemented(subscribe, MqttSubscribe.class);

        return new MqttSubAckSingle(mqttSubscribe, clientData).observeOn(
                clientData.getExecutorConfig().getApplicationScheduler());
    }

    @Override
    public @NotNull FlowableWithSingle<Mqtt5SubAck, Mqtt5Publish> subscribeWithStream(
            @NotNull final Mqtt5Subscribe subscribe) {

        final MqttSubscribe mqttSubscribe =
                MustNotBeImplementedUtil.checkNotImplemented(subscribe, MqttSubscribe.class);

        final Flowable<Mqtt5SubscribeResult> subscriptionFlowable =
                new MqttSubscriptionFlowable(mqttSubscribe, clientData).observeOn(
                        clientData.getExecutorConfig().getApplicationScheduler());
        return new FlowableWithSingleSplit<>(subscriptionFlowable, Mqtt5SubAck.class, Mqtt5Publish.class);
    }

    @Override
    public @NotNull Flowable<Mqtt5Publish> publishes(final @NotNull MqttGlobalPublishFlowType type) {
        Preconditions.checkNotNull(type, "Global publish flow type must not be null.");

        return new MqttGlobalIncomingPublishFlowable(type, clientData).observeOn(
                clientData.getExecutorConfig().getApplicationScheduler());
    }

    @Override
    public @NotNull Single<Mqtt5UnsubAck> unsubscribe(final @NotNull Mqtt5Unsubscribe unsubscribe) {
        final MqttUnsubscribe mqttUnsubscribe =
                MustNotBeImplementedUtil.checkNotImplemented(unsubscribe, MqttUnsubscribe.class);

        return new MqttUnsubAckSingle(mqttUnsubscribe, clientData).observeOn(
                clientData.getExecutorConfig().getApplicationScheduler());
    }

    @Override
    public @NotNull Flowable<Mqtt5PublishResult> publish(final @NotNull Flowable<Mqtt5Publish> publishFlowable) {
        return new MqttIncomingAckFlowable(publishFlowable.map(PUBLISH_MAPPER), clientData).observeOn(
                clientData.getExecutorConfig().getApplicationScheduler());
    }

    @Override
    public @NotNull Completable reauth() {
        return new MqttReAuthCompletable(clientData).observeOn(
                clientData.getExecutorConfig().getApplicationScheduler());
    }

    @Override
    public @NotNull Completable disconnect(final @NotNull Mqtt5Disconnect disconnect) {
        final MqttDisconnect mqttDisconnect =
                MustNotBeImplementedUtil.checkNotImplemented(disconnect, MqttDisconnect.class);

        return new MqttDisconnectCompletable(clientData, mqttDisconnect).observeOn(
                clientData.getExecutorConfig().getApplicationScheduler());
    }

    @Override
    public @NotNull MqttClientData getClientData() {
        return clientData;
    }

}
