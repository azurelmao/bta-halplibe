package turniplabs.halplibe.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.handler.NetHandler;
import org.apache.commons.lang3.reflect.FieldUtils;

public final class PlayerUtils {

	public static EntityPlayer getPlayer(NetHandler handler) {
		if(handler.isServerHandler()) {
			try {
				return (EntityPlayer) FieldUtils.readField(handler, "playerEntity", true);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		return Minecraft.getMinecraft(PlayerUtils.class).thePlayer;
	}

}
