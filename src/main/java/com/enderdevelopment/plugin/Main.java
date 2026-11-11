package com.enderdevelopment.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public final class Main extends JavaPlugin {
    private static final long DAY_MILLIS = TimeUnit.DAYS.toMillis(1);
    private static final int EVENT_DAYS = 7;
    private static final String BUILD_TAG = "seven_day_omen_structure";
    private long startMillis;
    private int renderedStage = -1;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        startMillis = getConfig().getLong("start-time", 0L);
        if (startMillis <= 0L) {
            startMillis = System.currentTimeMillis();
            getConfig().set("start-time", startMillis);
            saveConfig();
        }
        refreshStructure();
        Bukkit.getScheduler().runTaskTimer(this, this::refreshStructure, 20L, 1200L);
        getLogger().info("The seven-day omen is active.");
    }

    private int getStage() {
        long elapsed = Math.max(0L, System.currentTimeMillis() - startMillis);
        return Math.min(EVENT_DAYS, (int) (elapsed / DAY_MILLIS) + 1);
    }

    public long getRemainingMillis() {
        return Math.max(0L, startMillis + ((long) EVENT_DAYS * DAY_MILLIS) - System.currentTimeMillis());
    }

    public int getCurrentStage() {
        return getStage();
    }

    private void refreshStructure() {
        World world = Bukkit.getWorlds().isEmpty() ? null : Bukkit.getWorlds().get(0);
        if (world == null) {
            return;
        }
        int stage = getStage();
        if (stage == renderedStage) {
            return;
        }
        removeStructure();
        Location origin = world.getSpawnLocation().getBlock().getLocation().add(0.5, 2.0, 0.5);
        buildStage(origin, stage);
        renderedStage = stage;
    }

    private void buildStage(Location origin, int stage) {
        List<BlockShape> shapes = new ArrayList<>();
        shapes.add(new BlockShape(0, 0, 0, 3.0f, 0.7f, 3.0f, 0.0f));
        shapes.add(new BlockShape(-2.0f, 0.7f, -1.5f, 0.8f, 4.0f, 0.8f, 0.12f));
        shapes.add(new BlockShape(1.4f, 0.7f, -1.2f, 0.8f, 4.8f, 0.8f, -0.1f));
        shapes.add(new BlockShape(-1.8f, 0.7f, 1.0f, 0.8f, 3.4f, 0.8f, -0.2f));
        shapes.add(new BlockShape(1.8f, 0.7f, 1.1f, 0.8f, 3.8f, 0.8f, 0.16f));
        if (stage >= 2) {
            shapes.add(new BlockShape(-3.2f, 2.4f, -0.5f, 0.7f, 5.0f, 0.7f, 0.3f));
            shapes.add(new BlockShape(2.8f, 2.1f, 0.4f, 0.7f, 5.5f, 0.7f, -0.28f));
        }
        if (stage >= 3) {
            shapes.add(new BlockShape(-1.0f, 3.7f, -1.2f, 0.7f, 0.7f, 5.0f, 0.55f));
            shapes.add(new BlockShape(1.0f, 4.0f, 0.8f, 0.7f, 0.7f, 5.3f, -0.48f));
        }
        if (stage >= 4) {
            shapes.add(new BlockShape(-2.0f, 6.0f, -0.2f, 4.2f, 0.7f, 0.7f, 0.2f));
            shapes.add(new BlockShape(0.4f, 6.8f, 0.2f, 0.7f, 0.7f, 4.5f, -0.2f));
        }
        if (stage >= 5) {
            shapes.add(new BlockShape(0.0f, 7.7f, 0.0f, 1.4f, 3.2f, 1.4f, 0.0f));
            shapes.add(new BlockShape(-3.0f, 5.8f, 0.0f, 0.7f, 0.7f, 4.5f, 0.7f));
            shapes.add(new BlockShape(2.6f, 5.9f, 0.0f, 0.7f, 0.7f, 4.8f, -0.65f));
        }
        if (stage >= 6) {
            shapes.add(new BlockShape(0.0f, 9.5f, 0.0f, 0.7f, 4.0f, 0.7f, 0.0f));
            shapes.add(new BlockShape(-1.0f, 8.5f, 0.0f, 3.5f, 0.6f, 0.6f, 0.35f));
            shapes.add(new BlockShape(1.0f, 8.5f, 0.0f, 3.5f, 0.6f, 0.6f, -0.35f));
        }
        if (stage >= 7) {
            shapes.add(new BlockShape(0.0f, 11.5f, 0.0f, 2.2f, 0.7f, 2.2f, 0.0f));
            shapes.add(new BlockShape(0.0f, 13.0f, 0.0f, 0.7f, 2.5f, 0.7f, 0.0f));
        }
        for (BlockShape shape : shapes) {
            createDisplay(origin, shape);
        }
    }

    private void createDisplay(Location origin, BlockShape shape) {
        Location location = origin.clone().add(shape.x, shape.y, shape.z);
        BlockDisplay display = origin.getWorld().spawn(location, BlockDisplay.class);
        display.setBlock(Material.OBSIDIAN.createBlockData());
        display.setBrightness(new Display.Brightness(5, 5));
        display.setTransformation(new Transformation(new Vector3f(), new AxisAngle4f(shape.angle, 0, 1, 0),
                new Vector3f(shape.width, shape.height, shape.depth), new AxisAngle4f()));
        display.addScoreboardTag(BUILD_TAG);
        display.setPersistent(false);
    }

    private void removeStructure() {
        for (World world : Bukkit.getWorlds()) {
            for (BlockDisplay display : world.getEntitiesByClass(BlockDisplay.class)) {
                if (display.getScoreboardTags().contains(BUILD_TAG)) {
                    display.remove();
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("omen")) {
            return false;
        }
        long remaining = getRemainingMillis();
        long days = TimeUnit.MILLISECONDS.toDays(remaining);
        long hours = TimeUnit.MILLISECONDS.toHours(remaining) % 24;
        sender.sendMessage(Component.text("The obsidian omen is in day " + getCurrentStage() + " of 7.")
                .color(NamedTextColor.DARK_PURPLE));
        sender.sendMessage(Component.text("Time until the event: " + days + "d " + hours + "h")
                .color(NamedTextColor.GRAY));
        return true;
    }

    private record BlockShape(float x, float y, float z, float width, float height, float depth, float angle) {
    }
}
