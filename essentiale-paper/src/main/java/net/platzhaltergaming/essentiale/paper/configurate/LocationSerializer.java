package net.platzhaltergaming.essentiale.paper.configurate;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LocationSerializer implements TypeSerializer<Location> {

    public static LocationSerializer INSTANCE;

    @Getter(AccessLevel.PRIVATE)
    private final Plugin plugin;

    private ConfigurationNode nonVirtualNode(final ConfigurationNode source, final Object... path)
            throws SerializationException {
        if (!source.hasChild(path)) {
            throw new SerializationException("Required field " + Arrays.toString(path) + " was not present in node");
        }
        return source.node(path);
    }

    @Override
    public Location deserialize(final Type type, final ConfigurationNode source) throws SerializationException {
        World world;
        if (source.hasChild("world")) {
            world = getPlugin().getServer().getWorld(source.node("world").getString());
        } else {
            world = null;
        }

        double x = nonVirtualNode(source, "x").getDouble();
        double y = nonVirtualNode(source, "y").getDouble();
        double z = nonVirtualNode(source, "z").getDouble();
        // Optional fields
        float yaw = source.node("yaw").getFloat(0.0f);
        float pitch = source.node("pitch").getFloat(0.0f);

        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public void serialize(final Type type, final @Nullable Location location, final ConfigurationNode target)
            throws SerializationException {
        if (location == null) {
            target.raw(null);
            return;
        }

        if (location.getWorld() != null) {
            target.node("world").set(location.getWorld().getName());
        }
        target.node("x").set(location.getX());
        target.node("y").set(location.getY());
        target.node("z").set(location.getZ());
        target.node("yaw").set(location.getYaw());
        target.node("pitch").set(location.getPitch());
    }

}
