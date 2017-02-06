package com.touchrom.gaoshouyou.entity;

import com.arialyy.frame.util.AESEncryption;
import com.touchrom.gaoshouyou.base.BaseEntity;

/**
 * Created by lyy on 2016/3/1.
 * 账户实体
 */
public class AccountEntity extends BaseEntity {
    private static final String SEED = "@!*&$123456";
    String account;
    String password;

    public String getAccount() {
        try {
            return AESEncryption.decryptString(SEED, account);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setAccount(String account) {
        try {
            this.account = AESEncryption.encryptString(SEED, account);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPassword() {
        try {
            return AESEncryption.decryptString(SEED, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPassword(String password) {
        try {
            this.password = AESEncryption.encryptString(SEED, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
