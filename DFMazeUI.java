package myMaze;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Stack;

import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import myMaze.DepthFirstMaze.Cell;



public class DFMazeUI extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	int mazeSize = 20;
	//�Թ���С��ÿά�Ⱦ��θ���
	
	int lenPerWal = 20;
	//ÿ��ǽ�ĳ���
	
	
	BorderPane bp = new BorderPane();
	Pane p = new Pane();
	ComboBox<Integer> chgSize = new ComboBox<>();
	ComboBox<Integer> bgX = new ComboBox<>();
	ComboBox<Integer> bgY = new ComboBox<>();
	ComboBox<Integer> edX = new ComboBox<>();
	ComboBox<Integer> edY = new ComboBox<>();
	//����ı��С��ʼĩ�������Ͽ�
	

	Button ntSize = new Button("ȷ��");
	Button ntBgPoint = new Button("ȷ��");
	Button ntEdPoint = new Button("ȷ��");
	Circle bgn = new Circle(lenPerWal/4);
	Circle end = new Circle(lenPerWal/4);
	TextField mapToLoad = new TextField("default.txt");
	DepthFirstMaze dfm;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		final short MAX = 300;//�Թ������
		for(int i = 3; i < MAX; i++) {
			chgSize.getItems().add(i);
		}
		chgSize.setValue(mazeSize);
		setData();
		
		ntSize.setOnAction(e->{
			mazeSize = chgSize.getValue();
			//��ȡ�ı��е����֣������Թ���С
			
			Line[] walls = new Line[4*mazeSize*mazeSize];
			
			cleanData();
			//��ջ��Թ�ǽ����
			
			setData();
			
			
			dfm = new DepthFirstMaze(mazeSize);
			drawWalls(walls);
		});
		ntBgPoint.setOnAction(e->{
			int y = bgY.getValue();
			int x = bgX.getValue();
			
			dfm.setStart(x + y*mazeSize);
			cleanPath();
			
			bgn.setCenterX(x*lenPerWal + lenPerWal/2);
			bgn.setCenterY(y*lenPerWal + lenPerWal/2);
			if(!p.getChildren().contains(bgn)) {
				
				p.getChildren().add(bgn);
			}
		});
		ntEdPoint.setOnAction(e->{
			int y = edY.getValue();
			int x = edX.getValue();
			
			dfm.setEnd(x + y*mazeSize);
			cleanPath();
			
			end.setCenterX(x*lenPerWal + lenPerWal/2);
			end.setCenterY(y*lenPerWal + lenPerWal/2);
			if(!p.getChildren().contains(end)) {
				
				p.getChildren().add(end);
			}
		});
		
		
		
		Line[] walls = new Line[4*mazeSize*mazeSize];
		
		dfm = new DepthFirstMaze(mazeSize);
		drawWalls(walls);
		printTrack(dfm.getSolution());
		

		bgn.setFill(Color.RED);
		bgn.setCenterX(lenPerWal/2);
		bgn.setCenterY(lenPerWal/2);
		p.getChildren().add(bgn);
		end.setFill(Color.YELLOW);
		end.setCenterX((mazeSize-1)*lenPerWal + lenPerWal/2);
		end.setCenterY((mazeSize-1)*lenPerWal + lenPerWal/2);
		p.getChildren().add(end);
		
		Button searchPath = new Button("����Ѱ·");
		searchPath.setOnAction(e->{
			clearTrack();
			cleanPath();
			printTrack(dfm.getSolution());
			p.getChildren().remove(bgn);
			p.getChildren().remove(end);
			//���·���ʼĩλ�ñ�ǣ���ֹ���㼣����
			p.getChildren().add(bgn);
			p.getChildren().add(end);
			end.setCenterX(edX.getValue()*lenPerWal + lenPerWal/2);
			end.setCenterY(edY.getValue()*lenPerWal + lenPerWal/2);
			if(p.getChildren().contains(imv)) {
				p.getChildren().remove(imv);
			}
			musRun();
		});
		
		Button sav = new Button("���浱ǰ��ͼ");
		sav.setOnAction(e->{
			savMap();
			Alert alert = new Alert(AlertType.INFORMATION);
            alert.titleProperty().set("��ʾ");
            alert.headerTextProperty().set("����ɹ�");
            alert.showAndWait();
		});
		
		Button load = new Button("��ȡ��ͼ");
		load.setOnAction(e->{			
			cleanData();
			//��ջ��Թ�ǽ����
			loadMap();
			
			chgSize.setValue(mazeSize);
			setData();
			
			Line[] newWalls = new Line[4*mazeSize*mazeSize];
			//��ǽ����Ҫ�ڼ����µ�ͼ֮����ȷ�����������������������������Խ��
			drawWalls(newWalls);
		});
		
		Button walkOver = new Button("����");
		walkOver.setOnAction(e->{
			printWalkOver(dfm.walkOver());
			
		});
		
		Button shortestPath = new Button("���·��");
		shortestPath.setOnAction(e->{
			printTrack(stdSolution(dfm.walkOver()));
		});
		
		
		
		chgSize.setMaxWidth(100);
		
		
		VBox vBox = new VBox();
		HBox hbSize = new HBox(), 
			 xy = new HBox(new Label("X                  Y")),
			 hbBgPoint = new HBox(),
			 hbEdPoint = new HBox(),
			 ldMap = new HBox();
		
		
		hbSize.getChildren().addAll(new Label("�Թ���С"), chgSize, ntSize);
		hbSize.setPadding(new Insets(10));
		hbSize.setSpacing(10);
		hbSize.setAlignment(Pos.CENTER);
		xy.setAlignment(Pos.CENTER);
		hbBgPoint.getChildren().addAll(new Label("���λ��"), bgX, bgY, ntBgPoint);
		hbBgPoint.setPadding(new Insets(10));
		hbBgPoint.setSpacing(10);
		hbBgPoint.setAlignment(Pos.CENTER);
		hbEdPoint.getChildren().addAll(new Label("�յ�λ��"), edX, edY, ntEdPoint);
		hbEdPoint.setPadding(new Insets(10));
		hbEdPoint.setSpacing(10);
		hbEdPoint.setAlignment(Pos.CENTER);
		ldMap.getChildren().addAll(mapToLoad, load);
		ldMap.setAlignment(Pos.CENTER);
		
		
		FlowPane func = new FlowPane();
		func.getChildren().addAll(searchPath, sav, walkOver, shortestPath);
		func.setPadding(new Insets(10));
		func.setAlignment(Pos.CENTER);
		
		
		vBox.getChildren().add(hbSize);
		vBox.getChildren().add(xy);
		vBox.getChildren().add(hbBgPoint);
		vBox.getChildren().add(hbEdPoint);
		vBox.getChildren().add(func);
		vBox.getChildren().add(ldMap);
		
		vBox.setAlignment(Pos.CENTER);
		VBox.setMargin(searchPath, new Insets(5));
		VBox.setMargin(ntSize, new Insets(5));
		VBox.setMargin(chgSize, new Insets(10));
		

		ScrollPane scp = new ScrollPane(p);
		bp.setCenter(scp);
		bp.setRight(vBox);
		bp.setPadding(new Insets(lenPerWal));
		
		
		
		primaryStage.setScene(new Scene(bp));
		primaryStage.setFullScreen(true);
		//ȫ��
		primaryStage.show();
	}
	
	
	//��DepthFirstMaze�������ɵ�����ȷ��ÿ��ǽwalls[x]��ʼĩλ�����꣬����ǽ�������p
	private void drawWalls(Line[] walls) {
			int row = 0, col = 0;
			
			for(int i = 0; i < walls.length; i += 4) {
				walls[i + DepthFirstMaze.UP] = new Line();
				walls[i + DepthFirstMaze.RIGHT] = new Line();
				walls[i + DepthFirstMaze.DOWN] = new Line();
				walls[i + DepthFirstMaze.LEFT] = new Line();
				
				if(dfm.cells[i/4].sides[DepthFirstMaze.UP] == DepthFirstMaze.Cell.PATH) {//����õ�Ԫ����һ����Ϊ·
					walls[i + DepthFirstMaze.UP].setStroke(Color.WHITE);//�Ͱ����������߻��ɰ�ɫ
				}
				if(dfm.cells[i/4].sides[DepthFirstMaze.RIGHT] == DepthFirstMaze.Cell.PATH) {
					walls[i + DepthFirstMaze.RIGHT].setStroke(Color.WHITE);
				}
				if(dfm.cells[i/4].sides[DepthFirstMaze.DOWN] == DepthFirstMaze.Cell.PATH) {
					walls[i + DepthFirstMaze.DOWN].setStroke(Color.WHITE);
				}
				if(dfm.cells[i/4].sides[DepthFirstMaze.LEFT] == DepthFirstMaze.Cell.PATH) {
					walls[i + DepthFirstMaze.LEFT].setStroke(Color.WHITE);
				}
				
				if(col == mazeSize) {//����
					row++;
					col = 0;
				}
				
				walls[i + DepthFirstMaze.UP].setStartX(col*lenPerWal);
				walls[i + DepthFirstMaze.UP].setStartY(row*lenPerWal);
				walls[i + DepthFirstMaze.UP].setEndX(col*lenPerWal + lenPerWal);
				walls[i + DepthFirstMaze.UP].setEndY(row*lenPerWal);
				p.getChildren().add(walls[i + DepthFirstMaze.UP]);
				
				walls[i + DepthFirstMaze.RIGHT].setStartX(col*lenPerWal + lenPerWal);
				walls[i + DepthFirstMaze.RIGHT].setStartY(row*lenPerWal);
				walls[i + DepthFirstMaze.RIGHT].setEndX(col*lenPerWal + lenPerWal);
				walls[i + DepthFirstMaze.RIGHT].setEndY(row*lenPerWal + lenPerWal);
				p.getChildren().add(walls[i + DepthFirstMaze.RIGHT]);
				
				walls[i + DepthFirstMaze.DOWN].setStartX(col*lenPerWal);
				walls[i + DepthFirstMaze.DOWN].setStartY(row*lenPerWal + lenPerWal);
				walls[i + DepthFirstMaze.DOWN].setEndX(col*lenPerWal + lenPerWal);
				walls[i + DepthFirstMaze.DOWN].setEndY(row*lenPerWal + lenPerWal);
				p.getChildren().add(walls[i + DepthFirstMaze.DOWN]);
				
				walls[i + DepthFirstMaze.LEFT].setStartX(col*lenPerWal);
				walls[i + DepthFirstMaze.LEFT].setStartY(row*lenPerWal);
				walls[i + DepthFirstMaze.LEFT].setEndX(col*lenPerWal);
				walls[i + DepthFirstMaze.LEFT].setEndY(row*lenPerWal + lenPerWal);
				p.getChildren().add(walls[i + DepthFirstMaze.LEFT]);
				
				col++;
			}
		}
		
		


	//�������·��
	private Stack<Cell> stdSolution(ArrayList<Stack<Cell>> allPath) {
		Stack<Cell> temp = allPath.get(0);
		
		if(allPath.size() == 0) {
			System.out.println("��·��");
			return null;
		}
		
		for(int i = 1; i < allPath.size(); i++) {
			if(allPath.get(i).size() < temp.size()) {
				temp = allPath.get(i);
			}
		}
		
		return temp;
	}
	
	
	
	private void printWalkOver(ArrayList<Stack<Cell>> allPath) {
		Rectangle[] rec = new Rectangle[allPath.size()*mazeSize*mazeSize];
		for(int i = 0 , k = 0; i < allPath.size(); i++) {
			Stack<Cell> curPath = allPath.get(i);
			int pathLen = curPath.size();
			//��ǰ·����ջ����Ĺ�ģ��䣬Ӧ�ȴ�����һ�������У���Ϊ����ѭ���˳��ıȽ�����
			
			for(int j = 0; j < pathLen; j++) {
				rec[k] = new Rectangle(lenPerWal/2, lenPerWal/2);
				rec[k].setFill(Color.AQUAMARINE);
				
				for(int m = 0; m < dfm.cells.length && !curPath.isEmpty(); m++) {
//					System.out.println(curPath.size());//����
					if(dfm.cells[m] == curPath.peek()) {
						//�������꣬��ӽڵ�
						rec[k].setX((m%mazeSize)*lenPerWal + lenPerWal/4);
						rec[k].setY((m/mazeSize)*lenPerWal + lenPerWal/4);
						p.getChildren().add(rec[k]);
						//������ǰ��Ԫ��
						curPath.pop();
						break;
					}
				}
				
				k++;
			}
		}
	}
	
	Circle[] track;
	
	//��ӡ�㼣
	private void printTrack(Stack<Cell> path) {
//		Stack<Cell> solution = dfm.getSolution();
		
		if(path.isEmpty()) {
			System.out.println("û�нⷨ");
			return;
		}
		
		
		track = new Circle[path.size()];
		
		for(int i = 0; i < track.length; i++) {
			track[i] = new Circle(lenPerWal/4);
			track[i].setFill(Color.GREEN);
		}
		
		for(int i = 0, j = 0; i < dfm.cells.length; i++) {
			
			for(int k = 0; k < dfm.cells.length && !path.isEmpty(); k++) {
				
				if(dfm.cells[k] == path.peek()) {
					track[j].setCenterX((k%mazeSize)*lenPerWal + lenPerWal/2);
					track[j].setCenterY((k/mazeSize)*lenPerWal + lenPerWal/2);
					p.getChildren().add(track[j]);
					j++;
					
					path.pop();
				}
			}
			
		}
	}
	
	
	
	ImageView imv;
	//�������󶯻�
	private void musRun() {
		Line[] path = new Line[track.length-1];
		Image im = new Image("https://s1.aigei.com/src/img/gif/98/98cc86ccc28b4958866916de4486a862.gif?e=1735488000&token=P7S2Xpzfz11vAkASLTkfHN7Fw-oOZBecqeJaxypL:XkO5j1JrklSECHRXzr0APUhrgCE=");
		imv = new ImageView(im);
		imv.setFitHeight(25);
		imv.setFitWidth(50);
		p.getChildren().add(imv);
		
		
		int len = track.length-1;
		
		for(int i = 0; i < len; i++) {
			path[i] = new Line(track[len - i].getCenterX(), 
							   track[len - i].getCenterY(),
							   track[len - i -1].getCenterX(), 
							   track[len - i -1].getCenterY());
					
			Line clip = path[i];
			EventHandler<ActionEvent> move = e->{
				PathTransition pt = new PathTransition(Duration.millis(4000), 
						clip, imv);
				
//				pt.setCycleCount(1);
				pt.play();
			};
			Timeline anim = new Timeline(new KeyFrame(Duration.millis(20), move));
			anim.play();
			System.out.println(i);
		}
		
	}
	
	
	//��ɨ�㼣
	private void clearTrack() {
		for(int i = 0; i < track.length; i++) {
			p.getChildren().remove(track[i]);
		}
	}
	
	
	//�浵��ͼ��Ϣ
	private void savMap() {
		
		String mapRec = "";
		
		for(int i = 0, j; i < dfm.cells.length; i++) {
			for(j = 0; j < dfm.cells[0].sides.length; j++) {
				if(dfm.cells[i].sides[j] == DepthFirstMaze.Cell.PATH) {
					mapRec += '0';
				}
				else {
					mapRec += '1';
				}
				
			}
			
			if(i%mazeSize == mazeSize-1) {
				mapRec += System.getProperty("line.separator");
			}
			else {
				mapRec += " ";
			}
			
		}
		
		
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_HH_mm_ss");  
		String tm = formatter.format(date);
		File save = new File("mapAt" + tm + ".txt");
		
		try {
			PrintWriter wr = new PrintWriter(save);
			wr.write(mapRec);
			wr.flush();
			wr.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("�Ҳ����ļ�" + save.getName());
			e.printStackTrace();
		}
	}

	
	//���ص�ͼ��Ϣ
	private void loadMap() {
		
		File data = new File(mapToLoad.getText());
		String map = "";
		
		try {
			Scanner load = new Scanner(data);
			while(load.hasNext()) {
				map += load.next();
			}
			load.close();
			mazeSize = (int) Math.sqrt(map.length()/4);
			dfm = new DepthFirstMaze(mazeSize);
			for(int i = 0, j; i < map.length();) {
				for(j = 0; j < 4; j++, i++) {
					switch (map.charAt(i)) {
					case '0': 
						dfm.cells[i/4].sides[j] = Cell.PATH;
						break;
					case '1': 
						dfm.cells[i/4].sides[j] = Cell.WALL;
						break;
					default:
						throw new IllegalArgumentException("�ļ����ݲ��Ϸ�");
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("�ļ�������");
			e.printStackTrace();
		}catch (IllegalArgumentException e) {
			System.out.println("���ļ��޷����������Թ�");
			e.printStackTrace();
		}
		
		
	}

	
	//��ԭÿ����Ԫ�񱻾�����״̬
	private void cleanPath() {
		for(int i = 0; i < dfm.cells.length; i++) {
			dfm.cells[i].passedBy = false;
		}
	}

	
	
	//�����Ͽ�����
	private void cleanData() {
		bgX.getItems().clear();
		bgY.getItems().clear();
		edX.getItems().clear();
		edY.getItems().clear();
		p.getChildren().clear();
	}
	
	
	//������Ͽ�����
	private void setData() {
		for(int i = 0; i < mazeSize; i++) {
			bgX.getItems().add(i);
			bgY.getItems().add(i);
			edY.getItems().add(i);
			edX.getItems().add(i);
		}
		bgX.setValue(0);
		bgY.setValue(0);
		edY.setValue(mazeSize-1);
		edX.setValue(mazeSize-1);
	}
	
}
