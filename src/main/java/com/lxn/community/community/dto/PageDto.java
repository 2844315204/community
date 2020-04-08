package com.lxn.community.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDto<T> {
    private List<T> data;
    private boolean showPrevious;//是否有上一页
    private boolean showFirstPage;//是否有第一页
    private boolean showNext;//是否有下一页
    private boolean showEndPage;//是否是最后一页
    private Integer page;//当前页面
    private List<Integer> pages=new ArrayList<>();//显示有多少页面
    private Integer totalPage;//总页码数

    public void setPagination(Integer totalPage, Integer page) {
        this.totalPage=totalPage;
        this.page = page;
        if (totalPage>0){
            pages.add(page);
            for (int i=1;i<=2;i++){
//            给前面加页码数
                if (page-i>0){
                    pages.add(0,page-i);
                }
//            给末尾加页码数
                if (page + i <= totalPage){
                    pages.add(page+i);
                }
            }

//        是否展示上一页
            showPrevious= page==1 ? false : true;
//        是否展示下一页
            showNext= page==totalPage?false:true;
//        是否展示第一页

            showFirstPage=pages.contains(1)?false:true;
//        是否展示最后一页
            showEndPage=pages.contains(totalPage)?false:true;
        }

    }
}
