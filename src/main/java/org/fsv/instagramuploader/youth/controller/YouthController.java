package org.fsv.instagramuploader.youth.controller;

import org.fsv.instagramuploader.model.MatchModel;
import org.fsv.instagramuploader.model.ResultModel;
import org.fsv.instagramuploader.youth.pictureCreator.MatchdaysCreator;
import org.fsv.instagramuploader.youth.pictureCreator.ResultsCreator;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

@RestController
public class YouthController {
	final MatchdaysCreator msc;
	final ResultsCreator rsc;
	
	public YouthController(MatchdaysCreator msc, ResultsCreator rsc) {
		this.msc = msc;
		this.rsc = rsc;
	}
	
	@GetMapping("/download/youth/{pathName}/{fileName:.+}")
	public ResponseEntity<?> downloadLocalFile(@PathVariable String pathName, @PathVariable String fileName) throws MalformedURLException {
		Path path = Paths.get("save/youth/" + pathName + "/" + fileName);
		Resource res = new UrlResource(path.toUri());
		return ResponseEntity.ok()
										.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + res.getFilename() + "\"")
										.contentType(MediaType.parseMediaType("application/octet-stream"))
										.body(res);
	}
	
	@RequestMapping("/createMatchFilesYouth")
	public ResponseEntity<?> createMatchFilesYouth(@RequestBody ArrayList<MatchModel> mmArr) throws IOException, ParseException {
		Map<String, Integer> result = msc.createMatches(mmArr);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@RequestMapping("/getAllYouthMatches")
	public ResponseEntity<JSONArray> getAllYouthMatches() throws IOException, ParseException {
		JSONArray arr = (JSONArray) new JSONParser()
										.parse(new FileReader("src\\main\\resources\\templates\\youth-games.json"));
		return new ResponseEntity<>(arr, HttpStatus.OK);
	}
	
	@RequestMapping("/createYouthResults")
	public ResponseEntity<?> createYouthResult(@RequestBody ArrayList<ResultModel> rmArr) throws IOException, ParseException {
		Map<String, Integer> result = rsc.createResults(rmArr);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
