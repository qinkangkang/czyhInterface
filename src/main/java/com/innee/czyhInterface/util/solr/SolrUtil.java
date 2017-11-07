package com.innee.czyhInterface.util.solr;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.innee.czyhInterface.dto.m.goods.GoodsDTO;
import com.innee.czyhInterface.dto.m.solr.GoodsSolrDTO;
import com.innee.czyhInterface.dto.m.system.SolrPageDTO;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.solr.model.SearchableGoodsInfo;

public class SolrUtil {

	private static String SolrServiceUrl = null;

	private static SolrServer solrServer = null;

	public static void init() {
		SolrServiceUrl = PropertiesUtil.getProperty("SolrService");

		solrServer = new HttpSolrServer(SolrServiceUrl);
	}

	public void solrSave(List<GoodsSolrDTO> goodsDTOList) {

		System.out.println(goodsDTOList);
		try {
			SolrInputDocument doucument = null;

			for (GoodsSolrDTO goodsDTO : goodsDTOList) {
				doucument = new SolrInputDocument();
				System.out.println(goodsDTO.getGoodsId());
				doucument.addField(SearchableGoodsInfo.ID_FIELD, goodsDTO.getGoodsId());
				doucument.addField(SearchableGoodsInfo.BARGAININGID_FIELD, goodsDTO.getBargainingId());
				doucument.addField(SearchableGoodsInfo.IMAGE_FIELD, goodsDTO.getImageUrl());
				doucument.addField(SearchableGoodsInfo.TYPE_FIELD, goodsDTO.getType());
				doucument.addField(SearchableGoodsInfo.CATEGORYID_FIELD, goodsDTO.getCategoryId());

				doucument.addField(SearchableGoodsInfo.SPEC_FIELD, goodsDTO.getSpec());
				doucument.addField(SearchableGoodsInfo.NAME_FIELD, goodsDTO.getGoodsTitle());

				doucument.addField(SearchableGoodsInfo.ORIGINALPRICE_FIELD, goodsDTO.getOriginalPrice());
				doucument.addField(SearchableGoodsInfo.PRESENTPRICE_FIELD, goodsDTO.getPresentPrice());

				doucument.addField(SearchableGoodsInfo.DESC_FIELD, goodsDTO.getDesc());
				doucument.addField(SearchableGoodsInfo.SPONSORNAME_FIELD, goodsDTO.getSponsorName());
				doucument.addField(SearchableGoodsInfo.STOCKFLAG_FIELD, goodsDTO.getStockFlag());
				doucument.addField(SearchableGoodsInfo.STATUS_FIELD, goodsDTO.getStatus());
				doucument.addField(SearchableGoodsInfo.SELLMODEL_FIELD, goodsDTO.getSellModel());
				doucument.addField(SearchableGoodsInfo.SPECMODEL_FIELD, goodsDTO.getSpecModel());

				doucument.addField(SearchableGoodsInfo.PROMOTIONMODEL_FIELD, goodsDTO.getPromotionModel());
				doucument.addField(SearchableGoodsInfo.LIMITATION_FIELD, goodsDTO.getLimitation());
				doucument.addField(SearchableGoodsInfo.PERCENTAGE_FIELD, goodsDTO.getPercentage());
				doucument.addField(SearchableGoodsInfo.SALETOTAL_FIELD, goodsDTO.getSaleTotal());

				doucument.addField(SearchableGoodsInfo.SDEALSMODEL_FIELD, goodsDTO.getSdealsModel());
				doucument.addField(SearchableGoodsInfo.USECOUPON_FIELD, goodsDTO.isUseCoupon());
				doucument.addField(SearchableGoodsInfo.IFINSTOCK_FIELD, goodsDTO.isIfInStock());
				doucument.addField(SearchableGoodsInfo.COUNT_FIELD, goodsDTO.getCount());

				doucument.addField(SearchableGoodsInfo.STATUSSTRING_FIELD, goodsDTO.getStatusString());
				doucument.addField(SearchableGoodsInfo.CREATETIME_FIELD, goodsDTO.getCreateTime());
				solrServer.add(doucument);
				solrServer.commit();
			}

		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SolrPageDTO querySolr(String searchValue, Integer categoryId, Integer orderBy, Integer pageSize,
			Integer offset) {

		SolrPageDTO solrPageDTO = new SolrPageDTO();
		SolrDocumentList solrDocumentList = new SolrDocumentList();
		SolrQuery query = null;
		try {

			if (StringUtils.isNoneBlank(searchValue)) {
				query = new SolrQuery(SearchableGoodsInfo.NAME_FIELD + ":" + searchValue);
				// 设置从第N条开始取
				query.setStart(offset);
				// 每次取出N条记录
				query.setRows(pageSize);
				if (orderBy.intValue() == 0) {
					query.setSort(SearchableGoodsInfo.CREATETIME_FIELD, ORDER.desc);
				} else if (orderBy.intValue() == 1) {
					query.setSort(SearchableGoodsInfo.SALETOTAL_FIELD, ORDER.desc);
				} else if (orderBy.intValue() == 2) {
					query.addSort(SearchableGoodsInfo.PRESENTPRICE_FIELD, ORDER.asc);
				} else if (orderBy.intValue() == 3) {
					query.addSort(SearchableGoodsInfo.PRESENTPRICE_FIELD, ORDER.desc);
				}
			} else if (categoryId != null) {
				// 条件查询 ："+"相当于and 注意:拼接两个条件时必须带空格否则不识别
				if (StringUtils.isNoneBlank(searchValue)) {
					query = new SolrQuery("+" + SearchableGoodsInfo.NAME_FIELD + ":" + searchValue + "  " + "+"
							+ SearchableGoodsInfo.CATEGORYID_FIELD + ":" + categoryId);
				} else {
					query = new SolrQuery(SearchableGoodsInfo.CATEGORYID_FIELD + ":" + categoryId);
				}

				query.setStart(offset);
				query.setRows(pageSize);

				if (orderBy.intValue() == 0) {
					query.setSort(SearchableGoodsInfo.CREATETIME_FIELD, ORDER.desc);
				} else if (orderBy.intValue() == 1) {
					query.setSort(SearchableGoodsInfo.SALETOTAL_FIELD, ORDER.desc);
				} else if (orderBy.intValue() == 2) {
					query.addSort(SearchableGoodsInfo.PRESENTPRICE_FIELD, ORDER.asc);
				} else if (orderBy.intValue() == 3) {
					query.addSort(SearchableGoodsInfo.PRESENTPRICE_FIELD, ORDER.desc);
				}
			}

			QueryResponse queryResponse = solrServer.query(query);
			solrDocumentList = queryResponse.getResults();

		} catch (SolrServerException e) {
			e.printStackTrace();
		}

		solrPageDTO.setSolrDocumentList(solrDocumentList);
		if (query.getStart() != null) {
			solrPageDTO.setOffset(query.getStart());
		}
		if (query.getStart() != null) {
			solrPageDTO.setPageSize(query.getRows());
		}
		solrPageDTO.setTotalCount(solrDocumentList.getNumFound());

		return solrPageDTO;
	}

	public void deleteSolr(String value) {

		try {
			solrServer.deleteByQuery("*:*");
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void solrSave(GoodsDTO goodsDTO) {

		try {
			SolrInputDocument doucument = new SolrInputDocument();

			doucument.addField(SearchableGoodsInfo.ID_FIELD, goodsDTO.getGoodsId());
			doucument.addField(SearchableGoodsInfo.IMAGE_FIELD, goodsDTO.getImageUrl());
			doucument.addField(SearchableGoodsInfo.TYPE_FIELD, goodsDTO.getType());

			doucument.addField(SearchableGoodsInfo.SPEC_FIELD, goodsDTO.getSpec());
			doucument.addField(SearchableGoodsInfo.NAME_FIELD, goodsDTO.getGoodsTitle());

			doucument.addField(SearchableGoodsInfo.ORIGINALPRICE_FIELD, goodsDTO.getOriginalPrice());
			doucument.addField(SearchableGoodsInfo.PRESENTPRICE_FIELD, goodsDTO.getPresentPrice());

			doucument.addField(SearchableGoodsInfo.DESC_FIELD, goodsDTO.getDesc());
			doucument.addField(SearchableGoodsInfo.SPONSORNAME_FIELD, goodsDTO.getSponsorName());
			doucument.addField(SearchableGoodsInfo.STOCKFLAG_FIELD, goodsDTO.getStockFlag());
			doucument.addField(SearchableGoodsInfo.STATUS_FIELD, goodsDTO.getStatus());
			doucument.addField(SearchableGoodsInfo.SELLMODEL_FIELD, goodsDTO.getSellModel());
			doucument.addField(SearchableGoodsInfo.SPECMODEL_FIELD, goodsDTO.getSpecModel());

			doucument.addField(SearchableGoodsInfo.PROMOTIONMODEL_FIELD, goodsDTO.getPromotionModel());
			doucument.addField(SearchableGoodsInfo.LIMITATION_FIELD, goodsDTO.getLimitation());
			doucument.addField(SearchableGoodsInfo.PERCENTAGE_FIELD, goodsDTO.getPercentage());
			doucument.addField(SearchableGoodsInfo.SALETOTAL_FIELD, goodsDTO.getSaleTotal());

			doucument.addField(SearchableGoodsInfo.SDEALSMODEL_FIELD, goodsDTO.getSdealsModel());
			doucument.addField(SearchableGoodsInfo.USECOUPON_FIELD, goodsDTO.isUseCoupon());
			doucument.addField(SearchableGoodsInfo.IFINSTOCK_FIELD, goodsDTO.isIfInStock());
			doucument.addField(SearchableGoodsInfo.COUNT_FIELD, goodsDTO.getCount());

			doucument.addField(SearchableGoodsInfo.STATUSSTRING_FIELD, goodsDTO.getStatusString());

			System.out.println(doucument.size() + "doucument多大");
			solrServer.add(doucument);
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
