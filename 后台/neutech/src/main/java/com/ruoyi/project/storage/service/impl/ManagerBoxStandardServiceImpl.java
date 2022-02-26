package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.exception.CustomException;
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
import com.ruoyi.project.storage.service.ManagerBoxStandardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ManagerBoxStandardServiceImpl implements ManagerBoxStandardService {

    @Resource
    private ManagerBoxStandardMapper managerBoxStandardMapper;

    @Resource
    private ManagerBoxInfoMapper managerBoxInfoMapper;

    /**
     * 箱子规格列表（分页）
     * @param searchValue
     * @return
     */
    @Override
    public List<BoxStandardVO> getList(String searchValue) {

        List<BoxStandardVO> list = managerBoxStandardMapper.getList(searchValue);
        return list;
    }

    /**
     * 箱子规格新增
     * @param boxStandardVO
     * @return
     */
    @Override
    public AjaxResult saveBoxStandard(BoxStandardVO boxStandardVO) {

        if(boxStandardVO == null){
            throw new CustomException("参数不能为空");
        }

        if (StringUtils.isEmpty(boxStandardVO.getBoxStandard())){
            throw new CustomException("BoxStandard 不能为空");
        }

        if (StringUtils.isEmpty(boxStandardVO.getRemark())){
            throw new CustomException("Remark 不能为空");
        }

        if (null == boxStandardVO.getBoxUnitPrice()){
            throw new CustomException("BoxUnitPrice 不能为空");
        }

        if(null == boxStandardVO.getInventoryNumber()){
            return AjaxResult.error("InventoryNumber 不能为空");
//            上面代码等同于下面
//            throw new CustomException("InventoryNumber 不能为空");
        }

        /**
         * 箱子数量大于0
         */
        if(boxStandardVO.getInventoryNumber() == 0 ){
            return AjaxResult.error("InventoryNumber 必须大于0");
        }


        BoxStandardVO boxStandardVO1 = managerBoxStandardMapper.findBoxStandard(boxStandardVO.getBoxStandard());

        /**
         * 箱子类型和积分必须相同
         */
        if (null != boxStandardVO1){
            if (boxStandardVO1.getBoxUnitPrice().longValue() != boxStandardVO.getBoxUnitPrice().longValue()){
                throw new CustomException("箱子类型相同，所需积分相同");
            }else {
                /**
                 * 如果箱子规格存在并且箱子积分相同，那么直接在箱子信息表生成箱子
                 */
                List<BoxInfoVO> list = getBoxInfo(boxStandardVO);
                int i = 0;
                for (BoxInfoVO boxInfoVO : list) {
                    managerBoxInfoMapper.createBoxInfo(boxInfoVO);
                    i++;
                }
                if ( i != list.size()){
                    return AjaxResult.error("箱子信息添加失败");
                }
                return AjaxResult.success("箱子信息添加成功");
            }
        }else {
            /**
             * 如果本身就存在箱子就不需要设置默认值,否则执行一下代码
             * 默认值
             */
            boxStandardVO.setCreateBy(SecurityUtils.getUsername());
            boxStandardVO.setUpdateBy(SecurityUtils.getUsername());
            boxStandardVO.setVersion((long) 0);
            boxStandardVO.setDelFlag("0");
            Integer nums = managerBoxStandardMapper.saveBoxStandard(boxStandardVO);
            if (nums > 0){
                /**
                 * 箱子数量也需要保存 @TODO
                 */
                List<BoxInfoVO> list = getBoxInfo(boxStandardVO);
                int i = 0;
                for (BoxInfoVO boxInfoVO : list) {
                    managerBoxInfoMapper.createBoxInfo(boxInfoVO);
                    i++;
                }
                if ( i != list.size()){
                    return AjaxResult.error("箱子信息添加失败");
                }
                return AjaxResult.success(nums);
            }
        }
        return AjaxResult.error("箱子保存失败");
    }

    /**
     * 箱子规格删除
     * @param ids
     * @return
     */
    @Override
    public AjaxResult deleteBox(Long[] ids) {

        if (ids.length == 0){
            return AjaxResult.error("没有选择删除项");
        }

        Integer nums = managerBoxStandardMapper.deleteBox(ids , SecurityUtils.getUsername());

        if (nums > 0 ){
            return AjaxResult.success("删除成功");
        }
        return AjaxResult.error("删除失败");
    }

    /**
     * 箱子规格下拉列表（非分页）
     * @return
     */
    @Override
    public AjaxResult getBoxStandardSelect() {


        List<BoxStandardVO> list = managerBoxStandardMapper.getList(null);
        return AjaxResult.success("查询成功",list);
    }

    public List<BoxInfoVO> getBoxInfo(BoxStandardVO boxStandardVO){
        Long num = boxStandardVO.getInventoryNumber();
        List<BoxInfoVO> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            BoxInfoVO boxInfoVO = new BoxInfoVO();
            boxInfoVO.setBoxCode(Long.valueOf(SeqGeneratorUtil.seqGenerator(DateUtils.getNowDateStr(),6)));
            boxInfoVO.setIsUsed(0);
            boxInfoVO.setBoxStandard(boxStandardVO.getBoxStandard());
            boxInfoVO.setBoxUnitPrice(boxStandardVO.getBoxUnitPrice());
            boxInfoVO.setCreateBy(SecurityUtils.getUsername());
            boxInfoVO.setUpdateBy(SecurityUtils.getUsername());
            boxInfoVO.setRemark(boxStandardVO.getRemark());
            list.add(boxInfoVO);
        }
        return list;
    }
}
