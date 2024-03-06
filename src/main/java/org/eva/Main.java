package org.eva;

import org.eva.service.impl.HttpServerImpl;

import static org.eva.common.HttpConstants.PORT;

public class Main {
    public static void main(String[] args) {
        HttpServerImpl server = new HttpServerImpl();
        server.start(PORT);
    }
}