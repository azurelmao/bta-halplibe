package turniplabs.halplibe.helper.gui.packet;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketOpenItemGui extends PacketOpenGui {

    public ItemStack itemStack;

    public PacketOpenItemGui(@NotNull RegisteredGui gui, int windowId, @NotNull ItemStack itemStack) {
        super(gui, windowId);
        this.itemStack = itemStack;
    }

    public PacketOpenItemGui() {
    }


    @Override
    public void readPacketData(DataInputStream input) throws IOException {
        super.readPacketData(input);
        this.itemStack = ItemStack.readItemStackFromNbt(readCompressedCompoundTag(input));
    }

    @Override
    public void writePacketData(DataOutputStream output) throws IOException {
        if(itemStack == null) throw new IOException("Itemstack must be set!");
        super.writePacketData(output);

        writeCompressedCompoundTag(itemStack.writeToNBT(new CompoundTag()), output);
    }

    @Override
    public int getPacketSize() {
        return super.getPacketSize() + 3 * 4;
    }
}
