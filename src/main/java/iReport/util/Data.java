package iReport.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Data implements Serializable {
    private static final long serialVersionUID = 8569734081216879919L;
    public Map<UUID, String> playermap = new HashMap<UUID, String>();
    public static Data instens;

    public static Data init() {
        if (instens == null) {
            instens = new Data();
        }
        return instens;
    }
}
