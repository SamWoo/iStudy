package com.samwoo.istudy.constant

object Constant {
    const val BASE_URL = "https://www.wanandroid.com"

    const val LOGIN_KEY = "login"
    const val USERNAME_KEY = "username"
    const val PASSWORD_KEY = "password"
    const val REMEMBER_PASSWORD_KEY = "is_remember"

    const val CONTENT_URL_KEY = "url"
    const val CONTENT_TITLE_KEY = "title"
    const val CONTENT_ID_KEY = "id"
    const val CONTENT_CID_KEY = "cid"
    const val CONTENT_DATA_KEY = "data"
    const val CONTENT_SHARE_TYPE = "text/plain"

    const val SEARCH_KEY = "search_key"
    const val TYPE_KEY = "type"

    object TYPE {
        const val SEARCH_TYPE_KEY = "search_type_key"
        const val COLLECT_TYPE_KEY = "collect_type_key"
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

}