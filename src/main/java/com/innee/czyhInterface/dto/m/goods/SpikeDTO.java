package com.innee.czyhInterface.dto.m.goods;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class SpikeDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String seckillTypeTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String seckillTypeimageUrl;

	private List<SpikeGoodsDTO> SpikeGoodsDTOList;// 商品列表list

	public String getSeckillTypeTitle() {
		return seckillTypeTitle;
	}

	public void setSeckillTypeTitle(String seckillTypeTitle) {
		this.seckillTypeTitle = seckillTypeTitle;
	}

	public String getSeckillTypeimageUrl() {
		return seckillTypeimageUrl;
	}

	public void setSeckillTypeimageUrl(String seckillTypeimageUrl) {
		this.seckillTypeimageUrl = seckillTypeimageUrl;
	}

	public List<SpikeGoodsDTO> getSpikeGoodsDTOList() {
		return SpikeGoodsDTOList;
	}

	public void setSpikeGoodsDTOList(List<SpikeGoodsDTO> spikeGoodsDTOList) {
		SpikeGoodsDTOList = spikeGoodsDTOList;
	}
	
}