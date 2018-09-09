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

package org.mqttbee.api.mqtt.mqtt5;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mqttbee.api.mqtt.*;
import org.mqttbee.api.mqtt.datatypes.MqttClientIdentifier;
import org.mqttbee.api.mqtt.mqtt3.Mqtt3ClientBuilder;
import org.mqttbee.api.mqtt.mqtt5.advanced.Mqtt5AdvancedClientData;
import org.mqttbee.api.mqtt.mqtt5.advanced.Mqtt5AdvancedClientDataBuilder;
import org.mqttbee.mqtt.MqttClientData;
import org.mqttbee.mqtt.MqttClientExecutorConfigImpl;
import org.mqttbee.mqtt.MqttVersion;
import org.mqttbee.mqtt.advanced.MqttAdvancedClientData;
import org.mqttbee.mqtt.datatypes.MqttClientIdentifierImpl;
import org.mqttbee.mqtt.mqtt5.Mqtt5ClientImpl;
import org.mqttbee.util.MustNotBeImplementedUtil;

/**
 * @author Silvio Giebl
 */
public class Mqtt5ClientBuilder extends MqttClientBuilder {

    private boolean followRedirects = false;
    private boolean allowServerReAuth = false;
    private @Nullable MqttAdvancedClientData advancedClientData;

    public Mqtt5ClientBuilder(
            final @NotNull MqttClientIdentifierImpl identifier, final @NotNull String serverHost, final int serverPort,
            final @Nullable MqttClientSslConfig sslConfig, final @Nullable MqttWebSocketConfig webSocketConfig,
            final @Nullable MqttClientExecutorConfigImpl executorConfig) {

        Preconditions.checkNotNull(identifier, "Identifier must not be null.");
        Preconditions.checkNotNull(serverHost, "Server host must not be null.");

        this.identifier = identifier;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.sslConfig = sslConfig;
        this.webSocketConfig = webSocketConfig;
        this.executorConfig = executorConfig;
    }

    @Override
    public @NotNull Mqtt5ClientBuilder identifier(final @NotNull String identifier) {
        super.identifier(identifier);
        return this;
    }

    @Override
    public @NotNull Mqtt5ClientBuilder identifier(final @NotNull MqttClientIdentifier identifier) {
        super.identifier(identifier);
        return this;
    }

    @Override
    public @NotNull Mqtt5ClientBuilder serverHost(final @NotNull String host) {
        super.serverHost(host);
        return this;
    }

    @Override
    public @NotNull Mqtt5ClientBuilder serverPort(final int port) {
        super.serverPort(port);
        return this;
    }

    @Override
    public @NotNull Mqtt5ClientBuilder useSslWithDefaultConfig() {
        super.useSslWithDefaultConfig();
        return this;
    }

    @Override
    public @NotNull Mqtt5ClientBuilder useSsl(final @NotNull MqttClientSslConfig sslConfig) {
        super.useSsl(sslConfig);
        return this;
    }

    @Override
    public @NotNull MqttClientSslConfigBuilder<? extends Mqtt5ClientBuilder> useSsl() {
        return new MqttClientSslConfigBuilder<>(this::useSsl);
    }

    @Override
    public @NotNull Mqtt5ClientBuilder useWebSocketWithDefaultConfig() {
        super.useWebSocketWithDefaultConfig();
        return this;
    }

    @Override
    public @NotNull Mqtt5ClientBuilder useWebSocket(final @NotNull MqttWebSocketConfig webSocketConfig) {
        super.useWebSocket(webSocketConfig);
        return this;
    }

    @Override
    public @NotNull MqttWebSocketConfigBuilder<? extends Mqtt5ClientBuilder> useWebSocket() {
        return new MqttWebSocketConfigBuilder<>(this::useWebSocket);
    }

    @Override
    public @NotNull Mqtt5ClientBuilder executorConfig(final @NotNull MqttClientExecutorConfig executorConfig) {
        super.executorConfig(executorConfig);
        return this;
    }

    @Override
    public @NotNull MqttClientExecutorConfigBuilder<? extends Mqtt5ClientBuilder> executorConfig() {
        return new MqttClientExecutorConfigBuilder<>(this::executorConfig);
    }

    @Override
    public @NotNull Mqtt3ClientBuilder useMqttVersion3() {
        throw new UnsupportedOperationException(
                "Switching MQTT Version is not allowed. Please call useMqttVersion3/5 only once.");
    }

    @Override
    public @NotNull Mqtt5ClientBuilder useMqttVersion5() {
        return this;
    }

    public @NotNull Mqtt5ClientBuilder followRedirects(final boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    public @NotNull Mqtt5ClientBuilder allowServerReAuth(final boolean allowServerReAuth) {
        this.allowServerReAuth = allowServerReAuth;
        return this;
    }

    public @NotNull Mqtt5ClientBuilder advanced(final @Nullable Mqtt5AdvancedClientData advancedClientData) {
        this.advancedClientData =
                MustNotBeImplementedUtil.checkNullOrNotImplemented(advancedClientData, MqttAdvancedClientData.class);
        return this;
    }

    public @NotNull Mqtt5AdvancedClientDataBuilder<? extends Mqtt5ClientBuilder> advanced() {
        return new Mqtt5AdvancedClientDataBuilder<>(this::advanced);
    }

    public @NotNull Mqtt5Client buildReactive() {
        return new Mqtt5ClientImpl(buildClientData());
    }

    private @NotNull MqttClientData buildClientData() {
        return new MqttClientData(MqttVersion.MQTT_5_0, identifier, serverHost, serverPort, sslConfig, webSocketConfig,
                followRedirects, allowServerReAuth, MqttClientExecutorConfigImpl.orDefault(executorConfig),
                advancedClientData);
    }

}
