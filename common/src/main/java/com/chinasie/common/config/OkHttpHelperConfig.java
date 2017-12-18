package com.chinasie.common.config;


/**
 * 请求配置,用于管理时效性
 * Created by gyc on 2017-05-09.
 */

public class OkHttpHelperConfig {

    //---默认时效为15秒-----
    public static final int DEFAULT_READ_TIMEOUT_SECONDS = 15;
    public static final int DEFAULT_WRITE_TIMEOUT_SECONDS = 15;
    public static final int DEFAULT_CONNECT_TIMEOUT_SECONDS = 15;

    private static int ReadTimeOutSeconds = DEFAULT_READ_TIMEOUT_SECONDS;
    private static int WriteTimeOutSeconds = DEFAULT_WRITE_TIMEOUT_SECONDS;
    private static int ConnectTimeOutSeconds = DEFAULT_CONNECT_TIMEOUT_SECONDS;

    public static void setConnectTimeOutSeconds(int intConnectTimeOutSeconds) {
        OkHttpHelperConfig.ConnectTimeOutSeconds = intConnectTimeOutSeconds;
    }

    public static void setReadTimeOutSeconds(int intReadTimeOutSeconds) {
        OkHttpHelperConfig.ReadTimeOutSeconds = intReadTimeOutSeconds;
    }

    public static void setWriteTimeOutSeconds(int intWriteTimeOutSeconds) {
        OkHttpHelperConfig.WriteTimeOutSeconds = intWriteTimeOutSeconds;
    }

    public static int getReadTimeOutSeconds() {
        int intTemp = ReadTimeOutSeconds;
        return intTemp;
    }

    public static int getConnectTimeoutSeconds() {
        int intTemp = ConnectTimeOutSeconds;
        return intTemp;
    }

    public static int getWriteTimeOutSeconds() {
        int intTemp = WriteTimeOutSeconds;
        return intTemp;
    }

    public static boolean IsDefaultTimeOut() {
        return (ReadTimeOutSeconds == DEFAULT_READ_TIMEOUT_SECONDS) &&
                (WriteTimeOutSeconds == DEFAULT_WRITE_TIMEOUT_SECONDS) &&
                (ConnectTimeOutSeconds == DEFAULT_CONNECT_TIMEOUT_SECONDS);
    }

    public static void resetDefalultTimeOut()
    {
        ReadTimeOutSeconds = DEFAULT_READ_TIMEOUT_SECONDS;
        ConnectTimeOutSeconds = DEFAULT_CONNECT_TIMEOUT_SECONDS;
        WriteTimeOutSeconds = DEFAULT_WRITE_TIMEOUT_SECONDS;
    }
}
