package com.lsh.serviceedu.client;

import com.lsh.commonutils.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderDegradeFeignClient implements OrdersClient {
    @Override
    public boolean isBuyCourse(String courseId, String memberId) {
        log.error("time out");
        return false;
    }
}
