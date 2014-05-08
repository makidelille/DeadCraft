package mak.dc.canEffects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.world.World;

public class CanEffectFly extends CanEffect{

	public CanEffectFly(int effectId) {
		super(effectId, 10, "fly");
	}

	@Override
	public void applyEffect(World world, EntityPlayer player) {
		if(player.capabilities.allowFlying) return;
		player.capabilities.allowFlying = true;
		player.sendPlayerAbilities();
		
	}

	@Override
	public void removeEffect(World world, EntityPlayer player) {
		if(!player.capabilities.allowFlying) return;
		PlayerCapabilities newCap = player.capabilities;
		newCap.allowFlying = false;
		((EntityPlayerMP)player).playerNetServerHandler.processPlayerAbilities(new C13PacketPlayerAbilities(newCap));
		((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S39PacketPlayerAbilities(newCap));
		
	}

}
