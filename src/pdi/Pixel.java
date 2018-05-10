package pdi;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public class Pixel {
	
	private double red;
	private double green;
	private double blue;
	private double opacity;
	
	private int posicaoX;
	private int posicaoY;
	
	private Pixel[] vizinhosCruz = new Pixel[4];
	private Pixel[] vizinhosX = new Pixel[4];
	private Pixel[] vizinhos3x3 = new Pixel[8];
	
	public Pixel(Color cor, int x, int y) {
		red = cor.getRed()*255;
		green = cor.getGreen()*255;
		blue = cor.getBlue()*255;
		opacity = cor.getOpacity();
		posicaoX = x;
		posicaoY = y;
	}
	
	private double[] getMedia(Pixel[] vizinhos) {
		
		double[] cores = new double[3];
		
		int quantVizinhos = 1;
		
		for(int i = 0; i < vizinhos.length; i++) {
			if (vizinhos[i] != null) {
				quantVizinhos++;
				cores[0]+= vizinhos[i].getRed();
				cores[1]+= vizinhos[i].getGreen();
				cores[2]+= vizinhos[i].getBlue();
			}
		}
		
		cores[0] = (red+cores[0])/quantVizinhos;
		cores[1] = (green+cores[1])/quantVizinhos;
		cores[2] = (blue+cores[2])/quantVizinhos;
		
		return cores;
		
	}
	
	private double[] bubbleSort(double[] arr) {  
        int n = arr.length;  
        double temp = 0;  
         for(int i=0; i < n; i++){  
             for(int j=1; j < (n-i); j++){  
	              if(arr[j-1] > arr[j]){  
                     //swap elements  
                     temp = arr[j-1];  
                     arr[j-1] = arr[j];  
                     arr[j] = temp;  
	             }
             }
         }
         
         return arr;
  
    } 
	
	
	private double[] generateMediana(double[][] cores) {
		
		double[] medianas = new double[3];
		
		for(int i = 0; i < 3; i++) {
			
			double[] tonalidade = cores[i];
			double[] tonalidadeMediana = bubbleSort(tonalidade);
			
			if (tonalidadeMediana.length%2 == 0) {
				//par
				int pos1 =  tonalidadeMediana.length,
						pos2 =  tonalidadeMediana.length+1;
				medianas[i] = (tonalidade[pos1]+tonalidade[pos2])/2;
			}else {
				//impar
				medianas[i] = tonalidadeMediana[tonalidadeMediana.length+1-((tonalidadeMediana.length-1)/2)];
			}
		}
		
		return medianas;
	}
	
	private double[] getMediana(Pixel[] vizinhos) {
		
		double[][] cores = new double[3][vizinhos.length+1];
		
		cores[0][vizinhos.length] = red;
		cores[0][vizinhos.length] = green;
		cores[0][vizinhos.length] = blue;
		
		for(int i = 0; i < vizinhos.length; i++) {
			if (vizinhos[i] != null) {
				cores[0][i] = vizinhos[i].getRed();
				cores[1][i] = vizinhos[i].getGreen();
				cores[2][i] = vizinhos[i].getBlue();
			}
		}
		
		return generateMediana(cores);
		
	}
	
	private int length(Pixel[] pixels) {
		
		int count = 0;
		
		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i] != null) {
				count++;
			}
		}
		
		return count;
		
		
	}
	
	
	
	public void addVizinhosCruz(Pixel pixel) {
		vizinhosCruz[length(vizinhosCruz)] = pixel;
	}
	
	public void addVizinhosX(Pixel pixel) {
		vizinhosX[length(vizinhosX)] = pixel;
	}
	public void addVizinhos3x3(Pixel pixel) {
		vizinhos3x3[length(vizinhos3x3)] = pixel;
	}

	public double getRed() {
		return red;
	}

	public void setRed(double red) {
		this.red = red;
	}

	public double getGreen() {
		return green;
	}

	public void setGreen(double green) {
		this.green = green;
	}

	public double getBlue() {
		return blue;
	}

	public void setBlue(double blue) {
		this.blue = blue;
	}

	public double getOpacity() {
		return opacity;
	}

	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}
	
	private Color factoryColor(double[] cores) {
		
		return new Color(cores[0]/255, cores[1]/255, cores[2]/255, this.opacity);
		
	}
	
	public Color generateCorMediaCruz() {
		
		return factoryColor(getMedia(vizinhosCruz));
		
	}
	
	public Color generateCorMedianaCruz() {
		
		return factoryColor(getMediana(vizinhosCruz));
		
	}
	
	public Color generateCorMediaX() {
		
		return factoryColor(getMedia(vizinhosX));
		
	}
	
	public Color generateCorMedianaX() {
		
		return factoryColor(getMediana(vizinhosX));
		
	}
	
	
	public Color generateCorMedia3x3() {
		
		return factoryColor(getMedia(vizinhos3x3));
		
	}
	
	public Color generateCorMediana3x3() {
		
		return factoryColor(getMediana(vizinhos3x3));
		
	}
	

	
	

}
