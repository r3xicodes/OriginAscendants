package org.bukkit.scheduler;

public interface BukkitScheduler {
    void runTaskTimer(Object plugin, Runnable task, long delay, long period);
}
