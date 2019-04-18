package cn.lger.plugin.entity;

import cn.lger.mybatis.plugin.page.AbstractPage;

import java.util.List;

/**
 * Code that Changed the World
 *
 * @author Pro
 * @date 2019-04-18.
 */
public class MyPage<T> extends AbstractPage<T> {

    private int size;
    private int currPage;
    private List<T> body;

    public MyPage(int size, int currPage) {
        this.size = size;
        this.currPage = currPage;
    }

    @Override
    public void setPageBody(List<T> list) {
        this.body = list;
    }

    @Override
    public void setPageCount(int count) {

    }

    @Override
    public int limit() {
        return this.size;
    }

    @Override
    public int offset() {
        return (currPage - 1) * size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public List<T> getBody() {
        return body;
    }

    public void setBody(List<T> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "MyPage{" +
                "size=" + size +
                ", currPage=" + currPage +
                ", body=" + body +
                '}';
    }
}
