package com.callphone.client.base.data;

import java.io.Serializable;
import java.util.List;

public class PocketInfo {
    public String coins;//球币
    public String money;//钱/红包
    public String limitredpacket;//冻结红包
    public String canuseredpacket;//提现红包
    public String isbindmobile;//是否绑定了手机
    public String bankcard;
    public String limitmoney;//最少取现额度
    public String redpacketvalid;//红包过期时间
    public String ticketng;//球票
    public List<Coupon> coupon;//优惠券

    public boolean isBindMobile() {
        return "1".equals(isbindmobile);
    }


    public String getLimitredpacket() {
        return limitredpacket;
    }

    public String getCanuseredpacket() {
        return canuseredpacket;
    }


    public String getMoney() {
        return money;
    }

    public String getLimitmoney() {
        return limitmoney;
    }

    //优惠券
    public static class Coupon implements Serializable {
        public int id;//int 优惠券id
        public String title;//string 优惠券标题   如：球球消费抵用券
        public String name;//       string 优惠金额描述 如：5元，10元
        public float amount;//        int 优惠金额   如：5，10
        public String validendtime;//        time 到期时间
        public String note;//        string 优惠券说明 如：满10元可用
    }

}
