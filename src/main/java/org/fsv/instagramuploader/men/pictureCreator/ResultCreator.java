package org.fsv.instagramuploader.men.pictureCreator;

import org.fsv.instagramuploader.ClubSelector;
import org.fsv.instagramuploader.Helper;
import org.fsv.instagramuploader.model.ClubModel;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
@Component("rc")
public class ResultCreator {
	BufferedImage targetImg;
	final ArrayList<BufferedImage> allImg = new ArrayList<>();
	
	public JSONObject addLogos(JSONObject match) throws IOException, ParseException {
		Helper h = new Helper(targetImg);
		JSONObject m = h.parser(match.get("match").toString());
		String mType = m.get("matchType").toString();
		String mOpp = m.get("opponent").toString();
		String mDate = m.get("matchDate").toString();
		String mHome = m.get("homeGame").toString();
		String date = DateTimeFormatter.ofPattern("yyyyMMdd").format(DateTimeFormatter.ofPattern("dd.MM.yyyy").parse(mDate));
		ClubSelector getClub = new ClubSelector();
		ClubModel ownClub = getClub.getClubDetails("FSV Treuen");
		ClubModel oppClub = getClub.getClubDetails(mOpp);
		
		String matchResult;
		if(Boolean.parseBoolean(mHome)){
			matchResult = ownClub.clubName() + " : " + oppClub.clubName() + " (" + match.get("result") + ")\n\n";
		} else {
			matchResult = oppClub.clubName() + " : " + ownClub.clubName() + " (" + match.get("result") + ")\n\n";
		}
		
		String oppRep = "";
		if (!match.get("reporterOpp").equals("") && !match.get("reportOpp").equals("")){
			oppRep = match.get("reporterOpp") + " (" + oppClub.clubName() + "):\n" + match.get("reportOpp") + "\n";
		}
		
		String ownRep = "";
		if (!match.get("reporterOwn").equals("") && !match.get("reportOwn").equals("")){
			ownRep = match.get("reporterOwn") + " (" + ownClub.clubName() + "):\n" + match.get("reportOwn") + "\n";
		}
		
		String fullReport = matchResult + match.get("headline") + "\uD83D\uDD34⚪ \n\n \uD83D\uDCF8: @arminvogtland @netti_1909 \n\n"
										+ match.get("report") + "\n\nStimmen zum Spiel:\n" + oppRep + ownRep + "\n" +match.get("future");
		
		String saveName = getClub.getClubDetails(mOpp).saveClubName();
		int imgCount = 0;
		String dir = date + "_" + mType + "_" + saveName;
		File directory = new File("save/" + dir + "/Bilder");
		if (!directory.exists()) {
			//noinspection ResultOfMethodCallIgnored
			directory.mkdir();
		}
		for (BufferedImage img : allImg){
			h = new Helper(img);
			h.pictureOnPicture(oppClub.clubLogo(), "smallClubResult-men", 0);
			h.pictureOnPicture(ownClub.clubLogo(), "bigClubResult-men", 0);
			File fileToSave = new File(directory + "/" + "Pic" + imgCount + ".jpeg");
			ImageIO.write(img, "jpeg",fileToSave);
			imgCount++;
		}
		h.deleteTempTxt(m, "men-games-result");
		JSONObject result = new JSONObject();
		result.put("fileDir", dir);
		result.put("caption", fullReport);
		return result;
	}
	
	public void savePicture(JSONObject c, MultipartFile file) throws IOException {
		targetImg = new BufferedImage(1365, 1365, BufferedImage.TYPE_INT_RGB);
		BufferedImage image = ImageIO.read(file.getInputStream());
		BufferedImage subImg = image.getSubimage(Helper.getC(c, "x"),Helper.getC(c, "y"),Helper.getC(c, "w"),Helper.getC(c, "h"));
		Graphics2D g2 = targetImg.createGraphics();
		g2.drawImage(subImg,0,0, 1365,1365, null);
		allImg.add(targetImg);
	}
}
