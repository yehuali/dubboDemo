package com.examle.core.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetUtils {
    public static String getIpByHost(String hostName) {
        try {
            return InetAddress.getByName(hostName).getHostAddress();
        } catch (UnknownHostException e) {
            return hostName;
        }
    }
}
