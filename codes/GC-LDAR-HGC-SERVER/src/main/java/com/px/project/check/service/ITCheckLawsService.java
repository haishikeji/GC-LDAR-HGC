package com.px.project.check.service;

import java.util.List;
import com.px.project.check.domain.TCheckLaws;

/**
 * 法规Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface ITCheckLawsService
{
    /**
     * 查询法规
     *
     * @param id 法规主键
     * @return 法规
     */
    public TCheckLaws selectTCheckLawsById(Long id);

    /**
     * 查询法规列表
     *
     * @param tCheckLaws 法规
     * @return 法规集合
     */
    public List<TCheckLaws> selectTCheckLawsList(TCheckLaws tCheckLaws);

    /**
     * 新增法规
     *
     * @param tCheckLaws 法规
     * @return 结果
     */
    public int insertTCheckLaws(TCheckLaws tCheckLaws);

    /**
     * 修改法规
     *
     * @param tCheckLaws 法规
     * @return 结果
     */
    public int updateTCheckLaws(TCheckLaws tCheckLaws);
    public int updateTCheckLawsStatus(TCheckLaws tCheckLaws);

    /**
     * 批量删除法规
     *
     * @param ids 需要删除的法规主键集合
     * @return 结果
     */
    public int deleteTCheckLawsByIds(Long[] ids);

    /**
     * 删除法规信息
     *
     * @param id 法规主键
     * @return 结果
     */
    public int deleteTCheckLawsById(Long id);
}
