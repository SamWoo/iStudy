package com.samwoo.istudy.constant

object Constant {
    const val BASE_URL = "https://www.wanandroid.com"
    const val GANK_GIRL_URL = "http://gank.io/api/"
    //权限
    const val WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE"
    const val READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE"
    //login&register
    const val LOGIN_KEY = "login"
    const val USERNAME_KEY = "username"
    const val PASSWORD_KEY = "password"
    const val REMEMBER_PASSWORD_KEY = "is_remember"
    //content
    const val CONTENT_URL_KEY = "url"
    const val CONTENT_TITLE_KEY = "title"
    const val CONTENT_ID_KEY = "id"
    const val CONTENT_CID_KEY = "cid"
    const val CONTENT_DATA_KEY = "data"
    const val CONTENT_SHARE_TYPE = "text/plain"
    //search
    const val SEARCH_KEY = "search_key"
    const val TYPE_KEY = "type"

    object TYPE {
        const val SEARCH_TYPE_KEY = "search_type_key"
        const val COLLECT_TYPE_KEY = "collect_type_key"
        const val GANK_GIRL_PHOTOS = "gank_girl_photos"
    }

    //网络变化action
    const val NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE"
    //上次网络状态
    const val LAST_NETWORK_STATUS_KEY = "last_network_status"
    //token
    const val TOKEN_KEY = "token"
    const val SAVE_USER_LOGIN_KEY = "/user/login"
    const val SAVE_USER_REGISTER_KEY = "/user/register"

    //收藏网址
    const val COLLECTIONS_WEBSITE = "/lg/collect"
    //取消收藏网址
    const val UNCOLLECTIONS_WEBSITE = "/lg/uncollect"
    //
    const val ARTICLE_WEBSITE = "article"
    const val PROJECT_WEBSITE = "project"

    //Cookie
    const val LOGIN_URL_KEY = "https://www.wanandroid.com/user/login"
    const val REGISTER_URL_KEY = "https://www.wanandroid.com/user/register"
    const val DOMAIN_KEY = "www.wanandroid.com"
    const val COOKIE_NAME = "Cookie"
    const val SET_COOKIE_KEY = "set-cookie"

    //Cache
    const val MAX_CACHE_SIZE: Long = 1024 * 1024 * 50

    //动态改变host url
    fun getHost(hostType: Int): String? {
        var host: String? = null
        when (hostType) {
            HostType.WAN_ANDROID -> host = BASE_URL
            HostType.GANK_GIRL_PHOTO -> host = GANK_GIRL_URL
        }
        return host
    }

}