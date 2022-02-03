package net.platzhaltergaming.essentiale.paper.modules;

import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.platzhaltergaming.essentiale.paper.PlaceholderResolver;
import net.platzhaltergaming.essentiale.paper.settings.features.NameTagProps;

@Getter
@RequiredArgsConstructor
public class NameTagModule implements Listener {

    private final JavaPlugin plugin;
    private final LuckPerms luckPerms;
    private final NameTagProps settings;

    private final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.legacyAmpersand();

    private int refreshSeconds;
    private int maxWeight = 10000;
    private int fillDigits = 4;
    private String namePrefixFormat;

    private Scoreboard scoreboard;
    private int taskId = -1;

    public void onEnable() {
        if (!getSettings().isEnabled()) {
            getPlugin().getLogger().info("NameTag Module is disabled!");
            return;
        }

        this.refreshSeconds = getSettings().getRefreshSeconds();
        this.maxWeight = getSettings().getMaxWeight();
        // We don't need +1 here because we except the fill digits to be one less than
        // the maxWeight
        this.fillDigits = (int) Math.log10(this.maxWeight);
        this.namePrefixFormat = getSettings().getPrefix();

        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        taskId = getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(getPlugin(), () -> {
            getPlugin().getServer().getOnlinePlayers().forEach((player) -> updatePlayerPrefix(player));
        }, 7 * 20, this.refreshSeconds * 20);

        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    public void onDisable() {
        if (taskId != -1) {
            getPlugin().getServer().getScheduler().cancelTask(taskId);
        }

        // Remove every teams + clear scoreboard
        for (Team team : this.scoreboard.getTeams()) {
            team.unregister();
        }
        this.scoreboard = null;
    }

    protected Team getTeamForPlayer(Player player) {
        User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
        Group group = luckPerms.getGroupManager().getGroup(user.getPrimaryGroup());
        int weight = group.getWeight().isPresent() ? group.getWeight().getAsInt() : 0;

        // https://stackoverflow.com/questions/275711/add-leading-zeroes-to-number-in-java#comment38985013_275715
        String teamName = String.format(Locale.US, "0%0" + this.fillDigits + "d%s%s", (maxWeight - weight),
                group.getName(), player.getName());
        Team team = scoreboard.getPlayerTeam(player);

        if (team == null) {
            // Does a team exist but not with the player in it?
            // Remove it and return a new team
            team = scoreboard.getTeam(teamName);
            if (team != null) {
                team.unregister();
            }

            return scoreboard.registerNewTeam(teamName);
        }
        if (team.getName().equals(teamName)) {
            return team;
        }

        // If team name does not match (anymore), unregister it and return a new one
        team.unregister();
        return scoreboard.registerNewTeam(teamName);
    }

    protected void removeTeamFromPlayer(Player player) {
        Team team = getTeamForPlayer(player);
        team.removePlayer(player);
        team.unregister();
    }

    protected void updatePlayerPrefix(Player player) {
        User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
        String prefix = user.getCachedData().getMetaData().getPrefix();

        if (prefix != null) {
            Team team = getTeamForPlayer(player);
            String namePrefix = PlaceholderResolver.apply(player, namePrefixFormat.replace("%prefix%", prefix));
            String currentPrefix = this.legacyComponentSerializer.serialize(team.prefix());
            if (currentPrefix.equals(namePrefix)) {
                return;
            }

            team.prefix(this.legacyComponentSerializer.deserialize(namePrefix));

            if (!team.hasPlayer(player)) {
                team.addPlayer(player);
            }
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Reset user display name
        player.displayName(player.name());

        getPlugin().getServer().getScheduler().runTaskLater(getPlugin(), () -> {
            updatePlayerPrefix(player);
            player.setScoreboard(this.scoreboard);
        }, 2);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        removeTeamFromPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        getTeamForPlayer(event.getPlayer()).setOption(Option.NAME_TAG_VISIBILITY,
                event.isSneaking() ? OptionStatus.NEVER : OptionStatus.ALWAYS);
    }

    protected String convertIntToString(int weight) {
        String out = "";
        String number = Integer.toString(weight);

        for (int i = 0; i < number.length(); i++) {
            int charInt = Integer.parseInt(String.valueOf(number.charAt(i)));
            // 65 is A (printable "start" of the ASCII table; 90 is Z)
            int digit = 65 + charInt;
            out += (char) digit;
        }

        return out;
    }

}
