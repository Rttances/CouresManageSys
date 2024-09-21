package course.manager.system.model.ro;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于分页查询的接收参数的类
 *
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRO<T> {

    @Schema(description = "条件实体")
    T entity;


    @Schema(description = "页码")
    private Long pageNumber = 0L;


    @Schema(description = "一页数据量")
    private Long pageSize = 10L;


    @Schema(description = "排序字段")
    private String orderBy;

    @Schema(description = "排序方式: ASC, DESC")
    private String orderType = "ASC";

    @Schema(description = "是否获取所有数据：默认否")
    private Boolean isAll = false;

    /**
     * 转化为Mybatis-Plus分页查询的参数类
     *
     * @param <M>
     * @return
     */
    public <M> Page<M> toPage() {
        Page<M> page = new Page<>();
        page.setCurrent(this.pageNumber);
        page.setSize(this.pageSize);
        return page;
    }
}
