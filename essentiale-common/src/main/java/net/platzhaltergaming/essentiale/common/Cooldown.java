package net.platzhaltergaming.essentiale.common;

import java.util.concurrent.ConcurrentHashMap;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Cooldown<T> {

    private ConcurrentHashMap<T, Long> entries = new ConcurrentHashMap<T, Long>();

    private final long cooldown;

    public long check(T key) {
        if (entries.containsKey(key)) {
            long milliSecondsLeft = (entries.get(key) + this.cooldown) - System.currentTimeMillis();
            if (milliSecondsLeft > 0) {
                return milliSecondsLeft / 1000;
            }
        }

        this.remove(key);
        return 0;
    }

    public boolean has(T key) {
        return (check(key) > 0);
    }

    public void add(T key) {
        entries.put(key, System.currentTimeMillis());
    }

    public void remove(T key) {
        entries.remove(key);
    }

}
