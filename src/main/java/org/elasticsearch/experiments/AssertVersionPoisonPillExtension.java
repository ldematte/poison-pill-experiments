package org.elasticsearch.experiments;

import org.elasticsearch.common.Version;

public class AssertVersionPoisonPillExtension implements PoisonPillExtension {

    @Override
    public void evaluateAndFailWithMessage(String msg, String version) {
        try {
            var major = Integer.parseInt(version);
            assert major > Version.CURRENT.getMajor() : msg;
        }
        catch (NumberFormatException ignored) {
        }
    }
}
