/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.innee.czyhInterface.util.solr.model;

/**
 * solr实体类
 * 
 * @author jinsey
 *
 */
public interface SearchableGoodsInfo {

	String ID_FIELD = "id";
	String BARGAININGID_FIELD = "bargainingId_s";
	String IMAGE_FIELD = "imageUrl_s";
	String TYPE_FIELD = "type_s";
	String CATEGORYID_FIELD = "categoryId_i";
	String SPEC_FIELD = "spec_s";
	String NAME_FIELD = "name";

	String ORIGINALPRICE_FIELD = "originalPrice_f";
	String PRESENTPRICE_FIELD = "presentPrice_f";
	String DESC_FIELD = "desc_s";
	String SPONSORNAME_FIELD = "sponsorName_s";
	String STOCKFLAG_FIELD = "stockFlag_i";
	String STATUS_FIELD = "status_i";
	String SELLMODEL_FIELD = "sellModel_i";
	String SPECMODEL_FIELD = "specModel_i";

	String PROMOTIONMODEL_FIELD = "promotionModel_i";
	String LIMITATION_FIELD = "limitation_i";
	String PERCENTAGE_FIELD = "percentage_i";
	String SALETOTAL_FIELD = "saleTotal_i";
	String SDEALSMODEL_FIELD = "sdealsModel_i";
	String USECOUPON_FIELD = "useCoupon_b";
	String IFINSTOCK_FIELD = "ifInStock_b";
	String COUNT_FIELD = "count_i";
	String STATUSSTRING_FIELD = "statusString_s";
	String CREATETIME_FIELD = "createtime";

}
