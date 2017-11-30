package com.rokid.httpdns;

public interface DegradationFilter {
    boolean shouldDegradeHttpDNS(String hostName);
}
