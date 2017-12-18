package priv.wind.recycleviewdemo;

import priv.wind.recycleviewdemo.annotation.HeaderName;

/**
 * @author Dongbaicheng
 * @version 2017/12/4
 */

public class ItemLabel {
    @HeaderName(name = "标签号")
    String no;
    @HeaderName(name = "物料编码")
    public String itemCode;
    @HeaderName(name = "物料名称")
    public String itemName;
    @HeaderName(name = "数量")
    public double qty;
    @HeaderName(name = "单位")
    public String unit;
    @HeaderName(name = "货位")
    public String location;
}
