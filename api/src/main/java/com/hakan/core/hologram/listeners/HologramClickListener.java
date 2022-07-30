package com.hakan.core.hologram.listeners;

import com.hakan.core.HCore;
import com.hakan.core.hologram.HHologram;
import com.hakan.core.hologram.HHologramHandler;
import com.hakan.core.packet.event.PacketEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class HologramClickListener implements Listener {

    @EventHandler
    public void onPacketEvent(PacketEvent event) {
        Player player = event.getPlayer();
        Object packet = event.getPacket();

        if (!packet.getClass().getName().contains("PacketPlayInUseEntity"))
            return;

        HCore.asyncScheduler().run(() -> {
            for (HHologram hologram : HHologramHandler.getValues()) {
                if (hologram.hasLineByEntityID(event.getValue("a"))) {
                    Location playerLocation = player.getEyeLocation();
                    Location hologramLocation = hologram.getLocation();

                    double xP = playerLocation.getX();
                    double zP = playerLocation.getZ();
                    double xH = hologramLocation.getX();
                    double zH = hologramLocation.getZ();
                    double distance = Math.sqrt(Math.pow(xP - xH, 2) + Math.pow(zP - zH, 2));

                    float pitch = -playerLocation.getPitch();

                    double y1 = (hologram.getLineDistance() / 2) * (hologram.getLines().size() + 2) + hologramLocation.getY();
                    double y2 = distance * Math.tan(Math.toRadians(pitch)) + playerLocation.getY();

                    int index = (int) Math.ceil((y1 - y2) / 0.24);

                    if (index < 0 || index >= hologram.getLines().size())
                        break;

                    hologram.getAction().onClick(event.getPlayer(), hologram.getLine(index));
                    return;
                }
            }
        });
    }
}