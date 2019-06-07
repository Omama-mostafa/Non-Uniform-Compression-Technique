package NonUniform;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NonUniform
{
	static class Node
	{
		ArrayList<Integer> Main_arr =  new ArrayList<>();
		float Ave = 0;
		Node Left;
		Node Right;
		Node()
		{
			Left = null;
			Right = null;
		}
	}
	static class Table
	{
		int FirstValue;
		int LastValue;
		int Q;
		Table()
		{
			this.FirstValue = 0;
			this.LastValue = 0;
			this.Q = 0;
		}
	}
	static ArrayList<Integer> Average = new ArrayList<>();
	
	public static void main(String[] args) throws IOException
	{
		File file1 = new File("Pic.JPG");
		BufferedImage image = ImageIO.read(file1);
		ConvertToPexil(image, image.getHeight(), image.getWidth());
		ArrayList<Integer> arr1 = new ArrayList<>();
		arr1.add(6);
		arr1.add(15);
		arr1.add(17);
		arr1.add(60);
		arr1.add(100);
		arr1.add(90);
		arr1.add(66);
		arr1.add(59);
		arr1.add(18);
		arr1.add(3);
		arr1.add(5);
		arr1.add(16);
		arr1.add(14);
		arr1.add(67);
		arr1.add(63);
		arr1.add(2);
		arr1.add(98);
		arr1.add(92);
		Node N = new Node();
		N.Main_arr = arr1;
		N.Ave = Get_Average(arr1);
		Split(2 , N);
		Create_Average(N);
		Create_Table();
	}
	
	public static ArrayList<Integer> ConvertToPexil(BufferedImage bufImage, int ImgHeight, int ImgWidth)
	{
		int[][] vec_matrix = new int[ImgHeight][ImgWidth];
		ArrayList<Integer> Img = new ArrayList<>();
		for(int h = 0; h < ImgHeight; h++)
		{
			for(int w = 0; w < ImgWidth; w++)
			{
				int p = bufImage.getRGB(w, h);
				//int a = (p >> 24) & 0xff;
				int r = (p >> 16) & 0xff;
				int g = (p >> 8) & 0xff;
				int b = p & 0xff;
				
				int ColAvgVal = (r + g + b) / 3;
				vec_matrix[h][w] = ColAvgVal;
			}
		}
		for(int i = 0; i < ImgHeight; i++)
		{
			for(int j = 0; j < ImgWidth; j++)
			{
				Img.add(vec_matrix[i][j]);
			}
		}
		return Img;
	}
	
	public static float Get_Average(ArrayList<Integer> arr)
	{
		int counter = 0;
		float Ave = 0;
		for(int i = 0; i < arr.size(); i++)
		{
			counter += arr.get(i);
		}
		Ave = (float) counter / arr.size();
		return Ave;
	}
	
	public static void Split(int Level_num , Node root)
	{
		if(Level_num == 0)
			return;
		else
		{
			int Left_Ave = (int)root.Ave -1;
			int Right_Ave = (int) root.Ave + 1;
			root.Left = new Node();
			root.Right = new Node();
			for(int i=0; i< root.Main_arr.size(); i++)
			{
				if(Math.abs(root.Main_arr.get(i) - Left_Ave) <= Math.abs(root.Main_arr.get(i) - Right_Ave))
				{
					root.Left.Main_arr.add(root.Main_arr.get(i));
				}
				else
					root.Right.Main_arr.add(root.Main_arr.get(i));
			}
			if(root.Left.Main_arr.size() == 0)
			{
				root.Left.Ave = root.Ave;
			}
			else
			{
				int AveLeft = 0;
				for(int i=0; i<root.Left.Main_arr.size(); i++)
				{
					AveLeft += root.Left.Main_arr.get(i);
				}
				AveLeft /= (float) root.Left.Main_arr.size();
				root.Left.Ave = AveLeft;
			}
			if(root.Right.Main_arr.size() == 0)
			{
				root.Right.Ave = root.Ave;
			}
			else
			{
				int AveRight =0;
				for(int i=0; i<root.Right.Main_arr.size(); i++)
				{
					AveRight  += root.Right.Main_arr.get(i);
				}
				AveRight /= (float) root.Right.Main_arr.size();
				root.Right.Ave = AveRight;
			}
			Level_num--;
			Split(Level_num , root.Left);
			Split(Level_num , root.Right);
		}
	}
	
	public static void Create_Average(Node root)
	{
		if((root.Left.Left == null && root.Left.Right == null) || root.Right.Left == null && root.Right.Right == null)
		{
			Average.add((int)root.Left.Ave);
			Average.add(((int)root.Right.Ave));
			return;
		}
		Create_Average(root.Left);
		Create_Average(root.Right);
	}
	
	public static void Create_Table()
	{
		ArrayList<Table> QuantizerTable = new ArrayList<>();
		for(int i=1; i<Average.size(); i++)
		{
			Table Obj = new Table();
			if(i-1 == 0)
				Obj.FirstValue = 0;
			else
				Obj.FirstValue = (Average.get(i) + Average.get(i+1)) / 2;
			if(i == Average.size() - 1)
				Obj.LastValue = 255;
			else
				Obj.LastValue = ((Average.get(i) + Average.get(i+1)) / 2)-1;
			Obj.Q = Average.get(i);
			QuantizerTable.add(Obj);
		}
		for(int i=0; i<QuantizerTable.size(); i++)
		{
			System.out.println(QuantizerTable.get(i).FirstValue + "\t\t" + QuantizerTable.get(i).LastValue+ "\t\t" + QuantizerTable.get(i).Q);
		}
	}
}