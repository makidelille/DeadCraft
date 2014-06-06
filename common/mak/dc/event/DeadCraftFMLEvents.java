package mak.dc.event;

import mak.dc.DeadCraft;
import mak.dc.network.packet.DeadCraftForceSyncPakcet;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class DeadCraftFMLEvents {

	@SubscribeEvent
	public void onJoin(PlayerEvent.PlayerLoggedInEvent e) {
		DeadCraft.packetPipeline.sendToServer(new DeadCraftForceSyncPakcet(e.player));
		System.out.println("test");
	}
	
}
