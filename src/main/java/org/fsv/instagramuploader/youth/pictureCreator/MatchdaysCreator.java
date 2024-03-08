package org.fsv.instagramuploader.youth.pictureCreator;

import org.fsv.instagramuploader.ClubSelector;
import org.fsv.instagramuploader.FontClass;
import org.fsv.instagramuploader.Helper;
import org.fsv.instagramuploader.model.ClubModel;
import org.fsv.instagramuploader.model.MatchModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component("msc")
public class MatchdaysCreator {
	private static Helper h;
	private static ClubSelector getClub;
	private static FileWriter fw;
	private static BufferedImage background;
	private List<LocalDate> matchDates;
	
	public File createMatches(ArrayList<MatchModel> mmArr) throws IOException, ParseException {
		matchDates = new ArrayList<>();
		background = ImageIO.read(new File("src\\main\\resources\\pictures\\template\\youth\\matchdayTemp.jpg"));
		h = new Helper(background);
		getClub = new ClubSelector();
		int blockStart = 530;
		int pageCount = 1;
		for (MatchModel m : mmArr) {
			if (blockStart > 2100) {
				blockStart = 530;
				String savePathPart = h.createMatchdaysHead(matchDates);
				savePicture(savePathPart, pageCount);
				background = ImageIO.read(new File("src\\main\\resources\\pictures\\template\\youth\\matchdayTemp.jpg"));
				h = new Helper(background);
				pageCount++;
			}
			Graphics g = background.getGraphics();
			g.setColor(Color.GRAY);
			int[] polyX = {0, 275, 250, 0};
			int[] polyY = {blockStart, blockStart, blockStart + 100, blockStart + 100};
			g.fillPolygon(polyX, polyY, polyY.length);
			checkMatchDate(m.matchDate());
			if (m.matchType() == null || m.matchType().equals("kidsMatch")) {
				emptyBlock(m, blockStart);
			} else {
				filledBlock(m, blockStart);
			}
			h.writeOnPicture(m.team(), "team-name", FontClass.teamKids, Color.BLACK, blockStart);
			blockStart += 220;
		}
		String savePathPart = h.createMatchdaysHead(matchDates);
		fw.close();
		return savePicture(savePathPart, pageCount);
	}
	
	@SuppressWarnings("unchecked")
	private void writeTempTxt(MatchModel m) throws IOException {
		JSONParser jp = new JSONParser();
		JSONArray ja = new JSONArray();
		try {
			ja = (JSONArray) jp.parse(new FileReader("src\\main\\resources\\templates\\kids-games.json"));
			
			JSONObject gameDetails = new JSONObject();
			gameDetails.put("team", m.team());
			gameDetails.put("matchType", m.getMatchType());
			gameDetails.put("homeGame", m.homeGame());
			gameDetails.put("opponent", m.opponent());
			gameDetails.put("oppName", m.oppName());
			gameDetails.put("date", m.matchDate());
			h.saveTempTxt(ja, gameDetails);
		} catch (ParseException ignored) {
		}
		
		
		fw = new FileWriter("src\\main\\resources\\templates\\kids-games.json");
		fw.write(ja.toJSONString());
		fw.flush();
	}
	
	private void filledBlock(MatchModel m, int startPoint) throws IOException, ParseException {
		ClubModel ownClub = getClub.getClubDetails("SpG Treuener Land");
		ClubModel oppClub = getClub.getClubDetails(m.opponent());
		
		h.writeOnPicture("(" + m.getMatchType() + ")", "match-type-short", FontClass.simpleKids, Color.BLACK, startPoint);
		
		String gamePlace = "Sportplatz ";
		String oppTeamName = m.opponent();
		if (!m.oppName().isEmpty()) {
			oppTeamName = m.oppName();
		}
		
		if (Boolean.parseBoolean(m.homeGame())) {
			gamePlace += m.homePlace();
			h.writeOnPicture("SpG Treuener Land", "club-name-home", FontClass.clubOwnKids, Color.BLACK, startPoint);
			h.writeOnPicture(Helper.wrapString(oppTeamName, 23), "club-name-away", FontClass.simpleKids, Color.BLACK, startPoint);
			h.pictureOnPicture(ownClub.clubLogo(), "logo-left-youth", startPoint);
			h.pictureOnPicture(oppClub.clubLogo(), "logo-right-youth", startPoint);
		} else {
			gamePlace += oppClub.clubPlace();
			h.writeOnPicture("SpG Treuener Land", "club-name-away", FontClass.clubOwnKids, Color.BLACK, startPoint);
			h.writeOnPicture(Helper.wrapString(oppTeamName, 23), "club-name-home", FontClass.simpleKids, Color.BLACK, startPoint);
			h.pictureOnPicture(oppClub.clubLogo(), "logo-left-youth", startPoint);
			h.pictureOnPicture(ownClub.clubLogo(), "logo-right-youth", startPoint);
		}
		String bottom = m.fullMatchDate() + " - " + m.matchTime() + " Uhr" + "\n" + gamePlace;
		h.writeOnPicture(bottom, "bottom-center", FontClass.simpleKids, Color.BLACK, startPoint);
		h.writeOnPicture(":", "center-point", FontClass.simpleKids, Color.BLACK, startPoint);
		writeTempTxt(m);
	}
	
	private void emptyBlock(MatchModel m, int startPoint) throws IOException, ParseException {
		ClubModel club = getClub.getClubDetails("SpG Treuener Land");
		ClubModel oppClub = getClub.getClubDetails(m.opponent());
		h.pictureOnPicture(club.clubLogo(), "logo-left-youth", startPoint);
		if (m.matchType() == null) {
			h.writeOnPicture("Spielfrei!", "matchType", FontClass.mTypeKids, Color.BLACK, startPoint);
		} else {
			String gamePlace = m.homePlace();
			if (Boolean.parseBoolean(m.homeGame())) {
				gamePlace = oppClub.clubPlace();
			}
			h.writeOnPicture("(" + m.getMatchType() + ")", "match-type-short", FontClass.simpleKids, Color.BLACK, startPoint);
			h.writeOnPicture(m.getMatchType() + " (Sportplatz " + gamePlace + ")!", "matchType", FontClass.mTypeKids, Color.BLACK, startPoint);
			writeTempTxt(m);
		}
	}
	
	private void checkMatchDate(String date) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		LocalDate formattedDate = LocalDate.parse(date, dtf);
		if (!matchDates.contains(formattedDate)) {
			matchDates.add(formattedDate);
			Collections.sort(matchDates);
		}
	}
	
	private File savePicture(String datePart, int matchCount) throws IOException {
		File directory = new File("save\\kids\\" + datePart);
		if (!directory.exists()) {
			//noinspection ResultOfMethodCallIgnored
			directory.mkdir();
		}
		ImageIO.write(background, "png", new File(directory + "\\" + "Matchday" + matchCount + ".png"));
		return new File(directory + "\\" + "Matchday" + matchCount + ".png");
	}
	
	
}
