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

package org.mqttbee.mqtt.codec.decoder;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mqttbee.mqtt.datatypes.MqttTopicImpl;
import org.mqttbee.mqtt.message.ping.MqttPingResp;
import org.mqttbee.util.collections.IntMap;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.EnumSet;

import static org.mqttbee.mqtt.codec.decoder.MqttMessageDecoderUtil.checkFixedHeaderFlags;
import static org.mqttbee.mqtt.codec.decoder.MqttMessageDecoderUtil.checkRemainingLength;
import static org.mqttbee.mqtt.message.ping.MqttPingResp.INSTANCE;

/**
 * @author Silvio Giebl
 */
@Singleton
public class MqttPingRespDecoder implements MqttMessageDecoder {

    private static final int FLAGS = 0b0000;
    private static final int REMAINING_LENGTH = 0;

    @Inject
    MqttPingRespDecoder() {
    }

    @Override
    public @NotNull MqttPingResp decode(
            final int flags, final @NotNull ByteBuf in, final @NotNull EnumSet<MqttDecoderFlag> decoderFlags,
            final @Nullable IntMap<MqttTopicImpl> topicAliasMapping) throws MqttDecoderException {

        checkFixedHeaderFlags(FLAGS, flags);
        checkRemainingLength(REMAINING_LENGTH, in.readableBytes());

        return INSTANCE;
    }

}
