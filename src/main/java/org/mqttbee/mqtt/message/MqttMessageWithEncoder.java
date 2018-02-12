package org.mqttbee.mqtt.message;

import org.mqttbee.annotations.NotNull;
import org.mqttbee.annotations.Nullable;
import org.mqttbee.mqtt.codec.encoder.MqttMessageEncoder;
import org.mqttbee.mqtt.codec.encoder.provider.MqttMessageEncoderApplier;
import org.mqttbee.mqtt.codec.encoder.provider.MqttMessageEncoderProvider;

/**
 * MQTT message with an {@link MqttMessageEncoderApplier} this MQTT message is applied to for encoding.
 *
 * @param <M> the type of the MQTT message.
 * @param <P> the type of the encoder provider for the MQTT message.
 * @author Silvio Giebl
 */
public abstract class MqttMessageWithEncoder<M extends MqttMessageWithEncoder<M, P>, P extends MqttMessageEncoderProvider<M>>
        implements MqttMessage {

    protected final P encoderProvider;
    private MqttMessageEncoderApplier<M> encoderApplier;

    MqttMessageWithEncoder(@Nullable final P encoderProvider) {
        this.encoderProvider = encoderProvider;
    }

    @NotNull
    @Override
    public MqttMessageEncoder getEncoder() {
        if (encoderApplier == null) {
            if (encoderProvider == null) {
                throw new UnsupportedOperationException();
            }
            encoderApplier = encoderProvider.get();
        }
        return encoderApplier.apply(getCodable());
    }

    /**
     * @return the codable MQTT message.
     */
    @NotNull
    protected abstract M getCodable();

}