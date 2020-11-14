package dev.zodo.openfaas.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;
import java.util.ResourceBundle;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Bundles {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages");

    public static String getString(String name) {
        try {
            return BUNDLE.getString(name);
        } catch (Exception e) {
            return name;
        }
    }

    public static String getString(String name, Object... args) {
        return MessageFormat.format(getString(name), args);
    }

}
