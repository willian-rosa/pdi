package view;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pdi.Pdi;

public class PrincipalController {
	
	private String pathSave = "C:\\Users\\Willian\\Pictures\\Civitas2";
	
	@FXML Label lblR;  
	@FXML Label lblG;  
	@FXML Label lblB;  
	
	@FXML ImageView imgW1;
	@FXML ImageView imgW2;
	@FXML ImageView imgW3;
	
	@FXML TextField txtFieldTonsCinzaRed;
	@FXML TextField txtFieldTonsCinzaGreen;
	@FXML TextField txtFieldTonsCinzaBlue;
	@FXML TextField txtFieldQuantidadeSegmentacao;
	
	@FXML Slider sliderLimiar;
	
	@FXML RadioButton radioRuidoCruz;
	@FXML RadioButton radioRuidoX;
	@FXML RadioButton radioRuido3x3;
	
	
	@FXML RadioButton radioCorPreto;
	@FXML RadioButton radioCorAzul;
	@FXML RadioButton radioCorVerde;
	@FXML TextField txtFieldTamanhoMoldura;
	
	@FXML Label lblAddSubImg1;
	@FXML Label lblAddSubImg2;
	@FXML Slider sliderAddSub;
	
	@FXML RadioButton radioCorPretoGrade;
	@FXML RadioButton radioCorAzulGrade;
	@FXML RadioButton radioCorVerdeGrade;
	@FXML TextField txtFieldDistanciaGrade;
	
	private Image imagem1, imagem2, imagem3;
	
	double[] clickOnMouse = new double[2];
	double[] clickStopMouse = new double[2];
	
//	private void exibeMsg(String titulo, String cabecalho, String msg, AlertType tipo){
//		  Alert alert = new Alert(tipo);
//		  alert.setTitle(titulo);
//		  alert.setHeaderText(cabecalho);
//		  alert.setContentText(msg);
//		  alert.showAndWait();
//	  }
	
	private void atualizaImg3() {
		imgW3.setImage(imagem3);
		imgW3.setFitWidth(imagem3.getWidth());
		imgW3.setFitHeight(imagem3.getHeight());
	}
	
