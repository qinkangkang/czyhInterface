package com.innee.czyhInterface.util.publicImage.happydns;

import java.io.IOException;

/**
 * Created by bailong on 15/6/19.
 */
public class DnsException extends IOException {
    public DnsException(String domain, String message) {
        super(domain + ": " + message);
    }
}
