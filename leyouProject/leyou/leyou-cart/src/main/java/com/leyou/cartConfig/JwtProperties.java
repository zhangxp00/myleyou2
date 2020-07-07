package com.leyou.cartConfig;

import com.leyou.common.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import javax.annotation.PostConstruct;
import java.security.PublicKey;

@ConfigurationProperties(prefix = "leyou.jwt")
public class JwtProperties {
    private String pubKeyPath;

    private String cookieName;

    private PublicKey publicKey;

    private static final Logger log= LoggerFactory.getLogger(JwtProperties.class);

    @PostConstruct
    public void init() {
        try {
            publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("初始化公钥和私钥失败！",e);
            throw new RuntimeException();
        }
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
