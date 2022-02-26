package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.AdviceVO;
import com.ruoyi.project.storage.mapper.AppAdviceMapper;
import com.ruoyi.project.storage.service.AppAdviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AppAdviceServiceImpl implements AppAdviceService {

    @Resource
    private AppAdviceMapper appAdviceMapper;

    @Override
    public AjaxResult setAdvice(AdviceVO adviceVO) {

        if (null == adviceVO){
            return AjaxResult.error("广告参数不能为空");
        }

        if (StringUtils.isEmpty(adviceVO.getContent())){
            return AjaxResult.error("广告内容不能为空");
        }

        if (StringUtils.isEmpty(adviceVO.getTitle())){
            return AjaxResult.error("广告标题不能为空");
        }

        adviceVO.setTitle(adviceVO.getTitle());
        adviceVO.setContent(adviceVO.getContent());
        adviceVO.setUserId(SecurityUtils.getUserId());
        adviceVO.setCreateBy(SecurityUtils.getUsername());
        adviceVO.setUpdateBy(SecurityUtils.getUsername());

        Integer nums = appAdviceMapper.setAdvice(adviceVO);
        if (1 != nums){
            return AjaxResult.error("意见更新失败");
        }
        return AjaxResult.success("意见更新成功");
    }
}
