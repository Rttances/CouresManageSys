package course.manager.system.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import course.manager.system.model.ro.PageRO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页参数返回类
 *
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVO<T> {
    @Schema(description = "数据集合")
    private List<T> records;

    @Schema(description = "一页数量")
    private Long pageSize;

    @Schema(description = "总数据量")
    private Long total;

    @Schema(description = "页数")
    private Long pages;

    @Schema(description = "页码")
    private Long pageNumber;

    /**
     * 填充分页参数
     *
     * @param pageSize   一页数据量
     * @param total      数据总量
     * @param pageNumber 页码
     * @param data       数据
     * @param <T>
     * @return
     */
    public static <T> PageVO<T> of(Long pageSize, Long total, Long pageNumber, List<T> data) {
        PageVO<T> tPageVO = new PageVO<>();
        tPageVO.pageSize = pageSize;
        tPageVO.total = total;
        tPageVO.pages = total / pageSize;
        tPageVO.pageNumber = pageNumber;
        tPageVO.records = data;
        return tPageVO;
    }

    /**
     * 根据 分页接收参数 RO 以及 数据填充 分页响应参数
     *
     * @param pageRO 分页接收参数
     * @param total  数据总量
     * @param data   数据
     * @param <M>    适配 RO 中的泛型
     * @param <T>    适配 VO 中的泛型
     * @return
     */
    public static <M, T> PageVO<T> of(PageRO<M> pageRO, Long total, List<T> data) {
        return PageVO.of(pageRO.getPageSize(), total, pageRO.getPageNumber(), data);
    }

    /**
     * 根据 Mybatis-Plus 分页查询结果构建分页响应参数
     *
     * @param mPage Flex 分页查询结果
     * @param data  数据
     * @param <M>   Flex 分页查询结果 PO 的泛型
     * @param <T>   适配分页响应参数 VO 的泛型
     * @return
     */
    public static <M, T> PageVO<T> of(Page<M> mPage, List<T> data) {
        return PageVO.of(mPage.getSize(), mPage.getTotal(), mPage.getCurrent(), data);
    }
}
