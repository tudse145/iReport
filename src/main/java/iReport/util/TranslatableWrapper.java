package iReport.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.spongepowered.api.text.translation.ResourceBundleTranslation;
import org.spongepowered.api.text.translation.Translatable;
import org.spongepowered.api.text.translation.Translation;

import com.google.common.base.Function;

public class TranslatableWrapper implements Translatable {

    private ResourceBundleTranslation name;
    private static final Function<Locale, ResourceBundle> LOOKUP_FUNC = new Function<Locale, ResourceBundle>() {
        public ResourceBundle apply(Locale input) {
           return ResourceBundle.getBundle("org.spongepowered.api.Translations", input);
        }
    };


    public TranslatableWrapper(String name) {
        this.name = new ResourceBundleTranslation(name, LOOKUP_FUNC);
    }

    @Override
    public Translation getTranslation() {
        return name;
    }
}
