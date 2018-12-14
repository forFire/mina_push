package com.push.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DeviceRedirectPositionCache {

    @Autowired
    private RedisTemplate <String, Object> redis;

    private String toRedirctKey () {

        return "redirect-carcental";
    }

    public void addRedirectDevice (String deviceSn, String phone) {

        redis.opsForHash ().put (this.toRedirctKey (), deviceSn, phone);
    }

    public Set <Object> getRedirectDevices () {

        return redis.opsForHash ().keys (this.toRedirctKey ());
    }

    public void resetRedirectDevices (Object [] deviceSnSet) {

        this.redis.opsForHash ().entries (this.toRedirctKey ());
    }

    public String getRedirectPhone (String deviceSn) {

        Object object = this.redis.opsForHash ().get (this.toRedirctKey (), deviceSn);

        return object == null ? "" : object.toString ();
    }

    public boolean checkRedircet (String deviceSn) {

        return this.redis.opsForHash ().hasKey (this.toRedirctKey (), deviceSn);
    }
}
