package com.kf5.sdk.helpcenter.entity;

import com.google.gson.annotations.SerializedName;
import com.kf5.sdk.system.entity.MultiPageEntity;

import java.util.List;

/**
 * @author Chosen
 * @create 2019/4/2 17:40
 * @email 812219713@qq.com
 * 这里用list作为变量名称，由于返回的json数据里的数组key不是list，所以根据属性名称解析数据是解析不到数据的，故使用注解声明key的值。
 * 之前单独封装的类是这样子的
 * public class HelpCenterItemCategory extends MultiPageEntity {
 *
 *     private List<HelpCenterItem> categories;
 *
 *     public List<HelpCenterItem> getCategories() {
 *         return categories;
 *     }
 *
 *     public void setCategories(List<HelpCenterItem> categories) {
 *         this.categories = categories;
 *     }
 * }
 *
 * public class HelpCenterItemForum extends MultiPageEntity {
 *
 *     private List<HelpCenterItem> forums;
 *
 *     public List<HelpCenterItem> getForums() {
 *         return forums;
 *     }
 *
 *     public void setForums(List<HelpCenterItem> forums) {
 *         this.forums = forums;
 *     }
 * }
 *
 * public class HelpCenterItemPost extends MultiPageEntity {
 *
 *     private List<HelpCenterItem> posts;
 *
 *     public List<HelpCenterItem> getPosts() {
 *         return posts;
 *     }
 *
 *     public void setPosts(List<HelpCenterItem> posts) {
 *         this.posts = posts;
 *     }
 * }
 * 由于上面三个类的集合List对应的变量名为posts，categories，forums；因此没有使用注解设置key值依然可以正常解析到数据；其实在HelpCenterCollection
 * 采用注解绑定key值跟上面未设置直接使用变量名效果一样，HelpCenterCollection用更少的代码实现了同样的效果。
 */
public class HelpCenterCollection extends MultiPageEntity {

    @SerializedName(value = "posts", alternate = {"categories", "forums"})
    private List<HelpCenterItem> list;

    public List<HelpCenterItem> getList() {
        return list;
    }

    public void setList(List<HelpCenterItem> list) {
        this.list = list;
    }
}
