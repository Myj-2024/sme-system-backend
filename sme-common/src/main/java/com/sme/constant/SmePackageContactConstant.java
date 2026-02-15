package com.sme.constant;

/**
 * 包抓联业务状态常量（与前端完全对齐）
 */
public class SmePackageContactConstant {
    // 流程状态常量（完全匹配前端）
    public static final String PROCESS_STATUS_HANDLING = "HANDLING";       // 受理中
    public static final String PROCESS_STATUS_PROCESSING = "PROCESSING";   // 办理中
    public static final String PROCESS_STATUS_FINISHING = "FINISHING";     // 办结中
    public static final String PROCESS_STATUS_COMPLETED = "COMPLETED";     // 完全办结
    public static final String PROCESS_STATUS_UNABLE = "UNABLE";           // 暂无法办结

    // 时间格式常量
    public static final String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String YEAR_MONTH_FORMAT_PATTERN = "yyyyMM";

    // 重试相关常量
    public static final int MAX_RETRY = 3;
    public static final long RETRY_SLEEP_MS = 50;
}