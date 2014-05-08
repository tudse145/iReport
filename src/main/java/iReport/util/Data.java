package iReport.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class Data implements Serializable {
    private static final long serialVersionUID = 8569734081216879910L;
    public HashMap<UUID, String> playermap;
    public HashMap<UUID, String> playermapo;
    public HashMap<String, UUID> playermapor;
    public HashMap<UUID, String> playermapr;
    public static Data instens;

    public Data() {
        playermap = new HashMap<UUID, String>();
        playermapo = new HashMap<UUID, String>();
        playermapr = new HashMap<UUID, String>();
        playermapor = new HashMap<String, UUID>();
    }

    public static Data init() {
        if (instens == null) {
            instens = new Data();
        }
        return instens;
    }
}
