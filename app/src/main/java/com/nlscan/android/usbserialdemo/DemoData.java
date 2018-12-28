package com.nlscan.android.usbserialdemo;

/**
 * @author Alan
 * @Company nlscan
 * @date 2018/7/4 21:24
 * @Description:
 */
public class DemoData {
    private String name;
    private String code;
    private String price;

    public DemoData(String name, String code, String price) {
        this.name = name;
        this.code = code;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
