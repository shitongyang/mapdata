package iscas.stategrid.mapdata.Utils;

import java.util.*;

public class StaticResource {

    public static final HashSet<String> AREA_Set=new HashSet<String>(){
        {
            add("华北");
            add("华中");
            add("华东");
            add("西北");
            add("西南");
            add("东北");
        }
    };
    public static final HashSet<String> PROVINCE_Set=new HashSet<String>()
            {{
                add("内蒙古");
                add("山东");
                add("福建");
                add("湖北");
                add("北京");
                add("天津");
                add("吉林");
                add("黑龙江");
                add("浙江");
                add("新疆");
                add("江苏");
                add("江西");
                add("西藏");
                add("重庆");
                add("辽宁");
                add("湖南");
                add("四川");
                add("山西");
                add("陕西");
                add("甘肃");
                add("安徽");
                add("上海");
                add("河北");
                add("宁夏");
                add("青海");
                add("河南");
            }};

    public static final HashSet<String> CITY_SET=new HashSet<String>(){
        {
            add("呼和浩特市");
            add("淄博市");
            add("福州市");
            add("武汉市");
            add("北京市");
            add("天津市");
            add("吉林市");
            add("哈尔滨市");
            add("杭州市");
            add("乌鲁木齐市");
            add("锡林郭勒盟");
            add("苏州市");
            add("伊犁哈萨克自治州");
            add("吐鲁番市");
            add("厦门市");
            add("乌兰察布市");
            add("九江市");
            add("莆田市");
            add("鄂尔多斯市");
            add("济南市");
            add("南昌市");
            add("日喀则市");
            add("重庆市");
            add("铁岭市");
            add("巴音郭楞蒙古自治州");
            add("南京市");
            add("长沙市");
            add("扬州市");
            add("德阳市");
            add("临沂市");
            add("新乡市");
            add("太原市");
            add("拉萨市");
            add("成都市");
            add("榆林市");
            add("鹤岗市");
            add("葫芦岛市");
            add("沈阳市");
            add("白银市");
            add("周口市");
            add("阿克苏地区");
            add("芜湖市");
            add("上海市");
            add("烟台市");
            add("宁波市");
            add("石家庄市");
            add("郑州市");
            add("张掖市");
            add("三明市");
            add("南充市");
            add("银川市");
            add("辽源市");
            add("萍乡市");
            add("青岛市");
            add("忻州市");
            add("宿州市");
            add("日照市");
            add("巴彦淖尔市");
            add("绥化市");
            add("锦州市");
            add("合肥市");
            add("岳阳市");
            add("宜春市");
            add("呼伦贝尔市");
            add("西安市");
            add("山南市");
            add("邢台市");
            add("辽阳市");
            add("平顶山市");
            add("昌都市");
            add("兰州市");
            add("黄冈市");
            add("开封市");
            add("绵阳市");
            add("铜陵市");
            add("固原市");
            add("丽水市");
            add("张家口市");
            add("阜新市");
            add("嘉兴市");
            add("张家界市");
            add("甘孜藏族自治州");
            add("徐州市");
            add("安阳市");
            add("景德镇市");
            add("西宁市");
            add("聊城市");
            add("资阳市");
            add("镇江市");
            add("无锡市");
            add("喀什地区");
            add("焦作市");
            add("保定市");
            add("常德市");
            add("庆阳市");
            add("阳泉市");
            add("梧州市");
            add("十堰市");
            add("广元市");
            add("南阳市");
            add("漳州市");
            add("大连市");
            add("信阳市");
            add("哈密市");
            add("淮南市");
            add("荆门市");
            add("雅安市");
            add("长治市");
            add("通化市");
            add("承德市");
            add("抚州市");
            add("龙岩市");
            add("中卫市");
            add("晋城市");
            add("双鸭山市");
            add("齐齐哈尔市");
            add("郴州市");
            add("温州市");
            add("连云港市");
            add("鹰潭市");
            add("荆州市");
            add("鞍山市");
            add("阿勒泰地区");
            add("襄阳市");
            add("包头市");
            add("黑河市");
            add("长春市");
            add("恩施土家族苗族自治州");
            add("东营市");
            add("宜昌市");
            add("吉安市");
            add("衢州市");
            add("潍坊市");
            add("海南藏族自治州");
            add("淮安市");
            add("新余市");
            add("湘潭市");
            add("吴忠市");
            add("滁州市");
            add("洛阳市");
            add("阿拉尔市");
            add("沧州市");
            add("泉州市");
            add("泰州市");
            add("阜阳市");
            add("驻马店市");
            add("三门峡市");
            add("枣庄市");
            add("塔城地区");
            add("通辽市");
            add("赣州市");
            add("延边朝鲜族自治州");
            add("泰安市");
            add("赤峰市");
            add("林芝市");
            add("常州市");
            add("德州市");
            add("运城市");
            add("海东市");
            add("济宁市");
            add("吕梁市");
            add("南通市");
            add("蚌埠市");
            add("伊春市");
            add("亳州市");
            add("昌吉回族自治州");
            add("怀化市");
            add("大庆市");
            add("鸡西市");
            add("临汾市");
            add("台南市");
            add("盐城市");
            add("娄底市");
            add("和田地区");
            add("抚顺市");
            add("达州市");
            add("马鞍山市");
            add("许昌市");
            add("乐山市");
            add("随州市");
            add("唐山市");
            add("鹤壁市");
            add("菏泽市");
            add("攀枝花市");
            add("宝鸡市");
            add("果洛藏族自治州");
            add("邯郸市");
            add("商洛市");
            add("石河子市");
            add("白山市");
            add("邵阳市");
            add("那曲市");
            add("衡水市");
            add("丹东市");
            add("金昌市");
            add("遂宁市");
            add("湖州市");
            add("海西蒙古族藏族自治州");
            add("宁德市");
            add("自贡市");
            add("濮阳市");
            add("上饶市");
            add("营口市");
            add("滨州市");
            add("白城市");
            add("兴安盟");
            add("朝阳市");
            add("廊坊市");
            add("济源市");
            add("盘锦市");
            add("武威市");
            add("凉山彝族自治州");
            add("黄石市");
            add("台州市");
            add("渭南市");
            add("宣城市");
            add("昆明市");
            add("阿拉善盟");
            add("株洲市");
            add("大同市");
            add("松原市");
            add("威海市");
            add("佳木斯市");
            add("阿坝藏族羌族自治州");
            add("铜川市");
            add("广安市");
            add("咸宁市");
            add("绍兴市");
            add("孝感市");
            add("延安市");
            add("乌海市");
            add("陇南市");
            add("韶关市");
            add("湘西土家族苗族自治州");
            add("鄂州市");
            add("泸州市");
            add("牡丹江市");
            add("阳江市");
            add("仙桃市");
            add("天水市");
            add("巴中市");
            add("安庆市");
            add("晋中市");
            add("秦皇岛市");
            add("益阳市");
            add("内江市");
            add("克孜勒苏柯尔克孜自治州");
            add("嘉峪关市");
            add("定西市");
            add("宜宾市");
            add("金华市");
            add("潜江市");
            add("南平市");
            add("石嘴山市");
            add("汉中市");
            add("酒泉市");
            add("六安市");
            add("池州市");
            add("衡阳市");
            add("四平市");
            add("咸阳市");
            add("眉山市");
            add("玉树藏族自治州");
            add("舟山市");
            add("朔州市");
            add("本溪市");
            add("平凉市");
            add("永州市");
            add("黄山市");
            add("图木舒克市");
            add("大兴安岭地区");
            add("宿迁市");
            add("克拉玛依市");
            add("七台河市");
            add("商丘市");
            add("博尔塔拉蒙古自治州");
            add("漯河市");
            add("安康市");
            add("甘南藏族自治州");
            add("双河市");
            add("海北藏族自治州");
            add("淮北市");
            add("阿里地区");
            add("五家渠市");
        }
    };

    public static List<Map<String,Object>> errorResult(String area){

        HashMap<String,Object> map=new HashMap<>();
        map.put("描述:","参数错误");
        map.put("你输入的参数是:",area);
        List<Map<String,Object>> list=new ArrayList<>();
        list.add(map);
        return list;
    }
    public static Map<String,Object> jsonErrorResult(String area){

        HashMap<String,Object> map=new HashMap<>();
        map.put("描述:","输入的数据不是json格式的字符串");
        map.put("你输入的参数是:",area);
        return map;
    }
    public static final Map<String,String> levelMap=new HashMap(){
        {
           put("高风险","1");
           put("一般风险","2");
           put("低风险","3");
           put("无风险","0");
        }
    };
    public static void main(String[] args) {
        System.out.println(PROVINCE_Set);
    }
}
