package org.lorislab.zeebe.dev.monitor;


public class ZeebeContainer {

    private static io.zeebe.containers.ZeebeContainer CONTAINER;

    public static void main(String ...args) throws InterruptedException {
        CONTAINER = new io.zeebe.containers.ZeebeContainer().withDebugExporter(8080);
        CONTAINER.start();
        Thread.currentThread().join();
    }

}
