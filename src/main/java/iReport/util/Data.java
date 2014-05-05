package iReport.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Data implements Serializable {
    private static final long serialVersionUID = 8569734081216879919L;
    public HashMap<UUID, String> playermap;
    public HashMap<UUID, String> playermapo;
    public HashMap<UUID, String> playermapr;
    public static Data instens;
    public List<String> playerlistu;
    public List<String> playerlistn;

    public Data() {
        playermap = new HashMap<UUID, String>();
        playermapo = new HashMap<UUID, String>();
        playermapr = new HashMap<UUID, String>();
        playerlistu = new ArrayList<String>();
        playerlistn = new ArrayList<String>();
    }

    public static Data init() {
        if (instens == null) {
            instens = new Data();
        }
        return instens;
    }
}
