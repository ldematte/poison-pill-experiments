package org.elasticsearch.common;

public class Version {

    public static Version CURRENT = new Version(8);
    private final int major;

    public Version(int major) {

        this.major = major;
    }

    public int getMajor() {
        return major;
    }
}
