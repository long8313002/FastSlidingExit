package com.focus.slidingexit;

/**
 * Created by zhangzheng on 2016/11/4.
 */

class CacheSwipeLayoutManager {

    private static ICacheSwipeLayout cacheManager;

    public static ICacheSwipeLayout getCacheManagerImp() {
        if(cacheManager == null){
            cacheManager = new CacheSwipeLayoutImp();
        }
        return cacheManager;
    }
}
