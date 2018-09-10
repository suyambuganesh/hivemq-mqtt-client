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

package org.mqttbee.api.mqtt.mqtt5.message.subscribe.suback;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mqttbee.api.mqtt.mqtt5.message.Mqtt5ReasonCode;
import org.mqttbee.mqtt.message.MqttCommonReasonCode;

/**
 * MQTT Reason Codes that can be used in SUBACK packets according to the MQTT 5 specification.
 *
 * @author Silvio Giebl
 */
public enum Mqtt5SubAckReasonCode implements Mqtt5ReasonCode {

    GRANTED_QOS_0(0x00),
    GRANTED_QOS_1(0x01),
    GRANTED_QOS_2(0x02),
    UNSPECIFIED_ERROR(MqttCommonReasonCode.UNSPECIFIED_ERROR),
    IMPLEMENTATION_SPECIFIC_ERROR(MqttCommonReasonCode.IMPLEMENTATION_SPECIFIC_ERROR),
    NOT_AUTHORIZED(MqttCommonReasonCode.NOT_AUTHORIZED),
    TOPIC_FILTER_INVALID(MqttCommonReasonCode.TOPIC_FILTER_INVALID),
    PACKET_IDENTIFIER_IN_USE(MqttCommonReasonCode.PACKET_IDENTIFIER_IN_USE),
    QUOTA_EXCEEDED(MqttCommonReasonCode.QUOTA_EXCEEDED),
    SHARED_SUBSCRIPTION_NOT_SUPPORTED(MqttCommonReasonCode.SHARED_SUBSCRIPTION_NOT_SUPPORTED),
    SUBSCRIPTION_IDENTIFIERS_NOT_SUPPORTED(MqttCommonReasonCode.SUBSCRIPTION_IDENTIFIERS_NOT_SUPPORTED),
    WILDCARD_SUBSCRIPTION_NOT_SUPPORTED(MqttCommonReasonCode.WILDCARD_SUBSCRIPTION_NOT_SUPPORTED);

    private final int code;

    Mqtt5SubAckReasonCode(final int code) {
        this.code = code;
    }

    Mqtt5SubAckReasonCode(final @NotNull MqttCommonReasonCode reasonCode) {
        this(reasonCode.getCode());
    }

    /**
     * @return the byte code of this SUBACK Reason Code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns the SUBACK Reason Code belonging to the given byte code.
     *
     * @param code the byte code.
     * @return the SUBACK Reason Code belonging to the given byte code or null if the byte code is not a valid SUBACK
     *         Reason Code code.
     */
    public static @Nullable Mqtt5SubAckReasonCode fromCode(final int code) {
        for (final Mqtt5SubAckReasonCode reasonCode : values()) {
            if (reasonCode.code == code) {
                return reasonCode;
            }
        }
        return null;
    }

}
