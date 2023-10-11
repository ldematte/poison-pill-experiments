package org.elasticsearch.experiments;

import java.util.ServiceLoader;
import java.util.function.BiConsumer;

public final class PoisonPills {

    public static BiConsumer<String, String> maybeFailWithMessage = createFailMethod();

    private static BiConsumer<String, String> createFailMethod() {
        var extensions = ServiceLoader.load(PoisonPillExtension.class);
        return extensions.findFirst().map(x -> (BiConsumer<String, String>) x::evaluateAndFailWithMessage).orElse((msg, v) -> {});
    }

    public static void poisonPill(String message, String version) {
        maybeFailWithMessage.accept(message, version);
    }

    public static void checkPoisonPill(String version, String message) {
        // Placeholder (do-nothing) for checkstyle
    }
}
