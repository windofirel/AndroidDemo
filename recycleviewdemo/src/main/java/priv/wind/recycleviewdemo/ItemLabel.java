package priv.wind.recycleviewdemo;

import priv.wind.recycleviewdemo.form.FormAttr;

/**
 * @author Dongbaicheng
 * @version 2017/12/4
 */

public class ItemLabel {
    @FormAttr(name = "物料编码", sequence = 2)
    public String itemCode;
    @FormAttr(name = "物料名称", sequence = 3)
    public String itemName;
    @FormAttr(name = "数量", sequence = 4, width = 100)
    public double qty;
    @FormAttr(name = "单位", sequence = 5, width = 100)
    public String unit;
    @FormAttr(name = "货位", sequence = 6)
    public String location;
    @FormAttr(name = "标签号", sequence = 1, width = 250)
    public String no;

    double requireQty;
}
