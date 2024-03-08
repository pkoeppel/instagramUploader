package org.fsv.instagramuploader.men.pictureCreator;

import org.fsv.instagramuploader.ClubSelector;
import org.fsv.instagramuploader.FontClass;
import org.fsv.instagramuploader.Helper;
import org.fsv.instagramuploader.model.ClubModel;
import org.fsv.instagramuploader.model.KickoffModel;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Component("kc")
public class KickoffCreator {
	public File createKickoff(KickoffModel model) throws IOException, ParseException {
		BufferedImage image = ImageIO.read(new File("src\\main\\resources\\pictures\\template\\men\\kickoffTemp.jpg"));
		Helper h = new Helper(image);
		JSONObject m = model.getMatch();
		String mType = m.get("matchType").toString();
		String mOpp = m.get("opponent").toString();
		String mDate = m.get("matchDate").toString();
		String date = DateTimeFormatter.ofPattern("yyyyMMdd").format(DateTimeFormatter.ofPattern("dd.MM.yyyy").parse(mDate));
		String headline = switch (mType) {
			case "Liga" -> "in der" + "\n" + "Vogtlandliga";
			case "Pokal" -> "im" + "\n" + "Vogtlandpokal";
			case "Testspiel" -> "zum" + "\n" + "Testspiel";
			default -> "";
		};
		h.writeOnPicture(headline, "headline2-men", FontClass.headMen1, Color.WHITE, 100);
		
		ClubSelector getClub = new ClubSelector();
		ClubModel ownClub = getClub.getClubDetails("FSV Treuen");
		ClubModel oppClub = getClub.getClubDetails(mOpp);
		
		//Logo, posX, posY, Höhe, Breite, null
		h.pictureOnPicture(oppClub.clubLogo(), "smallClub-men", 0);
		h.pictureOnPicture(ownClub.clubLogo(), "bigClub-men", 0);
		
		BufferedImage formattedImage = chanceFormat(ImageIO.read(new File("buffer\\" + model.playerPic())));
		h.pictureOnPicture(formattedImage, "playerPic-men", 0);
		
		String saveName = getClub.getClubDetails(mOpp).saveClubName();
		File directory = new File("save\\" + date + "_" + mType + "_" + saveName);
		if (!directory.exists()) {
			//noinspection ResultOfMethodCallIgnored
			directory.mkdir();
		}
		ImageIO.write(image, "png", new File(directory + "\\" + "Kickoff.png"));
		h.deleteTempTxt(m, "men-games");
		return new File(directory + "\\" + "Kickoff.png");
		
	}
	
	private BufferedImage chanceFormat(BufferedImage input) {
		BufferedImage output = new BufferedImage(670, 670, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = output.createGraphics();
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHints(qualityHints);
		g2.setClip(new RoundRectangle2D.Double(0, 0, 670, 670, 670, 670));
		g2.drawImage(input, 0, 0, 670, 670, null);
		g2.dispose();
		return output;
	}
	
}
