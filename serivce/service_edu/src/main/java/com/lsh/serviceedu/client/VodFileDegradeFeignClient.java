package com.lsh.serviceedu.client;

import com.lsh.commonutils.util.R;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class VodFileDegradeFeignClient implements VodClient {
    //出错之后会执行
    @Override
    public R removeVideo(String videoId) {
        return R.error().message("time out");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("time out");
    }
}