	private File selecionarImagem() {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.png", "*.gif"));
		fileChooser.setInitialDirectory(new File(this.pathSave));
		File imgSelect = fileChooser.showOpenDialog(null) ;
		
		try {
			if (imgSelect != null) {
				return imgSelect;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private void verificarCor(Image img, int x, int y) {
		/*
		Color cor = img.getPixelReader().getColor(x, y);
		try {
			if(x < 255 && y < 255) {
				lblR.setText("R "+(int) (cor.getRed()*255));
				lblG.setText("G "+(int) (cor.getGreen()*255));
				lblB.setText("B "+(int) (cor.getBlue()*255));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}
	
	@FXML
	public void abrirImagem1() {
		File f = selecionarImagem();
		
		if (f != null) {
			imagem1 =  new Image(f.toURI().toString());
			imgW1.setImage(imagem1);
			imgW1.setFitHeight(imagem1.getHeight());
			imgW1.setFitWidth(imagem1.getWidth());
		}
		
	}
	
	@FXML
	public void abrirImagem2() {
		File f = selecionarImagem();
		
		if (f != null) {
			imagem2 =  new Image(f.toURI().toString());
			imgW2.setImage(imagem2);
			imgW2.setFitHeight(imagem2.getHeight());
			imgW2.setFitWidth(imagem2.getWidth());
		}
		
	}
	
	@FXML
	public void rastrearImg(MouseEvent evt) {
		ImageView iw = (ImageView)evt.getTarget();
		if(iw.getImage() != null) {
			verificarCor(iw.getImage(), (int) evt.getX(), (int) evt.getY());
		}
	}
	
	@FXML
	public void salvarImagem() {
		if (imagem3 != null) {
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.png"));
			fc.setInitialDirectory(new File(this.pathSave));
			File f = fc.showSaveDialog(null);
			if (f != null) {
				BufferedImage bImg = SwingFXUtils.fromFXImage(imagem3, null);
				try {
					ImageIO.write(bImg, "PNG", f);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@FXML
	public void teste() {
		imagem3 = Pdi.exemploModificacaoPixel(imagem1);
		atualizaImg3();
		
	}
	
	@FXML
	public void tonsDeCinza() {
		
		int r = Integer.parseInt(txtFieldTonsCinzaRed.getText());
		int g = Integer.parseInt(txtFieldTonsCinzaGreen.getText());
		int b = Integer.parseInt(txtFieldTonsCinzaBlue.getText());
		
		imagem3 = Pdi.tonsDeCinza(imagem1, r, g, b);
		atualizaImg3();
		
	}
	
	@FXML
	public void limiar() {
		
		double limiar = sliderLimiar.getValue()/255;
		
		imagem3 = Pdi.limiar(imagem1, limiar);
		atualizaImg3();
		
	}
	
	@FXML
	public void negativar() {
		
		imagem3 = Pdi.negativar(imagem1);
		atualizaImg3();
		
	}
	
	@FXML
	public void desafio1() {
		
		imagem3 = Pdi.desafio1(imagem1);
		atualizaImg3();
		
	}
	
	@FXML
	public void ruidosMedia() {
		
		int tipo = 0;
		
		if (radioRuidoCruz.isSelected()) {
			tipo = 1;
		}else if (radioRuidoX.isSelected()) {
			tipo = 2;
		}else {
			tipo = 3;
		}
		
		imagem3 = Pdi.ruido(imagem1, false, tipo);
		atualizaImg3();
		
	}
	
	@FXML
	public void ruidosMediana() {
		
		int tipo = 0;
		
		if (radioRuidoCruz.isSelected()) {
			tipo = 1;
		}else if (radioRuidoX.isSelected()) {
			tipo = 2;
		}else {
			tipo = 3;
		}
		
		imagem3 = Pdi.ruido(imagem1, true, tipo);
		atualizaImg3();
		
	}
	
	@FXML
	public void gerarHistograma(ActionEvent event) {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("HistogramaModal.fxml"));
			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.setTitle("Histograma");
			stage.initOwner(((Node)event.getSource()).getScene().getWindow());
			stage.show();
			
			HistogramaModalController controller = (HistogramaModalController)loader.getController();
			
			if (imagem1 != null) {
				Pdi.populateCharts(imagem1, controller.hist1);
			}
			if (imagem2 != null) {
				Pdi.populateCharts(imagem2, controller.hist2);
			}
			if (imagem3 != null) {
				Pdi.populateCharts(imagem3, controller.hist3);
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void equalizarHistograma() {
		
		imagem3 = Pdi.equalizacaoHistograma(imagem1);
		atualizaImg3();
		
	}
	
	@FXML
	public void segmentar() {
		int numeroCoresDesejadas = 0;
		try {
			numeroCoresDesejadas = Integer.parseInt(txtFieldQuantidadeSegmentacao.getText());
			
		} catch (Exception e) {
			e.printStackTrace();
			numeroCoresDesejadas = 3;
		}
		
		imagem3 = Pdi.segmentar(imagem1, numeroCoresDesejadas);
		atualizaImg3();
		
	}
	
	@FXML
	public void colocarMoldura() {
		
		int tipo = 1;
		
		if (radioCorAzul.isSelected()) {
			tipo = 2;
		}else if (radioCorVerde.isSelected()) {
			tipo = 3;
		}
		
		int tamanhoMoldura = Integer.parseInt(txtFieldTamanhoMoldura.getText());
		
		imagem3 = Pdi.colocarMoldura(imagem1, tamanhoMoldura, tipo);
		atualizaImg3();
		
	}
	
	@FXML
	public void negativarETonsCinza() {
		imagem3 = Pdi.negativarETonsCinza(imagem1);
		atualizaImg3();
	}
	
	@FXML
	public void identificarFiguras() {
		if ( Pdi.isimagemQuadrado(imagem1)) {
			System.out.println("É quadrado");
		}else {
			System.out.println("Não é quadrado");
			
		}
		
	}
	
	@FXML
	public void rotacionarImagem() {
		imagem3 = Pdi.rotacionarImagem(imagem1);
		atualizaImg3();
	}
	
	@FXML
	public void aumentarImagem() {
		imagem3 = Pdi.aumentarImagem(imagem1);
		atualizaImg3();
	}
	
	@FXML
	public void diminuirImagem() {
		imagem3 = Pdi.diminuirImagem(imagem1);
		atualizaImg3();
	}
	
	@FXML
	public void moveSlideAddSub() {
		int valorImagem1 = (int)sliderAddSub.getValue();
		int valorImagem2 = 100-valorImagem1;
		
		lblAddSubImg1.setText(valorImagem1+"");
		lblAddSubImg2.setText(valorImagem2+"");
	}
	
	@FXML
	public void addImage() {
		
		double qtDistribuicao = sliderAddSub.getValue();
		
		imagem3 = Pdi.addSubImage(imagem1, imagem2, qtDistribuicao, true);
		atualizaImg3();
	}
	
	@FXML
	public void subImage() {
		
		double qtDistribuicao = sliderAddSub.getValue();
		
		imagem3 = Pdi.addSubImage(imagem1, imagem2, qtDistribuicao, false);
		atualizaImg3();
	}
	
	@FXML
	public void selectOnMouse(MouseEvent event) {
		
		clickOnMouse[0] = event.getX();
		clickOnMouse[1] = event.getY();

	}
	
	@FXML
	public void selectStopMouse(MouseEvent event) {
		
		clickStopMouse[0] = event.getX();
		clickStopMouse[1] = event.getY();
		
		ImageView imageView = (ImageView)event.getTarget();
		
		if(imageView != null) {
			String resposta = Pdi.detectarCores(imageView.getImage(), clickOnMouse, clickStopMouse);
			//Esta em JOptionPane porque meu scene.control.Alert não funciona
			JOptionPane.showMessageDialog(null, resposta);
			
		}
		
	}
	
	@FXML
	public void colocarGrade() {
		
		int tipo = 1;
		
		if (radioCorAzulGrade.isSelected()) {
			tipo = 2;
		}else if (radioCorVerdeGrade.isSelected()) {
			tipo = 3;
		}
		
		int distancia = Integer.parseInt(txtFieldDistanciaGrade.getText());
		
		imagem3 = Pdi.colocarGrade(imagem1, distancia, tipo);
		atualizaImg3();
		
	}
	
	@FXML
	public void dividirHorizontalmente() {
		imagem3 = Pdi.dividirHorizontalmente(imagem1);
		atualizaImg3();
	}
	
	@FXML
	public void identificarRosto() {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		CascadeClassifier faceDetector = new CascadeClassifier("src/xml-opencv/haarcascade_frontalface_alt.xml");
		
		Mat imageMat = Util.imageToMat(imagem1);
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(imageMat, faceDetections);
		
		System.out.println("Rosto detectados: "+faceDetections.toArray().length);
		
		for(Rect rect : faceDetections.toArray()) {
			Imgproc.rectangle(imageMat, 
								new Point(rect.x, rect.y), 
								new Point(rect.x + rect.width, rect.y + rect.height),
								new Scalar(0, 1, 0),
								3);
		}
		
		MatOfByte mtb  = new MatOfByte();
		Imgcodecs.imencode(".png", imageMat, mtb);
		imagem3 = new Image(new ByteArrayInputStream(mtb.toArray()));
		
		atualizaImg3();
		
		
	}
	
}

