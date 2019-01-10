package com.chungo.baseapp.config

/**
 * @Description app基本信息
 *
 * @Author huangchangguo
 * @Created  2019/1/8 14:24
 *
 */
class Config {
    companion object {
        /******************** base  *********************/
        const val DEVELOPER = "hcg_"
        const val DIR_PATH = "/PRIZE_WG_DOWNLOAD"

        /******************** 广点通  *********************/
        const val APPID = "1107915881"
        const val VIDEO_ID = "7020946618677785" //激励视频广告位
        const val SPLASH_ID = "4060946644808157" //开屏广告位

        /******************** server domain*********************/
        const val LOCAL_DOMAIN = "127.0.0.1"
        const val LOCAL_HOST = "http://$LOCAL_DOMAIN:8080/"
        const val LOCAL_LOGIN = "/index.html"
        /******************** umeng*********************/
        const val appKey = "5c00a10cf1f5568d2e000075"
        const val channel = "coosea"
    }
}