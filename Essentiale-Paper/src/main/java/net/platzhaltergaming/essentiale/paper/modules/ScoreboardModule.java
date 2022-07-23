package net.platzhaltergaming.essentiale.paper.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.platzhaltergaming.essentiale.paper.PlaceholderResolver;
import net.platzhaltergaming.essentiale.paper.settings.features.ScoreboardProps;

@Getter
@RequiredArgsConstructor
public class ScoreboardModule implements Listener {

    private final JavaPlugin plugin;
    private final ScoreboardProps settings;
    private final Map<UUID, FastBoard> boards = new HashMap<>();

    private int refreshTime;
    private String title;
    private List<String> lines = new ArrayList<String>();

    private int taskId;

    public void onEnable() {
        if (!getSettings().isEnabled()) {
            getPlugin().getLogger().info("Scoreboard Module is disabled!");
            return;
        }

        this.refreshTime = getSettings().getRefreshSeconds();
        this.title = getSettings().getTitle().replace('&', ChatColor.COLOR_CHAR);
        this.lines = getSettings().getLines().stream().map(l -> l.replace('&', ChatColor.COLOR_CHAR))
                .collect(Collectors.toList());

        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());

        taskId = getPlugin().getServer().getScheduler().runTaskTimer(getPlugin(), () -> {
            for (FastBoard board : this.boards.values()) {
                updateBoard(board);
            }
        }, 0, this.refreshTime * 20).getTaskId();

        getPlugin().getLogger().info("Enabled Scoreboard Module");
    }

    public void onDisable() {
        getPlugin().getServer().getScheduler().cancelTask(taskId);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        FastBoard board = new FastBoard(player);
        board.updateTitle(this.title);

        this.boards.put(player.getUniqueId(), board);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        FastBoard board = this.boards.remove(player.getUniqueId());

        if (board != null) {
            board.delete();
        }
    }

    protected void updateBoard(FastBoard board) {
        ArrayList<String> lines = new ArrayList<>();
        for (String line : this.lines) {
            lines.add(PlaceholderResolver.apply(board.getPlayer(), line));
        }

        board.updateLines(lines);
    }

}
