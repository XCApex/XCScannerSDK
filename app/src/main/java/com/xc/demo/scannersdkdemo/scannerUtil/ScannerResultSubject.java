package com.xc.demo.scannersdkdemo.scannerUtil;

public interface ScannerResultSubject {
    void attach(ScannerResultObserver observer);

    void detach(ScannerResultObserver observer);

    void notifyObservers(String symbol, String content);
}
