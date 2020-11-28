package dev.zodo.openfaas.i18n;

import dev.zodo.openfaas.util.Util;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Bundles {

    protected static ResourceBundle defaultBundle;
    protected static Map<String, ResourceBundle> bundlesByLocale;
    protected static Locale lcPtBr = new Locale("pt", "BR");
    static {
        loadBundles(Bundles.class.getClassLoader());
    }

    public static String getString(String name, Locale locale) {
        try {
            ResourceBundle rbByLocale = getBundleByLocale(locale);
            String msg = rbByLocale.getString(name);
            if (!Util.isNullOrEmpty(msg)) {
                return msg;
            }
            if (rbByLocale != defaultBundle) {
                msg = defaultBundle.getString(name);
            } else {
                return name;
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
            return defaultBundle;
        }
        return bundlesByLocale.getOrDefault(locale.toString(), defaultBundle);
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

    protected static void loadBundles(ClassLoader classLoader) {
        defaultBundle = ResourceBundle.getBundle("messages", Locale.getDefault(), classLoader);
        bundlesByLocale = new HashMap<>();
        bundlesByLocale.put("en", defaultBundle);
        bundlesByLocale.put("en_US", defaultBundle);
        bundlesByLocale.put("pt_BR", ResourceBundle.getBundle("messages", lcPtBr));
    }

}
