package com.innee.czyhInterface.dto.m;

import java.io.Serializable;

public class SceneStrDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String sceneStr;
	
	private Integer sceneID = 0;

	public SceneStrDTO(String sceneStr, Integer sceneID) {
		this.sceneStr = sceneStr;
		this.sceneID = sceneID;
	}

	public String getSceneStr() {
		return sceneStr;
	}

	public void setSceneStr(String sceneStr) {
		this.sceneStr = sceneStr;
	}

	public Integer getSceneID() {
		return sceneID;
	}

	public void setSceneID(Integer sceneID) {
		this.sceneID = sceneID;
	}

}
