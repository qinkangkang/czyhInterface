package com.innee.czyhInterface.util;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

public class DictionaryUtil {

	// 用户状态
	public static final long UserStatus = 1;

	// 客户类型
	public static final long CustomerType = 2;

	// 活动标签
	public static final long EventTag = 3;

	// 活动状态
	public static final long EventStatus = 4;

	// 支付类型
	public static final long PayType = 5;

	// 达人级别
	public static final long SponsorLevel = 6;

	// 达人类型
	public static final long SponsorType = 7;

	// 订单状态
	public static final long OrderStatus = 8;

	// 城市
	public static final long City = 9;

	// 订单类型
	public static final long OrderType = 10;

	// 活动场地类型
	public static final long SiteType = 11;

	// 活动场次状态
	public static final long SessionStatus = 12;

	// 活动规格状态
	public static final long SpecStatus = 13;

	// 活动场次名额类型
	public static final long SessionFlimitationType = 14;

	// 是否选项
	public static final long YesNo = 15;

	// 活动库存位置标志
	public static final long EventStockFlag = 16;

	// 图片表实体标志类型
	public static final long ImageEntityType = 17;

	// 评论类型
	public static final long CommentType = 18;

	// 性别
	public static final long Sex = 19;

	// 栏目轮播图链接类型
	public static final long ChannelSliderUrlType = 20;

	// 微信支付状态
	public static final long WxpayStatus = 21;

	// 发送短类型
	public static final long SmsType = 22;

	// 栏目默认排序类型
	public static final long ChannelDefaultOrderType = 23;

	// 客户端类型
	public static final long ClientType = 24;

	// 提现状态
	public static final long WithdrawalStatus = 25;

	// 收支类型
	public static final long BalanceType = 26;

	// 员工类别
	public static final long UserCategory = 27;

	// 活动时长
	public static final long EventDuration = 28;

	// 活动主标签
	public static final long EventMainTag = 29;

	// 结算方式
	public static final long SettlementType = 30;

	// 核算单状态
	public static final long VerificationStatus = 31;

	// 银行
	public static final long Bank = 32;

	// 收支明细状态
	public static final long BalanceStatus = 33;

	// 订单状态变更操作方
	public static final long OrderStatusChanger = 34;

	// 活动副标题配图
	public static final long EventSubtitleImg = 35;

	// 优惠券状态
	public static final long CouponStatus = 36;

	// 优惠券审核状态
	public static final long CouponAuditStatus = 37;

	// 优惠券适用范围
	public static final long CouponUseType = 38;

	// 优惠券发放形式
	public static final long CouponDeliverType = 39;

	// 活动适合年龄
	public static final long EventAge = 40;

	// 推送消息状态
	public static final long PushStatus = 41;

	// 发现文章状态
	public static final long ArticleStatus = 42;

	// 推送链接类型
	public static final long PushLinkTargetType = 43;

	// 推送时间类型
	public static final long PushTimeType = 45;

	// 推送目标用户
	public static final long PushUserType = 46;

	// 推送维度
	public static final long PushDimension = 47;

	// 用户标签
	public static final long PushUserTag = 48;

	// 版本号
	public static final long AppVersion = 49;

	// 推送审核状态
	public static final long PushfauditStatus = 50;

	// 活动详情类别
	public static final long EventTypeDetail = 51;

	// 用户注册渠道
	public static final long RegistrationChannel = 52;

	// 活动详情类别icon
	public static final long EventIcon = 53;

	// 优惠券使用终端
	public static final long UserPoint = 54;

	// 优惠券使用终端
	public static final long FomsModule = 55;

	// 栏目类型
	public static final long ChannelType = 56;

	// 公告类别
	public static final long FomsNotice = 57;

	// 公告状态
	public static final long NoticeStatus = 58;

	// 评论状态
	public static final long CommentStatus = 59;

	// 热搜词
	public static final long HotSearchWord = 60;

	// 核销类型
	public static final long VerificationType = 61;

	// 用户常用信息
	public static final long commonInfoType = 62;

	// 积分类型
	public static final long BonusType = 64;

	// 促销活动状态
	public static final long BonusStatus = 65;

	// 促销活动类型
	public static final long EventBonusType = 66;

	// 积分货品状态
	public static final long PosterStatus = 67;

	// 积分订单状态
	public static final long OrderBonusType = 68;

	// 地推码与地推人员映射
	public static final long SceneMap = 69;

