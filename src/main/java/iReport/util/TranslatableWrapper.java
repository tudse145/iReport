package iReport.util;

import org.spongepowered.api.text.translation.Translatable;
import org.spongepowered.api.text.translation.Translation;

public class TranslatableWrapper implements Translatable, Translation {

    private String name;

    public TranslatableWrapper(String name) {
        this.name = name;
    }

    @Override
    public Translation getTranslation() {
        return this;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public String get() {
        return name;
    }

    @Override
    public String get(Object... args) {
        return name;
    }
}
