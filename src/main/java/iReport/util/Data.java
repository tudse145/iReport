package iReport.util;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class Data implements Serializable {
    private static final long serialVersionUID = 8569734081216879910L;
    public ConcurrentMap<UUID, String> playermap;
    public ConcurrentMap<UUID, String> playermapo;
    public ConcurrentMap<String, UUID> playermapor;
    public ConcurrentMap<UUID, String> playermapr;
    public static Data instens;

    public Data() {
        playermap = new ConcurrentHashMap<>();
        playermapo = new ConcurrentHashMap<>();
        playermapr = new ConcurrentHashMap<>();
        playermapor = new ConcurrentHashMap<>();
    }

    public static Data init() {
        if (instens == null) {
            instens = new Data();
        }
        return instens;
    }
}
