import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HomeView extends BorderPane {
	
	public HomeView(Stage primaryStage , Scene scene) {
		Label welcomeL = new Label ("Welcome to BZU clinic's database management system!\nYou can navigate through the tabs above.\n\nMade by: Omar Kashour - 1210082.");
		welcomeL.setWrapText(true);
		Font font = Font.loadFont(getClass().getResourceAsStream("Satoshi-Variable.ttf"), 30);
		Image logo = new Image("birzeit clinic logo.jpg");
		ImageView view = new ImageView(logo);
		view.setFitHeight(280);
		view.setFitWidth(800);
		setTop(view);
		welcomeL.setFont(font);
		setAlignment(view, Pos.CENTER);
		setAlignment(welcomeL, Pos.CENTER);
		welcomeL.setAlignment(Pos.CENTER);
		setPadding(new Insets(15));
		setCenter(welcomeL);
	}

}
