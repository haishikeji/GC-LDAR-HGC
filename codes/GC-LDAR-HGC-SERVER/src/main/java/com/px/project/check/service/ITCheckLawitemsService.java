package com.px.project.check.service;

import java.util.List;
import com.px.project.check.domain.TCheckLawitems;

/**
 * 法规项Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface ITCheckLawitemsService
{
    /**
     * 查询法规项
     *
     * @param id 法规项主键
     * @return 法规项
     */
    public TCheckLawitems selectTCheckLawitemsById(Long id);
    public List<TCheckLawitems> selectTCheckLawitemsByLawStatus();

    /**
     * 查询法规项列表
     *
     * @param tCheckLawitems 法规项
     * @return 法规项集合
     */
    public List<TCheckLawitems> selectTCheckLawitemsList(TCheckLawitems tCheckLawitems);

    /**
     * 新增法规项
     *
     * @param tCheckLawitems 法规项
     * @return 结果
     */
    public int insertTCheckLawitems(TCheckLawitems tCheckLawitems);
    public int insertTCheckLawitemsByList(List<TCheckLawitems> tCheckLawitems);

    /**
     * 修改法规项
     *
     * @param tCheckLawitems 法规项
     * @return 结果
     */
    public int updateTCheckLawitems(TCheckLawitems tCheckLawitems);

    /**
     * 批量删除法规项
     *
     * @param ids 需要删除的法规项主键集合
     * @return 结果
     */
    public int deleteTCheckLawitemsByIds(Long[] ids);

    /**
     * 删除法规项信息
     *
     * @param id 法规项主键
     * @return 结果
     */
    public int deleteTCheckLawitemsById(Long id);
}
