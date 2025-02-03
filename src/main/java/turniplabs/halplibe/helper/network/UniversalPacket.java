package turniplabs.halplibe.helper.network;

import com.mojang.nbt.NbtIo;
import com.mojang.nbt.tags.CompoundTag;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.net.handler.PacketHandler;
import net.minecraft.core.net.packet.Packet;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.mixin.accessors.PacketHandlerServerAccessor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * UniversalPacket is a general purpose packet made to transport multiple message into the same PacketType
 * This work similar as DataInput/DataOutput, netty ByteBuf or modern Minecraft PacketByteBuf
 */
public class UniversalPacket extends Packet {
    private byte[] buffer;
    private int writeIndex;
    private int readIndex;

    public UniversalPacket() {
        this.buffer = new byte[0];
        this.writeIndex = 0;
        this.readIndex = 0;
    }

    @Deprecated
    public void read(DataInputStream dis) throws IOException {
        final int length = dis.readInt();
        buffer = new byte[length];
        writeIndex = dis.read(buffer, 0, length);
    }

    /**
     * If you want to write the UniversalPacket content to a DataOutputStream, use rawWrite instead
     * since this method add an extra 4 bytes to every packet
     */
    @Deprecated
    public void write(DataOutputStream dos) throws IOException {
        dos.writeInt(this.buffer.length);
        dos.write(this.buffer);
    }

    @SuppressWarnings("unused")
    public void rawWrite(DataOutputStream dos) throws IOException {
        dos.write(this.buffer);
    }

    public void handlePacket(PacketHandler packetHandler) {
        if (EnvironmentHelper.isServerEnvironment()) {
            handlePacketServer(packetHandler);
            return;
        }
        handlePacketClient();
    }

    @Environment(EnvType.SERVER)
    private void handlePacketServer(PacketHandler packetHandler) {
        NetworkHandler.receiveUniversalPacket(new NetworkMessage.NetworkContext((
                (PacketHandlerServerAccessor)packetHandler).getPlayerEntity()
        ), this);
    }

    @Environment(EnvType.CLIENT)
    private void handlePacketClient() {
        NetworkHandler.receiveUniversalPacket(new NetworkMessage.NetworkContext(
                Minecraft.getMinecraft().thePlayer
        ), this);
    }

    public int getEstimatedSize() {
        return buffer.length;
    }

    @SuppressWarnings("unused")
    public void writeByte(byte value) {
        ensureCapacity(1);
        buffer[writeIndex++] = value;
    }

    @SuppressWarnings("unused")
    public void writeByte(int value) {
        writeByte((byte) value);
    }

    @SuppressWarnings("unused")
    public byte readByte() {
        ensureReadable(1);
        return buffer[readIndex++];
    }

    @SuppressWarnings("unused")
    public void writeBytes(int... values) {
        ensureCapacity(values.length);
        for (int value : values) {
            buffer[writeIndex++] = (byte) value;
        }
    }

    @SuppressWarnings("unused")
    public void writeBytes(byte... values) {
        ensureCapacity(values.length);
        for (int value : values) {
            buffer[writeIndex++] = (byte) value;
        }
    }

    @SuppressWarnings("unused")
    public void readBytes(byte[] destination, int length) {
        if (length > destination.length) {
            throw new IllegalArgumentException("");
        }
        ensureReadable(length);
        System.arraycopy(buffer, readIndex, destination, 0, length);
        readIndex += length;
    }

    @SuppressWarnings("unused")
    public void writeInt(int value) {
        ensureCapacity(4);
        buffer[writeIndex++] = (byte) (value >> 24);
        buffer[writeIndex++] = (byte) (value >> 16);
        buffer[writeIndex++] = (byte) (value >> 8);
        buffer[writeIndex++] = (byte) value;
    }

    @SuppressWarnings("unused")
    public int readInt() {
        ensureReadable(4);
        return ((buffer[readIndex++] & 0xFF) << 24) |
                ((buffer[readIndex++] & 0xFF) << 16) |
                ((buffer[readIndex++] & 0xFF) << 8) |
                (buffer[readIndex++] & 0xFF);
    }

