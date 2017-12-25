package ireport.util;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class Data implements Serializable {
    private static final long serialVersionUID = 8569734081216879910L;
    private final ConcurrentMap<UUID, String> playermap;
    private final ConcurrentMap<UUID, String> playermapo;
    private final ConcurrentMap<String, UUID> playermapor;
    private final ConcurrentMap<UUID, String> playermapr;
    private static Data instens;

    public Data() {
        playermap = new ConcurrentHashMap<>();
        playermapo = new ConcurrentHashMap<>();
        playermapr = new ConcurrentHashMap<>();
        playermapor = new ConcurrentHashMap<>();
    }

    public static Data getInstens() {
		return instens;
	}

	public static void setInstens(Data instens) {
		Data.instens = instens;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ConcurrentMap<UUID, String> getPlayermap() {
		return playermap;
	}

	public ConcurrentMap<UUID, String> getPlayermapo() {
		return playermapo;
	}

	public ConcurrentMap<String, UUID> getPlayermapor() {
		return playermapor;
	}

	public ConcurrentMap<UUID, String> getPlayermapr() {
		return playermapr;
	}

	public static Data init() {
        if (instens == null) {
            instens = new Data();
        }
        return instens;
    }
}
