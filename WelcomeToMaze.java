package myMaze;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WelcomeToMaze extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Image cover = new Image("https://s1.chu0.com/src/img/png/7c/7ce4f8cc23c84864ad707bedf171d369.png?imageMogr2/auto-orient/thumbnail/!234x234r/gravity/Center/crop/234x234/quality/85/&e=1735488000&token=1srnZGLKZ0Aqlz6dk7yF4SkiYf4eP-YrEOdM1sob:WeDaG6q3RJqVzbIRUaCsCtCQymE=");
		//封面图链接
		ImageView view = new ImageView();
		view.setImage(cover);
		//展示封面
		
		Text A = new Text("A");
		A.setFill(Color.AQUA);
		A.setFont(Font.font(null, FontWeight.BOLD, 25)); 
		Reflection r = new Reflection(); 
		r.setFraction(0.7); 
		A.setEffect(r);  
		Text maze = new Text("maze");
		maze.setFill(Color.RED);
		maze.setFont(Font.font(null, FontWeight.BOLD, 25)); 
		maze.setEffect(r);
		Text ing = new Text("!ng");
		ing.setFill(Color.AQUA);
		ing.setFont(Font.font(null, FontWeight.BOLD, 25)); 
		ing.setEffect(r);
		
		Image btBg = new Image("https://s1.aigei.com/src/img/png/8c/8c130ceb5685456f89105d177e2d4060.png?imageMogr2/auto-orient/thumbnail/!234x234r/gravity/Center/crop/234x234/quality/85/&e=1735488000&token=P7S2Xpzfz11vAkASLTkfHN7Fw-oOZBecqeJaxypL:ceC3dS7zVkuLInZoWZWRHInaKRY=");
		ImageView showBt = new ImageView(btBg);
		showBt.setFitHeight(50);
		showBt.setFitWidth(50);
		
		Button ntr = new Button();
		ntr.setGraphic(showBt);
		ntr.setMaxHeight(10);
		ntr.setOnAction(e->{//进入正文内容
		});
		
		BorderPane pn = new BorderPane();
		HBox title = new HBox();
		title.getChildren().addAll(A, maze, ing);
		title.setMaxSize(100, 100);
		BorderPane.setAlignment(ntr, Pos.CENTER);
		BorderPane.setAlignment(title, Pos.CENTER);
		pn.setCenter(view);
		pn.setTop(title);
		pn.setBottom(ntr);
		Scene sn = new Scene(pn);
		primaryStage.setScene(sn);
		primaryStage.setTitle("Amaze!ng");
		primaryStage.setFullScreen(true);
		primaryStage.show();
	}

}
