package cn.lger.mybatis.plugin.page;

import java.io.Serializable;
import java.util.List;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-04-17.
 */
public class Page<T> extends AbstractPage<T> implements Serializable{

    private static final long serialVersionUID = 0L;

    /**
     * 总记录数
     */
    private int count = 0;

    /**
     * 当前页
     */
    private int curr = 1;

    /**
     * 一页大小
     */
    private int size = 10;


    /**
     * 查询结果体
     */
    private List<T> list;

    /**
     * 计算总页数 根据 (总记录数 % 2) 是否为0 判断奇偶数
     * 奇数：总记录数 / 每页大小 + 1
     * 偶数：总记录数 / 每页大小
     * @return int 总页数
     */
    public int getTotal() {
        return size > 0 ? ((count % 2) == 0 ? count / size : count / size + 1) : 0;
    }

    /**
     *
     * @param currPage
     * @param size
     * @param <T>
     * @return
     */
    @SuppressWarnings("all")
    public static <T> Page<T> of(Integer currPage, Integer size) {
        if (currPage == null || currPage <= 0){
            currPage = 1;
        }
        if (size == null || size <= 0){
            size = 10;
        }
        Page<?> page = new Page();
        page.setCurr(currPage);
        page.setSize(size);
        return (Page<T>) page;
    }

    @Override
    public void setPageBody(List<T> list) {
        this.list = list;
    }

    @Override
    public void setPageCount(int count) {
        this.count = count;
    }

    @Override
    public int limit() {
        return this.size;
    }

    @Override
    public int offset() {
        return (curr - 1) * size;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCurr() {
        return curr;
    }

    public void setCurr(int curr) {
        this.curr = curr;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }



    @Override
    public String toString() {
        return "Page{" +
                "count=" + count +
                ", curr=" + curr +
                ", size=" + size +
                ", list=" + list +
                '}';
    }
}