	// 优惠券频道状态
	public static final long CouponChannelStatus = 70;

	// 扫码用户位置定义
	public static final long GpsRisk = 71;

	// 适用前端类型
	public static final long ChannelFrontType = 72;

	// 客户砍价状态
	public static final long CustomerBargainingStatus = 73;

	// 订单来源
	public static final long OrderSource = 74;

	// 活动促销类型
	public static final long EventSalesType = 75;

	// 售卖模式
	public static final long SellModel = 79;

	// 促销模式
	public static final long PromotionModel = 80;

	// 是否有规格
	public static final long SpecModel = 81;

	// 快递编码
	public static final long ExpressCode = 82;

	// 商户标签
	public static final long SponsorTag = 83;

	// 商品标签
	public static final long GoodsTag = 84;

	// 文章类型
	public static final long ArticleType = 85;

	// 文章类型
	public static final long BackGroundImage = 86;

	// 公告
	public static final long NoticeApp = 87;

	// 白菜头图
	public static final long SpecialImage = 89;
	
	// 推荐有礼字典
	public static final long Invitation = 90;

	// 邀请有礼
	public static final long InvitationCoupon = 91;

	// 邀请有礼
	public static final long placeholderWord = 95;

	// 今日秒杀商品类型
	public static final long TodaySeckillType = 96;

	// 今日秒杀促销类型
	public static final long SeckillModuleType = 97;

	// 栏目头图
	public static final long ColumnBanner = 98;

	// 时间戳模块
	public static final long TimeStampModel = 99;
	
	// 退款原因
	public static final long RefundReason = 101;
	
	// 退款类型
	public static final long RefundType = 102;
	
	// 退款类型
	public static final long GoodsLabel = 103;
	
	// 店铺热搜词
	public static final long SponsorSearch = 104;
	
	// 字典数据缓存容器
	private static Map<Long, Map<String, Map<Integer, String>>> statusClassMap = Maps.newHashMap();

	// name与code对应的数据字典缓存容器
	private static Map<Long, Map<String, String>> nameCodeMap = Maps.newHashMap();

	public static String getString(Long classId, Integer value) {
		if (value == null) {
			return StringUtils.EMPTY;
		}
		return getString(classId, value, Constant.defaultLanguage);
	}

	public static String getString(Long classId, Integer value, String language) {
		if (value == null) {
			return StringUtils.EMPTY;
		}
		Map<String, Map<Integer, String>> codeMainMap = statusClassMap.get(classId);
		Map<Integer, String> codeMap = codeMainMap.get(language);
		return StringUtils.trimToEmpty(codeMap.get(value));
	}

	public static String getCode(Long classId, Integer value) {
		if (value == null) {
			return StringUtils.EMPTY;
		}
		return getCode(classId, value, Constant.englishLanguage);
	}

	public static String getCode(Long classId, Integer value, String language) {
		if (value == null) {
			return StringUtils.EMPTY;
		}
		Map<String, Map<Integer, String>> codeMainMap = statusClassMap.get(classId);
		Map<Integer, String> codeMap = codeMainMap.get(language);
		return StringUtils.trimToEmpty(codeMap.get(value));
	}

	public static Map<Integer, String> getStatueMap(Long classId) {
		return getStatueMap(classId, Constant.defaultLanguage);
	}

	public static Map<Integer, String> getStatueMap(Long classId, String language) {
		if (StringUtils.isBlank(language)) {
			language = Constant.defaultLanguage;
		}
		Map<String, Map<Integer, String>> codeMap = statusClassMap.get(classId);
		return codeMap.get(language);
	}

	public static String getName(Long classId, String code) {
		if (StringUtils.isBlank(code)) {
			return StringUtils.EMPTY;
		}
		return getNameCodeMap().get(classId).get(code);
	}

	public static Map<Long, Map<String, String>> getNameCodeMap() {
		return nameCodeMap;
	}

	public static Map<Long, Map<String, Map<Integer, String>>> getCodeCalssMap() {
		return statusClassMap;
	}

	public static void setCodeCalssMap(Map<Long, Map<String, Map<Integer, String>>> codeCalssMap,
			Map<Long, Map<String, String>> codeMap) {
		statusClassMap = codeCalssMap;
		nameCodeMap = codeMap;
	}

	public static void clear() {
		statusClassMap = null;
		nameCodeMap = null;
	}
}