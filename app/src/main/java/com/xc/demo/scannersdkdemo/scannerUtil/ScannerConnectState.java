package com.xc.demo.scannersdkdemo.scannerUtil;

public enum ScannerConnectState {
    CONNECTING("connecting", 0),
    DISCONNECTED("disconnected", 1),
    CONNECTED("connected", 2);

    private final String name;
    private final int index;

    ScannerConnectState(String name, int index) {
        this.name = name;
        this.index = index;
    }
}
