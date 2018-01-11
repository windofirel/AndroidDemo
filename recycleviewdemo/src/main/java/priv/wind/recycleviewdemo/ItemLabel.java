package priv.wind.recycleviewdemo;

import priv.wind.recycleviewdemo.annotation.FormHeader;

/**
 * @author Dongbaicheng
 * @version 2017/12/4
 */

public class ItemLabel {
    @FormHeader(name = "物料编码")
    public String itemCode;
    @FormHeader(name = "物料名称")
    public String itemName;
    @FormHeader(name = "数量")
    public double qty;
    @FormHeader(name = "单位")
    public String unit;
    @FormHeader(name = "货位")
    public String location;
    @FormHeader(name = "标签号")
    String no;
}
