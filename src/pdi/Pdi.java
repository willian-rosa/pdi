package pdi;

import javax.swing.JOptionPane;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Pdi {
	
	private static Pixel[][] factoryMatrixPixel(PixelReader pr, int w, int h) {
		
		Pixel[][] pixels = new Pixel[w][h]; 
				
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				
				Pixel pixelAdjacente = null;
				
				Pixel pixel = new Pixel(pr.getColor(i, j), i, j);
				
				//1
				if (i-1>0 && j-1>0) {
					pixelAdjacente = new Pixel(pr.getColor(i-1, j-1), i-1, j-1);
					pixel.addVizinhos3x3(pixelAdjacente);
					pixel.addVizinhosX(pixelAdjacente);
				}
				
				//2
				if (j-1>0) {
					pixelAdjacente = new Pixel(pr.getColor(i, j-1), i, j-1);
					pixel.addVizinhosCruz(pixelAdjacente);
					pixel.addVizinhos3x3(pixelAdjacente);
				}
				
				//3
				if (i+1<w && j-1>0) {
					pixelAdjacente = new Pixel(pr.getColor(i+1, j-1), i+1, j-1);
					pixel.addVizinhosX(pixelAdjacente);
					pixel.addVizinhos3x3(pixelAdjacente);
				}
				
				//4
				if (i-1>0) {
					pixelAdjacente = new Pixel(pr.getColor(i-1, j), i-1, j);
					pixel.addVizinhosCruz(pixelAdjacente);
					pixel.addVizinhos3x3(pixelAdjacente);
				}
				
				//6
				if (i+1<w) {
					pixelAdjacente = new Pixel(pr.getColor(i+1, j), i+1, j);
					pixel.addVizinhosCruz(pixelAdjacente);
					pixel.addVizinhos3x3(pixelAdjacente);
				}
				
				//7
				if (i-1>0 && j+1<h) {
					pixelAdjacente = new Pixel(pr.getColor(i-1, j+1), i-1, j+1);
					pixel.addVizinhosX(pixelAdjacente);
					pixel.addVizinhos3x3(pixelAdjacente);
				}
				
				//8
				if (i<w && j+1<h) {					
					pixelAdjacente = new Pixel(pr.getColor(i, j+1), i, j+1);
					pixel.addVizinhosCruz(pixelAdjacente);
					pixel.addVizinhos3x3(pixelAdjacente);
				}
				
				//9
				if (i+1<w && j+1<h) {
					pixelAdjacente = new Pixel(pr.getColor(i+1, j+1), i+1, j+1);
					pixel.addVizinhosX(pixelAdjacente);
					pixel.addVizinhos3x3(pixelAdjacente);
				}
				
				pixels[i][j] = pixel;
				
			}
		}
		
		return pixels;
		
	}
	
	private static int[] generateTableColor(int[][] histogramaSimples) {
		
		int[] histogramaNivelado = new int[256];
		int tamanho = 0;
		
		
		for (int j = 0; j < histogramaSimples[0].length; j++) {
			int qdt = histogramaSimples[0][j] + histogramaSimples[1][j]  + histogramaSimples[2][j];
			if (qdt > 0) {
				histogramaNivelado[j] = j;
				tamanho++;
			}else {
				histogramaNivelado[j] = -1;
			}
		}
		
		int index = 0;
		int[] cores = new int[tamanho];
		
		for (int i = 0; i < histogramaNivelado.length; i++) {
			if (histogramaNivelado[i] > -1) {
				cores[index] = histogramaNivelado[i];
				index++;
			} 
		}
		
		return cores;
	}
	
	public static Image exemploModificacaoPixel(Image imagem) {
		
		int w = (int)imagem.getWidth();
		int h = (int)imagem.getHeight();
		
		WritableImage wi = new WritableImage(w, h);
		PixelReader pr = imagem.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color cor = pr.getColor(i, j);
				Color corNova = new Color(cor.getRed()/2, cor.getGreen()/2, cor.getBlue()/2, cor.getOpacity());
				pw.setColor(i, j, corNova);
			}
		}
		
		
		return wi;
		
	}
	
	public static Image tonsDeCinza(Image imagem, int r, int g, int b) {
		
		int w = (int)imagem.getWidth();
		int h = (int)imagem.getHeight();
		
		WritableImage wi = new WritableImage(w, h);
		PixelReader pr = imagem.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				
				Color oldColor = pr.getColor(i, j);

				double media = 0;
				
				if (r == 0 && r == g && r == b) {
					media = (oldColor.getRed()+oldColor.getGreen()+oldColor.getBlue())/3;
				}else {
					media = (oldColor.getRed()*r+
								oldColor.getGreen()*g+
								oldColor.getBlue()*b
							)/100;
				}
				
				Color corNova = new Color(media, media, media, oldColor.getOpacity());
				pw.setColor(i, j, corNova);
			}
		}
				
		return wi;
		
	}
	
	public static Image limiar(Image imagem, double limiar) {
		
		int w = (int)imagem.getWidth();
		int h = (int)imagem.getHeight();
		
		WritableImage wi = new WritableImage(w, h);
		PixelReader pr = imagem.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color oldColor = pr.getColor(i, j);
				Color corNova = null;
				if (oldColor.getRed() > limiar) {
					corNova = new Color(1, 1, 1, oldColor.getOpacity());
					
				}else {
					corNova = new Color(0, 0, 0, oldColor.getOpacity());
				}
				pw.setColor(i, j, corNova);
			}
		}
		
		
		return wi;
		
	}
	
	public static Image negativar(Image imagem) {
		
		int w = (int)imagem.getWidth();
		int h = (int)imagem.getHeight();
		
		WritableImage wi = new WritableImage(w, h);
		PixelReader pr = imagem.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color oldColor = pr.getColor(i, j);
				Color corNova = new Color(1-oldColor.getRed(), 1-oldColor.getGreen(), 1-oldColor.getBlue(), oldColor.getOpacity());
				pw.setColor(i, j, corNova);
			}
		}
		
		
		return wi;
		
	}
	
	public static Image desafio1(Image imagem) {
		
		int w = (int)imagem.getWidth();
		int h = (int)imagem.getHeight();
		
		double div = w/3;
		
		WritableImage wi = new WritableImage(w, h);
		PixelReader pr = imagem.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color oldColor = pr.getColor(i, j);
				Color newColor = null;
				if (i < div) {
					double media = (oldColor.getRed()+oldColor.getGreen()+oldColor.getBlue())/3;
					//tons de cinza
					newColor = new Color(media, media, media, oldColor.getOpacity());
				}else if (i < (div*2)) {
					//limiar
					if (oldColor.getRed()*255 > 170) {
						newColor = new Color(1, 1, 1, oldColor.getOpacity());
					}else {
						newColor = new Color(0, 0, 0, oldColor.getOpacity());
					}
					
				}else {
					//negativar
					newColor = new Color(1-oldColor.getRed(), 1-oldColor.getGreen(), 1-oldColor.getBlue(), oldColor.getOpacity());
					
				}
				
				pw.setColor(i, j, newColor);
			}
		}
		
		
		return wi;
		
	}
	
	public static Image ruido(Image imagem, boolean isMediana, int tipo) {
		
		int w = (int)imagem.getWidth();
		int h = (int)imagem.getHeight();
		
		WritableImage wi = new WritableImage(w, h);
		PixelReader pr = imagem.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		Pixel[][] pixels = factoryMatrixPixel(pr, w, h);
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				
				Color newColor = null;
				Pixel pixel = pixels[i][j];
				
				if (isMediana) {
					if (tipo == 1) {
						//tipo cruz mediana
						newColor = pixel.generateCorMedianaCruz();
					}else if (tipo == 2) {
						//tipo x mediana
						newColor = pixel.generateCorMedianaX();
					}else if (tipo == 3) {
						//tipo 3x3 mediana
						newColor = pixel.generateCorMediana3x3();
					}
				}else {
					//tipo cruz media
					if (tipo == 1) {
						newColor = pixel.generateCorMediaCruz();
					}else if (tipo == 2) {
						//tipo x media
						newColor = pixel.generateCorMediaX();
					}else if (tipo == 3) {
						//tipo 3x3 media
						newColor = pixel.generateCorMedia3x3();
					}
				}
				
				
				pw.setColor(i, j, newColor);
				
			}
		}
		
		
		return wi;
		
	}

	public static int[][] genareteHistograma(Image img){
		int[][] quant = new int[3][256];
		PixelReader pr = img.getPixelReader();
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				quant[0][(int)(pr.getColor(i, j).getRed()*255)]++;
				quant[1][(int)(pr.getColor(i, j).getGreen()*255)]++;
				quant[2][(int)(pr.getColor(i, j).getBlue()*255)]++;
			}
		}
		
		return quant;
	}
	
	public static int[][] genareteHistogramaAcumulado(int[][] histogramaSimples){
		
		int[][] quant = new int[3][256];
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < histogramaSimples[i].length; j++) {
				quant[i][j] = histogramaSimples[i][j] + ((j == 0)?0:quant[i][j-1]);
			}
		}
		
		return quant;
	}
	
	public static int[] genareteTotalTons(int[][] histogramaSimples){
		
		int[] quant = new int[3];
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < histogramaSimples[i].length; j++) {
				if (histogramaSimples[i][j] > 0) {
					quant[i]++;
				}
			}
		}
		
		return quant;
	}
	
	public static double generateConstantHistogramaTom(int numeroTotalTonsDisponivel, int totalPixils){
		
		double L = (double)numeroTotalTonsDisponivel;
		double N = (double)totalPixils;
		
		return (L-1)/N;
		
	}
	
	public static double generateColorNewHistograma(double constantHistograma, int posicaoTom, int posicaoCor, int[][] histogramaAcumulado){
		
		return (constantHistograma*histogramaAcumulado[posicaoCor][posicaoTom])/255;
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void populateCharts(Image img, BarChart<String, Number> grafico){
		CategoryAxis eixoX = new CategoryAxis();
		NumberAxis eixoY = new NumberAxis();
	    eixoX.setLabel("Canal");       
	    eixoY.setLabel("valor");
	    XYChart.Series vlrIntensidade = new XYChart.Series();
	    vlrIntensidade.setName("Intensidade");
	    
	    int[][] histogramas = genareteHistograma(img);
	    
	    for (int i=0; i< histogramas[0].length; i++) {
	    	vlrIntensidade.getData().add(new XYChart.Data(i+"", histogramas[0][i]+histogramas[1][i]+histogramas[2][i]));
	    }
	    grafico.getData().addAll(vlrIntensidade);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void populateChartsTeste(Image img, BarChart<String, Number> grafico){
		CategoryAxis eixoX = new CategoryAxis();
		NumberAxis eixoY = new NumberAxis();
		eixoX.setLabel("Canal");       
		eixoY.setLabel("valor");
		XYChart.Series vlrR = new XYChart.Series();
		vlrR.setName("R");
		XYChart.Series vlrG = new XYChart.Series();
		vlrG.setName("G");
		XYChart.Series vlrB = new XYChart.Series();
		vlrB.setName("B");
		
		int[][] histogramas = genareteHistograma(img);
		
		for (int i=0; i< histogramas[0].length; i++) {
			vlrR.getData().add(new XYChart.Data(i+"", histogramas[0][i]));
			vlrG.getData().add(new XYChart.Data(i+"", histogramas[1][i]));
			vlrB.getData().add(new XYChart.Data(i+"", histogramas[2][i]));
		}
		grafico.getData().addAll(vlrR,vlrG,vlrB);
	}
	
	public static Image equalizacaoHistograma(Image img) {
		
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		
		int totalPixils = w*h;
		
		int[][] histogramaSimples	= genareteHistograma(img);
		int[][] histogramaAcumulado = genareteHistogramaAcumulado(histogramaSimples);
		int[] totalTons				= genareteTotalTons(histogramaSimples);
		
		double constHistogramaTomRed = generateConstantHistogramaTom(totalTons[0], totalPixils);
		double constHistogramaTomGreem = generateConstantHistogramaTom(totalTons[1], totalPixils);
		double constHistogramaTomBlue = generateConstantHistogramaTom(totalTons[2], totalPixils);
		
		WritableImage wi = new WritableImage(w, h);
		PixelReader pr = img.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				
				Color oldColor = pr.getColor(i, j);
				
				Color corNova = new Color(generateColorNewHistograma(constHistogramaTomRed,     (int)(oldColor.getRed()*255),		0, histogramaAcumulado),
				  						  generateColorNewHistograma(constHistogramaTomGreem,   (int)(oldColor.getGreen()*255), 	1, histogramaAcumulado),
										  generateColorNewHistograma(constHistogramaTomBlue, 	(int)(oldColor.getBlue()*255), 		2, histogramaAcumulado),
										  oldColor.getOpacity());
				pw.setColor(i, j, corNova);
			}
		}
				
		return wi;
		
	}
	
	private static Color segmentacaoSimples(int[] coresUtilizadas, Color oldColor, int numeroCoresDesejadas) {
		
		
		int totalCores = coresUtilizadas.length;
		int espacoFragmentoHistograma = (int)(totalCores/numeroCoresDesejadas);
		int tonsCinzaCor = (int)(oldColor.getRed()*255);
		
		int corFinal = 255;

		for (int i = 1; i <= numeroCoresDesejadas; i++) {
			if(tonsCinzaCor < coresUtilizadas[espacoFragmentoHistograma*i]) {
				int pontoCorteInicial = (int)(espacoFragmentoHistograma*(i-1));
				int pontoCorteFinal = (int)(espacoFragmentoHistograma*i);
				corFinal = (coresUtilizadas[pontoCorteFinal]-coresUtilizadas[pontoCorteInicial])/2;
			}
		}
		
		return new Color(((double)corFinal/255.0), ((double)corFinal/255.0), ((double)corFinal/255.0), oldColor.getOpacity());
		
	}
	
	public static Image segmentar(Image img, int numeroCoresDesejadas) {
		
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		
		int[][] histogramaSimples	= genareteHistograma(img);
		int[] coresUtilizada = generateTableColor(histogramaSimples);
		
		WritableImage wi = new WritableImage(w, h);
		PixelReader pr = img.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				
				Color oldColor = pr.getColor(i, j);
				
				Color corNova = segmentacaoSimples(coresUtilizada, oldColor, numeroCoresDesejadas);
				
				pw.setColor(i, j, corNova);
			}
		}
		
		System.out.println(coresUtilizada.length);
				
		return wi;
		
	}
	
	public static Image colocarMoldura(Image imagem, int tamanhoMoldura, int tipo) {
		
		int w = (int)imagem.getWidth();
		int h = (int)imagem.getHeight();
		
		WritableImage wi = new WritableImage(w, h);
		PixelReader pr = imagem.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		Color newColor = null;
		
		if (tipo == 1) {
			newColor = new Color(0, 0, 0, 0.8);
		}else if (tipo == 2) {
			newColor = new Color(0, 0, 1, 0.8);
		}else if (tipo == 3) {
			newColor = new Color(0, 1, 0, 0.8);
		}
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if ( i < tamanhoMoldura || i > (w-tamanhoMoldura-1) ||
					 j < tamanhoMoldura || j > (h-tamanhoMoldura-1)) {
					pw.setColor(i, j, newColor);
				}else {
					pw.setColor(i, j, pr.getColor(i, j));
				}
			}
		}
		
		
		return wi;
		
	}
	
	public static Image negativarETonsCinza(Image imagem) {
		
		int w = (int)imagem.getWidth();
		int h = (int)imagem.getHeight();
		
		WritableImage wi = new WritableImage(w, h);
		PixelReader pr = imagem.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		int meioH = h/2;
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				
				Color oldColor = pr.getColor(i, j);
				double red = oldColor.getRed();
				double green = oldColor.getGreen();
				double blue = oldColor.getBlue();
				
				if(j < meioH) {
					//negativa
					Color newColor = new Color(1-red, 1-green, 1-blue, oldColor.getOpacity());
					pw.setColor(i, j, newColor);
				}else {
					//tons de cinza
					
					double tom = (red+green+blue)/3;
					Color newColor = new Color(tom, tom, tom, oldColor.getOpacity());
					pw.setColor(i, j, newColor);
				}
				
				
				
			}
		}
		
		
		return wi;
		
	}
	
	public static boolean isimagemQuadrado(Image imagem) {
		
		int w = (int)imagem.getWidth();
		int h = (int)imagem.getHeight();
		
		PixelReader pr = imagem.getPixelReader();
		
		boolean isQuadrado = false;
		
		for (int i = 0; i < w; i++) {
			
			double count = 0;
			
			for (int j = 0; j < h; j++) {
				
				Color oldColor = pr.getColor(i, j);
				
				double corAtual = (oldColor.getRed()+oldColor.getBlue()+oldColor.getGreen())/3;
				
				if(corAtual < 0.5) {
					count++;
				}
				
			}
			
			if((count/h)*100 > 30) {
				isQuadrado = true;
				break;
			}
			
		}
		
		
		return isQuadrado;
		
	}
	
	public static Image rotacionarImagem(Image imagem) {
		
		int w = (int)imagem.getWidth();
		int h = (int)imagem.getHeight();
		
		WritableImage wi = new WritableImage(h, w);
		PixelReader pr = imagem.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				
				Color oldColor = pr.getColor(i, j);
				
				pw.setColor(j, w-i-1, oldColor);
				
			}
		}
		
		return wi;
		
	}
	
	public static Image aumentarImagem(Image imagem) {
		
		int w = (int)imagem.getWidth();
		int h = (int)imagem.getHeight();
		
		WritableImage wi = new WritableImage(w*2, h*2);
		PixelReader pr = imagem.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Color oldColor = pr.getColor(i, j);
				
				int x = i*2;
				int y = j*2;
				
				pw.setColor(x, y, oldColor);
				pw.setColor(x+1, y+1, oldColor);
				pw.setColor(x+1, y, oldColor);
				pw.setColor(x, y+1, oldColor);
				
			}
		}
		
		return wi;
		
	}
	
	public static Image diminuirImagem(Image imagem) {
		
		double w = imagem.getWidth();
		double h = imagem.getHeight();
		
		WritableImage wi = new WritableImage((int)(w/2), (int)(h/2));
		PixelReader pr = imagem.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		for (int i = 0; i < w; i+=2) {
			for (int j = 0; j < h; j++) {
				Color oldColor = pr.getColor(i, j);
				pw.setColor(i/2, j/2, oldColor);
			}
		}
		
		return wi;
		
	}
	
	private static double calcAddSubImage(double cor1, double cor2, double qtDistribuicao, boolean adicao) {
		double newColor1 = ((cor1*qtDistribuicao)/100);
		double newColor2 = ((cor2*(100-qtDistribuicao))/100);

		double corFinal = 0;
		if(adicao) {
			corFinal = (newColor1+newColor2);
		}else {
			corFinal = newColor1-newColor2;
			if(corFinal < 0) {
				corFinal = 1-corFinal;
			}
		}
		
		
		
		return corFinal;

	}
	
	public static Image addSubImage(Image imagem1, Image imagem2, double qtDistribuicao, boolean adicao) {
		
		int w1 = (int)imagem1.getWidth();
		int h1 = (int)imagem1.getHeight();
		PixelReader pr1 = imagem1.getPixelReader();
		
		int w2 = (int)imagem2.getWidth();
		int h2 = (int)imagem2.getHeight();
		PixelReader pr2 = imagem2.getPixelReader();
		
		int w = (w1>w2)?w1:w2;
		int h = (h1>h2)?h1:h2;
		
		WritableImage wi = new WritableImage(w, h);
		PixelWriter pw = wi.getPixelWriter();
		
		Color colorDefault = new Color(1,0,0,1);
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				
				Color colorImage1 = null;
				Color colorImage2 = null;
				
				if(i<w1 && j<h1) {
					colorImage1 = pr1.getColor(i, j);
				}
				
				if(i<w2 && j<h2) {
					colorImage2 = pr2.getColor(i, j);
				}
				
				Color newColor;
				
				if(colorImage1 != null && colorImage2 != null) {
					double red		= calcAddSubImage(colorImage1.getRed(), colorImage2.getRed(), qtDistribuicao, adicao);
					double green	= calcAddSubImage(colorImage1.getGreen(), colorImage2.getGreen(), qtDistribuicao, adicao);
					double blue		= calcAddSubImage(colorImage1.getBlue(), colorImage2.getBlue(), qtDistribuicao, adicao);
					double opacity	= calcAddSubImage(colorImage1.getOpacity(), colorImage1.getOpacity(), qtDistribuicao, adicao);
					
					newColor = new Color(red, green, blue, opacity);
				}else if(colorImage1 != null) {
					newColor = colorImage1;
				}else if(colorImage2 != null) {
					newColor = colorImage2;
				}else {
					newColor = colorDefault;
				}
				
				
				pw.setColor(i, j, newColor);
				
				
			}
		}
		
		return wi;
		
	}
	
	public static Image destacarSelecao(Image imagem, double[] clickOnMouse, double[] clickStopMouse) {
		
		int w = (int)imagem.getWidth();
		int h = (int)imagem.getHeight();
		
		WritableImage wi = new WritableImage(w, h);
		PixelReader pr = imagem.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		Color colorDefault = new Color(0,0,0,1);
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				boolean pintar = false;
				Color newColor = null;
				
				//fazer tudo
//				if (clickOnMouse[1] < y && clickStopMouse[1] > y //linha horizontal
//					&& clickOnMouse[0] < x && clickStopMouse[0] > x //linha horizontal	
//						) {
//					pintar = true;
//				}
				if (((clickOnMouse[1] == y || clickStopMouse[1] == y) && clickOnMouse[0] < x && clickStopMouse[0] > x) //linha horizontal
					|| ((clickOnMouse[0] == x || clickStopMouse[0] == x) && clickOnMouse[1] < y && clickStopMouse[1] > y) //linha vertical	
						) {
					pintar = true;
				}
				
				if(pintar) {
					newColor = colorDefault;
				}else {
					newColor = pr.getColor(x, y);
				}
				
				pw.setColor(x, y, newColor);
				
			}
		}
		
		
		return wi;
		
	}
	
	public static Image colocarGrade(Image imagem, int distancia, int idCor) {
		
		int w = (int)imagem.getWidth();
		int h = (int)imagem.getHeight();
		
		WritableImage wi = new WritableImage(w, h);
		PixelReader pr = imagem.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		Color colorSelect = null;
		
		if (idCor == 1) {
			colorSelect = new Color(0, 0, 0, 0.8);
		}else if (idCor == 2) {
			colorSelect = new Color(0, 0, 1, 0.8);
		}else if (idCor == 3) {
			colorSelect = new Color(0, 1, 0, 0.8);
		}
		
		
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				
				Color newColor = null;
				
				if(i%distancia == 0) {
					newColor = colorSelect;
				}else {
					newColor = pr.getColor(i, j);
				}
				
				pw.setColor(i, j, newColor);
			}
		}
		
		
		return wi;
		
	}
	
	public static Image dividirHorizontalmente(Image imagem) {
		
		int w = (int)imagem.getWidth();
		int h = (int)imagem.getHeight();
		
		WritableImage wi = new WritableImage(w, h);
		PixelReader pr = imagem.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		
		int meioH = h/2;
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				
				int newJ = 0;
				if(j < meioH) {
					newJ = j;
				}else {
					newJ = (h-j-1)+meioH;
				}
				
				pw.setColor(i, newJ, pr.getColor(i, j));
				
			}
		}
		
		return wi;
		
	}
	
	
	public static String detectarCores(Image imagem, double[] clickOnMouse, double[] clickStopMouse) {
		
		int w = (int)imagem.getWidth();
		int h = (int)imagem.getHeight();
		
		PixelReader pr = imagem.getPixelReader();
		
		boolean detectRed  = false;
		boolean detectBlue  = false;
		boolean detectGreen  = false;
		
		String resposta = "As cores detectadas forem: ";
		
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				
				if (clickOnMouse[1] < y && clickStopMouse[1] > y //linha horizontal
						&& clickOnMouse[0] < x && clickStopMouse[0] > x //linha horizontal	
						) {
					Color colorImg = pr.getColor(x, y);
					if(!detectRed && colorImg.getRed() == 1 && colorImg.getGreen() == 0 && colorImg.getBlue() == 0) {
						detectRed = true;
						resposta+= "\n - Vermelha";
					}else if(!detectBlue && colorImg.getRed() == 0 && colorImg.getGreen() == 0 && colorImg.getBlue() == 1) {
						detectBlue = true;
						resposta+= "\n - Azul";
					}else if(!detectGreen && colorImg.getRed() == 0 && colorImg.getGreen() == 1 && colorImg.getBlue() == 0) {
						detectGreen = true;
						resposta+= "\n - Verde";
					}
				}
			}
		}
		
		return resposta;
		
	}

}
