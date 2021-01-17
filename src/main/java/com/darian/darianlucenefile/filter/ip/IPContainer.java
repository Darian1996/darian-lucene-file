package com.darian.darianlucenefile.filter.ip;

import io.netty.util.internal.ConcurrentSet;

import java.util.Set;

public class IPContainer {

    public static Set<String> VISITOR_IP_SET = new ConcurrentSet<>();

    public static Set<String> ILLEGAL_IP_SET = new ConcurrentSet<>();

}
