package org.elasticsearch.experiments;

public interface PoisonPillExtension {
    void evaluateAndFailWithMessage(String msg, String version);
}
