package com.gexin.artplatform.bean;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;

import com.gexin.artplatform.utils.PinYin;

@SuppressLint("DefaultLocale")
public class CityItem implements Comparable<CityItem>{

	private static String[] citys = { "合肥", "宿州", "淮北", "阜阳", "蚌埠", "淮南", "滁州",
			"马鞍山", "芜湖", "铜陵", "安庆", "黄山", "六安", "池州", "宣城", "亳州", " 福州", "南平",
			"三明", "莆田", "泉州", "漳州", "龙岩", "宁德", "兰州", "嘉峪关", "金昌", "白银", "天水",
			"酒泉", "张掖", "武威", "庆阳", "平凉", "定西", "陇南", "厦门", "广州", "深圳", "清远",
			"韶关", "河源", "梅州", "潮州", "汕头", "揭阳", "汕尾", "惠州", "东莞", "珠海", "中山",
			"江门", "佛山", "肇庆", "云浮", "阳江", "茂名", "湛江", "贵阳", "六盘水", "遵义", "安顺",
			"毕节", "铜仁", "石家庄", "邯郸", "唐山", "保定", "秦皇岛", "邢台", "张家口", "承德",
			"沧州", "廊坊", "衡水", "哈尔滨", "齐齐哈尔", "黑河", "大庆", "伊春", "鹤岗", "佳木斯",
			"双鸭山", "七台河", "鸡西", "牡丹江", "绥化", "郑州", "开封", "洛阳", "平顶山", "安阳",
			"鹤壁", "新乡", "焦作", "濮阳", "许昌", "漯河", "三门峡", "南阳", "商丘", "周口", "驻马店",
			"信阳", "武汉", "十堰", "襄阳", "荆门", "孝感", "黄冈", "鄂州", "黄石", "咸宁", "荆州",
			"宜昌", "随州", "长沙", "衡阳", "张家界", "常德", "益阳", "岳阳", "株洲", "湘潭", "郴州",
			"永州", "邵阳", "怀化", "娄底", "长春", "吉林", "白城", "松原", "四平", "辽源", "通化",
			"白山", "南昌", "九江", "景德镇", "鹰潭", "新余", "萍乡", "赣州", "上饶", "抚州", "宜春",
			"吉安 ", "南京", "徐州", "连云港", "宿迁", "淮安", "盐城", "扬州", "泰州", "南通", "镇江",
			"常州", "无锡", "苏州", "沈阳", "大连", "朝阳", "阜新", "铁岭", "抚顺", "本溪", "辽阳",
			"鞍山", "丹东", "营口", "盘锦", "锦州", "葫芦岛", "济南", "青岛", "聊城", "德州", "东营",
			"淄博", "潍坊", "烟台", "威海", "日照", "临沂", "枣庄", "济宁", "泰安", "莱芜", "滨州",
			"菏泽", "西安", "延安", "铜川", "渭南", "咸阳", "宝鸡", "汉中", "榆林", "商洛", "安康",
			"太原", "大同", "朔州", "阳泉", "长治", "晋城", "忻州", "吕梁", "晋中", "临汾", "运城",
			"成都", "广元", "绵阳", "德阳", "南充", "广安", "遂宁", "内江", "乐山", "自贡", "泸州",
			"宜宾", "攀枝花", "巴中", "达州", "资阳", "眉山", "雅安", "昆明", "曲靖", "玉溪", "丽江",
			"昭通", "普洱", "临沧", "保山", "杭州", "宁波", "湖州", "嘉兴", "舟山", "绍兴", "衢州",
			"金华", "台州", "温州", "丽水", "西宁", "海东", "海口", "三亚", "三沙", "儋州", "南宁",
			"桂林", "柳州", "梧州", "贵港", "玉林", "钦州", "北海", "防城港", "崇左", "百色", "河池",
			"来宾", "贺州", "呼和浩特", "包头", "乌海", "赤峰", "呼伦贝尔", "通辽", "乌兰察布", "鄂尔多斯",
			"巴彦淖尔", "银川", "石嘴山", "吴忠", "中卫", "固原", "拉萨", "日喀则", "昌都", "林芝",
			"乌鲁木齐", "克拉玛依", "吐鲁番", "香港", "澳樵 ","北京","上海","重庆" };

	private String name;
	private String fullPinyin;
	private String simplePinyin;
	private String sortLetter;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		try {
			this.fullPinyin = PinYin.getPinYin(name);
			this.simplePinyin = PinYin.getSimplePinYin(name);
			this.sortLetter = fullPinyin.substring(0, 1).toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getFullPinyin() {
		return fullPinyin;
	}

	public String getSimplePinyin() {
		return simplePinyin;
	}

	public String getSortLetter() {
		return sortLetter;
	}

	public static List<CityItem> getCityList() {
		List<CityItem> list = new ArrayList<CityItem>();
		for (String str : citys) {
			CityItem item = new CityItem();
			item.setName(str);
			list.add(item);
		}
		return list;
	}

	@Override
	public String toString() {
		return "CityItem [name=" + name + ", fullPinyin=" + fullPinyin
				+ ", simplePinyin=" + simplePinyin + ", sortLetter="
				+ sortLetter + "]";
	}

	@Override
	public int compareTo(CityItem arg0) {
		
		return this.fullPinyin.compareTo(arg0.fullPinyin);
	}

}
