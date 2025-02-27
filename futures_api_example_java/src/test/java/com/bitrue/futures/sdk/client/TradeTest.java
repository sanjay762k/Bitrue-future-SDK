package com.bitrue.futures.sdk.client;

import com.bitrue.futures.sdk.client.model.enums.*;
import com.bitrue.futures.sdk.client.model.trade.Order;
import org.junit.Test;

import java.util.UUID;

public class TradeTest {

    @Test
    public void testPlaceOrder(){
        SyncRequestClient client = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, new RequestOptions());
        int i = UUID.randomUUID().hashCode();
//        Response =====> {"orderId":"1141178565827068518"}
//        Order ord = client.placeOrder(
//                "E-BTC-USDT", OrderSide.BUY, PositionActiion.OPEN, OrderType.LIMIT, PositionType.CROSS,
//                TimeInForce.LIMIT, "24394.4", "300", String.valueOf(i < 0 ? i * -1 : i)
        Order ord = client.placeOrder(
                        "E-BTC-USDT", OrderSide.SELL, PositionActiion.CLOSE, OrderType.LIMIT, PositionType.CROSS,
                        TimeInForce.LIMIT, "21394.4", "300", String.valueOf(i < 0 ? i * -1 : i)
        );
        System.out.println(ord);

//        testCancel(ord);

    }

//    @Test
    @Test
    public void testInversePlaceOrder(){
        SyncRequestClient client = SyncRequestClient.createInverse(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, new RequestOptions());
        int i = UUID.randomUUID().hashCode();
//        Response =====> {"orderId":"1194156214330328799"}
        Order ord = client.placeOrder(
                "E-BTC-USD", OrderSide.BUY, PositionActiion.OPEN, OrderType.LIMIT, PositionType.CROSS,
                TimeInForce.LIMIT, "20111", "100", String.valueOf(i < 0 ? i * -1 : i)
        );
        System.out.println(ord);

//        testCancel(ord);

    }


//    @Test
    @Test
    public void testCancel(){
        SyncRequestClient client = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, new RequestOptions());
        Order ord = Order.builder().clientOrdId("1534994023").orderId(1141178101970600166L).contractName("E-ETH-USDT").build();
        // Response =====> {"orderId":"1141178101970600166"}
        client.cancelOrder(ord.getContractName(), ord.getOrderId(), ord.getClientOrdId());
    }

//    @Test
    @Test
    public void testOpenOrders(){
        SyncRequestClient client = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, new RequestOptions());
        System.out.println(client.getOpenOrder("E-ETH-USDT"));
    }

//    @Test
    @Test
    public void testInverseOpenOrders(){
        SyncRequestClient client = SyncRequestClient.createInverse(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, new RequestOptions());
        System.out.println(client.getOpenOrder("E-BTC-USD"));
    }

//    @Test
    @Test
    public void testQueryOrder(){
        SyncRequestClient client = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, new RequestOptions());
        System.out.println(client.queryOrder("E-ETH-USDT", 1141178101970600166L));
    }

//    @Test
    @Test
    public void testInverseQueryOrder(){
        SyncRequestClient client = SyncRequestClient.createInverse(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, new RequestOptions());
        System.out.println(client.queryOrder("E-BTC-USD", 1194156214330328799L));
    }
}
