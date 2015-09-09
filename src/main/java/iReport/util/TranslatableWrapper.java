package iReport.util;

import org.spongepowered.api.text.translation.ResourceBundleTranslation;
import org.spongepowered.api.text.translation.Translatable;
import org.spongepowered.api.text.translation.Translation;

public class TranslatableWrapper implements Translatable {

    private ResourceBundleTranslation name;

    public TranslatableWrapper(String name) {
        this.name = new ResourceBundleTranslation(name, Constance.LOOKUP_FUNC);
    }

    @Override
    public Translation getTranslation() {
        return name;
    }
}
