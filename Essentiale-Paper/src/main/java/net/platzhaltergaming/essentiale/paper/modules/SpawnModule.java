package net.platzhaltergaming.essentiale.paper.modules;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.platzhaltergaming.essentiale.paper.settings.features.SpawnProps;

@Getter
@RequiredArgsConstructor
public class SpawnModule implements Listener {

    private final JavaPlugin plugin;
    private final SpawnProps settings;

    private boolean spawnOnJoin;
    private boolean spawnOnRespawn;
    private String spawnWorld;
    private Location spawnLocation;

    public void onEnable() {
        if (!getSettings().isEnabled()) {
            getPlugin().getLogger().info("Spawn Module is disabled!");
            return;
        }

        this.spawnLocation = getSettings().getLocation();

        this.spawnOnJoin = getSettings().isOnJoin();
        this.spawnOnRespawn = getSettings().isOnRespawn();

        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        if (!this.spawnOnJoin) {
            return;
        }
        getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> {
            teleportToSpawn(e.getPlayer());
        }, 1L);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        if (!this.spawnOnJoin) {
            return;
        }
        teleportToSpawn(e.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent e) {
        if (!this.spawnOnRespawn) {
            return;
        }
        e.setRespawnLocation(this.spawnLocation);
    }

    public void teleportToSpawn(Player player) {
        teleportToSpawn(player, TeleportCause.PLUGIN);
    }

    public void teleportToSpawn(Player player, TeleportCause cause) {
        // Set player velocity to 0
        player.setVelocity(new Vector(0.0, 0.0, 0.0));

        // Teleport player to spawn location
        player.teleport(this.spawnLocation, cause);
    }

}
