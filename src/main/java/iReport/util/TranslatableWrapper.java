package iReport.util;

import org.spongepowered.api.text.translation.FixedTranslation;
import org.spongepowered.api.text.translation.Translatable;
import org.spongepowered.api.text.translation.Translation;

public class TranslatableWrapper implements Translatable {

    private FixedTranslation name;

    public TranslatableWrapper(String name) {
        this.name = new FixedTranslation(name);
    }

    @Override
    public Translation getTranslation() {
        return name;
    }
}
