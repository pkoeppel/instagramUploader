package org.fsv.instagramuploader.men.controller;

import org.fsv.instagramuploader.men.pictureCreator.KickoffCreator;
import org.fsv.instagramuploader.men.pictureCreator.MatchdayCreator;
import org.fsv.instagramuploader.model.KickoffModel;
import org.fsv.instagramuploader.model.MatchModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@RestController
public class MenController {
	
	final
	MatchdayCreator mc;
	final
	KickoffCreator kc;
	
	public MenController(MatchdayCreator mc, KickoffCreator kc) {
		this.mc = mc;
		this.kc = kc;
	}
	
	@RequestMapping("/createLeagueMatchFile")
	public ResponseEntity<File> createLeagueMatchFile(@RequestBody MatchModel match)
									throws IOException, ParseException {
		File result = mc.createMatch(match);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@RequestMapping("/createCupMatchFile")
	public ResponseEntity<File> createCupMatchFile(@RequestBody MatchModel match)
									throws IOException, ParseException {
		File result = mc.createMatch(match);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@RequestMapping("/getAllTeams")
	public ResponseEntity<ArrayList<String>> getAllTeams() throws IOException, ParseException {
		ArrayList<String> result = new ArrayList<>();
		JSONObject obj = (JSONObject) new JSONParser()
										.parse(new FileReader("src\\main\\resources\\templates\\clubs.json"));
		for (Object key : obj.keySet()) {
			result.add(key.toString());
		}
		Collections.sort(result);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@RequestMapping("/createFriendMatchFile")
	public ResponseEntity<File> createFriendMatchFile(@RequestBody MatchModel match)
									throws IOException, ParseException {
		File result = mc.createMatch(match);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@RequestMapping("/createKickoffFile")
	public ResponseEntity<File> createKickoffFile(@RequestBody KickoffModel model)
									throws IOException, ParseException {
		File result = kc.createKickoff(model);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@RequestMapping("/getAllMenMatches")
	public ResponseEntity<JSONArray> getAllMenMatches() throws IOException, ParseException {
		JSONArray arr = (JSONArray) new JSONParser()
										.parse(new FileReader("src\\main\\resources\\templates\\men-games.json"));
		return new ResponseEntity<>(arr, HttpStatus.OK);
	}
}
