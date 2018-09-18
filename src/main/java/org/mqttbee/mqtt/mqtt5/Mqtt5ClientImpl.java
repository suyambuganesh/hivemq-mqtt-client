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
import org.mqttbee.mqtt.handler.publish.incoming.MqttGlobalIncomingPublishFlowable;
import org.mqttbee.mqtt.handler.publish.incoming.MqttSubscriptionFlowable;
import org.mqttbee.mqtt.handler.publish.outgoing.MqttIncomingAckFlowable;
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

        return observeOn(new MqttConnAckSingle(clientData, mqttConnect));
    }

    @Override
    public @NotNull Single<Mqtt5SubAck> subscribe(final @NotNull Mqtt5Subscribe subscribe) {
        final MqttSubscribe mqttSubscribe =
                MustNotBeImplementedUtil.checkNotImplemented(subscribe, MqttSubscribe.class);

        return observeOn(new MqttSubAckSingle(mqttSubscribe, clientData));
    }

    @Override
    public @NotNull FlowableWithSingle<Mqtt5SubAck, Mqtt5Publish> subscribeWithStream(
            @NotNull final Mqtt5Subscribe subscribe) {

        final MqttSubscribe mqttSubscribe =
                MustNotBeImplementedUtil.checkNotImplemented(subscribe, MqttSubscribe.class);

        final Flowable<Mqtt5SubscribeResult> subscriptionFlowable =
                observeOn(new MqttSubscriptionFlowable(mqttSubscribe, clientData));
        return new FlowableWithSingleSplit<>(subscriptionFlowable, Mqtt5SubAck.class, Mqtt5Publish.class);
    }

    @Override
    public @NotNull Flowable<Mqtt5Publish> publishes(final @NotNull MqttGlobalPublishFlowType type) {
        Preconditions.checkNotNull(type, "Global publish flow type must not be null.");

        return observeOn(new MqttGlobalIncomingPublishFlowable(type, clientData));
    }

    @Override
    public @NotNull Single<Mqtt5UnsubAck> unsubscribe(final @NotNull Mqtt5Unsubscribe unsubscribe) {
        final MqttUnsubscribe mqttUnsubscribe =
                MustNotBeImplementedUtil.checkNotImplemented(unsubscribe, MqttUnsubscribe.class);

        return observeOn(new MqttUnsubAckSingle(mqttUnsubscribe, clientData));
    }

    @Override
    public @NotNull Flowable<Mqtt5PublishResult> publish(final @NotNull Flowable<Mqtt5Publish> publishFlowable) {
        return observeOn(new MqttIncomingAckFlowable(publishFlowable.map(PUBLISH_MAPPER), clientData));
    }

    @Override
    public @NotNull Completable reauth() {
        return observeOn(new MqttReAuthCompletable(clientData));
    }

    @Override
    public @NotNull Completable disconnect(final @NotNull Mqtt5Disconnect disconnect) {
        final MqttDisconnect mqttDisconnect =
                MustNotBeImplementedUtil.checkNotImplemented(disconnect, MqttDisconnect.class);

        return observeOn(new MqttDisconnectCompletable(clientData, mqttDisconnect));
    }

    @Override
    public @NotNull MqttClientData getClientData() {
        return clientData;
    }

    private <T> @NotNull Flowable<T> observeOn(final @NotNull Flowable<T> flowable) {
        if (clientData.getExecutorConfig().getApplicationScheduler() != null) {
            return flowable.observeOn(clientData.getExecutorConfig().getApplicationScheduler());
        }
        return flowable;
    }

    private <T> @NotNull Single<T> observeOn(final @NotNull Single<T> single) {
        if (clientData.getExecutorConfig().getApplicationScheduler() != null) {
            return single.observeOn(clientData.getExecutorConfig().getApplicationScheduler());
        }
        return single;
    }

    private @NotNull Completable observeOn(final @NotNull Completable completable) {
        if (clientData.getExecutorConfig().getApplicationScheduler() != null) {
            return completable.observeOn(clientData.getExecutorConfig().getApplicationScheduler());
        }
        return completable;
    }

}
