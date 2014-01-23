package mak.dc.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mak.dc.client.gui.container.ContainerDeadCraft;
import mak.dc.client.gui.container.ContainerEggSpawner;
import mak.dc.lib.Lib;
import mak.dc.tileEntities.TileEntityDeadCraft;
import mak.dc.tileEntities.TileEntityEggSpawner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

    @Override
    public void onPacketData (INetworkManager manager, Packet250CustomPayload packet, Player player) {
        ByteArrayDataInput reader = ByteStreams.newDataInput(packet.data);

        EntityPlayer entityPlayer = (EntityPlayer) player;
        Container container = entityPlayer.openContainer;
        ;
        byte packetId = reader.readByte();
        byte buttonId;

        switch (packetId) {
            case 0:
                // int itemId = reader.readInt();
                // int val = reader.readInt();

                // TODO some code here

                break;
            case 1: // interfaces
                byte interfaceId = reader.readByte();
                byte typeSent = reader.readByte();
                switch (interfaceId) {
                    case 0: //deadCraftMainInterface
                        if (container != null && container instanceof ContainerDeadCraft) {
                            TileEntityDeadCraft teDc = ((ContainerDeadCraft)container).getTileEntity();
                            switch(typeSent) {
                                case 0 : //buttons
                                    break;
                                case 1 : //slider
                                    break;
                                case 2 : //switch
                                    byte switchId = reader.readByte();
                                    switch(switchId) {
                                        case 0:
                                            teDc.setLocked(reader.readBoolean());
                                            break;
                                    }

                                    break;
                            }
                        }

                        break;
                    case 1: // eggspawner interface
                        switch (typeSent) {
                            case 0: //buttons
                                buttonId = reader.readByte();
                                if (container != null && container instanceof ContainerEggSpawner) {
                                    TileEntityEggSpawner te = ((ContainerEggSpawner) container).getTileEntity();
                                    te.receiveInterfaceEvent(buttonId);
                                }
                                break;
                        }
                        break;

                }
                break;
        }


    }

    public void sendItemPacket (int itemId, int val) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            dataStream.writeByte(0); // Items are 0
            dataStream.writeByte(itemId);
            dataStream.writeInt(val);

            PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(Lib.MOD_ID, byteStream.toByteArray()));
        }
        catch (IOException ex) {
            System.err.append("failed to send item packet " + itemId + " with value " + val);

        }
    }

    public static void sendInterfaceButtonPacket (byte interfaceId, byte buttonId) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            dataStream.writeByte(1); // Interfaces are 1
            dataStream.writeByte(interfaceId);
            dataStream.writeByte(0); // buttons are 0
            dataStream.writeByte(buttonId);

            PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(Lib.MOD_ID, byteStream.toByteArray()));
        }
        catch (IOException ex) {
            System.out.println("failed to send interface packet from interface " + interfaceId + " from button "
                    + buttonId);
        }
    }

    public static void sendInterfaceSliderPacket (byte interfaceId, byte sliderId, int cursorPos) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            dataStream.writeByte(1); // Interfaces are 1
            dataStream.writeByte(interfaceId);
            dataStream.writeByte(1); // Sliders are 1
            dataStream.writeByte(sliderId);
            dataStream.writeInt(cursorPos);

            PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(Lib.MOD_ID, byteStream.toByteArray()));
        }
        catch (IOException ex) {
            System.out.println("failed to send interface packet from interface " + interfaceId + " from slider " + sliderId
                    + "with pos :" + cursorPos);
        }

    }

    public static void sendInterfaceSwitchPacket (byte interfaceId, byte switchId, boolean state) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try {
            dataStream.writeByte(1); // Interfaces are 1
            dataStream.writeByte(interfaceId);
            dataStream.writeByte(2); // Switches are 1
            dataStream.writeByte(switchId);
            dataStream.writeBoolean(state);

            PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(Lib.MOD_ID, byteStream.toByteArray()));
        }
        catch (IOException ex) {
            System.out.println("failed to send interface packet from interface " + interfaceId + " from switchr " + switchId
                    + "with state : " + state);
        }

    }

}
