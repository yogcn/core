package com.yogcn.core.util

import android.content.Context
import com.lzy.okgo.OkGo
import com.lzy.okgo.cache.CacheMode
import com.lzy.okgo.callback.FileCallback
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.cookie.CookieJarImpl
import com.lzy.okgo.cookie.store.DBCookieStore
import com.lzy.okgo.cookie.store.MemoryCookieStore
import com.lzy.okgo.cookie.store.SPCookieStore
import com.lzy.okgo.https.HttpsUtils
import com.lzy.okgo.interceptor.HttpLoggingInterceptor
import com.lzy.okgo.model.HttpParams
import com.yogcn.core.base.BaseApplication
import okhttp3.MediaType
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.security.KeyStore
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import javax.net.ssl.*

/**
 * Created by lyndon on 2017/12/14.
 */
class HttpUtil {

    private val builder:OkHttpClient.Builder = OkHttpClient.Builder()

    companion object {
        const val Tag = "httpUtil"
        fun getInstance() = Holder.instance
    }

    private object Holder {
        val instance = HttpUtil()
    }

    enum class CookType{
        SP,DB,MEMORY
    }


    constructor(){
        //设置调试日志
        val logInterceptor=HttpLoggingInterceptor(Tag)
        logInterceptor.setColorLevel(Level.WARNING)
        if(BaseApplication.instance.isDebug()){
            logInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY)
        }else
            logInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.NONE)
        builder.addInterceptor(logInterceptor)

        builder.readTimeout(60,TimeUnit.SECONDS)//读取超时
        builder.writeTimeout(60,TimeUnit.SECONDS)//写入超时
        builder.connectTimeout(60,TimeUnit.SECONDS)//连接超时


    }

    /**
     * 缓存设置
     * @param context
     * @param type
     */
    fun setCookie(context: Context,type:CookType): HttpUtil {
        when(type){
            CookType.SP->builder.cookieJar(CookieJarImpl(SPCookieStore(context)))
            CookType.DB->builder.cookieJar(CookieJarImpl(DBCookieStore(context)))
            else -> builder.cookieJar(CookieJarImpl(MemoryCookieStore()))
        }
        return this
    }

    /**
     * 信任所有证书
     */
    fun setTrustHttps(): HttpUtil {
        val sslSocketFactory = HttpsUtils.getSslSocketFactory()
        builder.sslSocketFactory(sslSocketFactory.sSLSocketFactory,sslSocketFactory.trustManager)
        return this
    }
    /**
     * 自定义信任规则，校验服务端证书
     * @param trustManager
     */
    fun setTrustHttps(trustManager:X509TrustManager): HttpUtil {
        val sslSocketFactory = HttpsUtils.getSslSocketFactory(trustManager)
        builder.sslSocketFactory(sslSocketFactory.sSLSocketFactory,sslSocketFactory.trustManager)
        return this
    }

    /**
     * 使用预埋证书，校验服务端证书（自签名证书）
     * @param cer 放置于assets目录下的自签名证书
     */
    fun setTrustHttps(cer:String): HttpUtil {
        var inputStream=BaseApplication.instance.assets.open(cer)
        val sslSocketFactory = HttpsUtils.getSslSocketFactory(inputStream)
        builder.sslSocketFactory(sslSocketFactory.sSLSocketFactory,sslSocketFactory.trustManager)
        inputStream.close()
        return this
    }
    /**
     * 使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
     * @param bsk 放置于assets目录下的信任的服务器端证书
     * @param bskPwd 服务器端证书密码
     * @param cer 放置于assets目录下的自签名证书
     */
    fun setTrustHttps(bsk:String,bskPwd:String,cer:String): HttpUtil {
        var bskInputStream=BaseApplication.instance.assets.open(bsk)
        var cerInputStream=BaseApplication.instance.assets.open(cer)
        val sslSocketFactory = HttpsUtils.getSslSocketFactory(bskInputStream,bskPwd,cerInputStream)
        builder.sslSocketFactory(sslSocketFactory.sSLSocketFactory,sslSocketFactory.trustManager)
        bskInputStream.close()
        cerInputStream.close()
        return this
    }
    /**
     * 使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
     * @param cer 放置于assets目录下的自签名证书
     * @param cer 放置于assets目录下的自签名证书
     */
    fun setTrustHttps(clientP12:String,p12Pwd:String,bsk:String,bskPwd:String): HttpUtil {
        val trustManager = chooseTrustManager(getTrustManager( bsk, bskPwd))
        val sslSocketFactory = getSSLSocketFactory(clientP12, p12Pwd, trustManager!!)
        builder.sslSocketFactory(sslSocketFactory,trustManager)
        return this
    }

    private fun getTrustManager(trustPath:String,trustPwd:String):Array<TrustManager>?{
        return  try {
            val trustStore = KeyStore.getInstance("BKS")
            var trustInputStream=BaseApplication.instance.assets.open(trustPath)
            trustStore.load(trustInputStream,trustPwd.toCharArray())
            trustInputStream.close()
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(trustStore)
             trustManagerFactory.trustManagers
        }catch (e:Exception){
            e.printStackTrace()
            null
        }
    }

    private fun chooseTrustManager(trustManagers: Array<TrustManager>?): X509TrustManager? {
        for (trustManager in trustManagers!!) {
            if (trustManager is X509TrustManager) {
                return trustManager
            }
        }
        return null
    }

    private fun getSSLSocketFactory(keyPath: String, keyPwd: String, trustManager: X509TrustManager): SSLSocketFactory? {
        try {
            val keyStore = KeyStore.getInstance("PKCS12")
            val keyStoreInput = BaseApplication.instance.assets.open(keyPath)
            keyStore.load(keyStoreInput, keyPwd.toCharArray())
            val sslContext = SSLContext.getInstance("TLS")
            val keyManagerFactory = KeyManagerFactory.getInstance("X509")
            keyManagerFactory.init(keyStore, keyPwd.toCharArray())
            val trustManagers = arrayOf<TrustManager>(trustManager)
            sslContext.init(keyManagerFactory.keyManagers, trustManagers, null)
            return sslContext.socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    /**
     * 配置https的域名匹配规则
     * @param hostnameVerifier 匹配规则
     */
    fun setSoftHostname(hostnameVerifier: HostnameVerifier): HttpUtil {
        builder.hostnameVerifier(hostnameVerifier)
        return this
    }

    fun build() {
        OkGo.getInstance().init(BaseApplication.instance)
                .setOkHttpClient(builder.build())
                .setCacheMode(CacheMode.NO_CACHE).retryCount = 0
    }

    /**
     * 取消请求
     * @param tag
     */
    fun cancelRequest(tag: String) {
        OkGo.getInstance().cancelTag(tag)
    }

    /**
     * get请求
     * @param tag
     * @param url
     * @param params
     * @param callback
     */
    fun get(tag: String, url: String, params: HttpParams?, callback: StringCallback) {
        OkGo.get<String>(url).tag(tag).params(params).execute(callback)
    }

    /**
     * get请求
     * @param tag
     * @param url
     * @param params
     * @param callback
     */
    fun post(tag: String, url: String, params: HttpParams?, callback: StringCallback) {
        OkGo.post<String>(url).tag(tag).params(params).execute(callback)
    }


    /**
     * 文件下载
     * @param tag
     * @param url
     * @param callback
     */
    fun downFile(tag: String, url: String, callback: FileCallback) {
        OkGo.get<File>(url).tag(tag).execute(callback)
    }

    /**
     * 提交json
     * @param tag
     * @param url
     * @param t
     * @param callback
     */
    fun <T> postJson(tag: String, url: String, t: T, callback: StringCallback) {
        val postRequest = OkGo.post<String>(url).tag(tag)
        when (t) {
            is JSONObject -> postRequest.upJson(t)
            is JSONArray -> postRequest.upJson(t)
            is String -> postRequest.upJson(t)
        }
        postRequest.execute(callback)
    }
    /**
     * 提交字符串
     * @param tag
     * @param url
     * @param params
     * @param mediaType
     * @param callback
     */
    fun postString(tag: String, url: String, params: String, mediaType: MediaType?, callback: StringCallback) {
        val postRequest = OkGo.post<String>(url).tag(tag)
        if (null == mediaType)
            postRequest.upString(params)
        else
            postRequest.upString(params, mediaType)
        postRequest.execute(callback)
    }

    /**
     * 上传文件
     * @param tag
     * @param url
     * @param params
     * @param callback
     */
    fun postFile(tag: String, url: String, params: HttpParams?, callback: StringCallback) {
        OkGo.post<String>(url).isMultipart(true).tag(tag).params(params).execute(callback)
    }

}