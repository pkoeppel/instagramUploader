package org.fsv.instagramuploader.youth.controller;

import org.fsv.instagramuploader.model.MatchModel;
import org.fsv.instagramuploader.model.ResultModel;
import org.fsv.instagramuploader.youth.pictureCreator.MatchdaysCreator;
import org.fsv.instagramuploader.youth.pictureCreator.ResultsCreator;
import org.json.simple.JSONArray;
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

@RestController
public class YouthController {
	final MatchdaysCreator msc;
	final ResultsCreator rsc;
	
	public YouthController(MatchdaysCreator msc, ResultsCreator rsc) {
		this.msc = msc;
		this.rsc = rsc;
	}
	
	@RequestMapping("/createMatchFilesKids")
	public ResponseEntity<File> createMatchFilesKids(@RequestBody ArrayList<MatchModel> mmArr) throws IOException, ParseException {
		File result = msc.createMatches(mmArr);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@RequestMapping("/getAllKidsMatches")
	public ResponseEntity<JSONArray> getAllKidsMatches() throws IOException, ParseException {
		JSONArray arr = (JSONArray) new JSONParser()
										.parse(new FileReader("src\\main\\resources\\templates\\kids-games.json"));
		return new ResponseEntity<>(arr, HttpStatus.OK);
	}
	
	@RequestMapping("/createKidsResults")
	public ResponseEntity<File> createKidsResult(@RequestBody ArrayList<ResultModel> rmArr) throws IOException, ParseException {
		File result = rsc.createResults(rmArr);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
