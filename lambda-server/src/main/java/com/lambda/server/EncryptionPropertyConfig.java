package com.lambda.server;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lambda.core.utils.MD5DigestUtil;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;

/**
 * 配置文件部分字段加密
 * @author WKX
 */
@Configuration
public class EncryptionPropertyConfig {

    @Bean(name = "encryptablePropertyResolver")
    public EncryptablePropertyResolver encryptablePropertyResolver() {
        return new EncryptionPropertyResolver();
    }

    class EncryptionPropertyResolver implements EncryptablePropertyResolver {

        @Override
        public String resolvePropertyValue(String value) {
            if (StringUtils.isBlank(value)) {
                return value;
            }
            //值以DES@开头的均为DES加密,需要解密
            if (value.startsWith("DES@")) {
                return resolveDESValue(value.substring(4));
            }
            //不需要解密的值直接返回
            return value;
        }

        private String resolveDESValue(String value) {
            return MD5DigestUtil.decrypt(value);
        }
    }
}