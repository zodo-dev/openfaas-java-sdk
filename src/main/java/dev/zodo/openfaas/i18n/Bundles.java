package dev.zodo.openfaas.i18n;

import dev.zodo.openfaas.util.Util;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Bundles {

    private static final ResourceBundle DEFAULT_BUNDLE;
    private static final Map<String, ResourceBundle> BUNDLES_BY_LOCALE;
    static {
        DEFAULT_BUNDLE = ResourceBundle.getBundle("messages");

        BUNDLES_BY_LOCALE = new HashMap<>();
        BUNDLES_BY_LOCALE.put("en", DEFAULT_BUNDLE);
        BUNDLES_BY_LOCALE.put("en_US", DEFAULT_BUNDLE);
        BUNDLES_BY_LOCALE.put("pt_BR", ResourceBundle.getBundle("messages_pt_BR"));
    }

    public static String getString(String name, Locale locale) {
        try {
            ResourceBundle rbByLocale = getBundleByLocale(locale);
            String msg = rbByLocale.getString(name);
            if (!Util.isNullOrEmpty(msg)) {
                return msg;
            }
            if (rbByLocale == DEFAULT_BUNDLE) {
                msg = DEFAULT_BUNDLE.getString(name);
            }
            if (Util.isNullOrEmpty(msg)) {
                return name;
            }
            return msg;
        } catch (Exception e) {
            return name;
        }
    }

    private static ResourceBundle getBundleByLocale(Locale locale) {
        if (locale == null) {
            return DEFAULT_BUNDLE;
        }
        return BUNDLES_BY_LOCALE.getOrDefault(locale.toString(), DEFAULT_BUNDLE);
    }

    public static String getString(String name) {
        return getString(name, Locale.getDefault());
    }

    public static String getString(String name, Object... args) {
        return MessageFormat.format(getString(name), args);
    }

    public static String getString(String name, Locale locale, Object... args) {
        return MessageFormat.format(getString(name, locale), args);
    }

}
