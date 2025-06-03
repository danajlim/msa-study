package com.welab.backend_user.remote.noti;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RemoteNotiService {
    private final RestTemplate restTemplate;

    public String callNotiHello() {
        return restTemplate.getForObject(
                "http://alim-service/backend/noti/v1/hello", String.class);
    }
}
