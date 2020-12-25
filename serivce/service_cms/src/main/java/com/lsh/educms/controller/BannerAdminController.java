package com.lsh.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lsh.commonutils.util.R;
import com.lsh.educms.entity.CrmBanner;
import com.lsh.educms.service.CrmBannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author lsh
 * @since 2020-12-20
 */
@RestController
@RequestMapping("/educms/banneradmin")

public class BannerAdminController {

    @Autowired
    private CrmBannerService bannerService;

    //分页查询banner
    @ApiOperation(value = "获取Banner分页列表")
    @GetMapping("pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable long page,@PathVariable long limit){
        Page<CrmBanner> bannerPage = new Page<>(page,limit);
        bannerService.pageBanner(bannerPage,null);

        return R.ok().data("items",bannerPage.getRecords()).data("total",bannerPage.getTotal());
    }

    @ApiOperation(value = "获取首页banner")
    @GetMapping("getAllBanner")
    public R index() {
        List<CrmBanner> list = bannerService.selectAllBanner();
        System.out.println("-----------------超时了吗?");
        return R.ok().data("bannerList", list);
    }

    //添加banner
    @ApiOperation(value = "添加Banner")
    @PostMapping("addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner){
        bannerService.saveBanner(crmBanner);
        return R.ok();
    }

    @ApiOperation(value = "修改Banner")
    @PutMapping("update")
    public R updateById(@RequestBody CrmBanner banner) {
        bannerService.updateBannerById(banner);
        return R.ok();
    }

    @ApiOperation(value = "删除Banner")
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id) {
        bannerService.removeBannerById(id);
        return R.ok();
    }

    @ApiOperation(value = "获取Banner")
    @GetMapping("get/{id}")
    public R get(@PathVariable String id) {
        CrmBanner banner = bannerService.getBannerById(id);
        return R.ok().data("item", banner);
    }

}

