package com.hanclouds;


import com.alibaba.fastjson.JSON;
import com.hanclouds.model.DeviceInfo;
import com.hanclouds.util.CryptoUtils;
import org.junit.Test;

import java.util.Random;

public class Test1 {
    @Test
    public void test() {
        HancloudsClient hancloudsClient = HanCloudsClientFactory.getClient();
        MyCallBack callBack = new MyCallBack();
        hancloudsClient.init("X1UoJ1HY", "ygb4jEUb", "vMDZ0Z9D3poaUdfo", callBack);

        DeviceInfo deviceInfo = hancloudsClient.connect("typeSzl", "snSzl0", true, null);
        System.out.println("the one round");
        int i = 0;
        Random random = new Random();
        for (i = 0; i < 10; i++) {
            double value = 200.0 + random.nextInt(50);
            String stringValue = String.valueOf(value);
            hancloudsClient.uploadBin("binq", stringValue.getBytes());
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        hancloudsClient.disconnect();
        System.out.println("the second round");
        deviceInfo = hancloudsClient.connect("typeSzl", "snSzl0", false, null);
        for (i = 0; i < 10; i++) {
            double value = 200.0 + random.nextInt(50);
            hancloudsClient.uploadDouble("ddd", value);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        hancloudsClient.disconnect();
        hancloudsClient.close();
        System.out.println("completed!");
    }

    @Test
    public void test2() {
        HancloudsClient hancloudsClient = HanCloudsClientFactory.getClient();

        // 第1次连接创建设备
        MyCallBack callBack = new MyCallBack();
        String sn = CryptoUtils.getRandomString(8);
        System.out.printf("sn = %s", sn);
        System.out.println();
        hancloudsClient.init("X1UoJ1HY", "ygb4jEUb", "vMDZ0Z9D3poaUdfo", callBack);
        DeviceInfo deviceInfo1 = hancloudsClient.connect("typeSzl", sn, true, null);
        System.out.printf("deviceSecret = %s", deviceInfo1.getDeviceSecret());
        System.out.println();
        // 告知服务端已经收到设备鉴权参数，断开和重置连接，后续服务端不会再次向设备反馈deviceSecret
        hancloudsClient.publishInitAck();
        hancloudsClient.disconnect();
        hancloudsClient.close();

        // 第2 次连接
        hancloudsClient.init("X1UoJ1HY", "ygb4jEUb", "vMDZ0Z9D3poaUdfo", callBack);
        // 提供第一次请求时返回的deviceSecret
        DeviceInfo deviceInfo2 = hancloudsClient.connect("typeSzl", sn, true, deviceInfo1.getDeviceSecret());

        System.out.println("the one round upload int");
        int i = 0;
        Random random = new Random();
        for (i = 0; i < 500; i++) {
            int value = 100 + random.nextInt(50);
            hancloudsClient.uploadInt("bbb", value);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        hancloudsClient.disconnect();

        // 第3次连接
        System.out.println("the second round， upload double");
        deviceInfo2 = hancloudsClient.connect("typeSzl", sn, true, deviceInfo1.getDeviceSecret());
        for (i = 0; i < 500; i++) {
            double value = 100.0 + random.nextInt(50);
            hancloudsClient.uploadDouble("ddd", value);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        hancloudsClient.disconnect();

        // 第4次连接
        System.out.println("the second round upload string");
        deviceInfo2 = hancloudsClient.connect("typeSzl", sn, true, deviceInfo1.getDeviceSecret());
        for (i = 0; i < 500; i++) {
            double value = 200.0 + random.nextInt(50);
            String stringValue = String.valueOf(value);
            hancloudsClient.uploadString("sss", stringValue);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        hancloudsClient.disconnect();

        // 第5次连接
        System.out.println("the five round upload bin");
        deviceInfo2 = hancloudsClient.connect("typeSzl", sn, true, deviceInfo1.getDeviceSecret());
        for (i = 0; i < 500; i++) {
            double value = 100.0 + random.nextInt(50);
            String stringValue = String.valueOf(value);
            hancloudsClient.uploadBin("bin", stringValue.getBytes());
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        hancloudsClient.disconnect();

        // 第6次连接
        System.out.println("the six round upload json");
        deviceInfo2 = hancloudsClient.connect("typeSzl", sn, true, deviceInfo1.getDeviceSecret());
        for (i = 0; i < 500; i++) {
            int value = 200 + random.nextInt(50);
            String stringValue = String.valueOf(value);
            TestJson json = new TestJson();
            json.setParam1(stringValue);
            json.setParam2(value);
            hancloudsClient.uploadJson("json", JSON.toJSONString(json));
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        hancloudsClient.disconnect();

        hancloudsClient.close();
        System.out.println("completed!");
    }
}
