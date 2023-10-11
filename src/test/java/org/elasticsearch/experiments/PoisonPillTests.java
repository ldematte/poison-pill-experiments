package org.elasticsearch.experiments;

import org.junit.jupiter.api.Test;

import static org.elasticsearch.experiments.PoisonPills.poisonPill;

public class PoisonPillTests {

    @PoisonPill(expireVersion = "9")
    static void someGoodMethod() {
    }

    //@PoisonPill(expireVersion = "")
    static void wrongAnnotation() {
    }

    //@PoisonPill(expireVersion = "8")
    static void someExpiredMethod() {
    }

    @Test
    void testNeedToRemove() {
        //poisonPill("Need to remove", "8");
    }

    @Test
    void testNoNeedToRemove() {
        poisonPill("No need to remove", "9");
    }

    static void callingEmptyCheck() {
        PoisonPills.checkPoisonPill("8", "This method must be removed");
    }
}
