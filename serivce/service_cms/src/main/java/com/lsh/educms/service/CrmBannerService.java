package com.lsh.educms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsh.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author lsh
 * @since 2020-12-20
 */
public interface CrmBannerService extends IService<CrmBanner> {
    //前台查询
    List<CrmBanner> selectAllBanner();

    void pageBanner(Page<CrmBanner> pageParam, Object o);

    CrmBanner getBannerById(String id);

    void saveBanner(CrmBanner banner);

    void updateBannerById(CrmBanner banner);

    void removeBannerById(String id);
}
