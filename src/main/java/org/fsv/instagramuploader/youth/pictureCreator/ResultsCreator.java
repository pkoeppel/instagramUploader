package org.fsv.instagramuploader.youth.pictureCreator;

import org.fsv.instagramuploader.ClubSelector;
import org.fsv.instagramuploader.FontClass;
import org.fsv.instagramuploader.Helper;
import org.fsv.instagramuploader.model.ClubModel;
import org.fsv.instagramuploader.model.ResultModel;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component("rsc")
public class ResultsCreator {
	private static BufferedImage background;
	private List<LocalDate> matchDates;
	
	public File createResults(ArrayList<ResultModel> rmArr) throws IOException, ParseException {
		background = ImageIO.read(new File("src\\main\\resources\\pictures\\template\\youth\\resultTemp.jpg"));
		matchDates = new ArrayList<>();
		Helper h = new Helper(background);
		int blockStart = 500;
		int pageCount = 1;
		for (ResultModel rm : rmArr) {
			if (blockStart > 1100) {
				blockStart = 530;
				String savePathPart = h.createMatchdaysHead(matchDates);
				savePicture(savePathPart, pageCount);
				background = ImageIO.read(new File("src\\main\\resources\\pictures\\template\\youth\\resultTemp.jpg"));
				h = new Helper(background);
				pageCount++;
			}
			
			Graphics g = background.getGraphics();
			g.setColor(Color.GRAY);
			int[] polyX = {0, 275, 250, 0};
			int[] polyY = {blockStart, blockStart, blockStart + 100, blockStart + 100};
			g.fillPolygon(polyX, polyY, polyY.length);
			
			h.writeOnPicture(rm.getValue("team"), "team-name", FontClass.teamKids, Color.BLACK, blockStart);
			
			h.writeOnPicture("(" + rm.getMatchType() + ")", "match-type-short", FontClass.simpleKids, Color.BLACK, blockStart);
			
			checkMatchDate(rm.getValue("date"));
			
			ClubSelector getClub = new ClubSelector();
			ClubModel ownClub = getClub.getClubDetails("SpG Treuener Land");
			ClubModel oppClub = getClub.getClubDetails(rm.getValue("opponent"));
			if (rm.text().equals("Abgesagt")) {
				h.pictureOnPicture(ownClub.clubLogo(), "logo-left-youth", blockStart);
				h.pictureOnPicture(oppClub.clubLogo(), "logo-right-youth", blockStart);
				h.writeOnPicture("Abgesagt!", "center-point", FontClass.clubOwnKids, Color.BLACK, blockStart);
			} else {
				if (rm.getValue("matchType").equals("Kinderfest")) {
					h.pictureOnPicture(ownClub.clubLogo(), "logo-left-youth", blockStart);
					h.writeOnPicture("Kinderfest!", "center-point", FontClass.clubOwnKids, Color.BLACK, blockStart);
				} else {
					String ownTeamText = "SpG Treuener Land";
					String oppTeamText = Helper.wrapString(rm.getValue("oppName"), 23);
					if (rm.getValue("matchType").equals("Liga")) {
						ownTeamText += "\n" + rm.ownStats();
						oppTeamText += "\n" + rm.oppStats();
					}
					if (Boolean.parseBoolean(rm.getValue("homeGame"))) {
						h.writeOnPicture(ownTeamText, "club-name-home", FontClass.simpleKids, Color.BLACK, blockStart);
						h.writeOnPicture(oppTeamText, "club-name-away", FontClass.simpleKids, Color.BLACK, blockStart);
						h.pictureOnPicture(ownClub.clubLogo(), "logo-left-youth", blockStart);
						h.pictureOnPicture(oppClub.clubLogo(), "logo-right-youth", blockStart);
					} else {
						h.writeOnPicture(ownTeamText, "club-name-away", FontClass.simpleKids, Color.BLACK, blockStart);
						h.writeOnPicture(oppTeamText, "club-name-home", FontClass.simpleKids, Color.BLACK, blockStart);
						h.pictureOnPicture(oppClub.clubLogo(), "logo-left-youth", blockStart);
						h.pictureOnPicture(ownClub.clubLogo(), "logo-right-youth", blockStart);
					}
					h.writeOnPicture(rm.result(), "center-point", FontClass.resultKids, Color.BLACK, blockStart);
				}
			}
			blockStart += 200;
			h.deleteTempTxt(rm.id(), "kids-games");
		}
		String savePathPart = h.createMatchdaysHead(matchDates);
		return savePicture(savePathPart, pageCount);
	}
	
	private File savePicture(String datePart, int matchCount) throws IOException {
		File directory = new File("save\\kids\\" + datePart);
		if (!directory.exists()) {
			//noinspection ResultOfMethodCallIgnored
			directory.mkdir();
		}
		ImageIO.write(background, "png", new File(directory + "\\" + "Result" + matchCount + ".png"));
		return new File(directory + "\\" + "Result" + matchCount + ".png");
	}
	
	private void checkMatchDate(String date) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		LocalDate formattedDate = LocalDate.parse(date, dtf);
		if (!matchDates.contains(formattedDate)) {
			matchDates.add(formattedDate);
			Collections.sort(matchDates);
		}
	}
}
