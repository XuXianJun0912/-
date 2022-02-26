package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.common.util.SeqGeneratorUtil;
import com.ruoyi.project.storage.domain.BoxInfoVO;
import com.ruoyi.project.storage.domain.BoxStandardVO;
import com.ruoyi.project.storage.mapper.ManagerBoxInfoMapper;
import com.ruoyi.project.storage.mapper.ManagerBoxStandardMapper;
import com.ruoyi.project.storage.service.ManagerBoxInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ManagerBoxInfoServiceImpl implements ManagerBoxInfoService {

    @Resource
    private ManagerBoxInfoMapper managerBoxInfoMapper;

    @Resource
    private ManagerBoxStandardMapper managerBoxStandardMapper;

    /**
     * 箱子信息列表（分页）
     * @param boxInfoVO
     * @return
     */
    @Override
    public List<BoxInfoVO> getList(BoxInfoVO boxInfoVO) {

        List<BoxInfoVO> list = managerBoxInfoMapper.getList(boxInfoVO);
        return list;
    }

    /**
     * 箱子信息新增
     * @return
     */
    @Override
    public AjaxResult createBoxInfo(BoxInfoVO boxInfoVO) {

        if (null == boxInfoVO){
            return AjaxResult.error("参数不能为空");
        }

        if (StringUtils.isEmpty(boxInfoVO.getBoxStandard())){
            return AjaxResult.error("箱子规格不能为空");
        }

        if (null == boxInfoVO.getBoxUnitPrice() || 0 == boxInfoVO.getBoxUnitPrice()){
            return AjaxResult.error("积分不能为空或为0");
        }

        if (StringUtils.isEmpty(boxInfoVO.getRemark())){
            return AjaxResult.error("备注不能为空");
        }

        if (boxInfoVO.getRemark().length() > 256){
            return AjaxResult.error("备注长度最大为256");
        }

        BoxStandardVO boxStandardVO = managerBoxStandardMapper.findBoxStandard(boxInfoVO.getBoxStandard());

        if (null == boxStandardVO){
            return AjaxResult.error("箱子规格不存在");
        }else {
            if (boxStandardVO.getBoxUnitPrice().longValue() != boxInfoVO.getBoxUnitPrice().longValue()){
                return AjaxResult.error("箱子积分和已存在的箱子积分不同");
            }else {
                boxInfoVO.setCreateBy(SecurityUtils.getUsername());
                boxInfoVO.setUpdateBy(SecurityUtils.getUsername());
                /**
                 * 是否被使用（0：未使用；1：已使用）
                 */
                boxInfoVO.setIsUsed(0);

                //生成boxCode方法一
                Long boxCode = Long.valueOf(SeqGeneratorUtil.seqGenerator(DateUtils.getNowDateStr(),6));
                boxInfoVO.setBoxCode(boxCode);

                //生成boxCode方法er，这方法需要加个 synchronized 在方法名前面防止多线程并发执行该方法
                //Date date = new Date();
                //boxInfoVO.setBoxCode(date.getTime());


                Integer nums = managerBoxInfoMapper.createBoxInfo(boxInfoVO);
                if (nums ==1 ){
                    return AjaxResult.success("保存成功");
                }
                return AjaxResult.error("保存失败");
            }
        }
    }

    /**
     * 箱子信息删除
     * @param ids
     * @return
     */
    @Override
    public AjaxResult deleteBoxInfo(Long[] ids) {

        if (ids == null ){
            AjaxResult.error("参数不能为空");
        }

        if ( 0 == ids.length ){
            return AjaxResult.success();
        }

        Integer num = managerBoxInfoMapper.findBoxInfo(ids);
        if (num > 0){
            return AjaxResult.error("选中的箱子正在被使用");
        }

        Integer nums = managerBoxInfoMapper.deleteBoxInfo(ids,SecurityUtils.getUsername());
        if (nums == ids.length){
            return AjaxResult.success("删除成功");
        }
        return AjaxResult.error("删除失败");
    }
}