    @SuppressWarnings("unused")
    public void writeShort(short value) {
        ensureCapacity(2);
        buffer[writeIndex++] = (byte) (value >> 8);
        buffer[writeIndex++] = (byte) value;
    }

    @SuppressWarnings("unused")
    public short readShort() {
        ensureReadable(2);
        return (short) (((buffer[readIndex++] & 0xFF) << 8) |
                (buffer[readIndex++] & 0xFF));
    }

    @SuppressWarnings("unused")
    public void writeString(String value) {
        byte[] stringBytes = value.getBytes(StandardCharsets.UTF_8);
        writeInt(stringBytes.length);
        ensureCapacity(stringBytes.length);
        System.arraycopy(stringBytes, 0, buffer, writeIndex, stringBytes.length);
        writeIndex += stringBytes.length;
    }

    @SuppressWarnings("unused")
    public String readString() {
        int length = readInt();
        ensureReadable(length);
        String value = new String(buffer, readIndex, length, StandardCharsets.UTF_8);
        readIndex += length;
        return value;
    }

    @SuppressWarnings("unused")
    public void writeBoolean(boolean value) {
        ensureCapacity(1);
        buffer[writeIndex++] = (byte) (value ? 1 : 0);
    }

    @SuppressWarnings("unused")
    public boolean readBoolean() {
        ensureReadable(1);
        return buffer[readIndex++] != 0;
    }

    @SuppressWarnings("unused")
    public void writeDouble(double value) {
        long bits = Double.doubleToLongBits(value);
        writeLong(bits);
    }

    @SuppressWarnings("unused")
    public double readDouble() {
        long bits = readLong();
        return Double.longBitsToDouble(bits);
    }

    @SuppressWarnings("unused")
    public void writeLong(long value) {
        ensureCapacity(8);
        for (int i = 7; i >= 0; i--) {
            buffer[writeIndex++] = (byte) (value >> (i * 8));
        }
    }

    @SuppressWarnings("unused")
    public long readLong() {
        ensureReadable(8);
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value = (value << 8) | (buffer[readIndex++] & 0xFF);
        }
        return value;
    }

    @SuppressWarnings("unused")
    public void writeEnumConstant(Enum<?> instance) {
        int ordinal = instance.ordinal();
        this.writeByte(ordinal);
    }

    @SuppressWarnings("unused")
    public <T extends Enum<T>> T readEnumConstant(Class<T> enumClass) {
        int ordinal = this.readByte();
        T[] enumConstants = enumClass.getEnumConstants();
        return enumConstants[ordinal];
    }

    @SuppressWarnings("unused")
    public void writeCompoundTag(CompoundTag tag) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            NbtIo.writeCompressed(tag, baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] buffer = baos.toByteArray();
        writeShort((short)buffer.length);
        writeBytes(buffer);
    }

    @SuppressWarnings("unused")
    public CompoundTag readCompoundTag() {
        int length = Short.toUnsignedInt(readShort());
        if (length == 0) {
            return null;
        } else {
            byte[] data = new byte[length];
            readBytes(data, length);
            try {
                return NbtIo.readCompressed(new ByteArrayInputStream(data));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("unused")
    public InputStream readBytesAsStream(int length) {
        ensureReadable(length);
        return new InputStream() {
            private int remaining = length;

            @Override
            public int read() {
                if (remaining <= 0) {
                    return -1;
                }
                remaining--;
                return buffer[readIndex++] & 0xFF;
            }

            @Override
            public int read(byte @NotNull [] b, int off, int len) {
                if (remaining <= 0) {
                    return -1;
                }
                int toRead = Math.min(len, remaining);
                System.arraycopy(buffer, readIndex, b, off, toRead);
                readIndex += toRead;
                remaining -= toRead;
                return toRead;
            }
        };
    }

    private void ensureCapacity(int length) {
        if (writeIndex + length > buffer.length) {
            buffer = Arrays.copyOf(buffer, buffer.length + length + 64);
        }
    }

    private void ensureReadable(int length) {
        if (readIndex + length > writeIndex) {
            throw new IndexOutOfBoundsException("Not enough data to read.");
        }
    }
}