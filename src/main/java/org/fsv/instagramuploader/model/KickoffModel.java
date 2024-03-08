package org.fsv.instagramuploader.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public record KickoffModel(String match, String playerPic) {
	public JSONObject getMatch() throws ParseException {
		JSONParser jp = new JSONParser();
		return (JSONObject) jp.parse(match);
	}
}
